package io.github.thebusybiscuit.slimefun4.implementation.tasks;

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

    // These are "Queues" of blocks that need to be removed or moved.
    private final Map<Location, Location> movingQueue = new ConcurrentHashMap<>();
    private final Map<Location, Boolean> deletionQueue = new ConcurrentHashMap<>();

    // Making collections concurrent doesn't make their iterators thread safe.
    private final ReadWriteLock queueLock = new ReentrantReadWriteLock();

    // This Map tracks how many bugs have occurred in a given Location.
    // If too many bugs happen, we delete that Location.
    private final Map<BlockPosition, AtomicInteger> bugs = new ConcurrentHashMap<>();

    // Don't overload the Bukkit scheduler with a thousand runnables. It doesn't like that. This way, we can spread the
    // time cost over several game ticks and adapt ticker rate to server performance.
    private final ArrayBlockingQueue<Runnable> syncTasks = new ArrayBlockingQueue<>(32);


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
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // We're not done with the synchronous tickers yet. Give the server a breather and let it progress one tick.
        Bukkit.getScheduler().runTaskLater(
                SlimefunPlugin.instance(),
                () -> processSyncTasks(),
                2L
        );
    }

    private void finish() {
        running = false;

        // If the plugin has been disabled, do not schedule the next execution.
        if (!SlimefunPlugin.instance().isEnabled()) {
            return;
        }

        // Schedule the ticker task to run again once the tick interval has elapsed. Use a synchronous task instead of
        // an asynchronous one to bind the interval to the server clock, not the wall clock.
        Bukkit.getScheduler().runTaskLater(
                SlimefunPlugin.instance(),
                this,
                (long) tickRate
        );
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
            }
            finally {
                queueLock.writeLock().unlock();
            }

            if (!halted) {
                for (String chunk : BlockStorage.getTickingChunks()) {
                    tickChunk(tickers, chunk);
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
            }
            finally {
                queueLock.writeLock().unlock();
            }

            // Start a new tick cycle for every BlockTicker
            for (BlockTicker ticker : tickers) {
                ticker.startNewTick();
            }
            
            SlimefunPlugin.getProfiler().stop();
        }
        catch (Exception | LinkageError x) {
            Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + SlimefunPlugin.getVersion());
        }
        finally {
            try {
                // Notify the sync task processor that the end of the ticker list has been reached.
                syncTasks.put(END_ELEMENT);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void tickChunk(@Nonnull List<BlockTicker> tickers, @Nonnull String chunk) {
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
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
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

    @ParametersAreNonnullByDefault
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

    @ParametersAreNonnullByDefault
    private void reportErrors(Location l, SlimefunItem item, Throwable x) {
        BlockPosition position = new BlockPosition(l);
        int errors = bugs.computeIfAbsent(position, (key) -> new AtomicInteger()).incrementAndGet();

        if (errors == 1) {
            // Generate a new Error-Report
            new ErrorReport<>(x, l, item);
        }
        else if (errors >= 4) {
            Slimefun.getLogger().log(Level.SEVERE, "X: {0} Y: {1} Z: {2} ({3})", new Object[] { l.getBlockX(), l.getBlockY(), l.getBlockZ(), item.getID() });
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
        // This collection is iterated over in a different thread. Need to lock it.
        queueLock.readLock().lock();
        try {
            movingQueue.put(from, to);
        }
        finally {
            queueLock.readLock().unlock();
        }
    }

    @ParametersAreNonnullByDefault
    public void queueDelete(Location l, boolean destroy) {
        // This collection is iterated over in a different thread. Need to lock it.
        queueLock.readLock().lock();
        try {
            deletionQueue.put(l, destroy);
        }
        finally {
            queueLock.readLock().unlock();
        }
    }

    public int getTickRate() {
        return tickRate;
    }

    @Override
    public String toString() {
        return "TickerTask {\n" + "     HALTED = " + halted + "\n" + "     move = " + movingQueue + "\n" + "     delete = " + deletionQueue + "}";
    }

}
