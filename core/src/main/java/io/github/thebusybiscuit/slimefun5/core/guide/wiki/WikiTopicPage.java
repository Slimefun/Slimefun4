package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * A readable wiki topic page. The guide text (from {@link WikiText#getMechanic(String)}) sits at the
 * top as a paginated tooltip, and below it the items the guide talks about
 * ({@link WikiText#getTopicItems(String)}) are shown as clickable icons - clicking one opens that
 * item's {@link WikiPage} (recipe, stats, "used in"). This keeps the page full and turns each guide
 * into a hub that links straight to the relevant items and their recipes.
 */
public final class WikiTopicPage {

    private static final int BACK_SLOT = 0;
    private static final int PREV_SLOT = 3;
    private static final int TEXT_SLOT = 4;
    private static final int NEXT_SLOT = 5;
    private static final int LABEL_SLOT = 13;
    private static final int[] ITEM_SLOTS = { 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43 };
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

        // Frame only the top and bottom rows; the item area stays clean (no panes in item slots).
        for (int i = 0; i < 9; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i = 45; i < 54; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        menu.addItem(BACK_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back"));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            WikiIndex.openHomeFromTopic(pl, guide);
            return false;
        });

        addText(menu, lines, current, displayName, icon);

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

        addRelatedItems(menu, guide, topicId, displayName, icon);
        menu.open(p);
    }

    private static void addText(@Nonnull ChestMenu menu, @Nonnull List<String> lines, int page, @Nonnull String displayName, @Nonnull XMaterial icon) {
        List<String> lore = new ArrayList<>();
        lore.add("");

        if (lines.isEmpty()) {
            lore.add("&7(No information available yet.)");
        } else {
            int start = (page - 1) * LINES_PER_PAGE;
            int end = Math.min(start + LINES_PER_PAGE, lines.size());

            for (int i = start; i < end; i++) {
                lore.add(lines.get(i));
            }
        }

        menu.addItem(TEXT_SLOT, CustomItemStack.create(MaterialCompat.stack(icon), "&b" + displayName, lore.toArray(new String[0])));
        menu.addMenuClickHandler(TEXT_SLOT, ChestMenuUtils.getEmptyClickHandler());
    }

    /** Renders the topic's relevant items as clickable icons; each opens that item's wiki page. */
    private static void addRelatedItems(@Nonnull ChestMenu menu, @Nonnull ItemStack guide, @Nonnull String topicId, @Nonnull String displayName, @Nonnull XMaterial icon) {
        List<String> ids = Slimefun.getWikiText().getTopicItems(topicId);
        int placed = 0;

        for (String id : ids) {
            if (placed >= ITEM_SLOTS.length) {
                break;
            }

            SlimefunItem item = SlimefunItem.getById(id);

            if (item == null) {
                continue;
            }

            int slot = ITEM_SLOTS[placed];
            placed++;

            menu.addItem(slot, item.getItem());
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                WikiPage.open(pl, guide, item, () -> open(pl, guide, topicId, displayName, icon));
                return false;
            });
        }

        if (placed > 0) {
            menu.addItem(LABEL_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOKSHELF),
                "&e▼ Items in this guide",
                "",
                "&7Click any item below for its",
                "&7recipe, stats and where it''s used."),
                ChestMenuUtils.getEmptyClickHandler());
        }
    }
}
