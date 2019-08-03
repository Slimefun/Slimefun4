package me.mrCookieSlime.Slimefun.Lists;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomArmor;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomPotion;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.Christmas;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;

public class SlimefunItems {
	
	/*		 Items 		*/
	public static ItemStack PORTABLE_CRAFTER = null;
	public static ItemStack PORTABLE_DUSTBIN = null;
	public static ItemStack ENDER_BACKPACK = null;
	public static ItemStack MAGIC_EYE_OF_ENDER = new CustomItem(Material.ENDER_EYE, "&6&lMagic Eye of Ender", "&4&lRequires full Ender Armor", "", "&7&eRight Click&7 to shoot an Ender Pearl");
	public static ItemStack BROKEN_SPAWNER = new CustomItem(Material.SPAWNER, "&cBroken Spawner", "&7Type: &b<Type>", "", "&cFractured, must be repaired in an Ancient Altar");
	public static ItemStack REPAIRED_SPAWNER = new CustomItem(Material.SPAWNER, "&bReinforced Spawner", "&7Type: &b<Type>");
	public static ItemStack INFERNAL_BONEMEAL = new CustomItem(Material.BONE_MEAL, "&4Infernal Bonemeal", "", "&cSpeeds up the Growth of", "&cNether Warts as well");
	
	/*		 Gadgets 		*/
	public static ItemStack GOLD_PAN = new CustomItem(Material.BOWL, "&6Gold Pan", "&a&oCan get you all kinds of Goodies...", "", "&7&eRight Click&7 to pan various Stuff out of Gravel");
	public static ItemStack PARACHUTE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&r&lParachute", "", "&7Hold &eShift&7 to use"), Color.WHITE);
	public static ItemStack GRAPPLING_HOOK = new CustomItem(Material.LEAD, "&6Grappling Hook", "", "&7&eRight Click&7 to use");
	public static ItemStack SOLAR_HELMET = new CustomItem(Material.IRON_HELMET, "&bSolar Helmet", "", "&a&oCharges held Items and Armor");
	public static ItemStack CLOTH = new CustomItem(Material.PAPER, "&bCloth");
	public static ItemStack CAN = null;
	public static ItemStack NIGHT_VISION_GOGGLES = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&aNight Vision Goggles", "", "&9+ Night Vision"), Color.BLACK);
	public static ItemStack FARMER_SHOES = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&eFarmer Shoes", "", "&6&oPrevents you from trampling your Crops"), Color.YELLOW);
	public static ItemStack INFUSED_MAGNET = null;
	public static ItemStack FLASK_OF_KNOWLEDGE = new CustomItem(Material.GLASS_BOTTLE, "&cFlask of Knowledge", "", "&rAllows you to store some of", "&ryour Experience in a Bottle", "&7Cost: &a1 Level");
	public static ItemStack RAG = new CustomItem(Material.PAPER, "&cRag", "", "&aLevel I - Medical Supply", "", "&rRestores 2 Hearts", "&rExtinguishes Fire", "", "&7&eRight Click&7 to use");
	public static ItemStack BANDAGE = new CustomItem(Material.PAPER, "&cBandage", "", "&aLevel II - Medical Supply", "", "&rRestores 4 Hearts", "&rExtinguishes Fire", "", "&7&eRight Click&7 to use");
	public static ItemStack SPLINT = new CustomItem(Material.STICK, "&cSplint", "", "&aLevel I - Medical Supply", "", "&rRestores 2 Hearts", "", "&7&eRight Click&7 to use");
	public static ItemStack VITAMINS = new CustomItem(Material.NETHER_WART, "&cVitamins", "", "&aLevel III - Medical Supply", "", "&rRestores 4 Hearts", "&rExtinguishes Fire", "&rCures Poison/Wither/Radiation", "", "&7&eRight Click&7 to use");
	public static ItemStack MEDICINE = new CustomPotion("&cMedicine", Color.RED, new PotionEffect(PotionEffectType.HEAL, 0, 0), "", "&aLevel III - Medical Supply", "", "&rRestores 4 Hearts", "&rExtinguishes Fire", "&rCures Poison/Wither/Radiation");
	
	/*		Backpacks		*/
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

	/*		 Jetpacks		*/
	public static ItemStack DURALUMIN_JETPACK = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&9Electric Jetpack &7- &eI", "", "&8\u21E8 &7Material: &bDuralumin", "&c&o&8\u21E8 &e\u26A1 &70 / 20 J", "&8\u21E8 &7Thrust: &c0.35", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack SOLDER_JETPACK = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&9Electric Jetpack &7- &eII", "", "&8\u21E8 &7Material: &bSolder", "&c&o&8\u21E8 &e\u26A1 &70 / 30 J", "&8\u21E8 &7Thrust: &c0.4", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack BILLON_JETPACK = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&9Electric Jetpack &7- &eIII", "", "&8\u21E8 &7Material: &bBillon", "&c&o&8\u21E8 &e\u26A1 &70 / 45 J", "&8\u21E8 &7Thrust: &c0.45", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack STEEL_JETPACK = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&9Electric Jetpack &7- &eIV", "", "&8\u21E8 &7Material: &bSteel", "&c&o&8\u21E8 &e\u26A1 &70 / 60 J", "&8\u21E8 &7Thrust: &c0.5", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack DAMASCUS_STEEL_JETPACK = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&9Electric Jetpack &7- &eV", "", "&8\u21E8 &7Material: &bDamascus Steel", "&c&o&8\u21E8 &e\u26A1 &70 / 75 J", "&8\u21E8 &7Thrust: &c0.55", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack REINFORCED_ALLOY_JETPACK = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&9Electric Jetpack &7- &eVI", "", "&8\u21E8 &7Material: &bReinforced Alloy", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J", "&8\u21E8 &7Thrust: &c0.6", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack CARBONADO_JETPACK = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&9Electric Jetpack &7- &eVII", "", "&8\u21E8 &7Material: &bCarbonado", "&c&o&8\u21E8 &e\u26A1 &70 / 150 J", "&8\u21E8 &7Thrust: &c0.7", "", "&7Hold &eShift&7 to use"), Color.BLACK);
	public static ItemStack ARMORED_JETPACK = new CustomItem(Material.IRON_CHESTPLATE, "&9Armored Jetpack", "&8\u21E8 &7Material: &bSteel", "", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "&8\u21E8 &7Thrust: &c0.45", "", "&7Hold &eShift&7 to use");
	
	/*		 Jetboots		*/
	public static ItemStack DURALUMIN_JETBOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&9Jet Boots &7- &eI", "", "&8\u21E8 &7Material: &bDuralumin", "&c&o&8\u21E8 &e\u26A1 &70 / 20 J", "&8\u21E8 &7Speed: &a0.35", "&8\u21E8 &7Accuracy: &c50%", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack SOLDER_JETBOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&9Jet Boots &7- &eII", "", "&8\u21E8 &7Material: &bSolder", "&c&o&8\u21E8 &e\u26A1 &70 / 30 J", "&8\u21E8 &7Speed: &a0.4", "&8\u21E8 &7Accuracy: &660%", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack BILLON_JETBOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&9Jet Boots &7- &eIII", "", "&8\u21E8 &7Material: &bBillon", "&c&o&8\u21E8 &e\u26A1 &70 / 40 J", "&8\u21E8 &7Speed: &a0.45", "&8\u21E8 &7Accuracy: &665%", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack STEEL_JETBOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&9Jet Boots &7- &eIV", "", "&8\u21E8 &7Material: &bSteel", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "&8\u21E8 &7Speed: &a0.5", "&8\u21E8 &7Accuracy: &e70%", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack DAMASCUS_STEEL_JETBOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&9Jet Boots &7- &eV", "", "&8\u21E8 &7Material: &bDamascus Steel", "&c&o&8\u21E8 &e\u26A1 &70 / 75 J", "&8\u21E8 &7Speed: &a0.55", "&8\u21E8 &7Accuracy: &a75%", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack REINFORCED_ALLOY_JETBOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&9Jet Boots &7- &eVI", "", "&8\u21E8 &7Material: &bReinforced Alloy", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J", "&8\u21E8 &7Speed: &a0.6", "&8\u21E8 &7Accuracy: &c80%", "", "&7Hold &eShift&7 to use"), Color.SILVER);
	public static ItemStack CARBONADO_JETBOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&9Jet Boots &7- &eVII", "", "&8\u21E8 &7Material: &bCarbonado", "&c&o&8\u21E8 &e\u26A1 &70 / 125 J", "&8\u21E8 &7Speed: &a0.7", "&8\u21E8 &7Accuracy: &c99.9%", "", "&7Hold &eShift&7 to use"), Color.BLACK);
	public static ItemStack ARMORED_JETBOOTS = new CustomItem(Material.IRON_BOOTS, "&9Armored Jet Boots", "", "&8\u21E8 &7Material: &bSteel", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "&8\u21E8 &7Speed: &a0.45", "&8\u21E8 &7Accuracy: &e70%", "", "&7Hold &eShift&7 to use");
	
	/*		 Multi Tools		*/
	public static ItemStack DURALUMIN_MULTI_TOOL = new CustomItem(Material.SHEARS, "&9Multi Tool &7- &eI", "", "&8\u21E8 &7Material: &bDuralumin", "&c&o&8\u21E8 &e\u26A1 &70 / 20 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
	public static ItemStack SOLDER_MULTI_TOOL = new CustomItem(Material.SHEARS, "&9Multi Tool &7- &eII", "", "&8\u21E8 &7Material: &bSolder", "&c&o&8\u21E8 &e\u26A1 &70 / 30 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
	public static ItemStack BILLON_MULTI_TOOL = new CustomItem(Material.SHEARS, "&9Multi Tool &7- &eIII", "", "&8\u21E8 &7Material: &bBillon", "&c&o&8\u21E8 &e\u26A1 &70 / 40 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
	public static ItemStack STEEL_MULTI_TOOL = new CustomItem(Material.SHEARS, "&9Multi Tool &7- &eIV", "", "&8\u21E8 &7Material: &bSteel", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
	public static ItemStack DAMASCUS_STEEL_MULTI_TOOL = new CustomItem(Material.SHEARS, "&9Multi Tool &7- &eV", "", "&8\u21E8 &7Material: &bDamascus Steel", "&c&o&8\u21E8 &e\u26A1 &70 / 60 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
	public static ItemStack REINFORCED_ALLOY_MULTI_TOOL = new CustomItem(Material.SHEARS, "&9Multi Tool &7- &eVI", "", "&8\u21E8 &7Material: &bReinforced Alloy", "&c&o&8\u21E8 &e\u26A1 &70 / 75 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
	public static ItemStack CARBONADO_MULTI_TOOL = new CustomItem(Material.SHEARS, "&9Multi Tool &7- &eVII", "", "&8\u21E8 &7Material: &bCarbonado", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
	
	/*		 Food 		*/
	public static ItemStack FORTUNE_COOKIE = new CustomItem(Material.COOKIE, "&6Fortune Cookie", "", "&a&oTells you stuff about your Future :o");
	public static ItemStack BEEF_JERKY = new CustomItem(Material.COOKED_BEEF, "&6Beef Jerky", "", "&a&oSaturating");
	public static ItemStack MAGIC_SUGAR = new CustomItem(Material.SUGAR, "&6Magic Sugar", "", "&a&oFeel the Power of Hermes!");
	public static ItemStack MONSTER_JERKY = new CustomItem(Material.ROTTEN_FLESH, "&6Monster Jerky", "", "&a&oNo longer hungry");
	public static ItemStack APPLE_JUICE = new CustomPotion("&cApple Juice", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&oRestores &b&o" + "3.0" + " &7&oHunger");
	public static ItemStack MELON_JUICE = new CustomPotion("&cMelon Juice", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&oRestores &b&o" + "3.0" + " &7&oHunger");
	public static ItemStack CARROT_JUICE = new CustomPotion("&6Carrot Juice", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&oRestores &b&o" + "3.0" + " &7&oHunger");
	public static ItemStack PUMPKIN_JUICE = new CustomPotion("&6Pumpkin Juice", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&oRestores &b&o" + "3.0" + " &7&oHunger");
	public static ItemStack GOLDEN_APPLE_JUICE = new CustomPotion("&bGolden Apple Juice", Color.YELLOW, new PotionEffect(PotionEffectType.ABSORPTION, 20 * 20, 0));
	
	/*		Christmas		*/
	public static ItemStack CHRISTMAS_MILK = new CustomPotion("&6Glass of Milk", Color.WHITE, new PotionEffect(PotionEffectType.SATURATION, 5, 0), "", "&7&oRestores &b&o" + "2.5" + " &7&oHunger");
	public static ItemStack CHRISTMAS_CHOCOLATE_MILK = new CustomPotion("&6Chocolate Milk", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 12, 0), "", "&7&oRestores &b&o" + "6.0" + " &7&oHunger");
	public static ItemStack CHRISTMAS_EGG_NOG = new CustomPotion("&aEgg Nog", Color.GRAY, new PotionEffect(PotionEffectType.SATURATION, 7, 0), "", "&7&oRestores &b&o" + "3.5" + " &7&oHunger");
	public static ItemStack CHRISTMAS_APPLE_CIDER = new CustomPotion("&cApple Cider", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 14, 0), "", "&7&oRestores &b&o" + "7.0" + " &7&oHunger");
	public static ItemStack CHRISTMAS_COOKIE = new CustomItem(Material.COOKIE, Christmas.color("Christmas Cookie"));
	public static ItemStack CHRISTMAS_FRUIT_CAKE = new CustomItem(Material.PUMPKIN_PIE, Christmas.color("Fruit Cake"));
	public static ItemStack CHRISTMAS_APPLE_PIE = new CustomItem(Material.PUMPKIN_PIE, "&rApple Pie");
	public static ItemStack CHRISTMAS_HOT_CHOCOLATE = new CustomPotion("&6Hot Chocolate", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 14, 0), "", "&7&oRestores &b&o" + "7.0" + " &7&oHunger");
	public static ItemStack CHRISTMAS_CAKE = new CustomItem(Material.PUMPKIN_PIE, Christmas.color("Christmas Cake"));
	public static ItemStack CHRISTMAS_CARAMEL = new CustomItem(Material.BRICKS, "&6Caramel");
	public static ItemStack CHRISTMAS_CARAMEL_APPLE = new CustomItem(Material.APPLE, "&6Caramel Apple");
	public static ItemStack CHRISTMAS_CHOCOLATE_APPLE = new CustomItem(Material.APPLE, "&6Chocolate Apple");
	public static ItemStack CHRISTMAS_PRESENT = new CustomItem(Material.CHEST, Christmas.color("Christmas Present"), "&7From: &emrCookieSlime", "&7To: &eYou", "", "&eRight Click&7 to open");
	
	/*		Easter			*/
	public static ItemStack EASTER_EGG = new CustomItem(Material.EGG, "&rEaster Egg", "&bSurprise! Surprise!");
	public static ItemStack EASTER_CARROT_PIE = new CustomItem(Material.PUMPKIN_PIE, "&6Carrot Pie");
	
	/*		 Weapons 		*/
	public static ItemStack GRANDMAS_WALKING_STICK = new CustomItem(Material.STICK, "&7Grandmas Walking Stick", 0, new String[0], new String[] {"KNOCKBACK-2"});
	public static ItemStack GRANDPAS_WALKING_STICK = new CustomItem(Material.STICK, "&7Grandpas Walking Stick", 0, new String[0], new String[] {"KNOCKBACK-5"});
	public static ItemStack SWORD_OF_BEHEADING = new CustomItem(Material.IRON_SWORD, "&6Sword of Beheading", "&7Beheading II", "", "&rHas a chance to behead Mobs", "&r(even a higher chance for Wither Skeletons)");
	public static ItemStack BLADE_OF_VAMPIRES = new CustomItem(Material.GOLDEN_SWORD, "&cBlade of Vampires", 0, new String[] {"&7Life Steal I", "", "&rEverytime you attack something", "&ryou have a 45% chance to", "&rrecover 2 Hearts of your Health"}, new String[] {"FIRE_ASPECT-2", "DURABILITY-4", "DAMAGE_ALL-2"});
	public static ItemStack SEISMIC_AXE = new CustomItem(Material.IRON_AXE, "&aSeismic Axe", "", "&7&oA portable Earthquake...", "", "&7&eRight Click&7 to use");
	
	/*		Bows		*/
	public static ItemStack EXPLOSIVE_BOW = new CustomItem(Material.BOW, "&cExplosive Bow", "&rAny Arrows fired using this Bow", "&rwill launch hit enemys into the air");
	public static ItemStack ICY_BOW = new CustomItem(Material.BOW, "&bIcy Bow", "&rAny Arrows fired using this Bow", "&rwill prevent hit enemys from moving", "&rfor 2 seconds");
	
	/*		 Tools		*/
	public static ItemStack AUTO_SMELT_PICKAXE = new CustomItem(Material.DIAMOND_PICKAXE, "&6Smelter's Pickaxe", "&c&lAuto-Smelting", "", "&9Works with Fortune");
	public static ItemStack LUMBER_AXE = new CustomItem(Material.DIAMOND_AXE, "&6Lumber Axe", "&a&oCuts down the whole Tree...");
	public static ItemStack PICKAXE_OF_CONTAINMENT = new CustomItem(Material.IRON_PICKAXE, "&cPickaxe of Containment", "", "&9Can pickup Spawners");
	public static ItemStack HERCULES_PICKAXE = new CustomItem(Material.IRON_PICKAXE, "&9Hercules' Pickaxe", 0, new String[] {"", "&rSo powerful that it", "&rcrushes all mined Ores", "&rinto Dust..."}, new String[] {"DURABILITY-2", "DIG_SPEED-4"});
	public static ItemStack EXPLOSIVE_PICKAXE = new CustomItem(Material.DIAMOND_PICKAXE, "&eExplosive Pickaxe", "", "&rAllows you to mine a good bit", "&rof Blocks at once...", "", "&9Works with Fortune");
	public static ItemStack PICKAXE_OF_THE_SEEKER = new CustomItem(Material.DIAMOND_PICKAXE, "&aPickaxe of the Seeker", "&rWill always point you to the nearest Ore", "&rbut might get damaged when doing it", "", "&7&eRight Click&7 to be pointed to the nearest Ore");
	public static ItemStack COBALT_PICKAXE = new CustomItem(Material.IRON_PICKAXE, "&9Cobalt Pickaxe", 0, new String[0], new String[] {"DURABILITY-3", "DIG_SPEED-6"});
	public static ItemStack PICKAXE_OF_VEIN_MINING = new CustomItem(Material.DIAMOND_PICKAXE, "&ePickaxe of Vein Mining", "", "&rThis Pickaxe will dig out", "&rwhole Veins of Ores...");
	
	/*		 Armor 		*/
	public static ItemStack GLOWSTONE_HELMET = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&e&lGlowstone Helmet", "", "&a&oShining like the sun!", "", "&9+ Night Vision"), Color.YELLOW);
	public static ItemStack GLOWSTONE_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&e&lGlowstone Chestplate", "", "&a&oShining like the sun!", "", "&9+ Night Vision"), Color.YELLOW);
	public static ItemStack GLOWSTONE_LEGGINGS = new CustomArmor(new CustomItem(Material.LEATHER_LEGGINGS, "&e&lGlowstone Leggings", "", "&a&oShining like the sun!", "", "&9+ Night Vision"), Color.YELLOW);
	public static ItemStack GLOWSTONE_BOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&e&lGlowstone Boots", "", "&a&oShining like the sun!", "", "&9+ Night Vision"), Color.YELLOW);
	public static ItemStack ENDER_HELMET = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&5&lEnder Helmet", "", "&a&oSometimes its here, sometimes there!"), Color.fromRGB(28, 25, 112));
	public static ItemStack ENDER_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&5&lEnder Chestplate", "", "&a&oSometimes its here, sometimes there!"), Color.fromRGB(28, 25, 112));
	public static ItemStack ENDER_LEGGINGS = new CustomArmor(new CustomItem(Material.LEATHER_LEGGINGS, "&5&lEnder Leggings", "", "&a&oSometimes its here, sometimes there!"), Color.fromRGB(28, 25, 112));
	public static ItemStack ENDER_BOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&5&lEnder Boots", "", "&a&oSometimes its here, sometimes there!", "" , "&9+ No Enderpearl Damage"), Color.fromRGB(28, 25, 112));
	public static ItemStack SLIME_HELMET = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&a&lSlime Helmet", "", "&a&oBouncy Feeling"), Color.LIME);
	public static ItemStack SLIME_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&a&lSlime Chestplate", "", "&a&oBouncy Feeling"), Color.LIME);
	public static ItemStack SLIME_LEGGINGS = new CustomArmor(new CustomItem(Material.LEATHER_LEGGINGS, "&a&lSlime Leggings", "", "&a&oBouncy Feeling", "", "&9+ Speed"), Color.LIME);
	public static ItemStack SLIME_BOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&a&lSlime Boots", "", "&a&oBouncy Feeling", "", "&9+ Jump Boost", "&9+ No Fall Damage"), Color.LIME);
	public static ItemStack CACTUS_HELMET = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&2Cactus Helmet", 0, new String[0], new String[] {"THORNS-3", "DURABILITY-5"}), Color.GREEN);
	public static ItemStack CACTUS_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&2Cactus Chestplate", 0, new String[0], new String[] {"THORNS-3", "DURABILITY-5"}), Color.GREEN);
	public static ItemStack CACTUS_LEGGINGS = new CustomArmor(new CustomItem(Material.LEATHER_LEGGINGS, "&2Cactus Leggings", 0, new String[0], new String[] {"THORNS-3", "DURABILITY-5"}), Color.GREEN);
	public static ItemStack CACTUS_BOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&2Cactus Boots", 0, new String[0], new String[] {"THORNS-3", "DURABILITY-5"}), Color.GREEN);
	public static ItemStack DAMASCUS_STEEL_HELMET = new CustomItem(Material.IRON_HELMET, "&7Damascus Steel Helmet", new String[] {"DURABILITY-4", "PROTECTION_ENVIRONMENTAL-4"}, 0);
	public static ItemStack DAMASCUS_STEEL_CHESTPLATE = new CustomItem(Material.IRON_CHESTPLATE, "&7Damascus Steel Chestplate", new String[] {"DURABILITY-4", "PROTECTION_ENVIRONMENTAL-4"}, 0);
	public static ItemStack DAMASCUS_STEEL_LEGGINGS = new CustomItem(Material.IRON_LEGGINGS, "&7Damascus Steel Leggings", new String[] {"DURABILITY-4", "PROTECTION_ENVIRONMENTAL-4"}, 0);
	public static ItemStack DAMASCUS_STEEL_BOOTS = new CustomItem(Material.IRON_BOOTS, "&7Damascus Steel Boots", new String[] {"DURABILITY-4", "PROTECTION_ENVIRONMENTAL-4"}, 0);
	public static ItemStack REINFORCED_ALLOY_HELMET = new CustomItem(Material.IRON_HELMET, "&bReinforced Helmet", new String[] {"DURABILITY-9", "PROTECTION_ENVIRONMENTAL-9"}, 0);
	public static ItemStack REINFORCED_ALLOY_CHESTPLATE = new CustomItem(Material.IRON_CHESTPLATE, "&bReinforced Chestplate", new String[] {"DURABILITY-9", "PROTECTION_ENVIRONMENTAL-9"}, 0);
	public static ItemStack REINFORCED_ALLOY_LEGGINGS = new CustomItem(Material.IRON_LEGGINGS, "&bReinforced Leggings", new String[] {"DURABILITY-9", "PROTECTION_ENVIRONMENTAL-9"}, 0);
	public static ItemStack REINFORCED_ALLOY_BOOTS = new CustomItem(Material.IRON_BOOTS, "&bReinforced Boots", new String[] {"DURABILITY-9", "PROTECTION_ENVIRONMENTAL-9"}, 0);
	public static ItemStack SCUBA_HELMET = new CustomArmor(new CustomItem(Material.LEATHER_HELMET, "&cScuba Helmet", "", "&bAllows you to breathe Underwater", "&4&oPart of Hazmat Suit"), Color.ORANGE);
	public static ItemStack HAZMATSUIT_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&cHazmat Suit", "", "&bAllows you to walk through Fire", "&4&oPart of Hazmat Suit"), Color.ORANGE);
	public static ItemStack HAZMATSUIT_LEGGINGS = new CustomArmor(new CustomItem(Material.LEATHER_LEGGINGS, "&cHazmat Suit Leggings", "", "&4&oPart of Hazmat Suit"), Color.ORANGE);
	public static ItemStack RUBBER_BOOTS = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&cRubber Boots", "", "&4&oPart of Hazmat Suit"), Color.BLACK);
	public static ItemStack GILDED_IRON_HELMET = new CustomItem(Material.GOLDEN_HELMET, "&6Gilded Iron Helmet", new String[] {"DURABILITY-6", "PROTECTION_ENVIRONMENTAL-8"}, 0);
	public static ItemStack GILDED_IRON_CHESTPLATE = new CustomItem(Material.GOLDEN_CHESTPLATE, "&6Gilded Iron Chestplate", new String[] {"DURABILITY-6", "PROTECTION_ENVIRONMENTAL-8"}, 0);
	public static ItemStack GILDED_IRON_LEGGINGS = new CustomItem(Material.GOLDEN_LEGGINGS, "&6Gilded Iron Leggings", new String[] {"DURABILITY-6", "PROTECTION_ENVIRONMENTAL-8"}, 0);
	public static ItemStack GILDED_IRON_BOOTS = new CustomItem(Material.GOLDEN_BOOTS, "&6Gilded Iron Boots", new String[] {"DURABILITY-6", "PROTECTION_ENVIRONMENTAL-8"}, 0);
	public static ItemStack GOLD_HELMET = new CustomItem(Material.GOLDEN_HELMET, "&6Gold Helmet", 0, new String[] {"&912-Carat"}, new String[] {"DURABILITY-10"});
	public static ItemStack GOLD_CHESTPLATE = new CustomItem(Material.GOLDEN_CHESTPLATE, "&6Gold Chestplate", 0, new String[] {"&912-Carat"}, new String[] {"DURABILITY-10"});
	public static ItemStack GOLD_LEGGINGS = new CustomItem(Material.GOLDEN_LEGGINGS, "&6Gold Leggings", 0, new String[] {"&912-Carat"}, new String[] {"DURABILITY-10"});
	public static ItemStack GOLD_BOOTS = new CustomItem(Material.GOLDEN_BOOTS, "&6Gold Boots", 0, new String[] {"&912-Carat"}, new String[] {"DURABILITY-10"});
	public static ItemStack SLIME_HELMET_STEEL = new CustomItem(Material.IRON_HELMET, "&a&lSlime Helmet", 0, new String[] {"&7&oReinforced", "", "&a&oBouncy Feeling"}, new String[] {"DURABILITY-4", "PROTECTION_ENVIRONMENTAL-2"});
	public static ItemStack SLIME_CHESTPLATE_STEEL = new CustomItem(Material.IRON_CHESTPLATE, "&a&lSlime Chestplate", 0, new String[] {"&7&oReinforced", "", "&a&oBouncy Feeling"}, new String[] {"DURABILITY-4", "PROTECTION_ENVIRONMENTAL-2"});
	public static ItemStack SLIME_LEGGINGS_STEEL = new CustomItem(Material.IRON_LEGGINGS, "&a&lSlime Leggings", 0, new String[] {"&7&oReinforced", "", "&a&oBouncy Feeling", "", "&9+ Speed"}, new String[] {"DURABILITY-4", "PROTECTION_ENVIRONMENTAL-2"});
	public static ItemStack SLIME_BOOTS_STEEL = new CustomItem(Material.IRON_BOOTS, "&a&lSlime Boots", 0, new String[] {"&7&oReinforced", "", "&a&oBouncy Feeling", "", "&9+ Jump Boost", "&9+ No Fall Damage"}, new String[] {"DURABILITY-4", "PROTECTION_ENVIRONMENTAL-2"});
	public static ItemStack BOOTS_OF_THE_STOMPER = new CustomArmor(new CustomItem(Material.LEATHER_BOOTS, "&bBoots of the Stomper", "", "&9All Fall Damage you receive", "&9will be applied to nearby Mobs/Players", "", "&9+ No Fall Damage"), Color.AQUA);
	public static ItemStack HEAVY_METAL_HELMET = new CustomItem(Material.IRON_HELMET, "&cHeavy Helmet", 0, new String[] {"", "&9+ Strength", "&9+ Slowness"}, new String[] {"DURABILITY-10", "PROTECTION_ENVIRONMENTAL-10"});
	public static ItemStack HEAVY_METAL_CHESTPLATE = new CustomItem(Material.IRON_CHESTPLATE, "&cHeavy Chestplate", 0, new String[] {"", "&9+ Strength", "&9+ Slowness"}, new String[] {"DURABILITY-10", "PROTECTION_ENVIRONMENTAL-10"});
	public static ItemStack HEAVY_METAL_LEGGINGS = new CustomItem(Material.IRON_LEGGINGS, "&cHeavy Leggings", 0, new String[] {"", "&9+ Strength", "&9+ Slowness"}, new String[] {"DURABILITY-10", "PROTECTION_ENVIRONMENTAL-10"});
	public static ItemStack HEAVY_METAL_BOOTS = new CustomItem(Material.IRON_BOOTS, "&cHeavy Boots", 0, new String[] {"", "&9+ Strength", "&9+ Slowness"}, new String[] {"DURABILITY-10", "PROTECTION_ENVIRONMENTAL-10"});
	
	/*		 Misc 		*/
	public static ItemStack MAGIC_LUMP_1 = new CustomItem(Material.GOLD_NUGGET, "&6Magical Lump &7- &eI", "", "&c&oTier: I");
	public static ItemStack MAGIC_LUMP_2 = new CustomItem(Material.GOLD_NUGGET, "&6Magical Lump &7- &eII", "", "&c&oTier: II");
	public static ItemStack MAGIC_LUMP_3 = new CustomItem(Material.GOLD_NUGGET, "&6Magical Lump &7- &eIII", "", "&c&oTier: III");
	public static ItemStack ENDER_LUMP_1 = new CustomItem(Material.GOLD_NUGGET, "&5Ender Lump &7- &eI", "", "&c&oTier: I");
	public static ItemStack ENDER_LUMP_2 = new CustomItem(Material.GOLD_NUGGET, "&5Ender Lump &7- &eII", "", "&c&oTier: II");
	public static ItemStack ENDER_LUMP_3 = new CustomItem(Material.GOLD_NUGGET, "&5Ender Lump &7- &eIII", "", "&c&oTier: III");
	public static ItemStack MAGICAL_BOOK_COVER = new CustomItem(Material.PAPER, "&6Magical Book Cover", "", "&a&oUsed for various Magic Books");
	public static ItemStack BASIC_CIRCUIT_BOARD = new CustomItem(Material.ACTIVATOR_RAIL, "&bBasic Circuit Board");
	public static ItemStack ADVANCED_CIRCUIT_BOARD = new CustomItem(Material.POWERED_RAIL, "&bAdvanced Circuit Board");
	public static ItemStack WHEAT_FLOUR = new CustomItem(Material.SUGAR, "&rWheat Flour");
	public static ItemStack STEEL_PLATE = new CustomItem(Material.PAPER, "&7&lSteel Plate");
	public static ItemStack COMPRESSED_CARBON = null;
	public static ItemStack BATTERY = null;
	public static ItemStack CARBON_CHUNK = null;
	public static ItemStack STEEL_THRUSTER = new CustomItem(Material.BUCKET, "&7&lSteel Thruster");
	public static ItemStack POWER_CRYSTAL = null;
	public static ItemStack CHAIN = new CustomItem(Material.STRING, "&bChain");
	public static ItemStack HOOK = new CustomItem(Material.FLINT, "&bHook");
	public static ItemStack SIFTED_ORE = new CustomItem(Material.GUNPOWDER, "&6Sifted Ore");
	public static ItemStack STONE_CHUNK = null;
	public static ItemStack LAVA_CRYSTAL = null;
	public static ItemStack SALT = new CustomItem(Material.SUGAR, "&rSalt");
	public static ItemStack BUTTER = null;
	public static ItemStack CHEESE = null;
	public static ItemStack HEAVY_CREAM = new CustomItem(Material.SNOWBALL, "&rHeavy Cream");
	public static ItemStack CRUSHED_ORE = new CustomItem(Material.GUNPOWDER, "&6Crushed Ore");
	public static ItemStack PULVERIZED_ORE = new CustomItem(Material.GUNPOWDER, "&6Pulverized Ore");
	public static ItemStack PURE_ORE_CLUSTER = new CustomItem(Material.GUNPOWDER, "&6Pure Ore Cluster");
	public static ItemStack TINY_URANIUM = null;
	public static ItemStack SMALL_URANIUM = null;
	public static ItemStack MAGNET = null;
	public static ItemStack NECROTIC_SKULL = new CustomItem(Material.WITHER_SKELETON_SKULL, "&cNecrotic Skull");
	public static ItemStack ESSENCE_OF_AFTERLIFE = new CustomItem(Material.GUNPOWDER, "&4Essence of Afterlife");
	public static ItemStack ELECTRO_MAGNET = null;
	public static ItemStack HEATING_COIL = null;
	public static ItemStack COOLING_UNIT = null;
	public static ItemStack ELECTRIC_MOTOR = null;
	public static ItemStack CARGO_MOTOR = null;
	public static ItemStack SCROLL_OF_DIMENSIONAL_TELEPOSITION = new CustomItem(Material.PAPER, "&6Scroll of Dimensional Teleposition", "", "&cThis Scroll is capable of creating", "&ca temporary black Hole which pulls", "&cnearby Entities into itself and sends", "&cthem into another Dimension where", "&ceverything is turned around", "", "&rIn other words: Makes Entities turn by 180 Degrees");
	public static ItemStack TOME_OF_KNOWLEDGE_SHARING = new CustomItem(Material.BOOK, "&6Tome of Knowledge Sharing", "&7Owner: &bNone", "", "&eRight Click&7 to bind this Tome to yourself", "", "", "&eRight Click&7 to obtain all Researches by", "&7the previously assigned Owner");
	public static ItemStack HARDENED_GLASS = new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS, "&7Hardened Glass", "", "&rWithstands Explosions");
	public static ItemStack WITHER_PROOF_OBSIDIAN = new CustomItem(Material.OBSIDIAN, "&5Wither-Proof Obsidian", "", "&rWithstands Explosions", "&rWithstands Wither Bosses");
	public static ItemStack WITHER_PROOF_GLASS = new CustomItem(Material.PURPLE_STAINED_GLASS, "&5Wither-Proof Glass", "", "&rWithstands Explosions", "&rWithstands Wither Bosses");
	public static ItemStack REINFORCED_PLATE = new CustomItem(Material.PAPER, "&7Reinforced Plate");
	public static ItemStack ANCIENT_PEDESTAL = new CustomItem(Material.DISPENSER, "&dAncient Pedestal", "", "&5Part of the Ancient Altar");
	public static ItemStack ANCIENT_ALTAR = new CustomItem(Material.ENCHANTING_TABLE, "&dAncient Altar", "", "&5Multi-Block Altar for", "&5magical Crafting Processes");
	public static ItemStack DUCT_TAPE = null;
	public static ItemStack COPPER_WIRE = new CustomItem(Material.STRING, "&6Copper Wire", "", "&6Crucial component in electric modules");

	
	public static ItemStack RAINBOW_WOOL = new CustomItem(Material.WHITE_WOOL, "&5Rainbow Wool", "", "&dCycles through all Colors of the Rainbow!");
	public static ItemStack RAINBOW_GLASS = new CustomItem(Material.WHITE_STAINED_GLASS, "&5Rainbow Glass", "", "&dCycles through all Colors of the Rainbow!");
	public static ItemStack RAINBOW_CLAY = new CustomItem(Material.WHITE_TERRACOTTA, "&5Rainbow Clay", "", "&dCycles through all Colors of the Rainbow!");
	public static ItemStack RAINBOW_GLASS_PANE = new CustomItem(Material.WHITE_STAINED_GLASS_PANE, "&5Rainbow Glass Pane", "", "&dCycles through all Colors of the Rainbow!");
	
	public static ItemStack RAINBOW_WOOL_XMAS = new CustomItem(Material.WHITE_WOOL, "&5Rainbow Wool &7(Christmas)", "", Christmas.color("< Christmas Edition >"));
	public static ItemStack RAINBOW_GLASS_XMAS = new CustomItem(Material.WHITE_STAINED_GLASS, "&5Rainbow Glass &7(Christmas)", "", Christmas.color("< Christmas Edition >"));
	public static ItemStack RAINBOW_CLAY_XMAS = new CustomItem(Material.WHITE_TERRACOTTA, "&5Rainbow Clay &7(Christmas)", "", Christmas.color("< Christmas Edition >"));
	public static ItemStack RAINBOW_GLASS_PANE_XMAS = new CustomItem(Material.WHITE_STAINED_GLASS_PANE, "&5Rainbow Glass Pane &7(Christmas)", "", Christmas.color("< Christmas Edition >"));
	
	public static ItemStack RAINBOW_WOOL_VALENTINE = new CustomItem(Material.PINK_WOOL, "&5Rainbow Wool &7(Valentine's Day)", "", "&d< Valentine's Day Edition >");
	public static ItemStack RAINBOW_GLASS_VALENTINE = new CustomItem(Material.PINK_STAINED_GLASS, "&5Rainbow Glass &7(Valentine's Day)", "", "&d< Valentine's Day Edition >");
	public static ItemStack RAINBOW_CLAY_VALENTINE = new CustomItem(Material.PINK_TERRACOTTA, "&5Rainbow Clay &7(Valentine's Day)", "", "&d< Valentine's Day Edition >");
	public static ItemStack RAINBOW_GLASS_PANE_VALENTINE = new CustomItem(Material.PINK_STAINED_GLASS_PANE, "&5Rainbow Glass Pane &7(Valentine's Day)", "", "&d< Valentine's Day Edition >");
    
	/*		 Ingots 		*/
	public static ItemStack COPPER_INGOT = new CustomItem(Material.BRICK, "&bCopper Ingot");
	public static ItemStack TIN_INGOT = new CustomItem(Material.IRON_INGOT, "&bTin Ingot");
	public static ItemStack SILVER_INGOT = new CustomItem(Material.IRON_INGOT, "&bSilver Ingot");
	public static ItemStack ALUMINUM_INGOT = new CustomItem(Material.IRON_INGOT, "&bAluminum Ingot");
	public static ItemStack LEAD_INGOT = new CustomItem(Material.IRON_INGOT, "&bLead Ingot");
	public static ItemStack ZINC_INGOT = new CustomItem(Material.IRON_INGOT, "&bZinc Ingot");
	public static ItemStack MAGNESIUM_INGOT = new CustomItem(Material.IRON_INGOT, "&bMagnesium Ingot");
	
	/*		Alloy (Carbon + Iron)	*/
	public static ItemStack STEEL_INGOT = new CustomItem(Material.IRON_INGOT, "&bSteel Ingot");
	/*		Alloy (Copper + Tin)	*/
	public static ItemStack BRONZE_INGOT = new CustomItem(Material.BRICK, "&bBronze Ingot");
	/*		Alloy (Copper + Aluminum)	*/
	public static ItemStack DURALUMIN_INGOT = new CustomItem(Material.IRON_INGOT, "&bDuralumin Ingot");
	/*		Alloy (Copper + Silver)	*/
	public static ItemStack BILLON_INGOT = new CustomItem(Material.IRON_INGOT, "&bBillon Ingot");
	/*		Alloy (Copper + Zinc)	*/
	public static ItemStack BRASS_INGOT = new CustomItem(Material.GOLD_INGOT, "&bBrass Ingot");
	/*		Alloy (Aluminum + Brass)	*/
	public static ItemStack ALUMINUM_BRASS_INGOT = new CustomItem(Material.GOLD_INGOT, "&bAluminum Brass Ingot");
	/*		Alloy (Aluminum + Bronze)	*/
	public static ItemStack ALUMINUM_BRONZE_INGOT = new CustomItem(Material.GOLD_INGOT, "&bAluminum Bronze Ingot");
	/*		Alloy (Gold + Silver + Copper)	*/
	public static ItemStack CORINTHIAN_BRONZE_INGOT = new CustomItem(Material.GOLD_INGOT, "&bCorinthian Bronze Ingot");
	/*		Alloy (Lead + Tin)	*/
	public static ItemStack SOLDER_INGOT = new CustomItem(Material.IRON_INGOT, "&bSolder Ingot");
	/*		Alloy (Steel + Iron + Carbon)	*/
	public static ItemStack DAMASCUS_STEEL_INGOT = new CustomItem(Material.IRON_INGOT, "&bDamascus Steel Ingot");
	/*		Alloy (Damascus Steel + Duralumin + Compressed Carbon + Aluminium Bronze)	*/
	public static ItemStack HARDENED_METAL_INGOT = new CustomItem(Material.IRON_INGOT, "&b&lHardened Metal");
	/*		Alloy (Hardened Metal + Corinthian Bronze + Solder + Billon + Damascus Steel)	*/
	public static ItemStack REINFORCED_ALLOY_INGOT = new CustomItem(Material.IRON_INGOT, "&b&lReinforced Alloy Ingot");
	/*		Alloy (Iron + Silicon)		*/
	public static ItemStack FERROSILICON = new CustomItem(Material.IRON_INGOT, "&bFerrosilicon");
	/*		Alloy (Iron + Gold)			*/
	public static ItemStack GILDED_IRON = new CustomItem(Material.GOLD_INGOT, "&6&lGilded Iron");
	/*		Alloy (Redston + Ferrosilicon)	*/
	public static ItemStack REDSTONE_ALLOY = new CustomItem(Material.BRICK, "&cRedstone Alloy Ingot");
	/*		Alloy (Iron + Copper)		*/
	public static ItemStack NICKEL_INGOT = new CustomItem(Material.IRON_INGOT, "&bNickel Ingot");
	/*		Alloy (Nickel + Iron + Copper)		*/
	public static ItemStack COBALT_INGOT = new CustomItem(Material.IRON_INGOT, "&9Cobalt Ingot");
	
	/*		Gold		*/
	public static ItemStack GOLD_4K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(4-Carat)");
	public static ItemStack GOLD_6K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(6-Carat)");
	public static ItemStack GOLD_8K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(8-Carat)");
	public static ItemStack GOLD_10K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(10-Carat)");
	public static ItemStack GOLD_12K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(12-Carat)");
	public static ItemStack GOLD_14K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(14-Carat)");
	public static ItemStack GOLD_16K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(16-Carat)");
	public static ItemStack GOLD_18K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(18-Carat)");
	public static ItemStack GOLD_20K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(20-Carat)");
	public static ItemStack GOLD_22K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(22-Carat)");
	public static ItemStack GOLD_24K = new CustomItem(Material.GOLD_INGOT, "&rGold Ingot &7(24-Carat)");
	
	/*		 Dusts 		*/
	public static ItemStack IRON_DUST = new CustomItem(Material.GUNPOWDER, "&6Iron Dust");
	public static ItemStack GOLD_DUST = new CustomItem(Material.GLOWSTONE_DUST, "&6Gold Dust");
	public static ItemStack TIN_DUST = new CustomItem(Material.SUGAR, "&6Tin Dust");
	public static ItemStack COPPER_DUST = new CustomItem(Material.GLOWSTONE_DUST, "&6Copper Dust");
	public static ItemStack SILVER_DUST = new CustomItem(Material.SUGAR, "&6Silver Dust");
	public static ItemStack ALUMINUM_DUST = new CustomItem(Material.SUGAR, "&6Aluminum Dust");
	public static ItemStack LEAD_DUST = new CustomItem(Material.GUNPOWDER, "&6Lead Dust");
	public static ItemStack SULFATE = new CustomItem(Material.GLOWSTONE_DUST, "&6Sulfate");
	public static ItemStack ZINC_DUST = new CustomItem(Material.SUGAR, "&6Zinc Dust");
	public static ItemStack MAGNESIUM_DUST = new CustomItem(Material.SUGAR, "&6Magnesium");
	public static ItemStack CARBON = null;
	public static ItemStack SILICON = new CustomItem(Material.FIREWORK_STAR, "&6Silicon");
	public static ItemStack GOLD_24K_BLOCK = new CustomItem(Material.GOLD_BLOCK, "&rGold Block &7(24-Carat)");
	
	/*		 Gems 		*/
	public static ItemStack SYNTHETIC_DIAMOND = new CustomItem(Material.DIAMOND, "&bSynthetic Diamond");
	public static ItemStack SYNTHETIC_EMERALD = new CustomItem(Material.EMERALD, "&bSynthetic Emerald");
	public static ItemStack SYNTHETIC_SAPPHIRE = null;
	public static ItemStack CARBONADO = null;
	public static ItemStack RAW_CARBONADO = null;
	public static ItemStack URANIUM = null;
	public static ItemStack NEPTUNIUM = null;
	public static ItemStack PLUTONIUM = null;
	public static ItemStack BOOSTED_URANIUM = null;
	
	/*		Talisman		*/
	public static ItemStack TALISMAN = new CustomItem(Material.EMERALD, "&6Common Talisman");
	public static ItemStack TALISMAN_ANVIL = new CustomItem(Material.EMERALD, "&aTalisman of the Anvil", "", "&rEach Talisman can prevent", "&r1 Tool from breaking, but will then", "&rbe consumed", "", "&4&lWARNING:", "&4This Talisman does not work on", "&4Tools which are too powerful", "&4due to their complexity");
	public static ItemStack TALISMAN_MINER = new CustomItem(Material.EMERALD, "&aTalisman of the Miner", "", "&rWhile you have this Talisman", "&rin your Inventory it has", "&ra 20% chance of doubling", "&rall Ores you mine");
	public static ItemStack TALISMAN_HUNTER = new CustomItem(Material.EMERALD, "&aTalisman of the Hunter", "", "&rWhile you have this Talisman", "&rin your Inventory it has", "&ra 20% chance of doubling", "&rall Drops from Mobs you kill");
	public static ItemStack TALISMAN_LAVA = new CustomItem(Material.EMERALD, "&aTalisman of the Lava Walker", "", "&rWhile you have this Talisman", "&rin your Inventory it will", "&rgive you Fire Resistance", "&ras soon as you touch Lava", "&rbut will then be consumed");
	public static ItemStack TALISMAN_WATER = new CustomItem(Material.EMERALD, "&aTalisman of the Water Breather", "", "&rWhile you have this Talisman", "&rin your Inventory it will", "&rgive you the ability", "&rto breath underwater as", "&rsoon as you start drowning", "&rbut will then be consumed");
	public static ItemStack TALISMAN_ANGEL = new CustomItem(Material.EMERALD, "&aTalisman of the Angel", "", "&rWhile you have this Talisman", "&rin your Inventory it has a", "&r75% chance to prevent you", "&rfrom taking Fall Damage");
	public static ItemStack TALISMAN_FIRE = new CustomItem(Material.EMERALD, "&aTalisman of the Firefighter", "", "&rWhile you have this Talisman", "&rin your Inventory it will", "&rgive you Fire Resistance", "&ras soon as you start burning", "&rbut will then be consumed");
	public static ItemStack TALISMAN_MAGICIAN = new CustomItem(Material.EMERALD, "&aTalisman of the Magician", "", "&rWhile you have this Talisman", "&rin your Inventory it gives", "&ryou a 80% Luck Bonus on Enchanting", "&rYou will sometimes get an Extra Enchantment");
	public static ItemStack TALISMAN_TRAVELLER = new CustomItem(Material.EMERALD, "&aTalisman of the Traveller", "", "&rWhile you have this Talisman", "&rin your Inventory it gives", "&ryou a 60% Chance for a decent", "&rSpeed Buff when you start sprinting");
	public static ItemStack TALISMAN_WARRIOR = new CustomItem(Material.EMERALD, "&aTalisman of the Warrior", "", "&rWhile you have this Talisman", "&rin your Inventory it gives", "&ryou Strength III whenever you get hit", "&rbut will then be consumed");
	public static ItemStack TALISMAN_KNIGHT = new CustomItem(Material.EMERALD, "&aTalisman of the Knight", "", "&rWhile you have this Talisman", "&rin your Inventory it gives", "&ryou a 30% Chance for 5 Seconds of Regeneration", "&rwhenever You get hit", "&rbut will then be consumed");
	public static ItemStack TALISMAN_WHIRLWIND = new CustomItem(Material.EMERALD, "&aTalisman of the Whirlwind", "", "&rWhile you have this Talisman", "&rin your Inventory it will reflect", "&r60% of all Projectiles fired at you");
	public static ItemStack TALISMAN_WIZARD = new CustomItem(Material.EMERALD, "&aTalisman of the Wizard", "", "&rWhile you have this Talisman", "&rin your Inventory it allows you to", "&robtain Fortune Level 4/5 however", "&rit also has a chance to lower the", "&rLevel of some Enchantments on your Item");
	
	/*		Staves		*/
	public static ItemStack STAFF_ELEMENTAL = new CustomItem(Material.STICK, "&6Elemental Staff");
	public static ItemStack STAFF_WIND = new CustomItem(Material.STICK, "&6Elemental Staff &7- &b&oWind", 0, new String[] {"", "&7Element: &b&oWind", "", "&7&eRight Click&7 to launch yourself forward"}, new String[] {"LUCK-1"});
	public static ItemStack STAFF_FIRE = new CustomItem(Material.STICK, "&6Elemental Staff &7- &c&oFire", 0, new String[] {"", "&7Element: &c&oFire"}, new String[] {"FIRE_ASPECT-5"});
	public static ItemStack STAFF_WATER = new CustomItem(Material.STICK, "&6Elemental Staff &7- &1&oWater", 0, new String[] {"", "&7Element: &1&oWater", "", "&7&eRight Click&7 to extinguish yourself"}, new String[] {"WATER_WORKER-1"});
	
	/*		 Machines 		*/
	public static ItemStack GRIND_STONE = new CustomItem(Material.DISPENSER, "&bGrind Stone", "", "&a&oGrinds Items for more Efficiency");
	public static ItemStack ARMOR_FORGE = new CustomItem(Material.ANVIL, "&6Armor Forge", "", "&a&oGives you the Ability to create powerful Armor");
	public static ItemStack SMELTERY = new CustomItem(Material.FURNACE, "&6Smeltery", "", "&a&oActs as a high-temperature Furnace for Metals");
	public static ItemStack IGNITION_CHAMBER = new CustomItem(Material.HOPPER, "&4Automatic Ignition Chamber", "&rPrevents the Smeltery from using up fire.", "&rRequires Flint and Steel", "&rMust be placed adjacent to the Smeltery's dispenser");
	public static ItemStack ORE_CRUSHER = new CustomItem(Material.DISPENSER, "&bOre Crusher", "", "&a&oCrushes Ores to double them");
	public static ItemStack COMPRESSOR = new CustomItem(Material.PISTON, "&bCompressor", "", "&a&oCompresses Items");
	public static ItemStack PRESSURE_CHAMBER = new CustomItem(Material.GLASS, "&bPressure Chamber", "", "&a&oCompresses Items even more");
	public static ItemStack MAGIC_WORKBENCH = new CustomItem(Material.CRAFTING_TABLE, "&6Magic Workbench", "Infuses Items with magical Energy");
	public static ItemStack ORE_WASHER = new CustomItem(Material.CAULDRON, "&6Ore Washer", "", "&a&oWashes Sifted Ore to filter Ores", "&a&oand gives you small Stone Chunks");
	public static ItemStack TABLE_SAW = new CustomItem(Material.STONECUTTER, "&6Table Saw", "", "&a&oAllows you get 8 planks from 1 Log", "&a&o(Works with all log types)");
	public static ItemStack SAW_MILL = new CustomItem(Material.BARRIER, "&6Saw Mill", "", "&cObsolete! Use the Table Saw instead");
	public static ItemStack COMPOSTER = new CustomItem(Material.CAULDRON, "&aComposter", "", "&a&oCan convert various Materials over Time...");
	public static ItemStack ENHANCED_CRAFTING_TABLE = new CustomItem(Material.CRAFTING_TABLE, "&eEnhanced Crafting Table", "", "&a&oA regular Crafting Table cannot", "&a&ohold this massive Amount of Power...");
	public static ItemStack CRUCIBLE = new CustomItem(Material.CAULDRON, "&cCrucible", "", "&a&oUsed to smelt Items into Liquids");
	public static ItemStack JUICER = new CustomItem(Material.GLASS_BOTTLE, "&aJuicer", "", "&a&oAllows you to create delicious Juice");
	
	public static ItemStack SOLAR_PANEL = new CustomItem(Material.DAYLIGHT_DETECTOR, "&bSolar Panel", "", "&a&oTransforms Sunlight to Energy");
	public static ItemStack SOLAR_ARRAY = new CustomItem(Material.DAYLIGHT_DETECTOR, "&bSolar Array", "", "&a&oTransforms Sunlight to Energy");
	public static ItemStack DIGITAL_MINER = new CustomItem(Material.IRON_PICKAXE, "&bDigital Miner", "", "&a&oDigs out everything!");
	public static ItemStack ADVANCED_DIGITAL_MINER = new CustomItem(Material.DIAMOND_PICKAXE, "&6Advanced Digital Miner", "", "&a&oDigs out everything!", "&a&oAutomatically crushes your Ores");
	public static ItemStack AUTOMATED_PANNING_MACHINE = new CustomItem(Material.BOWL, "&aAutomated Panning Machine", "", "&a&oA MultiBlock Version of the Gold Pan");
	public static ItemStack OUTPUT_CHEST = new CustomItem(Material.CHEST, "&4Output Chest", "", "&c&oA basic machine will try to put", "&c&oitems in this chest if it's placed", "&c&oadjacent to the dispenser.");
	public static ItemStack HOLOGRAM_PROJECTOR = new CustomItem(Material.QUARTZ_SLAB, "&bHologram Projector", "", "&rProjects an Editable Hologram");
	
	/*		 Enhanced Furnaces 		*/
	public static ItemStack ENHANCED_FURNACE = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eI", "", "&7Processing Speed: &e1x", "&7Fuel Efficiency: &e1x", "&7Luck Multiplier: &e1x");
	public static ItemStack ENHANCED_FURNACE_2 = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eII", "", "&7Processing Speed: &e2x", "&7Fuel Efficiency: &e1x", "&7Luck Multiplier: &e1x");
	public static ItemStack ENHANCED_FURNACE_3 = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eIII", "", "&7Processing Speed: &e2x", "&7Fuel Efficiency: &e2x", "&7Luck Multiplier: &e1x");
	public static ItemStack ENHANCED_FURNACE_4 = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eIV", "", "&7Processing Speed: &e3x", "&7Fuel Efficiency: &e2x", "&7Luck Multiplier: &e1x");
	public static ItemStack ENHANCED_FURNACE_5 = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eV", "", "&7Processing Speed: &e3x", "&7Fuel Efficiency: &e2x", "&7Luck Multiplier: &e2x");
	public static ItemStack ENHANCED_FURNACE_6 = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eVI", "", "&7Processing Speed: &e3x", "&7Fuel Efficiency: &e3x", "&7Luck Multiplier: &e2x");
	public static ItemStack ENHANCED_FURNACE_7 = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eVII", "", "&7Processing Speed: &e4x", "&7Fuel Efficiency: &e3x", "&7Luck Multiplier: &e2x");
	public static ItemStack ENHANCED_FURNACE_8 = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eVIII", "", "&7Processing Speed: &e4x", "&7Fuel Efficiency: &e4x", "&7Luck Multiplier: &e2x");
	public static ItemStack ENHANCED_FURNACE_9 = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eIX", "", "&7Processing Speed: &e5x", "&7Fuel Efficiency: &e4x", "&7Luck Multiplier: &e2x");
	public static ItemStack ENHANCED_FURNACE_10 = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eX", "", "&7Processing Speed: &e5x", "&7Fuel Efficiency: &e5x", "&7Luck Multiplier: &e2x");
	public static ItemStack ENHANCED_FURNACE_11 = new CustomItem(Material.FURNACE, "&7Enhanced Furnace - &eXI", "", "&7Processing Speed: &e5x", "&7Fuel Efficiency: &e5x", "&7Luck Multiplier: &e3x");
	public static ItemStack REINFORCED_FURNACE = new CustomItem(Material.FURNACE, "&7Reinforced Furnace", "", "&7Processing Speed: &e10x", "&7Fuel Efficiency: &e10x", "&7Luck Multiplier: &e3x");
	public static ItemStack CARBONADO_EDGED_FURNACE = new CustomItem(Material.FURNACE, "&7Carbonado Edged Furnace", "", "&7Processing Speed: &e20x", "&7Fuel Efficiency: &e10x", "&7Luck Multiplier: &e3x");
	
	public static ItemStack BLOCK_PLACER = new CustomItem(Material.DISPENSER, "&aBlock Placer", "", "&rAll Blocks in this Dispenser", "&rwill automatically get placed");
	
	/*		Soulbound Items		*/
	public static ItemStack SOULBOUND_SWORD = new CustomItem(Material.DIAMOND_SWORD, "&cSoulbound Sword");
	public static ItemStack SOULBOUND_BOW = new CustomItem(Material.BOW, "&cSoulbound Bow");
	public static ItemStack SOULBOUND_PICKAXE = new CustomItem(Material.DIAMOND_PICKAXE, "&cSoulbound Pickaxe");
	public static ItemStack SOULBOUND_AXE = new CustomItem(Material.DIAMOND_AXE, "&cSoulbound Axe");
	public static ItemStack SOULBOUND_SHOVEL = new CustomItem(Material.DIAMOND_SHOVEL, "&cSoulbound Shovel");
	public static ItemStack SOULBOUND_HOE = new CustomItem(Material.DIAMOND_HOE, "&cSoulbound Hoe");
	
	public static ItemStack SOULBOUND_HELMET = new CustomItem(Material.DIAMOND_HELMET, "&cSoulbound Helmet");
	public static ItemStack SOULBOUND_CHESTPLATE = new CustomItem(Material.DIAMOND_CHESTPLATE, "&cSoulbound Chestplate");
	public static ItemStack SOULBOUND_LEGGINGS = new CustomItem(Material.DIAMOND_LEGGINGS, "&cSoulbound Leggings");
	public static ItemStack SOULBOUND_BOOTS = new CustomItem(Material.DIAMOND_BOOTS, "&cSoulbound Boots");
	
	/*		Runes				*/
	public static ItemStack BLANK_RUNE = null;
	public static ItemStack RUNE_AIR = null;
	public static ItemStack RUNE_WATER = null;
	public static ItemStack RUNE_FIRE = null;
	public static ItemStack RUNE_EARTH = null;
	public static ItemStack RUNE_ENDER = null;
	public static ItemStack RUNE_RAINBOW = null;
	
	static {
		ItemStack itemB = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta imB = (FireworkEffectMeta) itemB.getItemMeta();
		imB.setEffect(FireworkEffect.builder().with(Type.BURST).with(Type.BURST).withColor(Color.BLACK).build());
		imB.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8Blank Rune"));
		itemB.setItemMeta(imB);
		BLANK_RUNE = itemB;
		
		ItemStack itemA = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta imA = (FireworkEffectMeta) itemA.getItemMeta();
		imA.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.AQUA).build());
		imA.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Ancient Rune &8&l[&b&lAir&8&l]"));
		itemA.setItemMeta(imA);
		RUNE_AIR = itemA;
		
		ItemStack itemW = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta imW = (FireworkEffectMeta) itemW.getItemMeta();
		imW.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.BLUE).build());
		imW.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Ancient Rune &8&l[&1&lWater&8&l]"));
		itemW.setItemMeta(imW);
		RUNE_WATER = itemW;
		
		ItemStack itemF = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta imF = (FireworkEffectMeta) itemF.getItemMeta();
		imF.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.RED).build());
		imF.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Ancient Rune &8&l[&4&lFire&8&l]"));
		itemF.setItemMeta(imF);
		RUNE_FIRE = itemF;
		
		ItemStack itemE = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta imE = (FireworkEffectMeta) itemE.getItemMeta();
		imE.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.ORANGE).build());
		imE.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Ancient Rune &8&l[&c&lEarth&8&l]"));
		itemE.setItemMeta(imE);
		RUNE_EARTH = itemE;
		
		ItemStack itemN = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta imN = (FireworkEffectMeta) itemN.getItemMeta();
		imN.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.PURPLE).build());
		imN.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Ancient Rune &8&l[&5&lEnder&8&l]"));
		itemN.setItemMeta(imN);
		RUNE_ENDER = itemN;
		
		ItemStack itemR = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta imR = (FireworkEffectMeta) itemR.getItemMeta();
		imR.setEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.PURPLE).build());
		imR.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Ancient Rune &8&l[&d&lRainbow&8&l]"));
		itemR.setItemMeta(imR);
		RUNE_RAINBOW = itemR;
	}
	
	/*		Electricity			*/
	public static ItemStack SOLAR_GENERATOR = new CustomItem(Material.DAYLIGHT_DETECTOR, "&bSolar Generator", "", "&eBasic Generator", "&8\u21E8 &e\u26A1 &70 J Buffer", "&8\u21E8 &e\u26A1 &74 J/s");
	public static ItemStack SOLAR_GENERATOR_2 = new CustomItem(Material.DAYLIGHT_DETECTOR, "&cAdvanced Solar Generator", "", "&aMedium Generator", "&8\u21E8 &e\u26A1 &70 J Buffer", "&8\u21E8 &e\u26A1 &716 J/s");
	public static ItemStack SOLAR_GENERATOR_3 = new CustomItem(Material.DAYLIGHT_DETECTOR, "&4Carbonado Solar Generator", "", "&4End-Game Generator", "&8\u21E8 &e\u26A1 &70 J Buffer", "&8\u21E8 &e\u26A1 &764 J/s");
	public static ItemStack SOLAR_GENERATOR_4 = new CustomItem(Material.DAYLIGHT_DETECTOR, "&eEnergized Solar Generator", "", "&9Works at Night", "", "&4End-Game Generator", "&8\u21E8 &e\u26A1 &70 J Buffer", "&8\u21E8 &e\u26A1 &7256 J/s (Day)", "&8\u21E8 &e\u26A1 &7128 J/s (Night)");
	
	public static ItemStack COAL_GENERATOR = null;
	public static ItemStack LAVA_GENERATOR = null;
	
	public static ItemStack ELECTRIC_FURNACE = new CustomItem(Material.FURNACE, "&cElectric Furnace", "", "&eBasic Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &74 J/s");
	public static ItemStack ELECTRIC_FURNACE_2 = new CustomItem(Material.FURNACE, "&cElectric Furnace &7- &eII", "", "&aMedium Machine", "&8\u21E8 &7Speed: 2x", "&8\u21E8 &e\u26A1 &76 J/s");
	public static ItemStack ELECTRIC_FURNACE_3 = new CustomItem(Material.FURNACE, "&cElectric Furnace &7- &eIII", "", "&aMedium Machine", "&8\u21E8 &7Speed: 4x", "&8\u21E8 &e\u26A1 &710 J/s");
	
	public static ItemStack ELECTRIC_ORE_GRINDER = new CustomItem(Material.FURNACE, "&cElectric Ore Grinder", "","&rWorks as an Ore Crusher and Grind Stone", "", "&6Advanced Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &712 J/s");
	public static ItemStack ELECTRIC_ORE_GRINDER_2 = new CustomItem(Material.FURNACE, "&cElectric Ore Grinder &7(&eII&7)", "","&rWorks as an Ore Crusher and Grind Stone", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 4x", "&8\u21E8 &e\u26A1 &730 J/s");
	public static ItemStack ELECTRIC_INGOT_PULVERIZER = new CustomItem(Material.FURNACE, "&cElectric Ingot Pulverizer", "", "&rPulverizes Ingots into Dust", "", "&aMedium Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &714 J/s");
	public static ItemStack AUTO_ENCHANTER = new CustomItem(Material.ENCHANTING_TABLE, "&5Auto Enchanter", "", "&aMedium Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &718 J/s");
	public static ItemStack AUTO_DISENCHANTER = new CustomItem(Material.ENCHANTING_TABLE, "&5Auto Disenchanter", "", "&aMedium Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &718 J/s");
	public static ItemStack AUTO_ANVIL = new CustomItem(Material.IRON_BLOCK, "&7Auto Anvil", "", "&6Advanced Machine", "&8\u21E8 &7Repair Factor: 10%", "&8\u21E8 &e\u26A1 &724 J/s");
	public static ItemStack AUTO_ANVIL_2 = new CustomItem(Material.IRON_BLOCK, "&7Auto Anvil Mk.II", "", "&4End-Game Machine", "&8\u21E8 &7Repair Factor: 25%", "&8\u21E8 &e\u26A1 &732 J/s");
	
	public static ItemStack BIO_REACTOR = new CustomItem(Material.LIME_TERRACOTTA, "&2Bio Reactor", "", "&6Average Generator", "&8\u21E8 &e\u26A1 &7128 J Buffer", "&8\u21E8 &e\u26A1 &78 J/s");
	public static ItemStack MULTIMETER = new CustomItem(Material.CLOCK, "&eMultimeter", "", "&rMeasures the Amount of stored", "&rEnergy in a Block");
	public static ItemStack SMALL_CAPACITOR = null, MEDIUM_CAPACITOR = null, BIG_CAPACITOR = null, LARGE_CAPACITOR = null, CARBONADO_EDGED_CAPACITOR = null;
	
	/*		Robots				*/
	public static ItemStack PROGRAMMABLE_ANDROID = null;
	public static ItemStack PROGRAMMABLE_ANDROID_MINER = null;
	public static ItemStack PROGRAMMABLE_ANDROID_BUTCHER = null;
	public static ItemStack PROGRAMMABLE_ANDROID_FARMER = null;
	public static ItemStack PROGRAMMABLE_ANDROID_WOODCUTTER = null;
	public static ItemStack PROGRAMMABLE_ANDROID_FISHERMAN = null;

	public static ItemStack PROGRAMMABLE_ANDROID_2 = null;
	public static ItemStack PROGRAMMABLE_ANDROID_2_FISHERMAN = null;
	public static ItemStack PROGRAMMABLE_ANDROID_2_FARMER = null;
	public static ItemStack PROGRAMMABLE_ANDROID_2_BUTCHER = null;

	public static ItemStack PROGRAMMABLE_ANDROID_3 = null;
	public static ItemStack PROGRAMMABLE_ANDROID_3_FISHERMAN = null;
	public static ItemStack PROGRAMMABLE_ANDROID_3_BUTCHER = null;
	
	/*		GPS					*/
	public static ItemStack GPS_TRANSMITTER = null;
	public static ItemStack GPS_TRANSMITTER_2 = null;
	public static ItemStack GPS_TRANSMITTER_3 = null;
	public static ItemStack GPS_TRANSMITTER_4 = null;
	
	public static ItemStack GPS_CONTROL_PANEL = null;
	public static ItemStack GPS_MARKER_TOOL = new CustomItem(Material.REDSTONE_TORCH, "&bGPS Marker Tool", "", "&rAllows you to set a Waypoint at", "&rthe Location you place this");
	public static ItemStack GPS_EMERGENCY_TRANSMITTER = null;
	public static ItemStack GPS_GEO_SCANNER = null;
	
	public static ItemStack ANDROID_INTERFACE_FUEL = new CustomItem(Material.DISPENSER, "&7Android Interface &c(Fuel)", "", "&rItems stored in this Interface", "&rwill be inserted into an Android's Fuel Slot", "&rwhen its Script tells them to do so");
	public static ItemStack ANDROID_INTERFACE_ITEMS = new CustomItem(Material.DISPENSER, "&7Android Interface &9(Items)", "", "&rItems stored in an Android's Inventory", "&rwill be inserted into this Interface", "&rwhen its Script tells them to do so");

	public static ItemStack BUCKET_OF_OIL = null;
	public static ItemStack BUCKET_OF_FUEL = null;
	public static ItemStack OIL_PUMP = null;

	public static ItemStack REFINERY = new CustomItem(Material.PISTON, "&cRefinery", "", "&rRefines Oil to create Fuel");
	public static ItemStack COMBUSTION_REACTOR = null;
	public static ItemStack ANDROID_MEMORY_CORE = null;
	
	public static ItemStack GPS_TELEPORTER_PYLON = new CustomItem(Material.PURPLE_STAINED_GLASS, "&5GPS Teleporter Pylon", "", "&7Teleporter Component");
	public static ItemStack GPS_TELEPORTATION_MATRIX = new CustomItem(Material.IRON_BLOCK, "&bGPS Teleporter Matrix", "", "&rThis is your Teleporter's Main Component", "&rThis Matrix allows Players to choose from all", "&rWaypoints made by the Player who has placed", "&rthis Device.");
	public static ItemStack GPS_ACTIVATION_DEVICE_SHARED = new CustomItem(Material.STONE_PRESSURE_PLATE, "&rGPS Activation Device &3(Shared)", "", "&rPlace this onto a Teleportation Matrix", "&rand step onto this Plate to activate", "&rthe Teleportation Process");
	public static ItemStack GPS_ACTIVATION_DEVICE_PERSONAL = new CustomItem(Material.STONE_PRESSURE_PLATE, "&rGPS Activation Device &a(Personal)", "", "&rPlace this onto a Teleportation Matrix", "&rand step onto this Plate to activate", "&rthe Teleportation Process", "", "&rThis Version only allows the Person who", "&rplaced this Device to use it");

	public static ItemStack ELEVATOR = new CustomItem(Material.STONE_PRESSURE_PLATE, "&bElevator Plate", "", "&rPlace an Elevator Plate on every floor", "&rand you will be able to teleport between them.", "", "&eRight Click this Block &7to name it");
	
	public static ItemStack INFUSED_HOPPER = new CustomItem(Material.HOPPER, "&5Infused Hopper", "", "&rAutomatically picks up nearby Items in a 7x7x7", "&rRadius when placed.");

	public static ItemStack PLASTIC_SHEET = new CustomItem(Material.PAPER, "&rPlastic Sheet");
	public static ItemStack HEATED_PRESSURE_CHAMBER = new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS, "&cHeated Pressure Chamber", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710 J/s");
	public static ItemStack HEATED_PRESSURE_CHAMBER_2 = new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS, "&cHeated Pressure Chamber &7- &eII", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 5x", "&8\u21E8 &e\u26A1 &744 J/s");
	
	public static ItemStack ELECTRIC_SMELTERY = new CustomItem(Material.FURNACE, "&cElectric Smeltery", "", "&4Alloys-Only, doesn't smelt Dust into Ingots", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &720 J/s");
	public static ItemStack ELECTRIC_SMELTERY_2 = new CustomItem(Material.FURNACE, "&cElectric Smeltery &7- &eII", "", "&4Alloys-Only, doesn't smelt Dust into Ingots", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 3x", "&8\u21E8 &e\u26A1 &740 J/s");
	
	public static ItemStack ELECTRIFIED_CRUCIBLE = new CustomItem(Material.RED_TERRACOTTA, "&cElectrified Crucible", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &748 J/s");
	public static ItemStack ELECTRIFIED_CRUCIBLE_2 = new CustomItem(Material.RED_TERRACOTTA, "&cElectrified Crucible &7- &eII", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 2x", "&8\u21E8 &e\u26A1 &780 J/s");
	public static ItemStack ELECTRIFIED_CRUCIBLE_3 = new CustomItem(Material.RED_TERRACOTTA, "&cElectrified Crucible &7- &eIII", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 4x", "&8\u21E8 &e\u26A1 &7120 J/s");

	public static ItemStack CARBON_PRESS = new CustomItem(Material.BLACK_STAINED_GLASS, "&cCarbon Press", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &720 J/s");
	public static ItemStack CARBON_PRESS_2 = new CustomItem(Material.BLACK_STAINED_GLASS, "&cCarbon Press &7- &eII", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 3x", "&8\u21E8 &e\u26A1 &750 J/s");
	public static ItemStack CARBON_PRESS_3 = new CustomItem(Material.BLACK_STAINED_GLASS, "&cCarbon Press &7- &eIII", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 15x", "&8\u21E8 &e\u26A1 &7180 J/s");
	
	public static ItemStack BLISTERING_INGOT = new CustomItem(Material.GOLD_INGOT, "&6Blistering Ingot &7(33%)", "", "&2Radiation Level: HIGH", "&4&oHazmat Suit required");
	public static ItemStack BLISTERING_INGOT_2 = new CustomItem(Material.GOLD_INGOT, "&6Blistering Ingot &7(66%)", "", "&2Radiation Level: HIGH", "&4&oHazmat Suit required");
	public static ItemStack BLISTERING_INGOT_3 = new CustomItem(Material.GOLD_INGOT, "&6Blistering Ingot", "", "&2Radiation Level: HIGH", "&4&oHazmat Suit required");
	
	public static ItemStack ENERGY_REGULATOR = null;
	public static ItemStack DEBUG_FISH = new CustomItem(Material.PUFFERFISH, "&3How much is the Fish?", "", "&eRight Click &rany Block to view it's BlockData", "&eLeft Click &rto break a Block", "&eShift + Left Click &rany Block to erase it's BlockData", "&eShift + Right Click &rto place a Placeholder Block");


	public static ItemStack NETHER_ICE = null;
	public static ItemStack ENRICHED_NETHER_ICE = null;
	public static ItemStack NETHER_ICE_COOLANT_CELL = null;
	public static ItemStack NETHER_DRILL = new CustomItem(Material.RED_TERRACOTTA, "&4Nether Drill", "", "&rAllows you to mine Nether Ice", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7102 J/s", "", "&c&l! &cCan only be used in the Nether!", "&c&l! &cMake sure to Geo-Scan the Chunk first");
	
	// Cargo
	public static ItemStack CARGO_MANAGER = null;
	public static ItemStack CARGO_NODE = null;
	public static ItemStack CARGO_INPUT = null;
	public static ItemStack CARGO_OUTPUT = null;
	public static ItemStack CARGO_OUTPUT_ADVANCED = null;

	public static ItemStack AUTO_BREEDER = new CustomItem(Material.HAY_BLOCK, "&eAuto-Breeder", "", "&rRuns on &aOrganic Food", "", "&4End-Game Machine", "&8\u21E8 &e\u26A1 &71024 J Buffer", "&8\u21E8 &e\u26A1 &760 J/Animal");;
	
	public static ItemStack ORGANIC_FOOD = null;
	public static ItemStack WHEAT_ORGANIC_FOOD = null;
	public static ItemStack CARROT_ORGANIC_FOOD = null;
	public static ItemStack POTATO_ORGANIC_FOOD = null;
	public static ItemStack SEEDS_ORGANIC_FOOD = null;
	public static ItemStack BEETROOT_ORGANIC_FOOD = null;
	public static ItemStack MELON_ORGANIC_FOOD = null;
	public static ItemStack APPLE_ORGANIC_FOOD = null;
	
	public static ItemStack FERTILIZER = null;
	public static ItemStack WHEAT_FERTILIZER = null;
	public static ItemStack CARROT_FERTILIZER = null;
	public static ItemStack POTATO_FERTILIZER = null;
	public static ItemStack SEEDS_FERTILIZER = null;
	public static ItemStack BEETROOT_FERTILIZER = null;
	public static ItemStack MELON_FERTILIZER = null;
	public static ItemStack APPLE_FERTILIZER = null;
	
	public static ItemStack ANIMAL_GROWTH_ACCELERATOR = new CustomItem(Material.HAY_BLOCK, "&bAnimal Growth Accelerator", "", "&rRuns on &aOrganic Food", "", "&4End-Game Machine", "&8\u21E8 &e\u26A1 &71024 J Buffer", "&8\u21E8 &e\u26A1 &728 J/s");;
	public static ItemStack CROP_GROWTH_ACCELERATOR = new CustomItem(Material.LIME_TERRACOTTA, "&aCrop Growth Accelerator", "", "&rRuns on &aOrganic Fertilizer", "", "&4End-Game Machine", "&8\u21E8 &7Radius: 7x7", "&8\u21E8 &7Speed: &a3/time", "&8\u21E8 &e\u26A1 &71024 J Buffer", "&8\u21E8 &e\u26A1 &750 J/s");
	public static ItemStack CROP_GROWTH_ACCELERATOR_2 = new CustomItem(Material.LIME_TERRACOTTA, "&aCrop Growth Accelerator &7(&eII&7)", "", "&rRuns on &aOrganic Fertilizer", "", "&4End-Game Machine", "&8\u21E8 &7Radius: 9x9", "&8\u21E8 &7Speed: &a4/time", "&8\u21E8 &e\u26A1 &71024 J Buffer", "&8\u21E8 &e\u26A1 &760 J/s");

	public static ItemStack FOOD_FABRICATOR = new CustomItem(Material.GREEN_STAINED_GLASS, "&cFood Fabricator", "", "&rProduces &aOrganic Food", "", "&6Advanced Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7256 J Buffer", "&8\u21E8 &e\u26A1 &714 J/s");
	public static ItemStack FOOD_FABRICATOR_2 = new CustomItem(Material.GREEN_STAINED_GLASS, "&cFood Fabricator &7(&eII&7)", "", "&rProduces &aOrganic Food", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 6x", "&8\u21E8 &e\u26A1 &7512 J Buffer", "&8\u21E8 &e\u26A1 &748 J/s");
	
	public static ItemStack FOOD_COMPOSTER = new CustomItem(Material.GREEN_TERRACOTTA, "&cFood Composter", "", "&rProduces &aOrganic Fertilizer", "", "&6Advanced Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7256 J Buffer", "&8\u21E8 &e\u26A1 &716 J/s");
	public static ItemStack FOOD_COMPOSTER_2 = new CustomItem(Material.GREEN_TERRACOTTA, "&cFood Composter &7(&eII&7)", "", "&rProduces &aOrganic Fertilizer", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 10x", "&8\u21E8 &e\u26A1 &7512 J Buffer", "&8\u21E8 &e\u26A1 &752 J/s");

	public static ItemStack XP_COLLECTOR = null;
	public static ItemStack REACTOR_COOLANT_CELL = null;

	public static ItemStack NUCLEAR_REACTOR = null;
	public static ItemStack NETHERSTAR_REACTOR = null;
	public static ItemStack REACTOR_ACCESS_PORT = new CustomItem(Material.CYAN_TERRACOTTA, "&2Reactor Access Port", "", "&rAllows you to interact with a Reactor", "&rvia Cargo Nodes, can also be used", "&ras a Buffer", "", "&8\u21E8 &eMust be placed &a3 Blocks &eabove the Reactor");
	
	public static ItemStack FREEZER = new CustomItem(Material.LIGHT_BLUE_STAINED_GLASS, "&bFreezer", "", "&6Advanced Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7256 J Buffer", "&8\u21E8 &e\u26A1 &718 J/s");;
	public static ItemStack FREEZER_2 = new CustomItem(Material.LIGHT_BLUE_STAINED_GLASS, "&bFreezer &7(&eII&7)", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 2x", "&8\u21E8 &e\u26A1 &7256 J Buffer", "&8\u21E8 &e\u26A1 &730 J/s");;
	
	public static ItemStack ELECTRIC_GOLD_PAN = new CustomItem(Material.BROWN_TERRACOTTA, "&6Electric Gold Pan", "", "&eBasic Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &72 J/s");
	public static ItemStack ELECTRIC_GOLD_PAN_2 = new CustomItem(Material.BROWN_TERRACOTTA, "&6Electric Gold Pan &7(&eII&7)", "", "&eBasic Machine", "&8\u21E8 &7Speed: 3x", "&8\u21E8 &e\u26A1 &74 J/s");
	public static ItemStack ELECTRIC_GOLD_PAN_3 = new CustomItem(Material.BROWN_TERRACOTTA, "&6Electric Gold Pan &7(&eIII&7)", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 10x", "&8\u21E8 &e\u26A1 &714 J/s");

	public static ItemStack ELECTRIC_DUST_WASHER = new CustomItem(Material.BLUE_STAINED_GLASS, "&3Electric Dust Washer", "", "&eBasic Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &76 J/s");
	public static ItemStack ELECTRIC_DUST_WASHER_2 = new CustomItem(Material.BLUE_STAINED_GLASS, "&3Electric Dust Washer &7(&eII&7)", "", "&eBasic Machine", "&8\u21E8 &7Speed: 2x", "&8\u21E8 &e\u26A1 &710 J/s");
	public static ItemStack ELECTRIC_DUST_WASHER_3 = new CustomItem(Material.BLUE_STAINED_GLASS, "&3Electric Dust Washer &7(&eIII&7)", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 10x", "&8\u21E8 &e\u26A1 &730 J/s");

	public static ItemStack ELECTRIC_INGOT_FACTORY = new CustomItem(Material.RED_TERRACOTTA, "&cElectric Ingot Factory", "", "&eBasic Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &78 J/s");
	public static ItemStack ELECTRIC_INGOT_FACTORY_2 = new CustomItem(Material.RED_TERRACOTTA, "&cElectric Ingot Factory &7(&eII&7)", "", "&eBasic Machine", "&8\u21E8 &7Speed: 2x", "&8\u21E8 &e\u26A1 &714 J/s");
	public static ItemStack ELECTRIC_INGOT_FACTORY_3 = new CustomItem(Material.RED_TERRACOTTA, "&cElectric Ingot Factory &7(&eIII&7)", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 8x", "&8\u21E8 &e\u26A1 &740 J/s");

	public static ItemStack AUTOMATED_CRAFTING_CHAMBER = new CustomItem(Material.CRAFTING_TABLE, "&6Automated Crafting Chamber", "", "&6Advanced Machine", "&8\u21E8 &e\u26A1 &710 J/Item");
	public static ItemStack FLUID_PUMP = new CustomItem(Material.BLUE_TERRACOTTA, "&9Fluid Pump", "", "&6Advanced Machine", "&8\u21E8 &e\u26A1 &732 J/Block");
	public static ItemStack CHARGING_BENCH = new CustomItem(Material.CRAFTING_TABLE, "&6Charging Bench", "", "&rCharges Items such as Jetpacks", "", "&eBasic Machine", "&8\u21E8 &e\u26A1 &7128 J Buffer", "&8\u21E8 &e\u26A1 &7Energy Loss: &c50%");

	public static ItemStack WITHER_ASSEMBLER = new CustomItem(Material.OBSIDIAN, "&5Wither Assembler", "", "&4End-Game Machine", "&8\u21E8 &7Cooldown: &b30 Seconds", "&8\u21E8 &e\u26A1 &74096 J Buffer", "&8\u21E8 &e\u26A1 &74096 J/Wither");
	
	public static ItemStack TRASH_CAN = null;
	
	public static ItemStack ELYTRA = new ItemStack(Material.ELYTRA);
	public static ItemStack ELYTRA_SCALE = new CustomItem(Material.FEATHER, "&bEyltra Scale");
	public static ItemStack INFUSED_ELYTRA = new CustomItem(new CustomItem(ELYTRA, "&5Infused Elytra"), new String[] {"MENDING-1"});
	public static ItemStack SOULBOUND_ELYTRA = new CustomItem(ELYTRA, "&cSoulbound Elytra");
	
	// ChestTerminal Addon
	
	public static ItemStack CHEST_TERMINAL = null;
	public static ItemStack CT_IMPORT_BUS = null;
	public static ItemStack CT_EXPORT_BUS = null;
	
	static {
		try {
			PORTABLE_CRAFTER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzJlYzRhNGJkOGE1OGY4MzYxZjhhMDMwM2UyMTk5ZDMzZDYyNGVhNWY5MmY3Y2IzNDE0ZmVlOTVlMmQ4NjEifX19"), "&6Portable Crafter", "&a&oA portable Crafting Table", "", "&eRight Click&7 to open");
			PORTABLE_DUSTBIN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJkNDEwNDJjZTk5MTQ3Y2MzOGNhYzllNDY3NDE1NzZlN2VlNzkxMjgzZTZmYWM4ZDMyOTJjYWUyOTM1ZjFmIn19fQ=="), "&6Portable Dustbin", "&rYour portable Item-Destroyer", "", "&eRight Click&7 to open");
			TRASH_CAN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJkNDEwNDJjZTk5MTQ3Y2MzOGNhYzllNDY3NDE1NzZlN2VlNzkxMjgzZTZmYWM4ZDMyOTJjYWUyOTM1ZjFmIn19fQ=="), "&3Trash Can", "", "&rWill destroy all Items put into it");
			CAN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTRkYTk3ZjA4MGUzOTViODQyYzRjYzgyYTg0MDgyM2Q0ZGJkOGNhNjg4YTIwNjg1M2U1NzgzZTRiZmRjMDEyIn19fQ=="), "&rTin Can");
			
			STONE_CHUNK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2U4ZjVhZGIxNGQ2YzlmNmI4MTBkMDI3NTQzZjFhOGMxZjQxN2UyZmVkOTkzYzk3YmNkODljNzRmNWUyZTgifX19"), "&6Stone Chunk");
			
			INFUSED_MAGNET = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ=="), "&aInfused Magnet" , "", "&rMagical infused Magnets", "&rattract nearby Items", "&ras long as it is somewhere in", "&ryour Inventory", "", "&7Hold &eShift&7 to pick up nearby Items");
			MAGNET = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ=="), "&cMagnet");
			ELECTRO_MAGNET = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ=="), "&cElectromagnet");
			ELECTRIC_MOTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ=="), "&cElectric Motor");
			CARGO_MOTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ=="), "&3Cargo Motor");
			
			BACKPACK_SMALL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&eSmall Backpack", new String[] {"", "&7Size: &e9", "&7ID: <ID>", "", "&7&eRight Click&7 to open"});
			BACKPACK_MEDIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&eBackpack", new String[] {"", "&7Size: &e18", "&7ID: <ID>", "", "&7&eRight Click&7 to open"});
			BACKPACK_LARGE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&eLarge Backpack", new String[] {"", "&7Size: &e27", "&7ID: <ID>", "", "&7&eRight Click&7 to open"});
			WOVEN_BACKPACK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&eWoven Backpack", new String[] {"", "&7Size: &e36", "&7ID: <ID>", "", "&7&eRight Click&7 to open"});
			GILDED_BACKPACK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBjYjFlNjdiNTEyYWIyZDRiZjNkN2FjZTBlYWFmNjFjMzJjZDQ2ODFkZGMzOTg3Y2ViMzI2NzA2YTMzZmEifX19"), "&eGilded Backpack", new String[] {"", "&7Size: &e45", "&7ID: <ID>", "", "&7&eRight Click&7 to open"});
			BOUND_BACKPACK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&cSoulbound Backpack", new String[] {"", "&7Size: &e36", "&7ID: <ID>", "", "&7&eRight Click&7 to open"});
			COOLER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDRjMTU3MjU4NGViNWRlMjI5ZGU5ZjVhNGY3NzlkMGFhY2JhZmZkMzNiY2IzM2ViNDUzNmE2YTJiYzZhMSJ9fX0="), "&bCooler", new String[] {"&rAllows you to store Juices/Smoothies", "&rand automatically consumes them when you are hungry", "&rand you have this in your Inventory", "", "&7Size: &e27", "&7ID: <ID>", "", "&7&eRight Click&7 to open"});
			ENDER_BACKPACK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&6Ender Backpack", new String[] {"&a&oA portable Ender Chest", "", "&eRight Click&7 to open"});

			VOIDBAG_SMALL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&4Small Void Bag", new String[] {"", "&7Size: &e9", "&7ID: <ID>", "", "&7&eLeft Click&7 to suck up nearby Items", "&7&eRight Click&7 to open"});
			VOIDBAG_MEDIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&4Void Bag", new String[] {"", "&7Size: &e18", "&7ID: <ID>", "", "&7&eLeft Click&7 to suck up nearby Items", "&7&eRight Click&7 to open"});
			VOIDBAG_BIG = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&4Big Void Bag", new String[] {"", "&7Size: &e27", "&7ID: <ID>", "", "&7&eLeft Click&7 to suck up nearby Items", "&7&eRight Click&7 to open"});
			VOIDBAG_LARGE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&4Large Void Bag", new String[] {"", "&7Size: &e36", "&7ID: <ID>", "", "&7&eLeft Click&7 to suck up nearby Items", "&7&eRight Click&7 to open"});
			BOUND_VOIDBAG = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&4Soulbound Void Bag", new String[] {"", "&7Size: &e36", "&7ID: <ID>", "", "&7&eLeft Click&7 to suck up nearby Items", "&7&eRight Click&7 to open"});
			
			COAL_GENERATOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&cCoal Generator", "", "&6Average Generator", "&8\u21E8 &e\u26A1 &764 J Buffer", "&8\u21E8 &e\u26A1 &716 J/s");
			LAVA_GENERATOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&4Lava Generator", "", "&6Average Generator", "&8\u21E8 &e\u26A1 &7512 J Buffer", "&8\u21E8 &e\u26A1 &720 J/s");
			COMBUSTION_REACTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&cCombustion Reactor", "", "&6Advanced Generator", "&8\u21E8 &e\u26A1 &7256 J Buffer", "&8\u21E8 &e\u26A1 &724 J/s");

			NUCLEAR_REACTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&2Nuclear Reactor", "", "&rRequires Cooling!", "&8\u21E8 &bMust be surrounded by Water", "&8\u21E8 &bMust be supplied with Reactor Coolant Cells", "", "&4End-Game Generator", "&8\u21E8 &e\u26A1 &716384 J Buffer", "&8\u21E8 &e\u26A1 &7500 J/s");
			NETHERSTAR_REACTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&fNether Star Reactor", "", "&fRuns on Nether Stars", "&8\u21E8 &bMust be surrounded by Water", "&8\u21E8 &bMust be supplied with Nether Ice Coolant Cells", "", "&4End-Game Generator", "&8\u21E8 &e\u26A1 &732768 J Buffer", "&8\u21E8 &e\u26A1 &71024 J/s", "&8\u21E8 &4Causes nearby Entities to get Withered");

			SMALL_CAPACITOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ=="), "&aSmall Energy Capacitor", "", "&eBasic Capacitor", "&8\u21E8 &e\u26A1 &7128 J Capacity");
			MEDIUM_CAPACITOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ=="), "&aMedium Energy Capacitor", "", "&6Average Capacitor", "&8\u21E8 &e\u26A1 &7512 J Capacity");
			BIG_CAPACITOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ=="), "&aBig Energy Capacitor", "", "&aMedium Capacitor", "&8\u21E8 &e\u26A1 &71024 J Capacity");
			LARGE_CAPACITOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ=="), "&aLarge Energy Capacitor", "", "&2Good Capacitor", "&8\u21E8 &e\u26A1 &78192 J Capacity");
			CARBONADO_EDGED_CAPACITOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ=="), "&aCarbonado Edged Energy Capacitor", "", "&4End-Game Capacitor", "&8\u21E8 &e\u26A1 &765536 J Capacity");
			CHEESE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRmZWJiYzE1ZDFkNGNjNjJiZWRjNWQ3YTJiNmYwZjQ2Y2Q1YjA2OTZhODg0ZGU3NWUyODllMzVjYmI1M2EwIn19fQ=="), "&rCheese");
			BUTTER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY2YjE5ZjdkNjM1ZDAzNDczODkxZGYzMzAxN2M1NDkzNjMyMDlhOGY2MzI4YTg1NDJjMjEzZDA4NTI1ZSJ9fX0="), "&rButter");
			DUCT_TAPE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJmYWFjZWFiNjM4NGZmZjVlZDI0YmI0NGE0YWYyZjU4NGViMTM4MjcyOWVjZDkzYTUzNjlhY2ZkNjY1NCJ9fX0="), "&8Duct Tape", "", "&rYou can repair Items using this", "&rin an Auto-Anvil");
		
			URANIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiMjlhZmE2ZDZkYzkyM2UyZTEzMjRiZjgxOTI3NTBmN2JkYmRkYzY4OTYzMmEyYjZjMThkOWZlN2E1ZSJ9fX0="), "&4Uranium", "", "&2Radiation Level: HIGH", "&4&oHazmat Suit required");
			SMALL_URANIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiMjlhZmE2ZDZkYzkyM2UyZTEzMjRiZjgxOTI3NTBmN2JkYmRkYzY4OTYzMmEyYjZjMThkOWZlN2E1ZSJ9fX0="), "&cSmall Chunk of Uranium", "", "&eRadiation Level: MODERATE", "&4&oHazmat Suit required");
			TINY_URANIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiMjlhZmE2ZDZkYzkyM2UyZTEzMjRiZjgxOTI3NTBmN2JkYmRkYzY4OTYzMmEyYjZjMThkOWZlN2E1ZSJ9fX0="), "&cTiny Pile of Uranium", "", "&cRadiation Level: LOW", "&4&oNo Hazmat Suit required");

			NEPTUNIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVkZWE2YmZkMzdlNDlkZTQzZjE1NGZlNmZjYTYxN2Q0MTI5ZTYxYjk1NzU5YTNkNDlhMTU5MzVhMWMyZGNmMCJ9fX0="), "&aNeptunium", "", "&2Radiation Level: HIGH", "&4&oHazmat Suit required");
			PLUTONIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjZjkxYjczODg2NjVhNmQ3YzFiNjAyNmJkYjIzMjJjNmQyNzg5OTdhNDQ0Nzg2NzdjYmNjMTVmNzYxMjRmIn19fQ=="), "&7Plutonium", "", "&2Radiation Level: HIGH", "&4&oHazmat Suit required");
			BOOSTED_URANIUM = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgzN2NhMTJmMjIyZjQ3ODcxOTZhMTdiOGFiNjU2OTg1Zjg0MDRjNTA3NjdhZGJjYjZlN2YxNDI1NGZlZSJ9fX0="), "&2Boosted Uranium", "", "&2Radiation Level: HIGH", "&4&oHazmat Suit required");
			
			PROGRAMMABLE_ANDROID = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUwM2NiN2VkODQ1ZTdhNTA3ZjU2OWFmYzY0N2M0N2FjNDgzNzcxNDY1YzlhNjc5YTU0NTk0Yzc2YWZiYSJ9fX0="), "&cProgrammable Android &7(Normal)", "", "&8\u21E8 &7Function: None", "&8\u21E8 &7Fuel Efficiency: 1.0x");
			PROGRAMMABLE_ANDROID_FARMER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlkMzMzNTdlODQxODgyM2JmNzgzZGU5MmRlODAyOTFiNGViZDM5MmFlYzg3MDY2OThlMDY4OTZkNDk4ZjYifX19"), "&cProgrammable Android &7(Farmer)", "", "&8\u21E8 &7Function: Farming", "&8\u21E8 &7Fuel Efficiency: 1.0x");
			PROGRAMMABLE_ANDROID_MINER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTYzOGEyODU0MWFiM2FlMGE3MjNkNTU3ODczOGUwODc1ODM4OGVjNGMzMzI0N2JkNGNhMTM0ODJhZWYzMzQifX19"), "&cProgrammable Android &7(Miner)", "", "&8\u21E8 &7Function: Mining", "&8\u21E8 &7Fuel Efficiency: 1.0x");
			PROGRAMMABLE_ANDROID_WOODCUTTER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDMyYTgxNDUxMDE0MjIwNTE2OWExYWQzMmYwYTc0NWYxOGU5Y2I2YzY2ZWU2NGVjYTJlNjViYWJkZWY5ZmYifX19"), "&cProgrammable Android &7(Woodcutter)", "", "&8\u21E8 &7Function: Woodcutting", "&8\u21E8 &7Fuel Efficiency: 1.0x");
			PROGRAMMABLE_ANDROID_BUTCHER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NzJkZjBhZDlhM2JlODhmMmU1ZDVkNDIyZDAyYjExNmQ2NGQ4ZGYxNDc1ZWQzMmU1NDZhZmM4NGIzMSJ9fX0="), "&cProgrammable Android &7(Butcher)", "", "&8\u21E8 &7Function: Slaughtering", "&8\u21E8 &7Damage: 4", "&8\u21E8 &7Fuel Efficiency: 1.0x");
			PROGRAMMABLE_ANDROID_FISHERMAN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ1ZTg3MzNhNzMxMTQzMzNiOThiMzYwMTc1MTI0MTcyMmY0NzEzZTFhMWE1ZDM2ZmJiMTMyNDkzZjFjNyJ9fX0="), "&cProgrammable Android &7(Fisherman)", "", "&8\u21E8 &7Function: Fishing", "&8\u21E8 &7Success Rate: 10%", "&8\u21E8 &7Fuel Efficiency: 1.0x");
			
			PROGRAMMABLE_ANDROID_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUwM2NiN2VkODQ1ZTdhNTA3ZjU2OWFmYzY0N2M0N2FjNDgzNzcxNDY1YzlhNjc5YTU0NTk0Yzc2YWZiYSJ9fX0="), "&cAdvanced Programmable Android &7(Normal)", "", "&8\u21E8 &7Function: None", "&8\u21E8 &7Fuel Efficiency: 1.5x");
			PROGRAMMABLE_ANDROID_2_FISHERMAN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ1ZTg3MzNhNzMxMTQzMzNiOThiMzYwMTc1MTI0MTcyMmY0NzEzZTFhMWE1ZDM2ZmJiMTMyNDkzZjFjNyJ9fX0="), "&cAdvanced Programmable Android &7(Fisherman)", "", "&8\u21E8 &7Function: Fishing", "&8\u21E8 &7Success Rate: 20%", "&8\u21E8 &7Fuel Efficiency: 1.5x");
			PROGRAMMABLE_ANDROID_2_FARMER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlkMzMzNTdlODQxODgyM2JmNzgzZGU5MmRlODAyOTFiNGViZDM5MmFlYzg3MDY2OThlMDY4OTZkNDk4ZjYifX19"), "&cAdvanced Programmable Android &7(Farmer)", "", "&8\u21E8 &7Function: Farming", "&8\u21E8 &7Fuel Efficiency: 1.5x", "&8\u21E8 &7Can also harvest Plants from ExoticGarden");
			PROGRAMMABLE_ANDROID_2_BUTCHER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NzJkZjBhZDlhM2JlODhmMmU1ZDVkNDIyZDAyYjExNmQ2NGQ4ZGYxNDc1ZWQzMmU1NDZhZmM4NGIzMSJ9fX0="), "&cAdvanced Programmable Android &7(Butcher)", "", "&8\u21E8 &7Function: Slaughtering", "&8\u21E8 &7Damage: 8", "&8\u21E8 &7Fuel Efficiency: 1.5x");

			PROGRAMMABLE_ANDROID_3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUwM2NiN2VkODQ1ZTdhNTA3ZjU2OWFmYzY0N2M0N2FjNDgzNzcxNDY1YzlhNjc5YTU0NTk0Yzc2YWZiYSJ9fX0="), "&eEmpowered Programmable Android &7(Normal)", "", "&8\u21E8 &7Function: None", "&8\u21E8 &7Fuel Efficiency: 3.0x");
			PROGRAMMABLE_ANDROID_3_FISHERMAN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ1ZTg3MzNhNzMxMTQzMzNiOThiMzYwMTc1MTI0MTcyMmY0NzEzZTFhMWE1ZDM2ZmJiMTMyNDkzZjFjNyJ9fX0="), "&eEmpowered Programmable Android &7(Fisherman)", "", "&8\u21E8 &7Function: Fishing", "&8\u21E8 &7Success Rate: 30%", "&8\u21E8 &7Fuel Efficiency: 8.0x");
			PROGRAMMABLE_ANDROID_3_BUTCHER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NzJkZjBhZDlhM2JlODhmMmU1ZDVkNDIyZDAyYjExNmQ2NGQ4ZGYxNDc1ZWQzMmU1NDZhZmM4NGIzMSJ9fX0="), "&eEmpowered Programmable Android &7(Butcher)", "", "&8\u21E8 &7Function: Slaughtering", "&8\u21E8 &7Damage: 20", "&8\u21E8 &7Fuel Efficiency: 8.0x");
			
			GPS_TRANSMITTER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&bGPS Transmitter", "", "&8\u21E8 &e\u26A1 &716 J Buffer", "&8\u21E8 &e\u26A1 &72 J/s");
			GPS_TRANSMITTER_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&cAdvanced GPS Transmitter", "", "&8\u21E8 &e\u26A1 &764 J Buffer", "&8\u21E8 &e\u26A1 &76 J/s");
			GPS_TRANSMITTER_3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&4Carbonado GPS Transmitter", "", "&8\u21E8 &e\u26A1 &7256 J Buffer", "&8\u21E8 &e\u26A1 &722 J/s");
			GPS_TRANSMITTER_4 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&eEnergized GPS Transmitter", "", "&8\u21E8 &e\u26A1 &71024 J Buffer", "&8\u21E8 &e\u26A1 &792 J/s");
			
			GPS_CONTROL_PANEL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGRjZmJhNThmYWYxZjY0ODQ3ODg0MTExODIyYjY0YWZhMjFkN2ZjNjJkNDQ4MWYxNGYzZjNiY2I2MzMwIn19fQ=="), "&bGPS Control Panel", "", "&rAllows you to trach your Satellites", "&rand manage your Waypoints");
			GPS_EMERGENCY_TRANSMITTER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjOWMxYTAyMmY0MGI3M2YxNGI0Y2JhMzdjNzE4YzZhNTMzZjNhMjg2NGI2NTM2ZDVmNDU2OTM0Y2MxZiJ9fX0="), "&cGPS Emergency Transmitter", "", "&rCarrying this in your Inventory", "&rautomatically sets a Waypoint", "&rat your Location when you die.");
			
			GPS_GEO_SCANNER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkOGNmZWIzODdhNTZlM2U1YmNmODUzNDVkNmE0MTdiMjQyMjkzODg3ZGIzY2UzYmE5MWZhNDA5YjI1NGI4NiJ9fX0="), "&bGPS Geo-Scanner", "", "&rScans a Chunk for natural Resources", "&rsuch as &8Oil");
			OIL_PUMP = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZlMWEwNDBhNDI1ZTMxYTQ2ZDRmOWE5Yjk4MDZmYTJmMGM0N2VlODQ3MTFjYzE5MzJmZDhhYjMyYjJkMDM4In19fQ=="), "&rOil Pump", "", "&7Pumps up Oil and fills it into Buckets", "", "&c&l! &cMake sure to Geo-Scan the Chunk first");
			BUCKET_OF_OIL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlMDRiNDFkMTllYzc5MjdmOTgyYTYzYTk0YTNkNzlmNzhlY2VjMzMzNjMwNTFmZGUwODMxYmZhYmRiZCJ9fX0="), "&rBucket of Oil");
			BUCKET_OF_FUEL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg0ZGRjYTc2NjcyNWI4Yjk3NDEzZjI1OWMzZjc2NjgwNzBmNmFlNTU0ODNhOTBjOGU1NTI1Mzk0ZjljMDk5In19fQ=="), "&rBucket of Fuel");

			NETHER_ICE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2NlMmRhZDliYWY3ZWFiYTdlODBkNGQwZjlmYWMwYWFiMDFhNzZiMTJmYjcxYzNkMmFmMmExNmZkZDRjNzM4MyJ9fX0="), "&eNether Ice", "", "&eRadiation Level: MODERATE", "&4&oHazmat Suit required");
			ENRICHED_NETHER_ICE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M4MThhYTEzYWFiYzcyOTQ4MzhkMjFjYWFjMDU3ZTk3YmQ4Yzg5NjQxYTBjMGY4YTU1NDQyZmY0ZTI3In19fQ=="), "&eEnriched Nether Ice", "", "&2Radiation Level: EXTREMELY HIGH", "&4&oHazmat Suit required");

			LAVA_CRYSTAL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNhZDhlZTg0OWVkZjA0ZWQ5YTI2Y2EzMzQxZjYwMzNiZDc2ZGNjNDIzMWVkMWVhNjNiNzU2NTc1MWIyN2FjIn19fQ=="), "&4Lava Crystal");
			ANDROID_MEMORY_CORE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc4ZjJiN2U1ZTc1NjM5ZWE3ZmI3OTZjMzVkMzY0YzRkZjI4YjQyNDNlNjZiNzYyNzdhYWRjZDYyNjEzMzcifX19"), "&bAndroid Memory Core");

			CARBON = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGIzYTA5NWI2YjgxZTZiOTg1M2ExOTMyNGVlZGYwYmI5MzQ5NDE3MjU4ZGQxNzNiOGVmZjg3YTA4N2FhIn19fQ=="), "&eCarbon");
			COMPRESSED_CARBON = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIxZDQ5NTE2NTc0OGQzMTE2Zjk5ZDZiNWJkNWQ0MmViOGJhNTkyYmNkZmFkMzdmZDk1ZjliNmMwNGEzYiJ9fX0="), "&cCompressed Carbon");
			CARBON_CHUNK = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIxZDQ5NTE2NTc0OGQzMTE2Zjk5ZDZiNWJkNWQ0MmViOGJhNTkyYmNkZmFkMzdmZDk1ZjliNmMwNGEzYiJ9fX0="), "&4Carbon Chunk");
			
			SYNTHETIC_SAPPHIRE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM1MDMyZjRkN2QwMWRlOGVjOTlkODlmODcyMzAxMmQ0ZTc0ZmE3MzAyMmM0ZmFjZjFiNTdjN2ZmNmZmMCJ9fX0="), "&bSynthetic Sapphire");
			CARBONADO = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTJmNGIxNTc3ZjUxNjBjNjg5MzE3MjU3MWM0YTcxZDhiMzIxY2RjZWFhMDMyYzZlMGUzYjYwZTBiMzI4ZmEifX19"), "&b&lCarbonado", "", "&7&o\"Black Diamond\"");
			RAW_CARBONADO = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWI0OWU2ZWMxMDc3MWU4OTkyMjVhZWE3M2NkOGNmMDM2ODRmNDExZDE0MTVjNzMyM2M5M2NiOTQ3NjIzMCJ9fX0="), "&bRaw Carbonado");
			
			ENERGY_REGULATOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc4ZjJiN2U1ZTc1NjM5ZWE3ZmI3OTZjMzVkMzY0YzRkZjI4YjQyNDNlNjZiNzYyNzdhYWRjZDYyNjEzMzcifX19"), "&6Energy Regulator", "", "&rCore Component of an Energy Network");
			
			CARGO_MANAGER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTUxMGJjODUzNjJhMTMwYTZmZjlkOTFmZjExZDZmYTQ2ZDdkMTkxMmEzNDMxZjc1MTU1OGVmM2M0ZDljMiJ9fX0="), "&6Cargo Manager", "", "&rCore Component of an Item Transport Network");
			CARGO_NODE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDdiN2VmNmZkNzg2NDg2NWMzMWMxZGM4N2JlZDI0YWI1OTczNTc5ZjVjNjYzOGZlY2I4ZGVkZWI0NDNmZjAifX19"), "&7Cargo Node &c(Connector)", "", "&rCargo Connector Pipe");
			CARGO_INPUT = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZkMWMxYTY5YTNkZTlmZWM5NjJhNzdiZjNiMmUzNzZkZDI1Yzg3M2EzZDhmMTRmMWRkMzQ1ZGFlNGM0In19fQ=="), "&7Cargo Node &c(Input)", "", "&rCargo Input Pipe");
			CARGO_OUTPUT = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTViMjFmZDQ4MGMxYzQzYmYzYjlmODQyYzg2OWJkYzNiYzVhY2MyNTk5YmYyZWI2YjhhMWM5NWRjZTk3OGYifX19"), "&7Cargo Node &c(Output)", "", "&rCargo Output Pipe");
			CARGO_OUTPUT_ADVANCED = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTViMjFmZDQ4MGMxYzQzYmYzYjlmODQyYzg2OWJkYzNiYzVhY2MyNTk5YmYyZWI2YjhhMWM5NWRjZTk3OGYifX19"), "&6Advanced Cargo Node &c(Output)", "", "&rCargo Output Pipe");

			XP_COLLECTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTc2MmExNWIwNDY5MmEyZTRiM2ZiMzY2M2JkNGI3ODQzNGRjZTE3MzJiOGViMWM3YTlmN2MwZmJmNmYifX19"), "&aEXP Collector", "", "&rCollects nearby Exp and stores it", "", "&4End-Game Machine", "&8\u21E8 &e\u26A1 &71024 J Buffer", "&8\u21E8 &e\u26A1 &720 J/s");
			
			ORGANIC_FOOD = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Food", "&7Content: &9X");
			WHEAT_ORGANIC_FOOD = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Food", "&7Content: &9Wheat");
			CARROT_ORGANIC_FOOD = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Food", "&7Content: &9Carrots");
			POTATO_ORGANIC_FOOD = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Food", "&7Content: &9Potatoes");
			SEEDS_ORGANIC_FOOD = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Food", "&7Content: &9Seeds");
			BEETROOT_ORGANIC_FOOD = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Food", "&7Content: &9Beetroot");
			MELON_ORGANIC_FOOD = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Food", "&7Content: &9Melon");
			APPLE_ORGANIC_FOOD = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Food", "&7Content: &9Apple");

			FERTILIZER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Fertilizer", "&7Content: &9X");
			WHEAT_FERTILIZER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Fertilizer", "&7Content: &9Wheat");
			CARROT_FERTILIZER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Fertilizer", "&7Content: &9Carrots");
			POTATO_FERTILIZER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Fertilizer", "&7Content: &9Potatoes");
			SEEDS_FERTILIZER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Fertilizer", "&7Content: &9Seeds");
			BEETROOT_FERTILIZER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Fertilizer", "&7Content: &9Beetroot");
			MELON_FERTILIZER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Fertilizer", "&7Content: &9Melon");
			APPLE_FERTILIZER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzOWUzZjVhY2JlZTliZTRjNDI1OTI4OWQ2ZDlmMzVjNjM1ZmZhNjYxMTE0Njg3YjNlYTZkZGE4Yzc5In19fQ=="), "&aOrganic Fertilizer", "&7Content: &9Apple");

			NETHER_ICE_COOLANT_CELL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQzY2Q0MTI1NTVmODk3MDE2MjEzZTVkNmM3NDMxYjQ0OGI5ZTU2NDRlMWIxOWVjNTFiNTMxNmYzNTg0MGUwIn19fQ=="), "&6Nether Ice Coolant Cell");
			REACTOR_COOLANT_CELL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGU0MDczYmU0MGNiM2RlYjMxMGEwYmU5NTliNGNhYzY4ZTgyNTM3MjcyOGZhZmI2YzI5NzNlNGU3YzMzIn19fQ=="), "&bReactor Coolant Cell");

			CHEST_TERMINAL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E0NGZmM2E1ZjQ5YzY5Y2FiNjc2YmFkOGQ5OGEwNjNmYTc4Y2ZhNjE5MTZmZGVmM2UyNjc1NTdmZWMxODI4MyJ9fX0="), "&3CT Access Terminal", "&7If this Block is connected to a", "&7Cargo Network, it will allow you to remotely", "&7interact with any Items supplied by", "&7Nodes tuned into the ChestTerminal Channel");
			CT_IMPORT_BUS = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTEzZGIyZTdlNzJlYTQ0MzJlZWZiZDZlNThhODVlYWEyNDIzZjgzZTY0MmNhNDFhYmM2YTkzMTc3NTdiODg5In19fQ=="), "&3CT Import Bus", "&7If this Block is connected to a", "&7Cargo Network, it will pull any Items from", "&7the Inventory it is attached to and place it", "&7into the CT Network Channel");
			CT_EXPORT_BUS = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTEzZGIyZTdlNzJlYTQ0MzJlZWZiZDZlNThhODVlYWEyNDIzZjgzZTY0MmNhNDFhYmM2YTkzMTc3NTdiODg5In19fQ=="), "&3CT Export Bus", "&7If this Block is connected to a", "&7Cargo Network, it will pull any Items from", "&7the CT Network Channel and place these", "&7into the Inventory it is attached to");
			
			BATTERY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmUyZGRhNmVmNjE4NWQ0ZGQ2ZWE4Njg0ZTk3ZDM5YmE4YWIwMzdlMjVmNzVjZGVhNmJkMjlkZjhlYjM0ZWUifX19"), "&6Battery");
			
			HEATING_COIL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2UzYmM0ODkzYmE0MWEzZjczZWUyODE3NGNkZjRmZWY2YjE0NWU0MWZlNmM4MmNiN2JlOGQ4ZTk3NzFhNSJ9fX0="), "&cHeating Coil");
			COOLING_UNIT = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU0YmFkODZjOTlkZjc4MGM4ODlhMTA5OGY3NzY0OGVhZDczODVjYzFkZGIwOTNkYTVhN2Q4YzRjMmFlNTRkIn19fQ=="), "&bCooling Unit");
			POWER_CRYSTAL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNjMWIwMzZiNmUwMzUxN2IyODVhODExYmQ4NWU3M2Y1YWJmZGFjYzFkZGY5MGRmZjk2MmUxODA5MzRlMyJ9fX0="), "&c&lPower Crystal");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
