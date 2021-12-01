package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotConfigurable;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

/**
 * The {@link FilledKnowledgeFlask} is a {@link NotConfigurable} {@link SlimefunItem}
 * that results from interacting with a {@link KnowledgeFlask}.
 *
 * @see KnowledgeFlask
 *
 */
public class FilledKnowledgeFlask extends SlimefunItem implements NotConfigurable {

    @ParametersAreNonnullByDefault
    public FilledKnowledgeFlask(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        this.setHidden(true);
    }

}
