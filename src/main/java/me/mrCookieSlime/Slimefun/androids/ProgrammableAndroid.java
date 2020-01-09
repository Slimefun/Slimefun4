package me.mrCookieSlime.Slimefun.androids;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Tag;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import io.github.thebusybiscuit.cscorelib2.blocks.Vein;
import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import io.github.thebusybiscuit.cscorelib2.collections.RandomizedSet;
import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialConverter;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.skull.SkullBlock;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.ExoticGarden.ExoticGarden;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.androids.comparators.ScriptReputationSorter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public abstract class ProgrammableAndroid extends SlimefunItem implements InventoryBlock {

	// Determines the drops a miner android will get
	private static final ItemStack EFFECTIVE_PICKAXE = new ItemStack(Material.DIAMOND_PICKAXE);

	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 18, 24, 25, 26, 27, 33, 35, 36, 42, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
	private static final int[] border_out = {10, 11, 12, 13, 14, 19, 23, 28, 32, 37, 38, 39, 40, 41};

	private static final RandomizedSet<ItemStack> fishingLoot = new RandomizedSet<>();
	
	static {
		for (Material fish : MaterialCollections.getAllFishItems()) {
			fishingLoot.add(new ItemStack(fish), 20);
		}
		
		fishingLoot.add(new ItemStack(Material.BONE), 10);
		fishingLoot.add(new ItemStack(Material.STRING), 10);
		fishingLoot.add(new ItemStack(Material.STICK), 5);
		fishingLoot.add(new ItemStack(Material.INK_SAC), 4);
		fishingLoot.add(new ItemStack(Material.ROTTEN_FLESH), 3);
		fishingLoot.add(new ItemStack(Material.LEATHER), 2);
	}
	
	private static final List<BlockFace> directions = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);

	private final Set<MachineFuel> recipes = new HashSet<>();
	private final String texture;
	
	@Override
	public int[] getInputSlots() {
		return new int[0];
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {20, 21, 22, 29, 30, 31};
	}

	public abstract AndroidType getAndroidType();
	public abstract float getFuelEfficiency();
	public abstract int getTier();

	public ProgrammableAndroid(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		this.texture = item.getBase64Texture().orElse(null);

		if (getTier() == 1) {
			registerFuel(new MachineFuel(800, new ItemStack(Material.COAL_BLOCK)));
			registerFuel(new MachineFuel(45, new ItemStack(Material.BLAZE_ROD)));

			// Coals
			for (Material mat : Tag.ITEMS_COALS.getValues()) {
				registerFuel(new MachineFuel(8, new ItemStack(mat)));
			}
			
			// Logs
			for (Material mat : Tag.LOGS.getValues()) {
				registerFuel(new MachineFuel(2, new ItemStack(mat)));
			}

			// Wooden Planks
			for (Material mat : Tag.PLANKS.getValues()) {
				registerFuel(new MachineFuel(1, new ItemStack(mat)));
			}
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

		new BlockMenuPreset(getID(), "Programmable Android") {

			@Override
			public void init() {
				constructMenu(this);
			}

			@Override
			public boolean canOpen(Block b, Player p) {
				boolean open = BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString()) || p.hasPermission("slimefun.android.bypass");
				
				if (!open) {
					SlimefunPlugin.getLocal().sendMessage(p, "inventory.no-access", true);
				}
				
				return open;
			}

			@Override
			public void newInstance(BlockMenu menu, final Block b) {
				menu.replaceExistingItem(15, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTAxYzdiNTcyNjE3ODk3NGIzYjNhMDFiNDJhNTkwZTU0MzY2MDI2ZmQ0MzgwOGYyYTc4NzY0ODg0M2E3ZjVhIn19fQ=="), "&aStart/Continue"));
				menu.addMenuClickHandler(15, (p, slot, item, action) -> {
					SlimefunPlugin.getLocal().sendMessage(p, "robot.started", true);
					BlockStorage.addBlockInfo(b, "paused", "false");
					p.closeInventory();
					return false;
				});

				menu.replaceExistingItem(17, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYxMzlmZDFjNTY1NGU1NmU5ZTRlMmM4YmU3ZWIyYmQ1YjQ5OWQ2MzM2MTY2NjNmZWVlOTliNzQzNTJhZDY0In19fQ=="), "&4Pause"));
				menu.addMenuClickHandler(17, (p, slot, item, action) -> {
					BlockStorage.addBlockInfo(b, "paused", "true");
					SlimefunPlugin.getLocal().sendMessage(p, "robot.stopped", true);
					return false;
				});

				menu.replaceExistingItem(16, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc4ZjJiN2U1ZTc1NjM5ZWE3ZmI3OTZjMzVkMzY0YzRkZjI4YjQyNDNlNjZiNzYyNzdhYWRjZDYyNjEzMzcifX19"), "&bMemory Core", "", "&8\u21E8 &7Click to open the Script Editor"));
				menu.addMenuClickHandler(16, (p, slot, item, action) -> {
					BlockStorage.addBlockInfo(b, "paused", "true");
					SlimefunPlugin.getLocal().sendMessage(p, "robot.stopped", true);
					openScriptEditor(p, b);
					return false;
				});
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				return new int[0];
			}
		};

		registerBlockHandler(getID(), new SlimefunBlockHandler() {

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
				boolean allow =  reason == UnregisterReason.PLAYER_BREAK && (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString()) || p.hasPermission("slimefun.android.bypass"));

				if (allow) {
					BlockMenu inv = BlockStorage.getInventory(b);
					
					if (inv != null) {
						if (inv.getItemInSlot(43) != null) {
							b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(43));
							inv.replaceExistingItem(43, null);
						}
						
						for (int slot : getOutputSlots()) {
							if (inv.getItemInSlot(slot) != null) {
								b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
								inv.replaceExistingItem(slot, null);
							}
						}
					}
				}

				return allow;
			}
		});
	}

	protected void tick(Block b) {
		if (!(b.getState() instanceof Skull)) {
			// The Android was destroyed or moved.
			return;
		}

		if (BlockStorage.getLocationInfo(b.getLocation(), "paused").equals("false")) {
			BlockMenu menu = BlockStorage.getInventory(b);
			float fuel = Float.parseFloat(BlockStorage.getLocationInfo(b.getLocation(), "fuel"));
			
			if (fuel < 0.001) {
				ItemStack item = menu.getItemInSlot(43);
				
				if (item != null) {
					for (MachineFuel recipe : recipes) {
						if (SlimefunManager.isItemSimilar(item, recipe.getInput(), true)) {
							menu.consumeItem(43);
							
							if (getTier() == 2) {
								menu.pushItem(new ItemStack(Material.BUCKET), getOutputSlots());
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
					BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
					double damage = getTier() < 2 ? 20D : 4D * getTier();
					
					switch (part) {
						case GO_DOWN:
							move(b, face, b.getRelative(BlockFace.DOWN));
							break;
						case GO_FORWARD:
							move(b, face, b.getRelative(face));
							break;
						case GO_UP:
							move(b, face, b.getRelative(BlockFace.UP));
							break;
						case REPEAT:
							BlockStorage.addBlockInfo(b, "index", String.valueOf(0));
							break;
						case TURN_LEFT:
							int indexLeft = directions.indexOf(BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) - 1;
							if (indexLeft < 0) indexLeft = directions.size() - 1;
							Rotatable rotatableLeft = (Rotatable) b.getBlockData();
							rotatableLeft.setRotation(directions.get(indexLeft));
							b.setBlockData(rotatableLeft);
							BlockStorage.addBlockInfo(b, "rotation", directions.get(indexLeft).toString());
							break;
						case TURN_RIGHT:
							int indexRight = directions.indexOf(BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) + 1;
							if (indexRight == directions.size()) indexRight = 0;
							Rotatable rotatableRight = (Rotatable) b.getBlockData();
							rotatableRight.setRotation(directions.get(indexRight));
							b.setBlockData(rotatableRight);
							BlockStorage.addBlockInfo(b, "rotation", directions.get(indexRight).toString());
							break;
						case DIG_FORWARD:
							mine(b, menu, b.getRelative(face));
							break;
						case DIG_UP:
							mine(b, menu, b.getRelative(BlockFace.UP));
							break;
						case DIG_DOWN:
							mine(b, menu, b.getRelative(BlockFace.DOWN));
							break;
						case CATCH_FISH:
							Block water = b.getRelative(BlockFace.DOWN);
							if (water.getType() == Material.WATER) {
								water.getWorld().playSound(water.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);
								if (ThreadLocalRandom.current().nextInt(100) < 10 * getTier()) {
									ItemStack drop = fishingLoot.getRandom();
									if (menu.fits(drop, getOutputSlots())) {
										menu.pushItem(drop, getOutputSlots());
									}
								}

							}
							break;
						case MOVE_AND_DIG_FORWARD:
							movedig(b, menu, face, b.getRelative(face));
							break;
						case MOVE_AND_DIG_UP:
							movedig(b, menu, face, b.getRelative(BlockFace.UP));
							break;
						case MOVE_AND_DIG_DOWN:
							movedig(b, menu, face, b.getRelative(BlockFace.DOWN));
							break;
						case INTERFACE_ITEMS:
							if (BlockStorage.check(b.getRelative(face), "ANDROID_INTERFACE_ITEMS") && b.getRelative(face).getState() instanceof Dispenser) {
								Dispenser d = (Dispenser) b.getRelative(face).getState();
								
								for (int slot : getOutputSlots()) {
									ItemStack stack = menu.getItemInSlot(slot);
									
									if (stack != null) {
										Optional<ItemStack> optional = d.getInventory().addItem(stack).values().stream().findFirst();
										
										if (optional.isPresent()) {
											menu.replaceExistingItem(slot, optional.get());
										}
										else {
											menu.replaceExistingItem(slot, null);
										}
									}
								}
							}
							break;
						case INTERFACE_FUEL:
							if (BlockStorage.check(b.getRelative(face), "ANDROID_INTERFACE_FUEL") && b.getRelative(face).getState() instanceof Dispenser) {
								Dispenser d = (Dispenser) b.getRelative(face).getState();
								for (int slot = 0; slot < 9; slot++) {
									ItemStack item = d.getInventory().getItem(slot);
									
									if (item != null) {
										if (menu.getItemInSlot(43) == null) {
											menu.replaceExistingItem(43, item);
											d.getInventory().setItem(slot, null);
											break;
										}
										else if (SlimefunManager.isItemSimilar(item, menu.getItemInSlot(43), true)) {
											int rest = item.getType().getMaxStackSize() - menu.getItemInSlot(43).getAmount();
											
											if (rest > 0) {
												int amt = item.getAmount() > rest ? rest: item.getAmount();
												menu.replaceExistingItem(43, new CustomItem(item, menu.getItemInSlot(43).getAmount() + amt));
												ItemUtils.consumeItem(item, amt, false);
											}
											break;
										}
									}
								}
							}
							break;
						case FARM_FORWARD:
							farm(menu, b.getRelative(face));
							break;
						case FARM_DOWN:
							farm(menu, b.getRelative(BlockFace.DOWN));
							break;
						case FARM_EXOTIC_FORWARD:
							exoticFarm(menu, b.getRelative(face));
							break;
						case FARM_EXOTIC_DOWN:
							exoticFarm(menu, b.getRelative(BlockFace.DOWN));
							break;
						case CHOP_TREE:
							if (MaterialCollections.getAllLogs().contains(b.getRelative(face).getType())) {
								List<Block> list = Vein.find(b.getRelative(face), 180, block -> MaterialCollections.getAllLogs().contains(block.getType()));
								if (!list.isEmpty()) {
									refresh = false;
									Block log = list.get(list.size() - 1);
									log.getWorld().playEffect(log.getLocation(), Effect.STEP_SOUND, log.getType());
									
									if (SlimefunPlugin.getProtectionManager().hasPermission(Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"))), log.getLocation(), ProtectableAction.BREAK_BLOCK)) {
										ItemStack drop = new ItemStack(log.getType());
										
										if (menu.fits(drop, getOutputSlots())) {
											menu.pushItem(drop, getOutputSlots());
											log.getWorld().playEffect(log.getLocation(), Effect.STEP_SOUND, log.getType());
											
											if (log.getY() == b.getRelative(face).getY()) {
												Optional<Material> sapling = MaterialConverter.getSaplingFromLog(log.getType());
												if (sapling.isPresent()) {
													log.setType(sapling.get());
												}
											}
											else log.setType(Material.AIR);
										}

									}
								}
							}
							break;
						case ATTACK_MOBS_ANIMALS:
							killEntities(b, damage, e -> true);
							break;
						case ATTACK_MOBS:
							killEntities(b, damage, e -> e instanceof Monster);
							break;
						case ATTACK_ANIMALS:
							killEntities(b, damage, e -> e instanceof Animals);
							break;
						case ATTACK_ANIMALS_ADULT:
							killEntities(b, damage, e -> e instanceof Animals && e instanceof org.bukkit.entity.Ageable && ((org.bukkit.entity.Ageable) e).isAdult());
							break;
						default:
							break;
					}
				}
				if (refresh) BlockStorage.addBlockInfo(b, "index", String.valueOf(index));
			}
		}
	}
	
	private void killEntities(Block b, double damage, Predicate<Entity> predicate) {
		double radius = 4.0 + getTier();
		for (Entity n : b.getWorld().getNearbyEntities(b.getLocation(), radius, radius, radius, n -> n instanceof LivingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.isValid() && predicate.test(n))) {
			boolean attack = false;
			
			switch (BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) {
			case NORTH:
				attack = n.getLocation().getZ() < b.getZ();
				break;
			case EAST:
				attack = n.getLocation().getX() > b.getX();
				break;
			case SOUTH:
				attack = n.getLocation().getZ() > b.getZ();
				break;
			case WEST:
				attack = n.getLocation().getX() < b.getX();
				break;
			default:
				break;
			}
			
			if (attack) {
				if (n.hasMetadata("android_killer")) {
					n.removeMetadata("android_killer", SlimefunPlugin.instance);
				}
				
				n.setMetadata("android_killer", new FixedMetadataValue(SlimefunPlugin.instance, new AndroidEntity(this, b)));

				((LivingEntity) n).damage(damage);
				break;
			}
		}
	}

	private void move(Block b, BlockFace face, Block block) {
		if (block.getY() > 0 && block.getY() < block.getWorld().getMaxHeight() && (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR)) {
			block.setType(Material.PLAYER_HEAD);
			Rotatable blockData = (Rotatable) block.getBlockData();
			blockData.setRotation(face.getOppositeFace());
			block.setBlockData(blockData);

			SkullBlock.setFromBase64(block, texture);
			
			b.setType(Material.AIR);
			BlockStorage.moveBlockInfo(b.getLocation(), block.getLocation());
		}
	}

	private void mine(Block b, BlockMenu menu, Block block) {
		Collection<ItemStack> drops = block.getDrops(EFFECTIVE_PICKAXE);
		if (!MaterialCollections.getAllUnbreakableBlocks().contains(block.getType()) && !drops.isEmpty() && SlimefunPlugin.getProtectionManager().hasPermission(Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"))), block.getLocation(), ProtectableAction.BREAK_BLOCK)) {
			String item = BlockStorage.checkID(block);
			
			AndroidMineEvent event = new AndroidMineEvent(block, new AndroidEntity(this, b));
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return;
			}
			
			if (item == null) {
				for (ItemStack drop : drops) {
					if (menu.fits(drop, getOutputSlots())) {
						menu.pushItem(drop, getOutputSlots());
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
						block.setType(Material.AIR);
					}
				}
			}
			/*
			else if (fits(b, item.getItem())) {
	            if (SlimefunItem.blockhandler.containsKey(item.getID())) {
	                if (SlimefunItem.blockhandler.get(item.getID()).onBreak(null, block, item, UnregisterReason.ANDROID_DIG)) {
	                    pushItems(b, BlockStorage.retrieve(block));
	                    if (SlimefunItem.blockhandler.containsKey(item.getID())) SlimefunItem.blockhandler.get(item.getID()).onBreak(null, block, item, UnregisterReason.ANDROID_DIG);
	                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
	                    block.setType(Material.AIR);
	                }
	            }
        	}*/
		}
	}


	private void movedig(Block b, BlockMenu menu, BlockFace face, Block block) {
		Collection<ItemStack> drops = block.getDrops(EFFECTIVE_PICKAXE);
		if (!MaterialCollections.getAllUnbreakableBlocks().contains(block.getType()) && !drops.isEmpty() && SlimefunPlugin.getProtectionManager().hasPermission(Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"))), block.getLocation(), ProtectableAction.BREAK_BLOCK)) {
			SlimefunItem item = BlockStorage.check(block);
			
			AndroidMineEvent event = new AndroidMineEvent(block, new AndroidEntity(this, b));
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return;
			}
			
			if (item == null) {
				for (ItemStack drop : drops) {
					if (menu.fits(drop, getOutputSlots())) {
						menu.pushItem(drop, getOutputSlots());
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
						
						block.setType(Material.AIR);
						move(b, face, block);
						
						b.setType(Material.AIR);
						BlockStorage.moveBlockInfo(b.getLocation(), block.getLocation());
					}
				}
			}
			/*
			else {
				if (fits(b, item.getItem()) && SlimefunPlugin.getUtilities().blockHandlers.containsKey(item.getID()) && SlimefunPlugin.getUtilities().blockHandlers.get(item.getID()).onBreak(null, block, item, UnregisterReason.ANDROID_DIG)) {
					pushItems(b, BlockStorage.retrieve(block));
					block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
					
					block.setType(Material.AIR);
					move(b, face, block);
					
					b.setType(Material.AIR);
					BlockStorage.moveBlockInfo(b.getLocation(), block.getLocation());
				}
			}*/
		}
		else {
			move(b, face, block);
		}
	}

	private boolean isFullGrown(Block block){
		if (!(block.getBlockData() instanceof Ageable)) return false;
		Ageable ageable = ((Ageable) block.getBlockData());
		return ageable.getAge() >= ageable.getMaximumAge();
	}

	private void farm(BlockMenu menu, Block block) {
		if (isFullGrown(block)) {
			ItemStack drop = getDropFromCrop(block.getType());
			
			if (drop != null && menu.fits(drop, getOutputSlots())) {
				menu.pushItem(drop, getOutputSlots());
				Ageable ageable = (Ageable) block.getBlockData();
				ageable.setAge(0);
				block.setBlockData(ageable);
				block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
			}
		}
	}
	
	private ItemStack getDropFromCrop(Material crop) {
		Random random = ThreadLocalRandom.current();
		
		switch (crop) {
		case WHEAT:
			return new ItemStack(Material.WHEAT, random.nextInt(2) + 1);
		case POTATOES:
			return new ItemStack(Material.POTATO, random.nextInt(3) + 1);
		case CARROTS:
			return new ItemStack(Material.CARROT, random.nextInt(3) + 1);
		case BEETROOTS:
			return new ItemStack(Material.BEETROOT, random.nextInt(3) + 1);
		case COCOA:
			return new ItemStack(Material.COCOA_BEANS, random.nextInt(3) + 1);
		case NETHER_WART:
			return new ItemStack(Material.NETHER_WART, random.nextInt(3) + 1);
		case SWEET_BERRY_BUSH:
			return new ItemStack(Material.SWEET_BERRIES, random.nextInt(3) + 1);
		default:
			return null;
		}
	}

	private void exoticFarm(BlockMenu menu, Block block) {
		farm(menu, block);
		
		if (SlimefunPlugin.getHooks().isExoticGardenInstalled()) {
			ItemStack drop = ExoticGarden.harvestPlant(block);
			if (drop != null && menu.fits(drop, getOutputSlots())) {
				menu.pushItem(drop, getOutputSlots());
				block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
			}
		}
	}

	private void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}
		for (int i : border_out) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
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
			preset.addItem(34, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&8\u21E9 &cFuel Input &8\u21E9", "", "&rThis Android runs on solid Fuel", "&re.g. Coal, Wood, etc..."), (p, slot, item, action) -> false);
		}
		else if (getTier() == 2){
			preset.addItem(34, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&8\u21E9 &cFuel Input &8\u21E9", "", "&rThis Android runs on liquid Fuel", "&re.g. Lava, Oil, Fuel, etc..."), (p, slot, item, action) -> false);
		}
		else {
			preset.addItem(34, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&8\u21E9 &cFuel Input &8\u21E9", "", "&rThis Android runs on radioactive Fuel", "&re.g. Uranium, Neptunium or Boosted Uranium"), (p, slot, item, action) -> false);
		}
	}

	public void openScriptEditor(Player p, final Block b) {
		ChestMenu menu = new ChestMenu("&eScript Editor");
		
		try {
			menu.addItem(1, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliZjZkYjRhZWRhOWQ4ODIyYjlmNzM2NTM4ZThjMThiOWE0ODQ0Zjg0ZWI0NTUwNGFkZmJmZWU4N2ViIn19fQ=="), "&2> Edit Script", "", "&aEdits your current Script"));
			menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
				openScript(pl, b, BlockStorage.getLocationInfo(b.getLocation(), "script"));
				return false;
			});
	
			menu.addItem(3, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxZDg5NzljMTg3OGEwNTk4N2E3ZmFmMjFiNTZkMWI3NDRmOWQwNjhjNzRjZmZjZGUxZWExZWRhZDU4NTIifX19"), "&4> Create new Script", "", "&cDeletes your current Script", "&cand creates a blank one"));
			menu.addMenuClickHandler(3, (pl, slot, item, action) -> {
				openScript(pl, b, "START-TURN_LEFT-REPEAT");
				return false;
			});
	
			menu.addItem(5, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzAxNTg2ZTM5ZjZmZmE2M2I0ZmIzMDFiNjVjYTdkYThhOTJmNzM1M2FhYWI4OWQzODg2NTc5MTI1ZGZiYWY5In19fQ=="), "&6> Download a Script", "", "&eDownload a Script from the Server", "&eYou can edit or simply use it"));
			menu.addMenuClickHandler(5, (pl, slot, item, action) -> {
				openScriptDownloader(pl, b, 1);
				return false;
			});
	
			menu.addItem(8, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE4NWM5N2RiYjgzNTNkZTY1MjY5OGQyNGI2NDMyN2I3OTNhM2YzMmE5OGJlNjdiNzE5ZmJlZGFiMzVlIn19fQ=="), "&6> Back", "", "&7Return to the Android's interface"));
			menu.addMenuClickHandler(8, (pl, slot, item, action) -> {
				BlockStorage.getInventory(b).open(p);
				return false;
			});
		}
		catch (Exception x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occured while creating the Script Editor for Slimefun " + Slimefun.getVersion(), x);
		}

		menu.open(p);
	}

	public void openScript(final Player p, final Block b, final String script) {
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
					try {
						menu.addItem(i, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxZDg5NzljMTg3OGEwNTk4N2E3ZmFmMjFiNTZkMWI3NDRmOWQwNjhjNzRjZmZjZGUxZWExZWRhZDU4NTIifX19"), "&7> Add new Command"));
						menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
							openScriptComponentEditor(pl, b, script, index);
							return false;
						});
					}
					catch (Exception x) {
						Slimefun.getLogger().log(Level.SEVERE, "An Error occured while creating the Script Editor for Slimefun " + Slimefun.getVersion(), x);
					}
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

						openScript(pl, b, builder.toString());
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

						openScript(pl, b, builder.toString());
					}
					else {
						openScriptComponentEditor(pl, b, script, index);
					}
					return false;
				});
			}
		}

		menu.open(p);
	}

	private void openScriptDownloader(final Player p, final Block b, final int page) {
		final ChestMenu menu = new ChestMenu("Android Scripts");

		menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.7F, 0.7F));

		List<Config> scripts = getUploadedScripts();

		int index = 0;
		final int pages = (scripts.size() / 45) + 1;

		for (int i = 45; i < 54; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
		}

		menu.addItem(46, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r\u21E6 Previous Page", "", "&7(" + page + " / " + pages + ")"));
		menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
			int next = page - 1;
			if (next < 1) next = pages;
			if (next != page) {
				openScriptDownloader(pl, b, next);
			}
			return false;
		});
		
		try {
			menu.addItem(48, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTA1YTJjYWI4YjY4ZWE1N2UzYWY5OTJhMzZlNDdjOGZmOWFhODdjYzg3NzYyODE5NjZmOGMzY2YzMWEzOCJ9fX0="), "&eUpload a Script", "", "&6Click &7to upload your Android's Script", "&7to the Database"));
			menu.addMenuClickHandler(48, (pl, slot, item, action) -> {
				final String code = BlockStorage.getLocationInfo(b.getLocation(), "script");
				int num = 1;
	
				for (Config script: getUploadedScripts()) {
					if (script.getString("author").equals(pl.getUniqueId().toString())) num++;
					if (script.getString("code").equals(code)) {
						SlimefunPlugin.getLocal().sendMessage(pl, "android.scripts.already-uploaded", true);
						return false;
					}
				}
	
				final int id = num;
	
				pl.closeInventory();
				SlimefunPlugin.getLocal().sendMessages(pl, "android.scripts.enter-name");
	
				ChatInput.waitForPlayer(SlimefunPlugin.instance, pl, message -> {
					Config script = new Config("plugins/Slimefun/scripts/" + getAndroidType().toString() + '/' + p.getName() + ' ' + id + ".sfs");
	
					script.setValue("author", pl.getUniqueId().toString());
					script.setValue("author_name", pl.getName());
					script.setValue("name", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)));
					script.setValue("code", code);
					script.setValue("downloads", 0);
					script.setValue("android", getAndroidType().toString());
					script.setValue("rating.positive", new ArrayList<String>());
					script.setValue("rating.negative", new ArrayList<String>());
					script.save();
	
					SlimefunPlugin.getLocal().sendMessages(pl, "android.scripts.uploaded");
					openScriptDownloader(pl, b, page);
				});
				return false;
			});
	
			menu.addItem(50, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&rNext Page \u21E8", "", "&7(" + page + " / " + pages + ")"));
			menu.addMenuClickHandler(50, (pl, slot, item, action) -> {
				int next = page + 1;
				if (next > pages) next = 1;
				if (next != page) {
					openScriptDownloader(pl, b, next);
				}
				return false;
			});
	
			menu.addItem(53, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE4NWM5N2RiYjgzNTNkZTY1MjY5OGQyNGI2NDMyN2I3OTNhM2YzMmE5OGJlNjdiNzE5ZmJlZGFiMzVlIn19fQ=="), "&6> Back", "", "&7Return to the Android's interface"));
			menu.addMenuClickHandler(53, (pl, slot, item, action) -> {
				openScriptEditor(pl, b);
				return false;
			});
		}
		catch (Exception x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occured while creating the Script Download Menu for Slimefun " + Slimefun.getVersion(), x);
		}

		int categoryIndex = 45 * (page - 1);
		
		for (int i = 0; i < 45; i++) {
			int target = categoryIndex + i;
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
							SlimefunPlugin.getLocal().sendMessage(pl, "android.scripts.rating.own", true);
						}
						else if (action.isRightClicked()) {
							if (!script2.getStringList("rating.negative").contains(pl.getUniqueId().toString()) && !script2.getStringList("rating.positive").contains(pl.getUniqueId().toString())) {
								List<String> list = script2.getStringList("rating.negative");
								list.add(p.getUniqueId().toString());

								script2.setValue("rating.negative", list);
								script2.save();

								openScriptDownloader(pl, b, page);
							}
							else {
								SlimefunPlugin.getLocal().sendMessage(pl, "android.scripts.rating.already", true);
							}
						}
						else {
							if (!script2.getStringList("rating.negative").contains(pl.getUniqueId().toString()) && !script2.getStringList("rating.positive").contains(pl.getUniqueId().toString())) {
								List<String> list = script2.getStringList("rating.positive");
								list.add(pl.getUniqueId().toString());

								script2.setValue("rating.positive", list);
								script2.save();

								openScriptDownloader(pl, b, page);
							}
							else {
								SlimefunPlugin.getLocal().sendMessage(pl, "android.scripts.rating.already", true);
							}
						}
					}
					else if (!action.isRightClicked()) {
						script2.setValue("downloads", script2.getInt("downloads") + 1);
						script2.save();

						BlockStorage.addBlockInfo(b, "script", script2.getString("code"));
						openScriptEditor(pl, b);
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
		return Math.round((positive / (double) (positive + negative)) * 100.0F) / 100.0F;
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

	protected void openScriptComponentEditor(Player p, final Block b, final String script, final int index) {
		ChestMenu menu = new ChestMenu("&eScript Editor");

		final String[] commands = script.split("-");

		for (int i = 0; i < 9; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "), (pl, slot, item, action) -> false);
		}
		
		try {
			menu.addItem(9, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYxMzlmZDFjNTY1NGU1NmU5ZTRlMmM4YmU3ZWIyYmQ1YjQ5OWQ2MzM2MTY2NjNmZWVlOTliNzQzNTJhZDY0In19fQ=="), "&rDo nothing"), (pl, slot, item, action) -> {
				int i = 0;
				StringBuilder builder = new StringBuilder("START-");
				
				for (String command : commands) {
					if (i != index && i > 0 && i < commands.length - 1) builder.append(command + "-");
					i++;
				}
				
				builder.append("REPEAT");
				BlockStorage.addBlockInfo(b, "script", builder.toString());

				openScript(p, b, builder.toString());
				return false;
			});
		}
		catch (Exception x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occured while creating the Script Editor for Slimefun " + Slimefun.getVersion(), x);
		}

		int i = 10;
		for (final ScriptPart part : getAccessibleScriptParts()) {
			menu.addItem(i, part.toItemStack());
			menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
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

				openScript(pl, b, builder.toString());
				return false;	
			});
			i++;
		}

		menu.open(p);
	}

	public void addItems(Block b, ItemStack... items) {
		for (ItemStack item: items) {
			BlockStorage.getInventory(b).pushItem(item, getOutputSlots());
		}
	}

	@Override
	public void register(boolean slimefun) {
		addItemHandler(new BlockTicker() {

			@Override
			public void tick(Block b, SlimefunItem sf, me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config data) {
				if (b != null) ProgrammableAndroid.this.tick(b);
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
		List<Config> scripts = new ArrayList<>();

		File directory = new File("plugins/Slimefun/scripts/" + this.getAndroidType().toString());
		if (!directory.exists()) directory.mkdirs();

		for (File script : directory.listFiles()) {
			if (script.getName().endsWith("sfs")) scripts.add(new Config(script));
		}

		if (this.getAndroidType() != AndroidType.NONE) {
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
		List<ScriptPart> list = new ArrayList<>();

		for (final ScriptPart part : ScriptPart.values()) {
			if (part != ScriptPart.START && part != ScriptPart.REPEAT && getAndroidType().isType(part.getRequiredType())) {
				list.add(part);
			}
		}

		return list;
	}

}
