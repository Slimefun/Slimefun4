package me.mrCookieSlime.Slimefun.api.item_transport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import me.mrCookieSlime.Slimefun.api.network.Network;
import me.mrCookieSlime.Slimefun.api.network.NetworkComponent;
import me.mrCookieSlime.Slimefun.holograms.SimpleHologram;

public class CargoNet extends Network {
	
	public static boolean extraChannels = false;

	private static final int RANGE = 5;
	
	private static final int[] slots = new int[] {19, 20, 21, 28, 29, 30, 37, 38, 39};

	// Chest Terminal Stuff
	public static final int[] terminal_slots = new int[] {0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15, 18, 19, 20, 21, 22, 23, 24, 27, 28, 29, 30, 31, 32, 33, 36, 37, 38, 39, 40, 41, 42};
	public static final int TERMINAL_OUT_SLOT = 17;
	
	private static final ChestTerminalSorter sorter = new ChestTerminalSorter();
	private static final ItemStack terminal_noitem_item = new CustomItem(new ItemStack(Material.BARRIER), "&4No Item cached");
	private static final MenuClickHandler terminal_noitem_handler = (p, slot, item, action) -> false;

	public static CargoNet getNetworkFromLocation(Location l) {
		return getNetworkFromLocation(l, CargoNet.class);
	}

	public static CargoNet getNetworkFromLocationOrCreate(Location l) {
		CargoNet cargoNetwork = getNetworkFromLocation(l);
		if (cargoNetwork == null) {
			cargoNetwork = new CargoNet(l);
			registerNetwork(cargoNetwork);
		}
		return cargoNetwork;
	}

	@Deprecated
	public static boolean isConnected(Block b) {
		return getNetworkFromLocation(b.getLocation()) != null;
	}

	private Set<Location> inputNodes = new HashSet<>();
	private Set<Location> outputNodes = new HashSet<>();
	private Set<Location> advancedOutputNodes = new HashSet<>();


	//Chest Terminal Stuff
	private final Set<Location> terminals = new HashSet<>();
	private final Set<Location> imports = new HashSet<>();
	private final Set<Location> exports = new HashSet<>();

	protected CargoNet(Location l) {
		super(l);
	}

	public int getRange() {
		return RANGE;
	}

	public NetworkComponent classifyLocation(Location l) {
		String id = BlockStorage.checkID(l);
		if (id == null) return null;
		switch(id) {
			case "CARGO_MANAGER":
				return NetworkComponent.REGULATOR;
			case "CARGO_NODE":
				return NetworkComponent.CONNECTOR;
			case "CARGO_NODE_INPUT":
			case "CARGO_NODE_OUTPUT":
			case "CARGO_NODE_OUTPUT_ADVANCED":
			case "CT_IMPORT_BUS":
			case "CT_EXPORT_BUS":
			case "CHEST_TERMINAL":
				return NetworkComponent.TERMINUS;
			default:
				return null;
		}
	}

	public void locationClassificationChange(Location l, NetworkComponent from, NetworkComponent to) {
		if (from == NetworkComponent.TERMINUS) {
			inputNodes.remove(l);
			outputNodes.remove(l);
			advancedOutputNodes.remove(l);
			terminals.remove(l);
			imports.remove(l);
			exports.remove(l);
		}
		if (to == NetworkComponent.TERMINUS) {
			switch(BlockStorage.checkID(l)) {
				case "CARGO_NODE_INPUT":
					inputNodes.add(l);
					break;
				case "CARGO_NODE_OUTPUT":
					outputNodes.add(l);
					break;
				case "CARGO_NODE_OUTPUT_ADVANCED":
					advancedOutputNodes.add(l);
					break;
				case "CHEST_TERMINAL":
					terminals.add(l);
					break;
				case "CT_IMPORT_BUS":
					imports.add(l);
					break;
				case "CT_EXPORT_BUS":
					exports.add(l);
					break;
				default:
					break;
			}
		}
	}

	public void tick(final Block b) {
		if (!regulator.equals(b.getLocation())) {
			SimpleHologram.update(b, "&4Multiple Cargo Regulators connected");
			return;
		}
		super.tick();
		if (connectorNodes.isEmpty() && terminusNodes.isEmpty()) {
			SimpleHologram.update(b, "&7Status: &4&lOFFLINE");
		}
		else {
			SimpleHologram.update(b, "&7Status: &a&lONLINE");


			final Map<Integer, List<Location>> output = new HashMap<>();


			for (Location outputNode: outputNodes) {
				Integer frequency = getFrequency(outputNode);
				if (!output.containsKey(frequency)) {
					output.put(frequency, new ArrayList<Location>());
				}
				output.get(frequency).add(outputNode);
			}
			for (Location outputNode: advancedOutputNodes) {
				Integer frequency = getFrequency(outputNode);
				if (!output.containsKey(frequency)) {
					output.put(frequency, new ArrayList<Location>());
				}
				output.get(frequency).add(outputNode);
			}

			//Chest Terminal Stuff
			final Set<Location> providers = new HashSet<>();
			final Set<Location> destinations;
			if (output.containsKey(16)) {
				destinations = new HashSet<>(output.get(16));
			} else {
				destinations = new HashSet<>();
			}
			for (Location inputNode: inputNodes) {
				int frequency = getFrequency(inputNode);
				if (frequency == 16) {
					providers.add(inputNode);
				}
			}

			CargoNet self = this;
			final BlockStorage storage = BlockStorage.getStorage(b.getWorld());
			SlimefunPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
				if (BlockStorage.getLocationInfo(b.getLocation(), "visualizer") == null) {
					self.display();
				}
				//Chest Terminal Code
				if (extraChannels) {
					for (Location bus : imports) {
						BlockMenu menu = BlockStorage.getInventory(bus);

						if (menu.getItemInSlot(17) == null) {
							Block target = getAttachedBlock(bus.getBlock());
							ItemSlot stack = CargoManager.withdraw(bus.getBlock(), storage, target, -1);

							if (stack != null) {
								menu.replaceExistingItem(17, stack.getItem());
							}
						}

						if (menu.getItemInSlot(17) != null) {
							SlimefunPlugin.getUtilities().itemRequests.add(new ItemRequest(bus, 17, menu.getItemInSlot(17), ItemTransportFlow.INSERT));
						}
					}

					for (Location bus : exports) {
						BlockMenu menu = BlockStorage.getInventory(bus);

						if (menu.getItemInSlot(17) != null) {
							Block target = getAttachedBlock(bus.getBlock());

							menu.replaceExistingItem(17, CargoManager.insert(bus.getBlock(), storage, target, menu.getItemInSlot(17), -1));
						}

						if (menu.getItemInSlot(17) == null) {
							List<ItemStack> items = new ArrayList<>();
							for (int slot: slots) {
								ItemStack template = menu.getItemInSlot(slot);
								if (template != null) items.add(new CustomItem(template, 1));
							}

							if (!items.isEmpty()) {
								int index = Integer.parseInt(BlockStorage.getLocationInfo(bus, "index"));

								index++;
								if (index > (items.size() - 1)) index = 0;

								BlockStorage.addBlockInfo(bus, "index", String.valueOf(index));

								SlimefunPlugin.getUtilities().itemRequests.add(new ItemRequest(bus, 17, items.get(index), ItemTransportFlow.WITHDRAW));
							}
						}
					}
					
											for (final Location terminal : terminals) {
							BlockMenu menu = BlockStorage.getInventory(terminal);

							ItemStack sendingItem = menu.getItemInSlot(TERMINAL_OUT_SLOT);
							if (sendingItem != null) {
								SlimefunPlugin.getUtilities().itemRequests.add(new ItemRequest(terminal, TERMINAL_OUT_SLOT, sendingItem, ItemTransportFlow.INSERT));
							}
						}

					Iterator<ItemRequest> iterator = SlimefunPlugin.getUtilities().itemRequests.iterator();
					while (iterator.hasNext()) {
						ItemRequest request = iterator.next();
						if (terminals.contains(request.getTerminal()) || imports.contains(request.getTerminal()) || exports.contains(request.getTerminal())) {
							BlockMenu menu = BlockStorage.getInventory(request.getTerminal());

							switch (request.getDirection()) {
							case INSERT:
								ItemStack requestedItem = request.getItem();
								
								for (Location l: destinations) {
									Block target = getAttachedBlock(l.getBlock());
									requestedItem = CargoManager.insert(l.getBlock(), storage, target, requestedItem, -1);
									if (requestedItem == null) {
										menu.replaceExistingItem(request.getSlot(), null);
										break;
									}
								}

								if (requestedItem != null) {
									menu.replaceExistingItem(request.getSlot(), requestedItem);
								}

								iterator.remove();
								break;
							case WITHDRAW:
								int slot = request.getSlot();
								ItemStack prevStack = menu.getItemInSlot(slot);
								if (!(prevStack == null || (prevStack.getAmount() + request.getItem().getAmount() <= prevStack.getMaxStackSize() && SlimefunManager.isItemSimiliar(prevStack, new CustomItem(request.getItem(), 1), true)))) {
									iterator.remove();
									break;
								}

								ItemStack stack = null;
								ItemStack requested = request.getItem();
								
								for (Location l : providers) {
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
											break;
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
							default:
								break;
							}
						}
					}
				}
				// All operations happen here: Everything gets iterated from the Input Nodes. (Apart from ChestTerminal Buses)
				for (Location input: inputNodes) {
					Integer frequency = getFrequency(input);
					
					if (frequency < 0 || frequency > 15) {
						continue;
					}
					
					Block inputTarget = getAttachedBlock(input.getBlock());
					ItemStack stack = null;
					int previousSlot = -1;

					boolean roundrobin = BlockStorage.getLocationInfo(input, "round-robin").equals("true");

					if (inputTarget != null) {
						ItemSlot slot = CargoManager.withdraw(input.getBlock(), storage, inputTarget, Integer.parseInt(BlockStorage.getLocationInfo(input, "index")));
						if (slot != null) {
							stack = slot.getItem();
							previousSlot = slot.getSlot();
						}
					}

					if (stack != null && output.containsKey(frequency)) {
						List<Location> outputlist = new ArrayList<>(output.get(frequency));

						if (roundrobin) {
							if (!SlimefunPlugin.getUtilities().roundRobin.containsKey(input)) {
								SlimefunPlugin.getUtilities().roundRobin.put(input, 0);
							}

							int cIndex = SlimefunPlugin.getUtilities().roundRobin.get(input);

							if (cIndex < outputlist.size()) {
								for (int i = 0; i < cIndex; i++) {
									final Location temp = outputlist.get(0);
									outputlist.remove(temp);
									outputlist.add(temp);
								}
								cIndex++;
							}
							else cIndex = 1;

							SlimefunPlugin.getUtilities().roundRobin.put(input, cIndex);
						}
						
						for (Location out : outputlist) {
							Block target = getAttachedBlock(out.getBlock());
							if (target != null) {
								stack = CargoManager.insert(out.getBlock(), storage, target, stack, -1);
								if (stack == null) break;
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
				if (extraChannels) {
					List<StoredItem> items = new ArrayList<>();
					for (Location l: providers) {
						Block target = getAttachedBlock(l.getBlock());
						if (storage.hasUniversalInventory(target)) {
							UniversalBlockMenu menu = storage.getUniversalInventory(target);
							for (int slot: menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
								ItemStack is = menu.getItemInSlot(slot);
								if (is != null && CargoManager.matchesFilter(l.getBlock(), is, -1)) {
									boolean add = true;
									for (StoredItem item: items) {
										if (SlimefunManager.isItemSimiliar(is, item.getItem(), true)) {
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
							if (BlockStorage.checkID(target.getLocation()).startsWith("BARREL_") && BlockStorage.getLocationInfo(target.getLocation(), "storedItems") != null) {
								int stored = Integer.parseInt(BlockStorage.getLocationInfo(target.getLocation(), "storedItems"));
								for (int slot: menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
									ItemStack is = menu.getItemInSlot(slot);
									if (is != null && CargoManager.matchesFilter(l.getBlock(), is, -1)) {
										boolean add = true;
										for (StoredItem item: items) {
											if (SlimefunManager.isItemSimiliar(is, item.getItem(), true)) {
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
											if (SlimefunManager.isItemSimiliar(is, item.getItem(), true)) {
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
										if (SlimefunManager.isItemSimiliar(is, item.getItem(), true)) {
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
						int page = Integer.parseInt(BlockStorage.getLocationInfo(l, "page"));
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
								List<String> lore = new ArrayList<>();
								lore.add("");
								lore.add(ChatColor.translateAlternateColorCodes('&', "&7Stored Items: &r" + DoubleHandler.getFancyDouble(item.getAmount())));
								if (stack.getMaxStackSize() > 1) lore.add(ChatColor.translateAlternateColorCodes('&', "&7<Left Click: Request 1 | Right Click: Request " + (item.getAmount() > stack.getMaxStackSize() ? stack.getMaxStackSize(): item.getAmount()) + ">"));
								else lore.add(ChatColor.translateAlternateColorCodes('&', "&7<Left Click: Request 1>"));
								lore.add("");
								if (im.hasLore()) {
									for (String line: im.getLore()) {
										lore.add(line);
									}
								}
								im.setLore(lore);
								stack.setItemMeta(im);
								menu.replaceExistingItem(slot, stack);
								menu.addMenuClickHandler(slot, (p, sl, is, action) -> {
									SlimefunPlugin.getUtilities().itemRequests.add(new ItemRequest(l, 44, new CustomItem(item.getItem(), action.isRightClicked() ? (item.getAmount() > item.getItem().getMaxStackSize() ? item.getItem().getMaxStackSize(): item.getAmount()): 1), ItemTransportFlow.WITHDRAW));
									return false;
								});

							}
							else {
								menu.replaceExistingItem(slot, terminal_noitem_item);
								menu.addMenuClickHandler(slot, terminal_noitem_handler);
							}
						}
					}
				}
			});
		}
	}

	private static Block getAttachedBlock(Block block) {
		if (block.getBlockData() instanceof Directional) {
			return block.getRelative(((Directional) block.getBlockData()).getFacing().getOppositeFace());
		}
		return null;
	}

	private static int getFrequency(Location l) {
		int freq = 0;
		try {
			String str = BlockStorage.getLocationInfo(l).getString("frequency");
			if (str != null) freq = Integer.parseInt(str);
		} catch (Exception x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occured while parsing a Cargo Node Frequency", x);
		}
		return freq;
	}

}
