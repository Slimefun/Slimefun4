package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class PortableDustbin extends SimpleSlimefunItem<ItemUseHandler> {

	public PortableDustbin(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}
	
	@Override
	public ItemUseHandler getItemHandler() {
		return e -> {
			e.cancel();
			
			Player p = e.getPlayer();
			p.openInventory(Bukkit.createInventory(null, 9 * 3, ChatColor.DARK_RED + "垃圾桶"));
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
		};
	}
}
