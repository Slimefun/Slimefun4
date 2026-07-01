package io.github.thebusybiscuit.slimefun5.core.guide.themes;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.guide.SurvivalSlimefunGuide;

/**
 * A top-level guide entry representing one {@link GuideTheme}. Built transiently per guide-open from the
 * player's currently-visible categories, so it never pollutes the global item-group registry. Clicking it
 * lists the theme's member categories via {@link SurvivalSlimefunGuide#openThemeContents}.
 */
public class ThemeItemGroup extends FlexItemGroup {

    private final GuideTheme theme;
    private final List<ItemGroup> categories;

    public ThemeItemGroup(@Nonnull GuideTheme theme, @Nonnull ItemStack icon, @Nonnull List<ItemGroup> categories) {
        super(new NamespacedKey(Slimefun.instance(), "theme_" + theme.getId()), icon, theme.getOrder());
        this.theme = theme;
        this.categories = categories;
    }

    @Nonnull
    public GuideTheme getTheme() {
        return theme;
    }

    @Nonnull
    public List<ItemGroup> getCategories() {
        return categories;
    }

    @Override
    public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        return true;
    }

    @Override
    public void open(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        SlimefunGuideImplementation guide = Slimefun.getRegistry().getSlimefunGuide(mode);

        if (guide instanceof SurvivalSlimefunGuide) {
            ((SurvivalSlimefunGuide) guide).openThemeContents(profile, this, 1);
        }
    }
}
