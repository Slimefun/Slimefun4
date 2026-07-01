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
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiIndex;
import io.github.thebusybiscuit.slimefun5.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun5.core.services.github.GitHubService;
import io.github.thebusybiscuit.slimefun5.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.utils.NumberUtils;
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

    private static final int[] BACKGROUND_SLOTS = { 1, 3, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 48, 50, 52, 53 };
    private static final List<SlimefunGuideOption<?>> options = new ArrayList<>();

    static {
        options.add(new GuideModeOption());
        options.add(new FireworksOption());
        options.add(new LearningAnimationOption());
        options.add(new MachineMessagesOption());
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
        menu.addItem(2, CustomItemStack.create(SlimefunUtils.getCustomHead("e952d2b3f351a6b0487cc59db31bf5f2641133e5ba0006b18576e996a0293e52"),
            "&c" + locale.getMessage(p, "guide.title.credits"),
            contributorsLore.toArray(new String[0])));
        // @formatter:on

        menu.addMenuClickHandler(2, (pl, slot, action, item) -> {
            ContributorsMenu.open(pl, 0);
            return false;
        });

        // @formatter:off
        menu.addItem(4, CustomItemStack.create(XMaterial.WRITABLE_BOOK.parseMaterial(),
            ChatColor.GREEN + locale.getMessage(p, "guide.title.versions"),
            "&7&o" + locale.getMessage(p, "guide.tooltips.versions-notice"),
            "",
            "&fMinecraft: &a" + Bukkit.getBukkitVersion(),
            "&fSlimefun: &a" + Slimefun.getVersion()),
            ChestMenuUtils.getEmptyClickHandler()
        );
        // @formatter:on

        // External-website buttons (source, Discord) are shown only when guide.external-links is on.
        if (SlimefunGuide.showExternalLinks()) {
            List<String> sourceLore = locale.getMessages(p, "guide.panel.source", msg -> msg
                .replace("%activity%", NumberUtils.getElapsedTime(github.getLastUpdate()))
                .replace("%forks%", String.valueOf(github.getForks()))
                .replace("%stars%", String.valueOf(github.getStars())));
            menu.addItem(6, CustomItemStack.create(XMaterial.COMPARATOR.parseMaterial(),
               "&e" + locale.getMessage(p, "guide.title.source"),
               sourceLore.toArray(new String[0])));

            menu.addMenuClickHandler(6, (pl, slot, item, action) -> {
                pl.closeInventory();
                ChatUtils.sendURL(pl, "https://github.com/Slimefun5/Slimefun5");
                return false;
            });

            menu.addItem(8, CustomItemStack.create(XMaterial.LIGHT_BLUE_DYE.parseMaterial(),
                "&9" + locale.getMessage(p, "guide.title.discord"),
                locale.getMessages(p, "guide.panel.discord").toArray(new String[0])));

            menu.addMenuClickHandler(8, (pl, slot, item, action) -> {
                pl.closeInventory();
                ChatUtils.sendURL(pl, SlimefunGuide.DISCORD_INVITE);
                return false;
            });
        }

        // In-game Wiki, sitting opposite the Addon Installer (slot 47). Opens the teaching-focused
        // wiki home; shown to everyone, no external links.
        List<String> wikiLore = locale.getMessages(p, "guide.panel.wiki");
        menu.addItem(51, CustomItemStack.create(XMaterial.ENCHANTED_BOOK.parseMaterial(),
            "&3" + locale.getMessage(p, "guide.title.wiki"),
            wikiLore.toArray(new String[0])));

        menu.addMenuClickHandler(51, (pl, slot, item, action) -> {
            WikiIndex.open(pl, guide);
            return false;
        });

        // @formatter:off
        // Anyone with the permission can manage; everyone else can still VIEW (read-only) unless the
        // server disables it. Only when viewing is off do non-permitted players get the addons link.
        boolean canViewInstaller = p.hasPermission(AddonCatalog.PERMISSION)
            || Slimefun.getCfg().getBoolean("guide.show-addon-installer-to-everyone");
        if (canViewInstaller) {
            List<String> installerLore = locale.getMessages(p, "guide.panel.installer", msg -> msg.replace("%count%", String.valueOf(Slimefun.getInstalledAddons().size())));
            menu.addItem(47, CustomItemStack.create(Material.BOOKSHELF,
                "&3" + locale.getMessage(p, "guide.title.installer"),
                installerLore.toArray(new String[0])));

            menu.addMenuClickHandler(47, (pl, slot, item, action) -> {
                AddonInstallerMenu.open(pl, guide);
                return false;
            });
        } else if (SlimefunGuide.showExternalLinks()) {
            List<String> addonsLore = locale.getMessages(p, "guide.panel.addons", msg -> msg.replace("%count%", String.valueOf(Slimefun.getInstalledAddons().size())));
            menu.addItem(47, CustomItemStack.create(Material.BOOKSHELF,
                "&3" + locale.getMessage(p, "guide.title.addons"),
                addonsLore.toArray(new String[0])));

            menu.addMenuClickHandler(47, (pl, slot, item, action) -> {
                pl.closeInventory();
                ChatUtils.sendURL(pl, "https://github.com/Slimefun5/Slimefun5/wiki/Addons");
                return false;
            });
        }

        // In-game bug reporting (replaces the external issue-tracker link). Always available.
        menu.addItem(49, CustomItemStack.create(XMaterial.REDSTONE_TORCH.parseMaterial(),
            "&4" + locale.getMessage(p, "guide.title.bugs"),
            locale.getMessages(p, "guide.report.button-lore").toArray(new String[0])));

        menu.addMenuClickHandler(49, (pl, slot, item, action) -> {
            BugReportMenu.open(pl, guide);
            return false;
        });

        // Admin-only: opens the in-game config editor (also available via /sf config).
        if (ConfigEditorMenu.isEnabled() && ConfigEditorMenu.canUse(p)) {
            menu.addItem(45, CustomItemStack.create(XMaterial.COMMAND_BLOCK.parseMaterial(),
                "&c" + locale.getMessage(p, "guide.title.config-editor"),
                locale.getMessages(p, "guide.panel.config-editor").toArray(new String[0])));
            menu.addMenuClickHandler(45, (pl, slot, item, action) -> {
                ConfigEditorMenu.open(pl);
                return false;
            });
        }
    }

    @ParametersAreNonnullByDefault
    private static void addConfigurableOptions(Player p, ChestMenu menu, ItemStack guide) {
        int i = 19;

        for (SlimefunGuideOption<?> option : options) {
            Optional<ItemStack> item = option.getDisplayItem(p, guide);

            if (item.isPresent()) {
                menu.addItem(i, item.get());
                menu.addMenuClickHandler(i, (pl, slot, stack, action) -> {
                    option.onClick(p, guide);
                    return false;
                });

                i++;
            }
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
     * Whether the given {@link Player} wants to receive machine chat feedback (multiblock assembly
     * tips and "Assembled" messages). Defaults to {@code true}.
     *
     * @param p
     *            The {@link Player}
     *
     * @return Whether machine messages should be shown to this {@link Player}
     */
    public static boolean hasMachineMessagesEnabled(@Nonnull Player p) {
        return getOptionValue(p, MachineMessagesOption.class, true);
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

