package io.github.thebusybiscuit.slimefun4.api.network;

import javax.annotation.Nonnull;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;

/**
 * This class represents the visualizer task of a given {@link Network}.
 * 
 * @author TheBusyBiscuit
 *
 */
class NetworkVisualizer implements Runnable {

    /**
     * The {@link DustOptions} define the {@link Color} and size of our particles.
     */
    private final DustOptions options = new DustOptions(Color.BLUE, 3.5F);

    /**
     * This is our {@link Network} instance.
     */
    private final Network network;

    /**
     * This creates a new {@link NetworkVisualizer} for the given {@link Network}.
     * 
     * @param network
     *            The {@link Network} to visualize
     */
    NetworkVisualizer(@Nonnull Network network) {
        this.network = network;
    }

    @Override
    public void run() {
        for (Location l : network.connectorNodes) {
            spawnParticles(l);
        }

        for (Location l : network.terminusNodes) {
            spawnParticles(l);
        }
    }

    /**
     * This method will spawn the actual particles.
     * 
     * @param l
     *            The {@link Location} of our node
     */
    private void spawnParticles(@Nonnull Location l) {
        l.getWorld().spawnParticle(Particle.REDSTONE, l.getX() + 0.5, l.getY() + 0.5, l.getZ() + 0.5, 1, 0, 0, 0, 1, options);
    }

}
