package io.github.thebusybiscuit.slimefun4.implementation.tasks.armor;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * This is a base class for any ArmorTask, it checks every online player
 * and handles any armor functionality.
 *
 * @author TheBusyBiscuit
 * @author martinbrom
 *
 * @see SlimefunArmorTask
 * @see RainbowArmorTask
 */
public abstract class AbstractArmorTask implements Runnable {

    @Override
    public final void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.isValid() || p.isDead()) {
                continue;
            }

            PlayerProfile.get(p, profile -> onPlayerTick(p, profile));
        }

        onTick();
    }

    /**
     * Schedules this {@link AbstractArmorTask} to run every {@code tickInterval} ticks
     *
     * @param plugin
     *            The {@link Slimefun}
     * @param tickInterval
     *            Delay between two "runs" of this task in ticks
     */
    public final void schedule(@Nonnull Slimefun plugin, long tickInterval) {
        Preconditions.checkNotNull(plugin, "The plugin instance cannot be null!");

        if (tickInterval < 1) {
            tickInterval = 1;
            plugin.getLogger().log(Level.WARNING, "The interval for {0} is misconfigured! Received {1}, expected at least 1.", new Object[] { getClass().getSimpleName(), tickInterval });
        }

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, tickInterval);
    }

    /**
     * Method to handle things related to the task itself.
     * Called once per tick (per schedule interval).
     */
    protected void onTick() {
        // Can be overridden as necessary.
    }

    /**
     * Method to handle behavior for player's armor as a whole.
     * It is called once per player.
     *
     * @param p
     *            The {@link Player} wearing the armor
     * @param profile
     *            The {@link Player}'s {@link PlayerProfile}
     */
    @ParametersAreNonnullByDefault
    protected abstract void onPlayerTick(Player p, PlayerProfile profile);
}
