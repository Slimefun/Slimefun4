package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.VanillaInventoryDropHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link OutputChest} can be used to capture the output items from a {@link MultiBlockMachine}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MultiBlockMachine
 *
 */
public class OutputChest extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public OutputChest(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(new VanillaInventoryDropHandler<>(Chest.class));
    }

}
