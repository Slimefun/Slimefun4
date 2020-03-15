package me.mrCookieSlime.Slimefun.api.energy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import io.github.thebusybiscuit.cscorelib2.skull.SkullBlock;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
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

        return SlimefunPlugin.getRegistry().getEnergyCapacities().containsKey(BlockStorage.checkID(l));
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
            if (charge > capacity) charge = capacity;
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
        int energy = getCharge(l);
        int space = getMaxCharge(l) - energy;
        int rest = charge;

        if (space > 0 && charge > 0) {
            if (space > charge) {
                setCharge(l, energy + charge);
                rest = 0;
            }
            else {
                rest = charge - space;
                setCharge(l, getMaxCharge(l));
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
                if (charge < (int) (capacity * 0.25D)) SkullBlock.setFromBase64(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ==");
                else if (charge < (int) (capacity * 0.5D)) SkullBlock.setFromBase64(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzA1MzIzMzk0YTdkOTFiZmIzM2RmMDZkOTJiNjNjYjQxNGVmODBmMDU0ZDA0NzM0ZWEwMTVhMjNjNTM5In19fQ==");
                else if (charge < (int) (capacity * 0.75D)) SkullBlock.setFromBase64(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU4NDQzMmFmNmYzODIxNjcxMjAyNThkMWVlZThjODdjNmU3NWQ5ZTQ3OWU3YjBkNGM3YjZhZDQ4Y2ZlZWYifX19");
                else SkullBlock.setFromBase64(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2EyNTY5NDE1YzE0ZTMxYzk4ZWM5OTNhMmY5OWU2ZDY0ODQ2ZGIzNjdhMTNiMTk5OTY1YWQ5OWM0MzhjODZjIn19fQ==");
            }
        });
    }

    public static int getMaxCharge(Block b) {
        return getMaxCharge(b.getLocation());
    }

    public static int getMaxCharge(Location l) {
        Config cfg = BlockStorage.getLocationInfo(l);

        if (!cfg.contains("id")) {
            BlockStorage.clearBlockInfo(l);
            return 0;
        }

        String str = cfg.getString("energy-capacity");

        if (str != null) {
            return Integer.parseInt(str);
        }
        else {
            int capacity = SlimefunPlugin.getRegistry().getEnergyCapacities().get(cfg.getString("id"));
            BlockStorage.addBlockInfo(l, "energy-capacity", String.valueOf(capacity), false);
            return capacity;
        }
    }

}
