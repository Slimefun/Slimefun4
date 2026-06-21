package io.github.thebusybiscuit.slimefun5.core.guide.options;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.researches.Research;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun5.core.guide.installer.AddonCatalog;
import io.github.thebusybiscuit.slimefun5.core.guide.installer.AddonInstallerMenu;
import io.github.thebusybiscuit.slimefun5.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun5.core.services.github.GitHubService;
import io.github.thebusybiscuit.slimefun5.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * This static utility class offers various methods that provide access to the
 * Settings menu of our {@link SlimefunGuide}.
 *
 * This menu is used to allow a {@link Player} to change things such as the {@link Language}.
 *
 * @author TheBusyBiscuit
 *
 * @see SlimefunGuide
 *
 */
public final class SlimefunGuideSettings {

    // A compact 3-row (27-slot) menu: every slot is a background pane, then the functional items are
    // drawn on top. Sizing the menu to its content avoids the tall, half-empty chest that looked bare.
    private static final int[] BACKGROUND_SLOTS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };

    private static final int INSTALLER_SLOT = 22;
    private static final List<SlimefunGuideOption<?>> options = new ArrayList<>();

    static {
        options.add(new GuideModeOption());
        options.add(new FireworksOption());
        options.add(new LearningAnimationOption());
        options.add(new PlayerLanguageOption());
    }

    private SlimefunGuideSettings() {}

    public static <T> void addOption(@Nonnull SlimefunGuideOption<T> option) {
        options.add(option);
    }

    @ParametersAreNonnullByDefault
    public static void openSettings(Player p, ItemStack guide) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.settings"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_OPEN_SETTING_SOUND::playFor);

        ChestMenuUtils.drawBackground(menu, BACKGROUND_SLOTS);

        addHeader(p, menu, guide);
        addConfigurableOptions(p, menu, guide);

        menu.open(p);
    }

    @ParametersAreNonnullByDefault
    private static void addHeader(Player p, ChestMenu menu, ItemStack guide) {
        LocalizationService locale = Slimefun.getLocalization();

        // @formatter:off
        menu.addItem(0, CustomItemStack.create(SlimefunGuide.getItem(SlimefunGuideMode.SURVIVAL_MODE),
            "&e\u21E6 " + locale.getMessage(p, "guide.back.title"),
            "",
            "&7" + locale.getMessage(p, "guide.back.guide")));
        // @formatter:on

        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            SlimefunGuide.openGuide(pl, guide);
            return false;
        });

        GitHubService github = Slimefun.getGitHubService();

        List<String> contributorsLore = new ArrayList<>();
        contributorsLore.add("");
        contributorsLore.addAll(locale.getMessages(p, "guide.credits.description", msg -> msg.replace("%contributors%", String.valueOf(github.getContributors().size()))));
        contributorsLore.add("");
        contributorsLore.add("&7\u21E8 &e" + locale.getMessage(p, "guide.credits.open"));

        // @formatter:off
        menu.addItem(3, CustomItemStack.create(SlimefunUtils.getCustomHead("e952d2b3f351a6b0487cc59db31bf5f2641133e5ba0006b18576e996a0293e52"),
            "&c" + locale.getMessage(p, "guide.title.credits"),
            contributorsLore.toArray(new String[0])));
        // @formatter:on

        menu.addMenuClickHandler(3, (pl, slot, action, item) -> {
            ContributorsMenu.open(pl, 0);
            return false;
        });

        // @formatter:off
        menu.addItem(5, CustomItemStack.create(XMaterial.WRITABLE_BOOK.parseMaterial(),
            ChatColor.GREEN + locale.getMessage(p, "guide.title.versions"),
            "&7&o" + locale.getMessage(p, "guide.tooltips.versions-notice"),
            "",
            "&fMinecraft: &a" + Bukkit.getBukkitVersion(),
            "&fSlimefun: &a" + Slimefun.getVersion()),
            ChestMenuUtils.getEmptyClickHandler()
        );
        // @formatter:on

        // @formatter:off
        if (p.hasPermission(AddonCatalog.PERMISSION)) {
            menu.addItem(INSTALLER_SLOT, CustomItemStack.create(Material.BOOKSHELF,
                "&3" + locale.getMessage(p, "guide.title.installer"),
                "",
                "&7Install, update or build Slimefun and its",
                "&7addons without leaving the game.",
                "",
                "&7Installed on this Server: &b" + Slimefun.getInstalledAddons().size(),
                "",
                "&7\u21E8 &eClick to open the Addon Installer"));
            // @formatter:on

            menu.addMenuClickHandler(INSTALLER_SLOT, (pl, slot, item, action) -> {
                AddonInstallerMenu.open(pl, guide);
                return false;
            });
        }
        // Non-op players see no installer entry (the slot stays a background pane).
    }

    @ParametersAreNonnullByDefault
    private static void addConfigurableOptions(Player p, ChestMenu menu, ItemStack guide) {
        // Collect the options that are actually shown, then center them as a contiguous block in the
        // middle row (slots 9-17) so the layout stays balanced regardless of how many there are.
        List<ItemStack> displays = new ArrayList<>();
        List<SlimefunGuideOption<?>> shown = new ArrayList<>();

        for (SlimefunGuideOption<?> option : options) {
            Optional<ItemStack> item = option.getDisplayItem(p, guide);

            if (item.isPresent()) {
                displays.add(item.get());
                shown.add(option);
            }
        }

        int count = Math.min(displays.size(), 9);
        int start = 9 + Math.max(0, (9 - count) / 2);

        for (int i = 0; i < count; i++) {
            SlimefunGuideOption<?> option = shown.get(i);
            menu.addItem(start + i, displays.get(i));
            menu.addMenuClickHandler(start + i, (pl, slot, stack, action) -> {
                option.onClick(p, guide);
                return false;
            });
        }
    }

    /**
     * This method checks if the given {@link Player} has enabled the {@link FireworksOption}
     * in their {@link SlimefunGuide}.
     * If they enabled this setting, they will see fireworks when they unlock a {@link Research}.
     *
     * @param p
     *            The {@link Player}
     *
     * @return Whether this {@link Player} wants to see fireworks when unlocking a {@link Research}
     */
    public static boolean hasFireworksEnabled(@Nonnull Player p) {
        return getOptionValue(p, FireworksOption.class, true);
    }

    /**
     * This method checks if the given {@link Player} has enabled the {@link LearningAnimationOption}
     * in their {@link SlimefunGuide}.
     * If they enabled this setting, they will see messages in chat about the progress of their {@link Research}.
     *
     * @param p
     *            The {@link Player}
     *
     * @return Whether this {@link Player} wants to info messages in chat when unlocking a {@link Research}
     */
    public static boolean hasLearningAnimationEnabled(@Nonnull Player p) {
        return getOptionValue(p, LearningAnimationOption.class, true);
    }

    /**
     * Helper method to get the value of a {@link SlimefunGuideOption} that the {@link Player}
     * has set in their {@link SlimefunGuide}
     *
     * @param p
     *            The {@link Player}
     * @param optionsClass
     *            Class of the {@link SlimefunGuideOption} to get the value of
     * @param defaultValue
     *            Default value to return in case the option is not found at all or has no value set
     * @param <T>
     *            Type of the {@link SlimefunGuideOption}
     * @param <V>
     *            Type of the {@link SlimefunGuideOption} value
     *
     * @return The value of given {@link SlimefunGuideOption}
     */
    @Nonnull
    private static <T extends SlimefunGuideOption<V>, V> V getOptionValue(@Nonnull Player p, @Nonnull Class<T> optionsClass, @Nonnull V defaultValue) {
        for (SlimefunGuideOption<?> option : options) {
            if (optionsClass.isInstance(option)) {
                T o = optionsClass.cast(option);
                ItemStack guide = SlimefunGuide.getItem(SlimefunGuideMode.SURVIVAL_MODE);
                return o.getSelectedOption(p, guide).orElse(defaultValue);
            }
        }

        return defaultValue;
    }

}

