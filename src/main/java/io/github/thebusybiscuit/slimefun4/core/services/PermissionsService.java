package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
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

    private final Map<String, String> permissions = new HashMap<>();
    private final Config config;

    public PermissionsService(@Nonnull SlimefunPlugin plugin) {
        config = new Config(plugin, "permissions.yml");
        config.getConfiguration().options().header("This file is used to assign permission nodes to items from Slimefun or any of its addons.\nTo assign an item a certain permission node you simply have to set the 'permission' attribute\nto your desired permission node. You can also customize the text that is displayed when a Player does not have that permission.");
        config.getConfiguration().options().copyHeader(true);
    }

    public void register(@Nonnull Iterable<SlimefunItem> items, boolean save) {
        for (SlimefunItem item : items) {
            if (item != null) {
                String path = item.getId() + ".permission";

                config.setDefaultValue(path, "none");
                config.setDefaultValue(item.getId() + ".lore", new String[] { "&rYou do not have the permission", "&rto access this item." });

                permissions.put(item.getId(), config.getString(path));
            }
        }

        if (save) {
            config.save();
        }
    }

    /**
     * This method checks whether the given {@link Permissible} has the {@link Permission}
     * to access the given {@link SlimefunItem}.
     * 
     * @param p
     *            The {@link Permissible} to check
     * @param item
     *            The {@link SlimefunItem} in question
     * 
     * @return Whether the {@link Permissible} has the required {@link Permission}
     */
    public boolean hasPermission(Permissible p, SlimefunItem item) {
        if (item == null) {
            // Failsafe
            return true;
        }

        String permission = permissions.get(item.getId());
        return permission == null || permission.equals("none") || p.hasPermission(permission);
    }

    /**
     * This returns the associated {@link Permission} with the given {@link SlimefunItem}.
     * It actually returns an {@link Optional}, {@link Optional#empty()} means that there was no
     * {@link Permission} set for the given {@link SlimefunItem}
     * 
     * @param item
     *            The {@link SlimefunItem} to retrieve the {@link Permission} for.
     * 
     * @return An {@link Optional} holding the {@link Permission} as a {@link String} or an empty {@link Optional}
     */
    @Nonnull
    public Optional<String> getPermission(@Nonnull SlimefunItem item) {
        Validate.notNull(item, "Cannot get permissions for null");
        String permission = permissions.get(item.getId());

        if (permission == null || permission.equals("none")) {
            return Optional.empty();
        } else {
            return Optional.of(permission);
        }
    }

    /**
     * This method sets the {@link Permission} for a given {@link SlimefunItem}.
     * 
     * @param item
     *            The {@link SlimefunItem} to modify
     * @param permission
     *            The {@link Permission} to set
     */
    public void setPermission(@Nonnull SlimefunItem item, @Nullable String permission) {
        Validate.notNull(item, "You cannot set the permission for null");
        permissions.put(item.getId(), permission != null ? permission : "none");
    }

    /**
     * This saves every configured {@link Permission} to the permissions {@link File}.
     */
    public void save() {
        for (Map.Entry<String, String> entry : permissions.entrySet()) {
            config.setValue(entry.getKey() + ".permission", entry.getValue());
        }

        config.save();
    }

    @Nonnull
    public List<String> getLore(@Nonnull SlimefunItem item) {
        List<String> lore = config.getStringList(item.getId() + ".lore");
        return lore == null ? Arrays.asList("LORE NOT FOUND") : lore;
    }

}
