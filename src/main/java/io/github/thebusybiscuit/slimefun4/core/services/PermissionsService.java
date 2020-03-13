package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Arrays;
import java.util.List;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This Service is responsible for handling the {@link Permission} of a
 * {@link SlimefunItem}.
 * 
 * You can set up these {@link Permission} nodes inside the {@code permissions.yml} file.
 * 
 * @author TheBusyBiscuit
 *
 */
public class PermissionsService {

    private final SlimefunPlugin plugin;
    private Config config;

    public PermissionsService(SlimefunPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        config = new Config(plugin, "permissions.yml");

        config.getConfiguration().options().header("This file is used to assign permission nodes to items from Slimefun or any of its addons.\n" + "To assign an item a certain permission node you simply have to set the 'permission' attribute\n" + "to your desired permission node. You can also customize the text that is displayed when a Player does not have that permission.");

        config.getConfiguration().options().copyHeader(true);
    }

    public void register(Iterable<SlimefunItem> items) {
        for (SlimefunItem item : items) {
            if (item != null && item.getID() != null && !migrate(item)) {
                config.setDefaultValue(item.getID() + ".permission", "none");
                config.setDefaultValue(item.getID() + ".lore", new String[] { "&rYou do not have the permission", "&rto access this item." });
            }
        }

        config.save();
    }

    // Temporary migration method for the old system
    private boolean migrate(SlimefunItem item) {
        String permission = SlimefunPlugin.getItemCfg().getString(item.getID() + ".required-permission");

        if (permission != null) {
            config.setDefaultValue(item.getID() + ".permission", permission.length() == 0 ? "none" : permission);
            config.setDefaultValue(item.getID() + ".lore", SlimefunPlugin.getItemCfg().getString(item.getID() + ".no-permission-tooltip"));

            SlimefunPlugin.getItemCfg().setValue(item.getID() + ".required-permission", null);
            SlimefunPlugin.getItemCfg().setValue(item.getID() + ".no-permission-tooltip", null);
            return true;
        }

        return false;
    }

    public boolean hasPermission(Permissible p, SlimefunItem item) {
        if (item == null) {
            // Failsafe
            return true;
        }

        String permission = config.getString(item.getID() + ".permission");

        return permission == null || permission.equals("none") || p.hasPermission(permission);
    }

    public List<String> getLore(SlimefunItem item) {
        List<String> lore = config.getStringList(item.getID() + ".lore");
        return lore == null ? Arrays.asList("LORE NOT FOUND") : lore;
    }

}
