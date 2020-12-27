package io.github.thebusybiscuit.slimefun4.core.categories;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;

public class MultiCategory extends FlexCategory {

    private final List<SubCategory> subCategories = new ArrayList<>();

    @ParametersAreNonnullByDefault
    public MultiCategory(NamespacedKey key, ItemStack item) {
        this(key, item, 3);
    }

    @ParametersAreNonnullByDefault
    public MultiCategory(NamespacedKey key, ItemStack item, int tier) {
        super(key, item, tier);
    }

    public void addSubCategory(@Nonnull SubCategory category) {
        Validate.notNull(category, "The Category cannot be null!");

        subCategories.add(category);
    }

    @Override
    public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideLayout layout) {
        return true;
    }

    @Override
    public void open(Player p, PlayerProfile profile, SlimefunGuideLayout layout) {
        // TODO: Open Categories menu
    }

}
