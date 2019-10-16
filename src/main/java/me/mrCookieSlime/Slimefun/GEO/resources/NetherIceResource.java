package me.mrCookieSlime.Slimefun.GEO.resources;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;

public class NetherIceResource implements OreGenResource {
	
	@Override
	public int getDefaultSupply(Biome biome) {
		return biome == Biome.NETHER ? 32: 0;
	}

	@Override
	public String getName() {
		return "Nether Ice";
	}

	@Override
	public ItemStack getIcon() {
		return SlimefunItems.NETHER_ICE.clone();
	}

	@Override
	public String getMeasurementUnit() {
		return "Blocks";
	}

	@Override
	public boolean isLiquid() {
		return false;
	}

}
