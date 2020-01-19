package me.mrCookieSlime.Slimefun.api.network;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;

import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.MC_1_13.ParticleEffect;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public abstract class Network {
	
	private static List<Network> networkList = new ArrayList<>();
	
	public static<T extends Network> T getNetworkFromLocation(Location l, Class<T> type) {
		for (Network network : networkList) {
			if (type.isInstance(network) && network.connectsTo(l)) {
				return type.cast(network);
			}
		}
		return null;
	}

	public static<T extends Network> List<T> getNetworksFromLocation(Location l, Class<T> type) {
		List<T> list = new ArrayList<>();
		for (Network network : networkList) {
			if (type.isInstance(network) && network.connectsTo(l)) {
				list.add(type.cast(network));
			}
		}
		return list;
	}

	public static void registerNetwork(Network n) {
		networkList.add(n);
	}

	public static void unregisterNetwork(Network n) {
		networkList.remove(n);
	}

	public static void handleAllNetworkLocationUpdate(Location l) {
		for (Network n : getNetworksFromLocation(l, Network.class)) {
			n.handleLocationUpdate(l);
		}
	}

	public abstract int getRange();
	public abstract NetworkComponent classifyLocation(Location l);
	public abstract void locationClassificationChange(Location l, NetworkComponent from, NetworkComponent to);

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
		if (connectedLocations.contains(l)) {
			return;
		}
		
		connectedLocations.add(l.clone());
		handleLocationUpdate(l);
	}

	public void handleLocationUpdate(Location l) {
		if (regulator.equals(l)) {
			unregisterNetwork(this);
			return;
		}
		
		nodeQueue.add(l.clone());
	}

	public boolean connectsTo(Location l) {
		return connectedLocations.contains(l);
	}

	private NetworkComponent getCurrentClassification(Location l) {
		if (regulatorNodes.contains(l)) {
			return NetworkComponent.REGULATOR;
		} 
		else if (connectorNodes.contains(l)) {
			return NetworkComponent.CONNECTOR;
		} 
		else if (terminusNodes.contains(l)) {
			return NetworkComponent.TERMINUS;
		}
		
		return null;
	}

	private void discoverStep() {
		int steps = 0;
		
		while (nodeQueue.peek() != null) {
			Location l = nodeQueue.poll();
			NetworkComponent currentAssignment = getCurrentClassification(l);
			NetworkComponent classification = classifyLocation(l);
			
			if (classification != currentAssignment) {
				if (currentAssignment == NetworkComponent.REGULATOR || currentAssignment == NetworkComponent.CONNECTOR) {
					// Requires a complete rebuild of the network, so we just throw the current one away.
					unregisterNetwork(this);
					return;
				} 
				else if (currentAssignment == NetworkComponent.TERMINUS) {
					terminusNodes.remove(l);
				}
				
				if (classification == NetworkComponent.REGULATOR) {
					regulatorNodes.add(l);
					discoverNeighbors(l);
				} 
				else if(classification == NetworkComponent.CONNECTOR) {
					connectorNodes.add(l);
					discoverNeighbors(l);
				} 
				else if(classification == NetworkComponent.TERMINUS) {
					terminusNodes.add(l);
				}
				
				locationClassificationChange(l, currentAssignment, classification);
			}
			steps += 1;
			// Consider making this a configuration option so that it can be increased for servers
			// that can deal with the load.
			if(steps == 500) break;
		}
	}

	private void discoverNeighbors(Location l, double xDiff, double yDiff, double zDiff) {
		for (int i = getRange() + 1; i > 0; i --) {
			Location newLocation = l.clone().add(i * xDiff, i * yDiff, i * zDiff);
			addLocationToNetwork(newLocation);
		}
	}

	private void discoverNeighbors(Location l) {
		discoverNeighbors(l, 1.0, 0.0, 0.0);
		discoverNeighbors(l, -1.0, 0.0, 0.0);
		discoverNeighbors(l, 0.0, 1.0, 0.0);
		discoverNeighbors(l, 0.0, -1.0, 0.0);
		discoverNeighbors(l, 0.0, 0.0, 1.0);
		discoverNeighbors(l, 0.0, 0.0, -1.0);
	}

	public void display() {
		Slimefun.runSync(() -> {
			for (Location l : connectedLocations) {
				try {
					ParticleEffect.REDSTONE.display(l.clone().add(0.5, 0.5, 0.5), 0, 0, 0, 1, 1);
				} catch(Exception x) {
					Slimefun.getLogger().log(Level.SEVERE, "An Error occured while playing Network Animation for Slimefun " + Slimefun.getVersion(), x);
				}
			}
		});
	}

	public void tick() {
		discoverStep();
	}
}
