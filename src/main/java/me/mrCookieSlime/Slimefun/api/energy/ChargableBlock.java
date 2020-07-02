package me.mrCookieSlime.Slimefun.api.energy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import io.github.thebusybiscuit.cscorelib2.skull.SkullBlock;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class ChargableBlock {

    private ChargableBlock() {}

    public static boolean isChargable(Block b) {
        return isChargable(b.getLocation());
    }

    public static boolean isChargable(Location l) {
        if (!BlockStorage.hasBlockInfo(l)) {
            return false;
        }

        String id = BlockStorage.checkID(l);
        return SlimefunPlugin.getRegistry().getEnergyCapacities().containsKey(id);
    }

    public static int getCharge(Block b) {
        return getCharge(b.getLocation());
    }

    public static int getCharge(Location l) {
        String charge = BlockStorage.getLocationInfo(l, "energy-charge");

        if (charge != null) {
            return Integer.parseInt(charge);
        }
        else {
            BlockStorage.addBlockInfo(l, "energy-charge", "0", false);
            return 0;
        }
    }

    public static void setCharge(Block b, int charge) {
        setCharge(b.getLocation(), charge);
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
            BlockStorage.addBlockInfo(l, "energy-charge", String.valueOf(charge), false);
        }
    }

    public static void setUnsafeCharge(Location l, int charge, boolean updateTexture) {
        if (charge != getCharge(l)) {
            BlockStorage.addBlockInfo(l, "energy-charge", String.valueOf(charge), false);

            if (updateTexture) {
                updateCapacitor(l);
            }
        }
    }

    public static int addCharge(Block b, int charge) {
        return addCharge(b.getLocation(), charge);
    }

    public static int addCharge(Location l, int charge) {
        int capacity = getMaxCharge(l);
        int energy = getCharge(l);
        int space = capacity - energy;
        int rest = charge;

        if (space > 0 && charge > 0) {
            if (space > charge) {
                setCharge(l, energy + charge);
                rest = 0;
            }
            else {
                rest = charge - space;
                setCharge(l, capacity);
            }

            if (SlimefunPlugin.getRegistry().getEnergyCapacitors().contains(BlockStorage.checkID(l))) {
                updateCapacitor(l);
            }
        }
        else if (charge < 0 && energy >= -charge) {
            setCharge(l, energy + charge);

            if (SlimefunPlugin.getRegistry().getEnergyCapacitors().contains(BlockStorage.checkID(l))) {
                updateCapacitor(l);
            }
        }

        return rest;
    }

    private static void updateCapacitor(Location l) {
        Slimefun.runSync(() -> {
            Block b = l.getBlock();
            int charge = getCharge(b);
            int capacity = getMaxCharge(b);

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
        Config cfg = BlockStorage.getLocationInfo(l);
        String id = cfg.getString("id");

        if (id == null) {
            BlockStorage.clearBlockInfo(l);
            return 0;
        }

        return SlimefunPlugin.getRegistry().getEnergyCapacities().getOrDefault(id, 0);
    }

}
