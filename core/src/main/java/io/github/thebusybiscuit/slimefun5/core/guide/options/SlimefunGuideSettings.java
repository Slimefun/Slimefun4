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
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;

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

    // The whole top row (1-8) and bottom row (45-53) are background; the panel buttons are placed
    // centered on top and re-center automatically as buttons are toggled off, leaving glass in the gaps.
    private static final int[] BACKGROUND_SLOTS = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
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
        GitHubService github = Slimefun.getGitHubService();

        // Fixed back-to-guide button.
        menu.addItem(0, CustomItemStack.create(SlimefunGuide.getItem(SlimefunGuideMode.SURVIVAL_MODE),
            "&e\u21E6 " + locale.getMessage(p, "guide.back.title"), "", "&7" + locale.getMessage(p, "guide.back.guide")));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            SlimefunGuide.openGuide(pl, guide);
            return false;
        });

        // Top row: each button is individually toggleable via guide.settings-buttons.<name>; the row
        // re-centers automatically as buttons are hidden (slots 1-8, slot 0 is the back button).
        List<PanelButton> top = new ArrayList<>();

        if (buttonEnabled("credits")) {
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.addAll(locale.getMessages(p, "guide.credits.description", msg -> msg.replace("%contributors%", String.valueOf(github.getContributors().size()))));
            lore.add("");
            lore.add("&7\u21E8 &e" + locale.getMessage(p, "guide.credits.open"));
            top.add(new PanelButton(
                CustomItemStack.create(SlimefunUtils.getCustomHead("e952d2b3f351a6b0487cc59db31bf5f2641133e5ba0006b18576e996a0293e52"),
                    "&c" + locale.getMessage(p, "guide.title.credits"), lore.toArray(new String[0])),
                (pl, slot, item, action) -> {
                    ContributorsMenu.open(pl, 0);
                    return false;
                }));
        }

        if (buttonEnabled("versions")) {
            top.add(new PanelButton(
                CustomItemStack.create(XMaterial.WRITABLE_BOOK.parseMaterial(),
                    ChatColor.GREEN + locale.getMessage(p, "guide.title.versions"),
                    "&7&o" + locale.getMessage(p, "guide.tooltips.versions-notice"), "",
                    "&fMinecraft: &a" + Bukkit.getBukkitVersion(), "&fSlimefun: &a" + Slimefun.getVersion()),
                ChestMenuUtils.getEmptyClickHandler()));
        }

        if (buttonEnabled("source")) {
            List<String> lore = locale.getMessages(p, "guide.panel.source", msg -> msg
                .replace("%activity%", NumberUtils.getElapsedTime(github.getLastUpdate()))
                .replace("%forks%", String.valueOf(github.getForks()))
                .replace("%stars%", String.valueOf(github.getStars())));
            top.add(new PanelButton(
                CustomItemStack.create(XMaterial.COMPARATOR.parseMaterial(), "&e" + locale.getMessage(p, "guide.title.source"), lore.toArray(new String[0])),
                (pl, slot, item, action) -> {
                    pl.closeInventory();
                    ChatUtils.sendURL(pl, "https://github.com/Slimefun5/Slimefun5");
                    return false;
                }));
        }

        if (buttonEnabled("discord")) {
            top.add(new PanelButton(
                CustomItemStack.create(XMaterial.LIGHT_BLUE_DYE.parseMaterial(), "&9" + locale.getMessage(p, "guide.title.discord"),
                    locale.getMessages(p, "guide.panel.discord").toArray(new String[0])),
                (pl, slot, item, action) -> {
                    pl.closeInventory();
                    ChatUtils.sendURL(pl, SlimefunGuide.DISCORD_INVITE);
                    return false;
                }));
        }

        place(menu, top, 1, 8);

        // Bottom row: installer / bug reports / config editor are toggleable; the Wiki is always shown.
        List<PanelButton> bottom = new ArrayList<>();

        if (buttonEnabled("installer")) {
            boolean canViewInstaller = p.hasPermission(AddonCatalog.PERMISSION) || Slimefun.getCfg().getBoolean("guide.show-addon-installer-to-everyone");
            if (canViewInstaller) {
                List<String> lore = locale.getMessages(p, "guide.panel.installer", msg -> msg.replace("%count%", String.valueOf(Slimefun.getInstalledAddons().size())));
                bottom.add(new PanelButton(
                    CustomItemStack.create(Material.BOOKSHELF, "&3" + locale.getMessage(p, "guide.title.installer"), lore.toArray(new String[0])),
                    (pl, slot, item, action) -> {
                        AddonInstallerMenu.open(pl, guide);
                        return false;
                    }));
            } else if (SlimefunGuide.showExternalLinks()) {
                List<String> lore = locale.getMessages(p, "guide.panel.addons", msg -> msg.replace("%count%", String.valueOf(Slimefun.getInstalledAddons().size())));
                bottom.add(new PanelButton(
                    CustomItemStack.create(Material.BOOKSHELF, "&3" + locale.getMessage(p, "guide.title.addons"), lore.toArray(new String[0])),
                    (pl, slot, item, action) -> {
                        pl.closeInventory();
                        ChatUtils.sendURL(pl, "https://github.com/Slimefun5/Slimefun5/wiki/Addons");
                        return false;
                    }));
            }
        }

        if (buttonEnabled("bug-reports")) {
            bottom.add(new PanelButton(
                CustomItemStack.create(XMaterial.REDSTONE_TORCH.parseMaterial(), "&4" + locale.getMessage(p, "guide.title.bugs"),
                    locale.getMessages(p, "guide.report.button-lore").toArray(new String[0])),
                (pl, slot, item, action) -> {
                    BugReportMenu.open(pl, guide);
                    return false;
                }));
        }

        if (buttonEnabled("config-editor") && ConfigEditorMenu.isEnabled() && ConfigEditorMenu.canUse(p)) {
            bottom.add(new PanelButton(
                CustomItemStack.create(XMaterial.COMMAND_BLOCK.parseMaterial(), "&c" + locale.getMessage(p, "guide.title.config-editor"),
                    locale.getMessages(p, "guide.panel.config-editor").toArray(new String[0])),
                (pl, slot, item, action) -> {
                    ConfigEditorMenu.open(pl);
                    return false;
                }));
        }

        // The Wiki button is never toggleable.
        bottom.add(new PanelButton(
            CustomItemStack.create(XMaterial.ENCHANTED_BOOK.parseMaterial(), "&3" + locale.getMessage(p, "guide.title.wiki"),
                locale.getMessages(p, "guide.panel.wiki").toArray(new String[0])),
            (pl, slot, item, action) -> {
                WikiIndex.open(pl, guide);
                return false;
            }));

        place(menu, bottom, 45, 9);
    }

    /** Whether a settings-panel button is shown (config guide.settings-buttons.&lt;name&gt;, default true). */
    private static boolean buttonEnabled(@Nonnull String name) {
        String key = "guide.settings-buttons." + name;
        return !Slimefun.getCfg().contains(key) || Slimefun.getCfg().getBoolean(key);
    }

    /** Places the buttons centered within a 9-wide row, so hiding one re-centers the rest. */
    private static void place(@Nonnull ChestMenu menu, @Nonnull List<PanelButton> buttons, int rangeStart, int rangeWidth) {
        int[] slots = centeredRow(rangeStart, rangeWidth, buttons.size());
        for (int i = 0; i < buttons.size(); i++) {
            menu.addItem(slots[i], buttons.get(i).item);
            menu.addMenuClickHandler(slots[i], buttons.get(i).handler);
        }
    }

    private static int[] centeredRow(int rangeStart, int rangeWidth, int count) {
        int[] slots = new int[count];
        int span = 2 * count - 1;

        if (span >= rangeWidth) {
            for (int i = 0; i < count; i++) {
                slots[i] = rangeStart + Math.min(i, rangeWidth - 1);
            }
        } else {
            int start = rangeStart + (rangeWidth - span) / 2;
            for (int i = 0; i < count; i++) {
                slots[i] = start + i * 2;
            }
        }

        return slots;
    }

    private static final class PanelButton {

        private final ItemStack item;
        private final MenuClickHandler handler;

        private PanelButton(@Nonnull ItemStack item, @Nonnull MenuClickHandler handler) {
            this.item = item;
            this.handler = handler;
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

