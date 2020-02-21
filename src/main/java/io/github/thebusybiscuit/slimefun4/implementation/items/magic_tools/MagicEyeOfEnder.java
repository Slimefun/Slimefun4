package io.github.thebusybiscuit.slimefun4.implementation.items.magic_tools;

import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MagicEyeOfEnder extends SimpleSlimefunItem<ItemUseHandler> {

	public MagicEyeOfEnder(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
        	e.cancel();
			
        	Player p = e.getPlayer();
        	
			if (
				SlimefunManager.isItemSimilar(p.getInventory().getHelmet(), SlimefunItems.ENDER_HELMET, true) && 
				SlimefunManager.isItemSimilar(p.getInventory().getChestplate(), SlimefunItems.ENDER_CHESTPLATE, true) && 
				SlimefunManager.isItemSimilar(p.getInventory().getLeggings(), SlimefunItems.ENDER_LEGGINGS, true) && 
				SlimefunManager.isItemSimilar(p.getInventory().getBoots(), SlimefunItems.ENDER_BOOTS, true)
			) {
				p.launchProjectile(EnderPearl.class);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
			}
        };
    }
}
