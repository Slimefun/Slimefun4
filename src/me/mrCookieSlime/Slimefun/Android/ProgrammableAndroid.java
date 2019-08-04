package me.mrCookieSlime.Slimefun.Android;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.compatibility.MaterialHelper;
import me.mrCookieSlime.CSCoreLibPlugin.general.Block.TreeCalculator;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.MenuHelper;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.ExoticGarden.ExoticGarden;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Android.ScriptComparators.ScriptReputationSorter;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.holograms.AndroidStatusHologram;

public abstract class ProgrammableAndroid extends SlimefunItem {

	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 18, 24, 25, 26, 27, 33, 35, 36, 42, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
	private static final int[] border_out = {10, 11, 12, 13, 14, 19, 23, 28, 32, 37, 38, 39, 40, 41};

	private static final ItemStack[] fish = new ItemStack[] {new ItemStack(Material.COD), new ItemStack(Material.SALMON), new ItemStack(Material.PUFFERFISH), new ItemStack(Material.TROPICAL_FISH), new ItemStack(Material.STRING), new ItemStack(Material.BONE), new ItemStack(Material.STICK)};

	private static final List<BlockFace> directions = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
	private static final List<Material> blockblacklist = new ArrayList<>();


	static {
		blockblacklist.add(Material.BEDROCK);
		blockblacklist.add(Material.BARRIER);
		blockblacklist.add(Material.END_PORTAL_FRAME);
		blockblacklist.add(Material.COMMAND_BLOCK);
		blockblacklist.add(Material.CHAIN_COMMAND_BLOCK);
		blockblacklist.add(Material.REPEATING_COMMAND_BLOCK);
		blockblacklist.add(Material.STRUCTURE_BLOCK);
	}

	private Set<MachineFuel> recipes = new HashSet<>();

	public String getInventoryTitle() {
		return "Programmable Android";
	}

	public int[] getOutputSlots() {
		return new int[] {20, 21, 22, 29, 30, 31};
	}

	public abstract AndroidType getAndroidType();
	public abstract float getFuelEfficiency();
	public abstract int getTier();

	public ProgrammableAndroid(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);

		if (getTier() == 1) {
			registerFuel(new MachineFuel(80, new ItemStack(Material.COAL)));
			registerFuel(new MachineFuel(80, new ItemStack(Material.CHARCOAL)));
			registerFuel(new MachineFuel(800, new ItemStack(Material.COAL_BLOCK)));
			registerFuel(new MachineFuel(45, new ItemStack(Material.BLAZE_ROD)));

			// Logs
			registerFuel(new MachineFuel(4, new ItemStack(Material.OAK_LOG)));
			registerFuel(new MachineFuel(4, new ItemStack(Material.BIRCH_LOG)));
			registerFuel(new MachineFuel(4, new ItemStack(Material.SPRUCE_LOG)));
			registerFuel(new MachineFuel(4, new ItemStack(Material.JUNGLE_LOG)));
			registerFuel(new MachineFuel(4, new ItemStack(Material.DARK_OAK_LOG)));
			registerFuel(new MachineFuel(4, new ItemStack(Material.ACACIA_LOG)));

			// Wooden Planks
			registerFuel(new MachineFuel(1, new ItemStack(Material.OAK_PLANKS)));
			registerFuel(new MachineFuel(1, new ItemStack(Material.BIRCH_PLANKS)));
			registerFuel(new MachineFuel(1, new ItemStack(Material.SPRUCE_PLANKS)));
			registerFuel(new MachineFuel(1, new ItemStack(Material.JUNGLE_PLANKS)));
			registerFuel(new MachineFuel(1, new ItemStack(Material.DARK_OAK_PLANKS)));
			registerFuel(new MachineFuel(1, new ItemStack(Material.ACACIA_PLANKS)));
		}
		else if (getTier() == 2){
			registerFuel(new MachineFuel(100, new ItemStack(Material.LAVA_BUCKET)));
			registerFuel(new MachineFuel(200, SlimefunItems.BUCKET_OF_OIL));
			registerFuel(new MachineFuel(500, SlimefunItems.BUCKET_OF_FUEL));
		}
		else {
			registerFuel(new MachineFuel(2500, SlimefunItems.URANIUM));
			registerFuel(new MachineFuel(1200, SlimefunItems.NEPTUNIUM));
			registerFuel(new MachineFuel(3000, SlimefunItems.BOOSTED_URANIUM));
		}

		new BlockMenuPreset(name, getInventoryTitle()) {

			@Override
			public void init() {
				try {
					constructMenu(this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public boolean canOpen(Block b, Player p) {
				boolean open = BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString()) || p.hasPermission("slimefun.android.bypass");
				if (!open) {
					Messages.local.sendTranslation(p, "inventory.no-access", true);
				}
				return open;
			}

			@Override
			public void newInstance(BlockMenu menu, final Block b) {
				try {
					menu.replaceExistingItem(15, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTAxYzdiNTcyNjE3ODk3NGIzYjNhMDFiNDJhNTkwZTU0MzY2MDI2ZmQ0MzgwOGYyYTc4NzY0ODg0M2E3ZjVhIn19fQ=="), "&aStart/Continue"));
					menu.addMenuClickHandler(15, (p, slot, item, action) -> {
						Messages.local.sendTranslation(p, "robot.started", true);
						BlockStorage.addBlockInfo(b, "paused", "false");
						p.closeInventory();
						return false;
					});

					menu.replaceExistingItem(17, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYxMzlmZDFjNTY1NGU1NmU5ZTRlMmM4YmU3ZWIyYmQ1YjQ5OWQ2MzM2MTY2NjNmZWVlOTliNzQzNTJhZDY0In19fQ=="), "&4Pause"));
					menu.addMenuClickHandler(17, (p, slot, item, action) -> {
						BlockStorage.addBlockInfo(b, "paused", "true");
						Messages.local.sendTranslation(p, "robot.stopped", true);
						return false;
					});

					menu.replaceExistingItem(16, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc4ZjJiN2U1ZTc1NjM5ZWE3ZmI3OTZjMzVkMzY0YzRkZjI4YjQyNDNlNjZiNzYyNzdhYWRjZDYyNjEzMzcifX19"), "&bMemory Core", "", "&8\u21E8 &7Click to open the Script Editor"));
					menu.addMenuClickHandler(16, (p, slot, item, action) -> {
						try {
							BlockStorage.addBlockInfo(b, "paused", "true");
							Messages.local.sendTranslation(p, "robot.stopped", true);
							openScriptEditor(p, b);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return false;
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				return new int[0];
			}
		};

		registerBlockHandler(name, new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
				BlockStorage.addBlockInfo(b, "script", "START-TURN_LEFT-REPEAT");
				BlockStorage.addBlockInfo(b, "index", "0");
				BlockStorage.addBlockInfo(b, "fuel", "0");
				BlockStorage.addBlockInfo(b, "rotation", p.getFacing().getOppositeFace().toString());
				BlockStorage.addBlockInfo(b, "paused", "true");
				b.setType(Material.PLAYER_HEAD);
				Rotatable blockData = (Rotatable) b.getBlockData();
				blockData.setRotation(p.getFacing());
				b.setBlockData(blockData);
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				boolean allow =  reason.equals(UnregisterReason.PLAYER_BREAK) && (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString()) || p.hasPermission("slimefun.android.bypass"));

				if (BlockStorage.hasBlockInfo(b)) return false;

				if (allow) {
					BlockMenu inv = BlockStorage.getInventory(b);
					if (inv != null) {
						if (inv.getItemInSlot(43) != null) {
							b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(43));
							inv.replaceExistingItem(43, null);
						}
						for (int slot: getOutputSlots()) {
							if (inv.getItemInSlot(slot) != null) {
								b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
								inv.replaceExistingItem(slot, null);
							}
						}
					}
					AndroidStatusHologram.remove(b);
				}

				return allow;
			}
		});
	}

	protected void tick(Block b) {
		try {
			if (!(b.getState() instanceof Skull)) {
				return;
			}
		} catch (NullPointerException x) {
			return;
		}

		if (BlockStorage.getLocationInfo(b.getLocation(), "paused").equals("false")) {
			float fuel = Float.parseFloat(BlockStorage.getLocationInfo(b.getLocation(), "fuel"));
			if (fuel == 0) {
				ItemStack item = BlockStorage.getInventory(b).getItemInSlot(43);
				if (item != null) {
					for (MachineFuel recipe: recipes) {
						if (SlimefunManager.isItemSimiliar(item, recipe.getInput(), true)) {
							BlockStorage.getInventory(b).replaceExistingItem(43, InvUtils.decreaseItem(item, 1));
							if (getTier() == 2) {
								pushItems(b, new ItemStack(Material.BUCKET));
							}
							BlockStorage.addBlockInfo(b, "fuel", String.valueOf((int) (recipe.getTicks() * this.getFuelEfficiency())));
							break;
						}
					}
				}
			}
			else {
				String[] script = BlockStorage.getLocationInfo(b.getLocation(), "script").split("-");
				int index = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "index")) + 1;
				if (index >= script.length) index = 0;
				boolean refresh = true;
				BlockStorage.addBlockInfo(b, "fuel", String.valueOf(fuel - 1));
				ScriptPart part = ScriptPart.valueOf(script[index]);

				if (getAndroidType().isType(part.getRequiredType())) {
					switch (part) {
						case GO_DOWN: {
							try {
								BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
								Block block = b.getRelative(BlockFace.DOWN);
								move(b, face, block);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						}
						case GO_FORWARD: {
							try {
								BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
								Block block = b.getRelative(face);
								move(b, face, block);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						}
						case GO_UP: {
							try {
								BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
								Block block = b.getRelative(BlockFace.UP);
								move(b, face, block);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						}
						case REPEAT: {
							BlockStorage.addBlockInfo(b, "index", String.valueOf(0));
							break;
						}
						case TURN_LEFT: {
							int rotIndex = directions.indexOf(BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) - 1;
							if (rotIndex < 0) rotIndex = directions.size() - 1;
							BlockFace dir = directions.get(rotIndex);
							Rotatable blockData = (Rotatable) b.getBlockData();
							blockData.setRotation(dir);
							b.setBlockData(blockData);
							BlockStorage.addBlockInfo(b, "rotation", dir.toString());
							break;
						}
						case TURN_RIGHT: {
							int rotIndex = directions.indexOf(BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) + 1;
							if (rotIndex == directions.size()) rotIndex = 0;
							BlockFace dir = directions.get(rotIndex);
							Rotatable blockData = (Rotatable) b.getBlockData();
							blockData.setRotation(dir);
							b.setBlockData(blockData);
							BlockStorage.addBlockInfo(b, "rotation", dir.toString());
							break;
						}
						case DIG_FORWARD: {
							Block block = b.getRelative(BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation")));
							mine(b, block);
							break;
						}
						case DIG_UP: {
							Block block = b.getRelative(BlockFace.UP);
							mine(b, block);
							break;
						}
						case DIG_DOWN: {
							Block block = b.getRelative(BlockFace.DOWN);
							mine(b, block);
							break;
						}
						case CATCH_FISH: {
							Block block = b.getRelative(BlockFace.DOWN);
							if (block.getType().equals(Material.WATER)) {
								block.getWorld().playSound(block.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);
								if (CSCoreLib.randomizer().nextInt(100) < 10 * getTier()) {
									ItemStack drop = fish[CSCoreLib.randomizer().nextInt(fish.length)];
									if (fits(b, drop)) pushItems(b, drop);
								}

							}
							break;
						}
						case MOVE_AND_DIG_FORWARD: {
							BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
							Block block = b.getRelative(face);
							movedig(b, face, block);
							break;
						}
						case MOVE_AND_DIG_UP: {
							BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
							Block block = b.getRelative(BlockFace.UP);
							movedig(b, face, block);
							break;
						}
						case MOVE_AND_DIG_DOWN: {
							BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
							Block block = b.getRelative(BlockFace.DOWN);
							movedig(b, face, block);
							break;
						}
						case INTERFACE_ITEMS: {
							BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
							Block block = b.getRelative(face);
							if (BlockStorage.check(block, "ANDROID_INTERFACE_ITEMS") && block.getState() instanceof Dispenser) {
								Dispenser d = (Dispenser) block.getState();
								for (int slot: getOutputSlots()) {
									ItemStack stack = BlockStorage.getInventory(b).getItemInSlot(slot);
									if (stack != null) {
										Map<Integer, ItemStack> items = d.getInventory().addItem(stack);
										if (items.isEmpty()) BlockStorage.getInventory(b).replaceExistingItem(slot, null);
										else {
											for (Map.Entry<Integer, ItemStack> entry: items.entrySet()) {
												BlockStorage.getInventory(b).replaceExistingItem(slot, entry.getValue());
												break;
											}
										}
									}
								}
							}
							break;
						}
						case INTERFACE_FUEL: {
							BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
							Block block = b.getRelative(face);
							if (BlockStorage.check(block, "ANDROID_INTERFACE_FUEL") && block.getState() instanceof Dispenser) {
								Dispenser d = (Dispenser) block.getState();
								for (int slot = 0; slot < 9; slot++) {
									ItemStack item = d.getInventory().getItem(slot);
									if (item != null) {
										if (BlockStorage.getInventory(b).getItemInSlot(43) == null) {
											BlockStorage.getInventory(b).replaceExistingItem(43, item);
											d.getInventory().setItem(slot, null);
											break;
										}
										else if (SlimefunManager.isItemSimiliar(item, BlockStorage.getInventory(b).getItemInSlot(43), true)) {
											int rest = item.getType().getMaxStackSize() - BlockStorage.getInventory(b).getItemInSlot(43).getAmount();
											if (rest > 0) {
												int amt = item.getAmount() > rest ? rest: item.getAmount();
												BlockStorage.getInventory(b).replaceExistingItem(43, new CustomItem(item, BlockStorage.getInventory(b).getItemInSlot(43).getAmount() + amt));
												d.getInventory().setItem(slot, InvUtils.decreaseItem(item, amt));
											}
											break;
										}
									}
								}
							}
							break;
						}
						case FARM_FORWARD: {
							BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
							Block block = b.getRelative(face);
							farm(b, block);
							break;
						}
						case FARM_DOWN: {
							Block block = b.getRelative(BlockFace.DOWN);
							farm(b, block);
							break;
						}
						case FARM_EXOTIC_FORWARD: {
							BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
							Block block = b.getRelative(face);
							exoticFarm(b, block);
							break;
						}
						case FARM_EXOTIC_DOWN: {
							Block block = b.getRelative(BlockFace.DOWN);
							exoticFarm(b, block);
							break;
						}
						case CHOP_TREE: {
							BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
							Block block = b.getRelative(face);
							if (MaterialHelper.isLog( block.getType())) {
								List<Location> list = new ArrayList<Location>();
								list.add(block.getLocation());
								TreeCalculator.getTree(block.getLocation(), block.getLocation(), list);
								if (!list.isEmpty()) {
									refresh = false;
									Block log = list.get(list.size() - 1).getBlock();
									Collection<ItemStack> drops = log.getDrops();
									log.getWorld().playEffect(log.getLocation(), Effect.STEP_SOUND, log.getType());
									if (!drops.isEmpty() && CSCoreLib.getLib().getProtectionManager().canBuild(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), log)) {
										ItemStack[] items = drops.toArray(new ItemStack[drops.size()]);
										if (fits(b, items)) {
											pushItems(b, items);
											log.getWorld().playEffect(log.getLocation(), Effect.STEP_SOUND, log.getType());
											if (log.getY() == block.getY()) {
												log.setType(MaterialHelper.getSaplingFromLog(log.getType()));
											}
											else log.setType(Material.AIR);
										}

									}
								}
							}
							break;
						}
						case ATTACK_MOBS_ANIMALS: {
							double damage = getTier() < 2 ? 20D : 4D * getTier();

							entities:
							for (Entity n: AndroidStatusHologram.getNearbyEntities(b, 4D + getTier())) {
								switch (BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) {
									case NORTH: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getZ() < b.getZ()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case EAST: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getX() > b.getX()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case SOUTH: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getZ() > b.getZ()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case WEST: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getX() < b.getX()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									default:
										break;
								}
							}
							break;
						}
						case ATTACK_MOBS: {
							double damage = getTier() < 2 ? 20D : 4D * getTier();

							entities:
							for (Entity n: AndroidStatusHologram.getNearbyEntities(b, 4D + getTier())) {
								if (n instanceof Animals) continue;
								switch (BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) {
									case NORTH: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getZ() < b.getZ()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case EAST: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getX() > b.getX()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case SOUTH: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getZ() > b.getZ()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case WEST: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getX() < b.getX()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									default:
										break;
								}
							}
							break;
						}

						case ATTACK_ANIMALS: {
							double damage = getTier() < 2 ? 20D : 4D * getTier();

							entities:
							for (Entity n: AndroidStatusHologram.getNearbyEntities(b, 4D + getTier())) {
								if (n instanceof Monster) continue;
								switch (BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) {
									case NORTH: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getZ() < b.getZ()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case EAST: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getX() > b.getX()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case SOUTH: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getZ() > b.getZ()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case WEST: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getX() < b.getX()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									default:
										break;
								}
							}
							break;
						}

						case ATTACK_ANIMALS_ADULT: {
							double damage = getTier() < 2 ? 20D : 4D * getTier();

							entities:
							for (Entity n: AndroidStatusHologram.getNearbyEntities(b, 4D + getTier())) {
								if (n instanceof Monster) continue;
								if (n instanceof org.bukkit.entity.Ageable && !((org.bukkit.entity.Ageable) n).isAdult()) continue;
								switch (BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) {
									case NORTH: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getZ() < b.getZ()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case EAST: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getX() > b.getX()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case SOUTH: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getZ() > b.getZ()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									case WEST: {
										if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.getLocation().getX() < b.getX()) {
											if (n.hasMetadata("android_killer")) n.removeMetadata("android_killer", SlimefunStartup.instance);
											n.setMetadata("android_killer", new FixedMetadataValue(SlimefunStartup.instance, new AndroidObject(this, b)));

											((LivingEntity) n).damage(damage);
											break entities;
										}
										break;
									}
									default:
										break;
								}
							}
							break;
						}
						default:
							break;
					}
				}
				if (refresh) BlockStorage.addBlockInfo(b, "index", String.valueOf(index));
			}
		}
	}

	private void move(Block b, BlockFace face, Block block) throws Exception {
		if (block.getY() < 0 || block.getY() > block.getWorld().getMaxHeight())
			return;
		if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR) {
			block.setType(Material.PLAYER_HEAD);
			Rotatable blockData = (Rotatable) block.getBlockData();
			blockData.setRotation(face.getOppositeFace());
			block.setBlockData(blockData);
			CustomSkull.setSkull(block, CustomSkull.getTexture(getItem()));
			b.setType(Material.AIR);
			BlockStorage.moveBlockInfo(b.getLocation(), block.getLocation());
		}
	}

	private void mine(Block b, Block block) {
		Collection<ItemStack> drops = block.getDrops();
		if (!blockblacklist.contains(block.getType()) && !drops.isEmpty() && CSCoreLib.getLib().getProtectionManager().canBuild(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), block)) {
			SlimefunItem item = BlockStorage.check(block);

			if (item != null) {
				return;
                /*if (fits(b, item.getItem())) {
                    if (SlimefunItem.blockhandler.containsKey(item.getID())) {
                        if (SlimefunItem.blockhandler.get(item.getID()).onBreak(null, block, item, UnregisterReason.ANDROID_DIG)) {
                            pushItems(b, BlockStorage.retrieve(block));
                            if (SlimefunItem.blockhandler.containsKey(item.getID())) SlimefunItem.blockhandler.get(item.getID()).onBreak(null, block, item, UnregisterReason.ANDROID_DIG);
                            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                            block.setType(Material.AIR);
                        }
                    }
                }*/
			} else {
				ItemStack[] items = drops.toArray(new ItemStack[drops.size()]);
				if (fits(b, items)) {
					pushItems(b, items);
					block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
					block.setType(Material.AIR);
				}
			}
		}
	}


	private void movedig(Block b, BlockFace face, Block block) {
		Collection<ItemStack> drops = block.getDrops();
		if (!blockblacklist.contains(block.getType()) && !drops.isEmpty() && CSCoreLib.getLib().getProtectionManager().canBuild(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), block)) {
			try {
				SlimefunItem item = BlockStorage.check(block);
				if (item != null) {
					if (fits(b, item.getItem())) {
						if (SlimefunItem.blockhandler.containsKey(item.getID())) {
							if (SlimefunItem.blockhandler.get(item.getID()).onBreak(null, block, item, UnregisterReason.ANDROID_DIG)) {
								pushItems(b, BlockStorage.retrieve(block));
								block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
								block.setType(Material.PLAYER_HEAD);
								Rotatable blockData = (Rotatable) block.getBlockData();
								blockData.setRotation(face.getOppositeFace());
								block.setBlockData(blockData);
								CustomSkull.setSkull(block, CustomSkull.getTexture(getItem()));
								b.setType(Material.AIR);
								BlockStorage.moveBlockInfo(b.getLocation(), block.getLocation());
							}
						}
					}
				}
				else {
					ItemStack[] items = drops.toArray(new ItemStack[drops.size()]);
					if (fits(b, items)) {
						pushItems(b, items);
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
						block.setType(Material.PLAYER_HEAD);
						Rotatable blockData = (Rotatable) block.getBlockData();
						blockData.setRotation(face.getOppositeFace());
						block.setBlockData(blockData);
						CustomSkull.setSkull(block, CustomSkull.getTexture(getItem()));
						b.setType(Material.AIR);
						BlockStorage.moveBlockInfo(b.getLocation(), block.getLocation());
					}
				}
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		else {
			try {
				move(b, face, block);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isFullGrown(Block block){
		Ageable ageable = ((Ageable) block.getBlockData());
		return ageable.getAge() >= ageable.getMaximumAge();
	}

	private void farm(Block b, Block block) {
		switch (block.getType()) {
			case WHEAT: {
				if (isFullGrown(block)) {
					ItemStack drop = new ItemStack(Material.WHEAT, CSCoreLib.randomizer().nextInt(3) + 1);
					if (fits(b, drop)) {
						pushItems(b, drop);
						Ageable ageable = (Ageable) block.getBlockData();
						ageable.setAge(0);
						block.setBlockData(ageable);
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
					}
				}
				break;
			}
			case POTATOES: {
				if (isFullGrown(block)) {
					ItemStack drop = new ItemStack(Material.POTATO, CSCoreLib.randomizer().nextInt(3) + 1);
					if (fits(b, drop)) {
						pushItems(b, drop);
						Ageable ageable = (Ageable) block.getBlockData();
						ageable.setAge(0);
						block.setBlockData(ageable);
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
					}
				}
				break;
			}
			case CARROTS: {
				if (isFullGrown(block)) {
					ItemStack drop = new ItemStack(Material.CARROT, CSCoreLib.randomizer().nextInt(3) + 1);
					if (fits(b, drop)) {
						pushItems(b, drop);
						Ageable ageable = (Ageable) block.getBlockData();
						ageable.setAge(0);
						block.setBlockData(ageable);
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
					}
				}
				break;
			}
			case BEETROOTS: {
				if (isFullGrown(block)) {
					ItemStack drop = new ItemStack(Material.BEETROOT, CSCoreLib.randomizer().nextInt(3) + 1);
					if (fits(b, drop)) {
						pushItems(b, drop);
						Ageable ageable = (Ageable) block.getBlockData();
						ageable.setAge(0);
						block.setBlockData(ageable);
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
					}
				}
				break;
			}
			case COCOA: {
				if (isFullGrown(block)) {
					ItemStack drop = new ItemStack(Material.COCOA_BEANS, CSCoreLib.randomizer().nextInt(3) + 1);
					if (fits(b, drop)) {
						pushItems(b, drop);
						Ageable ageable = (Ageable) block.getBlockData();
						ageable.setAge(0);
						block.setBlockData(ageable);
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
					}
				}
				break;
			}
			case NETHER_WART: {
				if (isFullGrown(block)) {
					ItemStack drop = new ItemStack(Material.NETHER_WART, CSCoreLib.randomizer().nextInt(3) + 1);
					if (fits(b, drop)) {
						pushItems(b, drop);
						Ageable ageable = (Ageable) block.getBlockData();
						ageable.setAge(0);
						block.setBlockData(ageable);
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
					}
				}
				break;
			}
			default:
				break;
		}
	}

	private void exoticFarm(Block b, Block block) {
		farm(b, block);
		if (SlimefunStartup.instance.isExoticGardenInstalled()) {
			ItemStack drop = ExoticGarden.harvestPlant(block);
			if (drop != null && fits(b, drop)) {
				pushItems(b, drop);
				block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
			}
		}
	}

	private void constructMenu(BlockMenuPreset preset) throws Exception {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
					(p, slot, item, action) -> false
			);
		}
		for (int i : border_out) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "),
					(p, slot, item, action) -> false
			);
		}

		for (int i : getOutputSlots()) {
			preset.addMenuClickHandler(i, new AdvancedMenuClickHandler() {

				@Override
				public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
					return false;
				}

				@Override
				public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
					return cursor == null || cursor.getType() == null || cursor.getType() == Material.AIR;
				}
			});
		}

		if (getTier() == 1) {
			preset.addItem(34, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&8\u21E9 &cFuel Input &8\u21E9", "", "&rThis Android runs on solid Fuel", "&re.g. Coal, Wood, etc..."),
					(p, slot, item, action) -> false
			);
		}
		else if (getTier() == 2){
			preset.addItem(34, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&8\u21E9 &cFuel Input &8\u21E9", "", "&rThis Android runs on liquid Fuel", "&re.g. Lava, Oil, Fuel, etc..."),
					(p, slot, item, action) -> false
			);
		}
		else {
			preset.addItem(34, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&8\u21E9 &cFuel Input &8\u21E9", "", "&rThis Android runs on radioactive Fuel", "&re.g. Uranium, Neptunium or Boosted Uranium"),
					(p, slot, item, action) -> false
			);
		}
	}

	public void openScriptEditor(Player p, final Block b) throws Exception {
		ChestMenu menu = new ChestMenu("&eScript Editor");

		menu.addItem(1, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliZjZkYjRhZWRhOWQ4ODIyYjlmNzM2NTM4ZThjMThiOWE0ODQ0Zjg0ZWI0NTUwNGFkZmJmZWU4N2ViIn19fQ=="), "&2> Edit Script", "", "&aEdits your current Script"),
				(pl, slot, item, action) -> {
					try {
						openScript(pl, b, BlockStorage.getLocationInfo(b.getLocation(), "script"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return false;
				}
		);

		menu.addItem(3, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxZDg5NzljMTg3OGEwNTk4N2E3ZmFmMjFiNTZkMWI3NDRmOWQwNjhjNzRjZmZjZGUxZWExZWRhZDU4NTIifX19"), "&4> Create new Script", "", "&cDeletes your current Script", "&cand creates a blank one"),
				(pl, slot, item, action) -> {
					try {
						openScript(pl, b, "START-TURN_LEFT-REPEAT");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return false;
				}
		);

		menu.addItem(5, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzAxNTg2ZTM5ZjZmZmE2M2I0ZmIzMDFiNjVjYTdkYThhOTJmNzM1M2FhYWI4OWQzODg2NTc5MTI1ZGZiYWY5In19fQ=="), "&6> Download a Script", "", "&eDownload a Script from the Server", "&eYou can edit or simply use it"),
				(pl, slot, item, action) -> {
					try {
						openScriptDownloader(pl, b, 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return false;
				}
		);

		menu.addItem(8, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE4NWM5N2RiYjgzNTNkZTY1MjY5OGQyNGI2NDMyN2I3OTNhM2YzMmE5OGJlNjdiNzE5ZmJlZGFiMzVlIn19fQ=="), "&6> Back", "", "&7Return to the Android's interface"),
				(pl, slot, item, action) -> {
					try {
						BlockStorage.getInventory(b).open(p);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return false;
				}
		);

		menu.open(p);
	}

	public void openScript(final Player p, final Block b, final String script) throws Exception {
		ChestMenu menu = new ChestMenu("&eScript Editor");
		final String[] commands = script.split("-");

		menu.addItem(0, new CustomItem(ScriptPart.START.toItemStack(), ScriptPart.START.toItemStack().getItemMeta().getDisplayName(), "", "&7\u21E8 &eLeft Click &7to return to the Android's interface"));
		menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
			BlockStorage.getInventory(b).open(pl);
			return false;
		});

		for (int i = 1; i < commands.length; i++) {
			final int index = i;
			if (i == commands.length - 1) {
				int additional = commands.length == 54 ? 0: 1;

				if (additional == 1) {
					menu.addItem(i, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxZDg5NzljMTg3OGEwNTk4N2E3ZmFmMjFiNTZkMWI3NDRmOWQwNjhjNzRjZmZjZGUxZWExZWRhZDU4NTIifX19"), "&7> Add new Command"));
					menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
						try {
							openScriptComponentEditor(pl, b, script, index);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return false;
					});
				}

				menu.addItem(i + additional, new CustomItem(ScriptPart.REPEAT.toItemStack(), ScriptPart.REPEAT.toItemStack().getItemMeta().getDisplayName(), "", "&7\u21E8 &eLeft Click &7to return to the Android's interface"));
				menu.addMenuClickHandler(i + additional, (pl, slot, item, action) -> {
					BlockStorage.getInventory(b).open(pl);
					return false;
				});
			}
			else {
				ItemStack stack = ScriptPart.valueOf(commands[i]).toItemStack();
				menu.addItem(i, new CustomItem(stack, stack.getItemMeta().getDisplayName(), "", "&7\u21E8 &eLeft Click &7to edit", "&7\u21E8 &eRight Click &7to delete", "&7\u21E8 &eShift + Right Click &7to duplicate"));
				menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
					if (action.isRightClicked() && action.isShiftClicked()) {
						if (commands.length == 54) return false;

						int j = 0;
						StringBuilder builder = new StringBuilder("START-");
						for (String command : commands) {
							if (j > 0) {
								if (j == index) {
									builder.append(commands[j] + "-");
									builder.append(commands[j] + "-");
								}
								else if (j < commands.length - 1) builder.append(command + "-");
							}
							j++;
						}
						builder.append("REPEAT");
						BlockStorage.addBlockInfo(b, "script", builder.toString());

						try {
							openScript(pl, b, builder.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else if (action.isRightClicked()) {
						int j = 0;
						StringBuilder builder = new StringBuilder("START-");
						for (String command : commands) {
							if (j != index && j > 0 && j < commands.length - 1) builder.append(command + "-");
							j++;
						}
						builder.append("REPEAT");
						BlockStorage.addBlockInfo(b, "script", builder.toString());
						try {
							openScript(pl, b, builder.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else {
						try {
							openScriptComponentEditor(pl, b, script, index);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					return false;
				});
			}
		}

		menu.open(p);
	}

	private void openScriptDownloader(final Player p, final Block b, final int page) throws Exception {
		final ChestMenu menu = new ChestMenu("Android Scripts");

		menu.addMenuOpeningHandler(
				(pl) -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.7F, 0.7F)
		);

		List<Config> scripts = getUploadedScripts();

		int index = 0;
		final int pages = (scripts.size() / 45) + 1;

		for (int i = 45; i < 54; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i,
					(pl, slot, item, action) -> false
			);
		}

		menu.addItem(46, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r\u21E6 Previous Page", "", "&7(" + page + " / " + pages + ")"));
		menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
			int next = page - 1;
			if (next < 1) next = pages;
			if (next != page) {
				try {
					openScriptDownloader(pl, b, next);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		});

		menu.addItem(48, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTA1YTJjYWI4YjY4ZWE1N2UzYWY5OTJhMzZlNDdjOGZmOWFhODdjYzg3NzYyODE5NjZmOGMzY2YzMWEzOCJ9fX0="), "&eUpload a Script", "", "&6Click &7to upload your Android's Script", "&7to the Database"));
		menu.addMenuClickHandler(48, (pl, slot, item, action) -> {
			final String code = BlockStorage.getLocationInfo(b.getLocation(), "script");
			int num = 1;

			for (Config script: getUploadedScripts()) {
				if (script.getString("author").equals(pl.getUniqueId().toString())) num++;
				if (script.getString("code").equals(code)) {
					Messages.local.sendTranslation(pl, "android.scripts.already-uploaded", true);
					return false;
				}
			}

			final int id = num;

			pl.closeInventory();
			Messages.local.sendTranslation(pl, "android.scripts.enter-name", true);

			MenuHelper.awaitChatInput(pl, (player, message) -> {
				Config script = new Config("plugins/Slimefun/scripts/" + getAndroidType().toString() + "/" + p.getName() + " " + id + ".sfs");

				script.setValue("author", player.getUniqueId().toString());
				script.setValue("author_name", player.getName());
				script.setValue("name", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)));
				script.setValue("code", code);
				script.setValue("downloads", 0);
				script.setValue("android", getAndroidType().toString());
				script.setValue("rating.positive", new ArrayList<String>());
				script.setValue("rating.negative", new ArrayList<String>());
				script.save();

				try {
					Messages.local.sendTranslation(player, "android.uploaded", true);
					openScriptDownloader(player, b, page);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return false;
			});
			return false;
		});

		menu.addItem(50, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&rNext Page \u21E8", "", "&7(" + page + " / " + pages + ")"));
		menu.addMenuClickHandler(50, (pl, slot, item, action) -> {
			int next = page + 1;
			if (next > pages) next = 1;
			if (next != page) {
				try {
					openScriptDownloader(pl, b, next);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		});

		menu.addItem(53, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE4NWM5N2RiYjgzNTNkZTY1MjY5OGQyNGI2NDMyN2I3OTNhM2YzMmE5OGJlNjdiNzE5ZmJlZGFiMzVlIn19fQ=="), "&6> Back", "", "&7Return to the Android's interface"));
		menu.addMenuClickHandler(53, (pl, slot, item, action) -> {
			try {
				openScriptEditor(pl, b);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		});

		int category_index = 45 * (page - 1);
		for (int i = 0; i < 45; i++) {
			int target = category_index + i;
			if (target >= scripts.size()) break;
			else {
				final Config script = scripts.get(target);

				OfflinePlayer op = Bukkit.getOfflinePlayer(script.getUUID("author"));
				String author = (op != null && op.getName() != null) ? op.getName(): script.getString("author_name");

				if (script.getString("author").equals(p.getUniqueId().toString())) {
					menu.addItem(index, new CustomItem(this.getItem(), "&b" + script.getString("name"), "&7by &r" + author, "", "&7Downloads: &r" + script.getInt("downloads"), "&7Rating: " + getScriptRatingPercentage(script), "&a" + getScriptRating(script, true) + " \u263A &7- &4\u2639 " + getScriptRating(script, false), "", "&eLeft Click &rto download this Script", "&4(This will override your current Script)"));
				}
				else {
					menu.addItem(index, new CustomItem(this.getItem(), "&b" + script.getString("name"), "&7by &r" + author, "", "&7Downloads: &r" + script.getInt("downloads"), "&7Rating: " + getScriptRatingPercentage(script), "&a" + getScriptRating(script, true) + " \u263A &7- &4\u2639 " + getScriptRating(script, false), "", "&eLeft Click &rto download this Script", "&4(This will override your current Script)", "&eShift + Left Click &rto leave a positive Rating", "&eShift + Right Click &rto leave a negative Rating"));
				}

				menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
					Config script2 = new Config(script.getFile());

					if (action.isShiftClicked()) {
						if (script2.getString("author").equals(pl.getUniqueId().toString())) {
							Messages.local.sendTranslation(pl, "android.scripts.rating.own", true);
						}
						else if (action.isRightClicked()) {
							if (!script2.getStringList("rating.negative").contains(pl.getUniqueId().toString()) && !script2.getStringList("rating.positive").contains(pl.getUniqueId().toString())) {
								List<String> list = script2.getStringList("rating.negative");
								list.add(p.getUniqueId().toString());

								script2.setValue("rating.negative", list);
								script2.save();

								try {
									openScriptDownloader(pl, b, page);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							else {
								Messages.local.sendTranslation(pl, "android.scripts.rating.already", true);
							}
						}
						else {
							if (!script2.getStringList("rating.negative").contains(pl.getUniqueId().toString()) && !script2.getStringList("rating.positive").contains(pl.getUniqueId().toString())) {
								List<String> list = script2.getStringList("rating.positive");
								list.add(pl.getUniqueId().toString());

								script2.setValue("rating.positive", list);
								script2.save();

								try {
									openScriptDownloader(pl, b, page);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							else {
								Messages.local.sendTranslation(pl, "android.scripts.rating.already", true);
							}
						}
					}
					else if (!action.isRightClicked()) {
						try {
							script2.setValue("downloads", script2.getInt("downloads") + 1);
							script2.save();

							BlockStorage.addBlockInfo(b, "script", script2.getString("code"));
							openScriptEditor(pl, b);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					return false;
				});

				index++;
			}
		}

		menu.open(p);
	}

	public float getScriptRating(Config script) {
		int positive = getScriptRating(script, true) + 1;
		int negative = getScriptRating(script, false);
		return Math.round((positive / (positive + negative)) * 100.0f) / 100.0f;
	}

	private int getScriptRating(Config script, boolean positive) {
		if (positive) return script.getStringList("rating.positive").size();
		else return script.getStringList("rating.negative").size();
	}

	private String getScriptRatingPercentage(Config script) {
		String progress = String.valueOf(getScriptRating(script));
		if (Float.parseFloat(progress) < 16.0F) progress = "&4" + progress + "&r% ";
		else if (Float.parseFloat(progress) < 32.0F) progress = "&c" + progress + "&r% ";
		else if (Float.parseFloat(progress) < 48.0F) progress = "&6" + progress + "&r% ";
		else if (Float.parseFloat(progress) < 64.0F) progress = "&e" + progress + "&r% ";
		else if (Float.parseFloat(progress) < 80.0F) progress = "&2" + progress + "&r% ";
		else progress = "&a" + progress + "&r% ";

		return progress;
	}

	protected void openScriptComponentEditor(Player p, final Block b, final String script, final int index) throws Exception {
		ChestMenu menu = new ChestMenu("&eScript Editor");

		final String[] commands = script.split("-");

		for (int i = 0; i < 9; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
					(pl, slot, item, action) -> false
			);
		}

		menu.addItem(9, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYxMzlmZDFjNTY1NGU1NmU5ZTRlMmM4YmU3ZWIyYmQ1YjQ5OWQ2MzM2MTY2NjNmZWVlOTliNzQzNTJhZDY0In19fQ=="), "&rDo nothing"),
				(pl, slot, item, action) -> {
					int i = 0;
					StringBuilder builder = new StringBuilder("START-");
					for (String command : commands) {
						if (i != index && i > 0 && i < commands.length - 1) builder.append(command + "-");
						i++;
					}
					builder.append("REPEAT");
					BlockStorage.addBlockInfo(b, "script", builder.toString());
					try {
						openScript(p, b, builder.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
					return false;
				}
		);

		int i = 10;
		for (final ScriptPart part : getAccessibleScriptParts()) {
			menu.addItem(i, part.toItemStack(),
					(pl, slot, item, action) -> {
						int j = 0;
						StringBuilder builder = new StringBuilder("START-");
						for (String command : commands) {
							if (j > 0) {
								if (j == index) builder.append(part.toString() + "-");
								else if (j < commands.length - 1) builder.append(command + "-");
							}
							j++;
						}
						builder.append("REPEAT");
						BlockStorage.addBlockInfo(b, "script", builder.toString());

						try {
							openScript(pl, b, builder.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
						return false;
					}
			);
			i++;
		}

		menu.open(p);
	}

	private Inventory inject(Block b) {
		int size = BlockStorage.getInventory(b).toInventory().getSize();
		Inventory inv = Bukkit.createInventory(null, size);
		for (int i = 0; i < size; i++) {
			inv.setItem(i, new CustomItem(Material.COMMAND_BLOCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
		}
		for (int slot: getOutputSlots()) {
			inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
		}
		return inv;
	}

	protected boolean fits(Block b, ItemStack... items) {
		return inject(b).addItem(items).isEmpty();
	}

	protected void pushItems(Block b, ItemStack... items) {
		Inventory inv = inject(b);
		inv.addItem(items);

		for (int slot : getOutputSlots()) {
			BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
		}
	}

	public void addItems(Block b, ItemStack... items) {
		this.pushItems(b, items);
	}

	@Override
	public void register(boolean slimefun) {
		addItemHandler(new BlockTicker() {

			@Override
			public void tick(Block b, SlimefunItem sf, Config data) {
				if (b != null) ProgrammableAndroid.this.tick(b);
			}

			@Override
			public void uniqueTick() {
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		});

		super.register(slimefun);
	}

	public void registerFuel(MachineFuel fuel) {
		this.recipes.add(fuel);
	}

	public List<Config> getUploadedScripts() {
		List<Config> scripts = new ArrayList<Config>();

		File directory = new File("plugins/Slimefun/scripts/" + this.getAndroidType().toString());
		if (!directory.exists()) directory.mkdirs();

		for (File script : directory.listFiles()) {
			if (script.getName().endsWith("sfs")) scripts.add(new Config(script));
		}

		if (!this.getAndroidType().equals(AndroidType.NONE)) {
			File directory2 = new File("plugins/Slimefun/scripts/NONE");
			if (!directory2.exists()) directory2.mkdirs();

			for (File script : directory2.listFiles()) {
				if (script.getName().endsWith("sfs")) scripts.add(new Config(script));
			}
		}

		Collections.sort(scripts, new ScriptReputationSorter(this));

		return scripts;
	}

	public List<ScriptPart> getAccessibleScriptParts() {
		List<ScriptPart> list = new ArrayList<ScriptPart>();

		for (final ScriptPart part : ScriptPart.values()) {
			if (!part.equals(ScriptPart.START) && !part.equals(ScriptPart.REPEAT) && getAndroidType().isType(part.getRequiredType())) {
				list.add(part);
			}
		}

		return list;
	}

}
