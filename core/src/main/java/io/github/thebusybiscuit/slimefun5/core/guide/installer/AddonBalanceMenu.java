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
    private static final int LIMIT = 36; // slots 9..44

    private AddonBalanceMenu() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull String addonName, @Nonnull String displayName, @Nonnull Runnable back) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.installer"));
        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(0, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), Slimefun.getLocalization().getMessage(p, "guide.installer.back")));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            back.run();
            return false;
        });

        List<SlimefunItem> items = BalanceService.instance().topItems(addonName, LIMIT);
        int slot = 9;

        for (SlimefunItem sfItem : items) {
            if (slot >= 45) {
                break;
            }

            BalanceScore score = BalanceService.instance().scoreOf(sfItem);
            String tierName = Slimefun.getLocalization().getMessage(p, "guide.balance.tier." + score.getTier().name().toLowerCase(Locale.ROOT));
            String line = Slimefun.getLocalization().getMessage(p, "guide.balance.item-line")
                .replace("%tier%", tierName)
                .replace("%score%", String.valueOf(score.getScore()));

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(line);

            ItemStack icon = sfItem.getItem().clone();
            menu.addItem(slot, CustomItemStack.create(icon, "&f" + sfItem.getItemName(), lore.toArray(new String[0])));
            menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
            slot++;
        }

        menu.open(p);
    }
}
