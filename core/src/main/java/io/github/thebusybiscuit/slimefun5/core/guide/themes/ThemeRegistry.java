package io.github.thebusybiscuit.slimefun5.core.guide.themes;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.core.guide.AddonVisibility;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

/**
 * Buckets the guide's visible categories into {@link GuideTheme}s and builds the transient
 * {@link ThemeItemGroup} list shown at the guide's top level.
 *
 * <p>Slimefun's own groups declare a theme and are kept whole under it. An addon's groups are untagged, so
 * instead of dumping the whole addon into one bucket, each of the addon's items is classified individually
 * (see {@link ItemThemeClassifier}) and placed under the matching theme: its weapons under Weapons, its
 * tools under Tools, and so on. Within a theme, an addon that contributes at least
 * {@link #SECTION_THRESHOLD} items gets its own "section" tile; fewer than that and the items are shown
 * directly in the theme (loose), so a lone addon sword just appears under Weapons rather than behind an
 * almost-empty tile. Categories owned by an addon the player has hidden (see {@link AddonVisibility}) are
 * dropped, and empty themes are omitted.
 */
public final class ThemeRegistry {

    /**
     * How many items an addon must contribute to a single theme before those items get their own section
     * tile (rather than being shown loose alongside the theme's other content).
     */
    static final int SECTION_THRESHOLD = 4;

    private ThemeRegistry() {}

    @Nonnull
    public static GuideTheme themeOf(@Nonnull ItemGroup group) {
        GuideTheme theme = GuideTheme.byId(group.getThemeId());
        return theme != null ? theme : GuideTheme.MISC;
    }

    @Nonnull
    public static List<ItemGroup> buildThemeGroups(@Nonnull Player p, @Nonnull List<ItemGroup> categories) {
        // Theme-tagged groups (Slimefun's own) are kept whole under their theme.
        Map<GuideTheme, List<ItemGroup>> groupsByTheme = new EnumMap<>(GuideTheme.class);
        // Untagged (addon) items are classified per item, then grouped theme -> addon id -> items so we can
        // section a big contribution and merge a small one. LinkedHashMap keeps a stable section order.
        Map<GuideTheme, Map<String, List<SlimefunItem>>> addonItemsByTheme = new EnumMap<>(GuideTheme.class);

        for (ItemGroup group : categories) {
            String addonId = group.getKey().getNamespace();

            if (AddonVisibility.isHidden(p, addonId)) {
                continue;
            }

            if (group.getThemeId() != null) {
                groupsByTheme.computeIfAbsent(themeOf(group), k -> new ArrayList<>()).add(group);
                continue;
            }

            for (SlimefunItem item : group.getItems()) {
                if (item == null || item.isHidden() || item.isDisabledIn(p.getWorld())) {
                    continue;
                }

                GuideTheme theme = ItemThemeClassifier.classify(item);
                addonItemsByTheme
                    .computeIfAbsent(theme, k -> new LinkedHashMap<>())
                    .computeIfAbsent(addonId, k -> new ArrayList<>())
                    .add(item);
            }
        }

        List<ItemGroup> result = new ArrayList<>();

        for (GuideTheme theme : GuideTheme.values()) {
            List<ItemGroup> sections = new ArrayList<>();
            List<SlimefunItem> looseItems = new ArrayList<>();

            List<ItemGroup> coreGroups = groupsByTheme.get(theme);
            if (coreGroups != null) {
                sections.addAll(coreGroups);
            }

            Map<String, List<SlimefunItem>> byAddon = addonItemsByTheme.get(theme);
            if (byAddon != null) {
                for (Map.Entry<String, List<SlimefunItem>> entry : byAddon.entrySet()) {
                    List<SlimefunItem> items = entry.getValue();

                    if (items.size() >= SECTION_THRESHOLD) {
                        sections.add(buildAddonSection(theme, entry.getKey(), items));
                    } else {
                        looseItems.addAll(items);
                    }
                }
            }

            if (sections.isEmpty() && looseItems.isEmpty()) {
                continue;
            }

            int count = sections.size() + looseItems.size();
            String name = Slimefun.getLocalization().getMessage(p, "guide.themes." + theme.getId());

            if (name == null || name.startsWith("guide.themes.") || name.startsWith("! Missing")) {
                name = theme.getDefaultName();
            }

            ItemStack icon = CustomItemStack.create(MaterialCompat.stack(theme.getIcon()), name,
                "",
                Slimefun.getLocalization().getMessage(p, "guide.themes-meta.categories").replace("%count%", String.valueOf(count)),
                "",
                Slimefun.getLocalization().getMessage(p, "guide.themes-meta.open"));
            result.add(new ThemeItemGroup(theme, icon, sections, looseItems));
        }

        return result;
    }

    /**
     * Builds a transient (unregistered) section tile holding an addon's items of one theme, titled after
     * the addon. Never added to the global item-group registry - it lives only for this guide-open.
     *
     * @param theme   the theme this section belongs to (drives the tile icon and order)
     * @param addonId the addon's namespace (the group key namespace), used for a unique, valid key
     * @param items   the addon's items classified into this theme
     */
    @Nonnull
    private static ItemGroup buildAddonSection(@Nonnull GuideTheme theme, @Nonnull String addonId, @Nonnull List<SlimefunItem> items) {
        SlimefunAddon addon = items.get(0).getAddon();
        String title = addon != null ? addon.getName() : addonId;

        NamespacedKey key = new NamespacedKey(Slimefun.instance(), "themed_" + theme.getId() + '_' + addonId.toLowerCase(java.util.Locale.ROOT));
        ItemGroup section = new ItemGroup(key, CustomItemStack.create(MaterialCompat.stack(theme.getIcon()), title), theme.getOrder());

        for (SlimefunItem item : items) {
            section.add(item);
        }

        return section;
    }
}
