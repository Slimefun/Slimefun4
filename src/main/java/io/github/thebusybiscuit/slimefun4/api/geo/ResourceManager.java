package io.github.thebusybiscuit.slimefun4.api.geo;

import java.util.OptionalInt;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOMiner;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOScanner;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * The {@link ResourceManager} is responsible for registering and managing a {@link GEOResource}.
 * You have to use the {@link ResourceManager} if you want to generate or consume a {@link GEOResource} too.
 * 
 * @author TheBusyBiscuit
 * 
 * @see GEOResource
 * @see GEOMiner
 * @see GEOScanner
 *
 */
public class ResourceManager {

    private final int[] backgroundSlots = { 0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
    private final ItemStack chunkTexture = SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ0OWI5MzE4ZTMzMTU4ZTY0YTQ2YWIwZGUxMjFjM2Q0MDAwMGUzMzMyYzE1NzQ5MzJiM2M4NDlkOGZhMGRjMiJ9fX0=");
    private final Config config;

    public ResourceManager(SlimefunPlugin plugin) {
        config = new Config(plugin, "resources.yml");
    }

    public void register(GEOResource resource) {
        boolean enabled = config.getOrSetDefault(resource.getKey().toString().replace(':', '.') + ".enabled", true);

        if (enabled) {
            SlimefunPlugin.getRegistry().getGEOResources().add(resource);
        }

        config.save();
    }

    public OptionalInt getSupplies(GEOResource resource, World world, int x, int z) {
        String key = resource.getKey().toString().replace(':', '-');

        String value = BlockStorage.getChunkInfo(world, x, z, key);

        if (value != null) {
            return OptionalInt.of(Integer.parseInt(value));
        }
        else {
            return OptionalInt.empty();
        }
    }

    public void setSupplies(GEOResource resource, World world, int x, int z, int value) {
        String key = resource.getKey().toString().replace(':', '-');
        BlockStorage.setChunkInfo(world, x, z, key, String.valueOf(value));
    }

    private int generate(GEOResource resource, World world, int x, int z) {
        Block block = world.getBlockAt(x << 4, 72, z << 4);
        int value = resource.getDefaultSupply(world.getEnvironment(), block.getBiome());

        if (value > 0) {
            value += ThreadLocalRandom.current().nextInt(resource.getMaxDeviation());
        }

        setSupplies(resource, world, x, z, value);
        return value;
    }

    public void scan(Player p, Block block) {
        if (SlimefunPlugin.getGPSNetwork().getNetworkComplexity(p.getUniqueId()) < 600) {
            SlimefunPlugin.getLocal().sendMessages(p, "gps.insufficient-complexity", true, msg -> msg.replace("%complexity%", "600"));
            return;
        }

        int x = block.getX() >> 4;
        int z = block.getZ() >> 4;

        ChestMenu menu = new ChestMenu("&4" + SlimefunPlugin.getLocal().getResourceString(p, "tooltips.results"));

        for (int slot : backgroundSlots) {
            menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        menu.addItem(4, new CustomItem(chunkTexture, "&e" + SlimefunPlugin.getLocal().getResourceString(p, "tooltips.chunk"), "", "&8\u21E8 &7" + SlimefunPlugin.getLocal().getResourceString(p, "tooltips.world") + ": " + block.getWorld().getName(), "&8\u21E8 &7X: " + x + " Z: " + z), ChestMenuUtils.getEmptyClickHandler());

        int index = 10;

        for (GEOResource resource : SlimefunPlugin.getRegistry().getGEOResources().values()) {
            OptionalInt optional = getSupplies(resource, block.getWorld(), x, z);
            int supplies = optional.isPresent() ? optional.getAsInt() : generate(resource, block.getWorld(), x, z);
            String suffix = SlimefunPlugin.getLocal().getResourceString(p, supplies == 1 ? "tooltips.unit" : "tooltips.units");

            ItemStack item = new CustomItem(resource.getItem(), "&r" + resource.getName(p), "&8\u21E8 &e" + supplies + ' ' + suffix);

            if (supplies > 1) {
                item.setAmount(supplies > item.getMaxStackSize() ? item.getMaxStackSize() : supplies);
            }

            menu.addItem(index, item, ChestMenuUtils.getEmptyClickHandler());
            index++;

            if (index % 9 == 8) {
                index += 2;
            }
        }

        menu.open(p);
    }

}
