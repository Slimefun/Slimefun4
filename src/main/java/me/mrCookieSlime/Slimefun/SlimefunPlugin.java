package me.mrCookieSlime.Slimefun;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionManager;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.gps.GPSNetwork;
import io.github.thebusybiscuit.slimefun4.core.SlimefunRegistry;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.networks.NetworkManager;
import io.github.thebusybiscuit.slimefun4.core.services.*;
import io.github.thebusybiscuit.slimefun4.core.services.github.GitHubService;
import io.github.thebusybiscuit.slimefun4.core.services.plugins.ThirdPartyPluginService;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.AncientAltarListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BackpackListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.GrapplingHookListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SlimefunBowListener;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.TickerTask;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import org.bukkit.plugin.Plugin;

import java.util.Set;

/**
 * @author TheBusyBiscuit
 * @deprecated This class has been moved to {@link io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin}
 */
@Deprecated
public final class SlimefunPlugin {

    private SlimefunPlugin() {
    }

    public static Config getCfg() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getCfg();
    }

    public static Config getResearchCfg() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getResearchCfg();
    }

    public static Config getItemCfg() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getItemCfg();
    }

    public static GPSNetwork getGPSNetwork() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getGPSNetwork();
    }

    public static TickerTask getTicker() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getTickerTask();
    }

    public static String getVersion() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getVersion();
    }

    public static ProtectionManager getProtectionManager() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getProtectionManager();
    }

    public static LocalizationService getLocal() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getLocalization();
    }

    public static MinecraftRecipeService getMinecraftRecipes() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getMinecraftRecipeService();
    }

    public static CustomItemDataService getItemDataService() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getItemDataService();
    }

    public static CustomTextureService getItemTextureService() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getItemTextureService();
    }

    public static PermissionsService getPermissionsService() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getPermissionsService();
    }

    public static BlockDataService getBlockDataService() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getBlockDataService();
    }

    public static ThirdPartyPluginService getThirdPartySupportService() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getThirdPartySupportService();
    }

    public static PerWorldSettingsService getWorldSettingsService() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getWorldSettingsService();
    }

    public static GitHubService getGitHubService() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getGitHubService();
    }

    public static SlimefunRegistry getRegistry() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getRegistry();
    }

    public static NetworkManager getNetworkManager() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getNetworkManager();
    }

    public static AncientAltarListener getAncientAltarListener() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getAncientAltarListener();
    }

    public static GrapplingHookListener getGrapplingHookListener() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getGrapplingHookListener();
    }

    public static BackpackListener getBackpackListener() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getBackpackListener();
    }

    public static SlimefunBowListener getBowListener() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getBowListener();
    }

    public static Set<Plugin> getInstalledAddons() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getInstalledAddons();
    }

    public static SlimefunCommand getCommand() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getCommand();
    }

    public static MinecraftVersion getMinecraftVersion() {
        return io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin.getMinecraftVersion();
    }

    public static String getCSCoreLibVersion() {
        return CSCoreLib.getLib().getDescription().getVersion();
    }

}