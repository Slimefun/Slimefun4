package io.github.thebusybiscuit.slimefun5.implementation.guide;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.HandCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.bakedlibs.dough.chat.ChatInput;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.bakedlibs.dough.recipes.MinecraftRecipe;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.groups.LockedItemGroup;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.api.researches.Research;
import io.github.thebusybiscuit.slimefun5.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun5.core.guide.AddonVisibility;
import io.github.thebusybiscuit.slimefun5.core.guide.GuideHistory;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun5.core.guide.options.AddonVisibilityMenu;
import io.github.thebusybiscuit.slimefun5.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun5.core.guide.categories.CategoryItemGroup;
import io.github.thebusybiscuit.slimefun5.core.guide.categories.CategoryMenuBuilder;
import io.github.thebusybiscuit.slimefun5.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun5.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun5.core.services.localization.ItemTranslationService;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.AsyncRecipeChoiceTask;
import io.github.thebusybiscuit.slimefun5.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedItemFlag;
import io.github.thebusybiscuit.slimefun5.utils.itemstack.SlimefunGuideItem;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;

/**
 * The {@link SurvivalSlimefunGuide} is the standard version of our {@link SlimefunGuide}.
 * It uses an {@link Inventory} to display {@link SlimefunGuide} contents.
 *
 * @author TheBusyBiscuit
 *
 * @see SlimefunGuide
 * @see SlimefunGuideImplementation
 * @see CheatSheetSlimefunGuide
 *
 */
public class SurvivalSlimefunGuide implements SlimefunGuideImplementation {

    private static final int MAX_ITEM_GROUPS = 36;

    private final int[] recipeSlots = { 3, 4, 5, 12, 13, 14, 21, 22, 23 };
    // Built lazily: the guide is constructed during startup before the localization service exists, so the
    // localized name/lore can only be resolved on first access (by which time a player can request it).
    private ItemStack item;
    private final boolean showVanillaRecipes;
    private final boolean showHiddenItemGroupsInSearch;

    public SurvivalSlimefunGuide(boolean showVanillaRecipes, boolean showHiddenItemGroupsInSearch) {
        this.showVanillaRecipes = showVanillaRecipes;
        this.showHiddenItemGroupsInSearch = showHiddenItemGroupsInSearch;
    }

    @Override
    public @Nonnull SlimefunGuideMode getMode() {
        return SlimefunGuideMode.SURVIVAL_MODE;
    }

    @Override
    public @Nonnull ItemStack getItem() {
        if (item == null && Slimefun.getLocalization() != null) {
            item = new SlimefunGuideItem(this, Slimefun.getLocalization().getMessage("guide.item.name"));
        }

        // Localization not ready yet (very early access): a transient English copy, not cached.
        return item != null ? item : new SlimefunGuideItem(this, "&aSlimefun Guide &7(Chest GUI)");
    }

    protected final boolean isSurvivalMode() {
        return getMode() != SlimefunGuideMode.CHEAT_MODE;
    }

    /**
     * Returns a {@link List} of visible {@link ItemGroup} instances that the {@link SlimefunGuide} would display.
     *
     * @param p
     *            The {@link Player} who opened his {@link SlimefunGuide}
     * @param profile
     *            The {@link PlayerProfile} of the {@link Player}
     *
     * @return a {@link List} of visible {@link ItemGroup} instances
     */
    protected @Nonnull List<ItemGroup> getVisibleItemGroups(@Nonnull Player p, @Nonnull PlayerProfile profile) {
        List<ItemGroup> tiles = buildMainMenuTiles(p, profile);

        if (tiles.isEmpty()) {
            // The player's addon-visibility set hid everything (a corrupt/stale set that hides all content -
            // unreachable via the menu, which keeps at least one shown). The guide must never be blank
            // because of visibility, so re-render once with filtering disabled, then repair the set.
            List<ItemGroup> fallback = new ArrayList<>();
            AddonVisibility.runWithoutFiltering(() -> fallback.addAll(buildMainMenuTiles(p, profile)));

            if (!fallback.isEmpty()) {
                AddonVisibility.clear(p);
                Slimefun.logger().log(Level.WARNING, "Addon visibility hid all guide content for {0}; reset it and showing everything.", p.getName());
                return fallback;
            }
        }

        return tiles;
    }

    /**
     * The main-menu tiles for the current layout. Classic mirrors the old upstream main menu: the raw
     * {@link ItemGroup} tiles (Slimefun's own groups plus every addon's own groups), honoring addon
     * visibility. Categorized uses the shared-category system, where each category opens into
     * {@code <Addon> <Type>} sections.
     */
    @Nonnull
    private List<ItemGroup> buildMainMenuTiles(@Nonnull Player p, @Nonnull PlayerProfile profile) {
        List<ItemGroup> visible = collectVisibleCategories(p, profile);

        boolean categorized;
        try {
            categorized = SlimefunGuideSettings.isMainMenuCategorized(p);
        } catch (Exception | LinkageError x) {
            // Reading the per-player layout option can fail if the guide/localization isn't fully ready;
            // default to the richer categorized view rather than let the menu fail to build.
            categorized = true;
        }

        if (!categorized) {
            return visible;
        }

        return CategoryMenuBuilder.build(p, visible, Slimefun.getGuideCategories());
    }

    protected @Nonnull List<ItemGroup> collectVisibleCategories(@Nonnull Player p, @Nonnull PlayerProfile profile) {
        List<ItemGroup> groups = new LinkedList<>();

        for (ItemGroup group : Slimefun.getRegistry().getAllItemGroups()) {
            if (group instanceof CategoryItemGroup) {
                continue;
            }

            String namespace = group.getKey().getNamespace();

            // Respect the player's per-addon visibility in EVERY layout. Previously only the categorized
            // path filtered hidden addons, so switching the guide to the classic (flat) layout made hidden
            // addons reappear.
            if (AddonVisibility.isHidden(p, namespace)) {
                continue;
            }

            // Deprecated addon custom guide screens are taken OUT of the guide entirely - not shown, not
            // interactive (rendering them let cheat-mode players pull infinite free items). Their items
            // still appear via the shared categories. Standard NestedItemGroups (plain nested browsing) and
            // core's own flex groups (e.g. seasonal) are kept.
            if (group instanceof FlexItemGroup && !(group instanceof NestedItemGroup) && !"slimefun".equals(namespace)) {
                warnDeprecatedCustomGuideUi(group);
                continue;
            }

            try {
                if (group instanceof FlexItemGroup) {
                    FlexItemGroup flexItemGroup = (FlexItemGroup) group;
                    if (flexItemGroup.isVisible(p, profile, getMode())) {
                        groups.add(group);
                    }
                } else if (!group.isHidden(p)) {
                    groups.add(group);
                }
            } catch (Exception | LinkageError x) {
                SlimefunAddon addon = group.getAddon();

                if (addon != null) {
                    addon.getLogger().log(Level.SEVERE, x, () -> "Could not display item group: " + group);
                } else {
                    Slimefun.logger().log(Level.SEVERE, x, () -> "Could not display item group: " + group);
                }
            }
        }

        return groups;
    }

    @Override
    public void openMainMenu(PlayerProfile profile, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        if (isSurvivalMode()) {
            GuideHistory history = profile.getGuideHistory();
            history.clear();
            history.setMainMenuPage(page);
        }

        ChestMenu menu = create(p);

        List<ItemGroup> itemGroups;
        try {
            itemGroups = getVisibleItemGroups(p, profile);
        } catch (Exception | LinkageError x) {
            // A failure while building the menu must never leave the player with an unopened/blank guide.
            itemGroups = new ArrayList<>();
            Slimefun.logger().log(Level.SEVERE, x, () -> "Failed to build the guide main menu for " + p.getName());
        }

        if (itemGroups.isEmpty()) {
            // Surface the reason rather than silently showing an empty guide - the counts pinpoint which
            // link (categories registered / groups visible / items loaded) is broken on this server.
            int cats = Slimefun.getGuideCategories().getAll().size();
            int enabled = Slimefun.getRegistry().getEnabledSlimefunItems().size();
            String breakdown;
            try {
                breakdown = io.github.thebusybiscuit.slimefun5.core.guide.categories.CategoryMenuBuilder
                    .diagnose(p, collectVisibleCategories(p, profile), Slimefun.getGuideCategories());
            } catch (Exception | LinkageError x) {
                breakdown = "diagnose failed: " + x;
            }
            Slimefun.logger().log(Level.WARNING, "Guide main menu is empty: registeredCategories={0}, enabledItems={1}. {2}",
                new Object[] { cats, enabled, breakdown });
        }

        int index = 9;
        createHeader(p, profile, menu);

        // Addon Visibility entry (main menu only).
        List<String> addonVisibilityLore = new ArrayList<>();
        addonVisibilityLore.add(Slimefun.getLocalization().getMessage(p, "guide.addon-visibility.name"));
        addonVisibilityLore.add("");
        addonVisibilityLore.addAll(Slimefun.getLocalization().getMessages(p, "guide.addon-visibility.lore"));
        menu.addItem(4, CustomItemStack.create(XMaterial.BOOKSHELF.parseMaterial(), addonVisibilityLore));
        menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
            AddonVisibilityMenu.open(pl, this.item);
            return false;
        });

        int target = (MAX_ITEM_GROUPS * (page - 1)) - 1;

        while (target < (itemGroups.size() - 1) && index < MAX_ITEM_GROUPS + 9) {
            target++;

            ItemGroup group = itemGroups.get(target);
            showItemGroup(menu, p, profile, group, index);

            index++;
        }

        int pages = target == itemGroups.size() - 1 ? page : (itemGroups.size() - 1) / MAX_ITEM_GROUPS + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;

            if (next != page && next > 0) {
                openMainMenu(profile, next);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;

            if (next != page && next <= pages) {
                openMainMenu(profile, next);
            }

            return false;
        });

        // Functional addon widgets (e.g. an advancement tree) get a dedicated button, top or bottom row per
        // the widget's own preference, shown on every page and on both layouts.
        placeWidgets(menu, p, profile);

        menu.open(p);
    }

    // Widget buttons are centered in the bottom row; once it's full (>5) the extras spill into the free
    // header slots (1 = settings, 4 = addon-visibility, 7 = search are taken), also centered.
    private static final int[] BOTTOM_WIDGET_SLOTS = { 47, 48, 49, 50, 51 };
    private static final int[] TOP_WIDGET_SLOTS = { 0, 2, 3, 5, 6, 8 };

    private void placeWidgets(@Nonnull ChestMenu menu, @Nonnull Player p, @Nonnull PlayerProfile profile) {
        List<io.github.thebusybiscuit.slimefun5.core.guide.widgets.GuideWidget> widgets = Slimefun.getGuideWidgets().getAll();

        if (widgets.isEmpty()) {
            return;
        }

        int bottomCount = Math.min(widgets.size(), BOTTOM_WIDGET_SLOTS.length);
        int topCount = Math.min(widgets.size() - bottomCount, TOP_WIDGET_SLOTS.length);
        int[] slots = new int[bottomCount + topCount];
        System.arraycopy(centeredSlots(BOTTOM_WIDGET_SLOTS, bottomCount), 0, slots, 0, bottomCount);
        System.arraycopy(centeredSlots(TOP_WIDGET_SLOTS, topCount), 0, slots, bottomCount, topCount);

        for (int i = 0; i < slots.length; i++) {
            io.github.thebusybiscuit.slimefun5.core.guide.widgets.GuideWidget widget = widgets.get(i);
            menu.replaceExistingItem(slots[i], widgetTile(p, widget));
            menu.addMenuClickHandler(slots[i], (pl, s, item, action) -> {
                widget.open(pl, profile);
                return false;
            });
        }

        if (slots.length < widgets.size()) {
            Slimefun.logger().warning("[Guide] " + (widgets.size() - slots.length) + " guide widget(s) beyond the "
                + slots.length + " available button slots are not shown.");
        }
    }

    /** The {@code count} middle slots of {@code available}, so buttons sit centered rather than left-aligned. */
    @Nonnull
    private static int[] centeredSlots(@Nonnull int[] available, int count) {
        int n = Math.max(0, Math.min(count, available.length));
        int start = (available.length - n) / 2;
        int[] result = new int[n];
        System.arraycopy(available, start, result, 0, n);
        return result;
    }

    @Nonnull
    private ItemStack widgetTile(@Nonnull Player p, @Nonnull io.github.thebusybiscuit.slimefun5.core.guide.widgets.GuideWidget widget) {
        return CustomItemStack.create(MaterialCompat.stack(widget.getIcon()),
            ChatColor.translateAlternateColorCodes('&', widget.getDefaultName()),
            "",
            Slimefun.getLocalization().getMessage(p, "guide.categories-meta.open"));
    }

    /**
     * Opens the contents of a single category: a paginated grid of its member groups, with a back button
     * to the main menu. A category with a single member opens that member directly.
     */
    public void openCategoryContents(@Nonnull PlayerProfile profile, @Nonnull CategoryItemGroup categoryGroup, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        List<ItemGroup> categories = categoryGroup.getMembers();

        if (categories.size() == 1) {
            openItemGroup(profile, categories.get(0), 1);
            return;
        }

        if (isSurvivalMode()) {
            profile.getGuideHistory().add(categoryGroup, page);
        }

        ChestMenu menu = create(p);
        createHeader(p, profile, menu);
        addBackButton(menu, 1, p, profile);

        int index = 9;
        int target = (MAX_ITEM_GROUPS * (page - 1)) - 1;

        while (target < (categories.size() - 1) && index < MAX_ITEM_GROUPS + 9) {
            target++;
            showItemGroup(menu, p, profile, categories.get(target), index);
            index++;
        }

        int pages = target == categories.size() - 1 ? page : (categories.size() - 1) / MAX_ITEM_GROUPS + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;

            if (next != page && next > 0) {
                openCategoryContents(profile, categoryGroup, next);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;

            if (next != page && next <= pages) {
                openCategoryContents(profile, categoryGroup, next);
            }

            return false;
        });

        menu.open(p);
    }

    /**
     * The classic-layout view of a category: a flat, paginated grid of ALL its items (core + every addon),
     * with no per-source sub-sections and a back button to the main menu.
     */
    public void openCategoryItemsFlat(@Nonnull PlayerProfile profile, @Nonnull CategoryItemGroup categoryGroup, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        List<SlimefunItem> items = categoryGroup.getAllItems();

        if (isSurvivalMode()) {
            profile.getGuideHistory().add(categoryGroup, page);
        }

        ChestMenu menu = create(p);
        createHeader(p, profile, menu);
        addBackButton(menu, 1, p, profile);

        int pages = Math.max(1, (items.size() - 1) / MAX_ITEM_GROUPS + 1);

        int index = 9;
        int itemIndex = MAX_ITEM_GROUPS * (page - 1);
        for (int i = 0; i < MAX_ITEM_GROUPS; i++) {
            int target = itemIndex + i;

            if (target >= items.size()) {
                break;
            }

            SlimefunItem sfitem = items.get(target);

            if (!sfitem.isDisabledIn(p.getWorld())) {
                displaySlimefunItem(menu, categoryGroup, p, profile, sfitem, page, index);
                index++;
            }
        }

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;

            if (next != page && next > 0) {
                openCategoryItemsFlat(profile, categoryGroup, next);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;

            if (next != page && next <= pages) {
                openCategoryItemsFlat(profile, categoryGroup, next);
            }

            return false;
        });

        menu.open(p);
    }

    /** One consistent colour for every real category tile, so addons (which colour/prefix their group
     *  names inconsistently) don't make the guide's categories look mismatched. */
    private static final String UNIFIED_GROUP_COLOR = ChatColor.YELLOW.toString();

    /**
     * The category tile for a group, with its display name normalised to {@link #UNIFIED_GROUP_COLOR}.
     * Category tiles ({@link CategoryItemGroup}) are core-defined and intentionally colour-coded per
     * category, so they are left untouched; every other group (core or addon) is unified.
     */
    @Nonnull
    private ItemStack unifiedGroupTile(@Nonnull Player p, @Nonnull ItemGroup group) {
        ItemStack tile = group.getItem(p);

        if (group instanceof CategoryItemGroup) {
            return tile;
        }

        ItemStack copy = tile.clone();
        ItemMeta meta = copy.getItemMeta();

        if (meta != null && meta.hasDisplayName()) {
            meta.setDisplayName(UNIFIED_GROUP_COLOR + ChatColor.stripColor(meta.getDisplayName()));
            copy.setItemMeta(meta);
        }

        return copy;
    }

    private void showItemGroup(ChestMenu menu, Player p, PlayerProfile profile, ItemGroup group, int index) {
        if (!(group instanceof LockedItemGroup) || !isSurvivalMode() || ((LockedItemGroup) group).hasUnlocked(p, profile)) {
            menu.addItem(index, unifiedGroupTile(p, group));
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                openItemGroup(profile, group, 1);
                return false;
            });
        } else {
            List<String> lore = new ArrayList<>();
            lore.add("");

            for (String line : Slimefun.getLocalization().getMessages(p, "guide.locked-itemgroup")) {
                lore.add(ChatColor.WHITE + line);
            }

            lore.add("");

            for (ItemGroup parent : ((LockedItemGroup) group).getParents()) {
                lore.add(parent.getItem(p).getItemMeta().getDisplayName());
            }

            menu.addItem(index, CustomItemStack.create(Material.BARRIER, "&4" + Slimefun.getLocalization().getMessage(p, "guide.locked") + " &7- " + UNIFIED_GROUP_COLOR + ChatColor.stripColor(group.getItem(p).getItemMeta().getDisplayName()), lore.toArray(new String[0])));
            menu.addMenuClickHandler(index, ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void openItemGroup(PlayerProfile profile, ItemGroup itemGroup, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        // Core's category tiles and standard nested groups keep their normal open: CategoryItemGroup is the
        // category mechanism, and NestedItemGroup is plain nested browsing (still "categories + item lists").
        // Every OTHER FlexItemGroup is a deprecated addon custom screen - taken out of the guide, so it must
        // never open (guards search/history paths); bounce to the main menu instead.
        if (itemGroup instanceof CategoryItemGroup || itemGroup instanceof NestedItemGroup) {
            ((FlexItemGroup) itemGroup).open(p, profile, getMode());
            return;
        }

        if (itemGroup instanceof FlexItemGroup) {
            warnDeprecatedCustomGuideUi(itemGroup);
            openMainMenu(profile, profile.getGuideHistory().getMainMenuPage());
            return;
        }

        List<SlimefunItem> items = itemGroup.getItems();

        if (isSurvivalMode()) {
            profile.getGuideHistory().add(itemGroup, page);
        }

        ChestMenu menu = create(p);
        createHeader(p, profile, menu);

        addBackButton(menu, 1, p, profile);

        int pages = (items.size() - 1) / MAX_ITEM_GROUPS + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;

            if (next != page && next > 0) {
                openItemGroup(profile, itemGroup, next);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;

            if (next != page && next <= pages) {
                openItemGroup(profile, itemGroup, next);
            }

            return false;
        });

        int index = 9;
        int itemGroupIndex = MAX_ITEM_GROUPS * (page - 1);

        for (int i = 0; i < MAX_ITEM_GROUPS; i++) {
            int target = itemGroupIndex + i;

            if (target >= items.size()) {
                break;
            }

            SlimefunItem sfitem = items.get(target);

            if (!sfitem.isDisabledIn(p.getWorld())) {
                displaySlimefunItem(menu, itemGroup, p, profile, sfitem, page, index);
                index++;
            }
        }

        menu.open(p);
    }

    private final java.util.Set<String> warnedCustomGuideUis = java.util.concurrent.ConcurrentHashMap.newKeySet();

    private void warnDeprecatedCustomGuideUi(@Nonnull ItemGroup group) {
        if (warnedCustomGuideUis.add(group.getKey().toString())) {
            SlimefunAddon addon = group.getAddon();
            String owner = addon != null ? addon.getName() : "unknown";
            Slimefun.logger().warning("[Guide] Addon '" + owner + "' uses a custom guide screen (" + group.getKey()
                + "). Custom guide layouts are deprecated - the guide only shows categories and item lists. "
                + "It is now rendered as a plain item list. Use a GuideWidget button for functional screens.");
        }
    }

    private void displaySlimefunItem(ChestMenu menu, ItemGroup itemGroup, Player p, PlayerProfile profile, SlimefunItem sfitem, int page, int index) {
        Research research = sfitem.getResearch();

        if (isSurvivalMode() && !hasPermission(p, sfitem)) {
            List<String> message = Slimefun.getPermissionsService().getLore(sfitem);
            menu.addItem(index, CustomItemStack.create(ChestMenuUtils.getNoPermissionItem(), Slimefun.getItemTranslationService().getName(p, sfitem), message.toArray(new String[0])));
            menu.addMenuClickHandler(index, ChestMenuUtils.getEmptyClickHandler());
        } else if (isSurvivalMode() && research != null && !profile.hasUnlocked(research)) {
            menu.addItem(index, CustomItemStack.create(ChestMenuUtils.getNotResearchedItem(), ChatColor.WHITE + ChatColor.stripColor(Slimefun.getItemTranslationService().getName(p, sfitem)), "&4&l" + Slimefun.getLocalization().getMessage(p, "guide.locked"), "", Slimefun.getLocalization().getMessage(p, "guide.research.unlock"), "", Slimefun.getLocalization().getMessage(p, "guide.research.cost").replace("%levels%", String.valueOf(research.getCost()))));
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                research.unlockFromGuide(this, p, profile, sfitem, itemGroup, page);
                return false;
            });
        } else {
            menu.addItem(index, sfitem.getItem());
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                try {
                    if (isSurvivalMode()) {
                        displayItem(profile, sfitem, true);
                    } else if (pl.hasPermission("slimefun.cheat.items")) {
                        if (sfitem instanceof MultiBlockMachine) {
                            Slimefun.getLocalization().sendMessage(pl, "guide.cheat.no-multiblocks");
                        } else {
                            ItemStack clonedItem = sfitem.getItem().clone();

                            if (action.isShiftClicked()) {
                                clonedItem.setAmount(clonedItem.getMaxStackSize());
                            }

                            pl.getInventory().addItem(clonedItem);
                        }
                    } else {
                        /*
                         * Fixes #3548 - If for whatever reason,
                         * an unpermitted players gets access to this guide,
                         * this will be our last line of defense to prevent any exploit.
                         */
                        Slimefun.getLocalization().sendMessage(pl, "messages.no-permission", true);
                    }
                } catch (Exception | LinkageError x) {
                    printErrorMessage(pl, sfitem, x);
                }

                return false;
            });
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void openSearch(PlayerProfile profile, String input, boolean addToHistory) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.search.inventory").replace("%item%", ChatUtils.crop(ChatColor.WHITE, input)));
        String searchTerm = ChatColor.stripColor(input.toLowerCase(Locale.ROOT));

        if (addToHistory) {
            profile.getGuideHistory().add(searchTerm);
        }

        menu.setEmptySlotsClickable(false);
        createHeader(p, profile, menu);
        addBackButton(menu, 1, p, profile);

        int index = 9;
        // Find items and add them
        for (SlimefunItem slimefunItem : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            if (index == 44) {
                break;
            }

            if (!slimefunItem.isHidden()
                && !AddonVisibility.isHidden(p, slimefunItem.getItemGroup().getKey().getNamespace())
                && isItemGroupAccessible(p, slimefunItem)
                && isSearchFilterApplicable(p, slimefunItem, searchTerm)) {
                ItemStack itemstack = CustomItemStack.create(slimefunItem.getItem(), meta -> {
                    ItemGroup itemGroup = slimefunItem.getItemGroup();
                    String categoryId = itemGroup.getCategoryId() != null ? itemGroup.getCategoryId() : io.github.thebusybiscuit.slimefun5.core.guide.categories.DefaultGuideCategories.MISC;
                    io.github.thebusybiscuit.slimefun5.core.guide.categories.GuideCategory category = Slimefun.getGuideCategories().getById(categoryId);
                    String categoryLabel = Slimefun.getLocalization().getMessage(p, "guide.categories." + categoryId);
                    if (categoryLabel == null || categoryLabel.startsWith("guide.categories.")) {
                        categoryLabel = category != null ? category.getDefaultName()
                            : (itemGroup.getAddon() != null ? "&e" + itemGroup.getAddon().getName() : categoryId);
                    }
                    String themeName = ChatColor.translateAlternateColorCodes('&', categoryLabel);
                    meta.setLore(Arrays.asList("", ChatColor.DARK_GRAY + "\u21E8 " + ChatColor.WHITE + themeName + ChatColor.GRAY + " \u25B8 " + ChatColor.WHITE + itemGroup.getDisplayName(p)));
                    VersionedItemFlag.addFlags(meta, VersionedItemFlag.HIDE_ATTRIBUTES, VersionedItemFlag.HIDE_ENCHANTS, VersionedItemFlag.HIDE_ADDITIONAL_TOOLTIP);
                });

                menu.addItem(index, itemstack);
                menu.addMenuClickHandler(index, (pl, slot, itm, action) -> {
                    try {
                        if (!isSurvivalMode()) {
                            pl.getInventory().addItem(slimefunItem.getItem().clone());
                        } else {
                            displayItem(profile, slimefunItem, true);
                        }
                    } catch (Exception | LinkageError x) {
                        printErrorMessage(pl, slimefunItem, x);
                    }

                    return false;
                });

                index++;
            }
        }

        menu.open(p);
    }

    @ParametersAreNonnullByDefault
    private boolean isItemGroupAccessible(Player p, SlimefunItem slimefunItem) {
        return showHiddenItemGroupsInSearch || slimefunItem.getItemGroup().isAccessible(p);
    }

    @ParametersAreNonnullByDefault
    private boolean isSearchFilterApplicable(Player p, SlimefunItem slimefunItem, String searchTerm) {
        String englishName = ChatColor.stripColor(slimefunItem.getItemName()).toLowerCase(Locale.ROOT);

        if (!englishName.isEmpty() && englishName.contains(searchTerm)) {
            return true;
        }

        // Also match the item's name in the player's language, so search works for translated names.
        String translatedName = ChatColor.stripColor(Slimefun.getItemTranslationService().getName(p, slimefunItem)).toLowerCase(Locale.ROOT);
        return !translatedName.isEmpty() && translatedName.contains(searchTerm);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void displayItem(PlayerProfile profile, ItemStack item, int index, boolean addToHistory) {
        Player p = profile.getPlayer();

        if (p == null || item == null || item.getType() == Material.AIR) {
            return;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem != null) {
            displayItem(profile, sfItem, addToHistory);
            return;
        }

        if (!showVanillaRecipes) {
            return;
        }

        Recipe[] recipes = Slimefun.getMinecraftRecipeService().getRecipesFor(item);

        if (recipes.length == 0) {
            return;
        }

        showMinecraftRecipe(recipes, index, item, profile, p, addToHistory);
    }

    private void showMinecraftRecipe(Recipe[] recipes, int index, ItemStack item, PlayerProfile profile, Player p, boolean addToHistory) {
        Recipe recipe = recipes[index];

        ItemStack[] recipeItems = new ItemStack[9];
        RecipeType recipeType = RecipeType.NULL;
        ItemStack result = null;

        Optional<MinecraftRecipe<? super Recipe>> optional = MinecraftRecipe.of(recipe);
        AsyncRecipeChoiceTask task = new AsyncRecipeChoiceTask();

        if (optional.isPresent()) {
            showRecipeChoices(recipe, recipeItems, task);

            recipeType = new RecipeType(optional.get());
            result = recipe.getResult();
        } else {
            recipeItems = new ItemStack[] { null, null, null, null, CustomItemStack.create(Material.BARRIER, Slimefun.getLocalization().getMessage(p, "guide.recipe.error")), null, null, null, null };
        }

        ChestMenu menu = create(p);

        if (addToHistory) {
            profile.getGuideHistory().add(item, index);
        }

        displayItem(menu, profile, p, item, result, recipeType, recipeItems, task);

        if (recipes.length > 1) {
            for (int i = 27; i < 36; i++) {
                menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }

            menu.addItem(28, ChestMenuUtils.getPreviousButton(p, index + 1, recipes.length), (pl, slot, action, stack) -> {
                if (index > 0) {
                    showMinecraftRecipe(recipes, index - 1, item, profile, p, true);
                }
                return false;
            });

            menu.addItem(34, ChestMenuUtils.getNextButton(p, index + 1, recipes.length), (pl, slot, action, stack) -> {
                if (index < recipes.length - 1) {
                    showMinecraftRecipe(recipes, index + 1, item, profile, p, true);
                }
                return false;
            });
        }

        menu.open(p);

        if (!task.isEmpty()) {
            task.start(menu.toInventory());
        }
    }

    private <T extends Recipe> void showRecipeChoices(T recipe, ItemStack[] recipeItems, AsyncRecipeChoiceTask task) {
        RecipeChoice[] choices = Slimefun.getMinecraftRecipeService().getRecipeShape(recipe);

        if (choices.length == 1 && choices[0] instanceof MaterialChoice) {
            MaterialChoice materialChoice = (MaterialChoice) choices[0];            recipeItems[4] = new ItemStack(materialChoice.getChoices().get(0));

            if (materialChoice.getChoices().size() > 1) {
                task.add(recipeSlots[4], materialChoice);
            }
        } else {
            for (int i = 0; i < choices.length; i++) {
                if (choices[i] instanceof MaterialChoice) {
                    MaterialChoice materialChoice = (MaterialChoice) choices[i];                    recipeItems[i] = new ItemStack(materialChoice.getChoices().get(0));

                    if (materialChoice.getChoices().size() > 1) {
                        task.add(recipeSlots[i], materialChoice);
                    }
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        ChestMenu menu = create(p);

        if (io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide.showExternalLinks()) {
            // Every item links to the wiki (uniform per-plugin URL), even if its page isn't authored yet -
            // an item's explicit wiki page still wins. See WikiLinks.
            String wikiUrl = io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiLinks.urlFor(item);
            menu.addItem(8, CustomItemStack.create(XMaterial.KNOWLEDGE_BOOK.parseMaterial(), ChatColor.WHITE + Slimefun.getLocalization().getMessage(p, "guide.tooltips.wiki"), "", ChatColor.GRAY + "\u21E8 " + ChatColor.GREEN + Slimefun.getLocalization().getMessage(p, "guide.tooltips.open-itemgroup")));
            menu.addMenuClickHandler(8, (pl, slot, itemstack, action) -> {
                pl.closeInventory();
                ChatUtils.sendURL(pl, wikiUrl);
                return false;
            });
        } else {
            // External links disabled in config: fill the slot with background glass.
            menu.addItem(8, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        AsyncRecipeChoiceTask task = new AsyncRecipeChoiceTask();

        if (addToHistory) {
            profile.getGuideHistory().add(item);
        }

        ItemStack result = item.getRecipeOutput();
        RecipeType recipeType = item.getRecipeType();
        ItemStack[] recipe = item.getRecipe();

        displayItem(menu, profile, p, item, result, recipeType, recipe, task);

        if (item instanceof RecipeDisplayItem) {
            RecipeDisplayItem recipeDisplayItem = (RecipeDisplayItem) item;            displayRecipes(p, profile, menu, recipeDisplayItem, 0);
        }

        menu.open(p);

        if (!task.isEmpty()) {
            task.start(menu.toInventory());
        }
    }

    private void displayItem(ChestMenu menu, PlayerProfile profile, Player p, Object item, ItemStack output, RecipeType recipeType, ItemStack[] recipe, AsyncRecipeChoiceTask task) {
        addBackButton(menu, 0, p, profile);

        MenuClickHandler clickHandler = (pl, slot, itemstack, action) -> {
            try {
                if (itemstack != null && itemstack.getType() != Material.BARRIER) {
                    displayItem(profile, itemstack, 0, true);
                }
            } catch (Exception | LinkageError x) {
                printErrorMessage(pl, x);
            }
            return false;
        };

        boolean isSlimefunRecipe = item instanceof SlimefunItem;

        for (int i = 0; i < 9; i++) {
            ItemStack recipeItem = getDisplayItem(p, isSlimefunRecipe, recipe[i]);
            menu.addItem(recipeSlots[i], recipeItem, clickHandler);

            if (recipeItem != null && item instanceof MultiBlockMachine) {
                for (Tag<Material> tag : MultiBlock.getSupportedTags()) {
                    if (tag.isTagged(recipeItem.getType())) {
                        task.add(recipeSlots[i], tag);
                        break;
                    }
                }
            }
        }

        ItemStack machineIcon = recipeType.getItem(p);
        if (machineIcon == null || machineIcon.getType() == Material.AIR) {
            // The machine/multiblock icon (slot 10) came back empty - surface why so a missing machine in
            // the recipe view is diagnosable on the live server rather than silently blank.
            Slimefun.logger().warning("[Guide] Recipe machine icon is empty for recipe type '" + recipeType.getKey()
                + "' (machine item id=" + (recipeType.getMachine() != null ? recipeType.getMachine().getId() : "none")
                + ", toItem=" + (recipeType.toItem() == null ? "null" : recipeType.toItem().getType()) + ").");
        }
        menu.addItem(10, machineIcon, ChestMenuUtils.getEmptyClickHandler());

        // The packet layer translates the result per viewer; the canonical template is id-only here.
        ItemStack displayedOutput = isSlimefunRecipe ? ((SlimefunItem) item).getItem() : output;
        menu.addItem(16, displayedOutput, ChestMenuUtils.getEmptyClickHandler());
    }

    @ParametersAreNonnullByDefault
    public void createHeader(Player p, PlayerProfile profile, ChestMenu menu) {
        Validate.notNull(p, "The Player cannot be null!");
        Validate.notNull(profile, "The Profile cannot be null!");
        Validate.notNull(menu, "The Inventory cannot be null!");

        for (int i = 0; i < 9; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        // Settings Panel
        menu.addItem(1, ChestMenuUtils.getMenuButton(p));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            SlimefunGuideSettings.openSettings(pl, HandCompat.getMainHand(pl.getInventory()));
            return false;
        });

        // Search feature!
        menu.addItem(7, ChestMenuUtils.getSearchButton(p));
        menu.addMenuClickHandler(7, (pl, slot, item, action) -> {
            pl.closeInventory();

            Slimefun.getLocalization().sendMessage(pl, "guide.search.message");
            ChatInput.waitForPlayer(Slimefun.instance(), pl, msg -> SlimefunGuide.openSearch(profile, msg, getMode(), isSurvivalMode()));

            return false;
        });

        for (int i = 45; i < 54; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    private void addBackButton(ChestMenu menu, int slot, Player p, PlayerProfile profile) {
        GuideHistory history = profile.getGuideHistory();

        if (isSurvivalMode() && history.size() > 1) {
            menu.addItem(slot, ChestMenuUtils.getBackButton(p, Slimefun.getLocalization().getMessages(p, "guide.back.page").toArray(new String[0])));

            menu.addMenuClickHandler(slot, (pl, s, is, action) -> {
                if (action.isShiftClicked()) {
                    openMainMenu(profile, profile.getGuideHistory().getMainMenuPage());
                } else {
                    history.goBack(this);
                }
                return false;
            });

        } else {
            menu.addItem(slot, ChestMenuUtils.getBackButton(p, "", ChatColor.GRAY + Slimefun.getLocalization().getMessage(p, "guide.back.guide")));
            menu.addMenuClickHandler(slot, (pl, s, is, action) -> {
                openMainMenu(profile, profile.getGuideHistory().getMainMenuPage());
                return false;
            });
        }
    }

    @ParametersAreNonnullByDefault
    private static @Nonnull ItemStack getDisplayItem(Player p, boolean isSlimefunRecipe, ItemStack item) {
        if (isSlimefunRecipe) {
            SlimefunItem slimefunItem = SlimefunItem.getByItem(item);

            if (slimefunItem == null) {
                return item;
            }

            ItemTranslationService translations = Slimefun.getItemTranslationService();

            if (slimefunItem.canUse(p, false)) {
                // Preserve the recipe slot's required amount - returning the canonical item dropped it to 1,
                // so recipes needing several of a Slimefun ingredient wrongly showed a single item.
                ItemStack display = slimefunItem.getItem().clone();
                display.setAmount(item.getAmount());
                return display;
            }

            String lore = hasPermission(p, slimefunItem) ? Slimefun.getLocalization().getMessage(p, "guide.recipe.needs-unlock").replace("%group%", slimefunItem.getItemGroup().getDisplayName(p)) : Slimefun.getLocalization().getMessage(p, "guide.recipe.no-permission");
            return CustomItemStack.create(Material.BARRIER, translations.getName(p, slimefunItem), "&4&l" + Slimefun.getLocalization().getMessage(p, "guide.locked"), "", lore);
        } else {
            return item;
        }
    }

    @ParametersAreNonnullByDefault
    private void displayRecipes(Player p, PlayerProfile profile, ChestMenu menu, RecipeDisplayItem sfItem, int page) {
        List<ItemStack> recipes = sfItem.getDisplayRecipes();

        if (!recipes.isEmpty()) {
            menu.addItem(53, null);

            if (page == 0) {
                for (int i = 27; i < 36; i++) {
                    menu.replaceExistingItem(i, CustomItemStack.create(ChestMenuUtils.getBackground(), sfItem.getRecipeSectionLabel(p)));
                    menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
                }
            }

            int pages = (recipes.size() - 1) / 18 + 1;

            menu.replaceExistingItem(28, ChestMenuUtils.getPreviousButton(p, page + 1, pages));
            menu.addMenuClickHandler(28, (pl, slot, itemstack, action) -> {
                if (page > 0) {
                    displayRecipes(pl, profile, menu, sfItem, page - 1);
                    SoundEffect.GUIDE_BUTTON_CLICK_SOUND.playFor(pl);
                }

                return false;
            });

            menu.replaceExistingItem(34, ChestMenuUtils.getNextButton(p, page + 1, pages));
            menu.addMenuClickHandler(34, (pl, slot, itemstack, action) -> {
                if (recipes.size() > (18 * (page + 1))) {
                    displayRecipes(pl, profile, menu, sfItem, page + 1);
                    SoundEffect.GUIDE_BUTTON_CLICK_SOUND.playFor(pl);
                }

                return false;
            });

            int inputs = 36;
            int outputs = 45;

            for (int i = 0; i < 18; i++) {
                int slot;

                if (i % 2 == 0) {
                    slot = inputs;
                    inputs++;
                } else {
                    slot = outputs;
                    outputs++;
                }

                addDisplayRecipe(menu, profile, recipes, slot, i, page);
            }
        }
    }

    private void addDisplayRecipe(ChestMenu menu, PlayerProfile profile, List<ItemStack> recipes, int slot, int i, int page) {
        if ((i + (page * 18)) < recipes.size()) {
            ItemStack displayItem = recipes.get(i + (page * 18));

            /*
             * We want to clone this item to avoid corrupting the original
             * but we wanna make sure no stupid addon creator sneaked some nulls in here
             */
            if (displayItem != null) {
                displayItem = displayItem.clone();

                // Re-localize Slimefun items into the viewing player's language (vanilla items
                // are already shown in the client's locale by Minecraft). Only the name is
                // replaced, so any recipe-specific lore (amounts, chances) is preserved.
                Player p = profile.getPlayer();
                SlimefunItem sfItem = SlimefunItem.getByItem(displayItem);

                if (p != null && sfItem != null) {
                    String name = Slimefun.getItemTranslationService().getName(p, sfItem);
                    ItemMeta meta = displayItem.getItemMeta();

                    if (meta != null && name != null && !name.isEmpty()) {
                        meta.setDisplayName(name);
                        displayItem.setItemMeta(meta);
                    }
                }
            }

            menu.replaceExistingItem(slot, displayItem);

            if (page == 0) {
                menu.addMenuClickHandler(slot, (pl, s, itemstack, action) -> {
                    displayItem(profile, itemstack, 0, true);
                    return false;
                });
            }
        } else {
            menu.replaceExistingItem(slot, null);
            menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @ParametersAreNonnullByDefault
    private static boolean hasPermission(Player p, SlimefunItem item) {
        return Slimefun.getPermissionsService().hasPermission(p, item);
    }

    private @Nonnull ChestMenu create(@Nonnull Player p) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.main"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        return menu;
    }

    @ParametersAreNonnullByDefault
    private void printErrorMessage(Player p, Throwable x) {
        p.sendMessage(ChatColor.DARK_RED + "An internal server error has occurred. Please inform an admin, check the console for further info.");
        Slimefun.logger().log(Level.SEVERE, "An error has occurred while trying to open a SlimefunItem in the guide!", x);
    }

    @ParametersAreNonnullByDefault
    private void printErrorMessage(Player p, SlimefunItem item, Throwable x) {
        p.sendMessage(ChatColor.DARK_RED + "An internal server error has occurred. Please inform an admin, check the console for further info.");
        item.error("This item has caused an error message to be thrown while viewing it in the Slimefun guide.", x);
    }

}

