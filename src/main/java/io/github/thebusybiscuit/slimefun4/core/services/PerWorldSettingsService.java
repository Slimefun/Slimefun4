package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Server;
import org.bukkit.World;

import io.github.thebusybiscuit.cscorelib2.collections.OptionalMap;
import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This Service is responsible for disabling a {@link SlimefunItem} in a certain {@link World}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class PerWorldSettingsService {

    private final SlimefunPlugin plugin;

    private final OptionalMap<UUID, Set<String>> disabledItems = new OptionalMap<>(HashMap::new);
    private final Map<SlimefunAddon, Set<String>> disabledAddons = new HashMap<>();
    private final Set<UUID> disabledWorlds = new HashSet<>();

    public PerWorldSettingsService(SlimefunPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This method will forcefully load all currently active Worlds to load up their settings.
     * 
     * @param worlds
     *            An {@link Iterable} of {@link World Worlds} to load
     */
    public void load(Iterable<World> worlds) {
        try {
            migrate();
        }
        catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "An error occurred while migrating old world settings", e);
        }

        for (World world : worlds) {
            load(world);
        }
    }

    /**
     * This method loads the given {@link World} if it was not loaded before.
     * 
     * @param world
     *            The {@link World} to load
     */
    public void load(World world) {
        disabledItems.putIfAbsent(world.getUID(), loadWorldFromConfig(world));
    }

    /**
     * Temporary migration method for the old system
     * 
     * @throws IOException
     *             This will be thrown if we failed to delete the old {@link File}
     */
    private void migrate() throws IOException {
        Config oldConfig = new Config(plugin, "whitelist.yml");

        if (oldConfig.getFile().exists()) {
            for (String world : oldConfig.getKeys()) {
                Config newConfig = new Config(plugin, "world-settings/" + world + ".yml");
                newConfig.setDefaultValue("enabled", oldConfig.getBoolean(world + ".enabled"));

                for (String id : oldConfig.getKeys(world + ".enabled-items")) {
                    SlimefunItem item = SlimefunItem.getByID(id);

                    if (item != null) {
                        String addon = item.getAddon().getName().toLowerCase(Locale.ROOT);
                        newConfig.setDefaultValue(addon + ".enabled", true);
                        newConfig.setDefaultValue(addon + '.' + id, oldConfig.getBoolean(world + ".enabled-items." + id));
                    }
                }

                newConfig.save();
            }

            Files.delete(oldConfig.getFile().toPath());
        }
    }

    /**
     * This method checks whether the given {@link SlimefunItem} is enabled in the given {@link World}.
     * 
     * @param world
     *            The {@link World} to check
     * @param item
     *            The {@link SlimefunItem} that should be checked
     * 
     * @return Whether the given {@link SlimefunItem} is enabled in that {@link World}
     */
    public boolean isEnabled(World world, SlimefunItem item) {
        Set<String> items = disabledItems.computeIfAbsent(world.getUID(), id -> loadWorldFromConfig(world));

        if (disabledWorlds.contains(world.getUID())) {
            return false;
        }

        return !items.contains(item.getID());
    }

    /**
     * This method enables or disables the given {@link SlimefunItem} in the specified {@link World}.
     * 
     * @param world
     *            The {@link World} in which to disable or enable the given {@link SlimefunItem}
     * @param item
     *            The {@link SlimefunItem} to enable or disable
     * @param enabled
     *            Whether the given {@link SlimefunItem} should be enabled in that world
     */
    public void setEnabled(World world, SlimefunItem item, boolean enabled) {
        Set<String> items = disabledItems.computeIfAbsent(world.getUID(), id -> loadWorldFromConfig(world));

        if (enabled) {
            items.remove(item.getID());
        }
        else {
            items.add(item.getID());
        }
    }

    /**
     * This method enables or disables the given {@link World}.
     * 
     * @param world
     *            The {@link World} to enable or disable
     * @param enabled
     *            Whether this {@link World} should be enabled or not
     */
    public void setEnabled(World world, boolean enabled) {
        load(world);

        if (enabled) {
            disabledWorlds.remove(world.getUID());
        }
        else {
            disabledWorlds.add(world.getUID());
        }
    }

    /**
     * This checks whether the given {@link World} is enabled or not.
     * 
     * @param world
     *            The {@link World} to check
     * 
     * @return Whether this {@link World} is enabled
     */
    public boolean isWorldEnabled(World world) {
        load(world);

        return !disabledWorlds.contains(world.getUID());
    }

    /**
     * This method checks whether the given {@link SlimefunAddon} is enabled in that {@link World}.
     * 
     * @param world
     *            The {@link World} to check
     * @param addon
     *            The {@link SlimefunAddon} to check
     * 
     * @return Whether this addon is enabled in that {@link World}
     */
    public boolean isAddonEnabled(World world, SlimefunAddon addon) {
        return isWorldEnabled(world) && disabledAddons.getOrDefault(addon, Collections.emptySet()).contains(world.getName());
    }

    /**
     * This will forcefully save the settings for that {@link World}.
     * This should only be called if you altered the settings while the {@link Server} was still running.
     * This writes to a {@link File} so it can be a heavy operation.
     * 
     * @param world
     *            The {@link World} to save
     */
    public void save(World world) {
        Set<String> items = disabledItems.computeIfAbsent(world.getUID(), id -> loadWorldFromConfig(world));

        Config config = getConfig(world);

        for (SlimefunItem item : SlimefunPlugin.getRegistry().getEnabledSlimefunItems()) {
            if (item != null && item.getID() != null) {
                String addon = item.getAddon().getName().toLowerCase(Locale.ROOT);
                config.setValue(addon + '.' + item.getID(), !items.contains(item.getID()));
            }
        }

        config.save();
    }

    private Set<String> loadWorldFromConfig(World world) {
        String name = world.getName();
        Optional<Set<String>> optional = disabledItems.get(world.getUID());

        if (optional.isPresent()) {
            return optional.get();
        }
        else {
            Set<String> items = new LinkedHashSet<>();
            Config config = getConfig(world);

            config.getConfiguration().options().header("This file is used to disable certain items in a particular world.\nYou can set any item to 'false' to disable it in the world '" + name + "'.\nYou can also disable an entire addon from Slimefun by setting the respective\nvalue of 'enabled' for that Addon.\n\nItems which are disabled in this world will not show up in the Slimefun Guide.\nYou won't be able to use these items either. Using them will result in a warning message.");
            config.getConfiguration().options().copyHeader(true);
            config.setDefaultValue("enabled", true);

            if (config.getBoolean("enabled")) {
                loadItemsFromWorldConfig(name, config, items);

                if (SlimefunPlugin.getMinecraftVersion() != MinecraftVersion.UNIT_TEST) {
                    config.save();
                }
            }
            else {
                disabledWorlds.add(world.getUID());
            }

            return items;
        }
    }

    private void loadItemsFromWorldConfig(String worldName, Config config, Set<String> items) {
        for (SlimefunItem item : SlimefunPlugin.getRegistry().getEnabledSlimefunItems()) {
            if (item != null && item.getID() != null) {
                String addon = item.getAddon().getName().toLowerCase(Locale.ROOT);
                config.setDefaultValue(addon + ".enabled", true);
                config.setDefaultValue(addon + '.' + item.getID(), true);

                boolean isAddonDisabled = config.getBoolean(addon + ".enabled");

                if (isAddonDisabled) {
                    Set<String> blacklist = disabledAddons.computeIfAbsent(plugin, key -> new HashSet<>());
                    blacklist.add(worldName);
                }

                if (!isAddonDisabled || !config.getBoolean(addon + '.' + item.getID())) {
                    items.add(item.getID());
                }
            }
        }
    }

    private Config getConfig(World world) {
        return new Config(plugin, "world-settings/" + world.getName() + ".yml");
    }

}
