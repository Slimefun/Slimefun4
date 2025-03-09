package io.github.thebusybiscuit.slimefun4.implementation.setup;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

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
                new ItemStack[] {null, new ItemStack(Material.OAK_LOG), null, null, new ItemStack(Material.OAK_LOG), null, null, new ItemStack(Material.OAK_LOG), null})
                .register(plugin);

        new SlimefunItem(itemGroups.weapons, SlimefunItems.GRANDPAS_WALKING_STICK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.LEATHER), new ItemStack(Material.OAK_LOG), new ItemStack(Material.LEATHER), null, new ItemStack(Material.OAK_LOG), null, null, new ItemStack(Material.OAK_LOG), null})
                .register(plugin);

        new PortableCrafter(itemGroups.usefulItems, SlimefunItems.PORTABLE_CRAFTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.BOOK), new ItemStack(Material.CRAFTING_TABLE), null, null, null, null, null, null, null})
                .register(plugin);

        new FortuneCookie(itemGroups.food, SlimefunItems.FORTUNE_COOKIE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.COOKIE), new ItemStack(Material.PAPER), null, null, null, null, null, null, null})
                .register(plugin);

        new DietCookie(itemGroups.food, SlimefunItems.DIET_COOKIE, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {new ItemStack(Material.COOKIE), SlimefunItems.ELYTRA_SCALE.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new OutputChest(itemGroups.basicMachines, SlimefunItems.OUTPUT_CHEST, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.LEAD_INGOT.item(), new ItemStack(Material.HOPPER), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.LEAD_INGOT.item(), new ItemStack(Material.CHEST), SlimefunItems.LEAD_INGOT.item(), null, SlimefunItems.LEAD_INGOT.item(), null})
                .register(plugin);

        new EnhancedCraftingTable(itemGroups.basicMachines, SlimefunItems.ENHANCED_CRAFTING_TABLE).register(plugin);

        new PortableDustbin(itemGroups.usefulItems, SlimefunItems.PORTABLE_DUSTBIN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT)})
                .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.BEEF_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SALT.item(), new ItemStack(Material.COOKED_BEEF), null, null, null, null, null, null, null})
                .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.PORK_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SALT.item(), new ItemStack(Material.COOKED_PORKCHOP), null, null, null, null, null, null, null})
                .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.CHICKEN_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SALT.item(), new ItemStack(Material.COOKED_CHICKEN), null, null, null, null, null, null, null})
                .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.MUTTON_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SALT.item(), new ItemStack(Material.COOKED_MUTTON), null, null, null, null, null, null, null})
                .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.RABBIT_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SALT.item(), new ItemStack(Material.COOKED_RABBIT), null, null, null, null, null, null, null})
                .register(plugin);

        new MeatJerky(itemGroups.food, SlimefunItems.FISH_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SALT.item(), new ItemStack(Material.COOKED_COD), null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.food, SlimefunItems.KELP_COOKIE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.DRIED_KELP), null, new ItemStack(Material.DRIED_KELP), new ItemStack(Material.SUGAR), new ItemStack(Material.DRIED_KELP), null, new ItemStack(Material.DRIED_KELP), null},
                new SlimefunItemStack(SlimefunItems.KELP_COOKIE, 2).item())
                .register(plugin);

        new GrindStone(itemGroups.basicMachines, SlimefunItems.GRIND_STONE).register(plugin);
        new ArmorForge(itemGroups.basicMachines, SlimefunItems.ARMOR_FORGE).register(plugin);

        OreCrusher oreCrusher = new OreCrusher(itemGroups.basicMachines, SlimefunItems.ORE_CRUSHER);
        oreCrusher.register(plugin);

        new Compressor(itemGroups.basicMachines, SlimefunItems.COMPRESSOR).register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.MAGIC_LUMP_1, RecipeType.GRIND_STONE,
                new ItemStack[] {new ItemStack(Material.NETHER_WART), null, null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_1, 2).item())
                .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.MAGIC_LUMP_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_1.item(), SlimefunItems.MAGIC_LUMP_1.item(), null, SlimefunItems.MAGIC_LUMP_1.item(), SlimefunItems.MAGIC_LUMP_1.item(), null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.MAGIC_LUMP_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_2.item(), SlimefunItems.MAGIC_LUMP_2.item(), null, SlimefunItems.MAGIC_LUMP_2.item(), SlimefunItems.MAGIC_LUMP_2.item(), null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.ENDER_LUMP_1, RecipeType.GRIND_STONE,
                new ItemStack[] {new ItemStack(Material.ENDER_EYE), null, null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.ENDER_LUMP_1, 2).item())
                .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.ENDER_LUMP_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_1.item(), SlimefunItems.ENDER_LUMP_1.item(), null, SlimefunItems.ENDER_LUMP_1.item(), SlimefunItems.ENDER_LUMP_1.item(), null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.ENDER_LUMP_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_2.item(), SlimefunItems.ENDER_LUMP_2.item(), null, SlimefunItems.ENDER_LUMP_2.item(), SlimefunItems.ENDER_LUMP_2.item(), null, null, null, null})
                .register(plugin);

        new EnderBackpack(itemGroups.magicalGadgets, SlimefunItems.ENDER_BACKPACK, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_2.item(), new ItemStack(Material.LEATHER), SlimefunItems.ENDER_LUMP_2.item(), new ItemStack(Material.LEATHER), new ItemStack(Material.CHEST), new ItemStack(Material.LEATHER), SlimefunItems.ENDER_LUMP_2.item(), new ItemStack(Material.LEATHER), SlimefunItems.ENDER_LUMP_2.item()})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalArmor, SlimefunItems.ENDER_HELMET, RecipeType.ARMOR_FORGE,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_1.item(), new ItemStack(Material.ENDER_EYE), SlimefunItems.ENDER_LUMP_1.item(), new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalArmor, SlimefunItems.ENDER_CHESTPLATE, RecipeType.ARMOR_FORGE,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_1.item(), null, SlimefunItems.ENDER_LUMP_1.item(), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.ENDER_EYE), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN)})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalArmor, SlimefunItems.ENDER_LEGGINGS, RecipeType.ARMOR_FORGE,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_1.item(), new ItemStack(Material.ENDER_EYE), SlimefunItems.ENDER_LUMP_1.item(), new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN)})
                .register(plugin);

        new EnderBoots(itemGroups.magicalArmor, SlimefunItems.ENDER_BOOTS, RecipeType.ARMOR_FORGE,
                new ItemStack[] {null, null, null, SlimefunItems.ENDER_LUMP_1.item(), null, SlimefunItems.ENDER_LUMP_1.item(), new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN)})
                .register(plugin);

        new MagicEyeOfEnder(itemGroups.magicalGadgets, SlimefunItems.MAGIC_EYE_OF_ENDER, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_2.item(), new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2.item(), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_EYE), new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2.item(), new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2.item()})
                .register(plugin);

        new MagicSugar(itemGroups.food, SlimefunItems.MAGIC_SUGAR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.REDSTONE), new ItemStack(Material.GLOWSTONE_DUST), null, null, null, null, null, null})
                .register(plugin);

        new MonsterJerky(itemGroups.food, SlimefunItems.MONSTER_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SALT.item(), new ItemStack(Material.ROTTEN_FLESH), null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_HELMET, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT), null, null, null}, null)
                .register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_CHESTPLATE, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT)}, null)
                .register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_LEGGINGS, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT)},
                new PotionEffect[] {new PotionEffect(PotionEffectType.SPEED, 300, 2)})
                .register(plugin);

        new LongFallBoots(itemGroups.magicalArmor, SlimefunItems.SLIME_BOOTS, RecipeType.ARMOR_FORGE,
                new ItemStack[] {null, null, null, new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT)},
                new PotionEffect[] {new PotionEffect(VersionedPotionEffectType.JUMP_BOOST, 300, 5)},
                SoundEffect.SLIME_BOOTS_FALL_SOUND)
                .register(plugin);

        new SwordOfBeheading(itemGroups.weapons, SlimefunItems.SWORD_OF_BEHEADING, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.EMERALD), null, SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.EMERALD), SlimefunItems.MAGIC_LUMP_2.item(), null, new ItemStack(Material.BLAZE_ROD), null})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.MAGICAL_BOOK_COVER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.MAGIC_LUMP_2.item(), null, SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.BOOK), SlimefunItems.MAGIC_LUMP_2.item(), null, SlimefunItems.MAGIC_LUMP_2.item(), null})
                .register(plugin);

        new UnplaceableBlock(itemGroups.magicalResources, SlimefunItems.MAGICAL_GLASS, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_2.item(), SlimefunItems.GOLD_DUST.item(), SlimefunItems.MAGIC_LUMP_2.item(), SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE.item(), new ItemStack(Material.GLASS_PANE), SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE.item(), SlimefunItems.MAGIC_LUMP_2.item(), SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE.item(), SlimefunItems.MAGIC_LUMP_2.item()})
                .register(plugin);

        new BasicCircuitBoard(itemGroups.technicalComponents, SlimefunItems.BASIC_CIRCUIT_BOARD, RecipeType.MOB_DROP,
                new ItemStack[] {null, null, null, null, CustomItemStack.create(SlimefunUtils.getCustomHead(HeadTexture.IRON_GOLEM.getTexture()), "&aIron Golem"), null, null, null, null})
                .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.ADVANCED_CIRCUIT_BOARD, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK)})
                .register(plugin);

        new GoldPan(itemGroups.tools, SlimefunItems.GOLD_PAN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.STONE), new ItemStack(Material.BOWL), new ItemStack(Material.STONE), new ItemStack(Material.STONE), new ItemStack(Material.STONE), new ItemStack(Material.STONE)})
                .register(plugin);

        new NetherGoldPan(itemGroups.tools, SlimefunItems.NETHER_GOLD_PAN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.NETHER_BRICK), SlimefunItems.GOLD_PAN.item(), new ItemStack(Material.NETHER_BRICK), new ItemStack(Material.NETHER_BRICK), new ItemStack(Material.NETHER_BRICK), new ItemStack(Material.NETHER_BRICK)})
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.SIFTED_ORE, RecipeType.GOLD_PAN,
                new ItemStack[] {new ItemStack(Material.GRAVEL), null, null, null, null, null, null, null, null})
                .register(plugin);

        new MakeshiftSmeltery(itemGroups.basicMachines, SlimefunItems.MAKESHIFT_SMELTERY).register(plugin);
        new Smeltery(itemGroups.basicMachines, SlimefunItems.SMELTERY).register(plugin);

        new IgnitionChamber(itemGroups.basicMachines, SlimefunItems.IGNITION_CHAMBER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.IRON_INGOT), new ItemStack(Material.FLINT_AND_STEEL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.OBSERVER), null})
                .register(plugin);

        new PressureChamber(itemGroups.basicMachines, SlimefunItems.PRESSURE_CHAMBER).register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.BATTERY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.REDSTONE), null, SlimefunItems.ZINC_INGOT.item(), SlimefunItems.SULFATE.item(), SlimefunItems.COPPER_INGOT.item(), SlimefunItems.ZINC_INGOT.item(), SlimefunItems.SULFATE.item(), SlimefunItems.COPPER_INGOT.item()})
                .register(plugin);

        registerArmorSet(itemGroups.magicalArmor, new ItemStack(Material.GLOWSTONE), new ItemStack[] {SlimefunItems.GLOWSTONE_HELMET.item(), SlimefunItems.GLOWSTONE_CHESTPLATE.item(), SlimefunItems.GLOWSTONE_LEGGINGS.item(), SlimefunItems.GLOWSTONE_BOOTS.item()}, "GLOWSTONE", false,
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
                new ItemStack[] { SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), null, SlimefunItems.RAINBOW_LEATHER.item(), null, null, null },
                rainbowArmorColors)
                .register(plugin);

        new RainbowArmorPiece(itemGroups.magicalArmor, SlimefunItems.RAINBOW_CHESTPLATE, RecipeType.ARMOR_FORGE,
                new ItemStack[] { SlimefunItems.RAINBOW_LEATHER.item(), null, SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item() },
                rainbowArmorColors)
                .register(plugin);

        new RainbowArmorPiece(itemGroups.magicalArmor, SlimefunItems.RAINBOW_LEGGINGS, RecipeType.ARMOR_FORGE,
                new ItemStack[] { SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), null, SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), null, SlimefunItems.RAINBOW_LEATHER.item() },
                rainbowArmorColors)
                .register(plugin);

        new RainbowArmorPiece(itemGroups.magicalArmor, SlimefunItems.RAINBOW_BOOTS, RecipeType.ARMOR_FORGE,
                new ItemStack[] { null, null, null, SlimefunItems.RAINBOW_LEATHER.item(), null, SlimefunItems.RAINBOW_LEATHER.item(), SlimefunItems.RAINBOW_LEATHER.item(), null, SlimefunItems.RAINBOW_LEATHER.item() },
                rainbowArmorColors)
                .register(plugin);


        registerArmorSet(itemGroups.armor, SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_HELMET.item(), SlimefunItems.DAMASCUS_STEEL_CHESTPLATE.item(), SlimefunItems.DAMASCUS_STEEL_LEGGINGS.item(), SlimefunItems.DAMASCUS_STEEL_BOOTS.item()}, "DAMASCUS_STEEL", false, new PotionEffect[0][0], plugin);

        registerArmorSet(itemGroups.armor, SlimefunItems.REINFORCED_ALLOY_INGOT, new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_HELMET.item(), SlimefunItems.REINFORCED_ALLOY_CHESTPLATE.item(), SlimefunItems.REINFORCED_ALLOY_LEGGINGS.item(), SlimefunItems.REINFORCED_ALLOY_BOOTS.item()}, "REINFORCED_ALLOY", false, new PotionEffect[0][0], plugin);

        registerArmorSet(itemGroups.armor, new ItemStack(Material.CACTUS), new ItemStack[] {SlimefunItems.CACTUS_HELMET.item(), SlimefunItems.CACTUS_CHESTPLATE.item(), SlimefunItems.CACTUS_LEGGINGS.item(), SlimefunItems.CACTUS_BOOTS.item()}, "CACTUS", false, new PotionEffect[0][0], plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.REINFORCED_ALLOY_INGOT,
                new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.BILLON_INGOT.item(), SlimefunItems.GOLD_24K.item(), null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.HARDENED_METAL_INGOT,
                new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.DAMASCUS_STEEL_INGOT,
                new ItemStack[] {SlimefunItems.STEEL_INGOT.item(), SlimefunItems.IRON_DUST.item(), SlimefunItems.CARBON.item(), new ItemStack(Material.IRON_INGOT), null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.STEEL_INGOT,
                new ItemStack[] {SlimefunItems.IRON_DUST.item(), SlimefunItems.CARBON.item(), new ItemStack(Material.IRON_INGOT), null, null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.BRONZE_INGOT,
                new ItemStack[] {SlimefunItems.COPPER_DUST.item(), SlimefunItems.TIN_DUST.item(), SlimefunItems.COPPER_INGOT.item(), null, null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.DURALUMIN_INGOT,
                new ItemStack[] {SlimefunItems.ALUMINUM_DUST.item(), SlimefunItems.COPPER_DUST.item(), SlimefunItems.ALUMINUM_INGOT.item(), null, null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.BILLON_INGOT,
                new ItemStack[] {SlimefunItems.SILVER_DUST.item(), SlimefunItems.COPPER_DUST.item(), SlimefunItems.SILVER_INGOT.item(), null, null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.BRASS_INGOT,
                new ItemStack[] {SlimefunItems.COPPER_DUST.item(), SlimefunItems.ZINC_DUST.item(), SlimefunItems.COPPER_INGOT.item(), null, null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.ALUMINUM_BRASS_INGOT,
                new ItemStack[] {SlimefunItems.ALUMINUM_DUST.item(), SlimefunItems.BRASS_INGOT.item(), SlimefunItems.ALUMINUM_INGOT.item(), null, null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.ALUMINUM_BRONZE_INGOT,
                new ItemStack[] {SlimefunItems.ALUMINUM_DUST.item(), SlimefunItems.BRONZE_INGOT.item(), SlimefunItems.ALUMINUM_INGOT.item(), null, null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.CORINTHIAN_BRONZE_INGOT,
                new ItemStack[] {SlimefunItems.SILVER_DUST.item(), SlimefunItems.GOLD_DUST.item(), SlimefunItems.COPPER_DUST.item(), SlimefunItems.BRONZE_INGOT.item(), null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.SOLDER_INGOT,
                new ItemStack[] {SlimefunItems.LEAD_DUST.item(), SlimefunItems.TIN_DUST.item(), SlimefunItems.LEAD_INGOT.item(), null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SYNTHETIC_SAPPHIRE, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.ALUMINUM_DUST.item(), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS_PANE), SlimefunItems.ALUMINUM_INGOT.item(), new ItemStack(Material.LAPIS_LAZULI), null, null, null, null})
                .setUseableInWorkbench(true)
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SYNTHETIC_DIAMOND, RecipeType.PRESSURE_CHAMBER,
                new ItemStack[] {SlimefunItems.CARBON_CHUNK.item(), null, null, null, null, null, null, null, null})
                .setUseableInWorkbench(true)
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.RAW_CARBONADO, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND.item(), SlimefunItems.CARBON_CHUNK.item(), new ItemStack(Material.GLASS_PANE), null, null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.NICKEL_INGOT,
                new ItemStack[] {SlimefunItems.IRON_DUST.item(), new ItemStack(Material.IRON_INGOT), SlimefunItems.COPPER_DUST.item(), null, null, null, null, null, null})
                .register(plugin);

        new AlloyIngot(itemGroups.resources, SlimefunItems.COBALT_INGOT,
                new ItemStack[] {SlimefunItems.IRON_DUST.item(), SlimefunItems.COPPER_DUST.item(), SlimefunItems.NICKEL_INGOT.item(), null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.CARBONADO, RecipeType.PRESSURE_CHAMBER,
                new ItemStack[] {SlimefunItems.RAW_CARBONADO.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.FERROSILICON, RecipeType.SMELTERY,
                new ItemStack[] {new ItemStack(Material.IRON_INGOT), SlimefunItems.IRON_DUST.item(), SlimefunItems.SILICON.item(), null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.IRON_DUST, RecipeType.ORE_CRUSHER,
                new ItemStack[] {new ItemStack(Material.IRON_ORE), null, null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.IRON_DUST, oreCrusher.isOreDoublingEnabled() ? 2 : 1).item())
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.GOLD_DUST, RecipeType.ORE_CRUSHER,
                new ItemStack[] {new ItemStack(Material.GOLD_ORE), null, null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.GOLD_DUST, oreCrusher.isOreDoublingEnabled() ? 2 : 1).item())
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.COPPER_DUST, RecipeType.ORE_WASHER,
                new ItemStack[] {SlimefunItems.SIFTED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.TIN_DUST, RecipeType.ORE_WASHER,
                new ItemStack[] {SlimefunItems.SIFTED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SILVER_DUST, RecipeType.ORE_WASHER,
                new ItemStack[] {SlimefunItems.SIFTED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.LEAD_DUST, RecipeType.ORE_WASHER,
                new ItemStack[] {SlimefunItems.SIFTED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.ALUMINUM_DUST, RecipeType.ORE_WASHER,
                new ItemStack[] {SlimefunItems.SIFTED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.ZINC_DUST, RecipeType.ORE_WASHER,
                new ItemStack[] {SlimefunItems.SIFTED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.MAGNESIUM_DUST, RecipeType.ORE_WASHER,
                new ItemStack[] {SlimefunItems.SIFTED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.COPPER_INGOT, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.COPPER_DUST.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.TIN_INGOT, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.TIN_DUST.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SILVER_INGOT, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.SILVER_DUST.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.LEAD_INGOT, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.LEAD_DUST.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.ALUMINUM_INGOT, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.ALUMINUM_DUST.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.ZINC_INGOT, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.ZINC_DUST.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.MAGNESIUM_INGOT, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.MAGNESIUM_DUST.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SULFATE, RecipeType.ORE_CRUSHER,
                new ItemStack[] {new ItemStack(Material.NETHERRACK, 16), null, null, null, null, null, null, null, null})
                .register(plugin);

        new UnplaceableBlock(itemGroups.resources, SlimefunItems.CARBON, RecipeType.COMPRESSOR,
                new ItemStack[] {new ItemStack(Material.COAL, 8), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.WHEAT_FLOUR, RecipeType.GRIND_STONE,
                new ItemStack[] {new ItemStack(Material.WHEAT), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.STEEL_PLATE, RecipeType.COMPRESSOR,
                new ItemStack[] {new SlimefunItemStack(SlimefunItems.STEEL_INGOT, 8).item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new UnplaceableBlock(itemGroups.resources, SlimefunItems.COMPRESSED_CARBON, RecipeType.COMPRESSOR,
                new ItemStack[] {new SlimefunItemStack(SlimefunItems.CARBON, 4).item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new UnplaceableBlock(itemGroups.resources, SlimefunItems.CARBON_CHUNK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.COMPRESSED_CARBON.item(), new ItemStack(Material.FLINT), SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.COMPRESSED_CARBON.item()})
                .register(plugin);

        new SteelThruster(itemGroups.technicalComponents, SlimefunItems.STEEL_THRUSTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.REDSTONE), null, SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), SlimefunItems.STEEL_PLATE.item(), new ItemStack(Material.FIRE_CHARGE), SlimefunItems.STEEL_PLATE.item()})
                .register(plugin);

        new SlimefunItem(itemGroups.technicalComponents, SlimefunItems.POWER_CRYSTAL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE.item(), new ItemStack(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE.item(), SlimefunItems.SYNTHETIC_DIAMOND.item(), SlimefunItems.SYNTHETIC_SAPPHIRE.item(), new ItemStack(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE.item(), new ItemStack(Material.REDSTONE)})
                .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.DURALUMIN_JETPACK,
                new ItemStack[] {SlimefunItems.DURALUMIN_INGOT.item(), null, SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.35, 20)
                .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.SOLDER_JETPACK,
                new ItemStack[] {SlimefunItems.SOLDER_INGOT.item(), null, SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.4, 30)
                .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.BILLON_JETPACK,
                new ItemStack[] {SlimefunItems.BILLON_INGOT.item(), null, SlimefunItems.BILLON_INGOT.item(), SlimefunItems.BILLON_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.BILLON_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.45, 45)
                .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.STEEL_JETPACK,
                new ItemStack[] {SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.STEEL_INGOT.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.5, 60)
                .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.DAMASCUS_STEEL_JETPACK,
                new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT.item(), null, SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.55, 75)
                .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.REINFORCED_ALLOY_JETPACK,
                new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT.item(), null, SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.6, 100)
                .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.CARBONADO_JETPACK,
                new ItemStack[] {SlimefunItems.CARBON_CHUNK.item(), null, SlimefunItems.CARBON_CHUNK.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.LARGE_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.7, 150)
                .register(plugin);

        new Jetpack(itemGroups.technicalGadgets, SlimefunItems.ARMORED_JETPACK,
                new ItemStack[] {SlimefunItems.STEEL_PLATE.item(), null, SlimefunItems.STEEL_PLATE.item(), SlimefunItems.STEEL_PLATE.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.STEEL_PLATE.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.5, 50)
                .register(plugin);

        new Parachute(itemGroups.technicalGadgets, SlimefunItems.PARACHUTE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CLOTH.item(), SlimefunItems.CLOTH.item(), SlimefunItems.CLOTH.item(), SlimefunItems.CHAIN.item(), null, SlimefunItems.CHAIN.item(), null, null, null})
                .register(plugin);

        new HologramProjector(itemGroups.technicalGadgets, SlimefunItems.HOLOGRAM_PROJECTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.POWER_CRYSTAL.item(), null, SlimefunItems.ALUMINUM_BRASS_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.ALUMINUM_BRASS_INGOT.item(), null, SlimefunItems.ALUMINUM_BRASS_INGOT.item(), null},
                new SlimefunItemStack(SlimefunItems.HOLOGRAM_PROJECTOR, 3).item())
                .register(plugin);

        new UnplaceableBlock(itemGroups.misc, SlimefunItems.CHAIN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.STEEL_INGOT.item(), null, null},
                new SlimefunItemStack(SlimefunItems.CHAIN, 8).item())
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.HOOK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.STEEL_INGOT.item(), null, null, null})
                .register(plugin);

        new GrapplingHook(itemGroups.tools, SlimefunItems.GRAPPLING_HOOK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, SlimefunItems.HOOK.item(), null, SlimefunItems.CHAIN.item(), null, SlimefunItems.CHAIN.item(), null, null})
                .register(plugin);

        new MagicWorkbench(itemGroups.basicMachines, SlimefunItems.MAGIC_WORKBENCH).register(plugin);

        new SlimefunItem(itemGroups.magicalGadgets, SlimefunItems.STAFF_ELEMENTAL, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.MAGICAL_BOOK_COVER.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, new ItemStack(Material.STICK), SlimefunItems.MAGICAL_BOOK_COVER.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, null})
                .register(plugin);

        new WindStaff(itemGroups.magicalGadgets, SlimefunItems.STAFF_WIND, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, new ItemStack(Material.FEATHER), SlimefunItems.ENDER_LUMP_3.item(), null, SlimefunItems.STAFF_ELEMENTAL.item(), new ItemStack(Material.FEATHER), SlimefunItems.STAFF_ELEMENTAL.item(), null, null})
                .register(plugin);

        new WaterStaff(itemGroups.magicalGadgets, SlimefunItems.STAFF_WATER, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, new ItemStack(Material.LILY_PAD), SlimefunItems.MAGIC_LUMP_2.item(), null, SlimefunItems.STAFF_ELEMENTAL.item(), new ItemStack(Material.LILY_PAD), SlimefunItems.STAFF_ELEMENTAL.item(), null, null})
                .register(plugin);

        new EnchantedItem(itemGroups.magicalGadgets, SlimefunItems.STAFF_FIRE, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, null, SlimefunItems.LAVA_CRYSTAL.item(), null, SlimefunItems.STAFF_ELEMENTAL.item(), null, SlimefunItems.STAFF_ELEMENTAL.item(), null, null})
                .register(plugin);

        String[] multiToolItems = new String[] {"PORTABLE_CRAFTER", "MAGIC_EYE_OF_ENDER", "STAFF_ELEMENTAL_WIND", "GRAPPLING_HOOK"};

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.DURALUMIN_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.DURALUMIN_INGOT.item(), null, SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.DURALUMIN_INGOT.item(), null, SlimefunItems.DURALUMIN_INGOT.item(), null},
                20, multiToolItems)
                .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.SOLDER_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SOLDER_INGOT.item(), null, SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.SOLDER_INGOT.item(), null, SlimefunItems.SOLDER_INGOT.item(), null},
                30, multiToolItems)
                .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.BILLON_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.BILLON_INGOT.item(), null, SlimefunItems.BILLON_INGOT.item(), SlimefunItems.BILLON_INGOT.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.BILLON_INGOT.item(), null, SlimefunItems.BILLON_INGOT.item(), null},
                40, multiToolItems)
                .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.STEEL_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.STEEL_INGOT.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.STEEL_INGOT.item(), null},
                50, multiToolItems)
                .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.DAMASCUS_STEEL_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT.item(), null, SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), null, SlimefunItems.DAMASCUS_STEEL_INGOT.item(), null},
                60, multiToolItems)
                .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.REINFORCED_ALLOY_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT.item(), null, SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), null, SlimefunItems.REINFORCED_ALLOY_INGOT.item(), null},
                75, multiToolItems)
                .register(plugin);

        new MultiTool(itemGroups.technicalGadgets, SlimefunItems.CARBONADO_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CARBONADO.item(), null, SlimefunItems.CARBONADO.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.LARGE_CAPACITOR.item(), SlimefunItems.CARBONADO.item(), null, SlimefunItems.CARBONADO.item(), null},
                100, "PORTABLE_CRAFTER", "MAGIC_EYE_OF_ENDER", "STAFF_ELEMENTAL_WIND", "GRAPPLING_HOOK", "GOLD_PAN", "NETHER_GOLD_PAN")
                .register(plugin);

        new OreWasher(itemGroups.basicMachines, SlimefunItems.ORE_WASHER).register(plugin);

        new GoldIngot(itemGroups.resources, 24, SlimefunItems.GOLD_24K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), SlimefunItems.GOLD_22K.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new GoldIngot(itemGroups.resources, 22, SlimefunItems.GOLD_22K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), SlimefunItems.GOLD_20K.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new GoldIngot(itemGroups.resources, 20, SlimefunItems.GOLD_20K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), SlimefunItems.GOLD_18K.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new GoldIngot(itemGroups.resources, 18, SlimefunItems.GOLD_18K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), SlimefunItems.GOLD_16K.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new GoldIngot(itemGroups.resources, 16, SlimefunItems.GOLD_16K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), SlimefunItems.GOLD_14K.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new GoldIngot(itemGroups.resources, 14, SlimefunItems.GOLD_14K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), SlimefunItems.GOLD_12K.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new GoldIngot(itemGroups.resources, 12, SlimefunItems.GOLD_12K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), SlimefunItems.GOLD_10K.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new GoldIngot(itemGroups.resources, 10, SlimefunItems.GOLD_10K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), SlimefunItems.GOLD_8K.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new GoldIngot(itemGroups.resources, 8, SlimefunItems.GOLD_8K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), SlimefunItems.GOLD_6K.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new GoldIngot(itemGroups.resources, 6, SlimefunItems.GOLD_6K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), SlimefunItems.GOLD_4K.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new GoldIngot(itemGroups.resources, 4, SlimefunItems.GOLD_4K, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_DUST.item(), null, null, null, null, null, null, null, null})
                .setUseableInWorkbench(true)
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.STONE_CHUNK, RecipeType.ORE_WASHER,
                new ItemStack[] {SlimefunItems.SIFTED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.SILICON, RecipeType.SMELTERY,
                new ItemStack[] {new ItemStack(Material.QUARTZ_BLOCK), null, null, null, null, null, null, null, null})
                .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.SOLAR_PANEL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), SlimefunItems.SILICON.item(), SlimefunItems.SILICON.item(), SlimefunItems.SILICON.item(), SlimefunItems.FERROSILICON.item(), SlimefunItems.FERROSILICON.item(), SlimefunItems.FERROSILICON.item()})
                .register(plugin);

        new SolarHelmet(itemGroups.technicalGadgets, SlimefunItems.SOLAR_HELMET, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.SOLAR_PANEL.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), null, SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), null, SlimefunItems.MEDIUM_CAPACITOR.item()},
                0.1)
                .register(plugin);

        new UnplaceableBlock(itemGroups.magicalResources, SlimefunItems.LAVA_CRYSTAL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.BLAZE_POWDER), SlimefunItems.FIRE_RUNE.item(), new ItemStack(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1.item()})
                .register(plugin);

        new StormStaff(itemGroups.magicalGadgets, SlimefunItems.STAFF_STORM, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.LIGHTNING_RUNE.item(), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.LIGHTNING_RUNE.item(), SlimefunItems.STAFF_WATER.item(), SlimefunItems.MAGIC_SUGAR.item(), SlimefunItems.STAFF_WIND.item(), SlimefunItems.LIGHTNING_RUNE.item(), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.LIGHTNING_RUNE.item()})
                .register(plugin);

        ItemStack weaknessPotion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) weaknessPotion.getItemMeta();
        if (Slimefun.getMinecraftVersion().isBefore(20, 2)) {
            meta.setBasePotionData(new PotionData(PotionType.WEAKNESS, false, false));
        } else {
            meta.setBasePotionType(PotionType.WEAKNESS);
        }
        weaknessPotion.setItemMeta(meta);

        new MagicalZombiePills(itemGroups.magicalGadgets, SlimefunItems.MAGICAL_ZOMBIE_PILLS, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {new ItemStack(Material.GOLD_INGOT), SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.APPLE), weaknessPotion, new ItemStack(Material.APPLE), new ItemStack(Material.GOLD_INGOT), SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.GOLD_INGOT)},
                new SlimefunItemStack(SlimefunItems.MAGICAL_ZOMBIE_PILLS, 2).item())
                .register(plugin);

        new SmeltersPickaxe(itemGroups.tools, SlimefunItems.SMELTERS_PICKAXE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.LAVA_CRYSTAL.item(), null, SlimefunItems.FERROSILICON.item(), null, null, SlimefunItems.FERROSILICON.item(), null})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.COMMON_TALISMAN, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_2.item(), SlimefunItems.GOLD_8K.item(), SlimefunItems.MAGIC_LUMP_2.item(), null, new ItemStack(Material.EMERALD), null, SlimefunItems.MAGIC_LUMP_2.item(), SlimefunItems.GOLD_8K.item(), SlimefunItems.MAGIC_LUMP_2.item()})
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_ANVIL,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), new ItemStack(Material.ANVIL), SlimefunItems.COMMON_TALISMAN.item(), new ItemStack(Material.ANVIL), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                true, false, "anvil")
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_MINER,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.SYNTHETIC_SAPPHIRE.item(), SlimefunItems.COMMON_TALISMAN.item(), SlimefunItems.SIFTED_ORE.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                false, false, "miner", 20)
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_FARMER,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), new ItemStack(Material.DIAMOND_HOE), SlimefunItems.COMMON_TALISMAN.item(),  new ItemStack(Material.DIAMOND_HOE), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                false, false, "farmer", 20)
                .register(plugin);


        new Talisman(SlimefunItems.TALISMAN_HUNTER,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.SYNTHETIC_SAPPHIRE.item(), SlimefunItems.COMMON_TALISMAN.item(), SlimefunItems.MONSTER_JERKY.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                false, false, "hunter", 20)
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_LAVA,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.COMMON_TALISMAN.item(), new ItemStack(Material.LAVA_BUCKET), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                true, true, "lava", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4))
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_WATER,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), new ItemStack(Material.WATER_BUCKET), SlimefunItems.COMMON_TALISMAN.item(), new ItemStack(Material.FISHING_ROD), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                true, true, "water", new PotionEffect(PotionEffectType.WATER_BREATHING, 3600, 4))
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_ANGEL,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), new ItemStack(Material.FEATHER), SlimefunItems.COMMON_TALISMAN.item(), new ItemStack(Material.FEATHER), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                false, true, "angel", 75)
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_FIRE,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.COMMON_TALISMAN.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                true, true, "fire", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4))
                .register(plugin);

        new MagicianTalisman(SlimefunItems.TALISMAN_MAGICIAN,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_3.item(), null, SlimefunItems.ENDER_LUMP_3.item(), new ItemStack(Material.ENCHANTING_TABLE), SlimefunItems.COMMON_TALISMAN.item(), new ItemStack(Material.ENCHANTING_TABLE), SlimefunItems.ENDER_LUMP_3.item(), null, SlimefunItems.ENDER_LUMP_3.item()})
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_TRAVELLER,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.STAFF_WIND.item(), SlimefunItems.TALISMAN_ANGEL.item(), SlimefunItems.STAFF_WIND.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                false, false, "traveller", 60, new PotionEffect(PotionEffectType.SPEED, 3600, 2))
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_WARRIOR,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.COMMON_TALISMAN.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                true, true, "warrior", new PotionEffect(VersionedPotionEffectType.STRENGTH, 3600, 2))
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_KNIGHT,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.TALISMAN_WARRIOR.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                "knight", 30, new PotionEffect(PotionEffectType.REGENERATION, 100, 3))
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_CAVEMAN,
                new ItemStack[] { SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), new ItemStack(Material.GOLDEN_PICKAXE), SlimefunItems.TALISMAN_MINER.item(), SlimefunItems.EARTH_RUNE.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()},
                false, false, "caveman", 50, new PotionEffect(VersionedPotionEffectType.HASTE, 800, 2))
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_WISE,
                new ItemStack[] { SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE.item(), SlimefunItems.TALISMAN_MAGICIAN.item(), SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE.item(), SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.MAGIC_LUMP_3.item()},
                false, false, "wise", 20)
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.GILDED_IRON, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.GOLD_24K.item(), SlimefunItems.IRON_DUST.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new SyntheticEmerald(itemGroups.resources, SlimefunItems.SYNTHETIC_EMERALD, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.SYNTHETIC_SAPPHIRE.item(), SlimefunItems.ALUMINUM_DUST.item(), SlimefunItems.ALUMINUM_INGOT.item(), new ItemStack(Material.GLASS_PANE), null, null, null, null, null})
                .register(plugin);

        registerArmorSet(itemGroups.armor, SlimefunItems.CHAIN, new ItemStack[] {
                new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_BOOTS)
        }, "CHAIN", true, new PotionEffect[0][0], plugin);

        new Talisman(SlimefunItems.TALISMAN_WHIRLWIND,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.STAFF_WIND.item(), SlimefunItems.TALISMAN_TRAVELLER.item(), SlimefunItems.STAFF_WIND.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()}
                , false, true, "whirlwind", 60)
                .register(plugin);

        new Talisman(SlimefunItems.TALISMAN_WIZARD,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_3.item(), null, SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.MAGIC_EYE_OF_ENDER.item(), SlimefunItems.TALISMAN_MAGICIAN.item(), SlimefunItems.MAGIC_EYE_OF_ENDER.item(), SlimefunItems.ENDER_LUMP_3.item(), null, SlimefunItems.ENDER_LUMP_3.item()},
                false, false, "wizard", 60)
                .register(plugin);

        new LumberAxe(itemGroups.tools, SlimefunItems.LUMBER_AXE, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND.item(), SlimefunItems.SYNTHETIC_DIAMOND.item(), null, SlimefunItems.SYNTHETIC_EMERALD.item(), SlimefunItems.GILDED_IRON.item(), null, null, SlimefunItems.GILDED_IRON.item(), null})
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.SALT, RecipeType.ORE_WASHER,
                new ItemStack[] {new ItemStack(Material.SAND, 2), null, null, null, null, null, null, null, null})
                .register(plugin);

        new HeavyCream(itemGroups.misc, SlimefunItems.HEAVY_CREAM, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.MILK_BUCKET), null, null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.HEAVY_CREAM, 2).item())
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.CHEESE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.MILK_BUCKET), SlimefunItems.SALT.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.BUTTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.HEAVY_CREAM.item(), SlimefunItems.SALT.item(), null, null, null, null, null, null, null})
                .register(plugin);

        registerArmorSet(itemGroups.armor, SlimefunItems.GILDED_IRON, new ItemStack[] {
                SlimefunItems.GILDED_IRON_HELMET.item(), SlimefunItems.GILDED_IRON_CHESTPLATE.item(), SlimefunItems.GILDED_IRON_LEGGINGS.item(), SlimefunItems.GILDED_IRON_BOOTS.item()
        }, "GILDED_IRON", false, new PotionEffect[0][0], plugin);

        new SlimefunItem(itemGroups.technicalComponents, SlimefunItems.REINFORCED_CLOTH, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.CLOTH.item(), null, SlimefunItems.CLOTH.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.CLOTH.item(), null, SlimefunItems.CLOTH.item(), null}, new SlimefunItemStack(SlimefunItems.REINFORCED_CLOTH, 2).item())
                .register(plugin);

        new HazmatArmorPiece(itemGroups.armor, SlimefunItems.SCUBA_HELMET, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.ORANGE_WOOL), SlimefunItems.REINFORCED_CLOTH.item(), new ItemStack(Material.ORANGE_WOOL), SlimefunItems.REINFORCED_CLOTH.item(), new ItemStack(Material.GLASS_PANE), SlimefunItems.REINFORCED_CLOTH.item(), null, null, null},
                new PotionEffect[] {new PotionEffect(PotionEffectType.WATER_BREATHING, 300, 1)})
                .register(plugin);

        new HazmatArmorPiece(itemGroups.armor, SlimefunItems.HAZMAT_CHESTPLATE, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.ORANGE_WOOL), null, new ItemStack(Material.ORANGE_WOOL), SlimefunItems.REINFORCED_CLOTH.item(), SlimefunItems.REINFORCED_CLOTH.item(), SlimefunItems.REINFORCED_CLOTH.item(), new ItemStack(Material.BLACK_WOOL), SlimefunItems.REINFORCED_CLOTH.item(), new ItemStack(Material.BLACK_WOOL)},
                new PotionEffect[] {new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 1)})
                .register(plugin);

        new HazmatArmorPiece(itemGroups.armor, SlimefunItems.HAZMAT_LEGGINGS, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.BLACK_WOOL), SlimefunItems.REINFORCED_CLOTH.item(), new ItemStack(Material.BLACK_WOOL), SlimefunItems.REINFORCED_CLOTH.item(), null, SlimefunItems.REINFORCED_CLOTH.item(), SlimefunItems.REINFORCED_CLOTH.item(), null, SlimefunItems.REINFORCED_CLOTH.item()}, new PotionEffect[0])
                .register(plugin);

        new HazmatArmorPiece(itemGroups.armor, SlimefunItems.HAZMAT_BOOTS, RecipeType.ARMOR_FORGE,
                new ItemStack[] {SlimefunItems.REINFORCED_CLOTH.item(), null, SlimefunItems.REINFORCED_CLOTH.item(), SlimefunItems.REINFORCED_CLOTH.item(), null, SlimefunItems.REINFORCED_CLOTH.item(), new ItemStack(Material.BLACK_WOOL), null, new ItemStack(Material.BLACK_WOOL)}, new PotionEffect[0])
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.CRUSHED_ORE, RecipeType.ORE_CRUSHER,
                new ItemStack[] {SlimefunItems.SIFTED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.PULVERIZED_ORE, RecipeType.ORE_CRUSHER,
                new ItemStack[] {SlimefunItems.CRUSHED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.PURE_ORE_CLUSTER, RecipeType.ORE_WASHER,
                new ItemStack[] {SlimefunItems.PULVERIZED_ORE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new RadioactiveItem(itemGroups.misc, Radioactivity.LOW, SlimefunItems.TINY_URANIUM, RecipeType.ORE_CRUSHER,
                new ItemStack[] {SlimefunItems.PURE_ORE_CLUSTER.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new RadioactiveItem(itemGroups.misc, Radioactivity.MODERATE, SlimefunItems.SMALL_URANIUM, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.TINY_URANIUM.item(), SlimefunItems.TINY_URANIUM.item(), SlimefunItems.TINY_URANIUM.item(), SlimefunItems.TINY_URANIUM.item(), SlimefunItems.TINY_URANIUM.item(), SlimefunItems.TINY_URANIUM.item(), SlimefunItems.TINY_URANIUM.item(), SlimefunItems.TINY_URANIUM.item(), SlimefunItems.TINY_URANIUM.item()})
                .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.HIGH, SlimefunItems.URANIUM, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SMALL_URANIUM.item(), SlimefunItems.SMALL_URANIUM.item(), null, SlimefunItems.SMALL_URANIUM.item(), SlimefunItems.SMALL_URANIUM.item(), null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.REDSTONE_ALLOY, RecipeType.SMELTERY,
                new ItemStack[] {new ItemStack(Material.REDSTONE), new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.FERROSILICON.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), null, null, null, null, null})
                .register(plugin);

        registerArmorSet(itemGroups.armor, SlimefunItems.GOLD_12K, new ItemStack[] {
                SlimefunItems.GOLDEN_HELMET_12K.item(), SlimefunItems.GOLDEN_CHESTPLATE_12K.item(), SlimefunItems.GOLDEN_LEGGINGS_12K.item(), SlimefunItems.GOLDEN_BOOTS_12K.item()
        }, "GOLD_12K", false, new PotionEffect[0][0], plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.CLOTH, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.WHITE_WOOL), null, null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.CLOTH, 8).item())
                .register(plugin);

        new Bandage(itemGroups.usefulItems, SlimefunItems.RAG, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CLOTH.item(), SlimefunItems.CLOTH.item(), SlimefunItems.CLOTH.item(), new ItemStack(Material.STRING), null, new ItemStack(Material.STRING), SlimefunItems.CLOTH.item(), SlimefunItems.CLOTH.item(), SlimefunItems.CLOTH.item()},
                new SlimefunItemStack(SlimefunItems.RAG, 2).item(), 0)
                .register(plugin);

        new Bandage(itemGroups.usefulItems, SlimefunItems.BANDAGE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.RAG.item(), new ItemStack(Material.STRING), SlimefunItems.RAG.item(), null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.BANDAGE, 4).item(), 1)
                .register(plugin);

        new Splint(itemGroups.usefulItems, SlimefunItems.SPLINT, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.STICK), null, new ItemStack(Material.IRON_INGOT), null},
                new SlimefunItemStack(SlimefunItems.SPLINT, 4).item())
                .register(plugin);

        new UnplaceableBlock(itemGroups.misc, SlimefunItems.TIN_CAN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.TIN_INGOT.item(), SlimefunItems.TIN_INGOT.item(), SlimefunItems.TIN_INGOT.item(), SlimefunItems.TIN_INGOT.item(), null, SlimefunItems.TIN_INGOT.item(), SlimefunItems.TIN_INGOT.item(), SlimefunItems.TIN_INGOT.item(), SlimefunItems.TIN_INGOT.item()},
                new SlimefunItemStack(SlimefunItems.TIN_CAN, 8).item())
                .register(plugin);

        new Vitamins(itemGroups.usefulItems, SlimefunItems.VITAMINS, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.TIN_CAN.item(), new ItemStack(Material.APPLE), new ItemStack(Material.RED_MUSHROOM), new ItemStack(Material.SUGAR), null, null, null, null, null})
                .register(plugin);

        new Medicine(itemGroups.usefulItems, SlimefunItems.MEDICINE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.VITAMINS.item(), new ItemStack(Material.GLASS_BOTTLE), SlimefunItems.HEAVY_CREAM.item(), null, null, null, null, null, null})
                .register(plugin);

        new SlimefunArmorPiece(itemGroups.technicalGadgets, SlimefunItems.NIGHT_VISION_GOGGLES, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.LIME_STAINED_GLASS_PANE), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.LIME_STAINED_GLASS_PANE), new ItemStack(Material.COAL_BLOCK), null, new ItemStack(Material.COAL_BLOCK)},
                new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 20)})
                .register(plugin);

        new PickaxeOfContainment(itemGroups.tools, SlimefunItems.PICKAXE_OF_CONTAINMENT, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItems.FERROSILICON.item(), SlimefunItems.FERROSILICON.item(), SlimefunItems.FERROSILICON.item(), null, SlimefunItems.GILDED_IRON.item(), null, null, SlimefunItems.GILDED_IRON.item(), null})
                .register(plugin);

        new TableSaw(itemGroups.basicMachines, SlimefunItems.TABLE_SAW).register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_HELMET_STEEL, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE.item(), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), null, null, null}, null)
                .register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_CHESTPLATE_STEEL, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE.item(), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL)}, null)
                .register(plugin);

        new SlimefunArmorPiece(itemGroups.magicalArmor, SlimefunItems.SLIME_LEGGINGS_STEEL, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE.item(), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL)},
                new PotionEffect[] {new PotionEffect(PotionEffectType.SPEED, 300, 2)})
                .register(plugin);

        new LongFallBoots(itemGroups.magicalArmor, SlimefunItems.SLIME_BOOTS_STEEL, RecipeType.ARMOR_FORGE,
                new ItemStack[] {null, null, null, new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE.item(), new ItemStack(Material.SLIME_BALL)},
                new PotionEffect[] {new PotionEffect(VersionedPotionEffectType.JUMP_BOOST, 300, 5)},
                SoundEffect.SLIME_BOOTS_FALL_SOUND)
                .register(plugin);

        new VampireBlade(itemGroups.weapons, SlimefunItems.BLADE_OF_VAMPIRES, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, new ItemStack(Material.WITHER_SKELETON_SKULL), null, null, new ItemStack(Material.WITHER_SKELETON_SKULL), null, null, new ItemStack(Material.BLAZE_ROD), null})
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.GOLD_24K_BLOCK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.GOLD_24K.item(), SlimefunItems.GOLD_24K.item(), SlimefunItems.GOLD_24K.item(), SlimefunItems.GOLD_24K.item(), SlimefunItems.GOLD_24K.item(), SlimefunItems.GOLD_24K.item(), SlimefunItems.GOLD_24K.item(), SlimefunItems.GOLD_24K.item(), SlimefunItems.GOLD_24K.item()})
                .register(plugin);

        new Composter(itemGroups.basicMachines, SlimefunItems.COMPOSTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.OAK_SLAB), null, new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), null, new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), new ItemStack(Material.CAULDRON), new ItemStack(Material.OAK_SLAB)})
                .register(plugin);

        new FarmerShoes(itemGroups.magicalArmor, SlimefunItems.FARMER_SHOES, RecipeType.ARMOR_FORGE,
                new ItemStack[] {null, null, null, new ItemStack(Material.HAY_BLOCK), null, new ItemStack(Material.HAY_BLOCK), new ItemStack(Material.HAY_BLOCK), null, new ItemStack(Material.HAY_BLOCK)})
                .register(plugin);

        new ExplosivePickaxe(itemGroups.tools, SlimefunItems.EXPLOSIVE_PICKAXE, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {new ItemStack(Material.TNT), SlimefunItems.SYNTHETIC_DIAMOND.item(), new ItemStack(Material.TNT), null, SlimefunItems.FERROSILICON.item(), null, null, SlimefunItems.FERROSILICON.item(), null})
                .register(plugin);

        new ExplosiveShovel(itemGroups.tools, SlimefunItems.EXPLOSIVE_SHOVEL, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.SYNTHETIC_DIAMOND.item(), null, null, new ItemStack(Material.TNT), null, null, SlimefunItems.FERROSILICON.item(), null})
                .register(plugin);

        new AutomatedPanningMachine(itemGroups.basicMachines, SlimefunItems.AUTOMATED_PANNING_MACHINE).register(plugin);

        new IndustrialMiner(itemGroups.basicMachines, SlimefunItems.INDUSTRIAL_MINER, Material.IRON_BLOCK, false, 3).register(plugin);
        new AdvancedIndustrialMiner(itemGroups.basicMachines, SlimefunItems.ADVANCED_INDUSTRIAL_MINER).register(plugin);

        new StomperBoots(itemGroups.magicalArmor, SlimefunItems.BOOTS_OF_THE_STOMPER, RecipeType.ARMOR_FORGE,
                new ItemStack[] {null, null, null, new ItemStack(Material.YELLOW_WOOL), null, new ItemStack(Material.YELLOW_WOOL), new ItemStack(Material.PISTON), null, new ItemStack(Material.PISTON)})
                .register(plugin);

        new PickaxeOfTheSeeker(itemGroups.tools, SlimefunItems.PICKAXE_OF_THE_SEEKER, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {new ItemStack(Material.COMPASS), SlimefunItems.SYNTHETIC_DIAMOND.item(), new ItemStack(Material.COMPASS), null, SlimefunItems.FERROSILICON.item(), null, null, SlimefunItems.FERROSILICON.item(), null})
                .register(plugin);

        new SlimefunBackpack(9, itemGroups.usefulItems, SlimefunItems.BACKPACK_SMALL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.LEATHER), null, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_6K.item(), new ItemStack(Material.CHEST), SlimefunItems.GOLD_6K.item(), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER)})
                .register(plugin);

        new SlimefunBackpack(18, itemGroups.usefulItems, SlimefunItems.BACKPACK_MEDIUM, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.LEATHER), null, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_10K.item(), SlimefunItems.BACKPACK_SMALL.item(), SlimefunItems.GOLD_10K.item(), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER)})
                .register(plugin);

        new SlimefunBackpack(27, itemGroups.usefulItems, SlimefunItems.BACKPACK_LARGE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.LEATHER), null, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_14K.item(), SlimefunItems.BACKPACK_MEDIUM.item(), SlimefunItems.GOLD_14K.item(), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER)})
                .register(plugin);

        new SlimefunBackpack(36, itemGroups.usefulItems, SlimefunItems.WOVEN_BACKPACK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CLOTH.item(), null, SlimefunItems.CLOTH.item(), SlimefunItems.GOLD_16K.item(), SlimefunItems.BACKPACK_LARGE.item(), SlimefunItems.GOLD_16K.item(), SlimefunItems.CLOTH.item(), SlimefunItems.CLOTH.item(), SlimefunItems.CLOTH.item()})
                .register(plugin);

        new Crucible(itemGroups.basicMachines, SlimefunItems.CRUCIBLE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.TERRACOTTA), null, new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), null, new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), new ItemStack(Material.FLINT_AND_STEEL), new ItemStack(Material.TERRACOTTA)})
                .register(plugin);

        new SlimefunBackpack(45, itemGroups.usefulItems, SlimefunItems.GILDED_BACKPACK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.GOLD_22K.item(), null, SlimefunItems.GOLD_22K.item(), new ItemStack(Material.LEATHER), SlimefunItems.WOVEN_BACKPACK.item(), new ItemStack(Material.LEATHER), SlimefunItems.GOLD_22K.item(), null, SlimefunItems.GOLD_22K.item()})
                .register(plugin);

        new SlimefunBackpack(54, itemGroups.usefulItems, SlimefunItems.RADIANT_BACKPACK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.GOLD_24K.item(), null, SlimefunItems.GOLD_24K.item(), new ItemStack(Material.LEATHER), SlimefunItems.GILDED_BACKPACK.item(), new ItemStack(Material.LEATHER), SlimefunItems.GOLD_24K.item(), null, SlimefunItems.GOLD_24K.item()})
                .register(plugin);

        new RestoredBackpack(itemGroups.usefulItems).register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.MAGNET, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.NICKEL_INGOT.item(), SlimefunItems.ALUMINUM_DUST.item(), SlimefunItems.IRON_DUST.item(), SlimefunItems.COBALT_INGOT.item(), null, null, null, null, null})
                .register(plugin);

        new InfusedMagnet(itemGroups.magicalGadgets, SlimefunItems.INFUSED_MAGNET, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.ENDER_LUMP_2.item(), SlimefunItems.MAGNET.item(), SlimefunItems.ENDER_LUMP_2.item(), SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()})
                .register(plugin);

        new SlimefunItem(itemGroups.tools, SlimefunItems.COBALT_PICKAXE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.COBALT_INGOT.item(), SlimefunItems.COBALT_INGOT.item(), SlimefunItems.COBALT_INGOT.item(), null, SlimefunItems.NICKEL_INGOT.item(), null, null, SlimefunItems.NICKEL_INGOT.item(), null})
                .register(plugin);

        new UnplaceableBlock(itemGroups.magicalResources, SlimefunItems.NECROTIC_SKULL, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item(), null, new ItemStack(Material.WITHER_SKELETON_SKULL), null, SlimefunItems.MAGIC_LUMP_3.item(), null, SlimefunItems.MAGIC_LUMP_3.item()})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.ESSENCE_OF_AFTERLIFE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.AIR_RUNE.item(), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.EARTH_RUNE.item(), SlimefunItems.NECROTIC_SKULL.item(), SlimefunItems.FIRE_RUNE.item(), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.WATER_RUNE.item(), SlimefunItems.ENDER_LUMP_3.item()})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.SYNTHETIC_SHULKER_SHELL, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.ENDER_RUNE.item(), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.REINFORCED_PLATE.item(), new ItemStack(Material.TURTLE_HELMET), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.ENDER_RUNE.item(), SlimefunItems.ENDER_LUMP_3.item()})
                .setUseableInWorkbench(true)
                .register(plugin);

        new SoulboundBackpack(36, itemGroups.magicalGadgets, SlimefunItems.BOUND_BACKPACK, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_2.item(), null, SlimefunItems.ENDER_LUMP_2.item(), SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), SlimefunItems.WOVEN_BACKPACK.item(), SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), SlimefunItems.ENDER_LUMP_2.item(), null, SlimefunItems.ENDER_LUMP_2.item()})
                .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.DURALUMIN_JETBOOTS,
                new ItemStack[] {null, null, null, SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.35, 20)
                .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.SOLDER_JETBOOTS,
                new ItemStack[] {null, null, null, SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.4, 30)
                .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.BILLON_JETBOOTS,
                new ItemStack[] {null, null, null, SlimefunItems.BILLON_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.BILLON_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.45, 40)
                .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.STEEL_JETBOOTS,
                new ItemStack[] {null, null, null, SlimefunItems.STEEL_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.5, 50)
                .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.DAMASCUS_STEEL_JETBOOTS,
                new ItemStack[] {null, null, null, SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.55, 75)
                .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.REINFORCED_ALLOY_JETBOOTS,
                new ItemStack[] {null, null, null, SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.6, 100)
                .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.CARBONADO_JETBOOTS,
                new ItemStack[] {null, null, null, SlimefunItems.CARBONADO.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.LARGE_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.7, 125)
                .register(plugin);

        new JetBoots(itemGroups.technicalGadgets, SlimefunItems.ARMORED_JETBOOTS,
                new ItemStack[] {null, null, null, SlimefunItems.STEEL_PLATE.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.STEEL_PLATE.item(), SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.STEEL_THRUSTER.item()},
                0.45, 50)
                .register(plugin);

        new SeismicAxe(itemGroups.weapons, SlimefunItems.SEISMIC_AXE, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), null, SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.STAFF_ELEMENTAL.item(), null, null, SlimefunItems.STAFF_ELEMENTAL.item(), null})
                .register(plugin);

        new PickaxeOfVeinMining(itemGroups.tools, SlimefunItems.PICKAXE_OF_VEIN_MINING, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {new ItemStack(Material.EMERALD_ORE), SlimefunItems.SYNTHETIC_DIAMOND.item(), new ItemStack(Material.EMERALD_ORE), null, SlimefunItems.GILDED_IRON.item(), null, null, SlimefunItems.GILDED_IRON.item(), null})
                .register(plugin);

        new ClimbingPick(itemGroups.tools, SlimefunItems.CLIMBING_PICK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.STEEL_INGOT.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.STEEL_INGOT.item(), null, new ItemStack(Material.STICK), null, null, new ItemStack(Material.STICK), null})
                .register(plugin);

        new SoulboundItem(itemGroups.weapons, SlimefunItems.SOULBOUND_SWORD, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.DIAMOND_SWORD), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new SoulboundItem(itemGroups.weapons, SlimefunItems.SOULBOUND_TRIDENT, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.TRIDENT), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new SoulboundItem(itemGroups.weapons, SlimefunItems.SOULBOUND_BOW, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.BOW), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new SoulboundItem(itemGroups.tools, SlimefunItems.SOULBOUND_PICKAXE, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.DIAMOND_PICKAXE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new SoulboundItem(itemGroups.tools, SlimefunItems.SOULBOUND_AXE, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.DIAMOND_AXE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new SoulboundItem(itemGroups.tools, SlimefunItems.SOULBOUND_SHOVEL, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.DIAMOND_SHOVEL), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new SoulboundItem(itemGroups.tools, SlimefunItems.SOULBOUND_HOE, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.DIAMOND_HOE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new SoulboundItem(itemGroups.magicalArmor, SlimefunItems.SOULBOUND_HELMET, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.DIAMOND_HELMET), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new SoulboundItem(itemGroups.magicalArmor, SlimefunItems.SOULBOUND_CHESTPLATE, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.DIAMOND_CHESTPLATE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new SoulboundItem(itemGroups.magicalArmor, SlimefunItems.SOULBOUND_LEGGINGS, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.DIAMOND_LEGGINGS), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new SoulboundItem(itemGroups.magicalArmor, SlimefunItems.SOULBOUND_BOOTS, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null, null, new ItemStack(Material.DIAMOND_BOOTS), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new Juicer(itemGroups.basicMachines, SlimefunItems.JUICER).register(plugin);

        new Juice(itemGroups.food, SlimefunItems.APPLE_JUICE, RecipeType.JUICER,
                new ItemStack[] {new ItemStack(Material.APPLE), null, null, null, null, null, null, null, null})
                .register(plugin);

        new Juice(itemGroups.food, SlimefunItems.CARROT_JUICE, RecipeType.JUICER,
                new ItemStack[] {new ItemStack(Material.CARROT), null, null, null, null, null, null, null, null})
                .register(plugin);

        new Juice(itemGroups.food, SlimefunItems.MELON_JUICE, RecipeType.JUICER,
                new ItemStack[] {new ItemStack(Material.MELON_SLICE), null, null, null, null, null, null, null, null})
                .register(plugin);

        new Juice(itemGroups.food, SlimefunItems.PUMPKIN_JUICE, RecipeType.JUICER,
                new ItemStack[] {new ItemStack(Material.PUMPKIN), null, null, null, null, null, null, null, null})
                .register(plugin);

        new Juice(itemGroups.food, SlimefunItems.SWEET_BERRY_JUICE, RecipeType.JUICER,
                new ItemStack[] {new ItemStack(Material.SWEET_BERRIES), null, null, null, null, null, null, null, null})
                .register(plugin);

        new Juice(itemGroups.food, SlimefunItems.GOLDEN_APPLE_JUICE, RecipeType.JUICER,
                new ItemStack[] {new ItemStack(Material.GOLDEN_APPLE), null, null, null, null, null, null, null, null})
                .register(plugin);

        new VanillaItem(itemGroups.food, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), "ENCHANTED_GOLDEN_APPLE", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.GOLD_24K_BLOCK.item(), SlimefunItems.GOLD_24K_BLOCK.item(), SlimefunItems.GOLD_24K_BLOCK.item(), SlimefunItems.GOLD_24K_BLOCK.item(), new ItemStack(Material.APPLE), SlimefunItems.GOLD_24K_BLOCK.item(), SlimefunItems.GOLD_24K_BLOCK.item(), SlimefunItems.GOLD_24K_BLOCK.item(), SlimefunItems.GOLD_24K_BLOCK.item()})
                .register(plugin);

        new BrokenSpawner(itemGroups.magicalResources, SlimefunItems.BROKEN_SPAWNER, new RecipeType(new NamespacedKey(plugin, "pickaxe_of_containment"), SlimefunItems.PICKAXE_OF_CONTAINMENT),
                new ItemStack[] {null, null, null, null, new ItemStack(Material.SPAWNER), null, null, null, null})
                .register(plugin);

        new RepairedSpawner(itemGroups.magicalGadgets, SlimefunItems.REPAIRED_SPAWNER, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.ENDER_RUNE.item(), SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE.item(), SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE.item(), SlimefunItems.BROKEN_SPAWNER.item(), SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE.item(), SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE.item(), SlimefunItems.ENDER_RUNE.item()})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 1, 1, 1, SlimefunItems.ENHANCED_FURNACE,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), new ItemStack(Material.FURNACE), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 2, 1, 1, SlimefunItems.ENHANCED_FURNACE_2,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.ENHANCED_FURNACE.item(), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 2, 2, 1, SlimefunItems.ENHANCED_FURNACE_3,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.ENHANCED_FURNACE_2.item(), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 3, 2, 1, SlimefunItems.ENHANCED_FURNACE_4,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.ENHANCED_FURNACE_3.item(), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 3, 2, 2, SlimefunItems.ENHANCED_FURNACE_5,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.ENHANCED_FURNACE_4.item(), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 3, 3, 2, SlimefunItems.ENHANCED_FURNACE_6,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.ENHANCED_FURNACE_5.item(), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 4, 3, 2, SlimefunItems.ENHANCED_FURNACE_7,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.ENHANCED_FURNACE_6.item(), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 4, 4, 2, SlimefunItems.ENHANCED_FURNACE_8,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.ENHANCED_FURNACE_7.item(), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 5, 4, 2, SlimefunItems.ENHANCED_FURNACE_9,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.ENHANCED_FURNACE_8.item(), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 5, 5, 2, SlimefunItems.ENHANCED_FURNACE_10,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.ENHANCED_FURNACE_9.item(), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 5, 5, 3, SlimefunItems.ENHANCED_FURNACE_11,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.ENHANCED_FURNACE_10.item(), SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 10, 10, 3, SlimefunItems.REINFORCED_FURNACE,
                new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.ENHANCED_FURNACE_11.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item()})
                .register(plugin);

        new EnhancedFurnace(itemGroups.basicMachines, 20, 10, 3, SlimefunItems.CARBONADO_EDGED_FURNACE,
                new ItemStack[] {SlimefunItems.CARBONADO.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.REINFORCED_FURNACE.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.CARBONADO.item()})
                .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.ELECTRO_MAGNET, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.NICKEL_INGOT.item(), SlimefunItems.MAGNET.item(), SlimefunItems.COBALT_INGOT.item(), null, SlimefunItems.BATTERY.item(), null, null, null, null})
                .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.ELECTRIC_MOTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.COPPER_WIRE.item(), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.COPPER_WIRE.item(), null, SlimefunItems.ELECTRO_MAGNET.item(), null, SlimefunItems.COPPER_WIRE.item(), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.COPPER_WIRE.item()})
                .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.HEATING_COIL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.COPPER_WIRE.item(), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.COPPER_WIRE.item()})
                .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.COPPER_WIRE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, SlimefunItems.COPPER_INGOT.item(), SlimefunItems.COPPER_INGOT.item(), SlimefunItems.COPPER_INGOT.item(), null, null, null},
                new SlimefunItemStack(SlimefunItems.COPPER_WIRE, 8).item())
                .register(plugin);

        new BlockPlacer(itemGroups.basicMachines, SlimefunItems.BLOCK_PLACER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.GOLD_4K.item(), new ItemStack(Material.PISTON), SlimefunItems.GOLD_4K.item(), new ItemStack(Material.IRON_INGOT), SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.IRON_INGOT), SlimefunItems.GOLD_4K.item(), new ItemStack(Material.PISTON), SlimefunItems.GOLD_4K.item()})
                .register(plugin);

        new TelepositionScroll(itemGroups.magicalGadgets, SlimefunItems.SCROLL_OF_DIMENSIONAL_TELEPOSITION, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.MAGIC_EYE_OF_ENDER.item(), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.MAGICAL_BOOK_COVER.item(), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.MAGIC_EYE_OF_ENDER.item(), SlimefunItems.ENDER_LUMP_3.item(), null})
                .register(plugin);

        new ExplosiveBow(itemGroups.weapons, SlimefunItems.EXPLOSIVE_BOW,
                new ItemStack[] {null, new ItemStack(Material.STICK), new ItemStack(Material.GUNPOWDER), SlimefunItems.STAFF_FIRE.item(), null, SlimefunItems.SULFATE.item(), null, new ItemStack(Material.STICK), new ItemStack(Material.GUNPOWDER)})
                .register(plugin);

        new IcyBow(itemGroups.weapons, SlimefunItems.ICY_BOW,
                new ItemStack[] {null, new ItemStack(Material.STICK), new ItemStack(Material.ICE), SlimefunItems.STAFF_WATER.item(), null, new ItemStack(Material.PACKED_ICE), null, new ItemStack(Material.STICK), new ItemStack(Material.ICE)})
                .register(plugin);

        new KnowledgeTome(itemGroups.magicalGadgets, SlimefunItems.TOME_OF_KNOWLEDGE_SHARING, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, new ItemStack(Material.FEATHER), null, new ItemStack(Material.INK_SAC), SlimefunItems.MAGICAL_BOOK_COVER.item(), new ItemStack(Material.GLASS_BOTTLE), null, new ItemStack(Material.WRITABLE_BOOK), null})
                .register(plugin);

        new KnowledgeFlask(itemGroups.magicalGadgets, SlimefunItems.FLASK_OF_KNOWLEDGE, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, null, null, SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.GLASS_PANE), SlimefunItems.MAGIC_LUMP_2.item(), null, SlimefunItems.MAGIC_LUMP_2.item(), null},
                new SlimefunItemStack(SlimefunItems.FLASK_OF_KNOWLEDGE, 8).item())
                .register(plugin);

        new HiddenItem(itemGroups.magicalGadgets, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, RecipeType.INTERACT,
                new ItemStack[] {SlimefunItems.FLASK_OF_KNOWLEDGE.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new BirthdayCake(itemGroups.birthday, new SlimefunItemStack("BIRTHDAY_CAKE", Material.CAKE, "&bBirthday Cake"), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.TORCH), null, new ItemStack(Material.SUGAR), new ItemStack(Material.CAKE), new ItemStack(Material.SUGAR), null, null, null})
                .register(plugin);

        new Juice(itemGroups.christmas, SlimefunItems.CHRISTMAS_MILK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.GLASS_BOTTLE), null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_MILK, 4).item())
                .register(plugin);

        new Juice(itemGroups.christmas, SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CHRISTMAS_MILK.item(), new ItemStack(Material.COCOA_BEANS), null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, 2).item())
                .register(plugin);

        new Juice(itemGroups.christmas, SlimefunItems.CHRISTMAS_EGG_NOG, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CHRISTMAS_MILK.item(), new ItemStack(Material.EGG), null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_EGG_NOG, 2).item())
                .register(plugin);

        new Juice(itemGroups.christmas, SlimefunItems.CHRISTMAS_APPLE_CIDER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.APPLE_JUICE.item(), new ItemStack(Material.SUGAR), null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_APPLE_CIDER, 2).item())
                .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_COOKIE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.COOKIE), new ItemStack(Material.SUGAR), new ItemStack(Material.LIME_DYE), null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_COOKIE, 16).item())
                .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_FRUIT_CAKE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.EGG), new ItemStack(Material.APPLE), new ItemStack(Material.MELON), new ItemStack(Material.SUGAR), null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_FRUIT_CAKE, 4).item())
                .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_APPLE_PIE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.APPLE), new ItemStack(Material.EGG), null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_APPLE_PIE, 2).item())
                .register(plugin);

        new Juice(itemGroups.christmas, SlimefunItems.CHRISTMAS_HOT_CHOCOLATE, RecipeType.SMELTERY,
                new ItemStack[] {SlimefunItems.CHRISTMAS_CHOCOLATE_MILK.item(), null, null, null, null, null, null, null, null}, SlimefunItems.CHRISTMAS_HOT_CHOCOLATE.item())
                .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_CAKE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR.item(), new ItemStack(Material.MILK_BUCKET), null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_CAKE, 4).item())
                .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_CARAMEL, RecipeType.SMELTERY,
                new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.SUGAR), null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_CARAMEL, 4).item())
                .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_CARAMEL_APPLE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.CHRISTMAS_CARAMEL.item(), null, null, new ItemStack(Material.APPLE), null, null, new ItemStack(Material.STICK), null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_CARAMEL_APPLE, 2).item())
                .register(plugin);

        new SlimefunItem(itemGroups.christmas, SlimefunItems.CHRISTMAS_CHOCOLATE_APPLE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.COCOA_BEANS), null, null, new ItemStack(Material.APPLE), null, null, new ItemStack(Material.STICK), null},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_CHOCOLATE_APPLE, 2).item())
                .register(plugin);

        new ChristmasPresent(itemGroups.christmas, SlimefunItems.CHRISTMAS_PRESENT, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, new ItemStack(Material.NAME_TAG), null, new ItemStack(Material.RED_WOOL), new ItemStack(Material.GREEN_WOOL), new ItemStack(Material.RED_WOOL), new ItemStack(Material.RED_WOOL), new ItemStack(Material.GREEN_WOOL), new ItemStack(Material.RED_WOOL)},
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_HOT_CHOCOLATE, 1).item(),
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_CHOCOLATE_APPLE, 4).item(),
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_CARAMEL_APPLE, 4).item(),
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_CAKE, 4).item(),
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_COOKIE, 8).item(),
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_PRESENT, 1).item(),
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_EGG_NOG, 1).item(),
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_MILK, 1).item(),
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_APPLE_CIDER, 1).item(),
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_FRUIT_CAKE, 4).item(),
                new SlimefunItemStack(SlimefunItems.CHRISTMAS_APPLE_PIE, 4).item(),
                new ItemStack(Material.EMERALD)
        ).register(plugin);

        new SlimefunItem(itemGroups.easter, SlimefunItems.EASTER_CARROT_PIE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.CARROT), new ItemStack(Material.EGG), null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.EASTER_CARROT_PIE, 2).item())
                .register(plugin);

        new SlimefunItem(itemGroups.easter, SlimefunItems.EASTER_APPLE_PIE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.APPLE), new ItemStack(Material.EGG), null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.EASTER_APPLE_PIE, 2).item())
                .register(plugin);

        new EasterEgg(itemGroups.easter, SlimefunItems.EASTER_EGG, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.LIME_DYE), new ItemStack(Material.EGG), new ItemStack(Material.PURPLE_DYE), null, null, null},
                new SlimefunItemStack(SlimefunItems.EASTER_EGG, 2).item(),
                // Gifts:
                new SlimefunItemStack(SlimefunItems.EASTER_CARROT_PIE, 4).item(),
                new SlimefunItemStack(SlimefunItems.CARROT_JUICE, 1).item(),
                new ItemStack(Material.EMERALD),
                new ItemStack(Material.CAKE),
                new ItemStack(Material.RABBIT_FOOT),
                new ItemStack(Material.GOLDEN_CARROT, 4)
        ).register(plugin);

        itemGroups.rickFlexGroup.register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.REINFORCED_PLATE, RecipeType.COMPRESSOR,
                new ItemStack[] {new SlimefunItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 8).item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new HardenedGlass(itemGroups.technicalComponents, SlimefunItems.HARDENED_GLASS, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), SlimefunItems.REINFORCED_PLATE.item(), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS)},
                new SlimefunItemStack(SlimefunItems.HARDENED_GLASS, 16).item())
                .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.COOLING_UNIT, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.ICE), new ItemStack(Material.ICE), new ItemStack(Material.ICE), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.ALUMINUM_INGOT.item(), new ItemStack(Material.ICE), new ItemStack(Material.ICE), new ItemStack(Material.ICE)})
                .register(plugin);

        new Cooler(27, itemGroups.usefulItems, SlimefunItems.COOLER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CLOTH.item(), SlimefunItems.CLOTH.item(), SlimefunItems.CLOTH.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.COOLING_UNIT.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.ALUMINUM_INGOT.item()})
                .register(plugin);

        new WitherProofBlock(itemGroups.technicalComponents, SlimefunItems.WITHER_PROOF_OBSIDIAN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.LEAD_INGOT.item(), new ItemStack(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT.item(), new ItemStack(Material.OBSIDIAN), SlimefunItems.HARDENED_GLASS.item(), new ItemStack(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT.item(), new ItemStack(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT.item()},
                new SlimefunItemStack(SlimefunItems.WITHER_PROOF_OBSIDIAN, 4).item())
                .register(plugin);

        new AncientPedestal(itemGroups.magicalGadgets, SlimefunItems.ANCIENT_PEDESTAL, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K.item(), new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.STONE), null, new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K.item(), new ItemStack(Material.OBSIDIAN)},
                new SlimefunItemStack(SlimefunItems.ANCIENT_PEDESTAL, 4).item())
                .register(plugin);

        new AncientAltar(itemGroups.magicalGadgets, SlimefunItems.ANCIENT_ALTAR, RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {null, new ItemStack(Material.ENCHANTING_TABLE), null, SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.GOLD_8K.item(), SlimefunItems.MAGIC_LUMP_3.item(), new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K.item(), new ItemStack(Material.OBSIDIAN)})
                .register(plugin);

        new EnergyRegulator(itemGroups.electricity, SlimefunItems.ENERGY_REGULATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SILVER_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.SILVER_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.SILVER_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.SILVER_INGOT.item()})
                .register(plugin);

        new EnergyConnector(itemGroups.electricity, SlimefunItems.ENERGY_CONNECTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CARBON.item(), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.CARBON.item(), SlimefunItems.COPPER_WIRE.item(), new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.CARBON.item(), SlimefunItems.COPPER_WIRE.item(), SlimefunItems.CARBON.item()},
                new SlimefunItemStack(SlimefunItems.ENERGY_CONNECTOR, 8).item())
                .register(plugin);

        new SlimefunItem(itemGroups.misc, SlimefunItems.DUCT_TAPE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.ALUMINUM_DUST.item(), SlimefunItems.ALUMINUM_DUST.item(), SlimefunItems.ALUMINUM_DUST.item(), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.PAPER), new ItemStack(Material.PAPER), new ItemStack(Material.PAPER)},
                new SlimefunItemStack(SlimefunItems.DUCT_TAPE, 2).item())
                .register(plugin);

        new Capacitor(itemGroups.electricity, 128, SlimefunItems.SMALL_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.SULFATE.item(), SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.ENERGY_CONNECTOR.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.DURALUMIN_INGOT.item(), new ItemStack(Material.REDSTONE), SlimefunItems.DURALUMIN_INGOT.item()})
                .register(plugin);

        new Capacitor(itemGroups.electricity, 512, SlimefunItems.MEDIUM_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.BILLON_INGOT.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.BILLON_INGOT.item(), new ItemStack(Material.REDSTONE), SlimefunItems.SMALL_CAPACITOR.item(), new ItemStack(Material.REDSTONE), SlimefunItems.BILLON_INGOT.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.BILLON_INGOT.item()})
                .register(plugin);

        new Capacitor(itemGroups.electricity, 1024, SlimefunItems.BIG_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.STEEL_INGOT.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.STEEL_INGOT.item(), new ItemStack(Material.REDSTONE), SlimefunItems.MEDIUM_CAPACITOR.item(), new ItemStack(Material.REDSTONE), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.STEEL_INGOT.item()})
                .register(plugin);

        new Capacitor(itemGroups.electricity, 8192, SlimefunItems.LARGE_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), new ItemStack(Material.REDSTONE), SlimefunItems.BIG_CAPACITOR.item(), new ItemStack(Material.REDSTONE), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item()})
                .register(plugin);

        new Capacitor(itemGroups.electricity, 65536, SlimefunItems.CARBONADO_EDGED_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CARBONADO.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.CARBONADO.item(), new ItemStack(Material.REDSTONE), SlimefunItems.LARGE_CAPACITOR.item(), new ItemStack(Material.REDSTONE), SlimefunItems.CARBONADO.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.CARBONADO.item()})
                .register(plugin);

        new Capacitor(itemGroups.electricity, 524288, SlimefunItems.ENERGIZED_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CARBONADO.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.CARBONADO.item(), new ItemStack(Material.NETHER_STAR), SlimefunItems.CARBONADO_EDGED_CAPACITOR.item(), new ItemStack(Material.NETHER_STAR), SlimefunItems.CARBONADO.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.CARBONADO.item()})
                .register(plugin);

        new SolarGenerator(itemGroups.electricity, 2, 0, SlimefunItems.SOLAR_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SOLAR_PANEL.item(), SlimefunItems.SOLAR_PANEL.item(), SlimefunItems.SOLAR_PANEL.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.ALUMINUM_INGOT.item(), null, SlimefunItems.ALUMINUM_INGOT.item(), null})
                .register(plugin);

        new SolarGenerator(itemGroups.electricity, 8, 0, SlimefunItems.SOLAR_GENERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SOLAR_GENERATOR.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.SOLAR_GENERATOR.item(), SlimefunItems.ALUMINUM_INGOT.item(), new ItemStack(Material.REDSTONE), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.SOLAR_GENERATOR.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.SOLAR_GENERATOR.item()})
                .register(plugin);

        new SolarGenerator(itemGroups.electricity, 32, 0, SlimefunItems.SOLAR_GENERATOR_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SOLAR_GENERATOR_2.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.SOLAR_GENERATOR_2.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.SOLAR_GENERATOR_2.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.SOLAR_GENERATOR_2.item()})
                .register(plugin);

        new SolarGenerator(itemGroups.electricity, 128, 64, SlimefunItems.SOLAR_GENERATOR_4, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SOLAR_GENERATOR_3.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.SOLAR_GENERATOR_3.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.SOLAR_GENERATOR_3.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.SOLAR_GENERATOR_3.item()})
                .register(plugin);

        new ChargingBench(itemGroups.electricity, SlimefunItems.CHARGING_BENCH, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET.item(), null, SlimefunItems.BATTERY.item(), new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.BATTERY.item(), null, SlimefunItems.SMALL_CAPACITOR.item(), null})
                .setCapacity(128)
                .setEnergyConsumption(10)
                .setProcessingSpeed(1)
                .register(plugin);

        new ElectricFurnace(itemGroups.electricity, SlimefunItems.ELECTRIC_FURNACE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.FURNACE), null, SlimefunItems.GILDED_IRON.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.GILDED_IRON.item()})
                .setCapacity(64)
                .setEnergyConsumption(2)
                .setProcessingSpeed(1)
                .register(plugin);

        new ElectricFurnace(itemGroups.electricity, SlimefunItems.ELECTRIC_FURNACE_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.ELECTRIC_MOTOR.item(), null, SlimefunItems.GILDED_IRON.item(), SlimefunItems.ELECTRIC_FURNACE.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.GILDED_IRON.item()})
                .setCapacity(128)
                .setEnergyConsumption(3)
                .setProcessingSpeed(2)
                .register(plugin);

        new ElectricFurnace(itemGroups.electricity, SlimefunItems.ELECTRIC_FURNACE_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.ELECTRIC_MOTOR.item(), null, SlimefunItems.STEEL_INGOT.item(), SlimefunItems.ELECTRIC_FURNACE_2.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.GILDED_IRON.item()})
                .setCapacity(128)
                .setEnergyConsumption(5)
                .setProcessingSpeed(4)
                .register(plugin);

        new ElectricGoldPan(itemGroups.electricity, SlimefunItems.ELECTRIC_GOLD_PAN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GOLD_PAN.item(), null, new ItemStack(Material.FLINT), SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.FLINT), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.ALUMINUM_INGOT.item()})
                .setCapacity(128)
                .setEnergyConsumption(1)
                .setProcessingSpeed(1)
                .register(plugin);

        new ElectricGoldPan(itemGroups.electricity, SlimefunItems.ELECTRIC_GOLD_PAN_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GOLD_PAN.item(), null, new ItemStack(Material.IRON_INGOT), SlimefunItems.ELECTRIC_GOLD_PAN.item(), new ItemStack(Material.IRON_INGOT), SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.DURALUMIN_INGOT.item()})
                .setCapacity(128)
                .setEnergyConsumption(2)
                .setProcessingSpeed(3)
                .register(plugin);

        new ElectricGoldPan(itemGroups.electricity, SlimefunItems.ELECTRIC_GOLD_PAN_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GOLD_PAN.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.ELECTRIC_GOLD_PAN_2.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.COBALT_INGOT.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.COBALT_INGOT.item()})
                .setCapacity(512)
                .setEnergyConsumption(7)
                .setProcessingSpeed(10)
                .register(plugin);

        new ElectricDustWasher(itemGroups.electricity, SlimefunItems.ELECTRIC_DUST_WASHER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.WATER_BUCKET), null, SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.ELECTRIC_GOLD_PAN.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.COPPER_INGOT.item(), SlimefunItems.COPPER_INGOT.item(), SlimefunItems.COPPER_INGOT.item()})
                .setCapacity(128)
                .setEnergyConsumption(3)
                .setProcessingSpeed(1)
                .register(plugin);

        new ElectricDustWasher(itemGroups.electricity, SlimefunItems.ELECTRIC_DUST_WASHER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.WATER_BUCKET), null, SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.ELECTRIC_DUST_WASHER.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item()})
                .setCapacity(128)
                .setEnergyConsumption(5)
                .setProcessingSpeed(2)
                .register(plugin);

        new ElectricDustWasher(itemGroups.electricity, SlimefunItems.ELECTRIC_DUST_WASHER_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.WATER_BUCKET), null, SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.ELECTRIC_DUST_WASHER_2.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item()})
                .setCapacity(512)
                .setEnergyConsumption(15)
                .setProcessingSpeed(10)
                .register(plugin);

        new ElectricIngotFactory(itemGroups.electricity, SlimefunItems.ELECTRIC_INGOT_FACTORY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.FLINT_AND_STEEL), null, SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_DUST_WASHER.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item()})
                .setCapacity(256)
                .setEnergyConsumption(4)
                .setProcessingSpeed(1)
                .register(plugin);

        new ElectricIngotFactory(itemGroups.electricity, SlimefunItems.ELECTRIC_INGOT_FACTORY_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.GILDED_IRON.item(), new ItemStack(Material.FLINT_AND_STEEL), SlimefunItems.GILDED_IRON.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_INGOT_FACTORY.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.BRASS_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.BRASS_INGOT.item()})
                .setCapacity(256)
                .setEnergyConsumption(7)
                .setProcessingSpeed(2)
                .register(plugin);

        new ElectricIngotFactory(itemGroups.electricity, SlimefunItems.ELECTRIC_INGOT_FACTORY_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.GILDED_IRON.item(), new ItemStack(Material.FLINT_AND_STEEL), SlimefunItems.GILDED_IRON.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_INGOT_FACTORY_2.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.BRASS_INGOT.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.BRASS_INGOT.item()})
                .setCapacity(512)
                .setEnergyConsumption(20)
                .setProcessingSpeed(8)
                .register(plugin);

        new ElectrifiedCrucible(itemGroups.electricity, SlimefunItems.ELECTRIFIED_CRUCIBLE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.LEAD_INGOT.item(), SlimefunItems.CRUCIBLE.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.LARGE_CAPACITOR.item(), SlimefunItems.LEAD_INGOT.item()})
                .setCapacity(1024)
                .setEnergyConsumption(24)
                .setProcessingSpeed(1)
                .register(plugin);

        new ElectrifiedCrucible(itemGroups.electricity, SlimefunItems.ELECTRIFIED_CRUCIBLE_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.ELECTRIFIED_CRUCIBLE.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.LEAD_INGOT.item()})
                .setCapacity(1024)
                .setEnergyConsumption(40)
                .setProcessingSpeed(2)
                .register(plugin);

        new ElectrifiedCrucible(itemGroups.electricity, SlimefunItems.ELECTRIFIED_CRUCIBLE_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.ELECTRIFIED_CRUCIBLE_2.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.STEEL_PLATE.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.STEEL_PLATE.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.LEAD_INGOT.item()})
                .setCapacity(1024)
                .setEnergyConsumption(60)
                .setProcessingSpeed(4)
                .register(plugin);

        new ElectricOreGrinder(itemGroups.electricity, SlimefunItems.ELECTRIC_ORE_GRINDER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.DIAMOND_PICKAXE), null, SlimefunItems.GILDED_IRON.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.GILDED_IRON.item()})
                .setCapacity(128)
                .setEnergyConsumption(6)
                .setProcessingSpeed(1)
                .register(plugin);

        new ElectricOreGrinder(itemGroups.electricity, SlimefunItems.ELECTRIC_ORE_GRINDER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.DIAMOND_PICKAXE), null, SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_ORE_GRINDER.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.GILDED_IRON.item()})
                .setCapacity(512)
                .setEnergyConsumption(15)
                .setProcessingSpeed(4)
                .register(plugin);

        new ElectricOreGrinder(itemGroups.electricity, SlimefunItems.ELECTRIC_ORE_GRINDER_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.REINFORCED_PLATE.item(), null, SlimefunItems.ELECTRIC_ORE_GRINDER_2.item(), null, SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.REINFORCED_PLATE.item()})
                .setCapacity(1024)
                .setEnergyConsumption(45)
                .setProcessingSpeed(10)
                .register(plugin);

        new HeatedPressureChamber(itemGroups.electricity, SlimefunItems.HEATED_PRESSURE_CHAMBER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.LEAD_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.LEAD_INGOT.item(), new ItemStack(Material.GLASS), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.LEAD_INGOT.item()})
                .setCapacity(128)
                .setEnergyConsumption(5)
                .setProcessingSpeed(1)
                .register(plugin);

        new HeatedPressureChamber(itemGroups.electricity, SlimefunItems.HEATED_PRESSURE_CHAMBER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.LEAD_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.HEATED_PRESSURE_CHAMBER.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item()})
                .setCapacity(256)
                .setEnergyConsumption(22)
                .setProcessingSpeed(5)
                .register(plugin);

        new ElectricIngotPulverizer(itemGroups.electricity, SlimefunItems.ELECTRIC_INGOT_PULVERIZER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.ELECTRIC_ORE_GRINDER.item(), null, SlimefunItems.LEAD_INGOT.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.LEAD_INGOT.item()})
                .setCapacity(512)
                .setEnergyConsumption(7)
                .setProcessingSpeed(1)
                .register(plugin);

        new CoalGenerator(itemGroups.electricity, SlimefunItems.COAL_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.HEATING_COIL.item(), new ItemStack(Material.FURNACE), SlimefunItems.HEATING_COIL.item(), SlimefunItems.NICKEL_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.NICKEL_INGOT.item(), null, SlimefunItems.NICKEL_INGOT.item(), null})
                .setCapacity(64)
                .setEnergyProduction(8)
                .register(plugin);

        new CoalGenerator(itemGroups.electricity, SlimefunItems.COAL_GENERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.MAGMA_BLOCK), SlimefunItems.HEATING_COIL.item(), new ItemStack(Material.MAGMA_BLOCK), SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.COAL_GENERATOR.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .setCapacity(256)
                .setEnergyProduction(15)
                .register(plugin);

        new BioGenerator(itemGroups.electricity, SlimefunItems.BIO_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.HEATING_COIL.item(), SlimefunItems.COMPOSTER.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.ALUMINUM_BRASS_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.ALUMINUM_BRASS_INGOT.item(), null, SlimefunItems.ALUMINUM_BRASS_INGOT.item(), null})
                .setCapacity(128)
                .setEnergyProduction(4)
                .register(plugin);

        new AutoDrier(itemGroups.electricity, SlimefunItems.AUTO_DRIER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, null, null, SlimefunItems.HEATING_COIL.item(), new ItemStack(Material.SMOKER), SlimefunItems.HEATING_COIL.item(), null, new ItemStack(Material.CAMPFIRE), null})
                .setCapacity(128)
                .setEnergyConsumption(5)
                .setProcessingSpeed(1)
                .register(plugin);

        new AutoBrewer(itemGroups.electricity, SlimefunItems.AUTO_BREWER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.HEATING_COIL.item(), null, SlimefunItems.REINFORCED_PLATE.item(), new ItemStack(Material.BREWING_STAND), SlimefunItems.REINFORCED_PLATE.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .setCapacity(128)
                .setEnergyConsumption(6)
                .setProcessingSpeed(1)
                .register(plugin);

        new ElectricPress(itemGroups.electricity, SlimefunItems.ELECTRIC_PRESS, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.PISTON), null, SlimefunItems.MEDIUM_CAPACITOR.item(), null, SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item()})
                .setCapacity(256)
                .setEnergyConsumption(8)
                .setProcessingSpeed(1)
                .register(plugin);

        new ElectricPress(itemGroups.electricity, SlimefunItems.ELECTRIC_PRESS_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.STICKY_PISTON), SlimefunItems.ELECTRIC_PRESS.item(), new ItemStack(Material.STICKY_PISTON), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.BIG_CAPACITOR.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item()})
                .setCapacity(1024)
                .setEnergyConsumption(20)
                .setProcessingSpeed(3)
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.MAGNESIUM_SALT, RecipeType.HEATED_PRESSURE_CHAMBER,
                new ItemStack[] {SlimefunItems.MAGNESIUM_DUST.item(), SlimefunItems.SALT.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new MagnesiumGenerator(itemGroups.electricity, SlimefunItems.MAGNESIUM_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.ELECTRIC_MOTOR.item(), null, SlimefunItems.COMPRESSED_CARBON.item(), new ItemStack(Material.WATER_BUCKET), SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.DURALUMIN_INGOT.item(), SlimefunItems.DURALUMIN_INGOT.item()})
                .setCapacity(128)
                .setEnergyProduction(18)
                .register(plugin);

        new AutoEnchanter(itemGroups.electricity, SlimefunItems.AUTO_ENCHANTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.ENCHANTING_TABLE), null, SlimefunItems.CARBONADO.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item()})
                .setCapacity(128)
                .setEnergyConsumption(9)
                .setProcessingSpeed(1)
                .register(plugin);

        new AutoEnchanter(itemGroups.electricity, SlimefunItems.AUTO_ENCHANTER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.BIG_CAPACITOR.item(), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.AUTO_ENCHANTER.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.SYNTHETIC_DIAMOND.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item()})
                .setCapacity(1024)
                .setEnergyConsumption(24)
                .setProcessingSpeed(3)
                .register(plugin);

        new AutoDisenchanter(itemGroups.electricity, SlimefunItems.AUTO_DISENCHANTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.REDSTONE), new ItemStack(Material.ANVIL), new ItemStack(Material.REDSTONE), SlimefunItems.CARBONADO.item(), SlimefunItems.AUTO_ENCHANTER.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item()})
                .setCapacity(128)
                .setEnergyConsumption(9)
                .setProcessingSpeed(1)
                .register(plugin);

        new AutoDisenchanter(itemGroups.electricity, SlimefunItems.AUTO_DISENCHANTER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_PLATE.item(), new ItemStack(Material.ANVIL), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.AUTO_DISENCHANTER.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.BIG_CAPACITOR.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item()})
                .setCapacity(1024)
                .setEnergyConsumption(24)
                .setProcessingSpeed(3)
                .register(plugin);

        new AutoAnvil(itemGroups.electricity, 10, SlimefunItems.AUTO_ANVIL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.ANVIL), null, SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK)})
                .setCapacity(128)
                .setEnergyConsumption(12)
                .setProcessingSpeed(1)
                .register(plugin);

        new AutoAnvil(itemGroups.electricity, 25, SlimefunItems.AUTO_ANVIL_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.AUTO_ANVIL.item(), null, SlimefunItems.STEEL_PLATE.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.STEEL_PLATE.item(), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK)})
                .setCapacity(256)
                .setEnergyConsumption(16)
                .setProcessingSpeed(1)
                .register(plugin);

        new BookBinder(itemGroups.electricity, SlimefunItems.BOOK_BINDER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.ENCHANTING_TABLE), null, new ItemStack(Material.BOOKSHELF), SlimefunItems.HARDENED_METAL_INGOT.item(), new ItemStack(Material.BOOKSHELF), SlimefunItems.SYNTHETIC_SAPPHIRE.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.SYNTHETIC_SAPPHIRE.item()})
                .setCapacity(256)
                .setEnergyConsumption(16)
                .setProcessingSpeed(1)
                .register(plugin);

        new Multimeter(itemGroups.technicalGadgets, SlimefunItems.MULTIMETER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.COPPER_INGOT.item(), null, SlimefunItems.COPPER_INGOT.item(), null, SlimefunItems.REDSTONE_ALLOY.item(), null, null, SlimefunItems.GOLD_6K.item(), null})
                .register(plugin);

        new SlimefunItem(itemGroups.technicalComponents, SlimefunItems.PLASTIC_SHEET, RecipeType.HEATED_PRESSURE_CHAMBER,
                new ItemStack[] {null, null, null, null, SlimefunItems.OIL_BUCKET.item(), null, null, null, null},
                new SlimefunItemStack(SlimefunItems.PLASTIC_SHEET, 8).item())
                .register(plugin);

        new UnplaceableBlock(itemGroups.technicalComponents, SlimefunItems.ANDROID_MEMORY_CORE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.BRASS_INGOT.item(), new ItemStack(Material.ORANGE_STAINED_GLASS), SlimefunItems.BRASS_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.TIN_DUST.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.BRASS_INGOT.item(), new ItemStack(Material.ORANGE_STAINED_GLASS), SlimefunItems.BRASS_INGOT.item()})
                .register(plugin);

        new GPSTransmitter(itemGroups.gps, 1, SlimefunItems.GPS_TRANSMITTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.STEEL_INGOT.item()}) {

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
                new ItemStack[] {SlimefunItems.GPS_TRANSMITTER.item(), SlimefunItems.BRONZE_INGOT.item(), SlimefunItems.GPS_TRANSMITTER.item(), SlimefunItems.BRONZE_INGOT.item(), SlimefunItems.CARBON.item(), SlimefunItems.BRONZE_INGOT.item(), SlimefunItems.GPS_TRANSMITTER.item(), SlimefunItems.BRONZE_INGOT.item(), SlimefunItems.GPS_TRANSMITTER.item()}) {

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
                new ItemStack[] {SlimefunItems.GPS_TRANSMITTER_2.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.GPS_TRANSMITTER_2.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.GPS_TRANSMITTER_2.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.GPS_TRANSMITTER_2.item()}) {

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
                new ItemStack[] {SlimefunItems.GPS_TRANSMITTER_3.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.GPS_TRANSMITTER_3.item(), SlimefunItems.NICKEL_INGOT.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.NICKEL_INGOT.item(), SlimefunItems.GPS_TRANSMITTER_3.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.GPS_TRANSMITTER_3.item()}) {

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
                new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.COBALT_INGOT.item(), SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(), SlimefunItems.COBALT_INGOT.item(), SlimefunItems.ALUMINUM_BRASS_INGOT.item(), SlimefunItems.ALUMINUM_BRASS_INGOT.item(), SlimefunItems.ALUMINUM_BRASS_INGOT.item()})
                .register(plugin);

        new GPSMarkerTool(itemGroups.gps, SlimefunItems.GPS_MARKER_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET.item(), null, new ItemStack(Material.LAPIS_LAZULI), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), new ItemStack(Material.LAPIS_LAZULI), new ItemStack(Material.REDSTONE), SlimefunItems.REDSTONE_ALLOY.item(), new ItemStack(Material.REDSTONE)})
                .register(plugin);

        new SlimefunItem(itemGroups.gps, SlimefunItems.GPS_EMERGENCY_TRANSMITTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET.item(), null, null, SlimefunItems.GPS_TRANSMITTER.item(), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), null})
                .register(plugin);

        new AndroidInterface(itemGroups.androids, SlimefunItems.ANDROID_INTERFACE_ITEMS, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), new ItemStack(Material.BLUE_STAINED_GLASS), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.PLASTIC_SHEET.item()})
                .register(plugin);

        new AndroidInterface(itemGroups.androids, SlimefunItems.ANDROID_INTERFACE_FUEL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.PLASTIC_SHEET.item(), new ItemStack(Material.RED_STAINED_GLASS), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.PLASTIC_SHEET.item()})
                .register(plugin);

        new ProgrammableAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.ANDROID_MEMORY_CORE.item(), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.COAL_GENERATOR.item(), SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.CHEST), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.PLASTIC_SHEET.item()})
                .register(plugin);

        new MinerAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID_MINER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_PICKAXE), SlimefunItems.PROGRAMMABLE_ANDROID.item(), new ItemStack(Material.DIAMOND_PICKAXE), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new FarmerAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID_FARMER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_HOE), SlimefunItems.PROGRAMMABLE_ANDROID.item(), new ItemStack(Material.DIAMOND_HOE), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new WoodcutterAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_AXE), SlimefunItems.PROGRAMMABLE_ANDROID.item(), new ItemStack(Material.DIAMOND_AXE), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new FishermanAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID_FISHERMAN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID.item(), new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new ButcherAndroid(itemGroups.androids, 1, SlimefunItems.PROGRAMMABLE_ANDROID_BUTCHER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER.item(), null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID.item(), new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new ProgrammableAndroid(itemGroups.androids, 2, SlimefunItems.PROGRAMMABLE_ANDROID_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.ANDROID_MEMORY_CORE.item(), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.COMBUSTION_REACTOR.item(), SlimefunItems.PROGRAMMABLE_ANDROID.item(), new ItemStack(Material.CHEST), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.PLASTIC_SHEET.item()})
                .register(plugin);

        new FishermanAndroid(itemGroups.androids, 2, SlimefunItems.PROGRAMMABLE_ANDROID_2_FISHERMAN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID_2.item(), new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new ButcherAndroid(itemGroups.androids, 2, SlimefunItems.PROGRAMMABLE_ANDROID_2_BUTCHER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER.item(), null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID_2.item(), new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new FarmerAndroid(itemGroups.androids, 2, SlimefunItems.PROGRAMMABLE_ANDROID_2_FARMER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER.item(), null, new ItemStack(Material.DIAMOND_HOE), SlimefunItems.PROGRAMMABLE_ANDROID_2.item(), new ItemStack(Material.DIAMOND_HOE), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new ProgrammableAndroid(itemGroups.androids, 3, SlimefunItems.PROGRAMMABLE_ANDROID_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.ANDROID_MEMORY_CORE.item(), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.NUCLEAR_REACTOR.item(), SlimefunItems.PROGRAMMABLE_ANDROID_2.item(), new ItemStack(Material.CHEST), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.BLISTERING_INGOT_3.item()})
                .register(plugin);

        new FishermanAndroid(itemGroups.androids, 3, SlimefunItems.PROGRAMMABLE_ANDROID_3_FISHERMAN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID_3.item(), new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new ButcherAndroid(itemGroups.androids, 3, SlimefunItems.PROGRAMMABLE_ANDROID_3_BUTCHER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER_3.item(), null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID_3.item(), new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.BLANK_RUNE,
                new ItemStack[] {new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.OBSIDIAN), SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.STONE)})
                .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.AIR_RUNE,
                new ItemStack[] {new ItemStack(Material.FEATHER), SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.FEATHER), new ItemStack(Material.GHAST_TEAR), SlimefunItems.BLANK_RUNE.item(), new ItemStack(Material.GHAST_TEAR), new ItemStack(Material.FEATHER), SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.FEATHER)},
                new SlimefunItemStack(SlimefunItems.AIR_RUNE, 4).item())
                .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.EARTH_RUNE,
                new ItemStack[] {new ItemStack(Material.DIRT), SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.STONE), new ItemStack(Material.OBSIDIAN), SlimefunItems.BLANK_RUNE.item(), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1.item(), new ItemStack(Material.DIRT)},
                new SlimefunItemStack(SlimefunItems.EARTH_RUNE, 4).item())
                .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.FIRE_RUNE,
                new ItemStack[] {new ItemStack(Material.FIRE_CHARGE), SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.FIRE_CHARGE), new ItemStack(Material.BLAZE_POWDER), SlimefunItems.EARTH_RUNE.item(), new ItemStack(Material.FLINT_AND_STEEL), new ItemStack(Material.FIRE_CHARGE), SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.FIRE_CHARGE)},
                new SlimefunItemStack(SlimefunItems.FIRE_RUNE, 4).item())
                .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.WATER_RUNE,
                new ItemStack[] {new ItemStack(Material.SALMON), SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.SAND), SlimefunItems.BLANK_RUNE.item(), new ItemStack(Material.SAND), new ItemStack(Material.WATER_BUCKET), SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.COD)},
                new SlimefunItemStack(SlimefunItems.WATER_RUNE, 4).item())
                .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.ENDER_RUNE,
                new ItemStack[] {new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_3.item(), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_EYE), SlimefunItems.BLANK_RUNE.item(), new ItemStack(Material.ENDER_EYE), new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_3.item(), new ItemStack(Material.ENDER_PEARL)},
                new SlimefunItemStack(SlimefunItems.ENDER_RUNE, 6).item())
                .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.LIGHTNING_RUNE,
                new ItemStack[] {new ItemStack(Material.IRON_INGOT), SlimefunItems.MAGIC_LUMP_3.item(), new ItemStack(Material.IRON_INGOT), SlimefunItems.AIR_RUNE.item(), new ItemStack(Material.PHANTOM_MEMBRANE), SlimefunItems.WATER_RUNE.item(), new ItemStack(Material.IRON_INGOT), SlimefunItems.MAGIC_LUMP_3.item(), new ItemStack(Material.IRON_INGOT)},
                new SlimefunItemStack(SlimefunItems.LIGHTNING_RUNE, 4).item())
                .register(plugin);

        new ElementalRune(itemGroups.magicalResources, SlimefunItems.RAINBOW_RUNE,
                new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.MAGIC_LUMP_3.item(), new ItemStack(Material.CYAN_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.ENDER_RUNE.item(), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.YELLOW_DYE), SlimefunItems.ENDER_LUMP_3.item(), new ItemStack(Material.MAGENTA_DYE)})
                .register(plugin);

        new SoulboundRune(itemGroups.magicalResources, SlimefunItems.SOULBOUND_RUNE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.ENDER_RUNE.item(), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), SlimefunItems.MAGIC_LUMP_3.item()})
                .register(plugin);

        new EnchantmentRune(itemGroups.magicalResources, SlimefunItems.ENCHANTMENT_RUNE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.LIGHTNING_RUNE.item(), SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.MAGIC_LUMP_3.item()})
                .register(plugin);

        new InfernalBonemeal(itemGroups.magicalGadgets, SlimefunItems.INFERNAL_BONEMEAL, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.NETHER_WART), SlimefunItems.EARTH_RUNE.item(), new ItemStack(Material.NETHER_WART), SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.BONE_MEAL), SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.NETHER_WART), new ItemStack(Material.BLAZE_POWDER), new ItemStack(Material.NETHER_WART)},
                new SlimefunItemStack(SlimefunItems.INFERNAL_BONEMEAL, 8).item())
                .register(plugin);

        new SlimefunItem(itemGroups.magicalGadgets, SlimefunItems.ELYTRA_SCALE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.AIR_RUNE.item(), SlimefunItems.ENDER_LUMP_3.item(), new ItemStack(Material.PHANTOM_MEMBRANE), new ItemStack(Material.FEATHER), new ItemStack(Material.PHANTOM_MEMBRANE), SlimefunItems.ENDER_LUMP_3.item(), SlimefunItems.AIR_RUNE.item(), SlimefunItems.ENDER_LUMP_3.item()})
                .register(plugin);

        new VanillaItem(itemGroups.magicalGadgets, new ItemStack(Material.ELYTRA), "ELYTRA", RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.ELYTRA_SCALE.item(), SlimefunItems.AIR_RUNE.item(), SlimefunItems.ELYTRA_SCALE.item(), SlimefunItems.AIR_RUNE.item(), new ItemStack(Material.LEATHER_CHESTPLATE), SlimefunItems.AIR_RUNE.item(), SlimefunItems.ELYTRA_SCALE.item(), SlimefunItems.AIR_RUNE.item(), SlimefunItems.ELYTRA_SCALE.item()})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalGadgets, SlimefunItems.INFUSED_ELYTRA, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.FLASK_OF_KNOWLEDGE.item(), SlimefunItems.ELYTRA_SCALE.item(), SlimefunItems.FLASK_OF_KNOWLEDGE.item(), SlimefunItems.FLASK_OF_KNOWLEDGE.item(), new ItemStack(Material.ELYTRA), SlimefunItems.FLASK_OF_KNOWLEDGE.item(), SlimefunItems.FLASK_OF_KNOWLEDGE.item(), SlimefunItems.ELYTRA_SCALE.item(), SlimefunItems.FLASK_OF_KNOWLEDGE.item()})
                .register(plugin);

        new SoulboundItem(itemGroups.magicalGadgets, SlimefunItems.SOULBOUND_ELYTRA, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.FLASK_OF_KNOWLEDGE.item(), SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), SlimefunItems.FLASK_OF_KNOWLEDGE.item(), SlimefunItems.ELYTRA_SCALE.item(), new ItemStack(Material.ELYTRA), SlimefunItems.ELYTRA_SCALE.item(), SlimefunItems.FLASK_OF_KNOWLEDGE.item(), SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), SlimefunItems.FLASK_OF_KNOWLEDGE.item()})
                .register(plugin);

        new VanillaItem(itemGroups.magicalGadgets, new ItemStack(Material.TRIDENT), "TRIDENT", RecipeType.ANCIENT_ALTAR,
                new ItemStack[] { new ItemStack(Material.NAUTILUS_SHELL), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), new ItemStack(Material.NAUTILUS_SHELL), SlimefunItems.STAFF_WATER.item(), new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.STAFF_WATER.item(), SlimefunItems.MAGIC_LUMP_3.item(), new ItemStack(Material.NETHER_STAR), SlimefunItems.MAGIC_LUMP_3.item()})
                .register(plugin);

        new VanillaItem(itemGroups.magicalGadgets, new ItemStack(Material.TOTEM_OF_UNDYING), "TOTEM_OF_UNDYING", RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), new ItemStack(Material.EMERALD_BLOCK), SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.COMMON_TALISMAN.item(), SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), new ItemStack(Material.EMERALD_BLOCK), SlimefunItems.ESSENCE_OF_AFTERLIFE.item()})
                .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_WOOL, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_WOOL, 8).item(), new RainbowTickHandler(ColoredMaterial.WOOL))
                .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_GLASS, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS, 8).item(), new RainbowTickHandler(ColoredMaterial.STAINED_GLASS))
                .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_GLASS_PANE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_PANE, 8).item(), new RainbowTickHandler(ColoredMaterial.STAINED_GLASS_PANE))
                .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_CLAY, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_CLAY, 8).item(), new RainbowTickHandler(ColoredMaterial.TERRACOTTA))
                .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_CONCRETE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.WHITE_CONCRETE), new ItemStack(Material.WHITE_CONCRETE), new ItemStack(Material.WHITE_CONCRETE), new ItemStack(Material.WHITE_CONCRETE), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_CONCRETE), new ItemStack(Material.WHITE_CONCRETE), new ItemStack(Material.WHITE_CONCRETE), new ItemStack(Material.WHITE_CONCRETE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_CONCRETE, 8).item(), new RainbowTickHandler(ColoredMaterial.CONCRETE))
                .register(plugin);

        new RainbowBlock(itemGroups.magicalGadgets, SlimefunItems.RAINBOW_GLAZED_TERRACOTTA, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLAZED_TERRACOTTA, 8).item(), new RainbowTickHandler(ColoredMaterial.GLAZED_TERRACOTTA))
                .register(plugin);

        // Christmas

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_WOOL_XMAS, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_WOOL_XMAS, 2).item(), new RainbowTickHandler(Material.RED_WOOL, Material.GREEN_WOOL))
                .register(plugin);

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_GLASS_XMAS, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_XMAS, 2).item(), new RainbowTickHandler(Material.RED_STAINED_GLASS, Material.GREEN_STAINED_GLASS))
                .register(plugin);

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_GLASS_PANE_XMAS, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_PANE_XMAS, 2).item(), new RainbowTickHandler(Material.RED_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE))
                .register(plugin);

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_CLAY_XMAS, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_CLAY_XMAS, 2).item(), new RainbowTickHandler(Material.RED_TERRACOTTA, Material.GREEN_TERRACOTTA))
                .register(plugin);

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_CONCRETE_XMAS, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_CONCRETE), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_CONCRETE), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_CONCRETE_XMAS, 2).item(), new RainbowTickHandler(Material.RED_CONCRETE, Material.GREEN_CONCRETE))
                .register(plugin);

        new RainbowBlock(itemGroups.christmas, SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_XMAS, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE.item(), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_XMAS, 2).item(), new RainbowTickHandler(Material.RED_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA))
                .register(plugin);

        // Valentines Day

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_WOOL_VALENTINE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_WOOL_VALENTINE, 2).item(), new RainbowTickHandler(Material.MAGENTA_WOOL, Material.PINK_WOOL))
                .register(plugin);

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_GLASS_VALENTINE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_VALENTINE, 2).item(), new RainbowTickHandler(Material.MAGENTA_STAINED_GLASS, Material.PINK_STAINED_GLASS))
                .register(plugin);

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_GLASS_PANE_VALENTINE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_PANE_VALENTINE, 2).item(), new RainbowTickHandler(Material.MAGENTA_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE))
                .register(plugin);

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_CLAY_VALENTINE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_CLAY_VALENTINE, 2).item(), new RainbowTickHandler(Material.MAGENTA_TERRACOTTA, Material.PINK_TERRACOTTA))
                .register(plugin);

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_CONCRETE_VALENTINE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_CONCRETE), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_CONCRETE), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_CONCRETE_VALENTINE, 2).item(), new RainbowTickHandler(Material.MAGENTA_CONCRETE, Material.PINK_CONCRETE))
                .register(plugin);

        new RainbowBlock(itemGroups.valentinesDay, SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_VALENTINE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_VALENTINE, 2).item(), new RainbowTickHandler(Material.MAGENTA_GLAZED_TERRACOTTA, Material.PINK_GLAZED_TERRACOTTA))
                .register(plugin);

        // Halloween

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_WOOL_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.ORANGE_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_WOOL_HALLOWEEN, 2).item(), new RainbowTickHandler(Material.ORANGE_WOOL, Material.BLACK_WOOL))
                .register(plugin);

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_GLASS_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.ORANGE_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_HALLOWEEN, 2).item(), new RainbowTickHandler(Material.ORANGE_STAINED_GLASS, Material.BLACK_STAINED_GLASS))
                .register(plugin);

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_GLASS_PANE_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.ORANGE_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLASS_PANE_HALLOWEEN, 2).item(), new RainbowTickHandler(Material.ORANGE_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS_PANE))
                .register(plugin);

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_CLAY_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.ORANGE_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_CLAY_HALLOWEEN, 2).item(), new RainbowTickHandler(Material.ORANGE_TERRACOTTA, Material.BLACK_TERRACOTTA))
                .register(plugin);

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_CONCRETE_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.WHITE_CONCRETE), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_CONCRETE), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.ORANGE_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_CONCRETE_HALLOWEEN, 2).item(), new RainbowTickHandler(Material.ORANGE_CONCRETE, Material.BLACK_CONCRETE))
                .register(plugin);

        new RainbowBlock(itemGroups.halloween, SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.WHITE_GLAZED_TERRACOTTA), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.ORANGE_DYE)},
                new SlimefunItemStack(SlimefunItems.RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN, 2).item(), new RainbowTickHandler(Material.ORANGE_GLAZED_TERRACOTTA, Material.BLACK_GLAZED_TERRACOTTA))
                .register(plugin);

        new WitherProofBlock(itemGroups.technicalComponents, SlimefunItems.WITHER_PROOF_GLASS, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.LEAD_INGOT.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.LEAD_INGOT.item()},
                new SlimefunItemStack(SlimefunItems.WITHER_PROOF_GLASS, 4).item())
                .register(plugin);

        new GEOScanner(itemGroups.gps, SlimefunItems.GPS_GEO_SCANNER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET.item(), null, SlimefunItems.STEEL_INGOT.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.ELECTRO_MAGNET.item()})
                .register(plugin);

        new PortableGEOScanner(itemGroups.gps, SlimefunItems.PORTABLE_GEO_SCANNER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.ELECTRO_MAGNET.item(), new ItemStack(Material.COMPASS), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.GPS_MARKER_TOOL.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.SOLDER_INGOT.item()})
                .register(plugin);

        new OilPump(itemGroups.gps, SlimefunItems.OIL_PUMP, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.STEEL_INGOT.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.STEEL_INGOT.item(), null, new ItemStack(Material.BUCKET), null})
                .setCapacity(256)
                .setEnergyConsumption(14)
                .setProcessingSpeed(1)
                .register(plugin);

        new GEOMiner(itemGroups.gps, SlimefunItems.GEO_MINER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.DIAMOND_PICKAXE), SlimefunItems.MEDIUM_CAPACITOR.item(), new ItemStack(Material.DIAMOND_PICKAXE), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.OIL_PUMP.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .setCapacity(512)
                .setEnergyConsumption(24)
                .setProcessingSpeed(1)
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.OIL_BUCKET, new RecipeType(new NamespacedKey(plugin, "oil_pump"), SlimefunItems.OIL_PUMP),
                new ItemStack[] {null, null, null, null, new ItemStack(Material.BUCKET), null, null, null, null})
                .register(plugin);

        new SlimefunItem(itemGroups.resources, SlimefunItems.FUEL_BUCKET, RecipeType.REFINERY,
                new ItemStack[] {null, null, null, null, SlimefunItems.OIL_BUCKET.item(), null, null, null, null})
                .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.MODERATE, SlimefunItems.NETHER_ICE, RecipeType.GEO_MINER,
                new ItemStack[] {null, null, null, null, null, null, null, null, null})
                .register(plugin);

        new Refinery(itemGroups.electricity, SlimefunItems.REFINERY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.HARDENED_GLASS.item(), new ItemStack(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.PISTON)})
                .setCapacity(256)
                .setEnergyConsumption(16)
                .setProcessingSpeed(1)
                .register(plugin);

        new LavaGenerator(itemGroups.electricity, SlimefunItems.LAVA_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GOLD_16K.item(), null, SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.HEATING_COIL.item()})
                .setCapacity(512)
                .setEnergyProduction(10)
                .register(plugin);

        new LavaGenerator(itemGroups.electricity, SlimefunItems.LAVA_GENERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.LAVA_GENERATOR.item(), SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.COMPRESSED_CARBON.item(), SlimefunItems.HEATING_COIL.item()})
                .setCapacity(1024)
                .setEnergyProduction(20)
                .register(plugin);

        new CombustionGenerator(itemGroups.electricity, SlimefunItems.COMBUSTION_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.STEEL_INGOT.item(), null, SlimefunItems.STEEL_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.HEATING_COIL.item()})
                .setCapacity(256)
                .setEnergyProduction(12)
                .register(plugin);

        new TeleporterPylon(itemGroups.gps, SlimefunItems.GPS_TELEPORTER_PYLON, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.ZINC_INGOT.item(), new ItemStack(Material.GLASS), SlimefunItems.ZINC_INGOT.item(), new ItemStack(Material.GLASS), SlimefunItems.HEATING_COIL.item(), new ItemStack(Material.GLASS), SlimefunItems.ZINC_INGOT.item(), new ItemStack(Material.GLASS), SlimefunItems.ZINC_INGOT.item()},
                new SlimefunItemStack(SlimefunItems.GPS_TELEPORTER_PYLON, 8).item())
                .register(plugin);

        new Teleporter(itemGroups.gps, SlimefunItems.GPS_TELEPORTATION_MATRIX, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.GPS_TELEPORTER_PYLON.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.GPS_TELEPORTER_PYLON.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.GPS_CONTROL_PANEL.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.GPS_TELEPORTER_PYLON.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.GPS_TELEPORTER_PYLON.item()})
                .register(plugin);

        new PortableTeleporter(itemGroups.gps, SlimefunItems.PORTABLE_TELEPORTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.GPS_TRANSMITTER_3.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.GPS_TELEPORTATION_MATRIX.item(), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.BLISTERING_INGOT_3.item()})
                .register(plugin);

        new SharedActivationPlate(itemGroups.gps, SlimefunItems.GPS_ACTIVATION_DEVICE_SHARED, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.STONE_PRESSURE_PLATE), null, new ItemStack(Material.REDSTONE), SlimefunItems.GPS_TRANSMITTER.item(), new ItemStack(Material.REDSTONE), SlimefunItems.BILLON_INGOT.item(), SlimefunItems.BILLON_INGOT.item(), SlimefunItems.BILLON_INGOT.item()})
                .register(plugin);

        new PersonalActivationPlate(itemGroups.gps, SlimefunItems.GPS_ACTIVATION_DEVICE_PERSONAL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.LEAD_INGOT.item(), null, SlimefunItems.COBALT_INGOT.item(), SlimefunItems.GPS_ACTIVATION_DEVICE_SHARED.item(), SlimefunItems.COBALT_INGOT.item(), null, SlimefunItems.LEAD_INGOT.item(), null})
                .register(plugin);

        new InfusedHopper(itemGroups.magicalGadgets, SlimefunItems.INFUSED_HOPPER, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {new ItemStack(Material.OBSIDIAN), SlimefunItems.EARTH_RUNE.item(), new ItemStack(Material.HOPPER), SlimefunItems.ENDER_RUNE.item(), SlimefunItems.INFUSED_MAGNET.item(), SlimefunItems.ENDER_RUNE.item(), new ItemStack(Material.HOPPER), SlimefunItems.EARTH_RUNE.item(), new ItemStack(Material.OBSIDIAN)})
                .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.HIGH, SlimefunItems.BLISTERING_INGOT, RecipeType.HEATED_PRESSURE_CHAMBER,
                new ItemStack[] {SlimefunItems.GOLD_24K.item(), SlimefunItems.URANIUM.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.VERY_HIGH, SlimefunItems.BLISTERING_INGOT_2, RecipeType.HEATED_PRESSURE_CHAMBER,
                new ItemStack[] {SlimefunItems.BLISTERING_INGOT.item(), SlimefunItems.CARBONADO.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.VERY_HIGH, SlimefunItems.BLISTERING_INGOT_3, RecipeType.HEATED_PRESSURE_CHAMBER,
                new ItemStack[] {SlimefunItems.BLISTERING_INGOT_2.item(), new ItemStack(Material.NETHER_STAR), null, null, null, null, null, null, null})
                .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.VERY_HIGH, SlimefunItems.ENRICHED_NETHER_ICE, RecipeType.HEATED_PRESSURE_CHAMBER,
                new ItemStack[] {SlimefunItems.NETHER_ICE.item(), SlimefunItems.PLUTONIUM.item(), null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.ENRICHED_NETHER_ICE, 4).item())
                .register(plugin);

        new ElevatorPlate(itemGroups.gps, SlimefunItems.ELEVATOR_PLATE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.STONE_PRESSURE_PLATE), null, new ItemStack(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.PISTON), SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), SlimefunItems.ALUMINUM_BRONZE_INGOT.item()},
                new SlimefunItemStack(SlimefunItems.ELEVATOR_PLATE, 2).item())
                .register(plugin);

        new FoodFabricator(itemGroups.electricity, SlimefunItems.FOOD_FABRICATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.BILLON_INGOT.item(), SlimefunItems.SILVER_INGOT.item(), SlimefunItems.BILLON_INGOT.item(), SlimefunItems.TIN_CAN.item(), SlimefunItems.SMALL_CAPACITOR.item(), SlimefunItems.TIN_CAN.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .setCapacity(256)
                .setEnergyConsumption(7)
                .setProcessingSpeed(1)
                .register(plugin);

        new FoodFabricator(itemGroups.electricity, SlimefunItems.FOOD_FABRICATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.FOOD_FABRICATOR.item(), SlimefunItems.ELECTRIC_MOTOR.item(), null, SlimefunItems.ELECTRO_MAGNET.item(), null})
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
                new ItemStack[] {SlimefunItems.GOLD_18K.item(), SlimefunItems.TIN_CAN.item(), SlimefunItems.GOLD_18K.item(), SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.HAY_BLOCK), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.FOOD_FABRICATOR.item(), SlimefunItems.LEAD_INGOT.item()})
                .register(plugin);

        new AnimalGrowthAccelerator(itemGroups.electricity, SlimefunItems.ANIMAL_GROWTH_ACCELERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.WHEAT_ORGANIC_FOOD.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.AUTO_BREEDER.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item()})
                .register(plugin);

        new TreeGrowthAccelerator(itemGroups.electricity, SlimefunItems.TREE_GROWTH_ACCELERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.CARBONADO.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.DIAMOND_AXE), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.MAGNESIUM_SALT.item(), SlimefunItems.BIG_CAPACITOR.item(), SlimefunItems.MAGNESIUM_SALT.item()})
                .register(plugin);

        new ExpCollector(itemGroups.electricity, SlimefunItems.EXP_COLLECTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3.item(), null, SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.AUTO_ENCHANTER.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.ALUMINUM_BRONZE_INGOT.item()})
        .setEnergyConsumption(10)
        .setCapacity(1024)
        .register(plugin);

        new FoodComposter(itemGroups.electricity, SlimefunItems.FOOD_COMPOSTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.FOOD_FABRICATOR.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.TIN_CAN.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.TIN_CAN.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .setCapacity(256)
                .setEnergyConsumption(8)
                .setProcessingSpeed(1)
                .register(plugin);

        new FoodComposter(itemGroups.electricity, SlimefunItems.FOOD_COMPOSTER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.FOOD_COMPOSTER.item(), SlimefunItems.ELECTRIC_MOTOR.item(), null, SlimefunItems.ELECTRO_MAGNET.item(), null})
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
                new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.PROGRAMMABLE_ANDROID_FARMER.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.ANIMAL_GROWTH_ACCELERATOR.item(), SlimefunItems.ELECTRO_MAGNET.item()}) {

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
                new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.CROP_GROWTH_ACCELERATOR.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(), SlimefunItems.ELECTRO_MAGNET.item()}) {

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
                new ItemStack[] {null, SlimefunItems.SILVER_INGOT.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.PACKED_ICE), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.COOLING_UNIT.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.COOLING_UNIT.item()})
                .setCapacity(256)
                .setEnergyConsumption(9)
                .setProcessingSpeed(1)
                .register(plugin);

        new Freezer(itemGroups.electricity, SlimefunItems.FREEZER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.SILVER_INGOT.item(), null, SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.FREEZER.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.COOLING_UNIT.item(), SlimefunItems.ALUMINUM_BRASS_INGOT.item(), SlimefunItems.COOLING_UNIT.item()})
                .setCapacity(256)
                .setEnergyConsumption(15)
                .setProcessingSpeed(2)
                .register(plugin);

        new Freezer(itemGroups.electricity, SlimefunItems.FREEZER_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.FREEZER_2.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.COOLING_UNIT.item(), SlimefunItems.COOLING_UNIT.item(), SlimefunItems.COOLING_UNIT.item()})
                .setCapacity(256)
                .setEnergyConsumption(21)
                .setProcessingSpeed(3)
                .register(plugin);

        new CoolantCell(itemGroups.technicalComponents, SlimefunItems.REACTOR_COOLANT_CELL, RecipeType.FREEZER,
                new ItemStack[] {new ItemStack(Material.BLUE_ICE), null, null, null, null, null, null, null, null})
                .register(plugin);

        new CoolantCell(itemGroups.technicalComponents, SlimefunItems.NETHER_ICE_COOLANT_CELL, RecipeType.HEATED_PRESSURE_CHAMBER,
                new ItemStack[] {SlimefunItems.ENRICHED_NETHER_ICE.item(), null, null, null, null, null, null, null, null},
                new SlimefunItemStack(SlimefunItems.NETHER_ICE_COOLANT_CELL, 8).item())
                .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.HIGH, SlimefunItems.NEPTUNIUM, RecipeType.NUCLEAR_REACTOR,
                new ItemStack[] {SlimefunItems.URANIUM.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.VERY_HIGH, SlimefunItems.PLUTONIUM, RecipeType.NUCLEAR_REACTOR,
                new ItemStack[] {SlimefunItems.NEPTUNIUM.item(), null, null, null, null, null, null, null, null})
                .register(plugin);

        new RadioactiveItem(itemGroups.resources, Radioactivity.VERY_HIGH, SlimefunItems.BOOSTED_URANIUM, RecipeType.HEATED_PRESSURE_CHAMBER,
                new ItemStack[] {SlimefunItems.PLUTONIUM.item(), SlimefunItems.URANIUM.item(), null, null, null, null, null, null, null})
                .register(plugin);

        new NuclearReactor(itemGroups.electricity, SlimefunItems.NUCLEAR_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.CARBONADO_EDGED_CAPACITOR.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.COOLING_UNIT.item(), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.LEAD_INGOT.item()}){

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
                new ItemStack[] {SlimefunItems.BOOSTED_URANIUM.item(), SlimefunItems.CARBONADO_EDGED_CAPACITOR.item(), SlimefunItems.BOOSTED_URANIUM.item(), SlimefunItems.REINFORCED_PLATE.item(), new ItemStack(Material.NETHER_STAR), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item()}){

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
                new ItemStack[] {SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.SILVER_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.SILVER_INGOT.item(), SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.HARDENED_GLASS.item()},
                new SlimefunItemStack(SlimefunItems.CARGO_MOTOR, 4).item())
                .register(plugin);

        new CargoManager(itemGroups.cargo, SlimefunItems.CARGO_MANAGER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.HOLOGRAM_PROJECTOR.item(), null, SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.CARGO_MOTOR.item(), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), SlimefunItems.ANDROID_MEMORY_CORE.item(), SlimefunItems.ALUMINUM_BRONZE_INGOT.item()})
                .register(plugin);

        new CargoConnectorNode(itemGroups.cargo, SlimefunItems.CARGO_CONNECTOR_NODE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.BRONZE_INGOT.item(), SlimefunItems.SILVER_INGOT.item(), SlimefunItems.BRONZE_INGOT.item(), SlimefunItems.SILVER_INGOT.item(), SlimefunItems.CARGO_MOTOR.item(), SlimefunItems.SILVER_INGOT.item(), SlimefunItems.BRONZE_INGOT.item(), SlimefunItems.SILVER_INGOT.item(), SlimefunItems.BRONZE_INGOT.item()},
                new SlimefunItemStack(SlimefunItems.CARGO_CONNECTOR_NODE, 4).item())
                .register(plugin);

        new CargoInputNode(itemGroups.cargo, SlimefunItems.CARGO_INPUT_NODE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.HOPPER), null, SlimefunItems.BILLON_INGOT.item(), SlimefunItems.CARGO_CONNECTOR_NODE.item(), SlimefunItems.BILLON_INGOT.item(), null, new ItemStack(Material.HOPPER), null},
                new SlimefunItemStack(SlimefunItems.CARGO_INPUT_NODE, 2).item())
                .register(plugin);

        new CargoOutputNode(itemGroups.cargo, SlimefunItems.CARGO_OUTPUT_NODE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.HOPPER), null, SlimefunItems.BRASS_INGOT.item(), SlimefunItems.CARGO_CONNECTOR_NODE.item(), SlimefunItems.BRASS_INGOT.item(), null, new ItemStack(Material.HOPPER), null},
                new SlimefunItemStack(SlimefunItems.CARGO_OUTPUT_NODE, 2).item())
                .register(plugin);

        new AdvancedCargoOutputNode(itemGroups.cargo, SlimefunItems.CARGO_OUTPUT_NODE_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.CARGO_MOTOR.item(), null, SlimefunItems.COBALT_INGOT.item(), SlimefunItems.CARGO_OUTPUT_NODE.item(), SlimefunItems.COBALT_INGOT.item(), null, SlimefunItems.CARGO_MOTOR.item(), null})
                .register(plugin);

        new ReactorAccessPort(itemGroups.cargo, SlimefunItems.REACTOR_ACCESS_PORT, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3.item(), null, SlimefunItems.LEAD_INGOT.item(), SlimefunItems.CARGO_MOTOR.item(), SlimefunItems.LEAD_INGOT.item(), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .register(plugin);

        new FluidPump(itemGroups.electricity, SlimefunItems.FLUID_PUMP, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.MEDIUM_CAPACITOR.item(), null, new ItemStack(Material.BUCKET), SlimefunItems.CARGO_MOTOR.item(), new ItemStack(Material.BUCKET), null, SlimefunItems.OIL_PUMP.item(), null})
                .register(plugin);

        new TrashCan(itemGroups.cargo, SlimefunItems.TRASH_CAN, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.PORTABLE_DUSTBIN.item(), null, SlimefunItems.LEAD_INGOT.item(), SlimefunItems.CARGO_MOTOR.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.LEAD_INGOT.item(), SlimefunItems.ALUMINUM_INGOT.item()})
                .register(plugin);

        new CarbonPress(itemGroups.electricity, SlimefunItems.CARBON_PRESS, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CARBON.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.CARBON.item(), SlimefunItems.CARBON.item(), SlimefunItems.HEATED_PRESSURE_CHAMBER.item(), SlimefunItems.CARBON.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.HEATING_COIL.item()})
                .setCapacity(256)
                .setEnergyConsumption(10)
                .setProcessingSpeed(1)
                .register(plugin);

        new CarbonPress(itemGroups.electricity, SlimefunItems.CARBON_PRESS_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CARBONADO.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.CARBON.item(), SlimefunItems.CARBON_PRESS.item(), SlimefunItems.CARBON.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.HEATING_COIL.item()})
                .setCapacity(512)
                .setEnergyConsumption(25)
                .setProcessingSpeed(3)
                .register(plugin);

        new CarbonPress(itemGroups.electricity, SlimefunItems.CARBON_PRESS_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.CARBONADO.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.CARBON_PRESS_2.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.HEATING_COIL.item()})
                .setCapacity(512)
                .setEnergyConsumption(90)
                .setProcessingSpeed(15)
                .register(plugin);

        new ElectricSmeltery(itemGroups.electricity, SlimefunItems.ELECTRIC_SMELTERY, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.NETHER_BRICKS), SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.NETHER_BRICKS), SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_INGOT_FACTORY.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.GILDED_IRON.item()})
                .setCapacity(512)
                .setEnergyConsumption(10)
                .setProcessingSpeed(1)
                .register(plugin);

        new ElectricSmeltery(itemGroups.electricity, SlimefunItems.ELECTRIC_SMELTERY_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_SMELTERY.item(), SlimefunItems.HEATING_COIL.item(), SlimefunItems.GILDED_IRON.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.GILDED_IRON.item()})
                .setCapacity(1024)
                .setEnergyConsumption(20)
                .setProcessingSpeed(3)
                .register(plugin);

        new IronGolemAssembler(itemGroups.electricity, SlimefunItems.IRON_GOLEM_ASSEMBLER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(), SlimefunItems.BLISTERING_INGOT_3.item(), new ItemStack(Material.IRON_BLOCK), SlimefunItems.ANDROID_MEMORY_CORE.item(), new ItemStack(Material.IRON_BLOCK), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.CARBONADO_EDGED_CAPACITOR.item()})
                .register(plugin);

        new WitherAssembler(itemGroups.electricity, SlimefunItems.WITHER_ASSEMBLER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3.item(), new ItemStack(Material.NETHER_STAR), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.ANDROID_MEMORY_CORE.item(), SlimefunItems.WITHER_PROOF_OBSIDIAN.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.CARBONADO_EDGED_CAPACITOR.item()})
                .register(plugin);

        new TapeMeasure(itemGroups.usefulItems, SlimefunItems.TAPE_MEASURE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.SILICON.item(), new ItemStack(Material.YELLOW_DYE), SlimefunItems.SILICON.item(), new ItemStack(Material.YELLOW_DYE), new ItemStack(Material.STRING), new ItemStack(Material.YELLOW_DYE), SlimefunItems.GILDED_IRON.item(), new ItemStack(Material.YELLOW_DYE), SlimefunItems.SILICON.item()})
                .register(plugin);

        MinecraftVersion minecraftVersion = Slimefun.getMinecraftVersion();

        new SlimefunItem(itemGroups.magicalArmor, SlimefunItems.BEE_HELMET, RecipeType.ARMOR_FORGE,
                new ItemStack[] {SlimefunItems.GOLD_8K.item(), new ItemStack(Material.HONEY_BLOCK), SlimefunItems.GOLD_8K.item(), new ItemStack(Material.HONEYCOMB_BLOCK), null, new ItemStack(Material.HONEYCOMB_BLOCK), null, null, null})
                .register(plugin);

        new BeeWings(itemGroups.magicalArmor, SlimefunItems.BEE_WINGS, RecipeType.ARMOR_FORGE,
                new ItemStack[] {SlimefunItems.GOLD_8K.item(), null, SlimefunItems.GOLD_8K.item(), new ItemStack(Material.HONEYCOMB_BLOCK), new ItemStack(Material.ELYTRA), new ItemStack(Material.HONEYCOMB_BLOCK), new ItemStack(Material.HONEY_BLOCK), SlimefunItems.GOLD_8K.item(), new ItemStack(Material.HONEY_BLOCK)})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalArmor, SlimefunItems.BEE_LEGGINGS, RecipeType.ARMOR_FORGE,
                new ItemStack[] {SlimefunItems.GOLD_8K.item(), new ItemStack(Material.HONEY_BLOCK), SlimefunItems.GOLD_8K.item(), new ItemStack(Material.HONEYCOMB_BLOCK), null, new ItemStack(Material.HONEYCOMB_BLOCK), new ItemStack(Material.HONEYCOMB_BLOCK), null, new ItemStack(Material.HONEYCOMB_BLOCK)})
                .register(plugin);

        new LongFallBoots(itemGroups.magicalArmor, SlimefunItems.BEE_BOOTS, RecipeType.ARMOR_FORGE,
                new ItemStack[] {null, null, null, SlimefunItems.GOLD_8K.item(), null, SlimefunItems.GOLD_8K.item(), new ItemStack(Material.HONEY_BLOCK), null, new ItemStack(Material.HONEY_BLOCK)},
                new PotionEffect[] {new PotionEffect(VersionedPotionEffectType.JUMP_BOOST, 300, 2)},
                SoundEffect.BEE_BOOTS_FALL_SOUND)
                .register(plugin);

        new VillagerRune(itemGroups.magicalResources, SlimefunItems.VILLAGER_RUNE, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] {SlimefunItems.MAGIC_LUMP_3.item(), SlimefunItems.MAGICAL_GLASS.item(), new ItemStack(Material.CRYING_OBSIDIAN), SlimefunItems.STRANGE_NETHER_GOO.item(), SlimefunItems.FIRE_RUNE.item(), SlimefunItems.STRANGE_NETHER_GOO.item(), new ItemStack(Material.CRYING_OBSIDIAN), SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.MAGIC_LUMP_3.item()},
                new SlimefunItemStack(SlimefunItems.VILLAGER_RUNE, 3).item())
                .register(plugin);

        new StrangeNetherGoo(itemGroups.magicalResources, SlimefunItems.STRANGE_NETHER_GOO, RecipeType.BARTER_DROP,
                new ItemStack[] {null, null, null, null, CustomItemStack.create(HeadTexture.PIGLIN_HEAD.getAsItemStack(), "&fPiglin"), null, null, null, null})
                .register(plugin);

        if (minecraftVersion.isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            new Juice(itemGroups.food, SlimefunItems.GLOW_BERRY_JUICE, RecipeType.JUICER,
                    new ItemStack[] {new ItemStack(Material.GLOW_BERRIES), null, null, null, null, null, null, null, null})
                    .register(plugin);
        }

        new ElytraCap(itemGroups.magicalArmor, SlimefunItems.ELYTRA_CAP, RecipeType.ARMOR_FORGE,
                new ItemStack[] {new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), SlimefunItems.ELYTRA_SCALE.item(), SlimefunItems.ELYTRA_SCALE.item(), SlimefunItems.ELYTRA_SCALE.item(), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.SLIME_BALL)})
                .register(plugin);

        new SlimefunItem(itemGroups.magicalResources, SlimefunItems.RAINBOW_LEATHER, RecipeType.ANCIENT_ALTAR,
                new ItemStack[] { new ItemStack(Material.EMERALD), new ItemStack(Material.LEATHER), SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.RABBIT_HIDE), SlimefunItems.RAINBOW_RUNE.item(), new ItemStack(Material.RABBIT_HIDE), SlimefunItems.MAGIC_LUMP_2.item(), new ItemStack(Material.LEATHER), new ItemStack(Material.EMERALD) },
                new SlimefunItemStack(SlimefunItems.RAINBOW_LEATHER, 4).item())
                .register(plugin);

        new UnplaceableBlock(itemGroups.cargo, SlimefunItems.CRAFTING_MOTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.BLISTERING_INGOT_3.item(), new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.CARGO_MOTOR.item(), SlimefunItems.REDSTONE_ALLOY.item(), new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.BLISTERING_INGOT_3.item(), new ItemStack(Material.CRAFTING_TABLE)},
                new SlimefunItemStack(SlimefunItems.CRAFTING_MOTOR, 2).item())
                .register(plugin);

        new VanillaAutoCrafter(itemGroups.cargo, SlimefunItems.VANILLA_AUTO_CRAFTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.CARGO_MOTOR.item(), null, new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.CRAFTING_MOTOR.item(), new ItemStack(Material.CRAFTING_TABLE), null, SlimefunItems.ELECTRIC_MOTOR.item(), null})
                .setCapacity(256)
                .setEnergyConsumption(16)
                .register(plugin);

        new EnhancedAutoCrafter(itemGroups.cargo, SlimefunItems.ENHANCED_AUTO_CRAFTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.CRAFTING_MOTOR.item(), null, new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.DISPENSER), new ItemStack(Material.CRAFTING_TABLE), null, SlimefunItems.CARGO_MOTOR.item(), null})
                .setCapacity(256)
                .setEnergyConsumption(16)
                .register(plugin);

        new ArmorAutoCrafter(itemGroups.cargo, SlimefunItems.ARMOR_AUTO_CRAFTER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.CRAFTING_MOTOR.item(), null, new ItemStack(Material.DISPENSER), new ItemStack(Material.ANVIL), new ItemStack(Material.DISPENSER), new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.CRAFTING_TABLE)})
                .setCapacity(256)
                .setEnergyConsumption(32)
                .register(plugin);

        new ProduceCollector(itemGroups.electricity, SlimefunItems.PRODUCE_COLLECTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.HAY_BLOCK), null, new ItemStack(Material.BUCKET), SlimefunItems.MEDIUM_CAPACITOR.item(), new ItemStack(Material.BUCKET), SlimefunItems.ALUMINUM_BRASS_INGOT.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.ALUMINUM_BRASS_INGOT.item()})
                .setCapacity(256)
                .setProcessingSpeed(1)
                .setEnergyConsumption(16)
                .register(plugin);

        // @formatter:on
    }

    @ParametersAreNonnullByDefault
    private static void registerArmorSet(ItemGroup itemGroup, SlimefunItemStack baseComponent, ItemStack[] items, String idSyntax, boolean vanilla, PotionEffect[][] effects, SlimefunAddon addon) {
        registerArmorSet(itemGroup, baseComponent.item(), items, idSyntax, vanilla, effects, addon);
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
