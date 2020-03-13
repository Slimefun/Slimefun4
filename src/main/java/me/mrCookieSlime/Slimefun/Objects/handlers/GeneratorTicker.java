package me.mrCookieSlime.Slimefun.Objects.handlers;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.Location;

public abstract class GeneratorTicker implements ItemHandler {

    public abstract double generateEnergy(Location l, SlimefunItem item, Config data);

    public abstract boolean explode(Location l);

    @Override
    public Class<? extends ItemHandler> getIdentifier() {
        return GeneratorTicker.class;
    }

}
