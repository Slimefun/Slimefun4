package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.balance.BalanceScore;
import io.github.thebusybiscuit.slimefun5.core.balance.BalanceService;
import io.github.thebusybiscuit.slimefun5.core.balance.BalanceVerdict;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * Admin-only drill-down: the strongest items of one addon, each showing its balance score + tier.
 * Callers gate on {@link AddonCatalog#PERMISSION}; this menu assumes the viewer is a manager.
 */
public final class AddonBalanceMenu {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
    private static final int PER_PAGE = 36; // slots 9..44
    private static final int PREV_SLOT = 45;
    private static final int NEXT_SLOT = 53;

    private AddonBalanceMenu() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull String addonName, @Nonnull String displayName, @Nonnull Runnable back) {
        open(p, guide, addonName, displayName, back, 0);
    }

    /** Builds a chest title that never exceeds Bukkit's 32-char limit: keeps the suffix, truncates the name. */
    @Nonnull
    private static String fitTitle(@Nonnull String name, @Nonnull String suffix) {
        int room = 32 - suffix.length();

        if (name.length() > room) {
            name = room > 1 ? name.substring(0, room - 1) + "…" : name.substring(0, Math.max(0, room));
        }

        return name + suffix;
    }

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull String addonName, @Nonnull String displayName, @Nonnull Runnable back, int page) {
        List<SlimefunItem> all = BalanceService.instance().topItems(addonName, Integer.MAX_VALUE);

        int pages = Math.max(1, (all.size() + PER_PAGE - 1) / PER_PAGE);
        int clamped = Math.max(0, Math.min(page, pages - 1));
        int start = clamped * PER_PAGE;
        List<SlimefunItem> items = all.subList(start, Math.min(start + PER_PAGE, all.size()));

        // Chest titles must not exceed 32 chars (legacy servers throw above that). Keep the page
        // indicator and truncate the addon name to fit.
        String pageSuffix = pages > 1
            ? " " + Slimefun.getLocalization().getMessage(p, "guide.config.page")
                .replace("%page%", String.valueOf(clamped + 1))
                .replace("%pages%", String.valueOf(pages))
            : "";

        ChestMenu menu = new ChestMenu(fitTitle(displayName, pageSuffix));
        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(0, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), Slimefun.getLocalization().getMessage(p, "guide.installer.back")));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            back.run();
            return false;
        });

        int slot = 9;

        for (SlimefunItem sfItem : items) {
            BalanceScore score = BalanceService.instance().scoreOf(sfItem);
            int effort = BalanceService.instance().effortOf(sfItem);
            BalanceVerdict verdict = BalanceService.instance().verdictOf(sfItem);

            String tierName = Slimefun.getLocalization().getMessage(p, "guide.balance.tier." + score.getTier().name().toLowerCase(Locale.ROOT));
            String verdictName = Slimefun.getLocalization().getMessage(p, "guide.balance.verdict." + verdict.name().toLowerCase(Locale.ROOT));

            String powerLine = Slimefun.getLocalization().getMessage(p, "guide.balance.item-line")
                .replace("%tier%", tierName)
                .replace("%score%", String.valueOf(score.getScore()));
            String effortLine = Slimefun.getLocalization().getMessage(p, "guide.balance.item-effort-line")
                .replace("%effort%", String.valueOf(effort));
            String verdictLine = Slimefun.getLocalization().getMessage(p, "guide.balance.item-verdict-line")
                .replace("%verdict%", verdictName);

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(powerLine);
            lore.add(effortLine);
            lore.add(verdictLine);

            ItemStack icon = sfItem.getItem().clone();
            menu.addItem(slot, CustomItemStack.create(icon, "&f" + sfItem.getItemName(), lore.toArray(new String[0])));
            menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
            slot++;
        }

        if (clamped > 0) {
            menu.addItem(PREV_SLOT, ChestMenuUtils.getPreviousButton(p, clamped + 1, pages));
            menu.addMenuClickHandler(PREV_SLOT, (pl, s, item, action) -> {
                open(pl, guide, addonName, displayName, back, clamped - 1);
                return false;
            });
        }

        if (clamped < pages - 1) {
            menu.addItem(NEXT_SLOT, ChestMenuUtils.getNextButton(p, clamped + 1, pages));
            menu.addMenuClickHandler(NEXT_SLOT, (pl, s, item, action) -> {
                open(pl, guide, addonName, displayName, back, clamped + 1);
                return false;
            });
        }

        menu.open(p);
    }
}
