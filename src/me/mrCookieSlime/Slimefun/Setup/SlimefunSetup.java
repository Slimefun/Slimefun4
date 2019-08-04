package me.mrCookieSlime.Slimefun.Setup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.compatibility.MaterialHelper;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.CSCoreLibPlugin.general.Block.TreeCalculator;
import me.mrCookieSlime.CSCoreLibPlugin.general.Block.Vein;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.SkullItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.FireworkShow;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.CSCoreLibPlugin.general.Recipe.RecipeCalculator;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Variables;
import me.mrCookieSlime.Slimefun.Android.AndroidType;
import me.mrCookieSlime.Slimefun.Android.ProgrammableAndroid;
import me.mrCookieSlime.Slimefun.GPS.Elevator;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.GPS.NetworkStatus;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Misc.PostSlimefunLoadingHandler;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Alloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.EnhancedFurnace;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ExcludedSoulboundTool;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ExcludedTool;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.HandledBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.JetBoots;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Jetpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Juice;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.MultiTool;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ReplacingAlloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ReplacingItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunBackpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunBow;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SolarHelmet;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SoulboundBackpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SoulboundItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Talisman;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.VanillaItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AReactor;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.Teleporter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.AutonomousMachineHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.MultiBlockInteractionHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AdvancedCargoOutputNode;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AnimalGrowthAccelerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutoAnvil;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutoBreeder;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutoDisenchanter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutoEnchanter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutomatedCraftingChamber;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.CarbonPress;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.CargoInputNode;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.CargoOutputNode;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.ChargingBench;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.CropGrowthAccelerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.ElectricDustWasher;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.ElectricFurnace;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.ElectricGoldPan;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.ElectricIngotFactory;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.ElectricSmeltery;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.ElectrifiedCrucible;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.FluidPump;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.FoodComposter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.FoodFabricator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.Freezer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.HeatedPressureChamber;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.NetherDrill;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.OilPump;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.ReactorAccessPort;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.Refinery;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.TrashCan;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.WitherAssembler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.XPCollector;
import me.mrCookieSlime.Slimefun.Objects.tasks.RainbowTicker;
import me.mrCookieSlime.Slimefun.api.Backpacks;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;
import me.mrCookieSlime.Slimefun.api.item_transport.CargoNet;
import me.mrCookieSlime.Slimefun.holograms.CargoHologram;
import me.mrCookieSlime.Slimefun.holograms.EnergyHologram;
import me.mrCookieSlime.Slimefun.holograms.InfusedHopper;
import me.mrCookieSlime.Slimefun.holograms.Projector;
import me.mrCookieSlime.Slimefun.holograms.ReactorHologram;
import me.mrCookieSlime.Slimefun.listeners.AncientAltarListener;

public class SlimefunSetup {

	public static boolean legacy_ore_washer = false;

	public static void setupItems() throws Exception {
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

		new SlimefunItem(Categories.MACHINES_1, SlimefunItems.OUTPUT_CHEST, "OUTPUT_CHEST", RecipeType.ENHANCED_CRAFTING_TABLE,
				new ItemStack[] {SlimefunItems.LEAD_INGOT, new ItemStack(Material.HOPPER), SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, new ItemStack(Material.CHEST), SlimefunItems.LEAD_INGOT, null, SlimefunItems.LEAD_INGOT, null})
				.register(true);
		
		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.ENHANCED_CRAFTING_TABLE, "ENHANCED_CRAFTING_TABLE",
		new ItemStack[] {null, null, null, null, new ItemStack(Material.CRAFTING_TABLE), null, null, new ItemStack(Material.DISPENSER), null},
		new ItemStack[0], Material.CRAFTING_TABLE)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(Player p, MultiBlock mb, Block b) {

				SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("ENHANCED_CRAFTING_TABLE");

				if (mb.isMultiBlock(machine)) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, machine.getItem(), true)) {
							// Objects dispBlock and disp have been split up, in order to add the output chest functionallity, which is the only functionallity
							// that is dependant on the dispenser's block methods.
							// the Dispenser disp still remains the same though, and as such doesn't break any existing code which involves said object.
							Block dispBlock = b.getRelative(BlockFace.DOWN);
							Dispenser disp = (Dispenser) dispBlock.getState();
							Inventory inv = disp.getInventory();
							
							List<ItemStack[]> inputs = RecipeType.getRecipeInputList(machine);

							for (int i = 0; i < inputs.size(); i++) {
								boolean craft = true;
								for (int j = 0; j < inv.getContents().length; j++) {
									if (!SlimefunManager.isItemSimiliar(inv.getContents()[j], inputs.get(i)[j], true)) {
										if (SlimefunItem.getByItem(inputs.get(i)[j]) instanceof SlimefunBackpack) {
											if (!SlimefunManager.isItemSimiliar(inv.getContents()[j], inputs.get(i)[j], false)) {
												craft = false;
												break;
											}
										}
										else {
											craft = false;
											break;
										}
									}
								}
								
								if (craft) {
									final ItemStack adding = RecipeType.getRecipeOutputList(machine, inputs.get(i)).clone();
									if (Slimefun.hasUnlocked(p, adding, true)) {
										Inventory inv2 = Bukkit.createInventory(null, 9, "test");
										for (int j = 0; j < inv.getContents().length; j++) {
											inv2.setItem(j, inv.getContents()[j] != null ? (inv.getContents()[j].getAmount() > 1 ? new CustomItem(inv.getContents()[j], inv.getContents()[j].getAmount() - 1): null): null);
										}
										
										Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv, inv2);
										
										if (outputInv != null) {	
											SlimefunItem sfItem = SlimefunItem.getByItem(adding);
											if (sfItem instanceof SlimefunBackpack) {
												ItemStack backpack = null;
												
												for (int j = 0; j < 9; j++) {
													if (inv.getContents()[j] != null) {
														if (inv.getContents()[j].getType() != Material.AIR) {
															if (SlimefunItem.getByItem(inv.getContents()[j]) instanceof SlimefunBackpack) {
																backpack = inv.getContents()[j];
																break;
															}
														}
													}
												}
												String id = "";
												int size = ((SlimefunBackpack) sfItem).size;
												
												if (backpack != null) {
													for (String line: backpack.getItemMeta().getLore()) {
														if (line.startsWith(ChatColor.translateAlternateColorCodes('&', "&7ID: ")) && line.contains("#")) {
															id = line.replace(ChatColor.translateAlternateColorCodes('&', "&7ID: "), "");
															Config cfg = new Config(new File("data-storage/Slimefun/Players/" + id.split("#")[0] + ".yml"));
															cfg.setValue("backpacks." + id.split("#")[1] + ".size", size);
															cfg.save();
															break;
														}
													}
												}

												if (id.equals("")) {
													for (int line = 0; line < adding.getItemMeta().getLore().size(); line++) {
														if (adding.getItemMeta().getLore().get(line).equals(ChatColor.translateAlternateColorCodes('&', "&7ID: <ID>"))) {
															ItemMeta im = adding.getItemMeta();
															List<String> lore = im.getLore();
															lore.set(line, lore.get(line).replace("<ID>", Backpacks.createBackpack(p, size)));
															im.setLore(lore);
															adding.setItemMeta(im);
															break;
														}
													}
												}
												else {
													for (int line = 0; line < adding.getItemMeta().getLore().size(); line++) {
														if (adding.getItemMeta().getLore().get(line).equals(ChatColor.translateAlternateColorCodes('&', "&7ID: <ID>"))) {
															ItemMeta im = adding.getItemMeta();
															List<String> lore = im.getLore();
															lore.set(line, lore.get(line).replace("<ID>", id));
															im.setLore(lore);
															adding.setItemMeta(im);
															break;
														}
													}
												}
											}
											

											for (int j = 0; j < 9; j++) {
												if (inv.getContents()[j] != null) {
													if (inv.getContents()[j].getType() != Material.AIR) {
														if (inv.getContents()[j].getType().toString().endsWith("_BUCKET")) inv.setItem(j, new ItemStack(Material.BUCKET));
														else if (inv.getContents()[j].getAmount() > 1) inv.setItem(j, new CustomItem(inv.getContents()[j], inv.getContents()[j].getAmount() - 1));
														else inv.setItem(j, null);
													}
												}
											}
											p.getWorld().playSound(b.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
											
											outputInv.addItem(adding);
											
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
									}
									return true;
								}
							}
							Messages.local.sendTranslation(p, "machines.pattern-not-found", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

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

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.GRIND_STONE, "GRIND_STONE",
		new ItemStack[] {null, null, null, null, new ItemStack(Material.OAK_FENCE), null, null, new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), null},
		new ItemStack[] {new ItemStack(Material.BLAZE_ROD), new ItemStack(Material.BLAZE_POWDER, 4), new ItemStack(Material.BONE), new ItemStack(Material.BONE_MEAL, 4), new ItemStack(Material.GRAVEL), new ItemStack(Material.FLINT), new ItemStack(Material.NETHER_WART), new CustomItem(SlimefunItems.MAGIC_LUMP_1, 2), new ItemStack(Material.ENDER_EYE), new CustomItem(SlimefunItems.ENDER_LUMP_1, 2), new ItemStack(Material.COBBLESTONE), new ItemStack(Material.GRAVEL), new ItemStack(Material.WHEAT), SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.DIRT), SlimefunItems.STONE_CHUNK, new ItemStack(Material.SANDSTONE), new ItemStack(Material.SAND, 4), new ItemStack(Material.RED_SANDSTONE), new ItemStack(Material.RED_SAND, 4)},
		Material.OAK_FENCE)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(Player p, MultiBlock mb, Block b) {

				SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("GRIND_STONE");

				if (mb.isMultiBlock(machine)) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, machine.getItem(), true)) {
							Block dispBlock = b.getRelative(BlockFace.DOWN);
							Dispenser disp = (Dispenser) dispBlock.getState();
							Inventory inv = disp.getInventory();
							for (ItemStack current: inv.getContents()) {
								for (ItemStack convert: RecipeType.getRecipeInputs(machine)) {
									if (convert != null && SlimefunManager.isItemSimiliar(current, convert, true)) {
										ItemStack output = RecipeType.getRecipeOutput(machine, convert);
										Inventory outputInv = SlimefunMachine.findValidOutputInv(output, dispBlock, inv);
										if (outputInv != null) {
											ItemStack removing = current.clone();
											removing.setAmount(1);
											inv.removeItem(removing);
											outputInv.addItem(output);
											p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
										return true;
									}
								}
							}
							Messages.local.sendTranslation(p, "machines.unknown-material", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.ARMOR_FORGE, "ARMOR_FORGE",
		new ItemStack[] {null, null, null, null, new ItemStack(Material.ANVIL), null, null, new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), null},
		new ItemStack[] {},
		Material.ANVIL)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(final Player p, MultiBlock mb, Block b) {

				SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("ARMOR_FORGE");

				if (mb.isMultiBlock(machine)) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, machine.getItem(), true)) {
							Block dispBlock = b.getRelative(BlockFace.DOWN);
							Dispenser disp = (Dispenser) dispBlock.getState();
							Inventory inv = disp.getInventory();
							List<ItemStack[]> inputs = RecipeType.getRecipeInputList(machine);

							for (int i = 0; i < inputs.size(); i++) {
								boolean craft = true;
								for (int j = 0; j < inv.getContents().length; j++) {
									if (!SlimefunManager.isItemSimiliar(inv.getContents()[j], inputs.get(i)[j], true)) {
										craft = false;
										break;
									}
								}

								if (craft) {
									final ItemStack adding = RecipeType.getRecipeOutputList(machine, inputs.get(i)).clone();
									if (Slimefun.hasUnlocked(p, adding, true)) {
										Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
										if (outputInv != null) {
											for (ItemStack removing: inputs.get(i)) {
												if (removing != null) inv.removeItem(removing);
											}
											for (int j = 0; j < 4; j++) {
												int current = j;
												Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
													if (current < 3) {
														p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
													} else {
														p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
														outputInv.addItem(adding);
													}
												}, j*20L);
											}
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
									}
									return true;
								}
							}
							Messages.local.sendTranslation(p, "machines.pattern-not-found", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.ORE_CRUSHER, "ORE_CRUSHER",
		new ItemStack[] {null, null, null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.IRON_BARS), new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), new ItemStack(Material.IRON_BARS)},
		new ItemStack[] {new ItemStack(Material.IRON_ORE), new CustomItem(SlimefunItems.IRON_DUST, (Boolean) Slimefun.getItemValue("ORE_CRUSHER", "double-ores") ? 2: 1), new ItemStack(Material.GOLD_ORE), new CustomItem(SlimefunItems.GOLD_DUST, (Boolean) Slimefun.getItemValue("ORE_CRUSHER", "double-ores") ? 2: 1), new ItemStack(Material.NETHERRACK, 16), SlimefunItems.SULFATE, SlimefunItems.SIFTED_ORE, SlimefunItems.CRUSHED_ORE, SlimefunItems.CRUSHED_ORE, SlimefunItems.PULVERIZED_ORE, SlimefunItems.PURE_ORE_CLUSTER, SlimefunItems.TINY_URANIUM, new ItemStack(Material.COBBLESTONE, 8), new ItemStack(Material.SAND, 1), new ItemStack(Material.GOLD_INGOT), SlimefunItems.GOLD_DUST, SlimefunItems.GOLD_4K, SlimefunItems.GOLD_DUST},
		Material.NETHER_BRICK_FENCE)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(Player p, MultiBlock mb, Block b) {

				SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("ORE_CRUSHER");

				if (mb.isMultiBlock(machine)) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, machine.getItem(), true)) {
							Block dispBlock = b.getRelative(BlockFace.DOWN);
							Dispenser disp = (Dispenser) dispBlock.getState();
							Inventory inv = disp.getInventory();
							for (ItemStack current: inv.getContents()) {
								for (ItemStack convert: RecipeType.getRecipeInputs(machine)) {
									if (convert != null && SlimefunManager.isItemSimiliar(current, convert, true)) {
										ItemStack adding = RecipeType.getRecipeOutput(machine, convert);
										Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
										if (outputInv != null) {
											ItemStack removing = current.clone();
											removing.setAmount(convert.getAmount());
											inv.removeItem(removing);
											outputInv.addItem(adding);
											p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 1);
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
										return true;
									}
								}
							}
							Messages.local.sendTranslation(p, "machines.unknown-material", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.COMPRESSOR, "COMPRESSOR",
		new ItemStack[] {null, null, null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.PISTON), new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), new ItemStack(Material.PISTON)},
		new ItemStack[] {new ItemStack(Material.COAL, 8), SlimefunItems.CARBON, new CustomItem(SlimefunItems.STEEL_INGOT, 8), SlimefunItems.STEEL_PLATE, new CustomItem(SlimefunItems.CARBON, 4), SlimefunItems.COMPRESSED_CARBON, new CustomItem(SlimefunItems.STONE_CHUNK, 4), new ItemStack(Material.COBBLESTONE), new CustomItem(SlimefunItems.REINFORCED_ALLOY_INGOT, 8), SlimefunItems.REINFORCED_PLATE},
		Material.NETHER_BRICK_FENCE)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(final Player p, MultiBlock mb, Block b) {

				SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("COMPRESSOR");

				if (mb.isMultiBlock(machine)) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, machine.getItem(), true)) {
							Block dispBlock = b.getRelative(BlockFace.DOWN);
							Dispenser disp = (Dispenser) dispBlock.getState();
							Inventory inv = disp.getInventory();
							for (ItemStack current: inv.getContents()) {
								for (ItemStack convert: RecipeType.getRecipeInputs(machine)) {
									if (convert != null && SlimefunManager.isItemSimiliar(current, convert, true)) {
										final ItemStack adding = RecipeType.getRecipeOutput(machine, convert);
										Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
										if (outputInv != null) {
											ItemStack removing = current.clone();
											removing.setAmount(convert.getAmount());
											inv.removeItem(removing);
											for (int i = 0; i < 4; i++) {
												int j = i;
												Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
													if (j < 3) {
														p.getWorld().playSound(p.getLocation(), j == 1 ? Sound.BLOCK_PISTON_CONTRACT : Sound.BLOCK_PISTON_EXTEND, 1F, j == 0 ? 1F : 2F);
													} else {
														p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
														outputInv.addItem(adding);
													}
												}, i*20L);
											}
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
										return true;
									}
								}
							}
							Messages.local.sendTranslation(p, "machines.unknown-material", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.MAGIC_LUMP_1, "MAGIC_LUMP_1", RecipeType.GRIND_STONE,
		new ItemStack[] {null, null, null, null, new ItemStack(Material.NETHER_WART), null, null, null, null}, new CustomItem(SlimefunItems.MAGIC_LUMP_1, 2))
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

		new SlimefunItem(Categories.PORTABLE, SlimefunItems.ENDER_BACKPACK, "ENDER_BACKPACK", RecipeType.ENHANCED_CRAFTING_TABLE,
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
					if (p.getInventory().getHelmet() != null && p.getInventory().getChestplate() != null && p.getInventory().getLeggings() != null && p.getInventory().getBoots() != null) {
						if (SlimefunManager.isItemSimiliar(p.getInventory().getHelmet(), SlimefunItems.ENDER_HELMET, true) && SlimefunManager.isItemSimiliar(p.getInventory().getChestplate(), SlimefunItems.ENDER_CHESTPLATE, true) && SlimefunManager.isItemSimiliar(p.getInventory().getLeggings(), SlimefunItems.ENDER_LEGGINGS, true) && SlimefunManager.isItemSimiliar(p.getInventory().getBoots(), SlimefunItems.ENDER_BOOTS, true)) {
							p.launchProjectile(EnderPearl.class);
							p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.FOOD, SlimefunItems.MAGIC_SUGAR, "MAGIC_SUGAR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.SUGAR), new ItemStack(Material.REDSTONE), new ItemStack(Material.GLOWSTONE_DUST), null, null, null, null, null, null}, new String[] {"effects.SPEED"}, new Integer[] {4})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.MAGIC_SUGAR, true)) {
					PlayerInventory.consumeItemInHand(p);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, (Integer) Slimefun.getItemValue("MAGIC_SUGAR", "effects.SPEED")));
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.FOOD, SlimefunItems.MONSTER_JERKY, "MONSTER_JERKY", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new SlimefunItem(Categories.WEAPONS, SlimefunItems.SWORD_OF_BEHEADING, "SWORD_OF_BEHEADING", RecipeType.ENHANCED_CRAFTING_TABLE,
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

		new SlimefunGadget(Categories.TOOLS, SlimefunItems.GOLD_PAN, "GOLD_PAN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, new ItemStack(Material.STONE), new ItemStack(Material.BOWL), new ItemStack(Material.STONE), new ItemStack(Material.STONE), new ItemStack(Material.STONE), new ItemStack(Material.STONE)},
		new ItemStack[] {new ItemStack(Material.GRAVEL), new ItemStack(Material.FLINT), new ItemStack(Material.GRAVEL), new ItemStack(Material.CLAY_BALL), new ItemStack(Material.GRAVEL), SlimefunItems.SIFTED_ORE},
		new String[] {"chance.FLINT", "chance.CLAY", "chance.SIFTED_ORE"}, new Integer[] {47, 28, 15})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.GOLD_PAN, true)) {
					if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.GRAVEL) {
						if (CSCoreLib.getLib().getProtectionManager().canBuild(p.getUniqueId(), e.getClickedBlock(), true)) {
							List<ItemStack> drops = new ArrayList<ItemStack>();
							if (SlimefunStartup.chance(100, (Integer) Slimefun.getItemValue("GOLD_PAN", "chance.SIFTED_ORE"))) drops.add(SlimefunItems.SIFTED_ORE);
								else if (SlimefunStartup.chance(100, (Integer) Slimefun.getItemValue("GOLD_PAN", "chance.CLAY"))) drops.add(new ItemStack(Material.CLAY_BALL));
								else if (SlimefunStartup.chance(100, (Integer) Slimefun.getItemValue("GOLD_PAN", "chance.FLINT"))) drops.add(new ItemStack(Material.FLINT));


							
								e.getClickedBlock().getWorld().playEffect(e.getClickedBlock().getLocation(), Effect.STEP_SOUND, e.getClickedBlock().getType());
								e.getClickedBlock().setType(Material.AIR);
								for (ItemStack drop: drops) {
									e.getClickedBlock().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), drop);
							}
						}
					}
					e.setCancelled(true);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MISC, SlimefunItems.SIFTED_ORE, "SIFTED_ORE", RecipeType.GOLD_PAN,
		new ItemStack[] {new ItemStack(Material.GRAVEL), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.SMELTERY, "SMELTERY",
		new ItemStack[] {null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.NETHER_BRICKS), new CustomItem(Material.DISPENSER, "Dispencer (Faced up)"), new ItemStack(Material.NETHER_BRICKS), null, new ItemStack(Material.FLINT_AND_STEEL), null},
		new ItemStack[] {SlimefunItems.IRON_DUST, new ItemStack(Material.IRON_INGOT)}, Material.NETHER_BRICK_FENCE,
		new String[] {"chance.fireBreak"}, new Integer[] {34})
		.register(true, new MultiBlockInteractionHandler() {

					@Override
					public boolean onInteract(Player p, MultiBlock mb, Block b) {

						SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("SMELTERY");

						if (mb.isMultiBlock(machine)) {
							if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
								if (Slimefun.hasUnlocked(p, machine.getItem(), true)) {
									Block dispBlock = b.getRelative(BlockFace.DOWN);
									Dispenser disp = (Dispenser) dispBlock.getState();
									Inventory inv = disp.getInventory();
									List<ItemStack[]> inputs = RecipeType.getRecipeInputList(machine);

									for (int i = 0; i < inputs.size(); i++) {
										boolean craft = true;
										for (ItemStack converting: inputs.get(i)) {
											if (converting != null) {
												for (int j = 0; j < inv.getContents().length; j++) {
													if (j == (inv.getContents().length - 1) && !SlimefunManager.isItemSimiliar(converting, inv.getContents()[j], true)) {
														craft = false;
														break;
													}
													else if (SlimefunManager.isItemSimiliar(inv.getContents()[j], converting, true)) break;
												}
											}
										}

										if (craft) {
											ItemStack adding = RecipeType.getRecipeOutputList(machine, inputs.get(i)).clone();
											if (Slimefun.hasUnlocked(p, adding, true)) {
												Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
												if (outputInv != null) {
													for (ItemStack removing: inputs.get(i)) {
														if (removing != null) inv.removeItem(removing);
													}
													outputInv.addItem(adding);
													p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
													p.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
													// Block raw_disp = b.getRelative(BlockFace.DOWN);					
													// raw_disp has been removed since the outputInv functionality already uses such an object which is declared above as dispBlock. 
													// The chamber methods have been updated to reflect this change.
													// Maybe this code snippet should be turned into a loop?
													Hopper chamber = null;
													if (BlockStorage.check(dispBlock.getRelative(BlockFace.EAST).getState().getBlock(), "IGNITION_CHAMBER")) {
														chamber = (Hopper) dispBlock.getRelative(BlockFace.EAST).getState();
													} else if (BlockStorage.check(dispBlock.getRelative(BlockFace.WEST).getState().getBlock(), "IGNITION_CHAMBER")) {
														chamber = (Hopper) dispBlock.getRelative(BlockFace.WEST).getState();
													} else if (BlockStorage.check(dispBlock.getRelative(BlockFace.NORTH).getState().getBlock(), "IGNITION_CHAMBER")) {
														chamber = (Hopper) dispBlock.getRelative(BlockFace.NORTH).getState();
													} else if (BlockStorage.check(dispBlock.getRelative(BlockFace.SOUTH).getState().getBlock(), "IGNITION_CHAMBER")){
														chamber = (Hopper) dispBlock.getRelative(BlockFace.SOUTH).getState();
													}
													
													if (SlimefunStartup.chance(100, (Integer) Slimefun.getItemValue("SMELTERY", "chance.fireBreak"))) {
														if (chamber != null) {
															if (chamber.getInventory().contains(Material.FLINT_AND_STEEL)) {
																ItemStack item = chamber.getInventory().getItem(chamber.getInventory().first(Material.FLINT_AND_STEEL));
																ItemMeta meta = item.getItemMeta();
																((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
																item.setItemMeta(meta);
																if (((Damageable) item.getItemMeta()).getDamage() >= item.getType().getMaxDurability()) {
																	item.setAmount(0); 
																	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
																}
																p.getWorld().playSound(p.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1, 1);
															} 
															else {
																Messages.local.sendTranslation(p, "machines.ignition-chamber-no-flint", true);
																
																Block fire = b.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
																fire.getWorld().playEffect(fire.getLocation(), Effect.STEP_SOUND, fire.getType());
																fire.setType(Material.AIR);
															}
														} 
														else {
															Block fire = b.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
															fire.getWorld().playEffect(fire.getLocation(), Effect.STEP_SOUND, fire.getType());
															fire.setType(Material.AIR);
														}
													}
												}
												else Messages.local.sendTranslation(p, "machines.full-inventory", true);
											}
											return true;
										}
									}
									Messages.local.sendTranslation(p, "machines.pattern-not-found", true);
								}
							}
							return true;
						}
						else return false;
					}
				});
		
		
		new SlimefunItem(Categories.MACHINES_1, SlimefunItems.IGNITION_CHAMBER, "IGNITION_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.STEEL_PLATE, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.STEEL_PLATE, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_PLATE, SlimefunItems.ELECTRIC_MOTOR, null, new ItemStack(Material.HOPPER), null})
		.register(true);
		
		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.PRESSURE_CHAMBER, "PRESSURE_CHAMBER",
		new ItemStack[] {new ItemStack(Material.STONE_SLAB), new CustomItem(Material.DISPENSER, "Dispenser (Facing down)"), new ItemStack(Material.STONE_SLAB), new ItemStack(Material.PISTON), new ItemStack(Material.GLASS), new ItemStack(Material.PISTON), new ItemStack(Material.PISTON), new ItemStack(Material.CAULDRON), new ItemStack(Material.PISTON)},
		new ItemStack[] {SlimefunItems.CARBON_CHUNK, SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.RAW_CARBONADO, SlimefunItems.CARBONADO},
		Material.CAULDRON)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(final Player p, MultiBlock mb, final Block b) {

				SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("PRESSURE_CHAMBER");

				if (mb.isMultiBlock(machine)) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, machine.getItem(), true)) {
							Block dispBlock = b.getRelative(BlockFace.UP).getRelative(BlockFace.UP);
							Dispenser disp = (Dispenser) dispBlock.getState();
							final Inventory inv = disp.getInventory();
							for (ItemStack current: inv.getContents()) {
								for (ItemStack convert: RecipeType.getRecipeInputs(machine)) {
									if (convert != null && SlimefunManager.isItemSimiliar(current, convert, true)) {
										final ItemStack adding = RecipeType.getRecipeOutput(machine, convert);
										Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
										if (outputInv != null) {
											ItemStack removing = current.clone();
											removing.setAmount(convert.getAmount());
											inv.removeItem(removing);
											for (int i = 0; i < 4; i++) {
												int j = i;
												Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
													p.getWorld().playSound(b.getLocation(), Sound.ENTITY_TNT_PRIMED, 1, 1);
													p.getWorld().playEffect(b.getRelative(BlockFace.UP).getLocation(), Effect.SMOKE, 4);
													p.getWorld().playEffect(b.getRelative(BlockFace.UP).getLocation(), Effect.SMOKE, 4);
													p.getWorld().playEffect(b.getRelative(BlockFace.UP).getLocation(), Effect.SMOKE, 4);
													if (j < 3) {
														p.getWorld().playSound(b.getLocation(), Sound.ENTITY_TNT_PRIMED, 1F, 1F);
													} else {
														p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
														outputInv.addItem(adding);
													}
												}, i*20L);
											}
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
										return true;
									}
								}
							}
							Messages.local.sendTranslation(p, "machines.unknown-material", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.TECH_MISC, SlimefunItems.BATTERY, "BATTERY", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] { null, new ItemStack(Material.REDSTONE), null, SlimefunItems.ZINC_INGOT, SlimefunItems.SULFATE, SlimefunItems.COPPER_INGOT, SlimefunItems.ZINC_INGOT, SlimefunItems.SULFATE, SlimefunItems.COPPER_INGOT })
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
		new ItemStack[] {new ItemStack(Material.IRON_ORE), null, null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.IRON_DUST, (Boolean) Slimefun.getItemValue("ORE_CRUSHER", "double-ores") ? 2: 1))
		.register(true);

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.GOLD_DUST, "GOLD_DUST", RecipeType.ORE_CRUSHER,
		new ItemStack[] {new ItemStack(Material.GOLD_ORE), null, null, null, null, null, null, null, null}, new CustomItem(SlimefunItems.GOLD_DUST, (Boolean) Slimefun.getItemValue("ORE_CRUSHER", "double-ores") ? 2: 1))
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
		new ItemStack[] {null, null, null, null, new ItemStack(Material.WHEAT), null, null, null, null})
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

		new SlimefunItem(Categories.MISC, SlimefunItems.CHAIN, "CHAIN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, null}, new CustomItem(SlimefunItems.CHAIN, 8))
		.register(true);

		new SlimefunItem(Categories.MISC, SlimefunItems.HOOK, "HOOK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, null, null, null})
		.register(true);

		new SlimefunItem(Categories.TOOLS, SlimefunItems.GRAPPLING_HOOK, "GRAPPLING_HOOK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.HOOK, SlimefunItems.HOOK, null, SlimefunItems.CHAIN, SlimefunItems.HOOK, SlimefunItems.CHAIN, null, null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.GRAPPLING_HOOK, true)) {
					if (e.getClickedBlock() == null && !Variables.jump_state.containsKey(p.getUniqueId())) {
						e.setCancelled(true);
						if (p.getInventory().getItemInOffHand().getType().equals(Material.BOW)) {
							// Cancel, to fix dupe #740
							return false;
						}
						Variables.jump_state.put(p.getUniqueId(), p.getInventory().getItemInMainHand().getType() != Material.SHEARS);
						if (p.getInventory().getItemInMainHand().getType() == Material.LEAD) PlayerInventory.consumeItemInHand(p);

						Vector direction = p.getEyeLocation().getDirection().multiply(2.0);
				    	Projectile projectile = p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
				    	projectile.setShooter(p);
				    	projectile.setVelocity(direction);
				    	Arrow arrow = (Arrow) projectile;
				    	Bat b = (Bat) p.getWorld().spawnEntity(p.getLocation(), EntityType.BAT);
				    	b.setCanPickupItems(false);
				    	b.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 100000));
				    	b.setLeashHolder(arrow);

				    	Variables.damage.add(p.getUniqueId());
						Variables.remove.put(p.getUniqueId(), new Entity[] {b, arrow});
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.MAGIC_WORKBENCH, "MAGIC_WORKBENCH",
		new ItemStack[] {null, null, null, null, null, null, new ItemStack(Material.BOOKSHELF), new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.DISPENSER)},
		new ItemStack[0], Material.CRAFTING_TABLE)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(final Player p, MultiBlock mb, final Block b) {

				SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("MAGIC_WORKBENCH");

				if (mb.isMultiBlock(machine)) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, machine.getItem(), true)) {
							Block dispBlock = null;
							// Maybe this could be implemented by instead looping over a BlockFace<> array?
							if (b.getRelative(1, 0, 0).getType() == Material.DISPENSER) dispBlock = b.getRelative(1, 0, 0);
							else if (b.getRelative(0, 0, 1).getType() == Material.DISPENSER) dispBlock = b.getRelative(0, 0, 1);
							else if (b.getRelative(-1, 0, 0).getType() == Material.DISPENSER) dispBlock = b.getRelative(-1, 0, 0);
							else if (b.getRelative(0, 0, -1).getType() == Material.DISPENSER) dispBlock = b.getRelative(0, 0, -1);
							
							Dispenser disp = (Dispenser) dispBlock.getState();
							final Inventory inv = disp.getInventory();
							List<ItemStack[]> inputs = RecipeType.getRecipeInputList(machine);

							for (int i = 0; i < inputs.size(); i++) {
								boolean craft = true;
								for (int j = 0; j < inv.getContents().length; j++) {
									if (!SlimefunManager.isItemSimiliar(inv.getContents()[j], inputs.get(i)[j], true)) {
										if (SlimefunItem.getByItem(inputs.get(i)[j]) instanceof SlimefunBackpack) {
											if (!SlimefunManager.isItemSimiliar(inv.getContents()[j], inputs.get(i)[j], false)) {
												craft = false;
												break;
											}
										}
										else {
											craft = false;
											break;
										}
									}
								}

								if (craft) {
									final ItemStack adding = RecipeType.getRecipeOutputList(machine, inputs.get(i)).clone();
									if (Slimefun.hasUnlocked(p, adding, true)) {
										Inventory inv2 = Bukkit.createInventory(null, 9, "test");
										for (int j = 0; j < inv.getContents().length; j++) {
											inv2.setItem(j, inv.getContents()[j] != null ? (inv.getContents()[j].getAmount() > 1 ? new CustomItem(inv.getContents()[j], inv.getContents()[j].getAmount() - 1): null): null);
										}
										Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv, inv2);
										if (outputInv != null) {
											SlimefunItem sfItem = SlimefunItem.getByItem(adding);

											if (sfItem instanceof SlimefunBackpack) {
												ItemStack backpack = null;

												for (int j = 0; j < 9; j++) {
													if (inv.getContents()[j] != null) {
														if (inv.getContents()[j].getType() != Material.AIR) {
															if (SlimefunItem.getByItem(inv.getContents()[j]) instanceof SlimefunBackpack) {
																backpack = inv.getContents()[j];
																break;
															}
														}
													}
												}
												String id = "";
												int size = ((SlimefunBackpack) sfItem).size;

												if (backpack != null) {
													for (String line: backpack.getItemMeta().getLore()) {
														if (line.startsWith(ChatColor.translateAlternateColorCodes('&', "&7ID: ")) && line.contains("#")) {
															id = line.replace(ChatColor.translateAlternateColorCodes('&', "&7ID: "), "");
															Config cfg = new Config(new File("data-storage/Slimefun/Players/" + id.split("#")[0] + ".yml"));
															cfg.setValue("backpacks." + id.split("#")[1] + ".size", size);
															cfg.save();
															break;
														}
													}
												}

												if (id.equals("")) {
													for (int line = 0; line < adding.getItemMeta().getLore().size(); line++) {
														if (adding.getItemMeta().getLore().get(line).equals(ChatColor.translateAlternateColorCodes('&', "&7ID: <ID>"))) {
															ItemMeta im = adding.getItemMeta();
															List<String> lore = im.getLore();
															lore.set(line, lore.get(line).replace("<ID>", Backpacks.createBackpack(p, size)));
															im.setLore(lore);
															adding.setItemMeta(im);
															break;
														}
													}
												}
												else {
													for (int line = 0; line < adding.getItemMeta().getLore().size(); line++) {
														if (adding.getItemMeta().getLore().get(line).equals(ChatColor.translateAlternateColorCodes('&', "&7ID: <ID>"))) {
															ItemMeta im = adding.getItemMeta();
															List<String> lore = im.getLore();
															lore.set(line, lore.get(line).replace("<ID>", id));
															im.setLore(lore);
															adding.setItemMeta(im);
															break;
														}
													}
												}
											}
											
											for (int j = 0; j < 9; j++) {
												if (inv.getContents()[j] != null) {
													if (inv.getContents()[j].getType() != Material.AIR) {
														if (inv.getContents()[j].getAmount() > 1) inv.setItem(j, new CustomItem(inv.getContents()[j], inv.getContents()[j].getAmount() - 1));
														else inv.setItem(j, null);
													}
												}
											}
											for (int j = 0; j < 4; j++) {
												int current = j;
												Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
													p.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
													p.getWorld().playEffect(b.getLocation(), Effect.ENDER_SIGNAL, 1);
													if (current < 3) {
														p.getWorld().playSound(b.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1F, 1F);
													} else {
														p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
														outputInv.addItem(adding);
													}
												}, j*20L);
											}
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
									}
									return true;
								}
							}
							Messages.local.sendTranslation(p, "machines.pattern-not-found", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

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
						p.setFallDistance(0.0f);
					}
					else {
						Messages.local.sendTranslation(p, "messages.hungry", true);
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
					Messages.local.sendTranslation(p, "messages.fire-extinguish", true);
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

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.ORE_WASHER, "ORE_WASHER",
		new ItemStack[] {null, new ItemStack(Material.DISPENSER), null, null, new ItemStack(Material.OAK_FENCE), null, null, new ItemStack(Material.CAULDRON), null},
		new ItemStack[] {SlimefunItems.SIFTED_ORE, SlimefunItems.IRON_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.GOLD_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.COPPER_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.TIN_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.ZINC_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.ALUMINUM_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.MAGNESIUM_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.LEAD_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.SILVER_DUST},
		Material.OAK_FENCE)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(Player p, MultiBlock mb, Block b) {

				SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("ORE_WASHER");

				if (mb.isMultiBlock(machine)) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, machine.getItem(), true)) {
							Block dispBlock = b.getRelative(BlockFace.UP);
							Dispenser disp = (Dispenser) dispBlock.getState();
							Inventory inv = disp.getInventory();
							for (ItemStack current: inv.getContents()) {
								if (current != null) {
									if (SlimefunManager.isItemSimiliar(current, SlimefunItems.SIFTED_ORE, true)) {
										ItemStack adding = SlimefunItems.IRON_DUST;
										if (SlimefunStartup.chance(100, 25)) adding = SlimefunItems.GOLD_DUST;
										else if (SlimefunStartup.chance(100, 25)) adding = SlimefunItems.ALUMINUM_DUST;
										else if (SlimefunStartup.chance(100, 25)) adding = SlimefunItems.COPPER_DUST;
										else if (SlimefunStartup.chance(100, 25)) adding = SlimefunItems.ZINC_DUST;
										else if (SlimefunStartup.chance(100, 25)) adding = SlimefunItems.TIN_DUST;
										else if (SlimefunStartup.chance(100, 25)) adding = SlimefunItems.MAGNESIUM_DUST;
										else if (SlimefunStartup.chance(100, 25)) adding = SlimefunItems.LEAD_DUST;
										else if (SlimefunStartup.chance(100, 25)) adding = SlimefunItems.SILVER_DUST;
										
										Inventory outputInv = null;
										
										if (!legacy_ore_washer) {
											// This is a fancy way of checking if there is empty space in the inv; by checking if an unobtainable item could fit in it.
											// However, due to the way the method findValidOutputInv() functions, the dummyAdding will never actually be added to the real inventory,
											// so it really doesn't matter what item the ItemStack is made by. SlimefunItems.DEBUG_FISH however, signals that it's
											// not supposed to be given to the player.
											ItemStack dummyAdding = SlimefunItems.DEBUG_FISH;
											outputInv = SlimefunMachine.findValidOutputInv(dummyAdding, dispBlock, inv);
										} else outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
					
										if (outputInv != null) {
											ItemStack removing = current.clone();
											removing.setAmount(1);
											inv.removeItem(removing);
											outputInv.addItem(adding);
											p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
											p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WATER);
											if (InvUtils.fits(outputInv, SlimefunItems.STONE_CHUNK)) outputInv.addItem(SlimefunItems.STONE_CHUNK);
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
										return true;
									}
									else if (SlimefunManager.isItemSimiliar(current, new ItemStack(Material.SAND, 4), false)) {
										ItemStack adding = SlimefunItems.SALT;
										Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
										if (outputInv != null) {
											ItemStack removing = current.clone();
											removing.setAmount(4);
											inv.removeItem(removing);
											outputInv.addItem(adding);
											p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WATER);
											p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
										return true;
									}
									else if (SlimefunManager.isItemSimiliar(current, SlimefunItems.PULVERIZED_ORE, true)) {
										ItemStack adding = SlimefunItems.PURE_ORE_CLUSTER;
										Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
										if (outputInv != null) {
											ItemStack removing = current.clone();
											removing.setAmount(1);
											inv.removeItem(removing);
											outputInv.addItem(adding);
											p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WATER);
											p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
										return true;
									}
								}
							}
							Messages.local.sendTranslation(p, "machines.unknown-material", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

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

		new SlimefunItem(Categories.TOOLS, SlimefunItems.AUTO_SMELT_PICKAXE, "SMELTERS_PICKAXE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LAVA_CRYSTAL, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.LAVA_CRYSTAL, null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
		.register(true, new BlockBreakHandler() {

			@Override
			public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.AUTO_SMELT_PICKAXE, true)) {
					if (BlockStorage.hasBlockInfo(e.getBlock())) return true;
					if (e.getBlock().getType().equals(Material.PLAYER_HEAD)) return true;

					int j = -1;
					List<ItemStack> dropsList = (List<ItemStack>) e.getBlock().getDrops();
					for (int i = 0; i < dropsList.size(); i++) {
						if (dropsList.get(i) != null) {
							j++;
							drops.add(e.getBlock().getType().toString().endsWith("_ORE") ? new CustomItem(dropsList.get(i), fortune): dropsList.get(i));
							if (RecipeCalculator.getSmeltedOutput(drops.get(i).getType()) != null) {
								e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
								drops.set(j, new CustomItem(RecipeCalculator.getSmeltedOutput(drops.get(i).getType()), drops.get(i).getAmount()));
							}
						}
					}

					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.TALISMAN, "COMMON_TALISMAN", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2, null, new ItemStack(Material.EMERALD), null, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2},
		new String[] {"recipe-requires-nether-stars"}, new Boolean[] {false})
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_ANVIL, "ANVIL_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.ANVIL), SlimefunItems.TALISMAN, new ItemStack(Material.ANVIL), SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, false, "anvil", new PotionEffect[0])
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_MINER, "MINER_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.TALISMAN, SlimefunItems.SIFTED_ORE, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		false, false, "miner", 20, new PotionEffect[0])
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_HUNTER, "HUNTER_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.TALISMAN, SlimefunItems.MONSTER_JERKY, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		false, false, "hunter", 20, new PotionEffect[0])
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
		false, true, "angel", 75, new PotionEffect[0])
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_FIRE, "FIRE_TALISMAN",
		new ItemStack[] {SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.TALISMAN, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3},
		true, true, "fire", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4))
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_MAGICIAN, "MAGICIAN_TALISMAN",
		new ItemStack[] {SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.ENCHANTING_TABLE), SlimefunItems.TALISMAN, new ItemStack(Material.ENCHANTING_TABLE), SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3},
		false, false, "magician", 80, new PotionEffect[0])
		.register(true);

		for (Enchantment e : Enchantment.values()) {
			for (int i = 1; i <= e.getMaxLevel(); i++) {
				Slimefun.setItemVariable("MAGICIAN_TALISMAN", "allow-enchantments." + e.getKey().getKey() + ".level." + i, true);
			}
		}

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
		, false, true, "whirlwind", 60, new PotionEffect[0])
		.register(true);

		new Talisman(SlimefunItems.TALISMAN_WIZARD, "WIZARD_TALISMAN",
		new ItemStack[] {SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.TALISMAN_MAGICIAN, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3},
		false, false, "wizard", 60, new PotionEffect[0])
		.register(true);
		
		new ExcludedTool(Categories.TOOLS, SlimefunItems.LUMBER_AXE, "LUMBER_AXE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.SYNTHETIC_DIAMOND, null, SlimefunItems.SYNTHETIC_EMERALD, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
		.register(true, new BlockBreakHandler() {

			@Override
			public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
				if (SlimefunManager.isItemSimiliar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.LUMBER_AXE, true)) {
					if (MaterialHelper.isLog( e.getBlock().getType())) {
						List<Location> logs = new ArrayList<Location>();
						TreeCalculator.getTree(e.getBlock().getLocation(), e.getBlock().getLocation(), logs);

						if (logs.contains(e.getBlock().getLocation())) logs.remove(e.getBlock().getLocation());
						for (Location b: logs) {
							if (CSCoreLib.getLib().getProtectionManager().canBuild(e.getPlayer().getUniqueId(), b.getBlock())) {
								b.getWorld().playEffect(b, Effect.STEP_SOUND, b.getBlock().getType());
								for (ItemStack drop: b.getBlock().getDrops()) {
									b.getWorld().dropItemNaturally(b, drop);
								}
								b.getBlock().setType(Material.AIR);
							}
						}
					}
					return true;
				}
				else return false;
			}
		});

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
					p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 2));
					p.setFireTicks(0);
					e.setCancelled(true);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.PORTABLE, SlimefunItems.MEDICINE, "MEDICINE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.VITAMINS, new ItemStack(Material.GLASS_BOTTLE), SlimefunItems.HEAVY_CREAM, null, null, null, null, null, null})
		.register(true);

		new SlimefunArmorPiece(Categories.TECH, SlimefunItems.NIGHT_VISION_GOGGLES, "NIGHT_VISION_GOGGLES", RecipeType.ARMOR_FORGE,
		new ItemStack[] {new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.LIME_STAINED_GLASS_PANE), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.LIME_STAINED_GLASS_PANE), new ItemStack(Material.COAL_BLOCK), null, new ItemStack(Material.COAL_BLOCK)},
		new PotionEffect[] {new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 20)})
		.register(true);

		new SlimefunItem(Categories.TOOLS, SlimefunItems.PICKAXE_OF_CONTAINMENT, "PICKAXE_OF_CONTAINMENT", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, SlimefunItems.FERROSILICON, null, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
		.register(true, new BlockBreakHandler() {

			@Override
			public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.PICKAXE_OF_CONTAINMENT, true)) {
					Block b = e.getBlock(); // Refactored it into this so we don't need to call e.getBlock() all the time.
					if (b.getType() != Material.SPAWNER || BlockStorage.hasBlockInfo(b)) return true; 
					// If the spawner's BlockStorage has BlockInfo, then it's not a vanilla spawner and shouldn't give a broken spawner.
					ItemStack spawner = SlimefunItems.BROKEN_SPAWNER.clone();
					ItemMeta im = spawner.getItemMeta();
					List<String> lore = im.getLore();
					for (int i = 0; i < lore.size(); i++) {
						if (lore.get(i).contains("<Type>")) lore.set(i, lore.get(i).replace("<Type>", StringUtils.format(((CreatureSpawner) b.getState()).getSpawnedType().toString())));
					}
					im.setLore(lore);
					spawner.setItemMeta(im);
					b.getLocation().getWorld().dropItemNaturally(b.getLocation(), spawner);
					e.setExpToDrop(0);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.TOOLS, SlimefunItems.HERCULES_PICKAXE, "HERCULES_PICKAXE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
		.register(true, new BlockBreakHandler() {

			@Override
			public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
				if (SlimefunManager.isItemSimiliar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.HERCULES_PICKAXE, true) && e.getBlock().getType().toString().endsWith("_ORE")) {
					if (e.getBlock().getType() == Material.IRON_ORE) drops.add(new CustomItem(SlimefunItems.IRON_DUST, 2));
					else if (e.getBlock().getType() == Material.GOLD_ORE) drops.add(new CustomItem(SlimefunItems.GOLD_DUST, 2));
					else {
						for (ItemStack drop: e.getBlock().getDrops()) {
							drops.add(new CustomItem(drop, 2));
						}
					}
					return true;
				}
				else return false;
			}
		});
                
		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.TABLE_SAW, "TABLE_SAW",
		new ItemStack[] {null, null, null, new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STONECUTTER), new ItemStack(Material.STONE_SLAB), null, new ItemStack(Material.IRON_BLOCK), null},
		new ItemStack[] {}, Material.STONECUTTER)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(Player p, MultiBlock mb, Block b) {
				if (mb.isMultiBlock(SlimefunItem.getByID("TABLE_SAW"))) {
					if (CSCoreLib.getLib().getProtectionManager().canBuild(p.getUniqueId(), b.getRelative(BlockFace.UP), true)) {
						if (Slimefun.hasUnlocked(p, SlimefunItems.TABLE_SAW, true)) {
								if(p.getInventory().getItemInMainHand() != null && Tag.LOGS.getValues().contains(p.getInventory().getItemInMainHand().getType())) {
									ItemStack log = p.getInventory().getItemInMainHand();
									
									ItemStack item =  new ItemStack(MaterialHelper.getWoodFromLog(log.getType()), 8);
									b.getWorld().dropItemNaturally(b.getLocation(), item);
									b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, log.getType());
									log.setAmount(log.getAmount() -1);
									if(log.getAmount() <= 0)
										p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
									 
								
							}
						}
					}
					return true;
				}
				else return false;
			}
		});
		
		/*
		* dNiym 7/30/2019 added the Table_Saw machine to replace the Saw_mill, as the sawmill's design does not work with
		* the new types of log's in minecraft.   Now that there are multiple types with their own object ID the existing 
		* way of detecting multi blocks limits us to using specific material types in a build, therefore having a block that
		* needs to change like for the sawmill is not possible to do without major overhauling of multiblocks.  The Saw_Mill
		* machine has been left in as to not break machines on existing servers however it should no longer show up in the
		* slimefun guide.
		*/
		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.SAW_MILL, "SAW_MILL",
		new ItemStack[] {null, null, null, new ItemStack(Material.IRON_BARS), new ItemStack(Material.OAK_LOG), new ItemStack(Material.IRON_BARS), new ItemStack(Material.OAK_LOG), new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.OAK_LOG)},
		new ItemStack[] {}, Material.CRAFTING_TABLE)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(Player p, MultiBlock mb, Block b) {
				if (mb.isMultiBlock(SlimefunItem.getByID("SAW_MILL"))) {
					if (CSCoreLib.getLib().getProtectionManager().canBuild(p.getUniqueId(), b.getRelative(BlockFace.UP), true)) {
						if (Slimefun.hasUnlocked(p, SlimefunItems.SAW_MILL, true)) {
							if (MaterialHelper.isLog(b.getRelative(BlockFace.UP).getType())) {
								Block log = b.getRelative(BlockFace.UP);
								if (!BlockStorage.hasBlockInfo(log)) {
									ItemStack item =  new ItemStack(MaterialHelper.getWoodFromLog(log.getType()), 8);
									log.getWorld().dropItemNaturally(log.getLocation(), item);
									log.getWorld().playEffect(log.getLocation(), Effect.STEP_SOUND, log.getType());
									log.setType(Material.AIR); 
								}
							}
						}
					}
					return true;
				}
				else return false;
			}
		});

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
		new ItemStack[0], Material.DISPENSER)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(final Player p, MultiBlock mb, final Block b) {
				if (mb.isMultiBlock(SlimefunItem.getByID("DIGITAL_MINER"))) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, SlimefunItems.DIGITAL_MINER, true)) {
							Block chestBlock = b.getRelative(BlockFace.UP);

                        				if(!(BlockStorage.check(chestBlock.getRelative(BlockFace.WEST), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.EAST), "SOLAR_PANEL")) &&
								!(BlockStorage.check(chestBlock.getRelative(BlockFace.NORTH), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.SOUTH), "SOLAR_PANEL"))) {
								return false;
							}

                        				Chest chest = (Chest) chestBlock.getState();
							final Inventory inv = chest.getInventory();
							List<Location> ores = new ArrayList<Location>();
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
										Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
											if (j < 3) {
												b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, ore);
											} else {
												p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
												inv.addItem(adding);
											}
										}, i*20L);
									}
								}
								else Messages.local.sendTranslation(p, "machines.full-inventory", true);
							}
							else Messages.local.sendTranslation(p, "miner.no-ores", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.ADVANCED_DIGITAL_MINER, "ADVANCED_DIGITAL_MINER",
		new ItemStack[] {SlimefunItems.SOLAR_PANEL, new ItemStack(Material.CHEST), SlimefunItems.SOLAR_PANEL, SlimefunItems.GOLD_24K_BLOCK, new ItemStack(Material.DISPENSER), SlimefunItems.GOLD_24K_BLOCK, SlimefunItems.GOLD_24K_BLOCK, new ItemStack(Material.HOPPER), SlimefunItems.GOLD_24K_BLOCK},
		new ItemStack[0], Material.DISPENSER)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(final Player p, MultiBlock mb, final Block b) {
				if (mb.isMultiBlock(SlimefunItem.getByID("ADVANCED_DIGITAL_MINER"))) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, SlimefunItems.ADVANCED_DIGITAL_MINER, true)) {
							Block chestBlock = b.getRelative(BlockFace.UP);

                        				if(!(BlockStorage.check(chestBlock.getRelative(BlockFace.WEST), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.EAST), "SOLAR_PANEL")) &&
								!(BlockStorage.check(chestBlock.getRelative(BlockFace.NORTH), "SOLAR_PANEL") && BlockStorage.check(chestBlock.getRelative(BlockFace.SOUTH), "SOLAR_PANEL"))) {
								return false;
							}

                        				Chest chest = (Chest) chestBlock.getState();
							final Inventory inv = chest.getInventory();
							List<Location> ores = new ArrayList<Location>();
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
										Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
											if (j < 3) {
												b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, ore);
											} else {
												p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
												inv.addItem(adding);
											}
										}, i*20L);
									}
								}
								else Messages.local.sendTranslation(p, "machines.full-inventory", true);
							}
							else Messages.local.sendTranslation(p, "miner.no-ores", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MISC, SlimefunItems.GOLD_24K_BLOCK, "GOLD_24K_BLOCK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K})
		.register(true);

		new SlimefunGadget(Categories.MACHINES_1, SlimefunItems.COMPOSTER, "COMPOSTER", RecipeType.ENHANCED_CRAFTING_TABLE,
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
		}).register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, final Player p, ItemStack item) {
				if (e.getClickedBlock() != null) {
					SlimefunItem machine = BlockStorage.check(e.getClickedBlock());
					if (machine != null && machine.getID().equals("COMPOSTER")) {
						if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), e.getClickedBlock(), true)) {
							final ItemStack input = p.getInventory().getItemInMainHand();
							final Block b = e.getClickedBlock();
							for (ItemStack convert: RecipeType.getRecipeInputs(machine)) {
								if (convert != null && SlimefunManager.isItemSimiliar(input, convert, true)) {
									ItemStack removing = input.clone();
									removing.setAmount(convert.getAmount());
									p.getInventory().removeItem(removing);
									final ItemStack adding = RecipeType.getRecipeOutput(machine, convert);

									for (int i = 1; i < 12; i++) {
										int j = i;
										Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
											if (j < 11) {
												b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, input.getType().isBlock() ? input.getType() : Material.HAY_BLOCK);
											} else {
												p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
												b.getWorld().dropItemNaturally(b.getRelative(BlockFace.UP).getLocation(), adding);
											}
										}, i*30L);
									}

									return true;
								}
							}
							Messages.local.sendTranslation(p, "machines.wrong-item", true);
							return true;
						}
						return true;
					}
				}
				return false;
			}
		});

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.FARMER_SHOES, "FARMER_SHOES", RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.HAY_BLOCK), null, new ItemStack(Material.HAY_BLOCK), new ItemStack(Material.HAY_BLOCK), null, new ItemStack(Material.HAY_BLOCK)})
		.register(true);

		@SuppressWarnings("unchecked")
		final String[] explosiveblacklist = Slimefun.getItemValue("EXPLOSIVE_PICKAXE", "unbreakable-blocks") != null ? ((List<String>) Slimefun.getItemValue("EXPLOSIVE_PICKAXE", "unbreakable-blocks")).toArray(new String[((List<String>) Slimefun.getItemValue("EXPLOSIVE_PICKAXE", "unbreakable-blocks")).size()]): new String[] {"BEDROCK", "BARRIER", "COMMAND", "COMMAND_CHAIN", "COMMAND_REPEATING"};
		final boolean damageOnUse = Boolean.TRUE.equals(((Boolean) Slimefun.getItemValue("EXPLOSIVE_PICKAXE", "damage-on-use")));

		new SlimefunItem(Categories.TOOLS, SlimefunItems.EXPLOSIVE_PICKAXE, "EXPLOSIVE_PICKAXE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.TNT), SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.TNT), null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null},
		new String[] {"unbreakable-blocks", "damage-on-use"}, new Object[] {Arrays.asList("BEDROCK", "BARRIER", "COMMAND", "COMMAND_CHAIN", "COMMAND_REPEATING"), Boolean.FALSE })
		.register(true, new BlockBreakHandler() {

			@Override
			public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.EXPLOSIVE_PICKAXE, true)) {
					e.getBlock().getWorld().createExplosion(e.getBlock().getLocation(), 0.0F);
					e.getBlock().getWorld().playSound(e.getBlock().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							for (int z = -1; z <= 1; z++) {
								Block b = e.getBlock().getRelative(x, y, z);
								if (b.getType() != Material.AIR && !b.isLiquid() && !StringUtils.equals(b.getType().toString(), explosiveblacklist)) {
									if (CSCoreLib.getLib().getProtectionManager().canBuild(e.getPlayer().getUniqueId(), b)) {
										if (SlimefunStartup.instance.isCoreProtectInstalled()) SlimefunStartup.instance.getCoreProtectAPI().logRemoval(e.getPlayer().getName(), b.getLocation(), b.getType(), b.getBlockData());
										b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
										SlimefunItem sfItem = BlockStorage.check(b);
										boolean allow = false;
										if (sfItem != null && !(sfItem instanceof HandledBlock)) {
											if (SlimefunItem.blockhandler.containsKey(sfItem.getID())) {
												allow = SlimefunItem.blockhandler.get(sfItem.getID()).onBreak(e.getPlayer(), e.getBlock(), sfItem, UnregisterReason.PLAYER_BREAK);
											}
											if (allow) {
												drops.add(BlockStorage.retrieve(e.getBlock()));
											}
										}
										else if (b.getType().equals(Material.PLAYER_HEAD)) {
											b.breakNaturally();
										}
										else if (b.getType().name().endsWith("_SHULKER_BOX")) {
											b.breakNaturally();
										}
										else {
											for (ItemStack drop: b.getDrops()) {
												b.getWorld().dropItemNaturally(b.getLocation(), (b.getType().toString().endsWith("_ORE") && !b.getType().equals(Material.IRON_ORE) && !b.getType().equals(Material.GOLD_ORE)) ? new CustomItem(drop, fortune): drop);
											}
											b.setType(Material.AIR);
										}
										if (damageOnUse) {
											if (!item.getEnchantments().containsKey(Enchantment.DURABILITY) || SlimefunStartup.randomize(100) <= (60 + 40 / (item.getEnchantmentLevel(Enchantment.DURABILITY) + 1))) {
												PlayerInventory.damageItemInHand(e.getPlayer());
											}
										}
									}
								}
							}
						}
					}

					PlayerInventory.update(e.getPlayer());
					return true;
				}
				else return false;
			}
		});

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.AUTOMATED_PANNING_MACHINE, "AUTOMATED_PANNING_MACHINE",
		new ItemStack[] {null, null, null, null, new ItemStack(Material.OAK_TRAPDOOR), null, null, new ItemStack(Material.CAULDRON), null},
		new ItemStack[] {new ItemStack(Material.GRAVEL), new ItemStack(Material.FLINT), new ItemStack(Material.GRAVEL), new ItemStack(Material.CLAY_BALL), new ItemStack(Material.GRAVEL), SlimefunItems.SIFTED_ORE}, Material.OAK_TRAPDOOR)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(final Player p, MultiBlock mb, final Block b) {
				if (mb.isMultiBlock(SlimefunItem.getByID("AUTOMATED_PANNING_MACHINE"))) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						final ItemStack input = p.getInventory().getItemInMainHand();
						ItemStack output = null;
						if (SlimefunStartup.chance(100, (Integer) Slimefun.getItemValue("GOLD_PAN", "chance.SIFTED_ORE"))) output = SlimefunItems.SIFTED_ORE;
						else if (SlimefunStartup.chance(100, (Integer) Slimefun.getItemValue("GOLD_PAN", "chance.CLAY"))) output = new ItemStack(Material.CLAY_BALL);
						else if (SlimefunStartup.chance(100, (Integer) Slimefun.getItemValue("GOLD_PAN", "chance.FLINT"))) output = new ItemStack(Material.FLINT);
						final ItemStack drop = output;
						if (input != null) {
							if (input.getType() == Material.GRAVEL) {
								PlayerInventory.consumeItemInHand(p);
								for (int i = 1; i < 7; i++) {
									int j = i;
									Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
										b.getWorld().playEffect(b.getRelative(BlockFace.DOWN).getLocation(), Effect.STEP_SOUND, Material.GRAVEL);
										if (j == 6) {
											if (drop != null) b.getWorld().dropItemNaturally(b.getLocation(), drop);
											p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
										}
									}, i*30L);
								}
								return true;
							}
						}
						Messages.local.sendTranslation(p, "machines.wrong-item", true);
						return true;
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MAGIC_ARMOR, SlimefunItems.BOOTS_OF_THE_STOMPER, "BOOTS_OF_THE_STOMPER", RecipeType.ARMOR_FORGE,
		new ItemStack[] {null, null, null, new ItemStack(Material.YELLOW_WOOL), null, new ItemStack(Material.YELLOW_WOOL), new ItemStack(Material.PISTON), null, new ItemStack(Material.PISTON)})
		.register(true);

		new SlimefunItem(Categories.TOOLS, SlimefunItems.PICKAXE_OF_THE_SEEKER, "PICKAXE_OF_THE_SEEKER", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.COMPASS), SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.COMPASS), null, SlimefunItems.FERROSILICON, null, null, SlimefunItems.FERROSILICON, null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.PICKAXE_OF_THE_SEEKER, true)) {
					Block closest = null;

					for (int x = -4; x <= 4; x++) {
						for (int y = -4; y <= 4; y++) {
							for (int z = -4; z <= 4; z++) {
								if (p.getLocation().getBlock().getRelative(x, y, z).getType().toString().endsWith("_ORE")) {
									if (closest == null) closest = p.getLocation().getBlock().getRelative(x, y, z);
									else if (p.getLocation().distance(closest.getLocation()) < p.getLocation().distance(p.getLocation().getBlock().getRelative(x, y, z).getLocation())) closest = p.getLocation().getBlock().getRelative(x, y, z);
								}
							}
						}
					}

					if (closest == null) Messages.local.sendTranslation(p, "miner.no-ores", true);
					else {
						double l = closest.getX() + 0.5 - p.getLocation().getX();
						double w = closest.getZ() + 0.5 - p.getLocation().getZ();
						float yaw, pitch;
						double c = Math.sqrt(l * l + w * w);
						double alpha1 = -Math.asin(l / c) / Math.PI * 180;
						double alpha2 =  Math.acos(w / c) / Math.PI * 180;
						if (alpha2 > 90) yaw = (float) (180 - alpha1);
						else yaw = (float) alpha1;
						pitch = (float) ((-Math.atan((closest.getY() - 0.5 - p.getLocation().getY()) / Math.sqrt(l * l + w * w))) * 180F / Math.PI);

						p.teleport(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), yaw, pitch));
					}

					if (e.getPlayer().getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.DURABILITY)) {
						if (SlimefunStartup.randomize(100) <= (60 + 40 / (e.getPlayer().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DURABILITY) + 1))) PlayerInventory.damageItemInHand(e.getPlayer());
					}
					else PlayerInventory.damageItemInHand(e.getPlayer());

					PlayerInventory.update(e.getPlayer());
					return true;
				}
				else return false;
			}
		});

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

		new SlimefunGadget(Categories.MACHINES_1, SlimefunItems.CRUCIBLE, "CRUCIBLE", RecipeType.ENHANCED_CRAFTING_TABLE,
				new ItemStack [] {new ItemStack(Material.TERRACOTTA), null, new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), null, new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), new ItemStack(Material.FLINT_AND_STEEL), new ItemStack(Material.TERRACOTTA)},
				new ItemStack [] {
						new ItemStack(Material.COBBLESTONE, 16), new ItemStack(Material.LAVA_BUCKET), 
						
						new ItemStack(Material.OAK_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
						new ItemStack(Material.BIRCH_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
						new ItemStack(Material.SPRUCE_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
						new ItemStack(Material.JUNGLE_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
						new ItemStack(Material.ACACIA_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
						new ItemStack(Material.DARK_OAK_LEAVES, 16), new ItemStack(Material.WATER_BUCKET),
						
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
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, final Player p, ItemStack item) {
				if (e.getClickedBlock() != null) {
					SlimefunItem machine = BlockStorage.check(e.getClickedBlock());
					if (machine != null && machine.getID().equals("CRUCIBLE")) {
						if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), e.getClickedBlock(), true)) {
							final ItemStack input = p.getInventory().getItemInMainHand();
							final Block block = e.getClickedBlock().getRelative(BlockFace.UP);
							for (ItemStack convert: RecipeType.getRecipeInputs(machine)) {
								if (input != null && SlimefunManager.isItemSimiliar(input, convert, true)) {
									e.setCancelled(true);
									ItemStack removing = input.clone();
									removing.setAmount(convert.getAmount());
									p.getInventory().removeItem(removing);

									for (int i = 1; i < 9; i++) {
										int j = 8 - i;
										Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
											if (input.getType() == Material.COBBLESTONE || input.getType() == Material.TERRACOTTA || MaterialHelper.isTerracotta(input.getType())) {
												block.setType(Material.LAVA);
												Levelled le = (Levelled) block.getBlockData();
												le.setLevel(j);
												block.setBlockData(le, false);
												block.getWorld().playSound(block.getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1F);
											} else if (MaterialHelper.isLeavesBlock(input.getType())) {
												block.setType(Material.WATER);
												Levelled le = (Levelled) block.getBlockData();
												le.setLevel(j);
												block.setBlockData(le, false);
												block.getWorld().playSound(block.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);
											}
										}, i*50L);
									}

									return true;
								}
							}
							Messages.local.sendTranslation(p, "machines.wrong-item", true);
							return true;
						}
						return true;
					}
				}
				return false;
			}
		});

		new SlimefunBackpack(45, Categories.PORTABLE, SlimefunItems.GILDED_BACKPACK, "GILDED_BACKPACK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_22K, null, SlimefunItems.GOLD_22K, new ItemStack(Material.LEATHER), SlimefunItems.WOVEN_BACKPACK, new ItemStack(Material.LEATHER), SlimefunItems.GOLD_22K, null, SlimefunItems.GOLD_22K})
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
		new ItemStack[] {SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.GILDED_BACKPACK, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2})
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

		new ExcludedTool(Categories.WEAPONS, SlimefunItems.SEISMIC_AXE, "SEISMIC_AXE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.STAFF_ELEMENTAL, null, null, SlimefunItems.STAFF_ELEMENTAL, null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.SEISMIC_AXE, true)) {
					List<Block> blocks = p.getLineOfSight((HashSet<Material>) null, 10);
					for (int i = 0; i < blocks.size(); i++) {
						Block b = blocks.get(i);
						Location ground = b.getLocation();
						if (b.getType() == null || b.getType() == Material.AIR) {
							for (int y = ground.getBlockY(); y > 0; y--) {
								if (b.getWorld().getBlockAt(b.getX(), y, b.getZ()) != null) {
									if (b.getWorld().getBlockAt(b.getX(), y, b.getZ()).getType() != null) {
										if (b.getWorld().getBlockAt(b.getX(), y, b.getZ()).getType() != Material.AIR) {
											ground = new Location(b.getWorld(), b.getX(), y, b.getZ());
											break;
										}
									}
								}
							}
						}
						b.getWorld().playEffect(ground, Effect.STEP_SOUND, ground.getBlock().getType());
						if (ground.getBlock().getRelative(BlockFace.UP).getType() == null || ground.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
							FallingBlock block = ground.getWorld().spawnFallingBlock(ground.getBlock().getRelative(BlockFace.UP).getLocation(), ground.getBlock().getBlockData());
							block.setDropItem(false);
							block.setVelocity(new Vector(0, 0.4 + i * 0.01, 0));
							Variables.blocks.add(block.getUniqueId());
						}
						for (Entity n: ground.getChunk().getEntities()) {
							if (n instanceof LivingEntity) {
								if (n.getLocation().distance(ground) <= 2.0D && n.getUniqueId() != p.getUniqueId()) {
									Vector vector = n.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(1.4);
									vector.setY(0.9);
									n.setVelocity(vector);
									if (p.getWorld().getPVP()) {
										EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, n, DamageCause.ENTITY_ATTACK, 6D);
										Bukkit.getPluginManager().callEvent(event);
										if (!event.isCancelled()) ((LivingEntity) n).damage(6D);
									}
								}
							}
						}
					}

					for (int i = 0; i < 4; i++) {
						if (e.getPlayer().getInventory().getItemInMainHand() != null) {
							if (e.getPlayer().getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.DURABILITY)) {
								if (SlimefunStartup.randomize(100) <= (60 + 40 / (e.getPlayer().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DURABILITY) + 1))) PlayerInventory.damageItemInHand(e.getPlayer());
							}
							else PlayerInventory.damageItemInHand(e.getPlayer());
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.TOOLS, SlimefunItems.PICKAXE_OF_VEIN_MINING, "PICKAXE_OF_VEIN_MINING", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.EMERALD_ORE), SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.EMERALD_ORE), null, SlimefunItems.GILDED_IRON, null, null, SlimefunItems.GILDED_IRON, null})
		.register(true, new BlockBreakHandler() {

			@Override
			public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
				if (SlimefunManager.isItemSimiliar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.PICKAXE_OF_VEIN_MINING, true)) {
					if (e.getBlock().getType().toString().endsWith("_ORE")) {
						List<Location> blocks = new ArrayList<Location>();
						Vein.calculate(e.getBlock().getLocation(), e.getBlock().getLocation(), blocks, 16);
						for (Location block: blocks) {
							Block b = block.getBlock();
							b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
							for (ItemStack drop: b.getDrops()) {
								b.getWorld().dropItemNaturally(b.getLocation(), drop.getType().isBlock() ? drop: new CustomItem(drop, fortune));
							}
							b.setType(Material.AIR);
						}
					}
					return true;
				}
				else return false;
			}
		});

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

		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.JUICER, "JUICER",
		new ItemStack[] {null, new ItemStack(Material.GLASS), null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, null, new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), null},
		new ItemStack[] {
				new ItemStack(Material.APPLE), SlimefunItems.APPLE_JUICE,
				new ItemStack(Material.MELON), SlimefunItems.MELON_JUICE,
				new ItemStack(Material.CARROT), SlimefunItems.CARROT_JUICE,
				new ItemStack(Material.PUMPKIN), SlimefunItems.PUMPKIN_JUICE},
		Material.NETHER_BRICK_FENCE)
		.register(true, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(Player p, MultiBlock mb, Block b) {
				SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("JUICER");

				if (mb.isMultiBlock(machine)) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, SlimefunItems.JUICER, true)) {
							Block dispBlock = b.getRelative(BlockFace.DOWN);
							Dispenser disp = (Dispenser) dispBlock.getState();
							Inventory inv = disp.getInventory();
							for (ItemStack current: inv.getContents()) {
								for (ItemStack convert: RecipeType.getRecipeInputs(machine)) {
									if (convert != null && SlimefunManager.isItemSimiliar(current, convert, true)) {
										ItemStack adding = RecipeType.getRecipeOutput(machine, convert);
										Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
										if (outputInv != null) {
											ItemStack removing = current.clone();
											removing.setAmount(1);
											inv.removeItem(removing);
											outputInv.addItem(adding);
											p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);
											p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.HAY_BLOCK);
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
										return true;
									}
								}
							}
							Messages.local.sendTranslation(p, "machines.unknown-material", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

		new Juice(Categories.FOOD, SlimefunItems.APPLE_JUICE, "APPLE_JUICE", RecipeType.JUICER,
		new ItemStack[] {null, null, null, null, new ItemStack(Material.APPLE), null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, SlimefunItems.CARROT_JUICE, "CARROT_JUICE", RecipeType.JUICER,
		new ItemStack[] {null, null, null, null, new ItemStack(Material.CARROT), null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, SlimefunItems.MELON_JUICE, "MELON_JUICE", RecipeType.JUICER,
		new ItemStack[] {null, null, null, null, new ItemStack(Material.MELON), null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, SlimefunItems.PUMPKIN_JUICE, "PUMPKIN_JUICE", RecipeType.JUICER,
		new ItemStack[] {null, null, null, null, new ItemStack(Material.PUMPKIN), null, null, null, null})
		.register(true);

		new Juice(Categories.FOOD, SlimefunItems.GOLDEN_APPLE_JUICE, "GOLDEN_APPLE_JUICE", RecipeType.JUICER,
		new ItemStack[] {new ItemStack(Material.GOLDEN_APPLE), null, null, null, null, null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.BROKEN_SPAWNER, "BROKEN_SPAWNER", new RecipeType(SlimefunItems.PICKAXE_OF_CONTAINMENT),
		new ItemStack[] {null, null, null, null, new ItemStack(Material.SPAWNER), null, null, null, null})
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.REPAIRED_SPAWNER, "REINFORCED_SPAWNER", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {SlimefunItems.RUNE_ENDER, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"), SlimefunItems.ESSENCE_OF_AFTERLIFE, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"), SlimefunItems.BROKEN_SPAWNER, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"), SlimefunItems.ESSENCE_OF_AFTERLIFE, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"), SlimefunItems.RUNE_ENDER})
		.register(true, new BlockPlaceHandler() {

			@Override
			public boolean onBlockPlace(BlockPlaceEvent e, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.REPAIRED_SPAWNER, false)) {
					EntityType type = null;
					for (String line: item.getItemMeta().getLore()) {
						if (ChatColor.stripColor(line).startsWith("Type: ")) type = EntityType.valueOf(ChatColor.stripColor(line).replace("Type: ", "").replace(" ", "_").toUpperCase());
					}
					if (type != null) {
						CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
						spawner.setSpawnedType(type);
						spawner.update(true, false);
					}
					return true;
				}
				else return false;
			}
		}, new BlockBreakHandler() {

			@Override
			public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
				SlimefunItem spawner = BlockStorage.check(e.getBlock());
				if (spawner != null && SlimefunManager.isItemSimiliar(spawner.getItem(), SlimefunItems.REPAIRED_SPAWNER, false)) {
					if (SlimefunManager.isItemSimiliar(item, SlimefunItems.PICKAXE_OF_CONTAINMENT, true))
						return false;
					BlockStorage.clearBlockInfo(e.getBlock());
					return true;
				}
				else return false;
			}
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


		@SuppressWarnings("unchecked")
		final String[] blockPlacerBlacklist = Slimefun.getItemValue("BLOCK_PLACER", "unplaceable-blocks") != null ? ((List<String>) Slimefun.getItemValue("BLOCK_PLACER", "unplaceable-blocks")).toArray(new String[((List<String>) Slimefun.getItemValue("BLOCK_PLACER", "unplaceable-blocks")).size()]) : new String[] {"STRUCTURE_BLOCK"};

		new SlimefunItem(Categories.MACHINES_1, SlimefunItems.BLOCK_PLACER, "BLOCK_PLACER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GOLD_4K, new ItemStack(Material.PISTON), SlimefunItems.GOLD_4K, new ItemStack(Material.IRON_INGOT), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.IRON_INGOT), SlimefunItems.GOLD_4K, new ItemStack(Material.PISTON), SlimefunItems.GOLD_4K}, 
		new String[] {"unplaceable-blocks"}, new Object[] {Arrays.asList("STRUCTURE_BLOCK")})
		.register(true, new AutonomousMachineHandler() {

			@Override
			public boolean onBlockDispense(final BlockDispenseEvent e, Block dispenser, final Dispenser d, Block block, Block chest, SlimefunItem machine) {
				if (machine.getID().equalsIgnoreCase("BLOCK_PLACER")) {
					e.setCancelled(true);
					if ((block.getType() == null || block.getType() == Material.AIR) && e.getItem().getType().isBlock()) {
						for(String blockType : blockPlacerBlacklist) {
							if (e.getItem().getType().toString().equals(blockType)) {
								return false;
							}
						}
						
						SlimefunItem sfItem = SlimefunItem.getByItem(e.getItem());
						if (sfItem != null) {
							if (!SlimefunItem.blockhandler.containsKey(sfItem.getID())) {
								block.setType(e.getItem().getType());
								BlockStorage.store(block, sfItem.getID());
								block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, e.getItem().getType());
								if (d.getInventory().containsAtLeast(e.getItem(), 2)) d.getInventory().removeItem(new CustomItem(e.getItem(), 1));
								else {
									Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
										d.getInventory().removeItem(e.getItem());
									}, 2L);
								}
							}
						}
						else {
							block.setType(e.getItem().getType());
							block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, e.getItem().getType());
							if (d.getInventory().containsAtLeast(e.getItem(), 2)) d.getInventory().removeItem(new CustomItem(e.getItem(), 1));
							else {
								Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
									d.getInventory().removeItem(e.getItem());
								}, 2L);
							}
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MAGIC, SlimefunItems.SCROLL_OF_DIMENSIONAL_TELEPOSITION, "SCROLL_OF_DIMENSIONAL_TELEPOSITION", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGICAL_BOOK_COVER, SlimefunItems.ENDER_LUMP_3, SlimefunItems.MAGIC_EYE_OF_ENDER, SlimefunItems.ENDER_LUMP_3, null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.SCROLL_OF_DIMENSIONAL_TELEPOSITION, true)) {
					for (Entity n: p.getNearbyEntities(10.0, 10.0, 10.0)) {
						if (n instanceof LivingEntity && !(n instanceof ArmorStand) &&n.getUniqueId() != p.getUniqueId()) {
							float yaw = n.getLocation().getYaw() + 180.0F;
							if (yaw > 360.0F) yaw = yaw - 360.0F;
							n.teleport(new Location(n.getWorld(), n.getLocation().getX(), n.getLocation().getY(), n.getLocation().getZ(), yaw, n.getLocation().getPitch()));
						}
					}
					return true;
				}
				else return false;
			}
		});

		new SlimefunBow(SlimefunItems.EXPLOSIVE_BOW, "EXPLOSIVE_BOW",
		new ItemStack[] {null, new ItemStack(Material.STICK), new ItemStack(Material.GUNPOWDER), SlimefunItems.STAFF_FIRE, null, SlimefunItems.SULFATE, null, new ItemStack(Material.STICK), new ItemStack(Material.GUNPOWDER)})
		.register(true, new BowShootHandler() {

			@Override
			public boolean onHit(EntityDamageByEntityEvent e, LivingEntity n) {
				if (SlimefunManager.isItemSimiliar(Variables.arrows.get(e.getDamager().getUniqueId()), SlimefunItems.EXPLOSIVE_BOW, true)) {
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
				if (SlimefunManager.isItemSimiliar(Variables.arrows.get(e.getDamager().getUniqueId()), SlimefunItems.ICY_BOW, true)) {
					n.getWorld().playEffect(n.getLocation(), Effect.STEP_SOUND, Material.ICE);
					n.getWorld().playEffect(n.getEyeLocation(), Effect.STEP_SOUND, Material.ICE);
					n.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 10));
					n.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 2, -10));
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MAGIC, SlimefunItems.TOME_OF_KNOWLEDGE_SHARING, "TOME_OF_KNOWLEDGE_SHARING", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.FEATHER), null, new ItemStack(Material.INK_SAC), SlimefunItems.MAGICAL_BOOK_COVER, new ItemStack(Material.GLASS_BOTTLE), null, new ItemStack(Material.WRITABLE_BOOK), null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.TOME_OF_KNOWLEDGE_SHARING, true)) {
					List<String> lore = item.getItemMeta().getLore();
					lore.set(0, ChatColor.translateAlternateColorCodes('&', "&7Owner: &b" + p.getName()));
					lore.set(1, ChatColor.BLACK + "" + p.getUniqueId());
					ItemMeta im = item.getItemMeta();
					im.setLore(lore);
					item.setItemMeta(im);
					p.getEquipment().setItemInMainHand(item);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
					return true;
				}
				else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.TOME_OF_KNOWLEDGE_SHARING, false)) {
					List<Research> researches = Research.getResearches(ChatColor.stripColor(item.getItemMeta().getLore().get(1)));
					for (Research research: researches) {
						research.unlock(p, true);
					}
					PlayerInventory.consumeItemInHand(p);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.MAGIC, SlimefunItems.FLASK_OF_KNOWLEDGE, "FLASK_OF_KNOWLEDGE", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, null, null, SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.GLASS_PANE), SlimefunItems.MAGIC_LUMP_2, null, SlimefunItems.MAGIC_LUMP_2, null}, new CustomItem(SlimefunItems.FLASK_OF_KNOWLEDGE, 8))
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.FLASK_OF_KNOWLEDGE, true) && p.getLevel() >= 1) {
					p.setLevel(p.getLevel() - 1);
					p.getInventory().addItem(new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"));
					PlayerInventory.consumeItemInHand(p);
					PlayerInventory.update(p);
					return true;
				}
				else return false;
			}
		});

		new SlimefunItem(Categories.BIRTHDAY, new CustomItem(new ItemStack(Material.CAKE), "&bBirthday Cake"), "BIRTHDAY_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE,
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

					List<ItemStack> gifts = new ArrayList<ItemStack>();
					for (int i = 0; i < 2; i++) {
						gifts.add(new CustomItem(SlimefunItems.EASTER_CARROT_PIE, 4));
						gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_APPLE_PIE, 4));
						gifts.add(new CustomItem(SlimefunItems.CARROT_JUICE, 1));
					}

					gifts.add(new SkullItem("mrCookieSlime"));
					gifts.add(new SkullItem("timtower"));
					gifts.add(new SkullItem("bwfcwalshy"));
					gifts.add(new SkullItem("jadedcat"));
					gifts.add(new SkullItem("ZeldoKavira"));
					gifts.add(new SkullItem("eyamaz"));
					gifts.add(new SkullItem("Kaelten"));
					gifts.add(new SkullItem("Myrathi"));

					p.getWorld().dropItemNaturally(p.getLocation(), gifts.get(SlimefunStartup.randomize(gifts.size())));
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

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.ANCIENT_PEDESTAL, "ANCIENT_PEDESTAL", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.STONE), null, new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K, new ItemStack(Material.OBSIDIAN)}, new CustomItem(SlimefunItems.ANCIENT_PEDESTAL, 4))
		.register(true);

		SlimefunItem.registerBlockHandler("ANCIENT_PEDESTAL", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				Item stack = AncientAltarListener.findItem(b);
				if (stack != null) { 
					stack.removeMetadata("item_placed", SlimefunStartup.instance);
					b.getWorld().dropItem(b.getLocation(), AncientAltarListener.fixItemStack(stack.getItemStack(), stack.getCustomName()));
					stack.remove();
				}
				return true;
			}
		});

		new SlimefunItem(Categories.MAGIC, SlimefunItems.ANCIENT_ALTAR, "ANCIENT_ALTAR", RecipeType.MAGIC_WORKBENCH,
		new ItemStack[] {null, new ItemStack(Material.ENCHANTING_TABLE), null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.OBSIDIAN), SlimefunItems.GOLD_8K, new ItemStack(Material.OBSIDIAN)})
		.register(true);

		// Slimefun 4

		new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.ENERGY_REGULATOR, "ENERGY_REGULATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.SILVER_INGOT})
		.register(true, new BlockTicker() {

			@Override
			public boolean isSynchronized() {
				return false;
			}

			@Override
			public void uniqueTick() {
			}

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				EnergyNet.getNetworkFromLocationOrCreate(b.getLocation()).tick(b);
			}
		});

		SlimefunItem.registerBlockHandler("ENERGY_REGULATOR", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				EnergyHologram.remove(b);
				return true;
			}
		});

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

			@Override
			public String getInventoryTitle() {
				return "&bElectric Furnace";
			}

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

			@Override
			public String getInventoryTitle() {
				return "&bElectric Furnace";
			}

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

			@Override
			public String getInventoryTitle() {
				return "&bElectric Furnace";
			}

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

			@Override
			public String getInventoryTitle() {
				return "&cElectric Ingot Factory";
			}

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

			@Override
			public String getInventoryTitle() {
				return "&cElectric Ingot Factory";
			}

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

			@Override
			public String getInventoryTitle() {
				return "&cElectric Ingot Factory";
			}

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

		new AContainer(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_ORE_GRINDER, "ELECTRIC_ORE_GRINDER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.DIAMOND_PICKAXE), null, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.GILDED_IRON}) {

			@Override
			public void registerDefaultRecipes() {
			}

			@Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.STONE_PICKAXE);
			}

			@Override
			public String getInventoryTitle() {
				return "&bElectric Ore Grinder";
			}

			@Override
			public int getEnergyConsumption() {
				return 6;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

			@Override
			public String getMachineIdentifier() {
				return "ELECTRIC_ORE_GRINDER";
			}

		}.registerChargeableBlock(true, 128);

		new AContainer(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_ORE_GRINDER_2, "ELECTRIC_ORE_GRINDER_2", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.DIAMOND_PICKAXE), null, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_ORE_GRINDER, SlimefunItems.HEATING_COIL, SlimefunItems.GILDED_IRON, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GILDED_IRON}) {

			@Override
			public void registerDefaultRecipes() {
			}

			@Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.DIAMOND_PICKAXE);
			}

			@Override
			public String getInventoryTitle() {
				return "&bElectric Ore Grinder";
			}

			@Override
			public int getEnergyConsumption() {
				return 15;
			}

			@Override
			public int getSpeed() {
				return 4;
			}

			@Override
			public String getMachineIdentifier() {
				return "ELECTRIC_ORE_GRINDER";
			}

		}.registerChargeableBlock(true, 512);

		new HeatedPressureChamber(Categories.ELECTRICITY, SlimefunItems.HEATED_PRESSURE_CHAMBER, "HEATED_PRESSURE_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, new ItemStack(Material.GLASS), SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.LEAD_INGOT}) {

			@Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

			@Override
			public int getEnergyConsumption() {
				return 22;
			}

			@Override
			public int getSpeed() {
				return 5;
			}

		}.registerChargeableBlock(true, 256);

		new AContainer(Categories.ELECTRICITY, SlimefunItems.ELECTRIC_INGOT_PULVERIZER, "ELECTRIC_INGOT_PULVERIZER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.ELECTRIC_ORE_GRINDER, null, SlimefunItems.LEAD_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.LEAD_INGOT}){
			@Override
			public String getInventoryTitle() {
				return "&bElectric Ingot Pulverizer";
			}

			@Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.IRON_PICKAXE);
			}

			@Override
			public void registerDefaultRecipes() {
				registerRecipe(3, new ItemStack[]{SlimefunItems.ALUMINUM_INGOT}, new ItemStack[]{SlimefunItems.ALUMINUM_DUST});
				registerRecipe(3, new ItemStack[]{SlimefunItems.COPPER_INGOT}, new ItemStack[]{SlimefunItems.COPPER_DUST});
				registerRecipe(3, new ItemStack[]{SlimefunItems.GOLD_4K}, new ItemStack[]{SlimefunItems.GOLD_DUST});
				registerRecipe(3, new ItemStack[]{new ItemStack(Material.IRON_INGOT)}, new ItemStack[]{SlimefunItems.IRON_DUST});
				registerRecipe(3, new ItemStack[]{SlimefunItems.LEAD_INGOT}, new ItemStack[]{SlimefunItems.LEAD_DUST});
				registerRecipe(3, new ItemStack[]{SlimefunItems.MAGNESIUM_INGOT}, new ItemStack[]{SlimefunItems.MAGNESIUM_DUST});
				registerRecipe(3, new ItemStack[]{SlimefunItems.SILVER_INGOT}, new ItemStack[]{SlimefunItems.SILVER_DUST});
				registerRecipe(3, new ItemStack[]{SlimefunItems.TIN_INGOT}, new ItemStack[]{SlimefunItems.TIN_DUST});
				registerRecipe(3, new ItemStack[]{SlimefunItems.ZINC_INGOT}, new ItemStack[]{SlimefunItems.ZINC_DUST});


			}

			@Override
			public int getEnergyConsumption() {
				return 7;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

			@Override
			public String getMachineIdentifier() {
				return "ELECTRIC_INGOT_PULVERIZER";
			}
		}.registerChargeableBlock(true, 512);

		new AGenerator(Categories.ELECTRICITY, SlimefunItems.COAL_GENERATOR, "COAL_GENERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HEATING_COIL, new ItemStack(Material.FURNACE), SlimefunItems.HEATING_COIL, SlimefunItems.NICKEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.NICKEL_INGOT, null, SlimefunItems.NICKEL_INGOT, null}) {

			@Override
			public void registerDefaultRecipes() {
				registerFuel(new MachineFuel(8, new ItemStack(Material.COAL)));
				registerFuel(new MachineFuel(8, new ItemStack(Material.CHARCOAL)));
				registerFuel(new MachineFuel(80, new ItemStack(Material.COAL_BLOCK)));
				registerFuel(new MachineFuel(12, new ItemStack(Material.BLAZE_ROD)));

				// Logs
				registerFuel(new MachineFuel(1, new ItemStack(Material.OAK_LOG)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.SPRUCE_LOG)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.BIRCH_LOG)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.JUNGLE_LOG)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.ACACIA_LOG)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.DARK_OAK_LOG)));

				// Wooden Planks
				registerFuel(new MachineFuel(1, new ItemStack(Material.OAK_WOOD)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.SPRUCE_WOOD)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.BIRCH_WOOD)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.JUNGLE_WOOD)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.ACACIA_WOOD)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.DARK_OAK_WOOD)));
			}

			@Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

			@Override
			public String getInventoryTitle() {
				return "&cCoal Generator";
			}

			@Override
			public int getEnergyProduction() {
				return 8;
			}

		}.registerUnrechargeableBlock(true, 64);

		new AGenerator(Categories.ELECTRICITY, SlimefunItems.BIO_REACTOR, "BIO_REACTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HEATING_COIL, SlimefunItems.COMPOSTER, SlimefunItems.HEATING_COIL, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT, null, SlimefunItems.ALUMINUM_BRASS_INGOT, null}) {

			@Override
			public void registerDefaultRecipes() {
				registerFuel(new MachineFuel(2, new ItemStack(Material.ROTTEN_FLESH)));
				registerFuel(new MachineFuel(2, new ItemStack(Material.SPIDER_EYE)));
				registerFuel(new MachineFuel(2, new ItemStack(Material.BONE)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.APPLE)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.MELON)));
				registerFuel(new MachineFuel(27, new ItemStack(Material.MELON)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.PUMPKIN)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.PUMPKIN_SEEDS)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.MELON_SEEDS)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.WHEAT)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.WHEAT_SEEDS)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.CARROT)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.POTATO)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.SUGAR_CANE)));
				registerFuel(new MachineFuel(3, new ItemStack(Material.NETHER_WART)));
				registerFuel(new MachineFuel(2, new ItemStack(Material.RED_MUSHROOM)));
				registerFuel(new MachineFuel(2, new ItemStack(Material.BROWN_MUSHROOM)));
				registerFuel(new MachineFuel(2, new ItemStack(Material.VINE)));
				registerFuel(new MachineFuel(2, new ItemStack(Material.CACTUS)));
				registerFuel(new MachineFuel(2, new ItemStack(Material.LILY_PAD)));
				registerFuel(new MachineFuel(8, new ItemStack(Material.CHORUS_FRUIT)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.BAMBOO)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.KELP)));
				registerFuel(new MachineFuel(2, new ItemStack(Material.DRIED_KELP)));
				registerFuel(new MachineFuel(20, new ItemStack(Material.DRIED_KELP_BLOCK)));
				registerFuel(new MachineFuel(1, new ItemStack(Material.SEAGRASS)));
				registerFuel(new MachineFuel(2, new ItemStack(Material.SEA_PICKLE)));

				// Leaves
				for(Material m:Tag.LEAVES.getValues())
						registerFuel(new MachineFuel(1, new ItemStack(m)));

				// Saplings
				for (Material m:Tag.SAPLINGS.getValues())
						registerFuel(new MachineFuel(1, new ItemStack(m)));
				
				// Small Flowers (formally just dandelions and poppies.
				for(Material m:Tag.SMALL_FLOWERS.getValues())
					registerFuel(new MachineFuel(1, new ItemStack(m)));
			}

			@Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.GOLDEN_HOE);
			}

			@Override
			public String getInventoryTitle() {
				return "&2Bio Reactor";
			}

			@Override
			public int getEnergyProduction() {
				return 4;
			}

		}.registerUnrechargeableBlock(true, 128);

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

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_TRANSMITTER, "GPS_TRANSMITTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.STEEL_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT})
		.registerChargeableBlock(true, 16, new BlockTicker() {

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				int charge = ChargableBlock.getCharge(b);
				if (charge > 0) {
					Slimefun.getGPSNetwork().updateTransmitter(b, UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.ONLINE);
					ChargableBlock.setCharge(b, charge - 1);
				}
				else Slimefun.getGPSNetwork().updateTransmitter(b, UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.OFFLINE);
			}

			@Override
			public void uniqueTick() {
			}

			@Override
			public boolean isSynchronized() {
				return false;
			}
		});

		SlimefunItem.registerBlockHandler("GPS_TRANSMITTER", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				Slimefun.getGPSNetwork().updateTransmitter(b, UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.OFFLINE);
				return true;
			}
		});

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_TRANSMITTER_2, "GPS_TRANSMITTER_2", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.CARBON, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER, SlimefunItems.BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER})
		.registerChargeableBlock(true, 64, new BlockTicker() {

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				int charge = ChargableBlock.getCharge(b);
				if (charge > 2) {
					Slimefun.getGPSNetwork().updateTransmitter(new Location(b.getWorld(), b.getX(), b.getY() * 4 + 100, b.getZ()).getBlock(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.ONLINE);
					ChargableBlock.setCharge(b, charge - 3);
				}
				else {
					Slimefun.getGPSNetwork().updateTransmitter(new Location(b.getWorld(), b.getX(), b.getY() * 4 + 100, b.getZ()).getBlock(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.OFFLINE);
				}
			}

			@Override
			public void uniqueTick() {
			}

			@Override
			public boolean isSynchronized() {
				return false;
			}
		});

		SlimefunItem.registerBlockHandler("GPS_TRANSMITTER_2", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				Slimefun.getGPSNetwork().updateTransmitter(new Location(b.getWorld(), b.getX(), b.getY() * 4 + 100, b.getZ()).getBlock(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.OFFLINE);
				return true;
			}
		});

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_TRANSMITTER_3, "GPS_TRANSMITTER_3", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.CARBONADO, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.GPS_TRANSMITTER_2})
		.registerChargeableBlock(true, 256, new BlockTicker() {

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				int charge = ChargableBlock.getCharge(b);
				if (charge > 10) {
					Slimefun.getGPSNetwork().updateTransmitter(new Location(b.getWorld(), b.getX(), b.getY() * 16 + 500, b.getZ()).getBlock(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.ONLINE);
					ChargableBlock.setCharge(b, charge - 11);
				}
				else {
					Slimefun.getGPSNetwork().updateTransmitter(new Location(b.getWorld(), b.getX(), b.getY() * 16 + 500, b.getZ()).getBlock(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.OFFLINE);
				}
			}

			@Override
			public void uniqueTick() {
			}

			@Override
			public boolean isSynchronized() {
				return false;
			}
		});

		SlimefunItem.registerBlockHandler("GPS_TRANSMITTER_3", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				Slimefun.getGPSNetwork().updateTransmitter(new Location(b.getWorld(), b.getX(), b.getY() * 16 + 500, b.getZ()).getBlock(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.OFFLINE);
				return true;
			}
		});

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_TRANSMITTER_4, "GPS_TRANSMITTER_4", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.NICKEL_INGOT, SlimefunItems.CARBONADO, SlimefunItems.NICKEL_INGOT, SlimefunItems.GPS_TRANSMITTER_3, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GPS_TRANSMITTER_3})
		.registerChargeableBlock(true, 1024, new BlockTicker() {

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				int charge = ChargableBlock.getCharge(b);
				if (charge > 45) {
					Slimefun.getGPSNetwork().updateTransmitter(new Location(b.getWorld(), b.getX(), b.getY() * 64 + 800, b.getZ()).getBlock(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.ONLINE);
					ChargableBlock.setCharge(b, charge - 46);
				}
				else {
					Slimefun.getGPSNetwork().updateTransmitter(new Location(b.getWorld(), b.getX(), b.getY() * 64 + 800, b.getZ()).getBlock(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.OFFLINE);
				}
			}

			@Override
			public void uniqueTick() {
			}

			@Override
			public boolean isSynchronized() {
				return false;
			}
		});

		SlimefunItem.registerBlockHandler("GPS_TRANSMITTER_4", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				Slimefun.getGPSNetwork().updateTransmitter(new Location(b.getWorld(), b.getX(), b.getY() * 64 + 800, b.getZ()).getBlock(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.OFFLINE);
				return true;
			}
		});

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_CONTROL_PANEL, "GPS_CONTROL_PANEL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.COBALT_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.COBALT_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				SlimefunItem item = BlockStorage.check(e.getClickedBlock());
				if (item == null || !item.getID().equals("GPS_CONTROL_PANEL")) return false;
				e.setCancelled(true);
				try {
					Slimefun.getGPSNetwork().openTransmitterControlPanel(p);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				return true;
			}
		});

		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_MARKER_TOOL, "GPS_MARKER_TOOL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, new ItemStack(Material.LAPIS_LAZULI), SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.LAPIS_LAZULI), new ItemStack(Material.REDSTONE), SlimefunItems.REDSTONE_ALLOY, new ItemStack(Material.REDSTONE)})
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
		new ItemStack[] {new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_EYE), SlimefunItems.BLANK_RUNE, new ItemStack(Material.ENDER_EYE) ,new ItemStack(Material.ENDER_PEARL), SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.ENDER_PEARL)}, new CustomItem(SlimefunItems.RUNE_ENDER, 6))
		.register(true);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.RUNE_RAINBOW, "ANCIENT_RUNE_RAINBOW", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.RED_DYE), SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.CYAN_DYE), new ItemStack(Material.WHITE_WOOL), SlimefunItems.RUNE_ENDER, new ItemStack(Material.WHITE_WOOL) , new ItemStack(Material.YELLOW_DYE), SlimefunItems.ENDER_LUMP_3, new ItemStack(Material.MAGENTA_DYE)})
		.register(true);

		new SlimefunItem(Categories.MAGIC, SlimefunItems.INFERNAL_BONEMEAL, "INFERNAL_BONEMEAL", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.NETHER_WART), SlimefunItems.RUNE_EARTH, new ItemStack(Material.NETHER_WART), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.BONE_MEAL), SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.NETHER_WART), new ItemStack(Material.BLAZE_POWDER), new ItemStack(Material.NETHER_WART)}, new CustomItem(SlimefunItems.INFERNAL_BONEMEAL, 8))
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(e.getItem(), SlimefunItems.INFERNAL_BONEMEAL, true)) {
					if (e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.NETHER_WART)) {
						Ageable ageable = (Ageable)e.getClickedBlock().getBlockData();
						if (ageable.getAge() < ageable.getMaximumAge()) {
							ageable.setAge(ageable.getMaximumAge());
							e.getClickedBlock().setBlockData(ageable);
							e.getClickedBlock().getWorld().playEffect(e.getClickedBlock().getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
							PlayerInventory.consumeItemInHand(p);
						}
					}
					return true;
				}
				return false;
			}
		});

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


		new SlimefunItem(Categories.GPS, SlimefunItems.GPS_GEO_SCANNER, "GPS_GEO_SCANNER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, SlimefunItems.ELECTRO_MAGNET, null, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRO_MAGNET})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				SlimefunItem item = BlockStorage.check(e.getClickedBlock());
				if (item == null || !item.getID().equals("GPS_GEO_SCANNER")) return false;
				e.setCancelled(true);
				try {
					Slimefun.getGPSNetwork().scanChunk(p, e.getClickedBlock().getChunk());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				return true;
			}
		});

		new OilPump(Categories.GPS, SlimefunItems.OIL_PUMP, "OIL_PUMP", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT, null, new ItemStack(Material.BUCKET), null}) {

			@Override
			public int getEnergyConsumption() {
				return 14;
			}

			@Override
			public int getSpeed() {
				return 1;
			};

		}.registerChargeableBlock(true, 200);

		new NetherDrill(Categories.GPS, SlimefunItems.NETHER_DRILL, "NETHER_DRILL", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[]{SlimefunItems.LEAD_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.LEAD_INGOT, SlimefunItems.REINFORCED_PLATE, SlimefunItems.OIL_PUMP, SlimefunItems.REINFORCED_PLATE, SlimefunItems.LEAD_INGOT, SlimefunItems.BIG_CAPACITOR, SlimefunItems.LEAD_INGOT}){
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

		new SlimefunItem(Categories.RESOURCES, SlimefunItems.NETHER_ICE, "NETHER_ICE", new RecipeType(SlimefunItems.NETHER_DRILL),
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
			};

		}.registerChargeableBlock(true, 256);

		new AGenerator(Categories.ELECTRICITY, SlimefunItems.LAVA_GENERATOR, "LAVA_GENERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.GOLD_16K, null, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HEATING_COIL}) {

			@Override
			public void registerDefaultRecipes() {
				registerFuel(new MachineFuel(40, new ItemStack(Material.LAVA_BUCKET)));
			}

			@Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

			@Override
			public String getInventoryTitle() {
				return "&4Lava Generator";
			}

			@Override
			public int getEnergyProduction() {
				return 10;
			}

		}.registerUnrechargeableBlock(true, 512);

		new AGenerator(Categories.ELECTRICITY, SlimefunItems.COMBUSTION_REACTOR, "COMBUSTION_REACTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.STEEL_INGOT, null, SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.STEEL_INGOT, SlimefunItems.HEATING_COIL}) {

			@Override
			public void registerDefaultRecipes() {
				registerFuel(new MachineFuel(30, SlimefunItems.BUCKET_OF_OIL));
				registerFuel(new MachineFuel(90, SlimefunItems.BUCKET_OF_FUEL));
			}

			@Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.FLINT_AND_STEEL);
			}

			@Override
			public String getInventoryTitle() {
				return "&cCombustion Reactor";
			}

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
			public void onInteract(final Player p, final Block b) throws Exception {
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

		new SlimefunItem(Categories.TECH, SlimefunItems.HOLOGRAM_PROJECTOR, "HOLOGRAM_PROJECTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.POWER_CRYSTAL, null, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT, null, SlimefunItems.ALUMINUM_BRASS_INGOT, null}, new CustomItem(SlimefunItems.HOLOGRAM_PROJECTOR, 3))
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				SlimefunItem item = BlockStorage.check(e.getClickedBlock());
				if (item == null || !item.getID().equals("HOLOGRAM_PROJECTOR")) return false;
				e.setCancelled(true);

				if (BlockStorage.getLocationInfo(e.getClickedBlock().getLocation(), "owner").equals(p.getUniqueId().toString())) {
					Projector.openEditor(p, e.getClickedBlock());
				}

				return true;
			}
		});

		SlimefunItem.registerBlockHandler("HOLOGRAM_PROJECTOR", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "text", "&bHi, I am a Hologram, &3configure me using the Projector");
				BlockStorage.addBlockInfo(b, "offset", "-0.5");
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());

				Projector.getArmorStand(b);
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				Projector.getArmorStand(b).remove();
				return true;
			}
		});

		new SlimefunItem(Categories.MAGIC, SlimefunItems.INFUSED_HOPPER, "INFUSED_HOPPER", RecipeType.ANCIENT_ALTAR,
		new ItemStack[] {new ItemStack(Material.OBSIDIAN), SlimefunItems.RUNE_EARTH, new ItemStack(Material.HOPPER), SlimefunItems.RUNE_ENDER, SlimefunItems.INFUSED_MAGNET, SlimefunItems.RUNE_ENDER, new ItemStack(Material.HOPPER), SlimefunItems.RUNE_EARTH, new ItemStack(Material.OBSIDIAN)})
		.register(true, new BlockTicker() {

			@Override
			public void uniqueTick() {
			}

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				if (b.getType() != Material.HOPPER) {
					// we're no longer a hopper, we were probably destroyed. skipping this tick.
					return;
				}
				ArmorStand hologram = InfusedHopper.getArmorStand(b, true);
				boolean sound = false;
				for (Entity n: hologram.getNearbyEntities(3.5D, 3.5D, 3.5D)) {
					if (n instanceof Item && !n.hasMetadata("no_pickup") && n.getLocation().distance(hologram.getLocation()) > 0.4D) {
						n.setVelocity(new Vector(0, 0.1, 0));
						n.teleport(hologram);
						sound = true;
					}
				}
				if (sound) b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5F, 2F);
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		});

		SlimefunItem.registerBlockHandler("INFUSED_HOPPER", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				InfusedHopper.getArmorStand(b, true);
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				final ArmorStand hologram = InfusedHopper.getArmorStand(b, false);
				if (hologram != null) {
					hologram.remove();
				}
				return true;
			}
		});

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

		new SlimefunItem(Categories.GPS, SlimefunItems.ELEVATOR, "ELEVATOR_PLATE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.STONE_PRESSURE_PLATE), null, new ItemStack(Material.PISTON), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.PISTON), SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ALUMINUM_BRONZE_INGOT},
		new CustomItem(SlimefunItems.ELEVATOR, 2))
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(final ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				SlimefunItem item = BlockStorage.check(e.getClickedBlock());
				if (item == null) return false;
				if (!item.getID().equals("ELEVATOR_PLATE")) return false;

				if (BlockStorage.getLocationInfo(e.getClickedBlock().getLocation(), "owner").equals(p.getUniqueId().toString())) Elevator.openEditor(p, e.getClickedBlock());
				return true;
			}
		});

		SlimefunItem.registerBlockHandler("ELEVATOR_PLATE", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "floor", "&rFloor #0");
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				return true;
			}
		});

		new FoodFabricator(Categories.ELECTRICITY, SlimefunItems.FOOD_FABRICATOR, "FOOD_FABRICATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BILLON_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.CAN, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.CAN, null, SlimefunItems.ELECTRIC_MOTOR, null}) {

			@Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.GOLDEN_HOE);
			}

			@Override
			public String getInventoryTitle() {
				return "&cFood Fabricator";
			}

			@Override
			public int getEnergyConsumption() {
				return 7;
			}

			@Override
			public int getSpeed() {
				return 1;
			};

		}.registerChargeableBlock(true, 256);

		new FoodFabricator(Categories.ELECTRICITY, SlimefunItems.FOOD_FABRICATOR_2, "FOOD_FABRICATOR_2", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.FOOD_FABRICATOR, SlimefunItems.ELECTRIC_MOTOR, null, SlimefunItems.ELECTRO_MAGNET, null}) {

			@Override
			public ItemStack getProgressBar() {
				return new ItemStack(Material.DIAMOND_HOE);
			}

			@Override
			public String getInventoryTitle() {
				return "&cFood Fabricator";
			}

			@Override
			public int getEnergyConsumption() {
				return 24;
			}

			@Override
			public int getSpeed() {
				return 6;
			};

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.GOLDEN_HOE);
			}

			@Override
			public String getInventoryTitle() {
				return "&cFood Composter";
			}

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.DIAMOND_HOE);
			}

			@Override
			public String getInventoryTitle() {
				return "&cFood Composter";
			}

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
		new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.PROGRAMMABLE_ANDROID_FARMER, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ANIMAL_GROWTH_ACCELERATOR, SlimefunItems.ELECTRO_MAGNET})
		{

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
		new ItemStack[] {null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CROP_GROWTH_ACCELERATOR, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.ELECTRO_MAGNET})
		{

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.GOLDEN_PICKAXE);
			}

			@Override
			public String getInventoryTitle() {
				return "&bFreezer";
			}

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.DIAMOND_PICKAXE);
			}

			@Override
			public String getInventoryTitle() {
				return "&bFreezer";
			}

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
		new ItemStack[] {new ItemStack(Material.PACKED_ICE), null, null, null, null, null, null, null, null})
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
			public void extraTick(Location l) {

			}

			@Override
			public ItemStack getProgressBar() {
				try {
					return CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNhZDhlZTg0OWVkZjA0ZWQ5YTI2Y2EzMzQxZjYwMzNiZDc2ZGNjNDIzMWVkMWVhNjNiNzU2NTc1MWIyN2FjIn19fQ==");
				} catch (Exception e) {
					return new ItemStack(Material.BLAZE_POWDER);
				}
			}

            @Override
            public ItemStack getCoolant() {
                return SlimefunItems.REACTOR_COOLANT_CELL;
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
				Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
					for (Entity entity : ReactorHologram.getArmorStand(l).getNearbyEntities(5, 5, 5)) {
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

		new SlimefunItem(Categories.CARGO, SlimefunItems.CARGO_MANAGER, "CARGO_MANAGER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, SlimefunItems.HOLOGRAM_PROJECTOR, null, SlimefunItems.REINFORCED_PLATE, SlimefunItems.CARGO_MOTOR, SlimefunItems.REINFORCED_PLATE, SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.ALUMINUM_BRONZE_INGOT})
		.register(true, new BlockTicker() {

			@Override
			public void uniqueTick() {
			}

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				CargoNet.getNetworkFromLocationOrCreate(b.getLocation()).tick(b);
			}

			@Override
			public boolean isSynchronized() {
				return false;
			}
		}, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				SlimefunItem item = BlockStorage.check(e.getClickedBlock());
				if (item == null || !item.getID().equals("CARGO_MANAGER")) return false;
				e.setCancelled(true);

				if (BlockStorage.getLocationInfo(e.getClickedBlock().getLocation(), "visualizer") == null) {
					BlockStorage.addBlockInfo(e.getClickedBlock(), "visualizer", "disabled");
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCargo Net Visualizer: " + "&4\u2718"));
				}
				else {
					BlockStorage.addBlockInfo(e.getClickedBlock(), "visualizer", null);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCargo Net Visualizer: " + "&2\u2714"));
				}
				return true;
			}
		});

		SlimefunItem.registerBlockHandler("CARGO_MANAGER", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				CargoHologram.remove(b);
				return true;
			}
		});

		new SlimefunItem(Categories.CARGO, SlimefunItems.CARGO_NODE, "CARGO_NODE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.BRONZE_INGOT}, new CustomItem(SlimefunItems.CARGO_NODE, 4))
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(final ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				SlimefunItem item = BlockStorage.check(e.getClickedBlock());
				if (item == null) return false;
				if (!item.getID().equals("CARGO_NODE")) return false;

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.DIAMOND_PICKAXE);
			}

			@Override
			public String getInventoryTitle() {
				return "&cCarbon Press";
			}

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.DIAMOND_PICKAXE);
			}

			@Override
			public String getInventoryTitle() {
				return "&cCarbon Press";
			}

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
			public ItemStack getProgressBar() {
				return new ItemStack(Material.DIAMOND_PICKAXE);
			}

			@Override
			public String getInventoryTitle() {
				return "&cCarbon Press";
			}

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
			public void registerDefaultRecipes() {
			}

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
			public void registerDefaultRecipes() {
			}

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
		MiscSetup.post_handlers.add(handler);
	}

}
