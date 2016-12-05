package me.mrCookieSlime.Slimefun.api.energy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.TickerTask;
import me.mrCookieSlime.Slimefun.holograms.EnergyHologram;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class EnergyNet {
	
	public enum Axis {
		
		X_POSITIVE,
		X_NEGATIVE,
		Y_POSITIVE,
		Y_NEGATIVE,
		Z_POSITIVE,
		Z_NEGATIVE,
		UNKNOWN;
		
	}
	
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
	
	public static void tick(Block b) {
		Set<Location> input = new HashSet<Location>();
		Set<Location> storage = new HashSet<Location>();
		Set<Location> output = new HashSet<Location>();
		
		double supply = 0.0D;
		double demand = 0.0D;
		
		if (scan(b.getLocation(), Axis.UNKNOWN, new HashSet<Location>(), input, storage, output, supply, demand).isEmpty()) {
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
					if (ChargableBlock.isChargable(source)) {
						supply = DoubleHandler.fixDouble(supply + ChargableBlock.getCharge(source));
					}
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
	
	public static Set<Location> scan(Location source, Axis exclude, Set<Location> sources, Set<Location> input, Set<Location> storage, Set<Location> output, double supply, double demand) {
		sources.add(source);
		Set<Location> blocks = new HashSet<Location>();
		
		blocks.add(source);
		
		if (!exclude.equals(Axis.X_POSITIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX() + i + 1, source.getY(), source.getZ());
				if (!continueScan(l, Axis.X_NEGATIVE, blocks, sources, input, storage, output, supply, demand)) return new HashSet<Location>();
			}
		}
		if (!exclude.equals(Axis.X_NEGATIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX() - i - 1, source.getY(), source.getZ());
				if (!continueScan(l, Axis.X_POSITIVE, blocks, sources, input, storage, output, supply, demand)) return new HashSet<Location>();
			}
		}
		
		if (!exclude.equals(Axis.Y_POSITIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY() + i + 1, source.getZ());
				if (!continueScan(l, Axis.Y_NEGATIVE, blocks, sources, input, storage, output, supply, demand)) return new HashSet<Location>();
			}
		}
		if (!exclude.equals(Axis.Y_NEGATIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY() - i - 1, source.getZ());
				if (!continueScan(l, Axis.Y_POSITIVE, blocks, sources, input, storage, output, supply, demand)) return new HashSet<Location>();
			}
		}
		
		if (!exclude.equals(Axis.Z_POSITIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY(), source.getZ() + i + 1);
				if (!continueScan(l, Axis.Z_NEGATIVE, blocks, sources, input, storage, output, supply, demand)) return new HashSet<Location>();
			}
		}
		if (!exclude.equals(Axis.Z_NEGATIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY(), source.getZ() - i - 1);
				if (!continueScan(l, Axis.Z_POSITIVE, blocks, sources, input, storage, output, supply, demand)) return new HashSet<Location>();
			}
		}
		
		return blocks;
	}

	private static boolean continueScan(Location l, Axis axis, Set<Location> blocks, Set<Location> sources, Set<Location> input, Set<Location> storage, Set<Location> output, double supply, double demand) {
		if (!sources.contains(l)) {
			if (BlockStorage.check(l, "ENERGY_REGULATOR")) return false;
			switch (getComponent(l)) {
			case CONSUMER: {
				blocks.add(l);
				output.add(l);
				break;
			}
			case DISTRIBUTOR: {
				blocks.add(l);
				if (ChargableBlock.isCapacitor(l)) storage.add(l);
				Set<Location> nextBlocks = scan(l, axis, sources, input, storage, output, supply, demand);
				if (nextBlocks.isEmpty()) return false;
				for (Location sink: nextBlocks) {
					blocks.add(sink);
				}
				break;
			}
			case SOURCE: {
				blocks.add(l);
				input.add(l);
				break;
			}
			default:
				break;
			}
		}
		return true;
	}

}
