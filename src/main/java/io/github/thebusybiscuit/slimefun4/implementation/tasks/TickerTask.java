package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.cscorelib2.blocks.ChunkPosition;
import io.github.thebusybiscuit.slimefun4.api.ErrorReport;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * The {@link TickerTask} is responsible for ticking every {@link BlockTicker},
 * synchronous or not.
 * 
 * @author TheBusyBiscuit
 * @author Qalle
 * @author Linox
 * 
 * @see BlockTicker
 *
 */
public class TickerTask implements Runnable {

    private static final Runnable END_ELEMENT = () -> {};
    private static final long MAX_POLL_NS = 250_000L;
    private static final long MAX_TICK_NS = 1_500_000L;

    /**
     * This Map holds all currently actively ticking locations.
     */
    private final Map<ChunkPosition, Set<Location>> tickingLocations = new ConcurrentHashMap<>();

    // These are "Queues" of blocks that need to be removed or moved
    private final Map<Location, Location> movingQueue = new ConcurrentHashMap<>();
    private final Map<Location, Boolean> deletionQueue = new ConcurrentHashMap<>();

    // Making collections concurrent doesn't make their iterators thread safe.
    private final ReadWriteLock queueLock = new ReentrantReadWriteLock();

    // Don't overload the Bukkit scheduler with a thousand runnables. It doesn't like that. This way, we can spread the
    // time cost over several game ticks and adapt ticker rate to server performance.
    private final ArrayBlockingQueue<Runnable> syncTasks = new ArrayBlockingQueue<>(32);

    /**
     * This Map tracks how many bugs have occurred in a given Location .
     * If too many bugs happen, we delete that Location.
     */
    private final Map<BlockPosition, Integer> bugs = new ConcurrentHashMap<>();

    private int tickRate;
    private boolean halted = false;
    
    // This needs to be volatile. Visibility is not guaranteed for primitives
    // across multiple threads, and since this is a Runnable, we cannot control which thread calls Runnable#run().
    private volatile boolean running = false; 

    /**
     * This method starts the {@link TickerTask} on an asynchronous schedule.
     * 
     * @param plugin
     *            The instance of our {@link SlimefunPlugin}
     */
    public void start(@Nonnull SlimefunPlugin plugin) {
        this.tickRate = SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay");

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskLater(plugin, this, 100L);
    }

    /**
     * This method resets this {@link TickerTask} to run again.
     */
    private void reset() {
        running = false;
    }

    private void processSyncTasks() {
        if (!Bukkit.isPrimaryThread()) {
            return;
        }

        // Let's not lag the main thread. Spikes suck. Steady time usage is much better.
        long endNs = System.nanoTime() + MAX_TICK_NS;

        try {
            while (endNs > System.nanoTime() || !SlimefunPlugin.instance().isEnabled()) {

                // Don't wait on an empty queue for too long.
                Runnable task = syncTasks.poll(MAX_POLL_NS, TimeUnit.NANOSECONDS);

                if (task == null) {
                    if (SlimefunPlugin.instance().isEnabled()) {
                        // If the queue is empty, let the server do its thing. We'll check back later.
                        break;
                    } else {
                        // The server expects the plugin to shut down right now. Continue blocking until we are done.
                        continue;
                    }
                }

                // The end element is a singleton runnable and represents the end of the ticker task list. Finish up.
                if (task == END_ELEMENT) {
                    finish();
                    return;
                }

                // Run the synchronous ticker task.
                task.run();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // We're not done with the synchronous tickers yet. Give the server a breather and let it progress one tick.
        Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance(), this::processSyncTasks, 2);
    }

    @Override 
    public final void run() {

        // This should only happen when the plugin gets disabled and the task is already running. Process all
        // sync tasks before returning, blocking the main thread until the task is complete.
        if (running) {
            processSyncTasks();
            return;
        }

        if (!Bukkit.isPrimaryThread()) {
            return;
        }

        // Iterate over the ticker tasks. Process the asynchronous ones on the new thread, and queue the sync
        // tasks to the queue.
        new Thread(() -> this.tick(), "Slimefun Async Ticker Thread").start();

        // Process the synchronous ticker tasks on the main thread.
        processSyncTasks();
  
        // If the plugin has been disabled, do not schedule the next execution.
        if (!SlimefunPlugin.instance().isEnabled()) {
            return;
        }

        // Schedule the ticker task to run again once the tick interval has elapsed. Use a synchronous task instead of
        // an asynchronous one to bind the interval to the server clock, not the wall clock.
        Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance(), this, tickRate);
    }

    private void tick() {
        try {
            // We don't care about the equality of elements here. Use a
            // list, it has lower add/remove/iterate time complexity and smaller memory footprint.
            SlimefunPlugin.getProfiler().start();
            List<BlockTicker> tickers = new LinkedList<>(); 
            
            // Iterators will throw CME's if something modifies the collection
            // during iteration.
            queueLock.writeLock().lock(); 
            
            try {
                Iterator<Map.Entry<Location, Boolean>> removals = deletionQueue.entrySet().iterator();
                
                while (removals.hasNext()) {
                    Map.Entry<Location, Boolean> entry = removals.next();
                    BlockStorage.deleteLocationInfoUnsafely(entry.getKey(), entry.getValue());
                    removals.remove();
                }
            } finally {
                queueLock.writeLock().unlock();
            }

            // Fixes #2576 - Remove any deleted instances of BlockStorage
            Iterator<BlockStorage> worlds = SlimefunPlugin.getRegistry().getWorlds().values().iterator();
            while (worlds.hasNext()) {
                BlockStorage storage = worlds.next();

                if (storage.isMarkedForRemoval()) {
                    worlds.remove();
                }
            }

            // Run our ticker code
            if (!halted) {
                for (Map.Entry<ChunkPosition, Set<Location>> entry : tickingLocations.entrySet()) {
                    tickChunk(entry.getKey(), tickers, entry.getValue());
                }
            }

            queueLock.writeLock().lock();
            try {
                Iterator<Map.Entry<Location, Location>> moves = movingQueue.entrySet().iterator();
                
                while (moves.hasNext()) {
                    Map.Entry<Location, Location> entry = moves.next();
                    BlockStorage.moveLocationInfoUnsafely(entry.getKey(), entry.getValue());
                    moves.remove();
                }
            } finally {
                queueLock.writeLock().unlock();
            }

            // Start a new tick cycle for every BlockTicker
            for (BlockTicker ticker : tickers) {
                ticker.startNewTick();
            }
            
            SlimefunPlugin.getProfiler().stop();
        } catch (Exception | LinkageError x) {
            Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + SlimefunPlugin.getVersion());
        } finally {
            try {
                // Notify the sync task processor that the end of the ticker list has been reached.
                syncTasks.put(END_ELEMENT);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void tickChunk(ChunkPosition chunk, Set<BlockTicker> tickers, Set<Location> locations) {
        try {
            // Only continue if the Chunk is actually loaded
            if (chunk.isLoaded()) {
                for (Location l : locations) {
                    tickLocation(tickers, l);
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException x) {
            Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Exception has occurred while trying to resolve Chunk: " + chunk);
        }
    }

    private void tickLocation(@Nonnull List<BlockTicker> tickers, @Nonnull Location l) {
        Config data = BlockStorage.getLocationInfo(l);
        SlimefunItem item = SlimefunItem.getByID(data.getString("id"));

        if (item != null && item.getBlockTicker() != null) {
            try {
                if (item.getBlockTicker().isSynchronized()) {
                    SlimefunPlugin.getProfiler().scheduleEntries(1);
                    item.getBlockTicker().update();

                    // We are inserting a new timestamp because synchronized
                    // actions are always ran with a 50ms delay (1 game tick).
                    try {
                        Runnable sync = () -> {
                            Block b = l.getBlock();
                            tickBlock(l, b, item, data, System.nanoTime());
                        };
                        syncTasks.put(sync);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                } else {
                    long timestamp = SlimefunPlugin.getProfiler().newEntry();
                    item.getBlockTicker().update();
                    Block b = l.getBlock();
                    tickBlock(l, b, item, data, timestamp);
                }

                tickers.add(item.getBlockTicker());
            } catch (Exception x) {
                reportErrors(l, item, x);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void tickBlock(Location l, Block b, SlimefunItem item, Config data, long timestamp) {
        try {
            item.getBlockTicker().tick(b, item, data);
        } catch (Exception | LinkageError x) {
            reportErrors(l, item, x);
        } finally {
            SlimefunPlugin.getProfiler().closeEntry(l, item, timestamp);
        }
    }

    @ParametersAreNonnullByDefault
    private void reportErrors(Location l, SlimefunItem item, Throwable x) {
        BlockPosition position = new BlockPosition(l);
        int errors = bugs.computeIfAbsent(position, (key) -> new AtomicInteger()).incrementAndGet();

        if (errors == 1) {
            // Generate a new Error-Report
            new ErrorReport<>(x, l, item);
            bugs.put(position, new AtomicInteger(1));
        } else if (errors >= 4) {
            Slimefun.getLogger().log(Level.SEVERE, "X: {0} Y: {1} Z: {2} ({3})", new Object[] { l.getBlockX(), l.getBlockY(), l.getBlockZ(), item.getId() });
            Slimefun.getLogger().log(Level.SEVERE, "has thrown 4 error messages in the last 4 Ticks, the Block has been terminated.");
            Slimefun.getLogger().log(Level.SEVERE, "Check your /plugins/Slimefun/error-reports/ folder for details.");
            Slimefun.getLogger().log(Level.SEVERE, " ");
            bugs.remove(position);

            BlockStorage.deleteLocationInfoUnsafely(l, true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance(), () -> l.getBlock().setType(Material.AIR));
        }
    }

    public boolean isHalted() {
        return halted;
    }

    public void halt() {
        halted = true;
    }

    @ParametersAreNonnullByDefault
    public void queueMove(Location from, Location to) {
        Validate.notNull(from, "Source Location cannot be null!");
        Validate.notNull(to, "Target Location cannot be null!");

        // This collection is iterated over in a different thread. Need to lock it.
        queueLock.readLock().lock();
        try {
            movingQueue.put(from, to);
        } finally {
            queueLock.readLock().unlock();
        }
    }

    @ParametersAreNonnullByDefault
    public void queueDelete(Location l, boolean destroy) {
        Validate.notNull(l, "Location must not be null!");

        // This collection is iterated over in a different thread. Need to lock it.
        queueLock.readLock().lock();
        try {
            deletionQueue.put(l, destroy);
        } finally {
            queueLock.readLock().unlock();
        }
    }

    /**
     * This method checks if the given {@link Location} has been reserved
     * by this {@link TickerTask}.
     * A reserved {@link Location} does not currently hold any data but will
     * be occupied upon the next tick.
     * Checking this ensures that our {@link Location} does not get treated like a normal
     * {@link Location} as it is theoretically "moving".
     * 
     * @param l
     *            The {@link Location} to check
     * 
     * @return Whether this {@link Location} has been reserved and will be filled upon the next tick
     */
    public boolean isReserved(@Nonnull Location l) {
        return movingQueue.containsValue(l);
    }

    /**
     * This returns the delay between ticks
     * 
     * @return The tick delay
     */
    public int getTickRate() {
        return tickRate;
    }

    /**
     * This method returns a <strong>read-only</strong> {@link Map}
     * representation of every {@link ChunkPosition} and its corresponding
     * {@link Set} of ticking {@link Location Locations}.
     * 
     * This does include any {@link Location} from an unloaded {@link Chunk} too!
     * 
     * @return A {@link Map} representation of all ticking {@link Location Locations}
     */
    @Nonnull
    public Map<ChunkPosition, Set<Location>> getLocations() {
        return Collections.unmodifiableMap(tickingLocations);
    }

    /**
     * This method returns a <strong>read-only</strong> {@link Set}
     * of all ticking {@link Location Locations} in a given {@link Chunk}.
     * The {@link Chunk} does not have to be loaded.
     * If no {@link Location} is present, the returned {@link Set} will be empty.
     * 
     * @param chunk
     *            The {@link Chunk}
     * 
     * @return A {@link Set} of all ticking {@link Location Locations}
     */
    @Nonnull
    public Set<Location> getLocations(@Nonnull Chunk chunk) {
        Validate.notNull(chunk, "The Chunk cannot be null!");

        Set<Location> locations = tickingLocations.getOrDefault(new ChunkPosition(chunk), new HashSet<>());
        return Collections.unmodifiableSet(locations);
    }

    /**
     * This enables the ticker at the given {@link Location} and adds it to our "queue".
     * 
     * @param l
     *            The {@link Location} to activate
     */
    public void enableTicker(@Nonnull Location l) {
        Validate.notNull(l, "Location cannot be null!");

        ChunkPosition chunk = new ChunkPosition(l.getWorld(), l.getBlockX() >> 4, l.getBlockZ() >> 4);
        Set<Location> newValue = new HashSet<>();
        Set<Location> oldValue = tickingLocations.putIfAbsent(chunk, newValue);

        /**
         * This is faster than doing computeIfAbsent(...)
         * on a ConcurrentHashMap because it won't block the Thread for too long
         */
        if (oldValue != null) {
            oldValue.add(l);
        } else {
            newValue.add(l);
        }
    }

    /**
     * This method disables the ticker at the given {@link Location} and removes it from our internal
     * "queue".
     * 
     * @param l
     *            The {@link Location} to remove
     */
    public void disableTicker(@Nonnull Location l) {
        Validate.notNull(l, "Location cannot be null!");

        ChunkPosition chunk = new ChunkPosition(l.getWorld(), l.getBlockX() >> 4, l.getBlockZ() >> 4);
        Set<Location> locations = tickingLocations.get(chunk);

        if (locations != null) {
            locations.remove(l);

            if (locations.isEmpty()) {
                tickingLocations.remove(chunk);
            }
        }
    }

}
