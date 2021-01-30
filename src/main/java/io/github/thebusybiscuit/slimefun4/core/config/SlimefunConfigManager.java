package io.github.thebusybiscuit.slimefun4.core.config;

import java.util.Objects;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This class manages our {@link Config}, it caches values for faster access
 * and provides means to reload it (currently very limited)
 * 
 * @author TheBusyBiscuit
 *
 */
public class SlimefunConfigManager {

    // Our main config (config.yml)
    private final Config generalConfig;

    // Our items config (Items.yml)
    private final Config itemsConfig;

    // Our research config (Researches.yml)
    private final Config researchesConfig;

    private boolean isBackwardsCompatibilityEnabled;
    private boolean isResearchingEnabled;
    private boolean isResearchingFreeInCreativeMode;
    private boolean isResearchFireworkEnabled;
    private boolean isDuplicateBlockLoggingEnabled;
    private boolean isVanillaRecipeShowingEnabled;
    private boolean isSlimefunGuideGivenOnJoin;
    private boolean isUpdaterEnabled;

    public SlimefunConfigManager(@Nonnull SlimefunPlugin plugin) {
        Validate.notNull(plugin, "The Plugin instance cannot be null");

        generalConfig = new Config(plugin);
        itemsConfig = new Config(plugin, "Items.yml");
        researchesConfig = new Config(plugin, "Researches.yml");
    }

    public void update() {
        researchesConfig.setDefaultValue("enable-researching", true);

        isBackwardsCompatibilityEnabled = generalConfig.getBoolean("options.backwards-compatibility");
        isResearchingEnabled = researchesConfig.getBoolean("enable-researching");
        isResearchingFreeInCreativeMode = generalConfig.getBoolean("researches.free-in-creative-mode");
        isResearchFireworkEnabled = generalConfig.getBoolean("researches.enable-fireworks");
        isDuplicateBlockLoggingEnabled = generalConfig.getBoolean("options.log-duplicate-block-entries");
        isVanillaRecipeShowingEnabled = generalConfig.getBoolean("guide.show-vanilla-recipes");
        isSlimefunGuideGivenOnJoin = generalConfig.getBoolean("guide.receive-on-first-join");
        isUpdaterEnabled = generalConfig.getBoolean("options.auto-update");
    }

    @Nonnull
    public DiffMap reload() {
        DiffMap diff = new DiffMap();

        // Reload main config
        generalConfig.reload();
        researchesConfig.reload();
        update();

        // Reload Research costs
        for (Research research : SlimefunPlugin.getRegistry().getResearches()) {
            try {
                NamespacedKey key = research.getKey();
                int cost = researchesConfig.getInt(key.getNamespace() + '.' + key.getKey() + ".cost");
                diff.addResearchCostChange(research, research.getCost(), cost);
                research.setCost(cost);
            } catch (Exception x) {
                SlimefunPlugin.logger().log(Level.SEVERE, x, () -> "Something went wrong while trying to update the cost of a research: " + research);
            }
        }

        for (SlimefunItem item : SlimefunPlugin.getRegistry().getAllSlimefunItems()) {
            // Reload Item Settings
            for (ItemSetting<?> setting : item.getItemSettings()) {
                Object prev = setting.getValue();

                if (setting.load(item)) {
                    diff.addItemSettingsChange(setting, prev, setting.getValue());
                }
            }

            // Reload permissions
            String previousPermission = SlimefunPlugin.getPermissionsService().getPermission(item).orElse(null);
            SlimefunPlugin.getPermissionsService().update(item, false);
            String newPermission = SlimefunPlugin.getPermissionsService().getPermission(item).orElse(null);

            if (!Objects.equals(previousPermission, newPermission)) {
                diff.addItemPermissionChange(item, previousPermission, newPermission);
            }
        }

        return diff;
    }

    @Nonnull
    public Config getPluginConfig() {
        return generalConfig;
    }

    @Nonnull
    public Config getItemsConfig() {
        return itemsConfig;
    }

    @Nonnull
    public Config getResearchConfig() {
        return researchesConfig;
    }

    public void saveFiles() {
        generalConfig.save();
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
        return isBackwardsCompatibilityEnabled;
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
        isBackwardsCompatibilityEnabled = compatible;
    }

    public void setResearchingEnabled(boolean enabled) {
        isResearchingEnabled = enabled;
    }

    public boolean isResearchingEnabled() {
        return isResearchingEnabled;
    }

    public void setFreeCreativeResearchingEnabled(boolean enabled) {
        isResearchingFreeInCreativeMode = enabled;
    }

    public boolean isFreeCreativeResearchingEnabled() {
        return isResearchingFreeInCreativeMode;
    }

    public boolean isResearchFireworkEnabled() {
        return isResearchFireworkEnabled;
    }

    public boolean isDuplicateBlockLoggingEnabled() {
        return isDuplicateBlockLoggingEnabled;
    }

    public boolean isVanillaRecipeShown() {
        return isVanillaRecipeShowingEnabled;
    }

    public boolean isSlimefunGuideGivenOnJoin() {
        return isSlimefunGuideGivenOnJoin;
    }

    public boolean isUpdaterEnabled() {
        return isUpdaterEnabled;
    }

}
