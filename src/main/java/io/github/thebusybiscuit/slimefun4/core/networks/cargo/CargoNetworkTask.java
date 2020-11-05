package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * The {@link CargoNetworkTask} is the actual {@link Runnable} responsible for moving {@link ItemStack ItemStacks}
 * around the {@link CargoNet}.
 * 
 * Inbefore this was just a method in the {@link CargoNet} class.
 * However for aesthetic reasons but mainly to prevent the Cargo Task from showing up as
 * "lambda:xyz-123" in timing reports... this was moved.
 * 
 * @see CargoNet
 * @see CargoUtils
 * @see AbstractItemNetwork
 *
 */
class CargoNetworkTask implements Runnable {

    private final CargoNet network;
    private final Map<Location, Inventory> inventories = new HashMap<>();

    private final Map<Location, Integer> inputs;
    private final Map<Integer, List<Location>> outputs;

    private final Set<Location> chestTerminalInputs;
    private final Set<Location> chestTerminalOutputs;

    @ParametersAreNonnullByDefault
    CargoNetworkTask(CargoNet network, Map<Location, Integer> inputs, Map<Integer, List<Location>> outputs, Set<Location> chestTerminalInputs, Set<Location> chestTerminalOutputs) {
        this.network = network;

        this.inputs = inputs;
        this.outputs = outputs;
        this.chestTerminalInputs = chestTerminalInputs;
        this.chestTerminalOutputs = chestTerminalOutputs;
    }

    @Override
    public void run() {
        long timestamp = System.nanoTime();

        // Chest Terminal Code
        if (SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled()) {
            network.handleItemRequests(inventories, chestTerminalInputs, chestTerminalOutputs);
        }

        // All operations happen here: Everything gets iterated from the Input Nodes.
        // (Apart from ChestTerminal Buses)
        SlimefunItem inputNode = SlimefunItems.CARGO_INPUT_NODE.getItem();
        for (Map.Entry<Location, Integer> entry : inputs.entrySet()) {
            long nodeTimestamp = System.nanoTime();
            Location input = entry.getKey();
            Optional<Block> attachedBlock = network.getAttachedBlock(input);

            attachedBlock.ifPresent(block -> routeItems(input, block, entry.getValue(), outputs));

            // This will prevent this timings from showing up for the Cargo Manager
            timestamp += SlimefunPlugin.getProfiler().closeEntry(entry.getKey(), inputNode, nodeTimestamp);
        }

        // Chest Terminal Code
        if (SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled()) {
            // This will deduct any CT timings and attribute them towards the actual terminal
            timestamp += network.updateTerminals(chestTerminalInputs);
        }

        // Submit a timings report
        SlimefunPlugin.getProfiler().closeEntry(network.getRegulator(), SlimefunItems.CARGO_MANAGER.getItem(), timestamp);
    }

    private void routeItems(Location inputNode, Block inputTarget, int frequency, Map<Integer, List<Location>> outputNodes) {
        // List all output nodes with same frequency
        List<Location> destinations = outputNodes.get(frequency);

        if (destinations == null) {
            return;
        }

        // Get the avalible input target slot. The item stack of the slot is cheched which
        // could inserted to some destinations.
        ItemStackAndInteger slot = getInputTargetSlot(network, inputNode.getBlock(), inputTarget, destinations);
        
        if (slot == null) {
            return;
        }

        // Withdraw ths slot of the input target. Not the first legal item stack.
        slot = CargoUtils.withdraw(network, inventories, inputNode.getBlock(), inputTarget, slot);

        if (slot == null) {
            return;
        }

        // The item stack of the slot.
        ItemStack stack = slot.getItem();
        // The position of the slot.
        int previousSlot = slot.getInt();

        if (destinations != null) {
            // Distribute item stack to destinations. And return the remaining stack of items.
            // If there is no remaining, it is null.
            stack = distributeItem(stack, inputNode, destinations);
        }

        // Distributed but some items left
        if (stack != null) {
            Inventory inv = inventories.get(inputTarget.getLocation());

            if (inv != null) {
                // Check if the original slot hasn't been occupied in the meantime
                if (inv.getItem(previousSlot) == null) {
                    inv.setItem(previousSlot, stack);
                } else {
                    // Try to add the item into another available slot then
                    ItemStack rest = inv.addItem(stack).get(0);

                    if (rest != null) {
                        // If the item still couldn't be inserted, simply drop it on the ground
                        inputTarget.getWorld().dropItem(inputTarget.getLocation().add(0, 1, 0), rest);
                    }
                }
            } else {
                DirtyChestMenu menu = CargoUtils.getChestMenu(inputTarget);

                if (menu != null) {
                    if (menu.getItemInSlot(previousSlot) == null) {
                        menu.replaceExistingItem(previousSlot, stack);
                    } else {
                        inputTarget.getWorld().dropItem(inputTarget.getLocation().add(0, 1, 0), stack);
                    }
                }
            }
        }
    }

    private Inventory getInventoryByBlock(Block target) {
        DirtyChestMenu menu = CargoUtils.getChestMenu(target);

        if (CargoUtils.hasInventory(target)) {
            Inventory inventory = inventories.get(target.getLocation());

            if (inventory != null) {
                return inventory;
            }

            BlockState state = PaperLib.getBlockState(target, false).getState();

            if (state instanceof InventoryHolder) {
                inventory = ((InventoryHolder) state).getInventory();
                inventories.put(target.getLocation(), inventory);
                return inventory;
            }
        }

        return null;
    }

    private ItemStackAndInteger getAvalibleSlot(ItemStack stack, List<Location> destinations) {
        // Check destinations has avalible slot
        for (Location output : destinations) {
            if (!CargoUtils.matchesFilter(network, output.getBlock(), stack)) {
                continue;
            }

            Optional<Block> target = network.getAttachedBlock(output);

            if (target.isPresent()) {
                Inventory outputInventory = getInventoryByBlock(target.get());
                int[] range = CargoUtils.getOutputSlotRange(outputInventory);
                int minSlot = range[0];
                int maxSlot = range[1];

                ItemStackWrapper wrapper = new ItemStackWrapper(stack);
        
                for (int slot = minSlot; slot < maxSlot; slot++) {
                    ItemStack[] contents = outputInventory.getContents();
                    // Changes to this ItemStack are synchronized with the Item in the Inventory
                    ItemStack itemInSlot = contents[slot];
        
                    if (itemInSlot == null) {
                        return new ItemStackAndInteger(stack, slot);
                    } else {
                        int maxStackSize = itemInSlot.getType().getMaxStackSize();
        
                        if (SlimefunUtils.isItemSimilar(itemInSlot, wrapper, true, false) && itemInSlot.getAmount() < maxStackSize) {
                            int amount = itemInSlot.getAmount() + stack.getAmount();
        
                            if (amount > maxStackSize) {
                                return new ItemStackAndInteger(stack, slot);
                            }
                        }
                    }
                }
            }
        }

        // Has no avalible slot
        return null;
    }
    
    private ItemStackAndInteger getInputTargetSlot(AbstractItemNetwork network, Block inputNode, Block inputTarget, List<Location> outputNodes) {
        Inventory inputInventory = getInventoryByBlock(inputTarget);

        if (inputInventory == null) {
            return null;
        }
        
        ItemStack[] contents = inputInventory.getContents();

        for (int slot=0; slot < contents.length; slot++) {
            ItemStack stack = contents[slot];
            if (stack == null) {
                continue;
            }

            if (CargoUtils.matchesFilter(network, inputNode, stack)) {
                ItemStackAndInteger avalibleSlot = getAvalibleSlot(stack, outputNodes);
                if (avalibleSlot != null) {
                    return new ItemStackAndInteger(stack, slot);
                }
            }
        }

        // Has no avalible slot
        return null;
    }

    private ItemStack distributeItem(ItemStack stack, Location inputNode, List<Location> outputNodes) {
        ItemStack item = stack;

        Deque<Location> destinations = new LinkedList<>(outputNodes);
        Config cfg = BlockStorage.getLocationInfo(inputNode);
        boolean roundrobin = "true".equals(cfg.getString("round-robin"));

        if (roundrobin) {
            roundRobinSort(inputNode, destinations);
        }

        for (Location output : destinations) {
            Optional<Block> target = network.getAttachedBlock(output);

            if (target.isPresent()) {
                item = CargoUtils.insert(network, inventories, output.getBlock(), target.get(), item);

                if (item == null) {
                    break;
                }
            }
        }

        return item;
    }

    /**
     * This method sorts a given {@link Deque} of output node locations using a semi-accurate
     * round-robin method.
     * 
     * @param inputNode
     *            The {@link Location} of the input node
     * @param outputNodes
     *            A {@link Deque} of {@link Location Locations} of the output nodes
     */
    private void roundRobinSort(Location inputNode, Deque<Location> outputNodes) {
        int index = network.roundRobin.getOrDefault(inputNode, 0);

        if (index < outputNodes.size()) {
            // Not ideal but actually not bad performance-wise over more elegant alternatives
            for (int i = 0; i < index; i++) {
                Location temp = outputNodes.removeFirst();
                outputNodes.add(temp);
            }

            index++;
        } else {
            index = 1;
        }

        network.roundRobin.put(inputNode, index);
    }

}
