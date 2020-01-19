package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.cargo;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.item_transport.CargoNet;

public class CargoManagerBlock extends SlimefunItem {

	public CargoManagerBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
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
				Block b = e.getClickedBlock();
				if (b == null) return false;
				
				String item = BlockStorage.checkID(b);
				if (item == null || !item.equals(getID())) return false;
				e.setCancelled(true);

				if (BlockStorage.getLocationInfo(b.getLocation(), "visualizer") == null) {
					BlockStorage.addBlockInfo(b, "visualizer", "disabled");
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCargo Net Visualizer: " + "&4\u2718"));
				}
				else {
					BlockStorage.addBlockInfo(b, "visualizer", null);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCargo Net Visualizer: " + "&2\u2714"));
				}
				return true;
			}
		});
	}

}
