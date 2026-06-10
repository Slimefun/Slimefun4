package io.github.thebusybiscuit.slimefun5.core;

import org.bukkit.Bukkit;
import io.github.thebusybiscuit.slimefun5.compat.SlimefunNMS;

public class VersionManager {

    private static SlimefunNMS nms;

    public static SlimefunNMS getNMS() {
        if (nms != null) {
            return nms;
        }

        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            nms = (SlimefunNMS) Class.forName("io.github.thebusybiscuit.slimefun5.nms." + version + ".SlimefunNMSImpl").newInstance();
            return nms;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load NMS implementation for version: " + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3], e);
        }
    }
}
