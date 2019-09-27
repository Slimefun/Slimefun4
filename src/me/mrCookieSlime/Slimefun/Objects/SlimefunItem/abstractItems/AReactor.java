package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.ReactorAccessPort;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.holograms.ReactorHologram;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

public abstract class AReactor extends SlimefunItem implements RecipeDisplayItem {

	public static Map<Location, MachineFuel> processing = new HashMap<>();
	public static Map<Location, Integer> progress = new HashMap<>();

	private static final BlockFace[] cooling =
		{
				BlockFace.NORTH,
				BlockFace.NORTH_EAST,
				BlockFace.EAST,
				BlockFace.SOUTH_EAST,
				BlockFace.SOUTH,
				BlockFace.SOUTH_WEST,
				BlockFace.WEST,
				BlockFace.NORTH_WEST
		};

	private Set<MachineFuel> recipes = new HashSet<>();

	private static final int[] border = {0, 1, 2, 3, 5, 6, 7, 8, 12, 13, 14, 21, 23};
	private static final int[] border_1 = {9, 10, 11, 18, 20, 27, 29, 36, 38, 45, 46, 47};
	private static final int[] border_2 = {15, 16, 17, 24, 26, 33, 35, 42, 44, 51, 52, 53};
	private static final int[] border_3 = {30, 31, 32, 39, 41, 48, 50};
	
	// No coolant border
	private static final int[] border_4 = {25, 34, 43}; 
	private static final int infoSlot = 49;

	public AReactor(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);

		new BlockMenuPreset(id, getInventoryTitle()) {

			@Override
			public void init() {
				constructMenu(this);
			}

			@Override
			public void newInstance(final BlockMenu menu, final Block b) {
				try {
					if (BlockStorage.getLocationInfo(b.getLocation(), "reactor-mode") == null){
						BlockStorage.addBlockInfo(b, "reactor-mode", "generator");
					}
					
					if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "reactor-mode").equals("generator")) {
						menu.replaceExistingItem(4, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&7Focus: &eElectricity", "", "&6Your Reactor will focus on Power Generation", "&6If your Energy Network doesn't need Power", "&6it will not produce any either", "", "&7> Click to change the Focus to &eProduction"));
						menu.addMenuClickHandler(4, (p, slot, item, action) -> {
							BlockStorage.addBlockInfo(b, "reactor-mode", "production");
							newInstance(menu, b);
							return false;
						});
					}
					else {
						menu.replaceExistingItem(4, new CustomItem(SlimefunItems.PLUTONIUM, "&7Focus: &eProduction", "", "&6Your Reactor will focus on producing goods", "&6If your Energy Network doesn't need Power", "&6it will continue to run and simply will", "&6not generate any Power in the mean time", "", "&7> Click to change the Focus to &ePower Generation"));
						menu.addMenuClickHandler(4, (p, slot, item, action) -> {
							BlockStorage.addBlockInfo(b, "reactor-mode", "generator");
							newInstance(menu, b);
							return false;
						});
					}
					
					BlockMenu port = getAccessPort(b.getLocation());
					if (port != null) {
						menu.replaceExistingItem(infoSlot, new CustomItem(new ItemStack(Material.GREEN_WOOL), "&7Access Port", "", "&6Detected", "", "&7> Click to view Access Port"));
						menu.addMenuClickHandler(infoSlot, (p, slot, item, action) -> {
							port.open(p);
							newInstance(menu, b);

							return false;
						});
					} 
					else {
						menu.replaceExistingItem(infoSlot, new CustomItem(new ItemStack(Material.RED_WOOL), "&7Access Port", "", "&cNot detected", "", "&7Access Port must be", "&7placed 3 blocks above", "&7a reactor!"));
						menu.addMenuClickHandler(infoSlot, (p, slot, item, action) -> {
							newInstance(menu, b);
							menu.open(p);
							return false;
						});
					}

				} catch(Exception x) {
					Slimefun.getLogger().log(Level.SEVERE, "An Error occured when creating a Reactor Menu for Slimefun " + Slimefun.getVersion(), x);
				}
			}


			@Override
			public boolean canOpen(Block b, Player p) {
				return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				return new int[0];
			}
		};

		registerBlockHandler(id, (p, b, tool, reason) -> {
			BlockMenu inv = BlockStorage.getInventory(b);
			if (inv != null) {
				for (int slot : getFuelSlots()) {
					if (inv.getItemInSlot(slot) != null) {
						b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
						inv.replaceExistingItem(slot, null);
					}
				}
				
				for (int slot : getCoolantSlots()) {
					if (inv.getItemInSlot(slot) != null) {
						b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
						inv.replaceExistingItem(slot, null);
					}
				}
				
				for (int slot : getOutputSlots()) {
					if (inv.getItemInSlot(slot) != null) {
						b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
						inv.replaceExistingItem(slot, null);
					}
				}
			}
			
			progress.remove(b.getLocation());
			processing.remove(b.getLocation());
			ReactorHologram.remove(b.getLocation());
			return true;
		});

		this.registerDefaultRecipes();
	}

	private void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}

		for (int i : border_1) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}

		for (int i : border_3) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}

		preset.addItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);

		preset.addItem(1, new CustomItem(SlimefunItems.URANIUM, "&7Fuel Slot", "", "&rThis Slot accepts radioactive Fuel such as:", "&2Uranium &ror &aNeptunium"), (p, slot, item, action) -> false);

		for (int i : border_2) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}

		if (needsCooling()) {
			preset.addItem(7, new CustomItem(this.getCoolant(), "&bCoolant Slot", "", "&rThis Slot accepts Coolant Cells", "&4Without any Coolant Cells, your Reactor", "&4will explode"));
		}
		else {
			preset.addItem(7, new CustomItem(new ItemStack(Material.BARRIER), "&bCoolant Slot", "", "&rThis Slot accepts Coolant Cells"));

			for (int i : border_4) {
				preset.addItem(i, new CustomItem(new ItemStack(Material.BARRIER), "&cNo Coolant Required"), (p, slot, item, action) -> false);
			}
		}
	}

	public abstract String getInventoryTitle();

	public abstract void registerDefaultRecipes();

	public abstract int getEnergyProduction();

	public abstract void extraTick(Location l);

	public abstract ItemStack getCoolant();

	public boolean needsCooling() {
		return getCoolant() != null;
	}

	public int[] getInputSlots() {
		return new int[] {19, 28, 37, 25, 34, 43};
	}

	public int[] getFuelSlots() {
		return new int[] {19, 28, 37};
	}

	public int[] getCoolantSlots() {
		return needsCooling() ? new int[] {25, 34, 43} : new int[]{};
	}

	public int[] getOutputSlots() {
		return new int[] {40};
	}

	public MachineFuel getProcessing(Location l) {
		return processing.get(l);
	}

	public boolean isProcessing(Location l) {
		return progress.containsKey(l);
	}

	public void registerFuel(MachineFuel fuel) {
		this.recipes.add(fuel);
	}

	@Override
	public void preRegister() {
		addItemHandler(new EnergyTicker() {

			private Set<Location> explode = new HashSet<>();

			@Override
			public double generateEnergy(final Location l, SlimefunItem sf, Config data) {
				BlockMenu port = getAccessPort(l);

				if (isProcessing(l)) {
					extraTick(l);
					int timeleft = progress.get(l);
					if (timeleft > 0) {
						int produced = getEnergyProduction();
						int space = ChargableBlock.getMaxCharge(l) - ChargableBlock.getCharge(l);
						if (space >= produced) {
							ChargableBlock.addCharge(l, getEnergyProduction());
							space -= produced;
						}
						if (space >= produced || !BlockStorage.getLocationInfo(l, "reactor-mode").equals("generator")) {
							progress.put(l, timeleft - 1);

							Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
								if (!l.getBlock().getRelative(cooling[new Random().nextInt(cooling.length)]).isLiquid()) explode.add(l);
							});

							MachineHelper.updateProgressbar(BlockStorage.getInventory(l), 22, timeleft, processing.get(l).getTicks(), getProgressBar());

							if (needsCooling()) {
								boolean coolant = (processing.get(l).getTicks() - timeleft) % 25 == 0;

								if (coolant) {
									if (port != null) {
										for (int slot: getCoolantSlots()) {
											if (SlimefunManager.isItemSimiliar(port.getItemInSlot(slot), getCoolant(), true)) {
												port.replaceExistingItem(slot, pushItems(l, port.getItemInSlot(slot), getCoolantSlots()));
											}
										}
									}

									boolean explosion = true;
									for (int slot: getCoolantSlots()) {
										if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(l).getItemInSlot(slot), getCoolant(), true)) {
											BlockStorage.getInventory(l).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(l).getItemInSlot(slot), 1));
											ReactorHologram.update(l, "&b\u2744 &7100%");
											explosion = false;
											break;
										}
									}

									if (explosion) {
										explode.add(l);
										return 0;
									}
								}
								else {
									ReactorHologram.update(l, "&b\u2744 &7" + MachineHelper.getPercentage(timeleft, processing.get(l).getTicks()) + "%");
								}
							}

							return ChargableBlock.getCharge(l);
						}
						return 0;
					}
					else {
						BlockStorage.getInventory(l).replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
						if (processing.get(l).getOutput() != null) pushItems(l, processing.get(l).getOutput());

						if (port != null) {
							for (int slot: getOutputSlots()) {
								if (BlockStorage.getInventory(l).getItemInSlot(slot) != null) BlockStorage.getInventory(l).replaceExistingItem(slot, ReactorAccessPort.pushItems(port.getLocation(), BlockStorage.getInventory(l).getItemInSlot(slot)));
							}
						}

						progress.remove(l);
						processing.remove(l);
						return 0;
					}
				}
				else {
					BlockMenu menu = BlockStorage.getInventory(l);
					Map<Integer, Integer> found = new HashMap<>();
					MachineFuel fuel = findRecipe(menu, found);

					if (port != null) restockCoolant(l, port);

					if (fuel != null) {
						for (Map.Entry<Integer, Integer> entry: found.entrySet()) {
							menu.replaceExistingItem(entry.getKey(), InvUtils.decreaseItem(menu.getItemInSlot(entry.getKey()), entry.getValue()));
						}
						
						processing.put(l, fuel);
						progress.put(l, fuel.getTicks());
					}
					return 0;
				}
			}

			@Override
			public boolean explode(final Location l) {
				final boolean explosion = explode.contains(l);
				if (explosion) {
					BlockStorage.getInventory(l).close();

					Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> ReactorHologram.remove(l), 0);

					explode.remove(l);
					processing.remove(l);
					progress.remove(l);
				}
				return explosion;
			}
		});
	}
	
	private void restockCoolant(Location l, BlockMenu port) {
		for (int slot: getFuelSlots()) {
			for (MachineFuel recipe: recipes) {
				if (SlimefunManager.isItemSimiliar(port.getItemInSlot(slot), recipe.getInput(), true) && pushItems(l, new CustomItem(port.getItemInSlot(slot), 1), getFuelSlots()) == null) {
					port.replaceExistingItem(slot, InvUtils.decreaseItem(port.getItemInSlot(slot), 1));
					return;
				}
			}
		}
	}
	
	private MachineFuel findRecipe(BlockMenu menu, Map<Integer, Integer> found) {
		for (MachineFuel recipe: recipes) {
			for (int slot: getInputSlots()) {
				if (SlimefunManager.isItemSimiliar(menu.getItemInSlot(slot), recipe.getInput(), true)) {
					found.put(slot, recipe.getInput().getAmount());
					return recipe;
				}
			}
		}
		
		return null;
	}

	private Inventory inject(Location l) {
		int size = BlockStorage.getInventory(l).toInventory().getSize();
		Inventory inv = Bukkit.createInventory(null, size);
		for (int i = 0; i < size; i++) {
			inv.setItem(i, new CustomItem(Material.COMMAND_BLOCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
		}
		for (int slot : getOutputSlots()) {
			inv.setItem(slot, BlockStorage.getInventory(l).getItemInSlot(slot));
		}
		return inv;
	}

	private Inventory inject(Location l, int[] slots) {
		int size = BlockStorage.getInventory(l).toInventory().getSize();
		Inventory inv = Bukkit.createInventory(null, size);
		for (int i = 0; i < size; i++) {
			inv.setItem(i, new CustomItem(Material.COMMAND_BLOCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
		}
		for (int slot : slots) {
			inv.setItem(slot, BlockStorage.getInventory(l).getItemInSlot(slot));
		}
		return inv;
	}

	public void pushItems(Location l, ItemStack item) {
		Inventory inv = inject(l);
		inv.addItem(item);

		for (int slot: getOutputSlots()) {
			BlockStorage.getInventory(l).replaceExistingItem(slot, inv.getItem(slot));
		}
	}

	public ItemStack pushItems(Location l, ItemStack item, int[] slots) {
		Inventory inv = inject(l, slots);
		Optional<ItemStack> optional = inv.addItem(item).values().stream().findFirst();

		for (int slot : slots) {
			BlockStorage.getInventory(l).replaceExistingItem(slot, inv.getItem(slot));
		}

		if (optional.isPresent()) return optional.get();
		else return null;
	}

	public abstract ItemStack getProgressBar();

	public Set<MachineFuel> getFuelTypes() {
		return this.recipes;
	}

	public BlockMenu getAccessPort(Location l) {
		Location portL = new Location(l.getWorld(), l.getX(), l.getY() + 3, l.getZ());

		if (BlockStorage.check(portL, "REACTOR_ACCESS_PORT")) return BlockStorage.getInventory(portL);
		return null;
	}
	
	@Override
	public String getRecipeSectionLabel() {
		return "&7\u21E9 Available Types of Fuel \u21E9";
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> list = new ArrayList<>();
		
		for (MachineFuel fuel: recipes) {
			ItemStack item = fuel.getInput().clone();
			ItemMeta im = item.getItemMeta();
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &7Lasts " + getTimeLeft(fuel.getTicks() / 2)));
			lore.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &e\u26A1 &7" + getEnergyProduction() * 2) + " J/s");
			lore.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &e\u26A1 &7" + DoubleHandler.getFancyDouble((double) fuel.getTicks() * getEnergyProduction()) + " J in total"));
			im.setLore(lore);
			item.setItemMeta(im);
			list.add(item);
		}
		
		if (list.size() % 2 != 0) list.add(null);
		return list;
	}
	
	private static String getTimeLeft(int seconds) {
		String timeleft = "";
        final int minutes = (int) (seconds / 60L);
        if (minutes > 0) {
            timeleft += minutes + "m ";
        }
        seconds -= minutes * 60;
        return "&7" + timeleft + seconds + "s";
	}

}
