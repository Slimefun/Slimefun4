package io.github.thebusybiscuit.slimefun5.core.guide.categories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.guide.SurvivalSlimefunGuide;

/**
 * A transient top-level guide tile for one {@link GuideCategory}, built per guide-open from the player's
 * currently-visible groups so it never enters the global item-group registry. Clicking it lists the
 * category's member groups via {@link SurvivalSlimefunGuide#openCategoryContents}.
 */
public class CategoryItemGroup extends FlexItemGroup {

    private final GuideCategory category;
    private final List<ItemGroup> members;

    public CategoryItemGroup(@Nonnull GuideCategory category, @Nonnull ItemStack icon, @Nonnull List<ItemGroup> members) {
        super(new NamespacedKey(Slimefun.instance(), "category_" + sanitize(category.getId())), icon, category.getOrder());
        this.category = category;
        this.members = members;
    }

    private static String sanitize(@Nonnull String id) {
        return id.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_");
    }

    @Nonnull
    public GuideCategory getCategory() {
        return category;
    }

    @Nonnull
    public List<ItemGroup> getMembers() {
        return members;
    }

    /**
     * All items of this category flattened from its member groups (core groups + addon sections),
     * de-duplicated, preserving order. Used by the classic layout's flat per-category listing.
     */
    @Nonnull
    public List<SlimefunItem> getAllItems() {
        List<SlimefunItem> all = new ArrayList<>();
        Set<SlimefunItem> seen = new HashSet<>();

        for (ItemGroup member : members) {
            for (SlimefunItem item : member.getItems()) {
                if (seen.add(item)) {
                    all.add(item);
                }
            }
        }

        return all;
    }

    @Override
    public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        return true;
    }

    @Override
    public void open(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        SlimefunGuideImplementation guide = Slimefun.getRegistry().getSlimefunGuide(mode);

        if (guide instanceof SurvivalSlimefunGuide) {
            SurvivalSlimefunGuide survival = (SurvivalSlimefunGuide) guide;

            // The categorize toggle controls how a category opens: sectioned (categorized) or a flat item
            // grid (classic). Both use the same main-menu category tiles.
            if (io.github.thebusybiscuit.slimefun5.core.guide.options.SlimefunGuideSettings.isMainMenuCategorized(p)) {
                survival.openCategoryContents(profile, this, 1);
            } else {
                survival.openCategoryItemsFlat(profile, this, 1);
            }
        }
    }
}
