package me.mrCookieSlime.Slimefun.Lists;

import org.bukkit.Color;
import org.bukkit.Material;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomArmor;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.SeasonalCategory;

/**
 * Built-in categories.
 * 
 * @author TheBusyBiscuit
 * @since 4.0
 * @see Category
 */
public final class Categories {
	
	private Categories() {}
	
	private static final String LORE = "&a> Click to open";
	
	public static final Category WEAPONS = new Category(new CustomItem(Material.GOLDEN_SWORD, "&7Weapons", "", LORE), 1);
	public static final Category PORTABLE = new Category(new CustomItem(SlimefunItems.BACKPACK_MEDIUM, "&7Items", "", LORE), 1);
	public static final Category FOOD = new Category(new CustomItem(Material.APPLE, "&7Food", "", LORE), 2);
	public static final Category MACHINES_1 = new Category(new CustomItem(Material.SMITHING_TABLE, "&7Basic Machines", "", LORE), 1);
	public static final LockedCategory ELECTRICITY = new LockedCategory(new CustomItem(SlimefunItems.NUCLEAR_REACTOR, "&bEnergy and Electricity", "", LORE), 4, MACHINES_1);
	public static final LockedCategory GPS = new LockedCategory(new CustomItem(SlimefunItems.GPS_TRANSMITTER, "&bGPS-based Machines", "", LORE), 4, MACHINES_1);
	public static final Category ARMOR = new Category(new CustomItem(Material.IRON_CHESTPLATE, "&7Armor", "", LORE), 2);
	public static final Category LUMPS_AND_MAGIC = new Category(new CustomItem(SlimefunItems.RUNE_ENDER, "&7Magical Items", "", LORE), 2);
	public static final Category MAGIC = new Category(new CustomItem(Material.ELYTRA, "&7Magical Gadgets", "", LORE), 3);
	public static final Category MISC = new Category(new CustomItem(SlimefunItems.CAN, "&7Miscellaneous", "", LORE), 2);
	public static final Category TECH = new Category(new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&7Technical Gadgets", "", LORE), Color.SILVER), 3);
	public static final Category RESOURCES = new Category(new CustomItem(SlimefunItems.SYNTHETIC_SAPPHIRE, "&7Resources", "", LORE), 1);
	public static final Category CARGO = new LockedCategory(new CustomItem(SlimefunItems.CARGO_MANAGER, "&cCargo Management", "", LORE), 4, MACHINES_1);
	public static final Category TECH_MISC = new Category(new CustomItem(SlimefunItems.HEATING_COIL, "&7Technical Components", "", LORE), 2);
	public static final Category MAGIC_ARMOR = new Category(new CustomItem(SlimefunItems.ENDER_HELMET, "&7Magical Armor", "", LORE), 2);
	public static final Category TALISMANS_1 = new Category(new CustomItem(Material.EMERALD, "&7Talismans - &aTier I", "", LORE), 2);
	public static final LockedCategory TALISMANS_2 = new LockedCategory(new CustomItem(Material.EMERALD, "&7Talismans - &aTier II", "", LORE), 3, TALISMANS_1);
	public static final Category TOOLS = new Category(new CustomItem(Material.GOLDEN_PICKAXE, "&7Tools", "", LORE), 1);
	
	// Seasonal Categories
	public static final SeasonalCategory CHRISTMAS = new SeasonalCategory(12, 1, new CustomItem(Material.NETHER_STAR, "&aC&ch&ar&ci&as&ct&am&ca&as", "", "&c> Click to help &aSanta"));
	public static final SeasonalCategory VALENTINES_DAY = new SeasonalCategory(2, 2, new CustomItem(Material.POPPY, "&dValentine's Day", "", "&d> Click to celebrate Love"));
	public static final SeasonalCategory EASTER = new SeasonalCategory(4, 2, new CustomItem(Material.EGG, "&6Easter", "", "&a> Click to paint some Eggs"));
	public static final SeasonalCategory BIRTHDAY = new SeasonalCategory(10, 1, new CustomItem(Material.FIREWORK_ROCKET, "&a&lTheBusyBiscuit's Birthday &7(26th October)", "", "&a> Click to celebrate with me"));
	
}
