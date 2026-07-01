package org.bukkit;

/**
 * Compile-only shadow stub of Bukkit's {@code org.bukkit.Particle} (introduced in MC 1.9).
 * <p>
 * Core compiles against the 1.8.8 API floor where this type does not exist. This stub exposes only
 * the constants and the {@link DustOptions} nested type that Slimefun core references directly, so
 * the code compiles. It is a {@code compileOnly} dependency and is NOT shaded into the jar: on 1.9+
 * servers the server's real {@code Particle} is used at runtime (resolved by name, so differing enum
 * ordinals are irrelevant). On 1.8 the type is absent at runtime, so particle code paths must be
 * version-guarded (particles are a 1.9+ feature with no Bukkit API equivalent on 1.8).
 * <p>
 * Constants are the post-1.20.5 names used directly in source; older-named variants are resolved
 * reflectively at runtime by {@code VersionedParticle}.
 */
public enum Particle {

    DUST,
    SMOKE,
    HAPPY_VILLAGER,
    ENCHANTED_HIT,
    EXPLOSION,
    WITCH,
    FIREWORK,
    ENCHANT,
    PORTAL,
    HEART,
    CRIMSON_SPORE;

    /**
     * Stub of {@code Particle.DustOptions} (the colored-dust data object). Signatures mirror Bukkit's.
     */
    public static class DustOptions {

        private final Color color;
        private final float size;

        public DustOptions(Color color, float size) {
            this.color = color;
            this.size = size;
        }

        public Color getColor() {
            return color;
        }

        public float getSize() {
            return size;
        }
    }
}
