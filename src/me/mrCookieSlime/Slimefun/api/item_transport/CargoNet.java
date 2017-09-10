package me.mrCookieSlime.Slimefun.api.item_transport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.MC_1_8.ParticleEffect;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager.DataType;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import me.mrCookieSlime.Slimefun.holograms.CargoHologram;

public class CargoNet {
	
	public enum Axis {
		X_POSITIVE,
		X_NEGATIVE,
		Y_POSITIVE,
		Y_NEGATIVE,
		Z_POSITIVE,
		Z_NEGATIVE,
		UNKNOWN;
	}
	
	public static boolean EXTRA_CHANNELS = false;
	
	private static final int RANGE = 5;
	public static List<BlockFace> faces = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
	public static Map<Location, Integer> round_robin = new HashMap<Location, Integer>();
	public static Set<ItemRequest> requests = new HashSet<ItemRequest>();
	
	private static int[] slots = new int[] {19, 20, 21, 28, 29, 30, 37, 38, 39};

	//Chest Terminal Stuff
	private static final ChestTerminalSorter sorter = new ChestTerminalSorter();
	public static final int[] terminal_slots = new int[] {0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15, 18, 19, 20, 21, 22, 23, 24, 27, 28, 29, 30, 31, 32, 33, 36, 37, 38, 39, 40, 41, 42};
	private static final ItemStack terminal_noitem_item = new CustomItem(new MaterialData(Material.BARRIER), "&4No Item cached");
	private static final MenuClickHandler terminal_noitem_handler = new MenuClickHandler() {
		
		@Override
		public boolean onClick(Player p, int slot, ItemStack stack, ClickAction action) {
			return false;
		}
	};
	public static void tick(final Block b) {
		/*
		Input/Output Nodes can be found here after a scan.
		Input Nodes:
		Location = The location of the node.
		Integer = The frequency of the node.
		Output Nodes:
		Integer = The frequency of the node.
		List<Location> = The locations of the corresponding input nodes
		 */
		final Map<Location, Integer> input = new HashMap<Location, Integer>();
		final Map<Integer, List<Location>> output = new HashMap<Integer, List<Location>>();

		//Chest Terminal Stuff
		final Set<Location> providers = new HashSet<Location>();
		final Set<Location> terminals = new HashSet<Location>();
		final Set<Location> imports = new HashSet<Location>();
		final Set<Location> exports = new HashSet<Location>();
		final Set<Location> destinations = new HashSet<Location>();
		
		final Set<Location> blocks = new HashSet<Location>();
		blocks.add(b.getLocation());
		
		final List<Location> visualizer1 = new ArrayList<Location>();
		final List<Location> visualizer2 = new ArrayList<Location>();
		
		if (scan(b.getLocation(), blocks, visualizer1, visualizer2, Axis.UNKNOWN, input, output, terminals, providers, destinations, imports, exports).isEmpty()) {
			CargoHologram.update(b, "&7Status: &4&lOFFLINE");
		}
		else {
			final BlockStorage storage = BlockStorage.getStorage(b.getWorld());
			SlimefunStartup.instance.getServer().getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
				
				@Override
				public void run() {
					if (BlockStorage.getBlockInfo(b, "visualizer") == null) {
						for (int i = 0; i < visualizer1.size(); i++) {
							Location l1 = visualizer1.get(i);
							Location l2 = visualizer2.get(i);
							try {
								ParticleEffect.REDSTONE.drawLine(new Location(l1.getWorld(), l1.getBlockX() + 0.5D, l1.getBlockY() + 0.5D, l1.getBlockZ() + 0.5D), new Location(l2.getWorld(), l2.getBlockX() + 0.5D, l2.getBlockY() + 0.5D, l2.getBlockZ() + 0.5D));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					//Chest Terminal Code
					if (EXTRA_CHANNELS) {
						for (Location bus: imports) {
							BlockMenu menu = BlockStorage.getInventory(bus);
							
							if (menu.getItemInSlot(17) == null) {
								Block target = getAttachedBlock(bus.getBlock());
								ItemSlot stack = CargoManager.withdraw(bus.getBlock(), storage, target, -1);
								
								if (stack != null) {
									menu.replaceExistingItem(17, stack.getItem());
								}
							}

							if (menu.getItemInSlot(17) != null) {
								requests.add(new ItemRequest(bus, 17, menu.getItemInSlot(17), ItemTransportFlow.INSERT));
							}
						}
						
						for (Location bus: exports) {
							BlockMenu menu = BlockStorage.getInventory(bus);
							
							if (menu.getItemInSlot(17) != null) {
								Block target = getAttachedBlock(bus.getBlock());
								
								menu.replaceExistingItem(17, CargoManager.insert(bus.getBlock(), storage, target, menu.getItemInSlot(17), -1));
							}
							
							if (menu.getItemInSlot(17) == null) {
								List<ItemStack> items = new ArrayList<ItemStack>();
								for (int slot: slots) {
									ItemStack template = menu.getItemInSlot(slot);
									if (template != null) items.add(new CustomItem(template, 1));
								}
								
								if (!items.isEmpty()) {
									int index = Integer.parseInt(BlockStorage.getBlockInfo(bus, "index"));
									
									index++;
									if (index > (items.size() - 1)) index = 0;
									
									BlockStorage.addBlockInfo(bus, "index", String.valueOf(index));
									
									requests.add(new ItemRequest(bus, 17, items.get(index), ItemTransportFlow.WITHDRAW));
								}
							}
						}
						
						Iterator<ItemRequest> iterator = requests.iterator();
						while (iterator.hasNext()) {
							ItemRequest request = iterator.next();
							if (terminals.contains(request.getTerminal()) || imports.contains(request.getTerminal()) || exports.contains(request.getTerminal())) {
								BlockMenu menu = BlockStorage.getInventory(request.getTerminal());
								
								switch (request.getDirection()) {
								case INSERT: {
									ItemStack stack = request.getItem();
									nodes:
									for (Location l: destinations) {
										Block target = getAttachedBlock(l.getBlock());
										stack = CargoManager.insert(l.getBlock(), storage, target, stack, -1);
										if (stack == null) {
											menu.replaceExistingItem(request.getSlot(), null);
											break nodes;
										}
									}
									
									if (stack != null) {
										menu.replaceExistingItem(request.getSlot(), stack);
									}
									
									iterator.remove();
									break;
								}
								case WITHDRAW: {
									int slot = request.getSlot();
									ItemStack prevStack = menu.getItemInSlot(slot);
									if (!(prevStack == null || (prevStack.getAmount() + request.getItem().getAmount() <= prevStack.getMaxStackSize() && SlimefunManager.isItemSimiliar(prevStack, new CustomItem(request.getItem(), 1), true, DataType.ALWAYS)))) {
										iterator.remove();
										break;
									}
									
									ItemStack stack = null;
									ItemStack requested = request.getItem();
									nodes:
									for (Location l: providers) {
										Block target = getAttachedBlock(l.getBlock());
										ItemStack is = CargoManager.withdraw(l.getBlock(), storage, target, requested);
										if (is != null) {
											if (stack == null) {
												stack = is;
											}
											else {
												stack = new CustomItem(stack, stack.getAmount() + is.getAmount());
											}
											
											if (is.getAmount() == requested.getAmount()) {
												break nodes;
											}
											else {
												requested = new CustomItem(requested, requested.getAmount() - is.getAmount());
											}
										}
									}
									
									if (stack != null) {
										ItemStack prev = menu.getItemInSlot(slot);
										
										if (prev == null) menu.replaceExistingItem(slot, stack);
										else menu.replaceExistingItem(slot, new CustomItem(stack, stack.getAmount() + prev.getAmount()));
									}
									
									iterator.remove();
									break;
								}
								default: {
									break;
								}
								}
							}
						}
					}
					//All operations happen here: Everything gets iterated from the Input Nodes. (Apart from ChestTerminal Buses)
					for (Map.Entry<Location, Integer> entry: input.entrySet()) {
						Block inputTarget = getAttachedBlock(entry.getKey().getBlock());
						ItemStack stack = null;
						int previousSlot = -1;

						boolean roundrobin = BlockStorage.getBlockInfo(entry.getKey(), "round-robin").equals("true");
						
						if (inputTarget != null) {
							ItemSlot slot = CargoManager.withdraw(entry.getKey().getBlock(), storage, inputTarget, Integer.parseInt(BlockStorage.getBlockInfo(entry.getKey(), "index")));
							if (slot != null) {
								stack = slot.getItem();
								previousSlot = slot.getSlot();
							}
						}

						if (stack != null && output.containsKey(entry.getValue())) {
							List<Location> outputlist = new ArrayList<Location>(output.get(entry.getValue()));
							
							if (roundrobin) {
								if (!round_robin.containsKey(entry.getKey())) {
									round_robin.put(entry.getKey(), 0);
								}
								
								int c_index = round_robin.get(entry.getKey());
								
								if (c_index < outputlist.size()) {
									for (int i = 0; i < c_index; i++) {
										final Location temp = outputlist.get(0);
										outputlist.remove(temp);
										outputlist.add(temp);
									}
									c_index++;
								}
								else c_index = 1;
								
								round_robin.put(entry.getKey(), c_index);
							}
							
							destinations:
							for (Location out: outputlist) {
								Block target = getAttachedBlock(out.getBlock());
								if (target != null) {
									stack = CargoManager.insert(out.getBlock(), storage, target, stack, -1);
									if (stack == null) break destinations;
								}
							}
						}
						
						if (stack != null && previousSlot > -1) {
							if (storage.hasUniversalInventory(inputTarget)) {
								UniversalBlockMenu menu = storage.getUniversalInventory(inputTarget);
								menu.replaceExistingItem(previousSlot, stack);
							}
							else if (storage.hasInventory(inputTarget.getLocation())) {
								BlockMenu menu = BlockStorage.getInventory(inputTarget.getLocation());
								menu.replaceExistingItem(previousSlot, stack);
							}
							else if (inputTarget.getState() instanceof InventoryHolder) {
								Inventory inv = ((InventoryHolder) inputTarget.getState()).getInventory();
								inv.setItem(previousSlot, stack);
							}
						}
					}
					//Chest Terminal Code
					if (EXTRA_CHANNELS) {
						List<StoredItem> items = new ArrayList<StoredItem>();
						for (Location l: providers) {
							Block target = getAttachedBlock(l.getBlock());
							if (storage.hasUniversalInventory(target)) {
								UniversalBlockMenu menu = storage.getUniversalInventory(target);
								for (int slot: menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
									ItemStack is = menu.getItemInSlot(slot);
									if (is != null && CargoManager.matchesFilter(l.getBlock(), is, -1)) {
										boolean add = true;
										for (StoredItem item: items) {
											if (SlimefunManager.isItemSimiliar(is, item.getItem(), true, DataType.ALWAYS)) {
												add = false;
												item.add(is.getAmount());
											}
										}
										
										if (add) {
											items.add(new StoredItem(new CustomItem(is, 1), is.getAmount()));
										}
									}
								}
							}
							else if (storage.hasInventory(target.getLocation())) {
								BlockMenu menu = BlockStorage.getInventory(target.getLocation());
								if (BlockStorage.checkID(target.getLocation()).startsWith("BARREL_") && BlockStorage.getBlockInfo(target.getLocation(), "storedItems") != null) {
									int stored = Integer.valueOf(BlockStorage.getBlockInfo(target.getLocation(), "storedItems"));
									for (int slot: menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
										ItemStack is = menu.getItemInSlot(slot);
										if (is != null && CargoManager.matchesFilter(l.getBlock(), is, -1)) {
											boolean add = true;
											for (StoredItem item: items) {
												if (SlimefunManager.isItemSimiliar(is, item.getItem(), true, DataType.ALWAYS)) {
													add = false;
													item.add(is.getAmount() + stored);
												}
											}
											
											if (add) {
												items.add(new StoredItem(new CustomItem(is, 1), is.getAmount() + stored));
											}
										}
									}
								}
								else {
									for (int slot: menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
										ItemStack is = menu.getItemInSlot(slot);
										if (is != null && CargoManager.matchesFilter(l.getBlock(), is, -1)) {
											boolean add = true;
											for (StoredItem item: items) {
												if (SlimefunManager.isItemSimiliar(is, item.getItem(), true, DataType.ALWAYS)) {
													add = false;
													item.add(is.getAmount());
												}
											}
											
											if (add) {
												items.add(new StoredItem(new CustomItem(is, 1), is.getAmount()));
											}
										}
									}
								}
							}
							else if (target.getState() instanceof InventoryHolder) {
								Inventory inv = ((InventoryHolder) target.getState()).getInventory();
								for (ItemStack is: inv.getContents()) {
									if (is != null && CargoManager.matchesFilter(l.getBlock(), is, -1)) {
										boolean add = true;
										for (StoredItem item: items) {
											if (SlimefunManager.isItemSimiliar(is, item.getItem(), true, DataType.ALWAYS)) {
												add = false;
												item.add(is.getAmount());
											}
										}
										
										if (add) {
											items.add(new StoredItem(new CustomItem(is, 1), is.getAmount()));
										}
									}
								}
							}
						}
						
						Collections.sort(items, sorter);
						
						for (final Location l: terminals) {
							BlockMenu menu = BlockStorage.getInventory(l);
							int page = Integer.parseInt(BlockStorage.getBlockInfo(l, "page"));
							if (!items.isEmpty() && items.size() < (page - 1) * terminal_slots.length + 1) {
								page = 1;
								BlockStorage.addBlockInfo(l, "page", String.valueOf(1));
							}
							
							for (int i = 0; i < terminal_slots.length; i++) {
								int slot = terminal_slots[i];
								if (items.size() > i + (terminal_slots.length * (page - 1))) {
									final StoredItem item = items.get(i + (terminal_slots.length * (page - 1)));
									
									ItemStack stack = item.getItem().clone();
									ItemMeta im = stack.getItemMeta();
									List<String> lore = new ArrayList<String>();
									lore.add("");
									lore.add("&7Stored Items: &r" + DoubleHandler.getFancyDouble(item.getAmount()));
									if (stack.getMaxStackSize() > 1) lore.add("&7<Left Click: Request 1 | Right Click: Request " + (item.getAmount() > stack.getMaxStackSize() ? stack.getMaxStackSize(): item.getAmount()) + ">");
									else lore.add("&7<Left Click: Request 1>");
									lore.add("");
									if (im.hasLore()) {
										for (String line: im.getLore()) {
											lore.add(line);
										}
									}
									im.setLore(lore);
									stack.setItemMeta(im);
									menu.replaceExistingItem(slot, stack);
									menu.addMenuClickHandler(slot, new MenuClickHandler() {
										
										@Override
										public boolean onClick(Player p, int slot, ItemStack is, ClickAction action) {
											requests.add(new ItemRequest(l, 44, new CustomItem(item.getItem(), action.isRightClicked() ? (item.getAmount() > item.getItem().getMaxStackSize() ? item.getItem().getMaxStackSize(): item.getAmount()): 1), ItemTransportFlow.WITHDRAW));
											return false;
										}
									});
									
								}
								else {
									menu.replaceExistingItem(slot, terminal_noitem_item);
									menu.addMenuClickHandler(slot, terminal_noitem_handler);
								}
							}
							
							ItemStack sent_item = menu.getItemInSlot(17);
							if (sent_item != null) {
								requests.add(new ItemRequest(l, 17, sent_item, ItemTransportFlow.INSERT));
							}
						}
					}

					CargoHologram.update(b, "&7Status: &a&lONLINE");
				}
			});
		}
	}
	

	@SuppressWarnings("deprecation")
	private static Block getAttachedBlock(Block block) {
		if (block.getData() == 2) {
			return block.getRelative(BlockFace.SOUTH);
		}
		else if (block.getData() == 3) {
			return block.getRelative(BlockFace.NORTH);
		}
		else if (block.getData() == 4) {
			return block.getRelative(BlockFace.EAST);
		}
		else if (block.getData() == 5) {
			return block.getRelative(BlockFace.WEST);
		}
		return null;
	}

	private static int getFrequency(Location l) {
		int freq = 0;
		try {
			freq = Integer.parseInt(BlockStorage.getBlockInfo(l).getString("frequency"));
		} catch (Exception e) {}
		return freq;
	}
	
	public static Set<Location> scan(Location source, Set<Location> blocks, List<Location> l1, List<Location> l2, Axis exclude, Map<Location, Integer> input, Map<Integer, List<Location>> output, Set<Location> terminals, Set<Location> providers, Set<Location> destinations, Set<Location> imports, Set<Location> exports) {
		
		if (!exclude.equals(Axis.X_POSITIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX() + i + 1, source.getY(), source.getZ());
				if (!continueScan(source, l, l1, l2, Axis.X_NEGATIVE, blocks, input, output, terminals, providers, destinations, imports, exports)) return new HashSet<Location>();
			}
		}
		if (!exclude.equals(Axis.X_NEGATIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX() - i - 1, source.getY(), source.getZ());
				if (!continueScan(source, l, l1, l2, Axis.X_POSITIVE, blocks, input, output, terminals, providers, destinations, imports, exports)) return new HashSet<Location>();
			}
		}
		
		if (!exclude.equals(Axis.Y_POSITIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY() + i + 1, source.getZ());
				if (!continueScan(source, l, l1, l2, Axis.Y_NEGATIVE, blocks, input, output, terminals, providers, destinations, imports, exports)) return new HashSet<Location>();
			}
		}
		if (!exclude.equals(Axis.Y_NEGATIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY() - i - 1, source.getZ());
				if (!continueScan(source, l, l1, l2, Axis.Y_POSITIVE, blocks, input, output, terminals, providers, destinations, imports, exports)) return new HashSet<Location>();
			}
		}
		
		if (!exclude.equals(Axis.Z_POSITIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY(), source.getZ() + i + 1);
				if (!continueScan(source, l, l1, l2, Axis.Z_NEGATIVE, blocks, input, output, terminals, providers, destinations, imports, exports)) return new HashSet<Location>();
			}
		}
		if (!exclude.equals(Axis.Z_NEGATIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY(), source.getZ() - i - 1);
				if (!continueScan(source, l, l1, l2, Axis.Z_POSITIVE, blocks, input, output, terminals, providers, destinations, imports, exports)) return new HashSet<Location>();
			}
		}
		
		return blocks;
	}

	private static boolean continueScan(Location source, Location l, List<Location> l1, List<Location> l2, Axis axis, Set<Location> blocks, Map<Location, Integer> input, Map<Integer, List<Location>> output, Set<Location> terminals, Set<Location> providers, Set<Location> destinations, Set<Location> imports, Set<Location> exports) {
		if (!blocks.contains(l)) {
			String id = BlockStorage.checkID(l);
			if (id == null) return true;
			else if (id.equals("CARGO_MANAGER")) return false;
			else if (id.equals("CARGO_NODE")) {
				blocks.add(l);
				l1.add(source);
				l2.add(l);
				scan(l, blocks, l1, l2, axis, input, output, terminals, providers, destinations, imports, exports);
				if (blocks.isEmpty()) return false;
			}
			else if (id.equals("CARGO_NODE_INPUT")) {
				blocks.add(l);
				l1.add(source);
				l2.add(l);
				int freq = getFrequency(l);
				if (freq == 16) providers.add(l);
				else input.put(l, freq);
			}
			else if (id.equals("CHEST_TERMINAL")) {
				blocks.add(l);
				l1.add(source);
				l2.add(l);
				terminals.add(l);
			}
			else if (id.equals("CT_IMPORT_BUS")) {
				blocks.add(l);
				l1.add(source);
				l2.add(l);
				imports.add(l);
			}
			else if (id.equals("CT_EXPORT_BUS")) {
				blocks.add(l);
				l1.add(source);
				l2.add(l);
				exports.add(l);
			}
			else if (id.equals("CARGO_NODE_OUTPUT")) {
				blocks.add(l);
				l1.add(source);
				l2.add(l);
				int freq = getFrequency(l);

				if (freq == 16) destinations.add(l);
				else {
					List<Location> list = output.containsKey(freq) ? output.get(freq): new ArrayList<Location>();
					list.add(l);
					output.put(freq, list);
				}
			}
			else if (id.equals("CARGO_NODE_OUTPUT_ADVANCED")) {
				blocks.add(l);
				l1.add(source);
				l2.add(l);
				int freq = getFrequency(l);

				if (freq == 16) destinations.add(l);
				else {
					List<Location> list = output.containsKey(freq) ? output.get(freq): new ArrayList<Location>();
					list.add(l);
					output.put(freq, list);
				}
			}
		}
		return true;
	}
	
	public static boolean isConnected(Block b) {
		return passiveScan(b.getLocation(), Axis.UNKNOWN, new HashSet<Location>());
	}
	
	private static boolean passiveScan(Location source, Axis exclude, Set<Location> sources) {
		sources.add(source);
		Set<Location> blocks = new HashSet<Location>();
		
		blocks.add(source);
		
		if (!exclude.equals(Axis.X_POSITIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX() + i + 1, source.getY(), source.getZ());
				if (continuePassiveScan(l, Axis.X_NEGATIVE, sources)) {
					return true;
				}
			}
		}
		if (!exclude.equals(Axis.X_NEGATIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX() - i - 1, source.getY(), source.getZ());
				if (continuePassiveScan(l, Axis.X_POSITIVE, sources)) {
					return true;
				}
			}
		}
		
		if (!exclude.equals(Axis.Y_POSITIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY() + i + 1, source.getZ());
				if (continuePassiveScan(l, Axis.Y_NEGATIVE, sources)) {
					return true;
				}
			}
		}
		if (!exclude.equals(Axis.Y_NEGATIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY() - i - 1, source.getZ());
				if (continuePassiveScan(l, Axis.Y_POSITIVE, sources)) {
					return true;
				}
			}
		}
		
		if (!exclude.equals(Axis.Z_POSITIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY(), source.getZ() + i + 1);
				if (continuePassiveScan(l, Axis.Z_NEGATIVE, sources)) {
					return true;
				}
			}
		}
		if (!exclude.equals(Axis.Z_NEGATIVE)) {
			for (int i = 0; i <= RANGE; i++) {
				Location l = new Location(source.getWorld(), source.getX(), source.getY(), source.getZ() - i - 1);
				if (continuePassiveScan(l, Axis.Z_POSITIVE, sources)) {
					return true;
				}
			}
		}
		
		return false;
	}

	private static boolean continuePassiveScan(Location l, Axis axis, Set<Location> sources) {
		if (!sources.contains(l)) {
			String id = BlockStorage.checkID(l);
			if (id == null) return false;
			else if (id.equals("CARGO_MANAGER")) return true;
			else if (id.equals("CARGO_NODE")) return passiveScan(l, axis, sources);
		}
		return false;
	}

}
