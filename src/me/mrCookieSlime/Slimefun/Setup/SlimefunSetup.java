package me.mrCookieSlime.Slimefun.Setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.FireworkShow;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Alloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.EnhancedFurnace;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ExcludedBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ExcludedSoulboundTool;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.JetBoots;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Jetpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Juice;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.MultiTool;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ReplacingAlloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ReplacingItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunBackpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunBow;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SolarHelmet;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SoulboundBackpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SoulboundItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Talisman;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.VanillaItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AReactor;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.Teleporter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo.AdvancedCargoOutputNode;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo.CargoInputNode;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo.CargoManagerBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo.CargoOutputNode;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.DietCookie;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.ExplosivePickaxe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.ExplosiveShovel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.GoldPan;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.GrapplingHook;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.HerculesPickaxe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.HunterTalisman;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.InfernalBonemeal;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.KnowledgeFlask;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.KnowledgeTome;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.LumberAxe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.MagicSugar;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.MonsterJerky;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.PickaxeOfContainment;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.PickaxeOfTheSeeker;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.PickaxeOfVeinMining;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.PortableGEOScanner;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.SeismicAxe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.SmeltersPickaxe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.SoulboundRune;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.StormStaff;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.SwordOfBeheading;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items.TelepositionScroll;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AncientPedestal;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.BlockPlacer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.Composter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.Crucible;
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
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectricSmeltery;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElectrifiedCrucible;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.ElevatorPlate;
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
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo.GEOMiner;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo.GEOScannerBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo.NetherDrill;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo.OilPump;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps.GPSTransmitter;
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
import me.mrCookieSlime.Slimefun.Objects.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemConsumptionHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.MultiBlockInteractionHandler;
import me.mrCookieSlime.Slimefun.Objects.tasks.RainbowTicker;
import me.mrCookieSlime.Slimefun.androids.AndroidType;
import me.mrCookieSlime.Slimefun.androids.ProgrammableAndroid;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;
import me.mrCookieSlime.Slimefun.api.item_transport.CargoNet;
import me.mrCookieSlime.Slimefun.holograms.ReactorHologram;

public final class SlimefunSetup {
	
	private SlimefunSetup() {}

	public static void setupItems() {
		Random random = new Random();
		
		new SlimefunItem(Categories.WEAPONS, SlimefunItems.GRANDMAS_WALKING_STICK, "GRANDMAS_WALKING_STICK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.OAK_LOG), null, null, new ItemStack(Material.OAK_LOG), null, null, new ItemStack(Material.OAK_LOG), null})
		.register(true);

		new SlimefunItem(Categories.WEAPONS, SlimefunItems.GRANDPAS_WALKING_STICK, "GRANDPAS_WALKING_STICK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.LEATHER), new ItemStack(Material.OAK_LOG), new ItemStack(Material.LEATHER), null, new ItemStack(Material.OAK_LOG), null, null, new ItemStack(Material.OAK_LOG), null})
		.register(true);

		new SlimefunItem(Categories.PORTABLE, SlimefunItems.PORTABLE_CRAFTER, "PORTABLE_CRAFTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.BOOK), new ItemStack(Material.CRAFTING_TABLE), null, null, null, null, null, null, null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.PORTABLE_CRAFTER, true)) {
					p.openWorkbench(p.getLocation(), true);
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.FOOD, SlimefunItems.FORTUNE_COOKIE, "FORTUNE_COOKIE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.COOKIE), new ItemStack(Material.PAPER), null, null, null, null, null, null, null})
		.register(true);

		new DietCookie(Categories.FOOD, SlimefunItems.DIET_COOKIE, "DIET_COOKIE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.COOKIE), SlimefunItems.ELYTRA_SCALE, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MACHINES_1, SlimefunItems.OUTPUT_CHEST, "OUTPUT_CHEST", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LEAD_INGOT, new ItemStack(Material.HOPPER), SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, new ItemStack(Material.CHEST), SlimefunItems.LEAD_INGOT, null, SlimefunItems.LEAD_INGOT, null})
		.register(true);
		
		new EnhancedCraftingTable().register();

		new SlimefunItem(Categories.PORTABLE, SlimefunItems.PORTABLE_DUSTBIN, "PORTABLE_DUSTBIN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT)})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.PORTABLE_DUSTBIN, true)) {
					e.setCancelled(true);
					p.openInventory(Bukkit.createInventory(null, 9 * 3, ChatColor.DARK_RED + "Delete Items"));
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.FOOD, SlimefunItems.BEEF_JERKY, "BEEF_JERKY", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SALT, new ItemStack(Material.COOKED_BEEF), null, null, null, null, null, null, null}, new String[] {"Saturation"}, new Integer[] {20})
		.register(true);

		new GrindStone().register();
		new ArmorForge().register();
		new OreCrusher().register();
		new Compressor().register();

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.MAGIC_LUMP_1, "MAGIC_LUMP_1", RecipeType.GRIND_STONE,
		new ItemStack[] {new ItemStack(Material.NETHER_WART), null, null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.MAGIC_LUMP_1, 2))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.MAGIC_LUMP_2, "MAGIC_LUMP_2", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_1, SlimefunItems.MAGIC_LUMP_1, null, SlimefunItems.MAGIC_LUMP_1, SlimefunItems.MAGIC_LUMP_1, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.MAGIC_LUMP_3, "MAGIC_LUMP_3", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.MAGIC_LUMP_2, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.ENDER_LUMP_1, "ENDER_LUMP_1", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, null, new ItemStack(Material.ENDER_EYE), null, null, null, null}, new CustomItem(SlimefunItems.ENDER_LUMP_1, 2))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.ENDER_LUMP_2, "ENDER_LUMP_2", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_1, SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1, SlimefunItems.ENDER_LUMP_1, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.ENDER_LUMP_3, "ENDER_LUMP_3", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_2, SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2, SlimefunItems.ENDER_LUMP_2, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.ENDER_BACKPACK, "ENDER_BACKPACK", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.LEATHER), SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.LEATHER), new ItemStack(Material.CHEST), new ItemStack(Material.LEATHER), SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.LEATHER), SlimefunItems.ENDER_LUMP_2})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.ENDER_BACKPACK, true)) {
					e.setCancelled(true);
					p.openInventory(p.getEnderChest());
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.ENDER_HELMET, "ENDER_HELMET", RecipeType.ARMOR_FORGE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.ENDER_EYE), SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.ENDER_CHESTPLATE, "ENDER_CHESTPLATE", RecipeType.ARMOR_FORGE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.OBSIDIAN), new ItemStack(Material.ENDER_EYE), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN)})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.ENDER_LEGGINGS, "ENDER_LEGGINGS", RecipeType.ARMOR_FORGE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.ENDER_EYE), SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN)})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.ENDER_BOOTS, "ENDER_BOOTS", RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN)})
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.MAGIC_EYE_OF_ENDER, "MAGIC_EYE_OF_ENDER", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_EYE), new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_2})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.MAGIC_EYE_OF_ENDER, true)) {
					e.getParentEvent().setCancelled(true);
					PlayerInventory.update(p);
					if (p.getInventory().getHelmet() != null && p.getInventory().getChestplate() != null && p.getInventory().getLeggings() != null && p.getInventory().getBoots() != null && SlimefunManager.isItemSimiliar(p.getInventory().getHelmet(), SlimefunItems.ENDER_HELMET, true) && SlimefunManager.isItemSimiliar(p.getInventory().getChestplate(), SlimefunItems.ENDER_CHESTPLATE, true) && SlimefunManager.isItemSimiliar(p.getInventory().getLeggings(), SlimefunItems.ENDER_LEGGINGS, true) && SlimefunManager.isItemSimiliar(p.getInventory().getBoots(), SlimefunItems.ENDER_BOOTS, true)) {
						p.launchProjectile(EnderPearl.class);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
					}
					return true;
				}
				else return false;
			}
		});

		new MagicSugar(Categories.FOOD, SlimefunItems.MAGIC_SUGAR, "MAGIC_SUGAR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.REDSTONE), new ItemStack(Material.GLOWSTONE_DUST), null, null, null, null, null, null}, new String[] {"effects.SPEED"}, new Integer[] {4})
		.register(true);

		new MonsterJerky(Categories.FOOD, SlimefunItems.MONSTER_JERKY, "MONSTER_JERKY", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SALT, new ItemStack(Material.ROTTEN_FLESH), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.SLIME_HELMET, "SLIME_HELMET", RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT), null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.SLIME_CHESTPLATE, "SLIME_CHESTPLATE", RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT)})
		.register(true);

		new SlimefunArmorPiece(Categories.MAGIC_ARMOR, SlimefunItems.SLIME_LEGGINGS, "SLIME_LEGGINGS", RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.SPEED, 300, 2)})
		.register(true);

		new SlimefunArmorPiece(Categories.MAGIC_ARMOR, SlimefunItems.SLIME_BOOTS, "SLIME_BOOTS", RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.JUMP, 300, 5)})
		.register(true);

		new SwordOfBeheading(Categories.WEAPONS, SlimefunItems.SWORD_OF_BEHEADING, "SWORD_OF_BEHEADING", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.EMERALD), null, SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.EMERALD), SlimefunItems.MAGIC_LUMP_2, null, new ItemStack(Material.BLAZE_ROD), null}, new String[] {"chance.PLAYER", "chance.SKELETON", "chance.WITHER_SKELETON", "chance.ZOMBIE", "chance.CREEPER"}, new Integer[] {70, 40, 25, 40, 40})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.MAGICAL_BOOK_COVER, "MAGICAL_BOOK_COVER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.BOOK), SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.BASIC_CIRCUIT_BOARD, "BASIC_CIRCUIT_BOARD", RecipeType.MOB_DROP,
		new ItemStack[] {null, null, null, null, new CustomItem(new ItemStack(Material.POPPY), "&a&oIron Golem"), null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.ADVANCED_CIRCUIT_BOARD, "ADVANCED_CIRCUIT_BOARD", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_BLOCK)})
		.register(true);

		new GoldPan(Categories.TOOLS, SlimefunItems.GOLD_PAN, "GOLD_PAN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.STONE), new ItemStack(Material.BOWL), new ItemStack(Material.STONE), new ItemStack(Material.STONE), new ItemStack(Material.STONE), new ItemStack(Material.STONE)},
		new ItemStack[] {new ItemStack(Material.GRAVEL), new ItemStack(Material.FLINT), new ItemStack(Material.GRAVEL), new ItemStack(Material.CLAY_BALL), new ItemStack(Material.GRAVEL), SlimefunItems.SIFTED_ORE},
		new String[] {"chance.FLINT", "chance.CLAY", "chance.SIFTED_ORE"}, new Integer[] {40, 25, 35})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.SIFTED_ORE, "SIFTED_ORE", RecipeType.GOLD_PAN,
		new ItemStack[] {new ItemStack(Material.GRAVEL), null, null, null, null, null, null, null, null})
		.register(true);

		new Smeltery().register();
		
		new SlimefunItem(Categories.MACHINES_1, SlimefunItems.IGNITION_CHAMBER, "IGNITION_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.STEEL_PLATE, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.STEEL_PLATE, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_PLATE, SlimefunItems.ELECTRIC_MOTOR, null, new ItemStack(Material.HOPPER), null})
		.register(true);
		
		new PressureChamber().register();

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.BATTERY, "BATTERY", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.REDSTONE), null, SlimefunItems.ZINC_INGOT, SlimefunItems.SULFATE, SlimefunItems.COPPER_INGOT, SlimefunItems.ZINC_INGOT, SlimefunItems.SULFATE, SlimefunItems.COPPER_INGOT })
		.register(true);

		SlimefunManager.registerArmorSet(new ItemStack(Material.GLOWSTONE), new ItemStack[] {SlimefunItems.GLOWSTONE_HELMET, SlimefunItems.GLOWSTONE_CHESTPLATE, SlimefunItems.GLOWSTONE_LEGGINGS, SlimefunItems.GLOWSTONE_BOOTS}, "GLOWSTONE",
		new PotionEffect[][] {new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)}, new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)}, new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)}, new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0)}}, true, true);

		SlimefunManager.registerArmorSet(SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_HELMET, SlimefunItems.DAMASCUS_STEEL_CHESTPLATE, SlimefunItems.DAMASCUS_STEEL_LEGGINGS, SlimefunItems.DAMASCUS_STEEL_BOOTS}, "DAMASCUS_STEEL", true, false);

		SlimefunManager.registerArmorSet(SlimefunItems.REINFORCED_ALLOY_INGOT, new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_HELMET, SlimefunItems.REINFORCED_ALLOY_CHESTPLATE, SlimefunItems.REINFORCED_ALLOY_LEGGINGS, SlimefunItems.REINFORCED_ALLOY_BOOTS}, "REINFORCED_ALLOY", true, false);

		SlimefunManager.registerArmorSet(new ItemStack(Material.CACTUS), new ItemStack[] {SlimefunItems.CACTUS_HELMET, SlimefunItems.CACTUS_CHESTPLATE, SlimefunItems.CACTUS_LEGGINGS, SlimefunItems.CACTUS_BOOTS}, "CACTUS", true, false);

		new Alloy(SlimefunItems.REINFORCED_ALLOY_INGOT, "REINFORCED_ALLOY_INGOT",
		new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.GOLD_24K, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.HARDENED_METAL_INGOT, "HARDENED_METAL_INGOT",
		new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.ALUMINUM_BRONZE_INGOT, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.DAMASCUS_STEEL_INGOT, "DAMASCUS_STEEL_INGOT",
		new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.IRON_DUST, SlimefunItems.CARBON, new ItemStack(Material.IRON_INGOT), null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.STEEL_INGOT, "STEEL_INGOT",
		new ItemStack[] {SlimefunItems.IRON_DUST, SlimefunItems.CARBON, new ItemStack(Material.IRON_INGOT), null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.BRONZE_INGOT, "BRONZE_INGOT",
		new ItemStack[] {SlimefunItems.COPPER_DUST, SlimefunItems.TIN_DUST, SlimefunItems.COPPER_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.DURALUMIN_INGOT, "DURALUMIN_INGOT",
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.ALUMINUM_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.BILLON_INGOT, "BILLON_INGOT",
		new ItemStack[] {SlimefunItems.SILVER_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.SILVER_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.BRASS_INGOT, "BRASS_INGOT",
		new ItemStack[] {SlimefunItems.COPPER_DUST, SlimefunItems.ZINC_DUST, SlimefunItems.COPPER_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.ALUMINUM_BRASS_INGOT, "ALUMINUM_BRASS_INGOT",
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.BRASS_INGOT, SlimefunItems.ALUMINUM_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.ALUMINUM_BRONZE_INGOT, "ALUMINUM_BRONZE_INGOT",
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.BRONZE_INGOT, SlimefunItems.ALUMINUM_INGOT, null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.CORINTHIAN_BRONZE_INGOT, "CORINTHIAN_BRONZE_INGOT",
		new ItemStack[] {SlimefunItems.SILVER_DUST, SlimefunItems.GOLD_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.BRONZE_INGOT, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.SOLDER_INGOT, "SOLDER_INGOT",
		new ItemStack[] {SlimefunItems.LEAD_DUST, SlimefunItems.TIN_DUST, SlimefunItems.LEAD_INGOT, null, null, null, null, null, null})
		.register(true);

		new ReplacingAlloy(SlimefunItems.SYNTHETIC_SAPPHIRE, "SYNTHETIC_SAPPHIRE",
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, new ItemStack(Material.GLASS), new ItemStack(Material.GLASS_PANE), SlimefunItems.ALUMINUM_INGOT, new ItemStack(Material.LAPIS_LAZULI), null, null, null, null})
		.register(true);

		new ReplacingItem(Categories.RESOURCES, SlimefunItems.SYNTHETIC_DIAMOND, "SYNTHETIC_DIAMOND", RecipeType.PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.CARBON_CHUNK, null, null, null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.RAW_CARBONADO, "RAW_CARBONADO",
		new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.CARBON_CHUNK, new ItemStack(Material.GLASS_PANE), null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.NICKEL_INGOT, "NICKEL_INGOT",
		new ItemStack[] {SlimefunItems.IRON_DUST, new ItemStack(Material.IRON_INGOT), SlimefunItems.COPPER_DUST, null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.COBALT_INGOT, "COBALT_INGOT",
		new ItemStack[] {SlimefunItems.IRON_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.NICKEL_INGOT, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.CARBONADO, "CARBONADO", RecipeType.PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.RAW_CARBONADO, null, null, null, null, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.FERROSILICON, "FERROSILICON",
		new ItemStack[] {new ItemStack(Material.IRON_INGOT), SlimefunItems.IRON_DUST, SlimefunItems.SILICON, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.IRON_DUST, "IRON_DUST", RecipeType.ORE_CRUSHER,
		new ItemStack[] {new ItemStack(Material.IRON_ORE), null, null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.IRON_DUST, (boolean) Slimefun.getItemValue("ORE_CRUSHER", "double-ores") ? 2: 1))
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_DUST, "GOLD_DUST", RecipeType.ORE_CRUSHER,
		new ItemStack[] {new ItemStack(Material.GOLD_ORE), null, null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.GOLD_DUST, (boolean) Slimefun.getItemValue("ORE_CRUSHER", "double-ores") ? 2: 1))
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.COPPER_DUST, "COPPER_DUST", RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.TIN_DUST, "TIN_DUST", RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.LEAD_DUST, "LEAD_DUST", RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);
		
		new SlimefunItem(Categories.RESOURCES, SlimefunItems.SILVER_DUST, "SILVER_DUST", RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.ALUMINUM_DUST, "ALUMINUM_DUST", RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.ZINC_DUST, "ZINC_DUST", RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.MAGNESIUM_DUST, "MAGNESIUM_DUST", RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.COPPER_INGOT, "COPPER_INGOT", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.COPPER_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.TIN_INGOT, "TIN_INGOT", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.TIN_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.SILVER_INGOT, "SILVER_INGOT", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.SILVER_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.LEAD_INGOT, "LEAD_INGOT", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.LEAD_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.ALUMINUM_INGOT, "ALUMINUM_INGOT", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.ZINC_INGOT, "ZINC_INGOT", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.ZINC_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.MAGNESIUM_INGOT, "MAGNESIUM_INGOT", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.MAGNESIUM_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.SULFATE, "SULFATE", RecipeType.ORE_CRUSHER,
		new ItemStack[] {new ItemStack(Material.NETHERRACK, 16), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.CARBON, "CARBON", RecipeType.COMPRESSOR,
		new ItemStack[] {new ItemStack(Material.COAL, 8), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.WHEAT_FLOUR, "WHEAT_FLOUR", RecipeType.GRIND_STONE,
		new ItemStack[] {new ItemStack(Material.WHEAT), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.STEEL_PLATE, "STEEL_PLATE", RecipeType.COMPRESSOR,
		new ItemStack[] {new CustomItem(SlimefunItems.STEEL_INGOT, 8), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.COMPRESSED_CARBON, "COMPRESSED_CARBON", RecipeType.COMPRESSOR,
		new ItemStack[] {new CustomItem(SlimefunItems.CARBON, 4), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.CARBON_CHUNK, "CARBON_CHUNK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, new ItemStack(Material.FLINT), SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.COMPRESSED_CARBON})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.STEEL_THRUSTER, "STEEL_THRUSTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.REDSTONE), null, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.STEEL_PLATE, new ItemStack(Material.FIRE_CHARGE), SlimefunItems.STEEL_PLATE})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.POWER_CRYSTAL, "POWER_CRYSTAL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE, new ItemStack(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.SYNTHETIC_SAPPHIRE, new ItemStack(Material.REDSTONE), SlimefunItems.SYNTHETIC_SAPPHIRE, new ItemStack(Material.REDSTONE)})
		.register(true);

		new Jetpack(SlimefunItems.DURALUMIN_JETPACK, "DURALUMIN_JETPACK",
		new ItemStack[] {SlimefunItems.DURALUMIN_INGOT, null, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.35)
		.register(true);

		new Jetpack(SlimefunItems.SOLDER_JETPACK, "SOLDER_JETPACK",
		new ItemStack[] {SlimefunItems.SOLDER_INGOT, null, SlimefunItems.SOLDER_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.SOLDER_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.4)
		.register(true);

		new Jetpack(SlimefunItems.BILLON_JETPACK, "BILLON_JETPACK",
		new ItemStack[] {SlimefunItems.BILLON_INGOT, null, SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BILLON_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.45)
		.register(true);

		new Jetpack(SlimefunItems.STEEL_JETPACK, "STEEL_JETPACK",
		new ItemStack[] {SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.5)
		.register(true);

		new Jetpack(SlimefunItems.DAMASCUS_STEEL_JETPACK, "DAMASCUS_STEEL_JETPACK",
		new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.55)
		.register(true);

		new Jetpack(SlimefunItems.REINFORCED_ALLOY_JETPACK, "REINFORCED_ALLOY_JETPACK",
		new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.6)
		.register(true);

		new Jetpack(SlimefunItems.CARBONADO_JETPACK, "CARBONADO_JETPACK",
		new ItemStack[] {SlimefunItems.CARBON_CHUNK, null, SlimefunItems.CARBON_CHUNK, SlimefunItems.CARBONADO, SlimefunItems.POWER_CRYSTAL, SlimefunItems.CARBONADO, SlimefunItems.STEEL_THRUSTER, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.7)
		.register(true);

		new SlimefunItem(Categories.TECH, SlimefunItems.PARACHUTE, "PARACHUTE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CHAIN, null, SlimefunItems.CHAIN, null, null, null})
		.register(true);

		new HologramProjector(Categories.TECH, SlimefunItems.HOLOGRAM_PROJECTOR, "HOLOGRAM_PROJECTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.POWER_CRYSTAL, null, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT, null, SlimefunItems.ALUMINUM_BRASS_INGOT, null}, new CustomItem(SlimefunItems.HOLOGRAM_PROJECTOR, 3))
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.CHAIN, "CHAIN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, null}, new CustomItem(SlimefunItems.CHAIN, 8))
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.HOOK, "HOOK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, null, null})
		.register(true);

		new GrapplingHook(Categories.TOOLS, SlimefunItems.GRAPPLING_HOOK, "GRAPPLING_HOOK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.HOOK, SlimefunItems.HOOK, null, SlimefunItems.CHAIN, SlimefunItems.HOOK, SlimefunItems.CHAIN, null, null},
		new String[] {"despawn-seconds"}, new Object[] {60})
		.register(true);

		new MagicWorkbench().register();

		new SlimefunItem(Categories.MAGIC, SlimefunItems.STAFF_ELEMENTAL, "STAFF_ELEMENTAL", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.MAGIC_LUMP_3, null, new ItemStack(Material.STICK), SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.MAGIC_LUMP_3, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.STAFF_WIND, "STAFF_ELEMENTAL_WIND", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.FEATHER), SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.STAFF_ELEMENTAL, new ItemStack(Material.FEATHER), SlimefunItems.STAFF_ELEMENTAL, null, null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.STAFF_WIND, true)) {
					if (p.getFoodLevel() >= 2) {
						if (p.getInventory().getItemInMainHand().getType() != Material.SHEARS && p.getGameMode() != GameMode.CREATIVE) {
							FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 2);
							Bukkit.getPluginManager().callEvent(event);
							p.setFoodLevel(event.getFoodLevel());
						}
						p.setVelocity(p.getEyeLocation().getDirection().multiply(4));
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 1, 1);
						p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1);
						p.setFallDistance(0F);
					}
					else {
						SlimefunPlugin.getLocal().sendMessage(p, "messages.hungry", true);
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MAGIC, SlimefunItems.STAFF_WATER, "STAFF_ELEMENTAL_WATER", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.LILY_PAD), SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.STAFF_ELEMENTAL, new ItemStack(Material.LILY_PAD), SlimefunItems.STAFF_ELEMENTAL, null, null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.STAFF_WATER, true)) {
					p.setFireTicks(0);
					SlimefunPlugin.getLocal().sendMessage(p, "messages.fire-extinguish", true);
					return true;
				}
				else return false;
			}
		});

		new MultiTool(SlimefunItems.DURALUMIN_MULTI_TOOL, "DURALUMIN_MULTI_TOOL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.DURALUMIN_INGOT, null, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.DURALUMIN_INGOT, null, SlimefunItems.DURALUMIN_INGOT, null},
		new String[] {"mode.0.enabled", "mode.0.name", "mode.0.item", "mode.1.enabled", "mode.1.name", "mode.1.item", "mode.2.enabled", "mode.2.name", "mode.2.item", "mode.3.enabled", "mode.3.name", "mode.3.item"}, new Object[] {true, "Portable Crafter", "PORTABLE_CRAFTER", true, "Magic Eye of Ender", "MAGIC_EYE_OF_ENDER", true, "Wind Staff", "STAFF_ELEMENTAL_WIND", true, "Grappling Hook", "GRAPPLING_HOOK"})
		.register(true);

		new MultiTool(SlimefunItems.SOLDER_MULTI_TOOL, "SOLDER_MULTI_TOOL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SOLDER_INGOT, null, SlimefunItems.SOLDER_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.SOLDER_INGOT, null, SlimefunItems.SOLDER_INGOT, null},
		new String[] {"mode.0.enabled", "mode.0.name", "mode.0.item", "mode.1.enabled", "mode.1.name", "mode.1.item", "mode.2.enabled", "mode.2.name", "mode.2.item", "mode.3.enabled", "mode.3.name", "mode.3.item"}, new Object[] {true, "Portable Crafter", "PORTABLE_CRAFTER", true, "Magic Eye of Ender", "MAGIC_EYE_OF_ENDER", true, "Wind Staff", "STAFF_ELEMENTAL_WIND", true, "Grappling Hook", "GRAPPLING_HOOK"})
		.register(true);

		new MultiTool(SlimefunItems.BILLON_MULTI_TOOL, "BILLON_MULTI_TOOL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BILLON_INGOT, null, SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.BILLON_INGOT, null, SlimefunItems.BILLON_INGOT, null},
		new String[] {"mode.0.enabled", "mode.0.name", "mode.0.item", "mode.1.enabled", "mode.1.name", "mode.1.item", "mode.2.enabled", "mode.2.name", "mode.2.item", "mode.3.enabled", "mode.3.name", "mode.3.item"}, new Object[] {true, "Portable Crafter", "PORTABLE_CRAFTER", true, "Magic Eye of Ender", "MAGIC_EYE_OF_ENDER", true, "Wind Staff", "STAFF_ELEMENTAL_WIND", true, "Grappling Hook", "GRAPPLING_HOOK"})
		.register(true);

		new MultiTool(SlimefunItems.STEEL_MULTI_TOOL, "STEEL_MULTI_TOOL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null},
		new String[] {"mode.0.enabled", "mode.0.name", "mode.0.item", "mode.1.enabled", "mode.1.name", "mode.1.item", "mode.2.enabled", "mode.2.name", "mode.2.item", "mode.3.enabled", "mode.3.name", "mode.3.item"}, new Object[] {true, "Portable Crafter", "PORTABLE_CRAFTER", true, "Magic Eye of Ender", "MAGIC_EYE_OF_ENDER", true, "Wind Staff", "STAFF_ELEMENTAL_WIND", true, "Grappling Hook", "GRAPPLING_HOOK"})
		.register(true);

		new MultiTool(SlimefunItems.DAMASCUS_STEEL_MULTI_TOOL, "DAMASCUS_STEEL_MULTI_TOOL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.DAMASCUS_STEEL_INGOT, null, SlimefunItems.DAMASCUS_STEEL_INGOT, null},
		new String[] {"mode.0.enabled", "mode.0.name", "mode.0.item", "mode.1.enabled", "mode.1.name", "mode.1.item", "mode.2.enabled", "mode.2.name", "mode.2.item", "mode.3.enabled", "mode.3.name", "mode.3.item"}, new Object[] {true, "Portable Crafter", "PORTABLE_CRAFTER", true, "Magic Eye of Ender", "MAGIC_EYE_OF_ENDER", true, "Wind Staff", "STAFF_ELEMENTAL_WIND", true, "Grappling Hook", "GRAPPLING_HOOK"})
		.register(true);

		new MultiTool(SlimefunItems.REINFORCED_ALLOY_MULTI_TOOL, "REINFORCED_ALLOY_MULTI_TOOL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, null},
		new String[] {"mode.0.enabled", "mode.0.name", "mode.0.item", "mode.1.enabled", "mode.1.name", "mode.1.item", "mode.2.enabled", "mode.2.name", "mode.2.item", "mode.3.enabled", "mode.3.name", "mode.3.item"}, new Object[] {true, "Portable Crafter", "PORTABLE_CRAFTER", true, "Magic Eye of Ender", "MAGIC_EYE_OF_ENDER", true, "Wind Staff", "STAFF_ELEMENTAL_WIND", true, "Grappling Hook", "GRAPPLING_HOOK"})
		.register(true);

		new MultiTool(SlimefunItems.CARBONADO_MULTI_TOOL, "CARBONADO_MULTI_TOOL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CARBONADO, null, SlimefunItems.CARBONADO, SlimefunItems.CARBONADO, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.CARBONADO, null, SlimefunItems.CARBONADO, null},
		new String[] {"mode.0.enabled", "mode.0.name", "mode.0.item", "mode.1.enabled", "mode.1.name", "mode.1.item", "mode.2.enabled", "mode.2.name", "mode.2.item", "mode.3.enabled", "mode.3.name", "mode.3.item", "mode.4.enabled", "mode.4.name", "mode.4.item"}, new Object[] {true, "Portable Crafter", "PORTABLE_CRAFTER", true, "Magic Eye of Ender", "MAGIC_EYE_OF_ENDER", true, "Wind Staff", "STAFF_ELEMENTAL_WIND", true, "Grappling Hook", "GRAPPLING_HOOK", true, "Gold Pan", "GOLD_PAN"})
		.register(true);

		new OreWasher().register();

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_24K, "GOLD_24K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_22K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_22K, "GOLD_22K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_20K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_20K, "GOLD_20K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_18K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_18K, "GOLD_18K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_16K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_16K, "GOLD_16K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_14K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_14K, "GOLD_14K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_12K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_12K, "GOLD_12K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_10K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_10K, "GOLD_10K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_8K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_8K, "GOLD_8K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_6K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_6K, "GOLD_6K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_4K, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_4K, "GOLD_4K", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.GOLD_DUST, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.STONE_CHUNK, "STONE_CHUNK", RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.SILICON, "SILICON", RecipeType.SMELTERY,
		new ItemStack[] {new ItemStack(Material.QUARTZ_BLOCK), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.SOLAR_PANEL, "SOLAR_PANEL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), SlimefunItems.SILICON, SlimefunItems.SILICON, SlimefunItems.SILICON, SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON})
		.register(true);

		new SolarHelmet(Categories.TECH, SlimefunItems.SOLAR_HELMET, "SOLAR_HELMET", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.SOLAR_PANEL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.MEDIUM_CAPACITOR, null, SlimefunItems.MEDIUM_CAPACITOR},
		new String[] {"charge-amount"}, new Double[] {0.1})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.LAVA_CRYSTAL, "LAVA_CRYSTAL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.BLAZE_POWDER), SlimefunItems.RUNE_FIRE, new ItemStack(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.BLAZE_POWDER), SlimefunItems.MAGIC_LUMP_1})
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.STAFF_FIRE, "STAFF_ELEMENTAL_FIRE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, null, SlimefunItems.LAVA_CRYSTAL, null, SlimefunItems.STAFF_ELEMENTAL, null, SlimefunItems.STAFF_ELEMENTAL, null, null})
		.register(true);
		
		new StormStaff(Categories.MAGIC, SlimefunItems.STAFF_STORM, "STAFF_ELEMENTAL_STORM", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.RUNE_LIGHTNING, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_LIGHTNING, SlimefunItems.STAFF_WATER, SlimefunItems.MAGIC_SUGAR, SlimefunItems.STAFF_WIND, SlimefunItems.RUNE_LIGHTNING, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_LIGHTNING})
		.register(true);

		new SmeltersPickaxe(Categories.TOOLS, SlimefunItems.AUTO_SMELT_PICKAXE, "SMELTERS_PICKAXE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LAVA_CRYSTAL, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.LAVA_CRYSTAL, null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.TALISMAN, "COMMON_TALISMAN", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2, null, new ItemStack(Material.EMERALD), null, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2},
		new String[] {"recipe-requires-nether-stars"}, new Boolean[] {false})
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_ANVIL, "ANVIL_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.ANVIL), SlimefunItems.TALISMAN, new ItemStack(Material.ANVIL), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, false, "anvil")
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_MINER, "MINER_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.TALISMAN, SlimefunItems.SIFTED_ORE, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		false, false, "miner", 20)
		.register(true);

		new HunterTalisman(SlimefunItems.TALISMAN_HUNTER, "HUNTER_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.TALISMAN, SlimefunItems.MONSTER_JERKY, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		false, false, "hunter", 20)
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_LAVA, "LAVA_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.TALISMAN, new ItemStack(Material.LAVA_BUCKET), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, true, "lava", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4))
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_WATER, "WATER_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.WATER_BUCKET), SlimefunItems.TALISMAN, new ItemStack(Material.FISHING_ROD), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, true, "water", new PotionEffect(PotionEffectType.WATER_BREATHING, 3600, 4))
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_ANGEL, "ANGEL_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.FEATHER), SlimefunItems.TALISMAN, new ItemStack(Material.FEATHER), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		false, true, "angel", 75)
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_FIRE, "FIRE_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.TALISMAN, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, true, "fire", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4))
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_MAGICIAN, "MAGICIAN_TALISMAN",
		new ItemStack[] {SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.ENCHANTING_TABLE), SlimefunItems.TALISMAN, new ItemStack(Material.ENCHANTING_TABLE), SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3},
		false, false, "magician", 80)
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_TRAVELLER, "TRAVELLER_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.STAFF_WIND, SlimefunItems.TALISMAN_ANGEL, SlimefunItems.STAFF_WIND, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		false, false, "traveller", 60, new PotionEffect(PotionEffectType.SPEED, 3600, 2))
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_WARRIOR, "WARRIOR_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.TALISMAN, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, true, "warrior", new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 2))
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_KNIGHT, "KNIGHT_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.GILDED_IRON, SlimefunItems.TALISMAN_WARRIOR, SlimefunItems.GILDED_IRON, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		"knight", 30, new PotionEffect(PotionEffectType.REGENERATION, 100, 3))
		.register(true);

		new Alloy(SlimefunItems.GILDED_IRON, "GILDED_IRON",
		new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.IRON_DUST, null, null, null, null, null, null, null})
		.register(true);

		new ReplacingAlloy(SlimefunItems.SYNTHETIC_EMERALD, "SYNTHETIC_EMERALD",
		new ItemStack[] {SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.ALUMINUM_DUST, SlimefunItems.ALUMINUM_INGOT, new ItemStack(Material.GLASS_PANE), null, null, null, null, null})
		.register(true);

		SlimefunManager.registerArmorSet(SlimefunItems.CHAIN, new ItemStack[] {new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_BOOTS)}, "CHAIN", true, true);

		new Talisman(SlimefunItems.TALISMAN_WHIRLWIND, "WHIRLWIND_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.STAFF_WIND, SlimefunItems.TALISMAN_TRAVELLER, SlimefunItems.STAFF_WIND, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3}
		, false, true, "whirlwind", 60)
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_WIZARD, "WIZARD_TALISMAN",
		new ItemStack[] {SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.TALISMAN_MAGICIAN, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3},
		false, false, "wizard", 60)
		.register(true);
		
		new LumberAxe(Categories.TOOLS, SlimefunItems.LUMBER_AXE, "LUMBER_AXE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.SYNTHETIC_DIAMOND, null, SlimefunItems.SYNTHETIC_EMERALD, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.SALT, "SALT", RecipeType.ORE_WASHER,
		new ItemStack[] {null, null, null, null, new ItemStack(Material.SAND, 4), null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.HEAVY_CREAM, "HEAVY_CREAM", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.MILK_BUCKET), null, null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.HEAVY_CREAM, 2))
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.CHEESE, "CHEESE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.MILK_BUCKET), SlimefunItems.SALT, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.BUTTER, "BUTTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HEAVY_CREAM, SlimefunItems.SALT, null, null, null, null, null, null, null})
		.register(true);

		SlimefunManager.registerArmorSet(SlimefunItems.GILDED_IRON, new ItemStack[] {SlimefunItems.GILDED_IRON_HELMET, SlimefunItems.GILDED_IRON_CHESTPLATE, SlimefunItems.GILDED_IRON_LEGGINGS, SlimefunItems.GILDED_IRON_BOOTS}, "GILDED_IRON", true, false);

		new SlimefunArmorPiece(Categories.ARMOR, SlimefunItems.SCUBA_HELMET, "SCUBA_HELMET", RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.BLACK_WOOL), null, null, null},
		new PotionEffect[] {new PotionEffect(PotionEffectType.WATER_BREATHING, 300, 1)})
		.register(true);

		new SlimefunArmorPiece(Categories.ARMOR, SlimefunItems.HAZMATSUIT_CHESTPLATE, "HAZMAT_CHESTPLATE", RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.ORANGE_WOOL), null, new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.BLACK_WOOL)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 1)})
		.register(true);

		new SlimefunItem(Categories.ARMOR, SlimefunItems.HAZMATSUIT_LEGGINGS, "HAZMAT_LEGGINGS", RecipeType.ARMOR_FORGE,
		new ItemStack [] {new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.ORANGE_WOOL), null, new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.ORANGE_WOOL), null, new ItemStack(Material.ORANGE_WOOL)})
		.register(true);

		new SlimefunItem(Categories.ARMOR, SlimefunItems.RUBBER_BOOTS, "RUBBER_BOOTS", RecipeType.ARMOR_FORGE,
		new ItemStack [] {null, null, null, new ItemStack(Material.BLACK_WOOL), null, new ItemStack(Material.BLACK_WOOL), new ItemStack(Material.BLACK_WOOL), null, new ItemStack(Material.BLACK_WOOL)})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.CRUSHED_ORE, "CRUSHED_ORE", RecipeType.ORE_CRUSHER,
		new ItemStack[] {SlimefunItems.SIFTED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.PULVERIZED_ORE, "PULVERIZED_ORE", RecipeType.ORE_CRUSHER,
		new ItemStack[] {SlimefunItems.CRUSHED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.PURE_ORE_CLUSTER, "PURE_ORE_CLUSTER", RecipeType.ORE_WASHER,
		new ItemStack[] {SlimefunItems.PULVERIZED_ORE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.TINY_URANIUM, "TINY_URANIUM", RecipeType.ORE_CRUSHER,
		new ItemStack[] {SlimefunItems.PURE_ORE_CLUSTER, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.SMALL_URANIUM, "SMALL_URANIUM", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM, SlimefunItems.TINY_URANIUM})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.URANIUM, "URANIUM", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SMALL_URANIUM, SlimefunItems.SMALL_URANIUM, null, SlimefunItems.SMALL_URANIUM, SlimefunItems.SMALL_URANIUM, null, null, null, null})
		.register(true);

		new Alloy(SlimefunItems.REDSTONE_ALLOY, "REDSTONE_ALLOY",
		new ItemStack[] {new ItemStack(Material.REDSTONE), new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.FERROSILICON, SlimefunItems.HARDENED_METAL_INGOT, null, null, null, null, null})
		.register(true);

		SlimefunManager.registerArmorSet(SlimefunItems.GOLD_12K, new ItemStack[] {SlimefunItems.GOLD_HELMET, SlimefunItems.GOLD_CHESTPLATE, SlimefunItems.GOLD_LEGGINGS, SlimefunItems.GOLD_BOOTS}, "GOLD_12K", true, false);

		new SlimefunItem(Categories.MISC, SlimefunItems.CLOTH, "CLOTH", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.WHITE_WOOL), null, null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.CLOTH, 8))
		.register(true);

		new SlimefunItem(Categories.PORTABLE, SlimefunItems.RAG, "RAG", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH, new ItemStack(Material.STRING), null, new ItemStack(Material.STRING), SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.RAG, true)) {
					PlayerInventory.consumeItemInHand(p);
					p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.WHITE_WOOL);
					p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
					p.setFireTicks(0);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.PORTABLE, SlimefunItems.BANDAGE, "BANDAGE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.RAG, new ItemStack(Material.STRING), SlimefunItems.RAG, null, null, null, null, null, null},
		new CustomItem(SlimefunItems.BANDAGE, 4), new String[] {"enable-bleeding"}, new Boolean[] {true})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BANDAGE, true)) {
					PlayerInventory.consumeItemInHand(p);
					p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.WHITE_WOOL);
					p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
					p.setFireTicks(0);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.PORTABLE, SlimefunItems.SPLINT, "SPLINT", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.STICK), null, new ItemStack(Material.IRON_INGOT), null},
		new CustomItem(SlimefunItems.SPLINT, 4), new String[] {"enable-broken-legs"}, new Boolean[] {true})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.SPLINT, true)) {
					PlayerInventory.consumeItemInHand(p);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SKELETON_HURT, 1, 1);
					p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MISC, SlimefunItems.CAN, "TIN_CAN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, null, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_INGOT}, new CustomItem(SlimefunItems.CAN, 4))
		.register(true);

		new SlimefunItem(Categories.PORTABLE, SlimefunItems.VITAMINS, "VITAMINS", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.APPLE), new ItemStack(Material.RED_MUSHROOM), new ItemStack(Material.SUGAR), null, null, null, null, null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.VITAMINS, true)) {
					PlayerInventory.consumeItemInHand(p);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
					if (p.hasPotionEffect(PotionEffectType.POISON)) p.removePotionEffect(PotionEffectType.POISON);
					if (p.hasPotionEffect(PotionEffectType.WITHER)) p.removePotionEffect(PotionEffectType.WITHER);
					if (p.hasPotionEffect(PotionEffectType.SLOW)) p.removePotionEffect(PotionEffectType.SLOW);
					if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
					if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) p.removePotionEffect(PotionEffectType.WEAKNESS);
					if (p.hasPotionEffect(PotionEffectType.CONFUSION)) p.removePotionEffect(PotionEffectType.CONFUSION);
					if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) p.removePotionEffect(PotionEffectType.BLINDNESS);
					p.setFireTicks(0);
					p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 2));
					e.setCancelled(true);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.PORTABLE, SlimefunItems.MEDICINE, "MEDICINE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.VITAMINS, new ItemStack(Material.GLASS_BOTTLE), SlimefunItems.HEAVY_CREAM, null, null, null, null, null, null})
		.register(true, new ItemConsumptionHandler() {
			
			@Override
			public boolean onConsume(PlayerItemConsumeEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.MEDICINE, true)) {
					if (p.hasPotionEffect(PotionEffectType.POISON)) p.removePotionEffect(PotionEffectType.POISON);
					if (p.hasPotionEffect(PotionEffectType.WITHER)) p.removePotionEffect(PotionEffectType.WITHER);
					if (p.hasPotionEffect(PotionEffectType.SLOW)) p.removePotionEffect(PotionEffectType.SLOW);
					if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
					if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) p.removePotionEffect(PotionEffectType.WEAKNESS);
					if (p.hasPotionEffect(PotionEffectType.CONFUSION)) p.removePotionEffect(PotionEffectType.CONFUSION);
					if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) p.removePotionEffect(PotionEffectType.BLINDNESS);
					
					p.setFireTicks(0);
					return true;
				}
				else return false;
			}
		});

		new SlimefunArmorPiece(Categories.TECH, SlimefunItems.NIGHT_VISION_GOGGLES, "NIGHT_VISION_GOGGLES", RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.LIME_STAINED_GLASS_PANE), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.LIME_STAINED_GLASS_PANE), new ItemStack(Material.COAL_BLOCK), null, new ItemStack(Material.COAL_BLOCK)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 20)})
		.register(true);

		new PickaxeOfContainment(Categories.TOOLS, SlimefunItems.PICKAXE_OF_CONTAINMENT, "PICKAXE_OF_CONTAINMENT", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, null, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
		.register(true);

		new HerculesPickaxe(Categories.TOOLS, SlimefunItems.HERCULES_PICKAXE, "HERCULES_PICKAXE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
		.register(true);
                
		new TableSaw().register();

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.SLIME_HELMET_STEEL, "SLIME_STEEL_HELMET", RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.SLIME_CHESTPLATE_STEEL, "SLIME_STEEL_CHESTPLATE", RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL)})
		.register(true);

		new SlimefunArmorPiece(Categories.MAGIC_ARMOR, SlimefunItems.SLIME_LEGGINGS_STEEL, "SLIME_STEEL_LEGGINGS", RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.SPEED, 300, 2)})
		.register(true);

		new SlimefunArmorPiece(Categories.MAGIC_ARMOR, SlimefunItems.SLIME_BOOTS_STEEL, "SLIME_STEEL_BOOTS", RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BALL), SlimefunItems.STEEL_PLATE, new ItemStack(Material.SLIME_BALL)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.JUMP, 300, 5)})
		.register(true);

		new SlimefunItem(Categories.WEAPONS, SlimefunItems.BLADE_OF_VAMPIRES, "BLADE_OF_VAMPIRES", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.WITHER_SKELETON_SKULL), null, null, new ItemStack(Material.WITHER_SKELETON_SKULL), null, null, new ItemStack(Material.BLAZE_ROD), null})
		.register(true);

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.DIGITAL_MINER, "DIGITAL_MINER",
		new ItemStack[] {SlimefunItems.SOLAR_PANEL, new ItemStack(Material.CHEST), SlimefunItems.SOLAR_PANEL, new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.DISPENSER), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.HOPPER), new ItemStack(Material.IRON_BLOCK)},
		new ItemStack[0], BlockFace.SELF)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(final Player p, MultiBlock mb, final Block b) {
				if (mb.isMultiBlock(SlimefunItem.getByID("DIGITAL_MINER"))) {
					p.sendMessage(ChatColor.DARK_RED + "THIS MACHINE WILL SOON BE REMOVED!");
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true) && Slimefun.hasUnlocked(p, SlimefunItems.DIGITAL_MINER, true)) {
						Block chestBlock = b.getRelative(BlockFace.UP);

						if(!(BlockStorage.check(chestBlock.getRelative(BlockFace.WEST), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.EAST), "SOLAR_PANEL")) &&
								!(BlockStorage.check(chestBlock.getRelative(BlockFace.NORTH), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.SOUTH), "SOLAR_PANEL"))) {
							return false;
						}

						Chest chest = (Chest) chestBlock.getState();
						final Inventory inv = chest.getInventory();
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
							final Material ore = ores.get(0).getBlock().getType();
							final ItemStack adding = new ItemStack(ore);
							ores.get(0).getBlock().setType(Material.AIR);
							ores.clear();
							if (InvUtils.fits(inv, adding)) {
								for (int i = 0; i < 4; i++) {
									int j = i;
									Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> {
										if (j < 3) {
											b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, ore);
										} else {
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

			@Override
			public boolean onInteract(final Player p, MultiBlock mb, final Block b) {
				if (mb.isMultiBlock(SlimefunItem.getByID("ADVANCED_DIGITAL_MINER"))) {
					p.sendMessage(ChatColor.DARK_RED + "THIS MACHINE WILL SOON BE REMOVED!");
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true) && Slimefun.hasUnlocked(p, SlimefunItems.ADVANCED_DIGITAL_MINER, true)) {
						Block chestBlock = b.getRelative(BlockFace.UP);

						if(!(BlockStorage.check(chestBlock.getRelative(BlockFace.WEST), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.EAST), "SOLAR_PANEL")) &&
								!(BlockStorage.check(chestBlock.getRelative(BlockFace.NORTH), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.SOUTH), "SOLAR_PANEL"))) {
							return false;
						}

						Chest chest = (Chest) chestBlock.getState();
						final Inventory inv = chest.getInventory();
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
							final Material ore = ores.get(0).getBlock().getType();
							ItemStack drop = new ItemStack(ore);
							if (ore == Material.COAL_ORE)  drop = new ItemStack(Material.COAL, 4);
							else if (ore == Material.IRON_ORE) drop = new CustomItem(SlimefunItems.IRON_DUST, 2);
							else if (ore == Material.GOLD_ORE)  drop = new CustomItem(SlimefunItems.GOLD_DUST, 2);
							else if (ore == Material.REDSTONE_ORE)  drop = new ItemStack(Material.REDSTONE, 8);
							else if (ore == Material.NETHER_QUARTZ_ORE)  drop = new ItemStack(Material.QUARTZ, 4);
							else if (ore == Material.LAPIS_ORE)  drop = new ItemStack(Material.LAPIS_LAZULI, 12);
							else {
								for (ItemStack drops: ores.get(0).getBlock().getDrops()) {
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

		new SlimefunItem(Categories.MISC, SlimefunItems.GOLD_24K_BLOCK, "GOLD_24K_BLOCK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K})
		.register(true);

		new Composter(Categories.MACHINES_1, SlimefunItems.COMPOSTER, "COMPOSTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.OAK_SLAB), null, new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), null, new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), new ItemStack(Material.CAULDRON), new ItemStack(Material.OAK_SLAB)},
		new ItemStack[] {
				new ItemStack(Material.OAK_LEAVES, 8), new ItemStack(Material.DIRT), 
				new ItemStack(Material.BIRCH_LEAVES, 8), new ItemStack(Material.DIRT), 
				new ItemStack(Material.SPRUCE_LEAVES, 8), new ItemStack(Material.DIRT), 
				new ItemStack(Material.JUNGLE_LEAVES, 8), new ItemStack(Material.DIRT), 
				new ItemStack(Material.ACACIA_LEAVES, 8), new ItemStack(Material.DIRT), 
				new ItemStack(Material.DARK_OAK_LEAVES, 8), new ItemStack(Material.DIRT), 
				
				new ItemStack(Material.OAK_SAPLING, 8), new ItemStack(Material.DIRT), 
				new ItemStack(Material.BIRCH_SAPLING, 8), new ItemStack(Material.DIRT), 
				new ItemStack(Material.SPRUCE_SAPLING, 8), new ItemStack(Material.DIRT), 
				new ItemStack(Material.JUNGLE_SAPLING, 8), new ItemStack(Material.DIRT), 
				new ItemStack(Material.ACACIA_SAPLING, 8), new ItemStack(Material.DIRT), 
				new ItemStack(Material.DARK_OAK_SAPLING, 8), new ItemStack(Material.DIRT), 
				
				new ItemStack(Material.STONE, 4), new ItemStack(Material.NETHERRACK), 
				new ItemStack(Material.SAND, 2), new ItemStack(Material.SOUL_SAND), 
				new ItemStack(Material.WHEAT, 4), new ItemStack(Material.NETHER_WART)
		}).register(true);

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.FARMER_SHOES, "FARMER_SHOES", RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.HAY_BLOCK), null, new ItemStack(Material.HAY_BLOCK), new ItemStack(Material.HAY_BLOCK), null, new ItemStack(Material.HAY_BLOCK)})
		.register(true);
		
		new ExplosivePickaxe(Categories.TOOLS, SlimefunItems.EXPLOSIVE_PICKAXE, "EXPLOSIVE_PICKAXE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.TNT), SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.TNT), null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null},
		new String[] {"unbreakable-blocks", "damage-on-use"}, new Object[] {Stream.of(Material.BEDROCK, Material.END_PORTAL_FRAME, Material.END_PORTAL, Material.NETHER_PORTAL, Material.BARRIER, Material.STRUCTURE_BLOCK, Material.COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK).map(Material::toString).collect(Collectors.toList()), Boolean.FALSE })
		.register(true);

		new ExplosiveShovel(Categories.TOOLS, SlimefunItems.EXPLOSIVE_SHOVEL, "EXPLOSIVE_SHOVEL", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, SlimefunItems.SYNTHETIC_DIAMOND, null, null, new ItemStack(Material.TNT), null, null, SlimefunItems.FERROSILICON, null},
		new String[] {"damage-on-use"}, new Object[] {Boolean.FALSE })
		.register(true);

		new AutomatedPanningMachine().register();

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.BOOTS_OF_THE_STOMPER, "BOOTS_OF_THE_STOMPER", RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.YELLOW_WOOL), null, new ItemStack(Material.YELLOW_WOOL), new ItemStack(Material.PISTON), null, new ItemStack(Material.PISTON)})
		.register(true);

		new PickaxeOfTheSeeker(Categories.TOOLS, SlimefunItems.PICKAXE_OF_THE_SEEKER, "PICKAXE_OF_THE_SEEKER", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.COMPASS), SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.COMPASS), null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
		.register(true);

		new SlimefunBackpack(9, Categories.PORTABLE, SlimefunItems.BACKPACK_SMALL, "SMALL_BACKPACK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.LEATHER), null, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_6K, new ItemStack(Material.CHEST), SlimefunItems.GOLD_6K, new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER)})
		.register(true);

		new SlimefunBackpack(18, Categories.PORTABLE, SlimefunItems.BACKPACK_MEDIUM, "MEDIUM_BACKPACK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.LEATHER), null, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_10K, SlimefunItems.BACKPACK_SMALL, SlimefunItems.GOLD_10K, new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER)})
		.register(true);

		new SlimefunBackpack(27, Categories.PORTABLE, SlimefunItems.BACKPACK_LARGE, "LARGE_BACKPACK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.LEATHER), null, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_14K, SlimefunItems.BACKPACK_MEDIUM, SlimefunItems.GOLD_14K, new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER), new ItemStack(Material.LEATHER)})
		.register(true);

		new SlimefunBackpack(36, Categories.PORTABLE, SlimefunItems.WOVEN_BACKPACK, "WOVEN_BACKPACK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CLOTH, null, SlimefunItems.CLOTH, SlimefunItems.GOLD_16K, SlimefunItems.BACKPACK_LARGE, SlimefunItems.GOLD_16K, SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH})
		.register(true);

		new Crucible(Categories.MACHINES_1, SlimefunItems.CRUCIBLE, "CRUCIBLE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack [] {new ItemStack(Material.TERRACOTTA), null, new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), null, new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), new ItemStack(Material.FLINT_AND_STEEL), new ItemStack(Material.TERRACOTTA)},
		new ItemStack [] {
			new ItemStack(Material.COBBLESTONE, 16), new ItemStack(Material.LAVA_BUCKET), 
			new ItemStack(Material.STONE, 12), new ItemStack(Material.LAVA_BUCKET), 
						
			new ItemStack(Material.OAK_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
			new ItemStack(Material.BIRCH_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
			new ItemStack(Material.SPRUCE_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
			new ItemStack(Material.JUNGLE_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
			new ItemStack(Material.ACACIA_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
			new ItemStack(Material.DARK_OAK_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),

			new ItemStack(Material.TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.WHITE_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.ORANGE_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.MAGENTA_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.LIGHT_BLUE_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.YELLOW_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.LIME_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.PINK_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.GRAY_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.LIGHT_GRAY_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.CYAN_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.PURPLE_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.BROWN_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.GREEN_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.RED_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET),
			new ItemStack(Material.BLACK_TERRACOTTA, 12), new ItemStack(Material.LAVA_BUCKET)
		})
		.register(true);

		new SlimefunBackpack(45, Categories.PORTABLE, SlimefunItems.GILDED_BACKPACK, "GILDED_BACKPACK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_22K, null, SlimefunItems.GOLD_22K, new ItemStack(Material.LEATHER), SlimefunItems.WOVEN_BACKPACK, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_22K, null, SlimefunItems.GOLD_22K})
		.register(true);

		new SlimefunBackpack(54, Categories.PORTABLE, SlimefunItems.RADIANT_BACKPACK, "RADIANT_BACKPACK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, new ItemStack(Material.LEATHER), SlimefunItems.GILDED_BACKPACK, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K})
		.register(true);

		new Alloy(Categories.TECH_MISC, SlimefunItems.MAGNET, "MAGNET",
		new ItemStack[] {SlimefunItems.NICKEL_INGOT, SlimefunItems.ALUMINUM_DUST, SlimefunItems.IRON_DUST, SlimefunItems.COBALT_INGOT, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.INFUSED_MAGNET, "INFUSED_MAGNET", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ENDER_LUMP_2, SlimefunItems.MAGNET, SlimefunItems.ENDER_LUMP_2, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3})
		.register(true);

		new SlimefunItem(Categories.TOOLS, SlimefunItems.COBALT_PICKAXE, "COBALT_PICKAXE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.COBALT_INGOT, SlimefunItems.COBALT_INGOT, SlimefunItems.COBALT_INGOT, null, SlimefunItems.NICKEL_INGOT, null, null, SlimefunItems.NICKEL_INGOT, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.NECROTIC_SKULL, "NECROTIC_SKULL", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, null, new ItemStack(Material.WITHER_SKELETON_SKULL), null, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.ESSENCE_OF_AFTERLIFE, "ESSENCE_OF_AFTERLIFE", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_AIR, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_EARTH, SlimefunItems.NECROTIC_SKULL, SlimefunItems.RUNE_FIRE, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_WATER, SlimefunItems.ENDER_LUMP_3})
		.register(true);

		new SoulboundBackpack(36, Categories.PORTABLE, SlimefunItems.BOUND_BACKPACK, "BOUND_BACKPACK",
		new ItemStack[] {SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.RADIANT_BACKPACK, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2})
		.register(true);

		new JetBoots(SlimefunItems.DURALUMIN_JETBOOTS, "DURALUMIN_JETBOOTS",
		new ItemStack[] {null, null, null, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.35)
		.register(true);

		new JetBoots(SlimefunItems.SOLDER_JETBOOTS, "SOLDER_JETBOOTS",
		new ItemStack[] {null, null, null, SlimefunItems.SOLDER_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.SOLDER_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.4)
		.register(true);

		new JetBoots(SlimefunItems.BILLON_JETBOOTS, "BILLON_JETBOOTS",
		new ItemStack[] {null, null, null, SlimefunItems.BILLON_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BILLON_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.45)
		.register(true);

		new JetBoots(SlimefunItems.STEEL_JETBOOTS, "STEEL_JETBOOTS",
		new ItemStack[] {null, null, null, SlimefunItems.STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.5)
		.register(true);

		new JetBoots(SlimefunItems.DAMASCUS_STEEL_JETBOOTS, "DAMASCUS_STEEL_JETBOOTS",
		new ItemStack[] {null, null, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.55)
		.register(true);

		new JetBoots(SlimefunItems.REINFORCED_ALLOY_JETBOOTS, "REINFORCED_ALLOY_JETBOOTS",
		new ItemStack[] {null, null, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.STEEL_THRUSTER, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.6)
		.register(true);

		new JetBoots(SlimefunItems.CARBONADO_JETBOOTS, "CARBONADO_JETBOOTS",
		new ItemStack[] {null, null, null, SlimefunItems.CARBONADO, SlimefunItems.POWER_CRYSTAL, SlimefunItems.CARBONADO, SlimefunItems.STEEL_THRUSTER, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.7)
		.register(true);

		new JetBoots(SlimefunItems.ARMORED_JETBOOTS, "ARMORED_JETBOOTS",
		new ItemStack[] {null, null, null, SlimefunItems.STEEL_PLATE, SlimefunItems.POWER_CRYSTAL, SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_THRUSTER, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_THRUSTER},
		0.45)
		.register(true);

		new SeismicAxe(Categories.WEAPONS, SlimefunItems.SEISMIC_AXE, "SEISMIC_AXE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.STAFF_ELEMENTAL, null, null, SlimefunItems.STAFF_ELEMENTAL, null})
		.register(true);

		new PickaxeOfVeinMining(Categories.TOOLS, SlimefunItems.PICKAXE_OF_VEIN_MINING, "PICKAXE_OF_VEIN_MINING", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.EMERALD_ORE), SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.EMERALD_ORE), null, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
		.register(true);

		new SoulboundItem(Categories.WEAPONS, SlimefunItems.SOULBOUND_SWORD, "SOULBOUND_SWORD",
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_SWORD), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.WEAPONS, SlimefunItems.SOULBOUND_BOW, "SOULBOUND_BOW",
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.BOW), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.TOOLS, SlimefunItems.SOULBOUND_PICKAXE, "SOULBOUND_PICKAXE",
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_PICKAXE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new ExcludedSoulboundTool(Categories.TOOLS, SlimefunItems.SOULBOUND_AXE, "SOULBOUND_AXE",
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_AXE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new ExcludedSoulboundTool(Categories.TOOLS, SlimefunItems.SOULBOUND_SHOVEL, "SOULBOUND_SHOVEL",
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_SHOVEL), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new ExcludedSoulboundTool(Categories.TOOLS, SlimefunItems.SOULBOUND_HOE, "SOULBOUND_HOE",
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_HOE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.MAGIC_ARMOR, SlimefunItems.SOULBOUND_HELMET, "SOULBOUND_HELMET",
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_HELMET), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.MAGIC_ARMOR, SlimefunItems.SOULBOUND_CHESTPLATE, "SOULBOUND_CHESTPLATE",
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_CHESTPLATE), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.MAGIC_ARMOR, SlimefunItems.SOULBOUND_LEGGINGS, "SOULBOUND_LEGGINGS",
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_LEGGINGS), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new SoulboundItem(Categories.MAGIC_ARMOR, SlimefunItems.SOULBOUND_BOOTS, "SOULBOUND_BOOTS",
		new ItemStack[] {null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null, null, new ItemStack(Material.DIAMOND_BOOTS), null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new Juicer().register();

		new Juice(Categories.FOOD, SlimefunItems.APPLE_JUICE, "APPLE_JUICE", RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.APPLE), null, null, null, null, null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, SlimefunItems.CARROT_JUICE, "CARROT_JUICE", RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.CARROT), null, null, null, null, null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, SlimefunItems.MELON_JUICE, "MELON_JUICE", RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.MELON), null, null, null, null, null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, SlimefunItems.PUMPKIN_JUICE, "PUMPKIN_JUICE", RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.PUMPKIN), null, null, null, null, null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, SlimefunItems.GOLDEN_APPLE_JUICE, "GOLDEN_APPLE_JUICE", RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.GOLDEN_APPLE), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.BROKEN_SPAWNER, "BROKEN_SPAWNER", new RecipeType(SlimefunItems.PICKAXE_OF_CONTAINMENT),
		new ItemStack[] {null, null, null, null, new ItemStack(Material.SPAWNER), null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.REPAIRED_SPAWNER, "REINFORCED_SPAWNER", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.RUNE_ENDER, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"), SlimefunItems.ESSENCE_OF_AFTERLIFE, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"), SlimefunItems.BROKEN_SPAWNER, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"), SlimefunItems.ESSENCE_OF_AFTERLIFE, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"), SlimefunItems.RUNE_ENDER})
		.register(true, (BlockPlaceHandler) (e, item) -> {
			if (SlimefunManager.isItemSimiliar(item, SlimefunItems.REPAIRED_SPAWNER, false)) {
				EntityType type = null;
				for (String line: item.getItemMeta().getLore()) {
					if (ChatColor.stripColor(line).startsWith("Type: ") && !line.contains("<Type>"))
						type = EntityType.valueOf(ChatColor.stripColor(line).replace("Type: ", "")
							.replace(' ', '_').toUpperCase());
				}
				if (type != null) {
					CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
					spawner.setSpawnedType(type);
					spawner.update(true, false);
				}
				return true;
			}
			else return false;
		});

		new EnhancedFurnace(1, 1, 1, SlimefunItems.ENHANCED_FURNACE, "ENHANCED_FURNACE",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, new ItemStack(Material.FURNACE), SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(2, 1, 1, SlimefunItems.ENHANCED_FURNACE_2, "ENHANCED_FURNACE_2",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(2, 2, 1, SlimefunItems.ENHANCED_FURNACE_3, "ENHANCED_FURNACE_3",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_2, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(3, 2, 1, SlimefunItems.ENHANCED_FURNACE_4, "ENHANCED_FURNACE_4",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_3, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(3, 2, 2, SlimefunItems.ENHANCED_FURNACE_5, "ENHANCED_FURNACE_5",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_4, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(3, 3, 2, SlimefunItems.ENHANCED_FURNACE_6, "ENHANCED_FURNACE_6",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_5, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(4, 3, 2, SlimefunItems.ENHANCED_FURNACE_7, "ENHANCED_FURNACE_7",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_6, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(4, 4, 2, SlimefunItems.ENHANCED_FURNACE_8, "ENHANCED_FURNACE_8",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_7, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(5, 4, 2, SlimefunItems.ENHANCED_FURNACE_9, "ENHANCED_FURNACE_9",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_8, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(5, 5, 2, SlimefunItems.ENHANCED_FURNACE_10, "ENHANCED_FURNACE_10",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_9, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(5, 5, 3, SlimefunItems.ENHANCED_FURNACE_11, "ENHANCED_FURNACE_11",
		new ItemStack[] {null, SlimefunItems.BASIC_CIRCUIT_BOARD, null, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_10, SlimefunItems.HEATING_COIL, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new EnhancedFurnace(10, 10, 3, SlimefunItems.REINFORCED_FURNACE, "REINFORCED_FURNACE",
		new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.ENHANCED_FURNACE_11, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT})
		.register(true);

		new EnhancedFurnace(20, 10, 3, SlimefunItems.CARBONADO_EDGED_FURNACE, "CARBONADO_EDGED_FURNACE",
		new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.CARBONADO, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_FURNACE, SlimefunItems.HEATING_COIL, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.ELECTRO_MAGNET, "ELECTRO_MAGNET", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.NICKEL_INGOT, SlimefunItems.MAGNET, SlimefunItems.COBALT_INGOT, null, SlimefunItems.BATTERY, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.ELECTRIC_MOTOR, "ELECTRIC_MOTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, null, SlimefunItems.ELECTRO_MAGNET, null, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.HEATING_COIL, "HEATING_COIL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.COPPER_WIRE, "COPPER_WIRE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT, null, null, null}, new CustomItem(SlimefunItems.COPPER_WIRE, 8))
		.register(true);
		
		new BlockPlacer(Categories.MACHINES_1, SlimefunItems.BLOCK_PLACER, "BLOCK_PLACER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_4K, new ItemStack(Material.PISTON), SlimefunItems.GOLD_4K, new ItemStack(Material.IRON_INGOT), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.IRON_INGOT), SlimefunItems.GOLD_4K, new ItemStack(Material.PISTON), SlimefunItems.GOLD_4K}, 
		new String[] {"unplaceable-blocks"}, new Object[] {Stream.of(Material.STRUCTURE_BLOCK, Material.COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK).map(Material::toString).collect(Collectors.toList())})
		.register(true);

		new TelepositionScroll(Categories.MAGIC, SlimefunItems.SCROLL_OF_DIMENSIONAL_TELEPOSITION, "SCROLL_OF_DIMENSIONAL_TELEPOSITION", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, null})
		.register(true);

		new SlimefunBow(SlimefunItems.EXPLOSIVE_BOW, "EXPLOSIVE_BOW",
		new ItemStack[] {null, new ItemStack(Material.STICK), new ItemStack(Material.GUNPOWDER), SlimefunItems.STAFF_FIRE, null, SlimefunItems.SULFATE, null, new ItemStack(Material.STICK), new ItemStack(Material.GUNPOWDER)})
		.register(true, new BowShootHandler() {

			@Override
			public boolean onHit(EntityDamageByEntityEvent e, LivingEntity n) {
				if (SlimefunManager.isItemSimiliar(SlimefunPlugin.getUtilities().arrows.get(e.getDamager().getUniqueId()), SlimefunItems.EXPLOSIVE_BOW, true)) {
					Vector vector = n.getVelocity();
					vector.setY(0.6);
					n.setVelocity(vector);
					n.getWorld().createExplosion(n.getLocation(), 0F);
					n.getWorld().playSound(n.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
					return true;
				}
				else return false;
			}
		});

		new SlimefunBow(SlimefunItems.ICY_BOW, "ICY_BOW",
		new ItemStack[] {null, new ItemStack(Material.STICK), new ItemStack(Material.ICE), SlimefunItems.STAFF_WATER, null, new ItemStack(Material.PACKED_ICE), null, new ItemStack(Material.STICK), new ItemStack(Material.ICE)})
		.register(true, new BowShootHandler() {

			@Override
			public boolean onHit(EntityDamageByEntityEvent e, LivingEntity n) {
				if (SlimefunManager.isItemSimiliar(SlimefunPlugin.getUtilities().arrows.get(e.getDamager().getUniqueId()), SlimefunItems.ICY_BOW, true)) {
					n.getWorld().playEffect(n.getLocation(), Effect.STEP_SOUND, Material.ICE);
					n.getWorld().playEffect(n.getEyeLocation(), Effect.STEP_SOUND, Material.ICE);
					n.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 10));
					n.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 2, -10));
					return true;
				}
				else return false;
			}
		});

		new KnowledgeTome(Categories.MAGIC, SlimefunItems.TOME_OF_KNOWLEDGE_SHARING, "TOME_OF_KNOWLEDGE_SHARING", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.FEATHER), null, new ItemStack(Material.INK_SAC), SlimefunItems.MAGICAL_BOOK_COVER, new ItemStack(Material.GLASS_BOTTLE), null, new ItemStack(Material.WRITABLE_BOOK), null})
		.register(true);

		new KnowledgeFlask(Categories.MAGIC, SlimefunItems.FLASK_OF_KNOWLEDGE, "FLASK_OF_KNOWLEDGE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, null, null, SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.GLASS_PANE), SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, null}, new CustomItem(SlimefunItems.FLASK_OF_KNOWLEDGE, 8))
		.register(true);

		new ExcludedBlock(Categories.BIRTHDAY, new CustomItem(new ItemStack(Material.CAKE), "&bBirthday Cake"), "BIRTHDAY_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.TORCH), null, new ItemStack(Material.SUGAR), new ItemStack(Material.CAKE), new ItemStack(Material.SUGAR), null, null, null})
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_MILK, "CHRISTMAS_MILK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.GLASS_BOTTLE), null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.CHRISTMAS_MILK, 4))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, "CHRISTMAS_CHOCOLATE_MILK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CHRISTMAS_MILK, new ItemStack(Material.COCOA_BEANS), null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_EGG_NOG, "CHRISTMAS_EGG_NOG", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CHRISTMAS_MILK, new ItemStack(Material.EGG), null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.CHRISTMAS_EGG_NOG, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_APPLE_CIDER, "CHRISTMAS_APPLE_CIDER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.APPLE_JUICE, new ItemStack(Material.SUGAR), null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.CHRISTMAS_APPLE_CIDER, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_COOKIE, "CHRISTMAS_COOKIE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.COOKIE), new ItemStack(Material.SUGAR), new ItemStack(Material.LIME_DYE), null, null, null, null, null, null}, new CustomItem(SlimefunItems.CHRISTMAS_COOKIE, 16))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_FRUIT_CAKE, "CHRISTMAS_FRUIT_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.EGG), new ItemStack(Material.APPLE), new ItemStack(Material.MELON), new ItemStack(Material.SUGAR), null, null, null, null, null}, new CustomItem(SlimefunItems.CHRISTMAS_FRUIT_CAKE, 4))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_APPLE_PIE, "CHRISTMAS_APPLE_PIE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.APPLE), new ItemStack(Material.EGG), null, null, null, null, null, null}, new CustomItem(SlimefunItems.CHRISTMAS_APPLE_PIE, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_HOT_CHOCOLATE, "CHRISTMAS_HOT_CHOCOLATE", RecipeType.SMELTERY,
		new ItemStack[] {SlimefunItems.CHRISTMAS_CHOCOLATE_MILK, null, null, null, null, null, null, null, null}, SlimefunItems.CHRISTMAS_HOT_CHOCOLATE)
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_CAKE, "CHRISTMAS_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.MILK_BUCKET), null, null, null, null, null}, new CustomItem(SlimefunItems.CHRISTMAS_CAKE, 4))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_CARAMEL, "CHRISTMAS_CARAMEL", RecipeType.SMELTERY,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.SUGAR), null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.CHRISTMAS_CARAMEL, 4))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_CARAMEL_APPLE, "CHRISTMAS_CARAMEL_APPLE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.SUGAR), null, null, new ItemStack(Material.APPLE), null, null, new ItemStack(Material.STICK), null}, new CustomItem(SlimefunItems.CHRISTMAS_CARAMEL_APPLE, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_CHOCOLATE_APPLE, "CHRISTMAS_CHOCOLATE_APPLE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.COCOA_BEANS), null, null, new ItemStack(Material.APPLE), null, null, new ItemStack(Material.STICK), null}, new CustomItem(SlimefunItems.CHRISTMAS_CARAMEL_APPLE, 2))
		.register(true);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.CHRISTMAS_PRESENT, "CHRISTMAS_PRESENT", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.NAME_TAG), null, new ItemStack(Material.RED_WOOL), new ItemStack(Material.GREEN_WOOL), new ItemStack(Material.RED_WOOL), new ItemStack(Material.RED_WOOL), new ItemStack(Material.GREEN_WOOL), new ItemStack(Material.RED_WOOL)})
		.register(true);

		new SlimefunItem(Categories.EASTER, SlimefunItems.EASTER_CARROT_PIE, "EASTER_CARROT_PIE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.CARROT), new ItemStack(Material.EGG), null, null, null, null, null, null}, new CustomItem(SlimefunItems.EASTER_CARROT_PIE, 2))
		.register(true);

		new SlimefunItem(Categories.EASTER, SlimefunItems.CHRISTMAS_APPLE_PIE, "EASTER_APPLE_PIE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.APPLE), new ItemStack(Material.EGG), null, null, null, null, null, null}, new CustomItem(SlimefunItems.CHRISTMAS_APPLE_PIE, 2))
		.register(true);

		new SlimefunItem(Categories.EASTER, SlimefunItems.EASTER_EGG, "EASTER_EGG", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.LIME_DYE), new ItemStack(Material.EGG), new ItemStack(Material.PURPLE_DYE), null, null, null}, new CustomItem(SlimefunItems.EASTER_EGG, 2))
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.EASTER_EGG, true)) {
					e.setCancelled(true);
					PlayerInventory.consumeItemInHand(e.getPlayer());
					FireworkShow.launchRandom(e.getPlayer(), 2);

					List<ItemStack> gifts = new ArrayList<>();
					
					for (int i = 0; i < 2; i++) {
						gifts.add(new CustomItem(SlimefunItems.EASTER_CARROT_PIE, 4));
						gifts.add(new CustomItem(SlimefunItems.CARROT_JUICE, 1));
						gifts.add(new ItemStack(Material.EMERALD));
						gifts.add(new ItemStack(Material.CAKE));
						gifts.add(new ItemStack(Material.RABBIT_FOOT));
						gifts.add(new ItemStack(Material.GOLDEN_CARROT, 4));
					}

					p.getWorld().dropItemNaturally(p.getLocation(), gifts.get(random.nextInt(gifts.size())));
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MISC, SlimefunItems.REINFORCED_PLATE, "REINFORCED_PLATE", RecipeType.COMPRESSOR,
		new ItemStack[] {new CustomItem(SlimefunItems.REINFORCED_ALLOY_INGOT, 8), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.HARDENED_GLASS, "HARDENED_GLASS", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS)},
		new CustomItem(SlimefunItems.HARDENED_GLASS, 16))
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.SOLAR_ARRAY, "SOLAR_ARRAY", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SlimefunItems.SOLAR_PANEL, SlimefunItems.SOLAR_PANEL, SlimefunItems.SOLAR_PANEL, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.COOLING_UNIT, "COOLING_UNIT", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.ICE), new ItemStack(Material.ICE), new ItemStack(Material.ICE), SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_INGOT, new ItemStack(Material.ICE), new ItemStack(Material.ICE), new ItemStack(Material.ICE)})
		.register(true);

		new SlimefunBackpack(27, Categories.PORTABLE, SlimefunItems.COOLER, "COOLER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.COOLING_UNIT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_INGOT})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.WITHER_PROOF_OBSIDIAN, "WITHER_PROOF_OBSIDIAN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LEAD_INGOT, new ItemStack(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT, new ItemStack(Material.OBSIDIAN), SlimefunItems.HARDENED_GLASS, new ItemStack(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT, new ItemStack(Material.OBSIDIAN), SlimefunItems.LEAD_INGOT},
		new CustomItem(SlimefunItems.WITHER_PROOF_OBSIDIAN, 4))
		.register(true);

		new AncientPedestal(Categories.LUMPS_AND_MAGIC, SlimefunItems.ANCIENT_PEDESTAL, "ANCIENT_PEDESTAL", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.STONE), null, new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K, new ItemStack(Material.OBSIDIAN)}, new CustomItem(SlimefunItems.ANCIENT_PEDESTAL, 4))
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.ANCIENT_ALTAR, "ANCIENT_ALTAR", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.ENCHANTING_TABLE), null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K, new ItemStack(Material.OBSIDIAN)})
		.register(true);

		// Slimefun 4

		new EnergyRegulator(Categories.ELECTRICITY, SlimefunItems.ENERGY_REGULATOR, "ENERGY_REGULATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.DUCT_TAPE, "DUCT_TAPE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ALUMINUM_DUST, SlimefunItems.ALUMINUM_DUST, SlimefunItems.ALUMINUM_DUST, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.PAPER), new ItemStack(Material.PAPER), new ItemStack(Material.PAPER)}, new CustomItem(SlimefunItems.DUCT_TAPE, 2))
		.register(true);

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.SMALL_CAPACITOR, "SMALL_CAPACITOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.DURALUMIN_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.DURALUMIN_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.SULFATE, new ItemStack(Material.REDSTONE), SlimefunItems.DURALUMIN_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.DURALUMIN_INGOT})
		.registerDistibutingCapacitor(true, 128);

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.MEDIUM_CAPACITOR, "MEDIUM_CAPACITOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BILLON_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.BILLON_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.SMALL_CAPACITOR, new ItemStack(Material.REDSTONE), SlimefunItems.BILLON_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.BILLON_INGOT})
		.registerDistibutingCapacitor(true, 512);

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.BIG_CAPACITOR, "BIG_CAPACITOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.STEEL_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.MEDIUM_CAPACITOR, new ItemStack(Material.REDSTONE), SlimefunItems.STEEL_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.STEEL_INGOT})
		.registerDistibutingCapacitor(true, 1024);

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.LARGE_CAPACITOR, "LARGE_CAPACITOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REINFORCED_ALLOY_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.BIG_CAPACITOR, new ItemStack(Material.REDSTONE), SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REINFORCED_ALLOY_INGOT})
		.registerDistibutingCapacitor(true, 8192);

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.CARBONADO_EDGED_CAPACITOR, "CARBONADO_EDGED_CAPACITOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CARBONADO, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.CARBONADO, new ItemStack(Material.REDSTONE), SlimefunItems.LARGE_CAPACITOR, new ItemStack(Material.REDSTONE), SlimefunItems.CARBONADO, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.CARBONADO})
		.registerDistibutingCapacitor(true, 65536);

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.SOLAR_GENERATOR, "SOLAR_GENERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SOLAR_PANEL, SlimefunItems.SOLAR_PANEL, SlimefunItems.SOLAR_PANEL, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_INGOT, null, SlimefunItems.ALUMINUM_INGOT, null})
		.register(true, new EnergyTicker() {

			@Override
			public double generateEnergy(Location l, SlimefunItem item, Config data) {
				if (!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4) || l.getBlock().getLightFromSky() != 15) return 0D;
				if (l.getWorld().getTime() < 12300 || l.getWorld().getTime() > 23850) return 2D;
				return 0D;
			}

			@Override
			public boolean explode(Location l) {
				return false;
			}
		});

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.SOLAR_GENERATOR_2, "SOLAR_GENERATOR_2", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SOLAR_GENERATOR, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR, SlimefunItems.ALUMINUM_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR})
		.register(true, new EnergyTicker() {

			@Override
			public double generateEnergy(Location l, SlimefunItem item, Config data) {
				if (!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4) || l.getBlock().getLightFromSky() != 15) return 0D;
				if (l.getWorld().getTime() < 12300 || l.getWorld().getTime() > 23850) return 8;
				return 0D;
			}

			@Override
			public boolean explode(Location l) {
				return false;
			}
		});

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.SOLAR_GENERATOR_3, "SOLAR_GENERATOR_3", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SOLAR_GENERATOR_2, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR_2, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.CARBONADO, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR_2, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.SOLAR_GENERATOR_2})
		.register(true, new EnergyTicker() {

			@Override
			public double generateEnergy(Location l, SlimefunItem item, Config data) {
				if (!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4) || l.getBlock().getLightFromSky() != 15) return 0D;
				if (l.getWorld().getTime() < 12300 || l.getWorld().getTime() > 23850) return 32;
				return 0D;
			}

			@Override
			public boolean explode(Location l) {
				return false;
			}
		});

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.SOLAR_GENERATOR_4, "SOLAR_GENERATOR_4", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SOLAR_GENERATOR_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.SOLAR_GENERATOR_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.SOLAR_GENERATOR_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.SOLAR_GENERATOR_3})
		.register(true, new EnergyTicker() {

			@Override
			public double generateEnergy(Location l, SlimefunItem item, Config data) {
				if (!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4) || l.getBlock().getLightFromSky() != 15) return 0D;
				if (l.getWorld().getTime() < 12300 || l.getWorld().getTime() > 23850) return 128;
				return 64D;
			}

			@Override
			public boolean explode(Location l) {
				return false;
			}
		});

		new ChargingBench(Categories.ELECTRICITY, SlimefunItems.CHARGING_BENCH, "CHARGING_BENCH", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET, null, SlimefunItems.BATTERY, new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.BATTERY, null, SlimefunItems.SMALL_CAPACITOR, null})
		.registerChargeableBlock(true, 128);

		new ElectricFurnace(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_FURNACE, "ELECTRIC_FURNACE", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricFurnace(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_FURNACE_2, "ELECTRIC_FURNACE_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricFurnace(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_FURNACE_3, "ELECTRIC_FURNACE_3", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricGoldPan(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_GOLD_PAN, "ELECTRIC_GOLD_PAN", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricGoldPan(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_GOLD_PAN_2, "ELECTRIC_GOLD_PAN_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricGoldPan(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_GOLD_PAN_3, "ELECTRIC_GOLD_PAN_3", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricDustWasher(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_DUST_WASHER, "ELECTRIC_DUST_WASHER", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricDustWasher(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_DUST_WASHER_2, "ELECTRIC_DUST_WASHER_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricDustWasher(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_DUST_WASHER_3, "ELECTRIC_DUST_WASHER_3", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricIngotFactory(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_INGOT_FACTORY, "ELECTRIC_INGOT_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricIngotFactory(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_INGOT_FACTORY_2, "ELECTRIC_INGOT_FACTORY_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricIngotFactory(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_INGOT_FACTORY_3, "ELECTRIC_INGOT_FACTORY_3", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectrifiedCrucible(Categories.ELECTRICITY, SlimefunItems.ELECTRIFIED_CRUCIBLE, "ELECTRIFIED_CRUCIBLE", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectrifiedCrucible(Categories.ELECTRICITY, SlimefunItems.ELECTRIFIED_CRUCIBLE_2, "ELECTRIFIED_CRUCIBLE_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectrifiedCrucible(Categories.ELECTRICITY, SlimefunItems.ELECTRIFIED_CRUCIBLE_3, "ELECTRIFIED_CRUCIBLE_3", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricOreGrinder(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_ORE_GRINDER, "ELECTRIC_ORE_GRINDER", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricOreGrinder(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_ORE_GRINDER_2, "ELECTRIC_ORE_GRINDER_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new HeatedPressureChamber(Categories.ELECTRICITY, SlimefunItems.HEATED_PRESSURE_CHAMBER, "HEATED_PRESSURE_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new HeatedPressureChamber(Categories.ELECTRICITY, SlimefunItems.HEATED_PRESSURE_CHAMBER_2, "HEATED_PRESSURE_CHAMBER_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricIngotPulverizer(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_INGOT_PULVERIZER, "ELECTRIC_INGOT_PULVERIZER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRIC_ORE_GRINDER, null, SlimefunItems.LEAD_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.LEAD_INGOT})
		.registerChargeableBlock(true, 512);

		new CoalGenerator(Categories.ELECTRICITY, SlimefunItems.COAL_GENERATOR, "COAL_GENERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HEATING_COIL, new ItemStack(Material.FURNACE), SlimefunItems.HEATING_COIL, SlimefunItems.NICKEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.NICKEL_INGOT, null, SlimefunItems.NICKEL_INGOT, null}) {
			
			@Override
			public int getEnergyProduction() {
				return 8;
			}

		}.registerUnrechargeableBlock(true, 64);

		new CoalGenerator(Categories.ELECTRICITY, SlimefunItems.COAL_GENERATOR_2, "COAL_GENERATOR_2", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.MAGMA_BLOCK), SlimefunItems.HEATING_COIL, new ItemStack(Material.MAGMA_BLOCK), SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.COAL_GENERATOR, SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.ELECTRIC_MOTOR, null}) {
			
			@Override
			public int getEnergyProduction() {
				return 15;
			}

		}.registerUnrechargeableBlock(true, 256);

		new BioGenerator(Categories.ELECTRICITY, SlimefunItems.BIO_REACTOR, "BIO_REACTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HEATING_COIL, SlimefunItems.COMPOSTER, SlimefunItems.HEATING_COIL, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT, null, SlimefunItems.ALUMINUM_BRASS_INGOT, null}) {

			@Override
			public int getEnergyProduction() {
				return 4;
			}

		}.registerUnrechargeableBlock(true, 128);

		new AutoDrier(Categories.ELECTRICITY, SlimefunItems.AUTO_DRIER, "AUTO_DRIER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, SlimefunItems.HEATING_COIL, new ItemStack(Material.SMOKER), SlimefunItems.HEATING_COIL, null, new ItemStack(Material.CAMPFIRE), null})
		.registerChargeableBlock(true, 128);

		new AutoEnchanter(Categories.ELECTRICITY, SlimefunItems.AUTO_ENCHANTER, "AUTO_ENCHANTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.ENCHANTING_TABLE), null, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN})
		.registerChargeableBlock(true, 128);

		new AutoDisenchanter(Categories.ELECTRICITY, SlimefunItems.AUTO_DISENCHANTER, "AUTO_DISENCHANTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.REDSTONE), new ItemStack(Material.ANVIL), new ItemStack(Material.REDSTONE), SlimefunItems.CARBONADO, SlimefunItems.AUTO_ENCHANTER, SlimefunItems.CARBONADO, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.WITHER_PROOF_OBSIDIAN})
		.registerChargeableBlock(true, 128);

		new AutoAnvil(Categories.ELECTRICITY, SlimefunItems.AUTO_ANVIL, "AUTO_ANVIL", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new AutoAnvil(Categories.ELECTRICITY, SlimefunItems.AUTO_ANVIL_2, "AUTO_ANVIL_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.MULTIMETER, "MULTIMETER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.COPPER_INGOT, null, SlimefunItems.COPPER_INGOT, null, SlimefunItems.REDSTONE_ALLOY, null, null, SlimefunItems.GOLD_6K, null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(e.getItem(), SlimefunItems.MULTIMETER, true)) {
					if (e.getClickedBlock() != null && ChargableBlock.isChargable(e.getClickedBlock())) {
						e.setCancelled(true);
						p.sendMessage("");
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bStored Energy: &3" + DoubleHandler.getFancyDouble(ChargableBlock.getCharge(e.getClickedBlock())) + " J"));
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bCapacity: &3" + DoubleHandler.getFancyDouble(ChargableBlock.getMaxCharge(e.getClickedBlock())) + " J"));
						p.sendMessage("");
					}
					return true;
				}
				return false;
			}
		});

		new SlimefunItem(Categories.MISC, SlimefunItems.PLASTIC_SHEET, "PLASTIC_SHEET", RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {null, null, null, null, SlimefunItems.BUCKET_OF_OIL, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.ANDROID_MEMORY_CORE, "ANDROID_MEMORY_CORE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BRASS_INGOT, new ItemStack(Material.ORANGE_STAINED_GLASS), SlimefunItems.BRASS_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.TIN_DUST, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BRASS_INGOT, new ItemStack(Material.ORANGE_STAINED_GLASS), SlimefunItems.BRASS_INGOT})
		.register(true);

		new GPSTransmitter(Categories.GPS, SlimefunItems.GPS_TRANSMITTER, "GPS_TRANSMITTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.STEEL_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT}) {
			
			@Override
			public double getMultiplier(int y) {
				return y;
			}

			@Override
			public int getEnergyConsumption() {
				return 1;
			}
			
		}
		.registerChargeableBlock(true, 16);

		new GPSTransmitter(Categories.GPS, SlimefunItems.GPS_TRANSMITTER_2, "GPS_TRANSMITTER_2", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.CARBON, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER}) {
			
			@Override
			public double getMultiplier(int y) {
				return y * 4.0 + 100;
			}

			@Override
			public int getEnergyConsumption() {
				return 3;
			}
			
		}.registerChargeableBlock(true, 64);

		new GPSTransmitter(Categories.GPS, SlimefunItems.GPS_TRANSMITTER_3, "GPS_TRANSMITTER_3", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.CARBONADO, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2}) {
			
			@Override
			public double getMultiplier(int y) {
				return y * 16.0 + 500;
			}

			@Override
			public int getEnergyConsumption() {
				return 11;
			}
			
		}.registerChargeableBlock(true, 256);

		new GPSTransmitter(Categories.GPS, SlimefunItems.GPS_TRANSMITTER_4, "GPS_TRANSMITTER_4", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.NICKEL_INGOT, SlimefunItems.CARBONADO, SlimefunItems.NICKEL_INGOT, SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GPS_TRANSMITTER_3}) {
			
			@Override
			public double getMultiplier(int y) {
				return y * 64.0 + 600;
			}

			@Override
			public int getEnergyConsumption() {
				return 46;
			}
			
		}.registerChargeableBlock(true, 1024);

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_CONTROL_PANEL, "GPS_CONTROL_PANEL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.COBALT_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.COBALT_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				String item = BlockStorage.checkID(e.getClickedBlock());
				if (item == null || !item.equals("GPS_CONTROL_PANEL")) return false;
				e.setCancelled(true);

				Slimefun.getGPSNetwork().openTransmitterControlPanel(p);
				return true;
			}
		});

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_MARKER_TOOL, "GPS_MARKER_TOOL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET, null, new ItemStack(Material.LAPIS_LAZULI), SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.LAPIS_LAZULI), new ItemStack(Material.REDSTONE), SlimefunItems.REDSTONE_ALLOY, new ItemStack(Material.REDSTONE)})
		.register(true);

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_EMERGENCY_TRANSMITTER, "GPS_EMERGENCY_TRANSMITTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRO_MAGNET, null, null, SlimefunItems.GPS_TRANSMITTER, null, null, SlimefunItems.ESSENCE_OF_AFTERLIFE, null})
		.register(true);

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID, "PROGRAMMABLE_ANDROID", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_MINER, "PROGRAMMABLE_ANDROID_MINER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_PICKAXE), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_PICKAXE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.MINER;
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

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_FARMER, "PROGRAMMABLE_ANDROID_FARMER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_HOE), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_HOE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.FARMER;
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

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER, "PROGRAMMABLE_ANDROID_WOODCUTTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_AXE), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_AXE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.WOODCUTTER;
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

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_FISHERMAN, "PROGRAMMABLE_ANDROID_FISHERMAN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.FISHERMAN;
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

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_BUTCHER, "PROGRAMMABLE_ANDROID_BUTCHER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.FIGHTER;
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

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.ANDROID_INTERFACE_ITEMS, "ANDROID_INTERFACE_ITEMS", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.BLUE_STAINED_GLASS), SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET})
		.register(true);

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.ANDROID_INTERFACE_FUEL, "ANDROID_INTERFACE_FUEL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.RED_STAINED_GLASS), SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET})
		.register(true);


		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_2, "PROGRAMMABLE_ANDROID_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_2_FISHERMAN, "PROGRAMMABLE_ANDROID_2_FISHERMAN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.FISHERMAN;
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

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_2_BUTCHER, "PROGRAMMABLE_ANDROID_2_BUTCHER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.FIGHTER;
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

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_2_FARMER, "PROGRAMMABLE_ANDROID_2_FARMER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, new ItemStack(Material.DIAMOND_HOE), SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.DIAMOND_HOE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.ADVANCED_FARMER;
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


		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_3, "PROGRAMMABLE_ANDROID_3", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_3_FISHERMAN, "PROGRAMMABLE_ANDROID_3_FISHERMAN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID_3, new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.FISHERMAN;
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

		new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_3_BUTCHER, "PROGRAMMABLE_ANDROID_3_BUTCHER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER_3, null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID_3, new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public AndroidType getAndroidType() {
				return AndroidType.FIGHTER;
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

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.BLANK_RUNE, "BLANK_RUNE", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.OBSIDIAN), SlimefunItems.MAGIC_LUMP_1,new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.STONE)})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.RUNE_AIR, "ANCIENT_RUNE_AIR", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.FEATHER), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.FEATHER), new ItemStack(Material.GHAST_TEAR), SlimefunItems.BLANK_RUNE, new ItemStack(Material.GHAST_TEAR) ,new ItemStack(Material.FEATHER), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.FEATHER)}, new CustomItem(SlimefunItems.RUNE_AIR, 4))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.RUNE_EARTH, "ANCIENT_RUNE_EARTH", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.DIRT), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.STONE), new ItemStack(Material.OBSIDIAN), SlimefunItems.BLANK_RUNE, new ItemStack(Material.OBSIDIAN) ,new ItemStack(Material.STONE), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.DIRT)}, new CustomItem(SlimefunItems.RUNE_EARTH, 4))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.RUNE_FIRE, "ANCIENT_RUNE_FIRE", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.FIRE_CHARGE), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.FIRE_CHARGE), new ItemStack(Material.BLAZE_POWDER), SlimefunItems.RUNE_EARTH, new ItemStack(Material.FLINT_AND_STEEL) ,new ItemStack(Material.FIRE_CHARGE), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.FIRE_CHARGE)}, new CustomItem(SlimefunItems.RUNE_FIRE, 4))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.RUNE_WATER, "ANCIENT_RUNE_WATER", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.SALMON), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.SAND), SlimefunItems.BLANK_RUNE, new ItemStack(Material.SAND) ,new ItemStack(Material.WATER_BUCKET), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.COD)}, new CustomItem(SlimefunItems.RUNE_WATER, 4))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.RUNE_ENDER, "ANCIENT_RUNE_ENDER", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_EYE), SlimefunItems.BLANK_RUNE, new ItemStack(Material.ENDER_EYE), new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.ENDER_PEARL)}, new CustomItem(SlimefunItems.RUNE_ENDER, 6))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.RUNE_LIGHTNING, "ANCIENT_RUNE_LIGHTNING", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.IRON_INGOT), SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.IRON_INGOT), SlimefunItems.RUNE_AIR, new ItemStack(Material.PHANTOM_MEMBRANE), SlimefunItems.RUNE_WATER, new ItemStack(Material.IRON_INGOT), SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.IRON_INGOT)}, new CustomItem(SlimefunItems.RUNE_LIGHTNING, 4))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.RUNE_RAINBOW, "ANCIENT_RUNE_RAINBOW", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.CYAN_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RUNE_ENDER, new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.YELLOW_DYE), SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.MAGENTA_DYE)})
		.register(true);

        new SoulboundRune(Categories.LUMPS_AND_MAGIC, SlimefunItems.RUNE_SOULBOUND, "ANCIENT_RUNE_SOULBOUND", RecipeType.ANCIENT_ALTAR,
        new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_ENDER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.MAGIC_LUMP_3})
        .register(true);

		new InfernalBonemeal(Categories.MAGIC, SlimefunItems.INFERNAL_BONEMEAL, "INFERNAL_BONEMEAL", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.NETHER_WART), SlimefunItems.RUNE_EARTH, new ItemStack(Material.NETHER_WART), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.BONE_MEAL), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.NETHER_WART), new ItemStack(Material.BLAZE_POWDER), new ItemStack(Material.NETHER_WART)}, new CustomItem(SlimefunItems.INFERNAL_BONEMEAL, 8))
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.ELYTRA_SCALE, "ELYTRA_SCALE", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_AIR, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_ENDER, new ItemStack(Material.FEATHER), SlimefunItems.RUNE_ENDER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.RUNE_AIR, SlimefunItems.ENDER_LUMP_3})
		.register(true);

		new VanillaItem(Categories.MAGIC, SlimefunItems.ELYTRA, "ELYTRA", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.ELYTRA_SCALE, SlimefunItems.RUNE_AIR, SlimefunItems.ELYTRA_SCALE, SlimefunItems.RUNE_AIR, new ItemStack(Material.LEATHER_CHESTPLATE), SlimefunItems.RUNE_AIR, SlimefunItems.ELYTRA_SCALE, SlimefunItems.RUNE_AIR, SlimefunItems.ELYTRA_SCALE})
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.INFUSED_ELYTRA, "INFUSED_ELYTRA", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.FLASK_OF_KNOWLEDGE})
		.register(true);

		new SoulboundItem(Categories.MAGIC, SlimefunItems.SOULBOUND_ELYTRA, "SOULBOUND_ELYTRA", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ELYTRA_SCALE, SlimefunItems.ELYTRA, SlimefunItems.ELYTRA_SCALE, SlimefunItems.FLASK_OF_KNOWLEDGE, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.FLASK_OF_KNOWLEDGE})
		.register(true);

		RainbowTicker rainbow = new RainbowTicker();

		new SlimefunItem(Categories.MAGIC, SlimefunItems.RAINBOW_WOOL, "RAINBOW_WOOL", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL)}, new CustomItem(SlimefunItems.RAINBOW_WOOL, 8))
		.register(true, rainbow);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.RAINBOW_GLASS, "RAINBOW_GLASS", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.WHITE_STAINED_GLASS)}, new CustomItem(SlimefunItems.RAINBOW_GLASS, 8))
		.register(true, rainbow);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.RAINBOW_GLASS_PANE, "RAINBOW_GLASS_PANE", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE)}, new CustomItem(SlimefunItems.RAINBOW_GLASS_PANE, 8))
		.register(true, rainbow);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.RAINBOW_CLAY, "RAINBOW_CLAY", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.WHITE_TERRACOTTA)}, new CustomItem(SlimefunItems.RAINBOW_CLAY, 8))
		.register(true, rainbow);

		RainbowTicker xmas = new RainbowTicker(13, 14);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.RAINBOW_WOOL_XMAS, "RAINBOW_WOOL_XMAS", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.RED_DYE)}, new CustomItem(SlimefunItems.RAINBOW_WOOL_XMAS, 2))
		.register(true, xmas);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.RAINBOW_GLASS_XMAS, "RAINBOW_GLASS_XMAS", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.RED_DYE)}, new CustomItem(SlimefunItems.RAINBOW_GLASS_XMAS, 2))
		.register(true, xmas);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.RAINBOW_GLASS_PANE_XMAS, "RAINBOW_GLASS_PANE_XMAS", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.RED_DYE)}, new CustomItem(SlimefunItems.RAINBOW_GLASS_PANE_XMAS, 2))
		.register(true, xmas);

		new SlimefunItem(Categories.CHRISTMAS, SlimefunItems.RAINBOW_CLAY_XMAS, "RAINBOW_CLAY_XMAS", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.GREEN_DYE), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.GREEN_DYE), SlimefunItems.CHRISTMAS_COOKIE, new ItemStack(Material.RED_DYE)}, new CustomItem(SlimefunItems.RAINBOW_CLAY_XMAS, 2))
		.register(true, xmas);

		RainbowTicker valentine = new RainbowTicker(2, 6, 10);

		new SlimefunItem(Categories.VALENTINES_DAY, SlimefunItems.RAINBOW_WOOL_VALENTINE, "RAINBOW_WOOL_VALENTINE", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)}, new CustomItem(SlimefunItems.RAINBOW_WOOL_VALENTINE, 2))
		.register(true, valentine);

		new SlimefunItem(Categories.VALENTINES_DAY, SlimefunItems.RAINBOW_GLASS_VALENTINE, "RAINBOW_GLASS_VALENTINE", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_STAINED_GLASS), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)}, new CustomItem(SlimefunItems.RAINBOW_GLASS_VALENTINE, 2))
		.register(true, valentine);

		new SlimefunItem(Categories.VALENTINES_DAY, SlimefunItems.RAINBOW_GLASS_PANE_VALENTINE, "RAINBOW_GLASS_PANE_VALENTINE", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_STAINED_GLASS_PANE), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)}, new CustomItem(SlimefunItems.RAINBOW_GLASS_PANE_VALENTINE, 2))
		.register(true, valentine);

		new SlimefunItem(Categories.VALENTINES_DAY, SlimefunItems.RAINBOW_CLAY_VALENTINE, "RAINBOW_CLAY_VALENTINE", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.PINK_DYE), new ItemStack(Material.WHITE_TERRACOTTA), SlimefunItems.RUNE_RAINBOW, new ItemStack(Material.WHITE_TERRACOTTA), new ItemStack(Material.PINK_DYE), new ItemStack(Material.POPPY), new ItemStack(Material.RED_DYE)}, new CustomItem(SlimefunItems.RAINBOW_CLAY_VALENTINE, 2))
		.register(true, valentine);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.WITHER_PROOF_GLASS, "WITHER_PROOF_GLASS", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.LEAD_INGOT, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.HARDENED_GLASS, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.LEAD_INGOT, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.LEAD_INGOT},
		new CustomItem(SlimefunItems.WITHER_PROOF_GLASS, 4))
		.register(true);
		
		new GEOScannerBlock(Categories.GPS, SlimefunItems.GPS_GEO_SCANNER, "GPS_GEO_SCANNER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, null, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRO_MAGNET})
		.register(true);
		
		new PortableGEOScanner(Categories.GPS, SlimefunItems.PORTABLE_GEO_SCANNER, "PORTABLE_GEO_SCANNER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ELECTRO_MAGNET, new ItemStack(Material.COMPASS), SlimefunItems.ELECTRO_MAGNET, SlimefunItems.STEEL_INGOT, SlimefunItems.GPS_MARKER_TOOL, SlimefunItems.STEEL_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.SOLDER_INGOT})
		.register(true);

		new OilPump(Categories.GPS, SlimefunItems.OIL_PUMP, "OIL_PUMP", RecipeType.ENHANCED_CRAFTING_TABLE,
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
		
		new GEOMiner(Categories.GPS, SlimefunItems.GEO_MINER, "GEO_MINER", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new NetherDrill(Categories.GPS, SlimefunItems.NETHER_DRILL, "NETHER_DRILL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, null, new CustomItem(Material.BARRIER, "&4DEPRECATED", "", "&cThis Item will soon be removed.", "&cUse the GEO Miner instead."), null, null, null, null}){
			
			@Override
			public int getSpeed() {
				return 1;
			}

			@Override
			public int getEnergyConsumption() {
				return 51;
			}
			
		}.registerChargeableBlock(true, 1024);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.BUCKET_OF_OIL, "BUCKET_OF_OIL", new RecipeType(SlimefunItems.OIL_PUMP),
		new ItemStack[] {null, null, null, null, new ItemStack(Material.BUCKET), null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.BUCKET_OF_FUEL, "BUCKET_OF_FUEL", new RecipeType(SlimefunItems.REFINERY),
		new ItemStack[] {null, null, null, null, SlimefunItems.BUCKET_OF_OIL, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.NETHER_ICE, "NETHER_ICE", new RecipeType(SlimefunItems.GEO_MINER),
		new ItemStack[] {null, null, null, null, null, null, null, null})
		.register(true);

		new Refinery(Categories.ELECTRICITY, SlimefunItems.REFINERY, "REFINERY", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new LavaGenerator(Categories.ELECTRICITY, SlimefunItems.LAVA_GENERATOR, "LAVA_GENERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GOLD_16K, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL}) {
			
			@Override
			public int getEnergyProduction() {
				return 10;
			}

		}.registerUnrechargeableBlock(true, 512);

		new LavaGenerator(Categories.ELECTRICITY, SlimefunItems.LAVA_GENERATOR_2, "LAVA_GENERATOR_2", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.LAVA_GENERATOR, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.HEATING_COIL, SlimefunItems.COMPRESSED_CARBON, SlimefunItems.HEATING_COIL}) {
			
			@Override
			public int getEnergyProduction() {
				return 20;
			}

		}.registerUnrechargeableBlock(true, 1024);

		new CombustionGenerator(Categories.ELECTRICITY, SlimefunItems.COMBUSTION_REACTOR, "COMBUSTION_REACTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.STEEL_INGOT, SlimefunItems.HEATING_COIL}) {
			
			@Override
			public int getEnergyProduction() {
				return 12;
			}

		}.registerUnrechargeableBlock(true, 256);

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_TELEPORTER_PYLON, "GPS_TELEPORTER_PYLON", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ZINC_INGOT, new ItemStack(Material.GLASS), SlimefunItems.ZINC_INGOT, new ItemStack(Material.GLASS), SlimefunItems.HEATING_COIL, new ItemStack(Material.GLASS), SlimefunItems.ZINC_INGOT, new ItemStack(Material.GLASS), SlimefunItems.ZINC_INGOT}, new CustomItem(SlimefunItems.GPS_TELEPORTER_PYLON, 8))
		.register(true, new RainbowTicker(9, 10));

		new Teleporter(Categories.GPS, SlimefunItems.GPS_TELEPORTATION_MATRIX, "GPS_TELEPORTATION_MATRIX", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TELEPORTER_PYLON, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.GPS_TELEPORTER_PYLON, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.GPS_CONTROL_PANEL, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.GPS_TELEPORTER_PYLON, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.GPS_TELEPORTER_PYLON}) {
			
			@Override
			public void onInteract(final Player p, final Block b) {
				GPSNetwork.openTeleporterGUI(p, UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), b, Slimefun.getGPSNetwork().getNetworkComplexity(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"))));
			}

		}
		.register(true);

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_ACTIVATION_DEVICE_SHARED, "GPS_ACTIVATION_DEVICE_SHARED", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.STONE_PRESSURE_PLATE), null, new ItemStack(Material.REDSTONE), SlimefunItems.GPS_TRANSMITTER, new ItemStack(Material.REDSTONE), SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT})
		.register(true);

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_ACTIVATION_DEVICE_PERSONAL, "GPS_ACTIVATION_DEVICE_PERSONAL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.LEAD_INGOT, null, SlimefunItems.COBALT_INGOT, SlimefunItems.GPS_ACTIVATION_DEVICE_SHARED, SlimefunItems.COBALT_INGOT, null, SlimefunItems.LEAD_INGOT, null})
		.register(true);

		SlimefunItem.registerBlockHandler("GPS_ACTIVATION_DEVICE_PERSONAL", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				return true;
			}
		});

		new InfusedHopper(Categories.MAGIC, SlimefunItems.INFUSED_HOPPER, "INFUSED_HOPPER", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.OBSIDIAN), SlimefunItems.RUNE_EARTH, new ItemStack(Material.HOPPER), SlimefunItems.RUNE_ENDER, SlimefunItems.INFUSED_MAGNET, SlimefunItems.RUNE_ENDER, new ItemStack(Material.HOPPER), SlimefunItems.RUNE_EARTH, new ItemStack(Material.OBSIDIAN)})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.BLISTERING_INGOT, "BLISTERING_INGOT", RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.URANIUM, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.BLISTERING_INGOT_2, "BLISTERING_INGOT_2", RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.BLISTERING_INGOT, SlimefunItems.CARBONADO, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.BLISTERING_INGOT_3, "BLISTERING_INGOT_3", RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.BLISTERING_INGOT_2,new ItemStack(Material.NETHER_STAR), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.ENRICHED_NETHER_ICE, "ENRICHED_NETHER_ICE", RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[]{SlimefunItems.NETHER_ICE, SlimefunItems.PLUTONIUM, null, null, null, null, null, null, null})
		.register(true);

		new ElevatorPlate(Categories.GPS, SlimefunItems.ELEVATOR, "ELEVATOR_PLATE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.STONE_PRESSURE_PLATE), null, new ItemStack(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.PISTON), SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT},
		new CustomItem(SlimefunItems.ELEVATOR, 2))
		.register(true);

		new FoodFabricator(Categories.ELECTRICITY, SlimefunItems.FOOD_FABRICATOR, "FOOD_FABRICATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new FoodFabricator(Categories.ELECTRICITY, SlimefunItems.FOOD_FABRICATOR_2, "FOOD_FABRICATOR_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new SlimefunItem(Categories.MISC, SlimefunItems.WHEAT_ORGANIC_FOOD, "WHEAT_ORGANIC_FOOD", new RecipeType(SlimefunItems.FOOD_FABRICATOR),
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.WHEAT), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.CARROT_ORGANIC_FOOD, "CARROT_ORGANIC_FOOD", new RecipeType(SlimefunItems.FOOD_FABRICATOR),
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.CARROT), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.POTATO_ORGANIC_FOOD, "POTATO_ORGANIC_FOOD", new RecipeType(SlimefunItems.FOOD_FABRICATOR),
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.POTATO), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.SEEDS_ORGANIC_FOOD, "SEEDS_ORGANIC_FOOD", new RecipeType(SlimefunItems.FOOD_FABRICATOR),
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.WHEAT_SEEDS), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.BEETROOT_ORGANIC_FOOD, "BEETROOT_ORGANIC_FOOD", new RecipeType(SlimefunItems.FOOD_FABRICATOR),
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.BEETROOT), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.MELON_ORGANIC_FOOD, "MELON_ORGANIC_FOOD", new RecipeType(SlimefunItems.FOOD_FABRICATOR),
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.MELON), null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.APPLE_ORGANIC_FOOD, "APPLE_ORGANIC_FOOD", new RecipeType(SlimefunItems.FOOD_FABRICATOR),
		new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.APPLE), null, null, null, null, null, null, null})
		.register(true);

		new AutoBreeder(Categories.ELECTRICITY, SlimefunItems.AUTO_BREEDER, "AUTO_BREEDER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_18K, SlimefunItems.CAN, SlimefunItems.GOLD_18K, SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.HAY_BLOCK), SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.FOOD_FABRICATOR, SlimefunItems.LEAD_INGOT})
		.registerChargeableBlock(true, 1024);

		new AnimalGrowthAccelerator(Categories.ELECTRICITY, SlimefunItems.ANIMAL_GROWTH_ACCELERATOR, "ANIMAL_GROWTH_ACCELERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.WHEAT_ORGANIC_FOOD, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.AUTO_BREEDER, SlimefunItems.REINFORCED_ALLOY_INGOT})
		.registerChargeableBlock(true, 1024);

		new XPCollector(Categories.ELECTRICITY, SlimefunItems.XP_COLLECTOR, "XP_COLLECTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.AUTO_ENCHANTER, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRONZE_INGOT})
		.registerChargeableBlock(true, 1024);

		new FoodComposter(Categories.ELECTRICITY, SlimefunItems.FOOD_COMPOSTER, "FOOD_COMPOSTER", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new FoodComposter(Categories.ELECTRICITY, SlimefunItems.FOOD_COMPOSTER_2, "FOOD_COMPOSTER_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new SlimefunItem(Categories.MISC, SlimefunItems.WHEAT_FERTILIZER, "WHEAT_FERTILIZER", new RecipeType(SlimefunItems.FOOD_COMPOSTER),
		new ItemStack[] {SlimefunItems.WHEAT_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.CARROT_FERTILIZER, "CARROT_FERTILIZER", new RecipeType(SlimefunItems.FOOD_COMPOSTER),
		new ItemStack[] {SlimefunItems.CARROT_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.POTATO_FERTILIZER, "POTATO_FERTILIZER", new RecipeType(SlimefunItems.FOOD_COMPOSTER),
		new ItemStack[] {SlimefunItems.POTATO_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.SEEDS_FERTILIZER, "SEEDS_FERTILIZER", new RecipeType(SlimefunItems.FOOD_COMPOSTER),
		new ItemStack[] {SlimefunItems.SEEDS_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.BEETROOT_FERTILIZER, "BEETROOT_FERTILIZER", new RecipeType(SlimefunItems.FOOD_COMPOSTER),
		new ItemStack[] {SlimefunItems.BEETROOT_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.MELON_FERTILIZER, "MELON_FERTILIZER", new RecipeType(SlimefunItems.FOOD_COMPOSTER),
		new ItemStack[] {SlimefunItems.MELON_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.APPLE_FERTILIZER, "APPLE_FERTILIZER", new RecipeType(SlimefunItems.FOOD_COMPOSTER),
		new ItemStack[] {SlimefunItems.APPLE_ORGANIC_FOOD, null, null, null, null, null, null, null, null})
		.register(true);

		new CropGrowthAccelerator(Categories.ELECTRICITY, SlimefunItems.CROP_GROWTH_ACCELERATOR, "CROP_GROWTH_ACCELERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new CropGrowthAccelerator(Categories.ELECTRICITY, SlimefunItems.CROP_GROWTH_ACCELERATOR_2, "CROP_GROWTH_ACCELERATOR_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new Freezer(Categories.ELECTRICITY, SlimefunItems.FREEZER, "FREEZER", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new Freezer(Categories.ELECTRICITY, SlimefunItems.FREEZER_2, "FREEZER_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.REACTOR_COOLANT_CELL, "REACTOR_COOLANT_CELL", new RecipeType(SlimefunItems.FREEZER),
		new ItemStack[] {new ItemStack(Material.BLUE_ICE), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.NETHER_ICE_COOLANT_CELL, "NETHER_ICE_COOLANT_CELL", new RecipeType(SlimefunItems.HEATED_PRESSURE_CHAMBER),
		new ItemStack[]{SlimefunItems.ENRICHED_NETHER_ICE, null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.NEPTUNIUM, "NEPTUNIUM", new RecipeType(SlimefunItems.NUCLEAR_REACTOR),
		new ItemStack[] {SlimefunItems.URANIUM, null, null, null, null, null, null, null, null})
		.register(true);

		SlimefunItem.setRadioactive(SlimefunItems.NEPTUNIUM);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.PLUTONIUM, "PLUTONIUM", new RecipeType(SlimefunItems.NUCLEAR_REACTOR),
		new ItemStack[] {SlimefunItems.NEPTUNIUM, null, null, null, null, null, null, null, null})
		.register(true);

		SlimefunItem.setRadioactive(SlimefunItems.PLUTONIUM);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.BOOSTED_URANIUM, "BOOSTED_URANIUM", RecipeType.HEATED_PRESSURE_CHAMBER,
		new ItemStack[] {SlimefunItems.PLUTONIUM, SlimefunItems.URANIUM, null, null, null, null, null, null, null})
		.register(true);

		SlimefunItem.setRadioactive(SlimefunItems.BOOSTED_URANIUM);

		new AReactor(Categories.ELECTRICITY, SlimefunItems.NUCLEAR_REACTOR, "NUCLEAR_REACTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.REINFORCED_PLATE, SlimefunItems.COOLING_UNIT, SlimefunItems.REINFORCED_PLATE, SlimefunItems.LEAD_INGOT, SlimefunItems.REINFORCED_PLATE, SlimefunItems.LEAD_INGOT}){

            @Override
			public String getInventoryTitle() {
				return "&2Nuclear Reactor";
			}

			@Override
			public void registerDefaultRecipes() {
				registerFuel(new MachineFuel(1200, SlimefunItems.URANIUM, SlimefunItems.NEPTUNIUM));
				registerFuel(new MachineFuel(600, SlimefunItems.NEPTUNIUM, SlimefunItems.PLUTONIUM));
				registerFuel(new MachineFuel(1500, SlimefunItems.BOOSTED_URANIUM, null));
			}

			@Override
			public int getEnergyProduction() {
				return 250;
			}

			@Override
			public ItemStack getProgressBar() {
				try {
					return CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNhZDhlZTg0OWVkZjA0ZWQ5YTI2Y2EzMzQxZjYwMzNiZDc2ZGNjNDIzMWVkMWVhNjNiNzU2NTc1MWIyN2FjIn19fQ==");
				} catch (Exception e) {
					Slimefun.getLogger().log(Level.SEVERE, "An Error occured while creating the Progressbar of a Reactor for Slimefun " + Slimefun.getVersion());
					return new ItemStack(Material.BLAZE_POWDER);
				}
			}

            @Override
            public ItemStack getCoolant() {
                return SlimefunItems.REACTOR_COOLANT_CELL;
            }

			@Override
			public void extraTick(Location l) {
				// This machine does not need to perform anything while ticking
				// The Nether Star Reactor uses this method to generate the Wither Effect
			}
		}
		.registerChargeableBlock(true, 16384);

		new AReactor(Categories.ELECTRICITY, SlimefunItems.NETHERSTAR_REACTOR, "NETHERSTAR_REACTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[]{SlimefunItems.BOOSTED_URANIUM, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BOOSTED_URANIUM, SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.NETHER_STAR), SlimefunItems.REINFORCED_PLATE, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.REINFORCED_PLATE, SlimefunItems.CORINTHIAN_BRONZE_INGOT}){

			@Override
			public String getInventoryTitle() {
				return "&fNether Star Reactor";
			}

			@Override
			public void registerDefaultRecipes() {
				registerFuel(new MachineFuel(1800, new ItemStack(Material.NETHER_STAR)));
			}

			@Override
			public int getEnergyProduction() {
				return 512;
			}

			@Override
			public void extraTick(final Location l) {
				Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> {
					for (Entity entity : ReactorHologram.getArmorStand(l, true).getNearbyEntities(5, 5, 5)) {
						if (entity instanceof LivingEntity) {
							((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
						}
					}
				}, 0L);
			}

            @Override
            public ItemStack getCoolant() {
                return SlimefunItems.NETHER_ICE_COOLANT_CELL;
            }

            @Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.NETHER_STAR);
			}

		}.registerChargeableBlock(true, 32768);

		new SlimefunItem(Categories.CARGO, SlimefunItems.CARGO_MOTOR, "CARGO_MOTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HARDENED_GLASS, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.HARDENED_GLASS, SlimefunItems.SILVER_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.SILVER_INGOT, SlimefunItems.HARDENED_GLASS, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.HARDENED_GLASS}, new CustomItem(SlimefunItems.CARGO_MOTOR, 4))
		.register(true);

		new CargoManagerBlock(Categories.CARGO, SlimefunItems.CARGO_MANAGER, "CARGO_MANAGER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.HOLOGRAM_PROJECTOR, null, SlimefunItems.REINFORCED_PLATE, SlimefunItems.CARGO_MOTOR, SlimefunItems.REINFORCED_PLATE, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.ALUMINUM_BRONZE_INGOT})
		.register(true);

		new SlimefunItem(Categories.CARGO, SlimefunItems.CARGO_NODE, "CARGO_NODE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT}, new CustomItem(SlimefunItems.CARGO_NODE, 4))
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(final ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				String id = BlockStorage.checkID(e.getClickedBlock());
				if (id == null || !id.equals("CARGO_NODE")) return false;

				if (CargoNet.getNetworkFromLocation(e.getClickedBlock().getLocation()) != null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Connected: " + "&2\u2714"));
				}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Connected: " + "&4\u2718"));
				}
				return true;
			}
		});

		new CargoInputNode(Categories.CARGO, SlimefunItems.CARGO_INPUT, "CARGO_NODE_INPUT", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.HOPPER), null, SlimefunItems.BILLON_INGOT, SlimefunItems.CARGO_NODE, SlimefunItems.BILLON_INGOT, null, new ItemStack(Material.HOPPER), null}, new CustomItem(SlimefunItems.CARGO_INPUT, 2))
		.register(true);

		new CargoOutputNode(Categories.CARGO, SlimefunItems.CARGO_OUTPUT, "CARGO_NODE_OUTPUT", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.HOPPER), null, SlimefunItems.BRASS_INGOT, SlimefunItems.CARGO_NODE, SlimefunItems.BRASS_INGOT, null, new ItemStack(Material.HOPPER), null}, new CustomItem(SlimefunItems.CARGO_OUTPUT, 2))
		.register(true);

		new AdvancedCargoOutputNode(Categories.CARGO, SlimefunItems.CARGO_OUTPUT_ADVANCED, "CARGO_NODE_OUTPUT_ADVANCED", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.CARGO_MOTOR, null, SlimefunItems.COBALT_INGOT, SlimefunItems.CARGO_OUTPUT, SlimefunItems.COBALT_INGOT, null, SlimefunItems.CARGO_MOTOR, null}, new CustomItem(SlimefunItems.CARGO_OUTPUT_ADVANCED))
		.register(true);

		new AutomatedCraftingChamber(Categories.ELECTRICITY, SlimefunItems.AUTOMATED_CRAFTING_CHAMBER, "AUTOMATED_CRAFTING_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.CRAFTING_TABLE), null, SlimefunItems.CARGO_MOTOR, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.CARGO_MOTOR, null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public int getEnergyConsumption() {
				return 10;
			}
			
		}.registerChargeableBlock(true, 256);

		new ReactorAccessPort(Categories.ELECTRICITY, SlimefunItems.REACTOR_ACCESS_PORT, "REACTOR_ACCESS_PORT", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.LEAD_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.LEAD_INGOT, null, SlimefunItems.ELECTRIC_MOTOR, null})
		.register(true);

		new FluidPump(Categories.ELECTRICITY, SlimefunItems.FLUID_PUMP, "FLUID_PUMP", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.MEDIUM_CAPACITOR, null, new ItemStack(Material.BUCKET), SlimefunItems.CARGO_MOTOR, new ItemStack(Material.BUCKET), null, SlimefunItems.OIL_PUMP, null})
		.registerChargeableBlock(true, 512);


		new TrashCan(Categories.CARGO, SlimefunItems.TRASH_CAN, "TRASH_CAN_BLOCK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.PORTABLE_DUSTBIN, null, SlimefunItems.LEAD_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.ALUMINUM_INGOT})
		.register(true);

		new CarbonPress(Categories.ELECTRICITY, SlimefunItems.CARBON_PRESS, "CARBON_PRESS", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new CarbonPress(Categories.ELECTRICITY, SlimefunItems.CARBON_PRESS_2, "CARBON_PRESS_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new CarbonPress(Categories.ELECTRICITY, SlimefunItems.CARBON_PRESS_3, "CARBON_PRESS_3", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricSmeltery(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_SMELTERY, "ELECTRIC_SMELTERY", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new ElectricSmeltery(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_SMELTERY_2, "ELECTRIC_SMELTERY_2", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new WitherAssembler(Categories.ELECTRICITY, SlimefunItems.WITHER_ASSEMBLER, "WITHER_ASSEMBLER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3, new ItemStack(Material.NETHER_STAR), SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.CARBONADO_EDGED_CAPACITOR})
		.registerChargeableBlock(true, 4096);

	}
	
	public static void registerPostHandler(PostSlimefunLoadingHandler handler) {
		SlimefunPlugin.getUtilities().postHandlers.add(handler);
	}

}
