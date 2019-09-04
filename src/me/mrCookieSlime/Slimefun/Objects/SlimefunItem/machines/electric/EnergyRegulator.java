package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet;
import me.mrCookieSlime.Slimefun.holograms.EnergyHologram;

public class EnergyRegulator extends SlimefunItem {

	public EnergyRegulator(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
		
		SlimefunItem.registerBlockHandler("ENERGY_REGULATOR", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				// Spawn the hologram
				EnergyHologram.update(b, "&7Connecting...");
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				EnergyHologram.remove(b);
				return true;
			}
		});
	}
	
	@Override
	public void preRegister() {
		addItemHandler(new BlockTicker() {

			@Override
			public boolean isSynchronized() {
				return false;
			}

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				EnergyNet.getNetworkFromLocationOrCreate(b.getLocation()).tick(b);
			}
		});
	}

}
