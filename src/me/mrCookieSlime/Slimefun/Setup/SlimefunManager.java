package me.mrCookieSlime.Slimefun.Setup;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SoulboundItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.VanillaItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public final class SlimefunManager {
	
	private SlimefunManager() {}
	
	public static void registerArmorSet(ItemStack baseComponent, ItemStack[] items, String idSyntax, PotionEffect[][] effects, boolean special, boolean slimefun) {
		String[] components = new String[] {"_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS"};
		Category cat = special ? Categories.MAGIC_ARMOR: Categories.ARMOR;
		List<ItemStack[]> recipes = new ArrayList<>();
		recipes.add(new ItemStack[] {baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, null, null, null});
		recipes.add(new ItemStack[] {baseComponent, null, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent});
		recipes.add(new ItemStack[] {baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
		recipes.add(new ItemStack[] {null, null, null, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
		
		for (int i = 0; i < 4; i++) {
			if (i < effects.length && effects[i].length > 0) {
				new SlimefunArmorPiece(cat, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i), effects[i]).register(slimefun);
			} 
			else {
				new SlimefunItem(cat, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i)).register(slimefun);
			}
		}
	}
	
	public static void registerArmorSet(ItemStack baseComponent, ItemStack[] items, String idSyntax, boolean slimefun, boolean vanilla) {
		String[] components = new String[] {"_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS"};
		Category cat = Categories.ARMOR;
		List<ItemStack[]> recipes = new ArrayList<>();
		recipes.add(new ItemStack[] {baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, null, null, null});
		recipes.add(new ItemStack[] {baseComponent, null, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent});
		recipes.add(new ItemStack[] {baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
		recipes.add(new ItemStack[] {null, null, null, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
		
		for (int i = 0; i < 4; i++) {
			if (vanilla) {
				new VanillaItem(cat, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i)).register(slimefun);
			} 
			else {
				new SlimefunItem(cat, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i)).register(slimefun);
			}
		}
	}
	
	@Deprecated
	public static enum DataType {
		
		ALWAYS,
		NEVER,
		IF_COLORED;
		
	}
	
	public static boolean isItemSimiliar(ItemStack item, ItemStack sfitem, boolean lore) {
		if (item == null) return sfitem == null;
		if (sfitem == null) return false;
		
		if (item.getType() == sfitem.getType() && item.getAmount() >= sfitem.getAmount()) {
			if (item.hasItemMeta() && sfitem.hasItemMeta()) {
				if (item.getItemMeta().hasDisplayName() && sfitem.getItemMeta().hasDisplayName()) {
					if (item.getItemMeta().getDisplayName().equals(sfitem.getItemMeta().getDisplayName())) {
						if (lore) {
							if (item.getItemMeta().hasLore() && sfitem.getItemMeta().hasLore()) {
								return equalsLore(item.getItemMeta().getLore(), sfitem.getItemMeta().getLore());
							}
							else return !item.getItemMeta().hasLore() && !sfitem.getItemMeta().hasLore();
						}
						else return true;
					}
					else return false;
				}
				else if (!item.getItemMeta().hasDisplayName() && !sfitem.getItemMeta().hasDisplayName()) {
					if (lore) {
						if (item.getItemMeta().hasLore() && sfitem.getItemMeta().hasLore()) {
							return equalsLore(item.getItemMeta().getLore(), sfitem.getItemMeta().getLore());
						}
						else return !item.getItemMeta().hasLore() && !sfitem.getItemMeta().hasLore();
					}
					else return true;
				}
				else return false;
			} 
			else return !item.hasItemMeta() && !sfitem.hasItemMeta();
		}
		else return false;
	}

	@Deprecated
	public static boolean isItemSimiliar(ItemStack item, ItemStack sfitem, boolean lore, DataType data) {
		return isItemSimiliar(item, sfitem, lore);
	}
	
	public static boolean containsSimilarItem(Inventory inventory, ItemStack itemStack, boolean checkLore) {
		if (inventory == null || itemStack == null) return false;

		for (ItemStack is : inventory.getStorageContents()) {
			if (is == null || is.getType() == Material.AIR) continue;
			if (isItemSimiliar(is, itemStack, checkLore)) return true;
		}

		return false;
	}
	
	private static boolean equalsLore(List<String> lore, List<String> lore2) {
		StringBuilder string1 = new StringBuilder();
		StringBuilder string2 = new StringBuilder();

		String colors = ChatColor.YELLOW.toString() + ChatColor.YELLOW.toString() + ChatColor.GRAY.toString();
		for (String string: lore) {
			if (!string.equals(ChatColor.GRAY + "Soulbound") && !string.startsWith(colors)) string1.append("-NEW LINE-").append(string);
		}
		
		for (String string: lore2) {
			if (!string.equals(ChatColor.GRAY + "Soulbound") && !string.startsWith(colors)) string2.append("-NEW LINE-").append(string);
		}
		return string1.toString().equals(string2.toString());
	}

	public static boolean isItemSoulbound(ItemStack item) {
		if (item == null || item.getType() == Material.AIR) return false;
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BOUND_BACKPACK, false)) return true;
		else {
			ItemStack strippedItem = item.clone();

			for (Enchantment enchantment : item.getEnchantments().keySet()) {
				strippedItem.removeEnchantment(enchantment);
			}

			if (SlimefunPlugin.getHooks().isEmeraldEnchantsInstalled()) {
				for (ItemEnchantment enchantment : EmeraldEnchants.getInstance().getRegistry().getEnchantments(item)){
					EmeraldEnchants.getInstance().getRegistry().applyEnchantment(strippedItem, enchantment.getEnchantment(), 0);
				}
			}
			if (SlimefunItem.getByItem(strippedItem) instanceof SoulboundItem) return true;
			else if (item.hasItemMeta()) {
				ItemMeta im = item.getItemMeta();
				return (im.hasLore() && im.getLore().contains(ChatColor.GRAY + "Soulbound"));
			}
			return false;
		}
	}
}
