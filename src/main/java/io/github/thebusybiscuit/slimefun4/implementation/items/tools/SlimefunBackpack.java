package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BackpackListener;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This class represents a {@link SlimefunItem} that is considered a Backpack.
 * Right-Clicking will open the {@link Inventory} of the currently held Backpack.
 * 
 * @author TheBusyBiscuit
 * 
 * @see BackpackListener
 * @see PlayerBackpack
 *
 */
public class SlimefunBackpack extends SimpleSlimefunItem<ItemUseHandler> {

    private final int size;

    public SlimefunBackpack(int size, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            BackpackListener listener = SlimefunPlugin.getBackpackListener();

            if (listener != null) {
                listener.openBackpack(e.getPlayer(), e.getItem(), this);
            }
        };
    }

}
