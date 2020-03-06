package me.mrCookieSlime.Slimefun.Lists;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.StormStaff;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This class holds a static references to every {@link SlimefunItemStack}
 * found in Slimefun.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class SlimefunItems {

    private SlimefunItems() {}

    /* Items */
    public static final ItemStack PORTABLE_CRAFTER = new SlimefunItemStack("PORTABLE_CRAFTER", "72ec4a4bd8a58f8361f8a0303e2199d33d624ea5f92f7cb3414fee95e2d861", "&6Portable Crafter", "&a&oA portable Crafting Table", "", "&eRight Click&7 to open");
    public static final ItemStack PORTABLE_DUSTBIN = new SlimefunItemStack("PORTABLE_DUSTBIN", "32d41042ce99147cc38cac9e46741576e7ee791283e6fac8d3292cae2935f1f", "&6Portable Dustbin", "&rYour portable Item-Destroyer", "", "&eRight Click&7 to open");
    public static final ItemStack ENDER_BACKPACK = new SlimefunItemStack("ENDER_BACKPACK", "2a3b34862b9afb63cf8d5779966d3fba70af82b04e83f3eaf6449aeba", "&6Ender Backpack", "&a&oA portable Ender Chest", "", "&eRight Click&7 to open");
    public static final ItemStack MAGIC_EYE_OF_ENDER = new SlimefunItemStack("MAGIC_EYE_OF_ENDER", Material.ENDER_EYE, "&6&lMagic Eye of Ender", "&4&lRequires full Ender Armor", "", "&7&eRight Click&7 to shoot an Ender Pearl");
    public static final ItemStack BROKEN_SPAWNER = new SlimefunItemStack("BROKEN_SPAWNER", Material.SPAWNER, "&cBroken Spawner", "&7Type: &b<Type>", "", "&cFractured, must be repaired in an Ancient Altar");
    public static final ItemStack REPAIRED_SPAWNER = new SlimefunItemStack("REINFORCED_SPAWNER", Material.SPAWNER, "&bReinforced Spawner", "&7Type: &b<Type>");
    public static final ItemStack INFERNAL_BONEMEAL = new SlimefunItemStack("INFERNAL_BONEMEAL", Material.BONE_MEAL, "&4Infernal Bonemeal", "", "&cSpeeds up the Growth of", "&cNether Warts as well");

    /* Gadgets */
    public static final ItemStack GOLD_PAN = new SlimefunItemStack("GOLD_PAN", Material.BOWL, "&6Gold Pan", "&a&oCan get you all kinds of Goodies...", "", "&7&eRight Click&7 to pan various Stuff out of Gravel");
    public static final ItemStack NETHER_GOLD_PAN = new SlimefunItemStack("NETHER_GOLD_PAN", Material.BOWL, "&4Nether Gold Pan", "", "&7&eRight Click&7 to pan various stuff out of Soul Sand");
    public static final ItemStack PARACHUTE = new SlimefunItemStack("PARACHUTE", Material.LEATHER_CHESTPLATE, Color.WHITE, "&r&lParachute", "", "&7Hold &eShift&7 to use");
    public static final ItemStack GRAPPLING_HOOK = new SlimefunItemStack("GRAPPLING_HOOK", Material.LEAD, "&6Grappling Hook", "", "&7&eRight Click&7 to use");
    public static final ItemStack SOLAR_HELMET = new SlimefunItemStack("SOLAR_HELMET", Material.IRON_HELMET, "&bSolar Helmet", "", "&a&oCharges held Items and Armor");
    public static final ItemStack CLOTH = new SlimefunItemStack("CLOTH", Material.PAPER, "&bCloth");
    public static final ItemStack CAN = new SlimefunItemStack("CAN", "94da97f080e395b842c4cc82a840823d4dbd8ca688a206853e5783e4bfdc012", "&rTin Can");
    public static final ItemStack NIGHT_VISION_GOGGLES = new SlimefunItemStack("NIGHT_VISION_GOGGLES", Material.LEATHER_HELMET, Color.BLACK, "&aNight Vision Goggles", "", "&9+ Night Vision");
    public static final ItemStack FARMER_SHOES = new SlimefunItemStack("FARMER_SHOES", Material.LEATHER_BOOTS, Color.YELLOW, "&eFarmer Shoes", "", "&6&oPrevents you from trampling your Crops");
    public static final ItemStack INFUSED_MAGNET = new SlimefunItemStack("INFUSED_MAGNET", "aba8ebc4c6a81730947499bf7e1d5e73fed6c1bb2c051e96d35eb16d24610e7", "&aInfused Magnet", "", "&rMagical infused Magnets", "&rattract nearby Items", "&ras long as it is somewhere in", "&ryour Inventory", "", "&7Hold &eShift&7 to pick up nearby Items");
    public static final ItemStack RAG = new SlimefunItemStack("RAG", Material.PAPER, "&cRag", "", "&aLevel I - Medical Supply", "", "&rRestores 2 Hearts", "&rExtinguishes Fire", "", "&7&eRight Click&7 to use");
    public static final ItemStack BANDAGE = new SlimefunItemStack("BANDAGE", Material.PAPER, "&cBandage", "", "&aLevel II - Medical Supply", "", "&rRestores 4 Hearts", "&rExtinguishes Fire", "", "&7&eRight Click&7 to use");
    public static final ItemStack SPLINT = new SlimefunItemStack("SPLINT", Material.STICK, "&cSplint", "", "&aLevel I - Medical Supply", "", "&rRestores 2 Hearts", "", "&7&eRight Click&7 to use");
    public static final ItemStack VITAMINS = new SlimefunItemStack("VITAMINS", Material.NETHER_WART, "&cVitamins", "", "&aLevel III - Medical Supply", "", "&rRestores 4 Hearts", "&rExtinguishes Fire", "&rCures Poison/Wither/Radiation", "", "&7&eRight Click&7 to use");
    public static final ItemStack MEDICINE = new SlimefunItemStack("MEDICINE", Material.POTION, Color.RED, "&cMedicine", "", "&aLevel III - Medical Supply", "", "&rRestores 4 Hearts", "&rExtinguishes Fire", "&rCures Poison/Wither/Radiation");

    public static final ItemStack FLASK_OF_KNOWLEDGE = new SlimefunItemStack("FLASK_OF_KNOWLEDGE", Material.GLASS_BOTTLE, "&cFlask of Knowledge", "", "&rAllows you to store some of", "&ryour Experience in a Bottle", "&7Cost: &a1 Level");
    public static final ItemStack FILLED_FLASK_OF_KNOWLEDGE = new SlimefunItemStack("FILLED_FLASK_OF_KNOWLEDGE", Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge");

    /* Backpacks */
    public static final ItemStack BACKPACK_SMALL = new SlimefunItemStack("SMALL_BACKPACK", "40cb1e67b512ab2d4bf3d7ace0eaaf61c32cd4681ddc3987ceb326706a33fa", "&eSmall Backpack", "", "&7Size: &e9", "&7ID: <ID>", "", "&7&eRight Click&7 to open");
    public static final ItemStack BACKPACK_MEDIUM = new SlimefunItemStack("MEDIUM_BACKPACK", "40cb1e67b512ab2d4bf3d7ace0eaaf61c32cd4681ddc3987ceb326706a33fa", "&eBackpack", "", "&7Size: &e18", "&7ID: <ID>", "", "&7&eRight Click&7 to open");
    public static final ItemStack BACKPACK_LARGE = new SlimefunItemStack("LARGE_BACKPACK", "40cb1e67b512ab2d4bf3d7ace0eaaf61c32cd4681ddc3987ceb326706a33fa", "&eLarge Backpack", "", "&7Size: &e27", "&7ID: <ID>", "", "&7&eRight Click&7 to open");
    public static final ItemStack WOVEN_BACKPACK = new SlimefunItemStack("WOVEN_BACKPACK", "40cb1e67b512ab2d4bf3d7ace0eaaf61c32cd4681ddc3987ceb326706a33fa", "&eWoven Backpack", "", "&7Size: &e36", "&7ID: <ID>", "", "&7&eRight Click&7 to open");
    public static final ItemStack GILDED_BACKPACK = new SlimefunItemStack("GILDED_BACKPACK", "40cb1e67b512ab2d4bf3d7ace0eaaf61c32cd4681ddc3987ceb326706a33fa", "&eGilded Backpack", "", "&7Size: &e45", "&7ID: <ID>", "", "&7&eRight Click&7 to open");
    public static final ItemStack RADIANT_BACKPACK = new SlimefunItemStack("RADIANT_BACKPACK", "40cb1e67b512ab2d4bf3d7ace0eaaf61c32cd4681ddc3987ceb326706a33fa", "&eRadiant Backpack", "", "&7Size: &e54 (Double chest)", "&7ID: <ID>", "", "&7&eRight Click&7 to open");
    public static final ItemStack BOUND_BACKPACK = new SlimefunItemStack("BOUND_BACKPACK", "2a3b34862b9afb63cf8d5779966d3fba70af82b04e83f3eaf6449aeba", "&cSoulbound Backpack", "", "&7Size: &e36", "&7ID: <ID>", "", "&7&eRight Click&7 to open");
    public static final ItemStack COOLER = new SlimefunItemStack("COOLER", "d4c1572584eb5de229de9f5a4f779d0aacbaffd33bcb33eb4536a6a2bc6a1", "&bCooler", "&rAllows you to store Juices/Smoothies", "&rand automatically consumes them when you are hungry", "&rand you have this in your Inventory", "", "&7Size: &e27", "&7ID: <ID>", "", "&7&eRight Click&7 to open");

    /* Jetpacks */
    public static final ItemStack DURALUMIN_JETPACK = new SlimefunItemStack("DURALUMIN_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Electric Jetpack &7- &eI", "", "&8\u21E8 &7Material: &bDuralumin", "&c&o&8\u21E8 &e\u26A1 &70 / 20 J", "&8\u21E8 &7Thrust: &c0.35", "", "&7Hold &eShift&7 to use");
    public static final ItemStack SOLDER_JETPACK = new SlimefunItemStack("SOLDER_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Electric Jetpack &7- &eII", "", "&8\u21E8 &7Material: &bSolder", "&c&o&8\u21E8 &e\u26A1 &70 / 30 J", "&8\u21E8 &7Thrust: &c0.4", "", "&7Hold &eShift&7 to use");
    public static final ItemStack BILLON_JETPACK = new SlimefunItemStack("BILLON_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Electric Jetpack &7- &eIII", "", "&8\u21E8 &7Material: &bBillon", "&c&o&8\u21E8 &e\u26A1 &70 / 45 J", "&8\u21E8 &7Thrust: &c0.45", "", "&7Hold &eShift&7 to use");
    public static final ItemStack STEEL_JETPACK = new SlimefunItemStack("STEEL_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Electric Jetpack &7- &eIV", "", "&8\u21E8 &7Material: &bSteel", "&c&o&8\u21E8 &e\u26A1 &70 / 60 J", "&8\u21E8 &7Thrust: &c0.5", "", "&7Hold &eShift&7 to use");
    public static final ItemStack DAMASCUS_STEEL_JETPACK = new SlimefunItemStack("DAMASCUS_STEEL_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Electric Jetpack &7- &eV", "", "&8\u21E8 &7Material: &bDamascus Steel", "&c&o&8\u21E8 &e\u26A1 &70 / 75 J", "&8\u21E8 &7Thrust: &c0.55", "", "&7Hold &eShift&7 to use");
    public static final ItemStack REINFORCED_ALLOY_JETPACK = new SlimefunItemStack("REINFORCED_ALLOY_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Electric Jetpack &7- &eVI", "", "&8\u21E8 &7Material: &bReinforced Alloy", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J", "&8\u21E8 &7Thrust: &c0.6", "", "&7Hold &eShift&7 to use");
    public static final ItemStack CARBONADO_JETPACK = new SlimefunItemStack("CARBONADO_JETPACK", Material.LEATHER_CHESTPLATE, Color.BLACK, "&9Electric Jetpack &7- &eVII", "", "&8\u21E8 &7Material: &bCarbonado", "&c&o&8\u21E8 &e\u26A1 &70 / 150 J", "&8\u21E8 &7Thrust: &c0.7", "", "&7Hold &eShift&7 to use");
    public static final ItemStack ARMORED_JETPACK = new SlimefunItemStack("ARMORED_JETPACK", Material.IRON_CHESTPLATE, "&9Armored Jetpack", "&8\u21E8 &7Material: &bSteel", "", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "&8\u21E8 &7Thrust: &c0.45", "", "&7Hold &eShift&7 to use");

    /* Jetboots */
    public static final ItemStack DURALUMIN_JETBOOTS = new SlimefunItemStack("DURALUMIN_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Jet Boots &7- &eI", "", "&8\u21E8 &7Material: &bDuralumin", "&c&o&8\u21E8 &e\u26A1 &70 / 20 J", "&8\u21E8 &7Speed: &a0.35", "&8\u21E8 &7Accuracy: &c50%", "", "&7Hold &eShift&7 to use");
    public static final ItemStack SOLDER_JETBOOTS = new SlimefunItemStack("SOLDER_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Jet Boots &7- &eII", "", "&8\u21E8 &7Material: &bSolder", "&c&o&8\u21E8 &e\u26A1 &70 / 30 J", "&8\u21E8 &7Speed: &a0.4", "&8\u21E8 &7Accuracy: &660%", "", "&7Hold &eShift&7 to use");
    public static final ItemStack BILLON_JETBOOTS = new SlimefunItemStack("BILLON_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Jet Boots &7- &eIII", "", "&8\u21E8 &7Material: &bBillon", "&c&o&8\u21E8 &e\u26A1 &70 / 40 J", "&8\u21E8 &7Speed: &a0.45", "&8\u21E8 &7Accuracy: &665%", "", "&7Hold &eShift&7 to use");
    public static final ItemStack STEEL_JETBOOTS = new SlimefunItemStack("STEEL_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Jet Boots &7- &eIV", "", "&8\u21E8 &7Material: &bSteel", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "&8\u21E8 &7Speed: &a0.5", "&8\u21E8 &7Accuracy: &e70%", "", "&7Hold &eShift&7 to use");
    public static final ItemStack DAMASCUS_STEEL_JETBOOTS = new SlimefunItemStack("DAMASCUS_STEEL_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Jet Boots &7- &eV", "", "&8\u21E8 &7Material: &bDamascus Steel", "&c&o&8\u21E8 &e\u26A1 &70 / 75 J", "&8\u21E8 &7Speed: &a0.55", "&8\u21E8 &7Accuracy: &a75%", "", "&7Hold &eShift&7 to use");
    public static final ItemStack REINFORCED_ALLOY_JETBOOTS = new SlimefunItemStack("REINFORCED_ALLOY_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Jet Boots &7- &eVI", "", "&8\u21E8 &7Material: &bReinforced Alloy", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J", "&8\u21E8 &7Speed: &a0.6", "&8\u21E8 &7Accuracy: &c80%", "", "&7Hold &eShift&7 to use");
    public static final ItemStack CARBONADO_JETBOOTS = new SlimefunItemStack("CARBONADO_JETBOOTS", Material.LEATHER_BOOTS, Color.BLACK, "&9Jet Boots &7- &eVII", "", "&8\u21E8 &7Material: &bCarbonado", "&c&o&8\u21E8 &e\u26A1 &70 / 125 J", "&8\u21E8 &7Speed: &a0.7", "&8\u21E8 &7Accuracy: &c99.9%", "", "&7Hold &eShift&7 to use");
    public static final ItemStack ARMORED_JETBOOTS = new SlimefunItemStack("ARMORED_JETBOOTS", Material.IRON_BOOTS, "&9Armored Jet Boots", "", "&8\u21E8 &7Material: &bSteel", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "&8\u21E8 &7Speed: &a0.45", "&8\u21E8 &7Accuracy: &e70%", "", "&7Hold &eShift&7 to use");

    /* Multi Tools */
    public static final ItemStack DURALUMIN_MULTI_TOOL = new SlimefunItemStack("DURALUMIN_MULTI_TOOL", Material.SHEARS, "&9Multi Tool &7- &eI", "", "&8\u21E8 &7Material: &bDuralumin", "&c&o&8\u21E8 &e\u26A1 &70 / 20 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
    public static final ItemStack SOLDER_MULTI_TOOL = new SlimefunItemStack("SOLDER_MULTI_TOOL", Material.SHEARS, "&9Multi Tool &7- &eII", "", "&8\u21E8 &7Material: &bSolder", "&c&o&8\u21E8 &e\u26A1 &70 / 30 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
    public static final ItemStack BILLON_MULTI_TOOL = new SlimefunItemStack("BILLON_MULTI_TOOL", Material.SHEARS, "&9Multi Tool &7- &eIII", "", "&8\u21E8 &7Material: &bBillon", "&c&o&8\u21E8 &e\u26A1 &70 / 40 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
    public static final ItemStack STEEL_MULTI_TOOL = new SlimefunItemStack("STEEL_MULTI_TOOL", Material.SHEARS, "&9Multi Tool &7- &eIV", "", "&8\u21E8 &7Material: &bSteel", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
    public static final ItemStack DAMASCUS_STEEL_MULTI_TOOL = new SlimefunItemStack("DAMASCUS_STEEL_MULTI_TOOL", Material.SHEARS, "&9Multi Tool &7- &eV", "", "&8\u21E8 &7Material: &bDamascus Steel", "&c&o&8\u21E8 &e\u26A1 &70 / 60 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
    public static final ItemStack REINFORCED_ALLOY_MULTI_TOOL = new SlimefunItemStack("REINFORCED_ALLOY_MULTI_TOOL", Material.SHEARS, "&9Multi Tool &7- &eVI", "", "&8\u21E8 &7Material: &bReinforced Alloy", "&c&o&8\u21E8 &e\u26A1 &70 / 75 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");
    public static final ItemStack CARBONADO_MULTI_TOOL = new SlimefunItemStack("CARBONADO_MULTI_TOOL", Material.SHEARS, "&9Multi Tool &7- &eVII", "", "&8\u21E8 &7Material: &bCarbonado", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J", "", "&7&eRight Click&7 to use", "&7Hold &eShift + Right Click&7 to change the Mode");

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

    /* Food */
    public static final ItemStack FORTUNE_COOKIE = new SlimefunItemStack("FORTUNE_COOKIE", Material.COOKIE, "&6Fortune Cookie", "", "&a&oTells you stuff about your Future :o");
    public static final ItemStack DIET_COOKIE = new SlimefunItemStack("DIET_COOKIE", Material.COOKIE, "&6Diet Cookie", "", "&aA very &olightweight &r&acookie.");
    public static final ItemStack MAGIC_SUGAR = new SlimefunItemStack("MAGIC_SUGAR", Material.SUGAR, "&6Magic Sugar", "", "&a&oFeel the Power of Hermes!");
    public static final ItemStack MONSTER_JERKY = new SlimefunItemStack("MONSTER_JERKY", Material.ROTTEN_FLESH, "&6Monster Jerky", "", "&a&oNo longer hungry");
    public static final ItemStack APPLE_JUICE = new SlimefunItemStack("APPLE_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&cApple Juice", "", "&7&oRestores &b&o" + "3.0" + " &7&oHunger");
    public static final ItemStack MELON_JUICE = new SlimefunItemStack("MELON_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&cMelon Juice", "", "&7&oRestores &b&o" + "3.0" + " &7&oHunger");
    public static final ItemStack CARROT_JUICE = new SlimefunItemStack("CARROT_JUICE", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&6Carrot Juice", "", "&7&oRestores &b&o" + "3.0" + " &7&oHunger");
    public static final ItemStack PUMPKIN_JUICE = new SlimefunItemStack("PUMPKIN_JUICE", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&6Pumpkin Juice", "", "&7&oRestores &b&o" + "3.0" + " &7&oHunger");
    public static final ItemStack SWEET_BERRY_JUICE = new SlimefunItemStack("SWEET_BERRY_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&cSweet Berry Juice", "", "&7&oRestores &b&o" + "3.0" + " &7&oHunger");
    public static final ItemStack GOLDEN_APPLE_JUICE = new SlimefunItemStack("GOLDEN_APPLE_JUICE", Color.YELLOW, new PotionEffect(PotionEffectType.ABSORPTION, 20 * 20, 0), "&bGolden Apple Juice");

    public static final ItemStack BEEF_JERKY = new SlimefunItemStack("BEEF_JERKY", Material.COOKED_BEEF, "&6Beef Jerky", "", "&rExtra saturating!");
    public static final ItemStack PORK_JERKY = new SlimefunItemStack("PORK_JERKY", Material.COOKED_PORKCHOP, "&6Pork Jerky", "", "&rExtra saturating!");
    public static final ItemStack CHICKEN_JERKY = new SlimefunItemStack("CHICKEN_JERKY", Material.COOKED_CHICKEN, "&6Chicken Jerky", "", "&rExtra saturating!");
    public static final ItemStack MUTTON_JERKY = new SlimefunItemStack("MUTTON_JERKY", Material.COOKED_MUTTON, "&6Mutton Jerky", "", "&rExtra saturating!");
    public static final ItemStack RABBIT_JERKY = new SlimefunItemStack("RABBIT_JERKY", Material.COOKED_RABBIT, "&6Rabbit Jerky", "", "&rExtra saturating!");
    public static final ItemStack FISH_JERKY = new SlimefunItemStack("FISH_JERKY", Material.COOKED_COD, "&6Fish Jerky", "", "&rExtra saturating!");

    public static final ItemStack KELP_COOKIE = new SlimefunItemStack("KELP_COOKIE", Material.COOKIE, "&2Kelp Cookie");

    /* Christmas */
    public static final ItemStack CHRISTMAS_MILK = new SlimefunItemStack("CHRISTMAS_MILK", Color.WHITE, new PotionEffect(PotionEffectType.SATURATION, 5, 0), "&6Glass of Milk", "", "&7&oRestores &b&o" + "2.5" + " &7&oHunger");
    public static final ItemStack CHRISTMAS_CHOCOLATE_MILK = new SlimefunItemStack("CHRISTMAS_CHOCOLATE_MILK", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 12, 0), "&6Chocolate Milk", "", "&7&oRestores &b&o" + "6.0" + " &7&oHunger");
    public static final ItemStack CHRISTMAS_EGG_NOG = new SlimefunItemStack("CHRISTMAS_EGG_NOG", Color.GRAY, new PotionEffect(PotionEffectType.SATURATION, 7, 0), "&aEgg Nog", "", "&7&oRestores &b&o" + "3.5" + " &7&oHunger");
    public static final ItemStack CHRISTMAS_APPLE_CIDER = new SlimefunItemStack("CHRISTMAS_APPLE_CIDER", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 14, 0), "&cApple Cider", "", "&7&oRestores &b&o" + "7.0" + " &7&oHunger");
    public static final ItemStack CHRISTMAS_COOKIE = new SlimefunItemStack("CHRISTMAS_COOKIE", Material.COOKIE, ChatUtils.christmas("Christmas Cookie"));
    public static final ItemStack CHRISTMAS_FRUIT_CAKE = new SlimefunItemStack("CHRISTMAS_FRUIT_CAKE", Material.PUMPKIN_PIE, ChatUtils.christmas("Fruit Cake"));
    public static final ItemStack CHRISTMAS_APPLE_PIE = new SlimefunItemStack("CHRISTMAS_APPLE_PIE", Material.PUMPKIN_PIE, "&rApple Pie");
    public static final ItemStack CHRISTMAS_HOT_CHOCOLATE = new SlimefunItemStack("CHRISTMAS_HOT_CHOCOLATE", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 14, 0), "&6Hot Chocolate", "", "&7&oRestores &b&o" + "7.0" + " &7&oHunger");
    public static final ItemStack CHRISTMAS_CAKE = new SlimefunItemStack("CHRISTMAS_CAKE", Material.PUMPKIN_PIE, ChatUtils.christmas("Christmas Cake"));
    public static final ItemStack CHRISTMAS_CARAMEL = new SlimefunItemStack("CHRISTMAS_CARAMEL", Material.BRICK, "&6Caramel");
    public static final ItemStack CHRISTMAS_CARAMEL_APPLE = new SlimefunItemStack("CHRISTMAS_CARAMEL_APPLE", Material.APPLE, "&6Caramel Apple");
    public static final ItemStack CHRISTMAS_CHOCOLATE_APPLE = new SlimefunItemStack("CHRISTMAS_CHOCOLATE_APPLE", Material.APPLE, "&6Chocolate Apple");
    public static final ItemStack CHRISTMAS_PRESENT = new SlimefunItemStack("CHRISTMAS_PRESENT", Material.CHEST, ChatUtils.christmas("Christmas Present"), "&7From: &emrCookieSlime", "&7To: &eYou", "", "&eRight Click&7 to open");

    /* Easter */
    public static final ItemStack EASTER_EGG = new SlimefunItemStack("EASTER_EGG", Material.EGG, "&rEaster Egg", "&bSurprise! Surprise!");
    public static final ItemStack EASTER_CARROT_PIE = new SlimefunItemStack("CARROT_PIE", Material.PUMPKIN_PIE, "&6Carrot Pie");
    public static final ItemStack EASTER_APPLE_PIE = new SlimefunItemStack("EASTER_APPLE_PIE", Material.PUMPKIN_PIE, "&rApple Pie");

    /* Weapons */
    public static final ItemStack GRANDMAS_WALKING_STICK = new SlimefunItemStack("GRANDMAS_WALKING_STICK", Material.STICK, "&7Grandmas Walking Stick");
    public static final ItemStack GRANDPAS_WALKING_STICK = new SlimefunItemStack("GRANDPAS_WALKING_STICK", Material.STICK, "&7Grandpas Walking Stick");
    public static final ItemStack SWORD_OF_BEHEADING = new SlimefunItemStack("SWORD_OF_BEHEADING", Material.IRON_SWORD, "&6Sword of Beheading", "&7Beheading II", "", "&rHas a chance to behead Mobs", "&r(even a higher chance for Wither Skeletons)");
    public static final ItemStack BLADE_OF_VAMPIRES = new SlimefunItemStack("BLADE_OF_VAMPIRES", Material.GOLDEN_SWORD, "&cBlade of Vampires", "&7Life Steal I", "", "&rEverytime you attack something", "&ryou have a 45% chance to", "&rrecover 2 Hearts of your Health");
    public static final ItemStack SEISMIC_AXE = new SlimefunItemStack("SEISMIC_AXE", Material.IRON_AXE, "&aSeismic Axe", "", "&7&oA portable Earthquake...", "", "&7&eRight Click&7 to use");

    static {
        GRANDMAS_WALKING_STICK.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        GRANDPAS_WALKING_STICK.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
    }

    /* Bows */
    public static final ItemStack EXPLOSIVE_BOW = new SlimefunItemStack("EXPLOSIVE_BOW", Material.BOW, "&cExplosive Bow", "&rAny Arrows fired using this Bow", "&rwill launch hit enemys into the air");
    public static final ItemStack ICY_BOW = new SlimefunItemStack("ICY_BOW", Material.BOW, "&bIcy Bow", "&rAny Arrows fired using this Bow", "&rwill prevent hit enemys from moving", "&rfor 2 seconds");

    /* Tools */
    public static final ItemStack AUTO_SMELT_PICKAXE = new SlimefunItemStack("SMELTERS_PICKAXE", Material.DIAMOND_PICKAXE, "&6Smelter's Pickaxe", "&c&lAuto-Smelting", "", "&9Works with Fortune");
    public static final ItemStack LUMBER_AXE = new SlimefunItemStack("LUMBER_AXE", Material.DIAMOND_AXE, "&6Lumber Axe", "&a&oCuts down the whole Tree...");
    public static final ItemStack PICKAXE_OF_CONTAINMENT = new SlimefunItemStack("PICKAXE_OF_CONTAINMENT", Material.IRON_PICKAXE, "&cPickaxe of Containment", "", "&9Can pickup Spawners");
    public static final ItemStack HERCULES_PICKAXE = new SlimefunItemStack("HERCULES_PICKAXE", Material.IRON_PICKAXE, "&9Hercules' Pickaxe", "", "&rSo powerful that it", "&rcrushes all mined Ores", "&rinto Dust...");
    public static final ItemStack EXPLOSIVE_PICKAXE = new SlimefunItemStack("EXPLOSIVE_PICKAXE", Material.DIAMOND_PICKAXE, "&eExplosive Pickaxe", "", "&rAllows you to mine a good bit", "&rof Blocks at once...", "", "&9Works with Fortune");
    public static final ItemStack EXPLOSIVE_SHOVEL = new SlimefunItemStack("EXPLOSIVE_SHOVEL", Material.DIAMOND_SHOVEL, "&eExplosive Shovel", "", "&rAllows you to mine a good bit", "&rof diggable Blocks at once...");
    public static final ItemStack PICKAXE_OF_THE_SEEKER = new SlimefunItemStack("PICKAXE_OF_THE_SEEKER", Material.DIAMOND_PICKAXE, "&aPickaxe of the Seeker", "&rWill always point you to the nearest Ore", "&rbut might get damaged when doing it", "", "&7&eRight Click&7 to be pointed to the nearest Ore");
    public static final ItemStack COBALT_PICKAXE = new SlimefunItemStack("COBALT_PICKAXE", Material.IRON_PICKAXE, "&9Cobalt Pickaxe");
    public static final ItemStack PICKAXE_OF_VEIN_MINING = new SlimefunItemStack("PICKAXE_OF_VEIN_MINING", Material.DIAMOND_PICKAXE, "&ePickaxe of Vein Mining", "", "&rThis Pickaxe will dig out", "&rwhole Veins of Ores...");

    static {
        HERCULES_PICKAXE.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        HERCULES_PICKAXE.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);

        COBALT_PICKAXE.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        COBALT_PICKAXE.addUnsafeEnchantment(Enchantment.DIG_SPEED, 6);
    }

    /* Armor */
    public static final ItemStack GLOWSTONE_HELMET = new SlimefunItemStack("GLOWSTONE_HELMET", Material.LEATHER_HELMET, Color.YELLOW, "&e&lGlowstone Helmet", "", "&a&oShining like the sun!", "", "&9+ Night Vision");
    public static final ItemStack GLOWSTONE_CHESTPLATE = new SlimefunItemStack("GLOWSTONE_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.YELLOW, "&e&lGlowstone Chestplate", "", "&a&oShining like the sun!", "", "&9+ Night Vision");
    public static final ItemStack GLOWSTONE_LEGGINGS = new SlimefunItemStack("GLOWSTONE_LEGGINGS", Material.LEATHER_LEGGINGS, Color.YELLOW, "&e&lGlowstone Leggings", "", "&a&oShining like the sun!", "", "&9+ Night Vision");
    public static final ItemStack GLOWSTONE_BOOTS = new SlimefunItemStack("GLOWSTONE_BOOTS", Material.LEATHER_BOOTS, Color.YELLOW, "&e&lGlowstone Boots", "", "&a&oShining like the sun!", "", "&9+ Night Vision");

    public static final ItemStack ENDER_HELMET = new SlimefunItemStack("ENDER_HELMET", Material.LEATHER_HELMET, Color.fromRGB(28, 25, 112), "&5&lEnder Helmet", "", "&a&oSometimes its here, sometimes there!");
    public static final ItemStack ENDER_CHESTPLATE = new SlimefunItemStack("ENDER_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.fromRGB(28, 25, 112), "&5&lEnder Chestplate", "", "&a&oSometimes its here, sometimes there!");
    public static final ItemStack ENDER_LEGGINGS = new SlimefunItemStack("ENDER_LEGGINGS", Material.LEATHER_LEGGINGS, Color.fromRGB(28, 25, 112), "&5&lEnder Leggings", "", "&a&oSometimes its here, sometimes there!");
    public static final ItemStack ENDER_BOOTS = new SlimefunItemStack("ENDER_BOOTS", Material.LEATHER_BOOTS, Color.fromRGB(28, 25, 112), "&5&lEnder Boots", "", "&a&oSometimes its here, sometimes there!", "", "&9+ No Enderpearl Damage");

    public static final ItemStack SLIME_HELMET = new SlimefunItemStack("SLIME_HELMET", Material.LEATHER_HELMET, Color.LIME, "&a&lSlime Helmet", "", "&a&oBouncy Feeling");
    public static final ItemStack SLIME_CHESTPLATE = new SlimefunItemStack("SLIME_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.LIME, "&a&lSlime Chestplate", "", "&a&oBouncy Feeling");
    public static final ItemStack SLIME_LEGGINGS = new SlimefunItemStack("SLIME_LEGGINGS", Material.LEATHER_LEGGINGS, Color.LIME, "&a&lSlime Leggings", "", "&a&oBouncy Feeling", "", "&9+ Speed");
    public static final ItemStack SLIME_BOOTS = new SlimefunItemStack("SLIME_BOOTS", Material.LEATHER_BOOTS, Color.LIME, "&a&lSlime Boots", "", "&a&oBouncy Feeling", "", "&9+ Jump Boost", "&9+ No Fall Damage");

    public static final ItemStack CACTUS_HELMET = new SlimefunItemStack("CACTUS_HELMET", Material.LEATHER_HELMET, Color.GREEN, "&2Cactus Helmet");
    public static final ItemStack CACTUS_CHESTPLATE = new SlimefunItemStack("CACTUS_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.GREEN, "&2Cactus Chestplate");
    public static final ItemStack CACTUS_LEGGINGS = new SlimefunItemStack("CACTUS_LEGGINGS", Material.LEATHER_LEGGINGS, Color.GREEN, "&2Cactus Leggings");
    public static final ItemStack CACTUS_BOOTS = new SlimefunItemStack("CACTUS_BOOTS", Material.LEATHER_BOOTS, Color.GREEN, "&2Cactus Boots");

    public static final ItemStack DAMASCUS_STEEL_HELMET = new SlimefunItemStack("DAMASCUS_STEEL_HELMET", Material.IRON_HELMET, "&7Damascus Steel Helmet");
    public static final ItemStack DAMASCUS_STEEL_CHESTPLATE = new SlimefunItemStack("DAMASCUS_STEEL_CHESTPLATE", Material.IRON_CHESTPLATE, "&7Damascus Steel Chestplate");
    public static final ItemStack DAMASCUS_STEEL_LEGGINGS = new SlimefunItemStack("DAMASCUS_STEEL_LEGGINGS", Material.IRON_LEGGINGS, "&7Damascus Steel Leggings");
    public static final ItemStack DAMASCUS_STEEL_BOOTS = new SlimefunItemStack("DAMASCUS_STEEL_BOOTS", Material.IRON_BOOTS, "&7Damascus Steel Boots");

    public static final ItemStack REINFORCED_ALLOY_HELMET = new SlimefunItemStack("REINFORCED_ALLOY_HELMET", Material.IRON_HELMET, "&bReinforced Helmet");
    public static final ItemStack REINFORCED_ALLOY_CHESTPLATE = new SlimefunItemStack("REINFORCED_ALLOY_CHESTPLATE", Material.IRON_CHESTPLATE, "&bReinforced Chestplate");
    public static final ItemStack REINFORCED_ALLOY_LEGGINGS = new SlimefunItemStack("REINFORCED_ALLOY_LEGGINGS", Material.IRON_LEGGINGS, "&bReinforced Leggings");
    public static final ItemStack REINFORCED_ALLOY_BOOTS = new SlimefunItemStack("REINFORCED_ALLOY_BOOTS", Material.IRON_BOOTS, "&bReinforced Boots");

    public static final ItemStack SCUBA_HELMET = new SlimefunItemStack("SCUBA_HELMET", Material.LEATHER_HELMET, Color.ORANGE, "&cScuba Helmet", "", "&bAllows you to breathe Underwater", "&4&oPart of Hazmat Suit");
    public static final ItemStack HAZMATSUIT_CHESTPLATE = new SlimefunItemStack("HAZMAT_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.ORANGE, "&cHazmat Suit", "", "&bAllows you to walk through Fire", "&4&oPart of Hazmat Suit");
    public static final ItemStack HAZMATSUIT_LEGGINGS = new SlimefunItemStack("HAZMAT_LEGGINGS", Material.LEATHER_LEGGINGS, Color.ORANGE, "&cHazmat Suit Leggings", "", "&4&oPart of Hazmat Suit");
    public static final ItemStack RUBBER_BOOTS = new SlimefunItemStack("RUBBER_BOOTS", Material.LEATHER_BOOTS, Color.BLACK, "&cRubber Boots", "", "&4&oPart of Hazmat Suit");

    public static final ItemStack GILDED_IRON_HELMET = new SlimefunItemStack("GILDED_IRON_HELMET", Material.GOLDEN_HELMET, "&6Gilded Iron Helmet");
    public static final ItemStack GILDED_IRON_CHESTPLATE = new SlimefunItemStack("GILDED_IRON_CHESTPLATE", Material.GOLDEN_CHESTPLATE, "&6Gilded Iron Chestplate");
    public static final ItemStack GILDED_IRON_LEGGINGS = new SlimefunItemStack("GILDED_IRON_LEGGINGS", Material.GOLDEN_LEGGINGS, "&6Gilded Iron Leggings");
    public static final ItemStack GILDED_IRON_BOOTS = new SlimefunItemStack("GILDED_IRON_BOOTS", Material.GOLDEN_BOOTS, "&6Gilded Iron Boots");

    public static final ItemStack GOLD_HELMET = new SlimefunItemStack("GOLD_12K_HELMET", Material.GOLDEN_HELMET, "&6Gold Helmet", "&912-Carat");
    public static final ItemStack GOLD_CHESTPLATE = new SlimefunItemStack("GOLD_12K_CHESTPLATE", Material.GOLDEN_CHESTPLATE, "&6Gold Chestplate", "&912-Carat");
    public static final ItemStack GOLD_LEGGINGS = new SlimefunItemStack("GOLD_12K_LEGGINGS", Material.GOLDEN_LEGGINGS, "&6Gold Leggings", "&912-Carat");
    public static final ItemStack GOLD_BOOTS = new SlimefunItemStack("GOLD_12K_BOOTS", Material.GOLDEN_BOOTS, "&6Gold Boots", "&912-Carat");

    public static final ItemStack SLIME_HELMET_STEEL = new SlimefunItemStack("SLIME_STEEL_HELMET", Material.IRON_HELMET, "&a&lSlime Helmet", "&7&oReinforced", "", "&a&oBouncy Feeling");
    public static final ItemStack SLIME_CHESTPLATE_STEEL = new SlimefunItemStack("SLIME_STEEL_CHESTPLATE", Material.IRON_CHESTPLATE, "&a&lSlime Chestplate", "&7&oReinforced", "", "&a&oBouncy Feeling");
    public static final ItemStack SLIME_LEGGINGS_STEEL = new SlimefunItemStack("SLIME_STEEL_LEGGINGS", Material.IRON_LEGGINGS, "&a&lSlime Leggings", "&7&oReinforced", "", "&a&oBouncy Feeling", "", "&9+ Speed");
    public static final ItemStack SLIME_BOOTS_STEEL = new SlimefunItemStack("SLIME_STEEL_BOOTS", Material.IRON_BOOTS, "&a&lSlime Boots", "&7&oReinforced", "", "&a&oBouncy Feeling", "", "&9+ Jump Boost", "&9+ No Fall Damage");

    public static final ItemStack BOOTS_OF_THE_STOMPER = new SlimefunItemStack("BOOTS_OF_THE_STOMPER", Material.LEATHER_BOOTS, Color.AQUA, "&bBoots of the Stomper", "", "&9All Fall Damage you receive", "&9will be applied to nearby Mobs/Players", "", "&9+ No Fall Damage");

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

    /* Misc */
    public static final ItemStack MAGIC_LUMP_1 = new SlimefunItemStack("MAGIC_LUMP_1", Material.GOLD_NUGGET, "&6Magical Lump &7- &eI", "", "&c&oTier: I");
    public static final ItemStack MAGIC_LUMP_2 = new SlimefunItemStack("MAGIC_LUMP_2", Material.GOLD_NUGGET, "&6Magical Lump &7- &eII", "", "&c&oTier: II");
    public static final ItemStack MAGIC_LUMP_3 = new SlimefunItemStack("MAGIC_LUMP_3", Material.GOLD_NUGGET, "&6Magical Lump &7- &eIII", "", "&c&oTier: III");
    public static final ItemStack ENDER_LUMP_1 = new SlimefunItemStack("ENDER_LUMP_1", Material.GOLD_NUGGET, "&5Ender Lump &7- &eI", "", "&c&oTier: I");
    public static final ItemStack ENDER_LUMP_2 = new SlimefunItemStack("ENDER_LUMP_2", Material.GOLD_NUGGET, "&5Ender Lump &7- &eII", "", "&c&oTier: II");
    public static final ItemStack ENDER_LUMP_3 = new SlimefunItemStack("ENDER_LUMP_3", Material.GOLD_NUGGET, "&5Ender Lump &7- &eIII", "", "&c&oTier: III");
    public static final ItemStack MAGICAL_BOOK_COVER = new SlimefunItemStack("MAGICAL_BOOK_COVER", Material.PAPER, "&6Magical Book Cover", "", "&a&oUsed for various Magic Books");
    public static final ItemStack BASIC_CIRCUIT_BOARD = new SlimefunItemStack("BASIC_CIRCUIT_BOARD", Material.ACTIVATOR_RAIL, "&bBasic Circuit Board");
    public static final ItemStack ADVANCED_CIRCUIT_BOARD = new SlimefunItemStack("ADVANCED_CIRCUIT_BOARD", Material.POWERED_RAIL, "&bAdvanced Circuit Board");
    public static final ItemStack WHEAT_FLOUR = new SlimefunItemStack("WHEAT_FLOUR", Material.SUGAR, "&rWheat Flour");
    public static final ItemStack STEEL_PLATE = new SlimefunItemStack("STEEL_PLATE", Material.PAPER, "&7&lSteel Plate");
    public static final ItemStack BATTERY = new SlimefunItemStack("BATTERY", "6e2dda6ef6185d4dd6ea8684e97d39ba8ab037e25f75cdea6bd29df8eb34ee", "&6Battery");
    public static final ItemStack CARBON = new SlimefunItemStack("CARBON", "8b3a095b6b81e6b9853a19324eedf0bb9349417258dd173b8eff87a087aa", "&eCarbon");
    public static final ItemStack COMPRESSED_CARBON = new SlimefunItemStack("COMPRESSED_CARBON", "321d495165748d3116f99d6b5bd5d42eb8ba592bcdfad37fd95f9b6c04a3b", "&cCompressed Carbon");
    public static final ItemStack CARBON_CHUNK = new SlimefunItemStack("CARBON_CHUNK", "321d495165748d3116f99d6b5bd5d42eb8ba592bcdfad37fd95f9b6c04a3b", "&4Carbon Chunk");
    public static final ItemStack STEEL_THRUSTER = new SlimefunItemStack("STEEL_THRUSTER", Material.BUCKET, "&7&lSteel Thruster");
    public static final ItemStack POWER_CRYSTAL = new SlimefunItemStack("POWER_CRYSTAL", "53c1b036b6e03517b285a811bd85e73f5abfdacc1ddf90dff962e180934e3", "&c&lPower Crystal");
    public static final ItemStack CHAIN = new SlimefunItemStack("CHAIN", Material.STRING, "&bChain");
    public static final ItemStack HOOK = new SlimefunItemStack("HOOK", Material.FLINT, "&bHook");
    public static final ItemStack SIFTED_ORE = new SlimefunItemStack("SIFTED_ORE", Material.GUNPOWDER, "&6Sifted Ore");
    public static final ItemStack STONE_CHUNK = new SlimefunItemStack("STONE_CHUNK", "ce8f5adb14d6c9f6b810d027543f1a8c1f417e2fed993c97bcd89c74f5e2e8", "&6Stone Chunk");
    public static final ItemStack LAVA_CRYSTAL = new SlimefunItemStack("LAVA_CRYSTAL", "a3ad8ee849edf04ed9a26ca3341f6033bd76dcc4231ed1ea63b7565751b27ac", "&4Lava Crystal");
    public static final ItemStack SALT = new SlimefunItemStack("SALT", Material.SUGAR, "&rSalt");
    public static final ItemStack CHEESE = new SlimefunItemStack("CHEESE", "34febbc15d1d4cc62bedc5d7a2b6f0f46cd5b0696a884de75e289e35cbb53a0", "&rCheese");
    public static final ItemStack BUTTER = new SlimefunItemStack("BUTTER", "b66b19f7d635d03473891df33017c549363209a8f6328a8542c213d08525e", "&rButter");
    public static final ItemStack DUCT_TAPE = new SlimefunItemStack("DUCT_TAPE", "b2faaceab6384fff5ed24bb44a4af2f584eb1382729ecd93a5369acfd6654", "&8Duct Tape", "", "&rYou can repair Items using this", "&rin an Auto-Anvil");
    public static final ItemStack HEAVY_CREAM = new SlimefunItemStack("HEAVY_CREAM", Material.SNOWBALL, "&rHeavy Cream");
    public static final ItemStack CRUSHED_ORE = new SlimefunItemStack("CRUSHED_ORE", Material.GUNPOWDER, "&6Crushed Ore");
    public static final ItemStack PULVERIZED_ORE = new SlimefunItemStack("PULVERIZED_ORE", Material.GUNPOWDER, "&6Pulverized Ore");
    public static final ItemStack PURE_ORE_CLUSTER = new SlimefunItemStack("PURE_ORE_CLUSTER", Material.GUNPOWDER, "&6Pure Ore Cluster");
    public static final ItemStack SMALL_URANIUM = new SlimefunItemStack("SMALL_URANIUM", "c8b29afa6d6dc923e2e1324bf8192750f7bdbddc689632a2b6c18d9fe7a5e", "&cSmall Chunk of Uranium", "", LoreBuilder.radioactive(Radioactivity.MODERATE), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final ItemStack TINY_URANIUM = new SlimefunItemStack("TINY_URANIUM", "c8b29afa6d6dc923e2e1324bf8192750f7bdbddc689632a2b6c18d9fe7a5e", "&cTiny Pile of Uranium", "", LoreBuilder.radioactive(Radioactivity.LOW));

    public static final ItemStack MAGNET = new SlimefunItemStack("MAGNET", "aba8ebc4c6a81730947499bf7e1d5e73fed6c1bb2c051e96d35eb16d24610e7", "&cMagnet");
    public static final ItemStack NECROTIC_SKULL = new SlimefunItemStack("NECROTIC_SKULL", Material.WITHER_SKELETON_SKULL, "&cNecrotic Skull");
    public static final ItemStack ESSENCE_OF_AFTERLIFE = new SlimefunItemStack("ESSENCE_OF_AFTERLIFE", Material.GUNPOWDER, "&4Essence of Afterlife");
    public static final ItemStack ELECTRO_MAGNET = new SlimefunItemStack("ELECTRO_MAGNET", "aba8ebc4c6a81730947499bf7e1d5e73fed6c1bb2c051e96d35eb16d24610e7", "&cElectromagnet");
    public static final ItemStack HEATING_COIL = new SlimefunItemStack("HEATING_COIL", "7e3bc4893ba41a3f73ee28174cdf4fef6b145e41fe6c82cb7be8d8e9771a5", "&cHeating Coil");
    public static final ItemStack COOLING_UNIT = new SlimefunItemStack("COOLING_UNIT", "754bad86c99df780c889a1098f77648ead7385cc1ddb093da5a7d8c4c2ae54d", "&bCooling Unit");
    public static final ItemStack ELECTRIC_MOTOR = new SlimefunItemStack("ELECTRIC_MOTOR", "8cbca012f67e54de9aee72ff424e056c2ae58de5eacc949ab2bcd9683cec", "&cElectric Motor");
    public static final ItemStack CARGO_MOTOR = new SlimefunItemStack("CARGO_MOTOR", "8cbca012f67e54de9aee72ff424e056c2ae58de5eacc949ab2bcd9683cec", "&3Cargo Motor");
    public static final ItemStack SCROLL_OF_DIMENSIONAL_TELEPOSITION = new SlimefunItemStack("SCROLL_OF_DIMENSIONAL_TELEPOSITION", Material.PAPER, "&6Scroll of Dimensional Teleposition", "", "&cThis Scroll is capable of creating", "&ca temporary black Hole which pulls", "&cnearby Entities into itself and sends", "&cthem into another Dimension where", "&ceverything is turned around", "", "&rIn other words: Makes Entities turn by 180 Degrees");
    public static final ItemStack TOME_OF_KNOWLEDGE_SHARING = new SlimefunItemStack("TOME_OF_KNOWLEDGE_SHARING", Material.BOOK, "&6Tome of Knowledge Sharing", "&7Owner: &bNone", "", "&eRight Click&7 to bind this Tome to yourself", "", "", "&eRight Click&7 to obtain all Researches by", "&7the previously assigned Owner");
    public static final ItemStack HARDENED_GLASS = new SlimefunItemStack("HARDENED_GLASS", Material.LIGHT_GRAY_STAINED_GLASS, "&7Hardened Glass", "", "&rWithstands Explosions");
    public static final ItemStack WITHER_PROOF_OBSIDIAN = new SlimefunItemStack("WITHER_PROOF_OBSIDIAN", Material.OBSIDIAN, "&5Wither-Proof Obsidian", "", "&rWithstands Explosions", "&rWithstands Wither Bosses");
    public static final ItemStack WITHER_PROOF_GLASS = new SlimefunItemStack("WITHER_PROOF_GLASS", Material.PURPLE_STAINED_GLASS, "&5Wither-Proof Glass", "", "&rWithstands Explosions", "&rWithstands Wither Bosses");
    public static final ItemStack REINFORCED_PLATE = new SlimefunItemStack("REINFORCED_PLATE", Material.PAPER, "&7Reinforced Plate");
    public static final ItemStack ANCIENT_PEDESTAL = new SlimefunItemStack("ANCIENT_PEDESTAL", Material.DISPENSER, "&dAncient Pedestal", "", "&5Part of the Ancient Altar");
    public static final ItemStack ANCIENT_ALTAR = new SlimefunItemStack("ANCIENT_ALTAR", Material.ENCHANTING_TABLE, "&dAncient Altar", "", "&5Multi-Block Altar for", "&5magical Crafting Processes");
    public static final ItemStack COPPER_WIRE = new SlimefunItemStack("COPPER_WIRE", Material.STRING, "&6Copper Wire", "", "&6Crucial component in electric modules");

    public static final ItemStack RAINBOW_WOOL = new SlimefunItemStack("RAINBOW_WOOL", Material.WHITE_WOOL, "&5Rainbow Wool", "", "&dCycles through all Colors of the Rainbow!");
    public static final ItemStack RAINBOW_GLASS = new SlimefunItemStack("RAINBOW_GLASS", Material.WHITE_STAINED_GLASS, "&5Rainbow Glass", "", "&dCycles through all Colors of the Rainbow!");
    public static final ItemStack RAINBOW_CLAY = new SlimefunItemStack("RAINBOW_CLAY", Material.WHITE_TERRACOTTA, "&5Rainbow Clay", "", "&dCycles through all Colors of the Rainbow!");
    public static final ItemStack RAINBOW_GLASS_PANE = new SlimefunItemStack("RAINBOW_GLASS_PANE", Material.WHITE_STAINED_GLASS_PANE, "&5Rainbow Glass Pane", "", "&dCycles through all Colors of the Rainbow!");
    public static final ItemStack RAINBOW_CONCRETE = new SlimefunItemStack("RAINBOW_CONCRETE", Material.WHITE_CONCRETE, "&5Rainbow Concrete", "", "&dCycles through all Colors of the Rainbow!");
    public static final ItemStack RAINBOW_GLAZED_TERRACOTTA = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA", Material.WHITE_GLAZED_TERRACOTTA, "&5Rainbow Glazed Terracotta", "", "&dCycles through all Colors of the Rainbow!");

    public static final ItemStack RAINBOW_WOOL_XMAS = new SlimefunItemStack("RAINBOW_WOOL_XMAS", Material.WHITE_WOOL, "&5Rainbow Wool &7(Christmas)", "", ChatUtils.christmas("< Christmas Edition >"));
    public static final ItemStack RAINBOW_GLASS_XMAS = new SlimefunItemStack("RAINBOW_GLASS_XMAS", Material.WHITE_STAINED_GLASS, "&5Rainbow Glass &7(Christmas)", "", ChatUtils.christmas("< Christmas Edition >"));
    public static final ItemStack RAINBOW_CLAY_XMAS = new SlimefunItemStack("RAINBOW_CLAY_XMAS", Material.WHITE_TERRACOTTA, "&5Rainbow Clay &7(Christmas)", "", ChatUtils.christmas("< Christmas Edition >"));
    public static final ItemStack RAINBOW_GLASS_PANE_XMAS = new SlimefunItemStack("RAINBOW_GLASS_PANE_XMAS", Material.WHITE_STAINED_GLASS_PANE, "&5Rainbow Glass Pane &7(Christmas)", "", ChatUtils.christmas("< Christmas Edition >"));
    public static final ItemStack RAINBOW_CONCRETE_XMAS = new SlimefunItemStack("RAINBOW_CONCRETE_XMAS", Material.WHITE_CONCRETE, "&5Rainbow Concrete &7(Christmas)", "", ChatUtils.christmas("< Christmas Edition >"));
    public static final ItemStack RAINBOW_GLAZED_TERRACOTTA_XMAS = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_XMAS", Material.WHITE_GLAZED_TERRACOTTA, "&5Rainbow Glazed Terracotta &7(Christmas)", "", ChatUtils.christmas("< Christmas Edition >"));

    public static final ItemStack RAINBOW_WOOL_VALENTINE = new SlimefunItemStack("RAINBOW_WOOL_VALENTINE", Material.PINK_WOOL, "&5Rainbow Wool &7(Valentine's Day)", "", "&d< Valentine's Day Edition >");
    public static final ItemStack RAINBOW_GLASS_VALENTINE = new SlimefunItemStack("RAINBOW_GLASS_VALENTINE", Material.PINK_STAINED_GLASS, "&5Rainbow Glass &7(Valentine's Day)", "", "&d< Valentine's Day Edition >");
    public static final ItemStack RAINBOW_CLAY_VALENTINE = new SlimefunItemStack("RAINBOW_CLAY_VALENTINE", Material.PINK_TERRACOTTA, "&5Rainbow Clay &7(Valentine's Day)", "", "&d< Valentine's Day Edition >");
    public static final ItemStack RAINBOW_GLASS_PANE_VALENTINE = new SlimefunItemStack("RAINBOW_GLASS_PANE_VALENTINE", Material.PINK_STAINED_GLASS_PANE, "&5Rainbow Glass Pane &7(Valentine's Day)", "", "&d< Valentine's Day Edition >");
    public static final ItemStack RAINBOW_CONCRETE_VALENTINE = new SlimefunItemStack("RAINBOW_CONCRETE_VALENTINE", Material.PINK_CONCRETE, "&5Rainbow Concrete &7(Valentine's Day)", "", "&d< Valentine's Day Edition >");
    public static final ItemStack RAINBOW_GLAZED_TERRACOTTA_VALENTINE = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_VALENTINE", Material.PINK_GLAZED_TERRACOTTA, "&5Rainbow Glazed Terracotta &7(Valentine's Day)", "", "&d< Valentine's Day Edition >");

    public static final ItemStack RAINBOW_WOOL_HALLOWEEN = new SlimefunItemStack("RAINBOW_WOOL_HALLOWEEN", Material.ORANGE_WOOL, "&5Rainbow Wool &7(Halloween)", "", "&6< Halloween Edition >");
    public static final ItemStack RAINBOW_GLASS_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLASS_HALLOWEEN", Material.ORANGE_STAINED_GLASS, "&5Rainbow Glass &7(Halloween)", "", "&6< Halloween Edition >");
    public static final ItemStack RAINBOW_CLAY_HALLOWEEN = new SlimefunItemStack("RAINBOW_CLAY_HALLOWEEN", Material.ORANGE_TERRACOTTA, "&5Rainbow Clay &7(Halloween)", "", "&6< Halloween Edition >");
    public static final ItemStack RAINBOW_GLASS_PANE_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLASS_PANE_HALLOWEEN", Material.ORANGE_STAINED_GLASS_PANE, "&5Rainbow Glass Pane &7(Halloween)", "", "&6< Halloween Edition >");
    public static final ItemStack RAINBOW_CONCRETE_HALLOWEEN = new SlimefunItemStack("RAINBOW_CONCRETE_HALLOWEEN", Material.ORANGE_CONCRETE, "&5Rainbow Concrete &7(Halloween)", "", "&6< Halloween Edition >");
    public static final ItemStack RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN", Material.ORANGE_GLAZED_TERRACOTTA, "&5Rainbow Glazed Terracotta &7(Halloween)", "", "&6< Halloween Edition >");

    /* Ingots */
    public static final ItemStack COPPER_INGOT = new SlimefunItemStack("COPPER_INGOT", Material.BRICK, "&bCopper Ingot");
    public static final ItemStack TIN_INGOT = new SlimefunItemStack("TIN_INGOT", Material.IRON_INGOT, "&bTin Ingot");
    public static final ItemStack SILVER_INGOT = new SlimefunItemStack("SILVER_INGOT", Material.IRON_INGOT, "&bSilver Ingot");
    public static final ItemStack ALUMINUM_INGOT = new SlimefunItemStack("ALUMINUM_INGOT", Material.IRON_INGOT, "&bAluminum Ingot");
    public static final ItemStack LEAD_INGOT = new SlimefunItemStack("LEAD_INGOT", Material.IRON_INGOT, "&bLead Ingot");
    public static final ItemStack ZINC_INGOT = new SlimefunItemStack("ZINC_INGOT", Material.IRON_INGOT, "&bZinc Ingot");
    public static final ItemStack MAGNESIUM_INGOT = new SlimefunItemStack("MAGNESIUM_INGOT", Material.IRON_INGOT, "&bMagnesium Ingot");

    /* Alloy (Carbon + Iron) */
    public static final ItemStack STEEL_INGOT = new SlimefunItemStack("STEEL_INGOT", Material.IRON_INGOT, "&bSteel Ingot");
    /* Alloy (Copper + Tin) */
    public static final ItemStack BRONZE_INGOT = new SlimefunItemStack("BRONZE_INGOT", Material.BRICK, "&bBronze Ingot");
    /* Alloy (Copper + Aluminum) */
    public static final ItemStack DURALUMIN_INGOT = new SlimefunItemStack("DURALUMIN_INGOT", Material.IRON_INGOT, "&bDuralumin Ingot");
    /* Alloy (Copper + Silver) */
    public static final ItemStack BILLON_INGOT = new SlimefunItemStack("BILLON_INGOT", Material.IRON_INGOT, "&bBillon Ingot");
    /* Alloy (Copper + Zinc) */
    public static final ItemStack BRASS_INGOT = new SlimefunItemStack("BRASS_INGOT", Material.GOLD_INGOT, "&bBrass Ingot");
    /* Alloy (Aluminum + Brass) */
    public static final ItemStack ALUMINUM_BRASS_INGOT = new SlimefunItemStack("ALUMINUM_BRASS_INGOT", Material.GOLD_INGOT, "&bAluminum Brass Ingot");
    /* Alloy (Aluminum + Bronze) */
    public static final ItemStack ALUMINUM_BRONZE_INGOT = new SlimefunItemStack("ALUMINUM_BRONZE_INGOT", Material.GOLD_INGOT, "&bAluminum Bronze Ingot");
    /* Alloy (Gold + Silver + Copper) */
    public static final ItemStack CORINTHIAN_BRONZE_INGOT = new SlimefunItemStack("CORINTHIAN_BRONZE_INGOT", Material.GOLD_INGOT, "&bCorinthian Bronze Ingot");
    /* Alloy (Lead + Tin) */
    public static final ItemStack SOLDER_INGOT = new SlimefunItemStack("SOLDER_INGOT", Material.IRON_INGOT, "&bSolder Ingot");
    /* Alloy (Steel + Iron + Carbon) */
    public static final ItemStack DAMASCUS_STEEL_INGOT = new SlimefunItemStack("DAMASCUS_STEEL_INGOT", Material.IRON_INGOT, "&bDamascus Steel Ingot");
    /* Alloy (Damascus Steel + Duralumin + Compressed Carbon + Aluminium Bronze) */
    public static final ItemStack HARDENED_METAL_INGOT = new SlimefunItemStack("HARDENED_METAL_INGOT", Material.IRON_INGOT, "&b&lHardened Metal");
    /* Alloy (Hardened Metal + Corinthian Bronze + Solder + Billon + Damascus Steel) */
    public static final ItemStack REINFORCED_ALLOY_INGOT = new SlimefunItemStack("REINFORCED_ALLOY_INGOT", Material.IRON_INGOT, "&b&lReinforced Alloy Ingot");
    /* Alloy (Iron + Silicon) */
    public static final ItemStack FERROSILICON = new SlimefunItemStack("FERROSILICON", Material.IRON_INGOT, "&bFerrosilicon");
    /* Alloy (Iron + Gold) */
    public static final ItemStack GILDED_IRON = new SlimefunItemStack("GILDED_IRON", Material.GOLD_INGOT, "&6&lGilded Iron");
    /* Alloy (Redston + Ferrosilicon) */
    public static final ItemStack REDSTONE_ALLOY = new SlimefunItemStack("REDSTONE_ALLOY", Material.BRICK, "&cRedstone Alloy Ingot");
    /* Alloy (Iron + Copper) */
    public static final ItemStack NICKEL_INGOT = new SlimefunItemStack("NICKEL_INGOT", Material.IRON_INGOT, "&bNickel Ingot");
    /* Alloy (Nickel + Iron + Copper) */
    public static final ItemStack COBALT_INGOT = new SlimefunItemStack("COBALT_INGOT", Material.IRON_INGOT, "&9Cobalt Ingot");

    /* Gold */
    public static final ItemStack GOLD_4K = new SlimefunItemStack("GOLD_4K", Material.GOLD_INGOT, "&rGold Ingot &7(4-Carat)");
    public static final ItemStack GOLD_6K = new SlimefunItemStack("GOLD_6K", Material.GOLD_INGOT, "&rGold Ingot &7(6-Carat)");
    public static final ItemStack GOLD_8K = new SlimefunItemStack("GOLD_8K", Material.GOLD_INGOT, "&rGold Ingot &7(8-Carat)");
    public static final ItemStack GOLD_10K = new SlimefunItemStack("GOLD_10K", Material.GOLD_INGOT, "&rGold Ingot &7(10-Carat)");
    public static final ItemStack GOLD_12K = new SlimefunItemStack("GOLD_12K", Material.GOLD_INGOT, "&rGold Ingot &7(12-Carat)");
    public static final ItemStack GOLD_14K = new SlimefunItemStack("GOLD_14K", Material.GOLD_INGOT, "&rGold Ingot &7(14-Carat)");
    public static final ItemStack GOLD_16K = new SlimefunItemStack("GOLD_16K", Material.GOLD_INGOT, "&rGold Ingot &7(16-Carat)");
    public static final ItemStack GOLD_18K = new SlimefunItemStack("GOLD_18K", Material.GOLD_INGOT, "&rGold Ingot &7(18-Carat)");
    public static final ItemStack GOLD_20K = new SlimefunItemStack("GOLD_20K", Material.GOLD_INGOT, "&rGold Ingot &7(20-Carat)");
    public static final ItemStack GOLD_22K = new SlimefunItemStack("GOLD_22K", Material.GOLD_INGOT, "&rGold Ingot &7(22-Carat)");
    public static final ItemStack GOLD_24K = new SlimefunItemStack("GOLD_24K", Material.GOLD_INGOT, "&rGold Ingot &7(24-Carat)");

    /* Dusts */
    public static final ItemStack IRON_DUST = new SlimefunItemStack("IRON_DUST", Material.GUNPOWDER, "&6Iron Dust");
    public static final ItemStack GOLD_DUST = new SlimefunItemStack("GOLD_DUST", Material.GLOWSTONE_DUST, "&6Gold Dust");
    public static final ItemStack TIN_DUST = new SlimefunItemStack("TIN_DUST", Material.SUGAR, "&6Tin Dust");
    public static final ItemStack COPPER_DUST = new SlimefunItemStack("COPPER_DUST", Material.GLOWSTONE_DUST, "&6Copper Dust");
    public static final ItemStack SILVER_DUST = new SlimefunItemStack("SILVER_DUST", Material.SUGAR, "&6Silver Dust");
    public static final ItemStack ALUMINUM_DUST = new SlimefunItemStack("ALUMINUM_DUST", Material.SUGAR, "&6Aluminum Dust");
    public static final ItemStack LEAD_DUST = new SlimefunItemStack("LEAD_DUST", Material.GUNPOWDER, "&6Lead Dust");
    public static final ItemStack SULFATE = new SlimefunItemStack("SULFATE", Material.GLOWSTONE_DUST, "&6Sulfate");
    public static final ItemStack ZINC_DUST = new SlimefunItemStack("ZINC_DUST", Material.SUGAR, "&6Zinc Dust");
    public static final ItemStack MAGNESIUM_DUST = new SlimefunItemStack("MAGNESIUM_DUST", Material.SUGAR, "&6Magnesium");
    public static final ItemStack SILICON = new SlimefunItemStack("SILICON", Material.FIREWORK_STAR, "&6Silicon");
    public static final ItemStack GOLD_24K_BLOCK = new SlimefunItemStack("GOLD_24K_BLOCK", Material.GOLD_BLOCK, "&rGold Block &7(24-Carat)");

    /* Gems */
    public static final ItemStack SYNTHETIC_DIAMOND = new SlimefunItemStack("SYNTHETIC_DIAMOND", Material.DIAMOND, "&bSynthetic Diamond", "", "&rThis item can be used in a", "&rworkbench and acts like a normal Diamond");
    public static final ItemStack SYNTHETIC_EMERALD = new SlimefunItemStack("SYNTHETIC_EMERALD", Material.EMERALD, "&bSynthetic Emerald", "", "&rThis item can be used to", "&rtrade with Villagers");
    public static final ItemStack SYNTHETIC_SAPPHIRE = new SlimefunItemStack("SYNTHETIC_SAPPHIRE", "e35032f4d7d01de8ec99d89f8723012d4e74fa73022c4facf1b57c7ff6ff0", "&bSynthetic Sapphire", "", "&rThis item can be used in a", "&rworkbench and acts like Lapis Lazuli");
    public static final ItemStack CARBONADO = new SlimefunItemStack("CARBONADO", "12f4b1577f5160c6893172571c4a71d8b321cdceaa032c6e0e3b60e0b328fa", "&b&lCarbonado", "", "&7&o\"Black Diamond\"");
    public static final ItemStack RAW_CARBONADO = new SlimefunItemStack("RAW_CARBONADO", "eb49e6ec10771e899225aea73cd8cf03684f411d1415c7323c93cb9476230", "&bRaw Carbonado");

    public static final ItemStack URANIUM = new SlimefunItemStack("URANIUM", "c8b29afa6d6dc923e2e1324bf8192750f7bdbddc689632a2b6c18d9fe7a5e", "&4Uranium", "", LoreBuilder.radioactive(Radioactivity.HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final ItemStack NEPTUNIUM = new SlimefunItemStack("NEPTUNIUM", "4edea6bfd37e49de43f154fe6fca617d4129e61b95759a3d49a15935a1c2dcf0", "&aNeptunium", "", LoreBuilder.radioactive(Radioactivity.HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final ItemStack PLUTONIUM = new SlimefunItemStack("PLUTONIUM", "25cf91b7388665a6d7c1b6026bdb2322c6d278997a44478677cbcc15f76124f", "&7Plutonium", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final ItemStack BOOSTED_URANIUM = new SlimefunItemStack("BOOSTED_URANIUM", "6837ca12f222f4787196a17b8ab656985f8404c50767adbcb6e7f14254fee", "&2Boosted Uranium", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);

    /* Talisman */
    public static final ItemStack TALISMAN = new SlimefunItemStack("COMMON_TALISMAN", Material.EMERALD, "&6Common Talisman");
    public static final ItemStack ENDER_TALISMAN = new SlimefunItemStack("ENDER_TALISMAN", Material.EMERALD, "&5Ender Talisman");

    public static final ItemStack TALISMAN_ANVIL = new SlimefunItemStack("ANVIL_TALISMAN", Material.EMERALD, "&aTalisman of the Anvil", "", "&rEach Talisman can prevent", "&r1 Tool from breaking, but will then", "&rbe consumed", "", "&4&lWARNING:", "&4This Talisman does not work on", "&4Tools which are too powerful", "&4due to their complexity");
    public static final ItemStack TALISMAN_MINER = new SlimefunItemStack("MINER_TALISMAN", Material.EMERALD, "&aTalisman of the Miner", "", "&rWhile you have this Talisman", "&rin your Inventory it has", "&ra 20% chance of doubling", "&rall Ores you mine");
    public static final ItemStack TALISMAN_HUNTER = new SlimefunItemStack("HUNTER_TALISMAN", Material.EMERALD, "&aTalisman of the Hunter", "", "&rWhile you have this Talisman", "&rin your Inventory it has", "&ra 20% chance of doubling", "&rall Drops from Mobs you kill");
    public static final ItemStack TALISMAN_LAVA = new SlimefunItemStack("LAVA_TALISMAN", Material.EMERALD, "&aTalisman of the Lava Walker", "", "&rWhile you have this Talisman", "&rin your Inventory it will", "&rgive you Fire Resistance", "&ras soon as you touch Lava", "&rbut will then be consumed");
    public static final ItemStack TALISMAN_WATER = new SlimefunItemStack("WATER_TALISMAN", Material.EMERALD, "&aTalisman of the Water Breather", "", "&rWhile you have this Talisman", "&rin your Inventory it will", "&rgive you the ability", "&rto breath underwater as", "&rsoon as you start drowning", "&rbut will then be consumed");
    public static final ItemStack TALISMAN_ANGEL = new SlimefunItemStack("ANGEL_TALISMAN", Material.EMERALD, "&aTalisman of the Angel", "", "&rWhile you have this Talisman", "&rin your Inventory it has a", "&r75% chance to prevent you", "&rfrom taking Fall Damage");
    public static final ItemStack TALISMAN_FIRE = new SlimefunItemStack("FIRE_TALISMAN", Material.EMERALD, "&aTalisman of the Firefighter", "", "&rWhile you have this Talisman", "&rin your Inventory it will", "&rgive you Fire Resistance", "&ras soon as you start burning", "&rbut will then be consumed");
    public static final ItemStack TALISMAN_MAGICIAN = new SlimefunItemStack("MAGICIAN_TALISMAN", Material.EMERALD, "&aTalisman of the Magician", "", "&rWhile you have this Talisman", "&rin your Inventory it gives", "&ryou a 80% Luck Bonus on Enchanting", "&rYou will sometimes get an Extra Enchantment");
    public static final ItemStack TALISMAN_TRAVELLER = new SlimefunItemStack("TRAVELLER_TALISMAN", Material.EMERALD, "&aTalisman of the Traveller", "", "&rWhile you have this Talisman", "&rin your Inventory it gives", "&ryou a 60% Chance for a decent", "&rSpeed Buff when you start sprinting");
    public static final ItemStack TALISMAN_WARRIOR = new SlimefunItemStack("WARRIOR_TALISMAN", Material.EMERALD, "&aTalisman of the Warrior", "", "&rWhile you have this Talisman", "&rin your Inventory it gives", "&ryou Strength III whenever you get hit", "&rbut will then be consumed");
    public static final ItemStack TALISMAN_KNIGHT = new SlimefunItemStack("KNIGHT_TALISMAN", Material.EMERALD, "&aTalisman of the Knight", "", "&rWhile you have this Talisman", "&rin your Inventory it gives", "&ryou a 30% Chance for 5 Seconds of Regeneration", "&rwhenever You get hit", "&rbut will then be consumed");
    public static final ItemStack TALISMAN_WHIRLWIND = new SlimefunItemStack("WHIRLWIND_TALISMAN", Material.EMERALD, "&aTalisman of the Whirlwind", "", "&rWhile you have this Talisman", "&rin your Inventory it will reflect", "&r60% of all Projectiles fired at you");
    public static final ItemStack TALISMAN_WIZARD = new SlimefunItemStack("WIZARD_TALISMAN", Material.EMERALD, "&aTalisman of the Wizard", "", "&rWhile you have this Talisman", "&rin your Inventory it allows you to", "&robtain Fortune Level 4/5 however", "&rit also has a chance to lower the", "&rLevel of some Enchantments on your Item");

    /* Staves */
    public static final ItemStack STAFF_ELEMENTAL = new SlimefunItemStack("STAFF_ELEMENTAL", Material.STICK, "&6Elemental Staff");
    public static final ItemStack STAFF_WIND = new SlimefunItemStack("STAFF_ELEMENTAL_WIND", Material.STICK, "&6Elemental Staff &7- &b&oWind", "", "&7Element: &b&oWind", "", "&eRight Click&7 to launch yourself forward");
    public static final ItemStack STAFF_FIRE = new SlimefunItemStack("STAFF_ELEMENTAL_FIRE", Material.STICK, "&6Elemental Staff &7- &c&oFire", "", "&7Element: &c&oFire");
    public static final ItemStack STAFF_WATER = new SlimefunItemStack("STAFF_ELEMENTAL_WATER", Material.STICK, "&6Elemental Staff &7- &1&oWater", "", "&7Element: &1&oWater", "", "&eRight Click&7 to extinguish yourself");
    public static final ItemStack STAFF_STORM = new SlimefunItemStack("STAFF_ELEMENTAL_STORM", Material.STICK, "&6Elemental Staff &7- &8&oStorm", "", "&7Element: &8&oStorm", "", "&eRight Click&7 to summon a lightning", "&e" + StormStaff.MAX_USES + " Uses &7left");

    static {
        STAFF_WIND.addUnsafeEnchantment(Enchantment.LUCK, 1);
        STAFF_FIRE.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 5);
        STAFF_WATER.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
        STAFF_STORM.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
    }

    /* Machines */
    public static final ItemStack GRIND_STONE = new SlimefunItemStack("GRIND_STONE", Material.DISPENSER, "&bGrind Stone", "", "&aGrinds items down into other items");
    public static final ItemStack ARMOR_FORGE = new SlimefunItemStack("ARMOR_FORGE", Material.ANVIL, "&6Armor Forge", "", "&aGives you the ability to create powerful armor");
    public static final ItemStack SMELTERY = new SlimefunItemStack("SMELTERY", Material.FURNACE, "&6Smeltery", "", "&aActs as a high-temperature furnace for metals");
    public static final ItemStack IGNITION_CHAMBER = new SlimefunItemStack("IGNITION_CHAMBER", Material.DROPPER, "&4Automatic Ignition Chamber", "", "&rPrevents the Smeltery from using up fire.", "&rJust fill it up with \"Flint and Steel\"", "&rand place it adjacent to the Smeltery's dispenser");
    public static final ItemStack ORE_CRUSHER = new SlimefunItemStack("ORE_CRUSHER", Material.DISPENSER, "&bOre Crusher", "", "&aCrushes ores to double them");
    public static final ItemStack COMPRESSOR = new SlimefunItemStack("COMPRESSOR", Material.PISTON, "&bCompressor", "", "&aCompresses Items");
    public static final ItemStack PRESSURE_CHAMBER = new SlimefunItemStack("PRESSURE_CHAMBER", Material.GLASS, "&bPressure Chamber", "", "&aCompresses Items even further");
    public static final ItemStack MAGIC_WORKBENCH = new SlimefunItemStack("MAGIC_WORKBENCH", Material.CRAFTING_TABLE, "&6Magic Workbench", "", "&dInfuses Items with magical Energy");
    public static final ItemStack ORE_WASHER = new SlimefunItemStack("ORE_WASHER", Material.CAULDRON, "&6Ore Washer", "", "&aWashes Sifted Ore to filter Ores", "&aand gives you small Stone Chunks");
    public static final ItemStack TABLE_SAW = new SlimefunItemStack("TABLE_SAW", Material.STONECUTTER, "&6Table Saw", "", "&aAllows you to get 8 planks from 1 Log", "&a(Works with all log types)");
    public static final ItemStack COMPOSTER = new SlimefunItemStack("COMPOSTER", Material.CAULDRON, "&aComposter", "", "&a&oCan convert various Materials over Time...");
    public static final ItemStack ENHANCED_CRAFTING_TABLE = new SlimefunItemStack("ENHANCED_CRAFTING_TABLE", Material.CRAFTING_TABLE, "&eEnhanced Crafting Table", "", "&aA regular Crafting Table cannot", "&ahold this massive Amount of Power...");
    public static final ItemStack CRUCIBLE = new SlimefunItemStack("CRUCIBLE", Material.CAULDRON, "&cCrucible", "", "&a&oUsed to smelt Items into Liquids");
    public static final ItemStack JUICER = new SlimefunItemStack("JUICER", Material.GLASS_BOTTLE, "&aJuicer", "", "&aAllows you to create delicious Juice");

    public static final ItemStack SOLAR_PANEL = new SlimefunItemStack("SOLAR_PANEL", Material.DAYLIGHT_DETECTOR, "&bSolar Panel", "", "&a&oTransforms Sunlight to Energy");

    @Deprecated
    public static final ItemStack DIGITAL_MINER = new CustomItem(Material.IRON_PICKAXE, "&bDigital Miner", "", "&4DEPRECATED", "&cThis machine will be removed at some point!", "&cWe don't know when.");

    @Deprecated
    public static final ItemStack ADVANCED_DIGITAL_MINER = new CustomItem(Material.DIAMOND_PICKAXE, "&6Advanced Digital Miner", "", "&4DEPRECATED", "&cThis machine will be removed at some point!", "&cWe don't know when.");

    public static final ItemStack AUTOMATED_PANNING_MACHINE = new SlimefunItemStack("AUTOMATED_PANNING_MACHINE", Material.BOWL, "&eAutomated Panning Machine", "", "&rA MultiBlock Version of the Gold Pan", "&rand Nether Gold Pan combined in one machine.");
    public static final ItemStack OUTPUT_CHEST = new SlimefunItemStack("OUTPUT_CHEST", Material.CHEST, "&4Output Chest", "", "&c&oA basic machine will try to put", "&c&oitems in this chest if it's placed", "&c&oadjacent to the dispenser.");
    public static final ItemStack HOLOGRAM_PROJECTOR = new SlimefunItemStack("HOLOGRAM_PROJECTOR", Material.QUARTZ_SLAB, "&bHologram Projector", "", "&rProjects an Editable Hologram");

    /* Enhanced Furnaces */
    public static final ItemStack ENHANCED_FURNACE = new SlimefunItemStack("ENHANCED_FURNACE", Material.FURNACE, "&7Enhanced Furnace - &eI", "", "&7Processing Speed: &e1x", "&7Fuel Efficiency: &e1x", "&7Luck Multiplier: &e1x");
    public static final ItemStack ENHANCED_FURNACE_2 = new SlimefunItemStack("ENHANCED_FURNACE_2", Material.FURNACE, "&7Enhanced Furnace - &eII", "", "&7Processing Speed: &e2x", "&7Fuel Efficiency: &e1x", "&7Luck Multiplier: &e1x");
    public static final ItemStack ENHANCED_FURNACE_3 = new SlimefunItemStack("ENHANCED_FURNACE_3", Material.FURNACE, "&7Enhanced Furnace - &eIII", "", "&7Processing Speed: &e2x", "&7Fuel Efficiency: &e2x", "&7Luck Multiplier: &e1x");
    public static final ItemStack ENHANCED_FURNACE_4 = new SlimefunItemStack("ENHANCED_FURNACE_4", Material.FURNACE, "&7Enhanced Furnace - &eIV", "", "&7Processing Speed: &e3x", "&7Fuel Efficiency: &e2x", "&7Luck Multiplier: &e1x");
    public static final ItemStack ENHANCED_FURNACE_5 = new SlimefunItemStack("ENHANCED_FURNACE_5", Material.FURNACE, "&7Enhanced Furnace - &eV", "", "&7Processing Speed: &e3x", "&7Fuel Efficiency: &e2x", "&7Luck Multiplier: &e2x");
    public static final ItemStack ENHANCED_FURNACE_6 = new SlimefunItemStack("ENHANCED_FURNACE_6", Material.FURNACE, "&7Enhanced Furnace - &eVI", "", "&7Processing Speed: &e3x", "&7Fuel Efficiency: &e3x", "&7Luck Multiplier: &e2x");
    public static final ItemStack ENHANCED_FURNACE_7 = new SlimefunItemStack("ENHANCED_FURNACE_7", Material.FURNACE, "&7Enhanced Furnace - &eVII", "", "&7Processing Speed: &e4x", "&7Fuel Efficiency: &e3x", "&7Luck Multiplier: &e2x");
    public static final ItemStack ENHANCED_FURNACE_8 = new SlimefunItemStack("ENHANCED_FURNACE_8", Material.FURNACE, "&7Enhanced Furnace - &eVIII", "", "&7Processing Speed: &e4x", "&7Fuel Efficiency: &e4x", "&7Luck Multiplier: &e2x");
    public static final ItemStack ENHANCED_FURNACE_9 = new SlimefunItemStack("ENHANCED_FURNACE_9", Material.FURNACE, "&7Enhanced Furnace - &eIX", "", "&7Processing Speed: &e5x", "&7Fuel Efficiency: &e4x", "&7Luck Multiplier: &e2x");
    public static final ItemStack ENHANCED_FURNACE_10 = new SlimefunItemStack("ENHANCED_FURNACE_10", Material.FURNACE, "&7Enhanced Furnace - &eX", "", "&7Processing Speed: &e5x", "&7Fuel Efficiency: &e5x", "&7Luck Multiplier: &e2x");
    public static final ItemStack ENHANCED_FURNACE_11 = new SlimefunItemStack("ENHANCED_FURNACE_11", Material.FURNACE, "&7Enhanced Furnace - &eXI", "", "&7Processing Speed: &e5x", "&7Fuel Efficiency: &e5x", "&7Luck Multiplier: &e3x");
    public static final ItemStack REINFORCED_FURNACE = new SlimefunItemStack("REINFORCED_FURNACE", Material.FURNACE, "&7Reinforced Furnace", "", "&7Processing Speed: &e10x", "&7Fuel Efficiency: &e10x", "&7Luck Multiplier: &e3x");
    public static final ItemStack CARBONADO_EDGED_FURNACE = new SlimefunItemStack("CARBONADO_EDGED_FURNACE", Material.FURNACE, "&7Carbonado Edged Furnace", "", "&7Processing Speed: &e20x", "&7Fuel Efficiency: &e10x", "&7Luck Multiplier: &e3x");

    public static final ItemStack BLOCK_PLACER = new SlimefunItemStack("BLOCK_PLACER", Material.DISPENSER, "&aBlock Placer", "", "&rAll Blocks in this Dispenser", "&rwill automatically get placed");

    /* Soulbound Items */
    public static final ItemStack SOULBOUND_SWORD = new SlimefunItemStack("SOULBOUND_SWORD", Material.DIAMOND_SWORD, "&cSoulbound Sword");
    public static final ItemStack SOULBOUND_BOW = new SlimefunItemStack("SOULBOUND_BOW", Material.BOW, "&cSoulbound Bow");
    public static final ItemStack SOULBOUND_PICKAXE = new SlimefunItemStack("SOULBOUND_PICKAXE", Material.DIAMOND_PICKAXE, "&cSoulbound Pickaxe");
    public static final ItemStack SOULBOUND_AXE = new SlimefunItemStack("SOULBOUND_AXE", Material.DIAMOND_AXE, "&cSoulbound Axe");
    public static final ItemStack SOULBOUND_SHOVEL = new SlimefunItemStack("SOULBOUND_SHOVEL", Material.DIAMOND_SHOVEL, "&cSoulbound Shovel");
    public static final ItemStack SOULBOUND_HOE = new SlimefunItemStack("SOULBOUND_HOE", Material.DIAMOND_HOE, "&cSoulbound Hoe");
    public static final ItemStack SOULBOUND_TRIDENT = new SlimefunItemStack("SOULBOUND_TRIDENT", Material.TRIDENT, "&cSoulbound Trident");

    public static final ItemStack SOULBOUND_HELMET = new SlimefunItemStack("SOULBOUND_HELMET", Material.DIAMOND_HELMET, "&cSoulbound Helmet");
    public static final ItemStack SOULBOUND_CHESTPLATE = new SlimefunItemStack("SOULBOUND_CHESTPLATE", Material.DIAMOND_CHESTPLATE, "&cSoulbound Chestplate");
    public static final ItemStack SOULBOUND_LEGGINGS = new SlimefunItemStack("SOULBOUND_LEGGINGS", Material.DIAMOND_LEGGINGS, "&cSoulbound Leggings");
    public static final ItemStack SOULBOUND_BOOTS = new SlimefunItemStack("SOULBOUND_BOOTS", Material.DIAMOND_BOOTS, "&cSoulbound Boots");

    /* Runes */
    public static final ItemStack BLANK_RUNE = new SlimefunItemStack("BLANK_RUNE", new ColoredFireworkStar(Color.BLACK, "&8Blank Rune"));

    public static final ItemStack RUNE_AIR = new SlimefunItemStack("ANCIENT_RUNE_AIR", new ColoredFireworkStar(Color.AQUA, "&7Ancient Rune &8&l[&b&lAir&8&l]"));
    public static final ItemStack RUNE_WATER = new SlimefunItemStack("ANCIENT_RUNE_WATER", new ColoredFireworkStar(Color.BLUE, "&7Ancient Rune &8&l[&1&lWater&8&l]"));
    public static final ItemStack RUNE_FIRE = new SlimefunItemStack("ANCIENT_RUNE_FIRE", new ColoredFireworkStar(Color.RED, "&7Ancient Rune &8&l[&4&lFire&8&l]"));
    public static final ItemStack RUNE_EARTH = new SlimefunItemStack("ANCIENT_RUNE_EARTH", new ColoredFireworkStar(Color.fromRGB(112, 47, 7), "&7Ancient Rune &8&l[&c&lEarth&8&l]"));
    public static final ItemStack RUNE_ENDER = new SlimefunItemStack("ANCIENT_RUNE_ENDER", new ColoredFireworkStar(Color.PURPLE, "&7Ancient Rune &8&l[&5&lEnder&8&l]"));

    public static final ItemStack RUNE_RAINBOW = new SlimefunItemStack("ANCIENT_RUNE_RAINBOW", new ColoredFireworkStar(Color.FUCHSIA, "&7Ancient Rune &8&l[&d&lRainbow&8&l]"));
    public static final ItemStack RUNE_LIGHTNING = new SlimefunItemStack("ANCIENT_RUNE_LIGHTNING", new ColoredFireworkStar(Color.fromRGB(255, 255, 95), "&7Ancient Rune &8&l[&e&lLightning&8&l]"));
    public static final ItemStack RUNE_SOULBOUND = new SlimefunItemStack("ANCIENT_RUNE_SOULBOUND", new ColoredFireworkStar(Color.fromRGB(47, 0, 117), "&7Ancient Rune &8&l[&5&lSoulbound&8&l]", "&eDrop this rune onto a dropped item to", "&5bind &ethat item to your soul.", " ", "&eIt is advised that you only use this rune", "&eon &6important &eitems.", " ", "&eItems bound to your soul won't drop on death."));

    /* Electricity */
    public static final ItemStack SOLAR_GENERATOR = new SlimefunItemStack("SOLAR_GENERATOR", Material.DAYLIGHT_DETECTOR, "&bSolar Generator", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(4));
    public static final ItemStack SOLAR_GENERATOR_2 = new SlimefunItemStack("SOLAR_GENERATOR_2", Material.DAYLIGHT_DETECTOR, "&cAdvanced Solar Generator", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(16));
    public static final ItemStack SOLAR_GENERATOR_3 = new SlimefunItemStack("SOLAR_GENERATOR_3", Material.DAYLIGHT_DETECTOR, "&4Carbonado Solar Generator", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(64));
    public static final ItemStack SOLAR_GENERATOR_4 = new SlimefunItemStack("SOLAR_GENERATOR_4", Material.DAYLIGHT_DETECTOR, "&eEnergized Solar Generator", "", "&9Works at Night", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(256) + " (Day)", LoreBuilder.powerPerSecond(128) + " (Night)");

    public static final ItemStack COAL_GENERATOR = new SlimefunItemStack("COAL_GENERATOR", "9343ce58da54c79924a2c9331cfc417fe8ccbbea9be45a7ac85860a6c730", "&cCoal Generator", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.GENERATOR), LoreBuilder.powerBuffer(64), LoreBuilder.powerPerSecond(16));
    public static final ItemStack COAL_GENERATOR_2 = new SlimefunItemStack("COAL_GENERATOR_2", "9343ce58da54c79924a2c9331cfc417fe8ccbbea9be45a7ac85860a6c730", "&cCoal Generator &7(&eII&7)", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.GENERATOR), LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(30));

    public static final ItemStack LAVA_GENERATOR = new SlimefunItemStack("LAVA_GENERATOR", "9343ce58da54c79924a2c9331cfc417fe8ccbbea9be45a7ac85860a6c730", "&4Lava Generator", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.GENERATOR), LoreBuilder.powerBuffer(512), LoreBuilder.powerPerSecond(20));
    public static final ItemStack LAVA_GENERATOR_2 = new SlimefunItemStack("LAVA_GENERATOR_2", "9343ce58da54c79924a2c9331cfc417fe8ccbbea9be45a7ac85860a6c730", "&4Lava Generator &7(&eII&7)", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.GENERATOR), LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(40));

    public static final ItemStack ELECTRIC_FURNACE = new SlimefunItemStack("ELECTRIC_FURNACE", Material.FURNACE, "&cElectric Furnace", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(4));
    public static final ItemStack ELECTRIC_FURNACE_2 = new SlimefunItemStack("ELECTRIC_FURNACE_2", Material.FURNACE, "&cElectric Furnace &7- &eII", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7Speed: 2x", LoreBuilder.powerPerSecond(6));
    public static final ItemStack ELECTRIC_FURNACE_3 = new SlimefunItemStack("ELECTRIC_FURNACE_3", Material.FURNACE, "&cElectric Furnace &7- &eIII", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7Speed: 4x", LoreBuilder.powerPerSecond(10));

    public static final ItemStack ELECTRIC_ORE_GRINDER = new SlimefunItemStack("ELECTRIC_ORE_GRINDER", Material.FURNACE, "&cElectric Ore Grinder", "", "&rWorks as an Ore Crusher and Grind Stone", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(12));
    public static final ItemStack ELECTRIC_ORE_GRINDER_2 = new SlimefunItemStack("ELECTRIC_ORE_GRINDER_2", Material.FURNACE, "&cElectric Ore Grinder &7(&eII&7)", "", "&rWorks as an Ore Crusher and Grind Stone", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 4x", LoreBuilder.powerPerSecond(30));
    public static final ItemStack ELECTRIC_INGOT_PULVERIZER = new SlimefunItemStack("ELECTRIC_INGOT_PULVERIZER", Material.FURNACE, "&cElectric Ingot Pulverizer", "", "&rPulverizes Ingots into Dust", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(14));
    public static final ItemStack AUTO_DRIER = new SlimefunItemStack("AUTO_DRIER", Material.SMOKER, "&eAuto Drier", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(10));
    public static final ItemStack AUTO_ENCHANTER = new SlimefunItemStack("AUTO_ENCHANTER", Material.ENCHANTING_TABLE, "&5Auto Enchanter", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(18));
    public static final ItemStack AUTO_DISENCHANTER = new SlimefunItemStack("AUTO_DISENCHANTER", Material.ENCHANTING_TABLE, "&5Auto Disenchanter", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(18));
    public static final ItemStack AUTO_ANVIL = new SlimefunItemStack("AUTO_ANVIL", Material.IRON_BLOCK, "&7Auto Anvil", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7Repair Factor: 10%", LoreBuilder.powerPerSecond(24));
    public static final ItemStack AUTO_ANVIL_2 = new SlimefunItemStack("AUTO_ANVIL_2", Material.IRON_BLOCK, "&7Auto Anvil Mk.II", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Repair Factor: 25%", LoreBuilder.powerPerSecond(32));

    public static final ItemStack BIO_REACTOR = new SlimefunItemStack("BIO_REACTOR", Material.LIME_TERRACOTTA, "&2Bio Reactor", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.GENERATOR), LoreBuilder.powerBuffer(128), LoreBuilder.powerPerSecond(8));
    public static final ItemStack MULTIMETER = new SlimefunItemStack("MULTIMETER", Material.CLOCK, "&eMultimeter", "", "&rMeasures the Amount of stored", "&rEnergy in a Block");

    public static final ItemStack SMALL_CAPACITOR = new SlimefunItemStack("SMALL_CAPACITOR", "91361e576b493cbfdfae328661cedd1add55fab4e5eb418b92cebf6275f8bb4", "&aSmall Energy Capacitor", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &7128 J Capacity");
    public static final ItemStack MEDIUM_CAPACITOR = new SlimefunItemStack("MEDIUM_CAPACITOR", "91361e576b493cbfdfae328661cedd1add55fab4e5eb418b92cebf6275f8bb4", "&aMedium Energy Capacitor", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &7512 J Capacity");
    public static final ItemStack BIG_CAPACITOR = new SlimefunItemStack("BIG_CAPACITOR", "91361e576b493cbfdfae328661cedd1add55fab4e5eb418b92cebf6275f8bb4", "&aBig Energy Capacitor", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &71024 J Capacity");
    public static final ItemStack LARGE_CAPACITOR = new SlimefunItemStack("LARGE_CAPACITOR", "91361e576b493cbfdfae328661cedd1add55fab4e5eb418b92cebf6275f8bb4", "&aLarge Energy Capacitor", "", LoreBuilder.machine(MachineTier.GOOD, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &78192 J Capacity");
    public static final ItemStack CARBONADO_EDGED_CAPACITOR = new SlimefunItemStack("CARBONADO_EDGED_CAPACITOR", "91361e576b493cbfdfae328661cedd1add55fab4e5eb418b92cebf6275f8bb4", "&aCarbonado Edged Energy Capacitor", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &765536 J Capacity");

    /* Robots */
    public static final ItemStack PROGRAMMABLE_ANDROID = new SlimefunItemStack("PROGRAMMABLE_ANDROID", "3503cb7ed845e7a507f569afc647c47ac483771465c9a679a54594c76afba", "&cProgrammable Android &7(Normal)", "", "&8\u21E8 &7Function: None", "&8\u21E8 &7Fuel Efficiency: 1.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_FARMER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_FARMER", "f9d33357e8418823bf783de92de80291b4ebd392aec8706698e06896d498f6", "&cProgrammable Android &7(Farmer)", "", "&8\u21E8 &7Function: Farming", "&8\u21E8 &7Fuel Efficiency: 1.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_MINER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_MINER", "e638a28541ab3ae0a723d5578738e08758388ec4c33247bd4ca13482aef334", "&cProgrammable Android &7(Miner)", "", "&8\u21E8 &7Function: Mining", "&8\u21E8 &7Fuel Efficiency: 1.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_WOODCUTTER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_WOODCUTTER", "d32a814510142205169a1ad32f0a745f18e9cb6c66ee64eca2e65babdef9ff", "&cProgrammable Android &7(Woodcutter)", "", "&8\u21E8 &7Function: Woodcutting", "&8\u21E8 &7Fuel Efficiency: 1.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_BUTCHER", "3b472df0ad9a3be88f2e5d5d422d02b116d64d8df1475ed32e546afc84b31", "&cProgrammable Android &7(Butcher)", "", "&8\u21E8 &7Function: Slaughtering", "&8\u21E8 &7Damage: 4", "&8\u21E8 &7Fuel Efficiency: 1.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_FISHERMAN", "345e8733a73114333b98b3601751241722f4713e1a1a5d36fbb132493f1c7", "&cProgrammable Android &7(Fisherman)", "", "&8\u21E8 &7Function: Fishing", "&8\u21E8 &7Success Rate: 10%", "&8\u21E8 &7Fuel Efficiency: 1.0x");

    public static final ItemStack PROGRAMMABLE_ANDROID_2 = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2", "3503cb7ed845e7a507f569afc647c47ac483771465c9a679a54594c76afba", "&cAdvanced Programmable Android &7(Normal)", "", "&8\u21E8 &7Function: None", "&8\u21E8 &7Fuel Efficiency: 1.5x");
    public static final ItemStack PROGRAMMABLE_ANDROID_2_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_FISHERMAN", "345e8733a73114333b98b3601751241722f4713e1a1a5d36fbb132493f1c7", "&cAdvanced Programmable Android &7(Fisherman)", "", "&8\u21E8 &7Function: Fishing", "&8\u21E8 &7Success Rate: 20%", "&8\u21E8 &7Fuel Efficiency: 1.5x");
    public static final ItemStack PROGRAMMABLE_ANDROID_2_FARMER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_FARMER", "f9d33357e8418823bf783de92de80291b4ebd392aec8706698e06896d498f6", "&cAdvanced Programmable Android &7(Farmer)", "", "&8\u21E8 &7Function: Farming", "&8\u21E8 &7Fuel Efficiency: 1.5x", "&8\u21E8 &7Can also harvest Plants from ExoticGarden");
    public static final ItemStack PROGRAMMABLE_ANDROID_2_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_BUTCHER", "3b472df0ad9a3be88f2e5d5d422d02b116d64d8df1475ed32e546afc84b31", "&cAdvanced Programmable Android &7(Butcher)", "", "&8\u21E8 &7Function: Slaughtering", "&8\u21E8 &7Damage: 8", "&8\u21E8 &7Fuel Efficiency: 1.5x");

    public static final ItemStack PROGRAMMABLE_ANDROID_3 = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3", "3503cb7ed845e7a507f569afc647c47ac483771465c9a679a54594c76afba", "&eEmpowered Programmable Android &7(Normal)", "", "&8\u21E8 &7Function: None", "&8\u21E8 &7Fuel Efficiency: 3.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_3_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3_FISHERMAN", "345e8733a73114333b98b3601751241722f4713e1a1a5d36fbb132493f1c7", "&eEmpowered Programmable Android &7(Fisherman)", "", "&8\u21E8 &7Function: Fishing", "&8\u21E8 &7Success Rate: 30%", "&8\u21E8 &7Fuel Efficiency: 8.0x");
    public static final ItemStack PROGRAMMABLE_ANDROID_3_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3_BUTCHER", "3b472df0ad9a3be88f2e5d5d422d02b116d64d8df1475ed32e546afc84b31", "&eEmpowered Programmable Android &7(Butcher)", "", "&8\u21E8 &7Function: Slaughtering", "&8\u21E8 &7Damage: 20", "&8\u21E8 &7Fuel Efficiency: 8.0x");

    /* GPS */
    public static final ItemStack GPS_TRANSMITTER = new SlimefunItemStack("GPS_TRANSMITTER", "b0c9c1a022f40b73f14b4cba37c718c6a533f3a2864b6536d5f456934cc1f", "&bGPS Transmitter", "", LoreBuilder.powerBuffer(16), LoreBuilder.powerPerSecond(2));
    public static final ItemStack GPS_TRANSMITTER_2 = new SlimefunItemStack("GPS_TRANSMITTER_2", "b0c9c1a022f40b73f14b4cba37c718c6a533f3a2864b6536d5f456934cc1f", "&cAdvanced GPS Transmitter", "", LoreBuilder.powerBuffer(64), LoreBuilder.powerPerSecond(6));
    public static final ItemStack GPS_TRANSMITTER_3 = new SlimefunItemStack("GPS_TRANSMITTER_3", "b0c9c1a022f40b73f14b4cba37c718c6a533f3a2864b6536d5f456934cc1f", "&4Carbonado GPS Transmitter", "", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(22));
    public static final ItemStack GPS_TRANSMITTER_4 = new SlimefunItemStack("GPS_TRANSMITTER_4", "b0c9c1a022f40b73f14b4cba37c718c6a533f3a2864b6536d5f456934cc1f", "&eEnergized GPS Transmitter", "", LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(92));

    public static final ItemStack GPS_MARKER_TOOL = new SlimefunItemStack("GPS_MARKER_TOOL", Material.REDSTONE_TORCH, "&bGPS Marker Tool", "", "&rAllows you to set a Waypoint at", "&rthe Location you place this");
    public static final ItemStack GPS_CONTROL_PANEL = new SlimefunItemStack("GPS_CONTROL_PANEL", "ddcfba58faf1f64847884111822b64afa21d7fc62d4481f14f3f3bcb6330", "&bGPS Control Panel", "", "&rAllows you to track your Satellites", "&rand manage your Waypoints");
    public static final ItemStack GPS_EMERGENCY_TRANSMITTER = new SlimefunItemStack("GPS_EMERGENCY_TRANSMITTER", "b0c9c1a022f40b73f14b4cba37c718c6a533f3a2864b6536d5f456934cc1f", "&cGPS Emergency Transmitter", "", "&rCarrying this in your Inventory", "&rautomatically sets a Waypoint", "&rat your Location when you die.");

    public static final ItemStack ANDROID_INTERFACE_FUEL = new SlimefunItemStack("ANDROID_INTERFACE_FUEL", Material.DISPENSER, "&7Android Interface &c(Fuel)", "", "&rItems stored in this Interface", "&rwill be inserted into an Android's Fuel Slot", "&rwhen its Script tells them to do so");
    public static final ItemStack ANDROID_INTERFACE_ITEMS = new SlimefunItemStack("ANDROID_INTERFACE_ITEMS", Material.DISPENSER, "&7Android Interface &9(Items)", "", "&rItems stored in an Android's Inventory", "&rwill be inserted into this Interface", "&rwhen its Script tells them to do so");

    public static final ItemStack GPS_GEO_SCANNER = new SlimefunItemStack("GPS_GEO_SCANNER", "2ad8cfeb387a56e3e5bcf85345d6a417b242293887db3ce3ba91fa409b254b86", "&bGPS Geo-Scanner", "", "&rScans a Chunk for natural Resources", "&rsuch as &8Oil");
    public static final ItemStack PORTABLE_GEO_SCANNER = new SlimefunItemStack("PORTABLE_GEO_SCANNER", Material.CLOCK, "&bPortable Geo-Scanner", "", "&rScans a Chunk for natural Resources", "", "&eRight Click&7 to scan");
    public static final ItemStack GEO_MINER = new SlimefunItemStack("GEO_MINER", "a37741f764dd3dd7adaeb43b63d3959eb70e5eb28f15d6b34cab34a1d1f60387", "&6GEO Miner", "", "&eMines up resources from the chunk", "&eThese Resources cannot be mined with a pickaxe", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(48), "", "&c&l! &cMake sure to Geo-Scan the Chunk first");
    public static final ItemStack OIL_PUMP = new SlimefunItemStack("OIL_PUMP", "afe1a040a425e31a46d4f9a9b9806fa2f0c47ee84711cc1932fd8ab32b2d038", "&rOil Pump", "", "&7Pumps up Oil and fills it into Buckets", "", "&c&l! &cMake sure to Geo-Scan the Chunk first");
    public static final ItemStack BUCKET_OF_OIL = new SlimefunItemStack("BUCKET_OF_OIL", "6ce04b41d19ec7927f982a63a94a3d79f78ecec33363051fde0831bfabdbd", "&rBucket of Oil");
    public static final ItemStack BUCKET_OF_FUEL = new SlimefunItemStack("BUCKET_OF_FUEL", "a84ddca766725b8b97413f259c3f7668070f6ae55483a90c8e5525394f9c099", "&rBucket of Fuel");

    public static final ItemStack REFINERY = new SlimefunItemStack("REFINERY", Material.PISTON, "&cRefinery", "", "&rRefines Oil to create Fuel");
    public static final ItemStack COMBUSTION_REACTOR = new SlimefunItemStack("COMBUSTION_REACTOR", "9343ce58da54c79924a2c9331cfc417fe8ccbbea9be45a7ac85860a6c730", "&cCombustion Reactor", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.GENERATOR), LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(24));
    public static final ItemStack ANDROID_MEMORY_CORE = new SlimefunItemStack("ANDROID_MEMORY_CORE", "d78f2b7e5e75639ea7fb796c35d364c4df28b4243e66b76277aadcd6261337", "&bAndroid Memory Core");

    public static final ItemStack GPS_TELEPORTER_PYLON = new SlimefunItemStack("GPS_TELEPORTER_PYLON", Material.PURPLE_STAINED_GLASS, "&5GPS Teleporter Pylon", "", "&7Teleporter Component");
    public static final ItemStack GPS_TELEPORTATION_MATRIX = new SlimefunItemStack("GPS_TELEPORTATION_MATRIX", Material.IRON_BLOCK, "&bGPS Teleporter Matrix", "", "&rThis is your Teleporter's Main Component", "&rThis Matrix allows Players to choose from all", "&rWaypoints made by the Player who has placed", "&rthis Device.");
    public static final ItemStack GPS_ACTIVATION_DEVICE_SHARED = new SlimefunItemStack("GPS_ACTIVATION_DEVICE_SHARED", Material.STONE_PRESSURE_PLATE, "&rGPS Activation Device &3(Shared)", "", "&rPlace this onto a Teleportation Matrix", "&rand step onto this Plate to activate", "&rthe Teleportation Process");
    public static final ItemStack GPS_ACTIVATION_DEVICE_PERSONAL = new SlimefunItemStack("GPS_ACTIVATION_DEVICE_PERSONAL", Material.STONE_PRESSURE_PLATE, "&rGPS Activation Device &a(Personal)", "", "&rPlace this onto a Teleportation Matrix", "&rand step onto this Plate to activate", "&rthe Teleportation Process", "", "&rThis Version only allows the Person who", "&rplaced this Device to use it");

    public static final ItemStack ELEVATOR = new SlimefunItemStack("ELEVATOR_PLATE", Material.STONE_PRESSURE_PLATE, "&bElevator Plate", "", "&rPlace an Elevator Plate on every floor", "&rand you will be able to teleport between them.", "", "&eRight Click this Block &7to name it");

    public static final ItemStack INFUSED_HOPPER = new SlimefunItemStack("INFUSED_HOPPER", Material.HOPPER, "&5Infused Hopper", "", "&rAutomatically picks up nearby Items in a 7x7x7", "&rRadius when placed.");

    public static final ItemStack PLASTIC_SHEET = new SlimefunItemStack("PLASTIC_SHEET", Material.PAPER, "&rPlastic Sheet");

    public static final ItemStack HEATED_PRESSURE_CHAMBER = new SlimefunItemStack("HEATED_PRESSURE_CHAMBER", Material.LIGHT_GRAY_STAINED_GLASS, "&cHeated Pressure Chamber", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(10));
    public static final ItemStack HEATED_PRESSURE_CHAMBER_2 = new SlimefunItemStack("HEATED_PRESSURE_CHAMBER_2", Material.LIGHT_GRAY_STAINED_GLASS, "&cHeated Pressure Chamber &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 5x", LoreBuilder.powerPerSecond(44));

    public static final ItemStack ELECTRIC_SMELTERY = new SlimefunItemStack("ELECTRIC_SMELTERY", Material.FURNACE, "&cElectric Smeltery", "", "&4Alloys-Only, doesn't smelt Dust into Ingots", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(20));
    public static final ItemStack ELECTRIC_SMELTERY_2 = new SlimefunItemStack("ELECTRIC_SMELTERY_2", Material.FURNACE, "&cElectric Smeltery &7- &eII", "", "&4Alloys-Only, doesn't smelt Dust into Ingots", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 3x", LoreBuilder.powerPerSecond(40));

    public static final ItemStack ELECTRIC_PRESS = new SlimefunItemStack("ELECTRIC_PRESS", "8d5cf92bc79ec19f4106441affff1406a1367010dcafb197dd94cfca1a6de0fc", "&eElectric Press", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(16));
    public static final ItemStack ELECTRIC_PRESS_2 = new SlimefunItemStack("ELECTRIC_PRESS_2", "8d5cf92bc79ec19f4106441affff1406a1367010dcafb197dd94cfca1a6de0fc", "&eElectric Press &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 3x", LoreBuilder.powerPerSecond(40));

    public static final ItemStack ELECTRIFIED_CRUCIBLE = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE", Material.RED_TERRACOTTA, "&cElectrified Crucible", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(48));
    public static final ItemStack ELECTRIFIED_CRUCIBLE_2 = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE_2", Material.RED_TERRACOTTA, "&cElectrified Crucible &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 2x", LoreBuilder.powerPerSecond(80));
    public static final ItemStack ELECTRIFIED_CRUCIBLE_3 = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE_3", Material.RED_TERRACOTTA, "&cElectrified Crucible &7- &eIII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 4x", LoreBuilder.powerPerSecond(120));

    public static final ItemStack CARBON_PRESS = new SlimefunItemStack("CARBON_PRESS", Material.BLACK_STAINED_GLASS, "&cCarbon Press", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(20));
    public static final ItemStack CARBON_PRESS_2 = new SlimefunItemStack("CARBON_PRESS_2", Material.BLACK_STAINED_GLASS, "&cCarbon Press &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 3x", LoreBuilder.powerPerSecond(50));
    public static final ItemStack CARBON_PRESS_3 = new SlimefunItemStack("CARBON_PRESS_3", Material.BLACK_STAINED_GLASS, "&cCarbon Press &7- &eIII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 15x", LoreBuilder.powerPerSecond(180));

    public static final ItemStack BLISTERING_INGOT = new SlimefunItemStack("BLISTERING_INGOT", Material.GOLD_INGOT, "&6Blistering Ingot &7(33%)", "", LoreBuilder.radioactive(Radioactivity.HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final ItemStack BLISTERING_INGOT_2 = new SlimefunItemStack("BLISTERING_INGOT_2", Material.GOLD_INGOT, "&6Blistering Ingot &7(66%)", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final ItemStack BLISTERING_INGOT_3 = new SlimefunItemStack("BLISTERING_INGOT_3", Material.GOLD_INGOT, "&6Blistering Ingot", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);

    public static final ItemStack ENERGY_REGULATOR = new SlimefunItemStack("ENERGY_REGULATOR", "d78f2b7e5e75639ea7fb796c35d364c4df28b4243e66b76277aadcd6261337", "&6Energy Regulator", "", "&rCore Component of an Energy Network");
    public static final ItemStack DEBUG_FISH = new CustomItem(Material.SALMON, "&3How much is the Fish?", "", "&eRight Click &rany Block to view it's BlockData", "&eLeft Click &rto break a Block", "&eShift + Left Click &rany Block to erase it's BlockData", "&eShift + Right Click &rto place a Placeholder Block");

    public static final ItemStack NETHER_ICE = new SlimefunItemStack("NETHER_ICE", "3ce2dad9baf7eaba7e80d4d0f9fac0aab01a76b12fb71c3d2af2a16fdd4c7383", "&eNether Ice", "", LoreBuilder.radioactive(Radioactivity.MODERATE), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final ItemStack ENRICHED_NETHER_ICE = new SlimefunItemStack("ENRICHED_NETHER_ICE", "7c818aa13aabc7294838d21caac057e97bd8c89641a0c0f8a55442ff4e27", "&eEnriched Nether Ice", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final ItemStack NETHER_ICE_COOLANT_CELL = new SlimefunItemStack("NETHER_ICE_COOLANT_CELL", "8d3cd412555f897016213e5d6c7431b448b9e5644e1b19ec51b5316f35840e0", "&6Nether Ice Coolant Cell");

    // Cargo
    public static final ItemStack CARGO_MANAGER = new SlimefunItemStack("CARGO_MANAGER", "e510bc85362a130a6ff9d91ff11d6fa46d7d1912a3431f751558ef3c4d9c2", "&6Cargo Manager", "", "&rCore Component of an Item Transport Network");
    public static final ItemStack CARGO_NODE = new SlimefunItemStack("CARGO_NODE", "07b7ef6fd7864865c31c1dc87bed24ab5973579f5c6638fecb8dedeb443ff0", "&7Cargo Node &c(Connector)", "", "&rCargo Connector Pipe");
    public static final ItemStack CARGO_INPUT = new SlimefunItemStack("CARGO_NODE_INPUT", "16d1c1a69a3de9fec962a77bf3b2e376dd25c873a3d8f14f1dd345dae4c4", "&7Cargo Node &c(Input)", "", "&rCargo Input Pipe");
    public static final ItemStack CARGO_OUTPUT = new SlimefunItemStack("CARGO_NODE_OUTPUT", "55b21fd480c1c43bf3b9f842c869bdc3bc5acc2599bf2eb6b8a1c95dce978f", "&7Cargo Node &c(Output)", "", "&rCargo Output Pipe");
    public static final ItemStack CARGO_OUTPUT_ADVANCED = new SlimefunItemStack("CARGO_NODE_OUTPUT_ADVANCED", "55b21fd480c1c43bf3b9f842c869bdc3bc5acc2599bf2eb6b8a1c95dce978f", "&6Advanced Cargo Node &c(Output)", "", "&rCargo Output Pipe");

    public static final ItemStack AUTO_BREEDER = new SlimefunItemStack("AUTO_BREEDER", Material.HAY_BLOCK, "&eAuto-Breeder", "", "&rRuns on &aOrganic Food", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerBuffer(1024), "&8\u21E8 &e\u26A1 &760 J/Animal");

    public static final ItemStack ORGANIC_FOOD = new CustomItem(SkullItem.fromBase64("b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79"), "&aOrganic Food", "&7Content: &9X");
    public static final ItemStack WHEAT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_WHEAT", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Food", "&7Content: &9Wheat");
    public static final ItemStack CARROT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_CARROT", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Food", "&7Content: &9Carrots");
    public static final ItemStack POTATO_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_POTATO", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Food", "&7Content: &9Potatoes");
    public static final ItemStack SEEDS_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SEEDS", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Food", "&7Content: &9Seeds");
    public static final ItemStack BEETROOT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_BEETROOT", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Food", "&7Content: &9Beetroot");
    public static final ItemStack MELON_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_MELON", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Food", "&7Content: &9Melon");
    public static final ItemStack APPLE_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_APPLE", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Food", "&7Content: &9Apple");
    public static final ItemStack SWEET_BERRIES_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SWEET_BERRIES", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Food", "&7Content: &9Sweet Berries");
    public static final ItemStack KELP_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_KELP", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Food", "&7Content: &9Dried Kelp");

    public static final ItemStack FERTILIZER = new CustomItem(SkullItem.fromBase64("b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79"), "&aOrganic Fertilizer", "&7Content: &9X");
    public static final ItemStack WHEAT_FERTILIZER = new SlimefunItemStack("FERTILIZER_WHEAT", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Fertilizer", "&7Content: &9Wheat");
    public static final ItemStack CARROT_FERTILIZER = new SlimefunItemStack("FERTILIZER_CARROT", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Fertilizer", "&7Content: &9Carrots");
    public static final ItemStack POTATO_FERTILIZER = new SlimefunItemStack("FERTILIZER_POTATO", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Fertilizer", "&7Content: &9Potatoes");
    public static final ItemStack SEEDS_FERTILIZER = new SlimefunItemStack("FERTILIZER_SEEDS", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Fertilizer", "&7Content: &9Seeds");
    public static final ItemStack BEETROOT_FERTILIZER = new SlimefunItemStack("FERTILIZER_BEETROOT", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Fertilizer", "&7Content: &9Beetroot");
    public static final ItemStack MELON_FERTILIZER = new SlimefunItemStack("FERTILIZER_MELON", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Fertilizer", "&7Content: &9Melon");
    public static final ItemStack APPLE_FERTILIZER = new SlimefunItemStack("FERTILIZER_APPLE", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Fertilizer", "&7Content: &9Apple");
    public static final ItemStack SWEET_BERRIES_FERTILIZER = new SlimefunItemStack("FERTILIZER_SWEET_BERRIES", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Fertilizer", "&7Content: &9Sweet Berries");
    public static final ItemStack KELP_FERTILIZER = new SlimefunItemStack("FERTILIZER_KELP", "b439e3f5acbee9be4c4259289d6d9f35c635ffa661114687b3ea6dda8c79", "&aOrganic Fertilizer", "&7Content: &9Dried Kelp");

    public static final ItemStack ANIMAL_GROWTH_ACCELERATOR = new SlimefunItemStack("ANIMAL_GROWTH_ACCELERATOR", Material.HAY_BLOCK, "&bAnimal Growth Accelerator", "", "&rRuns on &aOrganic Food", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(28));
    public static final ItemStack CROP_GROWTH_ACCELERATOR = new SlimefunItemStack("CROP_GROWTH_ACCELERATOR", Material.LIME_TERRACOTTA, "&aCrop Growth Accelerator", "", "&rRuns on &aOrganic Fertilizer", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Radius: 7x7", "&8\u21E8 &7Speed: &a3/time", LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(50));
    public static final ItemStack CROP_GROWTH_ACCELERATOR_2 = new SlimefunItemStack("CROP_GROWTH_ACCELERATOR_2", Material.LIME_TERRACOTTA, "&aCrop Growth Accelerator &7(&eII&7)", "", "&rRuns on &aOrganic Fertilizer", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Radius: 9x9", "&8\u21E8 &7Speed: &a4/time", LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(60));

    public static final ItemStack FOOD_FABRICATOR = new SlimefunItemStack("FOOD_FABRICATOR", Material.GREEN_STAINED_GLASS, "&cFood Fabricator", "", "&rProduces &aOrganic Food", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(14));
    public static final ItemStack FOOD_FABRICATOR_2 = new SlimefunItemStack("FOOD_FABRICATOR_2", Material.GREEN_STAINED_GLASS, "&cFood Fabricator &7(&eII&7)", "", "&rProduces &aOrganic Food", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 6x", LoreBuilder.powerBuffer(512), LoreBuilder.powerPerSecond(48));

    public static final ItemStack FOOD_COMPOSTER = new SlimefunItemStack("FOOD_COMPOSTER", Material.GREEN_TERRACOTTA, "&cFood Composter", "", "&rProduces &aOrganic Fertilizer", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(16));
    public static final ItemStack FOOD_COMPOSTER_2 = new SlimefunItemStack("FOOD_COMPOSTER_2", Material.GREEN_TERRACOTTA, "&cFood Composter &7(&eII&7)", "", "&rProduces &aOrganic Fertilizer", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 10x", LoreBuilder.powerBuffer(512), LoreBuilder.powerPerSecond(52));

    public static final ItemStack XP_COLLECTOR = new SlimefunItemStack("XP_COLLECTOR", "1762a15b04692a2e4b3fb3663bd4b78434dce1732b8eb1c7a9f7c0fbf6f", "&aEXP Collector", "", "&rCollects nearby Exp and stores it", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(20));
    public static final ItemStack REACTOR_COOLANT_CELL = new SlimefunItemStack("REACTOR_COLLANT_CELL", "de4073be40cb3deb310a0be959b4cac68e825372728fafb6c2973e4e7c33", "&bReactor Coolant Cell");

    public static final ItemStack NUCLEAR_REACTOR = new SlimefunItemStack("NUCLEAR_REACTOR", "9343ce58da54c79924a2c9331cfc417fe8ccbbea9be45a7ac85860a6c730", "&2Nuclear Reactor", "", "&rRequires Cooling!", "&8\u21E8 &bMust be surrounded by Water", "&8\u21E8 &bMust be supplied with Reactor Coolant Cells", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(16384), LoreBuilder.powerPerSecond(500));
    public static final ItemStack NETHERSTAR_REACTOR = new SlimefunItemStack("NETHERSTAR_REACTOR", "9343ce58da54c79924a2c9331cfc417fe8ccbbea9be45a7ac85860a6c730", "&fNether Star Reactor", "", "&fRuns on Nether Stars", "&8\u21E8 &bMust be surrounded by Water", "&8\u21E8 &bMust be supplied with Nether Ice Coolant Cells", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(32768), LoreBuilder.powerPerSecond(1024), "&8\u21E8 &4Causes nearby Entities to get Withered");
    public static final ItemStack REACTOR_ACCESS_PORT = new SlimefunItemStack("REACTOR_ACCESS_PORT", Material.CYAN_TERRACOTTA, "&2Reactor Access Port", "", "&rAllows you to interact with a Reactor", "&rvia Cargo Nodes, can also be used", "&ras a Buffer", "", "&8\u21E8 &eMust be placed &a3 Blocks &eabove the Reactor");

    public static final ItemStack FREEZER = new SlimefunItemStack("FREEZER", Material.LIGHT_BLUE_STAINED_GLASS, "&bFreezer", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(18));
    public static final ItemStack FREEZER_2 = new SlimefunItemStack("FREEZER_2", Material.LIGHT_BLUE_STAINED_GLASS, "&bFreezer &7(&eII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 2x", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(30));

    public static final ItemStack ELECTRIC_GOLD_PAN = new SlimefunItemStack("ELECTRIC_GOLD_PAN", Material.BROWN_TERRACOTTA, "&6Electric Gold Pan", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(2));
    public static final ItemStack ELECTRIC_GOLD_PAN_2 = new SlimefunItemStack("ELECTRIC_GOLD_PAN_2", Material.BROWN_TERRACOTTA, "&6Electric Gold Pan &7(&eII&7)", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7Speed: 3x", LoreBuilder.powerPerSecond(4));
    public static final ItemStack ELECTRIC_GOLD_PAN_3 = new SlimefunItemStack("ELECTRIC_GOLD_PAN_3", Material.BROWN_TERRACOTTA, "&6Electric Gold Pan &7(&eIII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 10x", LoreBuilder.powerPerSecond(14));

    public static final ItemStack ELECTRIC_DUST_WASHER = new SlimefunItemStack("ELECTRIC_DUST_WASHER", Material.BLUE_STAINED_GLASS, "&3Electric Dust Washer", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(6));
    public static final ItemStack ELECTRIC_DUST_WASHER_2 = new SlimefunItemStack("ELECTRIC_DUST_WASHER_2", Material.BLUE_STAINED_GLASS, "&3Electric Dust Washer &7(&eII&7)", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7Speed: 2x", LoreBuilder.powerPerSecond(10));
    public static final ItemStack ELECTRIC_DUST_WASHER_3 = new SlimefunItemStack("ELECTRIC_DUST_WASHER_3", Material.BLUE_STAINED_GLASS, "&3Electric Dust Washer &7(&eIII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 10x", LoreBuilder.powerPerSecond(30));

    public static final ItemStack ELECTRIC_INGOT_FACTORY = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY", Material.RED_TERRACOTTA, "&cElectric Ingot Factory", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", LoreBuilder.powerPerSecond(8));
    public static final ItemStack ELECTRIC_INGOT_FACTORY_2 = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY_2", Material.RED_TERRACOTTA, "&cElectric Ingot Factory &7(&eII&7)", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7Speed: 2x", LoreBuilder.powerPerSecond(14));
    public static final ItemStack ELECTRIC_INGOT_FACTORY_3 = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY_3", Material.RED_TERRACOTTA, "&cElectric Ingot Factory &7(&eIII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 8x", LoreBuilder.powerPerSecond(40));

    public static final ItemStack AUTOMATED_CRAFTING_CHAMBER = new SlimefunItemStack("AUTOMATED_CRAFTING_CHAMBER", Material.CRAFTING_TABLE, "&6Automated Crafting Chamber", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &710 J/Item");
    public static final ItemStack FLUID_PUMP = new SlimefunItemStack("FLUID_PUMP", Material.BLUE_TERRACOTTA, "&9Fluid Pump", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &732 J/Block");
    public static final ItemStack CHARGING_BENCH = new SlimefunItemStack("CHARGING_BENCH", Material.CRAFTING_TABLE, "&6Charging Bench", "", "&rCharges Items such as Jetpacks", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), LoreBuilder.powerBuffer(128), "&8\u21E8 &e\u26A1 &7Energy Loss: &c50%");

    public static final ItemStack WITHER_ASSEMBLER = new SlimefunItemStack("WITHER_ASSEMBLER", Material.OBSIDIAN, "&5Wither Assembler", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Cooldown: &b30 Seconds", LoreBuilder.powerBuffer(4096), "&8\u21E8 &e\u26A1 &74096 J/Wither");

    public static final ItemStack TRASH_CAN = new SlimefunItemStack("TRASH_CAN_BLOCK", "32d41042ce99147cc38cac9e46741576e7ee791283e6fac8d3292cae2935f1f", "&3Trash Can", "", "&rWill destroy all Items put into it");

    public static final ItemStack ELYTRA = new ItemStack(Material.ELYTRA);
    public static final ItemStack ELYTRA_SCALE = new SlimefunItemStack("ELYTRA_SCALE", Material.FEATHER, "&bElytra Scale");
    public static final ItemStack INFUSED_ELYTRA = new SlimefunItemStack("INFUSED_ELYTRA", Material.ELYTRA, "&5Infused Elytra");
    public static final ItemStack SOULBOUND_ELYTRA = new SlimefunItemStack("SOULBOUND_ELYTRA", Material.ELYTRA, "&cSoulbound Elytra");

    public static final ItemStack TOTEM_OF_UNDYING = new ItemStack(Material.TOTEM_OF_UNDYING);

    public static final ItemStack MAGNESIUM_SALT = new SlimefunItemStack("MAGNESIUM_SALT", Material.SUGAR, "&cMagnesium Salt", "", "&7A special type of fuel that can be", "&7used in a Magnesium-powered Generator");
    public static final ItemStack MAGNESIUM_GENERATOR = new SlimefunItemStack("MAGNESIUM_GENERATOR", "9343ce58da54c79924a2c9331cfc417fe8ccbbea9be45a7ac85860a6c730", "&cMagnesium-powered Generator", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.GENERATOR), LoreBuilder.powerBuffer(128), LoreBuilder.powerPerSecond(36));

    static {
        INFUSED_ELYTRA.addUnsafeEnchantment(Enchantment.MENDING, 1);
    }
}
