package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

public class KnowledgeTome extends SimpleSlimefunItem<ItemInteractionHandler> {

	public KnowledgeTome(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
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
				PlayerProfile profile = PlayerProfile.get(p);
				Set<Research> researches = PlayerProfile.fromUUID(UUID.fromString(ChatColor.stripColor(item.getItemMeta().getLore().get(1)))).getResearches();
				researches.forEach(research -> profile.setResearched(research, true));
				
				PlayerInventory.consumeItemInHand(p);
				return true;
			}
			else return false;
		};
	}

}
