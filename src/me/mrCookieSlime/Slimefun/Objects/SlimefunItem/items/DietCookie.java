package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class DietCookie extends SimpleSlimefunItem {

	public DietCookie(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}
	
	@Override
	public ItemInteractionHandler onRightClick() {
		return (e, p, item) -> {
			if (SlimefunManager.isItemSimiliar(item, SlimefunItems.DIET_COOKIE, true)) {
				e.setCancelled(true);

				int amount = item.getAmount();
				if (amount <= 1) {
					if (e.getParentEvent().getHand() == EquipmentSlot.HAND) {
						p.getInventory().setItemInMainHand(null);
					}
					else {
						p.getInventory().setItemInOffHand(null);
					}
				}
				else {
					item.setAmount(amount - 1);
				}

				p.sendMessage(ChatColor.YELLOW + "You feel so light...");
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);

				if (p.hasPotionEffect(PotionEffectType.LEVITATION)) p.removePotionEffect(PotionEffectType.LEVITATION);
				p.addPotionEffect(PotionEffectType.LEVITATION.createEffect(60, 1));
				return true;
			}
			return false;
		};
	}

}
