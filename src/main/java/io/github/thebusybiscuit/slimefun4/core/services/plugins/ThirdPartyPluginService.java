package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.integrations.IntegrationsManager;

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
public class ThirdPartyPluginService extends IntegrationsManager {

    /**
     * This gets overridden if ExoticGarden is loaded
     */
    private Function<Block, Optional<ItemStack>> exoticGardenIntegration = b -> Optional.empty();

    private boolean isChestTerminalInstalled = false;
    private boolean isExoticGardenInstalled = false;

    /**
     * This initializes the {@link ThirdPartyPluginService}
     * 
     * @param plugin
     *            Our instance of {@link SlimefunPlugin}
     */
    public ThirdPartyPluginService(@Nonnull SlimefunPlugin plugin) {
        super(plugin);
    }

    @Override
    public void start() {
        super.start();

        plugin.getServer().getScheduler().runTask(plugin, () -> isChestTerminalInstalled = isPluginInstalled("ChestTerminal"));
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

    public Optional<ItemStack> harvestExoticGardenPlant(Block block) {
        return exoticGardenIntegration.apply(block);
    }

}
