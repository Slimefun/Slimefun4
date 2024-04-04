package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
 * This Service is responsible for automatically saving {@link Player} and {@link Block}
 * data.
 * 
 * @author TheBusyBiscuit
 *
 */
public class AutoSavingService {

    private int interval;
    private boolean savingPlayers;
    private boolean savingBlocks;

    /**
     * This method starts the {@link AutoSavingService} with the given interval.
     * 
     * @param plugin
     *            The current instance of Slimefun
     * @param interval
     *            The interval in which to run this task
     */
    public void start(@Nonnull Slimefun plugin, int interval) {
        this.interval = interval;

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
            if (auto) {
                Slimefun.logger().log(Level.INFO, "Auto-saved all player data for {0} player(s)!", players);
            } else {
                Slimefun.logger().log(Level.INFO, "Saved all player data for {0} player(s)!", players);
            }
            Slimefun.logger().log(Level.INFO, "Took {0}ms!", System.currentTimeMillis() - startTime);
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
        Set<BlockStorage> worlds = new HashSet<>();

        for (World world : Bukkit.getWorlds()) {
            BlockStorage storage = BlockStorage.getStorage(world);

            if (storage != null) {
                storage.computeChanges();

                if (storage.getChanges() > 0) {
                    worlds.add(storage);
                }
            }
        }

        if (!worlds.isEmpty()) {
            if (auto) {
                Slimefun.logger().log(Level.INFO, "Auto-saving block data... (Next auto-save: {0}m)", interval);
            } else {
                Slimefun.logger().log(Level.INFO, "Saving block data...");
            }

            for (BlockStorage storage : worlds) {
                storage.save();
            }
        }

        BlockStorage.saveChunks();
        Slimefun.logger().log(Level.INFO, "Saved all block data, took {0}ms!", System.currentTimeMillis() - startTime);

        this.savingBlocks = false;
        return true;
    }

}
