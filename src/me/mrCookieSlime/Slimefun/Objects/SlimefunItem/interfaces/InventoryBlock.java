package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public interface InventoryBlock {
	
	int[] getInputSlots();
	int[] getOutputSlots();
	
	default void createPreset(SlimefunItem item, String title, Consumer<BlockMenuPreset> setup) {
		new BlockMenuPreset(item.getID(), title) {
			
			@Override
			public void init() {
				setup.accept(this);
			}
			
			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				if (flow == ItemTransportFlow.INSERT) return getInputSlots();
				else return getOutputSlots();
			}
			
			@Override
			public boolean canOpen(Block b, Player p) {
				return p.hasPermission("slimefun.inventory.bypass") || (SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES) && Slimefun.hasUnlocked(p, item, false));
			}
		};
	}
	
	default Inventory inject(Block b) {
		int size = getOutputSlots().length;
		Inventory inv = Bukkit.createInventory(null, ((int) Math.ceil(size / 9F)) * 9);
		
		for (int i = 0; i < inv.getSize(); i++) {
			if (i < size) {
				inv.setItem(i, BlockStorage.getInventory(b).getItemInSlot(getOutputSlots()[i]));
			}
			else {
				inv.setItem(i, new CustomItem(Material.COMMAND_BLOCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
			}
		}
		
		return inv;
	}
	
	default boolean fits(Block b, ItemStack... items) {
		return inject(b).addItem(items).isEmpty();
	}
	
	default void pushItems(Block b, ItemStack... items) {
		Inventory inv = inject(b);
		inv.addItem(items);
		
		for (int i = 0; i < getOutputSlots().length; i++) {
			BlockStorage.getInventory(b).replaceExistingItem(getOutputSlots()[i], inv.getItem(i));
		}
	}

}
