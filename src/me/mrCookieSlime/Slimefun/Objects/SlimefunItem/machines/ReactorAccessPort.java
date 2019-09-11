package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AReactor;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public class ReactorAccessPort extends SlimefunItem {

	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 12, 13, 14, 21, 23};
	private static final int[] border_1 = {9, 10, 11, 18, 20, 27, 29, 36, 38, 45, 46, 47};
	private static final int[] border_2 = {15, 16, 17, 24, 26, 33, 35, 42, 44, 51, 52, 53};
	private static final int[] border_3 = {30, 31, 32, 39, 41, 48, 49, 50};

	public ReactorAccessPort(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);

		new BlockMenuPreset(name, "&2Reactor Access Port") {

			@Override
			public void init() {
				constructMenu(this);
			}

			@Override
			public boolean canOpen(Block b, Player p) {
				if(p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES)) {
					AReactor reactor = getReactor(b.getLocation());
					if (reactor != null) {
						boolean empty = true;
						BlockMenu bm = getReactorMenu(b.getLocation());
						
						if (bm != null) {
							for (int slot: reactor.getCoolantSlots()) {
								if (bm.getItemInSlot(slot) != null) {
									empty = false;
								}
							}
							
							for (int slot: reactor.getFuelSlots()) {
								if (bm.getItemInSlot(slot) != null) {
									empty = false;
								}
							}

							if(!empty || !p.isSneaking()) {
								//reactor is not empty, lets view it's inventory instead.
								bm.open(p);
								return false;
							}
							
						}
						
						return true;

					} 
				}

				return false;
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				if (flow == ItemTransportFlow.INSERT) return getInputSlots();
				else return getOutputSlots();
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(BlockMenu menu, ItemTransportFlow flow, ItemStack item) {
				if (flow == ItemTransportFlow.INSERT) {
					if (SlimefunManager.isItemSimiliar(item, SlimefunItems.REACTOR_COOLANT_CELL, true)) return getCoolantSlots();
					else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.NETHER_ICE_COOLANT_CELL, true)) return getCoolantSlots();
					else return getFuelSlots();
				}
				else return getOutputSlots();
			}
		};

		registerBlockHandler(name, (p, b, tool, reason) -> {
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
			return true;
		});
	}

	private void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}

		for (int i : border_1) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}

		for (int i : border_2) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}

		for (int i : border_3) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}

		preset.addItem(1, new CustomItem(SlimefunItems.URANIUM, "&7Fuel Slot", "", "&rThis Slot accepts radioactive Fuel such as:", "&2Uranium &ror &aNeptunium"), (p, slot, item, action) -> false);
		preset.addItem(22, new CustomItem(SlimefunItems.PLUTONIUM, "&7Byproduct Slot", "", "&rThis Slot contains the Reactor's Byproduct", "&rsuch as &aNeptunium &ror &7Plutonium"), (p, slot, item, action) -> false);
		preset.addItem(7, new CustomItem(SlimefunItems.REACTOR_COOLANT_CELL, "&bCoolant Slot", "", "&rThis Slot accepts Coolant Cells", "&4Without any Coolant Cells, your Reactor", "&4will explode"),(p, slot, item, action) -> false);
	}

	public int[] getInputSlots() {
		return new int[] {19, 28, 37, 25, 34, 43};
	}

	public int[] getFuelSlots() {
		return new int[] {19, 28, 37};
	}

	public int[] getCoolantSlots() {
		return new int[] {25, 34, 43};
	}

	public static int[] getOutputSlots() {
		return new int[] {40};
	}

	public AReactor getReactor(Location l) {
		Location reactorL = new Location(l.getWorld(), l.getX(), l.getY() - 3, l.getZ());

		SlimefunItem item = BlockStorage.check(reactorL.getBlock());
		if (item instanceof AReactor) return (AReactor) item;

		return null;
	}

	public BlockMenu getReactorMenu(Location l) {
		Location reactorL = new Location(l.getWorld(), l.getX(), l.getY() - 3, l.getZ());

		SlimefunItem item = BlockStorage.check(reactorL.getBlock());
		if (item instanceof AReactor) return BlockStorage.getInventory(l);

		return null;
	}

	private static Inventory inject(Location l) {
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

	public static ItemStack pushItems(Location l, ItemStack item) {
		Inventory inv = inject(l);
		Map<Integer, ItemStack> map = inv.addItem(item);

		for (int slot: getOutputSlots()) {
			BlockStorage.getInventory(l).replaceExistingItem(slot, inv.getItem(slot));
		}
		
		return map.values().stream().findAny().orElse(null);
	}

}
