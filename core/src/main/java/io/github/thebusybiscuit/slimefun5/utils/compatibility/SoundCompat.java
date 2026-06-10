package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Method;
import java.util.Optional;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XSound;

/**
 * Java-8 universal port: cross-version sound playback.
 * <p>
 * Sound enum names were renamed across 1.9/1.13, and {@code SoundCategory} plus the category-aware
 * {@code playSound} overloads only exist on 1.11+. This helper resolves a modern sound id to the
 * running server's {@link Sound} via {@link XSound}, then plays it through the category overload
 * reflectively when available (1.11+) and falls back to the plain {@code playSound(Location, Sound,
 * float, float)} overload (present since 1.8) otherwise. {@code org.bukkit.SoundCategory} is only ever
 * touched via reflection, so this class loads on every supported version.
 *
 * @author Slimefun (Java-8 port)
 */
public final class SoundCompat {

    private static final Class<?> BUKKIT_CATEGORY;
    private static final Method WORLD_PLAY_WITH_CATEGORY;
    private static final Method PLAYER_PLAY_WITH_CATEGORY;

    static {
        Class<?> category = null;
        Method worldMethod = null;
        Method playerMethod = null;

        try {
            category = Class.forName("org.bukkit.SoundCategory");
            worldMethod = World.class.getMethod("playSound", Location.class, Sound.class, category, float.class, float.class);
            playerMethod = Player.class.getMethod("playSound", Location.class, Sound.class, category, float.class, float.class);
        } catch (Throwable ignored) {
            // Pre-1.11 server: no SoundCategory / no category-aware overload. Fall back below.
            category = null;
            worldMethod = null;
            playerMethod = null;
        }

        BUKKIT_CATEGORY = category;
        WORLD_PLAY_WITH_CATEGORY = worldMethod;
        PLAYER_PLAY_WITH_CATEGORY = playerMethod;
    }

    private SoundCompat() {}

    @Nullable
    private static Sound resolve(@Nullable String soundId) {
        if (soundId == null) {
            return null;
        }

        Optional<XSound> sound = XSound.matchXSound(soundId);
        return sound.isPresent() ? sound.get().parseSound() : null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Nullable
    private static Object toBukkitCategory(@Nullable SoundCategory category) {
        if (BUKKIT_CATEGORY == null || category == null) {
            return null;
        }

        try {
            return Enum.valueOf((Class<? extends Enum>) BUKKIT_CATEGORY, category.name());
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * Plays a sound at a {@link Location} for everyone nearby, honouring the {@link SoundCategory} on
     * servers that support it.
     */
    public static void playAt(@Nullable Location loc, @Nullable String soundId, @Nullable SoundCategory category, float volume, float pitch) {
        if (loc == null || loc.getWorld() == null) {
            return;
        }

        Sound sound = resolve(soundId);

        if (sound == null) {
            return;
        }

        World world = loc.getWorld();
        Object bukkitCategory = toBukkitCategory(category);

        if (WORLD_PLAY_WITH_CATEGORY != null && bukkitCategory != null) {
            try {
                WORLD_PLAY_WITH_CATEGORY.invoke(world, loc, sound, bukkitCategory, volume, pitch);
                return;
            } catch (Throwable ignored) {
                // Fall through to the legacy overload.
            }
        }

        world.playSound(loc, sound, volume, pitch);
    }

    /**
     * Plays a sound to a single {@link Player}, honouring the {@link SoundCategory} on servers that
     * support it.
     */
    public static void playFor(@Nullable Player player, @Nullable Location loc, @Nullable String soundId, @Nullable SoundCategory category, float volume, float pitch) {
        if (player == null || loc == null) {
            return;
        }

        Sound sound = resolve(soundId);

        if (sound == null) {
            return;
        }

        Object bukkitCategory = toBukkitCategory(category);

        if (PLAYER_PLAY_WITH_CATEGORY != null && bukkitCategory != null) {
            try {
                PLAYER_PLAY_WITH_CATEGORY.invoke(player, loc, sound, bukkitCategory, volume, pitch);
                return;
            } catch (Throwable ignored) {
                // Fall through to the legacy overload.
            }
        }

        player.playSound(loc, sound, volume, pitch);
    }
}
