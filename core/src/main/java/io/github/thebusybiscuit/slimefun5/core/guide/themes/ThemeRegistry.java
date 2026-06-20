package io.github.thebusybiscuit.slimefun5.core.guide.themes;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.core.guide.AddonVisibility;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

/**
 * Buckets the guide's visible categories into {@link GuideTheme}s and builds the transient
 * {@link ThemeItemGroup} list shown at the guide's top level. A category's theme is its declared theme id,
 * or {@link GuideTheme#MISC} when untagged. Categories owned by an addon the player has hidden
 * (see {@link AddonVisibility}) are dropped. Empty themes are omitted.
 */
public final class ThemeRegistry {

    private ThemeRegistry() {}

    @Nonnull
    public static GuideTheme themeOf(@Nonnull ItemGroup group) {
        GuideTheme theme = GuideTheme.byId(group.getThemeId());
        return theme != null ? theme : GuideTheme.MISC;
    }

    @Nonnull
    public static List<ItemGroup> buildThemeGroups(@Nonnull Player p, @Nonnull List<ItemGroup> categories) {
        Map<GuideTheme, List<ItemGroup>> byTheme = new EnumMap<>(GuideTheme.class);

        for (ItemGroup group : categories) {
            String addonId = group.getKey().getNamespace();

            if (AddonVisibility.isHidden(p, addonId)) {
                continue;
            }

            byTheme.computeIfAbsent(themeOf(group), k -> new ArrayList<>()).add(group);
        }

        List<ItemGroup> result = new ArrayList<>();

        for (GuideTheme theme : GuideTheme.values()) {
            List<ItemGroup> members = byTheme.get(theme);

            if (members != null && !members.isEmpty()) {
                String name = Slimefun.getLocalization().getMessage(p, "guide.themes." + theme.getId());

                if (name == null || name.startsWith("guide.themes.")) {
                    name = theme.getDefaultName();
                }

                ItemStack icon = CustomItemStack.create(MaterialCompat.stack(theme.getIcon()), name,
                    "", "&7Categories: &e" + members.size(), "", "&7⇨ &eClick to open");
                result.add(new ThemeItemGroup(theme, icon, members));
            }
        }

        return result;
    }
}
