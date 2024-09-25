package io.github.thebusybiscuit.slimefun4.implementation.setup;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.utils.multiversion.StackResolver;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.core.handlers.RainbowTickHandler;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.EnchantedItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.HiddenItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.RadioactiveItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientAltar;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientPedestal;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.AndroidInterface;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.ButcherAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.FarmerAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.FishermanAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.MinerAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.ProgrammableAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.WoodcutterAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.ElytraCap;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.EnderBoots;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.FarmerShoes;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.HazmatArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.LongFallBoots;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.Parachute;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.RainbowArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.StomperBoots;
import io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters.ArmorAutoCrafter;
import io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters.EnhancedAutoCrafter;
import io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters.VanillaAutoCrafter;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.Cooler;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.EnderBackpack;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.RestoredBackpack;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SoulboundBackpack;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.BlockPlacer;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.BrokenSpawner;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.Composter;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.Crucible;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.EnhancedFurnace;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.HardenedGlass;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.HologramProjector;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.IgnitionChamber;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.OutputChest;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.RainbowBlock;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.RepairedSpawner;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.WitherProofBlock;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.AdvancedCargoOutputNode;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoConnectorNode;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoInputNode;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoManager;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoOutputNode;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.ReactorAccessPort;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.TrashCan;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyConnector;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyRegulator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.JetBoots;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.Jetpack;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.MultiTool;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.Multimeter;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.SolarHelmet;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.BioGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.CoalGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.CombustionGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.LavaGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.MagnesiumGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.AutoAnvil;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.AutoBrewer;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.AutoDrier;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.CarbonPress;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ChargingBench;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricDustWasher;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricFurnace;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricGoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricIngotFactory;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricIngotPulverizer;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricOreGrinder;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricPress;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricSmeltery;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectrifiedCrucible;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.FluidPump;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.FoodComposter;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.FoodFabricator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.Freezer;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.HeatedPressureChamber;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.Refinery;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.accelerators.AnimalGrowthAccelerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.accelerators.CropGrowthAccelerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.accelerators.TreeGrowthAccelerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.enchanting.AutoDisenchanter;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.enchanting.AutoEnchanter;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.enchanting.BookBinder;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.entities.AutoBreeder;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.entities.ExpCollector;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.entities.IronGolemAssembler;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.entities.ProduceCollector;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.entities.WitherAssembler;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.NetherStarReactor;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.NuclearReactor;
import io.github.thebusybiscuit.slimefun4.implementation.items.elevator.ElevatorPlate;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.BirthdayCake;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.DietCookie;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.FortuneCookie;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.HeavyCream;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.MagicSugar;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.MeatJerky;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.MonsterJerky;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOMiner;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOScanner;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.OilPump;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.PortableGEOScanner;
import io.github.thebusybiscuit.slimefun4.implementation.items.gps.GPSControlPanel;
import io.github.thebusybiscuit.slimefun4.implementation.items.gps.GPSMarkerTool;
import io.github.thebusybiscuit.slimefun4.implementation.items.gps.GPSTransmitter;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.BeeWings;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.InfernalBonemeal;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.InfusedHopper;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.InfusedMagnet;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.KnowledgeFlask;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.KnowledgeTome;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.MagicEyeOfEnder;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.MagicalZombiePills;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.SoulboundItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.TelepositionScroll;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.runes.ElementalRune;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.runes.EnchantmentRune;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.runes.SoulboundRune;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.runes.VillagerRune;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves.StormStaff;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves.WaterStaff;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves.WindStaff;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.MagicianTalisman;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.Talisman;
import io.github.thebusybiscuit.slimefun4.implementation.items.medical.Bandage;
import io.github.thebusybiscuit.slimefun4.implementation.items.medical.Medicine;
import io.github.thebusybiscuit.slimefun4.implementation.items.medical.Splint;
import io.github.thebusybiscuit.slimefun4.implementation.items.medical.Vitamins;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.AlloyIngot;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.BasicCircuitBoard;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.CoolantCell;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.GoldIngot;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.OrganicFertilizer;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.OrganicFood;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.SteelThruster;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.StrangeNetherGoo;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.SyntheticEmerald;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.ArmorForge;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.AutomatedPanningMachine;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.Compressor;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.GrindStone;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.Juicer;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.MagicWorkbench;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.MakeshiftSmeltery;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.OreCrusher;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.OreWasher;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.PressureChamber;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.Smeltery;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.TableSaw;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner.AdvancedIndustrialMiner;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner.IndustrialMiner;
import io.github.thebusybiscuit.slimefun4.implementation.items.seasonal.ChristmasPresent;
import io.github.thebusybiscuit.slimefun4.implementation.items.seasonal.EasterEgg;
import io.github.thebusybiscuit.slimefun4.implementation.items.teleporter.PersonalActivationPlate;
import io.github.thebusybiscuit.slimefun4.implementation.items.teleporter.PortableTeleporter;
import io.github.thebusybiscuit.slimefun4.implementation.items.teleporter.SharedActivationPlate;
import io.github.thebusybiscuit.slimefun4.implementation.items.teleporter.Teleporter;
import io.github.thebusybiscuit.slimefun4.implementation.items.teleporter.TeleporterPylon;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ClimbingPick;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ExplosivePickaxe;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ExplosiveShovel;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GrapplingHook;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.LumberAxe;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.NetherGoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.PickaxeOfContainment;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.PickaxeOfTheSeeker;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.PickaxeOfVeinMining;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.PortableCrafter;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.PortableDustbin;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.SmeltersPickaxe;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.TapeMeasure;
import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.ExplosiveBow;
import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.IcyBow;
import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.SeismicAxe;
import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.SwordOfBeheading;
import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.VampireBlade;
import io.github.thebusybiscuit.slimefun4.utils.ColoredMaterial;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedPotionEffectType;

/**
 * This class holds the recipes of all items.
 * This is the place where all items from Slimefun are registered.
 *
 */
public final class SlimefunItemSetup {

    private static boolean registeredItems = false;

    private SlimefunItemSetup() {}

    public static void setup(@Nonnull Slimefun plugin) {
        if (registeredItems) {
            throw new UnsupportedOperationException("Slimefun Items can only be registered once!");
        }

        registeredItems = true;
        DefaultItemGroups itemGroups = new DefaultItemGroups();

        // @formatter:off (We will need to refactor this one day)
        new SlimefunItem(itemGroups.weapons, SlimefunItems.GRANDMAS_WALKING_STICK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.OAK_LOG), null, null, StackResolver.of(Material.OAK_LOG), null, null, StackResolver.of(Material.OAK_LOG), null})
        .register(plugin);

        new SlimefunItem(itemGroups.weapons, SlimefunItems.GRANDPAS_WALKING_STICK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.LEATHER), StackResolver.of(Material.OAK_LOG), StackResolver.of(Material.LEATHER), null, StackResolver.of(Material.OAK_LOG), null, null, StackResolver.of(Material.OAK_LOG), null})
        .register(plugin);

        new PortableCrafter(itemGroups.usefulItems, SlimefunItems.PORTABLE_CRAFTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.BOOK), StackResolver.of(Material.CRAFTING_TABLE), null, null, null, null, null, null, null})
        .register(plugin);

        new FortuneCookie(itemGroups.food, SlimefunItems.FORTUNE_COOKIE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.COOKIE), StackResolver.of(Material.PAPER), null, null, null, null, null, null, null})
        .register(plugin);

        new DietCookie(itemGroups.food, SlimefunItems.DIET_COOKIE, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {StackResolver.of(Material.COOKIE), SlimefunItems.ELYTRA_SCALE, null, null, null, null, null, null, null})
        .register(plugin);

        new OutputChest(itemGroups.basicMachines, SlimefunItems.OUTPUT_CHEST, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.LEAD_INGOT, StackResolver.of(Material.HOPPER), SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, StackResolver.of(Material.CHEST), SlimefunItems.LEAD_INGOT, null, SlimefunItems.LEAD_INGOT, null})
        .register(plugin);

        new EnhancedCraftingTable(itemGroups.basicMachines, SlimefunItems.ENHANCED_CRAFTING_TABLE).register(plugin);

        new PortableDustbin(itemGroups.usefulItems, SlimefunItems.PORTABLE_DUSTBIN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.IRON_INGOT), null, StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.IRON_INGOT)})
        .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.BEEF_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SALT, StackResolver.of(Material.COOKED_BEEF), null, null, null, null, null, null, null})
        .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.PORK_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SALT, StackResolver.of(Material.COOKED_PORKCHOP), null, null, null, null, null, null, null})
        .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.CHICKEN_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SALT, StackResolver.of(Material.COOKED_CHICKEN), null, null, null, null, null, null, null})
        .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.MUTTON_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SALT, StackResolver.of(Material.COOKED_MUTTON), null, null, null, null, null, null, null})
        .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.RABBIT_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SALT, StackResolver.of(Material.COOKED_RABBIT), null, null, null, null, null, null, null})
        .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.FISH_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SALT, StackResolver.of(Material.COOKED_COD), null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.food, SlimefunItems.KELP_COOKIE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.DRIED_KELP), null, StackResolver.of(Material.DRIED_KELP), StackResolver.of(Material.SUGAR), StackResolver.of(Material.DRIED_KELP), null, StackResolver.of(Material.DRIED_KELP), null},
        new SlimefunItemStack(SlimefunItems.KELP_COOKIE, 2))
        .register(plugin);

        new GrindStone(itemGroups.basicMachines, SlimefunItems.GRIND_STONE).register(plugin);
        new ArmorForge(itemGroups.basicMachines, SlimefunItems.ARMOR_FORGE).register(plugin);

        OreCrusher oreCrusher = new OreCrusher(itemGroups.basicMachines, SlimefunItems.ORE_CRUSHER);
        oreCrusher.register(plugin);

        new Compressor(itemGroups.basicMachines, SlimefunItems.COMPRESSOR).register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.MAGIC_LUMP_1, RecipeType.GRIND_STONE,
        new ItemStack[] {StackResolver.of(Material.NETHER_WART), null, null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_1, 2))
        .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.MAGIC_LUMP_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_1, SlimefunItems.MAGIC_LUMP_1, null, SlimefunItems.MAGIC_LUMP_1, SlimefunItems.MAGIC_LUMP_1, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.MAGIC_LUMP_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.MAGIC_LUMP_2, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.ENDER_LUMP_1, RecipeType.GRIND_STONE,
        new ItemStack[] {StackResolver.of(Material.ENDER_EYE), null, null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.ENDER_LUMP_1, 2))
        .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.ENDER_LUMP_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_1, SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1, SlimefunItems.ENDER_LUMP_1, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.ENDER_LUMP_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_2, SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2, SlimefunItems.ENDER_LUMP_2, null, null, null, null})
        .register(plugin);

        new EnderBackpack(itemGroups.magicalGadgets, SlimefunItems.ENDER_BACKPACK, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_2, StackResolver.of(Material.LEATHER), SlimefunItems.ENDER_LUMP_2, StackResolver.of(Material.LEATHER), StackResolver.of(Material.CHEST), StackResolver.of(Material.LEATHER), SlimefunItems.ENDER_LUMP_2, StackResolver.of(Material.LEATHER), SlimefunItems.ENDER_LUMP_2})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalArmor, SlimefunItems.ENDER_HELMET, RecipeType.ARMOR_FORGE,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_1, StackResolver.of(Material.ENDER_EYE), SlimefunItems.ENDER_LUMP_1, StackResolver.of(Material.OBSIDIAN), null, StackResolver.of(Material.OBSIDIAN), null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalArmor, SlimefunItems.ENDER_CHESTPLATE, RecipeType.ARMOR_FORGE,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1, StackResolver.of(Material.OBSIDIAN), StackResolver.of(Material.ENDER_EYE), StackResolver.of(Material.OBSIDIAN), StackResolver.of(Material.OBSIDIAN), StackResolver.of(Material.OBSIDIAN), StackResolver.of(Material.OBSIDIAN)})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalArmor, SlimefunItems.ENDER_LEGGINGS, RecipeType.ARMOR_FORGE,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_1, StackResolver.of(Material.ENDER_EYE), SlimefunItems.ENDER_LUMP_1, StackResolver.of(Material.OBSIDIAN), null, StackResolver.of(Material.OBSIDIAN), StackResolver.of(Material.OBSIDIAN), null, StackResolver.of(Material.OBSIDIAN)})
        .register(plugin);

        new EnderBoots(itemGroups.magicalArmor, SlimefunItems.ENDER_BOOTS, RecipeType.ARMOR_FORGE,
        new ItemStack[] {null, null, null, SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1, StackResolver.of(Material.OBSIDIAN), null, StackResolver.of(Material.OBSIDIAN)})
        .register(plugin);

        new MagicEyeOfEnder(itemGroups.magicalGadgets, SlimefunItems.MAGIC_EYE_OF_ENDER, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_2, StackResolver.of(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2, StackResolver.of(Material.ENDER_PEARL), StackResolver.of(Material.ENDER_EYE), StackResolver.of(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2, StackResolver.of(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2})
        .register(plugin);

        new MagicSugar(itemGroups.food, SlimefunItems.MAGIC_SUGAR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.SUGAR), StackResolver.of(Material.REDSTONE), StackResolver.of(Material.GLOWSTONE_DUST), null, null, null, null, null, null})
        .register(plugin);

        new MonsterJerky(itemGroups.food, SlimefunItems.MONSTER_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SALT, StackResolver.of(Material.ROTTEN_FLESH), null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_HELMET, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.IRON_INGOT), null, StackResolver.of(Material.IRON_INGOT), null, null, null}, null)
        .register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_CHESTPLATE, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.SLIME_BALL), null, StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.IRON_INGOT)}, null)
        .register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_LEGGINGS, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.IRON_INGOT), null, StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.IRON_INGOT), null, StackResolver.of(Material.IRON_INGOT)},
        new PotionEffect[] {new PotionEffect(PotionEffectType.SPEED, 300, 2)})
        .register(plugin);

        new LongFallBoots(itemGroups.magicalArmor, SlimefunItems.SLIME_BOOTS, RecipeType.ARMOR_FORGE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.SLIME_BALL), null, StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.IRON_INGOT), null, StackResolver.of(Material.IRON_INGOT)},
        new PotionEffect[] {new PotionEffect(VersionedPotionEffectType.JUMP_BOOST, 300, 5)},
            SoundEffect.SLIME_BOOTS_FALL_SOUND)
        .register(plugin);

        new SwordOfBeheading(itemGroups.weapons, SlimefunItems.SWORD_OF_BEHEADING, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.EMERALD), null, SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.EMERALD), SlimefunItems.MAGIC_LUMP_2, null, StackResolver.of(Material.BLAZE_ROD), null})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.MAGICAL_BOOK_COVER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.BOOK), SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, null})
        .register(plugin);

        new UnplaceableBlock(itemGroups.magicalResources, SlimefunItems.MAGICAL_GLASS, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_DUST, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, StackResolver.of(Material.GLASS_PANE), SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.MAGIC_LUMP_2})
        .register(plugin);

        new BasicCircuitBoard(itemGroups.technicalComponents, SlimefunItems.BASIC_CIRCUIT_BOARD, RecipeType.MOB_DROP,
        new ItemStack[] {null, null, null, null, new CustomItemStack(SlimefunUtils.getCustomHead(HeadTexture.IRON_GOLEM.getTexture()), "&aIron Golem"), null, null, null, null})
        .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.ADVANCED_CIRCUIT_BOARD, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.LAPIS_BLOCK), StackResolver.of(Material.LAPIS_BLOCK), StackResolver.of(Material.LAPIS_BLOCK), StackResolver.of(Material.REDSTONE_BLOCK), SlimefunItems.BASIC_CIRCUIT_BOARD, StackResolver.of(Material.REDSTONE_BLOCK), StackResolver.of(Material.LAPIS_BLOCK), StackResolver.of(Material.LAPIS_BLOCK), StackResolver.of(Material.LAPIS_BLOCK)})
        .register(plugin);

        new GoldPan(itemGroups.tools, SlimefunItems.GOLD_PAN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.STONE), StackResolver.of(Material.BOWL), StackResolver.of(Material.STONE), StackResolver.of(Material.STONE), StackResolver.of(Material.STONE), StackResolver.of(Material.STONE)})
        .register(plugin);

        new NetherGoldPan(itemGroups.tools, SlimefunItems.NETHER_GOLD_PAN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.NETHER_BRICK), SlimefunItems.GOLD_PAN, StackResolver.of(Material.NETHER_BRICK), StackResolver.of(Material.NETHER_BRICK), StackResolver.of(Material.NETHER_BRICK), StackResolver.of(Material.NETHER_BRICK)})
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.SIFTED_ORE, RecipeType.GOLD_PAN,
        new ItemStack[] {StackResolver.of(Material.GRAVEL), null, null, null, null, null, null, null, null})
        .register(plugin);

        new MakeshiftSmeltery(itemGroups.basicMachines, SlimefunItems.MAKESHIFT_SMELTERY).register(plugin);
        new Smeltery(itemGroups.basicMachines, SlimefunItems.SMELTERY).register(plugin);

        new IgnitionChamber(itemGroups.basicMachines, SlimefunItems.IGNITION_CHAMBER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.FLINT_AND_STEEL), StackResolver.of(Material.IRON_INGOT), StackResolver.of(Material.IRON_INGOT), SlimefunItems.BASIC_CIRCUIT_BOARD, StackResolver.of(Material.IRON_INGOT), null, StackResolver.of(Material.OBSERVER), null})
        .register(plugin);

        new PressureChamber(itemGroups.basicMachines, SlimefunItems.PRESSURE_CHAMBER).register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.BATTERY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.REDSTONE), null, SlimefunItems.ZINC_INGOT, SlimefunItems.SULFATE, SlimefunItems.COPPER_INGOT, SlimefunItems.ZINC_INGOT, SlimefunItems.SULFATE, SlimefunItems.COPPER_INGOT})
        .register(plugin);

        registerArmorSet(itemGroups.magicalArmor, StackResolver.of(Material.GLOWSTONE), new ItemStack[] {SlimefunItems.GLOWSTONE_HELMET, SlimefunItems.GLOWSTONE_CHESTPLATE, SlimefunItems.GLOWSTONE_LEGGINGS, SlimefunItems.GLOWSTONE_BOOTS}, "GLOWSTONE", false,
        new PotionEffect[][] {
            new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)},
            new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)},
            new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)},
            new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)}
        }, plugin);

        DyeColor[] rainbowArmorColors = {
            DyeColor.RED,
            DyeColor.ORANGE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.LIGHT_BLUE,
            DyeColor.PURPLE,
            DyeColor.MAGENTA
        };

        new RainbowArmorPiece(itemGroups.magicalArmor, SlimefunItems.RAINBOW_HELMET, RecipeType.ARMOR_FORGE,
                new ItemStack[] { SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, null, SlimefunItems.RAINBOW_LEATHER, null, null, null },
                rainbowArmorColors)
                .register(plugin);

        new RainbowArmorPiece(itemGroups.magicalArmor, SlimefunItems.RAINBOW_CHESTPLATE, RecipeType.ARMOR_FORGE,
                new ItemStack[] { SlimefunItems.RAINBOW_LEATHER, null, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER },
                rainbowArmorColors)
                .register(plugin);

        new RainbowArmorPiece(itemGroups.magicalArmor, SlimefunItems.RAINBOW_LEGGINGS, RecipeType.ARMOR_FORGE,
                new ItemStack[] { SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, null, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, null, SlimefunItems.RAINBOW_LEATHER },
                rainbowArmorColors)
                .register(plugin);

        new RainbowArmorPiece(itemGroups.magicalArmor, SlimefunItems.RAINBOW_BOOTS, RecipeType.ARMOR_FORGE,
                new ItemStack[] { null, null, null, SlimefunItems.RAINBOW_LEATHER, null, SlimefunItems.RAINBOW_LEATHER, SlimefunItems.RAINBOW_LEATHER, null, SlimefunItems.RAINBOW_LEATHER },
                rainbowArmorColors)
                .register(plugin);


        registerArmorSet(itemGroups.armor, SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_HELMET, SlimefunItems.DAMASCUS_STEEL_CHESTPLATE, SlimefunItems.DAMASCUS_STEEL_LEGGINGS, SlimefunItems.DAMASCUS_STEEL_BOOTS}, "DAMASCUS_STEEL", false, new PotionEffect[0][0], plugin);

        registerArmorSet(itemGroups.armor, SlimefunItems.REINFORCED_ALLOY_INGOT, new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_HELMET, SlimefunItems.REINFORCED_ALLOY_CHESTPLATE, SlimefunItems.REINFORCED_ALLOY_LEGGINGS, SlimefunItems.REINFORCED_ALLOY_BOOTS}, "REINFORCED_ALLOY", false, new PotionEffect[0][0], plugin);

        registerArmorSet(itemGroups.armor, StackResolver.of(Material.CACTUS), new ItemStack[] {SlimefunItems.CACTUS_HELMET, SlimefunItems.CACTUS_CHESTPLATE, SlimefunItems.CACTUS_LEGGINGS, SlimefunItems.CACTUS_BOOTS}, "CACTUS", false, new PotionEffect[0][0], plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.REINFORCED_ALLOY_INGOT,
        new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.GOLD_24K, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.HARDENED_METAL_INGOT,
        new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.ALUMINUM_BRONZE_INGOT, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.DAMASCUS_STEEL_INGOT,
        new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.IRON_DUST, SlimefunItems.CARBON, StackResolver.of(Material.IRON_INGOT), null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.STEEL_INGOT,
        new ItemStack[] {SlimefunItems.IRON_DUST, SlimefunItems.CARBON, StackResolver.of(Material.IRON_INGOT), null, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.BRONZE_INGOT,
        new ItemStack[] {SlimefunItems.COPPER_DUST, SlimefunItems.TIN_DUST, SlimefunItems.COPPER_INGOT, null, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.DURALUMIN_INGOT,
        new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.ALUMINUM_INGOT, null, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.BILLON_INGOT,
        new ItemStack[] {SlimefunItems.SILVER_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.SILVER_INGOT, null, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.BRASS_INGOT,
        new ItemStack[] {SlimefunItems.COPPER_DUST, SlimefunItems.ZINC_DUST, SlimefunItems.COPPER_INGOT, null, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.ALUMINUM_BRASS_INGOT,
        new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.BRASS_INGOT, SlimefunItems.ALUMINUM_INGOT, null, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.ALUMINUM_BRONZE_INGOT,
        new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.BRONZE_INGOT, SlimefunItems.ALUMINUM_INGOT, null, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.CORINTHIAN_BRONZE_INGOT,
        new ItemStack[] {SlimefunItems.SILVER_DUST, SlimefunItems.GOLD_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.BRONZE_INGOT, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.SOLDER_INGOT,
        new ItemStack[] {SlimefunItems.LEAD_DUST, SlimefunItems.TIN_DUST, SlimefunItems.LEAD_INGOT, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SYNTHETIC_SAPPHIRE, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.ALUMINUM_DUST, StackResolver.of(Material.GLASS), StackResolver.of(Material.GLASS_PANE), SlimefunItems.ALUMINUM_INGOT, StackResolver.of(Material.LAPIS_LAZULI), null, null, null, null})
        .setUseableInWorkbench(true)
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SYNTHETIC_DIAMOND, RecipeType.PRESSURE_CHAMBER,
        new ItemStack[] {SlimefunItems.CARBON_CHUNK, null, null, null, null, null, null, null, null})
        .setUseableInWorkbench(true)
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.RAW_CARBONADO, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.CARBON_CHUNK, StackResolver.of(Material.GLASS_PANE), null, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.NICKEL_INGOT,
        new ItemStack[] {SlimefunItems.IRON_DUST, StackResolver.of(Material.IRON_INGOT), SlimefunItems.COPPER_DUST, null, null, null, null, null, null})
        .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.COBALT_INGOT,
        new ItemStack[] {SlimefunItems.IRON_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.NICKEL_INGOT, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.CARBONADO, RecipeType.PRESSURE_CHAMBER,
        new ItemStack[] {SlimefunItems.RAW_CARBONADO, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.FERROSILICON, RecipeType.SMELTERY,
        new ItemStack[] {StackResolver.of(Material.IRON_INGOT), SlimefunItems.IRON_DUST, SlimefunItems.SILICON, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.IRON_DUST, RecipeType.ORE_CRUSHER,
        new ItemStack[] {StackResolver.of(Material.IRON_ORE), null, null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.IRON_DUST, oreCrusher.isOreDoublingEnabled() ? 2 : 1))
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.GOLD_DUST, RecipeType.ORE_CRUSHER,
        new ItemStack[] {StackResolver.of(Material.GOLD_ORE), null, null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.GOLD_DUST, oreCrusher.isOreDoublingEnabled() ? 2 : 1))
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.COPPER_DUST, RecipeType.ORE_WASHER,
        new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.TIN_DUST, RecipeType.ORE_WASHER,
        new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SILVER_DUST, RecipeType.ORE_WASHER,
        new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.LEAD_DUST, RecipeType.ORE_WASHER,
        new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.ALUMINUM_DUST, RecipeType.ORE_WASHER,
        new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.ZINC_DUST, RecipeType.ORE_WASHER,
        new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.MAGNESIUM_DUST, RecipeType.ORE_WASHER,
        new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.COPPER_INGOT, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.COPPER_DUST, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.TIN_INGOT, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.TIN_DUST, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SILVER_INGOT, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.SILVER_DUST, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.LEAD_INGOT, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.LEAD_DUST, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.ALUMINUM_INGOT, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.ALUMINUM_DUST, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.ZINC_INGOT, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.ZINC_DUST, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.MAGNESIUM_INGOT, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.MAGNESIUM_DUST, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SULFATE, RecipeType.ORE_CRUSHER,
        new ItemStack[] {StackResolver.of(Material.NETHERRACK, 16), null, null, null, null, null, null, null, null})
        .register(plugin);

        new UnplaceableBlock(itemGroups.resources, SlimefunItems.CARBON, RecipeType.COMPRESSOR,
        new ItemStack[] {StackResolver.of(Material.COAL, 8), null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.WHEAT_FLOUR, RecipeType.GRIND_STONE,
        new ItemStack[] {StackResolver.of(Material.WHEAT), null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.STEEL_PLATE, RecipeType.COMPRESSOR,
        new ItemStack[] {new SlimefunItemStack(SlimefunItems.STEEL_INGOT, 8), null, null, null, null, null, null, null, null})
        .register(plugin);

        new UnplaceableBlock(itemGroups.resources, SlimefunItems.COMPRESSED_CARBON, RecipeType.COMPRESSOR,
        new ItemStack[] {new SlimefunItemStack(SlimefunItems.CARBON, 4), null, null, null, null, null, null, null, null})
        .register(plugin);

        new UnplaceableBlock(itemGroups.resources, SlimefunItems.CARBON_CHUNK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, StackResolver.of(Material.FLINT), SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON})
        .register(plugin);

        new SteelThruster(itemGroups.technicalComponents, SlimefunItems.STEEL_THRUSTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.REDSTONE), null, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.STEEL_PLATE, StackResolver.of(Material.FIRE_CHARGE), SlimefunItems.STEEL_PLATE})
        .register(plugin);

        new SlimefunItem(itemGroups.technicalComponents, SlimefunItems.POWER_CRYSTAL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE, StackResolver.of(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.SYNTHETIC_SAPPHIRE, StackResolver.of(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE, StackResolver.of(Material.REDSTONE)})
        .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.DURALUMIN_JETPACK,
        new ItemStack[] {SlimefunItems.DURALUMIN_INGOT, null, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.35, 20)
        .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.SOLDER_JETPACK,
        new ItemStack[] {SlimefunItems.SOLDER_INGOT, null, SlimefunItems.SOLDER_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.SOLDER_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.4, 30)
        .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.BILLON_JETPACK,
        new ItemStack[] {SlimefunItems.BILLON_INGOT, null, SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BILLON_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.45, 45)
        .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.STEEL_JETPACK,
        new ItemStack[] {SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.5, 60)
        .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.DAMASCUS_STEEL_JETPACK,
        new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.55, 75)
        .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.REINFORCED_ALLOY_JETPACK,
        new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.6, 100)
        .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.CARBONADO_JETPACK,
        new ItemStack[] {SlimefunItems.CARBON_CHUNK, null, SlimefunItems.CARBON_CHUNK, SlimefunItems.CARBONADO, SlimefunItems.POWER_CRYSTAL, SlimefunItems.CARBONADO, SlimefunItems.STEEL_THRUSTER, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.7, 150)
        .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.ARMORED_JETPACK,
        new ItemStack[] {SlimefunItems.STEEL_PLATE, null, SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_PLATE, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_THRUSTER, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.5, 50)
        .register(plugin);

        new Parachute(itemGroups.technicalGadgets, SlimefunItems.PARACHUTE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CHAIN, null, SlimefunItems.CHAIN, null, null, null})
        .register(plugin);

        new HologramProjector(itemGroups.technicalGadgets, SlimefunItems.HOLOGRAM_PROJECTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.POWER_CRYSTAL, null, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT, null, SlimefunItems.ALUMINUM_BRASS_INGOT, null},
        new SlimefunItemStack(SlimefunItems.HOLOGRAM_PROJECTOR, 3))
        .register(plugin);

        new UnplaceableBlock(itemGroups.misc, SlimefunItems.CHAIN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, null},
        new SlimefunItemStack(SlimefunItems.CHAIN, 8))
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.HOOK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, null, null})
        .register(plugin);

        new GrapplingHook(itemGroups.tools, SlimefunItems.GRAPPLING_HOOK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, SlimefunItems.HOOK, null, SlimefunItems.CHAIN, null, SlimefunItems.CHAIN, null, null})
        .register(plugin);

        new MagicWorkbench(itemGroups.basicMachines, SlimefunItems.MAGIC_WORKBENCH).register(plugin);

        new SlimefunItem(itemGroups.magicalGadgets, SlimefunItems.STAFF_ELEMENTAL, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.MAGIC_LUMP_3, null, StackResolver.of(Material.STICK), SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.MAGIC_LUMP_3, null, null})
        .register(plugin);

        new WindStaff(itemGroups.magicalGadgets, SlimefunItems.STAFF_WIND, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, StackResolver.of(Material.FEATHER), SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.STAFF_ELEMENTAL, StackResolver.of(Material.FEATHER), SlimefunItems.STAFF_ELEMENTAL, null, null})
        .register(plugin);

        new WaterStaff(itemGroups.magicalGadgets, SlimefunItems.STAFF_WATER, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, StackResolver.of(Material.LILY_PAD), SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.STAFF_ELEMENTAL, StackResolver.of(Material.LILY_PAD), SlimefunItems.STAFF_ELEMENTAL, null, null})
        .register(plugin);

        new EnchantedItem(itemGroups.magicalGadgets, SlimefunItems.STAFF_FIRE, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, null, SlimefunItems.LAVA_CRYSTAL, null, SlimefunItems.STAFF_ELEMENTAL, null, SlimefunItems.STAFF_ELEMENTAL, null, null})
        .register(plugin);

        String[] multiToolItems = new String[] {"PORTABLE_CRAFTER", "MAGIC_EYE_OF_ENDER", "STAFF_ELEMENTAL_WIND", "GRAPPLING_HOOK"};

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.DURALUMIN_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.DURALUMIN_INGOT, null, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.DURALUMIN_INGOT, null, SlimefunItems.DURALUMIN_INGOT, null},
        20, multiToolItems)
        .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.SOLDER_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SOLDER_INGOT, null, SlimefunItems.SOLDER_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.SOLDER_INGOT, null, SlimefunItems.SOLDER_INGOT, null},
        30, multiToolItems)
        .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.BILLON_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.BILLON_INGOT, null, SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.BILLON_INGOT, null, SlimefunItems.BILLON_INGOT, null},
        40, multiToolItems)
        .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.STEEL_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null},
        50, multiToolItems)
        .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.DAMASCUS_STEEL_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.DAMASCUS_STEEL_INGOT, null, SlimefunItems.DAMASCUS_STEEL_INGOT, null},
        60, multiToolItems)
        .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.REINFORCED_ALLOY_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, null},
        75, multiToolItems)
        .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.CARBONADO_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CARBONADO, null, SlimefunItems.CARBONADO, SlimefunItems.CARBONADO, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.CARBONADO, null, SlimefunItems.CARBONADO, null},
        100, "PORTABLE_CRAFTER", "MAGIC_EYE_OF_ENDER", "STAFF_ELEMENTAL_WIND", "GRAPPLING_HOOK", "GOLD_PAN", "NETHER_GOLD_PAN")
        .register(plugin);

        new OreWasher(itemGroups.basicMachines, SlimefunItems.ORE_WASHER).register(plugin);

        new GoldIngot(itemGroups.resources, 24, SlimefunItems.GOLD_24K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_22K, null, null, null, null, null, null, null})
        .register(plugin);

        new GoldIngot(itemGroups.resources, 22, SlimefunItems.GOLD_22K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_20K, null, null, null, null, null, null, null})
        .register(plugin);

        new GoldIngot(itemGroups.resources, 20, SlimefunItems.GOLD_20K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_18K, null, null, null, null, null, null, null})
        .register(plugin);

        new GoldIngot(itemGroups.resources, 18, SlimefunItems.GOLD_18K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_16K, null, null, null, null, null, null, null})
        .register(plugin);

        new GoldIngot(itemGroups.resources, 16, SlimefunItems.GOLD_16K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_14K, null, null, null, null, null, null, null})
        .register(plugin);

        new GoldIngot(itemGroups.resources, 14, SlimefunItems.GOLD_14K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_12K, null, null, null, null, null, null, null})
        .register(plugin);

        new GoldIngot(itemGroups.resources, 12, SlimefunItems.GOLD_12K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_10K, null, null, null, null, null, null, null})
        .register(plugin);

        new GoldIngot(itemGroups.resources, 10, SlimefunItems.GOLD_10K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_8K, null, null, null, null, null, null, null})
        .register(plugin);

        new GoldIngot(itemGroups.resources, 8, SlimefunItems.GOLD_8K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_6K, null, null, null, null, null, null, null})
        .register(plugin);

        new GoldIngot(itemGroups.resources, 6, SlimefunItems.GOLD_6K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_4K, null, null, null, null, null, null, null})
        .register(plugin);

        new GoldIngot(itemGroups.resources, 4, SlimefunItems.GOLD_4K, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_DUST, null, null, null, null, null, null, null, null})
        .setUseableInWorkbench(true)
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.STONE_CHUNK, RecipeType.ORE_WASHER,
        new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SILICON, RecipeType.SMELTERY,
        new ItemStack[] {StackResolver.of(Material.QUARTZ_BLOCK), null, null, null, null, null, null, null, null})
        .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.SOLAR_PANEL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.GLASS), StackResolver.of(Material.GLASS), StackResolver.of(Material.GLASS), SlimefunItems.SILICON, SlimefunItems.SILICON, SlimefunItems.SILICON, SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON})
        .register(plugin);

        new SolarHelmet(itemGroups.technicalGadgets, SlimefunItems.SOLAR_HELMET, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.SOLAR_PANEL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.MEDIUM_CAPACITOR, null, SlimefunItems.MEDIUM_CAPACITOR},
        0.1)
        .register(plugin);

        new UnplaceableBlock(itemGroups.magicalResources, SlimefunItems.LAVA_CRYSTAL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.BLAZE_POWDER), SlimefunItems.FIRE_RUNE, StackResolver.of(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1})
        .register(plugin);

        new StormStaff(itemGroups.magicalGadgets, SlimefunItems.STAFF_STORM, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.LIGHTNING_RUNE, SlimefunItems.ENDER_LUMP_3, SlimefunItems.LIGHTNING_RUNE, SlimefunItems.STAFF_WATER, SlimefunItems.MAGIC_SUGAR, SlimefunItems.STAFF_WIND, SlimefunItems.LIGHTNING_RUNE, SlimefunItems.ENDER_LUMP_3, SlimefunItems.LIGHTNING_RUNE})
        .register(plugin);

        ItemStack weaknessPotion = StackResolver.of(Material.POTION);
        PotionMeta meta = (PotionMeta) weaknessPotion.getItemMeta();
        if (Slimefun.getMinecraftVersion().isBefore(20, 2)) {
            meta.setBasePotionData(new PotionData(PotionType.WEAKNESS, false, false));
        } else {
            meta.setBasePotionType(PotionType.WEAKNESS);
        }
        weaknessPotion.setItemMeta(meta);

        new MagicalZombiePills(itemGroups.magicalGadgets, SlimefunItems.MAGICAL_ZOMBIE_PILLS, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {StackResolver.of(Material.GOLD_INGOT), SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.GOLD_INGOT), StackResolver.of(Material.APPLE), weaknessPotion, StackResolver.of(Material.APPLE), StackResolver.of(Material.GOLD_INGOT), SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.GOLD_INGOT)},
        new SlimefunItemStack(SlimefunItems.MAGICAL_ZOMBIE_PILLS, 2))
        .register(plugin);

        new SmeltersPickaxe(itemGroups.tools, SlimefunItems.SMELTERS_PICKAXE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.LAVA_CRYSTAL, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.LAVA_CRYSTAL, null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.COMMON_TALISMAN, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2, null, StackResolver.of(Material.EMERALD), null, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2})
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_ANVIL,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, StackResolver.of(Material.ANVIL), SlimefunItems.COMMON_TALISMAN, StackResolver.of(Material.ANVIL), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        true, false, "anvil")
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_MINER,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.COMMON_TALISMAN, SlimefunItems.SIFTED_ORE, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        false, false, "miner", 20)
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_FARMER,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, StackResolver.of(Material.DIAMOND_HOE), SlimefunItems.COMMON_TALISMAN,  StackResolver.of(Material.DIAMOND_HOE), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        false, false, "farmer", 20)
        .register(plugin);


        new Talisman(SlimefunItems.TALISMAN_HUNTER,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.COMMON_TALISMAN, SlimefunItems.MONSTER_JERKY, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        false, false, "hunter", 20)
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_LAVA,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.COMMON_TALISMAN, StackResolver.of(Material.LAVA_BUCKET), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        true, true, "lava", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4))
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_WATER,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, StackResolver.of(Material.WATER_BUCKET), SlimefunItems.COMMON_TALISMAN, StackResolver.of(Material.FISHING_ROD), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        true, true, "water", new PotionEffect(PotionEffectType.WATER_BREATHING, 3600, 4))
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_ANGEL,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, StackResolver.of(Material.FEATHER), SlimefunItems.COMMON_TALISMAN, StackResolver.of(Material.FEATHER), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        false, true, "angel", 75)
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_FIRE,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.COMMON_TALISMAN, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        true, true, "fire", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4))
        .register(plugin);

        new MagicianTalisman(SlimefunItems.TALISMAN_MAGICIAN,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, StackResolver.of(Material.ENCHANTING_TABLE), SlimefunItems.COMMON_TALISMAN, StackResolver.of(Material.ENCHANTING_TABLE), SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3})
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_TRAVELLER,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.STAFF_WIND, SlimefunItems.TALISMAN_ANGEL, SlimefunItems.STAFF_WIND, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        false, false, "traveller", 60, new PotionEffect(PotionEffectType.SPEED, 3600, 2))
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_WARRIOR,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.COMMON_TALISMAN, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        true, true, "warrior", new PotionEffect(VersionedPotionEffectType.STRENGTH, 3600, 2))
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_KNIGHT,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.GILDED_IRON, SlimefunItems.TALISMAN_WARRIOR, SlimefunItems.GILDED_IRON, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        "knight", 30, new PotionEffect(PotionEffectType.REGENERATION, 100, 3))
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_CAVEMAN,
        new ItemStack[] { SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, StackResolver.of(Material.GOLDEN_PICKAXE), SlimefunItems.TALISMAN_MINER, SlimefunItems.EARTH_RUNE, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
        false, false, "caveman", 50, new PotionEffect(VersionedPotionEffectType.HASTE, 800, 2))
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_WISE,
        new ItemStack[] { SlimefunItems.MAGIC_LUMP_3, SlimefunItems.MAGICAL_GLASS, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.TALISMAN_MAGICIAN, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.MAGICAL_GLASS, SlimefunItems.MAGIC_LUMP_3},
        false, false, "wise", 20)
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.GILDED_IRON, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.IRON_DUST, null, null, null, null, null, null, null})
        .register(plugin);

        new SyntheticEmerald(itemGroups.resources, SlimefunItems.SYNTHETIC_EMERALD, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.ALUMINUM_DUST, SlimefunItems.ALUMINUM_INGOT, StackResolver.of(Material.GLASS_PANE), null, null, null, null, null})
        .register(plugin);

        registerArmorSet(itemGroups.armor, SlimefunItems.CHAIN, new ItemStack[] {
            StackResolver.of(Material.CHAINMAIL_HELMET), StackResolver.of(Material.CHAINMAIL_CHESTPLATE), StackResolver.of(Material.CHAINMAIL_LEGGINGS), StackResolver.of(Material.CHAINMAIL_BOOTS)
        }, "CHAIN", true, new PotionEffect[0][0], plugin);

        new Talisman(SlimefunItems.TALISMAN_WHIRLWIND,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.STAFF_WIND, SlimefunItems.TALISMAN_TRAVELLER, SlimefunItems.STAFF_WIND, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3}
        , false, true, "whirlwind", 60)
        .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_WIZARD,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.TALISMAN_MAGICIAN, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3},
        false, false, "wizard", 60)
        .register(plugin);

        new LumberAxe(itemGroups.tools, SlimefunItems.LUMBER_AXE, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.SYNTHETIC_DIAMOND, null, SlimefunItems.SYNTHETIC_EMERALD, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.SALT, RecipeType.ORE_WASHER,
        new ItemStack[] {StackResolver.of(Material.SAND, 2), null, null, null, null, null, null, null, null})
        .register(plugin);

        new HeavyCream(itemGroups.misc, SlimefunItems.HEAVY_CREAM, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.MILK_BUCKET), null, null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.HEAVY_CREAM, 2))
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.CHEESE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.MILK_BUCKET), SlimefunItems.SALT, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.BUTTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.HEAVY_CREAM, SlimefunItems.SALT, null, null, null, null, null, null, null})
        .register(plugin);

        registerArmorSet(itemGroups.armor, SlimefunItems.GILDED_IRON, new ItemStack[] {
             SlimefunItems.GILDED_IRON_HELMET, SlimefunItems.GILDED_IRON_CHESTPLATE, SlimefunItems.GILDED_IRON_LEGGINGS, SlimefunItems.GILDED_IRON_BOOTS
        }, "GILDED_IRON", false, new PotionEffect[0][0], plugin);

        new SlimefunItem(itemGroups.technicalComponents, SlimefunItems.REINFORCED_CLOTH, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.CLOTH, null, SlimefunItems.CLOTH, SlimefunItems.LEAD_INGOT, SlimefunItems.CLOTH, null, SlimefunItems.CLOTH, null}, new SlimefunItemStack(SlimefunItems.REINFORCED_CLOTH, 2))
        .register(plugin);

        new HazmatArmorPiece(itemGroups.armor, SlimefunItems.SCUBA_HELMET, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.ORANGE_WOOL), SlimefunItems.REINFORCED_CLOTH, StackResolver.of(Material.ORANGE_WOOL), SlimefunItems.REINFORCED_CLOTH, StackResolver.of(Material.GLASS_PANE), SlimefunItems.REINFORCED_CLOTH, null, null, null},
        new PotionEffect[] {new PotionEffect(PotionEffectType.WATER_BREATHING, 300, 1)})
        .register(plugin);

        new HazmatArmorPiece(itemGroups.armor, SlimefunItems.HAZMAT_CHESTPLATE, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.ORANGE_WOOL), null, StackResolver.of(Material.ORANGE_WOOL), SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, StackResolver.of(Material.BLACK_WOOL), SlimefunItems.REINFORCED_CLOTH, StackResolver.of(Material.BLACK_WOOL)},
        new PotionEffect[] {new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 1)})
        .register(plugin);

        new HazmatArmorPiece(itemGroups.armor, SlimefunItems.HAZMAT_LEGGINGS, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.BLACK_WOOL), SlimefunItems.REINFORCED_CLOTH, StackResolver.of(Material.BLACK_WOOL), SlimefunItems.REINFORCED_CLOTH, null, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, null, SlimefunItems.REINFORCED_CLOTH}, new PotionEffect[0])
        .register(plugin);

        new HazmatArmorPiece(itemGroups.armor, SlimefunItems.HAZMAT_BOOTS, RecipeType.ARMOR_FORGE,
        new ItemStack[] {SlimefunItems.REINFORCED_CLOTH, null, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, null, SlimefunItems.REINFORCED_CLOTH, StackResolver.of(Material.BLACK_WOOL), null, StackResolver.of(Material.BLACK_WOOL)}, new PotionEffect[0])
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.CRUSHED_ORE, RecipeType.ORE_CRUSHER,
        new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.PULVERIZED_ORE, RecipeType.ORE_CRUSHER,
        new ItemStack[] {SlimefunItems.CRUSHED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.PURE_ORE_CLUSTER, RecipeType.ORE_WASHER,
        new ItemStack[] {SlimefunItems.PULVERIZED_ORE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new RadioactiveItem(itemGroups.misc, Radioactivity.LOW, SlimefunItems.TINY_URANIUM, RecipeType.ORE_CRUSHER,
        new ItemStack[] {SlimefunItems.PURE_ORE_CLUSTER, null, null, null, null, null, null, null, null})
        .register(plugin);

        new RadioactiveItem(itemGroups.misc, Radioactivity.MODERATE, SlimefunItems.SMALL_URANIUM, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM})
        .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.HIGH, SlimefunItems.URANIUM, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SMALL_URANIUM, SlimefunItems.SMALL_URANIUM, null, SlimefunItems.SMALL_URANIUM, SlimefunItems.SMALL_URANIUM, null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.REDSTONE_ALLOY, RecipeType.SMELTERY,
        new ItemStack[] {StackResolver.of(Material.REDSTONE), StackResolver.of(Material.REDSTONE_BLOCK), SlimefunItems.FERROSILICON, SlimefunItems.HARDENED_METAL_INGOT, null, null, null, null, null})
        .register(plugin);

        registerArmorSet(itemGroups.armor, SlimefunItems.GOLD_12K, new ItemStack[] {
            SlimefunItems.GOLDEN_HELMET_12K, SlimefunItems.GOLDEN_CHESTPLATE_12K, SlimefunItems.GOLDEN_LEGGINGS_12K, SlimefunItems.GOLDEN_BOOTS_12K
        }, "GOLD_12K", false, new PotionEffect[0][0], plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.CLOTH, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.WHITE_WOOL), null, null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.CLOTH, 8))
        .register(plugin);

        new Bandage(itemGroups.usefulItems, SlimefunItems.RAG, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH, StackResolver.of(Material.STRING), null, StackResolver.of(Material.STRING), SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH},
        new SlimefunItemStack(SlimefunItems.RAG, 2), 0)
        .register(plugin);

        new Bandage(itemGroups.usefulItems, SlimefunItems.BANDAGE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.RAG, StackResolver.of(Material.STRING), SlimefunItems.RAG, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.BANDAGE, 4), 1)
        .register(plugin);

        new Splint(itemGroups.usefulItems, SlimefunItems.SPLINT, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.IRON_INGOT), null, StackResolver.of(Material.STICK), StackResolver.of(Material.STICK), StackResolver.of(Material.STICK), null, StackResolver.of(Material.IRON_INGOT), null},
        new SlimefunItemStack(SlimefunItems.SPLINT, 4))
        .register(plugin);

        new UnplaceableBlock(itemGroups.misc, SlimefunItems.TIN_CAN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, null, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT},
        new SlimefunItemStack(SlimefunItems.TIN_CAN, 8))
        .register(plugin);

        new Vitamins(itemGroups.usefulItems, SlimefunItems.VITAMINS, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.TIN_CAN, StackResolver.of(Material.APPLE), StackResolver.of(Material.RED_MUSHROOM), StackResolver.of(Material.SUGAR), null, null, null, null, null})
        .register(plugin);

        new Medicine(itemGroups.usefulItems, SlimefunItems.MEDICINE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.VITAMINS, StackResolver.of(Material.GLASS_BOTTLE), SlimefunItems.HEAVY_CREAM, null, null, null, null, null, null})
        .register(plugin);

        new SlimefunArmorPiece(itemGroups.technicalGadgets, SlimefunItems.NIGHT_VISION_GOGGLES, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.COAL_BLOCK), StackResolver.of(Material.COAL_BLOCK), StackResolver.of(Material.COAL_BLOCK), StackResolver.of(Material.LIME_STAINED_GLASS_PANE), StackResolver.of(Material.COAL_BLOCK), StackResolver.of(Material.LIME_STAINED_GLASS_PANE), StackResolver.of(Material.COAL_BLOCK), null, StackResolver.of(Material.COAL_BLOCK)},
        new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 20)})
        .register(plugin);

        new PickaxeOfContainment(itemGroups.tools, SlimefunItems.PICKAXE_OF_CONTAINMENT, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, null, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
        .register(plugin);

        new TableSaw(itemGroups.basicMachines, SlimefunItems.TABLE_SAW).register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_HELMET_STEEL, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.SLIME_BALL), null, StackResolver.of(Material.SLIME_BALL), null, null, null}, null)
        .register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_CHESTPLATE_STEEL, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.SLIME_BALL), null, StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.SLIME_BALL)}, null)
        .register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_LEGGINGS_STEEL, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.SLIME_BALL), null, StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.SLIME_BALL), null, StackResolver.of(Material.SLIME_BALL)},
        new PotionEffect[] {new PotionEffect(PotionEffectType.SPEED, 300, 2)})
        .register(plugin);

        new LongFallBoots(itemGroups.magicalArmor, SlimefunItems.SLIME_BOOTS_STEEL, RecipeType.ARMOR_FORGE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.SLIME_BALL), null, StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, StackResolver.of(Material.SLIME_BALL)},
        new PotionEffect[] {new PotionEffect(VersionedPotionEffectType.JUMP_BOOST, 300, 5)},
        SoundEffect.SLIME_BOOTS_FALL_SOUND)
        .register(plugin);

        new VampireBlade(itemGroups.weapons, SlimefunItems.BLADE_OF_VAMPIRES, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, StackResolver.of(Material.WITHER_SKELETON_SKULL), null, null, StackResolver.of(Material.WITHER_SKELETON_SKULL), null, null, StackResolver.of(Material.BLAZE_ROD), null})
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.GOLD_24K_BLOCK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K})
        .register(plugin);

        new Composter(itemGroups.basicMachines, SlimefunItems.COMPOSTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.OAK_SLAB), null, StackResolver.of(Material.OAK_SLAB), StackResolver.of(Material.OAK_SLAB), null, StackResolver.of(Material.OAK_SLAB), StackResolver.of(Material.OAK_SLAB), StackResolver.of(Material.CAULDRON), StackResolver.of(Material.OAK_SLAB)})
        .register(plugin);

        new FarmerShoes(itemGroups.magicalArmor, SlimefunItems.FARMER_SHOES, RecipeType.ARMOR_FORGE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.HAY_BLOCK), null, StackResolver.of(Material.HAY_BLOCK), StackResolver.of(Material.HAY_BLOCK), null, StackResolver.of(Material.HAY_BLOCK)})
        .register(plugin);

        new ExplosivePickaxe(itemGroups.tools, SlimefunItems.EXPLOSIVE_PICKAXE, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {StackResolver.of(Material.TNT), SlimefunItems.SYNTHETIC_DIAMOND, StackResolver.of(Material.TNT), null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
        .register(plugin);

        new ExplosiveShovel(itemGroups.tools, SlimefunItems.EXPLOSIVE_SHOVEL, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.SYNTHETIC_DIAMOND, null, null, StackResolver.of(Material.TNT), null, null, SlimefunItems.FERROSILICON, null})
        .register(plugin);

        new AutomatedPanningMachine(itemGroups.basicMachines, SlimefunItems.AUTOMATED_PANNING_MACHINE).register(plugin);

        new IndustrialMiner(itemGroups.basicMachines, SlimefunItems.INDUSTRIAL_MINER, Material.IRON_BLOCK, false, 3).register(plugin);
        new AdvancedIndustrialMiner(itemGroups.basicMachines, SlimefunItems.ADVANCED_INDUSTRIAL_MINER).register(plugin);

        new StomperBoots(itemGroups.magicalArmor, SlimefunItems.BOOTS_OF_THE_STOMPER, RecipeType.ARMOR_FORGE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.YELLOW_WOOL), null, StackResolver.of(Material.YELLOW_WOOL), StackResolver.of(Material.PISTON), null, StackResolver.of(Material.PISTON)})
        .register(plugin);

        new PickaxeOfTheSeeker(itemGroups.tools, SlimefunItems.PICKAXE_OF_THE_SEEKER, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {StackResolver.of(Material.COMPASS), SlimefunItems.SYNTHETIC_DIAMOND, StackResolver.of(Material.COMPASS), null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
        .register(plugin);

        new SlimefunBackpack(9, itemGroups.usefulItems, SlimefunItems.BACKPACK_SMALL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.LEATHER), null, StackResolver.of(Material.LEATHER), SlimefunItems.GOLD_6K, StackResolver.of(Material.CHEST), SlimefunItems.GOLD_6K, StackResolver.of(Material.LEATHER), StackResolver.of(Material.LEATHER), StackResolver.of(Material.LEATHER)})
        .register(plugin);

        new SlimefunBackpack(18, itemGroups.usefulItems, SlimefunItems.BACKPACK_MEDIUM, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.LEATHER), null, StackResolver.of(Material.LEATHER), SlimefunItems.GOLD_10K, SlimefunItems.BACKPACK_SMALL, SlimefunItems.GOLD_10K, StackResolver.of(Material.LEATHER), StackResolver.of(Material.LEATHER), StackResolver.of(Material.LEATHER)})
        .register(plugin);

        new SlimefunBackpack(27, itemGroups.usefulItems, SlimefunItems.BACKPACK_LARGE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.LEATHER), null, StackResolver.of(Material.LEATHER), SlimefunItems.GOLD_14K, SlimefunItems.BACKPACK_MEDIUM, SlimefunItems.GOLD_14K, StackResolver.of(Material.LEATHER), StackResolver.of(Material.LEATHER), StackResolver.of(Material.LEATHER)})
        .register(plugin);

        new SlimefunBackpack(36, itemGroups.usefulItems, SlimefunItems.WOVEN_BACKPACK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CLOTH, null, SlimefunItems.CLOTH, SlimefunItems.GOLD_16K, SlimefunItems.BACKPACK_LARGE, SlimefunItems.GOLD_16K, SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH})
        .register(plugin);

        new Crucible(itemGroups.basicMachines, SlimefunItems.CRUCIBLE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.TERRACOTTA), null, StackResolver.of(Material.TERRACOTTA), StackResolver.of(Material.TERRACOTTA), null, StackResolver.of(Material.TERRACOTTA), StackResolver.of(Material.TERRACOTTA), StackResolver.of(Material.FLINT_AND_STEEL), StackResolver.of(Material.TERRACOTTA)})
        .register(plugin);

        new SlimefunBackpack(45, itemGroups.usefulItems, SlimefunItems.GILDED_BACKPACK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GOLD_22K, null, SlimefunItems.GOLD_22K, StackResolver.of(Material.LEATHER), SlimefunItems.WOVEN_BACKPACK, StackResolver.of(Material.LEATHER), SlimefunItems.GOLD_22K, null, SlimefunItems.GOLD_22K})
        .register(plugin);

        new SlimefunBackpack(54, itemGroups.usefulItems, SlimefunItems.RADIANT_BACKPACK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, StackResolver.of(Material.LEATHER), SlimefunItems.GILDED_BACKPACK, StackResolver.of(Material.LEATHER), SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K})
        .register(plugin);

        new RestoredBackpack(itemGroups.usefulItems).register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.MAGNET, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.NICKEL_INGOT, SlimefunItems.ALUMINUM_DUST, SlimefunItems.IRON_DUST, SlimefunItems.COBALT_INGOT, null, null, null, null, null})
        .register(plugin);

        new InfusedMagnet(itemGroups.magicalGadgets, SlimefunItems.INFUSED_MAGNET, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ENDER_LUMP_2, SlimefunItems.MAGNET, SlimefunItems.ENDER_LUMP_2, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3})
        .register(plugin);

        new SlimefunItem(itemGroups.tools, SlimefunItems.COBALT_PICKAXE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.COBALT_INGOT, SlimefunItems.COBALT_INGOT, SlimefunItems.COBALT_INGOT, null, SlimefunItems.NICKEL_INGOT, null, null, SlimefunItems.NICKEL_INGOT, null})
        .register(plugin);

        new UnplaceableBlock(itemGroups.magicalResources, SlimefunItems.NECROTIC_SKULL, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, null, StackResolver.of(Material.WITHER_SKELETON_SKULL), null, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.ESSENCE_OF_AFTERLIFE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_3, SlimefunItems.AIR_RUNE, SlimefunItems.ENDER_LUMP_3, SlimefunItems.EARTH_RUNE, SlimefunItems.NECROTIC_SKULL, SlimefunItems.FIRE_RUNE, SlimefunItems.ENDER_LUMP_3, SlimefunItems.WATER_RUNE, SlimefunItems.ENDER_LUMP_3})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.SYNTHETIC_SHULKER_SHELL, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_RUNE, SlimefunItems.ENDER_LUMP_3, SlimefunItems.REINFORCED_PLATE, StackResolver.of(Material.TURTLE_HELMET), SlimefunItems.REINFORCED_PLATE, SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_RUNE, SlimefunItems.ENDER_LUMP_3})
        .setUseableInWorkbench(true)
        .register(plugin);

        new SoulboundBackpack(36, itemGroups.magicalGadgets, SlimefunItems.BOUND_BACKPACK, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.WOVEN_BACKPACK, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2})
        .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.DURALUMIN_JETBOOTS,
        new ItemStack[] {null, null, null, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.35, 20)
        .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.SOLDER_JETBOOTS,
        new ItemStack[] {null, null, null, SlimefunItems.SOLDER_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.SOLDER_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.4, 30)
        .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.BILLON_JETBOOTS,
        new ItemStack[] {null, null, null, SlimefunItems.BILLON_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BILLON_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.45, 40)
        .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.STEEL_JETBOOTS,
        new ItemStack[] {null, null, null, SlimefunItems.STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.5, 50)
        .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.DAMASCUS_STEEL_JETBOOTS,
        new ItemStack[] {null, null, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.55, 75)
        .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.REINFORCED_ALLOY_JETBOOTS,
        new ItemStack[] {null, null, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.6, 100)
        .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.CARBONADO_JETBOOTS,
        new ItemStack[] {null, null, null, SlimefunItems.CARBONADO, SlimefunItems.POWER_CRYSTAL, SlimefunItems.CARBONADO, SlimefunItems.STEEL_THRUSTER, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.7, 125)
        .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.ARMORED_JETBOOTS,
        new ItemStack[] {null, null, null, SlimefunItems.STEEL_PLATE, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_THRUSTER, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
        0.45, 50)
        .register(plugin);

        new SeismicAxe(itemGroups.weapons, SlimefunItems.SEISMIC_AXE, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.STAFF_ELEMENTAL, null, null, SlimefunItems.STAFF_ELEMENTAL, null})
        .register(plugin);

        new PickaxeOfVeinMining(itemGroups.tools, SlimefunItems.PICKAXE_OF_VEIN_MINING, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {StackResolver.of(Material.EMERALD_ORE), SlimefunItems.SYNTHETIC_DIAMOND, StackResolver.of(Material.EMERALD_ORE), null, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
        .register(plugin);

        new ClimbingPick(itemGroups.tools, SlimefunItems.CLIMBING_PICK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.STEEL_INGOT, null, StackResolver.of(Material.STICK), null, null, StackResolver.of(Material.STICK), null})
        .register(plugin);

        new SoulboundItem(itemGroups.weapons, SlimefunItems.SOULBOUND_SWORD, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.DIAMOND_SWORD), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new SoulboundItem(itemGroups.weapons, SlimefunItems.SOULBOUND_TRIDENT, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.TRIDENT), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new SoulboundItem(itemGroups.weapons, SlimefunItems.SOULBOUND_BOW, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.BOW), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new SoulboundItem(itemGroups.tools, SlimefunItems.SOULBOUND_PICKAXE, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.DIAMOND_PICKAXE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new SoulboundItem(itemGroups.tools, SlimefunItems.SOULBOUND_AXE, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.DIAMOND_AXE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new SoulboundItem(itemGroups.tools, SlimefunItems.SOULBOUND_SHOVEL, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.DIAMOND_SHOVEL), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new SoulboundItem(itemGroups.tools, SlimefunItems.SOULBOUND_HOE, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.DIAMOND_HOE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new SoulboundItem(itemGroups.magicalArmor, SlimefunItems.SOULBOUND_HELMET, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.DIAMOND_HELMET), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new SoulboundItem(itemGroups.magicalArmor, SlimefunItems.SOULBOUND_CHESTPLATE, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.DIAMOND_CHESTPLATE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new SoulboundItem(itemGroups.magicalArmor, SlimefunItems.SOULBOUND_LEGGINGS, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.DIAMOND_LEGGINGS), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new SoulboundItem(itemGroups.magicalArmor, SlimefunItems.SOULBOUND_BOOTS, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, StackResolver.of(Material.DIAMOND_BOOTS), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new Juicer(itemGroups.basicMachines, SlimefunItems.JUICER).register(plugin);

        new Juice(itemGroups.food, SlimefunItems.APPLE_JUICE, RecipeType.JUICER,
        new ItemStack[] {StackResolver.of(Material.APPLE), null, null, null, null, null, null, null, null})
        .register(plugin);

        new Juice(itemGroups.food, SlimefunItems.CARROT_JUICE, RecipeType.JUICER,
        new ItemStack[] {StackResolver.of(Material.CARROT), null, null, null, null, null, null, null, null})
        .register(plugin);

        new Juice(itemGroups.food, SlimefunItems.MELON_JUICE, RecipeType.JUICER,
        new ItemStack[] {StackResolver.of(Material.MELON_SLICE), null, null, null, null, null, null, null, null})
        .register(plugin);

        new Juice(itemGroups.food, SlimefunItems.PUMPKIN_JUICE, RecipeType.JUICER,
        new ItemStack[] {StackResolver.of(Material.PUMPKIN), null, null, null, null, null, null, null, null})
        .register(plugin);

        new Juice(itemGroups.food, SlimefunItems.SWEET_BERRY_JUICE, RecipeType.JUICER,
        new ItemStack[] {StackResolver.of(Material.SWEET_BERRIES), null, null, null, null, null, null, null, null})
        .register(plugin);

        new Juice(itemGroups.food, SlimefunItems.GOLDEN_APPLE_JUICE, RecipeType.JUICER,
        new ItemStack[] {StackResolver.of(Material.GOLDEN_APPLE), null, null, null, null, null, null, null, null})
        .register(plugin);

        new VanillaItem(itemGroups.food, StackResolver.of(Material.ENCHANTED_GOLDEN_APPLE), "ENCHANTED_GOLDEN_APPLE", RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GOLD_24K_BLOCK, SlimefunItems.GOLD_24K_BLOCK, SlimefunItems.GOLD_24K_BLOCK, SlimefunItems.GOLD_24K_BLOCK, StackResolver.of(Material.APPLE), SlimefunItems.GOLD_24K_BLOCK, SlimefunItems.GOLD_24K_BLOCK, SlimefunItems.GOLD_24K_BLOCK, SlimefunItems.GOLD_24K_BLOCK})
        .register(plugin);

        new BrokenSpawner(itemGroups.magicalResources, SlimefunItems.BROKEN_SPAWNER, new RecipeType(new NamespacedKey(plugin, "pickaxe_of_containment"), SlimefunItems.PICKAXE_OF_CONTAINMENT),
        new ItemStack[] {null, null, null, null, StackResolver.of(Material.SPAWNER), null, null, null, null})
        .register(plugin);

        new RepairedSpawner(itemGroups.magicalGadgets, SlimefunItems.REPAIRED_SPAWNER, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.ENDER_RUNE, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.BROKEN_SPAWNER, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.ENDER_RUNE})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 1, 1, 1, SlimefunItems.ENHANCED_FURNACE,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, StackResolver.of(Material.FURNACE), SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 2, 1, 1, SlimefunItems.ENHANCED_FURNACE_2,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 2, 2, 1, SlimefunItems.ENHANCED_FURNACE_3,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_2, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 3, 2, 1, SlimefunItems.ENHANCED_FURNACE_4,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_3, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 3, 2, 2, SlimefunItems.ENHANCED_FURNACE_5,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_4, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 3, 3, 2, SlimefunItems.ENHANCED_FURNACE_6,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_5, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 4, 3, 2, SlimefunItems.ENHANCED_FURNACE_7,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_6, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 4, 4, 2, SlimefunItems.ENHANCED_FURNACE_8,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_7, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 5, 4, 2, SlimefunItems.ENHANCED_FURNACE_9,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_8, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 5, 5, 2, SlimefunItems.ENHANCED_FURNACE_10,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_9, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 5, 5, 3, SlimefunItems.ENHANCED_FURNACE_11,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_10, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 10, 10, 3, SlimefunItems.REINFORCED_FURNACE,
        new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_11, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT})
        .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 20, 10, 3, SlimefunItems.CARBONADO_EDGED_FURNACE,
        new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.CARBONADO, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_FURNACE, SlimefunItems.HEATING_COIL, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO})
        .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.ELECTRO_MAGNET, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.NICKEL_INGOT, SlimefunItems.MAGNET, SlimefunItems.COBALT_INGOT, null, SlimefunItems.BATTERY, null, null, null, null})
        .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.ELECTRIC_MOTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, null, SlimefunItems.ELECTRO_MAGNET, null, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE})
        .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.HEATING_COIL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE})
        .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.COPPER_WIRE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, null, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT, null, null, null},
        new SlimefunItemStack(SlimefunItems.COPPER_WIRE, 8))
        .register(plugin);

        new BlockPlacer(itemGroups.basicMachines, SlimefunItems.BLOCK_PLACER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GOLD_4K, StackResolver.of(Material.PISTON), SlimefunItems.GOLD_4K, StackResolver.of(Material.IRON_INGOT), SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.IRON_INGOT), SlimefunItems.GOLD_4K, StackResolver.of(Material.PISTON), SlimefunItems.GOLD_4K})
        .register(plugin);

        new TelepositionScroll(itemGroups.magicalGadgets, SlimefunItems.SCROLL_OF_DIMENSIONAL_TELEPOSITION, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, null})
        .register(plugin);

        new ExplosiveBow(itemGroups.weapons, SlimefunItems.EXPLOSIVE_BOW,
        new ItemStack[] {null, StackResolver.of(Material.STICK), StackResolver.of(Material.GUNPOWDER), SlimefunItems.STAFF_FIRE, null, SlimefunItems.SULFATE, null, StackResolver.of(Material.STICK), StackResolver.of(Material.GUNPOWDER)})
        .register(plugin);

        new IcyBow(itemGroups.weapons, SlimefunItems.ICY_BOW,
        new ItemStack[] {null, StackResolver.of(Material.STICK), StackResolver.of(Material.ICE), SlimefunItems.STAFF_WATER, null, StackResolver.of(Material.PACKED_ICE), null, StackResolver.of(Material.STICK), StackResolver.of(Material.ICE)})
        .register(plugin);

        new KnowledgeTome(itemGroups.magicalGadgets, SlimefunItems.TOME_OF_KNOWLEDGE_SHARING, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, StackResolver.of(Material.FEATHER), null, StackResolver.of(Material.INK_SAC), SlimefunItems.MAGICAL_BOOK_COVER, StackResolver.of(Material.GLASS_BOTTLE), null, StackResolver.of(Material.WRITABLE_BOOK), null})
        .register(plugin);

        new KnowledgeFlask(itemGroups.magicalGadgets, SlimefunItems.FLASK_OF_KNOWLEDGE, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, null, null, SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.GLASS_PANE), SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, null},
        new SlimefunItemStack(SlimefunItems.FLASK_OF_KNOWLEDGE, 8))
        .register(plugin);

        new HiddenItem(itemGroups.magicalGadgets, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, RecipeType.INTERACT,
        new ItemStack[] {SlimefunItems.FLASK_OF_KNOWLEDGE, null, null, null, null, null, null, null, null})
        .register(plugin);

        new BirthdayCake(itemGroups.birthday, new SlimefunItemStack("BIRTHDAY_CAKE", Material.CAKE, "&bBirthday Cake"), RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.TORCH), null, StackResolver.of(Material.SUGAR), StackResolver.of(Material.CAKE), StackResolver.of(Material.SUGAR), null, null, null})
        .register(plugin);

        new Juice(itemGroups.christmas, SlimefunItems.CHRISTMAS_MILK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.MILK_BUCKET), StackResolver.of(Material.GLASS_BOTTLE), null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_MILK, 4))
        .register(plugin);

        new Juice(itemGroups.christmas, SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CHRISTMAS_MILK, StackResolver.of(Material.COCOA_BEANS), null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, 2))
        .register(plugin);

        new Juice(itemGroups.christmas, SlimefunItems.CHRISTMAS_EGG_NOG, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CHRISTMAS_MILK, StackResolver.of(Material.EGG), null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_EGG_NOG, 2))
        .register(plugin);

        new Juice(itemGroups.christmas, SlimefunItems.CHRISTMAS_APPLE_CIDER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.APPLE_JUICE, StackResolver.of(Material.SUGAR), null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_APPLE_CIDER, 2))
        .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_COOKIE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.COOKIE), StackResolver.of(Material.SUGAR), StackResolver.of(Material.LIME_DYE), null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_COOKIE, 16))
        .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_FRUIT_CAKE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.EGG), StackResolver.of(Material.APPLE), StackResolver.of(Material.MELON), StackResolver.of(Material.SUGAR), null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_FRUIT_CAKE, 4))
        .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_APPLE_PIE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.SUGAR), StackResolver.of(Material.APPLE), StackResolver.of(Material.EGG), null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_APPLE_PIE, 2))
        .register(plugin);

        new Juice(itemGroups.christmas, SlimefunItems.CHRISTMAS_HOT_CHOCOLATE, RecipeType.SMELTERY,
        new ItemStack[] {SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, null, null, null, null, null, null, null, null}, SlimefunItems.CHRISTMAS_HOT_CHOCOLATE)
        .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_CAKE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.EGG), StackResolver.of(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, StackResolver.of(Material.MILK_BUCKET), null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_CAKE, 4))
        .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_CARAMEL, RecipeType.SMELTERY,
        new ItemStack[] {StackResolver.of(Material.SUGAR), StackResolver.of(Material.SUGAR), null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_CARAMEL, 4))
        .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_CARAMEL_APPLE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.CHRISTMAS_CARAMEL, null, null, StackResolver.of(Material.APPLE), null, null, StackResolver.of(Material.STICK), null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_CARAMEL_APPLE, 2))
        .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_CHOCOLATE_APPLE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.COCOA_BEANS), null, null, StackResolver.of(Material.APPLE), null, null, StackResolver.of(Material.STICK), null},
        new SlimefunItemStack(SlimefunItems.CHRISTMAS_CHOCOLATE_APPLE, 2))
        .register(plugin);

        new ChristmasPresent(itemGroups.christmas, SlimefunItems.CHRISTMAS_PRESENT, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, StackResolver.of(Material.NAME_TAG), null, StackResolver.of(Material.RED_WOOL), StackResolver.of(Material.GREEN_WOOL), StackResolver.of(Material.RED_WOOL), StackResolver.of(Material.RED_WOOL), StackResolver.of(Material.GREEN_WOOL), StackResolver.of(Material.RED_WOOL)},
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_HOT_CHOCOLATE, 1),
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_CHOCOLATE_APPLE, 4),
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_CARAMEL_APPLE, 4),
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_CAKE, 4),
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_COOKIE, 8),
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_PRESENT, 1),
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_EGG_NOG, 1),
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_MILK, 1),
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_APPLE_CIDER, 1),
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_FRUIT_CAKE, 4),
            new SlimefunItemStack(SlimefunItems.CHRISTMAS_APPLE_PIE, 4),
            StackResolver.of(Material.EMERALD)
        ).register(plugin);

        new SlimefunItem(itemGroups.easter, SlimefunItems.EASTER_CARROT_PIE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.SUGAR), StackResolver.of(Material.CARROT), StackResolver.of(Material.EGG), null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.EASTER_CARROT_PIE, 2))
        .register(plugin);

        new SlimefunItem(itemGroups.easter, SlimefunItems.EASTER_APPLE_PIE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.SUGAR), StackResolver.of(Material.APPLE), StackResolver.of(Material.EGG), null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.EASTER_APPLE_PIE, 2))
        .register(plugin);

        new EasterEgg(itemGroups.easter, SlimefunItems.EASTER_EGG, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.LIME_DYE), StackResolver.of(Material.EGG), StackResolver.of(Material.PURPLE_DYE), null, null, null},
        new SlimefunItemStack(SlimefunItems.EASTER_EGG, 2),
        // Gifts:
            new SlimefunItemStack(SlimefunItems.EASTER_CARROT_PIE, 4),
            new SlimefunItemStack(SlimefunItems.CARROT_JUICE, 1),
            StackResolver.of(Material.EMERALD),
            StackResolver.of(Material.CAKE),
            StackResolver.of(Material.RABBIT_FOOT),
            StackResolver.of(Material.GOLDEN_CARROT, 4)
        ).register(plugin);

        itemGroups.rickFlexGroup.register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.REINFORCED_PLATE, RecipeType.COMPRESSOR,
        new ItemStack[] {new SlimefunItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 8), null, null, null, null, null, null, null, null})
        .register(plugin);

        new HardenedGlass(itemGroups.technicalComponents, SlimefunItems.HARDENED_GLASS, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.GLASS), StackResolver.of(Material.GLASS), StackResolver.of(Material.GLASS), StackResolver.of(Material.GLASS), SlimefunItems.REINFORCED_PLATE, StackResolver.of(Material.GLASS), StackResolver.of(Material.GLASS), StackResolver.of(Material.GLASS), StackResolver.of(Material.GLASS)},
        new SlimefunItemStack(SlimefunItems.HARDENED_GLASS, 16))
        .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.COOLING_UNIT, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.ICE), StackResolver.of(Material.ICE), StackResolver.of(Material.ICE), SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_INGOT, StackResolver.of(Material.ICE), StackResolver.of(Material.ICE), StackResolver.of(Material.ICE)})
        .register(plugin);

        new Cooler(27, itemGroups.usefulItems, SlimefunItems.COOLER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.COOLING_UNIT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT})
        .register(plugin);

        new WitherProofBlock(itemGroups.technicalComponents, SlimefunItems.WITHER_PROOF_OBSIDIAN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.LEAD_INGOT, StackResolver.of(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT, StackResolver.of(Material.OBSIDIAN), SlimefunItems.HARDENED_GLASS, StackResolver.of(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT, StackResolver.of(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT},
        new SlimefunItemStack(SlimefunItems.WITHER_PROOF_OBSIDIAN, 4))
        .register(plugin);

        new AncientPedestal(itemGroups.magicalGadgets, SlimefunItems.ANCIENT_PEDESTAL, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {StackResolver.of(Material.OBSIDIAN), SlimefunItems.GOLD_8K, StackResolver.of(Material.OBSIDIAN), null, StackResolver.of(Material.STONE), null, StackResolver.of(Material.OBSIDIAN), SlimefunItems.GOLD_8K, StackResolver.of(Material.OBSIDIAN)},
        new SlimefunItemStack(SlimefunItems.ANCIENT_PEDESTAL, 4))
        .register(plugin);

        new AncientAltar(itemGroups.magicalGadgets, SlimefunItems.ANCIENT_ALTAR, RecipeType.MAGIC_WORKBENCH,
        new ItemStack[] {null, StackResolver.of(Material.ENCHANTING_TABLE), null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_3, StackResolver.of(Material.OBSIDIAN), SlimefunItems.GOLD_8K, StackResolver.of(Material.OBSIDIAN)})
        .register(plugin);

        new EnergyRegulator(itemGroups.electricity, SlimefunItems.ENERGY_REGULATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT})
        .register(plugin);

        new EnergyConnector(itemGroups.electricity, SlimefunItems.ENERGY_CONNECTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CARBON, SlimefunItems.COPPER_WIRE, SlimefunItems.CARBON, SlimefunItems.COPPER_WIRE, StackResolver.of(Material.REDSTONE_BLOCK), SlimefunItems.COPPER_WIRE, SlimefunItems.CARBON, SlimefunItems.COPPER_WIRE, SlimefunItems.CARBON},
        new SlimefunItemStack(SlimefunItems.ENERGY_CONNECTOR, 8))
        .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.DUCT_TAPE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.ALUMINUM_DUST, SlimefunItems.ALUMINUM_DUST, StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.PAPER), StackResolver.of(Material.PAPER), StackResolver.of(Material.PAPER)},
        new SlimefunItemStack(SlimefunItems.DUCT_TAPE, 2))
        .register(plugin);

        new Capacitor(itemGroups.electricity, 128, SlimefunItems.SMALL_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.DURALUMIN_INGOT, SlimefunItems.SULFATE, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.ENERGY_CONNECTOR, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.DURALUMIN_INGOT, StackResolver.of(Material.REDSTONE), SlimefunItems.DURALUMIN_INGOT})
        .register(plugin);

        new Capacitor(itemGroups.electricity, 512, SlimefunItems.MEDIUM_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.BILLON_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.BILLON_INGOT, StackResolver.of(Material.REDSTONE), SlimefunItems.SMALL_CAPACITOR, StackResolver.of(Material.REDSTONE), SlimefunItems.BILLON_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.BILLON_INGOT})
        .register(plugin);

        new Capacitor(itemGroups.electricity, 1024, SlimefunItems.BIG_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.STEEL_INGOT, StackResolver.of(Material.REDSTONE), SlimefunItems.MEDIUM_CAPACITOR, StackResolver.of(Material.REDSTONE), SlimefunItems.STEEL_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.STEEL_INGOT})
        .register(plugin);

        new Capacitor(itemGroups.electricity, 8192, SlimefunItems.LARGE_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REINFORCED_ALLOY_INGOT, StackResolver.of(Material.REDSTONE), SlimefunItems.BIG_CAPACITOR, StackResolver.of(Material.REDSTONE), SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REINFORCED_ALLOY_INGOT})
        .register(plugin);

        new Capacitor(itemGroups.electricity, 65536, SlimefunItems.CARBONADO_EDGED_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.CARBONADO, StackResolver.of(Material.REDSTONE), SlimefunItems.LARGE_CAPACITOR, StackResolver.of(Material.REDSTONE), SlimefunItems.CARBONADO, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.CARBONADO})
        .register(plugin);

        new Capacitor(itemGroups.electricity, 524288, SlimefunItems.ENERGIZED_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.CARBONADO, StackResolver.of(Material.NETHER_STAR), SlimefunItems.CARBONADO_EDGED_CAPACITOR, StackResolver.of(Material.NETHER_STAR), SlimefunItems.CARBONADO, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.CARBONADO})
        .register(plugin);

        new SolarGenerator(itemGroups.electricity, 2, 0, SlimefunItems.SOLAR_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SOLAR_PANEL, SlimefunItems.SOLAR_PANEL, SlimefunItems.SOLAR_PANEL, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_INGOT, null, SlimefunItems.ALUMINUM_INGOT, null})
        .register(plugin);

        new SolarGenerator(itemGroups.electricity, 8, 0, SlimefunItems.SOLAR_GENERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SOLAR_GENERATOR, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR, SlimefunItems.ALUMINUM_INGOT, StackResolver.of(Material.REDSTONE), SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR})
        .register(plugin);

        new SolarGenerator(itemGroups.electricity, 32, 0, SlimefunItems.SOLAR_GENERATOR_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SOLAR_GENERATOR_2, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR_2, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.CARBONADO, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR_2, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR_2})
        .register(plugin);

        new SolarGenerator(itemGroups.electricity, 128, 64, SlimefunItems.SOLAR_GENERATOR_4, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SOLAR_GENERATOR_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.SOLAR_GENERATOR_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.SOLAR_GENERATOR_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.SOLAR_GENERATOR_3})
        .register(plugin);

        new ChargingBench(itemGroups.electricity, SlimefunItems.CHARGING_BENCH, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET, null, SlimefunItems.BATTERY, StackResolver.of(Material.CRAFTING_TABLE), SlimefunItems.BATTERY, null, SlimefunItems.SMALL_CAPACITOR, null})
        .setCapacity(128)
        .setEnergyConsumption(10)
        .setProcessingSpeed(1)
        .register(plugin);

        new ElectricFurnace(itemGroups.electricity, SlimefunItems.ELECTRIC_FURNACE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.FURNACE), null, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.GILDED_IRON})
        .setCapacity(64)
        .setEnergyConsumption(2)
        .setProcessingSpeed(1)
        .register(plugin);

        new ElectricFurnace(itemGroups.electricity, SlimefunItems.ELECTRIC_FURNACE_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_FURNACE, SlimefunItems.GILDED_IRON, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON})
        .setCapacity(128)
        .setEnergyConsumption(3)
        .setProcessingSpeed(2)
        .register(plugin);

        new ElectricFurnace(itemGroups.electricity, SlimefunItems.ELECTRIC_FURNACE_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_FURNACE_2, SlimefunItems.STEEL_INGOT, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON})
        .setCapacity(128)
        .setEnergyConsumption(5)
        .setProcessingSpeed(4)
        .register(plugin);

        new ElectricGoldPan(itemGroups.electricity, SlimefunItems.ELECTRIC_GOLD_PAN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.GOLD_PAN, null, StackResolver.of(Material.FLINT), SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.FLINT), SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT})
        .setCapacity(128)
        .setEnergyConsumption(1)
        .setProcessingSpeed(1)
        .register(plugin);

        new ElectricGoldPan(itemGroups.electricity, SlimefunItems.ELECTRIC_GOLD_PAN_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.GOLD_PAN, null, StackResolver.of(Material.IRON_INGOT), SlimefunItems.ELECTRIC_GOLD_PAN, StackResolver.of(Material.IRON_INGOT), SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT})
        .setCapacity(128)
        .setEnergyConsumption(2)
        .setProcessingSpeed(3)
        .register(plugin);

        new ElectricGoldPan(itemGroups.electricity, SlimefunItems.ELECTRIC_GOLD_PAN_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.GOLD_PAN, null, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRIC_GOLD_PAN_2, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.COBALT_INGOT, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.COBALT_INGOT})
        .setCapacity(512)
        .setEnergyConsumption(7)
        .setProcessingSpeed(10)
        .register(plugin);

        new ElectricDustWasher(itemGroups.electricity, SlimefunItems.ELECTRIC_DUST_WASHER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.WATER_BUCKET), null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_GOLD_PAN, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT})
        .setCapacity(128)
        .setEnergyConsumption(3)
        .setProcessingSpeed(1)
        .register(plugin);

        new ElectricDustWasher(itemGroups.electricity, SlimefunItems.ELECTRIC_DUST_WASHER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.WATER_BUCKET), null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_DUST_WASHER, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT})
        .setCapacity(128)
        .setEnergyConsumption(5)
        .setProcessingSpeed(2)
        .register(plugin);

        new ElectricDustWasher(itemGroups.electricity, SlimefunItems.ELECTRIC_DUST_WASHER_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.WATER_BUCKET), null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_DUST_WASHER_2, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.CORINTHIAN_BRONZE_INGOT})
        .setCapacity(512)
        .setEnergyConsumption(15)
        .setProcessingSpeed(10)
        .register(plugin);

        new ElectricIngotFactory(itemGroups.electricity, SlimefunItems.ELECTRIC_INGOT_FACTORY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.FLINT_AND_STEEL), null, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_DUST_WASHER, SlimefunItems.HEATING_COIL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT})
        .setCapacity(256)
        .setEnergyConsumption(4)
        .setProcessingSpeed(1)
        .register(plugin);

        new ElectricIngotFactory(itemGroups.electricity, SlimefunItems.ELECTRIC_INGOT_FACTORY_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GILDED_IRON, StackResolver.of(Material.FLINT_AND_STEEL), SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_INGOT_FACTORY, SlimefunItems.HEATING_COIL, SlimefunItems.BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BRASS_INGOT})
        .setCapacity(256)
        .setEnergyConsumption(7)
        .setProcessingSpeed(2)
        .register(plugin);

        new ElectricIngotFactory(itemGroups.electricity, SlimefunItems.ELECTRIC_INGOT_FACTORY_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GILDED_IRON, StackResolver.of(Material.FLINT_AND_STEEL), SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_INGOT_FACTORY_2, SlimefunItems.HEATING_COIL, SlimefunItems.BRASS_INGOT, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.BRASS_INGOT})
        .setCapacity(512)
        .setEnergyConsumption(20)
        .setProcessingSpeed(8)
        .register(plugin);

        new ElectrifiedCrucible(itemGroups.electricity, SlimefunItems.ELECTRIFIED_CRUCIBLE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.CRUCIBLE, SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.LEAD_INGOT})
        .setCapacity(1024)
        .setEnergyConsumption(24)
        .setProcessingSpeed(1)
        .register(plugin);

        new ElectrifiedCrucible(itemGroups.electricity, SlimefunItems.ELECTRIFIED_CRUCIBLE_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.ELECTRIFIED_CRUCIBLE, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.LEAD_INGOT})
        .setCapacity(1024)
        .setEnergyConsumption(40)
        .setProcessingSpeed(2)
        .register(plugin);

        new ElectrifiedCrucible(itemGroups.electricity, SlimefunItems.ELECTRIFIED_CRUCIBLE_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.ELECTRIFIED_CRUCIBLE_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.STEEL_PLATE, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_PLATE, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT})
        .setCapacity(1024)
        .setEnergyConsumption(60)
        .setProcessingSpeed(4)
        .register(plugin);

        new ElectricOreGrinder(itemGroups.electricity, SlimefunItems.ELECTRIC_ORE_GRINDER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.DIAMOND_PICKAXE), null, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.GILDED_IRON})
        .setCapacity(128)
        .setEnergyConsumption(6)
        .setProcessingSpeed(1)
        .register(plugin);

        new ElectricOreGrinder(itemGroups.electricity, SlimefunItems.ELECTRIC_ORE_GRINDER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.DIAMOND_PICKAXE), null, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_ORE_GRINDER, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GILDED_IRON})
        .setCapacity(512)
        .setEnergyConsumption(15)
        .setProcessingSpeed(4)
        .register(plugin);

        new ElectricOreGrinder(itemGroups.electricity, SlimefunItems.ELECTRIC_ORE_GRINDER_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.REINFORCED_PLATE, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_PLATE, null, SlimefunItems.ELECTRIC_ORE_GRINDER_2, null, SlimefunItems.REINFORCED_PLATE, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.REINFORCED_PLATE})
        .setCapacity(1024)
        .setEnergyConsumption(45)
        .setProcessingSpeed(10)
        .register(plugin);

        new HeatedPressureChamber(itemGroups.electricity, SlimefunItems.HEATED_PRESSURE_CHAMBER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, StackResolver.of(Material.GLASS), SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.LEAD_INGOT})
        .setCapacity(128)
        .setEnergyConsumption(5)
        .setProcessingSpeed(1)
        .register(plugin);

        new HeatedPressureChamber(itemGroups.electricity, SlimefunItems.HEATED_PRESSURE_CHAMBER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATED_PRESSURE_CHAMBER, SlimefunItems.LEAD_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_ALLOY_INGOT})
        .setCapacity(256)
        .setEnergyConsumption(22)
        .setProcessingSpeed(5)
        .register(plugin);

        new ElectricIngotPulverizer(itemGroups.electricity, SlimefunItems.ELECTRIC_INGOT_PULVERIZER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.ELECTRIC_ORE_GRINDER, null, SlimefunItems.LEAD_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.LEAD_INGOT})
        .setCapacity(512)
        .setEnergyConsumption(7)
        .setProcessingSpeed(1)
        .register(plugin);

        new CoalGenerator(itemGroups.electricity, SlimefunItems.COAL_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.HEATING_COIL, StackResolver.of(Material.FURNACE), SlimefunItems.HEATING_COIL, SlimefunItems.NICKEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.NICKEL_INGOT, null, SlimefunItems.NICKEL_INGOT, null})
        .setCapacity(64)
        .setEnergyProduction(8)
        .register(plugin);

        new CoalGenerator(itemGroups.electricity, SlimefunItems.COAL_GENERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.MAGMA_BLOCK), SlimefunItems.HEATING_COIL, StackResolver.of(Material.MAGMA_BLOCK), SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.COAL_GENERATOR, SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .setCapacity(256)
        .setEnergyProduction(15)
        .register(plugin);

        new BioGenerator(itemGroups.electricity, SlimefunItems.BIO_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.HEATING_COIL, SlimefunItems.COMPOSTER, SlimefunItems.HEATING_COIL, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT, null, SlimefunItems.ALUMINUM_BRASS_INGOT, null})
        .setCapacity(128)
        .setEnergyProduction(4)
        .register(plugin);

        new AutoDrier(itemGroups.electricity, SlimefunItems.AUTO_DRIER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[]{null, null, null, SlimefunItems.HEATING_COIL, StackResolver.of(Material.SMOKER), SlimefunItems.HEATING_COIL, null, StackResolver.of(Material.CAMPFIRE), null})
        .setCapacity(128)
        .setEnergyConsumption(5)
        .setProcessingSpeed(1)
        .register(plugin);

        new AutoBrewer(itemGroups.electricity, SlimefunItems.AUTO_BREWER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.HEATING_COIL, null, SlimefunItems.REINFORCED_PLATE, StackResolver.of(Material.BREWING_STAND), SlimefunItems.REINFORCED_PLATE, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .setCapacity(128)
        .setEnergyConsumption(6)
        .setProcessingSpeed(1)
        .register(plugin);

        new ElectricPress(itemGroups.electricity, SlimefunItems.ELECTRIC_PRESS, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.PISTON), null, SlimefunItems.MEDIUM_CAPACITOR, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT})
        .setCapacity(256)
        .setEnergyConsumption(8)
        .setProcessingSpeed(1)
        .register(plugin);

        new ElectricPress(itemGroups.electricity, SlimefunItems.ELECTRIC_PRESS_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.STICKY_PISTON), SlimefunItems.ELECTRIC_PRESS, StackResolver.of(Material.STICKY_PISTON), SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.BIG_CAPACITOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT})
        .setCapacity(1024)
        .setEnergyConsumption(20)
        .setProcessingSpeed(3)
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.MAGNESIUM_SALT, RecipeType.HEATED_PRESSURE_CHAMBER,
        new ItemStack[] {SlimefunItems.MAGNESIUM_DUST, SlimefunItems.SALT, null, null, null, null, null, null, null})
        .register(plugin);

        new MagnesiumGenerator(itemGroups.electricity, SlimefunItems.MAGNESIUM_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.COMPRESSED_CARBON, StackResolver.of(Material.WATER_BUCKET), SlimefunItems.COMPRESSED_CARBON, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT})
        .setCapacity(128)
        .setEnergyProduction(18)
        .register(plugin);

        new AutoEnchanter(itemGroups.electricity, SlimefunItems.AUTO_ENCHANTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.ENCHANTING_TABLE), null, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN})
        .setCapacity(128)
        .setEnergyConsumption(9)
        .setProcessingSpeed(1)
        .register(plugin);

        new AutoEnchanter(itemGroups.electricity, SlimefunItems.AUTO_ENCHANTER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.REINFORCED_PLATE, SlimefunItems.BIG_CAPACITOR, SlimefunItems.REINFORCED_PLATE, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.AUTO_ENCHANTER, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.WITHER_PROOF_OBSIDIAN})
        .setCapacity(1024)
        .setEnergyConsumption(24)
        .setProcessingSpeed(3)
        .register(plugin);

        new AutoDisenchanter(itemGroups.electricity, SlimefunItems.AUTO_DISENCHANTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.REDSTONE), StackResolver.of(Material.ANVIL), StackResolver.of(Material.REDSTONE), SlimefunItems.CARBONADO, SlimefunItems.AUTO_ENCHANTER, SlimefunItems.CARBONADO, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN})
        .setCapacity(128)
        .setEnergyConsumption(9)
        .setProcessingSpeed(1)
        .register(plugin);

        new AutoDisenchanter(itemGroups.electricity, SlimefunItems.AUTO_DISENCHANTER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.REINFORCED_PLATE, StackResolver.of(Material.ANVIL), SlimefunItems.REINFORCED_PLATE, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.AUTO_DISENCHANTER, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.BIG_CAPACITOR, SlimefunItems.WITHER_PROOF_OBSIDIAN})
        .setCapacity(1024)
        .setEnergyConsumption(24)
        .setProcessingSpeed(3)
        .register(plugin);

        new AutoAnvil(itemGroups.electricity, 10, SlimefunItems.AUTO_ANVIL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.ANVIL), null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, StackResolver.of(Material.IRON_BLOCK), StackResolver.of(Material.IRON_BLOCK), StackResolver.of(Material.IRON_BLOCK)})
        .setCapacity(128)
        .setEnergyConsumption(12)
        .setProcessingSpeed(1)
        .register(plugin);

        new AutoAnvil(itemGroups.electricity, 25, SlimefunItems.AUTO_ANVIL_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.AUTO_ANVIL, null, SlimefunItems.STEEL_PLATE, SlimefunItems.HEATING_COIL, SlimefunItems.STEEL_PLATE, StackResolver.of(Material.IRON_BLOCK), StackResolver.of(Material.IRON_BLOCK), StackResolver.of(Material.IRON_BLOCK)})
        .setCapacity(256)
        .setEnergyConsumption(16)
        .setProcessingSpeed(1)
        .register(plugin);

        new BookBinder(itemGroups.electricity, SlimefunItems.BOOK_BINDER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.ENCHANTING_TABLE), null, StackResolver.of(Material.BOOKSHELF), SlimefunItems.HARDENED_METAL_INGOT, StackResolver.of(Material.BOOKSHELF), SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.SYNTHETIC_SAPPHIRE})
        .setCapacity(256)
        .setEnergyConsumption(16)
        .setProcessingSpeed(1)
        .register(plugin);

        new Multimeter(itemGroups.technicalGadgets, SlimefunItems.MULTIMETER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.COPPER_INGOT, null, SlimefunItems.COPPER_INGOT, null, SlimefunItems.REDSTONE_ALLOY, null, null, SlimefunItems.GOLD_6K, null})
        .register(plugin);

        new SlimefunItem(itemGroups.technicalComponents, SlimefunItems.PLASTIC_SHEET, RecipeType.HEATED_PRESSURE_CHAMBER,
        new ItemStack[] {null, null, null, null, SlimefunItems.OIL_BUCKET, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.PLASTIC_SHEET, 8))
        .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.ANDROID_MEMORY_CORE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.BRASS_INGOT, StackResolver.of(Material.ORANGE_STAINED_GLASS), SlimefunItems.BRASS_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.TIN_DUST, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BRASS_INGOT, StackResolver.of(Material.ORANGE_STAINED_GLASS), SlimefunItems.BRASS_INGOT})
        .register(plugin);

        new GPSTransmitter(itemGroups.gps, 1, SlimefunItems.GPS_TRANSMITTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.STEEL_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT}) {

            @Override
            public int getMultiplier(int y) {
                return y;
            }

            @Override
            public int getEnergyConsumption() {
                return 1;
            }

        }.register(plugin);

        new GPSTransmitter(itemGroups.gps, 2, SlimefunItems.GPS_TRANSMITTER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.CARBON, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER}) {

            @Override
            public int getMultiplier(int y) {
                return y * 4 + 100;
            }

            @Override
            public int getEnergyConsumption() {
                return 3;
            }

        }.register(plugin);

        new GPSTransmitter(itemGroups.gps, 3, SlimefunItems.GPS_TRANSMITTER_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.CARBONADO, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2}) {

            @Override
            public int getMultiplier(int y) {
                return y * 16 + 500;
            }

            @Override
            public int getEnergyConsumption() {
                return 11;
            }

        }.register(plugin);

        new GPSTransmitter(itemGroups.gps, 4, SlimefunItems.GPS_TRANSMITTER_4, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.NICKEL_INGOT, SlimefunItems.CARBONADO, SlimefunItems.NICKEL_INGOT, SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GPS_TRANSMITTER_3}) {

            @Override
            public int getMultiplier(int y) {
                return y * 64 + 2100;
            }

            @Override
            public int getEnergyConsumption() {
                return 43;
            }

        }.register(plugin);

        new GPSControlPanel(itemGroups.gps, SlimefunItems.GPS_CONTROL_PANEL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.COBALT_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.COBALT_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT})
        .register(plugin);

        new GPSMarkerTool(itemGroups.gps, SlimefunItems.GPS_MARKER_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET, null, StackResolver.of(Material.LAPIS_LAZULI), SlimefunItems.BASIC_CIRCUIT_BOARD, StackResolver.of(Material.LAPIS_LAZULI), StackResolver.of(Material.REDSTONE), SlimefunItems.REDSTONE_ALLOY, StackResolver.of(Material.REDSTONE)})
        .register(plugin);

        new SlimefunItem(itemGroups.gps, SlimefunItems.GPS_EMERGENCY_TRANSMITTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET, null, null, SlimefunItems.GPS_TRANSMITTER, null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
        .register(plugin);

        new AndroidInterface(itemGroups.androids, SlimefunItems.ANDROID_INTERFACE_ITEMS, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.STEEL_INGOT, SlimefunItems.PLASTIC_SHEET, SlimefunItems.STEEL_INGOT, SlimefunItems.BASIC_CIRCUIT_BOARD, StackResolver.of(Material.BLUE_STAINED_GLASS), SlimefunItems.PLASTIC_SHEET, SlimefunItems.STEEL_INGOT, SlimefunItems.PLASTIC_SHEET})
        .register(plugin);

        new AndroidInterface(itemGroups.androids, SlimefunItems.ANDROID_INTERFACE_FUEL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.STEEL_INGOT, SlimefunItems.PLASTIC_SHEET, StackResolver.of(Material.RED_STAINED_GLASS), SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.STEEL_INGOT, SlimefunItems.PLASTIC_SHEET, SlimefunItems.STEEL_INGOT, SlimefunItems.PLASTIC_SHEET})
        .register(plugin);

        new ProgrammableAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.PLASTIC_SHEET, SlimefunItems.COAL_GENERATOR, SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.CHEST), SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET})
        .register(plugin);

        new MinerAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID_MINER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.DIAMOND_PICKAXE), SlimefunItems.PROGRAMMABLE_ANDROID, StackResolver.of(Material.DIAMOND_PICKAXE), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new FarmerAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID_FARMER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.DIAMOND_HOE), SlimefunItems.PROGRAMMABLE_ANDROID, StackResolver.of(Material.DIAMOND_HOE), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new WoodcutterAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.DIAMOND_AXE), SlimefunItems.PROGRAMMABLE_ANDROID, StackResolver.of(Material.DIAMOND_AXE), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new FishermanAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID_FISHERMAN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID, StackResolver.of(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new ButcherAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID_BUTCHER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, StackResolver.of(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID, StackResolver.of(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new ProgrammableAndroid(itemGroups.androids, 2, SlimefunItems.PROGRAMMABLE_ANDROID_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.PLASTIC_SHEET, SlimefunItems.COMBUSTION_REACTOR, SlimefunItems.PROGRAMMABLE_ANDROID, StackResolver.of(Material.CHEST), SlimefunItems.PLASTIC_SHEET, SlimefunItems.POWER_CRYSTAL, SlimefunItems.PLASTIC_SHEET})
        .register(plugin);

        new FishermanAndroid(itemGroups.androids, 2, SlimefunItems.PROGRAMMABLE_ANDROID_2_FISHERMAN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID_2, StackResolver.of(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new ButcherAndroid(itemGroups.androids, 2, SlimefunItems.PROGRAMMABLE_ANDROID_2_BUTCHER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, StackResolver.of(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID_2, StackResolver.of(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new FarmerAndroid(itemGroups.androids, 2, SlimefunItems.PROGRAMMABLE_ANDROID_2_FARMER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, StackResolver.of(Material.DIAMOND_HOE), SlimefunItems.PROGRAMMABLE_ANDROID_2, StackResolver.of(Material.DIAMOND_HOE), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new ProgrammableAndroid(itemGroups.androids, 3, SlimefunItems.PROGRAMMABLE_ANDROID_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.PLASTIC_SHEET, SlimefunItems.NUCLEAR_REACTOR, SlimefunItems.PROGRAMMABLE_ANDROID_2, StackResolver.of(Material.CHEST), SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BLISTERING_INGOT_3})
        .register(plugin);

        new FishermanAndroid(itemGroups.androids, 3, SlimefunItems.PROGRAMMABLE_ANDROID_3_FISHERMAN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, null, StackResolver.of(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID_3, StackResolver.of(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new ButcherAndroid(itemGroups.androids, 3, SlimefunItems.PROGRAMMABLE_ANDROID_3_BUTCHER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER_3, null, StackResolver.of(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID_3, StackResolver.of(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.BLANK_RUNE,
        new ItemStack[] {StackResolver.of(Material.STONE), SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.STONE), SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.OBSIDIAN), SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.STONE), SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.STONE)})
        .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.AIR_RUNE,
        new ItemStack[] {StackResolver.of(Material.FEATHER), SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.FEATHER), StackResolver.of(Material.GHAST_TEAR), SlimefunItems.BLANK_RUNE, StackResolver.of(Material.GHAST_TEAR), StackResolver.of(Material.FEATHER), SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.FEATHER)},
        new SlimefunItemStack(SlimefunItems.AIR_RUNE, 4))
        .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.EARTH_RUNE,
        new ItemStack[] {StackResolver.of(Material.DIRT), SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.STONE), StackResolver.of(Material.OBSIDIAN), SlimefunItems.BLANK_RUNE, StackResolver.of(Material.OBSIDIAN), StackResolver.of(Material.STONE), SlimefunItems.MAGIC_LUMP_1, StackResolver.of(Material.DIRT)},
        new SlimefunItemStack(SlimefunItems.EARTH_RUNE, 4))
        .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.FIRE_RUNE,
        new ItemStack[] {StackResolver.of(Material.FIRE_CHARGE), SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.FIRE_CHARGE), StackResolver.of(Material.BLAZE_POWDER), SlimefunItems.EARTH_RUNE, StackResolver.of(Material.FLINT_AND_STEEL), StackResolver.of(Material.FIRE_CHARGE), SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.FIRE_CHARGE)},
        new SlimefunItemStack(SlimefunItems.FIRE_RUNE, 4))
        .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.WATER_RUNE,
        new ItemStack[] {StackResolver.of(Material.SALMON), SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.WATER_BUCKET), StackResolver.of(Material.SAND), SlimefunItems.BLANK_RUNE, StackResolver.of(Material.SAND), StackResolver.of(Material.WATER_BUCKET), SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.COD)},
        new SlimefunItemStack(SlimefunItems.WATER_RUNE, 4))
        .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.ENDER_RUNE,
        new ItemStack[] {StackResolver.of(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_3, StackResolver.of(Material.ENDER_PEARL), StackResolver.of(Material.ENDER_EYE), SlimefunItems.BLANK_RUNE, StackResolver.of(Material.ENDER_EYE), StackResolver.of(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_3, StackResolver.of(Material.ENDER_PEARL)},
        new SlimefunItemStack(SlimefunItems.ENDER_RUNE, 6))
        .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.LIGHTNING_RUNE,
        new ItemStack[] {StackResolver.of(Material.IRON_INGOT), SlimefunItems.MAGIC_LUMP_3, StackResolver.of(Material.IRON_INGOT), SlimefunItems.AIR_RUNE, StackResolver.of(Material.PHANTOM_MEMBRANE), SlimefunItems.WATER_RUNE, StackResolver.of(Material.IRON_INGOT), SlimefunItems.MAGIC_LUMP_3, StackResolver.of(Material.IRON_INGOT)},
        new SlimefunItemStack(SlimefunItems.LIGHTNING_RUNE, 4))
        .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.RAINBOW_RUNE,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), SlimefunItems.MAGIC_LUMP_3, StackResolver.of(Material.CYAN_DYE), StackResolver.of(Material.WHITE_WOOL), SlimefunItems.ENDER_RUNE, StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.YELLOW_DYE), SlimefunItems.ENDER_LUMP_3, StackResolver.of(Material.MAGENTA_DYE)})
        .register(plugin);

        new SoulboundRune(itemGroups.magicalResources, SlimefunItems.SOULBOUND_RUNE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_RUNE, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.MAGIC_LUMP_3})
        .register(plugin);

        new EnchantmentRune(itemGroups.magicalResources, SlimefunItems.ENCHANTMENT_RUNE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, SlimefunItems.MAGICAL_GLASS, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.MAGICAL_GLASS, SlimefunItems.LIGHTNING_RUNE, SlimefunItems.MAGICAL_GLASS, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.MAGICAL_GLASS, SlimefunItems.MAGIC_LUMP_3})
        .register(plugin);

        new InfernalBonemeal(itemGroups.magicalGadgets, SlimefunItems.INFERNAL_BONEMEAL, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.NETHER_WART), SlimefunItems.EARTH_RUNE, StackResolver.of(Material.NETHER_WART), SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.BONE_MEAL), SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.NETHER_WART), StackResolver.of(Material.BLAZE_POWDER), StackResolver.of(Material.NETHER_WART)},
        new SlimefunItemStack(SlimefunItems.INFERNAL_BONEMEAL, 8))
        .register(plugin);

        new SlimefunItem(itemGroups.magicalGadgets, SlimefunItems.ELYTRA_SCALE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.ENDER_LUMP_3, SlimefunItems.AIR_RUNE, SlimefunItems.ENDER_LUMP_3, StackResolver.of(Material.PHANTOM_MEMBRANE), StackResolver.of(Material.FEATHER), StackResolver.of(Material.PHANTOM_MEMBRANE), SlimefunItems.ENDER_LUMP_3, SlimefunItems.AIR_RUNE, SlimefunItems.ENDER_LUMP_3})
        .register(plugin);

        new VanillaItem(itemGroups.magicalGadgets, StackResolver.of(Material.ELYTRA), "ELYTRA", RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.ELYTRA_SCALE, SlimefunItems.AIR_RUNE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.AIR_RUNE, StackResolver.of(Material.LEATHER_CHESTPLATE), SlimefunItems.AIR_RUNE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.AIR_RUNE, SlimefunItems.ELYTRA_SCALE})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalGadgets, SlimefunItems.INFUSED_ELYTRA, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.FLASK_OF_KNOWLEDGE, StackResolver.of(Material.ELYTRA), SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.FLASK_OF_KNOWLEDGE})
        .register(plugin);

        new SoulboundItem(itemGroups.magicalGadgets, SlimefunItems.SOULBOUND_ELYTRA, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA_SCALE, StackResolver.of(Material.ELYTRA), SlimefunItems.ELYTRA_SCALE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.FLASK_OF_KNOWLEDGE})
        .register(plugin);

        new VanillaItem(itemGroups.magicalGadgets, StackResolver.of(Material.TRIDENT), "TRIDENT", RecipeType.ANCIENT_ALTAR,
        new ItemStack[] { StackResolver.of(Material.NAUTILUS_SHELL), SlimefunItems.REINFORCED_ALLOY_INGOT, StackResolver.of(Material.NAUTILUS_SHELL), SlimefunItems.STAFF_WATER, StackResolver.of(Material.DIAMOND_SWORD), SlimefunItems.STAFF_WATER, SlimefunItems.MAGIC_LUMP_3, StackResolver.of(Material.NETHER_STAR), SlimefunItems.MAGIC_LUMP_3})
        .register(plugin);

        new VanillaItem(itemGroups.magicalGadgets, StackResolver.of(Material.TOTEM_OF_UNDYING), "TOTEM_OF_UNDYING", RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.ESSENCE_OF_AFTERLIFE, StackResolver.of(Material.EMERALD_BLOCK), SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.COMMON_TALISMAN, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, StackResolver.of(Material.EMERALD_BLOCK), SlimefunItems.ESSENCE_OF_AFTERLIFE})
        .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_WOOL, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.WHITE_WOOL), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.WHITE_WOOL)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_WOOL, 8), new RainbowTickHandler(ColoredMaterial.WOOL))
        .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_GLASS, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.WHITE_STAINED_GLASS), StackResolver.of(Material.WHITE_STAINED_GLASS), StackResolver.of(Material.WHITE_STAINED_GLASS), StackResolver.of(Material.WHITE_STAINED_GLASS), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_STAINED_GLASS), StackResolver.of(Material.WHITE_STAINED_GLASS), StackResolver.of(Material.WHITE_STAINED_GLASS), StackResolver.of(Material.WHITE_STAINED_GLASS)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS, 8), new RainbowTickHandler(ColoredMaterial.STAINED_GLASS))
        .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_GLASS_PANE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), StackResolver.of(Material.WHITE_STAINED_GLASS_PANE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_PANE, 8), new RainbowTickHandler(ColoredMaterial.STAINED_GLASS_PANE))
        .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_CLAY, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.WHITE_TERRACOTTA), StackResolver.of(Material.WHITE_TERRACOTTA), StackResolver.of(Material.WHITE_TERRACOTTA), StackResolver.of(Material.WHITE_TERRACOTTA), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_TERRACOTTA), StackResolver.of(Material.WHITE_TERRACOTTA), StackResolver.of(Material.WHITE_TERRACOTTA), StackResolver.of(Material.WHITE_TERRACOTTA)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_CLAY, 8), new RainbowTickHandler(ColoredMaterial.TERRACOTTA))
        .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_CONCRETE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.WHITE_CONCRETE), StackResolver.of(Material.WHITE_CONCRETE), StackResolver.of(Material.WHITE_CONCRETE), StackResolver.of(Material.WHITE_CONCRETE), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_CONCRETE), StackResolver.of(Material.WHITE_CONCRETE), StackResolver.of(Material.WHITE_CONCRETE), StackResolver.of(Material.WHITE_CONCRETE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_CONCRETE, 8), new RainbowTickHandler(ColoredMaterial.CONCRETE))
        .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_GLAZED_TERRACOTTA, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLAZED_TERRACOTTA, 8), new RainbowTickHandler(ColoredMaterial.GLAZED_TERRACOTTA))
        .register(plugin);

        // Christmas

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_WOOL_XMAS, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.GREEN_DYE), StackResolver.of(Material.WHITE_WOOL), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_WOOL_XMAS, 2), new RainbowTickHandler(Material.RED_WOOL, Material.GREEN_WOOL))
        .register(plugin);

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_GLASS_XMAS, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.GREEN_DYE), StackResolver.of(Material.WHITE_STAINED_GLASS), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_STAINED_GLASS), StackResolver.of(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_XMAS, 2), new RainbowTickHandler(Material.RED_STAINED_GLASS, Material.GREEN_STAINED_GLASS))
        .register(plugin);

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_GLASS_PANE_XMAS, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.GREEN_DYE), StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), StackResolver.of(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_PANE_XMAS, 2), new RainbowTickHandler(Material.RED_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE))
        .register(plugin);

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_CLAY_XMAS, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.GREEN_DYE), StackResolver.of(Material.WHITE_TERRACOTTA), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_TERRACOTTA), StackResolver.of(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_CLAY_XMAS, 2), new RainbowTickHandler(Material.RED_TERRACOTTA, Material.GREEN_TERRACOTTA))
        .register(plugin);

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_CONCRETE_XMAS, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.GREEN_DYE), StackResolver.of(Material.WHITE_CONCRETE), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_CONCRETE), StackResolver.of(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_CONCRETE_XMAS, 2), new RainbowTickHandler(Material.RED_CONCRETE, Material.GREEN_CONCRETE))
        .register(plugin);

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_XMAS, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.GREEN_DYE), StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), StackResolver.of(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_XMAS, 2), new RainbowTickHandler(Material.RED_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA))
        .register(plugin);

        // Valentines Day

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_WOOL_VALENTINE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.WHITE_WOOL), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_WOOL_VALENTINE, 2), new RainbowTickHandler(Material.MAGENTA_WOOL, Material.PINK_WOOL))
        .register(plugin);

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_GLASS_VALENTINE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.WHITE_STAINED_GLASS), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_STAINED_GLASS), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_VALENTINE, 2), new RainbowTickHandler(Material.MAGENTA_STAINED_GLASS, Material.PINK_STAINED_GLASS))
        .register(plugin);

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_GLASS_PANE_VALENTINE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_PANE_VALENTINE, 2), new RainbowTickHandler(Material.MAGENTA_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE))
        .register(plugin);

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_CLAY_VALENTINE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.WHITE_TERRACOTTA), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_TERRACOTTA), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_CLAY_VALENTINE, 2), new RainbowTickHandler(Material.MAGENTA_TERRACOTTA, Material.PINK_TERRACOTTA))
        .register(plugin);

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_CONCRETE_VALENTINE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.WHITE_CONCRETE), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_CONCRETE), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_CONCRETE_VALENTINE, 2), new RainbowTickHandler(Material.MAGENTA_CONCRETE, Material.PINK_CONCRETE))
        .register(plugin);

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_VALENTINE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.RED_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), StackResolver.of(Material.PINK_DYE), StackResolver.of(Material.POPPY), StackResolver.of(Material.RED_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_VALENTINE, 2), new RainbowTickHandler(Material.MAGENTA_GLAZED_TERRACOTTA, Material.PINK_GLAZED_TERRACOTTA))
        .register(plugin);

        // Halloween

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_WOOL_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.ORANGE_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.WHITE_WOOL), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_WOOL), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.ORANGE_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_WOOL_HALLOWEEN, 2), new RainbowTickHandler(Material.ORANGE_WOOL, Material.BLACK_WOOL))
        .register(plugin);

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_GLASS_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.ORANGE_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.WHITE_STAINED_GLASS), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_STAINED_GLASS), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.ORANGE_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_HALLOWEEN, 2), new RainbowTickHandler(Material.ORANGE_STAINED_GLASS, Material.BLACK_STAINED_GLASS))
        .register(plugin);

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_GLASS_PANE_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.ORANGE_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_STAINED_GLASS_PANE), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.ORANGE_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_PANE_HALLOWEEN, 2), new RainbowTickHandler(Material.ORANGE_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS_PANE))
        .register(plugin);

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_CLAY_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.ORANGE_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.WHITE_TERRACOTTA), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_TERRACOTTA), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.ORANGE_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_CLAY_HALLOWEEN, 2), new RainbowTickHandler(Material.ORANGE_TERRACOTTA, Material.BLACK_TERRACOTTA))
        .register(plugin);

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_CONCRETE_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.ORANGE_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.WHITE_CONCRETE), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_CONCRETE), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.ORANGE_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_CONCRETE_HALLOWEEN, 2), new RainbowTickHandler(Material.ORANGE_CONCRETE, Material.BLACK_CONCRETE))
        .register(plugin);

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.ORANGE_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.WHITE_GLAZED_TERRACOTTA), StackResolver.of(Material.BLACK_DYE), StackResolver.of(Material.PUMPKIN), StackResolver.of(Material.ORANGE_DYE)},
        new SlimefunItemStack(SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN, 2), new RainbowTickHandler(Material.ORANGE_GLAZED_TERRACOTTA, Material.BLACK_GLAZED_TERRACOTTA))
        .register(plugin);

        new WitherProofBlock(itemGroups.technicalComponents, SlimefunItems.WITHER_PROOF_GLASS, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.LEAD_INGOT, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.HARDENED_GLASS, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.LEAD_INGOT, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.LEAD_INGOT},
        new SlimefunItemStack(SlimefunItems.WITHER_PROOF_GLASS, 4))
        .register(plugin);

        new GEOScanner(itemGroups.gps, SlimefunItems.GPS_GEO_SCANNER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, null, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRO_MAGNET})
        .register(plugin);

        new PortableGEOScanner(itemGroups.gps, SlimefunItems.PORTABLE_GEO_SCANNER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.ELECTRO_MAGNET, StackResolver.of(Material.COMPASS), SlimefunItems.ELECTRO_MAGNET, SlimefunItems.STEEL_INGOT, SlimefunItems.GPS_MARKER_TOOL, SlimefunItems.STEEL_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.SOLDER_INGOT})
        .register(plugin);

        new OilPump(itemGroups.gps, SlimefunItems.OIL_PUMP, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT, null, StackResolver.of(Material.BUCKET), null})
        .setCapacity(256)
        .setEnergyConsumption(14)
        .setProcessingSpeed(1)
        .register(plugin);

        new GEOMiner(itemGroups.gps, SlimefunItems.GEO_MINER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.DIAMOND_PICKAXE), SlimefunItems.MEDIUM_CAPACITOR, StackResolver.of(Material.DIAMOND_PICKAXE), SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.OIL_PUMP, SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .setCapacity(512)
        .setEnergyConsumption(24)
        .setProcessingSpeed(1)
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.OIL_BUCKET, new RecipeType(new NamespacedKey(plugin, "oil_pump"), SlimefunItems.OIL_PUMP),
        new ItemStack[] {null, null, null, null, StackResolver.of(Material.BUCKET), null, null, null, null})
        .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.FUEL_BUCKET, RecipeType.REFINERY,
        new ItemStack[] {null, null, null, null, SlimefunItems.OIL_BUCKET, null, null, null, null})
        .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.MODERATE, SlimefunItems.NETHER_ICE, RecipeType.GEO_MINER,
        new ItemStack[] {null, null, null, null, null, null, null, null, null})
        .register(plugin);

        new Refinery(itemGroups.electricity, SlimefunItems.REFINERY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.HARDENED_GLASS, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HARDENED_GLASS, StackResolver.of(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.PISTON)})
        .setCapacity(256)
        .setEnergyConsumption(16)
        .setProcessingSpeed(1)
        .register(plugin);

        new LavaGenerator(itemGroups.electricity, SlimefunItems.LAVA_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.GOLD_16K, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL})
        .setCapacity(512)
        .setEnergyProduction(10)
        .register(plugin);

        new LavaGenerator(itemGroups.electricity, SlimefunItems.LAVA_GENERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.LAVA_GENERATOR, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.HEATING_COIL, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.HEATING_COIL})
        .setCapacity(1024)
        .setEnergyProduction(20)
        .register(plugin);

        new CombustionGenerator(itemGroups.electricity, SlimefunItems.COMBUSTION_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.STEEL_INGOT, SlimefunItems.HEATING_COIL})
        .setCapacity(256)
        .setEnergyProduction(12)
        .register(plugin);

        new TeleporterPylon(itemGroups.gps, SlimefunItems.GPS_TELEPORTER_PYLON, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.ZINC_INGOT, StackResolver.of(Material.GLASS), SlimefunItems.ZINC_INGOT, StackResolver.of(Material.GLASS), SlimefunItems.HEATING_COIL, StackResolver.of(Material.GLASS), SlimefunItems.ZINC_INGOT, StackResolver.of(Material.GLASS), SlimefunItems.ZINC_INGOT},
        new SlimefunItemStack(SlimefunItems.GPS_TELEPORTER_PYLON, 8))
        .register(plugin);

        new Teleporter(itemGroups.gps, SlimefunItems.GPS_TELEPORTATION_MATRIX, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GPS_TELEPORTER_PYLON, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.GPS_TELEPORTER_PYLON, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.GPS_CONTROL_PANEL, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.GPS_TELEPORTER_PYLON, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.GPS_TELEPORTER_PYLON})
        .register(plugin);

        new PortableTeleporter(itemGroups.gps, SlimefunItems.PORTABLE_TELEPORTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.ELECTRO_MAGNET, SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.REINFORCED_PLATE, SlimefunItems.GPS_TELEPORTATION_MATRIX, SlimefunItems.REINFORCED_PLATE, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.BLISTERING_INGOT_3})
        .register(plugin);

        new SharedActivationPlate(itemGroups.gps, SlimefunItems.GPS_ACTIVATION_DEVICE_SHARED, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.STONE_PRESSURE_PLATE), null, StackResolver.of(Material.REDSTONE), SlimefunItems.GPS_TRANSMITTER, StackResolver.of(Material.REDSTONE), SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT})
        .register(plugin);

        new PersonalActivationPlate(itemGroups.gps, SlimefunItems.GPS_ACTIVATION_DEVICE_PERSONAL, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.LEAD_INGOT, null, SlimefunItems.COBALT_INGOT, SlimefunItems.GPS_ACTIVATION_DEVICE_SHARED, SlimefunItems.COBALT_INGOT, null, SlimefunItems.LEAD_INGOT, null})
        .register(plugin);

        new InfusedHopper(itemGroups.magicalGadgets, SlimefunItems.INFUSED_HOPPER, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {StackResolver.of(Material.OBSIDIAN), SlimefunItems.EARTH_RUNE, StackResolver.of(Material.HOPPER), SlimefunItems.ENDER_RUNE, SlimefunItems.INFUSED_MAGNET, SlimefunItems.ENDER_RUNE, StackResolver.of(Material.HOPPER), SlimefunItems.EARTH_RUNE, StackResolver.of(Material.OBSIDIAN)})
        .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.HIGH, SlimefunItems.BLISTERING_INGOT, RecipeType.HEATED_PRESSURE_CHAMBER,
        new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.URANIUM, null, null, null, null, null, null, null})
        .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.VERY_HIGH, SlimefunItems.BLISTERING_INGOT_2, RecipeType.HEATED_PRESSURE_CHAMBER,
        new ItemStack[] {SlimefunItems.BLISTERING_INGOT, SlimefunItems.CARBONADO, null, null, null, null, null, null, null})
        .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.VERY_HIGH, SlimefunItems.BLISTERING_INGOT_3, RecipeType.HEATED_PRESSURE_CHAMBER,
        new ItemStack[] {SlimefunItems.BLISTERING_INGOT_2, StackResolver.of(Material.NETHER_STAR), null, null, null, null, null, null, null})
        .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.VERY_HIGH, SlimefunItems.ENRICHED_NETHER_ICE, RecipeType.HEATED_PRESSURE_CHAMBER,
        new ItemStack[] {SlimefunItems.NETHER_ICE, SlimefunItems.PLUTONIUM, null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.ENRICHED_NETHER_ICE, 4))
        .register(plugin);

        new ElevatorPlate(itemGroups.gps, SlimefunItems.ELEVATOR_PLATE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.STONE_PRESSURE_PLATE), null, StackResolver.of(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.PISTON), SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT},
        new SlimefunItemStack(SlimefunItems.ELEVATOR_PLATE, 2))
        .register(plugin);

        new FoodFabricator(itemGroups.electricity, SlimefunItems.FOOD_FABRICATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.BILLON_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.TIN_CAN, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.TIN_CAN, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .setCapacity(256)
        .setEnergyConsumption(7)
        .setProcessingSpeed(1)
        .register(plugin);

        new FoodFabricator(itemGroups.electricity, SlimefunItems.FOOD_FABRICATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.FOOD_FABRICATOR, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.ELECTRO_MAGNET, null})
        .setCapacity(512)
        .setEnergyConsumption(24)
        .setProcessingSpeed(6)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.WHEAT_ORGANIC_FOOD, Material.WHEAT)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.CARROT_ORGANIC_FOOD, Material.CARROT)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.POTATO_ORGANIC_FOOD, Material.POTATO)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.SEEDS_ORGANIC_FOOD, Material.WHEAT_SEEDS)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.BEETROOT_ORGANIC_FOOD, Material.BEETROOT)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.MELON_ORGANIC_FOOD, Material.MELON_SLICE)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.APPLE_ORGANIC_FOOD, Material.APPLE)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.SWEET_BERRIES_ORGANIC_FOOD, Material.SWEET_BERRIES)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.KELP_ORGANIC_FOOD, Material.DRIED_KELP)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.COCOA_ORGANIC_FOOD, Material.COCOA_BEANS)
        .register(plugin);

        new OrganicFood(itemGroups.misc, SlimefunItems.SEAGRASS_ORGANIC_FOOD, Material.SEAGRASS)
        .register(plugin);

        new AutoBreeder(itemGroups.electricity, SlimefunItems.AUTO_BREEDER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.GOLD_18K, SlimefunItems.TIN_CAN, SlimefunItems.GOLD_18K, SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.HAY_BLOCK), SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.FOOD_FABRICATOR, SlimefunItems.LEAD_INGOT})
        .register(plugin);

        new AnimalGrowthAccelerator(itemGroups.electricity, SlimefunItems.ANIMAL_GROWTH_ACCELERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.WHEAT_ORGANIC_FOOD, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.AUTO_BREEDER, SlimefunItems.REINFORCED_ALLOY_INGOT})
        .register(plugin);

        new TreeGrowthAccelerator(itemGroups.electricity, SlimefunItems.TREE_GROWTH_ACCELERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.CARBONADO, null, SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.DIAMOND_AXE), SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.MAGNESIUM_SALT, SlimefunItems.BIG_CAPACITOR, SlimefunItems.MAGNESIUM_SALT})
        .register(plugin);

        new ExpCollector(itemGroups.electricity, SlimefunItems.EXP_COLLECTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.AUTO_ENCHANTER, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRONZE_INGOT})
        .register(plugin);

        new FoodComposter(itemGroups.electricity, SlimefunItems.FOOD_COMPOSTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.FOOD_FABRICATOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.TIN_CAN, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.TIN_CAN, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .setCapacity(256)
        .setEnergyConsumption(8)
        .setProcessingSpeed(1)
        .register(plugin);

        new FoodComposter(itemGroups.electricity, SlimefunItems.FOOD_COMPOSTER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.FOOD_COMPOSTER, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.ELECTRO_MAGNET, null})
        .setCapacity(512)
        .setEnergyConsumption(26)
        .setProcessingSpeed(10)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.WHEAT_FERTILIZER, SlimefunItems.WHEAT_ORGANIC_FOOD)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.CARROT_FERTILIZER, SlimefunItems.CARROT_ORGANIC_FOOD)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.POTATO_FERTILIZER, SlimefunItems.POTATO_ORGANIC_FOOD)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.SEEDS_FERTILIZER, SlimefunItems.SEEDS_ORGANIC_FOOD)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.BEETROOT_FERTILIZER, SlimefunItems.BEETROOT_ORGANIC_FOOD)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.MELON_FERTILIZER, SlimefunItems.MELON_ORGANIC_FOOD)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.APPLE_FERTILIZER, SlimefunItems.APPLE_ORGANIC_FOOD)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.SWEET_BERRIES_FERTILIZER, SlimefunItems.SWEET_BERRIES_ORGANIC_FOOD)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.KELP_FERTILIZER, SlimefunItems.KELP_ORGANIC_FOOD)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.COCOA_FERTILIZER, SlimefunItems.COCOA_ORGANIC_FOOD)
        .register(plugin);

        new OrganicFertilizer(itemGroups.misc, SlimefunItems.SEAGRASS_FERTILIZER, SlimefunItems.SEAGRASS_ORGANIC_FOOD)
        .register(plugin);

        new CropGrowthAccelerator(itemGroups.electricity, SlimefunItems.CROP_GROWTH_ACCELERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.PROGRAMMABLE_ANDROID_FARMER, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ANIMAL_GROWTH_ACCELERATOR, SlimefunItems.ELECTRO_MAGNET}) {

            @Override
            public int getEnergyConsumption() {
                return 25;
            }

            @Override
            public int getRadius() {
                return 3;
            }

            @Override
            public int getSpeed() {
                return 3;
            }

        }.register(plugin);

        new CropGrowthAccelerator(itemGroups.electricity, SlimefunItems.CROP_GROWTH_ACCELERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CROP_GROWTH_ACCELERATOR, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.ELECTRO_MAGNET}) {

            @Override
            public int getEnergyConsumption() {
                return 30;
            }

            @Override
            public int getRadius() {
                return 4;
            }

            @Override
            public int getSpeed() {
                return 4;
            }

        }.register(plugin);

        new Freezer(itemGroups.electricity, SlimefunItems.FREEZER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.SILVER_INGOT, null, SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.PACKED_ICE), SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.COOLING_UNIT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.COOLING_UNIT})
        .setCapacity(256)
        .setEnergyConsumption(9)
        .setProcessingSpeed(1)
        .register(plugin);

        new Freezer(itemGroups.electricity, SlimefunItems.FREEZER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.SILVER_INGOT, null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.FREEZER, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.COOLING_UNIT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.COOLING_UNIT})
        .setCapacity(256)
        .setEnergyConsumption(15)
        .setProcessingSpeed(2)
        .register(plugin);

        new Freezer(itemGroups.electricity, SlimefunItems.FREEZER_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.FREEZER_2, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.COOLING_UNIT, SlimefunItems.COOLING_UNIT, SlimefunItems.COOLING_UNIT})
        .setCapacity(256)
        .setEnergyConsumption(21)
        .setProcessingSpeed(3)
        .register(plugin);

        new CoolantCell(itemGroups.technicalComponents, SlimefunItems.REACTOR_COOLANT_CELL, RecipeType.FREEZER,
        new ItemStack[] {StackResolver.of(Material.BLUE_ICE), null, null, null, null, null, null, null, null})
        .register(plugin);

        new CoolantCell(itemGroups.technicalComponents, SlimefunItems.NETHER_ICE_COOLANT_CELL, RecipeType.HEATED_PRESSURE_CHAMBER,
        new ItemStack[] {SlimefunItems.ENRICHED_NETHER_ICE, null, null, null, null, null, null, null, null},
        new SlimefunItemStack(SlimefunItems.NETHER_ICE_COOLANT_CELL, 8))
        .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.HIGH, SlimefunItems.NEPTUNIUM, RecipeType.NUCLEAR_REACTOR,
        new ItemStack[] {SlimefunItems.URANIUM, null, null, null, null, null, null, null, null})
        .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.VERY_HIGH, SlimefunItems.PLUTONIUM, RecipeType.NUCLEAR_REACTOR,
        new ItemStack[] {SlimefunItems.NEPTUNIUM, null, null, null, null, null, null, null, null})
        .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.VERY_HIGH, SlimefunItems.BOOSTED_URANIUM, RecipeType.HEATED_PRESSURE_CHAMBER,
        new ItemStack[] {SlimefunItems.PLUTONIUM, SlimefunItems.URANIUM, null, null, null, null, null, null, null})
        .register(plugin);

        new NuclearReactor(itemGroups.electricity, SlimefunItems.NUCLEAR_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.REINFORCED_PLATE, SlimefunItems.COOLING_UNIT, SlimefunItems.REINFORCED_PLATE, SlimefunItems.LEAD_INGOT, SlimefunItems.REINFORCED_PLATE, SlimefunItems.LEAD_INGOT}){

            @Override
            public int getEnergyProduction() {
                return 250;
            }

            @Override
            public int getCapacity() {
                return 16384;
            }

        }.register(plugin);

        new NetherStarReactor(itemGroups.electricity, SlimefunItems.NETHER_STAR_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.BOOSTED_URANIUM, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BOOSTED_URANIUM, SlimefunItems.REINFORCED_PLATE, StackResolver.of(Material.NETHER_STAR), SlimefunItems.REINFORCED_PLATE, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.REINFORCED_PLATE, SlimefunItems.CORINTHIAN_BRONZE_INGOT}){

            @Override
            public int getEnergyProduction() {
                return 512;
            }

            @Override
            public int getCapacity() {
                return 32768;
            }

        }.register(plugin);

        new UnplaceableBlock(itemGroups.cargo, SlimefunItems.CARGO_MOTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.HARDENED_GLASS, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.HARDENED_GLASS, SlimefunItems.SILVER_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.SILVER_INGOT, SlimefunItems.HARDENED_GLASS, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.HARDENED_GLASS},
        new SlimefunItemStack(SlimefunItems.CARGO_MOTOR, 4))
        .register(plugin);

        new CargoManager(itemGroups.cargo, SlimefunItems.CARGO_MANAGER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.HOLOGRAM_PROJECTOR, null, SlimefunItems.REINFORCED_PLATE, SlimefunItems.CARGO_MOTOR, SlimefunItems.REINFORCED_PLATE, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.ALUMINUM_BRONZE_INGOT})
        .register(plugin);

        new CargoConnectorNode(itemGroups.cargo, SlimefunItems.CARGO_CONNECTOR_NODE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT},
        new SlimefunItemStack(SlimefunItems.CARGO_CONNECTOR_NODE, 4))
        .register(plugin);

        new CargoInputNode(itemGroups.cargo, SlimefunItems.CARGO_INPUT_NODE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.HOPPER), null, SlimefunItems.BILLON_INGOT, SlimefunItems.CARGO_CONNECTOR_NODE, SlimefunItems.BILLON_INGOT, null, StackResolver.of(Material.HOPPER), null},
        new SlimefunItemStack(SlimefunItems.CARGO_INPUT_NODE, 2))
        .register(plugin);

        new CargoOutputNode(itemGroups.cargo, SlimefunItems.CARGO_OUTPUT_NODE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.HOPPER), null, SlimefunItems.BRASS_INGOT, SlimefunItems.CARGO_CONNECTOR_NODE, SlimefunItems.BRASS_INGOT, null, StackResolver.of(Material.HOPPER), null},
        new SlimefunItemStack(SlimefunItems.CARGO_OUTPUT_NODE, 2))
        .register(plugin);

        new AdvancedCargoOutputNode(itemGroups.cargo, SlimefunItems.CARGO_OUTPUT_NODE_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.CARGO_MOTOR, null, SlimefunItems.COBALT_INGOT, SlimefunItems.CARGO_OUTPUT_NODE, SlimefunItems.COBALT_INGOT, null, SlimefunItems.CARGO_MOTOR, null})
        .register(plugin);

        new ReactorAccessPort(itemGroups.cargo, SlimefunItems.REACTOR_ACCESS_PORT, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.LEAD_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.LEAD_INGOT, null, SlimefunItems.ELECTRIC_MOTOR, null})
        .register(plugin);

        new FluidPump(itemGroups.electricity, SlimefunItems.FLUID_PUMP, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.MEDIUM_CAPACITOR, null, StackResolver.of(Material.BUCKET), SlimefunItems.CARGO_MOTOR, StackResolver.of(Material.BUCKET), null, SlimefunItems.OIL_PUMP, null})
        .register(plugin);

        new TrashCan(itemGroups.cargo, SlimefunItems.TRASH_CAN, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.PORTABLE_DUSTBIN, null, SlimefunItems.LEAD_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.ALUMINUM_INGOT})
        .register(plugin);

        new CarbonPress(itemGroups.electricity, SlimefunItems.CARBON_PRESS, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CARBON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBON, SlimefunItems.CARBON, SlimefunItems.HEATED_PRESSURE_CHAMBER, SlimefunItems.CARBON, SlimefunItems.HEATING_COIL, SlimefunItems.CARBONADO, SlimefunItems.HEATING_COIL})
        .setCapacity(256)
        .setEnergyConsumption(10)
        .setProcessingSpeed(1)
        .register(plugin);

        new CarbonPress(itemGroups.electricity, SlimefunItems.CARBON_PRESS_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.CARBON, SlimefunItems.CARBON_PRESS, SlimefunItems.CARBON, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.HEATING_COIL})
        .setCapacity(512)
        .setEnergyConsumption(25)
        .setProcessingSpeed(3)
        .register(plugin);

        new CarbonPress(itemGroups.electricity, SlimefunItems.CARBON_PRESS_3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.CARBON_PRESS_2, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.HEATING_COIL})
        .setCapacity(512)
        .setEnergyConsumption(90)
        .setProcessingSpeed(15)
        .register(plugin);

        new ElectricSmeltery(itemGroups.electricity, SlimefunItems.ELECTRIC_SMELTERY, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.NETHER_BRICKS), SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.NETHER_BRICKS), SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_INGOT_FACTORY, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.GILDED_IRON})
        .setCapacity(512)
        .setEnergyConsumption(10)
        .setProcessingSpeed(1)
        .register(plugin);

        new ElectricSmeltery(itemGroups.electricity, SlimefunItems.ELECTRIC_SMELTERY_2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_SMELTERY, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.GILDED_IRON})
        .setCapacity(1024)
        .setEnergyConsumption(20)
        .setProcessingSpeed(3)
        .register(plugin);

        new IronGolemAssembler(itemGroups.electricity, SlimefunItems.IRON_GOLEM_ASSEMBLER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.BLISTERING_INGOT_3, StackResolver.of(Material.IRON_BLOCK), SlimefunItems.ANDROID_MEMORY_CORE, StackResolver.of(Material.IRON_BLOCK), SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.CARBONADO_EDGED_CAPACITOR})
        .register(plugin);

        new WitherAssembler(itemGroups.electricity, SlimefunItems.WITHER_ASSEMBLER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3, StackResolver.of(Material.NETHER_STAR), SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.CARBONADO_EDGED_CAPACITOR})
        .register(plugin);

        new TapeMeasure(itemGroups.usefulItems, SlimefunItems.TAPE_MEASURE, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.SILICON, StackResolver.of(Material.YELLOW_DYE), SlimefunItems.SILICON, StackResolver.of(Material.YELLOW_DYE), StackResolver.of(Material.STRING), StackResolver.of(Material.YELLOW_DYE), SlimefunItems.GILDED_IRON, StackResolver.of(Material.YELLOW_DYE), SlimefunItems.SILICON})
        .register(plugin);

        MinecraftVersion minecraftVersion = Slimefun.getMinecraftVersion();

        new SlimefunItem(itemGroups.magicalArmor, SlimefunItems.BEE_HELMET, RecipeType.ARMOR_FORGE,
        new ItemStack[] {SlimefunItems.GOLD_8K, StackResolver.of(Material.HONEY_BLOCK), SlimefunItems.GOLD_8K, StackResolver.of(Material.HONEYCOMB_BLOCK), null, StackResolver.of(Material.HONEYCOMB_BLOCK), null, null, null})
        .register(plugin);

        new BeeWings(itemGroups.magicalArmor, SlimefunItems.BEE_WINGS, RecipeType.ARMOR_FORGE,
        new ItemStack[] {SlimefunItems.GOLD_8K, null, SlimefunItems.GOLD_8K, StackResolver.of(Material.HONEYCOMB_BLOCK), StackResolver.of(Material.ELYTRA), StackResolver.of(Material.HONEYCOMB_BLOCK), StackResolver.of(Material.HONEY_BLOCK), SlimefunItems.GOLD_8K, StackResolver.of(Material.HONEY_BLOCK)})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalArmor, SlimefunItems.BEE_LEGGINGS, RecipeType.ARMOR_FORGE,
        new ItemStack[] {SlimefunItems.GOLD_8K, StackResolver.of(Material.HONEY_BLOCK), SlimefunItems.GOLD_8K, StackResolver.of(Material.HONEYCOMB_BLOCK), null, StackResolver.of(Material.HONEYCOMB_BLOCK), StackResolver.of(Material.HONEYCOMB_BLOCK), null, StackResolver.of(Material.HONEYCOMB_BLOCK)})
        .register(plugin);

        new LongFallBoots(itemGroups.magicalArmor, SlimefunItems.BEE_BOOTS, RecipeType.ARMOR_FORGE,
        new ItemStack[] {null, null, null, SlimefunItems.GOLD_8K, null, SlimefunItems.GOLD_8K, StackResolver.of(Material.HONEY_BLOCK), null, StackResolver.of(Material.HONEY_BLOCK)},
        new PotionEffect[] {new PotionEffect(VersionedPotionEffectType.JUMP_BOOST, 300, 2)},
        SoundEffect.BEE_BOOTS_FALL_SOUND)
        .register(plugin);

        new VillagerRune(itemGroups.magicalResources, SlimefunItems.VILLAGER_RUNE, RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, SlimefunItems.MAGICAL_GLASS, StackResolver.of(Material.CRYING_OBSIDIAN), SlimefunItems.STRANGE_NETHER_GOO, SlimefunItems.FIRE_RUNE, SlimefunItems.STRANGE_NETHER_GOO, StackResolver.of(Material.CRYING_OBSIDIAN), SlimefunItems.MAGICAL_GLASS, SlimefunItems.MAGIC_LUMP_3},
        new SlimefunItemStack(SlimefunItems.VILLAGER_RUNE, 3))
        .register(plugin);

        new StrangeNetherGoo(itemGroups.magicalResources, SlimefunItems.STRANGE_NETHER_GOO, RecipeType.BARTER_DROP,
        new ItemStack[] {null, null, null, null, new CustomItemStack(HeadTexture.PIGLIN_HEAD.getAsItemStack(), "&fPiglin"), null, null, null, null})
        .register(plugin);

        if (minecraftVersion.isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            new Juice(itemGroups.food, SlimefunItems.GLOW_BERRY_JUICE, RecipeType.JUICER,
            new ItemStack[] {StackResolver.of(Material.GLOW_BERRIES), null, null, null, null, null, null, null, null})
            .register(plugin);
        }

        new ElytraCap(itemGroups.magicalArmor, SlimefunItems.ELYTRA_CAP, RecipeType.ARMOR_FORGE,
        new ItemStack[] {StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.SLIME_BALL), SlimefunItems.ELYTRA_SCALE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.ELYTRA_SCALE, StackResolver.of(Material.SLIME_BALL), StackResolver.of(Material.LEATHER_HELMET), StackResolver.of(Material.SLIME_BALL)})
        .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.RAINBOW_LEATHER, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] { StackResolver.of(Material.EMERALD), StackResolver.of(Material.LEATHER), SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.RABBIT_HIDE), SlimefunItems.RAINBOW_RUNE, StackResolver.of(Material.RABBIT_HIDE), SlimefunItems.MAGIC_LUMP_2, StackResolver.of(Material.LEATHER), StackResolver.of(Material.EMERALD) },
                new SlimefunItemStack(SlimefunItems.RAINBOW_LEATHER, 4))
                .register(plugin);

        new UnplaceableBlock(itemGroups.cargo, SlimefunItems.CRAFTING_MOTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {StackResolver.of(Material.CRAFTING_TABLE), SlimefunItems.BLISTERING_INGOT_3, StackResolver.of(Material.CRAFTING_TABLE), SlimefunItems.REDSTONE_ALLOY, SlimefunItems.CARGO_MOTOR, SlimefunItems.REDSTONE_ALLOY, StackResolver.of(Material.CRAFTING_TABLE), SlimefunItems.BLISTERING_INGOT_3, StackResolver.of(Material.CRAFTING_TABLE)},
        new SlimefunItemStack(SlimefunItems.CRAFTING_MOTOR, 2))
        .register(plugin);

        new VanillaAutoCrafter(itemGroups.cargo, SlimefunItems.VANILLA_AUTO_CRAFTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.CARGO_MOTOR, null, StackResolver.of(Material.CRAFTING_TABLE), SlimefunItems.CRAFTING_MOTOR, StackResolver.of(Material.CRAFTING_TABLE), null, SlimefunItems.ELECTRIC_MOTOR, null})
        .setCapacity(256)
        .setEnergyConsumption(16)
        .register(plugin);

        new EnhancedAutoCrafter(itemGroups.cargo, SlimefunItems.ENHANCED_AUTO_CRAFTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.CRAFTING_MOTOR, null, StackResolver.of(Material.CRAFTING_TABLE), StackResolver.of(Material.DISPENSER), StackResolver.of(Material.CRAFTING_TABLE), null, SlimefunItems.CARGO_MOTOR, null})
        .setCapacity(256)
        .setEnergyConsumption(16)
        .register(plugin);

        new ArmorAutoCrafter(itemGroups.cargo, SlimefunItems.ARMOR_AUTO_CRAFTER, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.CRAFTING_MOTOR, null, StackResolver.of(Material.DISPENSER), StackResolver.of(Material.ANVIL), StackResolver.of(Material.DISPENSER), StackResolver.of(Material.CRAFTING_TABLE), SlimefunItems.ELECTRIC_MOTOR, StackResolver.of(Material.CRAFTING_TABLE)})
        .setCapacity(256)
        .setEnergyConsumption(32)
        .register(plugin);

        new ProduceCollector(itemGroups.electricity, SlimefunItems.PRODUCE_COLLECTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, StackResolver.of(Material.HAY_BLOCK), null, StackResolver.of(Material.BUCKET), SlimefunItems.MEDIUM_CAPACITOR, StackResolver.of(Material.BUCKET), SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT})
        .setCapacity(256)
        .setProcessingSpeed(1)
        .setEnergyConsumption(16)
        .register(plugin);

        // @formatter:on
    }

    @ParametersAreNonnullByDefault
    private static void registerArmorSet(ItemGroup itemGroup, ItemStack baseComponent, ItemStack[] items, String idSyntax, boolean vanilla, PotionEffect[][] effects, SlimefunAddon addon) {
        String[] components = new String[] { "_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS" };
        List<ItemStack[]> recipes = new ArrayList<>();

        recipes.add(new ItemStack[] { baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, null, null, null });
        recipes.add(new ItemStack[] { baseComponent, null, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent });
        recipes.add(new ItemStack[] { baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, baseComponent, null, baseComponent });
        recipes.add(new ItemStack[] { null, null, null, baseComponent, null, baseComponent, baseComponent, null, baseComponent });

        for (int i = 0; i < 4; i++) {
            if (vanilla) {
                new VanillaItem(itemGroup, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i)).register(addon);
            } else if (i < effects.length && effects[i].length > 0) {
                new SlimefunArmorPiece(itemGroup, new SlimefunItemStack(idSyntax + components[i], items[i]), RecipeType.ARMOR_FORGE, recipes.get(i), effects[i]).register(addon);
            } else {
                new SlimefunItem(itemGroup, new SlimefunItemStack(idSyntax + components[i], items[i]), RecipeType.ARMOR_FORGE, recipes.get(i)).register(addon);
            }
        }
    }

}
