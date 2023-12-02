package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.jeff_media.customblockdata.CustomBlockData;
import com.sk89q.jnbt.StringTag;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.services.BlockDataService;
import io.github.thebusybiscuit.slimefun4.core.services.ChunkDataService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

public class BlockStorage {
    private static final String PATH_CHUNKS = "data-storage/Slimefun/stored-chunks/";
    
    private final World world;
    private final Map<Location, BlockMenu> inventories = new ConcurrentHashMap<>(); // Inventory saving writes to the BlockDataService for that BlockMenu location
    
    private AtomicBoolean isMarkedForRemoval = new AtomicBoolean(false);
    private boolean universalInventoriesLoaded = false;

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
        // TODO | We should load inventories directly from block data. If there are LegacyBlockStorage files, we should convert them to BlockStorage,
        // TODO | and then move the LegacyBlockStorage files to a backup 
        // TODO | folder incase something goes wrong. This way the user can at least restore them and downgrade until the bug is fixed
        // TODO | We might even need to load all of the chunks in the world to be able to get all the blocks with Custom Data. Ideally this would be done ignoring the tickers if the chunk wasn't already loaded - though this should only be triggered on WorldLoad (ideally no chunks would be loaded). I imagine this is going to be horrible for performance...
        loadBlocks();
        // TODO these should be loaded from the same function that loads blocks. Chunks have a "CustomBlockData.getAllBlocksInChunk" function to load blocks from directly. Though it likely requires the chunk to be loaded. hmm.
        loadChunks();

        loadUniversalInventories();

        Slimefun.getRegistry().getWorlds().put(world.getName(), this);
    }

    private void loadBlocks() {
        long start = System.currentTimeMillis();
        long done = 0;
        long timestamp = System.currentTimeMillis();
        long totalBlocks = 0;
        int delay = Slimefun.getCfg().getInt("URID.info-delay");

        Plugin plugin = Slimefun.getPlugin(Slimefun.class);
        
        try {
            // For each chunk in the world, get all blocks with custom data?
            long total = 1; // TODO get actual total blocks
            // TODO | this should get all chunks, not only loaded, and should also load any chunk data if needed
            // TODO | Chunk data is stored in persistent chunk storage and as such is already performant enough without in-memory caching
            for (Chunk chunk : world.getLoadedChunks()) {
                Set<Block> blocks = CustomBlockData.getBlocksWithCustomData(plugin, chunk);
                for (Block b : blocks) {
                    loadBlock(b);
                    totalBlocks++;
                }
            }

            int progress = Math.round((((done * 100.0F) / total) * 100.0F) / 100.0F);
            if (timestamp + delay < System.currentTimeMillis()) {
                Slimefun.logger().log(Level.INFO, "Loading Blocks... {0}% done (\"{1}\")", new Object[] { progress, world.getName() });  
            }
        } finally {
            long time = (System.currentTimeMillis() - start);
            Slimefun.logger().log(Level.INFO, "Loading Blocks... 100% (FINISHED - {0}ms)", time);
            Slimefun.logger().log(Level.INFO, "Loaded a total of {0} Blocks for World \"{1}\"", new Object[] { totalBlocks, world.getName() });

            if (totalBlocks > 0) {
                Slimefun.logger().log(Level.INFO, "Avg: {0}ms/Block", NumberUtils.roundDecimalNumber((double) time / (double) totalBlocks));
            }
        }
    }

    private void loadBlock(Block b) {
        Location l = b.getLocation();

        if (l == null) {
            // That location was malformed, we will skip this one
            return;
        }

        BlockMenuPreset preset = BlockMenuPreset.getPreset(getLocationInfo(l, "preset"));
        if (preset == null) {
            preset = BlockMenuPreset.getPreset(checkID(l));
        }
        if (preset != null) {
            inventories.put(l, new BlockMenu(preset, l));
        }

        if (Slimefun.getRegistry().getTickerBlocks().contains(serializeLocation(l))) {
            Slimefun.getTickerTask().enableTicker(l);
        }
    }

    private void loadChunks() {
        // TODO this should be done as part of loadBlocks (renamed func) since it needs the chunks to fetch the blocks with custom data in the chunk
        File chunks = new File(PATH_CHUNKS + "chunks.sfc");

        if (chunks.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(chunks);

            for (String key : cfg.getKeys(false)) {
                try {
                    if (world.getName().equals(CommonPatterns.SEMICOLON.split(key)[0])) {
                        BlockInfoConfig data = new BlockInfoConfig(parseJSON(cfg.getString(key)));
                        Slimefun.getRegistry().getChunks().put(key, data);
                    }
                } catch (Exception x) {
                    Slimefun.logger().log(Level.WARNING, x, () -> "Failed to load " + chunks.getName() + " in World " + world.getName() + '(' + key + ") for Slimefun " + Slimefun.getVersion());
                }
            }
        }
    }

    private void loadUniversalInventories() {
        // TODO could probably do this via CustomBlockData somehow? What do this store and in which block? What is "universal"?

        if (universalInventoriesLoaded) {
            return;
        }

        universalInventoriesLoaded = true;

        for (File file : new File("data-storage/Slimefun/universal-inventories").listFiles()) {
            if (file.getName().endsWith(".sfi")) {
                try {
                    io.github.bakedlibs.dough.config.Config cfg = new io.github.bakedlibs.dough.config.Config(file);
                    BlockMenuPreset preset = BlockMenuPreset.getPreset(cfg.getString("preset"));

                    if (preset != null) {
                        Slimefun.getRegistry().getUniversalInventories().put(preset.getID(), new UniversalBlockMenu(preset, cfg));
                    }
                } catch (Exception x) {
                    Slimefun.logger().log(Level.SEVERE, x, () -> "An Error occurred while loading this universal Inventory: " + file.getName());
                }
            }
        }
    }

    public static Boolean hasBlockInfo(@Nonnull Block b) {
        Validate.notNull(b, "The block cannot be null!");

        BlockDataService blockDataService = Slimefun.getBlockDataService();
        Optional<String> blockData = blockDataService.getBlockData(b, "id");
        return blockData.isPresent();
    }

    public static Boolean hasBlockInfo(@Nonnull Location l) {
        Validate.notNull(l, "The location cannot be null!");

        Block b = l.getBlock();
        if (b == null) {
            return false;
        }
        return hasBlockInfo(b);
    }

    public static void addBlockInfo(@Nonnull Block b, @Nonnull String key, String value, boolean updateTicker) {
        Validate.notNull(b, "The block cannot be null!");
        Validate.notNull(key, "The key cannot be null!");

        addBlockInfo(b.getLocation(), key, value, updateTicker);
    }

    public static void addBlockInfo(@Nonnull Block b, @Nonnull String key, String value) {
        Validate.notNull(b, "The block cannot be null!");
        Validate.notNull(key, "The key cannot be null!");

        addBlockInfo(b.getLocation(), key, value, false);
    }

    public static void addBlockInfo(Location l, @Nonnull String key, String value) {
        addBlockInfo(l, key, value, false);
    }

    public static void addBlockInfo(Location l, @Nonnull String key, String value, boolean updateTicker) {
        Block b = l.getBlock();
        World w = l.getWorld();

        if (w == null || b == null) {
            // unloaded world
            return;
        }

        BlockStorage storage = getStorage(w);
        BlockDataService blockData = Slimefun.getBlockDataService(); 

        blockData.setBlockData(b, key, value);
        Optional<String> id = blockData.getBlockData(b, "id");
        if (id.isPresent()) {
            BlockMenuPreset preset = BlockMenuPreset.getPreset(id.get());
            if (preset != null) {
                if (BlockMenuPreset.isUniversalInventory(id.get())) {
                    Slimefun.getRegistry().getUniversalInventories().computeIfAbsent(id.get(), _k -> new UniversalBlockMenu(preset));
                } else if (storage != null && !storage.hasInventory(l)) {
                    BlockMenu inventory = new BlockMenu(preset, l);
                    storage.inventories.put(l, inventory);
                }
            }
        }

        // It isn't possible for location to be null due to @NotNull, but @Nonnull is used by Slimefun and isn't compatible
        if (updateTicker && l != null) {
            SlimefunItem item = SlimefunItem.getById(key);

            if (item != null && item.isTicking() && value != null) {
                Slimefun.getTickerTask().enableTicker(l);
            }
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

    private static String serializeLocation(Location l) {
        return l.getWorld().getName() + ';' + l.getBlockX() + ';' + l.getBlockY() + ';' + l.getBlockZ();
    }

    @Nonnull
    private static Map<String, String> parseJSON(String json) {
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

    public BlockMenu loadInventory(Location l, BlockMenuPreset preset) {
        if (preset == null) {
            return null;
        }

        BlockMenu menu = new BlockMenu(preset, l);
        inventories.put(l, menu);
        return menu;
    }

    public boolean hasInventory(Location l) {
        return inventories.containsKey(l);
    }

    @Nullable
    public static String getChunkInfo(@Nonnull World w, int x, int y, String key) {
        BlockStorage storage = getStorage(w);
        if (storage == null) {
            return null;
        }
        return storage.getChunkInfo(x, y, key);
    }

    @Nullable
    public String getChunkInfo(int x, int y, String key) {
        Chunk chunk = this.world.getChunkAt(x, y);
        PersistentDataContainer dataContainer = chunk.getPersistentDataContainer();
        Plugin plugin = Slimefun.getPlugin(Slimefun.class);
        return dataContainer.get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
    }

    public static void setChunkInfo(@Nonnull World w, int x, int y, String key, String value) {
        BlockStorage storage = getStorage(w);
        if (storage == null) {
            return;
        }
        storage.setChunkInfo(x, y, key, value);
    }
    
    public void setChunkInfo(int x, int y, String key, String value) {
        Chunk chunk = this.world.getChunkAt(x, y);
        PersistentDataContainer dataContainer = chunk.getPersistentDataContainer();
        Plugin plugin = Slimefun.getPlugin(Slimefun.class);
        dataContainer.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
    }

    public static boolean hasChunkInfo(@Nonnull World w, int x, int y, @Nonnull String key) {
        BlockStorage storage = getStorage(w);
        if (storage == null) {
            return false;
        }
        return storage.hasChunkInfo(x, y, key);
    }

    public boolean hasChunkInfo(int x, int y, @Nonnull String key) {
        ChunkDataService dataService = Slimefun.getChunkDataService();
        Chunk chunk = this.world.getChunkAt(x, y);
        if (chunk == null) {
            return false; // Chunk not loaded for some reason
        }
        return dataService.hasChunkData(chunk, key);
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
        Optional<String> blockData = Slimefun.getBlockDataService().getBlockData(b, "id");

        if (blockData.isPresent()) {
            return blockData.get();
        }

        return null;
    }

    @Nullable
    public static String checkID(@Nonnull Location l) {
        Block b = l.getBlock();
        if (b == null) {
            return null;
        }
        return checkID(b);
    }

    public static boolean check(@Nonnull Location l, @Nullable String slimefunItem) {
        if (slimefunItem == null) {
            return false;
        }

        String id = checkID(l);
        return id != null && id.equals(slimefunItem);
    }

    public static @Nullable String getLocationInfo(Location l, @Nonnull String key) {
        Validate.notNull(key, "The key cannot be null!");

        BlockDataService blockDataService = Slimefun.getBlockDataService();
        Block b = l.getBlock();
        if (b == null) {
            return null;
        }
        Optional<String> blockData = blockDataService.getBlockData(b, key);
        if (blockData.isPresent()) {
            return blockData.get();
        }
        return null;
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

        BlockDataService blockDataService = Slimefun.getBlockDataService();
        Block b = l.getBlock();
        if (b == null) {
            return;
        }
        blockDataService.clearBlockData(b);

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

    public static void clearBlockInfo(@Nonnull Block b) {
        BlockDataService blockDataService = Slimefun.getBlockDataService();
        blockDataService.clearBlockData(b);
    }

    public static void clearBlockInfo(@Nonnull Location l) {
        Block b = l.getBlock();
        if (b == null) {
            return;
        }
        clearBlockInfo(b);
    }

    public static void moveBlockInfo(@Nonnull Location from, @Nonnull Location to) {
        BlockDataService blockDataService = Slimefun.getBlockDataService();
        Block fromBlock = from.getBlock();
        Block toBlock = to.getBlock();
        if (fromBlock == null || toBlock == null) {
            return;
        }
        blockDataService.moveBlockData(fromBlock, toBlock);
    }

    public void remove() {
        isMarkedForRemoval.set(true);
    }

    public static boolean hasInventory(@Nonnull Block b) {
        Location l = b.getLocation();
        BlockStorage storage = getStorage(l.getWorld());
        if (storage == null) {
            return false;
        }
        return storage.hasInventory(l);
    }

    public static void clearInventory(@Nonnull Block b) {
        Location l = b.getLocation();
        BlockStorage storage = getStorage(l.getWorld());
        if (storage == null) {
            return;
        }
        storage.clearInventory(l);
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

    public void clearInventory(@Nonnull Location l) {
        BlockMenu menu = getInventory(l);

        if (menu != null) {
            for (HumanEntity human : new ArrayList<>(menu.toInventory().getViewers())) {
                // Prevents "java.lang.IllegalStateException: Asynchronous entity add!"
                // when closing the inventory while holding an item
                Slimefun.runSync(human::closeInventory);
            }

            inventories.get(l).delete(l);
            inventories.remove(l);
        }
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

    public static boolean hasUniversalInventory(String id) {
        return Slimefun.getRegistry().getUniversalInventories().containsKey(id);
    }

    public static BlockMenu getInventory(Block b) {
        return getInventory(b.getLocation());
    }

    public static String getBlockInfoAsJson(Block block) {
        return getBlockInfoAsJson(block.getLocation());
    }

    public static String getBlockInfoAsJson(Location l) {
        Block b = l.getBlock();
        if (b == null) {
            return null;
        }
        BlockDataService blockDataService = Slimefun.getBlockDataService();
        // TODO | Try to support no required key to get all data. Problem is that namespaced keys are used for storage.
        // TODO | It's not a good solution to store everything on a single root level key just to fetch it all every time...
        Optional<String> data = blockDataService.getBlockData(b, "id");
        if (data.isPresent()) {
            return data.get();
        }
        return "{}";
    }

    public boolean isMarkedForRemoval() {
        return isMarkedForRemoval.get();
    }
}
