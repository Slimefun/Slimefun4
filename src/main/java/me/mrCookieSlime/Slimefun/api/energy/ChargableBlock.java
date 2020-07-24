package me.mrCookieSlime.Slimefun.api.energy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import io.github.thebusybiscuit.cscorelib2.skull.SkullBlock;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class ChargableBlock {

    private static final String KEY = "energy-charge";

    private ChargableBlock() {}

    public static boolean isChargable(Block b) {
        return isChargable(b.getLocation());
    }

    public static boolean isChargable(Location l) {
        String id = BlockStorage.checkID(l);

        if (id == null) {
            return false;
        }

        return SlimefunPlugin.getRegistry().getEnergyCapacities().containsKey(id);
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
                updateCapacitor(l, charge, getMaxCharge(l));
            }
        }
    }

    public static int addCharge(Block b, int charge) {
        return addCharge(b.getLocation(), charge);
    }

    public static int addCharge(Location l, int addedCharge) {
        String id = BlockStorage.checkID(l);

        if (id == null) {
            BlockStorage.clearBlockInfo(l);
            return 0;
        }

        int capacity = SlimefunPlugin.getRegistry().getEnergyCapacities().getOrDefault(id, 0);

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

            if (SlimefunItem.getByID(id) instanceof Capacitor) {
                updateCapacitor(l, charge, capacity);
            }
        }
        else if (addedCharge < 0 && charge >= -addedCharge) {
            charge += addedCharge;
            setCharge(l, charge);

            if (SlimefunItem.getByID(id) instanceof Capacitor) {
                updateCapacitor(l, charge, capacity);
            }
        }

        return rest;
    }

    private static void updateCapacitor(Location l, int charge, int capacity) {
        Slimefun.runSync(() -> {
            Block b = l.getBlock();

            if (b.getType() == Material.PLAYER_HEAD || b.getType() == Material.PLAYER_WALL_HEAD) {
                if (charge < (int) (capacity * 0.25)) {
                    SkullBlock.setFromHash(b, HeadTexture.CAPACITOR_25.getTexture());
                }
                else if (charge < (int) (capacity * 0.5)) {
                    SkullBlock.setFromHash(b, HeadTexture.CAPACITOR_50.getTexture());
                }
                else if (charge < (int) (capacity * 0.75)) {
                    SkullBlock.setFromHash(b, HeadTexture.CAPACITOR_75.getTexture());
                }
                else {
                    SkullBlock.setFromHash(b, HeadTexture.CAPACITOR_100.getTexture());
                }
            }
        });
    }

    public static int getMaxCharge(Block b) {
        return getMaxCharge(b.getLocation());
    }

    public static int getMaxCharge(Location l) {
        String id = BlockStorage.checkID(l);

        if (id == null) {
            BlockStorage.clearBlockInfo(l);
            return 0;
        }

        return SlimefunPlugin.getRegistry().getEnergyCapacities().getOrDefault(id, 0);
    }

}
