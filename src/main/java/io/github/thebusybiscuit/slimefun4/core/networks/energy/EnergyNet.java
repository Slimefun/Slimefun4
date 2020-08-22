package io.github.thebusybiscuit.slimefun4.core.networks.energy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongConsumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.api.ErrorReport;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;

/**
 * The {@link EnergyNet} is an implementation of {@link Network} that deals with
 * electrical energy being send from and to nodes.
 * 
 * @author meiamsome
 * @author TheBusyBiscuit
 * 
 * @see Network
 * @see EnergyNetComponent
 * @see EnergyNetProvider
 * @see EnergyNetComponentType
 *
 */
public class EnergyNet extends Network {

    private static final int RANGE = 6;

    public static EnergyNetComponentType getComponent(Location l) {
        String id = BlockStorage.checkID(l);

        if (id == null) {
            return EnergyNetComponentType.NONE;
        }

        SlimefunItem item = SlimefunItem.getByID(id);

        if (item instanceof EnergyNetComponent) {
            return ((EnergyNetComponent) item).getEnergyComponentType();
        }

        return EnergyNetComponentType.NONE;
    }

    public static EnergyNet getNetworkFromLocationOrCreate(Location l) {
        Optional<EnergyNet> cargoNetwork = SlimefunPlugin.getNetworkManager().getNetworkFromLocation(l, EnergyNet.class);

        if (cargoNetwork.isPresent()) {
            return cargoNetwork.get();
        }
        else {
            EnergyNet network = new EnergyNet(l);
            SlimefunPlugin.getNetworkManager().registerNetwork(network);
            return network;
        }
    }

    private final Set<Location> generators = new HashSet<>();
    private final Set<Location> storage = new HashSet<>();
    private final Set<Location> consumers = new HashSet<>();

    protected EnergyNet(Location l) {
        super(SlimefunPlugin.getNetworkManager(), l);
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    public NetworkComponent classifyLocation(Location l) {
        if (regulator.equals(l)) {
            return NetworkComponent.REGULATOR;
        }

        switch (getComponent(l)) {
        case CAPACITOR:
            return NetworkComponent.CONNECTOR;
        case CONSUMER:
        case GENERATOR:
            return NetworkComponent.TERMINUS;
        default:
            return null;
        }
    }

    @Override
    public void onClassificationChange(Location l, NetworkComponent from, NetworkComponent to) {
        if (from == NetworkComponent.TERMINUS) {
            generators.remove(l);
            consumers.remove(l);
        }

        switch (getComponent(l)) {
        case CAPACITOR:
            storage.add(l);
            break;
        case CONSUMER:
            consumers.add(l);
            break;
        case GENERATOR:
            generators.add(l);
            break;
        default:
            break;
        }
    }

    public void tick(Block b) {
        AtomicLong timestamp = new AtomicLong(SlimefunPlugin.getProfiler().newEntry());

        if (!regulator.equals(b.getLocation())) {
            SimpleHologram.update(b, "&4Multiple Energy Regulators connected");
            return;
        }

        super.tick();

        if (connectorNodes.isEmpty() && terminusNodes.isEmpty()) {
            SimpleHologram.update(b, "&4No Energy Network found");
        }
        else {
            Map<Location, Integer> generatorsWithCapacity = new HashMap<>();
            int supply = tickAllGenerators(generatorsWithCapacity, timestamp::getAndAdd) + tickAllCapacitors();
            int remainingEnergy = supply;
            int demand = 0;

            for (Location machine : consumers) {
                int capacity = ChargableBlock.getMaxCharge(machine);
                int charge = ChargableBlock.getCharge(machine);

                if (charge < capacity) {
                    int availableSpace = capacity - charge;
                    demand += availableSpace;

                    if (remainingEnergy > 0) {
                        if (remainingEnergy > availableSpace) {
                            ChargableBlock.setUnsafeCharge(machine, capacity, false);
                            remainingEnergy -= availableSpace;
                        }
                        else {
                            ChargableBlock.setUnsafeCharge(machine, charge + remainingEnergy, false);
                            remainingEnergy = 0;
                        }
                    }
                }
            }

            storeExcessEnergy(generatorsWithCapacity, remainingEnergy);
            updateHologram(b, supply, demand);
        }

        // We have subtracted the timings from Generators, so they do not show up twice.
        SlimefunPlugin.getProfiler().closeEntry(b.getLocation(), SlimefunItems.ENERGY_REGULATOR.getItem(), timestamp.get());
    }

    private void storeExcessEnergy(Map<Location, Integer> generators, int available) {
        for (Location capacitor : storage) {
            if (available > 0) {
                int capacity = ChargableBlock.getMaxCharge(capacitor);

                if (available > capacity) {
                    ChargableBlock.setUnsafeCharge(capacitor, capacity, true);
                    available -= capacity;
                }
                else {
                    ChargableBlock.setUnsafeCharge(capacitor, available, true);
                    available = 0;
                }
            }
            else {
                ChargableBlock.setUnsafeCharge(capacitor, 0, true);
            }
        }

        for (Map.Entry<Location, Integer> entry : generators.entrySet()) {
            Location generator = entry.getKey();
            int capacity = entry.getValue();

            if (available > 0) {
                if (available > capacity) {
                    ChargableBlock.setUnsafeCharge(generator, capacity, false);
                    available -= capacity;
                }
                else {
                    ChargableBlock.setUnsafeCharge(generator, available, false);
                    available = 0;
                }
            }
            else {
                ChargableBlock.setUnsafeCharge(generator, 0, false);
            }
        }
    }

    private int tickAllGenerators(Map<Location, Integer> generatorsWithCapacity, LongConsumer timeCallback) {
        Set<Location> exploded = new HashSet<>();
        int supply = 0;

        for (Location source : generators) {
            long timestamp = SlimefunPlugin.getProfiler().newEntry();
            Config config = BlockStorage.getLocationInfo(source);
            SlimefunItem item = SlimefunItem.getByID(config.getString("id"));

            if (item instanceof EnergyNetProvider) {
                try {
                    EnergyNetProvider provider = (EnergyNetProvider) item;
                    int energy = provider.getGeneratedOutput(source, config);

                    if (provider.getCapacity() > 0) {
                        generatorsWithCapacity.put(source, provider.getCapacity());
                        String charge = config.getString("energy-charge");

                        if (charge != null) {
                            energy += Integer.parseInt(charge);
                        }
                    }

                    if (provider.willExplode(source, config)) {
                        exploded.add(source);
                        BlockStorage.clearBlockInfo(source);

                        Slimefun.runSync(() -> {
                            source.getBlock().setType(Material.LAVA);
                            source.getWorld().createExplosion(source, 0F, false);
                        });
                    }
                    else {
                        supply += energy;
                    }
                }
                catch (Exception | LinkageError t) {
                    exploded.add(source);
                    new ErrorReport<>(t, source, item);
                }

                long time = SlimefunPlugin.getProfiler().closeEntry(source, item, timestamp);
                timeCallback.accept(time);
            }
            else {
                // This block seems to be gone now, better remove it to be extra safe
                exploded.add(source);
            }
        }

        generators.removeAll(exploded);
        return supply;
    }

    private int tickAllCapacitors() {
        int supply = 0;

        for (Location capacitor : storage) {
            supply += ChargableBlock.getCharge(capacitor);
        }

        return supply;
    }

    private void updateHologram(Block b, double supply, double demand) {
        if (demand > supply) {
            String netLoss = DoubleHandler.getFancyDouble(Math.abs(supply - demand));
            SimpleHologram.update(b, "&4&l- &c" + netLoss + " &7J &e\u26A1");
        }
        else {
            String netGain = DoubleHandler.getFancyDouble(supply - demand);
            SimpleHologram.update(b, "&2&l+ &a" + netGain + " &7J &e\u26A1");
        }
    }
}
