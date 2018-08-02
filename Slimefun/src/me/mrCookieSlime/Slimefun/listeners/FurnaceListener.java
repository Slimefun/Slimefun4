package me.mrCookieSlime.Slimefun.listeners;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Recipe.RecipeCalculator;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.EnhancedFurnace;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public class FurnaceListener implements Listener {
	
	public FurnaceListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBurn(FurnaceBurnEvent e) {
		if (BlockStorage.check(e.getBlock()) != null && BlockStorage.check(e.getBlock()) instanceof EnhancedFurnace) {
			EnhancedFurnace furnace = (EnhancedFurnace) BlockStorage.check(e.getBlock());
			if (furnace.getFuelEfficiency() > 0) e.setBurnTime(((int) ((1.0 + 0.2 * furnace.getFuelEfficiency()) * e.getBurnTime())));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSmelt(FurnaceSmeltEvent e) {
		if (BlockStorage.check(e.getBlock()) != null && BlockStorage.check(e.getBlock()) instanceof EnhancedFurnace) {
			EnhancedFurnace furnace = (EnhancedFurnace) BlockStorage.check(e.getBlock());
			Furnace f = (Furnace) e.getBlock().getState();
			int amount = f.getInventory().getSmelting().getType().toString().endsWith("_ORE") ? furnace.getOutput(): 1;
			ItemStack output = RecipeCalculator.getSmeltedOutput(f.getInventory().getSmelting().getType());
			ItemStack result = f.getInventory().getResult();
			if (result != null) result = result.clone();
			f.getInventory().setResult(null);
			if (result != null) f.getInventory().setResult(new CustomItem(result, result.getAmount() + amount > result.getMaxStackSize() ? result.getMaxStackSize(): result.getAmount() + amount));
			else f.getInventory().setResult(new CustomItem(output, output.getAmount() + amount > output.getType().getMaxStackSize() ? output.getType().getMaxStackSize(): output.getAmount() + amount));
		}
	}

}
