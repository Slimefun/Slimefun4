package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class KnowledgeTome extends SimpleSlimefunItem<ItemInteractionHandler> {

	public KnowledgeTome(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (isItem(item)) {
				ItemMeta im = item.getItemMeta();
				List<String> lore = im.getLore();
				
				if (lore.get(1).isEmpty()) {
					lore.set(0, ChatColor.translateAlternateColorCodes('&', "&7Owner: &b" + p.getName()));
					lore.set(1, ChatColor.BLACK + "" + p.getUniqueId());
					im.setLore(lore);
					item.setItemMeta(im);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
				} 
				else {
					UUID uuid = UUID.fromString(ChatColor.stripColor(item.getItemMeta().getLore().get(1)));
					
					if (p.getUniqueId().equals(uuid))
						return true;
					
					PlayerProfile.get(p, profile -> PlayerProfile.fromUUID(uuid, owner -> {
						Set<Research> researches = owner.getResearches();
						researches.forEach(research -> research.unlock(p, true));
					}));
					
					if (p.getGameMode() != GameMode.CREATIVE)
						item.setAmount(item.getAmount() - 1);
				}
				return true;
			}
			else return false;
		};
	}

}
