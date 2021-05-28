package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Dropper;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.handlers.VanillaInventoryDropHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.Smeltery;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link IgnitionChamber} is used to re-ignite a {@link Smeltery}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Smeltery
 *
 */
public class IgnitionChamber extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public IgnitionChamber(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(new VanillaInventoryDropHandler<>(Dropper.class));
    }

}
