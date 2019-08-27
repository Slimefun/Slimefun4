package me.mrCookieSlime.Slimefun.api.energy;

import org.bukkit.Location;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

public abstract class EnergyTicker implements ItemHandler {
	
	public abstract double generateEnergy(Location l, SlimefunItem item, Config data);
	public abstract boolean explode(Location l);

	@Override
	public String toCodename() {
		return "EnergyTicker";
	}

}
