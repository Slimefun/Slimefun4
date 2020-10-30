package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.nossr50.events.fake.FakeBlockBreakEvent;

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

    private boolean initialized = false;
    private boolean isExoticGardenInstalled = false;
    private boolean isChestTerminalInstalled = false;
    private boolean isEmeraldEnchantsInstalled = false;
    private boolean isMcMMOInstalled = false;

    /**
     * This gets overridden if ExoticGarden is loaded
     */
    private Function<Block, Optional<ItemStack>> exoticGardenIntegration = b -> Optional.empty();

    /**
     * This initializes the {@link ThirdPartyPluginService}
     * 
     * @param plugin
     *            Our instance of {@link SlimefunPlugin}
     */
    public ThirdPartyPluginService(@Nonnull SlimefunPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This method initializes all third party integrations.
     */
    public void start() {
        if (initialized) {
            throw new UnsupportedOperationException("Third Party Integrations have already been initialized!");
        }

        initialized = true;

        if (isPluginInstalled("PlaceholderAPI")) {
            try {
                PlaceholderAPIIntegration hook = new PlaceholderAPIIntegration(plugin);
                hook.register();
            } catch (Exception | LinkageError x) {
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
                new WorldEditIntegration();
            } catch (Exception | LinkageError x) {
                String version = plugin.getServer().getPluginManager().getPlugin("WorldEdit").getDescription().getVersion();

                Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating WorldEdit or Slimefun?");
                Slimefun.getLogger().log(Level.WARNING, x, () -> "Failed to hook into WorldEdit v" + version);
            }
        }

        // mcMMO Integration
        if (isPluginInstalled("mcMMO")) {
            try {
                // This makes sure that the FakeEvent interface is present.
                // Class.forName("com.gmail.nossr50.events.fake.FakeEvent");

                new McMMOIntegration(plugin);
                isMcMMOInstalled = true;
            } catch (Exception | LinkageError x) {
                String version = plugin.getServer().getPluginManager().getPlugin("mcMMO").getDescription().getVersion();
                Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating mcMMO or Slimefun?");
                Slimefun.getLogger().log(Level.WARNING, x, () -> "Failed to hook into mcMMO v" + version);
            }
        }

        /*
         * These Items are not marked as soft-dependencies and
         * therefore need to be loaded after the Server has finished
         * loading all plugins
         */
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (isPluginInstalled("ClearLag")) {
                new ClearLagIntegration(plugin);
            }

            isChestTerminalInstalled = isPluginInstalled("ChestTerminal");
        });
    }

    private boolean isPluginInstalled(@Nonnull String hook) {
        if (plugin.getServer().getPluginManager().isPluginEnabled(hook)) {
            Slimefun.getLogger().log(Level.INFO, "Hooked into Plugin: {0}", hook);
            return true;
        } else {
            return false;
        }
    }

    @ParametersAreNonnullByDefault
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

    public Optional<ItemStack> harvestExoticGardenPlant(Block block) {
        return exoticGardenIntegration.apply(block);
    }

    /**
     * This checks if one of our third party integrations faked an {@link Event}.
     * Faked {@link Event Events} should be ignored in our logic.
     * 
     * @param event
     *            The {@link Event} to test
     * 
     * @return Whether this is a fake event
     */
    public boolean isEventFaked(@Nonnull Event event) {
        // TODO: Change this to FakeEvent once the new mcMMO build was released
        return isMcMMOInstalled && event instanceof FakeBlockBreakEvent;
    }

}
