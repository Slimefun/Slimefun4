package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class KnowledgeTome extends SimpleSlimefunItem<ItemInteractionHandler> {

	public KnowledgeTome(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (SlimefunManager.isItemSimiliar(item, getItem(), true)) {
				List<String> lore = item.getItemMeta().getLore();
				lore.set(0, ChatColor.translateAlternateColorCodes('&', "&7Owner: &b" + p.getName()));
				lore.set(1, ChatColor.BLACK + "" + p.getUniqueId());
				ItemMeta im = item.getItemMeta();
				im.setLore(lore);
				item.setItemMeta(im);
				p.getEquipment().setItemInMainHand(item);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
				return true;
			}
			else if (SlimefunManager.isItemSimiliar(item, getItem(), false)) {
				PlayerProfile.get(p, profile -> {
					PlayerProfile.fromUUID(UUID.fromString(ChatColor.stripColor(item.getItemMeta().getLore().get(1))), owner -> {
						Set<Research> researches = owner.getResearches();
						researches.forEach(research -> profile.setResearched(research, true));
					});
				});
				
				if (p.getGameMode() != GameMode.CREATIVE) ItemUtils.consumeItem(item, false);
				return true;
			}
			else return false;
		};
	}

}
