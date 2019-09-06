package me.mrCookieSlime.Slimefun.api.energy;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.network.Network;
import me.mrCookieSlime.Slimefun.api.network.NetworkComponent;
import me.mrCookieSlime.Slimefun.holograms.EnergyHologram;

public class EnergyNet extends Network {

	private static final int RANGE = 6;
	
	public static EnergyNetComponent getComponent(Block b) {
		return getComponent(b.getLocation());
	}

	public static EnergyNetComponent getComponent(String id) {
		if (SlimefunPlugin.getUtilities().energyNetInput.contains(id)) return EnergyNetComponent.SOURCE;
		if (SlimefunPlugin.getUtilities().energyNetStorage.contains(id)) return EnergyNetComponent.DISTRIBUTOR;
		if (SlimefunPlugin.getUtilities().energyNetOutput.contains(id)) return EnergyNetComponent.CONSUMER;
		return EnergyNetComponent.NONE;
	}

	public static EnergyNetComponent getComponent(Location l) {
		if (!BlockStorage.hasBlockInfo(l)) return EnergyNetComponent.NONE;
		String id = BlockStorage.checkID(l);
		if (SlimefunPlugin.getUtilities().energyNetInput.contains(id)) return EnergyNetComponent.SOURCE;
		if (SlimefunPlugin.getUtilities().energyNetStorage.contains(id)) return EnergyNetComponent.DISTRIBUTOR;
		if (SlimefunPlugin.getUtilities().energyNetOutput.contains(id)) return EnergyNetComponent.CONSUMER;
		return EnergyNetComponent.NONE;
	}

	public static void registerComponent(String id, EnergyNetComponent component) {
		switch (component) {
		case CONSUMER:
			SlimefunPlugin.getUtilities().energyNetOutput.add(id);
			break;
		case DISTRIBUTOR:
			SlimefunPlugin.getUtilities().energyNetStorage.add(id);
			break;
		case SOURCE:
			SlimefunPlugin.getUtilities().energyNetInput.add(id);
			break;
		default:
			break;
		}
	}

	public static EnergyNet getNetworkFromLocation(Location l) {
		return getNetworkFromLocation(l, EnergyNet.class);
	}

	public static EnergyNet getNetworkFromLocationOrCreate(Location l) {
		EnergyNet energyNetwork = getNetworkFromLocation(l);
		if (energyNetwork == null) {
			energyNetwork = new EnergyNet(l);
			registerNetwork(energyNetwork);
		}
		return energyNetwork;
	}

	private Set<Location> input = new HashSet<>();
	private Set<Location> storage = new HashSet<>();
	private Set<Location> output = new HashSet<>();

	protected EnergyNet(Location l) {
		super(l);
	}

	public int getRange() {
		return RANGE;
	}

	public NetworkComponent classifyLocation(Location l) {
		if (regulator.equals(l)) return NetworkComponent.REGULATOR;
		switch (getComponent(l)) {
			case DISTRIBUTOR:
				return NetworkComponent.CONNECTOR;
			case CONSUMER:
			case SOURCE:
				return NetworkComponent.TERMINUS;
			default:
				return null;
		}
	}

	public void locationClassificationChange(Location l, NetworkComponent from, NetworkComponent to) {
		if (from == NetworkComponent.TERMINUS) {
			input.remove(l);
			output.remove(l);
		}
		
		switch (getComponent(l)) {
			case DISTRIBUTOR:
				if (ChargableBlock.isCapacitor(l)) storage.add(l);
				break;
			case CONSUMER:
				output.add(l);
				break;
			case SOURCE:
				input.add(l);
				break;
			default:
				break;
		}
	}

	public void tick(Block b) {
		if (!regulator.equals(b.getLocation())) {
			EnergyHologram.update(b, "&4Multiple Energy Regulators connected");
			return;
		}
		super.tick();
		double supply = 0.0D;
		double demand = 0.0D;

		if (connectorNodes.isEmpty() && terminusNodes.isEmpty()) {
			EnergyHologram.update(b, "&4No Energy Network found");
		}
		else {
			Set<Location> exploded = new HashSet<>();
			for (final Location source: input) {
				long timestamp = System.currentTimeMillis();
				SlimefunItem item = BlockStorage.check(source);
				double energy = item.getEnergyTicker().generateEnergy(source, item, BlockStorage.getLocationInfo(source));

				if (item.getEnergyTicker().explode(source)) {
					exploded.add(source); 
					BlockStorage.clearBlockInfo(source);
					Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
						source.getBlock().setType(Material.LAVA);
						source.getWorld().createExplosion(source, 0F, false);
					});
				}
				else {
					supply = supply + energy;
				}
				SlimefunPlugin.getTicker().addBlockTimings(source, System.currentTimeMillis() - timestamp);
			}

			input.removeAll(exploded);

			for (Location battery: storage) {
				supply = supply + ChargableBlock.getCharge(battery);
			}

			int available = (int) DoubleHandler.fixDouble(supply);

			for (Location destination: output) {
				int capacity = ChargableBlock.getMaxCharge(destination);
				int charge = ChargableBlock.getCharge(destination);
				if (charge < capacity) {
					int rest = capacity - charge;
					demand = demand + rest;
					if (available > 0) {
						if (available > rest) {
							ChargableBlock.setUnsafeCharge(destination, capacity, false);
							available = available - rest;
						}
						else {
							ChargableBlock.setUnsafeCharge(destination, charge + available, false);
							available = 0;
						}
					}
				}
			}

			for (Location battery: storage) {
				if (available > 0) {
					int capacity = ChargableBlock.getMaxCharge(battery);

					if (available > capacity) {
						ChargableBlock.setUnsafeCharge(battery, capacity, true);
						available = available - capacity;
					}
					else {
						ChargableBlock.setUnsafeCharge(battery, available, true);
						available = 0;
					}
				}
				else ChargableBlock.setUnsafeCharge(battery, 0, true);
			}

			for (Location source: input) {
				if (ChargableBlock.isChargable(source)) {
					if (available > 0) {
						int capacity = ChargableBlock.getMaxCharge(source);

						if (available > capacity) {
							ChargableBlock.setUnsafeCharge(source, capacity, false);
							available = available - capacity;
						}
						else {
							ChargableBlock.setUnsafeCharge(source, available, false);
							available = 0;
						}
					}
					else ChargableBlock.setUnsafeCharge(source, 0, false);
				}
			}

			EnergyHologram.update(b, supply, demand);
		}
	}
}
