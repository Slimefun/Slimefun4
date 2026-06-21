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
 * A readable wiki topic page. Shows the authored explanation for a mechanic topic
 * (from {@link WikiText#getMechanic(String)}) as the lore of a centered info item. Long topics are
 * split into pages of {@link #LINES_PER_PAGE} lines with previous/next buttons flanking the item,
 * so a guide can be as detailed as needed and stay readable. Back returns to the wiki home.
 */
public final class WikiTopicPage {

    // Everything is a background pane except back (0), the info item (22) and its flanking arrows (21/23).
    private static final int[] BORDER = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    private static final int BACK_SLOT = 0;
    private static final int INFO_SLOT = 22;
    private static final int PREV_SLOT = 21;
    private static final int NEXT_SLOT = 23;
    private static final int LINES_PER_PAGE = 12;

    private WikiTopicPage() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull String topicId, @Nonnull String displayName, @Nonnull XMaterial icon) {
        open(p, guide, topicId, displayName, icon, 1);
    }

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull String topicId, @Nonnull String displayName, @Nonnull XMaterial icon, int page) {
        List<String> lines = Slimefun.getWikiText().getMechanic(topicId);
        int pages = Math.max(1, (int) Math.ceil(lines.size() / (double) LINES_PER_PAGE));
        int current = Math.min(Math.max(1, page), pages);

        String title = "Wiki: " + displayName + (pages > 1 ? " (" + current + "/" + pages + ")" : "");
        ChestMenu menu = new ChestMenu(title);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back"));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            WikiIndex.openHomeFromTopic(pl, guide);
            return false;
        });

        List<String> lore = new ArrayList<>();
        lore.add("");

        if (lines.isEmpty()) {
            lore.add("&7(No information available yet.)");
        } else {
            int start = (current - 1) * LINES_PER_PAGE;
            int end = Math.min(start + LINES_PER_PAGE, lines.size());

            for (int i = start; i < end; i++) {
                lore.add(lines.get(i));
            }
        }

        menu.addItem(INFO_SLOT, CustomItemStack.create(MaterialCompat.stack(icon), "&b" + displayName, lore.toArray(new String[0])));
        menu.addMenuClickHandler(INFO_SLOT, ChestMenuUtils.getEmptyClickHandler());

        if (pages > 1) {
            menu.addItem(PREV_SLOT, ChestMenuUtils.getPreviousButton(p, current, pages));
            menu.addMenuClickHandler(PREV_SLOT, (pl, slot, clicked, action) -> {
                if (current > 1) {
                    open(pl, guide, topicId, displayName, icon, current - 1);
                }
                return false;
            });

            menu.addItem(NEXT_SLOT, ChestMenuUtils.getNextButton(p, current, pages));
            menu.addMenuClickHandler(NEXT_SLOT, (pl, slot, clicked, action) -> {
                if (current < pages) {
                    open(pl, guide, topicId, displayName, icon, current + 1);
                }
                return false;
            });
        }

        menu.open(p);
    }
}
