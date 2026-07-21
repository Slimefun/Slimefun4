package io.github.thebusybiscuit.slimefun5.core.guide.categories;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.core.guide.AddonVisibility;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

/**
 * Turns the guide's currently-visible groups into the transient {@link CategoryItemGroup} tiles for the
 * categorized main menu. Each group goes to its declared category, or an auto per-addon category when it
 * declares none. Purely registry-driven - no content heuristics.
 */
public final class CategoryMenuBuilder {

    private static final String ADDON_PREFIX = "addon:";
    // Pushes the per-addon fallback categories after every registered (canonical or addon-declared) one.
    private static final int ADDON_FALLBACK_ORDER = 1000;

    private CategoryMenuBuilder() {}

    /** The category a group belongs to: its declared id, else an "addon:&lt;namespace&gt;" fallback. */
    @Nonnull
    public static String resolveCategoryId(@Nonnull ItemGroup group) {
        String declared = group.getCategoryId();
        return declared != null ? declared : ADDON_PREFIX + group.getKey().getNamespace();
    }

    @Nonnull
    public static List<ItemGroup> build(@Nonnull Player p, @Nonnull List<ItemGroup> visibleGroups, @Nonnull GuideCategoryRegistry registry) {
        // category id -> member groups; LinkedHashMap keeps a stable order for the per-addon fallbacks.
        Map<String, List<ItemGroup>> membersById = new LinkedHashMap<>();

        for (ItemGroup group : visibleGroups) {
            if (AddonVisibility.isHidden(p, group.getKey().getNamespace())) {
                continue;
            }

            // A group goes to its declared category only if that category is actually registered; a group
            // tagged with an unknown id (unregistered custom category, a typo) falls back to its per-addon
            // tile rather than silently vanishing from the guide.
            String declared = group.getCategoryId();
            String target = (declared != null && registry.getById(declared) != null)
                ? declared
                : ADDON_PREFIX + group.getKey().getNamespace();

            membersById.computeIfAbsent(target, k -> new ArrayList<>()).add(group);
        }

        List<ItemGroup> tiles = new ArrayList<>();

        // Registered categories first, in registry order; consume them out of the map as we go.
        for (GuideCategory category : registry.getAll()) {
            List<ItemGroup> members = membersById.remove(category.getId());

            if (members != null && !members.isEmpty()) {
                tiles.add(tile(p, category, members));
            }
        }

        // Whatever is left is an undeclared per-addon fallback ("addon:<namespace>").
        for (Map.Entry<String, List<ItemGroup>> entry : membersById.entrySet()) {
            if (!entry.getKey().startsWith(ADDON_PREFIX) || entry.getValue().isEmpty()) {
                continue;
            }

            String namespace = entry.getKey().substring(ADDON_PREFIX.length());
            tiles.add(tile(p, autoCategory(namespace, entry.getValue()), entry.getValue()));
        }

        return tiles;
    }

    @Nonnull
    private static GuideCategory autoCategory(@Nonnull String namespace, @Nonnull List<ItemGroup> members) {
        SlimefunAddon addon = members.get(0).getAddon();
        String name = "&e" + (addon != null ? addon.getName() : namespace);
        return new GuideCategory(ADDON_PREFIX + namespace, name, XMaterial.BOOKSHELF, ADDON_FALLBACK_ORDER);
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
