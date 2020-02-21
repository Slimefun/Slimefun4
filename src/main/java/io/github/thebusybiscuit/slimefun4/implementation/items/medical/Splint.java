package io.github.thebusybiscuit.slimefun4.implementation.items.medical;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Splint extends SimpleSlimefunItem<ItemUseHandler> {

    public Splint(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
        	Player p = e.getPlayer();
        	
        	if (p.getGameMode() != GameMode.CREATIVE) {
        		ItemUtils.consumeItem(e.getItem(), false);
        	}
        	
        	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SKELETON_HURT, 1, 1);
			p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
			
			e.cancel();
        };
    }

}
