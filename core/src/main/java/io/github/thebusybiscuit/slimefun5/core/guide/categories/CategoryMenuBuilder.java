package io.github.thebusybiscuit.slimefun5.core.guide.categories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.core.guide.AddonVisibility;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

/**
 * Builds the transient {@link CategoryItemGroup} tiles for the categorized main menu. Slimefun's own
 * groups keep their curated category and their own names; every enabled ADDON item is pulled from the
 * registry, classified by type ({@link ItemTypeClassifier}) and grouped into "&lt;Addon&gt; &lt;Type&gt;"
 * section tiles under the matching type category - so an addon's items split across the shared categories
 * even when the addon's own guide is a single custom flex UI. Anything the classifier can't type lands in
 * the addon's Misc section (never dropped).
 */
public final class CategoryMenuBuilder {

    private CategoryMenuBuilder() {}

    @Nonnull
    public static List<ItemGroup> build(@Nonnull Player p, @Nonnull List<ItemGroup> visibleGroups, @Nonnull GuideCategoryRegistry registry) {
        return build(p, visibleGroups, Slimefun.getRegistry().getEnabledSlimefunItems(), registry);
    }

    /** Seam: the item source is a parameter so tests can pass constructed addon items without registering. */
    @Nonnull
    static List<ItemGroup> build(@Nonnull Player p, @Nonnull List<ItemGroup> visibleGroups, @Nonnull Collection<SlimefunItem> allItems, @Nonnull GuideCategoryRegistry registry) {
        // category id -> member tiles (core groups first, then addon "<Addon> <Type>" sections).
        Map<String, List<ItemGroup>> membersByCat = new LinkedHashMap<>();

        // 1. Slimefun's OWN groups keep their curated category + their own names. Addon groups are NOT
        //    bucketed here - their items are classified below (their guide UI stays in the classic layout).
        for (ItemGroup group : visibleGroups) {
            String ns = group.getKey().getNamespace();

            if (!"slimefun".equals(ns) || AddonVisibility.isHidden(p, ns)) {
                continue;
            }

            String declared = group.getCategoryId();
            String cat = (declared != null && registry.getById(declared) != null) ? declared : DefaultGuideCategories.MISC;
            membersByCat.computeIfAbsent(cat, k -> new ArrayList<>()).add(group);
        }

        // 2. Every enabled ADDON item, classified by type, grouped by (category id -> addon name -> items).
        Map<String, Map<String, List<SlimefunItem>>> addonItems = new LinkedHashMap<>();

        for (SlimefunItem item : allItems) {
            ItemGroup group = item.getItemGroup();

            if (group == null) {
                continue;
            }

            String ns = group.getKey().getNamespace();

            if ("slimefun".equals(ns) || AddonVisibility.isHidden(p, ns)) {
                continue; // core handled above; skip hidden addons
            }

            if (item.isHidden() || item.isDisabledIn(p.getWorld())) {
                continue;
            }

            String typeId = ItemTypeClassifier.classify(item);
            String cat = (typeId != null && registry.getById(typeId) != null) ? typeId : DefaultGuideCategories.MISC;
            String addonName = item.getAddon() != null ? item.getAddon().getName() : ns;

            addonItems
                .computeIfAbsent(cat, k -> new LinkedHashMap<>())
                .computeIfAbsent(addonName, k -> new ArrayList<>())
                .add(item);
        }

        // 3. Turn each (category, addon) bucket into a transient "<Addon> <Type>" section tile.
        for (Map.Entry<String, Map<String, List<SlimefunItem>>> catEntry : addonItems.entrySet()) {
            String cat = catEntry.getKey();

            for (Map.Entry<String, List<SlimefunItem>> addonEntry : catEntry.getValue().entrySet()) {
                membersByCat.computeIfAbsent(cat, k -> new ArrayList<>())
                    .add(section(cat, addonEntry.getKey(), addonEntry.getValue()));
            }
        }

        // 4. Emit one tile per non-empty registered category, in registry order.
        List<ItemGroup> tiles = new ArrayList<>();

        for (GuideCategory category : registry.getAll()) {
            List<ItemGroup> members = membersByCat.get(category.getId());

            if (members != null && !members.isEmpty()) {
                tiles.add(tile(p, category, members));
            }
        }

        return tiles;
    }

    /** A transient (unregistered) "<Addon> <Type>" section holding an addon's items of one type. */
    @Nonnull
    private static ItemGroup section(@Nonnull String categoryId, @Nonnull String addonName, @Nonnull List<SlimefunItem> items) {
        String title = ChatColor.YELLOW + addonName + " " + ItemTypeClassifier.typeSingular(categoryId);
        String keyId = "typed_" + categoryId + "_" + addonName.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_");
        ItemStack icon = ChestMenuUtils.stripTranslationIdentity(CustomItemStack.create(items.get(0).getItem().clone(), title));

        ItemGroup group = new ItemGroup(new NamespacedKey(Slimefun.instance(), keyId), icon);
        for (SlimefunItem item : items) {
            group.add(item);
        }

        return group;
    }

    @Nonnull
    private static CategoryItemGroup tile(@Nonnull Player p, @Nonnull GuideCategory category, @Nonnull List<ItemGroup> members) {
        String name = Slimefun.getLocalization().getMessage(p, "guide.categories." + category.getId());

        if (name == null || name.startsWith("guide.categories.") || name.startsWith("! Missing")) {
            name = category.getDefaultName();
        }

        ItemStack icon = CustomItemStack.create(MaterialCompat.stack(category.getIcon()), name,
            "",
            Slimefun.getLocalization().getMessage(p, "guide.categories-meta.categories").replace("%count%", String.valueOf(members.size())),
            "",
            Slimefun.getLocalization().getMessage(p, "guide.categories-meta.open"));

        return new CategoryItemGroup(category, icon, members);
    }
}
