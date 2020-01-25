package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MagicSugar extends SimpleSlimefunItem<ItemInteractionHandler> {

    public MagicSugar(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemInteractionHandler getItemHandler() {
        return (e, p, item) -> {
            if (isItem(item)) {
            	//Check if it is being placed into an ancient altar.
                if (e.getClickedBlock() != null) {
                	Material block = e.getClickedBlock().getType();
                	
                	if (block == Material.DISPENSER || block == Material.ENCHANTING_TABLE) {
                		return true;
                	}
                }
                
                if (p.getGameMode() != GameMode.CREATIVE) {
                	ItemUtils.consumeItem(item, false);
                }

                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 3));
                return true;
            } 
            
            return false;
        };
    }

}
