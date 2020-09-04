package me.mrCookieSlime.Slimefun.api.energy;

import org.bukkit.Location;
import org.bukkit.block.Block;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * 
 * @deprecated Use the methods provided by {@link EnergyNetComponent} instead.
 *
 */
@Deprecated
public final class ChargableBlock {

    private static final String KEY = "energy-charge";

    private ChargableBlock() {}

    public static boolean isChargable(Block b) {
        return isChargable(b.getLocation());
    }

    public static boolean isChargable(Location l) {
        SlimefunItem item = BlockStorage.check(l);
        return item instanceof EnergyNetComponent && ((EnergyNetComponent) item).isChargeable();
    }

    public static int getCharge(Block b) {
        return getCharge(b.getLocation());
    }

    public static int getCharge(Location l) {
        String charge = BlockStorage.getLocationInfo(l, KEY);

        if (charge != null) {
            return Integer.parseInt(charge);
        }
        else {
            BlockStorage.addBlockInfo(l, KEY, "0", false);
            return 0;
        }
    }

    public static void setCharge(Location l, int charge) {
        if (charge < 0) {
            charge = 0;
        }
        else {
            int capacity = getMaxCharge(l);

            if (charge > capacity) {
                charge = capacity;
            }
        }

        if (charge != getCharge(l)) {
            BlockStorage.addBlockInfo(l, KEY, String.valueOf(charge), false);
        }
    }

    public static void setUnsafeCharge(Location l, int charge, boolean updateTexture) {
        if (charge != getCharge(l)) {
            BlockStorage.addBlockInfo(l, KEY, String.valueOf(charge), false);

            if (updateTexture) {
                SlimefunUtils.updateCapacitorTexture(l, charge, getMaxCharge(l));
            }
        }
    }

    public static int addCharge(Block b, int charge) {
        return addCharge(b.getLocation(), charge);
    }

    public static int addCharge(Location l, int addedCharge) {
        SlimefunItem item = BlockStorage.check(l);

        if (item == null) {
            BlockStorage.clearBlockInfo(l);
            return 0;
        }

        int capacity = ((EnergyNetComponent) item).getCapacity();

        int charge = getCharge(l);
        int availableSpace = capacity - charge;
        int rest = addedCharge;

        if (availableSpace > 0 && addedCharge > 0) {
            if (availableSpace > addedCharge) {
                charge += addedCharge;
                rest = 0;
            }
            else {
                rest = addedCharge - availableSpace;
                charge = capacity;
            }

            setCharge(l, charge);

            if (item instanceof Capacitor) {
                SlimefunUtils.updateCapacitorTexture(l, charge, capacity);
            }
        }
        else if (addedCharge < 0 && charge >= -addedCharge) {
            charge += addedCharge;
            setCharge(l, charge);

            if (item instanceof Capacitor) {
                SlimefunUtils.updateCapacitorTexture(l, charge, capacity);
            }
        }

        return rest;
    }

    public static int getMaxCharge(Block b) {
        return getMaxCharge(b.getLocation());
    }

    public static int getMaxCharge(Location l) {
        SlimefunItem item = BlockStorage.check(l);

        if (item == null) {
            BlockStorage.clearBlockInfo(l);
            return 0;
        }

        return item instanceof EnergyNetComponent ? ((EnergyNetComponent) item).getCapacity() : 0;

    }

}
