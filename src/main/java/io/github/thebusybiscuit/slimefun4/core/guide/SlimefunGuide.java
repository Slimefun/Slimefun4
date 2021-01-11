package io.github.thebusybiscuit.slimefun4.core.guide;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.guide.SurvivalSlimefunGuide;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This is a static utility class that provides convenient access to the methods
 * of {@link SlimefunGuideImplementation} that abstracts away the actual implementation.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunGuideImplementation
 * @see SurvivalSlimefunGuide
 *
 */
public final class SlimefunGuide {

    private SlimefunGuide() {}

    @Nonnull
    public static ItemStack getItem(@Nonnull SlimefunGuideMode design) {
        return SlimefunPlugin.getRegistry().getGuideLayout(design).getItem();
    }

    public static void openCheatMenu(@Nonnull Player p) {
        openMainMenuAsync(p, SlimefunGuideMode.CHEAT_MODE, 1);
    }

    public static void openGuide(@Nonnull Player p, @Nullable ItemStack guide) {
        if (getItem(SlimefunGuideMode.CHEAT_MODE).equals(guide)) {
            openGuide(p, SlimefunGuideMode.CHEAT_MODE);
        } else {
            /*
             * When using /sf cheat or /sf open_guide the ItemStack is null anyway,
             * so we don't even need to check here at this point.
             */
            openGuide(p, SlimefunGuideMode.SURVIVAL_MODE);
        }
    }

    public static void openGuide(@Nonnull Player p, @Nonnull SlimefunGuideMode layout) {
        if (!SlimefunPlugin.getWorldSettingsService().isWorldEnabled(p.getWorld())) {
            return;
        }

        Optional<PlayerProfile> optional = PlayerProfile.find(p);

        if (optional.isPresent()) {
            PlayerProfile profile = optional.get();
            SlimefunGuideImplementation guide = SlimefunPlugin.getRegistry().getGuideLayout(layout);
            profile.getGuideHistory().openLastEntry(guide);
        } else {
            openMainMenuAsync(p, layout, 1);
        }
    }

    @ParametersAreNonnullByDefault
    private static void openMainMenuAsync(Player player, SlimefunGuideMode layout, int selectedPage) {
        if (!PlayerProfile.get(player, profile -> SlimefunPlugin.runSync(() -> openMainMenu(profile, layout, selectedPage)))) {
            SlimefunPlugin.getLocalization().sendMessage(player, "messages.opening-guide");
        }
    }

    @ParametersAreNonnullByDefault
    public static void openMainMenu(PlayerProfile profile, SlimefunGuideMode layout, int selectedPage) {
        SlimefunPlugin.getRegistry().getGuideLayout(layout).openMainMenu(profile, selectedPage);
    }

    @ParametersAreNonnullByDefault
    public static void openCategory(PlayerProfile profile, Category category, SlimefunGuideMode layout, int selectedPage) {
        SlimefunPlugin.getRegistry().getGuideLayout(layout).openCategory(profile, category, selectedPage);
    }

    @ParametersAreNonnullByDefault
    public static void openSearch(PlayerProfile profile, String input, boolean survival, boolean addToHistory) {
        SlimefunGuideImplementation layout = SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideMode.SURVIVAL_MODE);

        if (!survival) {
            layout = SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideMode.CHEAT_MODE);
        }

        layout.openSearch(profile, input, addToHistory);
    }

    @ParametersAreNonnullByDefault
    public static void displayItem(PlayerProfile profile, ItemStack item, boolean addToHistory) {
        SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideMode.SURVIVAL_MODE).displayItem(profile, item, 0, addToHistory);
    }

    @ParametersAreNonnullByDefault
    public static void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory) {
        SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideMode.SURVIVAL_MODE).displayItem(profile, item, addToHistory);
    }

    public static boolean isGuideItem(@Nullable ItemStack item) {
        if (item == null) {
            return false;
        } else {
            return SlimefunUtils.isItemSimilar(item, getItem(SlimefunGuideMode.SURVIVAL_MODE), true) || SlimefunUtils.isItemSimilar(item, getItem(SlimefunGuideMode.CHEAT_MODE), true);
        }
    }

    /**
     * Get the default layout for the Slimefun guide.
     * Currently this is only {@link SlimefunGuideMode#SURVIVAL_MODE}.
     *
     * @return The default {@link SlimefunGuideLayout}.
     */
    @Nonnull
    public static SlimefunGuideMode getDefaultLayout() {
        return SlimefunGuideMode.SURVIVAL_MODE;
    }
}
