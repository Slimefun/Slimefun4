package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.categories.FlexCategory;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This Service holds all interactions and hooks with third-party {@link Plugin Plugins}
 * that are not necessarily a dependency or a {@link SlimefunAddon}.
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
    private boolean isChestTerminalInstalled = false;
    private boolean isEmeraldEnchantsInstalled = false;
    private boolean isCoreProtectInstalled = false;
    private boolean isPlaceholderAPIInstalled = false;

    // Overridden if ExoticGarden is loaded
    private Function<Block, Optional<ItemStack>> exoticGardenIntegration = b -> Optional.empty();

    public ThirdPartyPluginService(SlimefunPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (isPluginInstalled("PlaceholderAPI")) {
            try {
                PlaceholderAPIHook hook = new PlaceholderAPIHook(plugin);
                hook.register();
                isPlaceholderAPIInstalled = true;
            }
            catch (Exception | LinkageError x) {
                String version = plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI").getDescription().getVersion();

                Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating PlaceholderAPI or Slimefun?");
                Slimefun.getLogger().log(Level.WARNING, x, () -> "Failed to hook into PlaceholderAPI v" + version);
            }
        }

        if (isPluginInstalled("EmeraldEnchants")) {
            isEmeraldEnchantsInstalled = true;
            Plugin emeraldEnchants = plugin.getServer().getPluginManager().getPlugin("EmeraldEnchants");
            FlexCategory category = new EmeraldEnchantsCategory(new NamespacedKey(emeraldEnchants, "enchantment_guide"));
            category.register();
        }

        // WorldEdit Hook to clear Slimefun Data upon //set 0 //cut or any other equivalent
        if (isPluginInstalled("WorldEdit")) {
            try {
                Class.forName("com.sk89q.worldedit.extent.Extent");
                new WorldEditHook();
            }
            catch (Exception | LinkageError x) {
                String version = plugin.getServer().getPluginManager().getPlugin("WorldEdit").getDescription().getVersion();

                Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating WorldEdit or Slimefun?");
                Slimefun.getLogger().log(Level.WARNING, x, () -> "Failed to hook into WorldEdit v" + version);
            }
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

            isChestTerminalInstalled = isPluginInstalled("ChestTerminal");
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

    public void loadExoticGarden(Plugin plugin, Function<Block, Optional<ItemStack>> method) {
        if (plugin.getName().equals("ExoticGarden")) {
            isExoticGardenInstalled = true;
            exoticGardenIntegration = method;
        }
    }

    public boolean isExoticGardenInstalled() {
        return isExoticGardenInstalled;
    }

    public boolean isChestTerminalInstalled() {
        return isChestTerminalInstalled;
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

    public Optional<ItemStack> harvestExoticGardenPlant(Block block) {
        return exoticGardenIntegration.apply(block);
    }

}
