package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.World;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.ButcherAndroidListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.NetworkListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.TeleporterListener;
import io.github.thebusybiscuit.slimefun4.implementation.setup.PostSetup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This Task initializes all items, some listeners and various other stuff.
 * This has been moved to its own class to make timings log easier to read, so
 * they say "SlimefunStartupTask" instead of "Slimefun:lambda:123456789".
 * 
 * @author TheBusyBiscuit
 *
 */
public class SlimefunStartupTask implements Runnable {

    private final SlimefunPlugin plugin;
    private final Runnable runnable;

    /**
     * This initializes our {@link SlimefunStartupTask} for the given {@link SlimefunPlugin}.
     * 
     * @param plugin
     *            The main instance of our {@link SlimefunPlugin}
     * @param runnable
     *            A {@link Runnable} containing additional operations that need to be run
     */
    public SlimefunStartupTask(@Nonnull SlimefunPlugin plugin, @Nonnull Runnable runnable) {
        this.plugin = plugin;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runnable.run();

        // Load all items
        PostSetup.loadItems();

        // Load all worlds
        SlimefunPlugin.getWorldSettingsService().load(Bukkit.getWorlds());

        for (World world : Bukkit.getWorlds()) {
            try {
                new BlockStorage(world);
            } catch (Exception x) {
                Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Error occurred while trying to load World \"" + world.getName() + "\" for Slimefun v" + SlimefunPlugin.getVersion());
            }
        }

        // Load all listeners that depend on items to be enabled

        if (isEnabled("ELEVATOR_PLATE", "GPS_ACTIVATION_DEVICE_SHARED", "GPS_ACTIVATION_DEVICE_PERSONAL")) {
            new TeleporterListener(plugin);
        }

        if (isEnabled("PROGRAMMABLE_ANDROID_BUTCHER", "PROGRAMMABLE_ANDROID_2_BUTCHER", "PROGRAMMABLE_ANDROID_3_BUTCHER")) {
            new ButcherAndroidListener(plugin);
        }

        if (isEnabled("ENERGY_REGULATOR", "CARGO_MANAGER")) {
            new NetworkListener(plugin, SlimefunPlugin.getNetworkManager());
        }
    }

    private boolean isEnabled(String... itemIds) {
        for (String id : itemIds) {
            SlimefunItem item = SlimefunItem.getByID(id);

            if (item != null && !item.isDisabled()) {
                return true;
            }
        }
        return false;
    }

}
