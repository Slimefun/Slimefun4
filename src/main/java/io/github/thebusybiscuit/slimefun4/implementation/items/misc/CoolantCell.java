package io.github.thebusybiscuit.slimefun4.implementation.items.misc;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.ReactorAccessPort;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.NetherStarReactor;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.NuclearReactor;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.Reactor;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * A {@link CoolantCell} is an {@link ItemStack} that is used to cool a {@link Reactor}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Reactor
 * @see ReactorAccessPort
 * @see NuclearReactor
 * @see NetherStarReactor
 *
 */
public class CoolantCell extends UnplaceableBlock {

    public CoolantCell(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    public CoolantCell(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

}
