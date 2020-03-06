package io.github.thebusybiscuit.slimefun4.api.network;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * An abstract Network class to manage networks in a stateful way
 *
 * @author meiamsome
 */
public abstract class Network {

    public abstract int getRange();

    public abstract NetworkComponent classifyLocation(Location l);

    public abstract void onClassificationChange(Location l, NetworkComponent from, NetworkComponent to);

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
			SlimefunPlugin.getNetworkManager().unregisterNetwork(this);
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
		int maxSteps = SlimefunPlugin.getNetworkManager().getMaxSize();
		int steps = 0;
		
		while (nodeQueue.peek() != null) {
			Location l = nodeQueue.poll();
			NetworkComponent currentAssignment = getCurrentClassification(l);
			NetworkComponent classification = classifyLocation(l);
			
			if (classification != currentAssignment) {
				if (currentAssignment == NetworkComponent.REGULATOR || currentAssignment == NetworkComponent.CONNECTOR) {
					// Requires a complete rebuild of the network, so we just throw the current one away.
					SlimefunPlugin.getNetworkManager().unregisterNetwork(this);
					return;
				} 
				else if (currentAssignment == NetworkComponent.TERMINUS) {
					terminusNodes.remove(l);
				}
				
				if (classification == NetworkComponent.REGULATOR) {
					regulatorNodes.add(l);
                    discoverNeighbors(l);
                } else if (classification == NetworkComponent.CONNECTOR) {
                    connectorNodes.add(l);
                    discoverNeighbors(l);
                } else if (classification == NetworkComponent.TERMINUS) {
                    terminusNodes.add(l);
                }

                onClassificationChange(l, currentAssignment, classification);
            }
            steps += 1;
			
			if (steps >= maxSteps) {
				break;
			}
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
			DustOptions options = new DustOptions(Color.BLUE, 2F);
			
			for (Location l : connectedLocations) {
				l.getWorld().spawnParticle(Particle.REDSTONE, l.getX() + 0.5, l.getY() + 0.5, l.getZ() + 0.5, 1, 0, 0, 0, 1, options);
			}
		});
	}

	public void tick() {
		discoverStep();
	}
}
