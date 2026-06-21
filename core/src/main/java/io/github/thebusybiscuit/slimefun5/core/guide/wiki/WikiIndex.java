package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * Entry point of the in-game wiki. {@link #open(Player, ItemStack)} opens a wiki HOME menu that
 * leads with explanatory topic guides (getting started, research, energy, cargo, multiblocks) and
 * offers a "Browse items by category" button into the classic group/item browser.
 *
 * Clicking a topic guide opens a readable {@link WikiTopicPage}. Clicking the browse button opens
 * a paged grid of every enabled {@link ItemGroup}; clicking a group lists that group's
 * {@link SlimefunItem items}, and clicking an item opens its {@link WikiPage}.
 */
public final class WikiIndex {

    private static final int[] BORDER = { 1, 2, 3, 4, 5, 6, 7, 8, 45, 47, 49, 50, 51, 53 };

    private static final int CONTENT_START = 9;
    private static final int CONTENT_END = 44;
    private static final int PAGE_SIZE = CONTENT_END - CONTENT_START + 1;

    private static final int BACK_SLOT = 0;
    private static final int PREV_SLOT = 46;
    private static final int PAGE_INDICATOR_SLOT = 48;
    private static final int NEXT_SLOT = 52;

    // Wiki HOME layout (9x6 = 54): a row of topic guides, then the browse button below.
    private static final int[] HOME_BORDER = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
    private static final int[] TOPIC_SLOTS = { 11, 12, 13, 14, 15 };
    private static final int BROWSE_SLOT = 31;

    private WikiIndex() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide) {
        openHome(p, guide);
    }

    @Nonnull
    private static String title(@Nonnull Player p) {
        String message = Slimefun.getLocalization().getMessage(p, "guide.title.wiki");
        return message != null ? message : "&3Slimefun Wiki";
    }

    /** The wiki HOME: explanatory topic guides plus a button into the item-category browser. */
    private static void openHome(@Nonnull Player p, @Nonnull ItemStack guide) {
        ChestMenu menu = new ChestMenu(title(p));
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, HOME_BORDER);

        menu.addItem(BACK_SLOT, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.back.guide")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            SlimefunGuide.openGuide(pl, guide);
            return false;
        });

        // Welcome header so a new player knows what this screen is for.
        menu.addItem(4, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK),
            "&3Slimefun Wiki",
            "",
            "&7New here? Read the guides below to learn",
            "&7how Slimefun works - start with &eGetting Started&7.",
            "&7Or browse items to look something up."),
            ChestMenuUtils.getEmptyClickHandler());

        Topic[] topics = topics();

        for (int i = 0; i < topics.length && i < TOPIC_SLOTS.length; i++) {
            Topic topic = topics[i];
            int slot = TOPIC_SLOTS[i];

            menu.addItem(slot, CustomItemStack.create(MaterialCompat.stack(topic.icon), "&b" + topic.displayName, "", topic.summary, "", "&7⇨ &eClick to read"));
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                WikiTopicPage.open(pl, guide, topic.id, topic.displayName, topic.icon);
                return false;
            });
        }

        menu.addItem(BROWSE_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOKSHELF), "&aBrowse items by category", "", "&7Explore every Slimefun category", "&7and look up individual items.", "", "&7⇨ &eClick"));
        menu.addMenuClickHandler(BROWSE_SLOT, (pl, slot, clicked, action) -> {
            openGroupList(pl, guide, 1);
            return false;
        });

        menu.open(p);
    }

    /** The explanatory topic guides shown on the wiki home, in display order. */
    @Nonnull
    private static Topic[] topics() {
        return new Topic[] {
            new Topic("getting_started", "Getting Started", XMaterial.MAP, "&7Your first steps in Slimefun"),
            new Topic("research", "Research", XMaterial.EXPERIENCE_BOTTLE, "&7Unlock items with experience"),
            new Topic("energy", "Energy Networks", XMaterial.REDSTONE, "&7Power your machines"),
            new Topic("cargo", "Cargo Networks", XMaterial.CHEST, "&7Move items automatically"),
            new Topic("multiblocks", "Multiblocks", XMaterial.BRICKS, "&7Build structures to craft")
        };
    }

    /** Lists every non-hidden item group; clicking one opens its item list. */
    private static void openGroupList(@Nonnull Player p, @Nonnull ItemStack guide, int page) {
        List<ItemGroup> groups = getVisibleGroups(p);

        ChestMenu menu = new ChestMenu(title(p));
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.back.title")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            openHome(pl, guide);
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
        List<SlimefunItem> items = new ArrayList<>(itemGroup.getItems());

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
            // FlexItemGroups (the Advancements group, installer, etc.) render their own UI and have no
            // enumerable item list - getItems() throws on them - so they are not browsable wiki categories.
            if (group instanceof FlexItemGroup) {
                continue;
            }

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

    /** Returns to the wiki home; used by topic pages as their back target. */
    static void openHomeFromTopic(@Nonnull Player p, @Nonnull ItemStack guide) {
        openHome(p, guide);
    }

    @FunctionalInterface
    private interface PageNavigator {
        void open(@Nonnull Player p, int page);
    }

    /** A single explanatory wiki topic: its mechanics.yml id, display name, icon, and one-line summary. */
    private static final class Topic {

        private final String id;
        private final String displayName;
        private final XMaterial icon;
        private final String summary;

        private Topic(@Nonnull String id, @Nonnull String displayName, @Nonnull XMaterial icon, @Nonnull String summary) {
            this.id = id;
            this.displayName = displayName;
            this.icon = icon;
            this.summary = summary;
        }
    }
}
