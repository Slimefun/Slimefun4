package io.github.thebusybiscuit.slimefun4.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves.StormStaff;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;

/**
 * This class holds a static references to every {@link SlimefunItemStack}
 * found in Slimefun.
 */
@SuppressWarnings("java:S1192") // Suppress "duplicate string literal" warnings
public final class SlimefunItems {

    private SlimefunItems() {}

    /* Items */
    public static final SlimefunItemStack PORTABLE_CRAFTER = new SlimefunItemStack("PORTABLE_CRAFTER", HeadTexture.PORTABLE_CRAFTER, "&6Établi portable", "&a&oUn établi portable", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack PORTABLE_DUSTBIN = new SlimefunItemStack("PORTABLE_DUSTBIN", HeadTexture.TRASH_CAN, "&6Poubelle portable", "&fVotre poubelle portable", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack ENDER_BACKPACK = new SlimefunItemStack("ENDER_BACKPACK", HeadTexture.ENDER_BACKPACK, "&6Sac-à-dos du Néant", "&a&oUn Coffre du Néant portable", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack MAGIC_EYE_OF_ENDER = new SlimefunItemStack("MAGIC_EYE_OF_ENDER", Material.ENDER_EYE, "&6&lŒil magique du Néant", "&4&lRequiert l'armure du Néant complète", "", "&7&eClic-droit&7 pour lancer une perle du Néant");
    public static final SlimefunItemStack BROKEN_SPAWNER = new SlimefunItemStack("BROKEN_SPAWNER", Material.SPAWNER, "&cGénérateur de monstres brisé", "&7Type: &b<Type>", "", "&cIl est fracturé, il faut le réparer dans un Autel Ancien..");
    public static final SlimefunItemStack REPAIRED_SPAWNER = new SlimefunItemStack("REINFORCED_SPAWNER", Material.SPAWNER, "&bGénérateur de monstres renforcé", "&7Type: &b<Type>");
    public static final SlimefunItemStack INFERNAL_BONEMEAL = new SlimefunItemStack("INFERNAL_BONEMEAL", Material.BONE_MEAL, "&4Poudre d'os infernale", "", "&cAméliore la vitesse de pousse des", "&cVerrues du Tréfond");
    public static final SlimefunItemStack TAPE_MEASURE = new SlimefunItemStack("TAPE_MEASURE", "180d5c43a6cf5bb7769fd0c8240e1e70d2ae38ef9d78a1db401aca6a2cb36f65", "&6Mètre", "", "&eS'accroupir & faire clic-droit &7pour placer", " &e le point A", "&eClic-gauche &7pour mesurer la distance");

    /* Gadgets */
    public static final SlimefunItemStack GOLD_PAN = new SlimefunItemStack("GOLD_PAN", Material.BOWL, "&6Tamis", "", "&eClic-droit&7 pour collecter des ressources", "&7provenant du Gravier");
    public static final SlimefunItemStack NETHER_GOLD_PAN = new SlimefunItemStack("NETHER_GOLD_PAN", Material.BOWL, "&4Tamis des Tréfonds", "", "&eClic-droit&7 pour collecter des ressources", "&7provenant du Sable des Âmes");
    public static final SlimefunItemStack PARACHUTE = new SlimefunItemStack("PARACHUTE", Material.LEATHER_CHESTPLATE, Color.WHITE, "&f&lParachute", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack GRAPPLING_HOOK = new SlimefunItemStack("GRAPPLING_HOOK", Material.LEAD, "&6Grappin", "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack SOLAR_HELMET = new SlimefunItemStack("SOLAR_HELMET", Material.IRON_HELMET, "&bCasque solaire", "", "&a&oRecharge les objetss votre inventaire/équippements");
    public static final SlimefunItemStack CLOTH = new SlimefunItemStack("CLOTH", Material.PAPER, "&bTissu");
    public static final SlimefunItemStack REINFORCED_CLOTH = new SlimefunItemStack("REINFORCED_CLOTH", Material.PAPER, "&bTissu renforcé", "", "&fCe tissu a été renforcé", "&favec du &bPlomb &fpour vous protéger contre", "&fles substances radioactives");
    public static final SlimefunItemStack TIN_CAN = new SlimefunItemStack("CAN", HeadTexture.TIN_CAN, "&fBoîte de conserve");
    public static final SlimefunItemStack NIGHT_VISION_GOGGLES = new SlimefunItemStack("NIGHT_VISION_GOGGLES", Material.LEATHER_HELMET, Color.BLACK, "&aLunettes de vision nocturne", "", "&9+ Vision Nocturne");
    public static final SlimefunItemStack ELYTRA_CAP = new SlimefunItemStack("ELYTRA_CAP", Material.LEATHER_HELMET, Color.PURPLE, "&5Casque d'Elytras", "", "&7Ce casque vous protègera contre", "&7les crashs en portant des Elytras.");
    public static final SlimefunItemStack FARMER_SHOES = new SlimefunItemStack("FARMER_SHOES", Material.LEATHER_BOOTS, Color.YELLOW, "&eBottes du fermier", "", "&6&oVous devenez assez adroit pour ne pas détruire les plantations..");
    public static final SlimefunItemStack INFUSED_MAGNET = new SlimefunItemStack("INFUSED_MAGNET", HeadTexture.MAGNET, "&aAimant infusé", "", "&fAimant infusé magique", "&fqui attire les objets", "&fà proximité de vous", "", "&7Maintenir &eShift&7 pour attirer les objets à vos pieds");
    public static final SlimefunItemStack RAG = new SlimefunItemStack("RAG", Material.PAPER, "&cChiffon", "", "&aNiveau I - Fournitures médicales", "", "&fRestaure 2 Coeurs", "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack BANDAGE = new SlimefunItemStack("BANDAGE", Material.PAPER, "&cBandage", "", "&aNiveau II - Fournitures médicales", "", "&fRestaure 4 Coeurs", "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack SPLINT = new SlimefunItemStack("SPLINT", Material.STICK, "&cAttelle", "", "&aNiveau I - Fournitures médicales", "", "&fRestaure 2 Coeurs", "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack VITAMINS = new SlimefunItemStack("VITAMINS", Material.NETHER_WART, "&cVitamines", "", "&aNiveau III - Fournitures médicales", "", "&fRestaure 4 Coeurs", "&fSoigne des effets Poisons/Withers/Radiations", "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack MEDICINE = new SlimefunItemStack("MEDICINE", Material.POTION, Color.RED, "&cMédicament", "", "&aNiveau III - Fournitures médicales", "", "&fRestaure 4 Coeurs", "&fSoigne des effets Poisons/Withers/Radiations");
    public static final SlimefunItemStack MAGICAL_ZOMBIE_PILLS = new SlimefunItemStack("MAGICAL_ZOMBIE_PILLS", Material.NETHER_WART, "&6Pilules magiques pour zombies", "", "&eClic droit sur un &avillageois zombifié", "&eou &7un Piglin zombifié pour", "&7le soigner instantanément de sa malédiction");

    public static final SlimefunItemStack FLASK_OF_KNOWLEDGE = new SlimefunItemStack("FLASK_OF_KNOWLEDGE", Material.GLASS_BOTTLE, "&cFlacon de connaissances", "", "&fPermet de stocker une partie de", "&fvotre expérience dans une bouteille", "&7Coût: &a1 Niveau");
    public static final SlimefunItemStack FILLED_FLASK_OF_KNOWLEDGE = new SlimefunItemStack("FILLED_FLASK_OF_KNOWLEDGE", Material.EXPERIENCE_BOTTLE, "&aFlacon de connaissances");

    /* Backpacks */
    private static final String BACKPACK_ID = "&7ID: <ID>";
    public static final SlimefunItemStack BACKPACK_SMALL = new SlimefunItemStack("SMALL_BACKPACK", HeadTexture.BACKPACK, "&ePetit sac-à-dos", "", "&7Taille: &e9", BACKPACK_ID, "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack BACKPACK_MEDIUM = new SlimefunItemStack("MEDIUM_BACKPACK", HeadTexture.BACKPACK, "&eSac-à-dos normal", "", "&7Taille: &e18", BACKPACK_ID, "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack BACKPACK_LARGE = new SlimefunItemStack("LARGE_BACKPACK", HeadTexture.BACKPACK, "&eGrand sac-à-dos", "", "&7Taille: &e27", BACKPACK_ID, "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack WOVEN_BACKPACK = new SlimefunItemStack("WOVEN_BACKPACK", HeadTexture.BACKPACK, "&eTrès grand sac-à-dos", "", "&7Taille: &e36", BACKPACK_ID, "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack GILDED_BACKPACK = new SlimefunItemStack("GILDED_BACKPACK", HeadTexture.BACKPACK, "&eSac-à-dos énorme", "", "&7Taille: &e45", BACKPACK_ID, "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack RADIANT_BACKPACK = new SlimefunItemStack("RADIANT_BACKPACK", HeadTexture.BACKPACK, "&eSac-à-dos sans fond", "", "&7Taille: &e54 (Double coffres)", BACKPACK_ID, "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack BOUND_BACKPACK = new SlimefunItemStack("BOUND_BACKPACK", HeadTexture.ENDER_BACKPACK, "&cSac-à-dos des âmes", "", "&7Taille: &e36", BACKPACK_ID, "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack COOLER = new SlimefunItemStack("COOLER", HeadTexture.COOLER, "&bCooler", "&fVous permet de stocker des jus/smoothies", "&fet automatiquement les consommer quand le besoin se présente", "", "&7Taille: &e27", BACKPACK_ID, "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack RESTORED_BACKPACK = new SlimefunItemStack("RESTORED_BACKPACK", HeadTexture.RESTORED_BACKPACK, "&eSac-à-dos de restauration", "", "&7écupère vos objets perdus", BACKPACK_ID, "", LoreBuilder.RIGHT_CLICK_TO_OPEN);

    /* Jetpacks */
    public static final SlimefunItemStack DURALUMIN_JETPACK = new SlimefunItemStack("DURALUMIN_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Jetpack électrique &7- &eI", "", LoreBuilder.material("Alliage d'alluminium léger"), LoreBuilder.powerCharged(0, 20), "&8\u21E8 &7Poussée: &c0.35", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack SOLDER_JETPACK = new SlimefunItemStack("SOLDER_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Jetpack électrique &7- &eII", "", LoreBuilder.material("Soudure d'étain"), LoreBuilder.powerCharged(0, 30), "&8\u21E8 &7Poussée: &c0.4", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack BILLON_JETPACK = new SlimefunItemStack("BILLON_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Jetpack électrique &7- &eIII", "", LoreBuilder.material("Billon"), LoreBuilder.powerCharged(0, 45), "&8\u21E8 &7Poussée: &c0.45", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack STEEL_JETPACK = new SlimefunItemStack("STEEL_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Jetpack électrique &7- &eIV", "", LoreBuilder.material("Acier"), LoreBuilder.powerCharged(0, 60), "&8\u21E8 &7Poussée: &c0.5", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack DAMASCUS_STEEL_JETPACK = new SlimefunItemStack("DAMASCUS_STEEL_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Jetpack électrique &7- &eV", "", LoreBuilder.material("Acier de Damas"), LoreBuilder.powerCharged(0, 75), "&8\u21E8 &7Poussée: &c0.55", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack REINFORCED_ALLOY_JETPACK = new SlimefunItemStack("REINFORCED_ALLOY_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9Jetpack électrique &7- &eVI", "", LoreBuilder.material("Alliage renforcé"), LoreBuilder.powerCharged(0, 100), "&8\u21E8 &7Poussée: &c0.6", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack CARBONADO_JETPACK = new SlimefunItemStack("CARBONADO_JETPACK", Material.LEATHER_CHESTPLATE, Color.BLACK, "&9Jetpack électrique &7- &eVII", "", LoreBuilder.material("Carbonado"), LoreBuilder.powerCharged(0, 150), "&8\u21E8 &7Poussée: &c0.7", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack ARMORED_JETPACK = new SlimefunItemStack("ARMORED_JETPACK", Material.IRON_CHESTPLATE, "&9Jetpack blindé", LoreBuilder.material("Acier"), "", LoreBuilder.powerCharged(0, 50), "&8\u21E8 &7Poussée: &c0.45", "", LoreBuilder.CROUCH_TO_USE);

    /* Jetboots */
    public static final SlimefunItemStack DURALUMIN_JETBOOTS = new SlimefunItemStack("DURALUMIN_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Bottes-Jet &7- &eI", "", LoreBuilder.material("Alliage d'alluminium léger"), LoreBuilder.powerCharged(0, 20), LoreBuilder.speed(0.35F), "&8\u21E8 &7Précision: &c50%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack SOLDER_JETBOOTS = new SlimefunItemStack("SOLDER_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Bottes-Jet &7- &eII", "", LoreBuilder.material("Soudure d'étain"), LoreBuilder.powerCharged(0, 30), LoreBuilder.speed(0.4F), "&8\u21E8 &7Précision: &660%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack BILLON_JETBOOTS = new SlimefunItemStack("BILLON_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Bottes-Jet &7- &eIII", "", LoreBuilder.material("Billon"), LoreBuilder.powerCharged(0, 40), LoreBuilder.speed(0.45F), "&8\u21E8 &7Précision: &665%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack STEEL_JETBOOTS = new SlimefunItemStack("STEEL_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Bottes-Jet &7- &eIV", "", LoreBuilder.material("Acier"), LoreBuilder.powerCharged(0, 50), LoreBuilder.speed(0.5F), "&8\u21E8 &7Précision: &e70%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack DAMASCUS_STEEL_JETBOOTS = new SlimefunItemStack("DAMASCUS_STEEL_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Bottes-Jet &7- &eV", "", LoreBuilder.material("Acier de Damas"), LoreBuilder.powerCharged(0, 75), LoreBuilder.speed(0.55F), "&8\u21E8 &7Précision: &a75%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack REINFORCED_ALLOY_JETBOOTS = new SlimefunItemStack("REINFORCED_ALLOY_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9Bottes-Jet &7- &eVI", "", LoreBuilder.material("Alliage renforcé"), LoreBuilder.powerCharged(0, 100), LoreBuilder.speed(0.6F), "&8\u21E8 &7Précision: &c80%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack CARBONADO_JETBOOTS = new SlimefunItemStack("CARBONADO_JETBOOTS", Material.LEATHER_BOOTS, Color.BLACK, "&9Bottes-Jet &7- &eVII", "", LoreBuilder.material("Carbonado"), LoreBuilder.powerCharged(0, 125), LoreBuilder.speed(0.7F), "&8\u21E8 &7Précision: &c99.9%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack ARMORED_JETBOOTS = new SlimefunItemStack("ARMORED_JETBOOTS", Material.IRON_BOOTS, "&9Bottes-Jet blindé", "", LoreBuilder.material("Acier"), LoreBuilder.powerCharged(0, 50), LoreBuilder.speed(0.45F), "&8\u21E8 &7Précision: &e70%", "", LoreBuilder.CROUCH_TO_USE);

    /* Multi Tools */
    private static final String MULTI_TOOL_LORE = "&eCrouch & Right Click&7 to switch modes";
    public static final SlimefunItemStack DURALUMIN_MULTI_TOOL = new SlimefunItemStack("DURALUMIN_MULTI_TOOL", Material.SHEARS, "&9Couteau-Suisse &7- &eI", "", LoreBuilder.material("Alliage d'alluminium léger"), LoreBuilder.powerCharged(0, 20), "", LoreBuilder.RIGHT_CLICK_TO_USE, MULTI_TOOL_LORE);
    public static final SlimefunItemStack SOLDER_MULTI_TOOL = new SlimefunItemStack("SOLDER_MULTI_TOOL", Material.SHEARS, "&9Couteau-Suisse &7- &eII", "", LoreBuilder.material("Soudure d'étain"), LoreBuilder.powerCharged(0, 30), "", LoreBuilder.RIGHT_CLICK_TO_USE, MULTI_TOOL_LORE);
    public static final SlimefunItemStack BILLON_MULTI_TOOL = new SlimefunItemStack("BILLON_MULTI_TOOL", Material.SHEARS, "&9Couteau-Suisse &7- &eIII", "", LoreBuilder.material("Billon"), LoreBuilder.powerCharged(0, 40), "", LoreBuilder.RIGHT_CLICK_TO_USE, MULTI_TOOL_LORE);
    public static final SlimefunItemStack STEEL_MULTI_TOOL = new SlimefunItemStack("STEEL_MULTI_TOOL", Material.SHEARS, "&9Couteau-Suisse &7- &eIV", "", LoreBuilder.material("Acier"), LoreBuilder.powerCharged(0, 50), "", LoreBuilder.RIGHT_CLICK_TO_USE, MULTI_TOOL_LORE);
    public static final SlimefunItemStack DAMASCUS_STEEL_MULTI_TOOL = new SlimefunItemStack("DAMASCUS_STEEL_MULTI_TOOL", Material.SHEARS, "&9Couteau-Suisse &7- &eV", "", LoreBuilder.material("Acier de Damas"), LoreBuilder.powerCharged(0, 60), "", LoreBuilder.RIGHT_CLICK_TO_USE, MULTI_TOOL_LORE);
    public static final SlimefunItemStack REINFORCED_ALLOY_MULTI_TOOL = new SlimefunItemStack("REINFORCED_ALLOY_MULTI_TOOL", Material.SHEARS, "&9Couteau-Suisse &7- &eVI", "", LoreBuilder.material("Acier renforcé"), LoreBuilder.powerCharged(0, 75), "", LoreBuilder.RIGHT_CLICK_TO_USE, MULTI_TOOL_LORE);
    public static final SlimefunItemStack CARBONADO_MULTI_TOOL = new SlimefunItemStack("CARBONADO_MULTI_TOOL", Material.SHEARS, "&9Couteau-Suisse &7- &eVII", "", LoreBuilder.material("Carbonado"), LoreBuilder.powerCharged(0, 100), "", LoreBuilder.RIGHT_CLICK_TO_USE, MULTI_TOOL_LORE);

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
    public static final SlimefunItemStack FORTUNE_COOKIE = new SlimefunItemStack("FORTUNE_COOKIE", Material.COOKIE, "&6Cookie de la fortune", "", "&a&oVous apprends des choses sur votre futur :o");
    public static final SlimefunItemStack DIET_COOKIE = new SlimefunItemStack("DIET_COOKIE", Material.COOKIE, "&6Cookie diététique", "", "&aUn cookie léger à la digestion");
    public static final SlimefunItemStack MAGIC_SUGAR = new SlimefunItemStack("MAGIC_SUGAR", Material.SUGAR, "&6Sucre magique", "", "&a&oRessentez le pouvoir d'Hermès!");
    public static final SlimefunItemStack MONSTER_JERKY = new SlimefunItemStack("MONSTER_JERKY", Material.ROTTEN_FLESH, "&6Morceau de monstre séché", "", "&a&oVous n'avez plus faim..");
    public static final SlimefunItemStack APPLE_JUICE = new SlimefunItemStack("APPLE_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 5, 0), "&cJus de pommes", "", LoreBuilder.hunger(3));
    public static final SlimefunItemStack MELON_JUICE = new SlimefunItemStack("MELON_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 5, 0), "&cJus de pastèques", "", LoreBuilder.hunger(3));
    public static final SlimefunItemStack CARROT_JUICE = new SlimefunItemStack("CARROT_JUICE", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 5, 0), "&6Jus de carottes", "", LoreBuilder.hunger(3));
    public static final SlimefunItemStack PUMPKIN_JUICE = new SlimefunItemStack("PUMPKIN_JUICE", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 5, 0), "&6Jus de citrouilles", "", LoreBuilder.hunger(3));
    public static final SlimefunItemStack SWEET_BERRY_JUICE = new SlimefunItemStack("SWEET_BERRY_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 5, 0), "&cJus de baies sucrées", "", LoreBuilder.hunger(3));
    public static final SlimefunItemStack GOLDEN_APPLE_JUICE = new SlimefunItemStack("GOLDEN_APPLE_JUICE", Color.YELLOW, new PotionEffect(PotionEffectType.ABSORPTION, 20 * 20, 0), "&bJus de pommes en or");

    public static final SlimefunItemStack BEEF_JERKY = new SlimefunItemStack("BEEF_JERKY", Material.COOKED_BEEF, "&6Boeuf séché", "", "&fSaturation bonus!");
    public static final SlimefunItemStack PORK_JERKY = new SlimefunItemStack("PORK_JERKY", Material.COOKED_PORKCHOP, "&6Porc séché", "", "&fSaturation bonus!");
    public static final SlimefunItemStack CHICKEN_JERKY = new SlimefunItemStack("CHICKEN_JERKY", Material.COOKED_CHICKEN, "&6Poulet séché", "", "&fSaturation bonus!");
    public static final SlimefunItemStack MUTTON_JERKY = new SlimefunItemStack("MUTTON_JERKY", Material.COOKED_MUTTON, "&6Mouton séché", "", "&fSaturation bonus!");
    public static final SlimefunItemStack RABBIT_JERKY = new SlimefunItemStack("RABBIT_JERKY", Material.COOKED_RABBIT, "&6Lapin séché", "", "&fSaturation bonus!");
    public static final SlimefunItemStack FISH_JERKY = new SlimefunItemStack("FISH_JERKY", Material.COOKED_COD, "&6Poisson séché", "", "&fSaturation bonus!");

    public static final SlimefunItemStack KELP_COOKIE = new SlimefunItemStack("KELP_COOKIE", Material.COOKIE, "&2Cookie aux algues");

    /* Christmas */
    public static final SlimefunItemStack CHRISTMAS_MILK = new SlimefunItemStack("CHRISTMAS_MILK", Color.WHITE, new PotionEffect(PotionEffectType.SATURATION, 4, 0), "&6Verre de lait", "", LoreBuilder.hunger(2.5));
    public static final SlimefunItemStack CHRISTMAS_CHOCOLATE_MILK = new SlimefunItemStack("CHRISTMAS_CHOCOLATE_MILK", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 11, 0), "&6Chocolat au lait", "", LoreBuilder.hunger(6));
    public static final SlimefunItemStack CHRISTMAS_EGG_NOG = new SlimefunItemStack("CHRISTMAS_EGG_NOG", Color.GRAY, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&aait de poule", "", LoreBuilder.hunger(3.5));
    public static final SlimefunItemStack CHRISTMAS_APPLE_CIDER = new SlimefunItemStack("CHRISTMAS_APPLE_CIDER", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 13, 0), "&cCidre", "", LoreBuilder.hunger(7));
    public static final SlimefunItemStack CHRISTMAS_COOKIE = new SlimefunItemStack("CHRISTMAS_COOKIE", Material.COOKIE, ChatUtils.christmas("Cookie de Noël"));
    public static final SlimefunItemStack CHRISTMAS_FRUIT_CAKE = new SlimefunItemStack("CHRISTMAS_FRUIT_CAKE", Material.PUMPKIN_PIE, ChatUtils.christmas("Gâteau aux fruits"));
    public static final SlimefunItemStack CHRISTMAS_APPLE_PIE = new SlimefunItemStack("CHRISTMAS_APPLE_PIE", Material.PUMPKIN_PIE, "&fTarte aux pommes");
    public static final SlimefunItemStack CHRISTMAS_HOT_CHOCOLATE = new SlimefunItemStack("CHRISTMAS_HOT_CHOCOLATE", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 13, 0), "&6Chocolat chaud", "", LoreBuilder.hunger(7));
    public static final SlimefunItemStack CHRISTMAS_CAKE = new SlimefunItemStack("CHRISTMAS_CAKE", Material.PUMPKIN_PIE, ChatUtils.christmas("Gâteau de Noël"));
    public static final SlimefunItemStack CHRISTMAS_CARAMEL = new SlimefunItemStack("CHRISTMAS_CARAMEL", Material.BRICK, "&6Caramel");
    public static final SlimefunItemStack CHRISTMAS_CARAMEL_APPLE = new SlimefunItemStack("CHRISTMAS_CARAMEL_APPLE", Material.APPLE, "&6Pomme au caramel");
    public static final SlimefunItemStack CHRISTMAS_CHOCOLATE_APPLE = new SlimefunItemStack("CHRISTMAS_CHOCOLATE_APPLE", Material.APPLE, "&6Pomme au chocolat");
    public static final SlimefunItemStack CHRISTMAS_PRESENT = new SlimefunItemStack("CHRISTMAS_PRESENT", HeadTexture.CHRISTMAS_PRESENT, ChatUtils.christmas("Cadeau de Noël"), "&7De: &cMelonyahh", "&7Pour: &eToi :D", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);

    /* Easter */
    public static final SlimefunItemStack EASTER_EGG = new SlimefunItemStack("EASTER_EGG", HeadTexture.EASTER_EGG, "&fŒuf de Pâques", "&dJoyeuse pâque! Faîtes une surprise.", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack EASTER_CARROT_PIE = new SlimefunItemStack("CARROT_PIE", Material.PUMPKIN_PIE, "&6Tarte à la carotte");
    public static final SlimefunItemStack EASTER_APPLE_PIE = new SlimefunItemStack("EASTER_APPLE_PIE", Material.PUMPKIN_PIE, "&fTarte aux pommes");

    /* Weapons */
    public static final SlimefunItemStack GRANDMAS_WALKING_STICK = new SlimefunItemStack("GRANDMAS_WALKING_STICK", Material.STICK, "&7Bâton de marche de grand-mère");
    public static final SlimefunItemStack GRANDPAS_WALKING_STICK = new SlimefunItemStack("GRANDPAS_WALKING_STICK", Material.STICK, "&7Bâton de marche de grand-père");
    public static final SlimefunItemStack SWORD_OF_BEHEADING = new SlimefunItemStack("SWORD_OF_BEHEADING", Material.IRON_SWORD, "&6Épée de décapitation", "&7Décapitation II", "", "&fVous donne une chance de décapiter les monstres", "&f(Y compris pour les Withers Squelettes)");
    public static final SlimefunItemStack BLADE_OF_VAMPIRES = new SlimefunItemStack("BLADE_OF_VAMPIRES", Material.GOLDEN_SWORD, "&cLame des Vampires", "&7Vol de vie I", "", "&fA chaque fois que vous attaquez quelque chose", "&fvous obtenez 45% de chance de", "&frécupérer 2 Coeurs de vie");
    public static final SlimefunItemStack SEISMIC_AXE = new SlimefunItemStack("SEISMIC_AXE", Material.IRON_AXE, "&aHache sismique", "", "&7&oUn tremblement de terre portable...", "", LoreBuilder.RIGHT_CLICK_TO_USE);

    static {
        GRANDMAS_WALKING_STICK.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        GRANDPAS_WALKING_STICK.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
    }

    /* Bows */
    public static final SlimefunItemStack EXPLOSIVE_BOW = new SlimefunItemStack("EXPLOSIVE_BOW", Material.BOW, "&cArc explosif", "&fToutes les flèches tirées avec cet arc", "&fenverra en l'air les ennemis touchés");
    public static final SlimefunItemStack ICY_BOW = new SlimefunItemStack("ICY_BOW", Material.BOW, "&bArc de glace", "&fToutes les flèches tirées avec cet arc", "&fimmobilisera les adversaires", "&fpendant 2 secondes");

    /* Tools */
    public static final SlimefunItemStack SMELTERS_PICKAXE = new SlimefunItemStack("SMELTERS_PICKAXE", Material.DIAMOND_PICKAXE, "&6Fon-Pioche", "&c&lUne pioche qui transforme en lingots !", "", "&9Fonctionne avec Fortune également !");
    public static final SlimefunItemStack LUMBER_AXE = new SlimefunItemStack("LUMBER_AXE", Material.DIAMOND_AXE, "&6Hache du bûcheron", "&a&oCoupe l'arbre en entier...");
    public static final SlimefunItemStack PICKAXE_OF_CONTAINMENT = new SlimefunItemStack("PICKAXE_OF_CONTAINMENT", Material.IRON_PICKAXE, "&cPioche du confinement", "", "&9Permet de récupérer les Générateurs de monstres");
    public static final SlimefunItemStack HERCULES_PICKAXE = new SlimefunItemStack("HERCULES_PICKAXE", Material.IRON_PICKAXE, "&9Pioche d'Hercules", "", "&fTellement puissante qu'elle broie", "&fles minerais en poudres...");
    public static final SlimefunItemStack EXPLOSIVE_PICKAXE = new SlimefunItemStack("EXPLOSIVE_PICKAXE", Material.DIAMOND_PICKAXE, "&ePioche explosive", "", "&fPermet de miner des zones", "&fde blocs en une fois...", "", "&9Fonctionne avec Fortune également !");
    public static final SlimefunItemStack EXPLOSIVE_SHOVEL = new SlimefunItemStack("EXPLOSIVE_SHOVEL", Material.DIAMOND_SHOVEL, "&ePelle explosive", "", "&fPermet d'extraire des zones", "&fde blocs en une fois...");
    public static final SlimefunItemStack PICKAXE_OF_THE_SEEKER = new SlimefunItemStack("PICKAXE_OF_THE_SEEKER", Material.DIAMOND_PICKAXE, "&aPioche du chercheur", "&fVous guidera jusqu'au minerai le plus proche", "&fmais s'endommagera avec ce pouvoir", "", "&7&eClic-droit&7 pour trouver le minerai le plus proche");
    public static final SlimefunItemStack COBALT_PICKAXE = new SlimefunItemStack("COBALT_PICKAXE", Material.IRON_PICKAXE, "&9Pioche de cobalt");
    public static final SlimefunItemStack PICKAXE_OF_VEIN_MINING = new SlimefunItemStack("PICKAXE_OF_VEIN_MINING", Material.DIAMOND_PICKAXE, "&ePioche de minage de veines", "", "&fCette pioche a la capacité", "&fde miner des veines entières de minerais...");
    public static final SlimefunItemStack CLIMBING_PICK = new SlimefunItemStack("CLIMBING_PICK", Material.IRON_PICKAXE, "&bCoinceurs à mousquetons", "", "&fVous permet d'escalader certaines surfaces", "&fen faisant clic-droit.", "&fEnchantez les pour", "&fgrimper plus vite!");

    static {
        HERCULES_PICKAXE.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        HERCULES_PICKAXE.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);

        COBALT_PICKAXE.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        COBALT_PICKAXE.addUnsafeEnchantment(Enchantment.DIG_SPEED, 6);
    }

    /* Armor */
    public static final SlimefunItemStack GLOWSTONE_HELMET = new SlimefunItemStack("GLOWSTONE_HELMET", Material.LEATHER_HELMET, Color.YELLOW, "&e&lCasque de pierres lumineuses", "", "&a&oIl brille comme le soleil !", "", "&9+ Vision Nocturne");
    public static final SlimefunItemStack GLOWSTONE_CHESTPLATE = new SlimefunItemStack("GLOWSTONE_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.YELLOW, "&e&lPlastron de pierres lumineuses", "", "&a&oIl brille comme le soleil !", "", "&9+ Vision Nocturne");
    public static final SlimefunItemStack GLOWSTONE_LEGGINGS = new SlimefunItemStack("GLOWSTONE_LEGGINGS", Material.LEATHER_LEGGINGS, Color.YELLOW, "&e&lJambières de pierres lumineuses", "", "&a&oElles brillent comme le soleil !", "", "&9+ Vision Nocturne");
    public static final SlimefunItemStack GLOWSTONE_BOOTS = new SlimefunItemStack("GLOWSTONE_BOOTS", Material.LEATHER_BOOTS, Color.YELLOW, "&e&lBottes de pierres lumineuses", "", "&a&oElles brillent comme le soleil !", "", "&9+ Vision Nocturne");

    public static final SlimefunItemStack ENDER_HELMET = new SlimefunItemStack("ENDER_HELMET", Material.LEATHER_HELMET, Color.fromRGB(28, 25, 112), "&5&lCasque du Néant", "", "&a&oParfois ici, parfois là..");
    public static final SlimefunItemStack ENDER_CHESTPLATE = new SlimefunItemStack("ENDER_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.fromRGB(28, 25, 112), "&5&lPlastron du Néant", "", "&a&oParfois ici, parfois là..");
    public static final SlimefunItemStack ENDER_LEGGINGS = new SlimefunItemStack("ENDER_LEGGINGS", Material.LEATHER_LEGGINGS, Color.fromRGB(28, 25, 112), "&5&lJambières du Néant", "", "&a&oParfois ici, parfois là..");
    public static final SlimefunItemStack ENDER_BOOTS = new SlimefunItemStack("ENDER_BOOTS", Material.LEATHER_BOOTS, Color.fromRGB(28, 25, 112), "&5&lBottes du Néant", "", "&a&oParfois ici, parfois là..", "", "&9+ Pas de dégâts des Perles du Néant");

    public static final SlimefunItemStack SLIME_HELMET = new SlimefunItemStack("SLIME_HELMET", Material.LEATHER_HELMET, Color.LIME, "&a&lCasque de slime", "", "&a&oComme une sensation de rebondissement..");
    public static final SlimefunItemStack SLIME_CHESTPLATE = new SlimefunItemStack("SLIME_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.LIME, "&a&lPlastron de slime", "", "&a&oComme une sensation de rebondissement..");
    public static final SlimefunItemStack SLIME_LEGGINGS = new SlimefunItemStack("SLIME_LEGGINGS", Material.LEATHER_LEGGINGS, Color.LIME, "&a&lJambières de slime", "", "&a&oComme une sensation de rebondissement..", "", "&9+ Vitesse");
    public static final SlimefunItemStack SLIME_BOOTS = new SlimefunItemStack("SLIME_BOOTS", Material.LEATHER_BOOTS, Color.LIME, "&a&lBottes de slime", "", "&a&oComme une sensation de rebondissement..", "", "&9+ Boosteur de saut", "&9+ Pas de dégâts de chutes");

    public static final SlimefunItemStack CACTUS_HELMET = new SlimefunItemStack("CACTUS_HELMET", Material.LEATHER_HELMET, Color.GREEN, "&2Casque en cactus");
    public static final SlimefunItemStack CACTUS_CHESTPLATE = new SlimefunItemStack("CACTUS_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.GREEN, "&2Plastron en cactus");
    public static final SlimefunItemStack CACTUS_LEGGINGS = new SlimefunItemStack("CACTUS_LEGGINGS", Material.LEATHER_LEGGINGS, Color.GREEN, "&2Jambières en cactus");
    public static final SlimefunItemStack CACTUS_BOOTS = new SlimefunItemStack("CACTUS_BOOTS", Material.LEATHER_BOOTS, Color.GREEN, "&2Bottes en cactus");

    public static final SlimefunItemStack DAMASCUS_STEEL_HELMET = new SlimefunItemStack("DAMASCUS_STEEL_HELMET", Material.IRON_HELMET, "&7Casque en acier de damas");
    public static final SlimefunItemStack DAMASCUS_STEEL_CHESTPLATE = new SlimefunItemStack("DAMASCUS_STEEL_CHESTPLATE", Material.IRON_CHESTPLATE, "&7Plastron en acier de damas");
    public static final SlimefunItemStack DAMASCUS_STEEL_LEGGINGS = new SlimefunItemStack("DAMASCUS_STEEL_LEGGINGS", Material.IRON_LEGGINGS, "&7Jambières en acier de damas");
    public static final SlimefunItemStack DAMASCUS_STEEL_BOOTS = new SlimefunItemStack("DAMASCUS_STEEL_BOOTS", Material.IRON_BOOTS, "&7Bottes en acier de damas");

    public static final SlimefunItemStack REINFORCED_ALLOY_HELMET = new SlimefunItemStack("REINFORCED_ALLOY_HELMET", Material.IRON_HELMET, "&bCasque renforcé");
    public static final SlimefunItemStack REINFORCED_ALLOY_CHESTPLATE = new SlimefunItemStack("REINFORCED_ALLOY_CHESTPLATE", Material.IRON_CHESTPLATE, "&bPlastron renforcé");
    public static final SlimefunItemStack REINFORCED_ALLOY_LEGGINGS = new SlimefunItemStack("REINFORCED_ALLOY_LEGGINGS", Material.IRON_LEGGINGS, "&bJambières renforcés");
    public static final SlimefunItemStack REINFORCED_ALLOY_BOOTS = new SlimefunItemStack("REINFORCED_ALLOY_BOOTS", Material.IRON_BOOTS, "&bBottes renforcés");

    private static final List<String> hazmatLore = new ArrayList<>();

    static {
        hazmatLore.add("");
        hazmatLore.add(ChatColor.GOLD + "Effets de tenue complète:");
        hazmatLore.add(ChatColor.YELLOW + "- Immunité aux radiations");

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_15)) {
            hazmatLore.add(ChatColor.YELLOW + "- Protection contre les piqûres de guèpes");
        }
    }

    public static final SlimefunItemStack SCUBA_HELMET = new SlimefunItemStack("SCUBA_HELMET", Material.LEATHER_HELMET, Color.ORANGE, "&cMasque étanche", "", "&7Vous permet de respirer sous l'eau");
    public static final SlimefunItemStack HAZMAT_CHESTPLATE = new SlimefunItemStack("HAZMAT_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.ORANGE, "&cTenue thermique", "", "&7Vous permet de marcher dans l'eau et dans la lave sans risques");
    public static final SlimefunItemStack HAZMAT_LEGGINGS = new SlimefunItemStack("HAZMAT_LEGGINGS", Material.LEATHER_LEGGINGS, Color.ORANGE, "&cPantalon type Hazmat", hazmatLore.toArray(new String[0]));
    public static final SlimefunItemStack HAZMAT_BOOTS = new SlimefunItemStack("RUBBER_BOOTS", Material.LEATHER_BOOTS, Color.BLACK, "&cBottes anti-corosions", hazmatLore.toArray(new String[0]));

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

    public static final SlimefunItemStack GILDED_IRON_HELMET = new SlimefunItemStack("GILDED_IRON_HELMET", Material.GOLDEN_HELMET, "&6Casque en fer nordique");
    public static final SlimefunItemStack GILDED_IRON_CHESTPLATE = new SlimefunItemStack("GILDED_IRON_CHESTPLATE", Material.GOLDEN_CHESTPLATE, "&6Plastron en fer nordique");
    public static final SlimefunItemStack GILDED_IRON_LEGGINGS = new SlimefunItemStack("GILDED_IRON_LEGGINGS", Material.GOLDEN_LEGGINGS, "&6Jambières en fer nordique");
    public static final SlimefunItemStack GILDED_IRON_BOOTS = new SlimefunItemStack("GILDED_IRON_BOOTS", Material.GOLDEN_BOOTS, "&6Bottes en fer nordique");

    public static final SlimefunItemStack GOLDEN_HELMET_12K = new SlimefunItemStack("GOLD_12K_HELMET", Material.GOLDEN_HELMET, "&6Casque en or &7(12-Carats)");
    public static final SlimefunItemStack GOLDEN_CHESTPLATE_12K = new SlimefunItemStack("GOLD_12K_CHESTPLATE", Material.GOLDEN_CHESTPLATE, "&6Plastron en or &7(12-Carats)");
    public static final SlimefunItemStack GOLDEN_LEGGINGS_12K = new SlimefunItemStack("GOLD_12K_LEGGINGS", Material.GOLDEN_LEGGINGS, "&6Jambières en or &7(12-Carats)");
    public static final SlimefunItemStack GOLDEN_BOOTS_12K = new SlimefunItemStack("GOLD_12K_BOOTS", Material.GOLDEN_BOOTS, "&6Bottes en or &7(12-Carats)");

    public static final SlimefunItemStack SLIME_HELMET_STEEL = new SlimefunItemStack("SLIME_STEEL_HELMET", Material.IRON_HELMET, "&a&lCasque en slime", "&7&oReinforced", "", "&a&oComme une sensation de rebondissement..");
    public static final SlimefunItemStack SLIME_CHESTPLATE_STEEL = new SlimefunItemStack("SLIME_STEEL_CHESTPLATE", Material.IRON_CHESTPLATE, "&a&lPlastron en slime", "&7&oRenforcé", "", "&a&oComme une sensation de rebondissement..");
    public static final SlimefunItemStack SLIME_LEGGINGS_STEEL = new SlimefunItemStack("SLIME_STEEL_LEGGINGS", Material.IRON_LEGGINGS, "&a&lJambières en slime", "&7&oRenforcé", "", "&a&oComme une sensation de rebondissement..", "", "&9+ Vitesse");
    public static final SlimefunItemStack SLIME_BOOTS_STEEL = new SlimefunItemStack("SLIME_STEEL_BOOTS", Material.IRON_BOOTS, "&a&lBottes en slime", "&7&oRenforcé", "", "&a&oComme une sensation de rebondissement..", "", "&9+ Boosteur de saut", "&9+ Pas de dégâts de chutes");

    public static final SlimefunItemStack BOOTS_OF_THE_STOMPER = new SlimefunItemStack("BOOTS_OF_THE_STOMPER", Material.LEATHER_BOOTS, Color.AQUA, "&bBottes de l'écraseur", "", "&9Tout les dégâts de chutes que vous devez recevoir", "&9seront appliquer aux joueurs/monstres à proximités", "", "&9+ Pas de dégâts de chutes");

    public static final SlimefunItemStack BEE_HELMET = new SlimefunItemStack("BEE_HELMET", Material.GOLDEN_HELMET, "&e&lCasque d'abeilles", " ", "&fBzzzzzzz");
    public static final SlimefunItemStack BEE_WINGS = new SlimefunItemStack("BEE_WINGS", Material.ELYTRA, "&e&lAiles d'abeilles", " ", "&fBzzzzzzz", " ", "&9Ralenti votre chute", "&9lorsque vous approchez du sol");
    public static final SlimefunItemStack BEE_LEGGINGS = new SlimefunItemStack("BEE_LEGGINGS", Material.GOLDEN_LEGGINGS, "&e&lPantalon d'abeilles", " ", "&fBzzzzzzz");
    public static final SlimefunItemStack BEE_BOOTS = new SlimefunItemStack("BEE_BOOTS", Material.GOLDEN_BOOTS, "&e&lBottes d'abeilles", "", "&fBzzzzzzz", "", "&9+ Boosteur de saut", "&9+ Pas de dégâts de chutes");

    static {
        Map<Enchantment, Integer> cactusEnchs = new HashMap<>();
        cactusEnchs.put(Enchantment.THORNS, 3);
        cactusEnchs.put(Enchantment.DURABILITY, 6);

        CACTUS_HELMET.addUnsafeEnchantments(cactusEnchs);
        CACTUS_CHESTPLATE.addUnsafeEnchantments(cactusEnchs);
        CACTUS_LEGGINGS.addUnsafeEnchantments(cactusEnchs);
        CACTUS_BOOTS.addUnsafeEnchantments(cactusEnchs);

        Map<Enchantment, Integer> damascusEnchs = new HashMap<>();
        damascusEnchs.put(Enchantment.DURABILITY, 5);
        damascusEnchs.put(Enchantment.PROTECTION_ENVIRONMENTAL, 5);

        DAMASCUS_STEEL_HELMET.addUnsafeEnchantments(damascusEnchs);
        DAMASCUS_STEEL_CHESTPLATE.addUnsafeEnchantments(damascusEnchs);
        DAMASCUS_STEEL_LEGGINGS.addUnsafeEnchantments(damascusEnchs);
        DAMASCUS_STEEL_BOOTS.addUnsafeEnchantments(damascusEnchs);

        Map<Enchantment, Integer> reinforcedEnchs = new HashMap<>();
        reinforcedEnchs.put(Enchantment.DURABILITY, 9);
        reinforcedEnchs.put(Enchantment.PROTECTION_ENVIRONMENTAL, 9);

        REINFORCED_ALLOY_HELMET.addUnsafeEnchantments(reinforcedEnchs);
        REINFORCED_ALLOY_CHESTPLATE.addUnsafeEnchantments(reinforcedEnchs);
        REINFORCED_ALLOY_LEGGINGS.addUnsafeEnchantments(reinforcedEnchs);
        REINFORCED_ALLOY_BOOTS.addUnsafeEnchantments(reinforcedEnchs);

        Map<Enchantment, Integer> gildedEnchs = new HashMap<>();
        gildedEnchs.put(Enchantment.DURABILITY, 6);
        gildedEnchs.put(Enchantment.PROTECTION_ENVIRONMENTAL, 8);

        GILDED_IRON_HELMET.addUnsafeEnchantments(gildedEnchs);
        GILDED_IRON_CHESTPLATE.addUnsafeEnchantments(gildedEnchs);
        GILDED_IRON_LEGGINGS.addUnsafeEnchantments(gildedEnchs);
        GILDED_IRON_BOOTS.addUnsafeEnchantments(gildedEnchs);

        GOLDEN_HELMET_12K.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        GOLDEN_CHESTPLATE_12K.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        GOLDEN_LEGGINGS_12K.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        GOLDEN_BOOTS_12K.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        Map<Enchantment, Integer> slimeEnchs = new HashMap<>();
        slimeEnchs.put(Enchantment.DURABILITY, 4);
        slimeEnchs.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        SLIME_HELMET_STEEL.addUnsafeEnchantments(slimeEnchs);
        SLIME_CHESTPLATE_STEEL.addUnsafeEnchantments(slimeEnchs);
        SLIME_LEGGINGS_STEEL.addUnsafeEnchantments(slimeEnchs);
        SLIME_BOOTS_STEEL.addUnsafeEnchantments(slimeEnchs);

        Map<Enchantment, Integer> beeEnchs = new HashMap<>();
        beeEnchs.put(Enchantment.DURABILITY, 4);
        beeEnchs.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        BEE_HELMET.addUnsafeEnchantments(beeEnchs);
        BEE_WINGS.addUnsafeEnchantments(beeEnchs);
        BEE_LEGGINGS.addUnsafeEnchantments(beeEnchs);
        BEE_BOOTS.addUnsafeEnchantments(beeEnchs);
    }

    /* Magical components */
    public static final SlimefunItemStack MAGIC_LUMP_1 = new SlimefunItemStack("MAGIC_LUMP_1", Material.GOLD_NUGGET, "&6Boule magique &7- &eI", "", "&c&oTier: I");
    public static final SlimefunItemStack MAGIC_LUMP_2 = new SlimefunItemStack("MAGIC_LUMP_2", Material.GOLD_NUGGET, "&6Boule magique &7- &eII", "", "&c&oTier: II");
    public static final SlimefunItemStack MAGIC_LUMP_3 = new SlimefunItemStack("MAGIC_LUMP_3", Material.GOLD_NUGGET, "&6Boule magique &7- &eIII", "", "&c&oTier: III");
    public static final SlimefunItemStack ENDER_LUMP_1 = new SlimefunItemStack("ENDER_LUMP_1", Material.GOLD_NUGGET, "&5Boule du Néant &7- &eI", "", "&c&oTier: I");
    public static final SlimefunItemStack ENDER_LUMP_2 = new SlimefunItemStack("ENDER_LUMP_2", Material.GOLD_NUGGET, "&5Boule du Néant &7- &eII", "", "&c&oTier: II");
    public static final SlimefunItemStack ENDER_LUMP_3 = new SlimefunItemStack("ENDER_LUMP_3", Material.GOLD_NUGGET, "&5Boule du Néant &7- &eIII", "", "&c&oTier: III");
    public static final SlimefunItemStack MAGICAL_BOOK_COVER = new SlimefunItemStack("MAGICAL_BOOK_COVER", Material.PAPER, "&6Couverture de livre magique", "", "&a&oUtilisé pour divers livres de magie");
    public static final SlimefunItemStack MAGICAL_GLASS = new SlimefunItemStack("MAGICAL_GLASS", Material.GLASS_PANE, "&6Verre magique", "", "&a&oUtilisé pour divers gadgets magiques");
    public static final SlimefunItemStack SYNTHETIC_SHULKER_SHELL = new SlimefunItemStack("SYNTHETIC_SHULKER_SHELL", Material.SHULKER_SHELL, "&dCoquille Synthétique de Shulkers", "", "&fPeut être utilisé dans un", "&fétabli comme une coquille de Shulker normale");

    /* Technical components */
    public static final SlimefunItemStack BASIC_CIRCUIT_BOARD = new SlimefunItemStack("BASIC_CIRCUIT_BOARD", Material.ACTIVATOR_RAIL, "&bCircuit imprimé de base");
    public static final SlimefunItemStack ADVANCED_CIRCUIT_BOARD = new SlimefunItemStack("ADVANCED_CIRCUIT_BOARD", Material.POWERED_RAIL, "&bCircuit imprimé avancé");
    public static final SlimefunItemStack WHEAT_FLOUR = new SlimefunItemStack("WHEAT_FLOUR", Material.SUGAR, "&fFarine de blé");
    public static final SlimefunItemStack STEEL_PLATE = new SlimefunItemStack("STEEL_PLATE", Material.PAPER, "&7&lPlaque d'acier");
    public static final SlimefunItemStack BATTERY = new SlimefunItemStack("BATTERY", HeadTexture.BATTERY, "&6Batterie");
    public static final SlimefunItemStack CARBON = new SlimefunItemStack("CARBON", HeadTexture.CARBON, "&eCarbone");
    public static final SlimefunItemStack COMPRESSED_CARBON = new SlimefunItemStack("COMPRESSED_CARBON", HeadTexture.COMPRESSED_CARBON, "&cCarbone compressé");
    public static final SlimefunItemStack CARBON_CHUNK = new SlimefunItemStack("CARBON_CHUNK", HeadTexture.COMPRESSED_CARBON, "&4Morceau de carbone");
    public static final SlimefunItemStack STEEL_THRUSTER = new SlimefunItemStack("STEEL_THRUSTER", Material.BUCKET, "&7&lPropulseur en acier");
    public static final SlimefunItemStack POWER_CRYSTAL = new SlimefunItemStack("POWER_CRYSTAL", HeadTexture.POWER_CRYSTAL, "&c&lCristal de puissance");
    public static final SlimefunItemStack CHAIN = new SlimefunItemStack("CHAIN", Material.STRING, "&bChaîne");
        public static final SlimefunItemStack HOOK = new SlimefunItemStack("HOOK", Material.FLINT, "&bCrochet");
    public static final SlimefunItemStack SIFTED_ORE = new SlimefunItemStack("SIFTED_ORE", Material.GUNPOWDER, "&6Minerai taminé");
    public static final SlimefunItemStack STONE_CHUNK = new SlimefunItemStack("STONE_CHUNK", HeadTexture.STONE_CHUNK, "&6Morceau de pierre");
    public static final SlimefunItemStack LAVA_CRYSTAL = new SlimefunItemStack("LAVA_CRYSTAL", HeadTexture.LAVA_CRYSTAL, "&4Cristal de lave");
    public static final SlimefunItemStack SALT = new SlimefunItemStack("SALT", Material.SUGAR, "&fSel");
    public static final SlimefunItemStack CHEESE = new SlimefunItemStack("CHEESE", HeadTexture.CHEESE, "&fFromage");
    public static final SlimefunItemStack BUTTER = new SlimefunItemStack("BUTTER", HeadTexture.BUTTER, "&fBeurre");
    public static final SlimefunItemStack DUCT_TAPE = new SlimefunItemStack("DUCT_TAPE", HeadTexture.DUCT_TAPE, "&8Ruban adhésif", "", "&fVous pouvez réparer les objets avec", "&fle ruban dans une enclume automatique");
    public static final SlimefunItemStack HEAVY_CREAM = new SlimefunItemStack("HEAVY_CREAM", Material.SNOWBALL, "&fCrème épaisse");
    public static final SlimefunItemStack CRUSHED_ORE = new SlimefunItemStack("CRUSHED_ORE", Material.GUNPOWDER, "&6Minerai broyé");
    public static final SlimefunItemStack PULVERIZED_ORE = new SlimefunItemStack("PULVERIZED_ORE", Material.GUNPOWDER, "&6Minerai pulvérisé");
    public static final SlimefunItemStack PURE_ORE_CLUSTER = new SlimefunItemStack("PURE_ORE_CLUSTER", Material.GUNPOWDER, "&6Amas de minerais purs");
    public static final SlimefunItemStack SMALL_URANIUM = new SlimefunItemStack("SMALL_URANIUM", HeadTexture.URANIUM, "&cPetit tas d'uranium", "", LoreBuilder.radioactive(Radioactivity.MODERATE), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final SlimefunItemStack TINY_URANIUM = new SlimefunItemStack("TINY_URANIUM", HeadTexture.URANIUM, "&cMinuscule tas d'uranium", "", LoreBuilder.radioactive(Radioactivity.LOW));
    public static final SlimefunItemStack SOLAR_PANEL = new SlimefunItemStack("SOLAR_PANEL", Material.DAYLIGHT_DETECTOR, "&9Cellule photovoltaïque", "", "&7Composant important pour la", "&7fabrication d'un &bgénéréteur solair");
    public static final SlimefunItemStack PLASTIC_SHEET = new SlimefunItemStack("PLASTIC_SHEET", Material.PAPER, "&fFeuille de plastique");

    public static final SlimefunItemStack MAGNET = new SlimefunItemStack("MAGNET", HeadTexture.MAGNET, "&cAimant");
    public static final SlimefunItemStack NECROTIC_SKULL = new SlimefunItemStack("NECROTIC_SKULL", HeadTexture.NECROTIC_SKULL, "&cCrâne nécrosé");
    public static final SlimefunItemStack ESSENCE_OF_AFTERLIFE = new SlimefunItemStack("ESSENCE_OF_AFTERLIFE", Material.GUNPOWDER, "&4Essence de l'au-delà");
    public static final SlimefunItemStack STRANGE_NETHER_GOO = new SlimefunItemStack("STRANGE_NETHER_GOO", Material.PURPLE_DYE, "&5Étrange substance des Tréfonds", "", "&fUne étrange matière bio-organique", "&fouvant être obtenu par le truc", "&favec les Piglins");
    public static final SlimefunItemStack ELECTRO_MAGNET = new SlimefunItemStack("ELECTRO_MAGNET", HeadTexture.MAGNET, "&cÉléctroaimant");
    public static final SlimefunItemStack HEATING_COIL = new SlimefunItemStack("HEATING_COIL", HeadTexture.HEATING_COIL, "&cBobine de chauffage");
    public static final SlimefunItemStack COOLING_UNIT = new SlimefunItemStack("COOLING_UNIT", HeadTexture.COOLING_UNIT, "&bUnité de refroidissement");
    public static final SlimefunItemStack ELECTRIC_MOTOR = new SlimefunItemStack("ELECTRIC_MOTOR", HeadTexture.MOTOR, "&cMoteur électrique");
    public static final SlimefunItemStack CARGO_MOTOR = new SlimefunItemStack("CARGO_MOTOR", HeadTexture.CARGO_MOTOR, "&3Moteur cargo", "", "&7Composant important des objets liés", "&7à la gestion des cargos");
    public static final SlimefunItemStack SCROLL_OF_DIMENSIONAL_TELEPOSITION = new SlimefunItemStack("SCROLL_OF_DIMENSIONAL_TELEPOSITION", Material.PAPER, "&6Parchemin de téléposition dimensionnelle", "", "&cCe parchemin est capable de créer", "&cun trou noir temporaire qui attire les ", "&centités voisines et les envoie dans", "&cune autre dimension où tout est ", "&cinversé : fait tourner les Entités de 180 degrés");
    public static final SlimefunItemStack TOME_OF_KNOWLEDGE_SHARING = new SlimefunItemStack("TOME_OF_KNOWLEDGE_SHARING", Material.ENCHANTED_BOOK, "&6Tome du partage des connaissances", "&7Propriétaire: &bAucun", "", "&eClic-droit&7 pour lier ce tome à vous-même", "", "", "&eClic-droit&7 pour acquérir toutes les recherches", "&7du propriétaire du tome");
    public static final SlimefunItemStack HARDENED_GLASS = new SlimefunItemStack("HARDENED_GLASS", Material.LIGHT_GRAY_STAINED_GLASS, "&7Verre renforcé", "", "&fRésistant aux explosions");
    public static final SlimefunItemStack WITHER_PROOF_OBSIDIAN = new SlimefunItemStack("WITHER_PROOF_OBSIDIAN", Material.OBSIDIAN, "&5Obsidienne anti-wither", "", "&fRésistant aux explosions", "&fRésistant aux Withers");
    public static final SlimefunItemStack WITHER_PROOF_GLASS = new SlimefunItemStack("WITHER_PROOF_GLASS", Material.PURPLE_STAINED_GLASS, "&5Verre anti-wither", "", "&fRésistant aux explosions", "&fRésistant aux Withers");
    public static final SlimefunItemStack REINFORCED_PLATE = new SlimefunItemStack("REINFORCED_PLATE", Material.PAPER, "&7Plaque renforcée");
    public static final SlimefunItemStack ANCIENT_PEDESTAL = new SlimefunItemStack("ANCIENT_PEDESTAL", Material.DISPENSER, "&dPiédestal ancien", "", "&5Élement de l'Ancien Altar");
    public static final SlimefunItemStack ANCIENT_ALTAR = new SlimefunItemStack("ANCIENT_ALTAR", Material.ENCHANTING_TABLE, "&dAutel Ancien", "", "&5Autel multi-bloc", "&5pour la fabrication d'objets magiques");
    public static final SlimefunItemStack COPPER_WIRE = new SlimefunItemStack("COPPER_WIRE", Material.STRING, "&6Câble en cuivre", "", "&6Composant primordial pour la fabrication", "&6d'objets électriques");
    public static final SlimefunItemStack CRAFTING_MOTOR = new SlimefunItemStack("CRAFTING_MOTOR", HeadTexture.CRAFTING_MOTOR, "&6Moteur de fabrication", "", "&7Composant important de l'auto-fabricateur");

    /* Rainbow blocks */
    private static final String RAINBOW = "&dToutes les couleurs de l'arc-en-ciel!";
    public static final SlimefunItemStack RAINBOW_WOOL = new SlimefunItemStack("RAINBOW_WOOL", Material.WHITE_WOOL, "&5Laine arc-en-ciel", "", RAINBOW);
    public static final SlimefunItemStack RAINBOW_GLASS = new SlimefunItemStack("RAINBOW_GLASS", Material.WHITE_STAINED_GLASS, "&5Verre arc-en-ciel", "", RAINBOW);
    public static final SlimefunItemStack RAINBOW_CLAY = new SlimefunItemStack("RAINBOW_CLAY", Material.WHITE_TERRACOTTA, "&5Argile arc-en-ciel", "", RAINBOW);
    public static final SlimefunItemStack RAINBOW_GLASS_PANE = new SlimefunItemStack("RAINBOW_GLASS_PANE", Material.WHITE_STAINED_GLASS_PANE, "&5Vitre arc-en-ciel", "", RAINBOW);
    public static final SlimefunItemStack RAINBOW_CONCRETE = new SlimefunItemStack("RAINBOW_CONCRETE", Material.WHITE_CONCRETE, "&5Béton arc-en-ciel", "", RAINBOW);
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA", Material.WHITE_GLAZED_TERRACOTTA, "&5Terracotta arc-en-ciel", "", RAINBOW);

    /* Seasonal */
    private static final String CHRISTMAS = ChatUtils.christmas("[Édition de Noël]");
    public static final SlimefunItemStack RAINBOW_WOOL_XMAS = new SlimefunItemStack("RAINBOW_WOOL_XMAS", Material.WHITE_WOOL, "&5Laine arc-en-ciel &7(Noël)", "", CHRISTMAS);
    public static final SlimefunItemStack RAINBOW_GLASS_XMAS = new SlimefunItemStack("RAINBOW_GLASS_XMAS", Material.WHITE_STAINED_GLASS, "&5Verre arc-en-ciel &7(Noël)", "", CHRISTMAS);
    public static final SlimefunItemStack RAINBOW_CLAY_XMAS = new SlimefunItemStack("RAINBOW_CLAY_XMAS", Material.WHITE_TERRACOTTA, "&5Argile arc-en-ciel &7(Noël)", "", CHRISTMAS);
    public static final SlimefunItemStack RAINBOW_GLASS_PANE_XMAS = new SlimefunItemStack("RAINBOW_GLASS_PANE_XMAS", Material.WHITE_STAINED_GLASS_PANE, "&5Vitre arc-en-ciel &7(Noël)", "", CHRISTMAS);
    public static final SlimefunItemStack RAINBOW_CONCRETE_XMAS = new SlimefunItemStack("RAINBOW_CONCRETE_XMAS", Material.WHITE_CONCRETE, "&5Béton arc-en-ciel &7(Noël)", "", CHRISTMAS);
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA_XMAS = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_XMAS", Material.WHITE_GLAZED_TERRACOTTA, "&5Terracotta arc-en-ciel &7(Noël)", "", CHRISTMAS);

    private static final String VALENTINES_DAY = "&5[&dÉdition de la Saint-Valentin&5]";
    public static final SlimefunItemStack RAINBOW_WOOL_VALENTINE = new SlimefunItemStack("RAINBOW_WOOL_VALENTINE", Material.PINK_WOOL, "&5Laine arc-en-ciel &7(Saint-Valentin)", "", VALENTINES_DAY);
    public static final SlimefunItemStack RAINBOW_GLASS_VALENTINE = new SlimefunItemStack("RAINBOW_GLASS_VALENTINE", Material.PINK_STAINED_GLASS, "&5Verre arc-en-ciel &7(Saint-Valentin)", "", VALENTINES_DAY);
    public static final SlimefunItemStack RAINBOW_CLAY_VALENTINE = new SlimefunItemStack("RAINBOW_CLAY_VALENTINE", Material.PINK_TERRACOTTA, "&5Argile arc-en-ciel &7(Saint-Valentin)", "", VALENTINES_DAY);
    public static final SlimefunItemStack RAINBOW_GLASS_PANE_VALENTINE = new SlimefunItemStack("RAINBOW_GLASS_PANE_VALENTINE", Material.PINK_STAINED_GLASS_PANE, "&5Vitre arc-en-ciel &7(Saint-Valentin)", "", VALENTINES_DAY);
    public static final SlimefunItemStack RAINBOW_CONCRETE_VALENTINE = new SlimefunItemStack("RAINBOW_CONCRETE_VALENTINE", Material.PINK_CONCRETE, "&5Béton arc-en-ciel &7(Saint-Valentin)", "", VALENTINES_DAY);
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA_VALENTINE = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_VALENTINE", Material.PINK_GLAZED_TERRACOTTA, "&5Terracotta arc-en-ciel &7(Saint-Valentin)", "", VALENTINES_DAY);

    private static final String HALLOWEEN = "&c[&6Édition de Halloween&c]";
    public static final SlimefunItemStack RAINBOW_WOOL_HALLOWEEN = new SlimefunItemStack("RAINBOW_WOOL_HALLOWEEN", Material.ORANGE_WOOL, "&5Laine arc-en-ciel &7(Halloween)", "", HALLOWEEN);
    public static final SlimefunItemStack RAINBOW_GLASS_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLASS_HALLOWEEN", Material.ORANGE_STAINED_GLASS, "&5Verre arc-en-ciel &7(Halloween)", "", HALLOWEEN);
    public static final SlimefunItemStack RAINBOW_CLAY_HALLOWEEN = new SlimefunItemStack("RAINBOW_CLAY_HALLOWEEN", Material.ORANGE_TERRACOTTA, "&5RArgile arc-en-ciel &7(Halloween)", "", HALLOWEEN);
    public static final SlimefunItemStack RAINBOW_GLASS_PANE_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLASS_PANE_HALLOWEEN", Material.ORANGE_STAINED_GLASS_PANE, "&5Vitre arc-en-ciel &7(Halloween)", "", HALLOWEEN);
    public static final SlimefunItemStack RAINBOW_CONCRETE_HALLOWEEN = new SlimefunItemStack("RAINBOW_CONCRETE_HALLOWEEN", Material.ORANGE_CONCRETE, "&5Béton arc-en-ciel &7(Halloween)", "", HALLOWEEN);
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN", Material.ORANGE_GLAZED_TERRACOTTA, "&5Terracotta arc-en-ciel &7(Halloween)", "", HALLOWEEN);

    /* Ingots */
    public static final SlimefunItemStack COPPER_INGOT = new SlimefunItemStack("COPPER_INGOT", Material.BRICK, "&bLingot de cuivre");
    public static final SlimefunItemStack TIN_INGOT = new SlimefunItemStack("TIN_INGOT", Material.IRON_INGOT, "&bLingot d'étain");
    public static final SlimefunItemStack SILVER_INGOT = new SlimefunItemStack("SILVER_INGOT", Material.IRON_INGOT, "&bLingot d'argent");
    public static final SlimefunItemStack ALUMINUM_INGOT = new SlimefunItemStack("ALUMINUM_INGOT", Material.IRON_INGOT, "&bLingot d'aluminium");
    public static final SlimefunItemStack LEAD_INGOT = new SlimefunItemStack("LEAD_INGOT", Material.IRON_INGOT, "&bLingot de plomb");
    public static final SlimefunItemStack ZINC_INGOT = new SlimefunItemStack("ZINC_INGOT", Material.IRON_INGOT, "&bLingot de zinc");
    public static final SlimefunItemStack MAGNESIUM_INGOT = new SlimefunItemStack("MAGNESIUM_INGOT", Material.IRON_INGOT, "&bLingot de magnésium");

    /* Alloy (Carbon + Iron) */
    public static final SlimefunItemStack STEEL_INGOT = new SlimefunItemStack("STEEL_INGOT", Material.IRON_INGOT, "&bLingot d'acier");
    /* Alloy (Copper + Tin) */
    public static final SlimefunItemStack BRONZE_INGOT = new SlimefunItemStack("BRONZE_INGOT", Material.BRICK, "&bLingot de bronze");
    /* Alloy (Copper + Aluminum) */
    public static final SlimefunItemStack DURALUMIN_INGOT = new SlimefunItemStack("DURALUMIN_INGOT", Material.IRON_INGOT, "&bLingot d'alliage d'aluminium léger");
    /* Alloy (Copper + Silver) */
    public static final SlimefunItemStack BILLON_INGOT = new SlimefunItemStack("BILLON_INGOT", Material.IRON_INGOT, "&bLingot de billon");
    /* Alloy (Copper + Zinc) */
    public static final SlimefunItemStack BRASS_INGOT = new SlimefunItemStack("BRASS_INGOT", Material.GOLD_INGOT, "&bingot de laiton");
    /* Alloy (Aluminum + Brass) */
    public static final SlimefunItemStack ALUMINUM_BRASS_INGOT = new SlimefunItemStack("ALUMINUM_BRASS_INGOT", Material.GOLD_INGOT, "&bLingot d'alliage d'aluminium et de laiton");
    /* Alloy (Aluminum + Bronze) */
    public static final SlimefunItemStack ALUMINUM_BRONZE_INGOT = new SlimefunItemStack("ALUMINUM_BRONZE_INGOT", Material.GOLD_INGOT, "&bLingot d'alliage d'aluminium et de bronze");
    /* Alloy (Gold + Silver + Copper) */
    public static final SlimefunItemStack CORINTHIAN_BRONZE_INGOT = new SlimefunItemStack("CORINTHIAN_BRONZE_INGOT", Material.GOLD_INGOT, "&bLingot de bronze corinthien");
    /* Alloy (Lead + Tin) */
    public static final SlimefunItemStack SOLDER_INGOT = new SlimefunItemStack("SOLDER_INGOT", Material.IRON_INGOT, "&bLingot de soudure");
    /* Alloy (Steel + Iron + Carbon) */
    public static final SlimefunItemStack DAMASCUS_STEEL_INGOT = new SlimefunItemStack("DAMASCUS_STEEL_INGOT", Material.IRON_INGOT, "&bLingot d'acier de damas");
    /* Alloy (Damascus Steel + Duralumin + Compressed Carbon + Aluminium Bronze) */
    public static final SlimefunItemStack HARDENED_METAL_INGOT = new SlimefunItemStack("HARDENED_METAL_INGOT", Material.IRON_INGOT, "&b&lMétal renforcé");
    /* Alloy (Hardened Metal + Corinthian Bronze + Solder + Billon + Damascus Steel) */
    public static final SlimefunItemStack REINFORCED_ALLOY_INGOT = new SlimefunItemStack("REINFORCED_ALLOY_INGOT", Material.IRON_INGOT, "&b&lLingot d'alliage renforcé");
    /* Alloy (Iron + Silicon) */
    public static final SlimefunItemStack FERROSILICON = new SlimefunItemStack("FERROSILICON", Material.IRON_INGOT, "&bLingot d'alliage de fer et de silice");
    /* Alloy (Iron + Gold) */
    public static final SlimefunItemStack GILDED_IRON = new SlimefunItemStack("GILDED_IRON", Material.GOLD_INGOT, "&6&lLingot d'alliage de fer nordique");
    /* Alloy (Redston + Ferrosilicon) */
    public static final SlimefunItemStack REDSTONE_ALLOY = new SlimefunItemStack("REDSTONE_ALLOY", Material.BRICK, "&cingot d'alliage de redstone");
    /* Alloy (Iron + Copper) */
    public static final SlimefunItemStack NICKEL_INGOT = new SlimefunItemStack("NICKEL_INGOT", Material.IRON_INGOT, "&bLingot de nickel");
    /* Alloy (Nickel + Iron + Copper) */
    public static final SlimefunItemStack COBALT_INGOT = new SlimefunItemStack("COBALT_INGOT", Material.IRON_INGOT, "&9Lingot de cobalte");

    /* Gold */
    public static final SlimefunItemStack GOLD_4K = new SlimefunItemStack("GOLD_4K", Material.GOLD_INGOT, "&fLingot d'or &7(4-Carats)");
    public static final SlimefunItemStack GOLD_6K = new SlimefunItemStack("GOLD_6K", Material.GOLD_INGOT, "&fLingot d'or &7(6-Carats)");
    public static final SlimefunItemStack GOLD_8K = new SlimefunItemStack("GOLD_8K", Material.GOLD_INGOT, "&fLingot d'or &7(8-Carats)");
    public static final SlimefunItemStack GOLD_10K = new SlimefunItemStack("GOLD_10K", Material.GOLD_INGOT, "&fLingot d'or &7(10-Carats)");
    public static final SlimefunItemStack GOLD_12K = new SlimefunItemStack("GOLD_12K", Material.GOLD_INGOT, "&fLingot d'or &7(12-Carats)");
    public static final SlimefunItemStack GOLD_14K = new SlimefunItemStack("GOLD_14K", Material.GOLD_INGOT, "&fLingot d'or &7(14-Carats)");
    public static final SlimefunItemStack GOLD_16K = new SlimefunItemStack("GOLD_16K", Material.GOLD_INGOT, "&fLingot d'or &7(16-Carats)");
    public static final SlimefunItemStack GOLD_18K = new SlimefunItemStack("GOLD_18K", Material.GOLD_INGOT, "&fLingot d'or &7(18-Carats)");
    public static final SlimefunItemStack GOLD_20K = new SlimefunItemStack("GOLD_20K", Material.GOLD_INGOT, "&fLingot d'or &7(20-Carats)");
    public static final SlimefunItemStack GOLD_22K = new SlimefunItemStack("GOLD_22K", Material.GOLD_INGOT, "&fLingot d'or &7(22-Carats)");
    public static final SlimefunItemStack GOLD_24K = new SlimefunItemStack("GOLD_24K", Material.GOLD_INGOT, "&fLingot d'or &7(24-Carats)");

    /* Dusts */
    public static final SlimefunItemStack IRON_DUST = new SlimefunItemStack("IRON_DUST", Material.GUNPOWDER, "&6Poudres de fer");
    public static final SlimefunItemStack GOLD_DUST = new SlimefunItemStack("GOLD_DUST", Material.GLOWSTONE_DUST, "&6Poudres d'or");
    public static final SlimefunItemStack TIN_DUST = new SlimefunItemStack("TIN_DUST", Material.SUGAR, "&6Poudres d'étain");
    public static final SlimefunItemStack COPPER_DUST = new SlimefunItemStack("COPPER_DUST", Material.GLOWSTONE_DUST, "&6Poudres de cuivre");
    public static final SlimefunItemStack SILVER_DUST = new SlimefunItemStack("SILVER_DUST", Material.SUGAR, "&6Poudres d'argent");
    public static final SlimefunItemStack ALUMINUM_DUST = new SlimefunItemStack("ALUMINUM_DUST", Material.SUGAR, "&6Poudres d'aluminium");
    public static final SlimefunItemStack LEAD_DUST = new SlimefunItemStack("LEAD_DUST", Material.GUNPOWDER, "&6Poudres de plomb");
    public static final SlimefunItemStack ZINC_DUST = new SlimefunItemStack("ZINC_DUST", Material.SUGAR, "&6Poudres de zinc");
    public static final SlimefunItemStack MAGNESIUM_DUST = new SlimefunItemStack("MAGNESIUM_DUST", Material.SUGAR, "&6Magnésium");

    public static final SlimefunItemStack SULFATE = new SlimefunItemStack("SULFATE", Material.GLOWSTONE_DUST, "&6Sulfate");
    public static final SlimefunItemStack SILICON = new SlimefunItemStack("SILICON", Material.FIREWORK_STAR, "&6Silice");
    public static final SlimefunItemStack GOLD_24K_BLOCK = new SlimefunItemStack("GOLD_24K_BLOCK", Material.GOLD_BLOCK, "&fBloc d'or &7(24-Carats)");

    /* Gems */
    public static final SlimefunItemStack SYNTHETIC_DIAMOND = new SlimefunItemStack("SYNTHETIC_DIAMOND", Material.DIAMOND, "&bDiamant synthétique", "", "&fCet objet peut être utilisé dans", "&fun établi comme un diamant réel");
    public static final SlimefunItemStack SYNTHETIC_EMERALD = new SlimefunItemStack("SYNTHETIC_EMERALD", Material.EMERALD, "&bÉmeraude synthétique", "", "&fCet objet peut être utilisé pour", "&féchanger avec les Villageois");
    public static final SlimefunItemStack SYNTHETIC_SAPPHIRE = new SlimefunItemStack("SYNTHETIC_SAPPHIRE", HeadTexture.SAPPHIRE, "&bSaphire synthétique", "", "&fCet objet peut être utilisé dans", "&fun établi comme du lapis lazuli");
    public static final SlimefunItemStack CARBONADO = new SlimefunItemStack("CARBONADO", HeadTexture.CARBONADO, "&b&lCarbonado", "", "&7&o\"Diamant noir\"");
    public static final SlimefunItemStack RAW_CARBONADO = new SlimefunItemStack("RAW_CARBONADO", HeadTexture.RAW_CARBONADO, "&bCarbonado brut");

    public static final SlimefunItemStack URANIUM = new SlimefunItemStack("URANIUM", HeadTexture.URANIUM, "&4Uranium", "", LoreBuilder.radioactive(Radioactivity.HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final SlimefunItemStack NEPTUNIUM = new SlimefunItemStack("NEPTUNIUM", HeadTexture.NEPTUNIUM, "&aNeptunium", "", LoreBuilder.radioactive(Radioactivity.HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final SlimefunItemStack PLUTONIUM = new SlimefunItemStack("PLUTONIUM", HeadTexture.PLUTONIUM, "&7Plutonium", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final SlimefunItemStack BOOSTED_URANIUM = new SlimefunItemStack("BOOSTED_URANIUM", HeadTexture.BOOSTED_URANIUM, "&2Uranium enrichi", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);

    /* Talisman */
    public static final SlimefunItemStack COMMON_TALISMAN = new SlimefunItemStack("COMMON_TALISMAN", Material.EMERALD, "&6Talisman commun");
    public static final SlimefunItemStack ENDER_TALISMAN = new SlimefunItemStack("ENDER_TALISMAN", Material.EMERALD, "&5Talisman du Néant");

    public static final SlimefunItemStack TALISMAN_ANVIL = new SlimefunItemStack("ANVIL_TALISMAN", Material.EMERALD, "&aTalisman de l'enclume", "", "&fChaque talisman peut empêcher", "&fun objet de se briser, mais", "&fle talisman sera consommé", "", "&4&lATTENTION:", "&4Ce talisman ne fonctionne pas sur", "&4les outils trop puissant", "&4a cause de leur complexité");
    public static final SlimefunItemStack TALISMAN_MINER = new SlimefunItemStack("MINER_TALISMAN", Material.EMERALD, "&aTalisman du mineur", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous avez", "&f20% de chance de doubler", "&fle minerai miné");
    public static final SlimefunItemStack TALISMAN_HUNTER = new SlimefunItemStack("HUNTER_TALISMAN", Material.EMERALD, "&aTalisman du chasseur", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous avez", "&f20% de chance de doubler", "&fle butin des monstres que vous tuez");
    public static final SlimefunItemStack TALISMAN_LAVA = new SlimefunItemStack("LAVA_TALISMAN", Material.EMERALD, "&aTalisman du marcheur de lave", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire,", "&fvous obtiendrez Résistance au feu", "&fquand vous toucherez la lave", "&fmais le talisman sera consommé");
    public static final SlimefunItemStack TALISMAN_WATER = new SlimefunItemStack("WATER_TALISMAN", Material.EMERALD, "&aTalisman de respiration sub-aquatique", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire,", "&fvous obtiendrez les capacités", "&fde respirer sous l'eau", "&fdès que vous vous noierez", "&fmais le talisman sera consommé");
    public static final SlimefunItemStack TALISMAN_ANGEL = new SlimefunItemStack("ANGEL_TALISMAN", Material.EMERALD, "&aTalisman de l'ange", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous aurez", "&f75% de chance de ne pas", "&fprendre de dégâts de chute");
    public static final SlimefunItemStack TALISMAN_FIRE = new SlimefunItemStack("FIRE_TALISMAN", Material.EMERALD, "&aTalisman du combattant du feu", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous aurez", "&fl'effet Résistance au feu", "&fdès que vous brûlerez", "&fmais le talisman sera consommé");
    public static final SlimefunItemStack TALISMAN_MAGICIAN = new SlimefunItemStack("MAGICIAN_TALISMAN", Material.EMERALD, "&aTalisman du magicien", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous aurez", "&f80% de chance bonus en enchantant un objet", "&fVous obtiendrez alors un enchantement supplémentaire");
    public static final SlimefunItemStack TALISMAN_TRAVELLER = new SlimefunItemStack("TRAVELLER_TALISMAN", Material.EMERALD, "&aTalisman du voyageur", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous aurez", "&f60% de chance d'obtenir un effet", "&fVitesse lorsque vous commencerez à courir");
    public static final SlimefunItemStack TALISMAN_WARRIOR = new SlimefunItemStack("WARRIOR_TALISMAN", Material.EMERALD, "&aTalisman du guerrier", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous aurez", "&fl'effet Force 3 lorsque vous êtes touché", "&fmais le talisman sera consommé");
    public static final SlimefunItemStack TALISMAN_KNIGHT = new SlimefunItemStack("KNIGHT_TALISMAN", Material.EMERALD, "&aTalisman du chevalier", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous aurez", "&f30% de chance d'obtenir 5 secondes de Regénération", "&fdès que vous serez touché", "&fmais le talisman sera consommé");
    public static final SlimefunItemStack TALISMAN_WHIRLWIND = new SlimefunItemStack("WHIRLWIND_TALISMAN", Material.EMERALD, "&aTalisman de la tornade", "", "&fAvoir ce Talisman", "&fdans votre inventaire renverra", "&f60% des projectiles tirés vers vous.", "&e&oSeul un lancé de Trident peut ", "&e&opercer cette couche de protection");
    public static final SlimefunItemStack TALISMAN_WIZARD = new SlimefunItemStack("WIZARD_TALISMAN", Material.EMERALD, "&aTalisman du sorcier", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous ", "&fobtenez un niveau de Fortune 4/5", "&fmais il a aussi une chance de diminuer", "&fle niveau de certains enchantements sur votre objet");
    public static final SlimefunItemStack TALISMAN_CAVEMAN = new SlimefunItemStack("CAVEMAN_TALISMAN", Material.EMERALD, "&aTalisman de l'homme des cavernes", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous aurez", "&f50% de chance d'obtenir", "&fl'effet Hâte quand vous minerez un minerai");
    public static final SlimefunItemStack TALISMAN_WISE = new SlimefunItemStack("WISE_TALISMAN", Material.EMERALD, "&aTalisman du sage", "", "&fTant que vous avez ce Talisman", "&fdans votre inventaire, vous aurez", "&f20% de chance de doubler", "&fl'expérience reçue");

    /* Staves */
    public static final SlimefunItemStack STAFF_ELEMENTAL = new SlimefunItemStack("STAFF_ELEMENTAL", Material.STICK, "&6Bâton élémentaire");
    public static final SlimefunItemStack STAFF_WIND = new SlimefunItemStack("STAFF_ELEMENTAL_WIND", Material.STICK, "&6Bâton élémentaire &7- &b&oVent", "", "&7Élément: &b&oVent", "", "&eClic droit&7 pour vous projeter en avant");
    public static final SlimefunItemStack STAFF_FIRE = new SlimefunItemStack("STAFF_ELEMENTAL_FIRE", Material.STICK, "&6Bâton élémentaire &7- &c&oFeu", "", "&7Élément: &c&oFeu");
    public static final SlimefunItemStack STAFF_WATER = new SlimefunItemStack("STAFF_ELEMENTAL_WATER", Material.STICK, "&6Bâton élémentaire &7- &1&oEau", "", "&7Élément: &1&oEau", "", "&eClic droit&7 pour éteindre le feu sur vous");
    public static final SlimefunItemStack STAFF_STORM = new SlimefunItemStack("STAFF_ELEMENTAL_STORM", Material.STICK, "&6Bâton élémentaire &7- &8&oTempête", "", "&7Élément: &8&oTempête", "", "&eClic droit&7 pour invoquer un éclair", LoreBuilder.usesLeft(StormStaff.MAX_USES));

    static {
        STAFF_WIND.addUnsafeEnchantment(Enchantment.LUCK, 1);
        STAFF_FIRE.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 5);
        STAFF_WATER.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
        STAFF_STORM.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
    }

    /* Multiblocks */
    public static final SlimefunItemStack ENHANCED_CRAFTING_TABLE = new SlimefunItemStack("ENHANCED_CRAFTING_TABLE", Material.CRAFTING_TABLE, "&eÉtabli amélioré", "", "&aUn simple établi ne peut pas", "&acontenir toute cette énergie ...");
    public static final SlimefunItemStack GRIND_STONE = new SlimefunItemStack("GRIND_STONE", Material.DISPENSER, "&bMeule", "", "&aTransforme un objet en un autre");
    public static final SlimefunItemStack ARMOR_FORGE = new SlimefunItemStack("ARMOR_FORGE", Material.ANVIL, "&6Forge d'armures", "", "&aVous permet de fabriquer de puissantes armures");
    public static final SlimefunItemStack MAKESHIFT_SMELTERY = new SlimefunItemStack("MAKESHIFT_SMELTERY", Material.BLAST_FURNACE, "&eFonderie de fortune", "", "&fUne fonderie improvisée bas-de-gamme", "&fvous permettant uniquement de", "&ffondre les poudres en lingots");
    public static final SlimefunItemStack SMELTERY = new SlimefunItemStack("SMELTERY", Material.FURNACE, "&6Fonderie", "", "&fUn four très-haute température", "&fvous permettant de fabriquer des lingots", "&fà l'aide de poudres ainsi que", "&fdes alliages divers");
    public static final SlimefunItemStack ORE_CRUSHER = new SlimefunItemStack("ORE_CRUSHER", Material.DISPENSER, "&bConcasseur de minerais", "", "&aConcasse les minerais pour les doubler");
    public static final SlimefunItemStack COMPRESSOR = new SlimefunItemStack("COMPRESSOR", Material.PISTON, "&bCompresseur", "", "&aCompresse les objets");
    public static final SlimefunItemStack PRESSURE_CHAMBER = new SlimefunItemStack("PRESSURE_CHAMBER", Material.GLASS, "&bChampre de compression", "", "&aComprime encore plus les objets");
    public static final SlimefunItemStack MAGIC_WORKBENCH = new SlimefunItemStack("MAGIC_WORKBENCH", Material.CRAFTING_TABLE, "&6Établi magique", "", "&dInfuse les objets avec de l'énergie magique");
    public static final SlimefunItemStack ORE_WASHER = new SlimefunItemStack("ORE_WASHER", Material.CAULDRON, "&6Laveuse de minerais", "", "&aNettoie les minerais pour les filtrer", "&aafin d'obtenir des petits morceaux de pierres");
    public static final SlimefunItemStack TABLE_SAW = new SlimefunItemStack("TABLE_SAW", Material.STONECUTTER, "&6Scie sur table", "", "&aVous permet d'obtenir 8 planches en 1 bûche", "&a(Fonctionne avec tout les types de bois)");
    public static final SlimefunItemStack JUICER = new SlimefunItemStack("JUICER", Material.GLASS_BOTTLE, "&aPresse-agrume", "", "&aVous permet de fabriquer des jus délicieux");
    public static final SlimefunItemStack AUTOMATED_PANNING_MACHINE = new SlimefunItemStack("AUTOMATED_PANNING_MACHINE", Material.BOWL, "&eTamis automatique", "", "&fUne version multiblocs du tamis", "&fet du tamis des Tréfonds en une seule machine.");

    public static final SlimefunItemStack INDUSTRIAL_MINER = new SlimefunItemStack("INDUSTRIAL_MINER", Material.GOLDEN_PICKAXE, "&bMineur industriel", "", "&fCe multibloc va miner tout les minerais", "&fans une zone de 7x7 en dessous de lui.", "&fPlacez du charbon ou autre dans son coffre", "&fpour l'alimenter en énergies.");
    public static final SlimefunItemStack ADVANCED_INDUSTRIAL_MINER = new SlimefunItemStack("ADVANCED_INDUSTRIAL_MINER", Material.DIAMOND_PICKAXE, "&cMineur industriel avancé", "", "&fCe multibloc va extraire tous les minerais", "&fdans une zone de 11x11 en dessous de lui.", "&fPlacez un seau de carburant ou de lave dans ", "&fson coffre pour l'alimenter en énergies", "", "&a+ Toucher de Soie");

    static {
        ItemMeta meta = INDUSTRIAL_MINER.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        INDUSTRIAL_MINER.setItemMeta(meta);

        ItemMeta meta2 = ADVANCED_INDUSTRIAL_MINER.getItemMeta();
        meta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ADVANCED_INDUSTRIAL_MINER.setItemMeta(meta2);
    }

    /* Machines */
    public static final SlimefunItemStack COMPOSTER = new SlimefunItemStack("COMPOSTER", Material.CAULDRON, "&aComposteur", "", "&a&oPeut convertir divers matériaux au fil du temps...");
    public static final SlimefunItemStack CRUCIBLE = new SlimefunItemStack("CRUCIBLE", Material.CAULDRON, "&cCreuset", "", "&a&oUtilisé pour fondre les objets en liquides");
    public static final SlimefunItemStack OUTPUT_CHEST = new SlimefunItemStack("OUTPUT_CHEST", Material.CHEST, "&4Coffre de sortie", "", "&c&oUne machine basique essaiera de", "&c&oplacer des objets dans ce coffre si", "&c&oil est placé à côté du coffre.");
    public static final SlimefunItemStack IGNITION_CHAMBER = new SlimefunItemStack("IGNITION_CHAMBER", Material.DROPPER, "&4Chambre d'allumage automatique", "", "&fEmpêche la fonderie d'étouffer le feu.", "&fIl suffit d'insérer un \"Briquet\"", "&fet le placer à côté du distributeur de la fonderie");
    public static final SlimefunItemStack HOLOGRAM_PROJECTOR = new SlimefunItemStack("HOLOGRAM_PROJECTOR", Material.QUARTZ_SLAB, "&bProjecteur d'hologrammes", "", "&fProjette un hologramme éditable");
    public static final SlimefunItemStack BLOCK_PLACER = new SlimefunItemStack("BLOCK_PLACER", Material.DISPENSER, "&aPlaceur de blocs", "", "&fTous les blocs présent dans ce", "&fdistributeur seront placés");

    /* Enhanced Furnaces */
    public static final SlimefunItemStack ENHANCED_FURNACE = new SlimefunItemStack("ENHANCED_FURNACE", Material.FURNACE, "&7Four amélioré - &eI", "", "&7Vitesse de traitement: &e1x", "&7Qualité du carburant: &e1x", "&7Multiplicateur de chance: &e1x");
    public static final SlimefunItemStack ENHANCED_FURNACE_2 = new SlimefunItemStack("ENHANCED_FURNACE_2", Material.FURNACE, "&7Four amélioré - &eII", "", "&7Vitesse de traitement: &e2x", "&7Qualité du carburant: &e1x", "&7Multiplicateur de chance: &e1x");
    public static final SlimefunItemStack ENHANCED_FURNACE_3 = new SlimefunItemStack("ENHANCED_FURNACE_3", Material.FURNACE, "&7Four amélioré - &eIII", "", "&7Vitesse de traitement: &e2x", "&7Qualité du carburant: &e2x", "&7Multiplicateur de chance: &e1x");
    public static final SlimefunItemStack ENHANCED_FURNACE_4 = new SlimefunItemStack("ENHANCED_FURNACE_4", Material.FURNACE, "&7Four amélioré - &eIV", "", "&7Vitesse de traitement: &e3x", "&7Qualité du carburant: &e2x", "&7Multiplicateur de chance: &e1x");
    public static final SlimefunItemStack ENHANCED_FURNACE_5 = new SlimefunItemStack("ENHANCED_FURNACE_5", Material.FURNACE, "&7Four amélioré - &eV", "", "&7Vitesse de traitement: &e3x", "&7Qualité du carburant: &e2x", "&7Multiplicateur de chance: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_6 = new SlimefunItemStack("ENHANCED_FURNACE_6", Material.FURNACE, "&7Four amélioré - &eVI", "", "&7Vitesse de traitement: &e3x", "&7Qualité du carburant: &e3x", "&7Multiplicateur de chance: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_7 = new SlimefunItemStack("ENHANCED_FURNACE_7", Material.FURNACE, "&7Four amélioré - &eVII", "", "&7Vitesse de traitement: &e4x", "&7Qualité du carburant: &e3x", "&7Multiplicateur de chance: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_8 = new SlimefunItemStack("ENHANCED_FURNACE_8", Material.FURNACE, "&7Four amélioré - &eVIII", "", "&7Vitesse de traitement: &e4x", "&7Qualité du carburant: &e4x", "&7Multiplicateur de chance: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_9 = new SlimefunItemStack("ENHANCED_FURNACE_9", Material.FURNACE, "&7Four amélioré - &eIX", "", "&7Vitesse de traitement: &e5x", "&7Qualité du carburant: &e4x", "&7Multiplicateur de chance: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_10 = new SlimefunItemStack("ENHANCED_FURNACE_10", Material.FURNACE, "&7Four amélioré - &eX", "", "&7Vitesse de traitement: &e5x", "&7Fuel Efficiency: &e5x", "&7Luck Multiplier: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_11 = new SlimefunItemStack("ENHANCED_FURNACE_11", Material.FURNACE, "&7Four amélioré - &eXI", "", "&7Vitesse de traitement: &e5x", "&7Fuel Efficiency: &e5x", "&7Luck Multiplier: &e3x");
    public static final SlimefunItemStack REINFORCED_FURNACE = new SlimefunItemStack("REINFORCED_FURNACE", Material.FURNACE, "&7Four renforcé", "", "&7Vitesse de traitement: &e10x", "&7Fuel Efficiency: &e10x", "&7Luck Multiplier: &e3x");
    public static final SlimefunItemStack CARBONADO_EDGED_FURNACE = new SlimefunItemStack("CARBONADO_EDGED_FURNACE", Material.FURNACE, "&7Méga-four en carbonado", "", "&7Vitesse de traitement: &e20x", "&7Qualité du carburant: &e10x", "&7Multiplicateur de chance: &e3x");

    /* Soulbound Items */
    public static final SlimefunItemStack SOULBOUND_SWORD = new SlimefunItemStack("SOULBOUND_SWORD", Material.DIAMOND_SWORD, "&cÉpée des âmes");
    public static final SlimefunItemStack SOULBOUND_BOW = new SlimefunItemStack("SOULBOUND_BOW", Material.BOW, "&cArc des âmes");
    public static final SlimefunItemStack SOULBOUND_PICKAXE = new SlimefunItemStack("SOULBOUND_PICKAXE", Material.DIAMOND_PICKAXE, "&cPioche des âmes");
    public static final SlimefunItemStack SOULBOUND_AXE = new SlimefunItemStack("SOULBOUND_AXE", Material.DIAMOND_AXE, "&cHache des âmes");
    public static final SlimefunItemStack SOULBOUND_SHOVEL = new SlimefunItemStack("SOULBOUND_SHOVEL", Material.DIAMOND_SHOVEL, "&cPelle des âmes");
    public static final SlimefunItemStack SOULBOUND_HOE = new SlimefunItemStack("SOULBOUND_HOE", Material.DIAMOND_HOE, "&cFaux des âmes");
    public static final SlimefunItemStack SOULBOUND_TRIDENT = new SlimefunItemStack("SOULBOUND_TRIDENT", Material.TRIDENT, "&cTrident des âmes");

    public static final SlimefunItemStack SOULBOUND_HELMET = new SlimefunItemStack("SOULBOUND_HELMET", Material.DIAMOND_HELMET, "&cCasque des âmes");
    public static final SlimefunItemStack SOULBOUND_CHESTPLATE = new SlimefunItemStack("SOULBOUND_CHESTPLATE", Material.DIAMOND_CHESTPLATE, "&cPlastron des âmes");
    public static final SlimefunItemStack SOULBOUND_LEGGINGS = new SlimefunItemStack("SOULBOUND_LEGGINGS", Material.DIAMOND_LEGGINGS, "&cJambières des âmes");
    public static final SlimefunItemStack SOULBOUND_BOOTS = new SlimefunItemStack("SOULBOUND_BOOTS", Material.DIAMOND_BOOTS, "&cBottes des âmes");

    /* Runes */
    public static final SlimefunItemStack BLANK_RUNE = new SlimefunItemStack("BLANK_RUNE", new ColoredFireworkStar(Color.BLACK, "&8Rune vierge"));

    public static final SlimefunItemStack AIR_RUNE = new SlimefunItemStack("ANCIENT_RUNE_AIR", new ColoredFireworkStar(Color.AQUA, "&7Ancienne rune &8&l[&b&lAir&8&l]"));
    public static final SlimefunItemStack WATER_RUNE = new SlimefunItemStack("ANCIENT_RUNE_WATER", new ColoredFireworkStar(Color.BLUE, "&7Ancienne rune &8&l[&1&lEau&8&l]"));
    public static final SlimefunItemStack FIRE_RUNE = new SlimefunItemStack("ANCIENT_RUNE_FIRE", new ColoredFireworkStar(Color.RED, "&7Ancienne rune &8&l[&4&lFeu&8&l]"));
    public static final SlimefunItemStack EARTH_RUNE = new SlimefunItemStack("ANCIENT_RUNE_EARTH", new ColoredFireworkStar(Color.fromRGB(112, 47, 7), "&7Ancienne rune &8&l[&c&lTerre&8&l]"));
    public static final SlimefunItemStack ENDER_RUNE = new SlimefunItemStack("ANCIENT_RUNE_ENDER", new ColoredFireworkStar(Color.PURPLE, "&7Ancienne rune &8&l[&5&lNéant&8&l]"));

    public static final SlimefunItemStack RAINBOW_RUNE = new SlimefunItemStack("ANCIENT_RUNE_RAINBOW", new ColoredFireworkStar(Color.FUCHSIA, "&7Ancienne rune &8&l[&d&lArc-en-ciel&8&l]"));
    public static final SlimefunItemStack LIGHTNING_RUNE = new SlimefunItemStack("ANCIENT_RUNE_LIGHTNING", new ColoredFireworkStar(Color.fromRGB(255, 255, 95), "&7Ancienne rune &8&l[&e&lFoudre&8&l]"));
    public static final SlimefunItemStack SOULBOUND_RUNE = new SlimefunItemStack("ANCIENT_RUNE_SOULBOUND", new ColoredFireworkStar(Color.fromRGB(47, 0, 117), "&7Ancienne rune &8&l[&5&lÂme emprisonnée&8&l]", "&eJetez cette rune sur un objet déjà au sol", "&5liera &ecet objet à votre âme.", " ", "&eN'utilisez cette rune que sur", "&edes objets &6très important.", " ", "&eLes objets liés à votre âme ne tomberont pas à votre mort."));
    public static final SlimefunItemStack ENCHANTMENT_RUNE = new SlimefunItemStack("ANCIENT_RUNE_ENCHANTMENT", new ColoredFireworkStar(Color.fromRGB(255, 217, 25), "&7Ancienne rune &8&l[&6&lEnchantement&8&l]", "&eJetez cette rune sur un objet déjà au sol", "&6pour enchanter &ecet objet d'un ehcnatment aléatoire."));
    public static final SlimefunItemStack VILLAGER_RUNE = new SlimefunItemStack("ANCIENT_RUNE_VILLAGERS", new ColoredFireworkStar(Color.fromRGB(160, 20, 5), "&7Ancienne rune &8&l[&4&lVillageois&8&l]", "&eClic-droit sur un villageois pour réinitialiser", "&eleur travail et commerce actuel.", "&eLe villageois va commencer à chercher", "&eun nouveau travail après", "&equelques temps."));

    /* Electricity */
    public static final SlimefunItemStack SOLAR_GENERATOR = new SlimefunItemStack("SOLAR_GENERATOR", Material.DAYLIGHT_DETECTOR, "&bGénérateur solaire", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(4));
    public static final SlimefunItemStack SOLAR_GENERATOR_2 = new SlimefunItemStack("SOLAR_GENERATOR_2", Material.DAYLIGHT_DETECTOR, "&cGénérateur solaire avancé", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(16));
    public static final SlimefunItemStack SOLAR_GENERATOR_3 = new SlimefunItemStack("SOLAR_GENERATOR_3", Material.DAYLIGHT_DETECTOR, "&4Générateur solaire en carbonado", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(64));
    public static final SlimefunItemStack SOLAR_GENERATOR_4 = new SlimefunItemStack("SOLAR_GENERATOR_4", Material.DAYLIGHT_DETECTOR, "&eGénérateur solaire énergisé", "", "&9Fonctionne la nuit", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(256) + " (Jour)", LoreBuilder.powerPerSecond(128) + " (Nuit)");

    public static final SlimefunItemStack COAL_GENERATOR = new SlimefunItemStack("COAL_GENERATOR", HeadTexture.GENERATOR, "&cGénérateur au charbon", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.GENERATOR), LoreBuilder.powerBuffer(64), LoreBuilder.powerPerSecond(16));
    public static final SlimefunItemStack COAL_GENERATOR_2 = new SlimefunItemStack("COAL_GENERATOR_2", HeadTexture.GENERATOR, "&cGénérateur au charbon &7(&eII&7)", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.GENERATOR), LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(30));

    public static final SlimefunItemStack LAVA_GENERATOR = new SlimefunItemStack("LAVA_GENERATOR", HeadTexture.GENERATOR, "&4Générateur thermique", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.GENERATOR), LoreBuilder.powerBuffer(512), LoreBuilder.powerPerSecond(20));
    public static final SlimefunItemStack LAVA_GENERATOR_2 = new SlimefunItemStack("LAVA_GENERATOR_2", HeadTexture.GENERATOR, "&4Générateur thermique &7(&eII&7)", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.GENERATOR), LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(40));

    public static final SlimefunItemStack ELECTRIC_FURNACE = new SlimefunItemStack("ELECTRIC_FURNACE", Material.FURNACE, "&cFour électrique", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(4));
    public static final SlimefunItemStack ELECTRIC_FURNACE_2 = new SlimefunItemStack("ELECTRIC_FURNACE_2", Material.FURNACE, "&cFour électrique &7- &eII", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.speed(2), LoreBuilder.powerPerSecond(6));
    public static final SlimefunItemStack ELECTRIC_FURNACE_3 = new SlimefunItemStack("ELECTRIC_FURNACE_3", Material.FURNACE, "&cFour électrique &7- &eIII", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.speed(4), LoreBuilder.powerPerSecond(10));

    public static final SlimefunItemStack ELECTRIC_ORE_GRINDER = new SlimefunItemStack("ELECTRIC_ORE_GRINDER", Material.FURNACE, "&cBroyeur électrique de minerais", "", "&fFonctionne comme un concasseur de minerai et un broyeur de pierre", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(12));
    public static final SlimefunItemStack ELECTRIC_ORE_GRINDER_2 = new SlimefunItemStack("ELECTRIC_ORE_GRINDER_2", Material.FURNACE, "&cBroyeur électrique de minerais &7(&eII&7)", "", "&fFonctionne comme un concasseur de minerai et un broyeur de pierre", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(4), LoreBuilder.powerPerSecond(30));
    public static final SlimefunItemStack ELECTRIC_ORE_GRINDER_3 = new SlimefunItemStack("ELECTRIC_ORE_GRINDER_3", Material.FURNACE, "&cBroyeur électrique de minerais &7(&eIII&7)", "", "&fFonctionne comme un concasseur de minerai et un broyeur de pierre", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(10), LoreBuilder.powerPerSecond(90));
    public static final SlimefunItemStack ELECTRIC_INGOT_PULVERIZER = new SlimefunItemStack("ELECTRIC_INGOT_PULVERIZER", Material.FURNACE, "&cPulvérisateur électrique de lingots", "", "&fPulvérise les lingots en poudres", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(14));
    public static final SlimefunItemStack AUTO_DRIER = new SlimefunItemStack("AUTO_DRIER", Material.SMOKER, "&6Séchoir automatique", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(10));
    public static final SlimefunItemStack AUTO_ENCHANTER = new SlimefunItemStack("AUTO_ENCHANTER", Material.ENCHANTING_TABLE, "&5Enchanteur automatique", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(18));
    public static final SlimefunItemStack AUTO_ENCHANTER_2 = new SlimefunItemStack("AUTO_ENCHANTER_2", Material.ENCHANTING_TABLE, "&5Enchanteur automatique &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(3), LoreBuilder.powerPerSecond(48));
    public static final SlimefunItemStack AUTO_DISENCHANTER = new SlimefunItemStack("AUTO_DISENCHANTER", Material.ENCHANTING_TABLE, "&5Désenchanteur automatique", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(18));
    public static final SlimefunItemStack AUTO_DISENCHANTER_2 = new SlimefunItemStack("AUTO_DISENCHANTER_2", Material.ENCHANTING_TABLE, "&5Désenchanteur automatique &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(3), LoreBuilder.powerPerSecond(48));
    public static final SlimefunItemStack AUTO_ANVIL = new SlimefunItemStack("AUTO_ANVIL", Material.IRON_BLOCK, "&7Enclume automatique", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7Taux de réparation: 10%", LoreBuilder.powerPerSecond(24));
    public static final SlimefunItemStack AUTO_ANVIL_2 = new SlimefunItemStack("AUTO_ANVIL_2", Material.IRON_BLOCK, "&7Enclume automatique Mk.II", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Taux de réparation: 25%", LoreBuilder.powerPerSecond(32));
    public static final SlimefunItemStack AUTO_BREWER = new SlimefunItemStack("AUTO_BREWER", Material.SMOKER, "&6Brasseur automatique", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(12));

    public static final SlimefunItemStack BOOK_BINDER = new SlimefunItemStack("BOOK_BINDER", Material.BOOKSHELF, "&6Assembleur de livres", "", "&fRassemble plusieurs livres enchantés en un seul.", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.powerPerSecond(16));

    public static final SlimefunItemStack BIO_REACTOR = new SlimefunItemStack("BIO_REACTOR", Material.LIME_TERRACOTTA, "&2Bio-Reacteur", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.GENERATOR), LoreBuilder.powerBuffer(128), LoreBuilder.powerPerSecond(8));
    public static final SlimefunItemStack MULTIMETER = new SlimefunItemStack("MULTIMETER", Material.CLOCK, "&eMultimètre", "", "&fMesure la quantité d'énergie", "&fstockée dans un bloc");

    public static final SlimefunItemStack SMALL_CAPACITOR = new SlimefunItemStack("SMALL_CAPACITOR", HeadTexture.CAPACITOR_25, "&aPetit condensateur d'énergie", LoreBuilder.range(6), "", LoreBuilder.machine(MachineTier.BASIC, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &7128 J de capacité");
    public static final SlimefunItemStack MEDIUM_CAPACITOR = new SlimefunItemStack("MEDIUM_CAPACITOR", HeadTexture.CAPACITOR_25, "&aCondensateur d'énergie moyen", LoreBuilder.range(6), "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &7512 J de capacité");
    public static final SlimefunItemStack BIG_CAPACITOR = new SlimefunItemStack("BIG_CAPACITOR", HeadTexture.CAPACITOR_25, "&aGrand condensateur d'énergie", LoreBuilder.range(6), "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &71024 J de capacité");
    public static final SlimefunItemStack LARGE_CAPACITOR = new SlimefunItemStack("LARGE_CAPACITOR", HeadTexture.CAPACITOR_25, "&aCondensateur d'énergie très haute capacité", LoreBuilder.range(6), "", LoreBuilder.machine(MachineTier.GOOD, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &78192 J de capacité");
    public static final SlimefunItemStack CARBONADO_EDGED_CAPACITOR = new SlimefunItemStack("CARBONADO_EDGED_CAPACITOR", HeadTexture.CAPACITOR_25, "&aCondensateur d'énergie en carbonado", LoreBuilder.range(6), "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &765536 J de capacité");
    public static final SlimefunItemStack ENERGIZED_CAPACITOR = new SlimefunItemStack("ENERGIZED_CAPACITOR", HeadTexture.CAPACITOR_25, "&aCondensateur d'énergie énergisé", LoreBuilder.range(6), "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &7524288 J de capacité");

    /* Robots */
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID = new SlimefunItemStack("PROGRAMMABLE_ANDROID", HeadTexture.PROGRAMMABLE_ANDROID, "&cAndroïde programmable &7(Normal)", "", "&8\u21E8 &7Fonction: Aucune", "&8\u21E8 &7Efficacité du carburant: 1.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_FARMER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_FARMER", HeadTexture.PROGRAMMABLE_ANDROID_FARMER, "&cAndroïde programmable &7(Fermier)", "", "&8\u21E8 &7Fonction: Planter/Cueillir", "&8\u21E8 &7Efficacité du carburant: 1.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_MINER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_MINER", HeadTexture.PROGRAMMABLE_ANDROID_MINER, "&cAndroïde programmable &7(Mineur)", "", "&8\u21E8 &7Fonction: Miner", "&8\u21E8 &7Efficacité du carburant: 1.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_WOODCUTTER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_WOODCUTTER", HeadTexture.PROGRAMMABLE_ANDROID_WOODCUTTER, "&cAndroïde programmable &7(Bûcheron)", "", "&8\u21E8 &7Fonction: Couper les arbres", "&8\u21E8 &7Efficacité du carburant: 1.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_BUTCHER", HeadTexture.PROGRAMMABLE_ANDROID_BUTCHER, "&cAndroïde programmable &7(Boucher)", "", "&8\u21E8 &7Fonction: Abattage", "&8\u21E8 &7Dégâts: 4", "&8\u21E8 &7Efficacité du carburant: 1.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_FISHERMAN", HeadTexture.PROGRAMMABLE_ANDROID_FISHERMAN, "&cAndroïde programmable &7(Pêcheur)", "", "&8\u21E8 &7Fonction: Pêcher", "&8\u21E8 &7Taux de réussite: 10%", "&8\u21E8 &7Efficacité du carburant: 1.0x");

    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2 = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2", HeadTexture.PROGRAMMABLE_ANDROID, "&cAndroïde programmable avancé &7(Normal)", "", "&8\u21E8 &7Fonction: Aucun", "&8\u21E8 &7Efficacité du carburant: 1.5x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_FISHERMAN", HeadTexture.PROGRAMMABLE_ANDROID_FISHERMAN, "&cAndroïde programmable avancé &7(Pêcheur)", "", "&8\u21E8 &7Fonction: Pêcher", "&8\u21E8 &7Taux de réussite: 20%", "&8\u21E8 &7Efficacité du carburant: 1.5x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2_FARMER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_FARMER", HeadTexture.PROGRAMMABLE_ANDROID_FARMER, "&cAndroïde programmable avancé &7(Fermier)", "", "&8\u21E8 &7Fonction: Planter/Cueillir", "&8\u21E8 &7Efficacité du carburant: 1.5x", "&8\u21E8 &7Peut aussi récolter des plantes d'ExoticGarden");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_BUTCHER", HeadTexture.PROGRAMMABLE_ANDROID_BUTCHER, "&cAndroïde programmable avancé &7(Boucher)", "", "&8\u21E8 &7Fonction: Abattage", "&8\u21E8 &7Dégâts: 8", "&8\u21E8 &7Efficacité du carburant: 1.5x");

    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_3 = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3", HeadTexture.PROGRAMMABLE_ANDROID, "&eAndroïde programmable sur-évolué &7(Normal)", "", "&8\u21E8 &7Fonction: None", "&8\u21E8 &7Efficacité du carburant: 3.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_3_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3_FISHERMAN", HeadTexture.PROGRAMMABLE_ANDROID_FISHERMAN, "&eAndroïde programmable sur-évolué &7(Pêcheur)", "", "&8\u21E8 &7Fonction: Pêcheur", "&8\u21E8 &7Taux de réussite: 30%", "&8\u21E8 &7Efficacité du carburant: 8.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_3_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3_BUTCHER", HeadTexture.PROGRAMMABLE_ANDROID_BUTCHER, "&eAndroïde programmable sur-évolué &7(Boucher)", "", "&8\u21E8 &7Fonction: Abbatage", "&8\u21E8 &7Dégâts: 20", "&8\u21E8 &7Efficacité du carburant: 8.0x");

    /* GPS */
    public static final SlimefunItemStack GPS_TRANSMITTER = new SlimefunItemStack("GPS_TRANSMITTER", HeadTexture.GPS_TRANSMITTER, "&bÉmetteur GPS", "", LoreBuilder.powerBuffer(16), LoreBuilder.powerPerSecond(2));
    public static final SlimefunItemStack GPS_TRANSMITTER_2 = new SlimefunItemStack("GPS_TRANSMITTER_2", HeadTexture.GPS_TRANSMITTER, "&cÉmetteur GPS avancé", "", LoreBuilder.powerBuffer(64), LoreBuilder.powerPerSecond(6));
    public static final SlimefunItemStack GPS_TRANSMITTER_3 = new SlimefunItemStack("GPS_TRANSMITTER_3", HeadTexture.GPS_TRANSMITTER, "&4Émetteur GPS en carbonado", "", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(22));
    public static final SlimefunItemStack GPS_TRANSMITTER_4 = new SlimefunItemStack("GPS_TRANSMITTER_4", HeadTexture.GPS_TRANSMITTER, "&eÉmetteur GPS sur-évolué", "", LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(92));

    public static final SlimefunItemStack GPS_MARKER_TOOL = new SlimefunItemStack("GPS_MARKER_TOOL", Material.REDSTONE_TORCH, "&bOutil de marquage GPS", "", "&fVous permet d'appliquer un \"point de contrôle\"", "&flà où vous vous trouvez");
    public static final SlimefunItemStack GPS_CONTROL_PANEL = new SlimefunItemStack("GPS_CONTROL_PANEL", HeadTexture.GPS_CONTROL_PANEL, "&bPanneau de contrôle GPS", "", "&fVous permet de suivre vos satellites", "&fet de gérer vos points de contrôles");
    public static final SlimefunItemStack GPS_EMERGENCY_TRANSMITTER = new SlimefunItemStack("GPS_EMERGENCY_TRANSMITTER", HeadTexture.GPS_TRANSMITTER, "&cÉmetteur GPS d'urgence", "", "&fGarder ceci dans votre inventaire pour", "&fdéfinir automatiquement un point de contrôle", "&fà l'endroit où vous mourrez.");

    public static final SlimefunItemStack ANDROID_INTERFACE_FUEL = new SlimefunItemStack("ANDROID_INTERFACE_FUEL", Material.DISPENSER, "&7Interface Androïde &c(Carburant)", "", "&fLes objets stockés dans cette interface", "&fseront stockés dans l'emplacement de carburant d'un Androïde.", "&fquand les scripts lui disent de le faire");
    public static final SlimefunItemStack ANDROID_INTERFACE_ITEMS = new SlimefunItemStack("ANDROID_INTERFACE_ITEMS", Material.DISPENSER, "&7Interface Androïde &9(Objets)", "", "&fLes objets stockés dans cette interface", "&fseront stockés dans cette interface", "&fquand les scripts lui disent de le faire");

    public static final SlimefunItemStack GPS_GEO_SCANNER = new SlimefunItemStack("GPS_GEO_SCANNER", HeadTexture.GEO_SCANNER, "&bGéo-Scanner GPS", "", "&fRecherche des ressources naturelles dans une zone", "&fcomme du &8pétrole");
    public static final SlimefunItemStack PORTABLE_GEO_SCANNER = new SlimefunItemStack("PORTABLE_GEO_SCANNER", Material.CLOCK, "&bGéo-Scanner portable", "", "&fRecherche des ressources naturelles dans un Chunk", "", "&eRClic-droit&7 pour scanner la zone");
    public static final SlimefunItemStack GEO_MINER = new SlimefunItemStack("GEO_MINER", HeadTexture.GEO_MINER, "&6Géo-Mineur", "", "&eExtrait les ressources d'une zone", "&eCes ressources ne peuvent pas être minées avec une pioche", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(48), "", "&c&l! &cGéo-Scannez la zone avant toutes choses !");
    public static final SlimefunItemStack OIL_PUMP = new SlimefunItemStack("OIL_PUMP", HeadTexture.OIL_PUMP, "&4Pompe à pétrole", "", "&7Pompe le pétrole et le remplit dans des seaux.", "", "&c&l! &cGéo-Scannez la zone avant toutes choses !");
    public static final SlimefunItemStack OIL_BUCKET = new SlimefunItemStack("BUCKET_OF_OIL", HeadTexture.OIL_BUCKET, "&fSeau de pétrole");
    public static final SlimefunItemStack FUEL_BUCKET = new SlimefunItemStack("BUCKET_OF_FUEL", HeadTexture.FUEL_BUCKET, "&fSeau de carburant");

    public static final SlimefunItemStack REFINERY = new SlimefunItemStack("REFINERY", Material.PISTON, "&cRaffinerie", "", "&fRaffine le pétrole pour en faire du carburant");
    public static final SlimefunItemStack COMBUSTION_REACTOR = new SlimefunItemStack("COMBUSTION_REACTOR", HeadTexture.GENERATOR, "&cRéacteur à combustion", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.GENERATOR), LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(24));
    public static final SlimefunItemStack ANDROID_MEMORY_CORE = new SlimefunItemStack("ANDROID_MEMORY_CORE", HeadTexture.ENERGY_REGULATOR, "&bNoyau de mémoire d'Androïde");

    public static final SlimefunItemStack GPS_TELEPORTER_PYLON = new SlimefunItemStack("GPS_TELEPORTER_PYLON", Material.PURPLE_STAINED_GLASS, "&5Pylône de téléportation GPS", "", "&7Composant du téléporteur");
    public static final SlimefunItemStack GPS_TELEPORTATION_MATRIX = new SlimefunItemStack("GPS_TELEPORTATION_MATRIX", Material.IRON_BLOCK, "&bMatrice de téléportation GPS", "", "&fC'est le composant principal de votre téléporteur.", "&fCette matrice permet aux joueurs de choisir", "&fparmi tous les points de passage créés par", "&fle joueur qui a placé ce dispositif.");
    public static final SlimefunItemStack GPS_ACTIVATION_DEVICE_SHARED = new SlimefunItemStack("GPS_ACTIVATION_DEVICE_SHARED", Material.STONE_PRESSURE_PLATE, "&fDispositif d'activation du GPS &3(Partagé)", "", "&fPlacez ceci sur une matrice de téléportation", "&fet montez sur cette plaque pour activer", "&fle processus de téléportation");
    public static final SlimefunItemStack GPS_ACTIVATION_DEVICE_PERSONAL = new SlimefunItemStack("GPS_ACTIVATION_DEVICE_PERSONAL", Material.STONE_PRESSURE_PLATE, "&fDispositif d'activation du GPS &a(Personnel)", "", "&fPlacez ceci sur une matrice de téléportation", "&fet montez sur cette plaque pour activer", "&fle processus de téléportation", "", "&fCette version autorise uniquement le propriétaire", "&fde cet appareil à l'utiliser");
    public static final SlimefunItemStack PORTABLE_TELEPORTER = new SlimefunItemStack("PORTABLE_TELEPORTER", Material.COMPASS, "&bTéléporteur portable", "", "&fPlacez ceci sur une matrice de téléportation", "&fà vos points de contrôles qu'importe où vous êtes", "", LoreBuilder.powerCharged(0, 50), "", "&eClic-droit&7 pour l'utiliser");

    public static final SlimefunItemStack ELEVATOR_PLATE = new SlimefunItemStack("ELEVATOR_PLATE", Material.STONE_PRESSURE_PLATE, "&bÉlevateur", "", "&fPlacez une plaque d'ascenseur à chaque étage", "&fet vous pourrez vous téléporter entre eux.", "", "&eClic-droit sur ce bloc &7pour lui donner un nom");
    public static final SlimefunItemStack INFUSED_HOPPER = new SlimefunItemStack("INFUSED_HOPPER", Material.HOPPER, "&5Entonnoir infusé", "", "&fRécupère automatiquement les objets proches dans un rayon de 7x7x7", "&fune fois qu'il est placé.");

    public static final SlimefunItemStack HEATED_PRESSURE_CHAMBER = new SlimefunItemStack("HEATED_PRESSURE_CHAMBER", Material.LIGHT_GRAY_STAINED_GLASS, "&cChambre de pression chauffée", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(10));
    public static final SlimefunItemStack HEATED_PRESSURE_CHAMBER_2 = new SlimefunItemStack("HEATED_PRESSURE_CHAMBER_2", Material.LIGHT_GRAY_STAINED_GLASS, "&cChambre de pression chauffée &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(5), LoreBuilder.powerPerSecond(44));

    public static final SlimefunItemStack ELECTRIC_SMELTERY = new SlimefunItemStack("ELECTRIC_SMELTERY", Material.FURNACE, "&cFonderie électrique", "", "&4Alliages seulement, ne fond pas les poudres en lingots.", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(20));
    public static final SlimefunItemStack ELECTRIC_SMELTERY_2 = new SlimefunItemStack("ELECTRIC_SMELTERY_2", Material.FURNACE, "&cFonderie électrique &7- &eII", "", "&4Alliages seulement, ne fond pas les poudres en lingots.", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(3), LoreBuilder.powerPerSecond(40));

    public static final SlimefunItemStack ELECTRIC_PRESS = new SlimefunItemStack("ELECTRIC_PRESS", HeadTexture.ELECTRIC_PRESS, "&ePresse électrique", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(16));
    public static final SlimefunItemStack ELECTRIC_PRESS_2 = new SlimefunItemStack("ELECTRIC_PRESS_2", HeadTexture.ELECTRIC_PRESS, "&ePresse électrique &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(3), LoreBuilder.powerPerSecond(40));

    public static final SlimefunItemStack ELECTRIFIED_CRUCIBLE = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE", Material.RED_TERRACOTTA, "&cCreuset électrique", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(48));
    public static final SlimefunItemStack ELECTRIFIED_CRUCIBLE_2 = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE_2", Material.RED_TERRACOTTA, "&cCreuset électrique &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(2), LoreBuilder.powerPerSecond(80));
    public static final SlimefunItemStack ELECTRIFIED_CRUCIBLE_3 = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE_3", Material.RED_TERRACOTTA, "&cCreuset électrique &7- &eIII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(4), LoreBuilder.powerPerSecond(120));

    public static final SlimefunItemStack CARBON_PRESS = new SlimefunItemStack("CARBON_PRESS", Material.BLACK_STAINED_GLASS, "&cPresse carbone", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(20));
    public static final SlimefunItemStack CARBON_PRESS_2 = new SlimefunItemStack("CARBON_PRESS_2", Material.BLACK_STAINED_GLASS, "&Presse carbone &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(3), LoreBuilder.powerPerSecond(50));
    public static final SlimefunItemStack CARBON_PRESS_3 = new SlimefunItemStack("CARBON_PRESS_3", Material.BLACK_STAINED_GLASS, "&Presse carbone &7- &eIII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(15), LoreBuilder.powerPerSecond(180));

    public static final SlimefunItemStack BLISTERING_INGOT = new SlimefunItemStack("BLISTERING_INGOT", Material.GOLD_INGOT, "&6Lingot brulant radioactif &7(33%)", "", LoreBuilder.radioactive(Radioactivity.HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final SlimefunItemStack BLISTERING_INGOT_2 = new SlimefunItemStack("BLISTERING_INGOT_2", Material.GOLD_INGOT, "&6Lingot brulant radioactif &7(66%)", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final SlimefunItemStack BLISTERING_INGOT_3 = new SlimefunItemStack("BLISTERING_INGOT_3", Material.GOLD_INGOT, "&6Lingot brulant radioactif", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);

    public static final SlimefunItemStack ENERGY_REGULATOR = new SlimefunItemStack("ENERGY_REGULATOR", HeadTexture.ENERGY_REGULATOR, "&6Régulateur d'énergie", "", "&fComposant principal d'un réseau énergétique");
    public static final SlimefunItemStack ENERGY_CONNECTOR = new SlimefunItemStack("ENERGY_CONNECTOR", HeadTexture.ENERGY_CONNECTOR, "&eConnecteur d'énergie", LoreBuilder.range(6), "", "&fPlacez ceci entre des machines", "&fet des générateurs pour les connecter", "&fà votre régulateur.");
    public static final SlimefunItemStack DEBUG_FISH = new SlimefunItemStack("DEBUG_FISH", Material.SALMON, "&3How much is the Fish?", "", "&eClic-droit sur &fn'importe quel bloc &epour afficher les données du bloc", "&eClic-gauche &fpour casser le bloc", "&eShift + clic-gauche sur &fn'importe quel bloc &epour effacer les données du bloc", "&eShift + clic-droit &fpour placer un bloc de type PlaceHolder");

    public static final SlimefunItemStack NETHER_ICE = new SlimefunItemStack("NETHER_ICE", HeadTexture.NETHER_ICE, "&eGlace des Tréfonds", "", LoreBuilder.radioactive(Radioactivity.MODERATE), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final SlimefunItemStack ENRICHED_NETHER_ICE = new SlimefunItemStack("ENRICHED_NETHER_ICE", HeadTexture.ENRICHED_NETHER_ICE, "&eGlace des Tréfonds enrichis", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final SlimefunItemStack NETHER_ICE_COOLANT_CELL = new SlimefunItemStack("NETHER_ICE_COOLANT_CELL", HeadTexture.NETHER_ICE_COOLANT_CELL, "&6Cellule de refroidissement de la glace des Tréfonds");

    // Cargo
    public static final SlimefunItemStack CARGO_MANAGER = new SlimefunItemStack("CARGO_MANAGER", HeadTexture.CARGO_MANAGER, "&6Gestionnaire de cargaisons", "", "&fComposant central d'un réseau de transport d'articles");
    public static final SlimefunItemStack CARGO_CONNECTOR_NODE = new SlimefunItemStack("CARGO_NODE", HeadTexture.CARGO_CONNECTOR_NODE, "&7Nœud de cargaisons &c(Connecteur)", "", "&fTuyau de raccordement de cargaisons");
    public static final SlimefunItemStack CARGO_INPUT_NODE = new SlimefunItemStack("CARGO_NODE_INPUT", HeadTexture.CARGO_INPUT_NODE, "&7Nœud de cargaisons &c(Entrée)", "", "&fTuyau d'entrée de cargaison");
    public static final SlimefunItemStack CARGO_OUTPUT_NODE = new SlimefunItemStack("CARGO_NODE_OUTPUT", HeadTexture.CARGO_OUTPUT_NODE, "&7Nœud de cargaisons &c(Sortie)", "", "&fToyau de sortie de cargaison");
    public static final SlimefunItemStack CARGO_OUTPUT_NODE_2 = new SlimefunItemStack("CARGO_NODE_OUTPUT_ADVANCED", HeadTexture.CARGO_OUTPUT_NODE, "&6Nœud de cargaisons avancé &c(Sortie)", "", "&fTuyau de sortie de cargaison");

    // Animal farm
    public static final SlimefunItemStack AUTO_BREEDER = new SlimefunItemStack("AUTO_BREEDER", Material.HAY_BLOCK, "&eFabricateur In-Vitro", "", "&fFonctionen sur &ala nourriture organique", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerBuffer(1024), "&8\u21E8 &e\u26A1 &760 J/Animal");
    public static final SlimefunItemStack PRODUCE_COLLECTOR = new SlimefunItemStack("PRODUCE_COLLECTOR", Material.HAY_BLOCK, "&bCollecteur", "", "&fCette machine vous permet de", "&frecueillir les produits des animaux voisins.", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), LoreBuilder.powerBuffer(512), LoreBuilder.powerPerSecond(32));

    public static final SlimefunItemStack ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD", HeadTexture.FILLED_CAN, "&aNourriture organique", "&7Contenu: &9???");
    public static final SlimefunItemStack WHEAT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_WHEAT", HeadTexture.FILLED_CAN, ORGANIC_FOOD.getDisplayName(), "&7Contenu: &9Blés");
    public static final SlimefunItemStack CARROT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_CARROT", HeadTexture.FILLED_CAN, ORGANIC_FOOD.getDisplayName(), "&7Contenu: &9Carottes");
    public static final SlimefunItemStack POTATO_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_POTATO", HeadTexture.FILLED_CAN, ORGANIC_FOOD.getDisplayName(), "&7Contenu: &9Patates");
    public static final SlimefunItemStack SEEDS_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SEEDS", HeadTexture.FILLED_CAN, ORGANIC_FOOD.getDisplayName(), "&7Contenu: &9Graines");
    public static final SlimefunItemStack BEETROOT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_BEETROOT", HeadTexture.FILLED_CAN, ORGANIC_FOOD.getDisplayName(), "&7Contenu: &9Betteraves");
    public static final SlimefunItemStack MELON_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_MELON", HeadTexture.FILLED_CAN, ORGANIC_FOOD.getDisplayName(), "&7Contenu: &9Pastèques");
    public static final SlimefunItemStack APPLE_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_APPLE", HeadTexture.FILLED_CAN, ORGANIC_FOOD.getDisplayName(), "&7Contenu: &9Pommes");
    public static final SlimefunItemStack SWEET_BERRIES_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SWEET_BERRIES", HeadTexture.FILLED_CAN, ORGANIC_FOOD.getDisplayName(), "&7Contenu: &9Baies sucréess");
    public static final SlimefunItemStack KELP_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_KELP", HeadTexture.FILLED_CAN, ORGANIC_FOOD.getDisplayName(), "&7Contenu: &9Algues séchéess");
    public static final SlimefunItemStack COCOA_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_COCOA", HeadTexture.FILLED_CAN, ORGANIC_FOOD.getDisplayName(), "&7Contenu: &9Fèves de cacaos");

    public static final SlimefunItemStack FERTILIZER = new SlimefunItemStack("FERTILIZER", HeadTexture.FILLED_CAN, "&aFertiliseur organique", "&7Contenu: &9???");
    public static final SlimefunItemStack WHEAT_FERTILIZER = new SlimefunItemStack("FERTILIZER_WHEAT", HeadTexture.FILLED_CAN, FERTILIZER.getDisplayName(), "&7Contenu: &9Blés");
    public static final SlimefunItemStack CARROT_FERTILIZER = new SlimefunItemStack("FERTILIZER_CARROT", HeadTexture.FILLED_CAN, FERTILIZER.getDisplayName(), "&7Contenu: &9Carottes");
    public static final SlimefunItemStack POTATO_FERTILIZER = new SlimefunItemStack("FERTILIZER_POTATO", HeadTexture.FILLED_CAN, FERTILIZER.getDisplayName(), "&7Contenu: &9Patates");
    public static final SlimefunItemStack SEEDS_FERTILIZER = new SlimefunItemStack("FERTILIZER_SEEDS", HeadTexture.FILLED_CAN, FERTILIZER.getDisplayName(), "&7Contenu: &9Graines");
    public static final SlimefunItemStack BEETROOT_FERTILIZER = new SlimefunItemStack("FERTILIZER_BEETROOT", HeadTexture.FILLED_CAN, FERTILIZER.getDisplayName(), "&7Contenu: &9Betteraves");
    public static final SlimefunItemStack MELON_FERTILIZER = new SlimefunItemStack("FERTILIZER_MELON", HeadTexture.FILLED_CAN, FERTILIZER.getDisplayName(), "&7Contenu: &9Pastèques");
    public static final SlimefunItemStack APPLE_FERTILIZER = new SlimefunItemStack("FERTILIZER_APPLE", HeadTexture.FILLED_CAN, FERTILIZER.getDisplayName(), "&7Contenu: &9Pommes");
    public static final SlimefunItemStack SWEET_BERRIES_FERTILIZER = new SlimefunItemStack("FERTILIZER_SWEET_BERRIES", HeadTexture.FILLED_CAN, FERTILIZER.getDisplayName(), "&7Contenu: &9Baies sucréess");
    public static final SlimefunItemStack KELP_FERTILIZER = new SlimefunItemStack("FERTILIZER_KELP", HeadTexture.FILLED_CAN, FERTILIZER.getDisplayName(), "&7Contenu: &9Algues séchéess");
    public static final SlimefunItemStack COCOA_FERTILIZER = new SlimefunItemStack("FERTILIZER_COCOA", HeadTexture.FILLED_CAN, FERTILIZER.getDisplayName(), "&7Contenu: &9Fèves de cacaos");

    public static final SlimefunItemStack ANIMAL_GROWTH_ACCELERATOR = new SlimefunItemStack("ANIMAL_GROWTH_ACCELERATOR", Material.HAY_BLOCK, "&bAccélérateur de croissance animales", "", "&fFonctionne sur les nourritures organiques", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(28));
    public static final SlimefunItemStack CROP_GROWTH_ACCELERATOR = new SlimefunItemStack("CROP_GROWTH_ACCELERATOR", Material.LIME_TERRACOTTA, "&aAccélérateur de croissance végétales", "", "&fFonctionne sur les &afertiliseurs organiques", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Rayon: 7x7", "&8\u21E8 &7Vitesse: &a3/Temps", LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(50));
    public static final SlimefunItemStack CROP_GROWTH_ACCELERATOR_2 = new SlimefunItemStack("CROP_GROWTH_ACCELERATOR_2", Material.LIME_TERRACOTTA, "&aAccélérateur de croissance végétales &7(&eII&7)", "", "&fFonctionne sur les &afertiliseurs organiques", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Rayon: 9x9", "&8\u21E8 &7Vitesse: &a4/Temps", LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(60));
    public static final SlimefunItemStack TREE_GROWTH_ACCELERATOR = new SlimefunItemStack("TREE_GROWTH_ACCELERATOR", Material.BROWN_TERRACOTTA, "&aAccélérateur de croissance d'arbres", "", "&fFonctionne sur les &afertiliseurs organiques", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Rayon: 9x9", "&8\u21E8 &7Vitesse: &a4/Temps", LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(48));

    public static final SlimefunItemStack FOOD_FABRICATOR = new SlimefunItemStack("FOOD_FABRICATOR", Material.GREEN_STAINED_GLASS, "&cFabricateur de nourriture", "", "&fProduit de &ala nourriture organique", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(14));
    public static final SlimefunItemStack FOOD_FABRICATOR_2 = new SlimefunItemStack("FOOD_FABRICATOR_2", Material.GREEN_STAINED_GLASS, "&cFabricateur de nourriture &7(&eII&7)", "", "&fProduit de &ala nourriture organique", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(6), LoreBuilder.powerBuffer(512), LoreBuilder.powerPerSecond(48));

    public static final SlimefunItemStack FOOD_COMPOSTER = new SlimefunItemStack("FOOD_COMPOSTER", Material.GREEN_TERRACOTTA, "&cComposteur à nourriture", "", "&fProduit des &afertiliseurs organiques", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(16));
    public static final SlimefunItemStack FOOD_COMPOSTER_2 = new SlimefunItemStack("FOOD_COMPOSTER_2", Material.GREEN_TERRACOTTA, "&cComposteur à nourriture &7(&eII&7)", "", "&fProduit des &afertiliseurs organiques", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(10), LoreBuilder.powerBuffer(512), LoreBuilder.powerPerSecond(52));

    public static final SlimefunItemStack EXP_COLLECTOR = new SlimefunItemStack("XP_COLLECTOR", HeadTexture.EXP_COLLECTOR, "&aCollecteur d'expérience", "", "&fCollecte et stocke l'expérience à proximité", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(20));
    public static final SlimefunItemStack REACTOR_COOLANT_CELL = new SlimefunItemStack("REACTOR_COLLANT_CELL", HeadTexture.COOLANT_CELL, "&bCellule de refroidissement de réacteur");

    public static final SlimefunItemStack NUCLEAR_REACTOR = new SlimefunItemStack("NUCLEAR_REACTOR", HeadTexture.NUCLEAR_REACTOR, "&2Réacteur nucléaire", "", "&fRequiert du refroidissement !", "&8\u21E8 &bDoit être entouré d'eau", "&8\u21E8 &bDoit être équipé de cellules de refroidissement de réacteur.", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(16384), LoreBuilder.powerPerSecond(500));
    public static final SlimefunItemStack NETHER_STAR_REACTOR = new SlimefunItemStack("NETHERSTAR_REACTOR", HeadTexture.NETHER_STAR_REACTOR, "&fRéacteur de l'étoile des Tréfonds", "", "&fFonctionne avec des étoiles des Tréfonds", "&8\u21E8 &bDoit être entouré d'eau", "&8\u21E8 &bDoit être équipé de cellules de refroidissement de la glace du Néant", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(32768), LoreBuilder.powerPerSecond(1024), "&8\u21E8 &4Les entitées proches contractent l'effet Wither");
    public static final SlimefunItemStack REACTOR_ACCESS_PORT = new SlimefunItemStack("REACTOR_ACCESS_PORT", Material.CYAN_TERRACOTTA, "&2Port d'accès du réacteur", "", "&fVous permet d'intérargir avec le réacteur", "&fvia des nœuds de cargaisons, peut aussi être utilisé", "&fcomme un buffer (tampon)", "", "&8\u21E8 &eMust be placed &a3 Blocks &eabove the Reactor");

    public static final SlimefunItemStack FREEZER = new SlimefunItemStack("FREEZER", Material.LIGHT_BLUE_STAINED_GLASS, "&bCongélateur", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(18));
    public static final SlimefunItemStack FREEZER_2 = new SlimefunItemStack("FREEZER_2", Material.LIGHT_BLUE_STAINED_GLASS, "&bCongélateur &7(&eII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(2), LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(30));

    public static final SlimefunItemStack ELECTRIC_GOLD_PAN = new SlimefunItemStack("ELECTRIC_GOLD_PAN", Material.BROWN_TERRACOTTA, "&6Tamis électrique", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(2));
    public static final SlimefunItemStack ELECTRIC_GOLD_PAN_2 = new SlimefunItemStack("ELECTRIC_GOLD_PAN_2", Material.BROWN_TERRACOTTA, "&6Tamis électrique &7(&eII&7)", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), LoreBuilder.speed(3), LoreBuilder.powerPerSecond(4));
    public static final SlimefunItemStack ELECTRIC_GOLD_PAN_3 = new SlimefunItemStack("ELECTRIC_GOLD_PAN_3", Material.BROWN_TERRACOTTA, "&6Tamis électrique &7(&eIII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(10), LoreBuilder.powerPerSecond(14));

    public static final SlimefunItemStack ELECTRIC_DUST_WASHER = new SlimefunItemStack("ELECTRIC_DUST_WASHER", Material.BLUE_STAINED_GLASS, "&3Nettoyeur de poudres électrique", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(6));
    public static final SlimefunItemStack ELECTRIC_DUST_WASHER_2 = new SlimefunItemStack("ELECTRIC_DUST_WASHER_2", Material.BLUE_STAINED_GLASS, "&3Nettoyeur de poudres électrique &7(&eII&7)", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), LoreBuilder.speed(2), LoreBuilder.powerPerSecond(10));
    public static final SlimefunItemStack ELECTRIC_DUST_WASHER_3 = new SlimefunItemStack("ELECTRIC_DUST_WASHER_3", Material.BLUE_STAINED_GLASS, "&3Nettoyeur de poudres électrique &7(&eIII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(10), LoreBuilder.powerPerSecond(30));

    public static final SlimefunItemStack ELECTRIC_INGOT_FACTORY = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY", Material.RED_TERRACOTTA, "&cUsine de lingots électriques", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), LoreBuilder.speed(1), LoreBuilder.powerPerSecond(8));
    public static final SlimefunItemStack ELECTRIC_INGOT_FACTORY_2 = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY_2", Material.RED_TERRACOTTA, "&cUsine de lingots électriques &7(&eII&7)", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), LoreBuilder.speed(2), LoreBuilder.powerPerSecond(14));
    public static final SlimefunItemStack ELECTRIC_INGOT_FACTORY_3 = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY_3", Material.RED_TERRACOTTA, "&cUsine de lingots électriques &7(&eIII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.speed(8), LoreBuilder.powerPerSecond(40));

    public static final SlimefunItemStack FLUID_PUMP = new SlimefunItemStack("FLUID_PUMP", Material.BLUE_TERRACOTTA, "&9Pompe à fluides", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &732 J/Block");
    public static final SlimefunItemStack CHARGING_BENCH = new SlimefunItemStack("CHARGING_BENCH", Material.CRAFTING_TABLE, "&6Banque de recharge", "", "&fRecharge les objets comme les jetpacks", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), LoreBuilder.powerBuffer(128), "&8\u21E8 &e\u26A1 &7Perte d'énergie: &c50%");

    public static final SlimefunItemStack VANILLA_AUTO_CRAFTER = new SlimefunItemStack("VANILLA_AUTO_CRAFTER", HeadTexture.VANILLA_AUTO_CRAFTER, "&2Fabricateur automatique &8(Vanilla)", "", "&fPlacez cette machine sur le dessus d'un", "&fcoffre ou similaire et faites le fabriquer", "&ftout ce qui est fabricable de façon", "&fnormale &e(Établi classique)", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &716 J/Objet");
    public static final SlimefunItemStack ENHANCED_AUTO_CRAFTER = new SlimefunItemStack("ENHANCED_AUTO_CRAFTER", HeadTexture.ENHANCED_AUTO_CRAFTER, "&2Fabricateur automatique &8(Amélioré)", "", "&fPlacez cette machine sur le dessus d'un\", \"&fcoffre ou similaire et faites le fabriquer", "&ftout ce qui est fabricable", "&e(Établi amélioré)", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &716 J/Objet");
    public static final SlimefunItemStack ARMOR_AUTO_CRAFTER = new SlimefunItemStack("ARMOR_AUTO_CRAFTER", HeadTexture.ARMOR_AUTO_CRAFTER, "&2Fabricateur automatique &8(Forge d'armure)", "", "&&fPlacez cette machine sur le dessus d'un\", \"&fcoffre ou similaire et faites le fabriquer", "&ftout ce qui est fabricable", "&e(Armor Forge)", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &732 J/Objet");

    public static final SlimefunItemStack IRON_GOLEM_ASSEMBLER = new SlimefunItemStack("IRON_GOLEM_ASSEMBLER", Material.IRON_BLOCK, "&6Assembleur de golems de fer", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Temps de recharge: &b30 secondss", LoreBuilder.powerBuffer(4096), "&8\u21E8 &e\u26A1 &72048 J/Golem");
    public static final SlimefunItemStack WITHER_ASSEMBLER = new SlimefunItemStack("WITHER_ASSEMBLER", Material.OBSIDIAN, "&5Assembleur de Withers", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Temps de recharge: &b30 secondes", LoreBuilder.powerBuffer(4096), "&8\u21E8 &e\u26A1 &74096 J/Wither");

    public static final SlimefunItemStack TRASH_CAN = new SlimefunItemStack("TRASH_CAN_BLOCK", HeadTexture.TRASH_CAN, "&3Poubelle", "", "&fDétruira tout ce qui va dedans");

    public static final SlimefunItemStack ELYTRA_SCALE = new SlimefunItemStack("ELYTRA_SCALE", Material.FEATHER, "&bElytra Scale");
    public static final SlimefunItemStack INFUSED_ELYTRA = new SlimefunItemStack("INFUSED_ELYTRA", Material.ELYTRA, "&5Élytras infusés");
    public static final SlimefunItemStack SOULBOUND_ELYTRA = new SlimefunItemStack("SOULBOUND_ELYTRA", Material.ELYTRA, "&cÉlytras des âmes");

    public static final SlimefunItemStack MAGNESIUM_SALT = new SlimefunItemStack("MAGNESIUM_SALT", Material.SUGAR, "&cSel de magnésium", "", "&7Un type spécial de carburant qui peut être", "&7utilisé dans les générateurs à magnésium");
    public static final SlimefunItemStack MAGNESIUM_GENERATOR = new SlimefunItemStack("MAGNESIUM_GENERATOR", HeadTexture.GENERATOR, "&cGénérateur à magnésium", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.GENERATOR), LoreBuilder.powerBuffer(128), LoreBuilder.powerPerSecond(36));

    static {
        INFUSED_ELYTRA.addUnsafeEnchantment(Enchantment.MENDING, 1);
    }
}
