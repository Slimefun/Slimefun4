package io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.ArmorForge;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link ArmorAutoCrafter} is an implementation of the {@link AbstractAutoCrafter}.
 * It can craft items that are crafted using the {@link ArmorForge}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ArmorForge
 * @see AbstractAutoCrafter
 * @see SlimefunAutoCrafter
 * @see SlimefunItemRecipe
 *
 */
public class ArmorAutoCrafter extends SlimefunAutoCrafter {

    @ParametersAreNonnullByDefault
    public ArmorAutoCrafter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe, RecipeType.ARMOR_FORGE);
    }

}
