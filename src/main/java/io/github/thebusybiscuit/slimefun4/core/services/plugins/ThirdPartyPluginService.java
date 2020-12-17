package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

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
 * @deprecated Renamed to {@link IntegrationsManager}
 * 
 * @see SlimefunPlugin
 *
 */
@Deprecated
public class ThirdPartyPluginService extends IntegrationsManager {

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
        super(plugin);
    }

    @Deprecated
    public void loadExoticGarden(Plugin plugin, Function<Block, Optional<ItemStack>> method) {
        // TODO: Move this method to IntegrationsManager and think of a better way to handle this
        if (plugin.getName().equals("ExoticGarden")) {
            exoticGardenIntegration = method;
        }
    }

    @Deprecated
    public Optional<ItemStack> harvestExoticGardenPlant(Block block) {
        return exoticGardenIntegration.apply(block);
    }

}
