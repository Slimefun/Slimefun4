package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.item_transport.CargoNet;
import me.mrCookieSlime.Slimefun.holograms.SimpleHologram;

public class CargoManagerBlock extends SlimefunItem {

	public CargoManagerBlock(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
		
		registerBlockHandler(getID(), (p, b, tool, reason) -> {
			SimpleHologram.remove(b);
			return true;
		});
	}
	
	@Override
	public void preRegister() {
		addItemHandler(new BlockTicker() {

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				CargoNet.getNetworkFromLocationOrCreate(b.getLocation()).tick(b);
			}

			@Override
			public boolean isSynchronized() {
				return false;
			}
			
		}, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				String item = BlockStorage.checkID(e.getClickedBlock());
				if (item == null || !item.equals(getID())) return false;
				e.setCancelled(true);

				if (BlockStorage.getLocationInfo(e.getClickedBlock().getLocation(), "visualizer") == null) {
					BlockStorage.addBlockInfo(e.getClickedBlock(), "visualizer", "disabled");
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCargo Net Visualizer: " + "&4\u2718"));
				}
				else {
					BlockStorage.addBlockInfo(e.getClickedBlock(), "visualizer", null);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCargo Net Visualizer: " + "&2\u2714"));
				}
				return true;
			}
		});
	}

}
