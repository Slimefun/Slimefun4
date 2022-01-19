package io.github.thebusybiscuit.slimefun4.implementation.resources;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;

import io.github.thebusybiscuit.slimefun4.api.exceptions.BiomeMapException;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.biomes.BiomeMap;

/**
 * This is an abstract parent class for any {@link GEOResource}
 * that is added by Slimefun itself. It is package-private, therefore
 * only classes inside this package can access it.
 * <p>
 * It just provides a bit of a convenience for us to reduce redundancies
 * in our {@link GEOResource} implementations.
 * 
 * @author TheBusyBiscuit
 *
 */
abstract class AbstractResource implements GEOResource {

    private final NamespacedKey key;
    private final String defaultName;
    private final ItemStack item;
    private final int maxDeviation;
    private final boolean geoMiner;

    @ParametersAreNonnullByDefault
    AbstractResource(String key, String defaultName, ItemStack item, int maxDeviation, boolean geoMiner) {
        Validate.notNull(key, "NamespacedKey cannot be null!");
        Validate.notNull(defaultName, "The default name cannot be null!");
        Validate.notNull(item, "item cannot be null!");

        this.key = new NamespacedKey(Slimefun.instance(), key);
        this.defaultName = defaultName;
        this.item = item;
        this.maxDeviation = maxDeviation;
        this.geoMiner = geoMiner;
    }

    @Override
    @Nonnull
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    @Nonnull
    public String getName() {
        return defaultName;
    }

    @Override
    @Nonnull
    public ItemStack getItem() {
        return item.clone();
    }

    @Override
    public int getMaxDeviation() {
        return maxDeviation;
    }

    @Override
    public boolean isObtainableFromGEOMiner() {
        return geoMiner;
    }

    /**
     * Internal helper method for reading a {@link BiomeMap} of {@link Integer} type values from
     * a resource file.
     * 
     * @param resource
     *            The {@link AbstractResource} instance
     * @param path
     *            The path to our biome map file
     * 
     * @return A {@link BiomeMap} for this resource
     */
    @ParametersAreNonnullByDefault
    static final @Nonnull BiomeMap<Integer> getBiomeMap(AbstractResource resource, String path) {
        Validate.notNull(resource, "Resource cannot be null.");
        Validate.notNull(path, "Path cannot be null.");

        try {
            return BiomeMap.fromResource(resource.getKey(), Slimefun.instance(), path, JsonElement::getAsInt);
        } catch (BiomeMapException x) {
            if (Slimefun.instance().isUnitTest()) {
                // Unit Tests should always fail here, so we re-throw the exception
                throw new IllegalStateException(x);
            } else {
                // In a server environment, we should just print a warning and carry on
                Slimefun.logger().log(Level.WARNING, x, () -> "Failed to load BiomeMap for GEO-resource: " + resource.getKey());
                return new BiomeMap<>(resource.getKey());
            }
        }
    }
}
