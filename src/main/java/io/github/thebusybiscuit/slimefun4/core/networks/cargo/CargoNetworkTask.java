package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;

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
 * @see ChestTerminalNetwork
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
        ItemStackAndInteger slot = CargoUtils.withdraw(inventories, inputNode.getBlock(), inputTarget);

        if (slot == null) {
            return;
        }

        ItemStack stack = slot.getItem();
        int previousSlot = slot.getInt();
        List<Location> destinations = outputNodes.get(frequency);

        if (destinations != null) {
            stack = distributeItem(stack, inputNode, destinations);
        }

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
                item = CargoUtils.insert(inventories, output.getBlock(), target.get(), item);

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
