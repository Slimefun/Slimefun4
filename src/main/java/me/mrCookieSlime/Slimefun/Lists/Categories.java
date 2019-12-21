package me.mrCookieSlime.Slimefun.Lists;

import org.bukkit.Material;

import io.github.thebusybiscuit.slimefun4.core.utils.ChatUtils;
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
	
	private static final String LORE = "&a> 點擊開啟";
	
	public static final Category WEAPONS = new Category(new CustomItem(SlimefunItems.BLADE_OF_VAMPIRES, "&7武器類", "", LORE), 1);
	public static final Category TOOLS = new Category(new CustomItem(SlimefunItems.AUTO_SMELT_PICKAXE, "&7工具類", "", LORE), 1);
	public static final Category PORTABLE = new Category(new CustomItem(SlimefunItems.BACKPACK_MEDIUM, "&7隨身道具", "", LORE), 1);
	public static final Category FOOD = new Category(new CustomItem(SlimefunItems.FORTUNE_COOKIE, "&7食物類", "", LORE), 2);
	public static final Category MACHINES_1 = new Category(new CustomItem(Material.SMITHING_TABLE, "&7基礎機器", "", LORE), 1);
	public static final LockedCategory ELECTRICITY = new LockedCategory(new CustomItem(SlimefunItems.NUCLEAR_REACTOR, "&b能源與電力", "", LORE), 4, MACHINES_1);
	public static final LockedCategory GPS = new LockedCategory(new CustomItem(SlimefunItems.GPS_TRANSMITTER, "&bGPS 傳送系統", "", LORE), 4, MACHINES_1);
	public static final Category ARMOR = new Category(new CustomItem(SlimefunItems.DAMASCUS_STEEL_CHESTPLATE, "&7科技盔甲", "", LORE), 2);
	public static final Category LUMPS_AND_MAGIC = new Category(new CustomItem(SlimefunItems.RUNE_ENDER, "&7魔法材料", "", LORE), 2);
	public static final Category MAGIC = new Category(new CustomItem(SlimefunItems.INFUSED_ELYTRA, "&7魔力物品", "", LORE), 3);
	public static final Category MISC = new Category(new CustomItem(SlimefunItems.CAN, "&7雜項", "", LORE), 2);
	public static final Category TECH = new Category(new CustomItem(SlimefunItems.STEEL_JETPACK, "&7科技道具", "", LORE), 3);
	public static final Category RESOURCES = new Category(new CustomItem(SlimefunItems.SYNTHETIC_SAPPHIRE, "&7資源", "", LORE), 1);
	public static final Category CARGO = new LockedCategory(new CustomItem(SlimefunItems.CARGO_MANAGER, "&c物流管理系統", "", LORE), 4, MACHINES_1);
	public static final Category TECH_MISC = new Category(new CustomItem(SlimefunItems.HEATING_COIL, "&7科技組件", "", LORE), 2);
	public static final Category MAGIC_ARMOR = new Category(new CustomItem(SlimefunItems.ENDER_HELMET, "&7魔法盔甲", "", LORE), 2);
	public static final Category TALISMANS_1 = new Category(new CustomItem(SlimefunItems.TALISMAN, "&7魔法水晶 - &aI", "", LORE), 2);
	public static final LockedCategory TALISMANS_2 = new LockedCategory(new CustomItem(SlimefunItems.ENDER_TALISMAN, "&7魔法水晶 - &aII", "", LORE), 3, TALISMANS_1);
	
	// Seasonal Categories
	public static final SeasonalCategory CHRISTMAS = new SeasonalCategory(12, 1, new CustomItem(Material.NETHER_STAR, ChatUtils.christmas("聖誕節") + " &7(12月)", "", "&a> &a幫助 &c聖誕老人"));
	public static final SeasonalCategory VALENTINES_DAY = new SeasonalCategory(2, 2, new CustomItem(Material.POPPY, "&d情人節" + " &7(2月)", "", "&d> 點我慶祝愛情❤"));
	public static final SeasonalCategory EASTER = new SeasonalCategory(4, 2, new CustomItem(Material.EGG, "&6復活節" + " &7(4月)", "", "&a> 來畫些蛋吧"));
	public static final SeasonalCategory BIRTHDAY = new SeasonalCategory(10, 1, new CustomItem(Material.FIREWORK_ROCKET, "&a&l科技作者TheBusyBiscuit的生日 &7(10/26)", "", "&a> 一同來慶祝"));
	
}
