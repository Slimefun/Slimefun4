package io.github.thebusybiscuit.slimefun4.implementation.items.medical;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

public class Rag extends SimpleSlimefunItem<ItemUseHandler> {

    public Rag(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
        	Player p = e.getPlayer();
        	
        	if (p.getGameMode() != GameMode.CREATIVE) {
        		ItemUtils.consumeItem(e.getItem(), false);
        	}
        	
			p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.WHITE_WOOL);
			p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
			p.setFireTicks(0);
			
			e.cancel();
        };
    }

}
