package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import io.github.thebusybiscuit.slimefun4.api.network.NetworkComponent;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

public class CargoNet extends ChestTerminalNetwork {

    private static final int RANGE = 5;
    private static final int TICK_DELAY = SlimefunPlugin.getCfg().getInt("networks.cargo-ticker-delay");

    private final Set<Location> inputNodes = new HashSet<>();
    private final Set<Location> outputNodes = new HashSet<>();

    private final Map<Location, Integer> roundRobin = new HashMap<>();
    private int tickDelayThreshold = 0;

    public static CargoNet getNetworkFromLocation(Location l) {
        return SlimefunPlugin.getNetworkManager().getNetworkFromLocation(l, CargoNet.class).orElse(null);
    }

    public static CargoNet getNetworkFromLocationOrCreate(Location l) {
        Optional<CargoNet> cargoNetwork = SlimefunPlugin.getNetworkManager().getNetworkFromLocation(l, CargoNet.class);

        if (cargoNetwork.isPresent()) {
            return cargoNetwork.get();
        } else {
            CargoNet network = new CargoNet(l);
            SlimefunPlugin.getNetworkManager().registerNetwork(network);
            return network;
        }
    }

    protected CargoNet(Location l) {
        super(l);
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    public NetworkComponent classifyLocation(Location l) {
        String id = BlockStorage.checkID(l);
        if (id == null) return null;

        switch (id) {
            case "CARGO_MANAGER":
                return NetworkComponent.REGULATOR;
            case "CARGO_NODE":
                return NetworkComponent.CONNECTOR;
            case "CARGO_NODE_INPUT":
            case "CARGO_NODE_OUTPUT":
            case "CARGO_NODE_OUTPUT_ADVANCED":
            case "CT_IMPORT_BUS":
            case "CT_EXPORT_BUS":
            case "CHEST_TERMINAL":
                return NetworkComponent.TERMINUS;
            default:
                return null;
        }
    }

    @Override
    public void onClassificationChange(Location l, NetworkComponent from, NetworkComponent to) {
        if (from == NetworkComponent.TERMINUS) {
            inputNodes.remove(l);
            outputNodes.remove(l);
            terminals.remove(l);
            imports.remove(l);
            exports.remove(l);
        }

        if (to == NetworkComponent.TERMINUS) {
            switch (BlockStorage.checkID(l)) {
                case "CARGO_NODE_INPUT":
                    inputNodes.add(l);
                    break;
                case "CARGO_NODE_OUTPUT":
                case "CARGO_NODE_OUTPUT_ADVANCED":
                    outputNodes.add(l);
                    break;
                case "CHEST_TERMINAL":
                    terminals.add(l);
                    break;
                case "CT_IMPORT_BUS":
                    imports.add(l);
                    break;
                case "CT_EXPORT_BUS":
                    exports.add(l);
                    break;
                default:
                    break;
            }
        }
    }

    public void tick(Block b) {
        if (!regulator.equals(b.getLocation())) {
            SimpleHologram.update(b, "&4已连接到多个货运网络管理器");
            return;
        }

        super.tick();

        if (connectorNodes.isEmpty() && terminusNodes.isEmpty()) {
            SimpleHologram.update(b, "&c找不到货运网络节点");
        } else {
            SimpleHologram.update(b, "&7状态: &a&l在线");
            Map<Integer, List<Location>> output = mapOutputNodes();

            // Chest Terminal Stuff
            Set<Location> destinations = new HashSet<>();

            List<Location> output16 = output.get(16);
            if (output16 != null) destinations.addAll(output16);

            Slimefun.runSync(() -> run(b, destinations, output));
        }
    }

    private Map<Integer, List<Location>> mapOutputNodes() {
        Map<Integer, List<Location>> output = new HashMap<>();

        List<Location> list = new LinkedList<>();
        int lastFrequency = -1;

        for (Location outputNode : outputNodes) {
            int frequency = getFrequency(outputNode);

            if (frequency != lastFrequency && lastFrequency != -1) {
                output.merge(lastFrequency, list, (prev, next) -> {
                    prev.addAll(next);
                    return prev;
                });

                list = new LinkedList<>();
            }

            list.add(outputNode);
            lastFrequency = frequency;
        }

        if (!list.isEmpty()) {
            output.merge(lastFrequency, list, (prev, next) -> {
                prev.addAll(next);
                return prev;
            });
        }

        return output;
    }

    private void run(Block b, Set<Location> destinations, Map<Integer, List<Location>> output) {
        if (BlockStorage.getLocationInfo(b.getLocation(), "visualizer") == null) {
            display();
        }

        // Skip ticking if the threshold is not reached. The delay is not same as minecraft tick,
        // but it's based on 'custom-ticker-delay' config.
        if (tickDelayThreshold < TICK_DELAY) {
            tickDelayThreshold++;
            return;
        }

        // Reset the internal threshold, so we can start skipping again
        tickDelayThreshold = 0;

        Map<Location, Integer> inputs = new HashMap<>();
        Set<Location> providers = new HashSet<>();

        for (Location node : inputNodes) {
            int frequency = getFrequency(node);

            if (frequency == 16) {
                providers.add(node);
            } else if (frequency >= 0 && frequency < 16) {
                inputs.put(node, frequency);
            }
        }

        // Chest Terminal Code
        if (SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled()) {
            handleItemRequests(providers, destinations);
        }

        // All operations happen here: Everything gets iterated from the Input Nodes.
        // (Apart from ChestTerminal Buses)
        for (Map.Entry<Location, Integer> entry : inputs.entrySet()) {
            Location input = entry.getKey();
            Optional<Block> attachedBlock = getAttachedBlock(input.getBlock());

            if (attachedBlock.isPresent()) {
                routeItems(input, attachedBlock.get(), entry.getValue(), output);
            }
        }

        // Chest Terminal Code
        if (SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled()) {
            updateTerminals(providers);
        }
    }

    private void routeItems(Location inputNode, Block inputTarget, int frequency, Map<Integer, List<Location>> outputNodes) {
        ItemStackAndInteger slot = CargoUtils.withdraw(inputNode.getBlock(), inputTarget);
        if (slot == null) {
            return;
        }

        ItemStack stack = slot.getItem();
        int previousSlot = slot.getInt();
        List<Location> outputs = outputNodes.get(frequency);

        if (outputs != null) {
            stack = distributeItem(stack, inputNode, outputs);
        }

        DirtyChestMenu menu = CargoUtils.getChestMenu(inputTarget);

        if (menu != null) {
            menu.replaceExistingItem(previousSlot, stack);
        } else if (CargoUtils.hasInventory(inputTarget)) {
            BlockState state = inputTarget.getState();

            if (state instanceof InventoryHolder) {
                Inventory inv = ((InventoryHolder) state).getInventory();
                inv.setItem(previousSlot, stack);
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
            Optional<Block> target = getAttachedBlock(output.getBlock());

            if (target.isPresent()) {
                item = CargoUtils.insert(output.getBlock(), target.get(), item);

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
     * @param inputNode   The {@link Location} of the input node
     * @param outputNodes A {@link Deque} of {@link Location Locations} of the output nodes
     */
    private void roundRobinSort(Location inputNode, Deque<Location> outputNodes) {
        int index = roundRobin.getOrDefault(inputNode, 0);

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

        roundRobin.put(inputNode, index);
    }

    /**
     * This method returns the frequency a given node is set to.
     * Should there be an {@link Exception} to this method it will fall back to zero in
     * order to protect the integrity of the {@link CargoNet}.
     *
     * @param node The {@link Location} of our cargo node
     * @return The frequency of the given node
     */
    private static int getFrequency(Location node) {
        try {
            String str = BlockStorage.getLocationInfo(node).getString("frequency");
            return Integer.parseInt(str);
        } catch (Exception x) {
            Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Error occurred while parsing a Cargo Node Frequency (" + node.getWorld().getName() + " - " + node.getBlockX() + "," + node.getBlockY() + "," + +node.getBlockZ() + ")");
            return 0;
        }
    }
}