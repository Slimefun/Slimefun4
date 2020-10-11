package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link PortableCrafter} is one of the oldest items in Slimefun.
 * It allows a {@link Player} to open up the {@link CraftingInventory} via right click.
 * 
 * @author TheBusyBiscuit
 *
 */
public class PortableCrafter extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    @ParametersAreNonnullByDefault
    public PortableCrafter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();
            p.openWorkbench(p.getLocation(), true);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
        };
    }
}
