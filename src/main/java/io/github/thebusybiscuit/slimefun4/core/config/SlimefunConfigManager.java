package io.github.thebusybiscuit.slimefun4.core.config;

import java.util.function.Supplier;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This class manages our {@link Config}, it caches values for faster access
 * and provides means to reload it.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class SlimefunConfigManager {

    /**
     * Our {@link SlimefunPlugin} instance
     */
    private final SlimefunPlugin plugin;

    /**
     * Our {@link Plugin} {@link Config} (config.yml)
     */
    private final Config pluginConfig;

    /**
     * Our {@link SlimefunItem} {@link Config} (Items.yml)
     */
    private final Config itemsConfig;

    /**
     * Our {@link Research} {@link Config} (Researches.yml)
     */
    private final Config researchesConfig;

    /**
     * This flag marks whether we require a {@link Server} restart to
     * apply the latest changes.
     */
    private boolean isRestartRequired = false;

    // Various booleans we want to cache instead of re-parsing everytime
    private boolean enableBackwardsCompatibility;
    private boolean enableResearching;
    private boolean enableFreeCreativeResearches;
    private boolean enableResearchFireworks;
    private boolean enableDuplicateBlockLogging;
    private boolean enableVanillaRecipesInGuide;
    private boolean enableGuideOnJoin;
    private boolean enableUpdater;
    private boolean enableActionbarTalismanMessages;
    private boolean enableCommandItemDropExcess;

    /**
     * This constructs a new {@link SlimefunConfigManager} for the given instance
     * of {@link SlimefunPlugin}.
     * 
     * @param plugin
     *            The {@link SlimefunPlugin} instance
     */
    public SlimefunConfigManager(@Nonnull SlimefunPlugin plugin) {
        Validate.notNull(plugin, "The Plugin instance cannot be null");

        this.plugin = plugin;
        pluginConfig = getConfig(plugin, "config", () -> new Config(plugin));
        itemsConfig = getConfig(plugin, "Items", () -> new Config(plugin, "Items.yml"));
        researchesConfig = getConfig(plugin, "Researches", () -> new Config(plugin, "Researches.yml"));
    }

    @Nullable
    @ParametersAreNonnullByDefault
    private Config getConfig(SlimefunPlugin plugin, String name, Supplier<Config> supplier) {
        try {
            return supplier.get();
        } catch (Exception x) {
            plugin.getLogger().log(Level.SEVERE, x, () -> "An Exception was thrown while loading the config file \"" + name + ".yml\" for Slimefun v" + plugin.getDescription().getVersion());
            return null;
        }
    }

    /**
     * This method (re)loads all relevant config values into our cache.
     * <p>
     * <strong>Note that this method is not guaranteed to reload all settings.</strong>
     * 
     * @return Whether the reloading was successful and completed without any errors
     */
    public boolean reload() {
        boolean isSuccessful = true;

        try {
            pluginConfig.reload();
            itemsConfig.reload();
            researchesConfig.reload();

            researchesConfig.setDefaultValue("enable-researching", true);

            enableBackwardsCompatibility = pluginConfig.getBoolean("options.backwards-compatibility");
            enableResearching = researchesConfig.getBoolean("enable-researching");
            enableFreeCreativeResearches = pluginConfig.getBoolean("researches.free-in-creative-mode");
            enableResearchFireworks = pluginConfig.getBoolean("researches.enable-fireworks");
            enableDuplicateBlockLogging = pluginConfig.getBoolean("options.log-duplicate-block-entries");
            enableVanillaRecipesInGuide = pluginConfig.getBoolean("guide.show-vanilla-recipes");
            enableGuideOnJoin = pluginConfig.getBoolean("guide.receive-on-first-join");
            enableUpdater = pluginConfig.getBoolean("options.auto-update");
            enableActionbarTalismanMessages = pluginConfig.getBoolean("talismans.use-actionbar");
            enableCommandItemDropExcess = pluginConfig.getBoolean("options.drop-excess-sf-give-items");
        } catch (Exception x) {
            plugin.getLogger().log(Level.SEVERE, x, () -> "An Exception was caught while (re)loading the config files for Slimefun v" + plugin.getDescription().getVersion());
            isSuccessful = false;
        }

        // Reload Research costs
        for (Research research : SlimefunPlugin.getRegistry().getResearches()) {
            try {
                NamespacedKey key = research.getKey();
                int cost = researchesConfig.getInt(key.getNamespace() + '.' + key.getKey() + ".cost");
                research.setCost(cost);

                if (!researchesConfig.getBoolean(key.getNamespace() + '.' + key.getKey() + ".enabled")) {
                    // Disabling a research requires a restart (for now)
                    isRestartRequired = true;
                }
            } catch (Exception x) {
                plugin.getLogger().log(Level.SEVERE, x, () -> "Something went wrong while trying to update the cost of a research: " + research);
                isSuccessful = false;
            }
        }

        for (SlimefunItem item : SlimefunPlugin.getRegistry().getAllSlimefunItems()) {
            if (item.isDisabled() != !itemsConfig.getBoolean(item.getId() + ".enabled")) {
                // Enabling/Disabling an item requires a restart (for now)
                isRestartRequired = true;
            }

            // Reload Item Settings
            try {
                for (ItemSetting<?> setting : item.getItemSettings()) {
                    // Make sure that the setting was loaded properly
                    if (!setting.reload()) {
                        isSuccessful = false;
                    }
                }
            } catch (Exception x) {
                item.error("Something went wrong while updating the settings for this item!", x);
                isSuccessful = false;
            }

            // Reload permissions
            try {
                SlimefunPlugin.getPermissionsService().update(item, false);
            } catch (Exception x) {
                item.error("Something went wrong while updating the permission node for this item!", x);
                isSuccessful = false;
            }
        }

        return isSuccessful;
    }

    /**
     * This method returns whether the {@link Server} should be restarted to apply the latest changes.
     * 
     * @return Whether a restart is required.
     */
    public boolean isRestartRequired() {
        return isRestartRequired;
    }

    @Nonnull
    public Config getPluginConfig() {
        return pluginConfig;
    }

    @Nonnull
    public Config getItemsConfig() {
        return itemsConfig;
    }

    @Nonnull
    public Config getResearchConfig() {
        return researchesConfig;
    }

    /**
     * This method saves all our {@link Config} files.
     */
    public void saveFiles() {
        pluginConfig.save();
        itemsConfig.save();
        researchesConfig.save();
    }

    /**
     * This method returns whether backwards-compatibility is enabled.
     * Backwards compatibility allows Slimefun to recognize items from older versions but comes
     * at a huge performance cost.
     * 
     * @return Whether backwards compatibility is enabled
     */
    public boolean isBackwardsCompatible() {
        return enableBackwardsCompatibility;
    }

    /**
     * This method sets the status of backwards compatibility.
     * Backwards compatibility allows Slimefun to recognize items from older versions but comes
     * at a huge performance cost.
     * 
     * @param compatible
     *            Whether backwards compatibility should be enabled
     */
    public void setBackwardsCompatible(boolean compatible) {
        enableBackwardsCompatibility = compatible;
    }

    public void setResearchingEnabled(boolean enabled) {
        enableResearching = enabled;
    }

    public boolean isResearchingEnabled() {
        return enableResearching;
    }

    public void setFreeCreativeResearchingEnabled(boolean enabled) {
        enableFreeCreativeResearches = enabled;
    }

    public boolean isFreeCreativeResearchingEnabled() {
        return enableFreeCreativeResearches;
    }

    public boolean isResearchFireworkEnabled() {
        return enableResearchFireworks;
    }

    public boolean isDuplicateBlockLoggingEnabled() {
        return enableDuplicateBlockLogging;
    }

    public boolean isVanillaRecipeShown() {
        return enableVanillaRecipesInGuide;
    }

    public boolean isSlimefunGuideGivenOnJoin() {
        return enableGuideOnJoin;
    }

    public boolean isUpdaterEnabled() {
        return enableUpdater;
    }

    public boolean isTalismanMessageInActionbar() {
        return enableActionbarTalismanMessages;
    }

    public boolean isExcessCommandItemsDroppingEnabled() {
        return enableCommandItemDropExcess;
    }

}
