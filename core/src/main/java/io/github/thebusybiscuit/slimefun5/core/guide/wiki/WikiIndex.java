package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * Entry point of the in-game wiki: a paged grid of every enabled {@link ItemGroup}.
 * Clicking a group opens a second paged screen listing that group's {@link SlimefunItem items},
 * and clicking an item opens its {@link WikiPage}.
 *
 * Slot layout (9x6 = 54): row 0 and row 5 are the border; rows 1-4 (slots 9-44) hold content,
 * with pagination on slots 46 (previous) and 52 (next).
 */
public final class WikiIndex {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    private static final int CONTENT_START = 9;
    private static final int CONTENT_END = 44;
    private static final int PAGE_SIZE = CONTENT_END - CONTENT_START + 1;

    private static final int BACK_SLOT = 0;
    private static final int PREV_SLOT = 46;
    private static final int PAGE_INDICATOR_SLOT = 48;
    private static final int NEXT_SLOT = 52;

    private WikiIndex() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide) {
        openGroupList(p, guide, 1);
    }

    @Nonnull
    private static String title(@Nonnull Player p) {
        String message = Slimefun.getLocalization().getMessage(p, "guide.title.wiki");
        return message != null ? message : "&3Slimefun Wiki";
    }

    /** Lists every non-hidden item group; clicking one opens its item list. */
    private static void openGroupList(@Nonnull Player p, @Nonnull ItemStack guide, int page) {
        List<ItemGroup> groups = getVisibleGroups(p);

        ChestMenu menu = new ChestMenu(title(p));
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.back.guide")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            SlimefunGuide.openGuide(pl, guide);
            return false;
        });

        int pages = pageCount(groups.size());
        int offset = (page - 1) * PAGE_SIZE;

        for (int i = 0; i < PAGE_SIZE && offset + i < groups.size(); i++) {
            ItemGroup group = groups.get(offset + i);
            int slot = CONTENT_START + i;

            menu.addItem(slot, group.getItem(p));
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                openItemList(pl, guide, group, 1);
                return false;
            });
        }

        addPagination(menu, p, page, pages, (pl, target) -> openGroupList(pl, guide, target));
        menu.open(p);
    }

    /** Lists the items of a single group; clicking one opens its wiki page. Back returns to the group list. */
    private static void openItemList(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull ItemGroup itemGroup, int page) {
        List<SlimefunItem> items = itemGroup.getItems();

        ChestMenu menu = new ChestMenu(title(p));
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.back.title")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            openGroupList(pl, guide, 1);
            return false;
        });

        int pages = pageCount(items.size());
        int offset = (page - 1) * PAGE_SIZE;

        for (int i = 0; i < PAGE_SIZE && offset + i < items.size(); i++) {
            SlimefunItem item = items.get(offset + i);
            int slot = CONTENT_START + i;

            menu.addItem(slot, item.getItem());
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                WikiPage.open(pl, guide, item);
                return false;
            });
        }

        addPagination(menu, p, page, pages, (pl, target) -> openItemList(pl, guide, itemGroup, target));
        menu.open(p);
    }

    @Nonnull
    private static List<ItemGroup> getVisibleGroups(@Nonnull Player p) {
        List<ItemGroup> groups = new ArrayList<>();

        for (ItemGroup group : Slimefun.getRegistry().getAllItemGroups()) {
            if (!group.isHidden(p)) {
                groups.add(group);
            }
        }

        return groups;
    }

    private static int pageCount(int size) {
        return Math.max(1, (int) Math.ceil(size / (double) PAGE_SIZE));
    }

    private static void addPagination(@Nonnull ChestMenu menu, @Nonnull Player p, int page, int pages, @Nonnull PageNavigator navigator) {
        menu.addItem(PREV_SLOT, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(PREV_SLOT, (pl, slot, clicked, action) -> {
            if (page > 1) {
                navigator.open(pl, page - 1);
            }
            return false;
        });

        menu.addItem(PAGE_INDICATOR_SLOT, ChestMenuUtils.getBackground());
        menu.addMenuClickHandler(PAGE_INDICATOR_SLOT, ChestMenuUtils.getEmptyClickHandler());

        menu.addItem(NEXT_SLOT, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(NEXT_SLOT, (pl, slot, clicked, action) -> {
            if (page < pages) {
                navigator.open(pl, page + 1);
            }
            return false;
        });
    }

    @FunctionalInterface
    private interface PageNavigator {
        void open(@Nonnull Player p, int page);
    }
}
