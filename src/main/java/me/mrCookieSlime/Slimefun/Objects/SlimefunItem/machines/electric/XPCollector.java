package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.Iterator;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class XPCollector extends SlimefunItem implements InventoryBlock {
	
	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};

	protected int energyConsumption = 10;
	
	public XPCollector(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
		createPreset(this, "&aEXP Collector", this::constructMenu);
		
		registerBlockHandler(name, new SlimefunBlockHandler() {
			
			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}
			
			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
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

	@Override
	public int[] getInputSlots() {
		return new int[0];
	}

	@Override
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
	
	@Override
	public void preRegister() {
		addItemHandler(new BlockTicker() {
			
			@Override
			public void tick(Block b, SlimefunItem sf, Config data) {
				try {
					XPCollector.this.tick(b);
				} catch (Exception x) {
					Slimefun.getLogger().log(Level.SEVERE, "An Error occured while ticking an Exp Collector for Slimefun " + Slimefun.getVersion(), x);
				}
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		});
	}
	
	protected void tick(Block b) {
		Iterator<Entity> iterator = b.getWorld().getNearbyEntities(b.getLocation(), 4.0, 4.0, 4.0, n -> n instanceof ExperienceOrb && n.isValid()).iterator();
		int xp = 0;
		
		while (iterator.hasNext() && xp == 0) {
			Entity n = iterator.next();
			if (ChargableBlock.getCharge(b) < energyConsumption) return;
			
			xp = getEXP(b) + ((ExperienceOrb) n).getExperience();
			
			ChargableBlock.addCharge(b, -energyConsumption);
			n.remove();
			
			int withdrawn = 0;
			for (int level = 0; level < getEXP(b); level = level + 10) {
				if (fits(b, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"))) {
					withdrawn = withdrawn + 10;
					pushItems(b, new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge"));
				}
			}
			BlockStorage.addBlockInfo(b, "stored-exp", String.valueOf(xp - withdrawn));
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
