package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.network.NetworkComponent;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;

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
        }
        else {
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
            SimpleHologram.update(b, "&4Multiple Cargo Regulators connected");
            return;
        }

        super.tick();

        if (connectorNodes.isEmpty() && terminusNodes.isEmpty()) {
            SimpleHologram.update(b, "&cNo Cargo Nodes found");
        }
        else {
            SimpleHologram.update(b, "&7Status: &a&lONLINE");
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
            }
            else if (frequency >= 0 && frequency < 16) {
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
        Config cfg = BlockStorage.getLocationInfo(inputNode);
        boolean roundrobin = "true".equals(cfg.getString("round-robin"));

        ItemStackAndInteger slot = CargoUtils.withdraw(inputNode.getBlock(), inputTarget, Integer.parseInt(cfg.getString("index")));
        if (slot == null) {
            return;
        }

        ItemStack stack = slot.getItem();
        int previousSlot = slot.getInt();
        List<Location> outputs = outputNodes.get(frequency);

        if (outputs != null) {
            List<Location> outputList = new LinkedList<>(outputs);

            if (roundrobin) {
                roundRobinSort(inputNode, outputList);
            }

            for (Location output : outputList) {
                Optional<Block> target = getAttachedBlock(output.getBlock());

                if (target.isPresent()) {
                    stack = CargoUtils.insert(output.getBlock(), target.get(), stack, -1);

                    if (stack == null) {
                        break;
                    }
                }
            }
        }

        DirtyChestMenu menu = CargoUtils.getChestMenu(inputTarget);

        if (menu != null) {
            menu.replaceExistingItem(previousSlot, stack);
        }
        else if (CargoUtils.hasInventory(inputTarget)) {
            BlockState state = inputTarget.getState();

            if (state instanceof InventoryHolder) {
                Inventory inv = ((InventoryHolder) state).getInventory();
                inv.setItem(previousSlot, stack);
            }
        }
    }

    private void roundRobinSort(Location input, List<Location> outputs) {
        int index = roundRobin.getOrDefault(input, 0);

        if (index < outputs.size()) {
            // Not ideal but actually not bad performance-wise over more elegant alternatives
            for (int i = 0; i < index; i++) {
                Location temp = outputs.remove(0);
                outputs.add(temp);
            }

            index++;
        }
        else {
            index = 1;
        }

        roundRobin.put(input, index);
    }

    private static int getFrequency(Location l) {
        try {
            String str = BlockStorage.getLocationInfo(l).getString("frequency");
            return Integer.parseInt(str);
        }
        catch (Exception x) {
            Slimefun.getLogger().log(Level.SEVERE, "An Error occured while parsing a Cargo Node Frequency", x);
            return 0;
        }
    }
}
