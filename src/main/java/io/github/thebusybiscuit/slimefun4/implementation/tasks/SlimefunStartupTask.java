package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.logging.Level;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.implementation.listeners.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.World;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.TeleporterListener;
import io.github.thebusybiscuit.slimefun4.implementation.setup.PostSetup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

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
                SlimefunPlugin.logger().log(Level.SEVERE, x, () -> "An Error occurred while trying to load World \"" + world.getName() + "\" for Slimefun v" + SlimefunPlugin.getVersion());
            }
        }

        // Load/Unload Worlds, only after all plugins have started up. Fixes #2862
        new WorldListener(this.plugin);

        // Only load this Listener if the corresponding items are enabled
        if (isEnabled("ELEVATOR_PLATE", "GPS_ACTIVATION_DEVICE_SHARED", "GPS_ACTIVATION_DEVICE_PERSONAL")) {
            new TeleporterListener(plugin);
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
