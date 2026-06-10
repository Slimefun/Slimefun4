package io.github.thebusybiscuit.slimefun5.utils.compatibility;

/**
 * Java-8 universal port: a version-independent stand-in for {@code org.bukkit.SoundCategory}
 * (introduced in MC 1.11). The constants mirror Bukkit's exactly so call sites only need to swap
 * their import. {@link io.github.thebusybiscuit.slimefun5.utils.compatibility.SoundCompat} maps these
 * to the real Bukkit {@code SoundCategory} reflectively on 1.11+, and simply ignores the category on
 * older servers (which have no sound categories).
 *
 * @author Slimefun (Java-8 port)
 */
public enum SoundCategory {
    MASTER,
    MUSIC,
    RECORDS,
    WEATHER,
    BLOCKS,
    HOSTILE,
    NEUTRAL,
    PLAYERS,
    AMBIENT,
    VOICE;
}
