package me.mrCookieSlime.Slimefun.GEO.resources;

import java.util.Random;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;

public class UraniumResource implements OreGenResource {
	
	private final Random random = new Random();
	
	@Override
	public int getDefaultSupply(Biome biome) {
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
		return "一小块铀";
	}

	@Override
	public ItemStack getIcon() {
		return SlimefunItems.SMALL_URANIUM.clone();
	}

	@Override
	public String getMeasurementUnit() {
		return "块";
	}

	@Override
	public boolean isLiquid() {
		return false;
	}

}
