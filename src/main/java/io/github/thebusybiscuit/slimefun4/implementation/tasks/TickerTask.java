package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import io.github.thebusybiscuit.slimefun4.api.ErrorReport;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class TickerTask implements Runnable {

    private final Set<BlockTicker> tickers = new HashSet<>();

    // These are "Queues" of blocks that need to be removed or moved
    private final Map<Location, Location> movingQueue = new ConcurrentHashMap<>();
    private final Map<Location, Boolean> deletionQueue = new ConcurrentHashMap<>();
    private final Map<Location, Integer> buggedBlocks = new ConcurrentHashMap<>();

    private boolean halted = false;
    private boolean running = false;

    public void abortTick() {
        running = false;
    }

    @Override
    public void run() {
        if (running) {
            return;
        }

        running = true;
        SlimefunPlugin.getProfiler().start();

        Map<Location, Integer> bugs = new HashMap<>(buggedBlocks);
        buggedBlocks.clear();

        Map<Location, Boolean> removals = new HashMap<>(deletionQueue);

        for (Map.Entry<Location, Boolean> entry : removals.entrySet()) {
            BlockStorage._integrated_removeBlockInfo(entry.getKey(), entry.getValue());
            deletionQueue.remove(entry.getKey());
        }

        if (!halted) {
            for (String chunk : BlockStorage.getTickingChunks()) {
                try {
                    Set<Location> locations = BlockStorage.getTickingLocations(chunk);
                    String[] components = PatternUtils.SEMICOLON.split(chunk);

                    World world = Bukkit.getWorld(components[0]);
                    int x = Integer.parseInt(components[components.length - 2]);
                    int z = Integer.parseInt(components[components.length - 1]);

                    if (world != null && world.isChunkLoaded(x, z)) {
                        for (Location l : locations) {
                            tick(l, bugs);
                        }
                    }
                }
                catch (ArrayIndexOutOfBoundsException | NumberFormatException x) {
                    Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Exception has occured while trying to parse Chunk: " + chunk);
                }
            }
        }

        for (Map.Entry<Location, Location> entry : movingQueue.entrySet()) {
            BlockStorage._integrated_moveLocationInfo(entry.getKey(), entry.getValue());
        }

        movingQueue.clear();

        Iterator<BlockTicker> iterator = tickers.iterator();
        while (iterator.hasNext()) {
            iterator.next().startNewTick();
            iterator.remove();
        }

        running = false;
        SlimefunPlugin.getProfiler().stop();
    }

    private void tick(Location l, Map<Location, Integer> bugs) {
        Config data = BlockStorage.getLocationInfo(l);
        SlimefunItem item = SlimefunItem.getByID(data.getString("id"));

        if (item != null && item.getBlockTicker() != null) {
            try {
                long timestamp = SlimefunPlugin.getProfiler().newEntry();
                Block b = l.getBlock();
                item.getBlockTicker().update();

                if (item.getBlockTicker().isSynchronized()) {
                    // We are ignoring the timestamp from above because synchronized actions
                    // are always ran with a 50ms delay (1 game tick)
                    Slimefun.runSync(() -> tickBlock(bugs, l, b, item, data, System.nanoTime()));
                }
                else {
                    tickBlock(bugs, l, b, item, data, timestamp);
                }

                tickers.add(item.getBlockTicker());
            }
            catch (Exception x) {
                int errors = bugs.getOrDefault(l, 0);
                reportErrors(l, item, x, errors);
            }
        }
    }

    private void tickBlock(Map<Location, Integer> bugs, Location l, Block b, SlimefunItem item, Config data, long timestamp) {
        try {
            item.getBlockTicker().tick(b, item, data);
        }
        catch (Exception | LinkageError x) {
            int errors = bugs.getOrDefault(l, 0);
            reportErrors(l, item, x, errors);
        }
        finally {
            SlimefunPlugin.getProfiler().closeEntry(l, item, timestamp);
        }
    }

    private void reportErrors(Location l, SlimefunItem item, Throwable x, int errors) {
        errors++;

        if (errors == 1) {
            // Generate a new Error-Report
            new ErrorReport(x, l, item);
            buggedBlocks.put(l, errors);
        }
        else if (errors == 4) {
            Slimefun.getLogger().log(Level.SEVERE, "X: {0} Y: {1} Z: {2} ({3})", new Object[] { l.getBlockX(), l.getBlockY(), l.getBlockZ(), item.getID() });
            Slimefun.getLogger().log(Level.SEVERE, "has thrown 4 error messages in the last 4 Ticks, the Block has been terminated.");
            Slimefun.getLogger().log(Level.SEVERE, "Check your /plugins/Slimefun/error-reports/ folder for details.");
            Slimefun.getLogger().log(Level.SEVERE, " ");

            BlockStorage._integrated_removeBlockInfo(l, true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> l.getBlock().setType(Material.AIR));
        }
        else {
            buggedBlocks.put(l, errors);
        }
    }

    public boolean isHalted() {
        return halted;
    }

    public void halt() {
        halted = true;
    }

    @Override
    public String toString() {
        return "TickerTask {\n" + "     HALTED = " + halted + "\n" + "     tickers = " + tickers + "\n" + "     move = " + movingQueue + "\n" + "     delete = " + deletionQueue + "}";
    }

    public void queueMove(Location from, Location to) {
        movingQueue.put(from, to);
    }

    public void queueDelete(Location l, boolean destroy) {
        deletionQueue.put(l, destroy);
    }

    public void start(SlimefunPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                run();
            }
            catch (Exception | LinkageError x) {
                plugin.getLogger().log(Level.SEVERE, x, () -> "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + SlimefunPlugin.getVersion());
                abortTick();
            }
        }, 100L, SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay"));
    }

}
