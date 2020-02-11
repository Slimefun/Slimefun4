package io.github.thebusybiscuit.slimefun4.implementation.setup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.implementation.android.AdvancedFarmerAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.android.AndroidType;
import io.github.thebusybiscuit.slimefun4.implementation.android.ButcherAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.android.FarmerAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.android.FisherAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.android.MinerAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.android.ProgrammableAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.android.WoodcutterAndroid;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.RainbowTicker;
import io.github.thebusybiscuit.slimefun4.utils.FireworkUtils;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Alloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.HandledBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.JetBoots;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Jetpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Juice;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.RadioactiveItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ReplacingAlloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ReplacingItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunBackpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SoulboundItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Talisman;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.VanillaItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo.AdvancedCargoOutputNode;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo.CargoConnector;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo.CargoInputNode;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo.CargoManagerBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo.CargoOutputNode;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.Bandage;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.DietCookie;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.EnderBackpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.ExplosiveBow;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.ExplosivePickaxe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.ExplosiveShovel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.FortuneCookie;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.GoldPan;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.GrapplingHook;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.HerculesPickaxe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.HunterTalisman;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.IcyBow;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.InfernalBonemeal;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.KnowledgeFlask;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.KnowledgeTome;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.LumberAxe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.MagicEyeOfEnder;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.MagicSugar;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.MeatJerky;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.Medicine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.MonsterJerky;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.MultiTool;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.Multimeter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.NetherGoldPan;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.PickaxeOfContainment;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.PickaxeOfTheSeeker;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.PickaxeOfVeinMining;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.PortableCrafter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.PortableDustbin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.PortableGEOScanner;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.Rag;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.SeismicAxe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.SmeltersPickaxe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.SolarHelmet;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.SoulboundBackpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.SoulboundRune;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.Splint;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.StormStaff;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.SwordOfBeheading;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.TelepositionScroll;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.Vitamins;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.WaterStaff;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.WindStaff;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AncientPedestal;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.BlockPlacer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.Composter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.Crucible;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.EnhancedFurnace;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.HologramProjector;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.InfusedHopper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.ReactorAccessPort;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.TrashCan;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.AnimalGrowthAccelerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.AutoAnvil;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.AutoBreeder;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.AutoDisenchanter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.AutoDrier;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.AutoEnchanter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.AutomatedCraftingChamber;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.CarbonPress;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ChargingBench;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.CropGrowthAccelerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectricDustWasher;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectricFurnace;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectricGoldPan;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectricIngotFactory;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectricIngotPulverizer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectricOreGrinder;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectricPress;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectricSmeltery;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectrifiedCrucible;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.EnergyRegulator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.FluidPump;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.FoodComposter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.FoodFabricator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.Freezer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.HeatedPressureChamber;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.Refinery;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.WitherAssembler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.XPCollector;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.generators.BioGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.generators.CoalGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.generators.CombustionGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.generators.LavaGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.generators.MagnesiumGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.generators.SolarGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo.GEOMiner;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo.GEOScannerBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo.OilPump;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps.ElevatorPlate;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps.GPSControlPanel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps.GPSTransmitter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps.PersonalActivationPlate;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps.Teleporter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.reactors.NetherStarReactor;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.reactors.NuclearReactor;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.ArmorForge;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.AutomatedPanningMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.Compressor;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.EnhancedCraftingTable;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.GrindStone;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.Juicer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.MagicWorkbench;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.OreCrusher;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.OreWasher;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.PressureChamber;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.Smeltery;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.TableSaw;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.MultiBlockInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public final class SlimefunItemSetup {

	private SlimefunItemSetup() {}

	public static void setup() {
		new SlimefunItem(Categories.WEAPONS, (SlimefunItemStack) SlimefunItems.GRANDMAS_WALKING_STICK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.OAK_LOG), null, null, new ItemStack(Material.OAK_LOG), null, null, new ItemStack(Material.OAK_LOG), null})
		.register(true);
		
		new SlimefunItem(Categories.WEAPONS, (SlimefunItemStack) SlimefunItems.GRANDPAS_WALKING_STICK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.LEATHER), new ItemStack(Material.OAK_LOG), new ItemStack(Material.LEATHER), null, new ItemStack(Material.OAK_LOG), null, null, new ItemStack(Material.OAK_LOG), null})
		.register(true);

		new PortableCrafter(Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.PORTABLE_CRAFTER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.BOOK), new ItemStack(Material.CRAFTING_TABLE), null, null, null, null, null, null, null})
		.register(true);

		new FortuneCookie(Categories.FOOD, (SlimefunItemStack) SlimefunItems.FORTUNE_COOKIE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.COOKIE), new ItemStack(Material.PAPER), null, null, null, null, null, null, null})
		.register(true);

		new DietCookie(Categories.FOOD, (SlimefunItemStack) SlimefunItems.DIET_COOKIE, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.COOKIE), SlimefunItems.ELYTRA_SCALE, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MACHINES_1, (SlimefunItemStack) SlimefunItems.OUTPUT_CHEST, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LEAD_INGOT, new ItemStack(Material.HOPPER), SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, new ItemStack(Material.CHEST), SlimefunItems.LEAD_INGOT, null, SlimefunItems.LEAD_INGOT, null})
		.register(true);
		
		new EnhancedCraftingTable().register();

		new PortableDustbin(Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.PORTABLE_DUSTBIN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT)})
		.register(true);

		new MeatJerky(Categories.FOOD, (SlimefunItemStack) SlimefunItems.BEEF_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SALT, new ItemStack(Material.COOKED_BEEF), null, null, null, null, null, null, null})
		.register(true);

		new MeatJerky(Categories.FOOD, (SlimefunItemStack) SlimefunItems.PORK_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SALT, new ItemStack(Material.COOKED_PORKCHOP), null, null, null, null, null, null, null})
		.register(true);

		new MeatJerky(Categories.FOOD, (SlimefunItemStack) SlimefunItems.CHICKEN_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SALT, new ItemStack(Material.COOKED_CHICKEN), null, null, null, null, null, null, null})
		.register(true);

		new MeatJerky(Categories.FOOD, (SlimefunItemStack) SlimefunItems.MUTTON_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SALT, new ItemStack(Material.COOKED_MUTTON), null, null, null, null, null, null, null})
		.register(true);

		new MeatJerky(Categories.FOOD, (SlimefunItemStack) SlimefunItems.RABBIT_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SALT, new ItemStack(Material.COOKED_RABBIT), null, null, null, null, null, null, null})
		.register(true);

		new MeatJerky(Categories.FOOD, (SlimefunItemStack) SlimefunItems.FISH_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SALT, new ItemStack(Material.COOKED_COD), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.FOOD, (SlimefunItemStack) SlimefunItems.KELP_COOKIE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.DRIED_KELP), null, new ItemStack(Material.DRIED_KELP), new ItemStack(Material.SUGAR), new ItemStack(Material.DRIED_KELP), null, new ItemStack(Material.DRIED_KELP), null},
		new CustomItem(SlimefunItems.KELP_COOKIE, 2))
		.register(true);

		new GrindStone().register();
		new ArmorForge().register();
		new OreCrusher().register();
		new Compressor().register();

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.MAGIC_LUMP_1, RecipeType.GRIND_STONE,
		new ItemStack[] {new ItemStack(Material.NETHER_WART), null, null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.MAGIC_LUMP_1, 2))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.MAGIC_LUMP_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_1, SlimefunItems.MAGIC_LUMP_1, null, SlimefunItems.MAGIC_LUMP_1, SlimefunItems.MAGIC_LUMP_1, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.MAGIC_LUMP_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.MAGIC_LUMP_2, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.ENDER_LUMP_1, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, null, new ItemStack(Material.ENDER_EYE), null, null, null, null}, 
		new CustomItem(SlimefunItems.ENDER_LUMP_1, 2))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.ENDER_LUMP_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_1, SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1, SlimefunItems.ENDER_LUMP_1, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.ENDER_LUMP_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_2, SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2, SlimefunItems.ENDER_LUMP_2, null, null, null, null})
		.register(true);

		new EnderBackpack(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.ENDER_BACKPACK, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.LEATHER), SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.LEATHER), new ItemStack(Material.CHEST), new ItemStack(Material.LEATHER), SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.LEATHER), SlimefunItems.ENDER_LUMP_2})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.ENDER_HELMET, RecipeType.ARMOR_FORGE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.ENDER_EYE), SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.ENDER_CHESTPLATE, RecipeType.ARMOR_FORGE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.OBSIDIAN), new ItemStack(Material.ENDER_EYE), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN)})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.ENDER_LEGGINGS, RecipeType.ARMOR_FORGE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.ENDER_EYE), SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN)})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.ENDER_BOOTS, RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN)})
		.register(true);

		new MagicEyeOfEnder(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.MAGIC_EYE_OF_ENDER, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_EYE), new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2})
		.register(true);

		new MagicSugar(Categories.FOOD, (SlimefunItemStack) SlimefunItems.MAGIC_SUGAR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.REDSTONE), new ItemStack(Material.GLOWSTONE_DUST), null, null, null, null, null, null})
		.register(true);

		new MonsterJerky(Categories.FOOD, (SlimefunItemStack) SlimefunItems.MONSTER_JERKY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SALT, new ItemStack(Material.ROTTEN_FLESH), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SLIME_HELMET, RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT), null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SLIME_CHESTPLATE, RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT)})
		.register(true);

		new SlimefunArmorPiece(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SLIME_LEGGINGS, RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.SPEED, 300, 2)})
		.register(true);

		new SlimefunArmorPiece(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SLIME_BOOTS, RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.JUMP, 300, 5)})
		.register(true);

		new SwordOfBeheading(Categories.WEAPONS, (SlimefunItemStack) SlimefunItems.SWORD_OF_BEHEADING, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.EMERALD), null, SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.EMERALD), SlimefunItems.MAGIC_LUMP_2, null, new ItemStack(Material.BLAZE_ROD), null},
		new String[] {"chance.PLAYER", "chance.SKELETON", "chance.WITHER_SKELETON", "chance.ZOMBIE", "chance.CREEPER"}, new Integer[] {70, 40, 25, 40, 40})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.MAGICAL_BOOK_COVER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.BOOK), SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.BASIC_CIRCUIT_BOARD, RecipeType.MOB_DROP,
		new ItemStack[] {null, null, null, null, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19"), "&aIron Golem"), null, null, null, null},
		new String[] {"drop-from-golems"}, new Object[] {true})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.ADVANCED_CIRCUIT_BOARD, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK)})
		.register(true);

		new GoldPan(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.GOLD_PAN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.STONE), new ItemStack(Material.BOWL), new ItemStack(Material.STONE), new ItemStack(Material.STONE), new ItemStack(Material.STONE), new ItemStack(Material.STONE)})
		.register(true);

		new NetherGoldPan(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.NETHER_GOLD_PAN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.NETHER_BRICK), SlimefunItems.GOLD_PAN, new ItemStack(Material.NETHER_BRICK), new ItemStack(Material.NETHER_BRICK), new ItemStack(Material.NETHER_BRICK), new ItemStack(Material.NETHER_BRICK)})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.SIFTED_ORE, RecipeType.GOLD_PAN,
		new ItemStack[] {new ItemStack(Material.GRAVEL), null, null, null, null, null, null, null, null})
		.register(true);

		new Smeltery().register();
		
		new SlimefunItem(Categories.MACHINES_1, (SlimefunItemStack) SlimefunItems.IGNITION_CHAMBER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.STEEL_PLATE, SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.FLINT_AND_STEEL), SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_PLATE, SlimefunItems.ELECTRIC_MOTOR, null, new ItemStack(Material.HOPPER), null})
		.register(true);
		
		new PressureChamber().register();

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.BATTERY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.REDSTONE), null, SlimefunItems.ZINC_INGOT, SlimefunItems.SULFATE, SlimefunItems.COPPER_INGOT, SlimefunItems.ZINC_INGOT, SlimefunItems.SULFATE, SlimefunItems.COPPER_INGOT})
		.register(true);

		SlimefunManager.registerArmorSet(new ItemStack(Material.GLOWSTONE), new ItemStack[] {SlimefunItems.GLOWSTONE_HELMET, SlimefunItems.GLOWSTONE_CHESTPLATE, SlimefunItems.GLOWSTONE_LEGGINGS, SlimefunItems.GLOWSTONE_BOOTS}, "GLOWSTONE",
		new PotionEffect[][] {new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)}, new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)}, new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)}, new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)}}, true, true);

		SlimefunManager.registerArmorSet(SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_HELMET, SlimefunItems.DAMASCUS_STEEL_CHESTPLATE, SlimefunItems.DAMASCUS_STEEL_LEGGINGS, SlimefunItems.DAMASCUS_STEEL_BOOTS}, "DAMASCUS_STEEL", true, false);

		SlimefunManager.registerArmorSet(SlimefunItems.REINFORCED_ALLOY_INGOT, new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_HELMET, SlimefunItems.REINFORCED_ALLOY_CHESTPLATE, SlimefunItems.REINFORCED_ALLOY_LEGGINGS, SlimefunItems.REINFORCED_ALLOY_BOOTS}, "REINFORCED_ALLOY", true, false);

		SlimefunManager.registerArmorSet(new ItemStack(Material.CACTUS), new ItemStack[] {SlimefunItems.CACTUS_HELMET, SlimefunItems.CACTUS_CHESTPLATE, SlimefunItems.CACTUS_LEGGINGS, SlimefunItems.CACTUS_BOOTS}, "CACTUS", true, false);

		new Alloy((SlimefunItemStack) SlimefunItems.REINFORCED_ALLOY_INGOT,
		new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.GOLD_24K, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.HARDENED_METAL_INGOT,
		new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.ALUMINUM_BRONZE_INGOT, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.DAMASCUS_STEEL_INGOT,
		new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.IRON_DUST, SlimefunItems.CARBON, new ItemStack(Material.IRON_INGOT), null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.STEEL_INGOT,
		new ItemStack[] {SlimefunItems.IRON_DUST, SlimefunItems.CARBON, new ItemStack(Material.IRON_INGOT), null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.BRONZE_INGOT,
		new ItemStack[] {SlimefunItems.COPPER_DUST, SlimefunItems.TIN_DUST, SlimefunItems.COPPER_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.DURALUMIN_INGOT,
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.ALUMINUM_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.BILLON_INGOT,
		new ItemStack[] {SlimefunItems.SILVER_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.SILVER_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.BRASS_INGOT,
		new ItemStack[] {SlimefunItems.COPPER_DUST, SlimefunItems.ZINC_DUST, SlimefunItems.COPPER_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.ALUMINUM_BRASS_INGOT,
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.BRASS_INGOT, SlimefunItems.ALUMINUM_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.ALUMINUM_BRONZE_INGOT,
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.BRONZE_INGOT, SlimefunItems.ALUMINUM_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.CORINTHIAN_BRONZE_INGOT,
		new ItemStack[] {SlimefunItems.SILVER_DUST, SlimefunItems.GOLD_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.BRONZE_INGOT, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.SOLDER_INGOT,
		new ItemStack[] {SlimefunItems.LEAD_DUST, SlimefunItems.TIN_DUST, SlimefunItems.LEAD_INGOT, null, null, null, null, null, null})
		.register(true);

		new ReplacingAlloy(SlimefunItems.SYNTHETIC_SAPPHIRE, "SYNTHETIC_SAPPHIRE",
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, new ItemStack(Material.GLASS), new ItemStack(Material.GLASS_PANE), SlimefunItems.ALUMINUM_INGOT, new ItemStack(Material.LAPIS_LAZULI), null, null, null, null})
		.register(true);

		new ReplacingItem(Categories.RESOURCES, SlimefunItems.SYNTHETIC_DIAMOND, "SYNTHETIC_DIAMOND", RecipeType.PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.CARBON_CHUNK, null, null, null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.RAW_CARBONADO,
		new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.CARBON_CHUNK, new ItemStack(Material.GLASS_PANE), null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.NICKEL_INGOT,
		new ItemStack[] {SlimefunItems.IRON_DUST, new ItemStack(Material.IRON_INGOT), SlimefunItems.COPPER_DUST, null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.COBALT_INGOT,
		new ItemStack[] {SlimefunItems.IRON_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.NICKEL_INGOT, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.CARBONADO, RecipeType.PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.RAW_CARBONADO, null, null, null, null, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.FERROSILICON,
		new ItemStack[] {new ItemStack(Material.IRON_INGOT), SlimefunItems.IRON_DUST, SlimefunItems.SILICON, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.IRON_DUST, RecipeType.ORE_CRUSHER,
		new ItemStack[] {new ItemStack(Material.IRON_ORE), null, null, null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.IRON_DUST, (boolean) Slimefun.getItemValue("ORE_CRUSHER", "double-ores") ? 2: 1))
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_DUST, RecipeType.ORE_CRUSHER,
		new ItemStack[] {new ItemStack(Material.GOLD_ORE), null, null, null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.GOLD_DUST, (boolean) Slimefun.getItemValue("ORE_CRUSHER", "double-ores") ? 2: 1))
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.COPPER_DUST, RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.TIN_DUST, RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.LEAD_DUST, RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);
		
		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.SILVER_DUST, RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.ALUMINUM_DUST, RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.ZINC_DUST, RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.MAGNESIUM_DUST, RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.COPPER_INGOT, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.COPPER_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.TIN_INGOT, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.TIN_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.SILVER_INGOT, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.SILVER_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.LEAD_INGOT, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.LEAD_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.ALUMINUM_INGOT, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.ZINC_INGOT, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.ZINC_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.MAGNESIUM_INGOT, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.MAGNESIUM_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.SULFATE, RecipeType.ORE_CRUSHER,
		new ItemStack[] {new ItemStack(Material.NETHERRACK, 16), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.CARBON, RecipeType.COMPRESSOR,
		new ItemStack[] {new ItemStack(Material.COAL, 8), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.WHEAT_FLOUR, RecipeType.GRIND_STONE,
		new ItemStack[] {new ItemStack(Material.WHEAT), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.STEEL_PLATE, RecipeType.COMPRESSOR,
		new ItemStack[] {new CustomItem(SlimefunItems.STEEL_INGOT, 8), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.COMPRESSED_CARBON, RecipeType.COMPRESSOR,
		new ItemStack[] {new CustomItem(SlimefunItems.CARBON, 4), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.CARBON_CHUNK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, new ItemStack(Material.FLINT), SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.STEEL_THRUSTER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.REDSTONE), null, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.STEEL_PLATE, new ItemStack(Material.FIRE_CHARGE), SlimefunItems.STEEL_PLATE})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.POWER_CRYSTAL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE, new ItemStack(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.SYNTHETIC_SAPPHIRE, new ItemStack(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE, new ItemStack(Material.REDSTONE)})
		.register(true);

		new Jetpack((SlimefunItemStack) SlimefunItems.DURALUMIN_JETPACK,
		new ItemStack[] {SlimefunItems.DURALUMIN_INGOT, null, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.35)
		.register(true);

		new Jetpack((SlimefunItemStack) SlimefunItems.SOLDER_JETPACK,
		new ItemStack[] {SlimefunItems.SOLDER_INGOT, null, SlimefunItems.SOLDER_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.SOLDER_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.4)
		.register(true);

		new Jetpack((SlimefunItemStack) SlimefunItems.BILLON_JETPACK,
		new ItemStack[] {SlimefunItems.BILLON_INGOT, null, SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BILLON_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.45)
		.register(true);

		new Jetpack((SlimefunItemStack) SlimefunItems.STEEL_JETPACK,
		new ItemStack[] {SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.5)
		.register(true);

		new Jetpack((SlimefunItemStack) SlimefunItems.DAMASCUS_STEEL_JETPACK,
		new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.55)
		.register(true);

		new Jetpack((SlimefunItemStack) SlimefunItems.REINFORCED_ALLOY_JETPACK,
		new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.6)
		.register(true);

		new Jetpack((SlimefunItemStack) SlimefunItems.CARBONADO_JETPACK,
		new ItemStack[] {SlimefunItems.CARBON_CHUNK, null, SlimefunItems.CARBON_CHUNK, SlimefunItems.CARBONADO, SlimefunItems.POWER_CRYSTAL, SlimefunItems.CARBONADO, SlimefunItems.STEEL_THRUSTER, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.7)
		.register(true);

		new SlimefunItem(Categories.TECH, (SlimefunItemStack) SlimefunItems.PARACHUTE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CHAIN, null, SlimefunItems.CHAIN, null, null, null})
		.register(true);

		new HologramProjector(Categories.TECH, (SlimefunItemStack) SlimefunItems.HOLOGRAM_PROJECTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.POWER_CRYSTAL, null, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT, null, SlimefunItems.ALUMINUM_BRASS_INGOT, null}, 
		new CustomItem(SlimefunItems.HOLOGRAM_PROJECTOR, 3))
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.CHAIN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, null}, new CustomItem(SlimefunItems.CHAIN, 8))
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.HOOK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, null, null})
		.register(true);

		new GrapplingHook(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.GRAPPLING_HOOK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.HOOK, null, SlimefunItems.CHAIN, null, SlimefunItems.CHAIN, null, null},
		new String[] {"despawn-seconds"}, new Object[] {60})
		.register(true);

		new MagicWorkbench().register();

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.STAFF_ELEMENTAL, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.MAGIC_LUMP_3, null, new ItemStack(Material.STICK), SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.MAGIC_LUMP_3, null, null})
		.register(true);

		new WindStaff(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.STAFF_WIND, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.FEATHER), SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.STAFF_ELEMENTAL, new ItemStack(Material.FEATHER), SlimefunItems.STAFF_ELEMENTAL, null, null})
		.register(true);

		new WaterStaff(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.STAFF_WATER, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.LILY_PAD), SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.STAFF_ELEMENTAL, new ItemStack(Material.LILY_PAD), SlimefunItems.STAFF_ELEMENTAL, null, null})
		.register(true);
		
		String[] multiToolItems = new String[] {"PORTABLE_CRAFTER", "MAGIC_EYE_OF_ENDER", "STAFF_ELEMENTAL_WIND", "GRAPPLING_HOOK"};

		new MultiTool((SlimefunItemStack) SlimefunItems.DURALUMIN_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.DURALUMIN_INGOT, null, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.DURALUMIN_INGOT, null, SlimefunItems.DURALUMIN_INGOT, null},
		multiToolItems)
		.register(true);

		new MultiTool((SlimefunItemStack) SlimefunItems.SOLDER_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SOLDER_INGOT, null, SlimefunItems.SOLDER_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.SOLDER_INGOT, null, SlimefunItems.SOLDER_INGOT, null},
		multiToolItems)
		.register(true);

		new MultiTool((SlimefunItemStack) SlimefunItems.BILLON_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BILLON_INGOT, null, SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.BILLON_INGOT, null, SlimefunItems.BILLON_INGOT, null},
		multiToolItems)
		.register(true);

		new MultiTool((SlimefunItemStack) SlimefunItems.STEEL_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null},
		multiToolItems)
		.register(true);

		new MultiTool((SlimefunItemStack) SlimefunItems.DAMASCUS_STEEL_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.DAMASCUS_STEEL_INGOT, null, SlimefunItems.DAMASCUS_STEEL_INGOT, null},
		multiToolItems)
		.register(true);

		new MultiTool((SlimefunItemStack) SlimefunItems.REINFORCED_ALLOY_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, null},
		multiToolItems)
		.register(true);

		new MultiTool((SlimefunItemStack) SlimefunItems.CARBONADO_MULTI_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CARBONADO, null, SlimefunItems.CARBONADO, SlimefunItems.CARBONADO, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.CARBONADO, null, SlimefunItems.CARBONADO, null},
		"PORTABLE_CRAFTER", "MAGIC_EYE_OF_ENDER", "STAFF_ELEMENTAL_WIND", "GRAPPLING_HOOK", "GOLD_PAN", "NETHER_GOLD_PAN")
		.register(true);

		new OreWasher().register();

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_24K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_22K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_22K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_20K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_20K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_18K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_18K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_16K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_16K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_14K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_14K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_12K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_12K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_10K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_10K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_8K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_8K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_6K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_6K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_4K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.GOLD_4K, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.STONE_CHUNK, RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.SILICON, RecipeType.SMELTERY,
		new ItemStack[] {new ItemStack(Material.QUARTZ_BLOCK), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.SOLAR_PANEL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), SlimefunItems.SILICON, SlimefunItems.SILICON, SlimefunItems.SILICON, SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON})
		.register(true);

		new SolarHelmet(Categories.TECH, (SlimefunItemStack) SlimefunItems.SOLAR_HELMET, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.SOLAR_PANEL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.MEDIUM_CAPACITOR, null, SlimefunItems.MEDIUM_CAPACITOR},
		new String[] {"charge-amount"}, new Double[] {0.1})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.LAVA_CRYSTAL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.BLAZE_POWDER), SlimefunItems.RUNE_FIRE, new ItemStack(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1})
		.register(true);

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.STAFF_FIRE, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, null, SlimefunItems.LAVA_CRYSTAL, null, SlimefunItems.STAFF_ELEMENTAL, null, SlimefunItems.STAFF_ELEMENTAL, null, null})
		.register(true);
		
		new StormStaff(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.STAFF_STORM, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.RUNE_LIGHTNING, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_LIGHTNING, SlimefunItems.STAFF_WATER, SlimefunItems.MAGIC_SUGAR, SlimefunItems.STAFF_WIND, SlimefunItems.RUNE_LIGHTNING, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_LIGHTNING})
		.register(true);

		new SmeltersPickaxe(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.AUTO_SMELT_PICKAXE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LAVA_CRYSTAL, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.LAVA_CRYSTAL, null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.TALISMAN, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2, null, new ItemStack(Material.EMERALD), null, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2},
		new String[] {"recipe-requires-nether-stars"}, new Boolean[] {false})
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_ANVIL,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.ANVIL), SlimefunItems.TALISMAN, new ItemStack(Material.ANVIL), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, false, "anvil")
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_MINER,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.TALISMAN, SlimefunItems.SIFTED_ORE, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		false, false, "miner", 20)
		.register(true);

		new HunterTalisman((SlimefunItemStack) SlimefunItems.TALISMAN_HUNTER,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.TALISMAN, SlimefunItems.MONSTER_JERKY, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		false, false, "hunter", 20)
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_LAVA,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.TALISMAN, new ItemStack(Material.LAVA_BUCKET), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, true, "lava", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4))
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_WATER,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.WATER_BUCKET), SlimefunItems.TALISMAN, new ItemStack(Material.FISHING_ROD), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, true, "water", new PotionEffect(PotionEffectType.WATER_BREATHING, 3600, 4))
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_ANGEL,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.FEATHER), SlimefunItems.TALISMAN, new ItemStack(Material.FEATHER), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		false, true, "angel", 75)
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_FIRE,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.TALISMAN, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, true, "fire", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4))
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_MAGICIAN,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.ENCHANTING_TABLE), SlimefunItems.TALISMAN, new ItemStack(Material.ENCHANTING_TABLE), SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3},
		false, false, "magician", 80)
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_TRAVELLER,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.STAFF_WIND, SlimefunItems.TALISMAN_ANGEL, SlimefunItems.STAFF_WIND, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		false, false, "traveller", 60, new PotionEffect(PotionEffectType.SPEED, 3600, 2))
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_WARRIOR,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.TALISMAN, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, true, "warrior", new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 2))
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_KNIGHT,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.GILDED_IRON, SlimefunItems.TALISMAN_WARRIOR, SlimefunItems.GILDED_IRON, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		"knight", 30, new PotionEffect(PotionEffectType.REGENERATION, 100, 3))
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.GILDED_IRON,
		new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.IRON_DUST, null, null, null, null, null, null, null})
		.register(true);

		new ReplacingAlloy(SlimefunItems.SYNTHETIC_EMERALD, "SYNTHETIC_EMERALD",
		new ItemStack[] {SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.ALUMINUM_DUST, SlimefunItems.ALUMINUM_INGOT, new ItemStack(Material.GLASS_PANE), null, null, null, null, null})
		.register(true);

		SlimefunManager.registerArmorSet(SlimefunItems.CHAIN, new ItemStack[] {new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_BOOTS)}, "CHAIN", true, true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_WHIRLWIND,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.STAFF_WIND, SlimefunItems.TALISMAN_TRAVELLER, SlimefunItems.STAFF_WIND, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3}
		, false, true, "whirlwind", 60)
		.register(true);

		new Talisman((SlimefunItemStack) SlimefunItems.TALISMAN_WIZARD,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.TALISMAN_MAGICIAN, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3},
		false, false, "wizard", 60)
		.register(true);
		
		new LumberAxe(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.LUMBER_AXE, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.SYNTHETIC_DIAMOND, null, SlimefunItems.SYNTHETIC_EMERALD, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.SALT, RecipeType.ORE_WASHER,
		new ItemStack[] {new ItemStack(Material.SAND, 4), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.HEAVY_CREAM, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.MILK_BUCKET), null, null, null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.HEAVY_CREAM, 2))
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.CHEESE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.MILK_BUCKET), SlimefunItems.SALT, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.BUTTER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HEAVY_CREAM, SlimefunItems.SALT, null, null, null, null, null, null, null})
		.register(true);

		SlimefunManager.registerArmorSet(SlimefunItems.GILDED_IRON, new ItemStack[] {SlimefunItems.GILDED_IRON_HELMET, SlimefunItems.GILDED_IRON_CHESTPLATE, SlimefunItems.GILDED_IRON_LEGGINGS, SlimefunItems.GILDED_IRON_BOOTS}, "GILDED_IRON", true, false);

		new SlimefunArmorPiece(Categories.ARMOR, (SlimefunItemStack) SlimefunItems.SCUBA_HELMET, RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.BLACK_WOOL), null, null, null},
		new PotionEffect[] {new PotionEffect(PotionEffectType.WATER_BREATHING, 300, 1)})
		.register(true);

		new SlimefunArmorPiece(Categories.ARMOR, (SlimefunItemStack) SlimefunItems.HAZMATSUIT_CHESTPLATE, RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.ORANGE_WOOL), null, new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.BLACK_WOOL)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 1)})
		.register(true);

		new SlimefunItem(Categories.ARMOR, (SlimefunItemStack) SlimefunItems.HAZMATSUIT_LEGGINGS, RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.ORANGE_WOOL), null, new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), null, new ItemStack(Material.ORANGE_WOOL)})
		.register(true);

		new SlimefunItem(Categories.ARMOR, (SlimefunItemStack) SlimefunItems.RUBBER_BOOTS, RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.BLACK_WOOL), null, new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.BLACK_WOOL), null, new ItemStack(Material.BLACK_WOOL)})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.CRUSHED_ORE, RecipeType.ORE_CRUSHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.PULVERIZED_ORE, RecipeType.ORE_CRUSHER,
		new ItemStack[] {SlimefunItems.CRUSHED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.PURE_ORE_CLUSTER, RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.PULVERIZED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.TINY_URANIUM, RecipeType.ORE_CRUSHER,
		new ItemStack[] {SlimefunItems.PURE_ORE_CLUSTER, null, null, null, null, null, null, null, null})
		.register(true);

		new RadioactiveItem(Categories.MISC, Radioactivity.MODERATE, (SlimefunItemStack) SlimefunItems.SMALL_URANIUM, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM})
		.register(true);

		new RadioactiveItem(Categories.RESOURCES, Radioactivity.HIGH, (SlimefunItemStack) SlimefunItems.URANIUM, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SMALL_URANIUM, SlimefunItems.SMALL_URANIUM, null, SlimefunItems.SMALL_URANIUM, SlimefunItems.SMALL_URANIUM, null, null, null, null})
		.register(true);

		new Alloy((SlimefunItemStack) SlimefunItems.REDSTONE_ALLOY,
		new ItemStack[] {new ItemStack(Material.REDSTONE), new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.FERROSILICON, SlimefunItems.HARDENED_METAL_INGOT, null, null, null, null, null})
		.register(true);

		SlimefunManager.registerArmorSet(SlimefunItems.GOLD_12K, new ItemStack[] {SlimefunItems.GOLD_HELMET, SlimefunItems.GOLD_CHESTPLATE, SlimefunItems.GOLD_LEGGINGS, SlimefunItems.GOLD_BOOTS}, "GOLD_12K", true, false);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.CLOTH, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.WHITE_WOOL), null, null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.CLOTH, 8))
		.register(true);

		new Rag(Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.RAG, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH, new ItemStack(Material.STRING), null, new ItemStack(Material.STRING), SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH})
		.register(true);

		new Bandage(Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.BANDAGE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.RAG, new ItemStack(Material.STRING), SlimefunItems.RAG, null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.BANDAGE, 4))
		.register(true);

		new Splint(Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.SPLINT, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.STICK), null, new ItemStack(Material.IRON_INGOT), null}, 
		new CustomItem(SlimefunItems.SPLINT, 4))
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.CAN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, null, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT}, 
		new CustomItem(SlimefunItems.CAN, 4))
		.register(true);

		new Vitamins(Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.VITAMINS, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.APPLE), new ItemStack(Material.RED_MUSHROOM), new ItemStack(Material.SUGAR), null, null, null, null, null})
		.register(true);

		new Medicine(Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.MEDICINE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.VITAMINS, new ItemStack(Material.GLASS_BOTTLE), SlimefunItems.HEAVY_CREAM, null, null, null, null, null, null})
		.register(true);

		new SlimefunArmorPiece(Categories.TECH, (SlimefunItemStack) SlimefunItems.NIGHT_VISION_GOGGLES, RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.LIME_STAINED_GLASS_PANE), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.LIME_STAINED_GLASS_PANE), new ItemStack(Material.COAL_BLOCK), null, new ItemStack(Material.COAL_BLOCK)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 20)})
		.register(true);

		new PickaxeOfContainment(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.PICKAXE_OF_CONTAINMENT, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, null, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
		.register(true);

		new HerculesPickaxe(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.HERCULES_PICKAXE, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
		.register(true);
                
		new TableSaw().register();

		new SlimefunItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SLIME_HELMET_STEEL, RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SLIME_CHESTPLATE_STEEL, RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL)})
		.register(true);

		new SlimefunArmorPiece(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SLIME_LEGGINGS_STEEL, RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.SPEED, 300, 2)})
		.register(true);

		new SlimefunArmorPiece(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SLIME_BOOTS_STEEL, RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, new ItemStack(Material.SLIME_BALL)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.JUMP, 300, 5)})
		.register(true);

		new SlimefunItem(Categories.WEAPONS, (SlimefunItemStack) SlimefunItems.BLADE_OF_VAMPIRES, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.WITHER_SKELETON_SKULL), null, null, new ItemStack(Material.WITHER_SKELETON_SKULL), null, null, new ItemStack(Material.BLAZE_ROD), null})
		.register(true);

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.DIGITAL_MINER, "DIGITAL_MINER",
		new ItemStack[] {SlimefunItems.SOLAR_PANEL, new ItemStack(Material.CHEST), SlimefunItems.SOLAR_PANEL, new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.DISPENSER), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.HOPPER), new ItemStack(Material.IRON_BLOCK)},
		new ItemStack[0], BlockFace.SELF)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(Player p, MultiBlock mb, Block b) {
				if (mb.equals(((SlimefunMachine) SlimefunItem.getByID("DIGITAL_MINER")).getMultiBlock())) {
					p.sendMessage(ChatColor.DARK_RED + "THIS MACHINE WILL SOON BE REMOVED!");
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true) && Slimefun.hasUnlocked(p, SlimefunItems.DIGITAL_MINER, true)) {
						Block chestBlock = b.getRelative(BlockFace.UP);

						if(!(BlockStorage.check(chestBlock.getRelative(BlockFace.WEST), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.EAST), "SOLAR_PANEL")) &&
								!(BlockStorage.check(chestBlock.getRelative(BlockFace.NORTH), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.SOUTH), "SOLAR_PANEL"))) {
							return false;
						}

						Chest chest = (Chest) chestBlock.getState();
						Inventory inv = chest.getInventory();
						List<Location> ores = new ArrayList<>();

						for (int x = b.getX() - 4; x <= b.getX() + 4; x++) {
							for (int z = b.getZ() - 4; z <= b.getZ() + 4; z++) {
								for (int y = b.getY(); y > 0; y--) {
									if (b.getWorld().getBlockAt(x, y, z).getType().toString().endsWith("_ORE")) {
										ores.add(b.getWorld().getBlockAt(x, y, z).getLocation());
									}
								}
							}
						}
						if (!ores.isEmpty()) {
							Material ore = ores.get(0).getBlock().getType();
							ItemStack adding = new ItemStack(ore);
							ores.get(0).getBlock().setType(Material.AIR);
							ores.clear();
							if (InvUtils.fits(inv, adding)) {
								for (int i = 0; i < 4; i++) {
									int j = i;
									Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> {
										if (j < 3) {
											b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, ore);
										} 
										else {
											p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
											inv.addItem(adding);
										}
									}, i*20L);
								}
							}
							else SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
						}
						else SlimefunPlugin.getLocal().sendMessage(p, "miner.no-ores", true);
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.ADVANCED_DIGITAL_MINER, "ADVANCED_DIGITAL_MINER",
		new ItemStack[] {SlimefunItems.SOLAR_PANEL, new ItemStack(Material.CHEST), SlimefunItems.SOLAR_PANEL, SlimefunItems.GOLD_24K_BLOCK, new ItemStack(Material.DISPENSER), SlimefunItems.GOLD_24K_BLOCK, SlimefunItems.GOLD_24K_BLOCK, new ItemStack(Material.HOPPER), SlimefunItems.GOLD_24K_BLOCK},
		new ItemStack[0], BlockFace.SELF)
		.register(true, new MultiBlockInteractionHandler() {
			// Determines the drops an Advanced Digital Miner will get
			private final ItemStack effectivePickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
			
			@Override
			public boolean onInteract(Player p, MultiBlock mb, Block b) {
				if (mb.equals(((SlimefunMachine) SlimefunItem.getByID("ADVANCED_DIGITAL_MINER")).getMultiBlock())) {
					p.sendMessage(ChatColor.DARK_RED + "THIS MACHINE WILL SOON BE REMOVED!");
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true) && Slimefun.hasUnlocked(p, SlimefunItems.ADVANCED_DIGITAL_MINER, true)) {
						Block chestBlock = b.getRelative(BlockFace.UP);

						if(!(BlockStorage.check(chestBlock.getRelative(BlockFace.WEST), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.EAST), "SOLAR_PANEL")) &&
								!(BlockStorage.check(chestBlock.getRelative(BlockFace.NORTH), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.SOUTH), "SOLAR_PANEL"))) {
							return false;
						}

						Chest chest = (Chest) chestBlock.getState();
						Inventory inv = chest.getInventory();
						List<Location> ores = new ArrayList<>();

						for (int x = b.getX() - 6; x <= b.getX() + 6; x++) {
							for (int z = b.getZ() - 6; z <= b.getZ() + 6; z++) {
								for (int y = b.getY(); y > 0; y--) {
									if (b.getWorld().getBlockAt(x, y, z).getType().toString().endsWith("_ORE")) {
										ores.add(b.getWorld().getBlockAt(x, y, z).getLocation());
									}
								}
							}
						}
						if (!ores.isEmpty()) {
							Material ore = ores.get(0).getBlock().getType();
							ItemStack drop = new ItemStack(ore);
							
							if (ore == Material.COAL_ORE)  drop = new ItemStack(Material.COAL, 4);
							else if (ore == Material.IRON_ORE) drop = new CustomItem(SlimefunItems.IRON_DUST, 2);
							else if (ore == Material.GOLD_ORE)  drop = new CustomItem(SlimefunItems.GOLD_DUST, 2);
							else if (ore == Material.REDSTONE_ORE)  drop = new ItemStack(Material.REDSTONE, 8);
							else if (ore == Material.NETHER_QUARTZ_ORE)  drop = new ItemStack(Material.QUARTZ, 4);
							else if (ore == Material.LAPIS_ORE)  drop = new ItemStack(Material.LAPIS_LAZULI, 12);
							else {
								for (ItemStack drops : ores.get(0).getBlock().getDrops(effectivePickaxe)) {
									if (!drops.getType().isBlock()) drop = new CustomItem(drops, 2);
								}
							}
							
							final ItemStack adding = drop;
							ores.get(0).getBlock().setType(Material.AIR);
							ores.clear();
							if (InvUtils.fits(inv, adding)) {
								for (int i = 0; i < 4; i++) {
									int j = i;
									Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> {
										if (j < 3) {
											b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, ore);
										} 
										else {
											p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
											inv.addItem(adding);
										}
									}, i*20L);
								}
							}
							else SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
						}
						else SlimefunPlugin.getLocal().sendMessage(p, "miner.no-ores", true);
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.GOLD_24K_BLOCK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K})
		.register(true);

		new Composter(Categories.MACHINES_1, (SlimefunItemStack) SlimefunItems.COMPOSTER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.OAK_SLAB), null, new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), null, new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), new ItemStack(Material.CAULDRON), new ItemStack(Material.OAK_SLAB)})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.FARMER_SHOES, RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.HAY_BLOCK), null, new ItemStack(Material.HAY_BLOCK), new ItemStack(Material.HAY_BLOCK), null, new ItemStack(Material.HAY_BLOCK)})
		.register(true);
		
		new ExplosivePickaxe(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.EXPLOSIVE_PICKAXE, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.TNT), SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.TNT), null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null},
		new String[] {"damage-on-use"}, new Object[] {false})
		.register(true);

		new ExplosiveShovel(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.EXPLOSIVE_SHOVEL, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, SlimefunItems.SYNTHETIC_DIAMOND, null, null, new ItemStack(Material.TNT), null, null, SlimefunItems.FERROSILICON, null},
		new String[] {"damage-on-use"}, new Object[] {false})
		.register(true);

		new AutomatedPanningMachine().register();

		new SlimefunItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.BOOTS_OF_THE_STOMPER, RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.YELLOW_WOOL), null, new ItemStack(Material.YELLOW_WOOL), new ItemStack(Material.PISTON), null, new ItemStack(Material.PISTON)})
		.register(true);

		new PickaxeOfTheSeeker(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.PICKAXE_OF_THE_SEEKER, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.COMPASS), SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.COMPASS), null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
		.register(true);

		new SlimefunBackpack(9, Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.BACKPACK_SMALL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.LEATHER), null, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_6K, new ItemStack(Material.CHEST), SlimefunItems.GOLD_6K, new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER)})
		.register(true);

		new SlimefunBackpack(18, Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.BACKPACK_MEDIUM, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.LEATHER), null, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_10K, SlimefunItems.BACKPACK_SMALL, SlimefunItems.GOLD_10K, new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER)})
		.register(true);

		new SlimefunBackpack(27, Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.BACKPACK_LARGE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.LEATHER), null, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_14K, SlimefunItems.BACKPACK_MEDIUM, SlimefunItems.GOLD_14K, new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER)})
		.register(true);

		new SlimefunBackpack(36, Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.WOVEN_BACKPACK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CLOTH, null, SlimefunItems.CLOTH, SlimefunItems.GOLD_16K, SlimefunItems.BACKPACK_LARGE, SlimefunItems.GOLD_16K, SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH})
		.register(true);

		new Crucible(Categories.MACHINES_1, (SlimefunItemStack) SlimefunItems.CRUCIBLE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.TERRACOTTA), null, new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), null, new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), new ItemStack(Material.FLINT_AND_STEEL), new ItemStack(Material.TERRACOTTA)})
		.register(true);

		new SlimefunBackpack(45, Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.GILDED_BACKPACK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_22K, null, SlimefunItems.GOLD_22K, new ItemStack(Material.LEATHER), SlimefunItems.WOVEN_BACKPACK, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_22K, null, SlimefunItems.GOLD_22K})
		.register(true);

		new SlimefunBackpack(54, Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.RADIANT_BACKPACK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, new ItemStack(Material.LEATHER), SlimefunItems.GILDED_BACKPACK, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K})
		.register(true);

		new Alloy(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.MAGNET,
		new ItemStack[] {SlimefunItems.NICKEL_INGOT, SlimefunItems.ALUMINUM_DUST, SlimefunItems.IRON_DUST, SlimefunItems.COBALT_INGOT, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.INFUSED_MAGNET, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ENDER_LUMP_2, SlimefunItems.MAGNET, SlimefunItems.ENDER_LUMP_2, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3})
		.register(true);

		new SlimefunItem(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.COBALT_PICKAXE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.COBALT_INGOT, SlimefunItems.COBALT_INGOT, SlimefunItems.COBALT_INGOT, null, SlimefunItems.NICKEL_INGOT, null, null, SlimefunItems.NICKEL_INGOT, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.NECROTIC_SKULL, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, null, new ItemStack(Material.WITHER_SKELETON_SKULL), null, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.ESSENCE_OF_AFTERLIFE, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_AIR, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_EARTH, SlimefunItems.NECROTIC_SKULL, SlimefunItems.RUNE_FIRE, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_WATER, SlimefunItems.ENDER_LUMP_3})
		.register(true);

		new SoulboundBackpack(36, Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.BOUND_BACKPACK,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.WOVEN_BACKPACK, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2})
		.register(true);

		new JetBoots((SlimefunItemStack) SlimefunItems.DURALUMIN_JETBOOTS,
		new ItemStack[] {null, null, null, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.35)
		.register(true);

		new JetBoots((SlimefunItemStack) SlimefunItems.SOLDER_JETBOOTS,
		new ItemStack[] {null, null, null, SlimefunItems.SOLDER_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.SOLDER_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.4)
		.register(true);

		new JetBoots((SlimefunItemStack) SlimefunItems.BILLON_JETBOOTS,
		new ItemStack[] {null, null, null, SlimefunItems.BILLON_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BILLON_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.45)
		.register(true);

		new JetBoots((SlimefunItemStack) SlimefunItems.STEEL_JETBOOTS,
		new ItemStack[] {null, null, null, SlimefunItems.STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.5)
		.register(true);

		new JetBoots((SlimefunItemStack) SlimefunItems.DAMASCUS_STEEL_JETBOOTS,
		new ItemStack[] {null, null, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.55)
		.register(true);

		new JetBoots((SlimefunItemStack) SlimefunItems.REINFORCED_ALLOY_JETBOOTS,
		new ItemStack[] {null, null, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.6)
		.register(true);

		new JetBoots((SlimefunItemStack) SlimefunItems.CARBONADO_JETBOOTS,
		new ItemStack[] {null, null, null, SlimefunItems.CARBONADO, SlimefunItems.POWER_CRYSTAL, SlimefunItems.CARBONADO, SlimefunItems.STEEL_THRUSTER, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.7)
		.register(true);

		new JetBoots((SlimefunItemStack) SlimefunItems.ARMORED_JETBOOTS,
		new ItemStack[] {null, null, null, SlimefunItems.STEEL_PLATE, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_THRUSTER, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.45)
		.register(true);

		new SeismicAxe(Categories.WEAPONS, (SlimefunItemStack) SlimefunItems.SEISMIC_AXE, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.STAFF_ELEMENTAL, null, null, SlimefunItems.STAFF_ELEMENTAL, null})
		.register(true);

		new PickaxeOfVeinMining(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.PICKAXE_OF_VEIN_MINING, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.EMERALD_ORE), SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.EMERALD_ORE), null, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
		.register(true);

		new SoulboundItem(Categories.WEAPONS, (SlimefunItemStack) SlimefunItems.SOULBOUND_SWORD,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_SWORD), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);
		
		new SoulboundItem(Categories.WEAPONS, (SlimefunItemStack) SlimefunItems.SOULBOUND_TRIDENT,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.TRIDENT), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.WEAPONS, (SlimefunItemStack) SlimefunItems.SOULBOUND_BOW,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.BOW), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.SOULBOUND_PICKAXE,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_PICKAXE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.SOULBOUND_AXE,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_AXE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.SOULBOUND_SHOVEL,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_SHOVEL), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.TOOLS, (SlimefunItemStack) SlimefunItems.SOULBOUND_HOE,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_HOE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SOULBOUND_HELMET,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_HELMET), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SOULBOUND_CHESTPLATE,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_CHESTPLATE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SOULBOUND_LEGGINGS,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_LEGGINGS), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.MAGIC_ARMOR, (SlimefunItemStack) SlimefunItems.SOULBOUND_BOOTS,
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_BOOTS), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new Juicer().register();

		new Juice(Categories.FOOD, (SlimefunItemStack) SlimefunItems.APPLE_JUICE, RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.APPLE), null, null, null, null, null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, (SlimefunItemStack) SlimefunItems.CARROT_JUICE, RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.CARROT), null, null, null, null, null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, (SlimefunItemStack) SlimefunItems.MELON_JUICE, RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.MELON), null, null, null, null, null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, (SlimefunItemStack) SlimefunItems.PUMPKIN_JUICE, RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.PUMPKIN), null, null, null, null, null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, (SlimefunItemStack) SlimefunItems.SWEET_BERRY_JUICE, RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.SWEET_BERRIES), null, null, null, null, null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, (SlimefunItemStack) SlimefunItems.GOLDEN_APPLE_JUICE, RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.GOLDEN_APPLE), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.BROKEN_SPAWNER, new RecipeType(SlimefunItems.PICKAXE_OF_CONTAINMENT),
		new ItemStack[] {null, null, null, null, new ItemStack(Material.SPAWNER), null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.REPAIRED_SPAWNER, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.RUNE_ENDER, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.BROKEN_SPAWNER, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, SlimefunItems.RUNE_ENDER})
		.register(true, (BlockPlaceHandler) (e, item) -> {
			if (SlimefunManager.isItemSimilar(item, SlimefunItems.REPAIRED_SPAWNER, false)) {
				EntityType type = null;
				
				for (String line: item.getItemMeta().getLore()) {
					if (ChatColor.stripColor(line).startsWith("Type: ") && !line.contains("<Type>")) {
						type = EntityType.valueOf(ChatColor.stripColor(line).replace("Type: ", "").replace(' ', '_').toUpperCase());
					}
				}
				
				if (type != null) {
					CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
					spawner.setSpawnedType(type);
					spawner.update(true, false);
				}
				
				return true;
			}
			else {
				return false;
			}
		});

		new EnhancedFurnace(1, 1, 1, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.FURNACE), SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(2, 1, 1, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE_2,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(2, 2, 1, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE_3,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_2, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(3, 2, 1, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE_4,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_3, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(3, 2, 2, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE_5,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_4, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(3, 3, 2, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE_6,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_5, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(4, 3, 2, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE_7,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_6, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(4, 4, 2, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE_8,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_7, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(5, 4, 2, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE_9,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_8, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(5, 5, 2, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE_10,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_9, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(5, 5, 3, (SlimefunItemStack) SlimefunItems.ENHANCED_FURNACE_11,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ENHANCED_FURNACE_10, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(10, 10, 3, (SlimefunItemStack) SlimefunItems.REINFORCED_FURNACE,
		new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_11, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT})
		.register(true);

		new EnhancedFurnace(20, 10, 3, (SlimefunItemStack) SlimefunItems.CARBONADO_EDGED_FURNACE,
		new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.CARBONADO, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_FURNACE, SlimefunItems.HEATING_COIL, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.ELECTRO_MAGNET, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.NICKEL_INGOT, SlimefunItems.MAGNET, SlimefunItems.COBALT_INGOT, null, SlimefunItems.BATTERY, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.ELECTRIC_MOTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, null, SlimefunItems.ELECTRO_MAGNET, null, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.HEATING_COIL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.COPPER_WIRE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT, null, null, null}, 
		new CustomItem(SlimefunItems.COPPER_WIRE, 8))
		.register(true);
		
		new BlockPlacer(Categories.MACHINES_1, (SlimefunItemStack) SlimefunItems.BLOCK_PLACER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_4K, new ItemStack(Material.PISTON), SlimefunItems.GOLD_4K, new ItemStack(Material.IRON_INGOT), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.IRON_INGOT), SlimefunItems.GOLD_4K, new ItemStack(Material.PISTON), SlimefunItems.GOLD_4K}, 
		new String[] {"unplaceable-blocks"}, new Object[] {MaterialCollections.getAllUnbreakableBlocks().stream().map(Material::name).collect(Collectors.toList())})
		.register(true);

		new TelepositionScroll(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.SCROLL_OF_DIMENSIONAL_TELEPOSITION, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, null})
		.register(true);

		new ExplosiveBow((SlimefunItemStack) SlimefunItems.EXPLOSIVE_BOW,
		new ItemStack[] {null, new ItemStack(Material.STICK), new ItemStack(Material.GUNPOWDER), SlimefunItems.STAFF_FIRE, null, SlimefunItems.SULFATE, null, new ItemStack(Material.STICK), new ItemStack(Material.GUNPOWDER)})
		.register(true);

		new IcyBow((SlimefunItemStack) SlimefunItems.ICY_BOW,
		new ItemStack[] {null, new ItemStack(Material.STICK), new ItemStack(Material.ICE), SlimefunItems.STAFF_WATER, null, new ItemStack(Material.PACKED_ICE), null, new ItemStack(Material.STICK), new ItemStack(Material.ICE)})
		.register(true);

		new KnowledgeTome(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.TOME_OF_KNOWLEDGE_SHARING, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.FEATHER), null, new ItemStack(Material.INK_SAC), SlimefunItems.MAGICAL_BOOK_COVER, new ItemStack(Material.GLASS_BOTTLE), null, new ItemStack(Material.WRITABLE_BOOK), null})
		.register(true);

		new KnowledgeFlask(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.FLASK_OF_KNOWLEDGE, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, null, null, SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.GLASS_PANE), SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, null}, 
		new CustomItem(SlimefunItems.FLASK_OF_KNOWLEDGE, 8))
		.register(true);

		new HandledBlock(Categories.BIRTHDAY, new SlimefunItemStack("BIRTHDAY_CAKE", Material.CAKE, "&bBirthday Cake"), RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.TORCH), null, new ItemStack(Material.SUGAR), new ItemStack(Material.CAKE), new ItemStack(Material.SUGAR), null, null, null})
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_MILK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.GLASS_BOTTLE), null, null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_MILK, 4))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CHRISTMAS_MILK, new ItemStack(Material.COCOA_BEANS), null, null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_EGG_NOG, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CHRISTMAS_MILK, new ItemStack(Material.EGG), null, null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_EGG_NOG, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_APPLE_CIDER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.APPLE_JUICE, new ItemStack(Material.SUGAR), null, null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_APPLE_CIDER, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_COOKIE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.COOKIE), new ItemStack(Material.SUGAR), new ItemStack(Material.LIME_DYE), null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_COOKIE, 16))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_FRUIT_CAKE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.EGG), new ItemStack(Material.APPLE), new ItemStack(Material.MELON), new ItemStack(Material.SUGAR), null, null, null, null, null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_FRUIT_CAKE, 4))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_APPLE_PIE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.APPLE), new ItemStack(Material.EGG), null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_APPLE_PIE, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_HOT_CHOCOLATE, RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, null, null, null, null, null, null, null, null}, SlimefunItems.CHRISTMAS_HOT_CHOCOLATE)
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_CAKE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.MILK_BUCKET), null, null, null, null, null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_CAKE, 4))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_CARAMEL, RecipeType.SMELTERY,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.SUGAR), null, null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_CARAMEL, 4))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_CARAMEL_APPLE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.CHRISTMAS_CARAMEL, null, null, new ItemStack(Material.APPLE), null, null, new ItemStack(Material.STICK), null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_CARAMEL_APPLE, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_CHOCOLATE_APPLE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.COCOA_BEANS), null, null, new ItemStack(Material.APPLE), null, null, new ItemStack(Material.STICK), null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_CHOCOLATE_APPLE, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.CHRISTMAS_PRESENT, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.NAME_TAG), null, new ItemStack(Material.RED_WOOL), new ItemStack(Material.GREEN_WOOL), new ItemStack(Material.RED_WOOL), new ItemStack(Material.RED_WOOL), new ItemStack(Material.GREEN_WOOL), new ItemStack(Material.RED_WOOL)})
		.register(true);

		new SlimefunItem(Categories.EASTER, (SlimefunItemStack) SlimefunItems.EASTER_CARROT_PIE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.CARROT), new ItemStack(Material.EGG), null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.EASTER_CARROT_PIE, 2))
		.register(true);

		new SlimefunItem(Categories.EASTER, (SlimefunItemStack) SlimefunItems.EASTER_APPLE_PIE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.APPLE), new ItemStack(Material.EGG), null, null, null, null, null, null}, 
		new CustomItem(SlimefunItems.CHRISTMAS_APPLE_PIE, 2))
		.register(true);

		new SlimefunItem(Categories.EASTER, (SlimefunItemStack) SlimefunItems.EASTER_EGG, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.LIME_DYE), new ItemStack(Material.EGG), new ItemStack(Material.PURPLE_DYE), null, null, null}, 
		new CustomItem(SlimefunItems.EASTER_EGG, 2))
		.register(true, new ItemUseHandler() {
			
			private final List<ItemStack> gifts = Arrays.asList(
				new CustomItem(SlimefunItems.EASTER_CARROT_PIE, 4),
				new CustomItem(SlimefunItems.CARROT_JUICE, 1),
				new ItemStack(Material.EMERALD),
				new ItemStack(Material.CAKE),
				new ItemStack(Material.RABBIT_FOOT),
				new ItemStack(Material.GOLDEN_CARROT, 4)
			);

			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				e.cancel();
				
				Player p = e.getPlayer();
				
				if (p.getGameMode() != GameMode.CREATIVE) {
					ItemUtils.consumeItem(e.getItem(), false);
				}
				
				FireworkUtils.launchRandom(p, 2);

				p.getWorld().dropItemNaturally(p.getLocation(), gifts.get(ThreadLocalRandom.current().nextInt(gifts.size())));
			}
		});

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.REINFORCED_PLATE, RecipeType.COMPRESSOR,
		new ItemStack[] {new CustomItem(SlimefunItems.REINFORCED_ALLOY_INGOT, 8), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.HARDENED_GLASS, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS)},
		new CustomItem(SlimefunItems.HARDENED_GLASS, 16))
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.COOLING_UNIT, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.ICE), new ItemStack(Material.ICE), new ItemStack(Material.ICE), SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_INGOT, new ItemStack(Material.ICE), new ItemStack(Material.ICE), new ItemStack(Material.ICE)})
		.register(true);

		new SlimefunBackpack(27, Categories.PORTABLE, (SlimefunItemStack) SlimefunItems.COOLER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.COOLING_UNIT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.WITHER_PROOF_OBSIDIAN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LEAD_INGOT, new ItemStack(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT, new ItemStack(Material.OBSIDIAN), SlimefunItems.HARDENED_GLASS, new ItemStack(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT, new ItemStack(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT},
		new CustomItem(SlimefunItems.WITHER_PROOF_OBSIDIAN, 4))
		.register(true);

		new AncientPedestal(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.ANCIENT_PEDESTAL, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.STONE), null, new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K, new ItemStack(Material.OBSIDIAN)}, 
		new CustomItem(SlimefunItems.ANCIENT_PEDESTAL, 4))
		.register(true);

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.ANCIENT_ALTAR, RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.ENCHANTING_TABLE), null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K, new ItemStack(Material.OBSIDIAN)})
		.register(true);

		new EnergyRegulator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ENERGY_REGULATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.DUCT_TAPE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.ALUMINUM_DUST, SlimefunItems.ALUMINUM_DUST, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.PAPER), new ItemStack(Material.PAPER), new ItemStack(Material.PAPER)}, 
		new CustomItem(SlimefunItems.DUCT_TAPE, 2))
		.register(true);

		new SlimefunItem(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.SMALL_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.DURALUMIN_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.DURALUMIN_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.SULFATE, new ItemStack(Material.REDSTONE), SlimefunItems.DURALUMIN_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.DURALUMIN_INGOT})
		.registerCapacitor(true, 128);

		new SlimefunItem(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.MEDIUM_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BILLON_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.BILLON_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.SMALL_CAPACITOR, new ItemStack(Material.REDSTONE), SlimefunItems.BILLON_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.BILLON_INGOT})
		.registerCapacitor(true, 512);

		new SlimefunItem(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.BIG_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.STEEL_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.MEDIUM_CAPACITOR, new ItemStack(Material.REDSTONE), SlimefunItems.STEEL_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.STEEL_INGOT})
		.registerCapacitor(true, 1024);

		new SlimefunItem(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.LARGE_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REINFORCED_ALLOY_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.BIG_CAPACITOR, new ItemStack(Material.REDSTONE), SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REINFORCED_ALLOY_INGOT})
		.registerCapacitor(true, 8192);

		new SlimefunItem(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.CARBONADO_EDGED_CAPACITOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.CARBONADO, new ItemStack(Material.REDSTONE), SlimefunItems.LARGE_CAPACITOR, new ItemStack(Material.REDSTONE), SlimefunItems.CARBONADO, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.CARBONADO})
		.registerCapacitor(true, 65536);

		new SolarGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.SOLAR_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SOLAR_PANEL, SlimefunItems.SOLAR_PANEL, SlimefunItems.SOLAR_PANEL, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_INGOT, null, SlimefunItems.ALUMINUM_INGOT, null}) {

			@Override
			public double getDayEnergy() {
				return 2;
			}
			
		}.register(true);

		new SolarGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.SOLAR_GENERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SOLAR_GENERATOR, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR, SlimefunItems.ALUMINUM_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR}) {

			@Override
			public double getDayEnergy() {
				return 8;
			}
			
		}.register(true);

		new SolarGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.SOLAR_GENERATOR_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SOLAR_GENERATOR_2, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR_2, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.CARBONADO, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR_2, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR_2}) {

			@Override
			public double getDayEnergy() {
				return 32;
			}
			
		}.register(true);

		new SolarGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.SOLAR_GENERATOR_4, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SOLAR_GENERATOR_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.SOLAR_GENERATOR_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.SOLAR_GENERATOR_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.SOLAR_GENERATOR_3}) {

			@Override
			public double getDayEnergy() {
				return 128;
			}

			@Override
			public double getNightEnergy() {
				return 64;
			}
			
		}.register(true);
		
		new ChargingBench(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.CHARGING_BENCH, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET, null, SlimefunItems.BATTERY, new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.BATTERY, null, SlimefunItems.SMALL_CAPACITOR, null})
		.registerChargeableBlock(true, 128);

		new ElectricFurnace(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_FURNACE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.FURNACE), null, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.GILDED_IRON}) {
			
			@Override
			public int getEnergyConsumption() {
				return 2;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 64);

		new ElectricFurnace(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_FURNACE_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_FURNACE, SlimefunItems.GILDED_IRON, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON}) {

			@Override
			public int getEnergyConsumption() {
				return 3;
			}

			@Override
			public int getSpeed() {
				return 2;
			}

		}.registerChargeableBlock(true, 128);

		new ElectricFurnace(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_FURNACE_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_FURNACE_2, SlimefunItems.STEEL_INGOT, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON}) {

			@Override
			public int getEnergyConsumption() {
				return 5;
			}

			@Override
			public int getSpeed() {
				return 4;
			}

		}.registerChargeableBlock(true, 128);

		new ElectricGoldPan(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_GOLD_PAN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GOLD_PAN, null, new ItemStack(Material.FLINT), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.FLINT), SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 1;
			}

			@Override
			public int getSpeed() {
				return 1;
			}
			
		}.registerChargeableBlock(true, 128);

		new ElectricGoldPan(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_GOLD_PAN_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GOLD_PAN, null, new ItemStack(Material.IRON_INGOT), SlimefunItems.ELECTRIC_GOLD_PAN, new ItemStack(Material.IRON_INGOT), SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 2;
			}

			@Override
			public int getSpeed() {
				return 3;
			}
			
		}.registerChargeableBlock(true, 128);

		new ElectricGoldPan(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_GOLD_PAN_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GOLD_PAN, null, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRIC_GOLD_PAN_2, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.COBALT_INGOT, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.COBALT_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 7;
			}

			@Override
			public int getSpeed() {
				return 10;
			}
			
		}.registerChargeableBlock(true, 512);

		new ElectricDustWasher(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_DUST_WASHER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.WATER_BUCKET), null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_GOLD_PAN, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 3;
			}

			@Override
			public int getSpeed() {
				return 1;
			}
			
		}.registerChargeableBlock(true, 128);

		new ElectricDustWasher(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_DUST_WASHER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.WATER_BUCKET), null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_DUST_WASHER, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 5;
			}

			@Override
			public int getSpeed() {
				return 2;
			}
			
		}.registerChargeableBlock(true, 128);

		new ElectricDustWasher(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_DUST_WASHER_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.WATER_BUCKET), null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_DUST_WASHER_2, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.CORINTHIAN_BRONZE_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 15;
			}

			@Override
			public int getSpeed() {
				return 10;
			}
			
		}.registerChargeableBlock(true, 512);

		new ElectricIngotFactory(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_INGOT_FACTORY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.FLINT_AND_STEEL), null, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_DUST_WASHER, SlimefunItems.HEATING_COIL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT}) {
			
			@Override
			public int getEnergyConsumption() {
				return 4;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 256);

		new ElectricIngotFactory(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_INGOT_FACTORY_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GILDED_IRON, new ItemStack(Material.FLINT_AND_STEEL), SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_INGOT_FACTORY, SlimefunItems.HEATING_COIL, SlimefunItems.BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BRASS_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 7;
			}

			@Override
			public int getSpeed() {
				return 2;
			}

		}.registerChargeableBlock(true, 256);

		new ElectricIngotFactory(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_INGOT_FACTORY_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GILDED_IRON, new ItemStack(Material.FLINT_AND_STEEL), SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_INGOT_FACTORY_2, SlimefunItems.HEATING_COIL, SlimefunItems.BRASS_INGOT, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.BRASS_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 20;
			}

			@Override
			public int getSpeed() {
				return 8;
			}

		}.registerChargeableBlock(true, 512);

		new ElectrifiedCrucible(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIFIED_CRUCIBLE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.CRUCIBLE, SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.LEAD_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 24;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 1024);

		new ElectrifiedCrucible(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIFIED_CRUCIBLE_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.ELECTRIFIED_CRUCIBLE, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.LEAD_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 40;
			}

			@Override
			public int getSpeed() {
				return 2;
			}

		}.registerChargeableBlock(true, 1024);

		new ElectrifiedCrucible(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIFIED_CRUCIBLE_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.ELECTRIFIED_CRUCIBLE_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.STEEL_PLATE, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_PLATE, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 60;
			}

			@Override
			public int getSpeed() {
				return 4;
			}

		}.registerChargeableBlock(true, 1024);

		new ElectricOreGrinder(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_ORE_GRINDER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.DIAMOND_PICKAXE), null, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.GILDED_IRON}) {
			
			@Override
			public int getEnergyConsumption() {
				return 6;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 128);

		new ElectricOreGrinder(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_ORE_GRINDER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.DIAMOND_PICKAXE), null, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_ORE_GRINDER, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GILDED_IRON}) {

			@Override
			public int getEnergyConsumption() {
				return 15;
			}

			@Override
			public int getSpeed() {
				return 4;
			}

		}.registerChargeableBlock(true, 512);

		new HeatedPressureChamber(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.HEATED_PRESSURE_CHAMBER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, new ItemStack(Material.GLASS), SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.LEAD_INGOT}) {
			
			@Override
			public int getEnergyConsumption() {
				return 5;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 128);

		new HeatedPressureChamber(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.HEATED_PRESSURE_CHAMBER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATED_PRESSURE_CHAMBER, SlimefunItems.LEAD_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_ALLOY_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 22;
			}

			@Override
			public int getSpeed() {
				return 5;
			}

		}.registerChargeableBlock(true, 256);

		new ElectricIngotPulverizer(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_INGOT_PULVERIZER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRIC_ORE_GRINDER, null, SlimefunItems.LEAD_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.LEAD_INGOT})
		.registerChargeableBlock(true, 512);

		new CoalGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.COAL_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HEATING_COIL, new ItemStack(Material.FURNACE), SlimefunItems.HEATING_COIL, SlimefunItems.NICKEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.NICKEL_INGOT, null, SlimefunItems.NICKEL_INGOT, null}) {
			
			@Override
			public int getEnergyProduction() {
				return 8;
			}

		}.registerEnergyGenerator(true, 64);

		new CoalGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.COAL_GENERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.MAGMA_BLOCK), SlimefunItems.HEATING_COIL, new ItemStack(Material.MAGMA_BLOCK), SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.COAL_GENERATOR, SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.ELECTRIC_MOTOR, null}) {
			
			@Override
			public int getEnergyProduction() {
				return 15;
			}

		}.registerEnergyGenerator(true, 256);

		new BioGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.BIO_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HEATING_COIL, SlimefunItems.COMPOSTER, SlimefunItems.HEATING_COIL, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT, null, SlimefunItems.ALUMINUM_BRASS_INGOT, null}) {

			@Override
			public int getEnergyProduction() {
				return 4;
			}

		}.registerEnergyGenerator(true, 128);

		new AutoDrier(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.AUTO_DRIER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, SlimefunItems.HEATING_COIL, new ItemStack(Material.SMOKER), SlimefunItems.HEATING_COIL, null, new ItemStack(Material.CAMPFIRE), null})
		.registerChargeableBlock(true, 128);

		new ElectricPress(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_PRESS, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.PISTON), null, SlimefunItems.MEDIUM_CAPACITOR, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 8;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 256);

		new ElectricPress(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_PRESS_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.STICKY_PISTON), SlimefunItems.ELECTRIC_PRESS, new ItemStack(Material.STICKY_PISTON), SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.BIG_CAPACITOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT}) {

			@Override
			public int getEnergyConsumption() {
				return 20;
			}

			@Override
			public int getSpeed() {
				return 3;
			}

		}.registerChargeableBlock(true, 1024);
		
		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.MAGNESIUM_SALT, RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.MAGNESIUM_DUST, SlimefunItems.SALT, null, null, null, null, null, null, null})
		.register(true);
		
		new MagnesiumGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.MAGNESIUM_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.COMPRESSED_CARBON, new ItemStack(Material.WATER_BUCKET), SlimefunItems.COMPRESSED_CARBON, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT}) {
			
			@Override
			public int getEnergyProduction() {
				return 18;
			}

		}.registerEnergyGenerator(true, 128);

		new AutoEnchanter(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.AUTO_ENCHANTER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.ENCHANTING_TABLE), null, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN})
		.registerChargeableBlock(true, 128);

		new AutoDisenchanter(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.AUTO_DISENCHANTER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.REDSTONE), new ItemStack(Material.ANVIL), new ItemStack(Material.REDSTONE), SlimefunItems.CARBONADO, SlimefunItems.AUTO_ENCHANTER, SlimefunItems.CARBONADO, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN})
		.registerChargeableBlock(true, 128);

		new AutoAnvil(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.AUTO_ANVIL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.ANVIL), null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK)}) {

			@Override
			public int getRepairFactor() {
				return 10;
			}

			@Override
			public int getEnergyConsumption() {
				return 12;
			}

		}.registerChargeableBlock(true, 128);

		new AutoAnvil(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.AUTO_ANVIL_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.AUTO_ANVIL, null, SlimefunItems.STEEL_PLATE, SlimefunItems.HEATING_COIL, SlimefunItems.STEEL_PLATE, new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK)}) {

			@Override
			public int getRepairFactor() {
				return 4;
			}

			@Override
			public int getEnergyConsumption() {
				return 16;
			}

		}.registerChargeableBlock(true, 256);

		new Multimeter(Categories.TECH, (SlimefunItemStack) SlimefunItems.MULTIMETER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.COPPER_INGOT, null, SlimefunItems.COPPER_INGOT, null, SlimefunItems.REDSTONE_ALLOY, null, null, SlimefunItems.GOLD_6K, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.PLASTIC_SHEET, RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {null, null, null, null, SlimefunItems.BUCKET_OF_OIL, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.ANDROID_MEMORY_CORE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BRASS_INGOT, new ItemStack(Material.ORANGE_STAINED_GLASS), SlimefunItems.BRASS_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.TIN_DUST, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BRASS_INGOT, new ItemStack(Material.ORANGE_STAINED_GLASS), SlimefunItems.BRASS_INGOT})
		.register(true);

		new GPSTransmitter(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_TRANSMITTER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.STEEL_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT}) {
			
			@Override
			public int getMultiplier(int y) {
				return y;
			}

			@Override
			public int getEnergyConsumption() {
				return 1;
			}
			
		}
		.registerChargeableBlock(true, 16);

		new GPSTransmitter(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_TRANSMITTER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.CARBON, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER}) {
			
			@Override
			public int getMultiplier(int y) {
				return y * 4 + 100;
			}

			@Override
			public int getEnergyConsumption() {
				return 3;
			}
			
		}.registerChargeableBlock(true, 64);

		new GPSTransmitter(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_TRANSMITTER_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.CARBONADO, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2}) {
			
			@Override
			public int getMultiplier(int y) {
				return y * 16 + 500;
			}

			@Override
			public int getEnergyConsumption() {
				return 11;
			}
			
		}.registerChargeableBlock(true, 256);

		new GPSTransmitter(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_TRANSMITTER_4, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.NICKEL_INGOT, SlimefunItems.CARBONADO, SlimefunItems.NICKEL_INGOT, SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GPS_TRANSMITTER_3}) {
			
			@Override
			public int getMultiplier(int y) {
				return y * 64 + 600;
			}

			@Override
			public int getEnergyConsumption() {
				return 46;
			}
			
		}.registerChargeableBlock(true, 1024);

		new GPSControlPanel(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_CONTROL_PANEL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.COBALT_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.COBALT_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT})
		.register(true);

		new SlimefunItem(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_MARKER_TOOL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET, null, new ItemStack(Material.LAPIS_LAZULI), SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.LAPIS_LAZULI), new ItemStack(Material.REDSTONE), SlimefunItems.REDSTONE_ALLOY, new ItemStack(Material.REDSTONE)})
		.register(true);

		new SlimefunItem(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_EMERGENCY_TRANSMITTER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET, null, null, SlimefunItems.GPS_TRANSMITTER, null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new ProgrammableAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.PLASTIC_SHEET, SlimefunItems.COAL_GENERATOR, SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.CHEST), SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.NONE;
			}

			@Override
			public float getFuelEfficiency() {
				return 1;
			}

			@Override
			public int getTier() {
				return 1;
			}

		}
		.register(true);

		new MinerAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_MINER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_PICKAXE), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_PICKAXE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public float getFuelEfficiency() {
				return 1;
			}

			@Override
			public int getTier() {
				return 1;
			}

		}
		.register(true);

		new FarmerAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_FARMER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_HOE), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_HOE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public float getFuelEfficiency() {
				return 1;
			}

			@Override
			public int getTier() {
				return 1;
			}

		}
		.register(true);

		new WoodcutterAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_AXE), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_AXE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public float getFuelEfficiency() {
				return 1;
			}

			@Override
			public int getTier() {
				return 1;
			}

		}
		.register(true);

		new FisherAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_FISHERMAN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public float getFuelEfficiency() {
				return 1;
			}

			@Override
			public int getTier() {
				return 1;
			}

		}
		.register(true);

		new ButcherAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_BUTCHER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public float getFuelEfficiency() {
				return 1;
			}

			@Override
			public int getTier() {
				return 1;
			}

		}
		.register(true);

		new SlimefunItem(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ANDROID_INTERFACE_ITEMS, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.BLUE_STAINED_GLASS), SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET})
		.register(true);

		new SlimefunItem(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ANDROID_INTERFACE_FUEL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.RED_STAINED_GLASS), SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET})
		.register(true);

		new ProgrammableAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.PLASTIC_SHEET, SlimefunItems.COMBUSTION_REACTOR, SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.CHEST), SlimefunItems.PLASTIC_SHEET, SlimefunItems.POWER_CRYSTAL, SlimefunItems.PLASTIC_SHEET}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.NONE;
			}

			@Override
			public float getFuelEfficiency() {
				return 1.5F;
			}

			@Override
			public int getTier() {
				return 2;
			}

		}
		.register(true);

		new FisherAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_2_FISHERMAN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public float getFuelEfficiency() {
				return 1.5F;
			}

			@Override
			public int getTier() {
				return 2;
			}

		}
		.register(true);

		new ButcherAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_2_BUTCHER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public float getFuelEfficiency() {
				return 1.5F;
			}

			@Override
			public int getTier() {
				return 2;
			}

		}
		.register(true);

		new AdvancedFarmerAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_2_FARMER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, new ItemStack(Material.DIAMOND_HOE), SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.DIAMOND_HOE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public float getFuelEfficiency() {
				return 1.5F;
			}

			@Override
			public int getTier() {
				return 2;
			}

		}
		.register(true);

		new ProgrammableAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.PLASTIC_SHEET, SlimefunItems.NUCLEAR_REACTOR, SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.CHEST), SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BLISTERING_INGOT_3}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.NONE;
			}

			@Override
			public float getFuelEfficiency() {
				return 1F;
			}

			@Override
			public int getTier() {
				return 3;
			}

		}
		.register(true);

		new FisherAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_3_FISHERMAN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID_3, new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public float getFuelEfficiency() {
				return 1F;
			}

			@Override
			public int getTier() {
				return 3;
			}

		}
		.register(true);

		new ButcherAndroid(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.PROGRAMMABLE_ANDROID_3_BUTCHER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER_3, null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID_3, new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public float getFuelEfficiency() {
				return 1F;
			}

			@Override
			public int getTier() {
				return 3;
			}

		}
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.BLANK_RUNE, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.OBSIDIAN), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.STONE)})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.RUNE_AIR, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.FEATHER), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.FEATHER), new ItemStack(Material.GHAST_TEAR), SlimefunItems.BLANK_RUNE, new ItemStack(Material.GHAST_TEAR), new ItemStack(Material.FEATHER), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.FEATHER)}, 
		new CustomItem(SlimefunItems.RUNE_AIR, 4))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.RUNE_EARTH, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.DIRT), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.STONE), new ItemStack(Material.OBSIDIAN), SlimefunItems.BLANK_RUNE, new ItemStack(Material.OBSIDIAN), new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.DIRT)}, 
		new CustomItem(SlimefunItems.RUNE_EARTH, 4))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.RUNE_FIRE, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.FIRE_CHARGE), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.FIRE_CHARGE), new ItemStack(Material.BLAZE_POWDER), SlimefunItems.RUNE_EARTH, new ItemStack(Material.FLINT_AND_STEEL), new ItemStack(Material.FIRE_CHARGE), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.FIRE_CHARGE)}, 
		new CustomItem(SlimefunItems.RUNE_FIRE, 4))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.RUNE_WATER, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.SALMON), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.SAND), SlimefunItems.BLANK_RUNE, new ItemStack(Material.SAND), new ItemStack(Material.WATER_BUCKET), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.COD)}, 
		new CustomItem(SlimefunItems.RUNE_WATER, 4))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.RUNE_ENDER, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_EYE), SlimefunItems.BLANK_RUNE, new ItemStack(Material.ENDER_EYE), new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.ENDER_PEARL)}, 
		new CustomItem(SlimefunItems.RUNE_ENDER, 6))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.RUNE_LIGHTNING, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.IRON_INGOT), SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.IRON_INGOT), SlimefunItems.RUNE_AIR, new ItemStack(Material.PHANTOM_MEMBRANE), SlimefunItems.RUNE_WATER, new ItemStack(Material.IRON_INGOT), SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.IRON_INGOT)}, 
		new CustomItem(SlimefunItems.RUNE_LIGHTNING, 4))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.RUNE_RAINBOW, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.CYAN_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RUNE_ENDER, new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.YELLOW_DYE), SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.MAGENTA_DYE)})
		.register(true);

		new SoulboundRune(Categories.LUMPS_AND_MAGIC, (SlimefunItemStack) SlimefunItems.RUNE_SOULBOUND, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_ENDER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.MAGIC_LUMP_3})
		.register(true);

		new InfernalBonemeal(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.INFERNAL_BONEMEAL, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.NETHER_WART), SlimefunItems.RUNE_EARTH, new ItemStack(Material.NETHER_WART), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.BONE_MEAL), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.NETHER_WART), new ItemStack(Material.BLAZE_POWDER), new ItemStack(Material.NETHER_WART)}, 
		new CustomItem(SlimefunItems.INFERNAL_BONEMEAL, 8))
		.register(true);

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.ELYTRA_SCALE, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_AIR, SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.PHANTOM_MEMBRANE), new ItemStack(Material.FEATHER), new ItemStack(Material.PHANTOM_MEMBRANE), SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_AIR, SlimefunItems.ENDER_LUMP_3})
		.register(true);

		new VanillaItem(Categories.MAGIC, SlimefunItems.ELYTRA, "ELYTRA", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.ELYTRA_SCALE, SlimefunItems.RUNE_AIR, SlimefunItems.ELYTRA_SCALE, SlimefunItems.RUNE_AIR, new ItemStack(Material.LEATHER_CHESTPLATE), SlimefunItems.RUNE_AIR, SlimefunItems.ELYTRA_SCALE, SlimefunItems.RUNE_AIR, SlimefunItems.ELYTRA_SCALE})
		.register(true);

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.INFUSED_ELYTRA, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.FLASK_OF_KNOWLEDGE})
		.register(true);

		new SoulboundItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.SOULBOUND_ELYTRA, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.ELYTRA, SlimefunItems.ELYTRA_SCALE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.FLASK_OF_KNOWLEDGE})
		.register(true);

		new VanillaItem(Categories.MAGIC, SlimefunItems.TOTEM_OF_UNDYING, "TOTEM_OF_UNDYING", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.ESSENCE_OF_AFTERLIFE, new ItemStack(Material.EMERALD_BLOCK), SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.TALISMAN, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, new ItemStack(Material.EMERALD_BLOCK), SlimefunItems.ESSENCE_OF_AFTERLIFE})
		.register(true);

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.RAINBOW_WOOL, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL)}, 
		new CustomItem(SlimefunItems.RAINBOW_WOOL, 8))
		.register(true, new RainbowTicker(MaterialCollections.getAllWoolColors()));

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.RAINBOW_GLASS, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS)}, 
		new CustomItem(SlimefunItems.RAINBOW_GLASS, 8))
		.register(true, new RainbowTicker(MaterialCollections.getAllStainedGlassColors()));

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.RAINBOW_GLASS_PANE, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE)}, 
		new CustomItem(SlimefunItems.RAINBOW_GLASS_PANE, 8))
		.register(true, new RainbowTicker(MaterialCollections.getAllStainedGlassPaneColors()));

		new SlimefunItem(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.RAINBOW_CLAY, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA)}, 
		new CustomItem(SlimefunItems.RAINBOW_CLAY, 8))
		.register(true, new RainbowTicker(MaterialCollections.getAllTerracottaColors()));

		// Christmas

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.RAINBOW_WOOL_XMAS, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.RED_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_WOOL_XMAS, 2))
		.register(true, new RainbowTicker(Material.RED_WOOL, Material.GREEN_WOOL));

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.RAINBOW_GLASS_XMAS, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.RED_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_GLASS_XMAS, 2))
		.register(true, new RainbowTicker(Material.RED_STAINED_GLASS, Material.GREEN_STAINED_GLASS));

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.RAINBOW_GLASS_PANE_XMAS, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.RED_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_GLASS_PANE_XMAS, 2))
		.register(true, new RainbowTicker(Material.RED_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE));

		new SlimefunItem(Categories.CHRISTMAS, (SlimefunItemStack) SlimefunItems.RAINBOW_CLAY_XMAS, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.RED_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_CLAY_XMAS, 2))
		.register(true, new RainbowTicker(Material.RED_TERRACOTTA, Material.GREEN_TERRACOTTA));

		// Valentines Day

		new SlimefunItem(Categories.VALENTINES_DAY, (SlimefunItemStack) SlimefunItems.RAINBOW_WOOL_VALENTINE, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_WOOL_VALENTINE, 2))
		.register(true, new RainbowTicker(Material.MAGENTA_WOOL, Material.PINK_WOOL));

		new SlimefunItem(Categories.VALENTINES_DAY, (SlimefunItemStack) SlimefunItems.RAINBOW_GLASS_VALENTINE, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_GLASS_VALENTINE, 2))
		.register(true, new RainbowTicker(Material.MAGENTA_STAINED_GLASS, Material.PINK_STAINED_GLASS));

		new SlimefunItem(Categories.VALENTINES_DAY, (SlimefunItemStack) SlimefunItems.RAINBOW_GLASS_PANE_VALENTINE, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_GLASS_PANE_VALENTINE, 2))
		.register(true, new RainbowTicker(Material.MAGENTA_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE));

		new SlimefunItem(Categories.VALENTINES_DAY, (SlimefunItemStack) SlimefunItems.RAINBOW_CLAY_VALENTINE, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_CLAY_VALENTINE, 2))
		.register(true, new RainbowTicker(Material.MAGENTA_TERRACOTTA, Material.PINK_TERRACOTTA));

		// Halloween
		
		new SlimefunItem(Categories.HALLOWEEN, (SlimefunItemStack) SlimefunItems.RAINBOW_WOOL_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.ORANGE_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_WOOL_HALLOWEEN, 2))
		.register(true, new RainbowTicker(Material.ORANGE_WOOL, Material.BLACK_WOOL));

		new SlimefunItem(Categories.HALLOWEEN, (SlimefunItemStack) SlimefunItems.RAINBOW_GLASS_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.ORANGE_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_GLASS_HALLOWEEN, 2))
		.register(true, new RainbowTicker(Material.ORANGE_STAINED_GLASS, Material.BLACK_STAINED_GLASS));

		new SlimefunItem(Categories.HALLOWEEN, (SlimefunItemStack) SlimefunItems.RAINBOW_GLASS_PANE_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.ORANGE_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_GLASS_PANE_HALLOWEEN, 2))
		.register(true, new RainbowTicker(Material.ORANGE_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS_PANE));

		new SlimefunItem(Categories.HALLOWEEN, (SlimefunItemStack) SlimefunItems.RAINBOW_CLAY_HALLOWEEN, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.PUMPKIN), new ItemStack(Material.ORANGE_DYE)}, 
		new CustomItem(SlimefunItems.RAINBOW_CLAY_HALLOWEEN, 2))
		.register(true, new RainbowTicker(Material.ORANGE_TERRACOTTA, Material.BLACK_TERRACOTTA));

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.WITHER_PROOF_GLASS, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.LEAD_INGOT, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.HARDENED_GLASS, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.LEAD_INGOT, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.LEAD_INGOT}, 
		new CustomItem(SlimefunItems.WITHER_PROOF_GLASS, 4))
		.register(true);

		new GEOScannerBlock(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_GEO_SCANNER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, null, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRO_MAGNET})
		.register(true);

		new PortableGEOScanner(Categories.GPS, (SlimefunItemStack) SlimefunItems.PORTABLE_GEO_SCANNER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ELECTRO_MAGNET, new ItemStack(Material.COMPASS), SlimefunItems.ELECTRO_MAGNET, SlimefunItems.STEEL_INGOT, SlimefunItems.GPS_MARKER_TOOL, SlimefunItems.STEEL_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.SOLDER_INGOT})
		.register(true);

		new OilPump(Categories.GPS, (SlimefunItemStack) SlimefunItems.OIL_PUMP, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT, null, new ItemStack(Material.BUCKET), null}) {

			@Override
			public int getEnergyConsumption() {
				return 14;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 200);

		new GEOMiner(Categories.GPS, (SlimefunItemStack) SlimefunItems.GEO_MINER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.DIAMOND_PICKAXE), SlimefunItems.MEDIUM_CAPACITOR, new ItemStack(Material.DIAMOND_PICKAXE), SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.OIL_PUMP, SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.ELECTRIC_MOTOR, null}) {
			
			@Override
			public int getSpeed() {
				return 1;
			}
			
			@Override
			public int getEnergyConsumption() {
				return 24;
			}
			
		}.registerChargeableBlock(true, 512);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.BUCKET_OF_OIL, new RecipeType(SlimefunItems.OIL_PUMP),
		new ItemStack[] {null, null, null, null, new ItemStack(Material.BUCKET), null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, (SlimefunItemStack) SlimefunItems.BUCKET_OF_FUEL, new RecipeType(SlimefunItems.REFINERY),
		new ItemStack[] {null, null, null, null, SlimefunItems.BUCKET_OF_OIL, null, null, null, null})
		.register(true);

		new RadioactiveItem(Categories.RESOURCES, Radioactivity.MODERATE, (SlimefunItemStack) SlimefunItems.NETHER_ICE, new RecipeType(SlimefunItems.GEO_MINER),
		new ItemStack[] {null, null, null, null, null, null, null, null, null})
		.register(true);

		new Refinery(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.REFINERY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HARDENED_GLASS, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HARDENED_GLASS, new ItemStack(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.PISTON)}) {

			@Override
			public int getEnergyConsumption() {
				return 16;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 256);

		new LavaGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.LAVA_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GOLD_16K, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL}) {
			
			@Override
			public int getEnergyProduction() {
				return 10;
			}

		}.registerEnergyGenerator(true, 512);

		new LavaGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.LAVA_GENERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.LAVA_GENERATOR, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.HEATING_COIL, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.HEATING_COIL}) {
			
			@Override
			public int getEnergyProduction() {
				return 20;
			}

		}.registerEnergyGenerator(true, 1024);

		new CombustionGenerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.COMBUSTION_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.STEEL_INGOT, SlimefunItems.HEATING_COIL}) {
			
			@Override
			public int getEnergyProduction() {
				return 12;
			}

		}.registerEnergyGenerator(true, 256);

		new SlimefunItem(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_TELEPORTER_PYLON, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ZINC_INGOT, new ItemStack(Material.GLASS), SlimefunItems.ZINC_INGOT, new ItemStack(Material.GLASS), SlimefunItems.HEATING_COIL, new ItemStack(Material.GLASS), SlimefunItems.ZINC_INGOT, new ItemStack(Material.GLASS), SlimefunItems.ZINC_INGOT}, 
		new CustomItem(SlimefunItems.GPS_TELEPORTER_PYLON, 8))
		.register(true, new RainbowTicker(Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS));

		new Teleporter(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_TELEPORTATION_MATRIX, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TELEPORTER_PYLON, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.GPS_TELEPORTER_PYLON, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.GPS_CONTROL_PANEL, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.GPS_TELEPORTER_PYLON, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.GPS_TELEPORTER_PYLON})
		.register(true);

		new SlimefunItem(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_ACTIVATION_DEVICE_SHARED, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.STONE_PRESSURE_PLATE), null, new ItemStack(Material.REDSTONE), SlimefunItems.GPS_TRANSMITTER, new ItemStack(Material.REDSTONE), SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT})
		.register(true);

		new PersonalActivationPlate(Categories.GPS, (SlimefunItemStack) SlimefunItems.GPS_ACTIVATION_DEVICE_PERSONAL, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.LEAD_INGOT, null, SlimefunItems.COBALT_INGOT, SlimefunItems.GPS_ACTIVATION_DEVICE_SHARED, SlimefunItems.COBALT_INGOT, null, SlimefunItems.LEAD_INGOT, null})
		.register(true);

		new InfusedHopper(Categories.MAGIC, (SlimefunItemStack) SlimefunItems.INFUSED_HOPPER, RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.OBSIDIAN), SlimefunItems.RUNE_EARTH, new ItemStack(Material.HOPPER), SlimefunItems.RUNE_ENDER, SlimefunItems.INFUSED_MAGNET, SlimefunItems.RUNE_ENDER, new ItemStack(Material.HOPPER), SlimefunItems.RUNE_EARTH, new ItemStack(Material.OBSIDIAN)})
		.register(true);

		new RadioactiveItem(Categories.RESOURCES, Radioactivity.HIGH, (SlimefunItemStack) SlimefunItems.BLISTERING_INGOT, RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.URANIUM, null, null, null, null, null, null, null})
		.register(true);

		new RadioactiveItem(Categories.RESOURCES, Radioactivity.VERY_HIGH, (SlimefunItemStack) SlimefunItems.BLISTERING_INGOT_2, RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.BLISTERING_INGOT, SlimefunItems.CARBONADO, null, null, null, null, null, null, null})
		.register(true);

		new RadioactiveItem(Categories.RESOURCES, Radioactivity.VERY_HIGH, (SlimefunItemStack) SlimefunItems.BLISTERING_INGOT_3, RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.BLISTERING_INGOT_2, new ItemStack(Material.NETHER_STAR), null, null, null, null, null, null, null})
		.register(true);

		new RadioactiveItem(Categories.RESOURCES, Radioactivity.VERY_HIGH, (SlimefunItemStack) SlimefunItems.ENRICHED_NETHER_ICE, RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.NETHER_ICE, SlimefunItems.PLUTONIUM, null, null, null, null, null, null, null})
		.register(true);

		new ElevatorPlate(Categories.GPS, (SlimefunItemStack) SlimefunItems.ELEVATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.STONE_PRESSURE_PLATE), null, new ItemStack(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.PISTON), SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT},
		new CustomItem(SlimefunItems.ELEVATOR, 2))
		.register(true);

		new FoodFabricator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.FOOD_FABRICATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BILLON_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.CAN, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.CAN, null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public int getEnergyConsumption() {
				return 7;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 256);

		new FoodFabricator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.FOOD_FABRICATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.FOOD_FABRICATOR, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.ELECTRO_MAGNET, null}) {
			
			@Override
			public int getEnergyConsumption() {
				return 24;
			}

			@Override
			public int getSpeed() {
				return 6;
			}

		}.registerChargeableBlock(true, 512);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.WHEAT_ORGANIC_FOOD, RecipeType.FOOD_FABRICATOR,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.WHEAT), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.CARROT_ORGANIC_FOOD, RecipeType.FOOD_FABRICATOR,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.CARROT), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.POTATO_ORGANIC_FOOD, RecipeType.FOOD_FABRICATOR,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.POTATO), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.SEEDS_ORGANIC_FOOD, RecipeType.FOOD_FABRICATOR,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.WHEAT_SEEDS), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.BEETROOT_ORGANIC_FOOD, RecipeType.FOOD_FABRICATOR,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.BEETROOT), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.MELON_ORGANIC_FOOD, RecipeType.FOOD_FABRICATOR,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.MELON), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.APPLE_ORGANIC_FOOD, RecipeType.FOOD_FABRICATOR,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.APPLE), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.SWEET_BERRIES_ORGANIC_FOOD, RecipeType.FOOD_FABRICATOR,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.SWEET_BERRIES), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.KELP_ORGANIC_FOOD, RecipeType.FOOD_FABRICATOR,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.DRIED_KELP), null, null, null, null, null, null, null})
		.register(true);

		new AutoBreeder(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.AUTO_BREEDER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_18K, SlimefunItems.CAN, SlimefunItems.GOLD_18K, SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.HAY_BLOCK), SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.FOOD_FABRICATOR, SlimefunItems.LEAD_INGOT})
		.registerChargeableBlock(true, 1024);

		new AnimalGrowthAccelerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ANIMAL_GROWTH_ACCELERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.WHEAT_ORGANIC_FOOD, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.AUTO_BREEDER, SlimefunItems.REINFORCED_ALLOY_INGOT})
		.registerChargeableBlock(true, 1024);

		new XPCollector(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.XP_COLLECTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.AUTO_ENCHANTER, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRONZE_INGOT})
		.registerChargeableBlock(true, 1024);

		new FoodComposter(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.FOOD_COMPOSTER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.FOOD_FABRICATOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.CAN, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.CAN, null, SlimefunItems.ELECTRIC_MOTOR, null}) {
			
			@Override
			public int getEnergyConsumption() {
				return 8;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 256);

		new FoodComposter(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.FOOD_COMPOSTER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.FOOD_COMPOSTER, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.ELECTRO_MAGNET, null}) {

			@Override
			public int getEnergyConsumption() {
				return 26;
			}

			@Override
			public int getSpeed() {
				return 10;
			}

		}.registerChargeableBlock(true, 256);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.WHEAT_FERTILIZER, RecipeType.FOOD_COMPOSTER,
		new ItemStack[] {SlimefunItems.WHEAT_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.CARROT_FERTILIZER, RecipeType.FOOD_COMPOSTER,
		new ItemStack[] {SlimefunItems.CARROT_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.POTATO_FERTILIZER, RecipeType.FOOD_COMPOSTER,
		new ItemStack[] {SlimefunItems.POTATO_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.SEEDS_FERTILIZER, RecipeType.FOOD_COMPOSTER,
		new ItemStack[] {SlimefunItems.SEEDS_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.BEETROOT_FERTILIZER, RecipeType.FOOD_COMPOSTER,
		new ItemStack[] {SlimefunItems.BEETROOT_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.MELON_FERTILIZER, RecipeType.FOOD_COMPOSTER,
		new ItemStack[] {SlimefunItems.MELON_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.APPLE_FERTILIZER, RecipeType.FOOD_COMPOSTER,
		new ItemStack[] {SlimefunItems.APPLE_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.SWEET_BERRIES_FERTILIZER, RecipeType.FOOD_COMPOSTER,
		new ItemStack[] {SlimefunItems.SWEET_BERRIES_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, (SlimefunItemStack) SlimefunItems.KELP_FERTILIZER, RecipeType.FOOD_COMPOSTER,
		new ItemStack[] {SlimefunItems.KELP_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new CropGrowthAccelerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.CROP_GROWTH_ACCELERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
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

		}.registerChargeableBlock(true, 1024);

		new CropGrowthAccelerator(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.CROP_GROWTH_ACCELERATOR_2, RecipeType.ENHANCED_CRAFTING_TABLE,
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

		}.registerChargeableBlock(true, 1024);

		new Freezer(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.FREEZER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.SILVER_INGOT, null, SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.PACKED_ICE), SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.COOLING_UNIT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.COOLING_UNIT}) {
			
			@Override
			public int getEnergyConsumption() {
				return 9;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 256);

		new Freezer(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.FREEZER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.SILVER_INGOT, null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.FREEZER, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.COOLING_UNIT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.COOLING_UNIT}) {

			@Override
			public int getEnergyConsumption() {
				return 15;
			}

			@Override
			public int getSpeed() {
				return 2;
			}

		}.registerChargeableBlock(true, 256);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.REACTOR_COOLANT_CELL, new RecipeType(SlimefunItems.FREEZER),
		new ItemStack[] {new ItemStack(Material.BLUE_ICE), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, (SlimefunItemStack) SlimefunItems.NETHER_ICE_COOLANT_CELL, new RecipeType(SlimefunItems.HEATED_PRESSURE_CHAMBER),
		new ItemStack[] {SlimefunItems.ENRICHED_NETHER_ICE, null, null, null, null, null, null, null, null})
		.register(true);

		new RadioactiveItem(Categories.RESOURCES, Radioactivity.HIGH, (SlimefunItemStack) SlimefunItems.NEPTUNIUM, new RecipeType(SlimefunItems.NUCLEAR_REACTOR),
		new ItemStack[] {SlimefunItems.URANIUM, null, null, null, null, null, null, null, null})
		.register(true);

		new RadioactiveItem(Categories.RESOURCES, Radioactivity.VERY_HIGH, (SlimefunItemStack) SlimefunItems.PLUTONIUM, new RecipeType(SlimefunItems.NUCLEAR_REACTOR),
		new ItemStack[] {SlimefunItems.NEPTUNIUM, null, null, null, null, null, null, null, null})
		.register(true);

		new RadioactiveItem(Categories.RESOURCES, Radioactivity.VERY_HIGH, (SlimefunItemStack) SlimefunItems.BOOSTED_URANIUM, RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.PLUTONIUM, SlimefunItems.URANIUM, null, null, null, null, null, null, null})
		.register(true);

		new NuclearReactor(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.NUCLEAR_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.REINFORCED_PLATE, SlimefunItems.COOLING_UNIT, SlimefunItems.REINFORCED_PLATE, SlimefunItems.LEAD_INGOT, SlimefunItems.REINFORCED_PLATE, SlimefunItems.LEAD_INGOT}){
			
			@Override
			public int getEnergyProduction() {
				return 250;
			}
			
		}.registerChargeableBlock(true, 16384);

		new NetherStarReactor(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.NETHERSTAR_REACTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BOOSTED_URANIUM, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BOOSTED_URANIUM, SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.NETHER_STAR), SlimefunItems.REINFORCED_PLATE, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.REINFORCED_PLATE, SlimefunItems.CORINTHIAN_BRONZE_INGOT}){

			@Override
			public int getEnergyProduction() {
				return 512;
			}

		}.registerChargeableBlock(true, 32768);

		new SlimefunItem(Categories.CARGO, (SlimefunItemStack) SlimefunItems.CARGO_MOTOR, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HARDENED_GLASS, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.HARDENED_GLASS, SlimefunItems.SILVER_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.SILVER_INGOT, SlimefunItems.HARDENED_GLASS, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.HARDENED_GLASS}, 
		new CustomItem(SlimefunItems.CARGO_MOTOR, 4))
		.register(true);

		new CargoManagerBlock(Categories.CARGO, (SlimefunItemStack) SlimefunItems.CARGO_MANAGER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.HOLOGRAM_PROJECTOR, null, SlimefunItems.REINFORCED_PLATE, SlimefunItems.CARGO_MOTOR, SlimefunItems.REINFORCED_PLATE, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.ALUMINUM_BRONZE_INGOT})
		.register(true);

		new CargoConnector(Categories.CARGO, (SlimefunItemStack) SlimefunItems.CARGO_NODE, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT}, 
		new CustomItem(SlimefunItems.CARGO_NODE, 4))
		.register(true);

		new CargoInputNode(Categories.CARGO, (SlimefunItemStack) SlimefunItems.CARGO_INPUT, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.HOPPER), null, SlimefunItems.BILLON_INGOT, SlimefunItems.CARGO_NODE, SlimefunItems.BILLON_INGOT, null, new ItemStack(Material.HOPPER), null}, 
		new CustomItem(SlimefunItems.CARGO_INPUT, 2))
		.register(true);

		new CargoOutputNode(Categories.CARGO, (SlimefunItemStack) SlimefunItems.CARGO_OUTPUT, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.HOPPER), null, SlimefunItems.BRASS_INGOT, SlimefunItems.CARGO_NODE, SlimefunItems.BRASS_INGOT, null, new ItemStack(Material.HOPPER), null}, 
		new CustomItem(SlimefunItems.CARGO_OUTPUT, 2))
		.register(true);

		new AdvancedCargoOutputNode(Categories.CARGO, (SlimefunItemStack) SlimefunItems.CARGO_OUTPUT_ADVANCED, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.CARGO_MOTOR, null, SlimefunItems.COBALT_INGOT, SlimefunItems.CARGO_OUTPUT, SlimefunItems.COBALT_INGOT, null, SlimefunItems.CARGO_MOTOR, null}, 
		new CustomItem(SlimefunItems.CARGO_OUTPUT_ADVANCED))
		.register(true);

		new AutomatedCraftingChamber(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.AUTOMATED_CRAFTING_CHAMBER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.CRAFTING_TABLE), null, SlimefunItems.CARGO_MOTOR, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.CARGO_MOTOR, null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public int getEnergyConsumption() {
				return 10;
			}

		}.registerChargeableBlock(true, 256);

		new ReactorAccessPort(Categories.CARGO, (SlimefunItemStack) SlimefunItems.REACTOR_ACCESS_PORT, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.LEAD_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.LEAD_INGOT, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new FluidPump(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.FLUID_PUMP, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.MEDIUM_CAPACITOR, null, new ItemStack(Material.BUCKET), SlimefunItems.CARGO_MOTOR, new ItemStack(Material.BUCKET), null, SlimefunItems.OIL_PUMP, null})
		.registerChargeableBlock(true, 512);

		new TrashCan(Categories.CARGO, (SlimefunItemStack) SlimefunItems.TRASH_CAN, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.PORTABLE_DUSTBIN, null, SlimefunItems.LEAD_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.ALUMINUM_INGOT})
		.register(true);

		new CarbonPress(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.CARBON_PRESS, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CARBON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBON, SlimefunItems.CARBON, SlimefunItems.HEATED_PRESSURE_CHAMBER, SlimefunItems.CARBON, SlimefunItems.HEATING_COIL, SlimefunItems.CARBONADO, SlimefunItems.HEATING_COIL}) {

			@Override
			public int getEnergyConsumption() {
				return 10;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 256);

		new CarbonPress(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.CARBON_PRESS_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.CARBON, SlimefunItems.CARBON_PRESS, SlimefunItems.CARBON, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.HEATING_COIL}) {

			@Override
			public int getEnergyConsumption() {
				return 25;
			}

			@Override
			public int getSpeed() {
				return 3;
			}

		}.registerChargeableBlock(true, 512);

		new CarbonPress(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.CARBON_PRESS_3, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.CARBON_PRESS_2, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.HEATING_COIL}) {

			@Override
			public int getEnergyConsumption() {
				return 90;
			}

			@Override
			public int getSpeed() {
				return 15;
			}

		}.registerChargeableBlock(true, 512);

		new ElectricSmeltery(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_SMELTERY, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.NETHER_BRICKS), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.NETHER_BRICKS), SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_INGOT_FACTORY, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.GILDED_IRON}) {

			@Override
			public int getEnergyConsumption() {
				return 10;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

		}.registerChargeableBlock(true, 512);

		new ElectricSmeltery(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.ELECTRIC_SMELTERY_2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_SMELTERY, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.GILDED_IRON}) {
			
			@Override
			public int getEnergyConsumption() {
				return 20;
			}

			@Override
			public int getSpeed() {
				return 3;
			}

		}.registerChargeableBlock(true, 1024);

		new WitherAssembler(Categories.ELECTRICITY, (SlimefunItemStack) SlimefunItems.WITHER_ASSEMBLER, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3, new ItemStack(Material.NETHER_STAR), SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.CARBONADO_EDGED_CAPACITOR})
		.registerChargeableBlock(true, 4096);
	}

}
