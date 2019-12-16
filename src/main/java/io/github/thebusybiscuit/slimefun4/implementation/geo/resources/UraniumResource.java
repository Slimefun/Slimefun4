package io.github.thebusybiscuit.slimefun4.implementation.geo.resources;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;

public class UraniumResource implements OreGenResource {
	
	@Override
	public int getDefaultSupply(Biome biome) {
		Random random = ThreadLocalRandom.current();
		
		switch (biome) {
			case THE_END:
			case END_BARRENS:
			case END_MIDLANDS:
			case SMALL_END_ISLANDS:
			case NETHER:
				return 0;
			default:
				return random.nextInt(5) + 1;
		}
	}

	@Override
	public String getName() {
		return "Small Chunks of Uranium";
	}

	@Override
	public ItemStack getIcon() {
		return SlimefunItems.SMALL_URANIUM.clone();
	}

	@Override
	public String getMeasurementUnit() {
		return "Piece(s)";
	}

	@Override
	public boolean isLiquid() {
		return false;
	}

}
