package me.mrCookieSlime.Slimefun.utils;

import java.util.function.Consumer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule.Action;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public interface InventoryBlock {
	
	int[] getInputSlots();
	int[] getOutputSlots();
	
	default void createPreset(String id, String title, Consumer<BlockMenuPreset> setup) {
		new BlockMenuPreset(id, title) {
			
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
				return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), Action.ACCESS_INVENTORIES);
			}
		};
	}

}
