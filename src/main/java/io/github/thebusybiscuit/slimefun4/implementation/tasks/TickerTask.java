package io.github.thebusybiscuit.slimefun4.implementation.tasks;

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
import org.bukkit.scheduler.BukkitScheduler;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.api.ErrorReport;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * The {@link TickerTask} is responsible for ticking every {@link BlockTicker}, synchronous
 * or not.
 * 
 * @author TheBusyBiscuit
 * 
 * @see BlockTicker
 *
 */
public class TickerTask implements Runnable {

    // These are "Queues" of blocks that need to be removed or moved
    private final Map<Location, Location> movingQueue = new ConcurrentHashMap<>();
    private final Map<Location, Boolean> deletionQueue = new ConcurrentHashMap<>();

    // This Map tracks how many bugs have occurred in a given Location
    // If too many bugs happen, we delete that Location
    private final Map<BlockPosition, Integer> bugs = new ConcurrentHashMap<>();

    private int tickRate;
    private boolean halted = false;
    private boolean running = false;

    /**
     * This method starts the {@link TickerTask} on an asynchronous schedule.
     * 
     * @param plugin
     *            The instance of our {@link SlimefunPlugin}
     */
    public void start(SlimefunPlugin plugin) {
        this.tickRate = SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay");

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskTimerAsynchronously(plugin, this, 100L, tickRate);
    }

    /**
     * This method resets this {@link TickerTask} to run again.
     */
    public void reset() {
        running = false;
    }

    @Override
    public void run() {
        try {
            // If this method is actually still running... DON'T
            if (running) {
                return;
            }

            running = true;
            SlimefunPlugin.getProfiler().start();
            Set<BlockTicker> tickers = new HashSet<>();

            Iterator<Map.Entry<Location, Boolean>> removals = deletionQueue.entrySet().iterator();
            while (removals.hasNext()) {
                Map.Entry<Location, Boolean> entry = removals.next();
                BlockStorage.deleteLocationInfoUnsafely(entry.getKey(), entry.getValue());
                removals.remove();
            }

            if (!halted) {
                for (String chunk : BlockStorage.getTickingChunks()) {
                    tickChunk(tickers, chunk);
                }
            }

            Iterator<Map.Entry<Location, Location>> moves = movingQueue.entrySet().iterator();
            while (moves.hasNext()) {
                Map.Entry<Location, Location> entry = moves.next();
                BlockStorage.moveLocationInfoUnsafely(entry.getKey(), entry.getValue());
                moves.remove();
            }

            // Start a new tick cycle for every BlockTicker
            for (BlockTicker ticker : tickers) {
                ticker.startNewTick();
            }

            reset();
            SlimefunPlugin.getProfiler().stop();
        }
        catch (Exception | LinkageError x) {
            Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + SlimefunPlugin.getVersion());
            reset();
        }
    }

    private void tickChunk(Set<BlockTicker> tickers, String chunk) {
        try {
            Set<Location> locations = BlockStorage.getTickingLocations(chunk);
            String[] components = PatternUtils.SEMICOLON.split(chunk);

            World world = Bukkit.getWorld(components[0]);
            int x = Integer.parseInt(components[components.length - 2]);
            int z = Integer.parseInt(components[components.length - 1]);

            if (world != null && world.isChunkLoaded(x, z)) {
                for (Location l : locations) {
                    tickLocation(tickers, l);
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException | NumberFormatException x) {
            Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Exception has occured while trying to parse Chunk: " + chunk);
        }
    }

    private void tickLocation(Set<BlockTicker> tickers, Location l) {
        Config data = BlockStorage.getLocationInfo(l);
        SlimefunItem item = SlimefunItem.getByID(data.getString("id"));

        if (item != null && item.getBlockTicker() != null) {
            try {
                if (item.getBlockTicker().isSynchronized()) {
                    SlimefunPlugin.getProfiler().scheduleEntries(1);
                    item.getBlockTicker().update();
                    // We are inserting a new timestamp because synchronized
                    // actions are always ran with a 50ms delay (1 game tick)
                    Slimefun.runSync(() -> {
                        Block b = l.getBlock();
                        tickBlock(l, b, item, data, System.nanoTime());
                    });
                }
                else {
                    long timestamp = SlimefunPlugin.getProfiler().newEntry();
                    item.getBlockTicker().update();
                    Block b = l.getBlock();
                    tickBlock(l, b, item, data, timestamp);
                }

                tickers.add(item.getBlockTicker());
            }
            catch (Exception x) {
                reportErrors(l, item, x);
            }
        }
    }

    private void tickBlock(Location l, Block b, SlimefunItem item, Config data, long timestamp) {
        try {
            item.getBlockTicker().tick(b, item, data);
        }
        catch (Exception | LinkageError x) {
            reportErrors(l, item, x);
        }
        finally {
            SlimefunPlugin.getProfiler().closeEntry(l, item, timestamp);
        }
    }

    private void reportErrors(Location l, SlimefunItem item, Throwable x) {
        BlockPosition position = new BlockPosition(l);
        int errors = bugs.getOrDefault(position, 0) + 1;

        if (errors == 1) {
            // Generate a new Error-Report
            new ErrorReport<>(x, l, item);
            bugs.put(position, errors);
        }
        else if (errors == 4) {
            Slimefun.getLogger().log(Level.SEVERE, "X: {0} Y: {1} Z: {2} ({3})", new Object[] { l.getBlockX(), l.getBlockY(), l.getBlockZ(), item.getID() });
            Slimefun.getLogger().log(Level.SEVERE, "has thrown 4 error messages in the last 4 Ticks, the Block has been terminated.");
            Slimefun.getLogger().log(Level.SEVERE, "Check your /plugins/Slimefun/error-reports/ folder for details.");
            Slimefun.getLogger().log(Level.SEVERE, " ");
            bugs.remove(position);

            BlockStorage.deleteLocationInfoUnsafely(l, true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance(), () -> l.getBlock().setType(Material.AIR));
        }
        else {
            bugs.put(position, errors);
        }
    }

    public boolean isHalted() {
        return halted;
    }

    public void halt() {
        halted = true;
    }

    public void queueMove(Location from, Location to) {
        movingQueue.put(from, to);
    }

    public void queueDelete(Location l, boolean destroy) {
        deletionQueue.put(l, destroy);
    }

    public int getTickRate() {
        return tickRate;
    }

    @Override
    public String toString() {
        return "TickerTask {\n" + "     HALTED = " + halted + "\n" + "     move = " + movingQueue + "\n" + "     delete = " + deletionQueue + "}";
    }

}
