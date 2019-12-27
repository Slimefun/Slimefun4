package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.block.Block;

import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;

public final class EnergyHologram {
	
	private EnergyHologram() {}
	
	public static void update(Block b, double supply, double demand) {
		SimpleHologram.update(b, demand > supply ? ("&4&l- &c" + DoubleHandler.getFancyDouble(Math.abs(supply - demand)) + " &7J &e\u26A1"): ("&2&l+ &a" + DoubleHandler.getFancyDouble(supply - demand) + " &7J &e\u26A1"));
	}

}
