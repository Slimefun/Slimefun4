package io.github.thebusybiscuit.slimefun4.api.geo;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.events.GEOResourceGenerationEvent;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOMiner;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOScanner;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

    private final int[] backgroundSlots = {0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 48, 49, 50, 52, 53};
    private final ItemStack chunkTexture = SlimefunUtils.getCustomHead("8449b9318e33158e64a46ab0de121c3d40000e3332c1574932b3c849d8fa0dc2");
    private final Config config;

    public ResourceManager(SlimefunPlugin plugin) {
        config = new Config(plugin, "resources.yml");
    }

    /**
     * This method registers the given {@link GEOResource}.
     * It may never be called directly, use {@link GEOResource#register()} instead.
     *
     * @param resource The {@link GEOResource} to register
     */
    void register(GEOResource resource) {
        Validate.notNull(resource, "Cannot register null as a GEO-Resource");
        Validate.notNull(resource.getKey(), "GEO-Resources must have a NamespacedKey which is not null");

        // Resources may only be registered once
        if (SlimefunPlugin.getRegistry().getGEOResources().containsKey(resource.getKey())) {
            throw new IllegalArgumentException("GEO-Resource \"" + resource.getKey() + "\" has already been registered!");
        }

        String key = resource.getKey().getNamespace() + '.' + resource.getKey().getKey();
        boolean enabled = config.getOrSetDefault(key + ".enabled", true);

        if (enabled) {
            SlimefunPlugin.getRegistry().getGEOResources().add(resource);
        }

        if (SlimefunPlugin.getMinecraftVersion() != MinecraftVersion.UNIT_TEST) {
            config.save();
        }
    }

    public OptionalInt getSupplies(GEOResource resource, World world, int x, int z) {
        String key = resource.getKey().toString().replace(':', '-');

        String value = BlockStorage.getChunkInfo(world, x, z, key);

        if (value != null) {
            return OptionalInt.of(Integer.parseInt(value));
        } else {
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
            int max = resource.getMaxDeviation();

            if (max <= 0) {
                throw new IllegalStateException("GEO Resource \"" + resource.getKey() + "\" was misconfigured! getMaxDeviation() must return a value higher than zero!");
            }

            value += ThreadLocalRandom.current().nextInt(max);
        }

        GEOResourceGenerationEvent event = new GEOResourceGenerationEvent(world, block.getBiome(), x, z, resource, value);
        Bukkit.getPluginManager().callEvent(event);
        value = event.getValue();

        setSupplies(resource, world, x, z, value);
        return value;
    }

    /**
     * This method will start a geo-scan at the given {@link Block} and display the result
     * of that scan to the given {@link Player}.
     * <p>
     * Note that scans are always per {@link Chunk}, not per {@link Block}, the {@link Block}
     * parameter only determines the {@link Location} that was clicked but it will still scan
     * the entire {@link Chunk}.
     *
     * @param p     The {@link Player} who requested these results
     * @param block The {@link Block} which the scan starts at
     * @param page  The page to display
     */
    public void scan(Player p, Block block, int page) {
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
        List<GEOResource> resources = new ArrayList<>(SlimefunPlugin.getRegistry().getGEOResources().values());
        Collections.sort(resources, Comparator.comparing(a -> a.getName(p).toLowerCase(Locale.ROOT)));

        int index = 10;
        int pages = (resources.size() - 1) / 36 + 1;

        for (int i = page * 28; i < resources.size() && i < (page + 1) * 28; i++) {
            GEOResource resource = resources.get(i);
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

        menu.addItem(47, ChestMenuUtils.getPreviousButton(p, page + 1, pages));
        menu.addMenuClickHandler(47, (pl, slot, item, action) -> {
            if (page > 0) scan(pl, block, page - 1);
            return false;
        });

        menu.addItem(51, ChestMenuUtils.getNextButton(p, page + 1, pages));
        menu.addMenuClickHandler(51, (pl, slot, item, action) -> {
            if (page + 1 < pages) scan(pl, block, page + 1);
            return false;
        });

        menu.open(p);
    }

}