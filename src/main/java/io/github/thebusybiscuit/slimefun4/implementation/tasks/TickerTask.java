package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.api.ErrorReport;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class TickerTask implements Runnable {

    private final Set<BlockTicker> tickers = new HashSet<>();

    // These are "Queues" of blocks that need to be removed or moved
    private final Map<Location, Location> movingQueue = new ConcurrentHashMap<>();
    private final Map<Location, Boolean> deletionQueue = new ConcurrentHashMap<>();
    private final Map<BlockPosition, Integer> bugs = new ConcurrentHashMap<>();

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

        Iterator<Map.Entry<Location, Boolean>> removals = deletionQueue.entrySet().iterator();
        while (removals.hasNext()) {
            Map.Entry<Location, Boolean> entry = removals.next();
            BlockStorage._integrated_removeBlockInfo(entry.getKey(), entry.getValue());
            removals.remove();
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
                            tick(l);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException x) {
                    Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Exception has occured while trying to parse Chunk: " + chunk);
                }
            }
        }

        Iterator<Map.Entry<Location, Location>> moves = movingQueue.entrySet().iterator();
        while (moves.hasNext()) {
            Map.Entry<Location, Location> entry = moves.next();
            BlockStorage._integrated_moveLocationInfo(entry.getKey(), entry.getValue());
            moves.remove();
        }

        Iterator<BlockTicker> iterator = tickers.iterator();
        while (iterator.hasNext()) {
            iterator.next().startNewTick();
            iterator.remove();
        }

        running = false;
        SlimefunPlugin.getProfiler().stop();
    }

    private void tick(Location l) {
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
                    Slimefun.runSync(() -> tickBlock(l, b, item, data, System.nanoTime()));
                } else {
                    tickBlock(l, b, item, data, timestamp);
                }

                tickers.add(item.getBlockTicker());
            } catch (Exception x) {
                reportErrors(l, item, x);
            }
        }
    }

    private void tickBlock(Location l, Block b, SlimefunItem item, Config data, long timestamp) {
        try {
            item.getBlockTicker().tick(b, item, data);
        } catch (Exception | LinkageError x) {
            reportErrors(l, item, x);
        } finally {
            SlimefunPlugin.getProfiler().closeEntry(l, item, timestamp);
        }
    }

    private void reportErrors(Location l, SlimefunItem item, Throwable x) {
        BlockPosition position = new BlockPosition(l);
        int errors = bugs.getOrDefault(position, 0) + 1;

        if (errors == 1) {
            // Generate a new Error-Report
            new ErrorReport(x, l, item);
            bugs.put(position, errors);
        } else if (errors == 4) {
            Slimefun.getLogger().log(Level.SEVERE, "X: {0} Y: {1} Z: {2} ({3})", new Object[]{l.getBlockX(), l.getBlockY(), l.getBlockZ(), item.getID()});
            Slimefun.getLogger().log(Level.SEVERE, "has thrown 4 error messages in the last 4 Ticks, the Block has been terminated.");
            Slimefun.getLogger().log(Level.SEVERE, "Check your /plugins/Slimefun/error-reports/ folder for details.");
            Slimefun.getLogger().log(Level.SEVERE, " ");
            bugs.remove(position);

            BlockStorage._integrated_removeBlockInfo(l, true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> l.getBlock().setType(Material.AIR));
        } else {
            bugs.put(position, errors);
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
            } catch (Exception | LinkageError x) {
                plugin.getLogger().log(Level.SEVERE, x, () -> "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + SlimefunPlugin.getVersion());
                abortTick();
            }
        }, 100L, SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay"));
    }

}