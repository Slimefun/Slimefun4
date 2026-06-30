package io.github.thebusybiscuit.slimefun5.core.guide;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.config.Config;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.guide.SurvivalSlimefunGuide;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;
import io.github.thebusybiscuit.slimefun5.utils.itemstack.SlimefunGuideItem;

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
        return Slimefun.getRegistry().getSlimefunGuide(design).getItem();
    }

    public static void openCheatMenu(@Nonnull Player p) {
        openMainMenuAsync(p, SlimefunGuideMode.CHEAT_MODE, 1);
    }

    public static void openGuide(@Nonnull Player p, @Nullable ItemStack guide) {
        SlimefunGuideMode mode = getGuideMode(guide);

        if (mode == null) {
            // Legacy/null item: fall back to identity comparison (a null item -> survival).
            mode = getItem(SlimefunGuideMode.CHEAT_MODE).equals(guide) ? SlimefunGuideMode.CHEAT_MODE : SlimefunGuideMode.SURVIVAL_MODE;
        }

        openGuide(p, mode);
    }

    /**
     * Returns the {@link SlimefunGuideMode} stored on a guide {@link ItemStack} via its PDC tag,
     * or {@code null} if the item is not a guide. Language-independent, so it still resolves after
     * the guide has been re-skinned into a player's language.
     *
     * @param item The {@link ItemStack} to inspect
     * @return The stored {@link SlimefunGuideMode}, or null
     */
    @Nullable
    public static SlimefunGuideMode getGuideMode(@Nullable ItemStack item) {
        if (item == null || item.getType() != Material.ENCHANTED_BOOK || !item.hasItemMeta()) {
            return null;
        }

        String mode = PdcCompat.getString(item.getItemMeta(), Slimefun.getRegistry().getGuideDataKey());

        if (mode == null) {
            return null;
        }

        try {
            return SlimefunGuideMode.valueOf(mode);
        } catch (IllegalArgumentException x) {
            return null;
        }
    }

    /**
     * Whether the given {@link Player} may use the Cheat Sheet, based on the admin-configured
     * access toggles ({@code guide.cheat-sheet.*}). Access is granted when ANY enabled rule
     * matches. Defaults: OPs and players with the {@code slimefun.cheat.items} permission.
     *
     * @param p The {@link Player}
     * @return Whether this player may open the Cheat Sheet
     */
    public static boolean canUseCheatSheet(@Nonnull Player p) {
        Config cfg = Slimefun.getCfg();

        if (boolOrDefault(cfg, "guide.cheat-sheet.allow-everyone", false)) {
            return true;
        }
        if (boolOrDefault(cfg, "guide.cheat-sheet.allow-op", true) && p.isOp()) {
            return true;
        }
        if (boolOrDefault(cfg, "guide.cheat-sheet.allow-creative", false) && p.getGameMode() == GameMode.CREATIVE) {
            return true;
        }
        if (boolOrDefault(cfg, "guide.cheat-sheet.allow-permission", true) && p.hasPermission("slimefun.cheat.items")) {
            return true;
        }

        return false;
    }

    private static boolean boolOrDefault(@Nonnull Config cfg, @Nonnull String key, boolean fallback) {
        return cfg.contains(key) ? cfg.getBoolean(key) : fallback;
    }

    public static void openGuide(@Nonnull Player p, @Nonnull SlimefunGuideMode mode) {
        if (!Slimefun.getWorldSettingsService().isWorldEnabled(p.getWorld())) {
            return;
        }

        Optional<PlayerProfile> optional = PlayerProfile.find(p);

        if (optional.isPresent()) {
            PlayerProfile profile = optional.get();
            SlimefunGuideImplementation guide = Slimefun.getRegistry().getSlimefunGuide(mode);
            profile.getGuideHistory().openLastEntry(guide);
        } else {
            openMainMenuAsync(p, mode, 1);
        }
    }

    @ParametersAreNonnullByDefault
    private static void openMainMenuAsync(Player player, SlimefunGuideMode mode, int selectedPage) {
        if (!PlayerProfile.get(player, profile -> Slimefun.runSync(() -> openMainMenu(profile, mode, selectedPage)))) {
            Slimefun.getLocalization().sendMessage(player, "messages.opening-guide");
        }
    }

    @ParametersAreNonnullByDefault
    public static void openMainMenu(PlayerProfile profile, SlimefunGuideMode mode, int selectedPage) {
        Slimefun.getRegistry().getSlimefunGuide(mode).openMainMenu(profile, selectedPage);
    }

    @ParametersAreNonnullByDefault
    public static void openItemGroup(PlayerProfile profile, ItemGroup itemGroup, SlimefunGuideMode mode, int selectedPage) {
        Slimefun.getRegistry().getSlimefunGuide(mode).openItemGroup(profile, itemGroup, selectedPage);
    }

    @ParametersAreNonnullByDefault
    public static void openSearch(PlayerProfile profile, String input, SlimefunGuideMode mode, boolean addToHistory) {
        SlimefunGuideImplementation guide = Slimefun.getRegistry().getSlimefunGuide(mode);
        guide.openSearch(profile, input, addToHistory);
    }

    @ParametersAreNonnullByDefault
    public static void displayItem(PlayerProfile profile, ItemStack item, boolean addToHistory) {
        Slimefun.getRegistry().getSlimefunGuide(SlimefunGuideMode.SURVIVAL_MODE).displayItem(profile, item, 0, addToHistory);
    }

    @ParametersAreNonnullByDefault
    public static void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory) {
        Slimefun.getRegistry().getSlimefunGuide(SlimefunGuideMode.SURVIVAL_MODE).displayItem(profile, item, addToHistory);
    }

    /**
     * This method checks if a given {@link ItemStack} is a {@link SlimefunGuide}.
     * 
     * @param item
     *            The {@link ItemStack} to check
     * 
     * @return Whether this {@link ItemStack} represents a {@link SlimefunGuide}
     */
    public static boolean isGuideItem(@Nullable ItemStack item) {
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) {
            return false;
        } else if (item instanceof SlimefunGuideItem) {
            return true;
        }

        // The guide-mode PDC tag is language-independent, so a guide that was re-skinned into
        // the holder's language (see ItemTranslationService#applyGuideTranslation) is still
        // recognized. Name/lore similarity alone broke translated guides from opening.
        if (item.hasItemMeta() && PdcCompat.getString(item.getItemMeta(), Slimefun.getRegistry().getGuideDataKey()) != null) {
            return true;
        }

        return SlimefunUtils.isItemSimilar(item, getItem(SlimefunGuideMode.SURVIVAL_MODE), true) || SlimefunUtils.isItemSimilar(item, getItem(SlimefunGuideMode.CHEAT_MODE), true);
    }

    /**
     * Get the default mode for the Slimefun guide.
     * Currently this is only {@link SlimefunGuideMode#SURVIVAL_MODE}.
     *
     * @return The default {@link SlimefunGuideMode}.
     */
    @Nonnull
    public static SlimefunGuideMode getDefaultMode() {
        return SlimefunGuideMode.SURVIVAL_MODE;
    }
}

