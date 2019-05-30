package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public class XPCollector extends SlimefunItem {
	
	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};

	public XPCollector(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
		
		new BlockMenuPreset(name, "&aEXP Collector") {
			
			@Override
			public void init() {
				constructMenu(this);
			}

			@Override
			public void newInstance(BlockMenu menu, Block b) {
			}

			@Override
			public boolean canOpen(Block b, Player p) {
				return p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true);
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				if (flow.equals(ItemTransportFlow.WITHDRAW)) return getOutputSlots();
				return new int[0];
			}
		};
		
		registerBlockHandler(name, new SlimefunBlockHandler() {
			
			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}
			
			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				me.mrCookieSlime.Slimefun.holograms.XPCollector.getArmorStand(b).remove();
				BlockMenu inv = BlockStorage.getInventory(b);
				if (inv != null) {
					for (int slot: getOutputSlots()) {
						if (inv.getItemInSlot(slot) != null) {
							b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
							inv.replaceExistingItem(slot, null);
						}
					}
				}
				return true;
			}
		});
	}
	
	private Inventory inject(Block b) {
		int size = BlockStorage.getInventory(b).toInventory().getSize();
		Inventory inv = Bukkit.createInventory(null, size);
		for (int i = 0; i < size; i++) {
			inv.setItem(i, new CustomItem(Material.COMMAND_BLOCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
		}
		for (int slot : getOutputSlots()) {
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

	public int[] getOutputSlots() {
		return new int[] {12, 13, 14};
	}
	
	protected void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE), " "),
				(p, slot, item, action) -> false
			);
		}
	}
	
	public int getEnergyConsumption() {
		return 10;
	}
	
	@Override
	public void register(boolean slimefun) {
		addItemHandler(new BlockTicker() {
			
			@Override
			public void tick(Block b, SlimefunItem sf, Config data) {
				try {
					XPCollector.this.tick(b);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
	
	protected void tick(Block b) throws Exception {
		Iterator<Entity> iterator = me.mrCookieSlime.Slimefun.holograms.XPCollector.getArmorStand(b).getNearbyEntities(4D, 4D, 4D).iterator();
		while (iterator.hasNext()) {
			Entity n = iterator.next();
			if (n instanceof ExperienceOrb) {
				if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
				
				if (n.isValid()) {
					int xp = getEXP(b) + ((ExperienceOrb) n).getExperience();
					
					ChargableBlock.addCharge(b, -getEnergyConsumption());
					n.remove();
					
					int withdrawn = 0;
					for (int level = 0; level < getEXP(b); level = level + 10) {
						if (fits(b, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"))) {
							withdrawn = withdrawn + 10;
							pushItems(b, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"));
						}
					}
					BlockStorage.addBlockInfo(b, "stored-exp", String.valueOf(xp - withdrawn));
					
					return;
				}
			}
		}
	}

	private int getEXP(Block b) {
		Config cfg = BlockStorage.getLocationInfo(b.getLocation());
		if (cfg.contains("stored-exp")) return Integer.parseInt(cfg.getString("stored-exp"));
		else {
			BlockStorage.addBlockInfo(b, "stored-exp", "0");
			return 0;
		}
	}

}
