package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.chat.ChatInput;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
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

    // Wiki HOME header slots (topic tiles fill the same CONTENT_START..CONTENT_END grid as the browser).
    private static final int WELCOME_SLOT = 4;
    private static final int ADDON_SLOT = 6;
    private static final int SEARCH_SLOT = 7;
    private static final int BROWSE_SLOT = 8;

    private WikiIndex() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide) {
        openHome(p, guide);
    }

    @Nonnull
    private static String title(@Nonnull Player p) {
        String message = Slimefun.getLocalization().getMessage(p, "guide.title.wiki");
        return message != null ? message : "&3Slimefun Wiki";
    }

    /** The wiki HOME: a paginated grid of explanatory topic guides, plus a button into the item browser. */
    private static void openHome(@Nonnull Player p, @Nonnull ItemStack guide) {
        openHome(p, guide, 1);
    }

    private static void openHome(@Nonnull Player p, @Nonnull ItemStack guide, int page) {
        List<WikiTopic> topics = Slimefun.getWikiText().getTopics();

        ChestMenu menu = new ChestMenu(title(p));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.back.guide")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            SlimefunGuide.openGuide(pl, guide);
            return false;
        });

        // Welcome header so a new player knows what this screen is for.
        menu.addItem(WELCOME_SLOT, tile(p, XMaterial.ENCHANTED_BOOK, "guide.wiki.welcome.title", "guide.wiki.welcome.lore"),
            ChestMenuUtils.getEmptyClickHandler());

        menu.addItem(SEARCH_SLOT, tile(p, XMaterial.COMPASS, "guide.wiki.search.name", "guide.wiki.search.lore"));
        menu.addMenuClickHandler(SEARCH_SLOT, (pl, slot, clicked, action) -> {
            pl.closeInventory();
            ChatInput.waitForPlayer(Slimefun.instance(), pl, msg -> openSearchResults(pl, guide, msg, 1));
            return false;
        });

        menu.addItem(BROWSE_SLOT, tile(p, XMaterial.BOOKSHELF, "guide.wiki.browse.name", "guide.wiki.browse.lore"));
        menu.addMenuClickHandler(BROWSE_SLOT, (pl, slot, clicked, action) -> {
            openGroupList(pl, guide, 1);
            return false;
        });

        menu.addItem(ADDON_SLOT, tile(p, XMaterial.CHEST_MINECART, "guide.wiki.addon.name", "guide.wiki.addon.lore"));
        menu.addMenuClickHandler(ADDON_SLOT, (pl, slot, clicked, action) -> {
            openAddonList(pl, guide, 1);
            return false;
        });

        int pages = pageCount(topics.size());
        int offset = (page - 1) * PAGE_SIZE;

        for (int i = 0; i < PAGE_SIZE && offset + i < topics.size(); i++) {
            WikiTopic topic = topics.get(offset + i);
            int slot = CONTENT_START + i;

            String topicName = localizedTopicName(p, topic);
            menu.addItem(slot, CustomItemStack.create(MaterialCompat.stack(topic.getIcon()), "&b" + topicName, "", localizedTopicSummary(p, topic), "", Slimefun.getLocalization().getMessage(p, "guide.wiki.topic-click")));
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                WikiTopicPage.open(pl, guide, topic.getId(), localizedTopicName(pl, topic), topic.getIcon());
                return false;
            });
        }

        addPagination(menu, p, page, pages, (pl, target) -> openHome(pl, guide, target));
        menu.open(p);
    }

    /** Lists every non-hidden item group; clicking one opens its item list. */
    private static void openGroupList(@Nonnull Player p, @Nonnull ItemStack guide, int page) {
        List<ItemGroup> groups = getVisibleGroups(p);

        ChestMenu menu = new ChestMenu(title(p));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
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
                openItemList(pl, guide, group, 1, () -> openGroupList(pl, guide, 1));
                return false;
            });
        }

        addPagination(menu, p, page, pages, (pl, target) -> openGroupList(pl, guide, target));
        menu.open(p);
    }

    /** Lists the items of a single group; clicking one opens its wiki page. {@code back} defines where the back button returns to (the category or addon list, depending on entry point). */
    private static void openItemList(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull ItemGroup itemGroup, int page, @Nonnull Runnable back) {
        List<SlimefunItem> items = new ArrayList<>(itemGroup.getItems());

        ChestMenu menu = new ChestMenu(title(p));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.back.title")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            back.run();
            return false;
        });

        int pages = pageCount(items.size());
        int offset = (page - 1) * PAGE_SIZE;

        for (int i = 0; i < PAGE_SIZE && offset + i < items.size(); i++) {
            SlimefunItem item = items.get(offset + i);
            int slot = CONTENT_START + i;

            menu.addItem(slot, item.getItem());
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                WikiPage.open(pl, guide, item, () -> openItemList(pl, guide, itemGroup, page, back));
                return false;
            });
        }

        addPagination(menu, p, page, pages, (pl, target) -> openItemList(pl, guide, itemGroup, target, back));
        menu.open(p);
    }

    /** Lists every addon that owns at least one visible item group; clicking one opens that addon's categories. */
    private static void openAddonList(@Nonnull Player p, @Nonnull ItemStack guide, int page) {
        List<String> addons = getAddonsWithGroups(p);

        ChestMenu menu = new ChestMenu(title(p));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.back.title")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            openHome(pl, guide);
            return false;
        });

        int pages = pageCount(addons.size());
        int offset = (page - 1) * PAGE_SIZE;

        for (int i = 0; i < PAGE_SIZE && offset + i < addons.size(); i++) {
            String addon = addons.get(offset + i);
            int slot = CONTENT_START + i;
            List<ItemGroup> groups = getAddonGroups(p, addon);

            menu.addItem(slot, CustomItemStack.create(addonIcon(p, groups), "&b" + addon, "",
                Slimefun.getLocalization().getMessage(p, "guide.wiki.addon-categories").replace("%count%", String.valueOf(groups.size())),
                "", Slimefun.getLocalization().getMessage(p, "guide.wiki.topic-click")));
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                openAddonGroups(pl, guide, addon, 1);
                return false;
            });
        }

        addPagination(menu, p, page, pages, (pl, target) -> openAddonList(pl, guide, target));
        menu.open(p);
    }

    /** Lists a single addon's visible item groups; clicking one lists that group's items. Back returns to the addon list. */
    private static void openAddonGroups(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull String addon, int page) {
        List<ItemGroup> groups = getAddonGroups(p, addon);

        ChestMenu menu = new ChestMenu(title(p));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.back.title")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            openAddonList(pl, guide, 1);
            return false;
        });

        int pages = pageCount(groups.size());
        int offset = (page - 1) * PAGE_SIZE;

        for (int i = 0; i < PAGE_SIZE && offset + i < groups.size(); i++) {
            ItemGroup group = groups.get(offset + i);
            int slot = CONTENT_START + i;

            menu.addItem(slot, group.getItem(p));
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                openItemList(pl, guide, group, 1, () -> openAddonGroups(pl, guide, addon, page));
                return false;
            });
        }

        addPagination(menu, p, page, pages, (pl, target) -> openAddonGroups(pl, guide, addon, target));
        menu.open(p);
    }

    /** Distinct addons that own at least one visible item group, core first, then alphabetical. */
    @Nonnull
    private static List<String> getAddonsWithGroups(@Nonnull Player p) {
        Set<String> names = new TreeSet<>();

        for (ItemGroup group : getVisibleGroups(p)) {
            SlimefunAddon addon = group.getAddon();
            if (addon != null) {
                names.add(addon.getName());
            }
        }

        List<String> sorted = new ArrayList<>(names);
        String core = Slimefun.instance().getName();
        if (sorted.remove(core)) {
            sorted.add(0, core);
        }

        return sorted;
    }

    /** The visible item groups registered by the addon with the given name. */
    @Nonnull
    private static List<ItemGroup> getAddonGroups(@Nonnull Player p, @Nonnull String addon) {
        List<ItemGroup> groups = new ArrayList<>();

        for (ItemGroup group : getVisibleGroups(p)) {
            SlimefunAddon a = group.getAddon();
            if (a != null && a.getName().equals(addon)) {
                groups.add(group);
            }
        }

        return groups;
    }

    /** Represents an addon by its first category's icon material (fresh, so the group's own name/lore is dropped); falls back to a book. */
    @Nonnull
    private static ItemStack addonIcon(@Nonnull Player p, @Nonnull List<ItemGroup> groups) {
        if (!groups.isEmpty()) {
            return new ItemStack(groups.get(0).getItem(p).getType());
        }

        return MaterialCompat.stack(XMaterial.BOOK);
    }

    /** Lists every enabled item whose (translated) name matches the search term; clicking one opens its wiki page. */
    private static void openSearchResults(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull String query, int page) {
        String term = ChatColor.stripColor(query).toLowerCase(Locale.ROOT).trim();
        List<SlimefunItem> matches = new ArrayList<>();

        if (!term.isEmpty()) {
            for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
                try {
                    String name = ChatColor.stripColor(Slimefun.getItemTranslationService().getName(p, item)).toLowerCase(Locale.ROOT);

                    if (!name.isEmpty() && name.contains(term)) {
                        matches.add(item);
                    }
                } catch (Exception | LinkageError ignored) {
                    // A broken item should not break the search.
                }
            }
        }

        ChestMenu menu = new ChestMenu(title(p));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.back.title")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            openHome(pl, guide);
            return false;
        });

        menu.addItem(WELCOME_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.COMPASS),
            Slimefun.getLocalization().getMessage(p, "guide.wiki.results-title").replace("%query%", query),
            "",
            Slimefun.getLocalization().getMessage(p, "guide.wiki.results-found").replace("%count%", String.valueOf(matches.size()))),
            ChestMenuUtils.getEmptyClickHandler());

        int pages = pageCount(matches.size());
        int offset = (page - 1) * PAGE_SIZE;

        for (int i = 0; i < PAGE_SIZE && offset + i < matches.size(); i++) {
            SlimefunItem item = matches.get(offset + i);
            int slot = CONTENT_START + i;

            menu.addItem(slot, Slimefun.getItemTranslationService().getDisplayItem(p, item));
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                WikiPage.open(pl, guide, item, () -> openSearchResults(pl, guide, query, page));
                return false;
            });
        }

        addPagination(menu, p, page, pages, (pl, target) -> openSearchResults(pl, guide, query, target));
        menu.open(p);
    }

    /** The topic's display name in the player's language ({@code guide.wiki-topics.<id>.name}); falls back to the registered name (e.g. for addon-registered topics with no message key). */
    @Nonnull
    private static String localizedTopicName(@Nonnull Player p, @Nonnull WikiTopic topic) {
        String value = Slimefun.getLocalization().getMessage(p, "guide.wiki-topics." + topic.getId() + ".name");
        return (value == null || value.startsWith("! Missing")) ? topic.getDisplayName() : value;
    }

    /** The topic's summary in the player's language ({@code guide.wiki-topics.<id>.summary}); falls back to the registered summary. */
    @Nonnull
    private static String localizedTopicSummary(@Nonnull Player p, @Nonnull WikiTopic topic) {
        String value = Slimefun.getLocalization().getMessage(p, "guide.wiki-topics." + topic.getId() + ".summary");
        return (value == null || value.startsWith("! Missing")) ? topic.getSummary() : value;
    }

    /** Builds a header tile from localized message keys: name line, a blank, then the lore lines. */
    @Nonnull
    private static ItemStack tile(@Nonnull Player p, @Nonnull XMaterial icon, @Nonnull String nameKey, @Nonnull String loreKey) {
        List<String> lore = new ArrayList<>();
        lore.add(Slimefun.getLocalization().getMessage(p, nameKey));
        lore.add("");
        lore.addAll(Slimefun.getLocalization().getMessages(p, loreKey));
        return CustomItemStack.create(MaterialCompat.stack(icon), lore);
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
}
