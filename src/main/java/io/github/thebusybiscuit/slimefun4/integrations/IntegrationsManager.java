package io.github.thebusybiscuit.slimefun4.integrations;

import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import com.gmail.nossr50.events.fake.FakeBlockBreakEvent;

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

    protected final SlimefunPlugin plugin;

    private boolean isPlaceholderAPIInstalled = false;
    private boolean isWorldEditInstalled = false;
    private boolean isMcMMOInstalled = false;
    private boolean isClearLagInstalled = false;
    private boolean isItemsAdderInstalled = false;

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
     * This method initializes all third party integrations.
     */
    public void start() {
        // PlaceholderAPI hook to provide playerholders from Slimefun.
        if (isPluginInstalled("PlaceholderAPI")) {
            try {
                PlaceholderAPIIntegration hook = new PlaceholderAPIIntegration(plugin);
                hook.register();
                isPlaceholderAPIInstalled = true;
            } catch (Exception | LinkageError x) {
                String version = plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI").getDescription().getVersion();

                Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating PlaceholderAPI or Slimefun?");
                Slimefun.getLogger().log(Level.WARNING, x, () -> "Failed to hook into PlaceholderAPI v" + version);
            }
        }

        // WorldEdit Hook to clear Slimefun Data upon //set 0 //cut or any other equivalent
        if (isPluginInstalled("WorldEdit")) {
            try {
                Class.forName("com.sk89q.worldedit.extent.Extent");
                new WorldEditIntegration();
                isWorldEditInstalled = true;
            } catch (Exception | LinkageError x) {
                String version = plugin.getServer().getPluginManager().getPlugin("WorldEdit").getDescription().getVersion();

                Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating WorldEdit or Slimefun?");
                Slimefun.getLogger().log(Level.WARNING, x, () -> "Failed to hook into WorldEdit v" + version);
            }
        }

        // mcMMO Integration
        if (isPluginInstalled("mcMMO")) {
            try {
                new McMMOIntegration(plugin);
                isMcMMOInstalled = true;
            } catch (Exception | LinkageError x) {
                String version = plugin.getServer().getPluginManager().getPlugin("mcMMO").getDescription().getVersion();
                Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating mcMMO or Slimefun?");
                Slimefun.getLogger().log(Level.WARNING, x, () -> "Failed to hook into mcMMO v" + version);
            }
        }

        // ItemsAdder Integration
        if (isPluginInstalled("ItemsAdder")) {
            try {
                isItemsAdderInstalled = true;
            } catch (Exception | LinkageError x) {
                String version = plugin.getServer().getPluginManager().getPlugin("ItemsAdder").getDescription().getVersion();
                Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating ItemsAdder or Slimefun?");
                Slimefun.getLogger().log(Level.WARNING, x, () -> "Failed to hook into ItemsAdder v" + version);
            }
        }

        // ClearLag integration (to prevent display items from getting deleted)
        if (isPluginInstalled("ClearLag")) {
            try {
                new ClearLagIntegration(plugin);
                isClearLagInstalled = true;
            } catch (Exception | LinkageError x) {
                String version = plugin.getServer().getPluginManager().getPlugin("ClearLag").getDescription().getVersion();
                Slimefun.getLogger().log(Level.WARNING, "Maybe consider updating ClearLag or Slimefun?");
                Slimefun.getLogger().log(Level.WARNING, x, () -> "Failed to hook into ClearLag v" + version);
            }
        }
    }

    protected boolean isPluginInstalled(@Nonnull String hook) {
        if (plugin.getServer().getPluginManager().isPluginEnabled(hook)) {
            Slimefun.getLogger().log(Level.INFO, "Hooked into Plugin: {0}", hook);
            return true;
        } else {
            return false;
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

}
