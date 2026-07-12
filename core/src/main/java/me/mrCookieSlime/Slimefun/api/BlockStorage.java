package me.mrCookieSlime.Slimefun.api;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.storage.backend.BlockStorageBackend;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

// This class really needs a VERY big overhaul
public class BlockStorage {

    public static final String PATH_BLOCKS = "data-storage/Slimefun/stored-blocks/";
    public static final String PATH_CHUNKS = "data-storage/Slimefun/stored-chunks/";
    public static final String PATH_INVENTORIES = "data-storage/Slimefun/stored-inventories/";
    public static final String PATH_UNIVERSAL_INVENTORIES = "data-storage/Slimefun/universal-inventories/";

    private static final EmptyBlockData emptyBlockData = new EmptyBlockData();

    /**
     * Locations whose {@link BlockMenu} currently has at least one viewer. This is written only on the
     * main thread (menu open/close) and read by the async ticker so it can run a viewed machine's tick
     * on the main thread instead - serializing the machine's inventory writes with the player's clicks
     * to close the async-tick-vs-player duplication race. A stale-late removal is harmless (that machine
     * just ticks synchronously for one extra cycle); a missed add would not be, so adds are eager.
     */
    private static final java.util.Set<Location> viewedInventories = ConcurrentHashMap.newKeySet();

    private final World world;
    private final Map<Location, Config> storage = new ConcurrentHashMap<>();
    private final Map<Location, BlockMenu> inventories = new ConcurrentHashMap<>();
    private final Map<String, Config> blocksCache = new ConcurrentHashMap<>();
    /**
     * Locations removed since the last flush. The dirty {@link Config} in {@link #blocksCache}
     * only tracks locations that were written or explicitly deleted; deleting simply drops the
     * location's key from that delta {@link Config}, which is indistinguishable from "not
     * touched" for a backend that doesn't load full per-id state (e.g. {@code JdbcBackend}). This
     * set is the explicit signal passed to {@link BlockStorageBackend#deleteBlocks(World, java.util.Collection)}.
     */
    private final java.util.Set<Location> deletedBlocks = ConcurrentHashMap.newKeySet();

    private static int chunkChanges = 0;
    private static boolean universalInventoriesLoaded = false;

    private int changes = 0;
    private AtomicBoolean isMarkedForRemoval = new AtomicBoolean(false);

    @Nullable
    public static BlockStorage getStorage(@Nonnull World world) {
        return Slimefun.getRegistry().getWorlds().get(world.getName());
    }

    @Nonnull
    public static BlockStorage getOrCreate(@Nonnull World world) {
        BlockStorage storage = Slimefun.getRegistry().getWorlds().get(world.getName());

        if (storage == null) {
            return new BlockStorage(world);
        } else {
            return storage;
        }
    }

    public static String serializeLocation(Location l) {
        return l.getWorld().getName() + ';' + l.getBlockX() + ';' + l.getBlockY() + ';' + l.getBlockZ();
    }

    public static String serializeChunk(World world, int x, int z) {
        return world.getName() + ";Chunk;" + x + ';' + z;
    }

    public static Location deserializeLocation(String l) {
        try {
            String[] components = CommonPatterns.SEMICOLON.split(l);
            if (components.length != 4) {
                return null;
            }

            World w = Bukkit.getWorld(components[0]);

            if (w != null) {
                return new Location(w, Integer.parseInt(components[1]), Integer.parseInt(components[2]), Integer.parseInt(components[3]));
            }
        } catch (NumberFormatException x) {
            Slimefun.logger().log(Level.WARNING, "Could not parse Number", x);
        }
        return null;
    }

    public BlockStorage(World w) {
        this.world = w;

        if (world.getName().indexOf('.') != -1) {
            throw new IllegalArgumentException("Slimefun cannot deal with World names that contain a dot: " + w.getName());
        }

        if (Slimefun.getRegistry().getWorlds().containsKey(w.getName())) {
            // Cancel the loading process if the world was already loaded
            return;
        }

        Slimefun.logger().log(Level.INFO, "Loading Blocks for World \"{0}\"", w.getName());
        Slimefun.logger().log(Level.INFO, "This may take a long time...");

        BlockStorageBackend backend = Slimefun.getBlockStorageBackend();

        Map<String, Map<Location, Config>> loadedBlocks = backend.loadWorldBlocks(world);

        for (Map.Entry<String, Map<Location, Config>> fileEntry : loadedBlocks.entrySet()) {
            String fileName = fileEntry.getKey();
            boolean isTickerBlock = Slimefun.getRegistry().getTickerBlocks().contains(fileName);

            for (Map.Entry<Location, Config> blockEntry : fileEntry.getValue().entrySet()) {
                Location l = blockEntry.getKey();
                Config blockInfo = blockEntry.getValue();

                if (storage.putIfAbsent(l, blockInfo) != null) {
                    /*
                     * It should not be possible to have two blocks on the same location.
                     * Ignore the new entry if a block is already present and print an
                     * error to the console (if enabled).
                     */
                    if (Slimefun.getRegistry().logDuplicateBlockEntries()) {
                        Slimefun.logger().log(Level.INFO, String.format("Ignoring duplicate block @ %d, %d, %d (%s -> %s)", l.getBlockX(), l.getBlockY(), l.getBlockZ(), blockInfo.getString("id"), storage.get(l).getString("id")));
                    }

                    continue;
                }

                if (isTickerBlock) {
                    Slimefun.getTickerTask().enableTicker(l);
                }
            }
        }

        Slimefun.getRegistry().getChunks().putAll(backend.loadChunksForWorld(world));

        // TODO: properly support loading inventories within unit tests
        if (!Slimefun.instance().isUnitTest()) {
            this.inventories.putAll(backend.loadWorldInventories(world));

            if (!universalInventoriesLoaded) {
                universalInventoriesLoaded = true;
                Slimefun.getRegistry().getUniversalInventories().putAll(backend.loadUniversalInventories());
            }
        }

        Slimefun.getRegistry().getWorlds().put(world.getName(), this);
    }

    public void computeChanges() {
        changes = blocksCache.size();

        Map<Location, BlockMenu> inventories2 = new HashMap<>(inventories);
        for (Map.Entry<Location, BlockMenu> entry : inventories2.entrySet()) {
            changes += entry.getValue().getUnsavedChanges();
        }

        Map<String, UniversalBlockMenu> universalInventories2 = new HashMap<>(Slimefun.getRegistry().getUniversalInventories());
        for (Map.Entry<String, UniversalBlockMenu> entry : universalInventories2.entrySet()) {
            changes += entry.getValue().getUnsavedChanges();
        }
    }

    public int getChanges() {
        return changes;
    }

    public void save() {
        computeChanges();

        if (changes == 0) {
            return;
        }

        Slimefun.logger().log(Level.INFO, "Saving block data for world \"{0}\" ({1} change(s) queued)", new Object[] { world.getName(), changes });
        Map<String, Config> cache = new HashMap<>(blocksCache);

        for (String key : cache.keySet()) {
            blocksCache.remove(key);
        }

        BlockStorageBackend backend = Slimefun.getBlockStorageBackend();

        if (!deletedBlocks.isEmpty()) {
            // Delete BEFORE upserting: a break-then-replace of the same spot within one flush
            // window must keep the new block, so the delete must not run after its upsert.
            java.util.List<Location> deleted = new ArrayList<>(deletedBlocks);
            deletedBlocks.clear();
            backend.deleteBlocks(world, deleted);
        }

        backend.flushBlocks(world, cache);

        Map<Location, BlockMenu> unsavedInventories = new HashMap<>(inventories);
        backend.flushInventories(unsavedInventories);

        Map<String, UniversalBlockMenu> unsavedUniversalInventories = new HashMap<>(Slimefun.getRegistry().getUniversalInventories());
        backend.flushUniversalInventories(unsavedUniversalInventories);

        changes = 0;
    }

    public void saveAndRemove() {
        save();
        saveChunks();
        isMarkedForRemoval.set(true);
    }

    public boolean isMarkedForRemoval() {
        return isMarkedForRemoval.get();
    }

    public static void saveChunks() {
        if (chunkChanges > 0) {
            Map<String, BlockInfoConfig> unsavedChunks = new HashMap<>(Slimefun.getRegistry().getChunks());
            Slimefun.getBlockStorageBackend().flushChunks(unsavedChunks);

            chunkChanges = 0;
        }
    }

    /**
     * This will return an {@link ImmutableMap} of the underline {@code Map<String, Config>} of
     * this worlds {@link BlockStorage}.
     *
     * @return An {@link ImmutableMap} of the raw data.
     */
    @Nonnull
    public Map<Location, Config> getRawStorage() {
        return ImmutableMap.copyOf(this.storage);
    }

    /**
     * This will return an {@link ImmutableMap} of the underline {@code Map<String, Config>} of
     * this worlds {@link BlockStorage}. If there is no registered world then this will return null.
     *
     * @param world
     *            The world of which to fetch the data from.
     * @return An {@link ImmutableMap} of the raw data or null if the world isn't registered.
     */
    @Nullable
    public static Map<Location, Config> getRawStorage(@Nonnull World world) {
        Validate.notNull(world, "World cannot be null!");

        BlockStorage storage = getStorage(world);
        if (storage != null) {
            return storage.getRawStorage();
        } else {
            return null;
        }
    }

    public static void store(Block block, ItemStack item) {
        SlimefunItem sfitem = SlimefunItem.getByItem(item);

        if (sfitem != null) {
            addBlockInfo(block, "id", sfitem.getId(), true);
        }
    }

    public static void store(Block block, String item) {
        addBlockInfo(block, "id", item, true);
    }

    /**
     * Retrieves the SlimefunItem's ItemStack from the specified Block.
     * If the specified Block is registered in BlockStorage,
     * its data will be erased from it, regardless of the returned value.
     *
     * @param block
     *            the block to retrieve the ItemStack from
     * 
     * @return the SlimefunItem's ItemStack corresponding to the block if it has one, otherwise null
     */
    @Nullable
    public static ItemStack retrieve(@Nonnull Block block) {
        SlimefunItem item = check(block);

        if (item == null) {
            return null;
        } else {
            clearBlockInfo(block);
            return item.getItem();
        }
    }

    @Nonnull
    public static Config getLocationInfo(Location l) {
        BlockStorage storage = getStorage(l.getWorld());

        if (storage == null) {
            return emptyBlockData;
        }

        Config cfg = storage.storage.get(l);
        return cfg == null ? emptyBlockData : cfg;
    }

    @Nonnull
    public static Map<String, String> parseJSON(String json) {
        Map<String, String> map = new HashMap<>();

        if (json != null && json.length() > 2) {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(json).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                map.put(entry.getKey(), entry.getValue().getAsString());
            }
        }

        return map;
    }

    public static BlockInfoConfig parseBlockInfo(Location l, String json) {
        try {
            return new BlockInfoConfig(parseJSON(json));
        } catch (Exception x) {
            Logger logger = Slimefun.logger();
            logger.log(Level.WARNING, x.getClass().getName());
            logger.log(Level.WARNING, "Failed to parse BlockInfo for Block @ {0}, {1}, {2}", new Object[] { l.getBlockX(), l.getBlockY(), l.getBlockZ() });
            logger.log(Level.WARNING, json);
            logger.log(Level.WARNING, "");
            logger.log(Level.WARNING, "IGNORE THIS ERROR UNLESS IT IS SPAMMING");
            logger.log(Level.WARNING, "");
            logger.log(Level.SEVERE, x, () -> "An Error occurred while parsing Block Info for Slimefun " + Slimefun.getVersion());
            return null;
        }
    }

    public static String serializeBlockInfo(Config cfg) {
        StringWriter string = new StringWriter();

        try (JsonWriter writer = new JsonWriter(string)) {
            writer.setLenient(true);
            writer.beginObject();

            for (String key : cfg.getKeys()) {
                writer.name(key).value(cfg.getString(key));
            }

            writer.endObject();
            return string.toString();
        } catch (IOException x) {
            Slimefun.logger().log(Level.SEVERE, "An error occurred while serializing BlockInfo", x);
            return null;
        }
    }

    public static String getLocationInfo(Location l, String key) {
        return getLocationInfo(l).getString(key);
    }

    public static String getLocationInfo(BlockPosition l, String key) {
        return getLocationInfo(l.toLocation()).getString(key);
    }

    public static void addBlockInfo(Location l, String key, String value) {
        addBlockInfo(l, key, value, false);
    }

    public static void addBlockInfo(BlockPosition l, String key, String value) {
        addBlockInfo(l.toLocation(), key, value, false);
    }

    public static void addBlockInfo(Block block, String key, String value) {
        addBlockInfo(block.getLocation(), key, value);
    }

    public static void addBlockInfo(Block block, String key, String value, boolean updateTicker) {
        addBlockInfo(block.getLocation(), key, value, updateTicker);
    }

    public static void addBlockInfo(BlockPosition l, String key, String value, boolean updateTicker) {
        addBlockInfo(l.toLocation(), key, value, updateTicker);
    }

    public static void addBlockInfo(Location l, String key, String value, boolean updateTicker) {
        Config cfg = getLocationInfo(l);

        if (cfg == emptyBlockData) {
            cfg = new BlockInfoConfig();
        }

        cfg.setValue(key, value);
        setBlockInfo(l, cfg, updateTicker);
    }

    public static boolean hasBlockInfo(Block block) {
        return hasBlockInfo(block.getLocation());
    }

    public static boolean hasBlockInfo(Location l) {
        BlockStorage storage = getStorage(l.getWorld());

        if (storage != null) {
            Config cfg = storage.storage.get(l);
            return cfg != null && cfg.getString("id") != null;
        } else {
            return false;
        }
    }

    private static void setBlockInfo(Location l, Config cfg, boolean updateTicker) {
        BlockStorage storage = getStorage(l.getWorld());

        if (storage == null) {
            Slimefun.logger().warning("Could not set Block info for non-registered World '" + l.getWorld().getName() + "'. Is some plugin trying to store data in a fake world?");
            return;
        }

        storage.storage.put(l, cfg);
        String id = cfg.getString("id");
        BlockMenuPreset preset = BlockMenuPreset.getPreset(id);

        if (preset != null) {
            if (BlockMenuPreset.isUniversalInventory(id)) {
                Slimefun.getRegistry().getUniversalInventories().computeIfAbsent(id, key -> new UniversalBlockMenu(preset));
            } else if (!storage.hasInventory(l)) {
                BlockMenu inventory = Slimefun.getBlockStorageBackend().loadInventoryIfPresent(l, preset);

                if (inventory != null) {
                    storage.inventories.put(l, inventory);
                } else {
                    storage.loadInventory(l, preset);
                }
            }
        }

        refreshCache(storage, l, id, serializeBlockInfo(cfg), updateTicker);
    }

    public static void setBlockInfo(Block b, String json, boolean updateTicker) {
        setBlockInfo(b.getLocation(), json, updateTicker);
    }

    public static void setBlockInfo(Location l, String json, boolean updateTicker) {
        Config blockInfo = json == null ? new BlockInfoConfig() : parseBlockInfo(l, json);

        if (blockInfo == null) {
            return;
        }

        setBlockInfo(l, blockInfo, updateTicker);
    }

    public static void clearBlockInfo(Block block) {
        clearBlockInfo(block.getLocation());
    }

    public static void clearBlockInfo(Location l) {
        clearBlockInfo(l, true);
    }

    public static void clearBlockInfo(Block b, boolean destroy) {
        clearBlockInfo(b.getLocation(), destroy);
    }

    public static void clearBlockInfo(Location l, boolean destroy) {
        Slimefun.getTickerTask().queueDelete(l, destroy);
    }

    public static void clearAllBlockInfoAtChunk(Chunk chunk, boolean destroy) {
        clearAllBlockInfoAtChunk(chunk.getWorld(), chunk.getX(), chunk.getZ(), destroy);
    }

    public static void clearAllBlockInfoAtChunk(World world, int chunkX, int chunkZ, boolean destroy) {
        BlockStorage blockStorage = getStorage(world);
        if (blockStorage == null) {
            return;
        }
        Map<Location, Boolean> toClear = new HashMap<>();
        // Unsafe: get raw storage for this world
        for (Location location : blockStorage.storage.keySet()) {
            if (location.getBlockX() >> 4 == chunkX && location.getBlockZ() >> 4 == chunkZ) {
                toClear.put(location, destroy);
            }
        }
        Slimefun.getTickerTask().queueDelete(toClear);
    }

    /**
     * <strong>Do not call this method!</strong>.
     * This method is used for internal purposes only.
     * 
     * @param l
     *            The {@link Location}
     * @param destroy
     *            Whether to completely destroy the block data
     */
    public static void deleteLocationInfoUnsafely(Location l, boolean destroy) {
        BlockStorage storage = getStorage(l.getWorld());

        if (storage == null) {
            throw new IllegalStateException("World \"" + l.getWorld().getName() + "\" seems to have been deleted. Do not call unsafe methods directly!");
        }

        if (hasBlockInfo(l)) {
            refreshCache(storage, l, getLocationInfo(l).getString("id"), null, destroy);
            storage.storage.remove(l);
            storage.deletedBlocks.add(l);
        }

        if (destroy) {
            if (storage.hasInventory(l)) {
                storage.clearInventory(l);
            }

            UniversalBlockMenu universalInventory = getUniversalInventory(l);

            if (universalInventory != null) {
                universalInventory.close();
                universalInventory.save();
            }

            Slimefun.getTickerTask().disableTicker(l);
        }
    }

    @ParametersAreNonnullByDefault
    public static void moveBlockInfo(Location from, Location to) {
        Slimefun.getTickerTask().queueMove(from, to);
    }

    /**
     * <strong>Do not call this method!</strong>.
     * This method is used for internal purposes only.
     * 
     * @param from
     *            The origin {@link Location}
     * @param to
     *            The destination {@link Location}
     */
    @ParametersAreNonnullByDefault
    public static void moveLocationInfoUnsafely(Location from, Location to) {
        if (!hasBlockInfo(from)) {
            return;
        }

        BlockStorage storage = getStorage(from.getWorld());
        Config previousData = getLocationInfo(from);
        setBlockInfo(to, previousData, true);

        if (storage.inventories.containsKey(from)) {
            BlockMenu menu = storage.inventories.get(from);
            storage.inventories.put(to, menu);
            storage.clearInventory(from);
            menu.move(to);
        }

        refreshCache(storage, from, previousData.getString("id"), null, true);
        storage.storage.remove(from);
        storage.deletedBlocks.add(from);

        Slimefun.getTickerTask().disableTicker(from);
    }

    private static void refreshCache(BlockStorage storage, Location l, String key, String value, boolean updateTicker) {
        if (key == null) {
            /**
             * This Block is no longer valid...
             * Fixes #1577
             */
            return;
        }

        Config cfg = storage.blocksCache.computeIfAbsent(key, k -> new Config(PATH_BLOCKS + l.getWorld().getName() + '/' + key + ".sfb"));
        cfg.setValue(serializeLocation(l), value);

        if (updateTicker) {
            SlimefunItem item = SlimefunItem.getById(key);

            if (item != null
                && value != null
                && l.getWorld() != null
                && item.isTicking()
                && !item.isDisabledIn(l.getWorld())
            ) {
                Slimefun.getTickerTask().enableTicker(l);
            }
        }
    }

    @Nullable
    public static SlimefunItem check(@Nonnull Block b) {
        String id = checkID(b);
        return id == null ? null : SlimefunItem.getById(id);
    }

    @Nullable
    public static SlimefunItem check(@Nonnull Location l) {
        String id = checkID(l);
        return id == null ? null : SlimefunItem.getById(id);
    }

    public static boolean check(Block block, String slimefunItem) {
        String id = checkID(block);
        return id != null && id.equals(slimefunItem);
    }

    @Nullable
    public static String checkID(@Nonnull Block b) {
        // Only access the BlockState when on the main thread
        if (Bukkit.isPrimaryThread() && Slimefun.getBlockDataService().isTileEntity(b.getType())) {
            Optional<String> blockData = Slimefun.getBlockDataService().getBlockData(b);

            if (blockData.isPresent()) {
                return blockData.get();
            }
        }

        return checkID(b.getLocation());
    }

    @Nullable
    public static String checkID(@Nonnull Location l) {
        return getLocationInfo(l, "id");
    }

    public static boolean check(@Nonnull Location l, @Nullable String slimefunItem) {
        if (slimefunItem == null) {
            return false;
        }

        String id = checkID(l);
        return id != null && id.equals(slimefunItem);
    }

    public static boolean isWorldLoaded(@Nonnull World world) {
        return Slimefun.getRegistry().getWorlds().containsKey(world.getName());
    }

    public BlockMenu loadInventory(Location l, BlockMenuPreset preset) {
        if (preset == null) {
            return null;
        }

        BlockMenu menu = new BlockMenu(preset, l);
        inventories.put(l, menu);
        return menu;
    }

    /**
     * Reload a BlockMenu based on the preset. This method is solely for if you wish to reload
     * based on data from the preset.
     *
     * @param l
     *            The location of the Block.
     */
    public void reloadInventory(Location l) {
        BlockMenu menu = this.inventories.get(l);

        if (menu != null) {
            menu.reload();
        }
    }

    public void clearInventory(Location l) {
        BlockMenu menu = getInventory(l);

        if (menu != null) {
            for (HumanEntity human : new ArrayList<>(menu.toInventory().getViewers())) {
                // Prevents "java.lang.IllegalStateException: Asynchronous entity add!"
                // when closing the inventory while holding an item
                Slimefun.runSync(human::closeInventory);
            }

            inventories.get(l).delete(l);
            inventories.remove(l);
            viewedInventories.remove(l);
        }
    }

    public boolean hasInventory(Location l) {
        return inventories.containsKey(l);
    }

    public static boolean hasUniversalInventory(String id) {
        return Slimefun.getRegistry().getUniversalInventories().containsKey(id);
    }

    public static UniversalBlockMenu getUniversalInventory(Block block) {
        return getUniversalInventory(block.getLocation());
    }

    public static UniversalBlockMenu getUniversalInventory(Location l) {
        String id = checkID(l);
        return id == null ? null : getUniversalInventory(id);
    }

    public static UniversalBlockMenu getUniversalInventory(String id) {
        return Slimefun.getRegistry().getUniversalInventories().get(id);
    }

    public static BlockMenu getInventory(Block b) {
        return getInventory(b.getLocation());
    }

    public static boolean hasInventory(Block b) {
        BlockStorage storage = getStorage(b.getWorld());

        if (storage == null) {
            return false;
        } else {
            return storage.hasInventory(b.getLocation());
        }
    }

    /**
     * Marks (or unmarks) a {@link Location}'s inventory as currently being viewed by a player. Call only
     * from the main thread. See {@link #isInventoryViewed(Location)}.
     *
     * @param l
     *            The block {@link Location}
     * @param viewed
     *            Whether a player is now viewing this block's menu
     */
    public static void setInventoryViewed(@Nonnull Location l, boolean viewed) {
        if (viewed) {
            viewedInventories.add(l);
        } else {
            viewedInventories.remove(l);
        }
    }

    /**
     * Whether a player is currently viewing this block's {@link BlockMenu}. Cheap and side-effect free
     * (a plain set lookup - never loads an inventory), so the async ticker can consult it every tick.
     *
     * @param l
     *            The block {@link Location}
     *
     * @return Whether the block's menu currently has a viewer
     */
    public static boolean isInventoryViewed(@Nonnull Location l) {
        return !viewedInventories.isEmpty() && viewedInventories.contains(l);
    }

    /**
     * Whether any of the given block inventories is currently being viewed by a player.
     *
     * @param locations
     *            The block {@link Location Locations} to check (nulls are ignored)
     *
     * @return Whether at least one is being viewed
     */
    public static boolean isAnyInventoryViewed(@Nonnull Location... locations) {
        if (viewedInventories.isEmpty()) {
            return false;
        }

        for (Location l : locations) {
            if (l != null && viewedInventories.contains(l)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Runs an inventory-mutating action so it can never race a player. When the action runs off the main
     * thread and any of the involved block inventories is currently being viewed, it is dispatched to the
     * main thread (serializing it with the viewer's clicks); otherwise it runs inline - the async fast
     * path taken by the ~all machines nobody is watching. The action MUST perform its own reads inside, so
     * a deferred run sees the current inventory state rather than a stale async snapshot.
     *
     * @param action
     *            The inventory read-modify-write to run safely
     * @param involved
     *            Every block {@link Location} whose inventory the action reads or writes
     */
    public static void mutateInventorySafely(@Nonnull Runnable action, @Nonnull Location... involved) {
        if (!Bukkit.isPrimaryThread() && isAnyInventoryViewed(involved)) {
            Slimefun.runSync(action);
        } else {
            action.run();
        }
    }

    public static BlockMenu getInventory(Location l) {
        BlockStorage storage = getStorage(l.getWorld());

        if (storage == null) {
            return null;
        }

        BlockMenu menu = storage.inventories.get(l);

        if (menu != null) {
            return menu;
        } else {
            return storage.loadInventory(l, BlockMenuPreset.getPreset(checkID(l)));
        }
    }

    public static Config getChunkInfo(World world, int x, int z) {
        try {
            if (!isWorldLoaded(world)) {
                return emptyBlockData;
            }

            String key = serializeChunk(world, x, z);
            BlockInfoConfig cfg = Slimefun.getRegistry().getChunks().get(key);

            if (cfg == null) {
                cfg = new BlockInfoConfig();
                Slimefun.getRegistry().getChunks().put(key, cfg);
            }

            return cfg;
        } catch (Exception e) {
            Slimefun.logger().log(Level.SEVERE, e, () -> "Failed to parse ChunkInfo for Slimefun " + Slimefun.getVersion());
            return emptyBlockData;
        }
    }

    public static void setChunkInfo(World world, int x, int z, String key, String value) {
        String serializedChunk = serializeChunk(world, x, z);
        BlockInfoConfig cfg = Slimefun.getRegistry().getChunks().get(serializedChunk);

        if (cfg == null) {
            cfg = new BlockInfoConfig();
            Slimefun.getRegistry().getChunks().put(serializedChunk, cfg);
        }

        cfg.setValue(key, value);

        chunkChanges++;
    }

    public static boolean hasChunkInfo(World world, int x, int z) {
        String serializedChunk = serializeChunk(world, x, z);
        return Slimefun.getRegistry().getChunks().containsKey(serializedChunk);
    }

    public static String getChunkInfo(World world, int x, int z, String key) {
        return getChunkInfo(world, x, z).getString(key);
    }

    public static String getBlockInfoAsJson(Block block) {
        return getBlockInfoAsJson(block.getLocation());
    }

    public static String getBlockInfoAsJson(Location l) {
        return serializeBlockInfo(getLocationInfo(l));
    }

    public boolean hasUniversalInventory(Block block) {
        return hasUniversalInventory(block.getLocation());
    }

    public boolean hasUniversalInventory(Location l) {
        String id = checkID(l);
        return id != null && hasUniversalInventory(id);
    }
}

