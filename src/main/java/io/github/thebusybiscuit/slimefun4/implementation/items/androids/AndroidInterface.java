package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockDispenseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link AndroidInterface} are inventories used to interact with a {@link ProgrammableAndroid}.
 * There are two variants of interfaces, fuel and items.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ProgrammableAndroid
 *
 */
public class AndroidInterface extends SimpleSlimefunItem<BlockDispenseHandler> {

    @ParametersAreNonnullByDefault
    public AndroidInterface(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockDispenseHandler getItemHandler() {
        return (e, d, block, machine) -> e.setCancelled(true);
    }

}
