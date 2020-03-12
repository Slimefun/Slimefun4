package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This Service holds all interactions and hooks with third-party {@link Plugin Plugins}
 * that are not a dependency or a {@link SlimefunAddon}.
 * 
 * Integration with these plugins happens inside Slimefun itself.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunPlugin
 *
 */
public class ThirdPartyPluginService {

    private final SlimefunPlugin plugin;

    private boolean isExoticGardenInstalled = false;
    private boolean isEmeraldEnchantsInstalled = false;
    private boolean isCoreProtectInstalled = false;
    private boolean isPlaceholderAPIInstalled = false;

    public ThirdPartyPluginService(SlimefunPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (isPluginInstalled("PlaceholderAPI")) {
            isPlaceholderAPIInstalled = true;
            new PlaceholderAPIHook().register();
        }

        /*
         * These Items are not marked as soft-dependencies and
         * therefore need to be loaded after the Server has finished
         * loading all plugins
         */
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (isPluginInstalled("ClearLag")) {
                new ClearLagHook(plugin);
            }

            isExoticGardenInstalled = isPluginInstalled("ExoticGarden");
            isEmeraldEnchantsInstalled = isPluginInstalled("EmeraldEnchants");

            SlimefunPlugin.getNetworkManager().setChestTerminalInstalled(isPluginInstalled("ChestTerminal"));

            // WorldEdit Hook to clear Slimefun Data upon //set 0 //cut or any other equivalent
            if (isPluginInstalled("WorldEdit")) {
                try {
                    Class.forName("com.sk89q.worldedit.extent.Extent");
                    new WorldEditHook();
                }
                catch (Exception x) {
                    Slimefun.getLogger().log(Level.WARNING, "Failed to hook into WorldEdit!");
                    Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating WorldEdit or Slimefun?");
                }
            }
        });
    }

    private boolean isPluginInstalled(String hook) {
        if (plugin.getServer().getPluginManager().isPluginEnabled(hook)) {
            Slimefun.getLogger().log(Level.INFO, "Hooked into Plugin: {0}", hook);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isExoticGardenInstalled() {
        return isExoticGardenInstalled;
    }

    public boolean isEmeraldEnchantsInstalled() {
        return isEmeraldEnchantsInstalled;
    }

    public boolean isCoreProtectInstalled() {
        return isCoreProtectInstalled;
    }

    public boolean isPlaceholderAPIInstalled() {
        return isPlaceholderAPIInstalled;
    }

}
