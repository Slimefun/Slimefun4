package me.mrCookieSlime.Slimefun.Lists;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomArmor;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.MenuItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.SeasonCategory;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public class Categories {
	
	public static Category WEAPONS = new Category(new MenuItem(Material.GOLD_SWORD, "&7Weapons", 0, "open"), 1);
	public static Category PORTABLE = null;
	public static Category FOOD = new Category(new MenuItem(Material.APPLE, "&7Food", 0, "open"), 2);
	public static Category MACHINES_1 = null;
	public static LockedCategory ELECTRICITY = null;
	public static LockedCategory GPS = null;
	public static Category ARMOR = new Category(new MenuItem(Material.IRON_CHESTPLATE, "&7Armor", 0, "open"), 2);
	public static Category LUMPS_AND_MAGIC = new Category(new MenuItem(Material.FIREBALL, "&7Magical Items", 0, "open"), 2);
	public static Category MAGIC = new Category(new MenuItem(Material.BLAZE_POWDER, "&7Magical Gadgets", 0, "open"), 3);
	public static Category MISC = null;
	public static Category TECH = new Category(new CustomArmor(new MenuItem(Material.LEATHER_CHESTPLATE, "&7Technical Gadgets", 0, "open"), Color.SILVER), 3);
	public static Category RESOURCES = null;
	public static Category CARGO = null;
	public static Category TECH_MISC = new Category(new MenuItem(Material.REDSTONE_COMPARATOR, "&7Technical Components", 0, "open"), 2);
	public static Category MAGIC_ARMOR = new Category(new MenuItem(Material.GOLD_CHESTPLATE, "&7Magical Armor", 0, "open"), 2);
	public static Category TALISMANS_1 = new Category(new MenuItem(Material.EMERALD, "&7Talismans - &aTier I", 0, "open"), 2);
	public static LockedCategory TALISMANS_2 = new LockedCategory(new MenuItem(Material.EMERALD, "&7Talismans - &aTier II", 0, "open"), 3, TALISMANS_1);
	public static Category TOOLS = new Category(new MenuItem(Material.GOLD_PICKAXE, "&7Tools", 0, "open"), 1);
	public static SeasonCategory CHRISTMAS = new SeasonCategory(12, 1, new MenuItem(Material.NETHER_STAR, "&aC&ch&ar&ci&as&ct&am&ca&as", 0, ChatColor.translateAlternateColorCodes('&', "&chelp &aSanta")));
	public static SeasonCategory VALENTINES_DAY = new SeasonCategory(2, 2, new MenuItem(Material.RED_ROSE, "&dValentine's Day", 0, ChatColor.translateAlternateColorCodes('&', "&dcelebrate Love")));
	public static SeasonCategory EASTER = new SeasonCategory(1, 2, new MenuItem(Material.EGG, "&6Easter", 0, ChatColor.translateAlternateColorCodes('&', "&apaint some Eggs")));
	public static SeasonCategory BIRTHDAY = new SeasonCategory(10, 1, new MenuItem(Material.FIREWORK, "&a&lmrCookieSlime's Birthday &7(26th October)", 0, ChatColor.translateAlternateColorCodes('&', "&acelebrate with me")));
	
	static {
		try {
			MISC = new Category(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTRkYTk3ZjA4MGUzOTViODQyYzRjYzgyYTg0MDgyM2Q0ZGJkOGNhNjg4YTIwNjg1M2U1NzgzZTRiZmRjMDEyIn19fQ=="), "&7Miscellaneous", "", "&a> Click to open"), 2);
			PORTABLE = new Category(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&7Items", "", "&a> Click to open"), 1);
			MACHINES_1 = new Category(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&7Basic Machines", "", "&a> Click to open"), 1);
			ELECTRICITY = new LockedCategory(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU4NDQzMmFmNmYzODIxNjcxMjAyNThkMWVlZThjODdjNmU3NWQ5ZTQ3OWU3YjBkNGM3YjZhZDQ4Y2ZlZWYifX19"), "&bEnergy and Electricity", "", "&a> Click to open"), 4, MACHINES_1);
			GPS = new LockedCategory(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&bGPS-based Machines", "", "&a> Click to open"), 4, MACHINES_1);
			RESOURCES = new Category(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2U4ZjVhZGIxNGQ2YzlmNmI4MTBkMDI3NTQzZjFhOGMxZjQxN2UyZmVkOTkzYzk3YmNkODljNzRmNWUyZTgifX19"), "&7Resources", "", "&a> Click to open"), 1);
			CARGO = new LockedCategory(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTUxMGJjODUzNjJhMTMwYTZmZjlkOTFmZjExZDZmYTQ2ZDdkMTkxMmEzNDMxZjc1MTU1OGVmM2M0ZDljMiJ9fX0="), "&cCargo Management", "", "&a> Click to open"), 4, MACHINES_1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
