package me.mrCookieSlime.Slimefun.api.network;

import java.util.Set;
import java.util.List;
import java.util.Queue;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.ArrayList;

import org.bukkit.Location;

import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.MC_1_13.ParticleEffect;
import me.mrCookieSlime.Slimefun.SlimefunStartup;

public abstract class Network {
	
	private static List<Network> NETWORK_LIST = new ArrayList<>();
	
	public static<T extends Network> T getNetworkFromLocation(Location l, Class<T> type) {
		for(Network n: NETWORK_LIST) {
			if(type.isInstance(n) && n.connectsTo(l)) {
				return type.cast(n);
			}
		}
		return null;
	}

	public static<T extends Network> List<T> getNetworksFromLocation(Location l, Class<T> type) {
		List<T> ret = new ArrayList<>();
		for(Network n: NETWORK_LIST) {
			if(type.isInstance(n) && n.connectsTo(l)) {
				ret.add(type.cast(n));
			}
		}
		return ret;
	}

	public static void registerNetwork(Network n) {
		NETWORK_LIST.add(n);
	}

	public static void unregisterNetwork(Network n) {
		NETWORK_LIST.remove(n);
	}

	public static void handleAllNetworkLocationUpdate(Location l) {
		for(Network n: getNetworksFromLocation(l, Network.class)) {
			n.handleLocationUpdate(l);
		}
	}

	public static enum Component {
		CONNECTOR,
		REGULATOR,
		TERMINUS;
	}


	public abstract int getRange();
	public abstract Component classifyLocation(Location l);
	public abstract void locationClassificationChange(Location l, Component from, Component to);

	protected Location regulator;
	private Queue<Location> nodeQueue = new ArrayDeque<>();

	protected Set<Location> connectedLocations = new HashSet<>();
	protected Set<Location> regulatorNodes = new HashSet<>();
	protected Set<Location> connectorNodes = new HashSet<>();
	protected Set<Location> terminusNodes = new HashSet<>();

	protected Network(Location regulator) {
		this.regulator = regulator;
		connectedLocations.add(regulator);
		nodeQueue.add(regulator.clone());
	}

	protected void addLocationToNetwork(Location l) {
		if(connectedLocations.contains(l)) {
			return;
		}
		connectedLocations.add(l.clone());
		handleLocationUpdate(l);
	}

	public void handleLocationUpdate(Location l) {
		if(regulator.equals(l)) {
			unregisterNetwork(this);
			return;
		}
		nodeQueue.add(l.clone());
	}

	public boolean connectsTo(Location l) {
		return connectedLocations.contains(l);
	}

	private Component getCurrentClassification(Location l) {
		if(regulatorNodes.contains(l)) {
			return Component.REGULATOR;
		} else if(connectorNodes.contains(l)) {
			return Component.CONNECTOR;
		} else if(terminusNodes.contains(l)) {
			return Component.TERMINUS;
		}
		return null;
	}

	private void discoverStep() {
		int steps = 0;
		while(nodeQueue.peek() != null) {
			Location l = nodeQueue.poll();
			Component currentAssignment = getCurrentClassification(l);
			Component classification = classifyLocation(l);
			if(classification != currentAssignment) {
				if(currentAssignment == Component.REGULATOR || currentAssignment == Component.CONNECTOR) {
					// Requires a complete rebuild of the network, so we just throw the current one away.
					unregisterNetwork(this);
					return;
				} else if(currentAssignment == Component.TERMINUS) {
					terminusNodes.remove(l);
				}
				if(classification == Component.REGULATOR) {
					regulatorNodes.add(l);
					discoverNeighbors(l);
				} else if(classification == Component.CONNECTOR) {
					connectorNodes.add(l);
					discoverNeighbors(l);
				} else if(classification == Component.TERMINUS) {
					terminusNodes.add(l);
				}
				locationClassificationChange(l, currentAssignment, classification);
			}
			steps += 1;
			// TODO: Consider making this a configuration option so that it can be increased for servers
			// that can deal with the load.
			if(steps == 500) break;
		}
	}

	private void discoverNeighbors(Location l, int xDiff, int yDiff, int zDiff) {
		for(int i = getRange() + 1; i > 0; i --) {
			Location new_location = l.clone().add(i * xDiff, i * yDiff, i * zDiff);
			addLocationToNetwork(new_location);
		}
	}

	private void discoverNeighbors(Location l) {
		discoverNeighbors(l, 1, 0, 0);
		discoverNeighbors(l, -1, 0, 0);
		discoverNeighbors(l, 0, 1, 0);
		discoverNeighbors(l, 0, -1, 0);
		discoverNeighbors(l, 0, 0, 1);
		discoverNeighbors(l, 0, 0, -1);
	}

	public void display() {
		SlimefunStartup.instance.getServer().getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
			@Override
			public void run() {
				for(Location l: connectedLocations) {
					try {
						ParticleEffect.REDSTONE.display(l.clone().add(0.5, 0.5, 0.5), 0, 0, 0, 1, 1);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void tick() {
		discoverStep();
	}
}
