package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;

import io.github.thebusybiscuit.slimefun4.implementation.listeners.ButcherAndroidListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.CoolerListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.NetworkListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SeismicAxeListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.TeleporterListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.VampireBladeListener;
import io.github.thebusybiscuit.slimefun4.implementation.setup.PostSetup;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
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
    public SlimefunStartupTask(SlimefunPlugin plugin, Runnable runnable) {
        this.plugin = plugin;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runnable.run();

        // Load all items
        PostSetup.loadItems();

        // Load all worlds
        for (World world : Bukkit.getWorlds()) {
            new BlockStorage(world);
        }

        // Load all listeners that depend on items to be enabled

        if (isEnabled("ANCIENT_ALTAR")) {
            SlimefunPlugin.getAncientAltarListener().load(plugin);
        }

        if (isEnabled("GRAPPLING_HOOK")) {
            SlimefunPlugin.getGrapplingHookListener().load(plugin);
        }

        if (isEnabled("BLADE_OF_VAMPIRES")) {
            new VampireBladeListener(plugin);
        }

        if (isEnabled("COOLER")) {
            new CoolerListener(plugin);
        }

        if (isEnabled("SEISMIC_AXE")) {
            new SeismicAxeListener(plugin);
        }

        if (isEnabled("ELEVATOR_PLATE", "GPS_ACTIVATION_DEVICE_SHARED", "GPS_ACTIVATION_DEVICE_PERSONAL")) {
            new TeleporterListener(plugin);
        }

        if (isEnabled("PROGRAMMABLE_ANDROID_BUTCHER", "PROGRAMMABLE_ANDROID_2_BUTCHER", "PROGRAMMABLE_ANDROID_3_BUTCHER")) {
            new ButcherAndroidListener(plugin);
        }

        if (isEnabled("ENERGY_REGULATOR", "CARGO_MANAGER")) {
            new NetworkListener(plugin);
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
