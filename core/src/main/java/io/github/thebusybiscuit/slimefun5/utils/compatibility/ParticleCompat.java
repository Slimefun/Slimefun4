package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Java-8 universal port: cross-version particle spawning.
 * <p>
 * {@code org.bukkit.Particle} and {@code World#spawnParticle(...)} are 1.9+ APIs that do not exist at
 * the 1.8.8 compile floor. A {@code compileOnly} stub provides the {@code Particle} type/constants, but
 * the {@code spawnParticle} <em>method</em> cannot be stubbed onto {@code World}, so call sites delegate
 * here. This helper invokes the fullest {@code spawnParticle(Particle, Location, int, double, double,
 * double, double, T)} overload reflectively, touching {@code org.bukkit.Particle} only via reflection so
 * this class loads on every version. On 1.8 (no particle API) the calls become no-ops.
 * <p>
 * The {@code particle} argument is typed as {@link Object} on purpose (it is an {@code org.bukkit.Particle}
 * at runtime on 1.9+); this keeps the {@code Particle} type out of this class's signatures.
 *
 * @author Slimefun (Java-8 port)
 */
public final class ParticleCompat {

    private static final Method SPAWN_PARTICLE;

    static {
        Method spawn = null;

        try {
            Class<?> particleClass = Class.forName("org.bukkit.Particle");
            spawn = World.class.getMethod("spawnParticle", particleClass, Location.class, int.class, double.class, double.class, double.class, double.class, Object.class);
        } catch (Throwable ignored) {
            // Pre-1.9 server: no particle API. Calls become no-ops.
            spawn = null;
        }

        SPAWN_PARTICLE = spawn;
    }

    private ParticleCompat() {}

    private static void invoke(@Nullable World world, @Nullable Object particle, @Nullable Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable Object data) {
        if (SPAWN_PARTICLE == null || world == null || particle == null || location == null) {
            return;
        }

        try {
            SPAWN_PARTICLE.invoke(world, particle, location, count, offsetX, offsetY, offsetZ, extra, data);
        } catch (Throwable ignored) {
            // Particle not supported on this version / invalid data — ignore.
        }
    }

    // --- Location-based overloads (mirror World#spawnParticle signatures) ---

    public static void spawn(World world, Object particle, Location location, int count) {
        invoke(world, particle, location, count, 0, 0, 0, 0, null);
    }

    public static void spawn(World world, Object particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        invoke(world, particle, location, count, offsetX, offsetY, offsetZ, 0, null);
    }

    public static void spawn(World world, Object particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        invoke(world, particle, location, count, offsetX, offsetY, offsetZ, extra, null);
    }

    public static void spawn(World world, Object particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, Object data) {
        invoke(world, particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }

    // --- Coordinate-based overloads ---

    public static void spawn(World world, Object particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        invoke(world, particle, world == null ? null : new Location(world, x, y, z), count, offsetX, offsetY, offsetZ, extra, null);
    }

    public static void spawn(World world, Object particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, Object data) {
        invoke(world, particle, world == null ? null : new Location(world, x, y, z), count, offsetX, offsetY, offsetZ, extra, data);
    }
}
