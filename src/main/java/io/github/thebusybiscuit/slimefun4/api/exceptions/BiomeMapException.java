package io.github.thebusybiscuit.slimefun4.api.exceptions;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;

import io.github.thebusybiscuit.slimefun4.utils.biomes.BiomeMap;

/**
 * A {@link BiomeMapException} is thrown whenever a {@link BiomeMap}
 * contains illegal, invalid or unknown values.
 * 
 * @author TheBusyBiscuit
 *
 */
public class BiomeMapException extends Exception {

    private static final long serialVersionUID = -1894334121194788527L;

    /**
     * This constructs a new {@link BiomeMapException} for the given
     * {@link BiomeMap}'s {@link NamespacedKey} with the provided context.
     * 
     * @param key
     *            The {@link NamespacedKey} of our {@link BiomeMap}
     * @param message
     *            The message to display
     */
    @ParametersAreNonnullByDefault
    public BiomeMapException(NamespacedKey key, String message) {
        super("Biome Map '" + key + "' has been misconfigured: " + message);
    }

    /**
     * This constructs a new {@link BiomeMapException} for the given
     * {@link BiomeMap}'s {@link NamespacedKey} with the provided context.
     * 
     * @param key
     *            The {@link NamespacedKey} of our {@link BiomeMap}
     * @param cause
     *            The {@link Throwable} which has caused this to happen
     */
    @ParametersAreNonnullByDefault
    public BiomeMapException(NamespacedKey key, Throwable cause) {
        super("Biome Map '" + key + "' has been misconfigured (" + cause.getMessage() + ')', cause);
    }
}
