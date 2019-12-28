package me.mrCookieSlime.Slimefun.Setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.cscorelib2.item.ImmutableItemMeta;
import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.VanillaItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.Soulbound;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public final class SlimefunManager {

	private SlimefunManager() {}

	public static void registerArmorSet(ItemStack baseComponent, ItemStack[] items, String idSyntax, PotionEffect[][] effects, boolean special, boolean slimefun) {
		String[] components = new String[] {"_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS"};
		Category category = special ? Categories.MAGIC_ARMOR: Categories.ARMOR;
		List<ItemStack[]> recipes = new ArrayList<>();
		
		recipes.add(new ItemStack[] {baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, null, null, null});
		recipes.add(new ItemStack[] {baseComponent, null, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent});
		recipes.add(new ItemStack[] {baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
		recipes.add(new ItemStack[] {null, null, null, baseComponent, null, baseComponent, baseComponent, null, baseComponent});

		for (int i = 0; i < 4; i++) {
			if (i < effects.length && effects[i].length > 0) {
				new SlimefunArmorPiece(category, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i), effects[i]).register(slimefun);
			}
			else {
				new SlimefunItem(category, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i)).register(slimefun);
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
	public static boolean isItemSimiliar(ItemStack item, ItemStack sfitem, boolean lore) {
		return isItemSimilar(item, sfitem, lore);
	}

	public static boolean isItemSimilar(ItemStack item, ItemStack sfitem, boolean checkLore) {
		if (item == null) return sfitem == null;
		if (sfitem == null) return false;
		
		if (item instanceof SlimefunItemStack && sfitem instanceof SlimefunItemStack) {
			return ((SlimefunItemStack) item).getItemID().equals(((SlimefunItemStack) sfitem).getItemID());
		}

		if (item.getType() == sfitem.getType() && item.getAmount() >= sfitem.getAmount()) {
			if (!item.hasItemMeta() && !sfitem.hasItemMeta()) {
				return true;
			}
			else {
				ItemMeta itemMeta = item.getItemMeta();
				
				if (sfitem instanceof SlimefunItemStack) {
					Optional<String> id = SlimefunPlugin.getItemDataService().getItemData(itemMeta);
					
					if (id.isPresent()) {
						return id.get().equals(((SlimefunItemStack) sfitem).getItemID());
					}
					
					ImmutableItemMeta meta = ((SlimefunItemStack) sfitem).getImmutableMeta();
					
					Optional<String> displayName = meta.getDisplayName();
					
					if (itemMeta.hasDisplayName() && displayName.isPresent()) {
						if (itemMeta.getDisplayName().equals(displayName.get())) {
							Optional<List<String>> itemLore = meta.getLore();
							
							if (checkLore) {
								if (itemMeta.hasLore() && itemLore.isPresent()) {
									return equalsLore(itemMeta.getLore(), itemLore.get());
								}
								else return !itemMeta.hasLore() && !itemLore.isPresent();
							}
							else return true;
						}
						else return false;
					}
					else if (!itemMeta.hasDisplayName() && !displayName.isPresent()) {
						Optional<List<String>> itemLore = meta.getLore();
						
						if (checkLore) {
							if (itemMeta.hasLore() && itemLore.isPresent()) {
								return equalsLore(itemMeta.getLore(), itemLore.get());
							}
							else return !itemMeta.hasLore() && !itemLore.isPresent();
						}
						else return true;
					}
					else return false;
				}
				else {
					ItemMeta sfitemMeta = sfitem.getItemMeta();
					
					if (itemMeta.hasDisplayName() && sfitemMeta.hasDisplayName()) {
						if (itemMeta.getDisplayName().equals(sfitemMeta.getDisplayName())) {
							if (checkLore) {
								if (itemMeta.hasLore() && sfitemMeta.hasLore()) {
									return equalsLore(itemMeta.getLore(), sfitemMeta.getLore());
								}
								else return !itemMeta.hasLore() && !sfitemMeta.hasLore();
							}
							else return true;
						}
						else return false;
					}
					else if (!itemMeta.hasDisplayName() && !sfitemMeta.hasDisplayName()) {
						if (checkLore) {
							if (itemMeta.hasLore() && sfitemMeta.hasLore()) {
								return equalsLore(itemMeta.getLore(), sfitemMeta.getLore());
							}
							else return !itemMeta.hasLore() && !sfitemMeta.hasLore();
						}
						else return true;
					}
					else return false;
				}
			}
		}
		else return false;
	}

	public static boolean containsSimilarItem(Inventory inventory, ItemStack itemStack, boolean checkLore) {
		if (inventory == null || itemStack == null) return false;

		for (ItemStack is : inventory.getStorageContents()) {
			if (is == null || is.getType() == Material.AIR) continue;
			if (isItemSimilar(is, itemStack, checkLore)) return true;
		}

		return false;
	}

	private static boolean equalsLore(List<String> lore, List<String> lore2) {
		StringBuilder string1 = new StringBuilder();
		StringBuilder string2 = new StringBuilder();

		String colors = ChatColor.YELLOW.toString() + ChatColor.YELLOW.toString() + ChatColor.GRAY.toString();
		
		for (String string : lore) {
			if (!string.equals(ChatColor.GRAY + "Soulbound") && !string.startsWith(colors)) {
				string1.append("-NEW LINE-").append(string);
			}
		}

		for (String string : lore2) {
			if (!string.equals(ChatColor.GRAY + "Soulbound") && !string.startsWith(colors)) {
				string2.append("-NEW LINE-").append(string);
			}
		}
		
		return string1.toString().equals(string2.toString());
	}

	public static boolean isItemSoulbound(ItemStack item) {
		if (item == null || item.getType() == Material.AIR) {
			return false;
		}
		else if (isItemSimilar(item, SlimefunItems.BOUND_BACKPACK, false)) {
			return !SlimefunItem.getByID("BOUND_BACKPACK").isDisabled();
		}
		else {
			ItemStack strippedItem = item.clone();

			if (SlimefunPlugin.getHooks().isEmeraldEnchantsInstalled()) {
				for (ItemEnchantment enchantment : EmeraldEnchants.getInstance().getRegistry().getEnchantments(item)){
					EmeraldEnchants.getInstance().getRegistry().applyEnchantment(strippedItem, enchantment.getEnchantment(), 0);
				}
			}
			
			SlimefunItem sfItem = SlimefunItem.getByItem(strippedItem);
			if (sfItem instanceof Soulbound && !sfItem.isDisabled()) {
				return true;
			}
			else if (item.hasItemMeta()) {
				ItemMeta im = item.getItemMeta();
				return (im.hasLore() && im.getLore().contains(ChatColor.GRAY + "Soulbound"));
			}
			return false;
		}
	}
}
