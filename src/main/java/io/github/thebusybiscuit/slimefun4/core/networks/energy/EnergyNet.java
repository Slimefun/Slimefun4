package io.github.thebusybiscuit.slimefun4.core.networks.energy;

import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.api.ErrorReport;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.Reactor;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.GeneratorTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongConsumer;

/**
 * The {@link EnergyNet} is an implementation of {@link Network} that deals with
 * electrical energy being send from and to nodes.
 *
 * @author meiamsome
 * @author TheBusyBiscuit
 *
 * @see Network
 * @see EnergyNetComponent
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

        if (SlimefunPlugin.getRegistry().getEnergyGenerators().contains(id)) {
            return EnergyNetComponentType.GENERATOR;
        }

        if (SlimefunPlugin.getRegistry().getEnergyCapacitors().contains(id)) {
            return EnergyNetComponentType.CAPACITOR;
        }

        if (SlimefunPlugin.getRegistry().getEnergyConsumers().contains(id)) {
            return EnergyNetComponentType.CONSUMER;
        }

        return EnergyNetComponentType.NONE;
    }

    public static EnergyNet getNetworkFromLocationOrCreate(Location l) {
        Optional<EnergyNet> cargoNetwork = SlimefunPlugin.getNetworkManager().getNetworkFromLocation(l, EnergyNet.class);

        if (cargoNetwork.isPresent()) {
            return cargoNetwork.get();
        } else {
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
            SimpleHologram.update(b, "&4检测到附近有能源调节器");
            return;
        }

        super.tick();

        if (connectorNodes.isEmpty() && terminusNodes.isEmpty()) {
            SimpleHologram.update(b, "&4附近没有能源网络");
        } else {
            double supply = DoubleHandler.fixDouble(tickAllGenerators(timestamp::getAndAdd) + tickAllCapacitors());
            double demand = 0;

            int availableEnergy = (int) supply;

            for (Location machine : consumers) {
                int capacity = ChargableBlock.getMaxCharge(machine);
                int charge = ChargableBlock.getCharge(machine);

                if (charge < capacity) {
                    int availableSpace = capacity - charge;
                    demand += availableSpace;

                    if (availableEnergy > 0) {
                        if (availableEnergy > availableSpace) {
                            ChargableBlock.setUnsafeCharge(machine, capacity, false);
                            availableEnergy -= availableSpace;
                        } else {
                            ChargableBlock.setUnsafeCharge(machine, charge + availableEnergy, false);
                            availableEnergy = 0;
                        }
                    }
                }
            }

            storeExcessEnergy(availableEnergy);
            updateHologram(b, supply, demand);
        }

        // We have subtracted the timings from Generators, so they do not show up twice.
        SlimefunPlugin.getProfiler().closeEntry(b.getLocation(), SlimefunItems.ENERGY_REGULATOR.getItem(), timestamp.get());
    }

    private void storeExcessEnergy(int available) {
        for (Location capacitor : storage) {
            if (available > 0) {
                int capacity = ChargableBlock.getMaxCharge(capacitor);

                if (available > capacity) {
                    ChargableBlock.setUnsafeCharge(capacitor, capacity, true);
                    available -= capacity;
                } else {
                    ChargableBlock.setUnsafeCharge(capacitor, available, true);
                    available = 0;
                }
            } else {
                ChargableBlock.setUnsafeCharge(capacitor, 0, true);
            }
        }

        for (Location generator : generators) {
            int capacity = ChargableBlock.getMaxCharge(generator);

            if (capacity > 0) {
                if (available > 0) {
                    if (available > capacity) {
                        ChargableBlock.setUnsafeCharge(generator, capacity, false);
                        available = available - capacity;
                    } else {
                        ChargableBlock.setUnsafeCharge(generator, available, false);
                        available = 0;
                    }
                } else {
                    ChargableBlock.setUnsafeCharge(generator, 0, false);
                }
            }
        }
    }

    private double tickAllGenerators(LongConsumer timeCallback) {
        double supply = 0;
        Set<Location> exploded = new HashSet<>();

        for (Location source : generators) {
            long timestamp = SlimefunPlugin.getProfiler().newEntry();
            Config config = BlockStorage.getLocationInfo(source);
            SlimefunItem item = SlimefunItem.getByID(config.getString("id"));

            if (item != null) {
                try {
                    GeneratorTicker generator = item.getEnergyTicker();

                    if (generator != null) {
                        double energy = generator.generateEnergy(source, item, config);

                        if (generator.explode(source)) {
                            exploded.add(source);
                            BlockStorage.clearBlockInfo(source);
                            Reactor.processing.remove(source);
                            Reactor.progress.remove(source);

                            Slimefun.runSync(() -> {
                                source.getBlock().setType(Material.LAVA);
                                source.getWorld().createExplosion(source, 0F, false);
                            });
                        } else {
                            supply += energy;
                        }
                    } else {
                        item.warn("This Item was marked as a 'GENERATOR' but has no 'GeneratorTicker' attached to it! This must be fixed.");
                    }
                } catch (Exception | LinkageError t) {
                    exploded.add(source);
                    new ErrorReport(t, source, item);
                }

                long time = SlimefunPlugin.getProfiler().closeEntry(source, item, timestamp);
                timeCallback.accept(time);
            } else {
                // This block seems to be gone now, better remove it to be extra safe
                exploded.add(source);
            }
        }

        generators.removeAll(exploded);

        return supply;
    }

    private double tickAllCapacitors() {
        double supply = 0;

        for (Location capacitor : storage) {
            supply += ChargableBlock.getCharge(capacitor);
        }

        return supply;
    }

    private void updateHologram(Block b, double supply, double demand) {
        if (demand > supply) {
            String netLoss = DoubleHandler.getFancyDouble(Math.abs(supply - demand));
            SimpleHologram.update(b, "&4&l- &c" + netLoss + " &7J &e\u26A1");
        } else {
            String netGain = DoubleHandler.getFancyDouble(supply - demand);
            SimpleHologram.update(b, "&2&l+ &a" + netGain + " &7J &e\u26A1");
        }
    }
}