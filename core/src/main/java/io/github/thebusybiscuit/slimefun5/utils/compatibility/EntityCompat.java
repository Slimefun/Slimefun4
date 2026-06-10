package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Compatibility helpers for entity-related API that changed across Minecraft versions.
 *
 * <p>
 * The predicate-filtered {@code World#getNearbyEntities(Location, double, double, double, Predicate)}
 * overload was only added in 1.9. The unfiltered four-argument variant has existed since 1.8, so we
 * fetch with that and apply the predicate in Java, which behaves identically on every version.
 */
public final class EntityCompat {

    private EntityCompat() {}

    /**
     * Version-independent equivalent of the predicate-filtered {@code getNearbyEntities} overload.
     *
     * @param world
     *            The {@link World} to search in
     * @param location
     *            The center {@link Location}
     * @param x
     *            Half the box length in the x direction
     * @param y
     *            Half the box length in the y direction
     * @param z
     *            Half the box length in the z direction
     * @param filter
     *            The predicate to filter entities, or {@code null} for no filtering
     *
     * @return The matching entities
     */
    public static Collection<Entity> getNearbyEntities(World world, Location location, double x, double y, double z, Predicate<Entity> filter) {
        Collection<Entity> nearby = world.getNearbyEntities(location, x, y, z);

        if (filter == null) {
            return nearby;
        }

        Collection<Entity> result = new ArrayList<>();

        for (Entity entity : nearby) {
            if (filter.test(entity)) {
                result.add(entity);
            }
        }

        return result;
    }

    /**
     * Resolves an {@link EntityType} by name, returning {@code null} when the constant does not exist
     * on the running server version (rather than throwing). Lets code reference entity types added in
     * later versions without a hard compile or runtime dependency on the constant.
     *
     * @param name
     *            The {@link EntityType} constant name (e.g. {@code "PIGLIN"})
     *
     * @return The matching {@link EntityType}, or {@code null} if absent on this version
     */
    public static EntityType entityType(String name) {
        try {
            return EntityType.valueOf(name);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Reflective {@code LivingEntity#isGliding()} (1.9+). Returns {@code false} on older servers, which
     * have no elytra gliding.
     *
     * @param entity
     *            The entity (a {@code LivingEntity} at runtime)
     *
     * @return Whether the entity is gliding
     */
    public static boolean isGliding(Object entity) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(entity, "isGliding"));
    }

    /**
     * Reflective {@code Entity#getFacing()} (1.13+). Falls back to {@link BlockFace#NORTH} on older
     * servers where the method is absent.
     *
     * @param entity
     *            The entity (an {@code Entity} at runtime)
     *
     * @return The facing direction, or {@link BlockFace#NORTH} if unavailable
     */
    public static BlockFace getFacing(Object entity) {
        Object result = ReflectionCompat.invoke(entity, "getFacing");
        return result instanceof BlockFace ? (BlockFace) result : BlockFace.NORTH;
    }
}
