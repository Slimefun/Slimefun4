package me.mrCookieSlime.Slimefun.Lists;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomArmor;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomPotion;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.Christmas;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SlimefunItems
{
  public static ItemStack PORTABLE_CRAFTER = new CustomItem(Material.BOOK, "&6������������", 0, new String[] { "&a&o������������������", "", "&e����&7 ������" });
  public static ItemStack PORTABLE_DUSTBIN = null;
  public static ItemStack ENDER_BACKPACK = null;
  public static ItemStack MAGIC_EYE_OF_ENDER = new CustomItem(Material.EYE_OF_ENDER, "&6&l������������", 0, new String[] { "&4&l����������������", "", "&7&e����&7 ������������������" });
  public static ItemStack BROKEN_SPAWNER = new CustomItem(new MaterialData(Material.MOB_SPAWNER), "&c��������������", new String[] { "&7����: &b<Type>", "", "&c������, ��������������������" });
  public static ItemStack REPAIRED_SPAWNER = new CustomItem(Material.MOB_SPAWNER, "&b��������������", 0, new String[] { "&7����: &b<Type>" });
  public static ItemStack INFERNAL_BONEMEAL = new CustomItem(new MaterialData(Material.INK_SACK, (byte)15), "&4��������", new String[] { "", "&c������������", "&c��������" });
  public static ItemStack GOLD_PAN = new CustomItem(Material.BOWL, "&6����", 0, new String[] { "&a&o������������������������...", "", "&7&e����&7 ������������������" });
  public static ItemStack PARACHUTE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&r&l������", 0, new String[] { "", "&7���� &eShift&7 ������" }), Color.WHITE);
  public static ItemStack GRAPPLING_HOOK = new CustomItem(Material.LEASH, "&6����", 0, new String[] { "", "&7&e����&7 ������" });
  public static ItemStack SOLAR_HELMET = new CustomItem(Material.IRON_HELMET, "&b����������", 0, new String[] { "", "&a&oCharges held Items and Armor" });
  public static ItemStack CLOTH = new CustomItem(Material.PAPER, "&b��", 0);
  public static ItemStack CAN = null;
  public static ItemStack NIGHT_VISION_GOGGLES = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&a��������", 0, new String[] { "", "&9+ ����" }), Color.BLACK);
  public static ItemStack FARMER_SHOES = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&e������", 0, new String[] { "", "&6&o����������������" }), Color.YELLOW);
  public static ItemStack INFUSED_MAGNET = null;
  public static ItemStack FLASK_OF_KNOWLEDGE = new CustomItem(Material.GLASS_BOTTLE, "&c��������", 0, new String[] { "", "&r��������������", "&r��������������������", "&7����: &a1 ������" });
  public static ItemStack RAG = new CustomItem(Material.PAPER, "&c����", 0, new String[] { "", "&aI �� - ��������", "", "&r�������� 2 ����������", "&r����������������", "", "&7&e����&7 ������" });
  public static ItemStack BANDAGE = new CustomItem(Material.PAPER, "&c����", 0, new String[] { "", "&aII �� - ��������", "", "&r�������� 4 ����������", "&r����������������", "", "&7&e����&7 ������" });
  public static ItemStack SPLINT = new CustomItem(Material.STICK, "&c����", 0, new String[] { "", "&aI �� - ��������", "", "&r���� 2 ����������", "", "&7&e����&7 ������" });
  public static ItemStack VITAMINS = new CustomItem(Material.NETHER_STALK, "&c������", 0, new String[] { "", "&aIII �� - ��������", "", "&r�������� 4 ����������", "&r��������������", "&r������������/����/���� ����", "", "&7&e����&7 ������" });
  public static ItemStack MEDICINE = new CustomItem(Material.POTION, "&c����", 8229, new String[] { "", "&aIII �� - ��������", "", "&r�������� 4 ����������", "&r��������������", "&r������������/����/���� ����", "", "&7&e����&7 ����" });
  public static ItemStack BACKPACK_SMALL = null;
  public static ItemStack BACKPACK_MEDIUM = null;
  public static ItemStack BACKPACK_LARGE = null;
  public static ItemStack WOVEN_BACKPACK = null;
  public static ItemStack GILDED_BACKPACK = null;
  public static ItemStack BOUND_BACKPACK = null;
  public static ItemStack COOLER = null;
  public static ItemStack VOIDBAG_SMALL = null;
  public static ItemStack VOIDBAG_MEDIUM = null;
  public static ItemStack VOIDBAG_BIG = null;
  public static ItemStack VOIDBAG_LARGE = null;
  public static ItemStack BOUND_VOIDBAG = null;
  public static ItemStack DURALUMIN_JETPACK = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_CHESTPLATE), "&9������������ &7- &eI", new String[] { "", "&8? &7����: &b����", "&c&o&8? &e? &70 / 20 J", "&8? &7����: &c0.35", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack SOLDER_JETPACK = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_CHESTPLATE), "&9������������ &7- &eII", new String[] { "", "&8? &7����: &b����", "&c&o&8? &e? &70 / 30 J", "&8? &7����: &c0.4", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack BILLON_JETPACK = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_CHESTPLATE), "&9������������ &7- &eIII", new String[] { "", "&8? &7����: &b��������", "&c&o&8? &e? &70 / 45 J", "&8? &7����: &c0.45", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack STEEL_JETPACK = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_CHESTPLATE), "&9������������ &7- &eIV", new String[] { "", "&8? &7����: &b����", "&c&o&8? &e? &70 / 60 J", "&8? &7����: &c0.5", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack DAMASCUS_STEEL_JETPACK = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_CHESTPLATE), "&9������������ &7- &eV", new String[] { "", "&8? &7����: &b����������", "&c&o&8? &e? &70 / 75 J", "&8? &7����: &c0.55", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack REINFORCED_ALLOY_JETPACK = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_CHESTPLATE), "&9������������ &7- &eVI", new String[] { "", "&8? &7����: &b��������", "&c&o&8? &e? &70 / 100 J", "&8? &7����: &c0.6", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack CARBONADO_JETPACK = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_CHESTPLATE), "&9������������ &7- &eVII", new String[] { "", "&8? &7����: &b��������", "&c&o&8? &e? &70 / 150 J", "&8? &7����: &c0.7", "", "&7���� &eShift&7 ������" }), Color.BLACK);
  public static ItemStack ARMORED_JETPACK = new CustomItem(new MaterialData(Material.IRON_CHESTPLATE), "&9������������", new String[] { "&8? &7����: &b����", "", "&c&o&8? &e? &70 / 50 J", "&8? &7����: &c0.45", "", "&7���� &eShift&7 ������" });
  public static ItemStack DURALUMIN_JETBOOTS = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_BOOTS), "&9������ &7- &eI", new String[] { "", "&8? &7����: &b����", "&c&o&8? &e? &70 / 20 J", "&8? &7����: &a0.35", "&8? &7������: &c50%", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack SOLDER_JETBOOTS = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_BOOTS), "&9������ &7- &eII", new String[] { "", "&8? &7����: &b����", "&c&o&8? &e? &70 / 30 J", "&8? &7����: &a0.4", "&8? &7������: &660%", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack BILLON_JETBOOTS = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_BOOTS), "&9������ &7- &eIII", new String[] { "", "&8? &7����: &b��������", "&c&o&8? &e? &70 / 40 J", "&8? &7����: &a0.45", "&8? &7������: &665%", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack STEEL_JETBOOTS = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_BOOTS), "&9������ &7- &eIV", new String[] { "", "&8? &7����: &b����", "&c&o&8? &e? &70 / 50 J", "&8? &7����: &a0.5", "&8? &7������: &e70%", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack DAMASCUS_STEEL_JETBOOTS = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_BOOTS), "&9������ &7- &eV", new String[] { "", "&8? &7����: &b������������", "&c&o&8? &e? &70 / 75 J", "&8? &7����: &a0.55", "&8? &7������: &a75%", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack REINFORCED_ALLOY_JETBOOTS = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_BOOTS), "&9������ &7- &eVI", new String[] { "", "&8? &7����: &b��������", "&c&o&8? &e? &70 / 100 J", "&8? &7����: &a0.6", "&8? &7������: &c80%", "", "&7���� &eShift&7 ������" }), Color.SILVER);
  public static ItemStack CARBONADO_JETBOOTS = new CustomArmor(new CustomItem(new MaterialData(Material.LEATHER_BOOTS), "&9������ &7- &eVII", new String[] { "", "&8? &7����: &b��������", "&c&o&8? &e? &70 / 125 J", "&8? &7����: &a0.7", "&8? &7������: &c99.9%", "", "&7���� &eShift&7 ������" }), Color.BLACK);
  public static ItemStack ARMORED_JETBOOTS = new CustomItem(new MaterialData(Material.IRON_BOOTS), "&9����������", new String[] { "", "&8? &7����: &b����", "&c&o&8? &e? &70 / 50 J", "&8? &7����: &a0.45", "&8? &7������: &e70%", "", "&7���� &eShift&7 ������" });
  public static ItemStack DURALUMIN_MULTI_TOOL = new CustomItem(new MaterialData(Material.SHEARS), "&9���������� &7- &eI", new String[] { "", "&8? &7����: &b����", "&c&o&8? &e? &70 / 20 J", "", "&7&e����&7 ������", "&7���� &eShift + ����&7 ����������" });
  public static ItemStack SOLDER_MULTI_TOOL = new CustomItem(new MaterialData(Material.SHEARS), "&9���������� &7- &eII", new String[] { "", "&8? &7����: &b����", "&c&o&8? &e? &70 / 30 J", "", "&7&e����&7 ������", "&7���� &eShift + ����&7 ����������" });
  public static ItemStack BILLON_MULTI_TOOL = new CustomItem(new MaterialData(Material.SHEARS), "&9���������� &7- &eIII", new String[] { "", "&8? &7����: &b��������", "&c&o&8? &e? &70 / 40 J", "", "&7&e����&7 ������", "&7���� &eShift + ����&7 ����������" });
  public static ItemStack STEEL_MULTI_TOOL = new CustomItem(new MaterialData(Material.SHEARS), "&9���������� &7- &eIV", new String[] { "", "&8? &7����: &b����", "&c&o&8? &e? &70 / 50 J", "", "&7&e����&7 ������", "&7���� &eShift + ����&7 ����������" });
  public static ItemStack DAMASCUS_STEEL_MULTI_TOOL = new CustomItem(new MaterialData(Material.SHEARS), "&9���������� &7- &eV", new String[] { "", "&8? &7����: &b������������", "&c&o&8? &e? &70 / 60 J", "", "&7&e����&7 ������", "&7���� &eShift + ����&7 ����������" });
  public static ItemStack REINFORCED_ALLOY_MULTI_TOOL = new CustomItem(new MaterialData(Material.SHEARS), "&9���������� &7- &eVI", new String[] { "", "&8? &7����: &b��������", "&c&o&8? &e? &70 / 75 J", "", "&7&e����&7 ������", "&7���� &eShift + ����&7 ����������" });
  public static ItemStack CARBONADO_MULTI_TOOL = new CustomItem(new MaterialData(Material.SHEARS), "&9���������� &7- &eVII", new String[] { "", "&8? &7����: &b��������", "&c&o&8? &e? &70 / 100 J", "", "&7&e����&7 ������", "&7���� &eShift + ����&7 ����������" });
  public static ItemStack FORTUNE_COOKIE = new CustomItem(Material.COOKIE, "&6��������", 0, new String[] { "", "&a&o������������������ :o" });
  public static ItemStack BEEF_JERKY = new CustomItem(Material.COOKED_BEEF, "&6������", 0, new String[] { "", "&a&o����" });
  public static ItemStack MAGIC_SUGAR = new CustomItem(Material.SUGAR, "&6������", 0, new String[] { "", "&a&o������������������!" });
  public static ItemStack MONSTER_JERKY = new CustomItem(Material.ROTTEN_FLESH, "&6��������", 0, new String[] { "", "&a&o����������������������" });
  public static ItemStack APPLE_JUICE = new CustomPotion("&c������", 8197, new String[0], new PotionEffect(PotionEffectType.SATURATION, 10, 0));
  public static ItemStack MELON_JUICE = new CustomPotion("&c������", 8197, new String[0], new PotionEffect(PotionEffectType.SATURATION, 10, 0));
  public static ItemStack CARROT_JUICE = new CustomPotion("&6��������", 8195, new String[0], new PotionEffect(PotionEffectType.SATURATION, 10, 0));
  public static ItemStack PUMPKIN_JUICE = new CustomPotion("&6������", 8195, new String[0], new PotionEffect(PotionEffectType.SATURATION, 10, 0));
  public static ItemStack GOLDE_APPLE_JUICE = new CustomPotion("&b��������", 8195, new String[0], new PotionEffect(PotionEffectType.ABSORPTION, 400, 0));
  public static ItemStack MILK = new CustomPotion("&6����", 8194, new String[0], new PotionEffect(PotionEffectType.SATURATION, 5, 0));
  public static ItemStack CHOCOLATE_MILK = new CustomPotion("&6����������", 8201, new String[0], new PotionEffect(PotionEffectType.SATURATION, 12, 0));
  public static ItemStack EGG_NOG = new CustomPotion("&a����", 8194, new String[0], new PotionEffect(PotionEffectType.SATURATION, 7, 0));
  public static ItemStack APPLE_CIDER = new CustomPotion("&c������", 8197, new String[0], new PotionEffect(PotionEffectType.SATURATION, 14, 0));
  public static ItemStack CHRISTMAS_COOKIE = new CustomItem(Material.COOKIE, Christmas.color("��������"), 0);
  public static ItemStack FRUIT_CAKE = new CustomItem(Material.PUMPKIN_PIE, Christmas.color("��������"), 0);
  public static ItemStack APPLE_PIE = new CustomItem(Material.PUMPKIN_PIE, "&r������", 0);
  public static ItemStack HOT_CHOCOLATE = new CustomPotion("&6������", 8201, new String[0], new PotionEffect(PotionEffectType.SATURATION, 14, 0));
  public static ItemStack CHRISTMAS_CAKE = new CustomItem(Material.PUMPKIN_PIE, Christmas.color("����������"), 0);
  public static ItemStack CARAMEL = new CustomItem(Material.CLAY_BRICK, "&6����", 0);
  public static ItemStack CARAMEL_APPLE = new CustomItem(Material.APPLE, "&6��������", 0);
  public static ItemStack CHOCOLATE_APPLE = new CustomItem(Material.APPLE, "&6����������", 0);
  public static ItemStack PRESENT = new CustomItem(Material.CHEST, Christmas.color("��������"), 0, new String[] { "&emrCookieSlime &7����", "&7���� &e��", "", "&e����&7 ��������" });
  public static ItemStack EASTER_EGG = new CustomItem(Material.EGG, "&r����������", 0, new String[] { "&b����! ����!" });
  public static ItemStack CARROT_PIE = new CustomItem(Material.PUMPKIN_PIE, "&6��������", 0);
  public static ItemStack GRANDMAS_WALKING_STICK = new CustomItem(Material.STICK, "&7����������", 0, new String[0], new String[] { "KNOCKBACK-2" });
  public static ItemStack GRANDPAS_WALKING_STICK = new CustomItem(Material.STICK, "&7����������", 0, new String[0], new String[] { "KNOCKBACK-5" });
  public static ItemStack SWORD_OF_BEHEADING = new CustomItem(Material.IRON_SWORD, "&6��������", 0, new String[] { "&7�������� II", "", "&r����������������������", "&r(������������������������)" });
  public static ItemStack BLADE_OF_VAMPIRES = new CustomItem(Material.GOLD_SWORD, "&c����������", 0, new String[] { "&7�������� I", "", "&r����������", "&r�� 45% ������", "&r����2����������" }, new String[] { "FIRE_ASPECT-2", "DURABILITY-4", "DAMAGE_ALL-2" });
  public static ItemStack SEISMIC_AXE = new CustomItem(Material.IRON_AXE, "&a������", 0, new String[] { "", "&7&o��������������...", "", "&7&e����&7 ������" });
  public static ItemStack EXPLOSIVE_BOW = new CustomItem(Material.BOW, "&c��������", 0, new String[] { "&r����������������������������", "&r����������������������" });
  public static ItemStack ICY_BOW = new CustomItem(Material.BOW, "&b��������", 0, new String[] { "&r����������������������������", "&r��������������, ��������", "&r(���� 2 ��)" });
  public static ItemStack AUTO_SMELT_PICKAXE = new CustomItem(Material.DIAMOND_PICKAXE, "&6������������", 0, new String[] { "&c&l��������", "", "&9������������" });
  public static ItemStack LUMBER_AXE = new CustomItem(Material.DIAMOND_AXE, "&6������", 0, new String[] { "&a&o����������������..." });
  public static ItemStack PICKAXE_OF_CONTAINMENT = new CustomItem(Material.IRON_PICKAXE, "&c����������", 0, new String[] { "", "&9��������������" });
  public static ItemStack HERCULES_PICKAXE = new CustomItem(Material.IRON_PICKAXE, "&9��������������", 0, new String[] { "", "&r����������", "&r����������������������", "&r��������..." }, new String[] { "DURABILITY-2", "DIG_SPEED-4" });
  public static ItemStack EXPLOSIVE_PICKAXE = new CustomItem(Material.DIAMOND_PICKAXE, "&e������", 0, new String[] { "", "&r������������������", "&r����������...", "", "&9��������������" });
  public static ItemStack PICKAXE_OF_THE_SEEKER = new CustomItem(Material.DIAMOND_PICKAXE, "&a��������", 0, new String[] { "&r��������������������������", "&r������������������", "", "&7&e����&7 ����������������" });
  public static ItemStack COBALT_PICKAXE = new CustomItem(Material.IRON_PICKAXE, "&9����", 0, new String[0], new String[] { "DURABILITY-3", "DIG_SPEED-6" });
  public static ItemStack PICKAXE_OF_VEIN_MINING = new CustomItem(Material.DIAMOND_PICKAXE, "&e��������", 0, new String[] { "", "&r����������������", "&r��������������..." });
  public static ItemStack GLOWSTONE_HELMET = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&e&l��������", 0, new String[] { "", "&a&o��������������!", "", "&9+ ��������" }), Color.YELLOW);
  public static ItemStack GLOWSTONE_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&e&l��������", 0, new String[] { "", "&a&o��������������!", "", "&9+ ��������" }), Color.YELLOW);
  public static ItemStack GLOWSTONE_LEGGINGS = new CustomArmor(new CustomItem(Material.LEATHER_LEGGINGS, "&e&l��������", 0, new String[] { "", "&a&o��������������!", "", "&9+ ��������" }), Color.YELLOW);
  public static ItemStack GLOWSTONE_BOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&e&l��������", 0, new String[] { "", "&a&o��������������!", "", "&9+ ��������" }), Color.YELLOW);
  public static ItemStack ENDER_HELMET = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&5&l��������", 0, new String[] { "", "&a&o����������, ������������!" }), Color.fromRGB(28, 25, 112));
  public static ItemStack ENDER_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&5&l��������", 0, new String[] { "", "&a&o����������, ������������!" }), Color.fromRGB(28, 25, 112));
  public static ItemStack ENDER_LEGGINGS = new CustomArmor(new CustomItem(Material.LEATHER_LEGGINGS, "&5&l��������", 0, new String[] { "", "&a&o����������, ������������!" }), Color.fromRGB(28, 25, 112));
  public static ItemStack ENDER_BOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&5&l��������", 0, new String[] { "", "&a&o����������, ������������!", "", "&9+ ��������������" }), Color.fromRGB(28, 25, 112));
  public static ItemStack SLIME_HELMET = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&a&l��������", 0, new String[] { "", "&a&o������������" }), Color.LIME);
  public static ItemStack SLIME_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&a&l��������", 0, new String[] { "", "&a&o������������" }), Color.LIME);
  public static ItemStack SLIME_LEGGINGS = new CustomArmor(new CustomItem(Material.LEATHER_LEGGINGS, "&a&l��������", 0, new String[] { "", "&a&o������������", "", "&9+ ��������" }), Color.LIME);
  public static ItemStack SLIME_BOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&a&l��������", 0, new String[] { "", "&a&o������������", "", "&9+ ��������", "&9+ ������������" }), Color.LIME);
  public static ItemStack CACTUS_HELMET = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&2����������", 0, new String[0], new String[] { "THORNS-3", "DURABILITY-5" }), Color.GREEN);
  public static ItemStack CACTUS_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&2����������", 0, new String[0], new String[] { "THORNS-3", "DURABILITY-5" }), Color.GREEN);
  public static ItemStack CACTUS_LEGGINGS = new CustomArmor(new CustomItem(Material.LEATHER_LEGGINGS, "&2����������", 0, new String[0], new String[] { "THORNS-3", "DURABILITY-5" }), Color.GREEN);
  public static ItemStack CACTUS_BOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&2����������", 0, new String[0], new String[] { "THORNS-3", "DURABILITY-5" }), Color.GREEN);
  public static ItemStack DAMASCUS_STEEL_HELMET = new CustomItem(Material.IRON_HELMET, "&7��������������", new String[] { "DURABILITY-4", "PROTECTION_ENVIRONMENTAL-4" }, 0);
  public static ItemStack DAMASCUS_STEEL_CHESTPLATE = new CustomItem(Material.IRON_CHESTPLATE, "&7��������������", new String[] { "DURABILITY-4", "PROTECTION_ENVIRONMENTAL-4" }, 0);
  public static ItemStack DAMASCUS_STEEL_LEGGINGS = new CustomItem(Material.IRON_LEGGINGS, "&7��������������", new String[] { "DURABILITY-4", "PROTECTION_ENVIRONMENTAL-4" }, 0);
  public static ItemStack DAMASCUS_STEEL_BOOTS = new CustomItem(Material.IRON_BOOTS, "&7��������������", new String[] { "DURABILITY-4", "PROTECTION_ENVIRONMENTAL-4" }, 0);
  public static ItemStack REINFORCED_ALLOY_HELMET = new CustomItem(Material.IRON_HELMET, "&b������������", new String[] { "DURABILITY-9", "PROTECTION_ENVIRONMENTAL-9" }, 0);
  public static ItemStack REINFORCED_ALLOY_CHESTPLATE = new CustomItem(Material.IRON_CHESTPLATE, "&b������������", new String[] { "DURABILITY-9", "PROTECTION_ENVIRONMENTAL-9" }, 0);
  public static ItemStack REINFORCED_ALLOY_LEGGINGS = new CustomItem(Material.IRON_LEGGINGS, "&b������������", new String[] { "DURABILITY-9", "PROTECTION_ENVIRONMENTAL-9" }, 0);
  public static ItemStack REINFORCED_ALLOY_BOOTS = new CustomItem(Material.IRON_BOOTS, "&b������������", new String[] { "DURABILITY-9", "PROTECTION_ENVIRONMENTAL-9" }, 0);
  public static ItemStack SCUBA_HELMET = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&c��������", 0, new String[] { "", "&b������������������", "&4&o��������������" }), Color.ORANGE);
  public static ItemStack HAZMATSUIT_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&c������", 0, new String[] { "", "&b��������������������", "&4&o��������������" }), Color.ORANGE);
  public static ItemStack HAZMATSUIT_LEGGINGS = new CustomArmor(new CustomItem(Material.LEATHER_LEGGINGS, "&c��������", 0, new String[] { "", "&4&o��������������" }), Color.ORANGE);
  public static ItemStack RUBBER_BOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&c������", 0, new String[] { "", "&4&o��������������" }), Color.BLACK);
  public static ItemStack GILDED_IRON_HELMET = new CustomItem(Material.GOLD_HELMET, "&6����������", new String[] { "DURABILITY-6", "PROTECTION_ENVIRONMENTAL-8" }, 0);
  public static ItemStack GILDED_IRON_CHESTPLATE = new CustomItem(Material.GOLD_CHESTPLATE, "&6����������", new String[] { "DURABILITY-6", "PROTECTION_ENVIRONMENTAL-8" }, 0);
  public static ItemStack GILDED_IRON_LEGGINGS = new CustomItem(Material.GOLD_LEGGINGS, "&6����������", new String[] { "DURABILITY-6", "PROTECTION_ENVIRONMENTAL-8" }, 0);
  public static ItemStack GILDED_IRON_BOOTS = new CustomItem(Material.GOLD_BOOTS, "&6����������", new String[] { "DURABILITY-6", "PROTECTION_ENVIRONMENTAL-8" }, 0);
  public static ItemStack GOLD_HELMET = new CustomItem(Material.GOLD_HELMET, "&6������", 0, new String[] { "&912����" }, new String[] { "DURABILITY-10" });
  public static ItemStack GOLD_CHESTPLATE = new CustomItem(Material.GOLD_CHESTPLATE, "&6������", 0, new String[] { "&912����" }, new String[] { "DURABILITY-10" });
  public static ItemStack GOLD_LEGGINGS = new CustomItem(Material.GOLD_LEGGINGS, "&6������", 0, new String[] { "&912����" }, new String[] { "DURABILITY-10" });
  public static ItemStack GOLD_BOOTS = new CustomItem(Material.GOLD_BOOTS, "&6������", 0, new String[] { "&912����" }, new String[] { "DURABILITY-10" });
  public static ItemStack SLIME_HELMET_STEEL = new CustomItem(Material.IRON_HELMET, "&a&l��������", 0, new String[] { "&7&o������", "", "&a&o������������" }, new String[] { "DURABILITY-4", "PROTECTION_ENVIRONMENTAL-2" });
  public static ItemStack SLIME_CHESTPLATE_STEEL = new CustomItem(Material.IRON_CHESTPLATE, "&a&l��������", 0, new String[] { "&7&o������", "", "&a&o������������" }, new String[] { "DURABILITY-4", "PROTECTION_ENVIRONMENTAL-2" });
  public static ItemStack SLIME_LEGGINGS_STEEL = new CustomItem(Material.IRON_LEGGINGS, "&a&l��������", 0, new String[] { "&7&o������", "", "&a&o������������", "", "&9+ ��������" }, new String[] { "DURABILITY-4", "PROTECTION_ENVIRONMENTAL-2" });
  public static ItemStack SLIME_BOOTS_STEEL = new CustomItem(Material.IRON_BOOTS, "&a&l��������", 0, new String[] { "&7&o������", "", "&a&o������������", "", "&9+ ��������", "&9+ ������������" }, new String[] { "DURABILITY-4", "PROTECTION_ENVIRONMENTAL-2" });
  public static ItemStack BOOTS_OF_THE_STOMPER = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&b����������", 0, new String[] { "", "&9��������������������", "&9����������������/����", "", "&9+ ������������" }), Color.AQUA);
  public static ItemStack HEAVY_METAL_HELMET = new CustomItem(Material.IRON_HELMET, "&c��������", 0, new String[] { "", "&9+ ��������", "&9+ ����" }, new String[] { "DURABILITY-10", "PROTECTION_ENVIRONMENTAL-10" });
  public static ItemStack HEAVY_METAL_CHESTPLATE = new CustomItem(Material.IRON_CHESTPLATE, "&c��������", 0, new String[] { "", "&9+ ��������", "&9+ ����" }, new String[] { "DURABILITY-10", "PROTECTION_ENVIRONMENTAL-10" });
  public static ItemStack HEAVY_METAL_LEGGINGS = new CustomItem(Material.IRON_LEGGINGS, "&c��������", 0, new String[] { "", "&9+ ��������", "&9+ ����" }, new String[] { "DURABILITY-10", "PROTECTION_ENVIRONMENTAL-10" });
  public static ItemStack HEAVY_METAL_BOOTS = new CustomItem(Material.IRON_BOOTS, "&c��������", 0, new String[] { "", "&9+ ��������", "&9+ ����" }, new String[] { "DURABILITY-10", "PROTECTION_ENVIRONMENTAL-10" });
  public static ItemStack MAGIC_LUMP_1 = new CustomItem(Material.GOLD_NUGGET, "&6�������� &7- &eI", 0, new String[] { "", "&c&o����: I" });
  public static ItemStack MAGIC_LUMP_2 = new CustomItem(Material.GOLD_NUGGET, "&6�������� &7- &eII", 0, new String[] { "", "&c&o����: II" });
  public static ItemStack MAGIC_LUMP_3 = new CustomItem(Material.GOLD_NUGGET, "&6�������� &7- &eIII", 0, new String[] { "", "&c&o����: III" });
  public static ItemStack ENDER_LUMP_1 = new CustomItem(Material.GOLD_NUGGET, "&5�������� &7- &eI", 0, new String[] { "", "&c&o����: I" });
  public static ItemStack ENDER_LUMP_2 = new CustomItem(Material.GOLD_NUGGET, "&5�������� &7- &eII", 0, new String[] { "", "&c&o����: II" });
  public static ItemStack ENDER_LUMP_3 = new CustomItem(Material.GOLD_NUGGET, "&5�������� &7- &eIII", 0, new String[] { "", "&c&o����: III" });
  public static ItemStack MAGICAL_BOOK_COVER = new CustomItem(Material.PAPER, "&6��������", 0, new String[] { "", "&a&o��������������" });
  public static ItemStack BASIC_CIRCUIT_BOARD = new CustomItem(Material.ACTIVATOR_RAIL, "&b����������", 0);
  public static ItemStack ADVANCED_CIRCUIT_BOARD = new CustomItem(Material.POWERED_RAIL, "&b����������", 0);
  public static ItemStack WHEAT_FLOUR = new CustomItem(Material.SUGAR, "&r��������", 0);
  public static ItemStack STEEL_PLATE = new CustomItem(Material.PAPER, "&7&l����", 0);
  public static ItemStack COMPRESSED_CARBON = null;
  public static ItemStack BATTERY = null;
  public static ItemStack CARBON_CHUNK = null;
  public static ItemStack STEEL_THRUSTER = new CustomItem(Material.BUCKET, "&7&l��������", 0);
  public static ItemStack POWER_CRYSTAL = null;
  public static ItemStack CHAIN = new CustomItem(Material.STRING, "&b����", 0);
  public static ItemStack HOOK = new CustomItem(Material.FLINT, "&b����", 0);
  public static ItemStack SIFTED_ORE = new CustomItem(Material.SULPHUR, "&6����", 0);
  public static ItemStack STONE_CHUNK = null;
  public static ItemStack LAVA_CRYSTAL = null;
  public static ItemStack SALT = new CustomItem(Material.SUGAR, "&r��", 0);
  public static ItemStack BUTTER = null;
  public static ItemStack CHEESE = null;
  public static ItemStack HEAVY_CREAM = new CustomItem(Material.SNOW_BALL, "&r������", 0);
  public static ItemStack CRUSHED_ORE = new CustomItem(Material.SULPHUR, "&6������������", 0);
  public static ItemStack PULVERIZED_ORE = new CustomItem(Material.SULPHUR, "&6������������", 0);
  public static ItemStack PURE_ORE_CLUSTER = new CustomItem(Material.SULPHUR, "&6������", 0);
  public static ItemStack TINY_URANIUM = null;
  public static ItemStack SMALL_URANIUM = null;
  public static ItemStack MAGNET = null;
  public static ItemStack NECROTIC_SKULL = new CustomItem(new MaterialData(Material.SKULL_ITEM, (byte)1).toItemStack(1), "&c��������");
  public static ItemStack ESSENCE_OF_AFTERLIFE = new CustomItem(Material.SULPHUR, "&4��������", 0);
  public static ItemStack ELECTRO_MAGNET = null;
  public static ItemStack HEATING_COIL = null;
  public static ItemStack COOLING_UNIT = null;
  public static ItemStack ELECTRIC_MOTOR = null;
  public static ItemStack CARGO_MOTOR = null;
  public static ItemStack SCROLL_OF_DIMENSIONAL_TELEPOSITION = new CustomItem(Material.PAPER, "&6������������", 0, new String[] { "", "&c����������������", "&c����������������", "&c������������������������", "&c������������������", "", "", "&r��������:���������� 180 ��" });
  public static ItemStack TOME_OF_KNOWLEDGE_SHARING = new CustomItem(Material.BOOK, "&6��������", 0, new String[] { "&7����: &bNone", "", "&e����&7 ������������������", "", "", "&e����&7 ��������������", "&7��������������" });
  public static ItemStack HARDENED_GLASS = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)8), "&7��������", new String[] { "", "&r������������" });
  public static ItemStack WITHER_PROOF_OBSIDIAN = new CustomItem(Material.OBSIDIAN, "&5������������", 0, new String[] { "", "&r������������", "&r����������������" });
  public static ItemStack WITHER_PROOF_GLASS = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)15), "&5������������", new String[] { "", "&r������������", "&r����������������" });
  public static ItemStack REINFORCED_PLATE = new CustomItem(Material.PAPER, "&7����������", 0);
  public static ItemStack ANCIENT_PEDESTAL = new CustomItem(Material.DISPENSER, "&d��������", 0, new String[] { "", "&5����������������" });
  public static ItemStack ANCIENT_ALTAR = new CustomItem(Material.ENCHANTMENT_TABLE, "&d��������", 0, new String[] { "", "&5������������������", "&5������������������" });
  public static ItemStack DUCT_TAPE = null;
  public static ItemStack RAINBOW_WOOL = new CustomItem(new MaterialData(Material.WOOL), "&5��������", new String[] { "", "&d������������������!" });
  public static ItemStack RAINBOW_GLASS = new CustomItem(new MaterialData(Material.STAINED_GLASS), "&5��������", new String[] { "", "&d������������������!" });
  public static ItemStack RAINBOW_CLAY = new CustomItem(new MaterialData(Material.STAINED_CLAY), "&5��������", new String[] { "", "&d������������������!" });
  public static ItemStack RAINBOW_GLASS_PANE = new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE), "&5����������", new String[] { "", "&d������������������!" });
  public static ItemStack RAINBOW_WOOL_XMAS = new CustomItem(new MaterialData(Material.WOOL), "&5�������� &7(����������)", new String[] { "", Christmas.color("< ������������ >") });
  public static ItemStack RAINBOW_GLASS_XMAS = new CustomItem(new MaterialData(Material.STAINED_GLASS), "&5�������� &7(����������)", new String[] { "", Christmas.color("< ������������ >") });
  public static ItemStack RAINBOW_CLAY_XMAS = new CustomItem(new MaterialData(Material.STAINED_CLAY), "&5�������� &7(����������)", new String[] { "", Christmas.color("< ������������ >") });
  public static ItemStack RAINBOW_GLASS_PANE_XMAS = new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE), "&5���������� &7(����������)", new String[] { "", Christmas.color("< ������������ >") });
  public static ItemStack RAINBOW_WOOL_VALENTINE = new CustomItem(new MaterialData(Material.WOOL), "&5�������� &7(����������)", new String[] { "", "&d< ������������ >" });
  public static ItemStack RAINBOW_GLASS_VALENTINE = new CustomItem(new MaterialData(Material.STAINED_GLASS), "&5�������� &7(����������)", new String[] { "", "&d< ������������ >" });
  public static ItemStack RAINBOW_CLAY_VALENTINE = new CustomItem(new MaterialData(Material.STAINED_CLAY), "&5�������� &7(����������)", new String[] { "", "&d< ������������ >" });
  public static ItemStack RAINBOW_GLASS_PANE_VALENTINE = new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE), "&5���������� &7(����������)", new String[] { "", "&d< ������������ >" });
  public static ItemStack COPPER_INGOT = new CustomItem(Material.CLAY_BRICK, "&b����", 0, new String[0]);
  public static ItemStack TIN_INGOT = new CustomItem(Material.IRON_INGOT, "&b����", 0, new String[0]);
  public static ItemStack SILVER_INGOT = new CustomItem(Material.IRON_INGOT, "&b����", 0, new String[0]);
  public static ItemStack ALUMINUM_INGOT = new CustomItem(Material.IRON_INGOT, "&b����", 0, new String[0]);
  public static ItemStack LEAD_INGOT = new CustomItem(Material.IRON_INGOT, "&b����", 0, new String[0]);
  public static ItemStack ZINC_INGOT = new CustomItem(Material.IRON_INGOT, "&b����", 0, new String[0]);
  public static ItemStack MAGNESIUM_INGOT = new CustomItem(Material.IRON_INGOT, "&b����", 0, new String[0]);
  public static ItemStack STEEL_INGOT = new CustomItem(Material.IRON_INGOT, "&b����", 0, new String[0]);
  public static ItemStack BRONZE_INGOT = new CustomItem(Material.CLAY_BRICK, "&b������", 0, new String[0]);
  public static ItemStack DURALUMIN_INGOT = new CustomItem(Material.IRON_INGOT, "&b����������", 0, new String[0]);
  public static ItemStack BILLON_INGOT = new CustomItem(Material.IRON_INGOT, "&b����������", 0, new String[0]);
  public static ItemStack BRASS_INGOT = new CustomItem(Material.GOLD_INGOT, "&b������", 0, new String[0]);
  public static ItemStack ALUMINUM_BRASS_INGOT = new CustomItem(Material.GOLD_INGOT, "&b��������", 0, new String[0]);
  public static ItemStack ALUMINUM_BRONZE_INGOT = new CustomItem(Material.GOLD_INGOT, "&b��������", 0, new String[0]);
  public static ItemStack CORINTHIAN_BRONZE_INGOT = new CustomItem(Material.GOLD_INGOT, "&b������������", 0, new String[0]);
  public static ItemStack SOLDER_INGOT = new CustomItem(Material.IRON_INGOT, "&b������", 0, new String[0]);
  public static ItemStack DAMASCUS_STEEL_INGOT = new CustomItem(Material.IRON_INGOT, "&b������������", 0, new String[0]);
  public static ItemStack HARDENED_METAL_INGOT = new CustomItem(Material.IRON_INGOT, "&b&l��������", 0);
  public static ItemStack REINFORCED_ALLOY_INGOT = new CustomItem(Material.IRON_INGOT, "&b&l����������", 0);
  public static ItemStack FERROSILICON = new CustomItem(Material.IRON_INGOT, "&b����", 0);
  public static ItemStack GILDED_IRON = new CustomItem(Material.GOLD_INGOT, "&6&l��������", 0);
  public static ItemStack REDSTONE_ALLOY = new CustomItem(Material.CLAY_BRICK, "&c����������", 0);
  public static ItemStack NICKEL_INGOT = new CustomItem(Material.IRON_INGOT, "&b����", 0);
  public static ItemStack COBALT_INGOT = new CustomItem(Material.IRON_INGOT, "&9����", 0);
  public static ItemStack GOLD_4K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(4����)", 0);
  public static ItemStack GOLD_6K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(6����)", 0);
  public static ItemStack GOLD_8K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(8����)", 0);
  public static ItemStack GOLD_10K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(10����)", 0);
  public static ItemStack GOLD_12K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(12����)", 0);
  public static ItemStack GOLD_14K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(14����)", 0);
  public static ItemStack GOLD_16K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(16����)", 0);
  public static ItemStack GOLD_18K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(18����)", 0);
  public static ItemStack GOLD_20K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(20����)", 0);
  public static ItemStack GOLD_22K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(22����)", 0);
  public static ItemStack GOLD_24K = new CustomItem(Material.GOLD_INGOT, "&r���� &7(24����)", 0);
  public static ItemStack IRON_DUST = new CustomItem(Material.SULPHUR, "&6����", 0);
  public static ItemStack GOLD_DUST = new CustomItem(Material.GLOWSTONE_DUST, "&6����", 0);
  public static ItemStack TIN_DUST = new CustomItem(Material.SUGAR, "&6����", 0);
  public static ItemStack COPPER_DUST = new CustomItem(Material.GLOWSTONE_DUST, "&6����", 0);
  public static ItemStack SILVER_DUST = new CustomItem(Material.SUGAR, "&6����", 0);
  public static ItemStack ALUMINUM_DUST = new CustomItem(Material.SUGAR, "&6����", 0);
  public static ItemStack LEAD_DUST = new CustomItem(Material.SULPHUR, "&6����", 0);
  public static ItemStack SULFATE = new CustomItem(Material.GLOWSTONE_DUST, "&6������", 0);
  public static ItemStack ZINC_DUST = new CustomItem(Material.SUGAR, "&6����", 0);
  public static ItemStack MAGNESIUM_DUST = new CustomItem(Material.SUGAR, "&6��", 0);
  public static ItemStack CARBON = null;
  public static ItemStack SILICON = new CustomItem(Material.FIREWORK_CHARGE, "&6��", 0);
  public static ItemStack GOLD_24K_BLOCK = new CustomItem(Material.GOLD_BLOCK, "&r���� &7(24����)", 0);
  public static ItemStack SYNTHETIC_DIAMOND = new CustomItem(Material.DIAMOND, "&b��������", 0);
  public static ItemStack SYNTHETIC_SAPPHIRE = new CustomItem(new MaterialData(Material.INK_SACK, (byte)4), "&b����������", new String[0]);
  public static ItemStack SYNTHETIC_EMERALD = new CustomItem(Material.EMERALD, "&b����������", 0);
  public static ItemStack CARBONADO = null;
  public static ItemStack RAW_CARBONADO = null;
  public static ItemStack URANIUM = null;
  public static ItemStack NEPTUNIUM = null;
  public static ItemStack PLUTONIUM = null;
  public static ItemStack BOOSTED_URANIUM = null;
  public static ItemStack TALISMAN = new CustomItem(Material.EMERALD, "&6����������", 0);
  public static ItemStack TALISMAN_ANVIL = new CustomItem(Material.EMERALD, "&a����������", 0, new String[] { "", "&r����������������������", "&r����������������.", "&c&l! &c������", "", "&4&l����:", "&4����������", "&4����������������������", "" });
  public static ItemStack TALISMAN_MINER = new CustomItem(Material.EMERALD, "&a����������", 0, new String[] { "", "&r����������������", "&r��������������", "&r���� 20% ������", "&r������������������" });
  public static ItemStack TALISMAN_HUNTER = new CustomItem(Material.EMERALD, "&a����������", 0, new String[] { "", "&r����������������", "&r��������������", "&r���� 20% ������", "&r������������������������" });
  public static ItemStack TALISMAN_LAVA = new CustomItem(Material.EMERALD, "&a��������������", 0, new String[] { "", "&r������������������������", "&r������������", "&r����������������", "&r������������������������", "&c&l! &c������" });
  public static ItemStack TALISMAN_WATER = new CustomItem(Material.EMERALD, "&a������������", 0, new String[] { "", "&r������������", "&r������������", "&r��������������", "&r������������������", "&r����", "&c&l! &c������" });
  public static ItemStack TALISMAN_ANGEL = new CustomItem(Material.EMERALD, "&a����������", 0, new String[] { "", "&r������������", "&r������������", "&r���� 75% ������������", "&r������������" });
  public static ItemStack TALISMAN_FIRE = new CustomItem(Material.EMERALD, "&a������������", 0, new String[] { "", "&r������������", "&r������������", "&r����������", "&r��������������", "&c&l! &c������" });
  public static ItemStack TALISMAN_MAGICIAN = new CustomItem(Material.EMERALD, "&a������������", 0, new String[] { "", "&r������������", "&r������������", "&r������ 80% ����������������", "&r������������������" });
  public static ItemStack TALISMAN_TRAVELLER = new CustomItem(Material.EMERALD, "&a������������", 0, new String[] { "", "&r������������", "&r������������", "&r�� 60% ������", "&r������������������������" });
  public static ItemStack TALISMAN_WARRIOR = new CustomItem(Material.EMERALD, "&a����������", 0, new String[] { "", "&r������������", "&r������������", "&r������������������ ���� III ������", "&c&l! &c������" });
  public static ItemStack TALISMAN_KNIGHT = new CustomItem(Material.EMERALD, "&a����������", 0, new String[] { "", "&r������������", "&r������������", "&r����������", "&r���� 30% ���������� 5 ����������������", "&c&l! &c������" });
  public static ItemStack TALISMAN_WHIRLWIND = new CustomItem(Material.EMERALD, "&a����������", 0, new String[] { "", "&r������������", "&r������������", "&r���� 60% ������������������������" });
  public static ItemStack TALISMAN_WIZARD = new CustomItem(Material.EMERALD, "&a����������", 0, new String[] { "", "&r������������", "&r������������", "&r�������� ����4/5", "&r������������������", "&r��������������������" });
  public static ItemStack STAFF_ELEMENTAL = new CustomItem(Material.STICK, "&6��������", 0);
  public static ItemStack STAFF_WIND = new CustomItem(Material.STICK, "&6�������� &7- &b&o��", 0, new String[] { "", "&7����: &b&o��", "", "&7&e����&7 ������������������" }, new String[] { "LUCK-1" });
  public static ItemStack STAFF_FIRE = new CustomItem(Material.STICK, "&6�������� &7- &c&o��", 0, new String[] { "", "&7����: &c&o��" }, new String[] { "FIRE_ASPECT-5" });
  public static ItemStack STAFF_WATER = new CustomItem(Material.STICK, "&6�������� &7- &1&o��", 0, new String[] { "", "&7����: &1&o��", "", "&7&e����&7 ����������������" }, new String[] { "WATER_WORKER-1" });
  public static ItemStack GRIND_STONE = new CustomItem(Material.DISPENSER, "&b����", 0, new String[] { "", "&a&o��������������" });
  public static ItemStack ARMOR_FORGE = new CustomItem(Material.ANVIL, "&6����������", 0, new String[] { "", "&a&o����������������������" });
  public static ItemStack SMELTERY = new CustomItem(Material.FURNACE, "&6������", 0, new String[] { "", "&a&o��������������������" });
  public static ItemStack ORE_CRUSHER = new CustomItem(Material.DISPENSER, "&b����������", 0, new String[] { "", "&a&o��������" });
  public static ItemStack COMPRESSOR = new CustomItem(Material.PISTON_BASE, "&b������", 0, new String[] { "", "&a&o��������" });
  public static ItemStack PRESSURE_CHAMBER = new CustomItem(Material.GLASS, "&b������", 0, new String[] { "", "&a&o��������������" });
  public static ItemStack MAGIC_WORKBENCH = new CustomItem(Material.WORKBENCH, "&6����������", 0, new String[] { "������������������" });
  public static ItemStack ORE_WASHER = new CustomItem(Material.CAULDRON_ITEM, "&6������", 0, new String[] { "", "&a&o��������������������", "&a&o������������������" });
  public static ItemStack SAW_MILL = new CustomItem(Material.IRON_FENCE, "&6������", 0, new String[] { "", "&a&o��������������������8������" });
  public static ItemStack COMPOSTER = new CustomItem(Material.CAULDRON_ITEM, "&a������", 0, new String[] { "", "&a&o������������������������������..." });
  public static ItemStack ENHANCED_CRAFTING_TABLE = new CustomItem(Material.WORKBENCH, "&e����������", 0, new String[] { "", "&a&o����������������", "&a&o������������������..." });
  public static ItemStack CRUCIBLE = new CustomItem(Material.CAULDRON_ITEM, "&c����", 0, new String[] { "", "&a&o������������������" });
  public static ItemStack JUICER = new CustomItem(Material.GLASS_BOTTLE, "&a������", 0, new String[] { "", "&a&o������������������" });
  public static ItemStack SOLAR_PANEL = new CustomItem(Material.DAYLIGHT_DETECTOR, "&b��������", 0, new String[] { "", "&a&o��������������" });
  public static ItemStack SOLAR_ARRAY = new CustomItem(Material.DAYLIGHT_DETECTOR, "&b������������", 0, new String[] { "", "&a&o��������������" });
  public static ItemStack DIGITAL_MINER = new CustomItem(Material.IRON_PICKAXE, "&b����������", 0, new String[] { "", "&a&o������������!" });
  public static ItemStack ADVANCED_DIGITAL_MINER = new CustomItem(Material.DIAMOND_PICKAXE, "&6��������������", 0, new String[] { "", "&a&o������������!", "&a&o������������" });
  public static ItemStack AUTOMATED_PANNING_MACHINE = new CustomItem(Material.BOWL, "&a����������", 0, new String[] { "", "&a&o����������" });
  public static ItemStack HOLOGRAM_PROJECTOR = new CustomItem(new MaterialData(Material.STEP, (byte)7), "&b����������", new String[] { "", "&r����������������������" });
  public static ItemStack ENHANCED_FURNACE = new CustomItem(Material.FURNACE, "&7�������� - &eI", 0, new String[] { "", "&7��������: &e1x", "&7��������: &e1x", "&7��������: &e1x" });
  public static ItemStack ENHANCED_FURNACE_2 = new CustomItem(Material.FURNACE, "&7�������� - &eII", 0, new String[] { "", "&7��������: &e2x", "&7��������: &e1x", "&7��������: &e1x" });
  public static ItemStack ENHANCED_FURNACE_3 = new CustomItem(Material.FURNACE, "&7�������� - &eIII", 0, new String[] { "", "&7��������: &e2x", "&7��������: &e2x", "&7��������: &e1x" });
  public static ItemStack ENHANCED_FURNACE_4 = new CustomItem(Material.FURNACE, "&7�������� - &eIV", 0, new String[] { "", "&7��������: &e3x", "&7��������: &e2x", "&7��������: &e1x" });
  public static ItemStack ENHANCED_FURNACE_5 = new CustomItem(Material.FURNACE, "&7�������� - &eV", 0, new String[] { "", "&7��������: &e3x", "&7��������: &e2x", "&7��������: &e2x" });
  public static ItemStack ENHANCED_FURNACE_6 = new CustomItem(Material.FURNACE, "&7�������� - &eVI", 0, new String[] { "", "&7��������: &e3x", "&7��������: &e3x", "&7��������: &e2x" });
  public static ItemStack ENHANCED_FURNACE_7 = new CustomItem(Material.FURNACE, "&7�������� - &eVII", 0, new String[] { "", "&7��������: &e4x", "&7��������: &e3x", "&7��������: &e2x" });
  public static ItemStack ENHANCED_FURNACE_8 = new CustomItem(Material.FURNACE, "&7�������� - &eVIII", 0, new String[] { "", "&7��������: &e4x", "&7��������: &e4x", "&7��������: &e2x" });
  public static ItemStack ENHANCED_FURNACE_9 = new CustomItem(Material.FURNACE, "&7�������� - &eIX", 0, new String[] { "", "&7��������: &e5x", "&7��������: &e4x", "&7��������: &e2x" });
  public static ItemStack ENHANCED_FURNACE_10 = new CustomItem(Material.FURNACE, "&7�������� - &eX", 0, new String[] { "", "&7��������: &e5x", "&7��������: &e5x", "&7��������: &e2x" });
  public static ItemStack ENHANCED_FURNACE_11 = new CustomItem(Material.FURNACE, "&7�������� - &eXI", 0, new String[] { "", "&7��������: &e5x", "&7��������: &e5x", "&7��������: &e3x" });
  public static ItemStack REINFORCED_FURNACE = new CustomItem(Material.FURNACE, "&7������������", 0, new String[] { "", "&7��������: &e10x", "&7��������: &e10x", "&7��������: &e3x" });
  public static ItemStack CARBONADO_EDGED_FURNACE = new CustomItem(Material.FURNACE, "&7����������������", 0, new String[] { "", "&7��������: &e20x", "&7��������: &e10x", "&7��������: &e3x" });
  public static ItemStack BLOCK_PLACER = new CustomItem(Material.DISPENSER, "&a����������", 0, new String[] { "", "&r������������������������", "&r������������" });
  public static ItemStack SOULBOUND_SWORD = new CustomItem(Material.DIAMOND_SWORD, "&c������������", 0);
  public static ItemStack SOULBOUND_BOW = new CustomItem(Material.BOW, "&c������������", 0);
  public static ItemStack SOULBOUND_PICKAXE = new CustomItem(Material.DIAMOND_PICKAXE, "&c������������", 0);
  public static ItemStack SOULBOUND_AXE = new CustomItem(Material.DIAMOND_AXE, "&c������������", 0);
  public static ItemStack SOULBOUND_SHOVEL = new CustomItem(Material.DIAMOND_SPADE, "&c������������", 0);
  public static ItemStack SOULBOUND_HOE = new CustomItem(Material.DIAMOND_HOE, "&c������������", 0);
  public static ItemStack SOULBOUND_HELMET = new CustomItem(Material.DIAMOND_HELMET, "&c������������", 0);
  public static ItemStack SOULBOUND_CHESTPLATE = new CustomItem(Material.DIAMOND_CHESTPLATE, "&c������������", 0);
  public static ItemStack SOULBOUND_LEGGINGS = new CustomItem(Material.DIAMOND_LEGGINGS, "&c������������", 0);
  public static ItemStack SOULBOUND_BOOTS = new CustomItem(Material.DIAMOND_BOOTS, "&c������������", 0);
  public static ItemStack BLANK_RUNE = null;
  public static ItemStack RUNE_AIR = null;
  public static ItemStack RUNE_WATER = null;
  public static ItemStack RUNE_FIRE = null;
  public static ItemStack RUNE_EARTH = null;
  public static ItemStack RUNE_ENDER = null;
  public static ItemStack RUNE_RAINBOW = null;
  public static ItemStack SOLAR_GENERATOR;
  public static ItemStack SOLAR_GENERATOR_2;
  public static ItemStack SOLAR_GENERATOR_3;
  public static ItemStack SOLAR_GENERATOR_4;
  public static ItemStack COAL_GENERATOR;
  public static ItemStack LAVA_GENERATOR;
  public static ItemStack ELECTRIC_FURNACE;
  public static ItemStack ELECTRIC_FURNACE_2;
  public static ItemStack ELECTRIC_FURNACE_3;
  public static ItemStack ELECTRIC_ORE_GRINDER;
  public static ItemStack ELECTRIC_ORE_GRINDER_2;
  public static ItemStack ELECTRIC_INGOT_PULVERIZER;
  public static ItemStack AUTO_ENCHANTER;
  public static ItemStack AUTO_DISENCHANTER;
  public static ItemStack AUTO_ANVIL;
  public static ItemStack AUTO_ANVIL_2;
  public static ItemStack BIO_REACTOR;
  public static ItemStack MULTIMETER;
  public static ItemStack SMALL_CAPACITOR;
  public static ItemStack MEDIUM_CAPACITOR;
  public static ItemStack BIG_CAPACITOR;
  public static ItemStack LARGE_CAPACITOR;
  public static ItemStack CARBONADO_EDGED_CAPACITOR;
  public static ItemStack PROGRAMMABLE_ANDROID;
  public static ItemStack PROGRAMMABLE_ANDROID_MINER;
  public static ItemStack PROGRAMMABLE_ANDROID_BUTCHER;
  public static ItemStack PROGRAMMABLE_ANDROID_FARMER;
  public static ItemStack PROGRAMMABLE_ANDROID_WOODCUTTER;
  public static ItemStack PROGRAMMABLE_ANDROID_FISHERMAN;
  public static ItemStack PROGRAMMABLE_ANDROID_2;
  public static ItemStack PROGRAMMABLE_ANDROID_2_FISHERMAN;
  public static ItemStack PROGRAMMABLE_ANDROID_2_FARMER;
  public static ItemStack PROGRAMMABLE_ANDROID_2_BUTCHER;
  public static ItemStack PROGRAMMABLE_ANDROID_3;
  public static ItemStack PROGRAMMABLE_ANDROID_3_FISHERMAN;
  public static ItemStack PROGRAMMABLE_ANDROID_3_BUTCHER;
  public static ItemStack GPS_TRANSMITTER;
  public static ItemStack GPS_TRANSMITTER_2;
  public static ItemStack GPS_TRANSMITTER_3;
  public static ItemStack GPS_TRANSMITTER_4;
  public static ItemStack GPS_CONTROL_PANEL;
  public static ItemStack GPS_MARKER_TOOL;
  public static ItemStack GPS_EMERGENCY_TRANSMITTER;
  public static ItemStack GPS_GEO_SCANNER;
  public static ItemStack ANDROID_INTERFACE_FUEL;
  public static ItemStack ANDROID_INTERFACE_ITEMS;
  public static ItemStack BUCKET_OF_OIL;
  public static ItemStack BUCKET_OF_FUEL;
  public static ItemStack OIL_PUMP;
  public static ItemStack REFINERY;
  public static ItemStack COMBUSTION_REACTOR;
  public static ItemStack ANDROID_MEMORY_CORE;
  public static ItemStack GPS_TELEPORTER_PYLON;
  public static ItemStack GPS_TELEPORTATION_MATRIX;
  public static ItemStack GPS_ACTIVATION_DEVICE_SHARED;
  public static ItemStack GPS_ACTIVATION_DEVICE_PERSONAL;
  public static ItemStack ELEVATOR;
  public static ItemStack INFUSED_HOPPER;
  public static ItemStack PLASTIC_SHEET;
  public static ItemStack HEATED_PRESSURE_CHAMBER;
  public static ItemStack HEATED_PRESSURE_CHAMBER_2;
  public static ItemStack ELECTRIC_SMELTERY;
  public static ItemStack ELECTRIC_SMELTERY_2;
  public static ItemStack ELECTRIFIED_CRUCIBLE;
  public static ItemStack ELECTRIFIED_CRUCIBLE_2;
  public static ItemStack ELECTRIFIED_CRUCIBLE_3;
  public static ItemStack CARBON_PRESS;
  public static ItemStack CARBON_PRESS_2;
  public static ItemStack CARBON_PRESS_3;
  public static ItemStack BLISTERING_INGOT;
  public static ItemStack BLISTERING_INGOT_2;
  public static ItemStack BLISTERING_INGOT_3;
  public static ItemStack ENERGY_REGULATOR;
  public static ItemStack DEBUG_FISH;
  public static ItemStack NETHER_ICE;
  public static ItemStack ENRICHED_NETHER_ICE;
  public static ItemStack NETHER_ICE_COOLANT_CELL;
  public static ItemStack NETHER_DRILL;
  public static ItemStack CARGO_MANAGER;
  public static ItemStack CARGO_NODE;
  public static ItemStack CARGO_INPUT;
  public static ItemStack CARGO_OUTPUT;
  public static ItemStack CARGO_OUTPUT_ADVANCED;
  public static ItemStack AUTO_BREEDER;
  public static ItemStack ORGANIC_FOOD;
  public static ItemStack ORGANIC_FOOD2;
  public static ItemStack ORGANIC_FOOD3;
  public static ItemStack ORGANIC_FOOD4;
  public static ItemStack ORGANIC_FOOD5;
  public static ItemStack ORGANIC_FOOD6;
  public static ItemStack ORGANIC_FOOD7;
  public static ItemStack ORGANIC_FOOD8;
  public static ItemStack FERTILIZER;
  public static ItemStack FERTILIZER2;
  public static ItemStack FERTILIZER3;
  public static ItemStack FERTILIZER4;
  public static ItemStack FERTILIZER5;
  public static ItemStack FERTILIZER6;
  public static ItemStack FERTILIZER7;
  public static ItemStack FERTILIZER8;
  public static ItemStack ANIMAL_GROWTH_ACCELERATOR;
  public static ItemStack CROP_GROWTH_ACCELERATOR;
  public static ItemStack CROP_GROWTH_ACCELERATOR_2;
  public static ItemStack FOOD_FABRICATOR;
  public static ItemStack FOOD_FABRICATOR_2;
  public static ItemStack FOOD_COMPOSTER;
  public static ItemStack FOOD_COMPOSTER_2;
  public static ItemStack XP_COLLECTOR;
  public static ItemStack REACTOR_COOLANT_CELL;
  public static ItemStack NUCLEAR_REACTOR;
  public static ItemStack NETHERSTAR_REACTOR;
  public static ItemStack REACTOR_ACCESS_PORT;
  public static ItemStack FREEZER;
  public static ItemStack FREEZER_2;
  public static ItemStack ELECTRIC_GOLD_PAN;
  public static ItemStack ELECTRIC_GOLD_PAN_2;
  public static ItemStack ELECTRIC_GOLD_PAN_3;
  public static ItemStack ELECTRIC_DUST_WASHER;
  public static ItemStack ELECTRIC_DUST_WASHER_2;
  public static ItemStack ELECTRIC_DUST_WASHER_3;
  public static ItemStack ELECTRIC_INGOT_FACTORY;
  public static ItemStack ELECTRIC_INGOT_FACTORY_2;
  public static ItemStack ELECTRIC_INGOT_FACTORY_3;
  public static ItemStack AUTOMATED_CRAFTING_CHAMBER;
  public static ItemStack FLUID_PUMP;
  public static ItemStack CHARGING_BENCH;
  public static ItemStack WITHER_ASSEMBLER;
  public static ItemStack TRASH_CAN;
  public static ItemStack ELYTRA;
  public static ItemStack ELYTRA_SCALE;
  public static ItemStack INFUSED_ELYTRA;
  public static ItemStack SOULBOUND_ELYTRA;
  public static ItemStack CHEST_TERMINAL;
  public static ItemStack CT_IMPORT_BUS;
  public static ItemStack CT_EXPORT_BUS;
  
  static
  {
    ItemStack itemB = new ItemStack(Material.FIREWORK_CHARGE);
    FireworkEffectMeta imB = (FireworkEffectMeta)itemB.getItemMeta();
    imB.setEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).with(FireworkEffect.Type.BURST).withColor(Color.BLACK).build());
    imB.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8��������"));
    itemB.setItemMeta(imB);
    BLANK_RUNE = itemB;
    
    ItemStack itemA = new ItemStack(Material.FIREWORK_CHARGE);
    FireworkEffectMeta imA = (FireworkEffectMeta)itemA.getItemMeta();
    imA.setEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.AQUA).build());
    imA.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7�������� &8&l[&b&l��&8&l]"));
    itemA.setItemMeta(imA);
    RUNE_AIR = itemA;
    
    ItemStack itemW = new ItemStack(Material.FIREWORK_CHARGE);
    FireworkEffectMeta imW = (FireworkEffectMeta)itemW.getItemMeta();
    imW.setEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.BLUE).build());
    imW.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7�������� &8&l[&1&l��&8&l]"));
    itemW.setItemMeta(imW);
    RUNE_WATER = itemW;
    
    ItemStack itemF = new ItemStack(Material.FIREWORK_CHARGE);
    FireworkEffectMeta imF = (FireworkEffectMeta)itemF.getItemMeta();
    imF.setEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.RED).build());
    imF.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7�������� &8&l[&4&l��&8&l]"));
    itemF.setItemMeta(imF);
    RUNE_FIRE = itemF;
    
    ItemStack itemE = new ItemStack(Material.FIREWORK_CHARGE);
    FireworkEffectMeta imE = (FireworkEffectMeta)itemE.getItemMeta();
    imE.setEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.ORANGE).build());
    imE.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7�������� &8&l[&c&l��&8&l]"));
    itemE.setItemMeta(imE);
    RUNE_EARTH = itemE;
    
    ItemStack itemN = new ItemStack(Material.FIREWORK_CHARGE);
    FireworkEffectMeta imN = (FireworkEffectMeta)itemN.getItemMeta();
    imN.setEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.PURPLE).build());
    imN.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7�������� &8&l[&5&l����&8&l]"));
    itemN.setItemMeta(imN);
    RUNE_ENDER = itemN;
    
    ItemStack itemR = new ItemStack(Material.FIREWORK_CHARGE);
    FireworkEffectMeta imR = (FireworkEffectMeta)itemR.getItemMeta();
    imR.setEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.PURPLE).build());
    imR.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&7�������� &8&l[&d&l����&8&l]"));
    itemR.setItemMeta(imR);
    RUNE_RAINBOW = itemR;
    
    SOLAR_GENERATOR = new CustomItem(new ItemStack(Material.DAYLIGHT_DETECTOR), "&b������������", new String[] { "", "&e����������", "&8? &e? &70 J ������", "&8? &e? &74 J/s" });
    SOLAR_GENERATOR_2 = new CustomItem(new ItemStack(Material.DAYLIGHT_DETECTOR), "&c����������������", new String[] { "", "&a����������", "&8? &e? &70 J ������", "&8? &e? &716 J/s" });
    SOLAR_GENERATOR_3 = new CustomItem(new ItemStack(Material.DAYLIGHT_DETECTOR), "&4��������������������", new String[] { "", "&4����������", "&8? &e? &70 J ������", "&8? &e? &764 J/s" });
    SOLAR_GENERATOR_4 = new CustomItem(new ItemStack(Material.DAYLIGHT_DETECTOR), "&e����������������", new String[] { "", "&9����������", "", "&4����������", "&8? &e? &70 J ������", "&8? &e? &7256 J/s (����)", "&8? &e? &7128 J/s (����)" });
    
    COAL_GENERATOR = null;
    LAVA_GENERATOR = null;
    
    ELECTRIC_FURNACE = new CustomItem(new ItemStack(Material.FURNACE), "&c����", new String[] { "", "&e��������", "&8? &7����: 1x", "&8? &e? &74 J/s" });
    ELECTRIC_FURNACE_2 = new CustomItem(new ItemStack(Material.FURNACE), "&c���� &7- &eII", new String[] { "", "&a��������", "&8? &7����: 2x", "&8? &e? &76 J/s" });
    ELECTRIC_FURNACE_3 = new CustomItem(new ItemStack(Material.FURNACE), "&c���� &7- &eIII", new String[] { "", "&a��������", "&8? &7����: 4x", "&8? &e? &710 J/s" });
    
    ELECTRIC_ORE_GRINDER = new CustomItem(new ItemStack(Material.FURNACE), "&c����������", new String[] { "", "&r��������������������������", "", "&6��������", "&8? &7����: 1x", "&8? &e? &712 J/s" });
    ELECTRIC_ORE_GRINDER_2 = new CustomItem(new ItemStack(Material.FURNACE), "&c���������� &7(&eII&7)", new String[] { "", "&r��������������������������", "", "&4��������", "&8? &7����: 4x", "&8? &e? &730 J/s" });
    ELECTRIC_INGOT_PULVERIZER = new CustomItem(new ItemStack(Material.FURNACE), "&c����������", new String[] { "", "&r����������", "", "&a��������", "&8? &7����: 1x", "&8? &e? &714 J/s" });
    AUTO_ENCHANTER = new CustomItem(new ItemStack(Material.ENCHANTMENT_TABLE), "&5����������", new String[] { "", "&a��������", "&8? &7����: 1x", "&8? &e? &718 J/s" });
    AUTO_DISENCHANTER = new CustomItem(new ItemStack(Material.ENCHANTMENT_TABLE), "&5��������������", new String[] { "", "&a��������", "&8? &7����: 1x", "&8? &e? &718 J/s" });
    AUTO_ANVIL = new CustomItem(new ItemStack(Material.IRON_BLOCK), "&7��������", new String[] { "", "&6��������", "&8? &7������������: 10%", "&8? &e? &724 J/s" });
    AUTO_ANVIL_2 = new CustomItem(new ItemStack(Material.IRON_BLOCK), "&7�������� &7(&eII&7)", new String[] { "", "&4��������", "&8? &7������������: 25%", "&8? &e? &732 J/s" });
    
    BIO_REACTOR = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)5), "&2����������", new String[] { "", "&6����������", "&8? &e? &7128 J ������", "&8? &e? &78 J/s" });
    MULTIMETER = new CustomItem(new MaterialData(Material.WATCH), "&e������", new String[] { "", "&r��������������", "&r������������" });
    SMALL_CAPACITOR = null;MEDIUM_CAPACITOR = null;BIG_CAPACITOR = null;LARGE_CAPACITOR = null;CARBONADO_EDGED_CAPACITOR = null;
    
    PROGRAMMABLE_ANDROID = null;
    PROGRAMMABLE_ANDROID_MINER = null;
    PROGRAMMABLE_ANDROID_BUTCHER = null;
    PROGRAMMABLE_ANDROID_FARMER = null;
    PROGRAMMABLE_ANDROID_WOODCUTTER = null;
    PROGRAMMABLE_ANDROID_FISHERMAN = null;
    
    PROGRAMMABLE_ANDROID_2 = null;
    PROGRAMMABLE_ANDROID_2_FISHERMAN = null;
    PROGRAMMABLE_ANDROID_2_FARMER = null;
    PROGRAMMABLE_ANDROID_2_BUTCHER = null;
    
    PROGRAMMABLE_ANDROID_3 = null;
    PROGRAMMABLE_ANDROID_3_FISHERMAN = null;
    PROGRAMMABLE_ANDROID_3_BUTCHER = null;
    
    GPS_TRANSMITTER = null;
    GPS_TRANSMITTER_2 = null;
    GPS_TRANSMITTER_3 = null;
    GPS_TRANSMITTER_4 = null;
    
    GPS_CONTROL_PANEL = null;
    GPS_MARKER_TOOL = new CustomItem(new MaterialData(Material.REDSTONE_TORCH_ON), "&bGPS ��������", new String[] { "", "&r��������������������������", "&r��������������������" });
    GPS_EMERGENCY_TRANSMITTER = null;
    GPS_GEO_SCANNER = null;
    
    ANDROID_INTERFACE_FUEL = new CustomItem(new ItemStack(Material.DISPENSER), "&7�������������� &c(����)", new String[] { "", "&r��������������������", "&r��������������������", "&r������������������������" });
    ANDROID_INTERFACE_ITEMS = new CustomItem(new ItemStack(Material.DISPENSER), "&7�������������� &9(Items)", new String[] { "", "&r����������������������", "&r������������������������", "&r������������������" });
    
    BUCKET_OF_OIL = null;
    BUCKET_OF_FUEL = null;
    OIL_PUMP = null;
    
    REFINERY = new CustomItem(new ItemStack(Material.PISTON_BASE), "&c������", new String[] { "", "&r������������������" });
    COMBUSTION_REACTOR = null;
    ANDROID_MEMORY_CORE = null;
    
    GPS_TELEPORTER_PYLON = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)10), "&5GPS ������", new String[] { "", "&7����������" });
    GPS_TELEPORTATION_MATRIX = new CustomItem(new MaterialData(Material.IRON_BLOCK), "&bGPS ��������", new String[] { "", "&r�������� GPS ������������", "&r������������������������", "&r������������", "&r������������." });
    GPS_ACTIVATION_DEVICE_SHARED = new CustomItem(new MaterialData(Material.STONE_PLATE), "&rGPS �������� &3(������)", new String[] { "", "&r������������������", "&r����������������������", "&r��������" });
    GPS_ACTIVATION_DEVICE_PERSONAL = new CustomItem(new MaterialData(Material.STONE_PLATE), "&rGPS �������� &a(����)", new String[] { "", "&r������������������", "&r����������������������", "&r��������", "", "&r������������������������", "&r����������������" });
    
    ELEVATOR = new CustomItem(new MaterialData(Material.STONE_PLATE), "&b������", new String[] { "", "&r������������������", "&r������������������������.", "", "&e����������&7 ������������" });
    
    INFUSED_HOPPER = new CustomItem(new MaterialData(Material.HOPPER), "&5����������", new String[] { "", "&r�������� 7x7x7 ����������������", "&r����������������" });
    
    PLASTIC_SHEET = new CustomItem(new MaterialData(Material.PAPER), "&r������", new String[0]);
    HEATED_PRESSURE_CHAMBER = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)8), "&c����������", new String[] { "", "&4��������", "&8? &7����: 1x", "&8? &e? &710 J/s" });
    HEATED_PRESSURE_CHAMBER_2 = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)8), "&c���������� &7- &eII", new String[] { "", "&4��������", "&8? &7����: 5x", "&8? &e? &744 J/s" });
    
    ELECTRIC_SMELTERY = new CustomItem(new MaterialData(Material.FURNACE), "&c����������", new String[] { "", "&4����������, ����������������", "", "&4��������", "&8? &7����: 1x", "&8? &e? &720 J/s" });
    ELECTRIC_SMELTERY_2 = new CustomItem(new MaterialData(Material.FURNACE), "&c���������� &7- &eII", new String[] { "", "&4����������, ����������������", "", "&4��������", "&8? &7����: 3x", "&8? &e? &740 J/s" });
    
    ELECTRIFIED_CRUCIBLE = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)14), "&c��������", new String[] { "", "&4��������", "&8? &7����: 1x", "&8? &e? &748 J/s" });
    ELECTRIFIED_CRUCIBLE_2 = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)14), "&c�������� &7- &eII", new String[] { "", "&4��������", "&8? &7����: 2x", "&8? &e? &780 J/s" });
    ELECTRIFIED_CRUCIBLE_3 = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)14), "&c�������� &7- &eIII", new String[] { "", "&4��������", "&8? &7����: 4x", "&8? &e? &7120 J/s" });
    
    CARBON_PRESS = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)15), "&c������", new String[] { "", "&4��������", "&8? &7����: 1x", "&8? &e? &720 J/s" });
    CARBON_PRESS_2 = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)15), "&c������ &7- &eII", new String[] { "", "&4��������", "&8? &7����: 3x", "&8? &e? &750 J/s" });
    CARBON_PRESS_3 = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)15), "&c������ &7- &eIII", new String[] { "", "&4��������", "&8? &7����: 15x", "&8? &e? &7180 J/s" });
    
    BLISTERING_INGOT = new CustomItem(new MaterialData(Material.GOLD_INGOT), "&6�������� &7(33%)", new String[] { "", "&2��������: ��", "&4&o������������" });
    BLISTERING_INGOT_2 = new CustomItem(new MaterialData(Material.GOLD_INGOT), "&6�������� &7(66%)", new String[] { "", "&2��������: ��", "&4&o������������" });
    BLISTERING_INGOT_3 = new CustomItem(new MaterialData(Material.GOLD_INGOT), "&6��������", new String[] { "", "&2��������: ��", "&4&o������������" });
    
    ENERGY_REGULATOR = null;
    DEBUG_FISH = new CustomItem(new MaterialData(Material.RAW_FISH), "&3����������? &e[Debug ����]", new String[] { "", "&e���� &r��������������������������", "&e���� &r��������������", "&eShift + ���� &r��������������������������", "&eShift + ���� &r��������������������" });
    
    NETHER_ICE = null;
    ENRICHED_NETHER_ICE = null;
    NETHER_ICE_COOLANT_CELL = null;
    NETHER_DRILL = new CustomItem(new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)14), "&4��������", new String[] { "", "&r����������������", "", "&4��������", "&8? &7����: 1x", "&8? &e? &7102 J/s", "", "&c&l! &c����! ��������������!", "&c&l! &c���������� GEO �������� ������!" }));
    
    CARGO_MANAGER = null;
    CARGO_NODE = null;
    CARGO_INPUT = null;
    CARGO_OUTPUT = null;
    CARGO_OUTPUT_ADVANCED = null;
    
    AUTO_BREEDER = null;
    
    ORGANIC_FOOD = null;
    ORGANIC_FOOD2 = null;
    ORGANIC_FOOD3 = null;
    ORGANIC_FOOD4 = null;
    ORGANIC_FOOD5 = null;
    ORGANIC_FOOD6 = null;
    ORGANIC_FOOD7 = null;
    ORGANIC_FOOD8 = null;
    
    FERTILIZER = null;
    FERTILIZER2 = null;
    FERTILIZER3 = null;
    FERTILIZER4 = null;
    FERTILIZER5 = null;
    FERTILIZER6 = null;
    FERTILIZER7 = null;
    FERTILIZER8 = null;
    
    ANIMAL_GROWTH_ACCELERATOR = null;
    CROP_GROWTH_ACCELERATOR = null;
    CROP_GROWTH_ACCELERATOR_2 = null;
    
    FOOD_FABRICATOR = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)13), "&c����������", new String[] { "", "&r���� &a��������", "", "&6��������", "&8? &7����: 1x", "&8? &e? &7256 J ������", "&8? &e? &714 J/s" });
    FOOD_FABRICATOR_2 = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)13), "&c���������� &7(&eII&7)", new String[] { "", "&r���� &a��������", "", "&4��������", "&8? &7����: 6x", "&8? &e? &7512 J ������", "&8? &e? &748 J/s" });
    
    FOOD_COMPOSTER = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)13), "&c����������", new String[] { "", "&r���� &a��������", "", "&6��������", "&8? &7����: 1x", "&8? &e? &7256 J ������", "&8? &e? &716 J/s" });
    FOOD_COMPOSTER_2 = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)13), "&c���������� &7(&eII&7)", new String[] { "", "&r���� &a��������", "", "&4��������", "&8? &7����: 10x", "&8? &e? &7512 J ������", "&8? &e? &752 J/s" });
    
    XP_COLLECTOR = null;
    REACTOR_COOLANT_CELL = null;
    
    NUCLEAR_REACTOR = null;
    NETHERSTAR_REACTOR = null;
    REACTOR_ACCESS_PORT = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)9), "&2��������������", new String[] { "", "&r��������������������", "&r����������, ����������", "&r����", "", "&8? &e�������� &a3 �� &e��������������" });
    
    FREEZER = null;
    FREEZER_2 = null;
    
    ELECTRIC_GOLD_PAN = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)12), "&6����������", new String[] { "", "&e��������", "&8? &7����: 1x", "&8? &e? &72 J/s" });
    ELECTRIC_GOLD_PAN_2 = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)12), "&6���������� &7(&eII&7)", new String[] { "", "&e��������", "&8? &7����: 3x", "&8? &e? &74 J/s" });
    ELECTRIC_GOLD_PAN_3 = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)12), "&6���������� &7(&eIII&7)", new String[] { "", "&4��������", "&8? &7����: 10x", "&8? &e? &714 J/s" });
    
    ELECTRIC_DUST_WASHER = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)11), "&3����������", new String[] { "", "&e��������", "&8? &7����: 1x", "&8? &e? &76 J/s" });
    ELECTRIC_DUST_WASHER_2 = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)11), "&3���������� &7(&eII&7)", new String[] { "", "&e��������", "&8? &7����: 2x", "&8? &e? &710 J/s" });
    ELECTRIC_DUST_WASHER_3 = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)11), "&3���������� &7(&eIII&7)", new String[] { "", "&4��������", "&8? &7����: 10x", "&8? &e? &730 J/s" });
    
    ELECTRIC_INGOT_FACTORY = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)14), "&c����������", new String[] { "", "&e��������", "&8? &7����: 1x", "&8? &e? &78 J/s" });
    ELECTRIC_INGOT_FACTORY_2 = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)14), "&c���������� &7(&eII&7)", new String[] { "", "&e��������", "&8? &7����: 2x", "&8? &e? &714 J/s" });
    ELECTRIC_INGOT_FACTORY_3 = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)14), "&c���������� &7(&eIII&7)", new String[] { "", "&4��������", "&8? &7����: 8x", "&8? &e? &740 J/s" });
    
    AUTOMATED_CRAFTING_CHAMBER = new CustomItem(new MaterialData(Material.WORKBENCH), "&6����������", new String[] { "", "&6��������", "&8? &e? &710 J/������" });
    FLUID_PUMP = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)11), "&9������", new String[] { "", "&6��������", "&8? &e? &732 J/������" });
    CHARGING_BENCH = new CustomItem(new MaterialData(Material.WORKBENCH), "&6������", new String[] { "", "&r����������������������", "", "&e��������", "&8? &e? &7128 J ������", "&8? &e? &7��������: &c50%" });
    
    WITHER_ASSEMBLER = new CustomItem(new MaterialData(Material.OBSIDIAN), "&5����������", new String[] { "", "&4��������", "&8? &7����: &b30 ��", "&8? &e? &74096 J ������", "&8? &e? &74096 J/������" });
    
    TRASH_CAN = null;
    
    ELYTRA = new ItemStack(Material.ELYTRA);
    ELYTRA_SCALE = new CustomItem(new ItemStack(Material.FEATHER), "&b��������");
    INFUSED_ELYTRA = new CustomItem(new CustomItem(ELYTRA, "&5������������"), new String[] { "MENDING-1" });
    SOULBOUND_ELYTRA = new CustomItem(ELYTRA, "&c������������");
    
    CHEST_TERMINAL = null;
    CT_IMPORT_BUS = null;
    CT_EXPORT_BUS = null;
    try
    {
      PORTABLE_DUSTBIN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJkNDEwNDJjZTk5MTQ3Y2MzOGNhYzllNDY3NDE1NzZlN2VlNzkxMjgzZTZmYWM4ZDMyOTJjYWUyOTM1ZjFmIn19fQ=="), "&6������������", new String[] { "&r����������������", "", "&e����&7 ������" });
      TRASH_CAN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJkNDEwNDJjZTk5MTQ3Y2MzOGNhYzllNDY3NDE1NzZlN2VlNzkxMjgzZTZmYWM4ZDMyOTJjYWUyOTM1ZjFmIn19fQ=="), "&3������", new String[] { "", "&r����������������������" });
      CAN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTRkYTk3ZjA4MGUzOTViODQyYzRjYzgyYTg0MDgyM2Q0ZGJkOGNhNjg4YTIwNjg1M2U1NzgzZTRiZmRjMDEyIn19fQ=="), "&r����");
      
      STONE_CHUNK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2U4ZjVhZGIxNGQ2YzlmNmI4MTBkMDI3NTQzZjFhOGMxZjQxN2UyZmVkOTkzYzk3YmNkODljNzRmNWUyZTgifX19"), "&6����");
      
      INFUSED_MAGNET = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ=="), "&a��������", new String[] { "", "&r����������������", "&r����������������", "&r��������������", "&r��������������", "", "&7���� &eShift&7 ����������������" });
      MAGNET = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ=="), "&c����");
      ELECTRO_MAGNET = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ=="), "&c������");
      ELECTRIC_MOTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ=="), "&c��������");
      CARGO_MOTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ=="), "&3��������");
      
      BACKPACK_SMALL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&e������", new String[] { "", "&7����: &e9", "&7ID: <ID>", "", "&7&e����&7 ������" });
      BACKPACK_MEDIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&e��������", new String[] { "", "&7����: &e18", "&7ID: <ID>", "", "&7&e����&7 ������" });
      BACKPACK_LARGE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&e������", new String[] { "", "&7����: &e27", "&7ID: <ID>", "", "&7&e����&7 ������" });
      WOVEN_BACKPACK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&e��������", new String[] { "", "&7����: &e36", "&7ID: <ID>", "", "&7&e����&7 ������" });
      GILDED_BACKPACK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&e��������", new String[] { "", "&7����: &e45", "&7ID: <ID>", "", "&7&e����&7 ������" });
      BOUND_BACKPACK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&c������������", new String[] { "", "&7����: &e36", "&7ID: <ID>", "", "&7&e����&7 ������" });
      COOLER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDRjMTU3MjU4NGViNWRlMjI5ZGU5ZjVhNGY3NzlkMGFhY2JhZmZkMzNiY2IzM2ViNDUzNmE2YTJiYzZhMSJ9fX0="), "&b������", new String[] { "&r��������������������", "&r������������������������", "&r��������������������������������", "", "&7����: &e27", "&7ID: <ID>", "", "&7&e����&7 ������" });
      ENDER_BACKPACK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&6��������", new String[] { "&a&o������������������", "", "&e����&7 ������" });
      
      VOIDBAG_SMALL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&4��������", new String[] { "", "&7����: &e9", "&7ID: <ID>", "", "&7&e����&7 ������������������", "&7&e����&7 ������" });
      VOIDBAG_MEDIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&4������", new String[] { "", "&7����: &e18", "&7ID: <ID>", "", "&7&e����&7 ������������������", "&7&e����&7 ������" });
      VOIDBAG_BIG = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&4����������", new String[] { "", "&7����: &e27", "&7ID: <ID>", "", "&7&e����&7 ������������������", "&7&e����&7 ������" });
      VOIDBAG_LARGE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&4��������", new String[] { "", "&7����: &e36", "&7ID: <ID>", "", "&7&e����&7 ������������������", "&7&e����&7 ������" });
      BOUND_VOIDBAG = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&4��������������", new String[] { "", "&7����: &e36", "&7ID: <ID>", "", "&7&e����&7 ������������������", "&7&e����&7 ������" });
      
      COAL_GENERATOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&c����������", new String[] { "", "&6����������", "&8? &e? &764 J ������", "&8? &e? &716 J/s" });
      LAVA_GENERATOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&4����������", new String[] { "", "&6����������", "&8? &e? &7512 J ������", "&8? &e? &720 J/s" });
      COMBUSTION_REACTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&c����������", new String[] { "", "&6����������", "&8? &e? &7256 J ������", "&8? &e? &724 J/s" });
      
      NUCLEAR_REACTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&2��������", new String[] { "", "&r��������!", "&8? &b������������", "&8? &b������������������������", "", "&4����������", "&8? &e? &716384 J ������", "&8? &e? &7500 J/s" });
      NETHERSTAR_REACTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&f��������������", new String[] { "", "&f���� �������� ��������", "&8? &b������������", "&8? &b������������������������", "", "&4����������", "&8? &e? &732768 J ������", "&8? &e? &71024 J/s", "&8? &4��������������������������" });
      
      SMALL_CAPACITOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ=="), "&a��������������", new String[] { "", "&e����������", "&8? &e? &7128 J ������" });
      MEDIUM_CAPACITOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ=="), "&a��������������", new String[] { "", "&6����������", "&8? &e? &7512 J ������" });
      BIG_CAPACITOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ=="), "&a��������������", new String[] { "", "&2����������", "&8? &e? &71024 J ������" });
      LARGE_CAPACITOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ=="), "&a��������������", new String[] { "", "&2����������", "&8? &e? &78192 J ������" });
      CARBONADO_EDGED_CAPACITOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ=="), "&a����������������������", new String[] { "", "&4����������", "&8? &e? &765536 J ������" });
      CHEESE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRmZWJiYzE1ZDFkNGNjNjJiZWRjNWQ3YTJiNmYwZjQ2Y2Q1YjA2OTZhODg0ZGU3NWUyODllMzVjYmI1M2EwIn19fQ=="), "&r����");
      BUTTER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY2YjE5ZjdkNjM1ZDAzNDczODkxZGYzMzAxN2M1NDkzNjMyMDlhOGY2MzI4YTg1NDJjMjEzZDA4NTI1ZSJ9fX0="), "&r����");
      DUCT_TAPE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJmYWFjZWFiNjM4NGZmZjVlZDI0YmI0NGE0YWYyZjU4NGViMTM4MjcyOWVjZDkzYTUzNjlhY2ZkNjY1NCJ9fX0="), "&8����", new String[] { "", "&r������������������������", "&r��������" });
      
      URANIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiMjlhZmE2ZDZkYzkyM2UyZTEzMjRiZjgxOTI3NTBmN2JkYmRkYzY4OTYzMmEyYjZjMThkOWZlN2E1ZSJ9fX0="), "&4��", new String[] { "", "&2��������: ��", "&4&o����������" });
      SMALL_URANIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiMjlhZmE2ZDZkYzkyM2UyZTEzMjRiZjgxOTI3NTBmN2JkYmRkYzY4OTYzMmEyYjZjMThkOWZlN2E1ZSJ9fX0="), "&c��������", new String[] { "", "&e��������: ����", "&4&o����������" });
      TINY_URANIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiMjlhZmE2ZDZkYzkyM2UyZTEzMjRiZjgxOTI3NTBmN2JkYmRkYzY4OTYzMmEyYjZjMThkOWZlN2E1ZSJ9fX0="), "&c������", new String[] { "", "&c��������: ��", "&4&o����������" });
      
      NEPTUNIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVkZWE2YmZkMzdlNDlkZTQzZjE1NGZlNmZjYTYxN2Q0MTI5ZTYxYjk1NzU5YTNkNDlhMTU5MzVhMWMyZGNmMCJ9fX0="), "&a��", new String[] { "", "&2��������: ��", "&4&o����������" });
      PLUTONIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjZjkxYjczODg2NjVhNmQ3YzFiNjAyNmJkYjIzMjJjNmQyNzg5OTdhNDQ0Nzg2NzdjYmNjMTVmNzYxMjRmIn19fQ=="), "&7��", new String[] { "", "&2��������: ��", "&4&o����������" });
      BOOSTED_URANIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgzN2NhMTJmMjIyZjQ3ODcxOTZhMTdiOGFiNjU2OTg1Zjg0MDRjNTA3NjdhZGJjYjZlN2YxNDI1NGZlZSJ9fX0="), "&2������", new String[] { "", "&2��������: ��", "&4&o����������" });
      
      PROGRAMMABLE_ANDROID = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUwM2NiN2VkODQ1ZTdhNTA3ZjU2OWFmYzY0N2M0N2FjNDgzNzcxNDY1YzlhNjc5YTU0NTk0Yzc2YWZiYSJ9fX0="), "&c������������ &7(����)", new String[] { "", "&8? &7����: ��", "&8? &7��������: 1.0x" });
      PROGRAMMABLE_ANDROID_FARMER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlkMzMzNTdlODQxODgyM2JmNzgzZGU5MmRlODAyOTFiNGViZDM5MmFlYzg3MDY2OThlMDY4OTZkNDk4ZjYifX19"), "&c������������ &7(����)", new String[] { "", "&8? &7����: ����", "&8? &7��������: 1.0x" });
      PROGRAMMABLE_ANDROID_MINER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTYzOGEyODU0MWFiM2FlMGE3MjNkNTU3ODczOGUwODc1ODM4OGVjNGMzMzI0N2JkNGNhMTM0ODJhZWYzMzQifX19"), "��c������������ &7(����)", new String[] { "", "��8? ��7����: ����", "��8? ��7��������: 1.0x" });
      PROGRAMMABLE_ANDROID_WOODCUTTER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDMyYTgxNDUxMDE0MjIwNTE2OWExYWQzMmYwYTc0NWYxOGU5Y2I2YzY2ZWU2NGVjYTJlNjViYWJkZWY5ZmYifX19"), "&c������������ &7(������)", new String[] { "", "&8? &7����: ����", "&8? &7��������: 1.0x" });
      PROGRAMMABLE_ANDROID_BUTCHER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NzJkZjBhZDlhM2JlODhmMmU1ZDVkNDIyZDAyYjExNmQ2NGQ4ZGYxNDc1ZWQzMmU1NDZhZmM4NGIzMSJ9fX0="), "&c������������ &7(����)", new String[] { "", "&8? &7����: ����", "&8? &7����: 4", "&8? &7��������: 1.0x" });
      PROGRAMMABLE_ANDROID_FISHERMAN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ1ZTg3MzNhNzMxMTQzMzNiOThiMzYwMTc1MTI0MTcyMmY0NzEzZTFhMWE1ZDM2ZmJiMTMyNDkzZjFjNyJ9fX0="), "&c������������ &7(����)", new String[] { "", "&8? &7����: ����", "&8? &7��������: 10%", "&8? &7��������: 1.0x" });
      
      PROGRAMMABLE_ANDROID_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUwM2NiN2VkODQ1ZTdhNTA3ZjU2OWFmYzY0N2M0N2FjNDgzNzcxNDY1YzlhNjc5YTU0NTk0Yzc2YWZiYSJ9fX0="), "&c���������������� &7(����)", new String[] { "", "&8? &7����: ��", "&8? &7��������: 1.5x" });
      PROGRAMMABLE_ANDROID_2_FISHERMAN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ1ZTg3MzNhNzMxMTQzMzNiOThiMzYwMTc1MTI0MTcyMmY0NzEzZTFhMWE1ZDM2ZmJiMTMyNDkzZjFjNyJ9fX0="), "&c���������������� &7(����)", new String[] { "", "&8? &7����: ����", "&8? &7��������: 20%", "&8? &7��������: 1.5x" });
      PROGRAMMABLE_ANDROID_2_FARMER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlkMzMzNTdlODQxODgyM2JmNzgzZGU5MmRlODAyOTFiNGViZDM5MmFlYzg3MDY2OThlMDY4OTZkNDk4ZjYifX19"), "&c���������������� &7(����)", new String[] { "", "&8? &7����: ����", "&8? &7��������: 1.5x", "&8? &7���������� ExoticGarden ��������" });
      PROGRAMMABLE_ANDROID_2_BUTCHER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NzJkZjBhZDlhM2JlODhmMmU1ZDVkNDIyZDAyYjExNmQ2NGQ4ZGYxNDc1ZWQzMmU1NDZhZmM4NGIzMSJ9fX0="), "&c���������������� &7(����)", new String[] { "", "&8? &7����: ����", "&8? &7������: 8", "&8? &7��������: 1.5x" });
      
      PROGRAMMABLE_ANDROID_3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUwM2NiN2VkODQ1ZTdhNTA3ZjU2OWFmYzY0N2M0N2FjNDgzNzcxNDY1YzlhNjc5YTU0NTk0Yzc2YWZiYSJ9fX0="), "&e������������������ &7(����)", new String[] { "", "&8? &7����: ��", "&8? &7��������: 3.0x" });
      PROGRAMMABLE_ANDROID_3_FISHERMAN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ1ZTg3MzNhNzMxMTQzMzNiOThiMzYwMTc1MTI0MTcyMmY0NzEzZTFhMWE1ZDM2ZmJiMTMyNDkzZjFjNyJ9fX0="), "&e������������������ &7(����)", new String[] { "", "&8? &7����: ����", "&8? &7��������: 30%", "&8? &7��������: 8.0x" });
      PROGRAMMABLE_ANDROID_3_BUTCHER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NzJkZjBhZDlhM2JlODhmMmU1ZDVkNDIyZDAyYjExNmQ2NGQ4ZGYxNDc1ZWQzMmU1NDZhZmM4NGIzMSJ9fX0="), "&e������������������ &7(����)", new String[] { "", "&8? &7����: ����", "&8? &7������: 20", "&8? &7��������: 8.0x" });
      
      GPS_TRANSMITTER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&bGPS ������", new String[] { "", "&8? &e? &716 J ������", "&8? &e? &72 J/s" });
      GPS_TRANSMITTER_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&c���� GPS ������", new String[] { "", "&8? &e? &764 J ������", "&8? &e? &76 J/s" });
      GPS_TRANSMITTER_3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&4�������� GPS ������", new String[] { "", "&8? &e? &7256 J ������", "&8? &e? &722 J/s" });
      GPS_TRANSMITTER_4 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&e���� GPS ������", new String[] { "", "&8? &e? &71024 J ������", "&8? &e? &792 J/s" });
      
      GPS_CONTROL_PANEL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGRjZmJhNThmYWYxZjY0ODQ3ODg0MTExODIyYjY0YWZhMjFkN2ZjNjJkNDQ4MWYxNGYzZjNiY2I2MzMwIn19fQ=="), "&bGPS ��������", new String[] { "", "&r������������������", "&r������������������" });
      GPS_EMERGENCY_TRANSMITTER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&cGPS ����������", new String[] { "", "&r��������������", "&r��������������������������", "&r������������������������" });
      
      GPS_GEO_SCANNER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkOGNmZWIzODdhNTZlM2U1YmNmODUzNDVkNmE0MTdiMjQyMjkzODg3ZGIzY2UzYmE5MWZhNDA5YjI1NGI4NiJ9fX0="), "&bGPS GEO����������", new String[] { "", "&r������������������������", "&r���� &8��" });
      OIL_PUMP = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZlMWEwNDBhNDI1ZTMxYTQ2ZDRmOWE5Yjk4MDZmYTJmMGM0N2VlODQ3MTFjYzE5MzJmZDhhYjMyYjJkMDM4In19fQ=="), "&r����", new String[] { "", "&7��������������������", "", "&c&l! &cMake sure to Geo-Scan the Chunk first" });
      BUCKET_OF_OIL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlMDRiNDFkMTllYzc5MjdmOTgyYTYzYTk0YTNkNzlmNzhlY2VjMzMzNjMwNTFmZGUwODMxYmZhYmRiZCJ9fX0="), "&r����");
      BUCKET_OF_FUEL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg0ZGRjYTc2NjcyNWI4Yjk3NDEzZjI1OWMzZjc2NjgwNzBmNmFlNTU0ODNhOTBjOGU1NTI1Mzk0ZjljMDk5In19fQ=="), "&r������");
      
      NETHER_ICE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2NlMmRhZDliYWY3ZWFiYTdlODBkNGQwZjlmYWMwYWFiMDFhNzZiMTJmYjcxYzNkMmFmMmExNmZkZDRjNzM4MyJ9fX0="), "&e������", new String[] { "", "&e��������: ����", "&4&o����������" });
      ENRICHED_NETHER_ICE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M4MThhYTEzYWFiYzcyOTQ4MzhkMjFjYWFjMDU3ZTk3YmQ4Yzg5NjQxYTBjMGY4YTU1NDQyZmY0ZTI3In19fQ=="), "&e����������", new String[] { "", "&2��������: ����", "&4&o����������" });
      
      LAVA_CRYSTAL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNhZDhlZTg0OWVkZjA0ZWQ5YTI2Y2EzMzQxZjYwMzNiZDc2ZGNjNDIzMWVkMWVhNjNiNzU2NTc1MWIyN2FjIn19fQ=="), "&4��������");
      ANDROID_MEMORY_CORE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc4ZjJiN2U1ZTc1NjM5ZWE3ZmI3OTZjMzVkMzY0YzRkZjI4YjQyNDNlNjZiNzYyNzdhYWRjZDYyNjEzMzcifX19"), "&b��������������");
      
      CARBON = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGIzYTA5NWI2YjgxZTZiOTg1M2ExOTMyNGVlZGYwYmI5MzQ5NDE3MjU4ZGQxNzNiOGVmZjg3YTA4N2FhIn19fQ=="), "&e��");
      COMPRESSED_CARBON = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIxZDQ5NTE2NTc0OGQzMTE2Zjk5ZDZiNWJkNWQ0MmViOGJhNTkyYmNkZmFkMzdmZDk1ZjliNmMwNGEzYiJ9fX0="), "&c������");
      CARBON_CHUNK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIxZDQ5NTE2NTc0OGQzMTE2Zjk5ZDZiNWJkNWQ0MmViOGJhNTkyYmNkZmFkMzdmZDk1ZjliNmMwNGEzYiJ9fX0="), "&4����");
      
      CARBONADO = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTJmNGIxNTc3ZjUxNjBjNjg5MzE3MjU3MWM0YTcxZDhiMzIxY2RjZWFhMDMyYzZlMGUzYjYwZTBiMzI4ZmEifX19"), "&b&l��������", new String[] { "", "&7&o\"��������\"" });
      RAW_CARBONADO = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWI0OWU2ZWMxMDc3MWU4OTkyMjVhZWE3M2NkOGNmMDM2ODRmNDExZDE0MTVjNzMyM2M5M2NiOTQ3NjIzMCJ9fX0="), "&b������������");
      
      ENERGY_REGULATOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc4ZjJiN2U1ZTc1NjM5ZWE3ZmI3OTZjMzVkMzY0YzRkZjI4YjQyNDNlNjZiNzYyNzdhYWRjZDYyNjEzMzcifX19"), "&6����������", new String[] { "", "&r������������������" });
      
      CARGO_MANAGER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTUxMGJjODUzNjJhMTMwYTZmZjlkOTFmZjExZDZmYTQ2ZDdkMTkxMmEzNDMxZjc1MTU1OGVmM2M0ZDljMiJ9fX0="), "&6����������", new String[] { "", "&r����������������������" });
      CARGO_NODE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDdiN2VmNmZkNzg2NDg2NWMzMWMxZGM4N2JlZDI0YWI1OTczNTc5ZjVjNjYzOGZlY2I4ZGVkZWI0NDNmZjAifX19"), "&7�������� &c(������)", new String[] { "", "&r������������" });
      CARGO_INPUT = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZkMWMxYTY5YTNkZTlmZWM5NjJhNzdiZjNiMmUzNzZkZDI1Yzg3M2EzZDhmMTRmMWRkMzQ1ZGFlNGM0In19fQ=="), "&7�������� &c(����)", new String[] { "", "&r������������" });
      CARGO_OUTPUT = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTViMjFmZDQ4MGMxYzQzYmYzYjlmODQyYzg2OWJkYzNiYzVhY2MyNTk5YmYyZWI2YjhhMWM5NWRjZTk3OGYifX19"), "&6�������� &c(����)", new String[] { "", "&r������������" });
      CARGO_OUTPUT_ADVANCED = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTViMjFmZDQ4MGMxYzQzYmYzYjlmODQyYzg2OWJkYzNiYzVhY2MyNTk5YmYyZWI2YjhhMWM5NWRjZTk3OGYifX19"), "&6������������ &c(����)", new String[] { "", "&r������������" });
      
      AUTO_BREEDER = new CustomItem(new MaterialData(Material.HAY_BLOCK), "&e����������", new String[] { "", "&r���� &a��������", "", "&4��������", "&8? &e? &71024 J ������", "&8? &e? &760 J/������" });
      ANIMAL_GROWTH_ACCELERATOR = new CustomItem(new MaterialData(Material.HAY_BLOCK), "&b��������������", new String[] { "", "&r���� &a��������", "", "&4��������", "&8? &e? &71024 J Buffer", "&8? &e? &728 J/s" });
      CROP_GROWTH_ACCELERATOR = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)5), "&a��������������", new String[] { "", "&r���� &a��������", "", "&4��������", "&8? &7����: 7x7", "&8? &7����: &a3/��", "&8? &e? &71024 J ������", "&8? &e? &750 J/s" });
      CROP_GROWTH_ACCELERATOR_2 = new CustomItem(new MaterialData(Material.STAINED_CLAY, (byte)5), "&a�������������� &7(&eII&7)", new String[] { "", "&r���� &a��������", "", "&4��������", "&8? &7����: 9x9", "&8? &7����: &a4/��", "&8? &e? &71024 J ������", "&8? &e? &760 J/s" });
      XP_COLLECTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTc2MmExNWIwNDY5MmEyZTRiM2ZiMzY2M2JkNGI3ODQzNGRjZTE3MzJiOGViMWM3YTlmN2MwZmJmNmYifX19"), "&a����������", new String[] { "", "&r������������������������", "", "&4��������", "&8? &e? &71024 J ������", "&8? &e? &720 J/s" });
      
      ORGANIC_FOOD = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9X" });
      ORGANIC_FOOD2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Wheat" });
      ORGANIC_FOOD3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Carrots" });
      ORGANIC_FOOD4 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Potatoes" });
      ORGANIC_FOOD5 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Seeds" });
      ORGANIC_FOOD6 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Beetroot" });
      ORGANIC_FOOD7 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Melon" });
      ORGANIC_FOOD8 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Apple" });
      
      FERTILIZER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9X" });
      FERTILIZER2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Wheat" });
      FERTILIZER3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Carrots" });
      FERTILIZER4 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Potatoes" });
      FERTILIZER5 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Seeds" });
      FERTILIZER6 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Beetroot" });
      FERTILIZER7 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7Content: &9Melon" });
      FERTILIZER8 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&a��������", new String[] { "&7����: &9����" });
      
      NETHER_ICE_COOLANT_CELL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQzY2Q0MTI1NTVmODk3MDE2MjEzZTVkNmM3NDMxYjQ0OGI5ZTU2NDRlMWIxOWVjNTFiNTMxNmYzNTg0MGUwIn19fQ=="), "&6������������");
      REACTOR_COOLANT_CELL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGU0MDczYmU0MGNiM2RlYjMxMGEwYmU5NTliNGNhYzY4ZTgyNTM3MjcyOGZhZmI2YzI5NzNlNGU3YzMzIn19fQ=="), "&b������������");
      
      CHEST_TERMINAL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E0NGZmM2E1ZjQ5YzY5Y2FiNjc2YmFkOGQ5OGEwNjNmYTc4Y2ZhNjE5MTZmZGVmM2UyNjc1NTdmZWMxODI4MyJ9fX0="), "&3CT��������", new String[] { "&7��������������������", "&7����������, ������������������", "&7��������������������", "&7������������������������" });
      CT_IMPORT_BUS = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTEzZGIyZTdlNzJlYTQ0MzJlZWZiZDZlNThhODVlYWEyNDIzZjgzZTY0MmNhNDFhYmM2YTkzMTc3NTdiODg5In19fQ=="), "&3CT��������", new String[] { "&7��������������������", "&7��������, ����������������", "&7����������", "&7���� CT ���� ����" });
      CT_EXPORT_BUS = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTEzZGIyZTdlNzJlYTQ0MzJlZWZiZDZlNThhODVlYWEyNDIzZjgzZTY0MmNhNDFhYmM2YTkzMTc3NTdiODg5In19fQ=="), "&3CT��������", new String[] { "&7��������������������", "&7��������, ������ CT ��������", "&7����������", "&7������������������" });
      
      FREEZER = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)3), "&b����", new String[] { "", "&6��������", "&8? &7����: 1��", "&8? &e? &7256 J ����������", "&8? &e? &718 J/����" });
      FREEZER_2 = new CustomItem(new MaterialData(Material.STAINED_GLASS, (byte)3), "&b���� &7(&eII&7)", new String[] { "", "&4��������", "&8? &7����: 2��", "&8? &e? &7256 J ������", "&8? &e? &730 J/����" });
      
      BATTERY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmUyZGRhNmVmNjE4NWQ0ZGQ2ZWE4Njg0ZTk3ZDM5YmE4YWIwMzdlMjVmNzVjZGVhNmJkMjlkZjhlYjM0ZWUifX19"), "&6����");
      
      HEATING_COIL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2UzYmM0ODkzYmE0MWEzZjczZWUyODE3NGNkZjRmZWY2YjE0NWU0MWZlNmM4MmNiN2JlOGQ4ZTk3NzFhNSJ9fX0="), "&c��������");
      COOLING_UNIT = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU0YmFkODZjOTlkZjc4MGM4ODlhMTA5OGY3NzY0OGVhZDczODVjYzFkZGIwOTNkYTVhN2Q4YzRjMmFlNTRkIn19fQ=="), "&b��������");
      POWER_CRYSTAL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNjMWIwMzZiNmUwMzUxN2IyODVhODExYmQ4NWU3M2Y1YWJmZGFjYzFkZGY5MGRmZjk2MmUxODA5MzRlMyJ9fX0="), "&c&l��������");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
