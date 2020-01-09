package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Optional;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.recipes.MinecraftRecipe;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.EnhancedFurnace;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class EnhancedFurnaceListener implements Listener {

	public EnhancedFurnaceListener(SlimefunPlugin plugin) {
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
			Optional<ItemStack> result = Optional.ofNullable(f.getInventory().getResult());
			
			if (!result.isPresent()) {
				result = SlimefunPlugin.getMinecraftRecipes().getRecipeOutput(MinecraftRecipe.FURNACE, f.getInventory().getSmelting());
			}

			if (result.isPresent()) {
				ItemStack item = result.get();
				f.getInventory().setResult(new CustomItem(item, Math.min(item.getAmount() + amount, item.getMaxStackSize())));
			}
		}
	}

}
