package io.github.thebusybiscuit.slimefun4.implementation.resources;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.attributes.PiglinBarterDrop;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This {@link SlimefunItem} can only be obtained via bartering with a {@link Piglin}, its 
 * only current uses is the recipe for crafting the {@link RuneOfUnemployment}. 
 *
 * @author dNiym
 *
 * @see PiglinBarterDrop
 *
 */
public class OrganicNetherGoo extends SlimefunItem implements PiglinBarterDrop {

    private final ItemSetting<Integer> chance = new ItemSetting<>("barter-chance", 3);

    public OrganicNetherGoo(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(chance);
    }

    @Override
    public int getBarteringLootChance() {
        return chance.getValue();
    }

}

