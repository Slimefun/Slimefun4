package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * {@link HardenedGlass} is a special kind of block which cannot be destroyed by explosions.
 * It is partially {@link WitherProof}, as it cannot be destroyed through explosions caused by
 * a {@link WitherSkull}. However the {@link Wither} is still able to destroy it directly.
 * 
 * @author TheBusyBiscuit
 * 
 * @see WitherProofBlock
 *
 */
public class HardenedGlass extends WitherProofBlock {

    public HardenedGlass(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

}
