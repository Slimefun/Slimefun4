package io.github.thebusybiscuit.slimefun5.implementation;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.ItemMetaCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun5.implementation.items.magical.staves.StormStaff;
import io.github.thebusybiscuit.slimefun5.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun5.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedEnchantment;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedItemFlag;
import io.github.thebusybiscuit.slimefun5.utils.itemstack.ColoredFireworkStar;

/**
 * This class holds a static references to every {@link SlimefunItemStack}
 * found in Slimefun.
 */
@SuppressWarnings("java:S1192") // Suppress "duplicate string literal" warnings
public final class SlimefunItems {

    private SlimefunItems() {}

    /* Items */
    public static final SlimefunItemStack PORTABLE_CRAFTER = new SlimefunItemStack("PORTABLE_CRAFTER", HeadTexture.PORTABLE_CRAFTER);
    public static final SlimefunItemStack PORTABLE_DUSTBIN = new SlimefunItemStack("PORTABLE_DUSTBIN", HeadTexture.TRASH_CAN);
    public static final SlimefunItemStack ENDER_BACKPACK = new SlimefunItemStack("ENDER_BACKPACK", HeadTexture.ENDER_BACKPACK);
    public static final SlimefunItemStack MAGIC_EYE_OF_ENDER = new SlimefunItemStack("MAGIC_EYE_OF_ENDER", XMaterial.ENDER_EYE.parseMaterial());
    public static final SlimefunItemStack BROKEN_SPAWNER = new SlimefunItemStack("BROKEN_SPAWNER", XMaterial.SPAWNER.parseMaterial());
    public static final SlimefunItemStack REPAIRED_SPAWNER = new SlimefunItemStack("REINFORCED_SPAWNER", XMaterial.SPAWNER.parseMaterial());
    public static final SlimefunItemStack INFERNAL_BONEMEAL = new SlimefunItemStack("INFERNAL_BONEMEAL", XMaterial.BONE_MEAL.parseMaterial());
    public static final SlimefunItemStack TAPE_MEASURE = new SlimefunItemStack("TAPE_MEASURE", "180d5c43a6cf5bb7769fd0c8240e1e70d2ae38ef9d78a1db401aca6a2cb36f65");

    /* Gadgets */
    // Pilot of the resource-driven form: no hardcoded name/lore - resolved from languages/en/items.yml.
    public static final SlimefunItemStack GOLD_PAN = new SlimefunItemStack("GOLD_PAN", Material.BOWL);
    public static final SlimefunItemStack NETHER_GOLD_PAN = new SlimefunItemStack("NETHER_GOLD_PAN", Material.BOWL);
    public static final SlimefunItemStack PARACHUTE = new SlimefunItemStack("PARACHUTE", Material.LEATHER_CHESTPLATE, Color.WHITE);
    public static final SlimefunItemStack GRAPPLING_HOOK = new SlimefunItemStack("GRAPPLING_HOOK", XMaterial.LEAD.parseMaterial());
    public static final SlimefunItemStack SOLAR_HELMET = new SlimefunItemStack("SOLAR_HELMET", Material.IRON_HELMET);
    public static final SlimefunItemStack CLOTH = new SlimefunItemStack("CLOTH", Material.PAPER);
    public static final SlimefunItemStack REINFORCED_CLOTH = new SlimefunItemStack("REINFORCED_CLOTH", Material.PAPER);
    public static final SlimefunItemStack TIN_CAN = new SlimefunItemStack("CAN", HeadTexture.TIN_CAN);
    public static final SlimefunItemStack NIGHT_VISION_GOGGLES = new SlimefunItemStack("NIGHT_VISION_GOGGLES", Material.LEATHER_HELMET, Color.BLACK);
    public static final SlimefunItemStack ELYTRA_CAP = new SlimefunItemStack("ELYTRA_CAP", Material.LEATHER_HELMET, Color.PURPLE);
    public static final SlimefunItemStack FARMER_SHOES = new SlimefunItemStack("FARMER_SHOES", Material.LEATHER_BOOTS, Color.YELLOW);
    public static final SlimefunItemStack INFUSED_MAGNET = new SlimefunItemStack("INFUSED_MAGNET", HeadTexture.MAGNET);
    public static final SlimefunItemStack RAG = new SlimefunItemStack("RAG", Material.PAPER);
    public static final SlimefunItemStack BANDAGE = new SlimefunItemStack("BANDAGE", Material.PAPER);
    public static final SlimefunItemStack SPLINT = new SlimefunItemStack("SPLINT", Material.STICK);
    public static final SlimefunItemStack VITAMINS = new SlimefunItemStack("VITAMINS", XMaterial.NETHER_WART.parseMaterial());
    public static final SlimefunItemStack MEDICINE = new SlimefunItemStack("MEDICINE", Material.POTION, Color.RED);
    public static final SlimefunItemStack MAGICAL_ZOMBIE_PILLS = new SlimefunItemStack("MAGICAL_ZOMBIE_PILLS", XMaterial.NETHER_WART.parseMaterial());

    public static final SlimefunItemStack FLASK_OF_KNOWLEDGE = new SlimefunItemStack("FLASK_OF_KNOWLEDGE", Material.GLASS_BOTTLE);
    public static final SlimefunItemStack FILLED_FLASK_OF_KNOWLEDGE = new SlimefunItemStack("FILLED_FLASK_OF_KNOWLEDGE", XMaterial.EXPERIENCE_BOTTLE.parseMaterial());

    /* Backpacks */
    public static final SlimefunItemStack BACKPACK_SMALL = new SlimefunItemStack("SMALL_BACKPACK", HeadTexture.BACKPACK);
    public static final SlimefunItemStack BACKPACK_MEDIUM = new SlimefunItemStack("MEDIUM_BACKPACK", HeadTexture.BACKPACK);
    public static final SlimefunItemStack BACKPACK_LARGE = new SlimefunItemStack("LARGE_BACKPACK", HeadTexture.BACKPACK);
    public static final SlimefunItemStack WOVEN_BACKPACK = new SlimefunItemStack("WOVEN_BACKPACK", HeadTexture.BACKPACK);
    public static final SlimefunItemStack GILDED_BACKPACK = new SlimefunItemStack("GILDED_BACKPACK", HeadTexture.BACKPACK);
    public static final SlimefunItemStack RADIANT_BACKPACK = new SlimefunItemStack("RADIANT_BACKPACK", HeadTexture.BACKPACK);
    public static final SlimefunItemStack BOUND_BACKPACK = new SlimefunItemStack("BOUND_BACKPACK", HeadTexture.ENDER_BACKPACK);
    public static final SlimefunItemStack COOLER = new SlimefunItemStack("COOLER", HeadTexture.COOLER);
    public static final SlimefunItemStack RESTORED_BACKPACK = new SlimefunItemStack("RESTORED_BACKPACK", HeadTexture.RESTORED_BACKPACK);

    /* Jetpacks */
    public static final SlimefunItemStack DURALUMIN_JETPACK = new SlimefunItemStack("DURALUMIN_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER);
    public static final SlimefunItemStack SOLDER_JETPACK = new SlimefunItemStack("SOLDER_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER);
    public static final SlimefunItemStack BILLON_JETPACK = new SlimefunItemStack("BILLON_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER);
    public static final SlimefunItemStack STEEL_JETPACK = new SlimefunItemStack("STEEL_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER);
    public static final SlimefunItemStack DAMASCUS_STEEL_JETPACK = new SlimefunItemStack("DAMASCUS_STEEL_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER);
    public static final SlimefunItemStack REINFORCED_ALLOY_JETPACK = new SlimefunItemStack("REINFORCED_ALLOY_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER);
    public static final SlimefunItemStack CARBONADO_JETPACK = new SlimefunItemStack("CARBONADO_JETPACK", Material.LEATHER_CHESTPLATE, Color.BLACK);
    public static final SlimefunItemStack ARMORED_JETPACK = new SlimefunItemStack("ARMORED_JETPACK", Material.IRON_CHESTPLATE);

    /* Jetboots */
    public static final SlimefunItemStack DURALUMIN_JETBOOTS = new SlimefunItemStack("DURALUMIN_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER);
    public static final SlimefunItemStack SOLDER_JETBOOTS = new SlimefunItemStack("SOLDER_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER);
    public static final SlimefunItemStack BILLON_JETBOOTS = new SlimefunItemStack("BILLON_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER);
    public static final SlimefunItemStack STEEL_JETBOOTS = new SlimefunItemStack("STEEL_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER);
    public static final SlimefunItemStack DAMASCUS_STEEL_JETBOOTS = new SlimefunItemStack("DAMASCUS_STEEL_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER);
    public static final SlimefunItemStack REINFORCED_ALLOY_JETBOOTS = new SlimefunItemStack("REINFORCED_ALLOY_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER);
    public static final SlimefunItemStack CARBONADO_JETBOOTS = new SlimefunItemStack("CARBONADO_JETBOOTS", Material.LEATHER_BOOTS, Color.BLACK);
    public static final SlimefunItemStack ARMORED_JETBOOTS = new SlimefunItemStack("ARMORED_JETBOOTS", Material.IRON_BOOTS);

    /* Multi Tools */
    public static final SlimefunItemStack DURALUMIN_MULTI_TOOL = new SlimefunItemStack("DURALUMIN_MULTI_TOOL", Material.SHEARS);
    public static final SlimefunItemStack SOLDER_MULTI_TOOL = new SlimefunItemStack("SOLDER_MULTI_TOOL", Material.SHEARS);
    public static final SlimefunItemStack BILLON_MULTI_TOOL = new SlimefunItemStack("BILLON_MULTI_TOOL", Material.SHEARS);
    public static final SlimefunItemStack STEEL_MULTI_TOOL = new SlimefunItemStack("STEEL_MULTI_TOOL", Material.SHEARS);
    public static final SlimefunItemStack DAMASCUS_STEEL_MULTI_TOOL = new SlimefunItemStack("DAMASCUS_STEEL_MULTI_TOOL", Material.SHEARS);
    public static final SlimefunItemStack REINFORCED_ALLOY_MULTI_TOOL = new SlimefunItemStack("REINFORCED_ALLOY_MULTI_TOOL", Material.SHEARS);
    public static final SlimefunItemStack CARBONADO_MULTI_TOOL = new SlimefunItemStack("CARBONADO_MULTI_TOOL", Material.SHEARS);

    static {
        ItemMeta duralumin = DURALUMIN_MULTI_TOOL.getItemMeta();
        ItemMetaCompat.setUnbreakable(duralumin, true);
        DURALUMIN_MULTI_TOOL.setItemMeta(duralumin);

        ItemMeta solder = SOLDER_MULTI_TOOL.getItemMeta();
        ItemMetaCompat.setUnbreakable(solder, true);
        SOLDER_MULTI_TOOL.setItemMeta(solder);

        ItemMeta billon = BILLON_MULTI_TOOL.getItemMeta();
        ItemMetaCompat.setUnbreakable(billon, true);
        BILLON_MULTI_TOOL.setItemMeta(billon);

        ItemMeta steel = STEEL_MULTI_TOOL.getItemMeta();
        ItemMetaCompat.setUnbreakable(steel, true);
        STEEL_MULTI_TOOL.setItemMeta(steel);

        ItemMeta damascus = DAMASCUS_STEEL_MULTI_TOOL.getItemMeta();
        ItemMetaCompat.setUnbreakable(damascus, true);
        DAMASCUS_STEEL_MULTI_TOOL.setItemMeta(damascus);

        ItemMeta reinforced = REINFORCED_ALLOY_MULTI_TOOL.getItemMeta();
        ItemMetaCompat.setUnbreakable(reinforced, true);
        REINFORCED_ALLOY_MULTI_TOOL.setItemMeta(reinforced);

        ItemMeta carbonado = CARBONADO_MULTI_TOOL.getItemMeta();
        ItemMetaCompat.setUnbreakable(carbonado, true);
        CARBONADO_MULTI_TOOL.setItemMeta(carbonado);
    }

    /* Food */
    public static final SlimefunItemStack FORTUNE_COOKIE = new SlimefunItemStack("FORTUNE_COOKIE", Material.COOKIE);
    public static final SlimefunItemStack DIET_COOKIE = new SlimefunItemStack("DIET_COOKIE", Material.COOKIE);
    public static final SlimefunItemStack MAGIC_SUGAR = new SlimefunItemStack("MAGIC_SUGAR", Material.SUGAR);
    public static final SlimefunItemStack MONSTER_JERKY = new SlimefunItemStack("MONSTER_JERKY", Material.ROTTEN_FLESH);
    public static final SlimefunItemStack APPLE_JUICE = new SlimefunItemStack("APPLE_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 5, 0));
    public static final SlimefunItemStack MELON_JUICE = new SlimefunItemStack("MELON_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 5, 0));
    public static final SlimefunItemStack CARROT_JUICE = new SlimefunItemStack("CARROT_JUICE", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 5, 0));
    public static final SlimefunItemStack PUMPKIN_JUICE = new SlimefunItemStack("PUMPKIN_JUICE", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 5, 0));
    public static final SlimefunItemStack SWEET_BERRY_JUICE = new SlimefunItemStack("SWEET_BERRY_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 5, 0));
    public static final SlimefunItemStack GLOW_BERRY_JUICE = new SlimefunItemStack("GLOW_BERRY_JUICE", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 5, 0));
    public static final SlimefunItemStack GOLDEN_APPLE_JUICE = new SlimefunItemStack("GOLDEN_APPLE_JUICE", Color.YELLOW, new PotionEffect(PotionEffectType.ABSORPTION, 20 * 20, 0));

    public static final SlimefunItemStack BEEF_JERKY = new SlimefunItemStack("BEEF_JERKY", Material.COOKED_BEEF);
    public static final SlimefunItemStack PORK_JERKY = new SlimefunItemStack("PORK_JERKY", XMaterial.COOKED_PORKCHOP.parseMaterial());
    public static final SlimefunItemStack CHICKEN_JERKY = new SlimefunItemStack("CHICKEN_JERKY", Material.COOKED_CHICKEN);
    public static final SlimefunItemStack MUTTON_JERKY = new SlimefunItemStack("MUTTON_JERKY", Material.COOKED_MUTTON);
    public static final SlimefunItemStack RABBIT_JERKY = new SlimefunItemStack("RABBIT_JERKY", Material.COOKED_RABBIT);
    public static final SlimefunItemStack FISH_JERKY = new SlimefunItemStack("FISH_JERKY", XMaterial.COOKED_COD.parseMaterial());

    public static final SlimefunItemStack KELP_COOKIE = new SlimefunItemStack("KELP_COOKIE", Material.COOKIE);

    /* Christmas */
    public static final SlimefunItemStack CHRISTMAS_MILK = new SlimefunItemStack("CHRISTMAS_MILK", Color.WHITE, new PotionEffect(PotionEffectType.SATURATION, 4, 0));
    public static final SlimefunItemStack CHRISTMAS_CHOCOLATE_MILK = new SlimefunItemStack("CHRISTMAS_CHOCOLATE_MILK", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 11, 0));
    public static final SlimefunItemStack CHRISTMAS_EGG_NOG = new SlimefunItemStack("CHRISTMAS_EGG_NOG", Color.GRAY, new PotionEffect(PotionEffectType.SATURATION, 6, 0));
    public static final SlimefunItemStack CHRISTMAS_APPLE_CIDER = new SlimefunItemStack("CHRISTMAS_APPLE_CIDER", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 13, 0));
    public static final SlimefunItemStack CHRISTMAS_COOKIE = new SlimefunItemStack("CHRISTMAS_COOKIE", Material.COOKIE);
    public static final SlimefunItemStack CHRISTMAS_FRUIT_CAKE = new SlimefunItemStack("CHRISTMAS_FRUIT_CAKE", Material.PUMPKIN_PIE);
    public static final SlimefunItemStack CHRISTMAS_APPLE_PIE = new SlimefunItemStack("CHRISTMAS_APPLE_PIE", Material.PUMPKIN_PIE);
    public static final SlimefunItemStack CHRISTMAS_HOT_CHOCOLATE = new SlimefunItemStack("CHRISTMAS_HOT_CHOCOLATE", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 13, 0));
    public static final SlimefunItemStack CHRISTMAS_CAKE = new SlimefunItemStack("CHRISTMAS_CAKE", Material.PUMPKIN_PIE);
    public static final SlimefunItemStack CHRISTMAS_CARAMEL = new SlimefunItemStack("CHRISTMAS_CARAMEL", Material.BRICK);
    public static final SlimefunItemStack CHRISTMAS_CARAMEL_APPLE = new SlimefunItemStack("CHRISTMAS_CARAMEL_APPLE", Material.APPLE);
    public static final SlimefunItemStack CHRISTMAS_CHOCOLATE_APPLE = new SlimefunItemStack("CHRISTMAS_CHOCOLATE_APPLE", Material.APPLE);
    public static final SlimefunItemStack CHRISTMAS_PRESENT = new SlimefunItemStack("CHRISTMAS_PRESENT", HeadTexture.CHRISTMAS_PRESENT);

    /* Easter */
    public static final SlimefunItemStack EASTER_EGG = new SlimefunItemStack("EASTER_EGG", HeadTexture.EASTER_EGG);
    public static final SlimefunItemStack EASTER_CARROT_PIE = new SlimefunItemStack("CARROT_PIE", Material.PUMPKIN_PIE);
    public static final SlimefunItemStack EASTER_APPLE_PIE = new SlimefunItemStack("EASTER_APPLE_PIE", Material.PUMPKIN_PIE);

    /* Weapons */
    public static final SlimefunItemStack GRANDMAS_WALKING_STICK = new SlimefunItemStack("GRANDMAS_WALKING_STICK", Material.STICK);
    public static final SlimefunItemStack GRANDPAS_WALKING_STICK = new SlimefunItemStack("GRANDPAS_WALKING_STICK", Material.STICK);
    public static final SlimefunItemStack SWORD_OF_BEHEADING = new SlimefunItemStack("SWORD_OF_BEHEADING", Material.IRON_SWORD);
    public static final SlimefunItemStack BLADE_OF_VAMPIRES = new SlimefunItemStack("BLADE_OF_VAMPIRES", XMaterial.GOLDEN_SWORD.parseMaterial());
    public static final SlimefunItemStack SEISMIC_AXE = new SlimefunItemStack("SEISMIC_AXE", Material.IRON_AXE);

    static {
        GRANDMAS_WALKING_STICK.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        GRANDPAS_WALKING_STICK.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        BLADE_OF_VAMPIRES.addUnsafeEnchantment(VersionedEnchantment.UNBREAKING, 4);
        BLADE_OF_VAMPIRES.addUnsafeEnchantment(VersionedEnchantment.SHARPNESS, 2);
    }

    /* Bows */
    public static final SlimefunItemStack EXPLOSIVE_BOW = new SlimefunItemStack("EXPLOSIVE_BOW", Material.BOW);
    public static final SlimefunItemStack ICY_BOW = new SlimefunItemStack("ICY_BOW", Material.BOW);

    /* Tools */
    public static final SlimefunItemStack SMELTERS_PICKAXE = new SlimefunItemStack("SMELTERS_PICKAXE", Material.DIAMOND_PICKAXE);
    public static final SlimefunItemStack LUMBER_AXE = new SlimefunItemStack("LUMBER_AXE", Material.DIAMOND_AXE);
    public static final SlimefunItemStack PICKAXE_OF_CONTAINMENT = new SlimefunItemStack("PICKAXE_OF_CONTAINMENT", Material.IRON_PICKAXE);
    public static final SlimefunItemStack EXPLOSIVE_PICKAXE = new SlimefunItemStack("EXPLOSIVE_PICKAXE", Material.DIAMOND_PICKAXE);
    public static final SlimefunItemStack EXPLOSIVE_SHOVEL = new SlimefunItemStack("EXPLOSIVE_SHOVEL", XMaterial.DIAMOND_SHOVEL.parseMaterial());
    public static final SlimefunItemStack PICKAXE_OF_THE_SEEKER = new SlimefunItemStack("PICKAXE_OF_THE_SEEKER", Material.DIAMOND_PICKAXE);
    public static final SlimefunItemStack COBALT_PICKAXE = new SlimefunItemStack("COBALT_PICKAXE", Material.IRON_PICKAXE);
    public static final SlimefunItemStack PICKAXE_OF_VEIN_MINING = new SlimefunItemStack("PICKAXE_OF_VEIN_MINING", Material.DIAMOND_PICKAXE);
    public static final SlimefunItemStack CLIMBING_PICK = new SlimefunItemStack("CLIMBING_PICK", Material.IRON_PICKAXE);

    static {
        COBALT_PICKAXE.addUnsafeEnchantment(VersionedEnchantment.UNBREAKING, 10);
        COBALT_PICKAXE.addUnsafeEnchantment(VersionedEnchantment.EFFICIENCY, 6);
    }

    /* Armor */
    public static final SlimefunItemStack GLOWSTONE_HELMET = new SlimefunItemStack("GLOWSTONE_HELMET", Material.LEATHER_HELMET, Color.YELLOW);
    public static final SlimefunItemStack GLOWSTONE_CHESTPLATE = new SlimefunItemStack("GLOWSTONE_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.YELLOW);
    public static final SlimefunItemStack GLOWSTONE_LEGGINGS = new SlimefunItemStack("GLOWSTONE_LEGGINGS", Material.LEATHER_LEGGINGS, Color.YELLOW);
    public static final SlimefunItemStack GLOWSTONE_BOOTS = new SlimefunItemStack("GLOWSTONE_BOOTS", Material.LEATHER_BOOTS, Color.YELLOW);
    public static final SlimefunItemStack RAINBOW_LEATHER = new SlimefunItemStack("RAINBOW_LEATHER", Material.RABBIT_HIDE, Color.FUCHSIA);
    public static final SlimefunItemStack RAINBOW_HELMET = new SlimefunItemStack("RAINBOW_HELMET", Material.LEATHER_HELMET, Color.FUCHSIA);
    public static final SlimefunItemStack RAINBOW_CHESTPLATE = new SlimefunItemStack("RAINBOW_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.FUCHSIA);
    public static final SlimefunItemStack RAINBOW_LEGGINGS = new SlimefunItemStack("RAINBOW_LEGGINGS", Material.LEATHER_LEGGINGS, Color.FUCHSIA);
    public static final SlimefunItemStack RAINBOW_BOOTS = new SlimefunItemStack("RAINBOW_BOOTS", Material.LEATHER_BOOTS, Color.FUCHSIA);
    public static final SlimefunItemStack ENDER_HELMET = new SlimefunItemStack("ENDER_HELMET", Material.LEATHER_HELMET, Color.fromRGB(28, 25, 112));
    public static final SlimefunItemStack ENDER_CHESTPLATE = new SlimefunItemStack("ENDER_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.fromRGB(28, 25, 112));
    public static final SlimefunItemStack ENDER_LEGGINGS = new SlimefunItemStack("ENDER_LEGGINGS", Material.LEATHER_LEGGINGS, Color.fromRGB(28, 25, 112));
    public static final SlimefunItemStack ENDER_BOOTS = new SlimefunItemStack("ENDER_BOOTS", Material.LEATHER_BOOTS, Color.fromRGB(28, 25, 112));

    public static final SlimefunItemStack SLIME_HELMET = new SlimefunItemStack("SLIME_HELMET", Material.LEATHER_HELMET, Color.LIME);
    public static final SlimefunItemStack SLIME_CHESTPLATE = new SlimefunItemStack("SLIME_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.LIME);
    public static final SlimefunItemStack SLIME_LEGGINGS = new SlimefunItemStack("SLIME_LEGGINGS", Material.LEATHER_LEGGINGS, Color.LIME);
    public static final SlimefunItemStack SLIME_BOOTS = new SlimefunItemStack("SLIME_BOOTS", Material.LEATHER_BOOTS, Color.LIME);

    public static final SlimefunItemStack CACTUS_HELMET = new SlimefunItemStack("CACTUS_HELMET", Material.LEATHER_HELMET, Color.GREEN);
    public static final SlimefunItemStack CACTUS_CHESTPLATE = new SlimefunItemStack("CACTUS_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.GREEN);
    public static final SlimefunItemStack CACTUS_LEGGINGS = new SlimefunItemStack("CACTUS_LEGGINGS", Material.LEATHER_LEGGINGS, Color.GREEN);
    public static final SlimefunItemStack CACTUS_BOOTS = new SlimefunItemStack("CACTUS_BOOTS", Material.LEATHER_BOOTS, Color.GREEN);

    public static final SlimefunItemStack DAMASCUS_STEEL_HELMET = new SlimefunItemStack("DAMASCUS_STEEL_HELMET", Material.IRON_HELMET);
    public static final SlimefunItemStack DAMASCUS_STEEL_CHESTPLATE = new SlimefunItemStack("DAMASCUS_STEEL_CHESTPLATE", Material.IRON_CHESTPLATE);
    public static final SlimefunItemStack DAMASCUS_STEEL_LEGGINGS = new SlimefunItemStack("DAMASCUS_STEEL_LEGGINGS", Material.IRON_LEGGINGS);
    public static final SlimefunItemStack DAMASCUS_STEEL_BOOTS = new SlimefunItemStack("DAMASCUS_STEEL_BOOTS", Material.IRON_BOOTS);

    public static final SlimefunItemStack REINFORCED_ALLOY_HELMET = new SlimefunItemStack("REINFORCED_ALLOY_HELMET", Material.IRON_HELMET);
    public static final SlimefunItemStack REINFORCED_ALLOY_CHESTPLATE = new SlimefunItemStack("REINFORCED_ALLOY_CHESTPLATE", Material.IRON_CHESTPLATE);
    public static final SlimefunItemStack REINFORCED_ALLOY_LEGGINGS = new SlimefunItemStack("REINFORCED_ALLOY_LEGGINGS", Material.IRON_LEGGINGS);
    public static final SlimefunItemStack REINFORCED_ALLOY_BOOTS = new SlimefunItemStack("REINFORCED_ALLOY_BOOTS", Material.IRON_BOOTS);

    private static final List<String> hazmatLore = new ArrayList<>();

    static {
        hazmatLore.add("");
        hazmatLore.add(ChatColor.GOLD + "Full set effects:");
        hazmatLore.add(ChatColor.YELLOW + "- Radiation immunity");
        hazmatLore.add(ChatColor.YELLOW + "- Bee Sting protection");
    }

    public static final SlimefunItemStack SCUBA_HELMET = new SlimefunItemStack("SCUBA_HELMET", Material.LEATHER_HELMET, Color.ORANGE);
    public static final SlimefunItemStack HAZMAT_CHESTPLATE = new SlimefunItemStack("HAZMAT_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.ORANGE);
    public static final SlimefunItemStack HAZMAT_LEGGINGS = new SlimefunItemStack("HAZMAT_LEGGINGS", Material.LEATHER_LEGGINGS, Color.ORANGE);
    public static final SlimefunItemStack HAZMAT_BOOTS = new SlimefunItemStack("RUBBER_BOOTS", Material.LEATHER_BOOTS, Color.BLACK);

    static {
        ItemMeta helmetMeta = SCUBA_HELMET.getItemMeta();
        List<String> helmetLore = helmetMeta.getLore();
        helmetLore.addAll(hazmatLore);
        helmetMeta.setLore(helmetLore);
        SCUBA_HELMET.setItemMeta(helmetMeta);

        ItemMeta chestplateMeta = HAZMAT_CHESTPLATE.getItemMeta();
        List<String> chestplateLore = chestplateMeta.getLore();
        chestplateLore.addAll(hazmatLore);
        chestplateMeta.setLore(chestplateLore);
        HAZMAT_CHESTPLATE.setItemMeta(chestplateMeta);
    }

    public static final SlimefunItemStack GILDED_IRON_HELMET = new SlimefunItemStack("GILDED_IRON_HELMET", XMaterial.GOLDEN_HELMET.parseMaterial());
    public static final SlimefunItemStack GILDED_IRON_CHESTPLATE = new SlimefunItemStack("GILDED_IRON_CHESTPLATE", XMaterial.GOLDEN_CHESTPLATE.parseMaterial());
    public static final SlimefunItemStack GILDED_IRON_LEGGINGS = new SlimefunItemStack("GILDED_IRON_LEGGINGS", XMaterial.GOLDEN_LEGGINGS.parseMaterial());
    public static final SlimefunItemStack GILDED_IRON_BOOTS = new SlimefunItemStack("GILDED_IRON_BOOTS", XMaterial.GOLDEN_BOOTS.parseMaterial());

    public static final SlimefunItemStack GOLDEN_HELMET_12K = new SlimefunItemStack("GOLD_12K_HELMET", XMaterial.GOLDEN_HELMET.parseMaterial());
    public static final SlimefunItemStack GOLDEN_CHESTPLATE_12K = new SlimefunItemStack("GOLD_12K_CHESTPLATE", XMaterial.GOLDEN_CHESTPLATE.parseMaterial());
    public static final SlimefunItemStack GOLDEN_LEGGINGS_12K = new SlimefunItemStack("GOLD_12K_LEGGINGS", XMaterial.GOLDEN_LEGGINGS.parseMaterial());
    public static final SlimefunItemStack GOLDEN_BOOTS_12K = new SlimefunItemStack("GOLD_12K_BOOTS", XMaterial.GOLDEN_BOOTS.parseMaterial());

    public static final SlimefunItemStack SLIME_HELMET_STEEL = new SlimefunItemStack("SLIME_STEEL_HELMET", Material.IRON_HELMET);
    public static final SlimefunItemStack SLIME_CHESTPLATE_STEEL = new SlimefunItemStack("SLIME_STEEL_CHESTPLATE", Material.IRON_CHESTPLATE);
    public static final SlimefunItemStack SLIME_LEGGINGS_STEEL = new SlimefunItemStack("SLIME_STEEL_LEGGINGS", Material.IRON_LEGGINGS);
    public static final SlimefunItemStack SLIME_BOOTS_STEEL = new SlimefunItemStack("SLIME_STEEL_BOOTS", Material.IRON_BOOTS);

    public static final SlimefunItemStack BOOTS_OF_THE_STOMPER = new SlimefunItemStack("BOOTS_OF_THE_STOMPER", Material.LEATHER_BOOTS, Color.AQUA);

    public static final SlimefunItemStack BEE_HELMET = new SlimefunItemStack("BEE_HELMET", XMaterial.GOLDEN_HELMET.parseMaterial());
    public static final SlimefunItemStack BEE_WINGS = new SlimefunItemStack("BEE_WINGS", XMaterial.ELYTRA.parseMaterial());
    public static final SlimefunItemStack BEE_LEGGINGS = new SlimefunItemStack("BEE_LEGGINGS", XMaterial.GOLDEN_LEGGINGS.parseMaterial());
    public static final SlimefunItemStack BEE_BOOTS = new SlimefunItemStack("BEE_BOOTS", XMaterial.GOLDEN_BOOTS.parseMaterial());

    static {
        Map<Enchantment, Integer> cactusEnchs = new HashMap<>();
        cactusEnchs.put(Enchantment.THORNS, 3);
        cactusEnchs.put(VersionedEnchantment.UNBREAKING, 6);

        CACTUS_HELMET.addUnsafeEnchantments(cactusEnchs);
        CACTUS_CHESTPLATE.addUnsafeEnchantments(cactusEnchs);
        CACTUS_LEGGINGS.addUnsafeEnchantments(cactusEnchs);
        CACTUS_BOOTS.addUnsafeEnchantments(cactusEnchs);

        Map<Enchantment, Integer> damascusEnchs = new HashMap<>();
        damascusEnchs.put(VersionedEnchantment.UNBREAKING, 5);
        damascusEnchs.put(VersionedEnchantment.PROTECTION, 5);

        DAMASCUS_STEEL_HELMET.addUnsafeEnchantments(damascusEnchs);
        DAMASCUS_STEEL_CHESTPLATE.addUnsafeEnchantments(damascusEnchs);
        DAMASCUS_STEEL_LEGGINGS.addUnsafeEnchantments(damascusEnchs);
        DAMASCUS_STEEL_BOOTS.addUnsafeEnchantments(damascusEnchs);

        Map<Enchantment, Integer> reinforcedEnchs = new HashMap<>();
        reinforcedEnchs.put(VersionedEnchantment.UNBREAKING, 9);
        reinforcedEnchs.put(VersionedEnchantment.PROTECTION, 9);

        REINFORCED_ALLOY_HELMET.addUnsafeEnchantments(reinforcedEnchs);
        REINFORCED_ALLOY_CHESTPLATE.addUnsafeEnchantments(reinforcedEnchs);
        REINFORCED_ALLOY_LEGGINGS.addUnsafeEnchantments(reinforcedEnchs);
        REINFORCED_ALLOY_BOOTS.addUnsafeEnchantments(reinforcedEnchs);

        Map<Enchantment, Integer> gildedEnchs = new HashMap<>();
        gildedEnchs.put(VersionedEnchantment.UNBREAKING, 6);
        gildedEnchs.put(VersionedEnchantment.PROTECTION, 8);

        GILDED_IRON_HELMET.addUnsafeEnchantments(gildedEnchs);
        GILDED_IRON_CHESTPLATE.addUnsafeEnchantments(gildedEnchs);
        GILDED_IRON_LEGGINGS.addUnsafeEnchantments(gildedEnchs);
        GILDED_IRON_BOOTS.addUnsafeEnchantments(gildedEnchs);

        GOLDEN_HELMET_12K.addUnsafeEnchantment(VersionedEnchantment.UNBREAKING, 10);
        GOLDEN_CHESTPLATE_12K.addUnsafeEnchantment(VersionedEnchantment.UNBREAKING, 10);
        GOLDEN_LEGGINGS_12K.addUnsafeEnchantment(VersionedEnchantment.UNBREAKING, 10);
        GOLDEN_BOOTS_12K.addUnsafeEnchantment(VersionedEnchantment.UNBREAKING, 10);

        Map<Enchantment, Integer> slimeEnchs = new HashMap<>();
        slimeEnchs.put(VersionedEnchantment.UNBREAKING, 4);
        slimeEnchs.put(VersionedEnchantment.PROTECTION, 2);

        SLIME_HELMET_STEEL.addUnsafeEnchantments(slimeEnchs);
        SLIME_CHESTPLATE_STEEL.addUnsafeEnchantments(slimeEnchs);
        SLIME_LEGGINGS_STEEL.addUnsafeEnchantments(slimeEnchs);
        SLIME_BOOTS_STEEL.addUnsafeEnchantments(slimeEnchs);

        Map<Enchantment, Integer> beeEnchs = new HashMap<>();
        beeEnchs.put(VersionedEnchantment.UNBREAKING, 4);
        beeEnchs.put(VersionedEnchantment.PROTECTION, 2);

        BEE_HELMET.addUnsafeEnchantments(beeEnchs);
        BEE_WINGS.addUnsafeEnchantments(beeEnchs);
        BEE_LEGGINGS.addUnsafeEnchantments(beeEnchs);
        BEE_BOOTS.addUnsafeEnchantments(beeEnchs);
    }

    /* Magical components */
    public static final SlimefunItemStack MAGIC_LUMP_1 = new SlimefunItemStack("MAGIC_LUMP_1", Material.GOLD_NUGGET);
    public static final SlimefunItemStack MAGIC_LUMP_2 = new SlimefunItemStack("MAGIC_LUMP_2", Material.GOLD_NUGGET);
    public static final SlimefunItemStack MAGIC_LUMP_3 = new SlimefunItemStack("MAGIC_LUMP_3", Material.GOLD_NUGGET);
    public static final SlimefunItemStack ENDER_LUMP_1 = new SlimefunItemStack("ENDER_LUMP_1", Material.GOLD_NUGGET);
    public static final SlimefunItemStack ENDER_LUMP_2 = new SlimefunItemStack("ENDER_LUMP_2", Material.GOLD_NUGGET);
    public static final SlimefunItemStack ENDER_LUMP_3 = new SlimefunItemStack("ENDER_LUMP_3", Material.GOLD_NUGGET);
    public static final SlimefunItemStack MAGICAL_BOOK_COVER = new SlimefunItemStack("MAGICAL_BOOK_COVER", Material.PAPER);
    public static final SlimefunItemStack MAGICAL_GLASS = new SlimefunItemStack("MAGICAL_GLASS", XMaterial.GLASS_PANE.parseMaterial());
    public static final SlimefunItemStack SYNTHETIC_SHULKER_SHELL = new SlimefunItemStack("SYNTHETIC_SHULKER_SHELL", XMaterial.SHULKER_SHELL.parseMaterial());

    /* Technical components */
    public static final SlimefunItemStack BASIC_CIRCUIT_BOARD = new SlimefunItemStack("BASIC_CIRCUIT_BOARD", Material.ACTIVATOR_RAIL);
    public static final SlimefunItemStack ADVANCED_CIRCUIT_BOARD = new SlimefunItemStack("ADVANCED_CIRCUIT_BOARD", Material.POWERED_RAIL);
    public static final SlimefunItemStack WHEAT_FLOUR = new SlimefunItemStack("WHEAT_FLOUR", Material.SUGAR);
    public static final SlimefunItemStack STEEL_PLATE = new SlimefunItemStack("STEEL_PLATE", Material.PAPER);
    public static final SlimefunItemStack BATTERY = new SlimefunItemStack("BATTERY", HeadTexture.BATTERY);
    public static final SlimefunItemStack CARBON = new SlimefunItemStack("CARBON", HeadTexture.CARBON);
    public static final SlimefunItemStack COMPRESSED_CARBON = new SlimefunItemStack("COMPRESSED_CARBON", HeadTexture.COMPRESSED_CARBON);
    public static final SlimefunItemStack CARBON_CHUNK = new SlimefunItemStack("CARBON_CHUNK", HeadTexture.COMPRESSED_CARBON);
    public static final SlimefunItemStack STEEL_THRUSTER = new SlimefunItemStack("STEEL_THRUSTER", Material.BUCKET);
    public static final SlimefunItemStack POWER_CRYSTAL = new SlimefunItemStack("POWER_CRYSTAL", HeadTexture.POWER_CRYSTAL);
    public static final SlimefunItemStack CHAIN = new SlimefunItemStack("CHAIN", Material.STRING);
    public static final SlimefunItemStack HOOK = new SlimefunItemStack("HOOK", Material.FLINT);
    public static final SlimefunItemStack SIFTED_ORE = new SlimefunItemStack("SIFTED_ORE", XMaterial.GUNPOWDER.parseMaterial());
    public static final SlimefunItemStack STONE_CHUNK = new SlimefunItemStack("STONE_CHUNK", HeadTexture.STONE_CHUNK);
    public static final SlimefunItemStack LAVA_CRYSTAL = new SlimefunItemStack("LAVA_CRYSTAL", HeadTexture.LAVA_CRYSTAL);
    public static final SlimefunItemStack SALT = new SlimefunItemStack("SALT", Material.SUGAR);
    public static final SlimefunItemStack CHEESE = new SlimefunItemStack("CHEESE", HeadTexture.CHEESE);
    public static final SlimefunItemStack BUTTER = new SlimefunItemStack("BUTTER", HeadTexture.BUTTER);
    public static final SlimefunItemStack DUCT_TAPE = new SlimefunItemStack("DUCT_TAPE", HeadTexture.DUCT_TAPE);
    public static final SlimefunItemStack HEAVY_CREAM = new SlimefunItemStack("HEAVY_CREAM", XMaterial.SNOWBALL.parseMaterial());
    public static final SlimefunItemStack CRUSHED_ORE = new SlimefunItemStack("CRUSHED_ORE", XMaterial.GUNPOWDER.parseMaterial());
    public static final SlimefunItemStack PULVERIZED_ORE = new SlimefunItemStack("PULVERIZED_ORE", XMaterial.GUNPOWDER.parseMaterial());
    public static final SlimefunItemStack PURE_ORE_CLUSTER = new SlimefunItemStack("PURE_ORE_CLUSTER", XMaterial.GUNPOWDER.parseMaterial());
    public static final SlimefunItemStack SMALL_URANIUM = new SlimefunItemStack("SMALL_URANIUM", HeadTexture.URANIUM);
    public static final SlimefunItemStack TINY_URANIUM = new SlimefunItemStack("TINY_URANIUM", HeadTexture.URANIUM);
    public static final SlimefunItemStack SOLAR_PANEL = new SlimefunItemStack("SOLAR_PANEL", Material.DAYLIGHT_DETECTOR);
    public static final SlimefunItemStack PLASTIC_SHEET = new SlimefunItemStack("PLASTIC_SHEET", Material.PAPER);

    public static final SlimefunItemStack MAGNET = new SlimefunItemStack("MAGNET", HeadTexture.MAGNET);
    public static final SlimefunItemStack NECROTIC_SKULL = new SlimefunItemStack("NECROTIC_SKULL", HeadTexture.NECROTIC_SKULL);
    public static final SlimefunItemStack ESSENCE_OF_AFTERLIFE = new SlimefunItemStack("ESSENCE_OF_AFTERLIFE", XMaterial.GUNPOWDER.parseMaterial());
    public static final SlimefunItemStack STRANGE_NETHER_GOO = new SlimefunItemStack("STRANGE_NETHER_GOO", XMaterial.PURPLE_DYE.parseMaterial());
    public static final SlimefunItemStack ELECTRO_MAGNET = new SlimefunItemStack("ELECTRO_MAGNET", HeadTexture.MAGNET);
    public static final SlimefunItemStack HEATING_COIL = new SlimefunItemStack("HEATING_COIL", HeadTexture.HEATING_COIL);
    public static final SlimefunItemStack COOLING_UNIT = new SlimefunItemStack("COOLING_UNIT", HeadTexture.COOLING_UNIT);
    public static final SlimefunItemStack ELECTRIC_MOTOR = new SlimefunItemStack("ELECTRIC_MOTOR", HeadTexture.MOTOR);
    public static final SlimefunItemStack SLOT_LOCK = new SlimefunItemStack("SLOT_LOCK", XMaterial.IRON_BARS.parseMaterial());
    public static final SlimefunItemStack CARGO_MOTOR = new SlimefunItemStack("CARGO_MOTOR", HeadTexture.CARGO_MOTOR);
    public static final SlimefunItemStack SCROLL_OF_DIMENSIONAL_TELEPOSITION = new SlimefunItemStack("SCROLL_OF_DIMENSIONAL_TELEPOSITION", Material.PAPER);
    public static final SlimefunItemStack TOME_OF_KNOWLEDGE_SHARING = new SlimefunItemStack("TOME_OF_KNOWLEDGE_SHARING", Material.ENCHANTED_BOOK, "&6Tome of Knowledge Sharing", "&7Owner: &bNone", "", "&eRight Click&7 to bind this Tome to yourself", "", "", "&eRight Click&7 to obtain all Researches by", "&7the previously assigned Owner");
    public static final SlimefunItemStack HARDENED_GLASS = new SlimefunItemStack("HARDENED_GLASS", XMaterial.LIGHT_GRAY_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack WITHER_PROOF_OBSIDIAN = new SlimefunItemStack("WITHER_PROOF_OBSIDIAN", Material.OBSIDIAN);
    public static final SlimefunItemStack WITHER_PROOF_GLASS = new SlimefunItemStack("WITHER_PROOF_GLASS", XMaterial.PURPLE_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack REINFORCED_PLATE = new SlimefunItemStack("REINFORCED_PLATE", Material.PAPER);
    public static final SlimefunItemStack ANCIENT_PEDESTAL = new SlimefunItemStack("ANCIENT_PEDESTAL", Material.DISPENSER);
    public static final SlimefunItemStack ANCIENT_ALTAR = new SlimefunItemStack("ANCIENT_ALTAR", XMaterial.ENCHANTING_TABLE.parseMaterial());
    public static final SlimefunItemStack COPPER_WIRE = new SlimefunItemStack("COPPER_WIRE", Material.STRING);
    public static final SlimefunItemStack CRAFTING_MOTOR = new SlimefunItemStack("CRAFTING_MOTOR", HeadTexture.CRAFTING_MOTOR);

    /* Rainbow blocks */
    public static final SlimefunItemStack RAINBOW_WOOL = new SlimefunItemStack("RAINBOW_WOOL", XMaterial.WHITE_WOOL.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLASS = new SlimefunItemStack("RAINBOW_GLASS", XMaterial.WHITE_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack RAINBOW_CLAY = new SlimefunItemStack("RAINBOW_CLAY", XMaterial.WHITE_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLASS_PANE = new SlimefunItemStack("RAINBOW_GLASS_PANE", XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial());
    public static final SlimefunItemStack RAINBOW_CONCRETE = new SlimefunItemStack("RAINBOW_CONCRETE", XMaterial.WHITE_CONCRETE.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA", XMaterial.WHITE_GLAZED_TERRACOTTA.parseMaterial());
    /* Seasonal */
    private static final String CHRISTMAS = ChatUtils.christmas("[Christmas Edition]");
    public static final SlimefunItemStack RAINBOW_WOOL_XMAS = new SlimefunItemStack("RAINBOW_WOOL_XMAS", XMaterial.WHITE_WOOL.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLASS_XMAS = new SlimefunItemStack("RAINBOW_GLASS_XMAS", XMaterial.WHITE_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack RAINBOW_CLAY_XMAS = new SlimefunItemStack("RAINBOW_CLAY_XMAS", XMaterial.WHITE_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLASS_PANE_XMAS = new SlimefunItemStack("RAINBOW_GLASS_PANE_XMAS", XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial());
    public static final SlimefunItemStack RAINBOW_CONCRETE_XMAS = new SlimefunItemStack("RAINBOW_CONCRETE_XMAS", XMaterial.WHITE_CONCRETE.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA_XMAS = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_XMAS", XMaterial.WHITE_GLAZED_TERRACOTTA.parseMaterial());

    private static final String VALENTINES_DAY = "&5[&dValentine's Day Edition&5]";
    public static final SlimefunItemStack RAINBOW_WOOL_VALENTINE = new SlimefunItemStack("RAINBOW_WOOL_VALENTINE", XMaterial.PINK_WOOL.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLASS_VALENTINE = new SlimefunItemStack("RAINBOW_GLASS_VALENTINE", XMaterial.PINK_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack RAINBOW_CLAY_VALENTINE = new SlimefunItemStack("RAINBOW_CLAY_VALENTINE", XMaterial.PINK_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLASS_PANE_VALENTINE = new SlimefunItemStack("RAINBOW_GLASS_PANE_VALENTINE", XMaterial.PINK_STAINED_GLASS_PANE.parseMaterial());
    public static final SlimefunItemStack RAINBOW_CONCRETE_VALENTINE = new SlimefunItemStack("RAINBOW_CONCRETE_VALENTINE", XMaterial.PINK_CONCRETE.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA_VALENTINE = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_VALENTINE", XMaterial.PINK_GLAZED_TERRACOTTA.parseMaterial());

    private static final String HALLOWEEN = "&c[&6Halloween Edition&c]";
    public static final SlimefunItemStack RAINBOW_WOOL_HALLOWEEN = new SlimefunItemStack("RAINBOW_WOOL_HALLOWEEN", XMaterial.ORANGE_WOOL.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLASS_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLASS_HALLOWEEN", XMaterial.ORANGE_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack RAINBOW_CLAY_HALLOWEEN = new SlimefunItemStack("RAINBOW_CLAY_HALLOWEEN", XMaterial.ORANGE_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLASS_PANE_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLASS_PANE_HALLOWEEN", XMaterial.ORANGE_STAINED_GLASS_PANE.parseMaterial());
    public static final SlimefunItemStack RAINBOW_CONCRETE_HALLOWEEN = new SlimefunItemStack("RAINBOW_CONCRETE_HALLOWEEN", XMaterial.ORANGE_CONCRETE.parseMaterial());
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN", XMaterial.ORANGE_GLAZED_TERRACOTTA.parseMaterial());

    /* Ingots */
    public static final SlimefunItemStack COPPER_INGOT = new SlimefunItemStack("COPPER_INGOT", Material.BRICK);
    public static final SlimefunItemStack TIN_INGOT = new SlimefunItemStack("TIN_INGOT", Material.IRON_INGOT);
    public static final SlimefunItemStack SILVER_INGOT = new SlimefunItemStack("SILVER_INGOT", Material.IRON_INGOT);
    public static final SlimefunItemStack ALUMINUM_INGOT = new SlimefunItemStack("ALUMINUM_INGOT", Material.IRON_INGOT);
    public static final SlimefunItemStack LEAD_INGOT = new SlimefunItemStack("LEAD_INGOT", Material.IRON_INGOT);
    public static final SlimefunItemStack ZINC_INGOT = new SlimefunItemStack("ZINC_INGOT", Material.IRON_INGOT);
    public static final SlimefunItemStack MAGNESIUM_INGOT = new SlimefunItemStack("MAGNESIUM_INGOT", Material.IRON_INGOT);

    /* Alloy (Carbon + Iron) */
    public static final SlimefunItemStack STEEL_INGOT = new SlimefunItemStack("STEEL_INGOT", Material.IRON_INGOT);
    /* Alloy (Copper + Tin) */
    public static final SlimefunItemStack BRONZE_INGOT = new SlimefunItemStack("BRONZE_INGOT", Material.BRICK);
    /* Alloy (Copper + Aluminum) */
    public static final SlimefunItemStack DURALUMIN_INGOT = new SlimefunItemStack("DURALUMIN_INGOT", Material.IRON_INGOT);
    /* Alloy (Copper + Silver) */
    public static final SlimefunItemStack BILLON_INGOT = new SlimefunItemStack("BILLON_INGOT", Material.IRON_INGOT);
    /* Alloy (Copper + Zinc) */
    public static final SlimefunItemStack BRASS_INGOT = new SlimefunItemStack("BRASS_INGOT", Material.GOLD_INGOT);
    /* Alloy (Aluminum + Brass) */
    public static final SlimefunItemStack ALUMINUM_BRASS_INGOT = new SlimefunItemStack("ALUMINUM_BRASS_INGOT", Material.GOLD_INGOT);
    /* Alloy (Aluminum + Bronze) */
    public static final SlimefunItemStack ALUMINUM_BRONZE_INGOT = new SlimefunItemStack("ALUMINUM_BRONZE_INGOT", Material.GOLD_INGOT);
    /* Alloy (Gold + Silver + Copper) */
    public static final SlimefunItemStack CORINTHIAN_BRONZE_INGOT = new SlimefunItemStack("CORINTHIAN_BRONZE_INGOT", Material.GOLD_INGOT);
    /* Alloy (Lead + Tin) */
    public static final SlimefunItemStack SOLDER_INGOT = new SlimefunItemStack("SOLDER_INGOT", Material.IRON_INGOT);
    /* Alloy (Steel + Iron + Carbon) */
    public static final SlimefunItemStack DAMASCUS_STEEL_INGOT = new SlimefunItemStack("DAMASCUS_STEEL_INGOT", Material.IRON_INGOT);
    /* Alloy (Damascus Steel + Duralumin + Compressed Carbon + Aluminium Bronze) */
    public static final SlimefunItemStack HARDENED_METAL_INGOT = new SlimefunItemStack("HARDENED_METAL_INGOT", Material.IRON_INGOT);
    /* Alloy (Hardened Metal + Corinthian Bronze + Solder + Billon + Damascus Steel) */
    public static final SlimefunItemStack REINFORCED_ALLOY_INGOT = new SlimefunItemStack("REINFORCED_ALLOY_INGOT", Material.IRON_INGOT);
    /* Alloy (Iron + Silicon) */
    public static final SlimefunItemStack FERROSILICON = new SlimefunItemStack("FERROSILICON", Material.IRON_INGOT);
    /* Alloy (Iron + Gold) */
    public static final SlimefunItemStack GILDED_IRON = new SlimefunItemStack("GILDED_IRON", Material.GOLD_INGOT);
    /* Alloy (Redstone + Ferrosilicon) */
    public static final SlimefunItemStack REDSTONE_ALLOY = new SlimefunItemStack("REDSTONE_ALLOY", Material.BRICK);
    /* Alloy (Iron + Copper) */
    public static final SlimefunItemStack NICKEL_INGOT = new SlimefunItemStack("NICKEL_INGOT", Material.IRON_INGOT);
    /* Alloy (Nickel + Iron + Copper) */
    public static final SlimefunItemStack COBALT_INGOT = new SlimefunItemStack("COBALT_INGOT", Material.IRON_INGOT);

    /* Gold */
    public static final SlimefunItemStack GOLD_4K = new SlimefunItemStack("GOLD_4K", Material.GOLD_INGOT);
    public static final SlimefunItemStack GOLD_6K = new SlimefunItemStack("GOLD_6K", Material.GOLD_INGOT);
    public static final SlimefunItemStack GOLD_8K = new SlimefunItemStack("GOLD_8K", Material.GOLD_INGOT);
    public static final SlimefunItemStack GOLD_10K = new SlimefunItemStack("GOLD_10K", Material.GOLD_INGOT);
    public static final SlimefunItemStack GOLD_12K = new SlimefunItemStack("GOLD_12K", Material.GOLD_INGOT);
    public static final SlimefunItemStack GOLD_14K = new SlimefunItemStack("GOLD_14K", Material.GOLD_INGOT);
    public static final SlimefunItemStack GOLD_16K = new SlimefunItemStack("GOLD_16K", Material.GOLD_INGOT);
    public static final SlimefunItemStack GOLD_18K = new SlimefunItemStack("GOLD_18K", Material.GOLD_INGOT);
    public static final SlimefunItemStack GOLD_20K = new SlimefunItemStack("GOLD_20K", Material.GOLD_INGOT);
    public static final SlimefunItemStack GOLD_22K = new SlimefunItemStack("GOLD_22K", Material.GOLD_INGOT);
    public static final SlimefunItemStack GOLD_24K = new SlimefunItemStack("GOLD_24K", Material.GOLD_INGOT);

    /* Dusts */
    public static final SlimefunItemStack IRON_DUST = new SlimefunItemStack("IRON_DUST", XMaterial.GUNPOWDER.parseMaterial());
    public static final SlimefunItemStack GOLD_DUST = new SlimefunItemStack("GOLD_DUST", Material.GLOWSTONE_DUST);
    public static final SlimefunItemStack TIN_DUST = new SlimefunItemStack("TIN_DUST", Material.SUGAR);
    public static final SlimefunItemStack COPPER_DUST = new SlimefunItemStack("COPPER_DUST", Material.GLOWSTONE_DUST);
    public static final SlimefunItemStack SILVER_DUST = new SlimefunItemStack("SILVER_DUST", Material.SUGAR);
    public static final SlimefunItemStack ALUMINUM_DUST = new SlimefunItemStack("ALUMINUM_DUST", Material.SUGAR);
    public static final SlimefunItemStack LEAD_DUST = new SlimefunItemStack("LEAD_DUST", XMaterial.GUNPOWDER.parseMaterial());
    public static final SlimefunItemStack ZINC_DUST = new SlimefunItemStack("ZINC_DUST", Material.SUGAR);
    public static final SlimefunItemStack MAGNESIUM_DUST = new SlimefunItemStack("MAGNESIUM_DUST", Material.SUGAR);

    public static final SlimefunItemStack SULFATE = new SlimefunItemStack("SULFATE", Material.GLOWSTONE_DUST);
    public static final SlimefunItemStack SILICON = new SlimefunItemStack("SILICON", XMaterial.FIREWORK_STAR.parseMaterial());
    public static final SlimefunItemStack GOLD_24K_BLOCK = new SlimefunItemStack("GOLD_24K_BLOCK", Material.GOLD_BLOCK);

    /* Gems */
    public static final SlimefunItemStack SYNTHETIC_DIAMOND = new SlimefunItemStack("SYNTHETIC_DIAMOND", Material.DIAMOND);
    public static final SlimefunItemStack SYNTHETIC_EMERALD = new SlimefunItemStack("SYNTHETIC_EMERALD", Material.EMERALD);
    public static final SlimefunItemStack SYNTHETIC_SAPPHIRE = new SlimefunItemStack("SYNTHETIC_SAPPHIRE", HeadTexture.SAPPHIRE);
    public static final SlimefunItemStack CARBONADO = new SlimefunItemStack("CARBONADO", HeadTexture.CARBONADO);
    public static final SlimefunItemStack RAW_CARBONADO = new SlimefunItemStack("RAW_CARBONADO", HeadTexture.RAW_CARBONADO);

    public static final SlimefunItemStack URANIUM = new SlimefunItemStack("URANIUM", HeadTexture.URANIUM);
    public static final SlimefunItemStack NEPTUNIUM = new SlimefunItemStack("NEPTUNIUM", HeadTexture.NEPTUNIUM);
    public static final SlimefunItemStack PLUTONIUM = new SlimefunItemStack("PLUTONIUM", HeadTexture.PLUTONIUM);
    public static final SlimefunItemStack BOOSTED_URANIUM = new SlimefunItemStack("BOOSTED_URANIUM", HeadTexture.BOOSTED_URANIUM);

    /* Talisman */
    public static final SlimefunItemStack COMMON_TALISMAN = new SlimefunItemStack("COMMON_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack ENDER_TALISMAN = new SlimefunItemStack("ENDER_TALISMAN", Material.EMERALD);

    public static final SlimefunItemStack TALISMAN_ANVIL = new SlimefunItemStack("ANVIL_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_MINER = new SlimefunItemStack("MINER_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_FARMER = new SlimefunItemStack("FARMER_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_HUNTER = new SlimefunItemStack("HUNTER_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_LAVA = new SlimefunItemStack("LAVA_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_WATER = new SlimefunItemStack("WATER_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_ANGEL = new SlimefunItemStack("ANGEL_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_FIRE = new SlimefunItemStack("FIRE_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_MAGICIAN = new SlimefunItemStack("MAGICIAN_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_TRAVELLER = new SlimefunItemStack("TRAVELLER_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_WARRIOR = new SlimefunItemStack("WARRIOR_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_KNIGHT = new SlimefunItemStack("KNIGHT_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_WHIRLWIND = new SlimefunItemStack("WHIRLWIND_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_WIZARD = new SlimefunItemStack("WIZARD_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_CAVEMAN = new SlimefunItemStack("CAVEMAN_TALISMAN", Material.EMERALD);
    public static final SlimefunItemStack TALISMAN_WISE = new SlimefunItemStack("WISE_TALISMAN", Material.EMERALD);

    /* Staves */
    public static final SlimefunItemStack STAFF_ELEMENTAL = new SlimefunItemStack("STAFF_ELEMENTAL", Material.STICK);
    public static final SlimefunItemStack STAFF_WIND = new SlimefunItemStack("STAFF_ELEMENTAL_WIND", Material.STICK);
    public static final SlimefunItemStack STAFF_FIRE = new SlimefunItemStack("STAFF_ELEMENTAL_FIRE", Material.STICK);
    public static final SlimefunItemStack STAFF_WATER = new SlimefunItemStack("STAFF_ELEMENTAL_WATER", Material.STICK);
    public static final SlimefunItemStack STAFF_STORM = new SlimefunItemStack("STAFF_ELEMENTAL_STORM", Material.STICK);

    static {
        STAFF_WIND.addUnsafeEnchantment(VersionedEnchantment.LUCK_OF_THE_SEA, 1);
        STAFF_FIRE.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 5);
        STAFF_WATER.addUnsafeEnchantment(VersionedEnchantment.AQUA_AFFINITY, 1);
        STAFF_STORM.addUnsafeEnchantment(VersionedEnchantment.UNBREAKING, 1);
    }

    /* Multiblocks */
    public static final SlimefunItemStack ENHANCED_CRAFTING_TABLE = new SlimefunItemStack("ENHANCED_CRAFTING_TABLE", XMaterial.CRAFTING_TABLE.parseMaterial());
    public static final SlimefunItemStack GRIND_STONE = new SlimefunItemStack("GRIND_STONE", Material.DISPENSER);
    public static final SlimefunItemStack ARMOR_FORGE = new SlimefunItemStack("ARMOR_FORGE", Material.ANVIL);
    public static final SlimefunItemStack MAKESHIFT_SMELTERY = new SlimefunItemStack("MAKESHIFT_SMELTERY", XMaterial.BLAST_FURNACE.parseMaterial());
    public static final SlimefunItemStack SMELTERY = new SlimefunItemStack("SMELTERY", Material.FURNACE);
    public static final SlimefunItemStack ORE_CRUSHER = new SlimefunItemStack("ORE_CRUSHER", Material.DISPENSER);
    public static final SlimefunItemStack COMPRESSOR = new SlimefunItemStack("COMPRESSOR", XMaterial.PISTON.parseMaterial());
    public static final SlimefunItemStack PRESSURE_CHAMBER = new SlimefunItemStack("PRESSURE_CHAMBER", Material.GLASS);
    public static final SlimefunItemStack MAGIC_WORKBENCH = new SlimefunItemStack("MAGIC_WORKBENCH", XMaterial.CRAFTING_TABLE.parseMaterial());
    public static final SlimefunItemStack ORE_WASHER = new SlimefunItemStack("ORE_WASHER", Material.CAULDRON);
    public static final SlimefunItemStack TABLE_SAW = new SlimefunItemStack("TABLE_SAW", XMaterial.STONECUTTER.parseMaterial());
    public static final SlimefunItemStack JUICER = new SlimefunItemStack("JUICER", Material.GLASS_BOTTLE);
    public static final SlimefunItemStack AUTOMATED_PANNING_MACHINE = new SlimefunItemStack("AUTOMATED_PANNING_MACHINE", Material.BOWL);

    public static final SlimefunItemStack INDUSTRIAL_MINER = new SlimefunItemStack("INDUSTRIAL_MINER", XMaterial.GOLDEN_PICKAXE.parseMaterial());
    public static final SlimefunItemStack ADVANCED_INDUSTRIAL_MINER = new SlimefunItemStack("ADVANCED_INDUSTRIAL_MINER", Material.DIAMOND_PICKAXE);

    static {
        ItemMeta meta = INDUSTRIAL_MINER.getItemMeta();
        VersionedItemFlag.addFlags(meta, VersionedItemFlag.HIDE_ATTRIBUTES);
        INDUSTRIAL_MINER.setItemMeta(meta);

        ItemMeta meta2 = ADVANCED_INDUSTRIAL_MINER.getItemMeta();
        VersionedItemFlag.addFlags(meta2, VersionedItemFlag.HIDE_ATTRIBUTES);
        ADVANCED_INDUSTRIAL_MINER.setItemMeta(meta2);
    }

    /* Machines */
    public static final SlimefunItemStack COMPOSTER = new SlimefunItemStack("COMPOSTER", Material.CAULDRON);
    public static final SlimefunItemStack CRUCIBLE = new SlimefunItemStack("CRUCIBLE", Material.CAULDRON);
    public static final SlimefunItemStack OUTPUT_CHEST = new SlimefunItemStack("OUTPUT_CHEST", Material.CHEST);
    public static final SlimefunItemStack IGNITION_CHAMBER = new SlimefunItemStack("IGNITION_CHAMBER", Material.DROPPER);
    public static final SlimefunItemStack HOLOGRAM_PROJECTOR = new SlimefunItemStack("HOLOGRAM_PROJECTOR", XMaterial.QUARTZ_SLAB.parseMaterial());
    public static final SlimefunItemStack BLOCK_PLACER = new SlimefunItemStack("BLOCK_PLACER", Material.DISPENSER);

    /* Enhanced Furnaces */
    public static final SlimefunItemStack ENHANCED_FURNACE = new SlimefunItemStack("ENHANCED_FURNACE", Material.FURNACE);
    public static final SlimefunItemStack ENHANCED_FURNACE_2 = new SlimefunItemStack("ENHANCED_FURNACE_2", Material.FURNACE);
    public static final SlimefunItemStack ENHANCED_FURNACE_3 = new SlimefunItemStack("ENHANCED_FURNACE_3", Material.FURNACE);
    public static final SlimefunItemStack ENHANCED_FURNACE_4 = new SlimefunItemStack("ENHANCED_FURNACE_4", Material.FURNACE);
    public static final SlimefunItemStack ENHANCED_FURNACE_5 = new SlimefunItemStack("ENHANCED_FURNACE_5", Material.FURNACE);
    public static final SlimefunItemStack ENHANCED_FURNACE_6 = new SlimefunItemStack("ENHANCED_FURNACE_6", Material.FURNACE);
    public static final SlimefunItemStack ENHANCED_FURNACE_7 = new SlimefunItemStack("ENHANCED_FURNACE_7", Material.FURNACE);
    public static final SlimefunItemStack ENHANCED_FURNACE_8 = new SlimefunItemStack("ENHANCED_FURNACE_8", Material.FURNACE);
    public static final SlimefunItemStack ENHANCED_FURNACE_9 = new SlimefunItemStack("ENHANCED_FURNACE_9", Material.FURNACE);
    public static final SlimefunItemStack ENHANCED_FURNACE_10 = new SlimefunItemStack("ENHANCED_FURNACE_10", Material.FURNACE);
    public static final SlimefunItemStack ENHANCED_FURNACE_11 = new SlimefunItemStack("ENHANCED_FURNACE_11", Material.FURNACE);
    public static final SlimefunItemStack REINFORCED_FURNACE = new SlimefunItemStack("REINFORCED_FURNACE", Material.FURNACE);
    public static final SlimefunItemStack CARBONADO_EDGED_FURNACE = new SlimefunItemStack("CARBONADO_EDGED_FURNACE", Material.FURNACE);

    /* Soulbound Items */
    public static final SlimefunItemStack SOULBOUND_SWORD = new SlimefunItemStack("SOULBOUND_SWORD", Material.DIAMOND_SWORD);
    public static final SlimefunItemStack SOULBOUND_BOW = new SlimefunItemStack("SOULBOUND_BOW", Material.BOW);
    public static final SlimefunItemStack SOULBOUND_PICKAXE = new SlimefunItemStack("SOULBOUND_PICKAXE", Material.DIAMOND_PICKAXE);
    public static final SlimefunItemStack SOULBOUND_AXE = new SlimefunItemStack("SOULBOUND_AXE", Material.DIAMOND_AXE);
    public static final SlimefunItemStack SOULBOUND_SHOVEL = new SlimefunItemStack("SOULBOUND_SHOVEL", XMaterial.DIAMOND_SHOVEL.parseMaterial());
    public static final SlimefunItemStack SOULBOUND_HOE = new SlimefunItemStack("SOULBOUND_HOE", Material.DIAMOND_HOE);
    public static final SlimefunItemStack SOULBOUND_TRIDENT = new SlimefunItemStack("SOULBOUND_TRIDENT", XMaterial.TRIDENT.parseMaterial());

    public static final SlimefunItemStack SOULBOUND_HELMET = new SlimefunItemStack("SOULBOUND_HELMET", Material.DIAMOND_HELMET);
    public static final SlimefunItemStack SOULBOUND_CHESTPLATE = new SlimefunItemStack("SOULBOUND_CHESTPLATE", Material.DIAMOND_CHESTPLATE);
    public static final SlimefunItemStack SOULBOUND_LEGGINGS = new SlimefunItemStack("SOULBOUND_LEGGINGS", Material.DIAMOND_LEGGINGS);
    public static final SlimefunItemStack SOULBOUND_BOOTS = new SlimefunItemStack("SOULBOUND_BOOTS", Material.DIAMOND_BOOTS);

    /* Runes */
    public static final SlimefunItemStack BLANK_RUNE = new SlimefunItemStack("BLANK_RUNE", ColoredFireworkStar.create(Color.BLACK));

    public static final SlimefunItemStack AIR_RUNE = new SlimefunItemStack("ANCIENT_RUNE_AIR", ColoredFireworkStar.create(Color.AQUA));
    public static final SlimefunItemStack WATER_RUNE = new SlimefunItemStack("ANCIENT_RUNE_WATER", ColoredFireworkStar.create(Color.BLUE));
    public static final SlimefunItemStack FIRE_RUNE = new SlimefunItemStack("ANCIENT_RUNE_FIRE", ColoredFireworkStar.create(Color.RED));
    public static final SlimefunItemStack EARTH_RUNE = new SlimefunItemStack("ANCIENT_RUNE_EARTH", ColoredFireworkStar.create(Color.fromRGB(112, 47, 7)));
    public static final SlimefunItemStack ENDER_RUNE = new SlimefunItemStack("ANCIENT_RUNE_ENDER", ColoredFireworkStar.create(Color.PURPLE));

    public static final SlimefunItemStack RAINBOW_RUNE = new SlimefunItemStack("ANCIENT_RUNE_RAINBOW", ColoredFireworkStar.create(Color.FUCHSIA));
    public static final SlimefunItemStack LIGHTNING_RUNE = new SlimefunItemStack("ANCIENT_RUNE_LIGHTNING", ColoredFireworkStar.create(Color.fromRGB(255, 255, 95)));
    public static final SlimefunItemStack SOULBOUND_RUNE = new SlimefunItemStack("ANCIENT_RUNE_SOULBOUND", ColoredFireworkStar.create(Color.fromRGB(47, 0, 117)));
    public static final SlimefunItemStack ENCHANTMENT_RUNE = new SlimefunItemStack("ANCIENT_RUNE_ENCHANTMENT", ColoredFireworkStar.create(Color.fromRGB(255, 217, 25)));
    public static final SlimefunItemStack VILLAGER_RUNE = new SlimefunItemStack("ANCIENT_RUNE_VILLAGERS", ColoredFireworkStar.create(Color.fromRGB(160, 20, 5)));
    public static final SlimefunItemStack RUNE_ANVIL = new SlimefunItemStack("RUNE_ANVIL", XMaterial.ANVIL.parseMaterial());

    /* Electricity */
    public static final SlimefunItemStack SOLAR_GENERATOR = new SlimefunItemStack("SOLAR_GENERATOR", Material.DAYLIGHT_DETECTOR);
    public static final SlimefunItemStack SOLAR_GENERATOR_2 = new SlimefunItemStack("SOLAR_GENERATOR_2", Material.DAYLIGHT_DETECTOR);
    public static final SlimefunItemStack SOLAR_GENERATOR_3 = new SlimefunItemStack("SOLAR_GENERATOR_3", Material.DAYLIGHT_DETECTOR);
    public static final SlimefunItemStack SOLAR_GENERATOR_4 = new SlimefunItemStack("SOLAR_GENERATOR_4", Material.DAYLIGHT_DETECTOR);

    public static final SlimefunItemStack COAL_GENERATOR = new SlimefunItemStack("COAL_GENERATOR", HeadTexture.GENERATOR);
    public static final SlimefunItemStack COAL_GENERATOR_2 = new SlimefunItemStack("COAL_GENERATOR_2", HeadTexture.GENERATOR);

    public static final SlimefunItemStack LAVA_GENERATOR = new SlimefunItemStack("LAVA_GENERATOR", HeadTexture.GENERATOR);
    public static final SlimefunItemStack LAVA_GENERATOR_2 = new SlimefunItemStack("LAVA_GENERATOR_2", HeadTexture.GENERATOR);

    public static final SlimefunItemStack ELECTRIC_FURNACE = new SlimefunItemStack("ELECTRIC_FURNACE", Material.FURNACE);
    public static final SlimefunItemStack ELECTRIC_FURNACE_2 = new SlimefunItemStack("ELECTRIC_FURNACE_2", Material.FURNACE);
    public static final SlimefunItemStack ELECTRIC_FURNACE_3 = new SlimefunItemStack("ELECTRIC_FURNACE_3", Material.FURNACE);

    public static final SlimefunItemStack ELECTRIC_ORE_GRINDER = new SlimefunItemStack("ELECTRIC_ORE_GRINDER", Material.FURNACE);
    public static final SlimefunItemStack ELECTRIC_ORE_GRINDER_2 = new SlimefunItemStack("ELECTRIC_ORE_GRINDER_2", Material.FURNACE);
    public static final SlimefunItemStack ELECTRIC_ORE_GRINDER_3 = new SlimefunItemStack("ELECTRIC_ORE_GRINDER_3", Material.FURNACE);
    public static final SlimefunItemStack ELECTRIC_INGOT_PULVERIZER = new SlimefunItemStack("ELECTRIC_INGOT_PULVERIZER", Material.FURNACE);
    public static final SlimefunItemStack AUTO_DRIER = new SlimefunItemStack("AUTO_DRIER", XMaterial.SMOKER.parseMaterial());
    public static final SlimefunItemStack AUTO_ENCHANTER = new SlimefunItemStack("AUTO_ENCHANTER", XMaterial.ENCHANTING_TABLE.parseMaterial());
    public static final SlimefunItemStack AUTO_ENCHANTER_2 = new SlimefunItemStack("AUTO_ENCHANTER_2", XMaterial.ENCHANTING_TABLE.parseMaterial());
    public static final SlimefunItemStack AUTO_DISENCHANTER = new SlimefunItemStack("AUTO_DISENCHANTER", XMaterial.ENCHANTING_TABLE.parseMaterial());
    public static final SlimefunItemStack AUTO_DISENCHANTER_2 = new SlimefunItemStack("AUTO_DISENCHANTER_2", XMaterial.ENCHANTING_TABLE.parseMaterial());
    public static final SlimefunItemStack AUTO_ANVIL = new SlimefunItemStack("AUTO_ANVIL", Material.IRON_BLOCK);
    public static final SlimefunItemStack AUTO_ANVIL_2 = new SlimefunItemStack("AUTO_ANVIL_2", Material.IRON_BLOCK);
    public static final SlimefunItemStack AUTO_BREWER = new SlimefunItemStack("AUTO_BREWER", XMaterial.SMOKER.parseMaterial());

    public static final SlimefunItemStack BOOK_BINDER = new SlimefunItemStack("BOOK_BINDER", Material.BOOKSHELF);

    public static final SlimefunItemStack BIO_REACTOR = new SlimefunItemStack("BIO_REACTOR", XMaterial.LIME_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack MULTIMETER = new SlimefunItemStack("MULTIMETER", XMaterial.CLOCK.parseMaterial());

    public static final SlimefunItemStack SMALL_CAPACITOR = new SlimefunItemStack("SMALL_CAPACITOR", HeadTexture.CAPACITOR_25);
    public static final SlimefunItemStack MEDIUM_CAPACITOR = new SlimefunItemStack("MEDIUM_CAPACITOR", HeadTexture.CAPACITOR_25);
    public static final SlimefunItemStack BIG_CAPACITOR = new SlimefunItemStack("BIG_CAPACITOR", HeadTexture.CAPACITOR_25);
    public static final SlimefunItemStack LARGE_CAPACITOR = new SlimefunItemStack("LARGE_CAPACITOR", HeadTexture.CAPACITOR_25);
    public static final SlimefunItemStack CARBONADO_EDGED_CAPACITOR = new SlimefunItemStack("CARBONADO_EDGED_CAPACITOR", HeadTexture.CAPACITOR_25);
    public static final SlimefunItemStack ENERGIZED_CAPACITOR = new SlimefunItemStack("ENERGIZED_CAPACITOR", HeadTexture.CAPACITOR_25);

    /* Robots */
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID = new SlimefunItemStack("PROGRAMMABLE_ANDROID", HeadTexture.PROGRAMMABLE_ANDROID);
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_FARMER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_FARMER", HeadTexture.PROGRAMMABLE_ANDROID_FARMER);
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_MINER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_MINER", HeadTexture.PROGRAMMABLE_ANDROID_MINER);
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_WOODCUTTER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_WOODCUTTER", HeadTexture.PROGRAMMABLE_ANDROID_WOODCUTTER);
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_BUTCHER", HeadTexture.PROGRAMMABLE_ANDROID_BUTCHER);
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_FISHERMAN", HeadTexture.PROGRAMMABLE_ANDROID_FISHERMAN);

    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2 = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2", HeadTexture.PROGRAMMABLE_ANDROID);
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_FISHERMAN", HeadTexture.PROGRAMMABLE_ANDROID_FISHERMAN);
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2_FARMER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_FARMER", HeadTexture.PROGRAMMABLE_ANDROID_FARMER);
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_BUTCHER", HeadTexture.PROGRAMMABLE_ANDROID_BUTCHER);

    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_3 = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3", HeadTexture.PROGRAMMABLE_ANDROID);
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_3_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3_FISHERMAN", HeadTexture.PROGRAMMABLE_ANDROID_FISHERMAN);
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_3_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3_BUTCHER", HeadTexture.PROGRAMMABLE_ANDROID_BUTCHER);

    /* GPS */
    public static final SlimefunItemStack GPS_TRANSMITTER = new SlimefunItemStack("GPS_TRANSMITTER", HeadTexture.GPS_TRANSMITTER);
    public static final SlimefunItemStack GPS_TRANSMITTER_2 = new SlimefunItemStack("GPS_TRANSMITTER_2", HeadTexture.GPS_TRANSMITTER);
    public static final SlimefunItemStack GPS_TRANSMITTER_3 = new SlimefunItemStack("GPS_TRANSMITTER_3", HeadTexture.GPS_TRANSMITTER);
    public static final SlimefunItemStack GPS_TRANSMITTER_4 = new SlimefunItemStack("GPS_TRANSMITTER_4", HeadTexture.GPS_TRANSMITTER);

    public static final SlimefunItemStack GPS_MARKER_TOOL = new SlimefunItemStack("GPS_MARKER_TOOL", XMaterial.REDSTONE_TORCH.parseMaterial());
    public static final SlimefunItemStack GPS_CONTROL_PANEL = new SlimefunItemStack("GPS_CONTROL_PANEL", HeadTexture.GPS_CONTROL_PANEL);
    public static final SlimefunItemStack GPS_EMERGENCY_TRANSMITTER = new SlimefunItemStack("GPS_EMERGENCY_TRANSMITTER", HeadTexture.GPS_TRANSMITTER);

    public static final SlimefunItemStack ANDROID_INTERFACE_FUEL = new SlimefunItemStack("ANDROID_INTERFACE_FUEL", Material.DISPENSER);
    public static final SlimefunItemStack ANDROID_INTERFACE_ITEMS = new SlimefunItemStack("ANDROID_INTERFACE_ITEMS", Material.DISPENSER);

    public static final SlimefunItemStack GPS_GEO_SCANNER = new SlimefunItemStack("GPS_GEO_SCANNER", HeadTexture.GEO_SCANNER);
    public static final SlimefunItemStack PORTABLE_GEO_SCANNER = new SlimefunItemStack("PORTABLE_GEO_SCANNER", XMaterial.CLOCK.parseMaterial());
    public static final SlimefunItemStack GEO_MINER = new SlimefunItemStack("GEO_MINER", HeadTexture.GEO_MINER);
    public static final SlimefunItemStack OIL_PUMP = new SlimefunItemStack("OIL_PUMP", HeadTexture.OIL_PUMP);
    public static final SlimefunItemStack OIL_BUCKET = new SlimefunItemStack("BUCKET_OF_OIL", HeadTexture.OIL_BUCKET);
    public static final SlimefunItemStack FUEL_BUCKET = new SlimefunItemStack("BUCKET_OF_FUEL", HeadTexture.FUEL_BUCKET);

    public static final SlimefunItemStack REFINERY = new SlimefunItemStack("REFINERY", XMaterial.PISTON.parseMaterial());
    public static final SlimefunItemStack COMBUSTION_REACTOR = new SlimefunItemStack("COMBUSTION_REACTOR", HeadTexture.GENERATOR);
    public static final SlimefunItemStack ANDROID_MEMORY_CORE = new SlimefunItemStack("ANDROID_MEMORY_CORE", HeadTexture.ENERGY_REGULATOR);

    public static final SlimefunItemStack GPS_TELEPORTER_PYLON = new SlimefunItemStack("GPS_TELEPORTER_PYLON", XMaterial.PURPLE_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack GPS_TELEPORTATION_MATRIX = new SlimefunItemStack("GPS_TELEPORTATION_MATRIX", Material.IRON_BLOCK);
    public static final SlimefunItemStack GPS_ACTIVATION_DEVICE_SHARED = new SlimefunItemStack("GPS_ACTIVATION_DEVICE_SHARED", XMaterial.STONE_PRESSURE_PLATE.parseMaterial());
    public static final SlimefunItemStack GPS_ACTIVATION_DEVICE_PERSONAL = new SlimefunItemStack("GPS_ACTIVATION_DEVICE_PERSONAL", XMaterial.STONE_PRESSURE_PLATE.parseMaterial());
    public static final SlimefunItemStack PORTABLE_TELEPORTER = new SlimefunItemStack("PORTABLE_TELEPORTER", Material.COMPASS);

    public static final SlimefunItemStack ELEVATOR_PLATE = new SlimefunItemStack("ELEVATOR_PLATE", XMaterial.STONE_PRESSURE_PLATE.parseMaterial());
    public static final SlimefunItemStack INFUSED_HOPPER = new SlimefunItemStack("INFUSED_HOPPER", Material.HOPPER);

    public static final SlimefunItemStack HEATED_PRESSURE_CHAMBER = new SlimefunItemStack("HEATED_PRESSURE_CHAMBER", XMaterial.LIGHT_GRAY_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack HEATED_PRESSURE_CHAMBER_2 = new SlimefunItemStack("HEATED_PRESSURE_CHAMBER_2", XMaterial.LIGHT_GRAY_STAINED_GLASS.parseMaterial());

    public static final SlimefunItemStack ELECTRIC_SMELTERY = new SlimefunItemStack("ELECTRIC_SMELTERY", Material.FURNACE);
    public static final SlimefunItemStack ELECTRIC_SMELTERY_2 = new SlimefunItemStack("ELECTRIC_SMELTERY_2", Material.FURNACE);

    public static final SlimefunItemStack ELECTRIC_PRESS = new SlimefunItemStack("ELECTRIC_PRESS", HeadTexture.ELECTRIC_PRESS);
    public static final SlimefunItemStack ELECTRIC_PRESS_2 = new SlimefunItemStack("ELECTRIC_PRESS_2", HeadTexture.ELECTRIC_PRESS);

    public static final SlimefunItemStack ELECTRIFIED_CRUCIBLE = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE", XMaterial.RED_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack ELECTRIFIED_CRUCIBLE_2 = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE_2", XMaterial.RED_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack ELECTRIFIED_CRUCIBLE_3 = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE_3", XMaterial.RED_TERRACOTTA.parseMaterial());

    public static final SlimefunItemStack CARBON_PRESS = new SlimefunItemStack("CARBON_PRESS", XMaterial.BLACK_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack CARBON_PRESS_2 = new SlimefunItemStack("CARBON_PRESS_2", XMaterial.BLACK_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack CARBON_PRESS_3 = new SlimefunItemStack("CARBON_PRESS_3", XMaterial.BLACK_STAINED_GLASS.parseMaterial());

    public static final SlimefunItemStack BLISTERING_INGOT = new SlimefunItemStack("BLISTERING_INGOT", Material.GOLD_INGOT);
    public static final SlimefunItemStack BLISTERING_INGOT_2 = new SlimefunItemStack("BLISTERING_INGOT_2", Material.GOLD_INGOT);
    public static final SlimefunItemStack BLISTERING_INGOT_3 = new SlimefunItemStack("BLISTERING_INGOT_3", Material.GOLD_INGOT);

    public static final SlimefunItemStack ENERGY_REGULATOR = new SlimefunItemStack("ENERGY_REGULATOR", HeadTexture.ENERGY_REGULATOR);
    public static final SlimefunItemStack ENERGY_CONNECTOR = new SlimefunItemStack("ENERGY_CONNECTOR", HeadTexture.ENERGY_CONNECTOR);
    public static final SlimefunItemStack DEBUG_FISH = new SlimefunItemStack("DEBUG_FISH", XMaterial.SALMON.parseMaterial());

    public static final SlimefunItemStack NETHER_ICE = new SlimefunItemStack("NETHER_ICE", HeadTexture.NETHER_ICE);
    public static final SlimefunItemStack ENRICHED_NETHER_ICE = new SlimefunItemStack("ENRICHED_NETHER_ICE", HeadTexture.ENRICHED_NETHER_ICE);
    public static final SlimefunItemStack NETHER_ICE_COOLANT_CELL = new SlimefunItemStack("NETHER_ICE_COOLANT_CELL", HeadTexture.NETHER_ICE_COOLANT_CELL);

    // Cargo
    public static final SlimefunItemStack CARGO_MANAGER = new SlimefunItemStack("CARGO_MANAGER", HeadTexture.CARGO_MANAGER);
    public static final SlimefunItemStack CARGO_CONNECTOR_NODE = new SlimefunItemStack("CARGO_NODE", HeadTexture.CARGO_CONNECTOR_NODE);
    public static final SlimefunItemStack CARGO_INPUT_NODE = new SlimefunItemStack("CARGO_NODE_INPUT", HeadTexture.CARGO_INPUT_NODE);
    public static final SlimefunItemStack CARGO_OUTPUT_NODE = new SlimefunItemStack("CARGO_NODE_OUTPUT", HeadTexture.CARGO_OUTPUT_NODE);
    public static final SlimefunItemStack CARGO_OUTPUT_NODE_2 = new SlimefunItemStack("CARGO_NODE_OUTPUT_ADVANCED", HeadTexture.CARGO_OUTPUT_NODE);

    // Animal farm
    public static final SlimefunItemStack AUTO_BREEDER = new SlimefunItemStack("AUTO_BREEDER", Material.HAY_BLOCK);
    public static final SlimefunItemStack PRODUCE_COLLECTOR = new SlimefunItemStack("PRODUCE_COLLECTOR", Material.HAY_BLOCK);

    public static final SlimefunItemStack ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack WHEAT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_WHEAT", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack CARROT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_CARROT", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack POTATO_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_POTATO", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack SEEDS_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SEEDS", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack BEETROOT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_BEETROOT", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack MELON_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_MELON", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack APPLE_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_APPLE", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack SWEET_BERRIES_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SWEET_BERRIES", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack KELP_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_KELP", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack COCOA_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_COCOA", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack SEAGRASS_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SEAGRASS", HeadTexture.FILLED_CAN);

    public static final SlimefunItemStack FERTILIZER = new SlimefunItemStack("FERTILIZER", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack WHEAT_FERTILIZER = new SlimefunItemStack("FERTILIZER_WHEAT", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack CARROT_FERTILIZER = new SlimefunItemStack("FERTILIZER_CARROT", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack POTATO_FERTILIZER = new SlimefunItemStack("FERTILIZER_POTATO", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack SEEDS_FERTILIZER = new SlimefunItemStack("FERTILIZER_SEEDS", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack BEETROOT_FERTILIZER = new SlimefunItemStack("FERTILIZER_BEETROOT", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack MELON_FERTILIZER = new SlimefunItemStack("FERTILIZER_MELON", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack APPLE_FERTILIZER = new SlimefunItemStack("FERTILIZER_APPLE", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack SWEET_BERRIES_FERTILIZER = new SlimefunItemStack("FERTILIZER_SWEET_BERRIES", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack KELP_FERTILIZER = new SlimefunItemStack("FERTILIZER_KELP", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack COCOA_FERTILIZER = new SlimefunItemStack("FERTILIZER_COCOA", HeadTexture.FILLED_CAN);
    public static final SlimefunItemStack SEAGRASS_FERTILIZER = new SlimefunItemStack("FERTILIZER_SEAGRASS", HeadTexture.FILLED_CAN);

    public static final SlimefunItemStack ANIMAL_GROWTH_ACCELERATOR = new SlimefunItemStack("ANIMAL_GROWTH_ACCELERATOR", Material.HAY_BLOCK);
    public static final SlimefunItemStack CROP_GROWTH_ACCELERATOR = new SlimefunItemStack("CROP_GROWTH_ACCELERATOR", XMaterial.LIME_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack CROP_GROWTH_ACCELERATOR_2 = new SlimefunItemStack("CROP_GROWTH_ACCELERATOR_2", XMaterial.LIME_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack TREE_GROWTH_ACCELERATOR = new SlimefunItemStack("TREE_GROWTH_ACCELERATOR", XMaterial.BROWN_TERRACOTTA.parseMaterial());

    public static final SlimefunItemStack FOOD_FABRICATOR = new SlimefunItemStack("FOOD_FABRICATOR", XMaterial.GREEN_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack FOOD_FABRICATOR_2 = new SlimefunItemStack("FOOD_FABRICATOR_2", XMaterial.GREEN_STAINED_GLASS.parseMaterial());

    public static final SlimefunItemStack FOOD_COMPOSTER = new SlimefunItemStack("FOOD_COMPOSTER", XMaterial.GREEN_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack FOOD_COMPOSTER_2 = new SlimefunItemStack("FOOD_COMPOSTER_2", XMaterial.GREEN_TERRACOTTA.parseMaterial());

    public static final SlimefunItemStack EXP_COLLECTOR = new SlimefunItemStack("XP_COLLECTOR", HeadTexture.EXP_COLLECTOR);
    public static final SlimefunItemStack REACTOR_COOLANT_CELL = new SlimefunItemStack("REACTOR_COLLANT_CELL", HeadTexture.COOLANT_CELL);

    public static final SlimefunItemStack NUCLEAR_REACTOR = new SlimefunItemStack("NUCLEAR_REACTOR", HeadTexture.NUCLEAR_REACTOR);
    public static final SlimefunItemStack NETHER_STAR_REACTOR = new SlimefunItemStack("NETHERSTAR_REACTOR", HeadTexture.NETHER_STAR_REACTOR);
    public static final SlimefunItemStack REACTOR_ACCESS_PORT = new SlimefunItemStack("REACTOR_ACCESS_PORT", XMaterial.CYAN_TERRACOTTA.parseMaterial());

    public static final SlimefunItemStack FREEZER = new SlimefunItemStack("FREEZER", XMaterial.LIGHT_BLUE_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack FREEZER_2 = new SlimefunItemStack("FREEZER_2", XMaterial.LIGHT_BLUE_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack FREEZER_3 = new SlimefunItemStack("FREEZER_3", XMaterial.LIGHT_BLUE_STAINED_GLASS.parseMaterial());

    public static final SlimefunItemStack ELECTRIC_GOLD_PAN = new SlimefunItemStack("ELECTRIC_GOLD_PAN", XMaterial.BROWN_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack ELECTRIC_GOLD_PAN_2 = new SlimefunItemStack("ELECTRIC_GOLD_PAN_2", XMaterial.BROWN_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack ELECTRIC_GOLD_PAN_3 = new SlimefunItemStack("ELECTRIC_GOLD_PAN_3", XMaterial.BROWN_TERRACOTTA.parseMaterial());

    public static final SlimefunItemStack ELECTRIC_DUST_WASHER = new SlimefunItemStack("ELECTRIC_DUST_WASHER", XMaterial.BLUE_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack ELECTRIC_DUST_WASHER_2 = new SlimefunItemStack("ELECTRIC_DUST_WASHER_2", XMaterial.BLUE_STAINED_GLASS.parseMaterial());
    public static final SlimefunItemStack ELECTRIC_DUST_WASHER_3 = new SlimefunItemStack("ELECTRIC_DUST_WASHER_3", XMaterial.BLUE_STAINED_GLASS.parseMaterial());

    public static final SlimefunItemStack ELECTRIC_INGOT_FACTORY = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY", XMaterial.RED_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack ELECTRIC_INGOT_FACTORY_2 = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY_2", XMaterial.RED_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack ELECTRIC_INGOT_FACTORY_3 = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY_3", XMaterial.RED_TERRACOTTA.parseMaterial());

    public static final SlimefunItemStack FLUID_PUMP = new SlimefunItemStack("FLUID_PUMP", XMaterial.BLUE_TERRACOTTA.parseMaterial());
    public static final SlimefunItemStack CHARGING_BENCH = new SlimefunItemStack("CHARGING_BENCH", XMaterial.CRAFTING_TABLE.parseMaterial());

    public static final SlimefunItemStack VANILLA_AUTO_CRAFTER = new SlimefunItemStack("VANILLA_AUTO_CRAFTER", HeadTexture.VANILLA_AUTO_CRAFTER);
    public static final SlimefunItemStack ENHANCED_AUTO_CRAFTER = new SlimefunItemStack("ENHANCED_AUTO_CRAFTER", HeadTexture.ENHANCED_AUTO_CRAFTER);
    public static final SlimefunItemStack ARMOR_AUTO_CRAFTER = new SlimefunItemStack("ARMOR_AUTO_CRAFTER", HeadTexture.ARMOR_AUTO_CRAFTER);

    public static final SlimefunItemStack IRON_GOLEM_ASSEMBLER = new SlimefunItemStack("IRON_GOLEM_ASSEMBLER", Material.IRON_BLOCK);
    public static final SlimefunItemStack WITHER_ASSEMBLER = new SlimefunItemStack("WITHER_ASSEMBLER", Material.OBSIDIAN);

    public static final SlimefunItemStack TRASH_CAN = new SlimefunItemStack("TRASH_CAN_BLOCK", HeadTexture.TRASH_CAN);

    public static final SlimefunItemStack ELYTRA_SCALE = new SlimefunItemStack("ELYTRA_SCALE", Material.FEATHER);
    public static final SlimefunItemStack INFUSED_ELYTRA = new SlimefunItemStack("INFUSED_ELYTRA", XMaterial.ELYTRA.parseMaterial());
    public static final SlimefunItemStack SOULBOUND_ELYTRA = new SlimefunItemStack("SOULBOUND_ELYTRA", XMaterial.ELYTRA.parseMaterial());

    public static final SlimefunItemStack MAGNESIUM_SALT = new SlimefunItemStack("MAGNESIUM_SALT", Material.SUGAR);
    public static final SlimefunItemStack MAGNESIUM_GENERATOR = new SlimefunItemStack("MAGNESIUM_GENERATOR", HeadTexture.GENERATOR);

    static {
        // MENDING is 1.9+ (null on older servers, where the Infused Elytra cannot carry it).
        if (VersionedEnchantment.MENDING != null) {
            INFUSED_ELYTRA.addUnsafeEnchantment(VersionedEnchantment.MENDING, 1);
        }
    }
}

