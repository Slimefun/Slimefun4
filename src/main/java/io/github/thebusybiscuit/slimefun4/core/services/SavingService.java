package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Iterator;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.debug.Debug;
import io.github.thebusybiscuit.slimefun4.core.debug.TestCase;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * This Service is responsible for saving {@link Player} and {@link Block}
 * data.
 * 
 * @author TheBusyBiscuit
 * @author JustAHuman
 */
public class SavingService {

    private int interval;
    private long lastPlayerSave;
    private long lastBlockSave;
    private boolean startedAutoSave;
    private boolean savingPlayers;
    private boolean savingBlocks;

    /**
     * This method starts a {@link SavingService} task with the given interval.
     * 
     * @param plugin
     *            The current instance of Slimefun
     * @param interval
     *            The interval in which to run this task
     */
    public void startAutoSave(@Nonnull Slimefun plugin, int interval) {
        if (this.startedAutoSave) {
            // TODO: handle this
            return;
        }

        this.interval = interval;
        this.startedAutoSave = true;

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> saveAllPlayers(true), 2000L, interval * 60L * 20L);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> saveAllBlocks(true), 2000L, interval * 60L * 20L);
    }

    /**
     * This method saves every {@link PlayerProfile} in memory and removes profiles
     * that were marked for deletion.
     */
    public boolean saveAllPlayers(boolean auto) {
        if (this.savingPlayers) {
            return false;
        }

        this.savingPlayers = true;
        long startTime = System.currentTimeMillis();
        Iterator<PlayerProfile> iterator = PlayerProfile.iterator();
        int players = 0;

        Debug.log(TestCase.PLAYER_PROFILE_DATA, "Saving all player data");

        while (iterator.hasNext()) {
            PlayerProfile profile = iterator.next();

            if (profile.isDirty()) {
                players++;
                profile.save();

                Debug.log(TestCase.PLAYER_PROFILE_DATA, "Saved data for {} ({})",
                    profile.getPlayer() != null ? profile.getPlayer().getName() : "Unknown", profile.getUUID()
                );
            }

            // Remove the PlayerProfile from memory if the player has left the server (marked from removal)
            // and they're still not on the server
            // At this point, we've already saved their profile so we can safely remove it
            // without worry for having a data sync issue (e.g. data is changed but then we try to re-load older data)
            if (profile.isMarkedForDeletion() && profile.getPlayer() == null) {
                iterator.remove();

                Debug.log(TestCase.PLAYER_PROFILE_DATA, "Removed data from memory for {}",
                    profile.getUUID()
                );
            }
        }

        if (players > 0) {
            long endTime = System.currentTimeMillis();
            if (auto) {
                this.lastPlayerSave = endTime;
                Slimefun.logger().log(Level.INFO, "Auto-saved all player data for {0} player(s)! (Next auto-save: {1}m)", new Object[] { players, this.interval });
            } else {
                long nextAutoSave = (this.interval * 60L) - ((endTime - this.lastPlayerSave) / 1000L);
                Slimefun.logger().log(Level.INFO, "Saved all player data for {0} player(s)! (Next auto-save: {1}m {2}s)", new Object[] { players, nextAutoSave / 60, nextAutoSave % 60 });
            }
            Slimefun.logger().log(Level.INFO, "Took {0}ms!", endTime - startTime);
        }

        this.savingPlayers = false;
        return true;
    }

    /**
     * This method saves the data of every {@link Block} marked dirty by {@link BlockStorage}.
     */
    public boolean saveAllBlocks(boolean auto) {
        if (this.savingBlocks) {
            return false;
        }

        this.savingBlocks = true;
        long startTime = System.currentTimeMillis();
        int savedChanges = 0;

        for (World world : Bukkit.getWorlds()) {
            BlockStorage storage = BlockStorage.getStorage(world);
            if (storage == null) {
                continue;
            }

            savedChanges += storage.saveChanges();
        }
        BlockStorage.saveChunks();

        long endTime = System.currentTimeMillis();
        if (auto) {
            Slimefun.logger().log(Level.INFO, "Auto-saved all block data from {0} changes! (Next auto-save: {1}m)", new Object[] { savedChanges, this.interval });
        } else {
            long nextAutoSave = (this.interval * 60L) - ((endTime - this.lastBlockSave) / 1000L);
            Slimefun.logger().log(Level.INFO, "Saved all block data from {0} changes! (Next auto-save: {1}m {2}s)", new Object[] { savedChanges, nextAutoSave / 60, nextAutoSave % 60 });
        }
        Slimefun.logger().log(Level.INFO, "Took {0}ms!", new Object[] { endTime - startTime });

        this.lastBlockSave = endTime;
        this.savingBlocks = false;
        return true;
    }

}
