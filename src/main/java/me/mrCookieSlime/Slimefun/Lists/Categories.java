package me.mrCookieSlime.Slimefun.Lists;

import org.bukkit.ChatColor;
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
	
	private static final String LORE = "&a> 单击打开";

    public static final Category WEAPONS = new Category(new CustomItem(Material.GOLDEN_SWORD, "&7武器", "", LORE), 1);
    public static final Category PORTABLE = new Category(new CustomItem(SlimefunItems.BACKPACK_MEDIUM, "&7物品", "", LORE), 1);
    public static final Category FOOD = new Category(new CustomItem(Material.APPLE, "&7食物", "", LORE), 2);
    public static final Category MACHINES_1 = new Category(new CustomItem(Material.SMITHING_TABLE, "&7基础机器", "", LORE), 1);
    public static final LockedCategory ELECTRICITY = new LockedCategory(new CustomItem(SlimefunItems.NUCLEAR_REACTOR, "&b能源与电力", "", LORE), 4, MACHINES_1);
    public static final LockedCategory GPS = new LockedCategory(new CustomItem(SlimefunItems.GPS_TRANSMITTER, "&b基于 GPS 的机器", "", "&a>单击打开"), 4, MACHINES_1);
    public static final Category ARMOR = new Category(new CustomItem(Material.IRON_CHESTPLATE, "&7防具", "", LORE), 2);
    public static final Category LUMPS_AND_MAGIC = new Category(new CustomItem(SlimefunItems.RUNE_ENDER, "&7魔法物品", "", LORE), 2);
    public static final Category MAGIC = new Category(new CustomItem(SlimefunItems.ENDER_BACKPACK, "&7魔法工具", "", LORE), 3);
    public static final Category MISC = new Category(new CustomItem(SlimefunItems.CAN, "&7杂项", "", LORE), 2);
    public static final Category TECH = new Category(new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&7科技工具", "", LORE), Color.SILVER), 3);
    public static final Category RESOURCES = new Category(new CustomItem(SlimefunItems.SYNTHETIC_SAPPHIRE, "&7资源", "", LORE), 1);
    public static final Category CARGO = new LockedCategory(new CustomItem(SlimefunItems.CARGO_MANAGER, "&c货物管理", "", LORE), 4, MACHINES_1);
    public static final Category TECH_MISC = new Category(new CustomItem(SlimefunItems.HEATING_COIL, "&7科技零件", "", LORE), 2);
    public static final Category MAGIC_ARMOR = new Category(new CustomItem(SlimefunItems.ENDER_HELMET, "&7魔法防具", "", LORE), 2);
    public static final Category TALISMANS_1 = new Category(new CustomItem(Material.EMERALD, "&7护身符 - &a1级", "", LORE), 2);
    public static final LockedCategory TALISMANS_2 = new LockedCategory(new CustomItem(Material.EMERALD, "&7护身符 - &a2级", "", LORE), 3, TALISMANS_1);
    public static final Category TOOLS = new Category(new CustomItem(Material.GOLDEN_PICKAXE, "&7工具", "", LORE), 1);

    // Seasonal Categories
    public static final SeasonalCategory CHRISTMAS = new SeasonalCategory(12, 1, new CustomItem(Material.NETHER_STAR, "&a圣&b诞&c节", "", ChatColor.translateAlternateColorCodes('&', "&c帮助 &a圣诞老人")));
    public static final SeasonalCategory VALENTINES_DAY = new SeasonalCategory(2, 2, new CustomItem(Material.POPPY, "&d情人节", "", ChatColor.translateAlternateColorCodes('&', "&d庆祝爱情")));
    public static final SeasonalCategory EASTER = new SeasonalCategory(4, 2, new CustomItem(Material.EGG, "&6复活节", "", ChatColor.translateAlternateColorCodes('&', "&a给一些蛋上色")));
    public static final SeasonalCategory BIRTHDAY = new SeasonalCategory(10, 1, new CustomItem(Material.FIREWORK_ROCKET, "&a&lTheBusyBiscuit 的生日 &7(10/26)", "", ChatColor.translateAlternateColorCodes('&', "&a和我一起庆祝")));

}
