package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

import io.github.thebusybiscuit.slimefun4.utils.BlockUtils;
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

    private final Set<Location> inputNodes = new HashSet<>();
    private final Set<Location> outputNodes = new HashSet<>();

    private final Map<Location, Integer> roundRobin = new HashMap<>();

    public static CargoNet getNetworkFromLocation(Location l) {
        return SlimefunPlugin.getNetworkManager().getNetworkFromLocation(l, CargoNet.class);
    }

    public static CargoNet getNetworkFromLocationOrCreate(Location l) {
        CargoNet cargoNetwork = getNetworkFromLocation(l);

        if (cargoNetwork == null) {
            cargoNetwork = new CargoNet(l);
            SlimefunPlugin.getNetworkManager().registerNetwork(cargoNetwork);
        }

        return cargoNetwork;
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

        Set<Location> inputs = new HashSet<>();
        Set<Location> providers = new HashSet<>();

        for (Location node : inputNodes) {
            int frequency = getFrequency(node);

            if (frequency == 16) {
                providers.add(node);
            }
            else if (frequency >= 0 && frequency < 16) {
                inputs.add(node);
            }
        }

        // Chest Terminal Code
        if (SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled()) {
            handleItemRequests(providers, destinations);
        }

        // All operations happen here: Everything gets iterated from the Input Nodes.
        // (Apart from ChestTerminal Buses)
        for (Location input : inputs) {
            Optional<Block> attachedBlock = getAttachedBlock(input.getBlock());
            if (!attachedBlock.isPresent()) {
                continue;
            }

            Block inputTarget = attachedBlock.get();
            Config cfg = BlockStorage.getLocationInfo(input);
            boolean roundrobin = "true".equals(cfg.getString("round-robin"));
            ItemStackAndInteger slot = CargoUtils.withdraw(input.getBlock(), inputTarget, Integer.parseInt(cfg.getString("index")));
            if (slot == null) {
                continue;
            }

            ItemStack stack = slot.getItem();
            int previousSlot = slot.getInt();

            List<Location> outputs = output.get(getFrequency(input));

            if (outputs != null) {
                List<Location> outputlist = new ArrayList<>(outputs);

                if (roundrobin) {
                    int index = roundRobin.getOrDefault(input, 0);

                    if (index < outputlist.size()) {
                        for (int i = 0; i < index; i++) {
                            Location temp = outputlist.get(0);
                            outputlist.remove(temp);
                            outputlist.add(temp);
                        }

                        index++;
                    }
                    else {
                        index = 1;
                    }

                    roundRobin.put(input, index);
                }

                for (Location out : outputlist) {
                    Optional<Block> target = getAttachedBlock(out.getBlock());

                    if (target.isPresent()) {
                        stack = CargoUtils.insert(out.getBlock(), target.get(), stack, -1);
                        if (stack == null) break;
                    }
                }
            }

            if (stack != null && previousSlot > -1) {
                DirtyChestMenu menu = CargoUtils.getChestMenu(inputTarget);

                if (menu != null) {
                    menu.replaceExistingItem(previousSlot, stack);
                }
                else if (BlockUtils.hasInventory(inputTarget)) {
                    BlockState state = inputTarget.getState();

                    if (state instanceof InventoryHolder) {
                        Inventory inv = ((InventoryHolder) state).getInventory();
                        inv.setItem(previousSlot, stack);
                    }
                }
            }


        }

        // Chest Terminal Code
        if (SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled()) {
            updateTerminals(providers);
        }
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
