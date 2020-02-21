package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.utils.FireworkUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class EasterEgg extends SimpleSlimefunItem<ItemUseHandler> {

	private final ItemStack[] gifts;
	
	public EasterEgg(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, ItemStack... gifts) {
		super(category, item, recipeType, recipe, recipeOutput);
		
		this.gifts = gifts;
	}

	@Override
	public ItemUseHandler getItemHandler() {
		return e -> {
			e.cancel();
			
			Player p = e.getPlayer();
			
			if (p.getGameMode() != GameMode.CREATIVE) {
				ItemUtils.consumeItem(e.getItem(), false);
			}
			
			FireworkUtils.launchRandom(p, 2);

			p.getWorld().dropItemNaturally(p.getLocation(), gifts[ThreadLocalRandom.current().nextInt(gifts.length)].clone());
		};
	}

}
