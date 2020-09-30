package io.github.thebusybiscuit.slimefun4.api.accessibility;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.core.attributes.TierAccessible;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;

/**
 * Represents an data wrapper object, responsible for reading from any given {@link SlimefunItem} which is also
 * an instance of {@link TierAccessible}.
 *
 * @author md5sha256
 */
public abstract class AbstractAccessManager implements AccessManager {

    private final NamespacedKey namespace;

    public AbstractAccessManager(@Nonnull final NamespacedKey namespace) {
        this.namespace = namespace;
    }

    public void clear() {

    }

    public <I extends SlimefunItem & TierAccessible> void load(@Nonnull I object) {
        clear();
        if (!SlimefunPlugin.getMinecraftVersion()
            .isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            // This feature was introduced post 1.13. Cannot load newer data on an older version
            return;
        }
    }


}
