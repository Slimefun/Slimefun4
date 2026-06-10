package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Java-8 universal port: cross-version {@link World} helpers for APIs added after 1.8.8.
 * <p>
 * {@code World#getMinHeight()} is 1.17+ (worlds started at y=0 before) and the predicate-less
 * {@code WorldBorder#isInside(Location)} is 1.20.4+. Both are reached reflectively.
 */
public final class WorldCompat {

    private WorldCompat() {}

    /**
     * Reflective {@code World#getMinHeight()} (1.17+). Returns {@code 0} on older servers, where worlds
     * always start at y=0.
     */
    public static int getMinHeight(World world) {
        Object result = ReflectionCompat.invoke(world, "getMinHeight");
        return result instanceof Integer ? (Integer) result : 0;
    }

    /**
     * Reflective {@code WorldBorder#isInside(Location)} (1.20.4+). Returns {@code true} (treat as inside)
     * on older servers where the method is absent, so legacy behaviour is unchanged.
     */
    public static boolean isInside(World world, Location location) {
        Object border = ReflectionCompat.invoke(world, "getWorldBorder");
        Object inside = ReflectionCompat.invoke(border, "isInside", location);
        // Only treat as outside when the call explicitly returned false.
        return !Boolean.FALSE.equals(inside);
    }
}
