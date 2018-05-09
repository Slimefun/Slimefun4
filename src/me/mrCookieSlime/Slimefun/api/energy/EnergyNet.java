package me.mrCookieSlime.Slimefun.api.energy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.mrCookieSlime.Slimefun.api.network.Network;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.TickerTask;
import me.mrCookieSlime.Slimefun.api.network.Network;
import me.mrCookieSlime.Slimefun.holograms.EnergyHologram;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class EnergyNet extends Network {
	public enum NetworkComponent {
		SOURCE,
		DISTRIBUTOR,
		CONSUMER,
		NONE;
	}

	private static final int RANGE = 6;

	public static Set<String> machines_input = new HashSet<String>();
	public static Set<String> machines_storage = new HashSet<String>();
	public static Set<String> machines_output = new HashSet<String>();

	public static Map<String, EnergyFlowListener> listeners = new HashMap<String, EnergyFlowListener>();

	public static NetworkComponent getComponent(Block b) {
		return getComponent(b.getLocation());
	}

	public static NetworkComponent getComponent(String id) {
		if (machines_input.contains(id)) return NetworkComponent.SOURCE;
		if (machines_storage.contains(id)) return NetworkComponent.DISTRIBUTOR;
		if (machines_output.contains(id)) return NetworkComponent.CONSUMER;
		return NetworkComponent.NONE;
	}

	public static NetworkComponent getComponent(Location l) {
		if (!BlockStorage.hasBlockInfo(l)) return NetworkComponent.NONE;
		String id = BlockStorage.checkID(l);
		if (machines_input.contains(id)) return NetworkComponent.SOURCE;
		if (machines_storage.contains(id)) return NetworkComponent.DISTRIBUTOR;
		if (machines_output.contains(id)) return NetworkComponent.CONSUMER;
		return NetworkComponent.NONE;
	}

	public static void registerComponent(String id, NetworkComponent component) {
		switch (component) {
		case CONSUMER:
			machines_output.add(id);
			break;
		case DISTRIBUTOR:
			machines_storage.add(id);
			break;
		case SOURCE:
			machines_input.add(id);
			break;
		default:
			break;
		}
	}

	public static EnergyNet getNetworkFromLocation(Location l) {
		return getNetworkFromLocation(l, EnergyNet.class);
	}

	public static EnergyNet getNetworkFromLocationOrCreate(Location l) {
		EnergyNet energy_network = getNetworkFromLocation(l);
		if(energy_network == null) {
			energy_network = new EnergyNet(l);
			registerNetwork(energy_network);
		}
		return energy_network;
	}

	private Set<Location> input = new HashSet<Location>();
	private Set<Location> storage = new HashSet<Location>();
	private Set<Location> output = new HashSet<Location>();

	protected EnergyNet(Location l) {
		super(l);
	}

	public int getRange() {
		return RANGE;
	}

	public Network.Component classifyLocation(Location l) {
		if(regulator.equals(l)) return Network.Component.REGULATOR;
		switch(getComponent(l)) {
			case DISTRIBUTOR:
				return Network.Component.CONNECTOR;
			case CONSUMER:
			case SOURCE:
				return Network.Component.TERMINUS;
			default:
				return null;
		}
	}

	public void locationClassificationChange(Location l, Network.Component from, Network.Component to) {
		if(from == Network.Component.TERMINUS) {
			input.remove(l);
			output.remove(l);
		}
		switch(getComponent(l)) {
			case DISTRIBUTOR:
				if (ChargableBlock.isCapacitor(l)) storage.add(l);
				break;
			case CONSUMER:
				output.add(l);
				break;
			case SOURCE:
				input.add(l);
				break;
		}
	}

	public void tick(Block b) {
		if(!regulator.equals(b.getLocation())) {
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
			for (final Location source: input) {
				long timestamp = System.currentTimeMillis();
				SlimefunItem item = BlockStorage.check(source);
				double energy = item.getEnergyTicker().generateEnergy(source, item, BlockStorage.getBlockInfo(source));

				if (item.getEnergyTicker().explode(source)) {
					BlockStorage.clearBlockInfo(source);
					Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {

						@Override
						public void run() {
							source.getBlock().setType(Material.LAVA);
							source.getWorld().createExplosion(source, 0F, false);
						}
					});
				}
				else {
					supply = supply + energy;
				}
				TickerTask.block_timings.put(source, System.currentTimeMillis() - timestamp);
			}

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
