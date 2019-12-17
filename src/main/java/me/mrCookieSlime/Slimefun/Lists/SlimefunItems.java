package me.mrCookieSlime.Slimefun.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomPotion;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.StormStaff;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.utils.Christmas;
import me.mrCookieSlime.Slimefun.utils.MachineTier;
import me.mrCookieSlime.Slimefun.utils.MachineType;

public final class SlimefunItems {
	
	private SlimefunItems() {}

    /*		 Items 		*/
    public static final ItemStack PORTABLE_CRAFTER = new SlimefunItemStack("PORTABLE_CRAFTER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzJlYzRhNGJkOGE1OGY4MzYxZjhhMDMwM2UyMTk5ZDMzZDYyNGVhNWY5MmY3Y2IzNDE0ZmVlOTVlMmQ4NjEifX19", "&6便携工作台", "&a&o一个便携式的工作台", "", "&e右键&7 打开");
    public static final ItemStack PORTABLE_DUSTBIN = new SlimefunItemStack("PORTABLE_DUSTBIN", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJkNDEwNDJjZTk5MTQ3Y2MzOGNhYzllNDY3NDE1NzZlN2VlNzkxMjgzZTZmYWM4ZDMyOTJjYWUyOTM1ZjFmIn19fQ==", "&6便携垃圾箱", "&r轻松的消除多余的物品", "", "&e右键&7 打开");
    public static final ItemStack ENDER_BACKPACK = new SlimefunItemStack("ENDER_BACKPACK", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ==", "&6末影背包", "&a&o便携式末影箱", "", "&e右键&7 打开");
    public static final ItemStack MAGIC_EYE_OF_ENDER = new SlimefunItemStack("MAGIC_EYE_OF_ENDER", Material.ENDER_EYE, "&6&l魔法末影之眼", "&4&l需要全套末影护甲", "", "&7&e右键&7 以射出一颗末影珍珠");
    public static final ItemStack BROKEN_SPAWNER = new SlimefunItemStack("BROKEN_SPAWNER", Material.SPAWNER, "&c已损坏的刷怪笼", "&7类型: &b<类型>", "", "&c已损坏, 需要在古代祭坛中修复");
    public static final ItemStack REPAIRED_SPAWNER = new SlimefunItemStack("REINFORCED_SPAWNER", Material.SPAWNER, "&b已修复的刷怪笼", "&7类型: &b<类型>");
    public static final ItemStack INFERNAL_BONEMEAL = new SlimefunItemStack("INFERNAL_BONEMEAL", Material.BONE_MEAL, "&4地狱骨粉", "", "&c加速地狱疣的生长速度");

    /*		 Gadgets 		*/
    public static final ItemStack GOLD_PAN = new SlimefunItemStack("GOLD_PAN", Material.BOWL, "&6淘金盘","&a&o可以获得各种各样的矿物", "", "&7&e右键&7 从沙砾中淘金");
    public static final ItemStack NETHER_GOLD_PAN = new SlimefunItemStack("NETHER_GOLD_PAN", Material.BOWL, "&4下界淘金盘", "", "&7&e右键&7 从灵魂沙中淘金");
    public static final ItemStack PARACHUTE = new SlimefunItemStack("PARACHUTE", Material.LEATHER_CHESTPLATE, Color.WHITE, "&r&l降落伞", "", "&7按住 &eShift&7 使用");
    public static final ItemStack GRAPPLING_HOOK = new SlimefunItemStack("GRAPPLING_HOOK", Material.LEAD, "&6抓钩", "", "&7&e右键&7 使用");
    public static final ItemStack SOLAR_HELMET = new SlimefunItemStack("SOLAR_HELMET", Material.IRON_HELMET, "&b太阳能头盔", "", "&a&oCharges held Items and Armor");
    public static final ItemStack CLOTH = new SlimefunItemStack("CLOTH", Material.PAPER, "&b布");
    public static final ItemStack CAN = new SlimefunItemStack("CAN", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTRkYTk3ZjA4MGUzOTViODQyYzRjYzgyYTg0MDgyM2Q0ZGJkOGNhNjg4YTIwNjg1M2U1NzgzZTRiZmRjMDEyIn19fQ==", "&r锡罐");
    public static final ItemStack NIGHT_VISION_GOGGLES = new SlimefunItemStack("NIGHT_VISION_GOGGLES", Material.LEATHER_HELMET, Color.BLACK, "&a夜视眼镜", "", "&9+ 夜视效果");
    public static final ItemStack FARMER_SHOES = new SlimefunItemStack("FARMER_SHOES", Material.LEATHER_BOOTS, Color.YELLOW, "&e农夫的靴子", "", "&6&o能够防止你踩坏农田");
    public static final ItemStack INFUSED_MAGNET = new SlimefunItemStack("INFUSED_MAGNET", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ==", "&aInfused Magnet" , "&a吸入磁铁" , "", "&r注入了魔法的磁铁", "&r能够将附近的物品", "&r放入你的背包", "", "&7按住 &eShift&7 吸取周围的物品");
    public static final ItemStack FLASK_OF_KNOWLEDGE = new SlimefunItemStack("FLASK_OF_KNOWLEDGE", Material.GLASS_BOTTLE, "&c学识之瓶", "", "&r允许你将经验储存在瓶子里", "&7需要消耗 &a1 个等级");
    public static final ItemStack RAG = new SlimefunItemStack("RAG", Material.PAPER, "&c破布", "", "&a1级医疗供给", "", "&r恢复2点血量", "&r可以熄灭身上的火", "", "&7&e右键&7 使用");
    public static final ItemStack BANDAGE = new SlimefunItemStack("BANDAGE", Material.PAPER, "&c绷带", "", "&a2级医疗供给", "", "&r恢复4点血量", "&r可以熄灭身上的火", "", "&7&e右键&7 使用");
    public static final ItemStack SPLINT = new SlimefunItemStack("SPLINT", Material.STICK, "&c夹板", "", "&a1级医疗供给", "", "&r恢复2点血量", "", "&7&e右键&7 使用");
    public static final ItemStack VITAMINS = new SlimefunItemStack("VITAMINS", Material.NETHER_WART, "&c维他命", "", "&a3级医疗供给", "", "&r恢复4点血量", "&r可以熄灭身上的火", "&r治愈中毒/凋零/辐射的负面效果", "", "&7&e右键&7 使用");
    public static final ItemStack MEDICINE = new SlimefunItemStack("MEDICINE", Material.POTION, Color.RED, "&c药物", "", "&a3级医疗供给", "", "&r恢复4点血量", "&r可以熄灭身上的火", "&r治愈中毒/凋零/辐射的负面效果", "", "&7&e右键&7 饮用");

    /*		Backpacks		*/
    public static final ItemStack BACKPACK_SMALL = new SlimefunItemStack("SMALL_BACKPACK","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19", "&e小型背包", "", "&7大小: &e9", "&7ID: <ID>", "", "&7&e右键&7 打开");
    public static final ItemStack BACKPACK_MEDIUM = new SlimefunItemStack("MEDIUM_BACKPACK","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19", "&e普通背包", "", "&7大小: &e18", "&7ID: <ID>", "", "&7&e右键&7 打开");
    public static final ItemStack BACKPACK_LARGE = new SlimefunItemStack("LARGE_BACKPACK","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19", "&e中型背包", "", "&7大小: &e27", "&7ID: <ID>", "", "&7&e右键&7 打开");
    public static final ItemStack WOVEN_BACKPACK =  new SlimefunItemStack("WOVEN_BACKPACK","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19", "&e编织背包", "", "&7大小: &e36", "&7ID: <ID>", "", "&7&e右键&7 打开");
    public static final ItemStack GILDED_BACKPACK = new SlimefunItemStack("GILDED_BACKPACK","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19", "&e镀金背包", "", "&7大小: &e45", "&7ID: <ID>", "", "&7&e右键&7 打开");
    public static final ItemStack RADIANT_BACKPACK = new SlimefunItemStack("RADIANT_BACKPACK","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19", "&e金光闪闪的背包", "", "&7大小: &e54 (大箱子)", "&7ID: <ID>", "", "&7&e右键&7 打开");
    public static final ItemStack BOUND_BACKPACK = new SlimefunItemStack("BOUND_BACKPACK", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ==", "&c灵魂绑定背包", "", "&7大小: &e36", "&7ID: <ID>", "", "&7&e右键&7 打开");
    public static final ItemStack COOLER = new SlimefunItemStack("COOLER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDRjMTU3MjU4NGViNWRlMjI5ZGU5ZjVhNGY3NzlkMGFhY2JhZmZkMzNiY2IzM2ViNDUzNmE2YTJiYzZhMSJ9fX0=", "&b小冰柜", "&r可以储存果汁和冰沙", "&r当小冰柜在你的物品栏里时", "&r在你饥饿时将会自动消耗里面的食物", "", "&7大小: &e27", "&7ID: <ID>", "", "&7&e右键&7 打开");

    /*		 Jetpacks		*/
    public static final ItemStack DURALUMIN_JETPACK = new SlimefunItemStack("DURALUMIN_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eI", "", "&8\u21E8 &7材料: &b硬铝", "&c&o&8\u21E8 &e\u26A1 &70 / 20 J", "&8\u21E8 &7推力: &c0.35", "", "&7按住 &eShift&7 使用");
    public static final ItemStack SOLDER_JETPACK = new SlimefunItemStack("SOLDER_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eII", "", "&8\u21E8 &7材料: &b焊锡", "&c&o&8\u21E8 &e\u26A1 &70 / 30 J", "&8\u21E8 &7推力: &c0.4", "", "&7按住 &eShift&7 使用");
    public static final ItemStack BILLON_JETPACK = new SlimefunItemStack("BILLON_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eIII", "", "&8\u21E8 &7材料: &b银铜合金", "&c&o&8\u21E8 &e\u26A1 &70 / 45 J", "&8\u21E8 &7推力: &c0.45", "", "&7按住 &eShift&7 使用");
    public static final ItemStack STEEL_JETPACK = new SlimefunItemStack("STEEL_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eIV", "", "&8\u21E8 &7材料: &b钢", "&c&o&8\u21E8 &e\u26A1 &70 / 60 J", "&8\u21E8 &7推力: &c0.5", "", "&7按住 &eShift&7 使用");
    public static final ItemStack DAMASCUS_STEEL_JETPACK = new SlimefunItemStack("DAMASCUS_STEEL_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eV", "", "&8\u21E8 &7材料: &b大马士革钢", "&c&o&8\u21E8 &e\u26A1 &70 / 75 J", "&8\u21E8 &7推力: &c0.55", "", "&7按住 &eShift&7 使用");
    public static final ItemStack REINFORCED_ALLOY_JETPACK = new SlimefunItemStack("REINFORCED_ALLOY_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eVI", "", "&8\u21E8 &7材料: &b强化合金", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J", "&8\u21E8 &7推力: &c0.6", "", "&7按住 &eShift&7 使用");
    public static final ItemStack CARBONADO_JETPACK = new SlimefunItemStack("CARBONADO_JETPACK", Material.LEATHER_CHESTPLATE, Color.BLACK, "&9电力喷气背包 &7- &eVII", "", "&8\u21E8 &7材料: &b黑金刚石", "&c&o&8\u21E8 &e\u26A1 &70 / 150 J", "&8\u21E8 &7推力: &c0.7", "", "&7按住 &eShift&7 使用");
    public static final ItemStack ARMORED_JETPACK = new SlimefunItemStack("ARMORED_JETPACK", Material.IRON_CHESTPLATE, "&9装甲喷气背包", "&8\u21E8 &7材料: &b钢", "", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "&8\u21E8 &7推力: &c0.45", "", "&7按住 &eShift&7 使用");

    /*		 Jetboots		*/
    public static final ItemStack DURALUMIN_JETBOOTS = new SlimefunItemStack("DURALUMIN_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eI", "", "&8\u21E8 &7材料: &b硬铝", "&c&o&8\u21E8 &e\u26A1 &70 / 20 J", "&8\u21E8 &7速度: &a0.35", "&8\u21E8 &7准确度: &c50%", "", "&7按住 &eShift&7 使用");
    public static final ItemStack SOLDER_JETBOOTS = new SlimefunItemStack("SOLDER_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eII", "", "&8\u21E8 &7材料: &b焊锡", "&c&o&8\u21E8 &e\u26A1 &70 / 30 J", "&8\u21E8 &7速度: &a0.4", "&8\u21E8 &7准确度: &660%", "", "&7按住 &eShift&7 使用");
    public static final ItemStack BILLON_JETBOOTS = new SlimefunItemStack("BILLON_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eIII", "", "&8\u21E8 &7材料: &b银铜合金", "&c&o&8\u21E8 &e\u26A1 &70 / 40 J", "&8\u21E8 &7速度: &a0.45", "&8\u21E8 &7准确度: &665%", "", "&7按住 &eShift&7 使用");
    public static final ItemStack STEEL_JETBOOTS = new SlimefunItemStack("STEEL_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eIV", "", "&8\u21E8 &7材料: &b钢", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "&8\u21E8 &7速度: &a0.5", "&8\u21E8 &7准确度: &e70%", "", "&7按住 &eShift&7 使用");
    public static final ItemStack DAMASCUS_STEEL_JETBOOTS = new SlimefunItemStack("DAMASCUS_STEEL_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eV", "", "&8\u21E8 &7材料: &b大马士革钢", "&c&o&8\u21E8 &e\u26A1 &70 / 75 J", "&8\u21E8 &7速度: &a0.55", "&8\u21E8 &7准确度: &a75%", "", "&7按住 &eShift&7 使用");
    public static final ItemStack REINFORCED_ALLOY_JETBOOTS = new SlimefunItemStack("REINFORCED_ALLOY_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eVI", "", "&8\u21E8 &7材料: &b强化合金", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J", "&8\u21E8 &7速度: &a0.6", "&8\u21E8 &7准确度: &c80%", "", "&7按住 &eShift&7 使用");
    public static final ItemStack CARBONADO_JETBOOTS = new SlimefunItemStack("CARBONADO_JETBOOTS", Material.LEATHER_BOOTS, Color.BLACK, "&9喷气靴 &7- &eVII", "", "&8\u21E8 &7材料: &b黑金刚石", "&c&o&8\u21E8 &e\u26A1 &70 / 125 J", "&8\u21E8 &7速度: &a0.7", "&8\u21E8 &7准确度: &c99.9%", "", "&7按住 &eShift&7 使用");
    public static final ItemStack ARMORED_JETBOOTS = new SlimefunItemStack("ARMORED_JETBOOTS", Material.IRON_BOOTS, "&9装甲喷气靴", "", "&8\u21E8 &7材料: &b钢", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "&8\u21E8 &7速度: &a0.45", "&8\u21E8 &7准确度: &e70%", "", "&7按住 &eShift&7 使用");

    /*		 Multi Tools		*/
    public static final ItemStack DURALUMIN_MULTI_TOOL = new SlimefunItemStack("DURALUMIN_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eI", "", "&8\u21E8 &7材料: &b硬铝", "&c&o&8\u21E8 &e\u26A1 &70 / 20 J", "", "&7&e右键&7 使用", "&7按住 &eShift + 右键&7 以更改模式");
    public static final ItemStack SOLDER_MULTI_TOOL = new SlimefunItemStack("SOLDER_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eII", "", "&8\u21E8 &7材料: &b焊锡", "&c&o&8\u21E8 &e\u26A1 &70 / 30 J", "", "&7&e右键&7 使用", "&7按住 &eShift + 右键&7 以更改模式");
    public static final ItemStack BILLON_MULTI_TOOL = new SlimefunItemStack("BILLON_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eIII", "", "&8\u21E8 &7材料: &b银铜合金", "&c&o&8\u21E8 &e\u26A1 &70 / 40 J", "", "&7&e右键&7 使用", "&7按住 &eShift + 右键&7 以更改模式");
    public static final ItemStack STEEL_MULTI_TOOL = new SlimefunItemStack("STEEL_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eIV", "", "&8\u21E8 &7材料: &b钢", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "", "&7&e右键&7 使用", "&7按住 &eShift + 右键&7 以更改模式");
    public static final ItemStack DAMASCUS_STEEL_MULTI_TOOL = new SlimefunItemStack("DAMASCUS_STEEL_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eV", "", "&8\u21E8 &7材料: &b大马士革钢", "&c&o&8\u21E8 &e\u26A1 &70 / 60 J", "", "&7&e右键&7 使用", "&7按住 &eShift + 右键&7 以更改模式");
    public static final ItemStack REINFORCED_ALLOY_MULTI_TOOL = new SlimefunItemStack("REINFORCED_ALLOY_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eVI", "", "&8\u21E8 &7材料: &b强化合金", "&c&o&8\u21E8 &e\u26A1 &70 / 75 J", "", "&7&e右键&7 使用", "&7按住 &eShift + 右键&7 以更改模式");
    public static final ItemStack CARBONADO_MULTI_TOOL = new SlimefunItemStack("CARBONADO_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eVII", "", "&8\u21E8 &7材料: &b黑金刚石", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J", "", "&7&e右键&7 使用", "&7按住 &eShift + 右键&7 以更改模式");

    static {
        ItemMeta duralumin = DURALUMIN_MULTI_TOOL.getItemMeta();
        duralumin.setUnbreakable(true);
        DURALUMIN_MULTI_TOOL.setItemMeta(duralumin);

        ItemMeta solder = SOLDER_MULTI_TOOL.getItemMeta();
        solder.setUnbreakable(true);
        SOLDER_MULTI_TOOL.setItemMeta(solder);

        ItemMeta billon = BILLON_MULTI_TOOL.getItemMeta();
        billon.setUnbreakable(true);
        BILLON_MULTI_TOOL.setItemMeta(billon);

        ItemMeta steel = STEEL_MULTI_TOOL.getItemMeta();
        steel.setUnbreakable(true);
        STEEL_MULTI_TOOL.setItemMeta(steel);

        ItemMeta damascus = DAMASCUS_STEEL_MULTI_TOOL.getItemMeta();
        damascus.setUnbreakable(true);
        DAMASCUS_STEEL_MULTI_TOOL.setItemMeta(damascus);

        ItemMeta reinforced = REINFORCED_ALLOY_MULTI_TOOL.getItemMeta();
        reinforced.setUnbreakable(true);
        REINFORCED_ALLOY_MULTI_TOOL.setItemMeta(reinforced);

        ItemMeta carbonado = CARBONADO_MULTI_TOOL.getItemMeta();
        carbonado.setUnbreakable(true);
        CARBONADO_MULTI_TOOL.setItemMeta(carbonado);
    }

    /*		 Food 		*/
    public static final ItemStack FORTUNE_COOKIE = new SlimefunItemStack("FORTUNE_COOKIE", Material.COOKIE, "&6幸运饼干", "", "&a&o告诉你未来发生的事 :o");
    public static final ItemStack DIET_COOKIE = new SlimefunItemStack("DIET_COOKIE", Material.COOKIE, "&6减肥曲奇", "", "&a一个非常&o轻&r&a的曲奇");
    public static final ItemStack MAGIC_SUGAR = new SlimefunItemStack("MAGIC_SUGAR", Material.SUGAR, "&6魔法糖", "", "&a&o感受赫尔墨斯的力量!");
    public static final ItemStack MONSTER_JERKY = new SlimefunItemStack("MONSTER_JERKY", Material.ROTTEN_FLESH, "&6怪物肉干", "", "&a&o提神抗饥饿");
    public static final ItemStack APPLE_JUICE = new SlimefunItemStack("APPLE_JUICE", new CustomPotion("&c苹果汁", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&o恢复 &b&o" + "3.0" + " &7&o的饥饿值"));
    public static final ItemStack MELON_JUICE = new SlimefunItemStack("MELON_JUICE", new CustomPotion("&c西瓜汁", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&o恢复 &b&o" + "3.0" + " &7&o的饥饿值"));
    public static final ItemStack CARROT_JUICE = new SlimefunItemStack("CARROT_JUICE", new CustomPotion("&6胡萝卜汁", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&o恢复 &b&o" + "3.0" + " &7&o的饥饿值"));
    public static final ItemStack PUMPKIN_JUICE = new SlimefunItemStack("PUMPKIN_JUICE", new CustomPotion("&6南瓜汁", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&o恢复 &b&o" + "3.0" + " &7&o的饥饿值"));
    public static final ItemStack SWEET_BERRY_JUICE = new SlimefunItemStack("SWEET_BERRY_JUICE", new CustomPotion("&c浆果汁", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&o恢复 &b&o" + "3.0" + " &7&o的饥饿值"));
    public static final ItemStack GOLDEN_APPLE_JUICE = new SlimefunItemStack("GOLDEN_APPLE_JUICE", new CustomPotion("&b金苹果汁", Color.YELLOW, new PotionEffect(PotionEffectType.ABSORPTION, 20 * 20, 0)));

    public static final ItemStack BEEF_JERKY = new SlimefunItemStack("BEEF_JERKY", Material.COOKED_BEEF, "&6牛肉干", "", "&r横扫饥饿!");
    public static final ItemStack PORK_JERKY = new SlimefunItemStack("PORK_JERKY", Material.COOKED_PORKCHOP, "&6猪肉干", "", "&r横扫饥饿!");
    public static final ItemStack CHICKEN_JERKY = new SlimefunItemStack("CHICKEN_JERKY", Material.COOKED_CHICKEN, "&6鸡肉干", "", "&r横扫饥饿!");
    public static final ItemStack MUTTON_JERKY = new SlimefunItemStack("MUTTON_JERKY", Material.COOKED_MUTTON, "&6羊肉干", "", "&r横扫饥饿!");
    public static final ItemStack RABBIT_JERKY = new SlimefunItemStack("RABBIT_JERKY", Material.COOKED_RABBIT, "&6兔肉干", "", "&r横扫饥饿!");
    public static final ItemStack FISH_JERKY = new SlimefunItemStack("FISH_JERKY", Material.COOKED_COD, "&6鱼干", "", "&r横扫饥饿!");

    /*		Christmas		*/
    public static final ItemStack CHRISTMAS_MILK = new CustomPotion("&6一杯牛奶", Color.WHITE, new PotionEffect(PotionEffectType.SATURATION, 5, 0), "", "&7&o恢复 &b&o" + "2.5" + " &7&o的饥饿值");
    public static final ItemStack CHRISTMAS_CHOCOLATE_MILK = new CustomPotion("&6巧克力牛奶", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 12, 0), "", "&7&o恢复 &b&o" + "6.0" + " &7&o的饥饿值");
    public static final ItemStack CHRISTMAS_EGG_NOG = new CustomPotion("&a蛋酒", Color.GRAY, new PotionEffect(PotionEffectType.SATURATION, 7, 0), "", "&7&o恢复 &b&o" + "3.5" + " &7&o的饥饿值");
    public static final ItemStack CHRISTMAS_APPLE_CIDER = new CustomPotion("&c苹果酒", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 14, 0), "", "&7&o恢复 &b&o" + "7.0" + " &7&o的饥饿值");
    public static final ItemStack CHRISTMAS_COOKIE = new CustomItem(Material.COOKIE, Christmas.color("圣诞曲奇"));
    public static final ItemStack CHRISTMAS_FRUIT_CAKE = new CustomItem(Material.PUMPKIN_PIE, Christmas.color("水果蛋糕"));
    public static final ItemStack CHRISTMAS_APPLE_PIE = new CustomItem(Material.PUMPKIN_PIE, "&r苹果派");
    public static final ItemStack CHRISTMAS_HOT_CHOCOLATE = new CustomPotion("&6热可可", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 14, 0), "", "&7&o恢复 &b&o" + "7.0" + " &7&o的饥饿值");
    public static final ItemStack CHRISTMAS_CAKE = new CustomItem(Material.PUMPKIN_PIE, Christmas.color("圣诞蛋糕"));
    public static final ItemStack CHRISTMAS_CARAMEL = new CustomItem(Material.BRICK, "&6焦糖");
    public static final ItemStack CHRISTMAS_CARAMEL_APPLE = new CustomItem(Material.APPLE, "&6焦糖苹果");
    public static final ItemStack CHRISTMAS_CHOCOLATE_APPLE = new CustomItem(Material.APPLE, "&6巧克力苹果");
    public static final ItemStack CHRISTMAS_PRESENT = new CustomItem(Material.CHEST, Christmas.color("圣诞礼物"),  "&7来自 &emrCookieSlime &7的礼物", "&7收件人: &e你", "", "&e右键&7 拆开");

    /*		Easter			*/
    public static final ItemStack EASTER_EGG = new SlimefunItemStack("EASTER_EGG", Material.EGG, "&r复活节彩蛋", "&b惊喜!");
    public static final ItemStack EASTER_CARROT_PIE = new SlimefunItemStack("CARROT_PIE", Material.PUMPKIN_PIE, "&6胡萝卜派");

    /*		 Weapons 		*/
    public static final ItemStack GRANDMAS_WALKING_STICK = new SlimefunItemStack("GRANDMAS_WALKING_STICK", Material.STICK, "&7奶奶的拐杖");
    public static final ItemStack GRANDPAS_WALKING_STICK = new SlimefunItemStack("GRANDPAS_WALKING_STICK", Material.STICK, "&7爷爷的拐杖");
    public static final ItemStack SWORD_OF_BEHEADING = new SlimefunItemStack("SWORD_OF_BEHEADING", Material.IRON_SWORD, "&6处决之剑", "&7斩首处决 II", "", "&r有几率砍下生物的头", "&r(提高掉落凋灵骷髅头的几率)");
    public static final ItemStack BLADE_OF_VAMPIRES = new SlimefunItemStack("BLADE_OF_VAMPIRES", Material.GOLDEN_SWORD, "&c吸血鬼之刀", "&7生命窃取 I", "", "&r在攻击时有 45% 的几率", "使自己恢复 2 点血量");
    public static final ItemStack SEISMIC_AXE = new SlimefunItemStack("SEISMIC_AXE", Material.IRON_AXE, "&a地震斧", "", "&7&o制造一场地震...", "", "&7&e右键&7 使用");

    static {
        GRANDMAS_WALKING_STICK.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        GRANDPAS_WALKING_STICK.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
    }

    /*		Bows		*/
    public static final ItemStack EXPLOSIVE_BOW = new SlimefunItemStack("EXPLOSIVE_BOW", Material.BOW, "&c爆裂之弓", "&r被爆裂之弓射出的箭击中将会飞上天");
    public static final ItemStack ICY_BOW = new SlimefunItemStack("ICY_BOW", Material.BOW, "&b冰封之弓", "&r被此弓射出的箭击中", "&r将会因寒冷而无法移动 (2秒内)");
    public static final ItemStack WITHER_BOW = new SlimefunItemStack("WITHER_BOW", Material.BOW, "&a凋零之弓", "&c&o花开凋谢之时", "", "&r将会给予射中的实体凋零效果");

    /*		 Tools		*/
    public static final ItemStack AUTO_SMELT_PICKAXE = new SlimefunItemStack("SMELTERS_PICKAXE", Material.DIAMOND_PICKAXE, "&6熔炉镐", "&c&l自动熔炼", "", "&9在挖矿时有时运效果");
    public static final ItemStack LUMBER_AXE = new SlimefunItemStack("LUMBER_AXE", Material.DIAMOND_AXE, "&6伐木斧","&a&o砍倒整棵树木...");
    public static final ItemStack PICKAXE_OF_CONTAINMENT = new SlimefunItemStack("PICKAXE_OF_CONTAINMENT", Material.IRON_PICKAXE, "&c刷怪笼之镐", "", "&9可以获取刷怪笼");
    public static final ItemStack HERCULES_PICKAXE = new SlimefunItemStack("HERCULES_PICKAXE", Material.IRON_PICKAXE, "&9赫拉克勒斯之镐", "", "&r它如此强大", "&r因此能自动将挖到的矿物变为粉末...");
    public static final ItemStack EXPLOSIVE_PICKAXE = new SlimefunItemStack("EXPLOSIVE_PICKAXE", Material.DIAMOND_PICKAXE, "&e爆炸稿", "", "&r允许你在一瞬间挖掘矿物", "", "&9在挖矿时有时运效果");
    public static final ItemStack EXPLOSIVE_SHOVEL = new SlimefunItemStack("EXPLOSIVE_SHOVEL", Material.DIAMOND_SHOVEL, "&e爆炸铲", "", "&r让你一下子就能挖掉很多方块");
    public static final ItemStack PICKAXE_OF_THE_SEEKER = new SlimefunItemStack("PICKAXE_OF_THE_SEEKER", Material.DIAMOND_PICKAXE, "&a寻矿稿", "&r使用时将会指出你附近的矿物", "&r但可能它会受到损伤", "", "&7&e右键&7 以寻找四周的矿物");
    public static final ItemStack COBALT_PICKAXE = new SlimefunItemStack("COBALT_PICKAXE", Material.IRON_PICKAXE, "&9钴镐");
    public static final ItemStack PICKAXE_OF_VEIN_MINING = new SlimefunItemStack("PICKAXE_OF_VEIN_MINING", Material.DIAMOND_PICKAXE, "&e矿脉镐", "", "&r这个镐子将会挖出", "&r整个矿脉的矿物...");

    static {
        HERCULES_PICKAXE.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        HERCULES_PICKAXE.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);

        COBALT_PICKAXE.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        COBALT_PICKAXE.addUnsafeEnchantment(Enchantment.DIG_SPEED, 6);
    }

    /*		 Armor 		*/
    public static final ItemStack GLOWSTONE_HELMET = new SlimefunItemStack("GLOWSTONE_HELMET", Material.LEATHER_HELMET, Color.YELLOW, "&e&l萤石头盔", "", "&a&o像太阳一样闪耀!", "", "&9+ 夜视效果");
    public static final ItemStack GLOWSTONE_CHESTPLATE = new SlimefunItemStack("GLOWSTONE_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.YELLOW, "&e&l萤石胸甲", "", "&a&o像太阳一样闪耀!", "", "&9+ 夜视效果");
    public static final ItemStack GLOWSTONE_LEGGINGS = new SlimefunItemStack("GLOWSTONE_LEGGINGS", Material.LEATHER_LEGGINGS, Color.YELLOW, "&e&l萤石护腿", "", "&a&o像太阳一样闪耀!", "", "&9+ 夜视效果");
    public static final ItemStack GLOWSTONE_BOOTS = new SlimefunItemStack("GLOWSTONE_BOOTS", Material.LEATHER_BOOTS, Color.YELLOW, "&e&l萤石靴子", "", "&a&o像太阳一样闪耀!", "", "&9+ 夜视效果");

    public static final ItemStack ENDER_HELMET = new SlimefunItemStack("ENDER_HELMET", Material.LEATHER_HELMET, Color.fromRGB(28, 25, 112), "&5&l末影头盔", "", "&a&o任意移动");
    public static final ItemStack ENDER_CHESTPLATE = new SlimefunItemStack("ENDER_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.fromRGB(28, 25, 112), "&5&l末影胸甲", "", "&a&o任意移动");
    public static final ItemStack ENDER_LEGGINGS = new SlimefunItemStack("ENDER_LEGGINGS", Material.LEATHER_LEGGINGS, Color.fromRGB(28, 25, 112), "&5&l末影护腿", "", "&a&o任意移动");
    public static final ItemStack ENDER_BOOTS = new SlimefunItemStack("ENDER_BOOTS", Material.LEATHER_BOOTS, Color.fromRGB(28, 25, 112), "&5&l末影靴子", "", "&a&o任意移动", "" , "&9+ 使用末影珍珠时无伤害");

    public static final ItemStack SLIME_HELMET = new SlimefunItemStack("SLIME_HELMET", Material.LEATHER_HELMET, Color.LIME, "&a&l史莱姆头盔", "", "&a&o有弹性的感觉");
    public static final ItemStack SLIME_CHESTPLATE = new SlimefunItemStack("SLIME_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.LIME, "&a&l史莱姆胸甲", "", "&a&o有弹性的感觉");
    public static final ItemStack SLIME_LEGGINGS = new SlimefunItemStack("SLIME_LEGGINGS", Material.LEATHER_LEGGINGS, Color.LIME, "&a&l史莱姆护腿", "", "&a&o有弹性的感觉", "", "&9+ 速度");
    public static final ItemStack SLIME_BOOTS = new SlimefunItemStack("SLIME_BOOTS", Material.LEATHER_BOOTS, Color.LIME, "&a&l史莱姆靴子", "", "&a&o有弹性的感觉", "", "&9+ 跳跃提升", "&9+ 减免摔落伤害");

    public static final ItemStack CACTUS_HELMET = new SlimefunItemStack("CACTUS_HELMET", Material.LEATHER_HELMET, Color.GREEN, "&2仙人掌头盔");
    public static final ItemStack CACTUS_CHESTPLATE = new SlimefunItemStack("CACTUS_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.GREEN, "&2仙人掌胸甲");
    public static final ItemStack CACTUS_LEGGINGS = new SlimefunItemStack("CACTUS_LEGGINGS", Material.LEATHER_LEGGINGS, Color.GREEN, "&2仙人掌护腿");
    public static final ItemStack CACTUS_BOOTS = new SlimefunItemStack("CACTUS_BOOTS", Material.LEATHER_BOOTS, Color.GREEN, "&2仙人掌靴子");

    public static final ItemStack DAMASCUS_STEEL_HELMET = new SlimefunItemStack("DAMASCUS_STEEL_HELMET", Material.IRON_HELMET, "&7大马士革钢头盔");
    public static final ItemStack DAMASCUS_STEEL_CHESTPLATE = new SlimefunItemStack("DAMASCUS_STEEL_CHESTPLATE", Material.IRON_CHESTPLATE, "&7大马士革钢胸甲");
    public static final ItemStack DAMASCUS_STEEL_LEGGINGS = new SlimefunItemStack("DAMASCUS_STEEL_LEGGINGS", Material.IRON_LEGGINGS, "&7大马士革钢护腿");
    public static final ItemStack DAMASCUS_STEEL_BOOTS = new SlimefunItemStack("DAMASCUS_STEEL_BOOTS", Material.IRON_BOOTS, "&7大马士革钢靴子");

    public static final ItemStack REINFORCED_ALLOY_HELMET = new SlimefunItemStack("REINFORCED_ALLOY_HELMET", Material.IRON_HELMET, "&b强化合金头盔");
    public static final ItemStack REINFORCED_ALLOY_CHESTPLATE = new SlimefunItemStack("REINFORCED_ALLOY_CHESTPLATE", Material.IRON_CHESTPLATE, "&b强化合金胸甲");
    public static final ItemStack REINFORCED_ALLOY_LEGGINGS = new SlimefunItemStack("REINFORCED_ALLOY_LEGGINGS", Material.IRON_LEGGINGS, "&b强化合金护腿");
    public static final ItemStack REINFORCED_ALLOY_BOOTS = new SlimefunItemStack("REINFORCED_ALLOY_BOOTS", Material.IRON_BOOTS, "&b强化合金靴子");

    public static final ItemStack SCUBA_HELMET = new SlimefunItemStack("SCUBA_HELMET", Material.LEATHER_HELMET, Color.ORANGE,  "&c潜水头盔", "", "&b让你能够在水下呼吸", "&4&o防化套装的一部分");
    public static final ItemStack HAZMATSUIT_CHESTPLATE = new SlimefunItemStack("HAZMAT_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.ORANGE,  "&c防化服", "", "&b让你能在火焰中行走", "&4&o防化套装的一部分");
    public static final ItemStack HAZMATSUIT_LEGGINGS = new SlimefunItemStack("HAZMAT_LEGGINGS", Material.LEATHER_LEGGINGS, Color.ORANGE, "&c防化护腿", "", "&4&o防化套装的一部分");
    public static final ItemStack RUBBER_BOOTS = new SlimefunItemStack("RUBBER_BOOTS", Material.LEATHER_BOOTS, Color.BLACK, "&c橡胶靴", "", "&4&o防化套装的一部分");

    public static final ItemStack GILDED_IRON_HELMET = new SlimefunItemStack("GILDED_IRON_HELMET", Material.GOLDEN_HELMET, "&6镀金铁头盔");
    public static final ItemStack GILDED_IRON_CHESTPLATE = new SlimefunItemStack("GILDED_IRON_CHESTPLATE", Material.GOLDEN_CHESTPLATE, "&6镀金铁胸甲");
    public static final ItemStack GILDED_IRON_LEGGINGS = new SlimefunItemStack("GILDED_IRON_LEGGINGS", Material.GOLDEN_LEGGINGS, "&6镀金铁护腿");
    public static final ItemStack GILDED_IRON_BOOTS = new SlimefunItemStack("GILDED_IRON_BOOTS", Material.GOLDEN_BOOTS, "&6镀金铁靴子");

    public static final ItemStack GOLD_HELMET = new SlimefunItemStack("GOLD_12K_HELMET", Material.GOLDEN_HELMET, "&6金头盔", "&912克拉");
    public static final ItemStack GOLD_CHESTPLATE = new SlimefunItemStack("GOLD_12K_CHESTPLATE", Material.GOLDEN_CHESTPLATE, "&6金胸甲", "&912克拉");
    public static final ItemStack GOLD_LEGGINGS = new SlimefunItemStack("GOLD_12K_LEGGINGS", Material.GOLDEN_LEGGINGS, "&6金护腿", "&912克拉");
    public static final ItemStack GOLD_BOOTS = new SlimefunItemStack("GOLD_12K_BOOTS", Material.GOLDEN_BOOTS, "&6金靴子", "&912克拉");

    public static final ItemStack SLIME_HELMET_STEEL = new SlimefunItemStack("SLIME_STEEL_HELMET", Material.IRON_HELMET, "&a&l史莱姆头盔", "&7&o已强化", "", "&a&o有弹性的感觉");
    public static final ItemStack SLIME_CHESTPLATE_STEEL = new SlimefunItemStack("SLIME_STEEL_CHESTPLATE", Material.IRON_CHESTPLATE, "&a&l史莱姆胸甲", "&7&o已强化", "", "&a&o有弹性的感觉");
    public static final ItemStack SLIME_LEGGINGS_STEEL = new SlimefunItemStack("SLIME_STEEL_LEGGINGS", Material.IRON_LEGGINGS, "&a&l史莱姆护腿", "&7&o已强化", "", "&a&o有弹性的感觉", "", "&9+ 速度");
    public static final ItemStack SLIME_BOOTS_STEEL = new SlimefunItemStack("SLIME_STEEL_BOOTS", Material.IRON_BOOTS, "&a&l史莱姆靴子", "&7&o已强化", "", "&a&o有弹性的感觉", "", "&9+ 跳跃提升", "&9+ 减免摔落伤害");

    public static final ItemStack BOOTS_OF_THE_STOMPER = new SlimefunItemStack("BOOTS_OF_THE_STOMPER", Material.LEATHER_BOOTS, Color.AQUA, "&b践踏者之靴", "", "&9你受到的所有摔落伤害", "&9将转给附近的生物/玩家", "", "&9+ 减免摔落伤害");

    static {
        Map<Enchantment, Integer> cactus = new HashMap<>();
        cactus.put(Enchantment.THORNS, 3);
        cactus.put(Enchantment.DURABILITY, 6);

        CACTUS_HELMET.addUnsafeEnchantments(cactus);
        CACTUS_CHESTPLATE.addUnsafeEnchantments(cactus);
        CACTUS_LEGGINGS.addUnsafeEnchantments(cactus);
        CACTUS_BOOTS.addUnsafeEnchantments(cactus);

        Map<Enchantment, Integer> damascus = new HashMap<>();
        damascus.put(Enchantment.DURABILITY, 5);
        damascus.put(Enchantment.PROTECTION_ENVIRONMENTAL, 5);

        DAMASCUS_STEEL_HELMET.addUnsafeEnchantments(damascus);
        DAMASCUS_STEEL_CHESTPLATE.addUnsafeEnchantments(damascus);
        DAMASCUS_STEEL_LEGGINGS.addUnsafeEnchantments(damascus);
        DAMASCUS_STEEL_BOOTS.addUnsafeEnchantments(damascus);

        Map<Enchantment, Integer> reinforced = new HashMap<>();
        reinforced.put(Enchantment.DURABILITY, 9);
        reinforced.put(Enchantment.PROTECTION_ENVIRONMENTAL, 9);

        REINFORCED_ALLOY_HELMET.addUnsafeEnchantments(reinforced);
        REINFORCED_ALLOY_CHESTPLATE.addUnsafeEnchantments(reinforced);
        REINFORCED_ALLOY_LEGGINGS.addUnsafeEnchantments(reinforced);
        REINFORCED_ALLOY_BOOTS.addUnsafeEnchantments(reinforced);

        Map<Enchantment, Integer> gilded = new HashMap<>();
        gilded.put(Enchantment.DURABILITY, 6);
        gilded.put(Enchantment.PROTECTION_ENVIRONMENTAL, 8);

        GILDED_IRON_HELMET.addUnsafeEnchantments(gilded);
        GILDED_IRON_CHESTPLATE.addUnsafeEnchantments(gilded);
        GILDED_IRON_LEGGINGS.addUnsafeEnchantments(gilded);
        GILDED_IRON_BOOTS.addUnsafeEnchantments(gilded);

        GOLD_HELMET.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        GOLD_CHESTPLATE.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        GOLD_LEGGINGS.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        GOLD_BOOTS.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        Map<Enchantment, Integer> slime = new HashMap<>();
        slime.put(Enchantment.DURABILITY, 4);
        slime.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        SLIME_HELMET_STEEL.addUnsafeEnchantments(slime);
        SLIME_CHESTPLATE_STEEL.addUnsafeEnchantments(slime);
        SLIME_LEGGINGS_STEEL.addUnsafeEnchantments(slime);
        SLIME_BOOTS_STEEL.addUnsafeEnchantments(slime);
    }

    /*		 Misc 		*/
    public static final ItemStack MAGIC_LUMP_1 = new SlimefunItemStack("MAGIC_LUMP_1", Material.GOLD_NUGGET, "&6魔法因子 &7- &eI", "", "&c&o等级: I");
    public static final ItemStack MAGIC_LUMP_2 = new SlimefunItemStack("MAGIC_LUMP_2", Material.GOLD_NUGGET, "&6魔法因子 &7- &eII", "", "&c&o等级: II");
    public static final ItemStack MAGIC_LUMP_3 = new SlimefunItemStack("MAGIC_LUMP_3", Material.GOLD_NUGGET, "&6魔法因子 &7- &eIII", "", "&c&o等级: III");
    public static final ItemStack ENDER_LUMP_1 = new SlimefunItemStack("ENDER_LUMP_1", Material.GOLD_NUGGET, "&5末影因子 &7- &eI", "", "&c&o等级: I");
    public static final ItemStack ENDER_LUMP_2 = new SlimefunItemStack("ENDER_LUMP_2", Material.GOLD_NUGGET, "&5末影因子 &7- &eII", "", "&c&o等级: II");
    public static final ItemStack ENDER_LUMP_3 = new SlimefunItemStack("ENDER_LUMP_3", Material.GOLD_NUGGET, "&5末影因子 &7- &eIII", "", "&c&o等级: III");
    public static final ItemStack MAGICAL_BOOK_COVER = new SlimefunItemStack("MAGICAL_BOOK_COVER", Material.PAPER, "&6魔法书皮", "", "&a&o用于各种魔法书");
    public static final ItemStack BASIC_CIRCUIT_BOARD = new SlimefunItemStack("BASIC_CIRCUIT_BOARD", Material.ACTIVATOR_RAIL, "&b基础电路板");
    public static final ItemStack ADVANCED_CIRCUIT_BOARD = new SlimefunItemStack("ADVANCED_CIRCUIT_BOARD", Material.POWERED_RAIL, "&b高级电路板");
    public static final ItemStack WHEAT_FLOUR = new SlimefunItemStack("WHEAT_FLOUR", Material.SUGAR, "&r小麦粉");
    public static final ItemStack STEEL_PLATE = new SlimefunItemStack("STEEL_PLATE", Material.PAPER, "&7&l钢板");
    public static final ItemStack BATTERY = new SlimefunItemStack("BATTERY", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmUyZGRhNmVmNjE4NWQ0ZGQ2ZWE4Njg0ZTk3ZDM5YmE4YWIwMzdlMjVmNzVjZGVhNmJkMjlkZjhlYjM0ZWUifX19", "&6电池");
    public static final ItemStack CARBON = new SlimefunItemStack("CARBON", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGIzYTA5NWI2YjgxZTZiOTg1M2ExOTMyNGVlZGYwYmI5MzQ5NDE3MjU4ZGQxNzNiOGVmZjg3YTA4N2FhIn19fQ==", "&e碳");
    public static final ItemStack COMPRESSED_CARBON = new SlimefunItemStack("COMPRESSED_CARBON", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIxZDQ5NTE2NTc0OGQzMTE2Zjk5ZDZiNWJkNWQ0MmViOGJhNTkyYmNkZmFkMzdmZDk1ZjliNmMwNGEzYiJ9fX0=", "&c压缩碳");
    public static final ItemStack CARBON_CHUNK = new SlimefunItemStack("CARBON_CHUNK", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIxZDQ5NTE2NTc0OGQzMTE2Zjk5ZDZiNWJkNWQ0MmViOGJhNTkyYmNkZmFkMzdmZDk1ZjliNmMwNGEzYiJ9fX0=", "&4碳块");
    public static final ItemStack STEEL_THRUSTER = new SlimefunItemStack("STEEL_THRUSTER", Material.BUCKET, "&7&l钢推进器");
    public static final ItemStack POWER_CRYSTAL = new SlimefunItemStack("POWER_CRYSTAL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNjMWIwMzZiNmUwMzUxN2IyODVhODExYmQ4NWU3M2Y1YWJmZGFjYzFkZGY5MGRmZjk2MmUxODA5MzRlMyJ9fX0=", "&c&l能量水晶");
    public static final ItemStack CHAIN = new SlimefunItemStack("CHAIN", Material.STRING, "&b锁链");
    public static final ItemStack HOOK = new SlimefunItemStack("HOOK", Material.FLINT, "&b钩子");
    public static final ItemStack SIFTED_ORE = new SlimefunItemStack("SIFTED_ORE", Material.GUNPOWDER, "&6筛矿");
    public static final ItemStack STONE_CHUNK = new SlimefunItemStack("STONE_CHUNK", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2U4ZjVhZGIxNGQ2YzlmNmI4MTBkMDI3NTQzZjFhOGMxZjQxN2UyZmVkOTkzYzk3YmNkODljNzRmNWUyZTgifX19", "&6石块");
    public static final ItemStack LAVA_CRYSTAL = new SlimefunItemStack("LAVA_CRYSTAL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNhZDhlZTg0OWVkZjA0ZWQ5YTI2Y2EzMzQxZjYwMzNiZDc2ZGNjNDIzMWVkMWVhNjNiNzU2NTc1MWIyN2FjIn19fQ==", "&4岩浆水晶");
    public static final ItemStack SALT = new SlimefunItemStack("SALT", Material.SUGAR, "&r盐");
    public static final ItemStack CHEESE = new SlimefunItemStack("CHEESE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRmZWJiYzE1ZDFkNGNjNjJiZWRjNWQ3YTJiNmYwZjQ2Y2Q1YjA2OTZhODg0ZGU3NWUyODllMzVjYmI1M2EwIn19fQ==", "&r黄油");
    public static final ItemStack BUTTER = new SlimefunItemStack("BUTTER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY2YjE5ZjdkNjM1ZDAzNDczODkxZGYzMzAxN2M1NDkzNjMyMDlhOGY2MzI4YTg1NDJjMjEzZDA4NTI1ZSJ9fX0=", "&r奶酪");
    public static final ItemStack DUCT_TAPE = new SlimefunItemStack("DUCT_TAPE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJmYWFjZWFiNjM4NGZmZjVlZDI0YmI0NGE0YWYyZjU4NGViMTM4MjcyOWVjZDkzYTUzNjlhY2ZkNjY1NCJ9fX0=",  "&8强力胶布", "", "&r可以用这个在自动铁砧里", "&r修复物品");
    public static final ItemStack HEAVY_CREAM = new SlimefunItemStack("HEAVY_CREAM", Material.SNOWBALL, "&r浓奶油");
    public static final ItemStack CRUSHED_ORE = new SlimefunItemStack("CRUSHED_ORE", Material.GUNPOWDER, "&6已粉碎的矿石");
    public static final ItemStack PULVERIZED_ORE = new SlimefunItemStack("PULVERIZED_ORE", Material.GUNPOWDER, "&6粉末状的矿石");
    public static final ItemStack PURE_ORE_CLUSTER = new SlimefunItemStack("PURE_ORE_CLUSTER", Material.GUNPOWDER, "&6纯矿簇");
    public static final ItemStack SMALL_URANIUM = new SlimefunItemStack("SMALL_URANIUM", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiMjlhZmE2ZDZkYzkyM2UyZTEzMjRiZjgxOTI3NTBmN2JkYmRkYzY4OTYzMmEyYjZjMThkOWZlN2E1ZSJ9fX0=", "&c一小堆铀", "", "&c辐射等级: 低", "&4&o不需要防化套装");
    public static final ItemStack TINY_URANIUM = new SlimefunItemStack("TINY_URANIUM", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiMjlhZmE2ZDZkYzkyM2UyZTEzMjRiZjgxOTI3NTBmN2JkYmRkYzY4OTYzMmEyYjZjMThkOWZlN2E1ZSJ9fX0=", "&c一小块铀", "", "&e辐射等级: 中等", "&4&o需要防化套装");

    public static final ItemStack MAGNET = new SlimefunItemStack("MAGNET", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ==", "&c磁铁");
    public static final ItemStack NECROTIC_SKULL = new SlimefunItemStack("NECROTIC_SKULL", Material.WITHER_SKELETON_SKULL, "&c坏死颅骨");
    public static final ItemStack ESSENCE_OF_AFTERLIFE = new SlimefunItemStack("ESSENCE_OF_AFTERLIFE", Material.GUNPOWDER, "&4来世精华");
    public static final ItemStack ELECTRO_MAGNET = new SlimefunItemStack("ELECTRO_MAGNET", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ==", "&c电磁铁");
    public static final ItemStack HEATING_COIL = new SlimefunItemStack("HEATING_COIL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2UzYmM0ODkzYmE0MWEzZjczZWUyODE3NGNkZjRmZWY2YjE0NWU0MWZlNmM4MmNiN2JlOGQ4ZTk3NzFhNSJ9fX0=", "&c加热线圈");
    public static final ItemStack COOLING_UNIT = new SlimefunItemStack("COOLING_UNIT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU0YmFkODZjOTlkZjc4MGM4ODlhMTA5OGY3NzY0OGVhZDczODVjYzFkZGIwOTNkYTVhN2Q4YzRjMmFlNTRkIn19fQ==", "&b冷却装置");
    public static final ItemStack ELECTRIC_MOTOR = new SlimefunItemStack("ELECTRIC_MOTOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ==", "&c电动马达");
    public static final ItemStack CARGO_MOTOR = new SlimefunItemStack("CARGO_MOTOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ==", "&3货运马达");
    public static final ItemStack SCROLL_OF_DIMENSIONAL_TELEPOSITION = new SlimefunItemStack("SCROLL_OF_DIMENSIONAL_TELEPOSITION", Material.PAPER, "&6维度传送卷轴", "", "&c这个卷轴可以便携地创建", "&c一个临时的黑洞", "&c将附近的实体都传送至", "&c另一个维度上", "&c所有东西都反转了", "", "&r就是说: 让实体转180°");
    public static final ItemStack TOME_OF_KNOWLEDGE_SHARING = new SlimefunItemStack("TOME_OF_KNOWLEDGE_SHARING", Material.BOOK, "&6知识共享之书", "&7主人: &bNone", "", "&e右键&7 以绑定你的所有研究", "", "", "&e右键&7 以获得前任主人的所有研究");
    public static final ItemStack HARDENED_GLASS = new SlimefunItemStack("HARDENED_GLASS", Material.LIGHT_GRAY_STAINED_GLASS, "&7钢化玻璃", "", "&r可以抵抗爆炸");
    public static final ItemStack WITHER_PROOF_OBSIDIAN = new SlimefunItemStack("WITHER_PROOF_OBSIDIAN", Material.OBSIDIAN, "&5防凋零黑曜石", "", "&r可以抵抗爆炸和", "&r凋零的攻击");
    public static final ItemStack WITHER_PROOF_GLASS = new SlimefunItemStack("WITHER_PROOF_GLASS", Material.PURPLE_STAINED_GLASS, "&5防凋零玻璃", "", "&r可以抵抗爆炸和", "&r凋零的攻击");
    public static final ItemStack REINFORCED_PLATE = new SlimefunItemStack("REINFORCED_PLATE", Material.PAPER, "&7钢筋板");
    public static final ItemStack ANCIENT_PEDESTAL = new SlimefunItemStack("ANCIENT_PEDESTAL", Material.DISPENSER, "&d古代基座", "", "&5古代祭坛的一部分");
    public static final ItemStack ANCIENT_ALTAR = new SlimefunItemStack("ANCIENT_ALTAR", Material.ENCHANTING_TABLE, "&d古代祭坛", "", "&5在世界里建造祭坛", "&5并用古老的仪式合成物品");
    public static final ItemStack COPPER_WIRE = new SlimefunItemStack("COPPER_WIRE", Material.STRING, "&6铜线", "", "&6电子配件中的重要组成部分");

    public static final ItemStack RAINBOW_WOOL = new SlimefunItemStack("RAINBOW_WOOL", Material.WHITE_WOOL, "&5彩虹羊毛", "", "&d轮番展现彩虹的颜色!");
    public static final ItemStack RAINBOW_GLASS = new SlimefunItemStack("RAINBOW_GLASS", Material.WHITE_STAINED_GLASS, "&5彩虹玻璃", "", "&d轮番展现彩虹的颜色!");
    public static final ItemStack RAINBOW_CLAY = new SlimefunItemStack("RAINBOW_CLAY", Material.WHITE_TERRACOTTA, "&5彩虹粘土块", "", "&d轮番展现彩虹的颜色!");
    public static final ItemStack RAINBOW_GLASS_PANE = new SlimefunItemStack("RAINBOW_GLASS_PANE", Material.WHITE_STAINED_GLASS_PANE, "&5彩虹玻璃板", "", "&d轮番展现彩虹的颜色!");

    public static final ItemStack RAINBOW_WOOL_XMAS = new SlimefunItemStack("RAINBOW_WOOL_XMAS", Material.WHITE_WOOL, "&5彩虹羊毛 &7(圣诞节版)", "", Christmas.color("< 圣诞节版 >"));
    public static final ItemStack RAINBOW_GLASS_XMAS = new SlimefunItemStack("RAINBOW_GLASS_XMAS", Material.WHITE_STAINED_GLASS, "&5彩虹玻璃 &7(圣诞节版)", "", Christmas.color("< 圣诞节版 >"));
    public static final ItemStack RAINBOW_CLAY_XMAS = new SlimefunItemStack("RAINBOW_CLAY_XMAS", Material.WHITE_TERRACOTTA, "&5彩虹粘土块 &7(圣诞节版)", "", Christmas.color("< 圣诞节版 >"));
    public static final ItemStack RAINBOW_GLASS_PANE_XMAS = new SlimefunItemStack("RAINBOW_GLASS_PANE_XMAS", Material.WHITE_STAINED_GLASS_PANE, "&5彩虹玻璃板 &7(圣诞节版)", "", Christmas.color("< 圣诞节版 >"));

    public static final ItemStack RAINBOW_WOOL_VALENTINE = new SlimefunItemStack("RAINBOW_WOOL_VALENTINE", Material.PINK_WOOL, "&5彩虹羊毛 &7(情人节版)", "", "&d< 情人节版 >");
    public static final ItemStack RAINBOW_GLASS_VALENTINE = new SlimefunItemStack("RAINBOW_GLASS_VALENTINE", Material.PINK_STAINED_GLASS, "&5彩虹玻璃 &7(情人节版)", "", "&d< 情人节版 >");
    public static final ItemStack RAINBOW_CLAY_VALENTINE = new SlimefunItemStack("RAINBOW_CLAY_VALENTINE", Material.PINK_TERRACOTTA, "&5彩虹粘土块 &7(情人节版)", "", "&d< 情人节版 >");
    public static final ItemStack RAINBOW_GLASS_PANE_VALENTINE = new SlimefunItemStack("RAINBOW_GLASS_PANE_VALENTINE", Material.PINK_STAINED_GLASS_PANE, "&5彩虹玻璃板 &7(情人节版)", "", "&d< 情人节版 >");

    /*		锭s 		*/
    public static final ItemStack COPPER_INGOT = new SlimefunItemStack("COPPER_INGOT", Material.BRICK, "&b铜锭");
    public static final ItemStack TIN_INGOT = new SlimefunItemStack("TIN_INGOT", Material.IRON_INGOT, "&b锡锭");
    public static final ItemStack SILVER_INGOT = new SlimefunItemStack("SILVER_INGOT", Material.IRON_INGOT, "&b银锭");
    public static final ItemStack ALUMINUM_INGOT = new SlimefunItemStack("ALUMINUM_INGOT", Material.IRON_INGOT, "&b铝锭");
    public static final ItemStack LEAD_INGOT = new SlimefunItemStack("LEAD_INGOT", Material.IRON_INGOT, "&b铅锭");
    public static final ItemStack ZINC_INGOT = new SlimefunItemStack("ZINC_INGOT", Material.IRON_INGOT, "&b锌锭");
    public static final ItemStack MAGNESIUM_INGOT = new SlimefunItemStack("MAGNESIUM_INGOT", Material.IRON_INGOT, "&b镁锭");

    /*		Alloy (Carbon + Iron)	*/
    public static final ItemStack STEEL_INGOT = new SlimefunItemStack("STEEL_INGOT", Material.IRON_INGOT, "&b钢锭");
    /*		Alloy (Copper + Tin)	*/
    public static final ItemStack BRONZE_INGOT = new SlimefunItemStack("BRONZE_INGOT", Material.BRICK, "&b青铜锭");
    /*		Alloy (Copper + Aluminum)	*/
    public static final ItemStack DURALUMIN_INGOT = new SlimefunItemStack("DURALUMIN_INGOT", Material.IRON_INGOT, "&b硬铝锭");
    /*		Alloy (Copper + Silver)	*/
    public static final ItemStack BILLON_INGOT = new SlimefunItemStack("BILLON_INGOT", Material.IRON_INGOT, "&b银铜合金锭");
    /*		Alloy (Copper + Zinc)	*/
    public static final ItemStack BRASS_INGOT = new SlimefunItemStack("BRASS_INGOT", Material.GOLD_INGOT, "&b黄铜锭");
    /*		Alloy (Aluminum + Brass)	*/
    public static final ItemStack ALUMINUM_BRASS_INGOT = new SlimefunItemStack("ALUMINUM_BRASS_INGOT", Material.GOLD_INGOT, "&b铝黄铜锭");
    /*		Alloy (Aluminum + Bronze)	*/
    public static final ItemStack ALUMINUM_BRONZE_INGOT = new SlimefunItemStack("ALUMINUM_BRONZE_INGOT", Material.GOLD_INGOT, "&b铝青铜锭");
    /*		Alloy (Gold + Silver + Copper)	*/
    public static final ItemStack CORINTHIAN_BRONZE_INGOT = new SlimefunItemStack("CORINTHIAN_BRONZE_INGOT", Material.GOLD_INGOT, "&b科林斯青铜锭");
    /*		Alloy (Lead + Tin)	*/
    public static final ItemStack SOLDER_INGOT = new SlimefunItemStack("SOLDER_INGOT", Material.IRON_INGOT, "&b焊锡锭");
    /*		Alloy (Steel + Iron + Carbon)	*/
    public static final ItemStack DAMASCUS_STEEL_INGOT = new SlimefunItemStack("DAMASCUS_STEEL_INGOT", Material.IRON_INGOT, "&b大马士革钢锭");
    /*		Alloy (大马士革钢 + 硬铝 + Compressed Carbon + Aluminium Bronze)	*/
    public static final ItemStack HARDENED_METAL_INGOT = new SlimefunItemStack("HARDENED_METAL_INGOT", Material.IRON_INGOT, "&b&l硬化金属");
    /*		Alloy (Hardened Metal + Corinthian Bronze + Solder + Billon + 大马士革钢)	*/
    public static final ItemStack REINFORCED_ALLOY_INGOT = new SlimefunItemStack("REINFORCED_ALLOY_INGOT", Material.IRON_INGOT, "&b&l强化合金锭");
    /*		Alloy (Iron + Silicon)		*/
    public static final ItemStack FERROSILICON = new SlimefunItemStack("FERROSILICON", Material.IRON_INGOT, "&b硅铁");
    /*		Alloy (Iron + Gold)			*/
    public static final ItemStack GILDED_IRON = new SlimefunItemStack("GILDED_IRON", Material.GOLD_INGOT, "&6&l镀金铁锭");
    /*		Alloy (Redston + Ferrosilicon)	*/
    public static final ItemStack REDSTONE_ALLOY = new SlimefunItemStack("REDSTONE_ALLOY", Material.BRICK, "&c红石合金锭");
    /*		Alloy (Iron + Copper)		*/
    public static final ItemStack NICKEL_INGOT = new SlimefunItemStack("NICKEL_INGOT", Material.IRON_INGOT, "&b镍锭");
    /*		Alloy (Nickel + Iron + Copper)		*/
    public static final ItemStack COBALT_INGOT = new SlimefunItemStack("COBALT_INGOT", Material.IRON_INGOT, "&9钴锭");

    /*		Gold		*/
    public static final ItemStack GOLD_4K = new SlimefunItemStack("GOLD_4K", Material.GOLD_INGOT, "&r金锭 &7(4克拉)");
    public static final ItemStack GOLD_6K = new SlimefunItemStack("GOLD_6K", Material.GOLD_INGOT, "&r金锭 &7(6克拉)");
    public static final ItemStack GOLD_8K = new SlimefunItemStack("GOLD_8K", Material.GOLD_INGOT, "&r金锭 &7(8克拉)");
    public static final ItemStack GOLD_10K = new SlimefunItemStack("GOLD_10K", Material.GOLD_INGOT, "&r金锭 &7(10克拉)");
    public static final ItemStack GOLD_12K = new SlimefunItemStack("GOLD_12K", Material.GOLD_INGOT, "&r金锭 &7(12克拉)");
    public static final ItemStack GOLD_14K = new SlimefunItemStack("GOLD_14K", Material.GOLD_INGOT, "&r金锭 &7(14克拉)");
    public static final ItemStack GOLD_16K = new SlimefunItemStack("GOLD_16K", Material.GOLD_INGOT, "&r金锭 &7(16克拉)");
    public static final ItemStack GOLD_18K = new SlimefunItemStack("GOLD_18K", Material.GOLD_INGOT, "&r金锭 &7(18克拉)");
    public static final ItemStack GOLD_20K = new SlimefunItemStack("GOLD_20K", Material.GOLD_INGOT, "&r金锭 &7(20克拉)");
    public static final ItemStack GOLD_22K = new SlimefunItemStack("GOLD_22K", Material.GOLD_INGOT, "&r金锭 &7(22克拉)");
    public static final ItemStack GOLD_24K = new SlimefunItemStack("GOLD_24K", Material.GOLD_INGOT, "&r金锭 &7(24克拉)");

    /*		 Dusts 		*/
    public static final ItemStack IRON_DUST = new SlimefunItemStack("IRON_DUST", Material.GUNPOWDER, "&6铁粉");
    public static final ItemStack GOLD_DUST = new SlimefunItemStack("GOLD_DUST", Material.GLOWSTONE_DUST, "&6金粉");
    public static final ItemStack TIN_DUST = new SlimefunItemStack("TIN_DUST", Material.SUGAR, "&6锡粉");
    public static final ItemStack COPPER_DUST = new SlimefunItemStack("COPPER_DUST", Material.GLOWSTONE_DUST, "&6铜粉");
    public static final ItemStack SILVER_DUST = new SlimefunItemStack("SILVER_DUST", Material.SUGAR, "&6银粉");
    public static final ItemStack ALUMINUM_DUST = new SlimefunItemStack("ALUMINUM_DUST", Material.SUGAR, "&6铝粉");
    public static final ItemStack LEAD_DUST = new SlimefunItemStack("LEAD_DUST", Material.GUNPOWDER, "&6铅粉");
    public static final ItemStack SULFATE = new SlimefunItemStack("SULFATE", Material.GLOWSTONE_DUST, "&6硫酸盐");
    public static final ItemStack ZINC_DUST = new SlimefunItemStack("ZINC_DUST", Material.SUGAR, "&6锌粉");
    public static final ItemStack MAGNESIUM_DUST = new SlimefunItemStack("MAGNESIUM_DUST", Material.SUGAR, "&6镁");
    public static final ItemStack SILICON = new SlimefunItemStack("SILICON", Material.FIREWORK_STAR, "&6硅");
    public static final ItemStack GOLD_24K_BLOCK = new SlimefunItemStack("GOLD_24K_BLOCK", Material.GOLD_BLOCK, "&r&r金块 &7(24克拉)");

    /*		 Gems 		*/
    public static final ItemStack SYNTHETIC_DIAMOND = new SlimefunItemStack("SYNTHETIC_DIAMOND", Material.DIAMOND, "&b人造钻石");
    public static final ItemStack SYNTHETIC_EMERALD = new SlimefunItemStack("SYNTHETIC_EMERALD", Material.EMERALD, "&b人造绿宝石");
    public static final ItemStack SYNTHETIC_SAPPHIRE = new SlimefunItemStack("SYNTHETIC_SAPPHIRE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM1MDMyZjRkN2QwMWRlOGVjOTlkODlmODcyMzAxMmQ0ZTc0ZmE3MzAyMmM0ZmFjZjFiNTdjN2ZmNmZmMCJ9fX0=", "&b人造蓝宝石");
    public static final ItemStack CARBONADO = new SlimefunItemStack("CARBONADO", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTJmNGIxNTc3ZjUxNjBjNjg5MzE3MjU3MWM0YTcxZDhiMzIxY2RjZWFhMDMyYzZlMGUzYjYwZTBiMzI4ZmEifX19", "&b&l黑金刚石", "", "&7&o\"黑色钻石\"");
    public static final ItemStack RAW_CARBONADO = new SlimefunItemStack("RAW_CARBONADO", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWI0OWU2ZWMxMDc3MWU4OTkyMjVhZWE3M2NkOGNmMDM2ODRmNDExZDE0MTVjNzMyM2M5M2NiOTQ3NjIzMCJ9fX0=", "&b黑金刚石原矿");

    public static final ItemStack URANIUM = new SlimefunItemStack("URANIUM", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiMjlhZmE2ZDZkYzkyM2UyZTEzMjRiZjgxOTI3NTBmN2JkYmRkYzY4OTYzMmEyYjZjMThkOWZlN2E1ZSJ9fX0=", "&4铀", "", "&2辐射等级: 高", "&4&o需要防化套装");
    public static final ItemStack NEPTUNIUM = new SlimefunItemStack("NEPTUNIUM", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVkZWE2YmZkMzdlNDlkZTQzZjE1NGZlNmZjYTYxN2Q0MTI5ZTYxYjk1NzU5YTNkNDlhMTU5MzVhMWMyZGNmMCJ9fX0=", "&a镎", "", "&2辐射等级: 高", "&4&o需要防化套装");
    public static final ItemStack PLUTONIUM = new SlimefunItemStack("PLUTONIUM", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjZjkxYjczODg2NjVhNmQ3YzFiNjAyNmJkYjIzMjJjNmQyNzg5OTdhNDQ0Nzg2NzdjYmNjMTVmNzYxMjRmIn19fQ==", "&7钚", "", "&2辐射等级: 高", "&4&o需要防化套装");
    public static final ItemStack BOOSTED_URANIUM = new SlimefunItemStack("BOOSTED_URANIUM", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgzN2NhMTJmMjIyZjQ3ODcxOTZhMTdiOGFiNjU2OTg1Zjg0MDRjNTA3NjdhZGJjYjZlN2YxNDI1NGZlZSJ9fX0=", "&2高纯度铀", "", "&2辐射等级: 高", "&4&o需要防化套装");

    /*		Talisman		*/
    public static final ItemStack TALISMAN = new SlimefunItemStack("COMMON_TALISMAN", Material.EMERALD, "&6普通护身符");
    public static final ItemStack ENDER_TALISMAN = new SlimefunItemStack("ENDER_TALISMAN", Material.EMERALD, "&5末影护身符");

    public static final ItemStack TALISMAN_ANVIL = new SlimefunItemStack("ANVIL_TALISMAN", Material.EMERALD, "&a铁砧护身符", "", "&r每个护身符可以防止", "&r一个工具因耐久不足而损坏", "&r然后就会被消耗", "", "&4&l警告:", "&4由于过于强大的工具的复杂性", "&4此护身符不能修复过于强大的工具");
    public static final ItemStack TALISMAN_MINER = new SlimefunItemStack("MINER_TALISMAN", Material.EMERALD, "&a矿工护身符", "", "&r当这个护身符在你的背包里时", "&r将有 20% 的几率双倍掉落", "&r你挖出的矿物");
    public static final ItemStack TALISMAN_HUNTER = new SlimefunItemStack("HUNTER_TALISMAN", Material.EMERALD, "&a猎人护身符", "", "&r当这个护身符在你的背包里时", "&r将有 20% 的几率双倍掉落", "&r你杀死的生物的掉落物");
    public static final ItemStack TALISMAN_LAVA = new SlimefunItemStack("LAVA_TALISMAN", Material.EMERALD,  "&a岩浆行者护身符", "", "&r当这个护身符在你的背包里时", "&r获得火焰保护效果", "&r仅在你行走在岩浆上时可用", "&r然后就会被消耗");
    public static final ItemStack TALISMAN_WATER = new SlimefunItemStack("WATER_TALISMAN", Material.EMERALD, "&a潜水者护身符", "", "&r当这个护身符在你的背包里时", "&r一旦你即将溺水而死", "&r护身符将会给予你水下呼吸", "&r然后就会被消耗");
    public static final ItemStack TALISMAN_ANGEL = new SlimefunItemStack("ANGEL_TALISMAN", Material.EMERALD, "&a天使护身符", "", "&r当这个护身符在你的背包里时", "&r有 75% 的几率减免你的摔落伤害");
    public static final ItemStack TALISMAN_FIRE = new SlimefunItemStack("FIRE_TALISMAN", Material.EMERALD, "&a消防员护身符", "", "&r当这个护身符在你的背包里时", "&r在你着火时", "&r给予你防火效果", "&r然后就会被消耗");
    public static final ItemStack TALISMAN_MAGICIAN = new SlimefunItemStack("MAGICIAN_TALISMAN", Material.EMERALD, "&a魔法师护身符", "", "&r当这个护身符在你的背包里时", "&r在附魔时有 80% 的几率", "&r得到一个额外的附魔");
    public static final ItemStack TALISMAN_TRAVELLER = new SlimefunItemStack("TRAVELLER_TALISMAN", Material.EMERALD, "&a旅行者护身符", "", "&r当这个护身符在你的背包里时", "&r在你开始疾跑时有 60% 的几率", "&r给予你速度效果");
    public static final ItemStack TALISMAN_WARRIOR = new SlimefunItemStack("WARRIOR_TALISMAN", Material.EMERALD, "&a战士护身符", "", "&r当这个护身符在你的背包里时", "&r你被攻击后将会获得力量3的效果", "&r然后就会被消耗");
    public static final ItemStack TALISMAN_KNIGHT = new SlimefunItemStack("KNIGHT_TALISMAN", Material.EMERALD, "&a骑士护身符", "", "&r当这个护身符在你的背包里时", "&r在你被攻击后", "&r有 30% 的几率获得五秒的生命恢复", "&r然后就会被消耗");
    public static final ItemStack TALISMAN_WHIRLWIND = new SlimefunItemStack("WHIRLWIND_TALISMAN", Material.EMERALD, "&a旋风护身符", "", "&r当这个护身符在你的背包里时", "&r将有 60% 的几率", "&r反弹所有冲向你的弹射物");
    public static final ItemStack TALISMAN_WIZARD = new SlimefunItemStack("WIZARD_TALISMAN", Material.EMERALD, "&a巫师护身符", "", "&r当这个护身符在你的背包里时", "&r在你附魔时可获得时运4/5", "&r但它也可能降低该物品", "&r其他附魔的等级");

    /*		Staves		*/
    public static final ItemStack STAFF_ELEMENTAL = new SlimefunItemStack("STAFF_ELEMENTAL", Material.STICK, "&6元素法杖");
    public static final ItemStack STAFF_WIND = new SlimefunItemStack("STAFF_ELEMENTAL_WIND", Material.STICK, "&6元素法杖 &7- &b&o风", "", "&7元素: &b&o风", "", "&7&e右键&7 以将你吹飞");
    public static final ItemStack STAFF_FIRE = new SlimefunItemStack("STAFF_ELEMENTAL_FIRE", Material.STICK, "&6元素法杖 &7- &c&o火", "", "&7元素: &c&o火", "", "&c让火焰净化一切!");
    public static final ItemStack STAFF_WATER = new SlimefunItemStack("STAFF_ELEMENTAL_WATER", Material.STICK, "&6元素法杖 &7- &1&o水", "", "&7元素: &1&o水", "", "&7&e右键&7 以灭掉你身上的火");
    public static final ItemStack STAFF_STORM = new SlimefunItemStack("STAFF_ELEMENTAL_STORM", Material.STICK, "&6元素法杖 &7- &8&o雷", "", "&7元素: &8&o雷", "", "&e右键&7 召唤一道闪电", "&7剩余 &e" + StormStaff.MAX_USES + " 次");

    static {
        STAFF_WIND.addUnsafeEnchantment(Enchantment.LUCK, 1);
        STAFF_FIRE.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 5);
        STAFF_WATER.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
        STAFF_STORM.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
    }

    /*		 Machines 		*/
    public static final ItemStack GRIND_STONE = new SlimefunItemStack("GRIND_STONE", Material.DISPENSER, "&b磨石", "", "&a&o更高效地研磨物品");
    public static final ItemStack ARMOR_FORGE = new SlimefunItemStack("ARMOR_FORGE", Material.ANVIL, "&6盔甲锻造台", "", "&a&o可以创造强大的防具");
    public static final ItemStack SMELTERY = new SlimefunItemStack("SMELTERY", Material.FURNACE, "&6冶炼炉", "", "&a能够冶炼金属的高温炉");
    public static final ItemStack IGNITION_CHAMBER = new SlimefunItemStack("IGNITION_CHAMBER", Material.HOPPER, "&4自动点火机", "&r以防你的冶炼炉失去火焰", "&r需要打火石", "&r必须放置于冶炼炉的发射器旁边");
    public static final ItemStack ORE_CRUSHER = new SlimefunItemStack("ORE_CRUSHER", Material.DISPENSER, "&b矿石粉碎机", "", "&a&o粉碎矿石并且得到双倍的矿粉");
    public static final ItemStack COMPRESSOR = new SlimefunItemStack("COMPRESSOR", Material.PISTON, "&b压缩机", "", "&a压缩物品");
    public static final ItemStack PRESSURE_CHAMBER = new SlimefunItemStack("PRESSURE_CHAMBER", Material.GLASS, "&b压力机", "", "&a压缩更多的物品");
    public static final ItemStack MAGIC_WORKBENCH = new SlimefunItemStack("MAGIC_WORKBENCH", Material.CRAFTING_TABLE, "&6魔法工作台", "", "&d给物品注入魔法能量");
    public static final ItemStack ORE_WASHER = new SlimefunItemStack("ORE_WASHER", Material.CAULDRON, "&6洗矿机", "", "&a&o清洗筛矿变成过滤矿石", "&a&o并且给你一些小石块");
    public static final ItemStack TABLE_SAW = new SlimefunItemStack("TABLE_SAW", Material.STONECUTTER, "&6台锯", "", "&a&o从1个木头里获得8个木板", "&a&o(支持所有的原木)");
    public static final ItemStack COMPOSTER = new SlimefunItemStack("COMPOSTER", Material.CAULDRON, "&a搅拌机", "", "&a&o随着时间的推移可以转换各种材料...");
    public static final ItemStack ENHANCED_CRAFTING_TABLE = new SlimefunItemStack("ENHANCED_CRAFTING_TABLE", Material.CRAFTING_TABLE, "&e高级工作台", "", "&a&o一个原始的工作台", "&a&o无法承受强大的力量...");
    public static final ItemStack CRUCIBLE = new SlimefunItemStack("CRUCIBLE", Material.CAULDRON, "&c坩埚", "", "&a&o用来把物品变为液体");
    public static final ItemStack JUICER = new SlimefunItemStack("JUICER", Material.GLASS_BOTTLE, "&a榨汁机", "", "&a让你创造美味的果汁");

    public static final ItemStack SOLAR_PANEL = new SlimefunItemStack("SOLAR_PANEL", Material.DAYLIGHT_DETECTOR, "&b太阳能板", "", "&a&o将阳光变为能量");
    public static final ItemStack SOLAR_ARRAY = new SlimefunItemStack("SOLAR_ARRAY", Material.DAYLIGHT_DETECTOR, "&b太阳能阵列", "", "&a&o将阳光变为能量");

    @Deprecated
    public static final ItemStack DIGITAL_MINER = new CustomItem(Material.IRON_PICKAXE, "&b挖矿机", "", "&4已过时", "&c这个物品即将被移除!");

    @Deprecated
    public static final ItemStack ADVANCED_DIGITAL_MINER = new CustomItem(Material.DIAMOND_PICKAXE, "&6高级挖矿机", "", "&4已过时", "&c这个物品即将被移除!");

    public static final ItemStack AUTOMATED_PANNING_MACHINE = new SlimefunItemStack("AUTOMATED_PANNING_MACHINE", Material.BOWL, "&a自动淘金机", "", "&a&o升级版淘金筛");
    public static final ItemStack OUTPUT_CHEST = new SlimefunItemStack("OUTPUT_CHEST", Material.CHEST, "&4物品输出箱", "", "&c&o这个机器会将箱子里的物品", "&c&o放入邻近的发射器");
    public static final ItemStack HOLOGRAM_PROJECTOR = new SlimefunItemStack("HOLOGRAM_PROJECTOR", Material.QUARTZ_SLAB, "&b全息投影仪", "", "&r投影出可编辑的全息文字");

    /*		 Enhanced Furnaces 		*/
    public static final ItemStack ENHANCED_FURNACE = new SlimefunItemStack("ENHANCED_FURNACE", Material.FURNACE, "&7强化熔炉 - &eI", "", "&7燃烧速度: &e1x", "&7燃料效率: &e1x", "&7产物翻倍倍数: &e1x");
    public static final ItemStack ENHANCED_FURNACE_2 = new SlimefunItemStack("ENHANCED_FURNACE_2", Material.FURNACE, "&7强化熔炉 - &eII", "", "&7燃烧速度: &e2x", "&7燃料效率: &e1x", "&7产物翻倍倍数: &e1x");
    public static final ItemStack ENHANCED_FURNACE_3 = new SlimefunItemStack("ENHANCED_FURNACE_3", Material.FURNACE, "&7强化熔炉 - &eIII", "", "&7燃烧速度: &e2x", "&7燃料效率: &e2x", "&7产物翻倍倍数: &e1x");
    public static final ItemStack ENHANCED_FURNACE_4 = new SlimefunItemStack("ENHANCED_FURNACE_4", Material.FURNACE, "&7强化熔炉 - &eIV", "", "&7燃烧速度: &e3x", "&7燃料效率: &e2x", "&7产物翻倍倍数: &e1x");
    public static final ItemStack ENHANCED_FURNACE_5 = new SlimefunItemStack("ENHANCED_FURNACE_5", Material.FURNACE, "&7强化熔炉 - &eV", "", "&7燃烧速度: &e3x", "&7燃料效率: &e2x", "&7产物翻倍倍数: &e2x");
    public static final ItemStack ENHANCED_FURNACE_6 = new SlimefunItemStack("ENHANCED_FURNACE_6", Material.FURNACE, "&7强化熔炉 - &eVI", "", "&7燃烧速度: &e3x", "&7燃料效率: &e3x", "&7产物翻倍倍数: &e2x");
    public static final ItemStack ENHANCED_FURNACE_7 = new SlimefunItemStack("ENHANCED_FURNACE_7", Material.FURNACE, "&7强化熔炉 - &eVII", "", "&7燃烧速度: &e4x", "&7燃料效率: &e3x", "&7产物翻倍倍数: &e2x");
    public static final ItemStack ENHANCED_FURNACE_8 = new SlimefunItemStack("ENHANCED_FURNACE_8", Material.FURNACE, "&7强化熔炉 - &eVIII", "", "&7燃烧速度: &e4x", "&7燃料效率: &e4x", "&7产物翻倍倍数: &e2x");
    public static final ItemStack ENHANCED_FURNACE_9 = new SlimefunItemStack("ENHANCED_FURNACE_9", Material.FURNACE, "&7强化熔炉 - &eIX", "", "&7燃烧速度: &e5x", "&7燃料效率: &e4x", "&7产物翻倍倍数: &e2x");
    public static final ItemStack ENHANCED_FURNACE_10 = new SlimefunItemStack("ENHANCED_FURNACE_10", Material.FURNACE, "&7强化熔炉 - &eX", "", "&7燃烧速度: &e5x", "&7燃料效率: &e5x", "&7产物翻倍倍数: &e2x");
    public static final ItemStack ENHANCED_FURNACE_11 = new SlimefunItemStack("ENHANCED_FURNACE_11", Material.FURNACE, "&7强化熔炉 - &eXI", "", "&7燃烧速度: &e5x", "&7燃料效率: &e5x", "&7产物翻倍倍数: &e3x");
    public static final ItemStack REINFORCED_FURNACE = new SlimefunItemStack("REINFORCED_FURNACE", Material.FURNACE, "&7强化合金熔炉", "", "&7燃烧速度: &e10x", "&7燃料效率: &e10x", "&7产物翻倍倍数: &e3x");
    public static final ItemStack CARBONADO_EDGED_FURNACE = new SlimefunItemStack("CARBONADO_EDGED_FURNACE", Material.FURNACE, "&7黑金刚石镶边熔炉", "", "&7燃烧速度: &e20x", "&7燃料效率: &e10x", "&7产物翻倍倍数: &e3x");

    public static final ItemStack BLOCK_PLACER = new SlimefunItemStack("BLOCK_PLACER", Material.DISPENSER, "&a方块放置机", "", "&r所有在这个发射器内的方块", "&r都会被自动放置");

    /*		Soulbound Items		*/
    public static final ItemStack SOULBOUND_SWORD = new SlimefunItemStack("SOULBOUND_SWORD", Material.DIAMOND_SWORD, "&c灵魂绑定剑");
    public static final ItemStack SOULBOUND_BOW = new SlimefunItemStack("SOULBOUND_BOW", Material.BOW, "&c灵魂绑定弓");
    public static final ItemStack SOULBOUND_PICKAXE = new SlimefunItemStack("SOULBOUND_PICKAXE", Material.DIAMOND_PICKAXE, "&c灵魂绑定镐");
    public static final ItemStack SOULBOUND_AXE = new SlimefunItemStack("SOULBOUND_AXE", Material.DIAMOND_AXE, "&c灵魂绑定斧");
    public static final ItemStack SOULBOUND_SHOVEL = new SlimefunItemStack("SOULBOUND_SHOVEL", Material.DIAMOND_SHOVEL, "&c灵魂绑定铲");
    public static final ItemStack SOULBOUND_HOE = new SlimefunItemStack("SOULBOUND_HOE", Material.DIAMOND_HOE, "&c灵魂绑定锄");
    public static final ItemStack SOULBOUND_TRIDENT = new SlimefunItemStack("SOULBOUND_TRIDENT", Material.TRIDENT, "&c三叉戟", "&7灵魂绑定");

    public static final ItemStack SOULBOUND_HELMET = new SlimefunItemStack("SOULBOUND_HELMET", Material.DIAMOND_HELMET, "&c灵魂绑定头盔");
    public static final ItemStack SOULBOUND_CHESTPLATE = new SlimefunItemStack("SOULBOUND_CHESTPLATE", Material.DIAMOND_CHESTPLATE, "&c灵魂绑定胸甲");
    public static final ItemStack SOULBOUND_LEGGINGS = new SlimefunItemStack("SOULBOUND_LEGGINGS", Material.DIAMOND_LEGGINGS, "&c灵魂绑定护腿");
    public static final ItemStack SOULBOUND_BOOTS = new SlimefunItemStack("SOULBOUND_BOOTS", Material.DIAMOND_BOOTS, "&c灵魂绑定靴子");

    /*		Runes				*/
    public static final ItemStack BLANK_RUNE;
    public static final ItemStack RUNE_AIR;
    public static final ItemStack RUNE_WATER;
    public static final ItemStack RUNE_FIRE;
    public static final ItemStack RUNE_EARTH;
    public static final ItemStack RUNE_ENDER;
    public static final ItemStack RUNE_RAINBOW;
    public static final ItemStack RUNE_LIGHTNING;
    public static final ItemStack RUNE_SOULBOUND;

    static {
        ItemStack itemB = new ItemStack(Material.FIREWORK_STAR);
        FireworkEffectMeta imB = (FireworkEffectMeta) itemB.getItemMeta();
        imB.setEffect(FireworkEffect.builder().with(Type.BURST).with(Type.BURST).withColor(Color.BLACK).build());
        imB.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8空白符文"));
        itemB.setItemMeta(imB);
        BLANK_RUNE = new SlimefunItemStack("BLANK_RUNE", itemB);

        ItemStack itemA = new ItemStack(Material.FIREWORK_STAR);
        FireworkEffectMeta imA = (FireworkEffectMeta) itemA.getItemMeta();
        imA.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.AQUA).build());
        imA.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7古代符文 &8&l[&b&l气&8&l]"));
        itemA.setItemMeta(imA);
        RUNE_AIR = new SlimefunItemStack("ANCIENT_RUNE_AIR", itemA);

        ItemStack itemW = new ItemStack(Material.FIREWORK_STAR);
        FireworkEffectMeta imW = (FireworkEffectMeta) itemW.getItemMeta();
        imW.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.BLUE).build());
        imW.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7古代符文 &8&l[&1&l水&8&l]"));
        itemW.setItemMeta(imW);
        RUNE_WATER = new SlimefunItemStack("ANCIENT_RUNE_WATER", itemW);

        ItemStack itemF = new ItemStack(Material.FIREWORK_STAR);
        FireworkEffectMeta imF = (FireworkEffectMeta) itemF.getItemMeta();
        imF.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.RED).build());
        imF.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7古代符文 &8&l[&4&l火&8&l]"));
        itemF.setItemMeta(imF);
        RUNE_FIRE = new SlimefunItemStack("ANCIENT_RUNE_FIRE", itemF);

        ItemStack itemE = new ItemStack(Material.FIREWORK_STAR);
        FireworkEffectMeta imE = (FireworkEffectMeta) itemE.getItemMeta();
        imE.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.fromRGB(112, 47, 7)).build());
        imE.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7古代符文 &8&l[&c&l地&8&l]"));
        itemE.setItemMeta(imE);
        RUNE_EARTH = new SlimefunItemStack("ANCIENT_RUNE_EARTH", itemE);

        ItemStack itemN = new ItemStack(Material.FIREWORK_STAR);
        FireworkEffectMeta imN = (FireworkEffectMeta) itemN.getItemMeta();
        imN.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.PURPLE).build());
        imN.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7古代符文 &8&l[&5&l末影&8&l]"));
        itemN.setItemMeta(imN);
        RUNE_ENDER = new SlimefunItemStack("ANCIENT_RUNE_ENDER", itemN);

        ItemStack itemR = new ItemStack(Material.FIREWORK_STAR);
        FireworkEffectMeta imR = (FireworkEffectMeta) itemR.getItemMeta();
        imR.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.FUCHSIA).build());
        imR.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7古代符文 &8&l[&d&l虹&8&l]"));
        itemR.setItemMeta(imR);
        RUNE_RAINBOW = new SlimefunItemStack("ANCIENT_RUNE_RAINBOW", itemR);

        ItemStack itemL = new ItemStack(Material.FIREWORK_STAR);
        FireworkEffectMeta imL = (FireworkEffectMeta) itemL.getItemMeta();
        imL.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.fromRGB(255, 255, 95)).build());
        imL.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7古代符文 &8&l[&e&l雷&8&l]"));
        itemL.setItemMeta(imL);
        RUNE_LIGHTNING = new SlimefunItemStack("ANCIENT_RUNE_LIGHTNING", itemL);

        ItemStack itemS = new ItemStack(Material.FIREWORK_STAR);
        FireworkEffectMeta imS = (FireworkEffectMeta) itemS.getItemMeta();
        imS.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.fromRGB(47, 0, 117)).build());
        imS.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7古代符文 &8&l[&5&l灵魂绑定&8&l]"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "先把你要绑定的物品丢到地上");
        lore.add(ChatColor.YELLOW + "然后把这个符文丢向那个物品");
        lore.add(ChatColor.DARK_PURPLE + "就能灵魂绑定 " + ChatColor.YELLOW + "那个物品.");
        lore.add(" ");
        lore.add(ChatColor.YELLOW + "建议你在" + ChatColor.GOLD + "重要 " + ChatColor.YELLOW + "物品上使用.");
        lore.add(" ");
        lore.add(ChatColor.YELLOW + "绑定后的物品死亡后不会掉落.");
        imS.setLore(lore);
        itemS.setItemMeta(imS);
        RUNE_SOULBOUND = new SlimefunItemStack("ANCIENT_RUNE_SOULBOUND", itemS);
    }

    /*		Electricity			*/
    public static final ItemStack SOLAR_GENERATOR = new SlimefunItemStack("SOLAR_GENERATOR", Material.DAYLIGHT_DETECTOR, "&b太阳能发电机", "", MachineTier.BASIC.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &70 J 可储存", "&8\u21E8 &e\u26A1 &74 J/s");
    public static final ItemStack SOLAR_GENERATOR_2 = new SlimefunItemStack("SOLAR_GENERATOR_2", Material.DAYLIGHT_DETECTOR, "&c高级太阳能发电机", "", MachineTier.MEDIUM.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &70 J 可储存", "&8\u21E8 &e\u26A1 &716 J/s");
    public static final ItemStack SOLAR_GENERATOR_3 = new SlimefunItemStack("SOLAR_GENERATOR_3", Material.DAYLIGHT_DETECTOR, "&4黑金刚石太阳能发电机", "", MachineTier.END_GAME.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &70 J 可储存", "&8\u21E8 &e\u26A1 &764 J/s");
    public static final ItemStack SOLAR_GENERATOR_4 = new SlimefunItemStack("SOLAR_GENERATOR_4", Material.DAYLIGHT_DETECTOR, "&e充能太阳能发电机", "", "&9可以在夜间工作", "", MachineTier.END_GAME.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &70 J 可储存", "&8\u21E8 &e\u26A1 &7256 J/s (日间)", "&8\u21E8 &e\u26A1 &7128 J/s (夜间)");

    public static final ItemStack COAL_GENERATOR = new SlimefunItemStack("COAL_GENERATOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ==", "&c煤发电机", "", MachineTier.AVERAGE.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &764 J 可储存", "&8\u21E8 &e\u26A1 &716 J/s");
    public static final ItemStack COAL_GENERATOR_2 = new SlimefunItemStack("COAL_GENERATOR_2", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ==", "&c煤发电机", "", MachineTier.ADVANCED.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &7256 J 可储存", "&8\u21E8 &e\u26A1 &730 J/s");

    public static final ItemStack LAVA_GENERATOR = new SlimefunItemStack("LAVA_GENERATOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ==", "&4岩浆发电机", "", MachineTier.AVERAGE.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &7512 J 可储存", "&8\u21E8 &e\u26A1 &720 J/s");
    public static final ItemStack LAVA_GENERATOR_2 = new SlimefunItemStack("LAVA_GENERATOR_2", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ==", "&4岩浆发电机 &7(&eII&7)", "", MachineTier.ADVANCED.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &71024 J 可储存", "&8\u21E8 &e\u26A1 &740 J/s");

    public static final ItemStack ELECTRIC_FURNACE = new SlimefunItemStack("ELECTRIC_FURNACE", Material.FURNACE, "&c电炉", "", MachineTier.BASIC.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &74 J/s");
    public static final ItemStack ELECTRIC_FURNACE_2 = new SlimefunItemStack("ELECTRIC_FURNACE_2", Material.FURNACE, "&c电炉 &7- &eII", "", MachineTier.MEDIUM.and(MachineType.MACHINE), "&8\u21E8 &7速度: 2x", "&8\u21E8 &e\u26A1 &76 J/s");
    public static final ItemStack ELECTRIC_FURNACE_3 = new SlimefunItemStack("ELECTRIC_FURNACE_3", Material.FURNACE, "&c电炉 &7- &eIII", "", MachineTier.MEDIUM.and(MachineType.MACHINE), "&8\u21E8 &7速度: 4x", "&8\u21E8 &e\u26A1 &710 J/s");

    public static final ItemStack ELECTRIC_ORE_GRINDER = new SlimefunItemStack("ELECTRIC_ORE_GRINDER", Material.FURNACE, "&c电力碎矿机", "","&r矿物粉碎机与磨石的完美结合", "", MachineTier.ADVANCED.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &712 J/s");
    public static final ItemStack ELECTRIC_ORE_GRINDER_2 = new SlimefunItemStack("ELECTRIC_ORE_GRINDER_2", Material.FURNACE, "&c电力碎矿机 &7(&eII&7)", "","&r矿物粉碎机与磨石的完美结合", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 4x", "&8\u21E8 &e\u26A1 &730 J/s");
    public static final ItemStack ELECTRIC_INGOT_PULVERIZER = new SlimefunItemStack("ELECTRIC_INGOT_PULVERIZER", Material.FURNACE, "&c电力打粉机", "", "&r将锭变为粉", "", MachineTier.MEDIUM.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &714 J/s");
    public static final ItemStack AUTO_DRIER = new SlimefunItemStack("AUTO_DRIER", Material.SMOKER, "&e自动烘干机", "", MachineTier.MEDIUM.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &710 J/s");
    public static final ItemStack AUTO_ENCHANTER = new SlimefunItemStack("AUTO_ENCHANTER", Material.ENCHANTING_TABLE, "&5自动附魔机", "", MachineTier.MEDIUM.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &718 J/s");
    public static final ItemStack AUTO_DISENCHANTER = new SlimefunItemStack("AUTO_DISENCHANTER", Material.ENCHANTING_TABLE, "&5自动祛魔机", "", MachineTier.MEDIUM.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &718 J/s");
    public static final ItemStack AUTO_ANVIL = new SlimefunItemStack("AUTO_ANVIL", Material.IRON_BLOCK, "&7自动铁砧", "", MachineTier.ADVANCED.and(MachineType.MACHINE), "&8\u21E8 &7可修复百分比: 10%", "&8\u21E8 &e\u26A1 &724 J/s");
    public static final ItemStack AUTO_ANVIL_2 = new SlimefunItemStack("AUTO_ANVIL_2", Material.IRON_BLOCK, "&7自动铁砧 Mk.II", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7可修复百分比: 25%", "&8\u21E8 &e\u26A1 &732 J/s");

    public static final ItemStack BIO_REACTOR = new SlimefunItemStack("BIO_REACTOR", Material.LIME_TERRACOTTA, "&2生物反应器", "", MachineTier.AVERAGE.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &7128 J 可储存", "&8\u21E8 &e\u26A1 &78 J/s");
    public static final ItemStack MULTIMETER = new SlimefunItemStack("MULTIMETER", Material.CLOCK, "&e万用表", "", "&r查看机器中储存的能量");

    public static final ItemStack SMALL_CAPACITOR = new SlimefunItemStack("SMALL_CAPACITOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ==", "&a小型储能电容", "", MachineTier.BASIC.and(MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &7128 J 可储存");
    public static final ItemStack MEDIUM_CAPACITOR = new SlimefunItemStack("MEDIUM_CAPACITOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ==", "&a中型储能电容", "", MachineTier.AVERAGE.and(MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &7512 J 可储存");
    public static final ItemStack BIG_CAPACITOR = new SlimefunItemStack("BIG_CAPACITOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ==", "&a大型储能电容", "", MachineTier.MEDIUM.and(MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &71024 J 可储存");
    public static final ItemStack LARGE_CAPACITOR = new SlimefunItemStack("LARGE_CAPACITOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ==", "&a巨型储能电容", "", MachineTier.GOOD.and(MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &78192 J 可储存");
    public static final ItemStack CARBONADO_EDGED_CAPACITOR = new SlimefunItemStack("CARBONADO_EDGED_CAPACITOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ==", "&a黑金刚石镶边储能电容", "", MachineTier.END_GAME.and(MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &765536 J 可储存");

    /*		Robots				*/
    public static final ItemStack PROGRAMMABLE_ANDROID = new SlimefunItemStack("PROGRAMMABLE_ANDROID", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUwM2NiN2VkODQ1ZTdhNTA3ZjU2OWFmYzY0N2M0N2FjNDgzNzcxNDY1YzlhNjc5YTU0NTk0Yzc2YWZiYSJ9fX0=", "&c可编程式机器人 &7(普通)", "", "&8\u21E8 &7功能: 无", "&8\u21E8 &7燃料效率: 1.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_FARMER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_FARMER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlkMzMzNTdlODQxODgyM2JmNzgzZGU5MmRlODAyOTFiNGViZDM5MmFlYzg3MDY2OThlMDY4OTZkNDk4ZjYifX19", "&c可编程式机器人 &7(农夫)", "", "&8\u21E8 &7功能: 耕作", "&8\u21E8 &7燃料效率: 1.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_MINER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_MINER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTYzOGEyODU0MWFiM2FlMGE3MjNkNTU3ODczOGUwODc1ODM4OGVjNGMzMzI0N2JkNGNhMTM0ODJhZWYzMzQifX19", "&c可编程式机器人 &7(矿工)", "", "&8\u21E8 &7功能: 挖矿", "&8\u21E8 &7燃料效率: 1.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_WOODCUTTER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_WOODCUTTER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDMyYTgxNDUxMDE0MjIwNTE2OWExYWQzMmYwYTc0NWYxOGU5Y2I2YzY2ZWU2NGVjYTJlNjViYWJkZWY5ZmYifX19", "&c可编程式机器人 &7(樵夫)", "", "&8\u21E8 &7功能: 伐木", "&8\u21E8 &7燃料效率: 1.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_BUTCHER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NzJkZjBhZDlhM2JlODhmMmU1ZDVkNDIyZDAyYjExNmQ2NGQ4ZGYxNDc1ZWQzMmU1NDZhZmM4NGIzMSJ9fX0=", "&c可编程式机器人 &7(屠夫)", "", "&8\u21E8 &7功能: 屠宰", "&8\u21E8 &7伤害: 4", "&8\u21E8 &7燃料效率: 1.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_FISHERMAN", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ1ZTg3MzNhNzMxMTQzMzNiOThiMzYwMTc1MTI0MTcyMmY0NzEzZTFhMWE1ZDM2ZmJiMTMyNDkzZjFjNyJ9fX0=", "&c可编程式机器人 &7(渔夫)", "", "&8\u21E8 &7功能: 钓鱼", "&8\u21E8 &7成功几率: 10%", "&8\u21E8 &7燃料效率: 1.0x");

    public static final ItemStack PROGRAMMABLE_ANDROID_2 = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUwM2NiN2VkODQ1ZTdhNTA3ZjU2OWFmYzY0N2M0N2FjNDgzNzcxNDY1YzlhNjc5YTU0NTk0Yzc2YWZiYSJ9fX0=", "&c高级可编程式机器人 &7(普通)", "", "&8\u21E8 &7功能: 无", "&8\u21E8 &7燃料效率: 1.5x");
    public static final ItemStack PROGRAMMABLE_ANDROID_2_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_FISHERMAN", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ1ZTg3MzNhNzMxMTQzMzNiOThiMzYwMTc1MTI0MTcyMmY0NzEzZTFhMWE1ZDM2ZmJiMTMyNDkzZjFjNyJ9fX0=", "&c高级可编程式机器人 &7(渔夫)", "", "&8\u21E8 &7功能: 钓鱼", "&8\u21E8 &7成功几率: 20%", "&8\u21E8 &7燃料效率: 1.5x");
    public static final ItemStack PROGRAMMABLE_ANDROID_2_FARMER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_FARMER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlkMzMzNTdlODQxODgyM2JmNzgzZGU5MmRlODAyOTFiNGViZDM5MmFlYzg3MDY2OThlMDY4OTZkNDk4ZjYifX19", "&c高级可编程式机器人 &7(农夫)", "", "&8\u21E8 &7功能: 耕作", "&8\u21E8 &7燃料效率: 1.5x", "&8\u21E8 &7可以收割异域花园内的植物");
    public static final ItemStack PROGRAMMABLE_ANDROID_2_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_BUTCHER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NzJkZjBhZDlhM2JlODhmMmU1ZDVkNDIyZDAyYjExNmQ2NGQ4ZGYxNDc1ZWQzMmU1NDZhZmM4NGIzMSJ9fX0=", "&c高级可编程式机器人 &7(屠夫)", "", "&8\u21E8 &7功能: 屠宰", "&8\u21E8 &7伤害: 8", "&8\u21E8 &7燃料效率: 1.5x");

    public static final ItemStack PROGRAMMABLE_ANDROID_3 = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUwM2NiN2VkODQ1ZTdhNTA3ZjU2OWFmYzY0N2M0N2FjNDgzNzcxNDY1YzlhNjc5YTU0NTk0Yzc2YWZiYSJ9fX0=", "&e可授权式可编程式机器人 &7(普通)", "", "&8\u21E8 &7功能: 无", "&8\u21E8 &7燃料效率: 3.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_3_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3_FISHERMAN", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ1ZTg3MzNhNzMxMTQzMzNiOThiMzYwMTc1MTI0MTcyMmY0NzEzZTFhMWE1ZDM2ZmJiMTMyNDkzZjFjNyJ9fX0=", "&e可授权式可编程式机器人 &7(渔夫)", "", "&8\u21E8 &7功能: 钓鱼", "&8\u21E8 &7成功几率: 30%", "&8\u21E8 &7燃料效率: 8.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_3_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3_BUTCHER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NzJkZjBhZDlhM2JlODhmMmU1ZDVkNDIyZDAyYjExNmQ2NGQ4ZGYxNDc1ZWQzMmU1NDZhZmM4NGIzMSJ9fX0=", "&e可授权式可编程式机器人 &7(屠夫)", "", "&8\u21E8 &7功能: 屠宰", "&8\u21E8 &7伤害: 20", "&8\u21E8 &7燃料效率: 8.0x");

    /*		       GPS		       */
    public static final ItemStack GPS_TRANSMITTER = new SlimefunItemStack("GPS_TRANSMITTER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0=", "&bGPS 发射器", "", "&8\u21E8 &e\u26A1 &716 J 可储存", "&8\u21E8 &e\u26A1 &72 J/s");
    public static final ItemStack GPS_TRANSMITTER_2 = new SlimefunItemStack("GPS_TRANSMITTER_2", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0=", "&c高级 GPS 发射器", "", "&8\u21E8 &e\u26A1 &764 J 可储存", "&8\u21E8 &e\u26A1 &76 J/s");
    public static final ItemStack GPS_TRANSMITTER_3 = new SlimefunItemStack("GPS_TRANSMITTER_3", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0=", "&4黑金刚石 GPS 发射器", "", "&8\u21E8 &e\u26A1 &7256 J 可储存", "&8\u21E8 &e\u26A1 &722 J/s");
    public static final ItemStack GPS_TRANSMITTER_4 = new SlimefunItemStack("GPS_TRANSMITTER_4", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0=", "&e充能 GPS 发射器", "", "&8\u21E8 &e\u26A1 &71024 J 可储存", "&8\u21E8 &e\u26A1 &792 J/s");

    public static final ItemStack GPS_MARKER_TOOL = new SlimefunItemStack("GPS_MARKER_TOOL", Material.REDSTONE_TORCH, "&bGPS 设置路径点工具", "", "&r允许你在放置标记工具的位置", "&r设置一个传送点并命名");
    public static final ItemStack GPS_CONTROL_PANEL = new SlimefunItemStack("GPS_CONTROL_PANEL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGRjZmJhNThmYWYxZjY0ODQ3ODg0MTExODIyYjY0YWZhMjFkN2ZjNjJkNDQ4MWYxNGYzZjNiY2I2MzMwIn19fQ==", "&bGPS 控制面板", "", "&r允许你追踪你的 GPS 卫星", "&r并且管理已有的路径点");
    public static final ItemStack GPS_EMERGENCY_TRANSMITTER = new SlimefunItemStack("GPS_EMERGENCY_TRANSMITTER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0=", "&cGPS 应急发射器", "", "&r当你死亡的时候", "&r如果应急发射器在你的背包内", "&r将会自动把你的死亡位置设为路径点");

    public static final ItemStack ANDROID_INTERFACE_FUEL = new SlimefunItemStack("ANDROID_INTERFACE_FUEL", Material.DISPENSER, "&7机器人交互接口 &c(燃料)", "", "&r当脚本告诉它这样做时", "&r储存在交互接口的物品", "&r将会被放入机器人的燃料槽");
    public static final ItemStack ANDROID_INTERFACE_ITEMS = new SlimefunItemStack("ANDROID_INTERFACE_ITEMS", Material.DISPENSER, "&7机器人交互接口 &9(物品)", "", "&r当脚本告诉它该这样做时", "&r储存在机器人物品栏的物品", "&r将会被放入交互界面");

    public static final ItemStack GPS_GEO_SCANNER = new SlimefunItemStack("GPS_GEO_SCANNER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkOGNmZWIzODdhNTZlM2U1YmNmODUzNDVkNmE0MTdiMjQyMjkzODg3ZGIzY2UzYmE5MWZhNDA5YjI1NGI4NiJ9fX0=", "&bGPS 地形扫描器", "", "&r扫描一个区块中有多少自然资源", "&r例如 &8原油");
    public static final ItemStack PORTABLE_GEO_SCANNER = new SlimefunItemStack("PORTABLE_GEO_SCANNER", Material.CLOCK, "&b便携式资源扫描器", "", "&r扫描出区块中的自然资源", "", "&e右键&7 扫描");
    public static final ItemStack GEO_MINER = new SlimefunItemStack("GEO_MINER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM3NzQxZjc2NGRkM2RkN2FkYWViNDNiNjNkMzk1OWViNzBlNWViMjhmMTVkNmIzNGNhYjM0YTFkMWY2MDM4NyJ9fX0=", "&6GEO 矿机", "", "&e从区块中开采出资源", "&e可以开采出不能被矿镐挖出的资源", "", MachineTier.ADVANCED.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &748 J/s", "", "&c&l! &cMake sure to Geo-Scan the Chunk first");
    public static final ItemStack OIL_PUMP = new SlimefunItemStack("OIL_PUMP", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZlMWEwNDBhNDI1ZTMxYTQ2ZDRmOWE5Yjk4MDZmYTJmMGM0N2VlODQ3MTFjYzE5MzJmZDhhYjMyYjJkMDM4In19fQ==", "&r原油泵", "", "&7泵出原油并把它装进桶里", "", "&c&l! &c请先对所在区块进行地形扫描");
    public static final ItemStack BUCKET_OF_OIL = new SlimefunItemStack("BUCKET_OF_OIL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlMDRiNDFkMTllYzc5MjdmOTgyYTYzYTk0YTNkNzlmNzhlY2VjMzMzNjMwNTFmZGUwODMxYmZhYmRiZCJ9fX0=", "&r原油桶");
    public static final ItemStack BUCKET_OF_FUEL = new SlimefunItemStack("BUCKET_OF_FUEL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg0ZGRjYTc2NjcyNWI4Yjk3NDEzZjI1OWMzZjc2NjgwNzBmNmFlNTU0ODNhOTBjOGU1NTI1Mzk0ZjljMDk5In19fQ==", "&r燃料桶");

    public static final ItemStack REFINERY = new SlimefunItemStack("REFINERY", Material.PISTON, "&cRefinery", "", "&rRefines Oil to create Fuel");
    public static final ItemStack COMBUSTION_REACTOR = new SlimefunItemStack("COMBUSTION_REACTOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ==", "&c燃烧反应机", "", MachineTier.ADVANCED.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &7256 J 可储存", "&8\u21E8 &e\u26A1 &724 J/s");
    public static final ItemStack ANDROID_MEMORY_CORE = new SlimefunItemStack("ANDROID_MEMORY_CORE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc4ZjJiN2U1ZTc1NjM5ZWE3ZmI3OTZjMzVkMzY0YzRkZjI4YjQyNDNlNjZiNzYyNzdhYWRjZDYyNjEzMzcifX19", "&b机器人内存核心");

    public static final ItemStack GPS_TELEPORTER_PYLON = new SlimefunItemStack("GPS_TELEPORTER_PYLON", Material.PURPLE_STAINED_GLASS, "&5GPS 传送塔", "", "&7传送器组件");
    public static final ItemStack GPS_TELEPORTATION_MATRIX = new SlimefunItemStack("GPS_TELEPORTATION_MATRIX", Material.IRON_BLOCK, "&bGPS 传送矩阵", "", "&r这是 GPS 传送的主要零件", "&r这个矩阵允许玩家传送至", "&r已设置的路径点");
    public static final ItemStack GPS_ACTIVATION_DEVICE_SHARED = new SlimefunItemStack("GPS_ACTIVATION_DEVICE_SHARED", Material.STONE_PRESSURE_PLATE, "&rGPS 激活设备 &3(公共)", "", "&r把它放在传送矩阵上", "&r并且踩下这个踏板以选择", "&r要传送的路径点");
    public static final ItemStack GPS_ACTIVATION_DEVICE_PERSONAL = new SlimefunItemStack("GPS_ACTIVATION_DEVICE_PERSONAL", Material.STONE_PRESSURE_PLATE, "&rGPS 激活设备 &a(私人)", "", "&r把它放在传送矩阵上", "&r并且踩下这个踏板以选择", "&r要传送的路径点", "", "&r这种激活装置仅允许", "&r放置它的人使用");

    public static final ItemStack ELEVATOR = new SlimefunItemStack("ELEVATOR_PLATE", Material.STONE_PRESSURE_PLATE, "&b电梯板", "", "&r在每一层放置电梯板", "&r你就能够在每一层之间传送.", "", "&e右键电梯板&7 以为此层命名");

    public static final ItemStack INFUSED_HOPPER = new SlimefunItemStack("INFUSED_HOPPER", Material.HOPPER, "&5吸入漏斗", "", "&r自动吸入在漏斗附近", "&r7x7x7 范围内的所有物品");

    public static final ItemStack PLASTIC_SHEET = new SlimefunItemStack("PLASTIC_SHEET", Material.PAPER, "&r塑料纸");

    public static final ItemStack HEATED_PRESSURE_CHAMBER = new SlimefunItemStack("HEATED_PRESSURE_CHAMBER", Material.LIGHT_GRAY_STAINED_GLASS, "&c加热压力舱", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &710 J/s");
    public static final ItemStack HEATED_PRESSURE_CHAMBER_2 = new SlimefunItemStack("HEATED_PRESSURE_CHAMBER_2", Material.LIGHT_GRAY_STAINED_GLASS, "&c加热压力舱 &7- &eII", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 5x", "&8\u21E8 &e\u26A1 &744 J/s");

    public static final ItemStack ELECTRIC_SMELTERY = new SlimefunItemStack("ELECTRIC_SMELTERY", Material.FURNACE, "&c电力冶炼机", "", "&4仅支持合金, 不能将粉冶炼成锭", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &720 J/s");
    public static final ItemStack ELECTRIC_SMELTERY_2 = new SlimefunItemStack("ELECTRIC_SMELTERY_2", Material.FURNACE, "&c电力冶炼机 &7- &eII", "", "&4仅支持合金, 不能将粉冶炼成锭", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 3x", "&8\u21E8 &e\u26A1 &740 J/s");

    public static final ItemStack ELECTRIC_PRESS = new SlimefunItemStack("ELECTRIC_PRESS", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ1Y2Y5MmJjNzllYzE5ZjQxMDY0NDFhZmZmZjE0MDZhMTM2NzAxMGRjYWZiMTk3ZGQ5NGNmY2ExYTZkZTBmYyJ9fX0=", "&e电力碳压机", "", MachineTier.MEDIUM.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &716 J/s");
    public static final ItemStack ELECTRIC_PRESS_2 = new SlimefunItemStack("ELECTRIC_PRESS_2", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ1Y2Y5MmJjNzllYzE5ZjQxMDY0NDFhZmZmZjE0MDZhMTM2NzAxMGRjYWZiMTk3ZGQ5NGNmY2ExYTZkZTBmYyJ9fX0=", "&e电力碳压机 &7- &eII", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 3x", "&8\u21E8 &e\u26A1 &740 J/s");

    public static final ItemStack ELECTRIFIED_CRUCIBLE = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE", Material.RED_TERRACOTTA, "&c电动坩埚", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &748 J/s");
    public static final ItemStack ELECTRIFIED_CRUCIBLE_2 = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE_2", Material.RED_TERRACOTTA, "&c电动坩埚 &7- &eII", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 2x", "&8\u21E8 &e\u26A1 &780 J/s");
    public static final ItemStack ELECTRIFIED_CRUCIBLE_3 = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE_3", Material.RED_TERRACOTTA, "&c电动坩埚 &7- &eIII", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 4x", "&8\u21E8 &e\u26A1 &7120 J/s");

    public static final ItemStack CARBON_PRESS = new SlimefunItemStack("CARBON_PRESS", Material.BLACK_STAINED_GLASS, "&c碳压机", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &720 J/s");
    public static final ItemStack CARBON_PRESS_2 = new SlimefunItemStack("CARBON_PRESS_2", Material.BLACK_STAINED_GLASS, "&c碳压机 &7- &eII", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 3x", "&8\u21E8 &e\u26A1 &750 J/s");
    public static final ItemStack CARBON_PRESS_3 = new SlimefunItemStack("CARBON_PRESS_3", Material.BLACK_STAINED_GLASS, "&c碳压机 &7- &eIII", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 15x", "&8\u21E8 &e\u26A1 &7180 J/s");

    public static final ItemStack BLISTERING_INGOT = new SlimefunItemStack("BLISTERING_INGOT", Material.GOLD_INGOT, "&6起泡锭 &7(33%)", "", "&2辐射等级: 高", "&4&o需要防化套装");
    public static final ItemStack BLISTERING_INGOT_2 = new SlimefunItemStack("BLISTERING_INGOT_2", Material.GOLD_INGOT, "&6起泡锭 &7(66%)", "", "&2辐射等级: 高", "&4&o需要防化套装");
    public static final ItemStack BLISTERING_INGOT_3 = new SlimefunItemStack("BLISTERING_INGOT_3", Material.GOLD_INGOT, "&6起泡锭", "", "&2辐射等级: 高", "&4&o需要防化套装");

    public static final ItemStack ENERGY_REGULATOR = new SlimefunItemStack("ENERGY_REGULATOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc4ZjJiN2U1ZTc1NjM5ZWE3ZmI3OTZjMzVkMzY0YzRkZjI4YjQyNDNlNjZiNzYyNzdhYWRjZDYyNjEzMzcifX19", "&6能源调节器", "", "&r能源网络的核心部分");
    public static final ItemStack DEBUG_FISH = new CustomItem(Material.SALMON, "&3这鱼多少钱?", "", "&e右键 &r任意方块以查看它的方块数据", "&e左键 &r破坏方块", "&eShift + 左键 &r任意方块以清除它的方块数据", "&eShift + 右键 &r放置一个占位符方块");

    public static final ItemStack NETHER_ICE = new SlimefunItemStack("NETHER_ICE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2NlMmRhZDliYWY3ZWFiYTdlODBkNGQwZjlmYWMwYWFiMDFhNzZiMTJmYjcxYzNkMmFmMmExNmZkZDRjNzM4MyJ9fX0=", "&e下界冰", "", "&e辐射等级: 中等", "&4&o需要防化套装");
    public static final ItemStack ENRICHED_NETHER_ICE = new SlimefunItemStack("ENRICHED_NETHER_ICE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M4MThhYTEzYWFiYzcyOTQ4MzhkMjFjYWFjMDU3ZTk3YmQ4Yzg5NjQxYTBjMGY4YTU1NDQyZmY0ZTI3In19fQ==", "&e浓缩下界冰", "", "&2辐射等级: 极高", "&4&o需要防化套装");
    public static final ItemStack NETHER_ICE_COOLANT_CELL = new SlimefunItemStack("NETHER_ICE_COOLANT_CELL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQzY2Q0MTI1NTVmODk3MDE2MjEzZTVkNmM3NDMxYjQ0OGI5ZTU2NDRlMWIxOWVjNTFiNTMxNmYzNTg0MGUwIn19fQ==", "&6下界冰冷却剂");

    @Deprecated
    public static final ItemStack NETHER_DRILL = new CustomItem(Material.RED_TERRACOTTA, "&4下界钻头", "", "&r允许你开采下界冰", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &7102 J/s", "", "&c&l! &c只能在地狱使用!", "&c&l! &c请先对所在区块进行地形扫描");

    // Cargo
    public static final ItemStack CARGO_MANAGER = new SlimefunItemStack("CARGO_MANAGER", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTUxMGJjODUzNjJhMTMwYTZmZjlkOTFmZjExZDZmYTQ2ZDdkMTkxMmEzNDMxZjc1MTU1OGVmM2M0ZDljMiJ9fX0=", "&6货运管理器", "", "&r物品传输网络的核心组件");
    public static final ItemStack CARGO_NODE = new SlimefunItemStack("CARGO_NODE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDdiN2VmNmZkNzg2NDg2NWMzMWMxZGM4N2JlZDI0YWI1OTczNTc5ZjVjNjYzOGZlY2I4ZGVkZWI0NDNmZjAifX19", "&7货运节点 &c(连接器)", "", "&r货运连接管道");
    public static final ItemStack CARGO_INPUT = new SlimefunItemStack("CARGO_NODE_INPUT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZkMWMxYTY5YTNkZTlmZWM5NjJhNzdiZjNiMmUzNzZkZDI1Yzg3M2EzZDhmMTRmMWRkMzQ1ZGFlNGM0In19fQ==", "&7货运节点 &c(输入)", "", "&r货运输入管道");
    public static final ItemStack CARGO_OUTPUT = new SlimefunItemStack("CARGO_NODE_OUTPUT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTViMjFmZDQ4MGMxYzQzYmYzYjlmODQyYzg2OWJkYzNiYzVhY2MyNTk5YmYyZWI2YjhhMWM5NWRjZTk3OGYifX19", "&7货运节点 &c(输出)", "", "&r货运输出管道");
    public static final ItemStack CARGO_OUTPUT_ADVANCED = new SlimefunItemStack("CARGO_NODE_OUTPUT_ADVANCED", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTViMjFmZDQ4MGMxYzQzYmYzYjlmODQyYzg2OWJkYzNiYzVhY2MyNTk5YmYyZWI2YjhhMWM5NWRjZTk3OGYifX19", "&6高级货运节点 &c(输出)", "", "&r货运输出管道");

    public static final ItemStack AUTO_BREEDER = new SlimefunItemStack("AUTO_BREEDER", Material.HAY_BLOCK, "&e自动喂食机", "", "&r需要 &a有机食物", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &71024 J 可储存", "&8\u21E8 &e\u26A1 &760 J/个动物");

    public static final ItemStack ORGANIC_FOOD = new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a有机食物", "&7内含 &9X");
    public static final ItemStack WHEAT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_WHEAT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机食物", "&7内含 &9小麦");
    public static final ItemStack CARROT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_CARROT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机食物", "&7内含 &9胡萝卜");
    public static final ItemStack POTATO_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_POTATO", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机食物", "&7内含 &9土豆");
    public static final ItemStack SEEDS_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SEEDS", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机食物", "&7内含 &9小麦种子");
    public static final ItemStack BEETROOT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_BEETROOT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机食物", "&7内含 &9甜菜");
    public static final ItemStack MELON_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_MELON", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机食物", "&7内含 &9西瓜");
    public static final ItemStack APPLE_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_APPLE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机食物", "&7内含 &9苹果");
    public static final ItemStack SWEET_BERRIES_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SWEET_BERRIES", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机食物", "&7内含 &9浆果");

    public static final ItemStack FERTILIZER = new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a有机肥料", "&7内含 &9X");
    public static final ItemStack WHEAT_FERTILIZER = new SlimefunItemStack("FERTILIZER_WHEAT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机肥料", "&7内含 &9小麦");
    public static final ItemStack CARROT_FERTILIZER = new SlimefunItemStack("FERTILIZER_CARROT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机肥料", "&7内含 &9胡萝卜");
    public static final ItemStack POTATO_FERTILIZER = new SlimefunItemStack("FERTILIZER_POTATO", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机肥料", "&7内含 &9土豆");
    public static final ItemStack SEEDS_FERTILIZER = new SlimefunItemStack("FERTILIZER_SEEDS", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机肥料", "&7内含 &9小麦种子");
    public static final ItemStack BEETROOT_FERTILIZER = new SlimefunItemStack("FERTILIZER_BEETROOT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机肥料", "&7内含 &9甜菜");
    public static final ItemStack MELON_FERTILIZER = new SlimefunItemStack("FERTILIZER_MELON", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机肥料", "&7内含 &9西瓜");
    public static final ItemStack APPLE_FERTILIZER = new SlimefunItemStack("FERTILIZER_APPLE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机肥料", "&7内含 &9苹果");
    public static final ItemStack SWEET_BERRIES_FERTILIZER = new SlimefunItemStack("FERTILIZER_SWEET_BERRIES", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ==", "&a有机肥料", "&7内含 &9浆果");

    public static final ItemStack ANIMAL_GROWTH_ACCELERATOR = new SlimefunItemStack("ANIMAL_GROWTH_ACCELERATOR", Material.HAY_BLOCK, "&b动物生长加速器", "", "&r需要 &a有机食物", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &71024 J 可储存", "&8\u21E8 &e\u26A1 &728 J/s");
    public static final ItemStack CROP_GROWTH_ACCELERATOR = new SlimefunItemStack("CROP_GROWTH_ACCELERATOR", Material.LIME_TERRACOTTA, "&a作物生长加速器", "", "&r需要 &a有机肥料", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7Radius: 7x7", "&8\u21E8 &7速度: &a3/time", "&8\u21E8 &e\u26A1 &71024 J 可储存", "&8\u21E8 &e\u26A1 &750 J/s");
    public static final ItemStack CROP_GROWTH_ACCELERATOR_2 = new SlimefunItemStack("CROP_GROWTH_ACCELERATOR_2", Material.LIME_TERRACOTTA, "&a作物生长加速器 &7(&eII&7)", "", "&r需要 &a有机肥料", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7Radius: 9x9", "&8\u21E8 &7速度: &a4/time", "&8\u21E8 &e\u26A1 &71024 J 可储存", "&8\u21E8 &e\u26A1 &760 J/s");

    public static final ItemStack FOOD_FABRICATOR = new SlimefunItemStack("FOOD_FABRICATOR", Material.GREEN_STAINED_GLASS, "&c食品加工机", "", "&r可制造 &a有机食物", "", MachineTier.ADVANCED.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &7256 J 可储存", "&8\u21E8 &e\u26A1 &714 J/s");
    public static final ItemStack FOOD_FABRICATOR_2 = new SlimefunItemStack("FOOD_FABRICATOR_2", Material.GREEN_STAINED_GLASS, "&c食品加工机 &7(&eII&7)", "", "&r可制造 &a有机食物", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 6x", "&8\u21E8 &e\u26A1 &7512 J 可储存", "&8\u21E8 &e\u26A1 &748 J/s");

    public static final ItemStack FOOD_COMPOSTER = new SlimefunItemStack("FOOD_COMPOSTER", Material.GREEN_TERRACOTTA, "&c食品堆肥器", "", "&r可制造 &a有机肥料", "", MachineTier.ADVANCED.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &7256 J 可储存", "&8\u21E8 &e\u26A1 &716 J/s");
    public static final ItemStack FOOD_COMPOSTER_2 = new SlimefunItemStack("FOOD_COMPOSTER_2", Material.GREEN_TERRACOTTA, "&c食品堆肥器 &7(&eII&7)", "", "&r可制造 &a有机肥料", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 10x", "&8\u21E8 &e\u26A1 &7512 J 可储存", "&8\u21E8 &e\u26A1 &752 J/s");

    public static final ItemStack XP_COLLECTOR = new SlimefunItemStack("XP_COLLECTOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTc2MmExNWIwNDY5MmEyZTRiM2ZiMzY2M2JkNGI3ODQzNGRjZTE3MzJiOGViMWM3YTlmN2MwZmJmNmYifX19", "&a经验收集器", "", "&r收集附近的经验并储存它们", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &71024 J 可储存", "&8\u21E8 &e\u26A1 &720 J/s");
    public static final ItemStack REACTOR_COOLANT_CELL = new SlimefunItemStack("REACTOR_COLLANT_CELL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGU0MDczYmU0MGNiM2RlYjMxMGEwYmU5NTliNGNhYzY4ZTgyNTM3MjcyOGZhZmI2YzI5NzNlNGU3YzMzIn19fQ==", "&b反应堆冷却剂");

    public static final ItemStack NUCLEAR_REACTOR = new SlimefunItemStack("NUCLEAR_REACTOR","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ==", "&2核反应堆", "", "&r需要冷却剂!", "&8\u21E8 &b必须被水包围", "&8\u21E8 &b必须使用反应堆冷却剂工作", "", MachineTier.END_GAME.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &716384 J 可储存", "&8\u21E8 &e\u26A1 &7500 J/s");
    public static final ItemStack NETHERSTAR_REACTOR = new SlimefunItemStack("NETHERSTAR_REACTOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ==", "&f下界之星反应堆", "", "&f需要下界之星", "&8\u21E8 &b必须被水包围", "&8\u21E8 &b必须使用下界之星冷却剂工作", "", MachineTier.END_GAME.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &732768 J 可储存", "&8\u21E8 &e\u26A1 &71024 J/s", "&8\u21E8 &4会导致附近的生物获得凋零效果");
    public static final ItemStack REACTOR_ACCESS_PORT = new SlimefunItemStack("REACTOR_ACCESS_PORT", Material.CYAN_TERRACOTTA,  "&2反应堆访问接口", "",  "&r允许你通过货运节点来访问反应堆", "&r也可以用于储存", "", "&8\u21E8 &c必须 &e放置在反应堆上方的第三格处");

    public static final ItemStack FREEZER = new SlimefunItemStack("FREEZER", Material.LIGHT_BLUE_STAINED_GLASS, "&b冰箱", "", MachineTier.ADVANCED.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &7256 J 可储存", "&8\u21E8 &e\u26A1 &718 J/s");
    public static final ItemStack FREEZER_2 = new SlimefunItemStack("FREEZER_2", Material.LIGHT_BLUE_STAINED_GLASS, "&b冰箱 &7(&eII&7)", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 2x", "&8\u21E8 &e\u26A1 &7256 J 可储存", "&8\u21E8 &e\u26A1 &730 J/s");

    public static final ItemStack ELECTRIC_GOLD_PAN = new SlimefunItemStack("ELECTRIC_GOLD_PAN", Material.BROWN_TERRACOTTA, "&6电动淘金机", "", MachineTier.BASIC.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &72 J/s");
    public static final ItemStack ELECTRIC_GOLD_PAN_2 = new SlimefunItemStack("ELECTRIC_GOLD_PAN_2", Material.BROWN_TERRACOTTA, "&6电动淘金机 &7(&eII&7)", "", MachineTier.BASIC.and(MachineType.MACHINE), "&8\u21E8 &7速度: 3x", "&8\u21E8 &e\u26A1 &74 J/s");
    public static final ItemStack ELECTRIC_GOLD_PAN_3 = new SlimefunItemStack("ELECTRIC_GOLD_PAN_3", Material.BROWN_TERRACOTTA, "&6电动淘金机 &7(&eIII&7)", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 10x", "&8\u21E8 &e\u26A1 &714 J/s");

    public static final ItemStack ELECTRIC_DUST_WASHER = new SlimefunItemStack("ELECTRIC_DUST_WASHER", Material.BLUE_STAINED_GLASS, "&3电动洗矿机", "", MachineTier.BASIC.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &76 J/s");
    public static final ItemStack ELECTRIC_DUST_WASHER_2 = new SlimefunItemStack("ELECTRIC_DUST_WASHER_2", Material.BLUE_STAINED_GLASS, "&3电动洗矿机 &7(&eII&7)", "", MachineTier.BASIC.and(MachineType.MACHINE), "&8\u21E8 &7速度: 2x", "&8\u21E8 &e\u26A1 &710 J/s");
    public static final ItemStack ELECTRIC_DUST_WASHER_3 = new SlimefunItemStack("ELECTRIC_DUST_WASHER_3", Material.BLUE_STAINED_GLASS, "&3电动洗矿机 &7(&eIII&7)", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 10x", "&8\u21E8 &e\u26A1 &730 J/s");

    public static final ItemStack ELECTRIC_INGOT_FACTORY = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY", Material.RED_TERRACOTTA, "&c电动铸锭机", "", MachineTier.BASIC.and(MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &78 J/s");
    public static final ItemStack ELECTRIC_INGOT_FACTORY_2 = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY_2", Material.RED_TERRACOTTA, "&c电动铸锭机 &7(&eII&7)", "", MachineTier.BASIC.and(MachineType.MACHINE), "&8\u21E8 &7速度: 2x", "&8\u21E8 &e\u26A1 &714 J/s");
    public static final ItemStack ELECTRIC_INGOT_FACTORY_3 = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY_3", Material.RED_TERRACOTTA, "&c电动铸锭机 &7(&eIII&7)", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7速度: 8x", "&8\u21E8 &e\u26A1 &740 J/s");

    public static final ItemStack AUTOMATED_CRAFTING_CHAMBER = new SlimefunItemStack("AUTOMATED_CRAFTING_CHAMBER", Material.CRAFTING_TABLE, "&6自动合成机", "", MachineTier.ADVANCED.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &710 J/个物品");
    public static final ItemStack FLUID_PUMP = new SlimefunItemStack("FLUID_PUMP", Material.BLUE_TERRACOTTA, "&9流体泵", "", MachineTier.ADVANCED.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &732 J/个方块");
    public static final ItemStack CHARGING_BENCH = new SlimefunItemStack("CHARGING_BENCH", Material.CRAFTING_TABLE, "&6充电台", "", "&r能够给物品充电, 比如喷气背包", "", MachineTier.BASIC.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &7128 J 可储存", "&8\u21E8 &e\u26A1 &7能源损失率: &c50%");

    public static final ItemStack WITHER_ASSEMBLER = new SlimefunItemStack("WITHER_ASSEMBLER", Material.OBSIDIAN, "&5凋零汇编器", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7冷却时间: &b30 秒", "&8\u21E8 &e\u26A1 &74096 J 可储存", "&8\u21E8 &e\u26A1 &74096 J/个凋灵");

    public static final ItemStack TRASH_CAN = new SlimefunItemStack("TRASH_CAN_BLOCK","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJkNDEwNDJjZTk5MTQ3Y2MzOGNhYzllNDY3NDE1NzZlN2VlNzkxMjgzZTZmYWM4ZDMyOTJjYWUyOTM1ZjFmIn19fQ==", "&3垃圾箱", "", "&r将不需要的物品放入可以消除");

    public static final ItemStack ELYTRA = new ItemStack(Material.ELYTRA);
    public static final ItemStack ELYTRA_SCALE = new SlimefunItemStack("ELYTRA_SCALE", Material.FEATHER, "&b鞘翅鳞片");
    public static final ItemStack INFUSED_ELYTRA = new SlimefunItemStack("INFUSED_ELYTRA", ELYTRA, "&5鞘翅 (经验修补)");
    public static final ItemStack SOULBOUND_ELYTRA = new SlimefunItemStack("SOULBOUND_ELYTRA", ELYTRA, "&c鞘翅 (灵魂绑定)");

    public static final ItemStack TOTEM_OF_UNDYING = new ItemStack(Material.TOTEM_OF_UNDYING);

    static {
        INFUSED_ELYTRA.addUnsafeEnchantment(Enchantment.MENDING, 1);
    }

	public static final ItemStack MAGNESIUM_SALT = new SlimefunItemStack("MAGNESIUM_SALT", Material.SUGAR, "&c镁盐", "", "&7可以作为镁盐发电机的燃料");
	public static final ItemStack MAGNESIUM_GENERATOR = new SlimefunItemStack("MAGNESIUM_GENERATOR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ==", "&c镁盐发电机", "", MachineTier.MEDIUM.and(MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &7可储存 128 J", "&8\u21E8 &e\u26A1 &736 J/s");
	
	// ChestTerminal Addon
    public static final ItemStack CHEST_TERMINAL = new SlimefunItemStack("CHEST_TERMINAL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E0NGZmM2E1ZjQ5YzY5Y2FiNjc2YmFkOGQ5OGEwNjNmYTc4Y2ZhNjE5MTZmZGVmM2UyNjc1NTdmZWMxODI4MyJ9fX0=", "&3CT接入终端", "&7当此设备成功连接至货运网络时", "它将允许你远程与", "&7货运网络中的任何物品交互", "&7节点将切换到 ChestTerminal 频道");
	public static final ItemStack CT_IMPORT_BUS = new SlimefunItemStack("CT_IMPORT_BUS", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTEzZGIyZTdlNzJlYTQ0MzJlZWZiZDZlNThhODVlYWEyNDIzZjgzZTY0MmNhNDFhYmM2YTkzMTc3NTdiODg5In19fQ==", "&3CT输入总线", "&7当此设备成功连接至货运网络时", "它将从其所附属于的容器中", "&7取出物品并放入CT网络频道)");
	public static final ItemStack CT_EXPORT_BUS = new SlimefunItemStack("CT_EXPORT_BUS", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTEzZGIyZTdlNzJlYTQ0MzJlZWZiZDZlNThhODVlYWEyNDIzZjgzZTY0MmNhNDFhYmM2YTkzMTc3NTdiODg5In19fQ==", "&3CT输出总线", "&7当此设备成功连接至货运网络时", "它将从CT网络频道取出物品", "&7并将它们放入邻近总线的容器中");

}
