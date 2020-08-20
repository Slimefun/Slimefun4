package io.github.thebusybiscuit.slimefun4.implementation.items.misc;

import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.listeners.VillagerTradingListener;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link SyntheticEmerald} is an almost normal emerald.
 * It can even be used to trade with {@link Villager Villagers}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see VillagerTradingListener
 *
 */
public class SyntheticEmerald extends SlimefunItem {

    public SyntheticEmerald(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        setUseableInWorkbench(true);
    }

}
