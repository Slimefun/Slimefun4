package me.mrCookieSlime.Slimefun.listeners;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Recipe.RecipeCalculator;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.EnhancedFurnace;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class FurnaceListener implements Listener {

	public FurnaceListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBurn(FurnaceBurnEvent e) {
		SlimefunItem furnace = BlockStorage.check(e.getBlock());

		if (furnace instanceof EnhancedFurnace && ((EnhancedFurnace) furnace).getFuelEfficiency() > 0) {
			e.setBurnTime(((EnhancedFurnace) furnace).getFuelEfficiency() * e.getBurnTime());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSmelt(FurnaceSmeltEvent e) {
		SlimefunItem furnace = BlockStorage.check(e.getBlock());

		if (furnace instanceof EnhancedFurnace) {
			Furnace f = (Furnace) e.getBlock().getState();
			int amount = f.getInventory().getSmelting().getType().toString().endsWith("_ORE") ? ((EnhancedFurnace) furnace).getOutput() : 1;
			ItemStack result = f.getInventory().getResult() == null ? RecipeCalculator.getSmeltedOutput(f.getInventory().getSmelting().getType()) : f.getInventory().getResult().clone();

			if (result != null) {
				f.getInventory().setResult(new CustomItem(result, Math.min(result.getAmount() + amount, result.getMaxStackSize())));
			}
		}
	}

}
