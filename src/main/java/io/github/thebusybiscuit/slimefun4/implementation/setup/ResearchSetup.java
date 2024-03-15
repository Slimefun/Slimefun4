package io.github.thebusybiscuit.slimefun4.implementation.setup;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ExplosiveShovel;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

/**
 * This static setup class is used to register all default implementations of
 * {@link Research} on startup.
 *
 * @see Research
 * @see SlimefunItems
 *
 */
public final class ResearchSetup {

    private static boolean alreadyRan = false;

    private ResearchSetup() {}

    public static void setupResearches() {
        if (alreadyRan) {
            throw new UnsupportedOperationException("Researches can only be registered once!");
        }

        alreadyRan = true;

        register("weapons", 0, "Weapons are the best defense", 69, SlimefunItems.SWORD_OF_BEHEADING, SlimefunItems.BLADE_OF_VAMPIRES, SlimefunItems.SEISMIC_AXE, new ItemStack(Material.TRIDENT));
        register("life_quality", 1, "Improving player life", 69, SlimefunItems.PORTABLE_CRAFTER, SlimefunItems.PORTABLE_DUSTBIN, SlimefunItems.RAG, SlimefunItems.BANDAGE, SlimefunItems.SPLINT, SlimefunItems.VITAMINS, SlimefunItems.MEDICINE, SlimefunItems.TAPE_MEASURE, SlimefunItems.COOLER, new ItemStack(Material.TOTEM_OF_UNDYING), SlimefunItems.FARMER_SHOES, SlimefunItems.ELYTRA_CAP, SlimefunItems.DUCT_TAPE);
        register("backpack", 2, "More inventory", 69, SlimefunItems.BACKPACK_SMALL, SlimefunItems.BACKPACK_MEDIUM, SlimefunItems.BACKPACK_LARGE, SlimefunItems.WOVEN_BACKPACK, SlimefunItems.GILDED_BACKPACK, SlimefunItems.RADIANT_BACKPACK, SlimefunItems.ENDER_BACKPACK);
        register("basic_machines", 3, "Beginning the technical era", 69, SlimefunItems.OUTPUT_CHEST, SlimefunItems.GRIND_STONE, SlimefunItems.ARMOR_FORGE, SlimefunItems.ORE_CRUSHER, SlimefunItems.COMPRESSOR, SlimefunItems.MAKESHIFT_SMELTERY, SlimefunItems.SMELTERY, SlimefunItems.IGNITION_CHAMBER, SlimefunItems.PRESSURE_CHAMBER, SlimefunItems.ORE_WASHER, SlimefunItems.TABLE_SAW, SlimefunItems.COMPOSTER, SlimefunItems.AUTOMATED_PANNING_MACHINE, SlimefunItems.INDUSTRIAL_MINER, SlimefunItems.CRUCIBLE, SlimefunItems.JUICER, SlimefunItems.BLOCK_PLACER);
        register("furnaces", 4, "let's warm up a bit", 69, SlimefunItems.ENHANCED_FURNACE, SlimefunItems.ENHANCED_FURNACE_2, SlimefunItems.ENHANCED_FURNACE_3, SlimefunItems.ENHANCED_FURNACE_4, SlimefunItems.ENHANCED_FURNACE_5, SlimefunItems.ENHANCED_FURNACE_6, SlimefunItems.ENHANCED_FURNACE_7, SlimefunItems.ENHANCED_FURNACE_8, SlimefunItems.ENHANCED_FURNACE_9, SlimefunItems.ENHANCED_FURNACE_10, SlimefunItems.ENHANCED_FURNACE_11, SlimefunItems.REINFORCED_FURNACE, SlimefunItems.CARBONADO_EDGED_FURNACE);
        register("basic_tools", 5, "Simple but useful tools", 69, SlimefunItems.CHAIN, SlimefunItems.GRAPPLING_HOOK, SlimefunItems.SMELTERS_PICKAXE, SlimefunItems.LUMBER_AXE, SlimefunItems.EXPLOSIVE_PICKAXE, SlimefunItems.EXPLOSIVE_SHOVEL, SlimefunItems.PICKAXE_OF_THE_SEEKER, SlimefunItems.COBALT_PICKAXE, SlimefunItems.PICKAXE_OF_VEIN_MINING, SlimefunItems.CLIMBING_PICK);
        register("alloys", 6, "Mixing Ingots to form alloys", 69, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.BRONZE_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.NICKEL_INGOT, SlimefunItems.COBALT_INGOT, SlimefunItems.FERROSILICON, SlimefunItems.GILDED_IRON, SlimefunItems.REDSTONE_ALLOY);
        register("refinery", 7, "Refining our production", 69, SlimefunItems.GOLD_4K, SlimefunItems.GOLD_6K, SlimefunItems.GOLD_8K, SlimefunItems.GOLD_10K, SlimefunItems.GOLD_12K, SlimefunItems.GOLD_14K, SlimefunItems.GOLD_16K, SlimefunItems.GOLD_18K, SlimefunItems.GOLD_20K, SlimefunItems.GOLD_22K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K_BLOCK, SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.SYNTHETIC_EMERALD, SlimefunItems.OIL_BUCKET, SlimefunItems.FUEL_BUCKET);
        register("radioactive", 8, "If you don't protect yourself, you'll become nuclear", 69, SlimefunItems.TINY_URANIUM, SlimefunItems.SMALL_URANIUM, SlimefunItems.URANIUM, SlimefunItems.BOOSTED_URANIUM, SlimefunItems.BLISTERING_INGOT, SlimefunItems.BLISTERING_INGOT_2, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.NETHER_ICE, SlimefunItems.ENRICHED_NETHER_ICE, SlimefunItems.NEPTUNIUM, SlimefunItems.PLUTONIUM, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.NETHER_ICE_COOLANT_CELL, SlimefunItems.HAZMAT_BOOTS, SlimefunItems.HAZMAT_LEGGINGS, SlimefunItems.HAZMAT_CHESTPLATE, SlimefunItems.SCUBA_HELMET);
        register("basic_components", 9, "Basic components to evolve", 69, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.BATTERY, SlimefunItems.STEEL_THRUSTER, SlimefunItems.POWER_CRYSTAL, SlimefunItems.SOLAR_PANEL, SlimefunItems.MAGNET, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.HEATING_COIL, SlimefunItems.COPPER_WIRE, SlimefunItems.COOLING_UNIT, SlimefunItems.PLASTIC_SHEET, SlimefunItems.REACTOR_COOLANT_CELL, SlimefunItems.STEEL_PLATE, SlimefunItems.TIN_CAN, SlimefunItems.REINFORCED_PLATE);
        register("armors", 10, "Put on your armor to defend yourself", 69, SlimefunItems.DAMASCUS_STEEL_BOOTS, SlimefunItems.DAMASCUS_STEEL_LEGGINGS, SlimefunItems.DAMASCUS_STEEL_CHESTPLATE, SlimefunItems.DAMASCUS_STEEL_HELMET, SlimefunItems.REINFORCED_ALLOY_BOOTS, SlimefunItems.REINFORCED_ALLOY_LEGGINGS, SlimefunItems.REINFORCED_ALLOY_CHESTPLATE, SlimefunItems.REINFORCED_ALLOY_HELMET, SlimefunItems.CACTUS_BOOTS, SlimefunItems.CACTUS_LEGGINGS, SlimefunItems.CACTUS_CHESTPLATE, SlimefunItems.CACTUS_HELMET, SlimefunItems.GILDED_IRON_BOOTS, SlimefunItems.GILDED_IRON_LEGGINGS, SlimefunItems.GILDED_IRON_CHESTPLATE, SlimefunItems.GILDED_IRON_HELMET, SlimefunItems.GOLDEN_BOOTS_12K, SlimefunItems.GOLDEN_LEGGINGS_12K, SlimefunItems.GOLDEN_CHESTPLATE_12K, SlimefunItems.GOLDEN_HELMET_12K);
        register("jet_armors", 11, "To infinity and beyond, or not", 69, SlimefunItems.DURALUMIN_JETPACK, SlimefunItems.SOLDER_JETPACK, SlimefunItems.BILLON_JETPACK, SlimefunItems.STEEL_JETPACK, SlimefunItems.DAMASCUS_STEEL_JETPACK, SlimefunItems.REINFORCED_ALLOY_JETPACK, SlimefunItems.CARBONADO_JETPACK, SlimefunItems.ARMORED_JETPACK, SlimefunItems.DURALUMIN_JETBOOTS, SlimefunItems.SOLDER_JETBOOTS, SlimefunItems.BILLON_JETBOOTS, SlimefunItems.STEEL_JETBOOTS, SlimefunItems.DAMASCUS_STEEL_JETBOOTS, SlimefunItems.REINFORCED_ALLOY_JETBOOTS, SlimefunItems.CARBONADO_JETBOOTS, SlimefunItems.ARMORED_JETBOOTS);
        register("energy_tools", 12, "Energy tools", 69, SlimefunItems.DURALUMIN_MULTI_TOOL, SlimefunItems.SOLDER_MULTI_TOOL, SlimefunItems.BILLON_MULTI_TOOL, SlimefunItems.STEEL_MULTI_TOOL, SlimefunItems.DAMASCUS_STEEL_MULTI_TOOL, SlimefunItems.REINFORCED_ALLOY_MULTI_TOOL, SlimefunItems.CARBONADO_MULTI_TOOL, SlimefunItems.MULTIMETER);
        register("basic_energy", 13, "basic of energy", 69, SlimefunItems.ENERGY_REGULATOR, SlimefunItems.ENERGY_CONNECTOR, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.BIG_CAPACITOR, SlimefunItems.SOLAR_GENERATOR, SlimefunItems.SOLAR_GENERATOR_2, SlimefunItems.COAL_GENERATOR, SlimefunItems.COAL_GENERATOR_2, SlimefunItems.BIO_REACTOR, SlimefunItems.LAVA_GENERATOR, SlimefunItems.COMBUSTION_REACTOR, SlimefunItems.MAGNESIUM_GENERATOR);
        register("advanced_energy", 14, "advanced energy system", 69, SlimefunItems.SOLAR_GENERATOR_3, SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.LAVA_GENERATOR_2, SlimefunItems.NUCLEAR_REACTOR, SlimefunItems.NETHER_STAR_REACTOR);
        register("basic_ore_processing", 15, "the start of real mineral processing", 69, SlimefunItems.ELECTRIC_FURNACE, SlimefunItems.ELECTRIC_GOLD_PAN, SlimefunItems.ELECTRIC_DUST_WASHER, SlimefunItems.ELECTRIC_INGOT_FACTORY, SlimefunItems.ELECTRIC_ORE_GRINDER, SlimefunItems.ELECTRIC_INGOT_PULVERIZER, SlimefunItems.ELECTRIC_SMELTERY);
        register("advanced_ore_processing", 16, "Advanced Ore Processing", 69, SlimefunItems.ELECTRIC_INGOT_FACTORY_2, SlimefunItems.ELECTRIC_FURNACE_3, SlimefunItems.ELECTRIC_GOLD_PAN_2, SlimefunItems.ELECTRIC_GOLD_PAN_3, SlimefunItems.ELECTRIC_DUST_WASHER_2, SlimefunItems.ELECTRIC_DUST_WASHER_3, SlimefunItems.ELECTRIC_INGOT_FACTORY_2, SlimefunItems.ELECTRIC_INGOT_FACTORY_3, SlimefunItems.ELECTRIC_ORE_GRINDER_2, SlimefunItems.ELECTRIC_ORE_GRINDER_3, SlimefunItems.ELECTRIC_SMELTERY_2);
        register("basic_machine", 17, "basic machines", 69, SlimefunItems.CHARGING_BENCH,SlimefunItems.ELECTRIFIED_CRUCIBLE_2 , SlimefunItems.ELECTRIFIED_CRUCIBLE_2, SlimefunItems.HEATED_PRESSURE_CHAMBER, SlimefunItems.AUTO_DRIER, SlimefunItems.ELECTRIC_PRESS, SlimefunItems.AUTO_ANVIL, SlimefunItems.FOOD_FABRICATOR, SlimefunItems.AUTO_BREEDER, SlimefunItems.ANIMAL_GROWTH_ACCELERATOR, SlimefunItems.TREE_GROWTH_ACCELERATOR, SlimefunItems.FOOD_COMPOSTER, SlimefunItems.CROP_GROWTH_ACCELERATOR, SlimefunItems.CROP_GROWTH_ACCELERATOR_2, SlimefunItems.FREEZER, SlimefunItems.FREEZER_2, SlimefunItems.FLUID_PUMP, SlimefunItems.CARBON_PRESS, SlimefunItems.PRODUCE_COLLECTOR, SlimefunItems.AUTO_BREWER);
        register("advanced_machine", 18, "advanced machines", 69, SlimefunItems.ELECTRIFIED_CRUCIBLE_3, SlimefunItems.AUTO_ANVIL_2, SlimefunItems.HEATED_PRESSURE_CHAMBER_2, SlimefunItems.ELECTRIC_PRESS_2, SlimefunItems.REFINERY, SlimefunItems.FOOD_FABRICATOR_2, SlimefunItems.FOOD_COMPOSTER_2, SlimefunItems.FREEZER_3, SlimefunItems.CARBON_PRESS_2, SlimefunItems.CARBON_PRESS_3, SlimefunItems.IRON_GOLEM_ASSEMBLER, SlimefunItems.WITHER_ASSEMBLER);
        register("basic_enchanting", 19, "basic enchanting", 69, SlimefunItems.AUTO_ENCHANTER, SlimefunItems.AUTO_DISENCHANTER, SlimefunItems.BOOK_BINDER);
        register("advanced_enchanting", 20, "advanced enchanting", 69, SlimefunItems.AUTO_ENCHANTER_2, SlimefunItems.AUTO_DISENCHANTER_2, SlimefunItems.EXP_COLLECTOR);
        register("gps", 21, "gps system", 69, SlimefunItems.GPS_TRANSMITTER, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.GPS_TRANSMITTER_4, SlimefunItems.GPS_CONTROL_PANEL, SlimefunItems.GPS_MARKER_TOOL, SlimefunItems.GPS_EMERGENCY_TRANSMITTER, SlimefunItems.GPS_GEO_SCANNER, SlimefunItems.OIL_PUMP, SlimefunItems.GEO_MINER, SlimefunItems.GPS_TELEPORTER_PYLON, SlimefunItems.GPS_TELEPORTATION_MATRIX, SlimefunItems.PORTABLE_TELEPORTER, SlimefunItems.PORTABLE_GEO_SCANNER);
        register("basic_androids", 22, "Basic android System", 69, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.ANDROID_INTERFACE_FUEL, SlimefunItems.ANDROID_INTERFACE_ITEMS, SlimefunItems.PROGRAMMABLE_ANDROID, SlimefunItems.PROGRAMMABLE_ANDROID_MINER, SlimefunItems.PROGRAMMABLE_ANDROID_FARMER, SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER, SlimefunItems.PROGRAMMABLE_ANDROID_FISHERMAN, SlimefunItems.PROGRAMMABLE_ANDROID_BUTCHER);
        register("advanced_androids", 23,"Advanced android System", 69, SlimefunItems.PROGRAMMABLE_ANDROID_2, SlimefunItems.PROGRAMMABLE_ANDROID_2_FISHERMAN, SlimefunItems.PROGRAMMABLE_ANDROID_2_FARMER, SlimefunItems.PROGRAMMABLE_ANDROID_2_BUTCHER);
        register("empowered_androids", 24, "Empowered android System", 69, SlimefunItems.PROGRAMMABLE_ANDROID_3, SlimefunItems.PROGRAMMABLE_ANDROID_3_FISHERMAN, SlimefunItems.PROGRAMMABLE_ANDROID_3_BUTCHER);
        register("cargo_system", 25, "Cargo System, time to start the logistics system", 69, SlimefunItems.CARGO_MOTOR, SlimefunItems.CARGO_MANAGER, SlimefunItems.CARGO_CONNECTOR_NODE, SlimefunItems.CARGO_INPUT_NODE, SlimefunItems.CARGO_OUTPUT_NODE, SlimefunItems.CARGO_OUTPUT_NODE_2, SlimefunItems.REACTOR_ACCESS_PORT, SlimefunItems.CRAFTING_MOTOR, SlimefunItems.VANILLA_AUTO_CRAFTER, SlimefunItems.ENHANCED_AUTO_CRAFTER, SlimefunItems.ARMOR_AUTO_CRAFTER);
        register("magic_basic", 26, "Getting started in magic", 666, SlimefunItems.MAGIC_WORKBENCH, SlimefunItems.MAGIC_LUMP_1, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ENDER_LUMP_1, SlimefunItems.ENDER_LUMP_2, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.MAGICAL_GLASS, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.STRANGE_NETHER_GOO, SlimefunItems.NECROTIC_SKULL, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.SYNTHETIC_SHULKER_SHELL);
        register("magic_advanced", 27, "Technology and magic go hand in hand", 666, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.MAGICAL_ZOMBIE_PILLS, SlimefunItems.INFUSED_MAGNET, SlimefunItems.ANCIENT_PEDESTAL, SlimefunItems.ANCIENT_ALTAR, SlimefunItems.INFERNAL_BONEMEAL, SlimefunItems.ELYTRA_SCALE, SlimefunItems.INFUSED_HOPPER);
        register("talisman_i", 28, "Talismans are the basics of the user's magic", 666, SlimefunItems.COMMON_TALISMAN, SlimefunItems.TALISMAN_ANVIL, SlimefunItems.TALISMAN_MINER, SlimefunItems.TALISMAN_FARMER, SlimefunItems.TALISMAN_HUNTER, SlimefunItems.TALISMAN_LAVA, SlimefunItems.TALISMAN_WATER, SlimefunItems.TALISMAN_ANGEL, SlimefunItems.TALISMAN_FIRE, SlimefunItems.TALISMAN_MAGICIAN, SlimefunItems.TALISMAN_TRAVELLER, SlimefunItems.TALISMAN_WARRIOR, SlimefunItems.TALISMAN_KNIGHT, SlimefunItems.TALISMAN_CAVEMAN, SlimefunItems.TALISMAN_WISE, SlimefunItems.TALISMAN_WHIRLWIND, SlimefunItems.TALISMAN_WIZARD);
        register("runic_crafter", 29, "The creation of runes is an important part of evolution", 666, SlimefunItems.BLANK_RUNE, SlimefunItems.AIR_RUNE, SlimefunItems.WATER_RUNE, SlimefunItems.EARTH_RUNE, SlimefunItems.FIRE_RUNE, SlimefunItems.LIGHTNING_RUNE, SlimefunItems.RAINBOW_RUNE, SlimefunItems.SOULBOUND_RUNE, SlimefunItems.ENCHANTMENT_RUNE, SlimefunItems.VILLAGER_RUNE);
        register("ender_talismans", 30, "Ender Talismans", 666, new String[0]);
        register("magic_armor", 31, "The Magic Defence", 666, SlimefunItems.ENDER_BOOTS, SlimefunItems.ENDER_LEGGINGS, SlimefunItems.ENDER_CHESTPLATE, SlimefunItems.ENDER_HELMET, SlimefunItems.SLIME_BOOTS, SlimefunItems.SLIME_LEGGINGS, SlimefunItems.SLIME_CHESTPLATE, SlimefunItems.SLIME_HELMET, SlimefunItems.SLIME_BOOTS_STEEL, SlimefunItems.SLIME_LEGGINGS_STEEL, SlimefunItems.SLIME_CHESTPLATE_STEEL, SlimefunItems.SLIME_HELMET_STEEL, SlimefunItems.GLOWSTONE_BOOTS, SlimefunItems.GLOWSTONE_LEGGINGS, SlimefunItems.GLOWSTONE_CHESTPLATE, SlimefunItems.GLOWSTONE_HELMET, SlimefunItems.BEE_BOOTS, SlimefunItems.BEE_LEGGINGS, SlimefunItems.BEE_WINGS, SlimefunItems.BEE_HELMET);
        register("rainbow", 32, "RGB EVERYWHERE", 666, SlimefunItems.RAINBOW_WOOL, SlimefunItems.RAINBOW_GLASS, SlimefunItems.RAINBOW_GLASS_PANE, SlimefunItems.RAINBOW_CLAY, SlimefunItems.RAINBOW_CONCRETE, SlimefunItems.RAINBOW_GLAZED_TERRACOTTA, SlimefunItems.RAINBOW_BOOTS, SlimefunItems.RAINBOW_LEGGINGS, SlimefunItems.RAINBOW_CHESTPLATE, SlimefunItems.RAINBOW_HELMET, SlimefunItems.RAINBOW_LEATHER);
    }

    @ParametersAreNonnullByDefault
    private static void register(String key, int id, String name, int defaultCost, ItemStack... items) {
        Research research = new Research(new NamespacedKey(Slimefun.instance(), key), id, name, defaultCost);

        for (ItemStack item : items) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);

            if (sfItem != null) {
                research.addItems(sfItem);
            }
        }

        research.register();
    }

    @ParametersAreNonnullByDefault
    private static void register(String key, int id, String name, int defaultCost, String... items) {
        Research research = new Research(new NamespacedKey(Slimefun.instance(), key), id, name, defaultCost);

        for (String itemId : items) {
            SlimefunItem sfItem = SlimefunItem.getById(itemId);

            if (sfItem != null) {
                research.addItems(sfItem);
            }
        }

        research.register();
    }
}