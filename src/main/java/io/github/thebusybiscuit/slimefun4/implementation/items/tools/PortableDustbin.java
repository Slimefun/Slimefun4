package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class PortableDustbin extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public PortableDustbin(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();
            p.openInventory(Bukkit.createInventory(null, 9 * 3, ChatColor.DARK_RED + "Delete Items"));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        };
    }
}
