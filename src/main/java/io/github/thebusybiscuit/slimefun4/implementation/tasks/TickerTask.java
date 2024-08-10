package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.bakedlibs.dough.blocks.ChunkPosition;
import io.github.thebusybiscuit.slimefun4.api.ErrorReport;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * The {@link TickerTask} is responsible for ticking every {@link BlockTicker},
 * synchronous or not.
 * 
 * @author TheBusyBiscuit
 * 
 * @see BlockTicker
 *
 */
public class TickerTask implements Runnable {

    /**
     * This Map holds all currently actively ticking locations.
     */
    private final Map<ChunkPosition, Set<BlockPosition>> tickingPositions = new ConcurrentHashMap<>();

    // These are "Queues" of blocks that need to be removed or moved
    private final Map<BlockPosition, BlockPosition> movingQueue = new ConcurrentHashMap<>();
    private final Map<BlockPosition, Boolean> deletionQueue = new ConcurrentHashMap<>();

    /**
     * This Map tracks how many bugs have occurred in a given Location .
     * If too many bugs happen, we delete that Location.
     */
    private final Map<BlockPosition, Integer> bugs = new ConcurrentHashMap<>();

    private int tickRate;
    private boolean halted = false;
    private boolean running = false;

    /**
     * This method starts the {@link TickerTask} on an asynchronous schedule.
     * 
     * @param plugin
     *            The instance of our {@link Slimefun}
     */
    public void start(@Nonnull Slimefun plugin) {
        this.tickRate = Slimefun.getCfg().getInt("URID.custom-ticker-delay");

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskTimerAsynchronously(plugin, this, 100L, tickRate);
    }

    /**
     * This method resets this {@link TickerTask} to run again.
     */
    private void reset() {
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
            Slimefun.getProfiler().start();
            Set<BlockTicker> tickers = new HashSet<>();

            // Remove any deleted blocks
            Iterator<Map.Entry<BlockPosition, Boolean>> removals = deletionQueue.entrySet().iterator();
            while (removals.hasNext()) {
                Map.Entry<BlockPosition, Boolean> entry = removals.next();
                BlockStorage.deleteLocationInfoUnsafely(entry.getKey(), entry.getValue());
                removals.remove();
            }

            // Fixes #2576 - Remove any deleted instances of BlockStorage
            Slimefun.getRegistry().getWorlds().values().removeIf(BlockStorage::isMarkedForRemoval);

            // Run our ticker code
            if (!halted) {
                for (Map.Entry<ChunkPosition, Set<BlockPosition>> entry : tickingPositions.entrySet()) {
                    tickChunk(entry.getKey(), tickers, entry.getValue());
                }
            }

            // Move any moved block data
            Iterator<Map.Entry<BlockPosition, BlockPosition>> moves = movingQueue.entrySet().iterator();
            while (moves.hasNext()) {
                Map.Entry<BlockPosition, BlockPosition> entry = moves.next();
                BlockStorage.moveLocationInfoUnsafely(entry.getKey(), entry.getValue());
                moves.remove();
            }

            // Start a new tick cycle for every BlockTicker
            for (BlockTicker ticker : tickers) {
                ticker.startNewTick();
            }

            reset();
            Slimefun.getProfiler().stop();
        } catch (Exception | LinkageError x) {
            Slimefun.logger().log(Level.SEVERE, x, () -> "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + Slimefun.getVersion());
            reset();
        }
    }

    @ParametersAreNonnullByDefault
    private void tickChunk(ChunkPosition chunk, Set<BlockTicker> tickers, Set<BlockPosition> positions) {
        try {
            // Only continue if the Chunk is actually loaded
            if (chunk.isLoaded()) {
                for (BlockPosition position : positions) {
                    tickPosition(tickers, position);
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException x) {
            Slimefun.logger().log(Level.SEVERE, x, () -> "An Exception has occurred while trying to resolve Chunk: " + chunk);
        }
    }

    private void tickPosition(@Nonnull Set<BlockTicker> tickers, @Nonnull BlockPosition position) {
        Config data = BlockStorage.getBlockInfo(position);
        SlimefunItem item = SlimefunItem.getById(data.getString("id"));

        if (item != null && item.getBlockTicker() != null) {
            try {
                if (item.getBlockTicker().isSynchronized()) {
                    Slimefun.getProfiler().scheduleEntries(1);
                    item.getBlockTicker().update();

                    /**
                     * We are inserting a new timestamp because synchronized actions
                     * are always ran with a 50ms delay (1 game tick)
                     */
                    Slimefun.runSync(() -> {
                        Block b = position.getBlock();
                        tickBlock(position, b, item, data, System.nanoTime());
                    });
                } else {
                    long timestamp = Slimefun.getProfiler().newEntry();
                    item.getBlockTicker().update();
                    Block b = position.getBlock();
                    tickBlock(position, b, item, data, timestamp);
                }

                tickers.add(item.getBlockTicker());
            } catch (Exception x) {
                reportErrors(position, item, x);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void tickBlock(BlockPosition position, Block block, SlimefunItem item, Config data, long timestamp) {
        try {
            item.getBlockTicker().tick(block, item, data);
        } catch (Exception | LinkageError x) {
            reportErrors(position, item, x);
        } finally {
            Slimefun.getProfiler().closeEntry(position, item, timestamp);
        }
    }

    @ParametersAreNonnullByDefault
    private void reportErrors(BlockPosition position, SlimefunItem item, Throwable x) {
        int errors = bugs.getOrDefault(position, 0) + 1;

        if (errors == 1) {
            // Generate a new Error-Report
            new ErrorReport<>(x, position, item);
            bugs.put(position, errors);
        } else if (errors == 4) {
            Slimefun.logger().log(Level.SEVERE, "X: {0} Y: {1} Z: {2} ({3})", new Object[] { position.getX(), position.getY(), position.getZ(), item.getId() });
            Slimefun.logger().log(Level.SEVERE, "has thrown 4 error messages in the last 4 Ticks, the Block has been terminated.");
            Slimefun.logger().log(Level.SEVERE, "Check your /plugins/Slimefun/error-reports/ folder for details.");
            Slimefun.logger().log(Level.SEVERE, " ");
            bugs.remove(position);

            BlockStorage.deleteLocationInfoUnsafely(position, true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Slimefun.instance(), () -> position.getBlock().setType(Material.AIR));
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

    @ParametersAreNonnullByDefault
    public void queueMove(Location from, Location to) {
        Validate.notNull(from, "Source Location cannot be null!");
        Validate.notNull(to, "Target Location cannot be null!");

        movingQueue.put(new BlockPosition(from), new BlockPosition(to));
    }

    @ParametersAreNonnullByDefault
    public void queueMove(BlockPosition from, BlockPosition to) {
        Validate.notNull(from, "Source BlockPosition cannot be null!");
        Validate.notNull(to, "Target BlockPosition cannot be null!");

        movingQueue.put(from, to);
    }

    @ParametersAreNonnullByDefault
    public void queueDelete(Location l, boolean destroy) {
        Validate.notNull(l, "Location must not be null!");

        deletionQueue.put(new BlockPosition(l), destroy);
    }

    @ParametersAreNonnullByDefault
    public void queueDelete(BlockPosition position, boolean destroy) {
        Validate.notNull(position, "BlockPosition must not be null!");

        deletionQueue.put(position, destroy);
    }


    @ParametersAreNonnullByDefault
    public void queueDelete(Collection<Location> locations, boolean destroy) {
        Validate.notNull(locations, "Locations must not be null");
        queueDeletions(locations.stream().map(BlockPosition::new).collect(Collectors.toSet()), destroy);
    }

    @ParametersAreNonnullByDefault
    public void queueDeletions(Collection<BlockPosition> positions, boolean destroy) {
        Validate.notNull(positions, "Positions must not be null");

        Map<BlockPosition, Boolean> toDelete = new HashMap<>(positions.size(), 1.0F);
        for (BlockPosition position : positions) {
            Validate.notNull(position, "Positions must not contain null positions");
            toDelete.put(position, destroy);
        }
        deletionQueue.putAll(toDelete);
    }

    @ParametersAreNonnullByDefault
    public void queueDelete(Map<Location, Boolean> locations) {
        Validate.notNull(locations, "Locations must not be null");

    }

    @ParametersAreNonnullByDefault
    public void queueDeletions(Map<BlockPosition, Boolean> positions) {
        Validate.notNull(positions, "Positions must not be null");
        for (Map.Entry<BlockPosition, Boolean> entry : positions.entrySet()) {
            Validate.notNull(entry.getKey(), "BlockPosition in positions cannot be null");
            Validate.notNull(entry.getValue(), "Boolean toDestroy in positions cannot be null");
        }
        deletionQueue.putAll(positions);
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
    public boolean isOccupiedSoon(@Nonnull Location l) {
        Validate.notNull(l, "Null is not a valid Location!");
        return isOccupiedSoon(new BlockPosition(l));
    }

    public boolean isOccupiedSoon(@Nonnull BlockPosition position) {
        Validate.notNull(position, "Null is not a valid BlockPosition!");

        return movingQueue.containsValue(position);
    }

    /**
     * This method checks if a given {@link Location} will be deleted on the next tick.
     * 
     * @param l
     *            The {@link Location} to check
     * 
     * @return Whether this {@link Location} will be deleted on the next tick
     */
    public boolean isDeletedSoon(@Nonnull Location l) {
        Validate.notNull(l, "Null is not a valid Location!");
        return isDeletedSoon(new BlockPosition(l));
    }

    public boolean isDeletedSoon(@Nonnull BlockPosition position) {
        Validate.notNull(position, "Null is not a valid BlockPosition!");

        return deletionQueue.containsKey(position);
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
        Map<ChunkPosition, Set<Location>> locations = new HashMap<>();
        for (var entry : tickingPositions.entrySet()) {
            locations.put(entry.getKey(), entry.getValue().stream()
                    .map(BlockPosition::toLocation).collect(Collectors.toUnmodifiableSet()));
        }
        return Collections.unmodifiableMap(locations);
    }

    @Nonnull
    public Map<ChunkPosition, Set<BlockPosition>> getPositions() {
        return Collections.unmodifiableMap(tickingPositions);
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

        return tickingPositions.getOrDefault(new ChunkPosition(chunk), new HashSet<>())
                .stream().map(BlockPosition::toLocation).collect(Collectors.toUnmodifiableSet());
    }

    public Set<BlockPosition> getPositions(@Nonnull Chunk chunk) {
        Validate.notNull(chunk, "The Chunk cannot be null!");

        Set<BlockPosition> positions = tickingPositions.getOrDefault(new ChunkPosition(chunk), new HashSet<>());
        return Collections.unmodifiableSet(positions);
    }

    /**
     * This enables the ticker at the given {@link Location} and adds it to our "queue".
     * 
     * @param l
     *            The {@link Location} to activate
     */
    public void enableTicker(@Nonnull Location l) {
        Validate.notNull(l, "Location cannot be null!");
        enableTicker(new BlockPosition(l));
    }

    public void enableTicker(@Nonnull BlockPosition position) {
        Validate.notNull(position, "BlockPosition cannot be null!");

        ChunkPosition chunk = new ChunkPosition(position.getWorld(), position.getChunkX(), position.getChunkZ());
        Set<BlockPosition> newValue = new HashSet<>();
        Set<BlockPosition> oldValue = tickingPositions.putIfAbsent(chunk, newValue);

        /**
         * This is faster than doing computeIfAbsent(...)
         * on a ConcurrentHashMap because it won't block the Thread for too long
         */
        if (oldValue != null) {
            oldValue.add(position);
        } else {
            newValue.add(position);
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
        disableTicker(new BlockPosition(l));
    }

    public void disableTicker(@Nonnull BlockPosition position) {
        Validate.notNull(position, "BlockPosition cannot be null!");

        ChunkPosition chunk = new ChunkPosition(position.getWorld(), position.getChunkX(), position.getChunkZ());
        Set<BlockPosition> positions = tickingPositions.get(chunk);

        if (positions != null) {
            positions.remove(position);

            if (positions.isEmpty()) {
                tickingPositions.remove(chunk);
            }
        }
    }

}
