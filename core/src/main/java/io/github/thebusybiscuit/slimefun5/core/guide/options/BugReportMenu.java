package io.github.thebusybiscuit.slimefun5.core.guide.options;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.cryptomorin.xseries.XMaterial;

import io.github.bakedlibs.dough.chat.ChatInput;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.core.services.BugReportService;
import io.github.thebusybiscuit.slimefun5.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * The in-game bug-report builder. The player selects one or more affected plugins, sets a title and
 * description, then submits. The in-progress report is held per player until it is submitted. Delivery
 * is handled by {@link BugReportService}.
 *
 * @author Slimefun5
 *
 * @see BugReportService
 */
public final class BugReportMenu {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
    private static final int LAST_TARGET_SLOT = 44;
    private static final int TITLE_SLOT = 47;
    private static final int DESCRIPTION_SLOT = 49;
    private static final int SUBMIT_SLOT = 51;
    private static final int MAX_TITLE = 100;
    private static final int MAX_DESCRIPTION = 1500;

    /** An in-progress report. Kept per player until submitted. */
    private static final class Draft {
        private final Map<String, String> selected = new LinkedHashMap<>();
        private String title = "";
        private String description = "";
    }

    private static final Map<UUID, Draft> drafts = new ConcurrentHashMap<>();

    private BugReportMenu() {}

    private static Draft draft(Player p) {
        return drafts.computeIfAbsent(p.getUniqueId(), k -> new Draft());
    }

    @ParametersAreNonnullByDefault
    public static void open(Player p, ItemStack guide) {
        LocalizationService locale = Slimefun.getLocalization();

        if (!BugReportService.isEnabled()) {
            locale.sendMessage(p, "guide.report.disabled", true);
            return;
        }

        Draft draft = draft(p);
        ChestMenu menu = new ChestMenu(locale.getMessage(p, "guide.report.menu-title"));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(0, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&7" + locale.getMessage(p, "guide.back.settings")));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            SlimefunGuideSettings.openSettings(pl, guide);
            return false;
        });

        menu.addItem(4, summaryItem(p, draft));
        menu.addMenuClickHandler(4, ChestMenuUtils.getEmptyClickHandler());

        int slot = addTarget(menu, p, guide, draft, 9, "Slimefun", "Slimefun5");

        List<Plugin> addons = new ArrayList<>(Slimefun.getInstalledAddons());
        addons.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        for (Plugin addon : addons) {
            if (slot > LAST_TARGET_SLOT) {
                break;
            }
            if (addon.getName().equalsIgnoreCase("Slimefun")) {
                continue;
            }
            slot = addTarget(menu, p, guide, draft, slot, addon.getName(), addon.getName());
        }

        menu.addItem(TITLE_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.NAME_TAG), locale.getMessage(p, "guide.report.title-button"), field(p, "guide.report.title-button-lore", "%value%", draft.title)));
        menu.addMenuClickHandler(TITLE_SLOT, (pl, s, item, action) -> {
            promptTitle(pl, guide);
            return false;
        });

        menu.addItem(DESCRIPTION_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.WRITABLE_BOOK), locale.getMessage(p, "guide.report.description-button"), field(p, "guide.report.description-button-lore", "%value%", draft.description)));
        menu.addMenuClickHandler(DESCRIPTION_SLOT, (pl, s, item, action) -> {
            promptDescription(pl, guide);
            return false;
        });

        menu.addItem(SUBMIT_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.LIME_DYE), locale.getMessage(p, "guide.report.submit-button"), locale.getMessages(p, "guide.report.submit-button-lore").toArray(new String[0])));
        menu.addMenuClickHandler(SUBMIT_SLOT, (pl, s, item, action) -> {
            trySubmit(pl, guide);
            return false;
        });

        menu.open(p);
    }

    @ParametersAreNonnullByDefault
    private static ItemStack summaryItem(Player p, Draft draft) {
        LocalizationService locale = Slimefun.getLocalization();
        List<String> lore = new ArrayList<>();
        lore.add(locale.getMessage(p, "guide.report.summary-title").replace("%title%", draft.title.isEmpty() ? "—" : shorten(draft.title)));
        lore.add(locale.getMessage(p, "guide.report.summary-plugins").replace("%plugins%", draft.selected.isEmpty() ? "—" : String.join(", ", draft.selected.values())));
        lore.add("");
        lore.addAll(locale.getMessages(p, "guide.report.summary-hint"));
        return CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOK), locale.getMessage(p, "guide.report.summary-name"), lore.toArray(new String[0]));
    }

    @ParametersAreNonnullByDefault
    private static int addTarget(ChestMenu menu, Player p, ItemStack guide, Draft draft, int slot, String displayName, String repoName) {
        LocalizationService locale = Slimefun.getLocalization();
        boolean selected = draft.selected.containsKey(repoName);
        String name = (selected ? "&a✔ " : "&e") + displayName;
        List<String> lore = locale.getMessages(p, selected ? "guide.report.target-selected" : "guide.report.target-select");

        menu.addItem(slot, CustomItemStack.create(Material.PAPER, name, lore.toArray(new String[0])));
        menu.addMenuClickHandler(slot, (pl, s, item, action) -> {
            if (draft.selected.containsKey(repoName)) {
                draft.selected.remove(repoName);
            } else {
                draft.selected.put(repoName, displayName);
            }
            open(pl, guide);
            return false;
        });

        return slot + 1;
    }

    @ParametersAreNonnullByDefault
    private static void promptTitle(Player p, ItemStack guide) {
        LocalizationService locale = Slimefun.getLocalization();
        p.closeInventory();
        locale.sendMessage(p, "guide.report.prompt-title", true);
        ChatInput.waitForPlayer(Slimefun.instance(), p, input -> {
            draft(p).title = cap(input.trim(), MAX_TITLE);
            Slimefun.runSync(() -> open(p, guide));
        });
    }

    @ParametersAreNonnullByDefault
    private static void promptDescription(Player p, ItemStack guide) {
        LocalizationService locale = Slimefun.getLocalization();
        p.closeInventory();
        locale.sendMessage(p, "guide.report.prompt-description", true);
        ChatInput.waitForPlayer(Slimefun.instance(), p, input -> {
            draft(p).description = cap(input.trim(), MAX_DESCRIPTION);
            Slimefun.runSync(() -> open(p, guide));
        });
    }

    @ParametersAreNonnullByDefault
    private static void trySubmit(Player p, ItemStack guide) {
        LocalizationService locale = Slimefun.getLocalization();
        Draft draft = draft(p);

        if (draft.selected.isEmpty()) {
            locale.sendMessage(p, "guide.report.need-plugin", true);
            return;
        }
        if (draft.title.isEmpty()) {
            locale.sendMessage(p, "guide.report.need-title", true);
            return;
        }
        if (BugReportService.isOnCooldown(p)) {
            locale.sendMessage(p, "guide.report.cooldown", true);
            return;
        }

        p.closeInventory();
        locale.sendMessage(p, "guide.report.submitting", true);
        BugReportService.submit(p, draft.title, draft.description, new ArrayList<>(draft.selected.values()));
        drafts.remove(p.getUniqueId());
    }

    @ParametersAreNonnullByDefault
    private static String[] field(Player p, String key, String token, String value) {
        List<String> lore = new ArrayList<>();
        for (String line : Slimefun.getLocalization().getMessages(p, key)) {
            lore.add(line.replace(token, value.isEmpty() ? "—" : shorten(value)));
        }
        return lore.toArray(new String[0]);
    }

    private static String shorten(String s) {
        return s.length() <= 32 ? s : s.substring(0, 31) + "…";
    }

    private static String cap(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max);
    }
}
