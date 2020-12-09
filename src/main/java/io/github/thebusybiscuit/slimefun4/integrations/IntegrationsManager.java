package io.github.thebusybiscuit.slimefun4.integrations;

import java.util.function.Consumer;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import com.gmail.nossr50.events.fake.FakeBlockBreakEvent;

import dev.lone.itemsadder.api.ItemsAdder;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
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
public class IntegrationsManager {

    /**
     * This is our instance of {@link SlimefunPlugin}.
     */
    protected final SlimefunPlugin plugin;

    /**
     * This boolean determines whether {@link #start()} was run.
     */
    private boolean isEnabled = false;

    // Soft dependencies
    private boolean isPlaceholderAPIInstalled = false;
    private boolean isWorldEditInstalled = false;
    private boolean isMcMMOInstalled = false;
    private boolean isClearLagInstalled = false;
    private boolean isItemsAdderInstalled = false;

    // Addon support
    private boolean isChestTerminalInstalled = false;
    private boolean isExoticGardenInstalled = false;

    /**
     * This initializes the {@link IntegrationsManager}
     * 
     * @param plugin
     *            Our instance of {@link SlimefunPlugin}
     */
    public IntegrationsManager(@Nonnull SlimefunPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This method returns whether the {@link IntegrationsManager} was enabled yet.
     * 
     * @return Whether this {@link IntegrationsManager} has been enabled already.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * This method initializes all third party integrations.
     */
    public void start() {
        if (isEnabled) {
            // Prevent double-registration
            throw new UnsupportedOperationException("All integrations have already been loaded.");
        } else {
            isEnabled = true;
        }

        // PlaceholderAPI hook to provide playerholders from Slimefun.
        load("PlaceholderAPI", integration -> {
            new PlaceholderAPIIntegration(plugin).register();
            isPlaceholderAPIInstalled = true;
        });

        // WorldEdit Hook to clear Slimefun Data upon //set 0 //cut or any other equivalent
        load("WorldEdit", integration -> {
            new WorldEditIntegration().register();
            isWorldEditInstalled = true;
        });

        // mcMMO Integration
        load("mcMMO", integration -> {
            new McMMOIntegration(plugin).register();
            isMcMMOInstalled = true;
        });

        // ClearLag integration (to prevent display items from getting deleted)
        load("ClearLag", integration -> {
            new ClearLagIntegration(plugin).register();
            isClearLagInstalled = true;
        });

        // ItemsAdder Integration (custom blocks)
        load("ItemsAdder", integration -> isItemsAdderInstalled = true);

        // Load any integrations which aren't dependencies (loadBefore)
        plugin.getServer().getScheduler().runTask(plugin, this::onServerStart);
    }

    /**
     * This method is called when the {@link Server} has finished loading its plugins.
     */
    private void onServerStart() {
        isChestTerminalInstalled = isAddonInstalled("ChestTerminal");
        isExoticGardenInstalled = isAddonInstalled("ExoticGarden");
    }

    private boolean isAddonInstalled(@Nonnull String addon) {
        if (plugin.getServer().getPluginManager().isPluginEnabled(addon)) {
            Slimefun.getLogger().log(Level.INFO, "Hooked into Slimefun Addon: {0}", addon);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method loads an integration with a {@link Plugin} of the specified name.
     * If that {@link Plugin} is installed and enabled, the provided callback will be run.
     * 
     * @param pluginName
     *            The name of this {@link Plugin}
     * @param consumer
     *            The callback to run if that {@link Plugin} is installed and enabled
     */
    private void load(@Nonnull String pluginName, @Nonnull Consumer<Plugin> consumer) {
        Plugin integration = plugin.getServer().getPluginManager().getPlugin(pluginName);

        if (integration != null && integration.isEnabled()) {
            String version = integration.getDescription().getVersion();
            Slimefun.getLogger().log(Level.INFO, "Hooked into Plugin: {0} v{1}", new Object[] { pluginName, version });

            try {
                // Run our callback
                consumer.accept(integration);
            } catch (Exception | LinkageError x) {
                Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating {0} or Slimefun?", pluginName);
                Slimefun.getLogger().log(Level.WARNING, x, () -> "Failed to hook into " + pluginName + " v" + version);
            }
        }
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
        // This can be changed to "FakeEvent" in a later version
        return isMcMMOInstalled && event instanceof FakeBlockBreakEvent;
    }

    /**
     * This checks if one of our third party integrations has placed a custom
     * {@link Block} at this {@link Location}.
     * 
     * @param block
     *            The {@link Block} to check
     * 
     * @return Whether a different custom {@link Block} exists at that {@link Location}
     */
    @SuppressWarnings("deprecation")
    public boolean isCustomBlock(@Nonnull Block block) {
        return isItemsAdderInstalled && ItemsAdder.isCustomBlock(block);
    }

    public boolean isPlaceholderAPIInstalled() {
        return isPlaceholderAPIInstalled;
    }

    public boolean isWorldEditInstalled() {
        return isWorldEditInstalled;
    }

    public boolean isMcMMOInstalled() {
        return isMcMMOInstalled;
    }

    public boolean isClearLagInstalled() {
        return isClearLagInstalled;
    }

    public boolean isItemsAdderInstalled() {
        return isItemsAdderInstalled;
    }

    public boolean isChestTerminalInstalled() {
        return isChestTerminalInstalled;
    }

    public boolean isExoticGardenInstalled() {
        return isExoticGardenInstalled;
    }

}
