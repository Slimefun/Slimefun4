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
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
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
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

public class BlockStorage implements Listener {
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

        // For loaded chunks, handle loading the chunk data. This is done as the world might already have loaded chunks that
        // would not trigger the Chunk load event in a way that this instance would have handled
        for (Chunk c : w.getLoadedChunks()) {
            loadChunk(c);
        }

        loadUniversalInventories();

        Slimefun.getRegistry().getWorlds().put(world.getName(), this);

        // Register the events for this class instance
        Bukkit.getPluginManager().registerEvents(this, Slimefun.getPlugin(Slimefun.class));
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        if (!e.getWorld().equals(world)) {
            return; // Not our world
        }
        // We need to load BlockMenu and start Tickers for this chunk if they're not already started
        Chunk c = e.getChunk();
        loadChunk(c);
    }

    private void loadChunk(Chunk c) {
        // Load data for the Chunk
        
        // Load data for blocks in the chunk
        Set<Block> blocks = CustomBlockData.getBlocksWithCustomData(Slimefun.getPlugin(Slimefun.class), c);
        loadBlocks(blocks);
    }

    private void loadBlocks(Set<Block> blocks) {
        long start = System.currentTimeMillis();
        long done = 0;
        long timestamp = System.currentTimeMillis();
        long totalBlocks = 0;
        int delay = Slimefun.getCfg().getInt("URID.info-delay");
        long total = blocks.size();
        if (total == 0) {
            return;
        }

        try {
            for (Block b : blocks) {
                loadBlock(b);
                totalBlocks++;
            }
            int progress = Math.round((((done * 100.0F) / total) * 100.0F) / 100.0F);
            if (timestamp + delay < System.currentTimeMillis()) {
                Slimefun.logger().log(Level.INFO, "Loading Blocks... {0}% done (in \"{1}\")", new Object[] { progress, world.getName() });  
            }
        } finally {
            long time = (System.currentTimeMillis() - start);
            Slimefun.logger().log(Level.INFO, "Loading Blocks... 100% (FINISHED - {0}ms)", time);
            Slimefun.logger().log(Level.INFO, "Loaded a total of {0} Blocks for Chunk", new Object[] { totalBlocks });

            if (totalBlocks > 0) {
                Slimefun.logger().log(Level.INFO, "Avg: {0}ms/Block", NumberUtils.roundDecimalNumber((double) time / (double) totalBlocks));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent e) {
        if (!e.getWorld().equals(world)) {
            return; // Not our world
        }
        Chunk c = e.getChunk();
        saveChunk(c);
    }

    private void saveChunk(Chunk c) {
        // Unload data for the Chunk
        Set<Block> blocks = CustomBlockData.getBlocksWithCustomData(Slimefun.getPlugin(Slimefun.class), c);

        if (blocks.size() == 0) {
            return; // No blocks to unload
        }

        Slimefun.logger().log(Level.INFO, "Unloading {0} Blocks for Chunk in {1}", new Object[] { blocks.size(), world.getName() });

        for (Block block : blocks) {
            unloadBlock(block);
        }
    }

    private void loadBlock(Block b) {
        Location l = b.getLocation();

        if (l == null) {
            // That location was malformed, we will skip this one
            return;
        }

        String id = getLocationInfo(l, "id");
        if (id == null) {
            // Not a valid SF block, so can't load it - shouldn't happen
            return; 
        }
        BlockMenuPreset preset = BlockMenuPreset.getPreset(getLocationInfo(l, "preset"));
        if (preset == null) {
            preset = BlockMenuPreset.getPreset(checkID(l));
        }
        if (preset != null) {
            inventories.put(l, new BlockMenu(preset, l));
        }

        if (Slimefun.getRegistry().getTickerBlocks().contains(id)) {
            Slimefun.getTickerTask().enableTicker(l);
        }
    }

    private void unloadBlock(Block b) {
        Location l = b.getLocation();

        if (l == null) {
            // That location was malformed, we will skip this one
            return;
        }
        String id = getLocationInfo(l, "id");
        if (id == null) {
            // Not a valid SF block, so can't unload it - shouldn't happen
            return; 
        }

        if (Slimefun.getRegistry().getTickerBlocks().contains(id)) {
            Slimefun.getTickerTask().disableTicker(l);
        }

        BlockMenu menu = inventories.get(l);
        if (menu != null) {
            menu.save(l);
            inventories.remove(l);
        }
    }

    private void loadUniversalInventories() {
        if (universalInventoriesLoaded) {
            return;
        }

        universalInventoriesLoaded = true;

        File[] files = new File("data-storage/Slimefun/universal-inventories").listFiles();

        if (files == null) {
            return; // No universal inventories
        }

        for (File file : files) {
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
        boolean hasBlockData = blockData.isPresent();
        return hasBlockData;
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

    public void saveAndRemove() {
        for (Chunk c : world.getLoadedChunks()) {
            // I'd love to call saveChunk to save inventories... but that would cause an IllegalPluginAccessException
            // as the world is being unloaded and plugin is disabled. The CustomBlockData (to be replaced) lib, uses a
            // Runnable to set the block as no longer dirty a tick after it was marked dirty. Ideally we'd like to ignore this
            // on plugin shutdown since it wont matter in about 500ms anyway.
            // saveChunk(c);
        }

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
