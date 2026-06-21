package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * A readable wiki topic page: shows the authored explanation lines for a mechanic topic
 * (from {@link WikiText#getMechanic(String)}) as the lore of a single centered info item.
 * A Back button returns to the wiki home.
 *
 * Slot layout (9x6 = 54): the whole grid is border except slot 0 (back) and slot 22 (the
 * centered topic info item).
 */
public final class WikiTopicPage {

    private static final int[] BORDER = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    private static final int BACK_SLOT = 0;
    private static final int INFO_SLOT = 22;

    private WikiTopicPage() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull String topicId, @Nonnull String displayName, @Nonnull XMaterial icon) {
        ChestMenu menu = new ChestMenu("Wiki: " + displayName);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back"));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            WikiIndex.openHomeFromTopic(pl, guide);
            return false;
        });

        List<String> lore = new ArrayList<>();
        lore.add("");

        List<String> lines = Slimefun.getWikiText().getMechanic(topicId);

        if (lines.isEmpty()) {
            lore.add("&7(No information available yet.)");
        } else {
            lore.addAll(lines);
        }

        menu.addItem(INFO_SLOT, CustomItemStack.create(MaterialCompat.stack(icon), "&b" + displayName, lore.toArray(new String[0])));
        menu.addMenuClickHandler(INFO_SLOT, ChestMenuUtils.getEmptyClickHandler());

        menu.open(p);
    }
}
