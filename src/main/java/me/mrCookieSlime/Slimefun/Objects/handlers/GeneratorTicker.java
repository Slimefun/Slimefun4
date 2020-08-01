package me.mrCookieSlime.Slimefun.Objects.handlers;

import java.util.Optional;

import org.bukkit.Location;

import io.github.thebusybiscuit.slimefun4.api.exceptions.IncompatibleItemHandlerException;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * @deprecated Please implement the {@link EnergyNetProvider} interface instead.
 *
 */
@Deprecated
public abstract class GeneratorTicker implements ItemHandler {

    public abstract double generateEnergy(Location l, SlimefunItem item, Config data);

    public abstract boolean explode(Location l);

    @Override
    public Optional<IncompatibleItemHandlerException> validate(SlimefunItem item) {
        if (item instanceof NotPlaceable || !item.getItem().getType().isBlock()) {
            return Optional.of(new IncompatibleItemHandlerException("Only blocks that are not marked as 'NotPlaceable' can have a BlockTicker.", item, this));
        }

        if (item instanceof EnergyNetComponent && ((EnergyNetComponent) item).getEnergyComponentType() == EnergyNetComponentType.GENERATOR) {
            return Optional.empty();
        }

        return Optional.of(new IncompatibleItemHandlerException("Only items that implement 'EnergyNetComponent' with the type 'GENERATOR' can have a GeneratorTicker.", item, this));
    }

    @Override
    public Class<? extends ItemHandler> getIdentifier() {
        return GeneratorTicker.class;
    }

}
