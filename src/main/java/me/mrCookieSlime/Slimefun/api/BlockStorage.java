package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

public class BlockStorage {

    private static final String PATH_BLOCKS = "data-storage/Slimefun/stored-blocks/";
    private static final String PATH_CHUNKS = "data-storage/Slimefun/stored-chunks/";

    private final World world;
    private final Map<Location, Config> storage = new ConcurrentHashMap<>();
    private final Map<Location, BlockMenu> inventories = new ConcurrentHashMap<>();
    private final Map<String, Config> blocksCache = new ConcurrentHashMap<>();

    public static BlockStorage getStorage(World world) {
        return SlimefunPlugin.getRegistry().getWorlds().get(world.getName());
    }

    public static BlockStorage getForcedStorage(World world) {
        return isWorldRegistered(world.getName()) ? SlimefunPlugin.getRegistry().getWorlds().get(world.getName()) : new BlockStorage(world);
    }

    private static String serializeLocation(Location l) {
        return l.getWorld().getName() + ';' + l.getBlockX() + ';' + l.getBlockY() + ';' + l.getBlockZ();
    }

    private static String locationToChunkString(Location l) {
        return l.getWorld().getName() + ";Chunk;" + (l.getBlockX() >> 4) + ';' + (l.getBlockZ() >> 4);
    }

    private static String serializeChunk(World world, int x, int z) {
        return world.getName() + ";Chunk;" + x + ';' + z;
    }

    private static Location deserializeLocation(String l) {
        try {
            String[] components = PatternUtils.SEMICOLON.split(l);
            if (components.length != 4) return null;

            World w = Bukkit.getWorld(components[0]);
            if (w != null) return new Location(w, Integer.parseInt(components[1]), Integer.parseInt(components[2]), Integer.parseInt(components[3]));
        }
        catch (NumberFormatException x) {
            Slimefun.getLogger().log(Level.WARNING, "Could not parse Number", x);
        }
        return null;
    }

    public BlockStorage(World w) {
        this.world = w;

        if (SlimefunPlugin.getRegistry().getWorlds().containsKey(w.getName())) {
            // Cancel the loading process if the world was already loaded
            return;
        }

        Slimefun.getLogger().log(Level.INFO, "Loading Blocks for World \"" + w.getName() + "\"");
        Slimefun.getLogger().log(Level.INFO, "This may take a long time...");

        File dir = new File(PATH_BLOCKS + w.getName());

        if (dir.exists()) {
            long total = dir.listFiles().length;
            long start = System.currentTimeMillis();
            long done = 0;
            long timestamp = System.currentTimeMillis();
            long totalBlocks = 0;
            int delay = SlimefunPlugin.getCfg().getInt("URID.info-delay");

            try {
                for (File file : dir.listFiles()) {
                    if (file.getName().equals("null.sfb")) {
                        Slimefun.getLogger().log(Level.WARNING, "Corrupted file detected!");
                        Slimefun.getLogger().log(Level.WARNING, "Slimefun will simply skip this File, but you");
                        Slimefun.getLogger().log(Level.WARNING, "should maybe look into it!");
                        Slimefun.getLogger().log(Level.WARNING, file.getPath());
                    }
                    else if (file.getName().endsWith(".sfb")) {
                        if (timestamp + delay < System.currentTimeMillis()) {
                            int progress = Math.round((((done * 100.0F) / total) * 100.0F) / 100.0F);
                            Slimefun.getLogger().log(Level.INFO, "Loading Blocks... {0}% done (\"{1}\")", new Object[] { progress, w.getName() });
                            timestamp = System.currentTimeMillis();
                        }

                        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

                        for (String key : cfg.getKeys(false)) {
                            Location l = deserializeLocation(key);
                            String chunkString = locationToChunkString(l);

                            try {
                                totalBlocks++;
                                String json = cfg.getString(key);
                                Config blockInfo = parseBlockInfo(l, json);

                                if (blockInfo != null && blockInfo.contains("id")) {
                                    if (storage.containsKey(l)) {
                                        // It should not be possible to have two blocks on the same location. Ignore the
                                        // new entry if a block is already present and print an error to the console.

                                        Slimefun.getLogger().log(Level.INFO, "Ignoring duplicate block @ " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
                                        Slimefun.getLogger().log(Level.INFO, "Old block data: {0}", serializeBlockInfo(storage.get(l)));
                                        Slimefun.getLogger().log(Level.INFO, "New block data ({0}): {1}", new Object[] { key, json });
                                        continue;
                                    }

                                    storage.put(l, blockInfo);

                                    if (SlimefunPlugin.getRegistry().getTickerBlocks().contains(file.getName().replace(".sfb", ""))) {
                                        Set<Location> locations = SlimefunPlugin.getRegistry().getActiveTickers().getOrDefault(chunkString, new HashSet<>());
                                        locations.add(l);
                                        SlimefunPlugin.getRegistry().getActiveTickers().put(chunkString, locations);

                                        if (!SlimefunPlugin.getRegistry().getActiveChunks().contains(chunkString)) {
                                            SlimefunPlugin.getRegistry().getActiveChunks().add(chunkString);
                                        }
                                    }
                                }
                            }
                            catch (Exception x) {
                                Slimefun.getLogger().log(Level.WARNING, "Failed to load " + file.getName() + '(' + key + ") for Slimefun " + SlimefunPlugin.getVersion(), x);
                            }
                        }
                        done++;
                    }
                }
            }
            finally {
                long time = (System.currentTimeMillis() - start);
                Slimefun.getLogger().log(Level.INFO, "Loading Blocks... 100% (FINISHED - {0}ms)", time);
                Slimefun.getLogger().log(Level.INFO, "Loaded a total of {0} Blocks for World \"{1}\"", new Object[] { totalBlocks, world.getName() });

                if (totalBlocks > 0) {
                    Slimefun.getLogger().log(Level.INFO, "Avg: {0}ms/Block", DoubleHandler.fixDouble((double) time / (double) totalBlocks, 3));
                }
            }
        }
        else {
            dir.mkdirs();
        }

        File chunks = new File(PATH_CHUNKS + "chunks.sfc");

        if (chunks.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(chunks);

            for (String key : cfg.getKeys(false)) {
                try {
                    if (world.getName().equals(PatternUtils.SEMICOLON.split(key)[0])) {
                        SlimefunPlugin.getRegistry().getChunks().put(key, new BlockInfoConfig(parseJSON(cfg.getString(key))));
                    }
                }
                catch (Exception x) {
                    Slimefun.getLogger().log(Level.WARNING, "Failed to load " + chunks.getName() + " in World " + world.getName() + '(' + key + ") for Slimefun " + SlimefunPlugin.getVersion(), x);
                }
            }
        }

        SlimefunPlugin.getRegistry().getWorlds().put(world.getName(), this);

        for (File file : new File("data-storage/Slimefun/stored-inventories").listFiles()) {
            if (file.getName().startsWith(w.getName()) && file.getName().endsWith(".sfi")) {
                Location l = deserializeLocation(file.getName().replace(".sfi", ""));
                io.github.thebusybiscuit.cscorelib2.config.Config cfg = new io.github.thebusybiscuit.cscorelib2.config.Config(file);

                try {
                    BlockMenuPreset preset = BlockMenuPreset.getPreset(cfg.getString("preset"));

                    if (preset == null) {
                        preset = BlockMenuPreset.getPreset(checkID(l));
                    }

                    if (preset != null) {
                        inventories.put(l, new BlockMenu(preset, l, cfg));
                    }
                }
                catch (Exception x) {
                    Slimefun.getLogger().log(Level.SEVERE, "An Error occured while loading this Inventory: " + file.getName(), x);
                }
            }
        }

        for (File file : new File("data-storage/Slimefun/universal-inventories").listFiles()) {
            if (file.getName().endsWith(".sfi")) {
                io.github.thebusybiscuit.cscorelib2.config.Config cfg = new io.github.thebusybiscuit.cscorelib2.config.Config(file);
                BlockMenuPreset preset = BlockMenuPreset.getPreset(cfg.getString("preset"));

                if (preset != null) {
                    SlimefunPlugin.getRegistry().getUniversalInventories().put(preset.getID(), new UniversalBlockMenu(preset, cfg));
                }
            }
        }
    }

    private static int chunkChanges = 0;
    private int changes = 0;

    public void computeChanges() {
        changes = blocksCache.size() + chunkChanges;

        Map<Location, BlockMenu> inventories2 = new HashMap<>(inventories);
        for (Map.Entry<Location, BlockMenu> entry : inventories2.entrySet()) {
            changes += entry.getValue().getUnsavedChanges();
        }

        Map<String, UniversalBlockMenu> universalInventories2 = new HashMap<>(SlimefunPlugin.getRegistry().getUniversalInventories());
        for (Map.Entry<String, UniversalBlockMenu> entry : universalInventories2.entrySet()) {
            changes += entry.getValue().getUnsavedChanges();
        }
    }

    public int getChanges() {
        return changes;
    }

    public void save(boolean remove) {
        this.save(true, remove);
    }

    public void save(boolean computeChanges, boolean remove) {
        if (computeChanges) computeChanges();
        if (changes == 0) return;

        Slimefun.getLogger().log(Level.INFO, "Saving Blocks for World \"" + world.getName() + "\" (" + changes + " Change(s) queued)");

        Map<String, Config> cache = new HashMap<>(blocksCache);

        for (Map.Entry<String, Config> entry : cache.entrySet()) {
            blocksCache.remove(entry.getKey());
            Config cfg = entry.getValue();

            if (cfg.getKeys().isEmpty()) {
                File file = cfg.getFile();
                if (file.exists() && !file.delete()) {
                    Slimefun.getLogger().log(Level.WARNING, "Could not delete File: " + file.getName());
                }
            }
            else {
                File tmpFile = new File(cfg.getFile().getParentFile(), cfg.getFile().getName() + ".tmp");
                cfg.save(tmpFile);

                try {
                    Files.move(tmpFile.toPath(), cfg.getFile().toPath(), StandardCopyOption.ATOMIC_MOVE);
                }
                catch (IOException x) {
                    Slimefun.getLogger().log(Level.SEVERE, "An Error occured while copying a temporary File for Slimefun " + SlimefunPlugin.getVersion(), x);
                }
            }
        }

        Map<Location, BlockMenu> inventories2 = new HashMap<>(inventories);

        for (Map.Entry<Location, BlockMenu> entry : inventories2.entrySet()) {
            entry.getValue().save(entry.getKey());
        }

        Map<String, UniversalBlockMenu> universalInventories2 = new HashMap<>(SlimefunPlugin.getRegistry().getUniversalInventories());

        for (Map.Entry<String, UniversalBlockMenu> entry : universalInventories2.entrySet()) {
            entry.getValue().save();
        }

        if (chunkChanges > 0) {
            File chunks = new File(PATH_CHUNKS + "chunks.sfc");
            Config cfg = new Config(PATH_CHUNKS + "chunks.temp");

            for (Map.Entry<String, BlockInfoConfig> entry : SlimefunPlugin.getRegistry().getChunks().entrySet()) {
                cfg.setValue(entry.getKey(), entry.getValue().toJSON());
            }

            cfg.save(chunks);

            if (remove) {
                SlimefunPlugin.getRegistry().getWorlds().remove(world.getName());
            }
        }

        changes = 0;
        chunkChanges = 0;
    }

    public static void store(Block block, ItemStack item) {
        SlimefunItem sfitem = SlimefunItem.getByItem(item);
        if (sfitem != null) addBlockInfo(block, "id", sfitem.getID(), true);
    }

    public static void store(Block block, String item) {
        addBlockInfo(block, "id", item, true);
    }

    /**
     * Retrieves the SlimefunItem's ItemStack from the specified Block.
     * If the specified Block is registered in BlockStorage, its data will be erased from it, regardless of the returned
     * value.
     *
     * @param block
     *            the block to retrieve the ItemStack from
     * @return the SlimefunItem's ItemStack corresponding to the block if it has one, otherwise null
     *
     * @since 4.0
     */
    public static ItemStack retrieve(Block block) {
        if (!hasBlockInfo(block)) return null;
        else {
            final SlimefunItem item = SlimefunItem.getByID(getLocationInfo(block.getLocation(), "id"));
            clearBlockInfo(block);
            if (item == null) return null;
            else return item.getItem();
        }
    }

    public static Config getLocationInfo(Location l) {
        BlockStorage storage = getStorage(l.getWorld());
        Config cfg = storage.storage.get(l);
        return cfg == null ? new BlockInfoConfig() : cfg;
    }

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

    private static BlockInfoConfig parseBlockInfo(Location l, String json) {
        try {
            return new BlockInfoConfig(parseJSON(json));
        }
        catch (Exception x) {
            Logger logger = Slimefun.getLogger();
            logger.log(Level.WARNING, x.getClass().getName());
            logger.log(Level.WARNING, "Failed to parse BlockInfo for Block @ " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
            logger.log(Level.WARNING, json);
            logger.log(Level.WARNING, "");
            logger.log(Level.WARNING, "IGNORE THIS ERROR UNLESS IT IS SPAMMING");
            logger.log(Level.WARNING, "");
            logger.log(Level.SEVERE, "An Error occured while parsing Block Info for Slimefun " + SlimefunPlugin.getVersion(), x);
            return null;
        }
    }

    private static String serializeBlockInfo(Config cfg) {
        JsonObject json = new JsonObject();

        for (String key : cfg.getKeys()) {
            json.add(key, new JsonPrimitive(cfg.getString(key)));
        }

        return json.toString();
    }

    public static String getLocationInfo(Location l, String key) {
        return getLocationInfo(l).getString(key);
    }

    public static void addBlockInfo(Location l, String key, String value) {
        addBlockInfo(l, key, value, false);
    }

    public static void addBlockInfo(Block block, String key, String value) {
        addBlockInfo(block.getLocation(), key, value);
    }

    public static void addBlockInfo(Block block, String key, String value, boolean updateTicker) {
        addBlockInfo(block.getLocation(), key, value, updateTicker);
    }

    public static void addBlockInfo(Location l, String key, String value, boolean updateTicker) {
        Config cfg = hasBlockInfo(l) ? getLocationInfo(l) : new BlockInfoConfig();
        cfg.setValue(key, value);
        setBlockInfo(l, cfg, updateTicker);
    }

    public static boolean hasBlockInfo(Block block) {
        return hasBlockInfo(block.getLocation());
    }

    public static boolean hasBlockInfo(Location l) {
        BlockStorage storage = getStorage(l.getWorld());
        return storage != null && storage.storage.containsKey(l) && getLocationInfo(l, "id") != null;
    }

    public static void setBlockInfo(Block block, Config cfg, boolean updateTicker) {
        setBlockInfo(block.getLocation(), cfg, updateTicker);
    }

    public static void setBlockInfo(Location l, Config cfg, boolean updateTicker) {
        BlockStorage storage = getStorage(l.getWorld());
        storage.storage.put(l, cfg);

        String id = cfg.getString("id");

        if (BlockMenuPreset.isInventory(id)) {
            if (BlockMenuPreset.isUniversalInventory(id)) {
                if (!SlimefunPlugin.getRegistry().getUniversalInventories().containsKey(id)) {
                    storage.loadUniversalInventory(BlockMenuPreset.getPreset(id));
                }
            }
            else if (!storage.hasInventory(l)) {
                File file = new File("data-storage/Slimefun/stored-inventories/" + serializeLocation(l) + ".sfi");

                if (file.exists()) storage.inventories.put(l, new BlockMenu(BlockMenuPreset.getPreset(id), l, new io.github.thebusybiscuit.cscorelib2.config.Config(file)));
                else storage.loadInventory(l, BlockMenuPreset.getPreset(id));
            }
        }

        refreshCache(getStorage(l.getWorld()), l, id, serializeBlockInfo(cfg), updateTicker);
    }

    public static void setBlockInfo(Block b, String json, boolean updateTicker) {
        setBlockInfo(b.getLocation(), json, updateTicker);
    }

    public static void setBlockInfo(Location l, String json, boolean updateTicker) {
        Config blockInfo = json == null ? new BlockInfoConfig() : parseBlockInfo(l, json);
        if (blockInfo == null) return;
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
        SlimefunPlugin.getTicker().queueDelete(l, destroy);
    }

    public static void _integrated_removeBlockInfo(Location l, boolean destroy) {
        BlockStorage storage = getStorage(l.getWorld());

        if (hasBlockInfo(l)) {
            refreshCache(storage, l, getLocationInfo(l).getString("id"), null, destroy);
            storage.storage.remove(l);
        }

        if (destroy) {
            if (storage.hasInventory(l)) storage.clearInventory(l);

            UniversalBlockMenu universalInventory = getUniversalInventory(l);
            if (universalInventory != null) {
                universalInventory.close();
                universalInventory.save();
            }

            String chunkString = locationToChunkString(l);
            if (SlimefunPlugin.getRegistry().getActiveTickers().containsKey(chunkString)) {
                Set<Location> locations = SlimefunPlugin.getRegistry().getActiveTickers().get(chunkString);
                locations.remove(l);

                if (locations.isEmpty()) {
                    SlimefunPlugin.getRegistry().getActiveTickers().remove(chunkString);
                    SlimefunPlugin.getRegistry().getActiveChunks().remove(chunkString);
                }
                else SlimefunPlugin.getRegistry().getActiveTickers().put(chunkString, locations);
            }
        }
    }

    public static void moveBlockInfo(Location from, Location to) {
        SlimefunPlugin.getTicker().queueMove(from, to);
    }

    public static void _integrated_moveLocationInfo(Location from, Location to) {
        if (!hasBlockInfo(from)) return;
        BlockStorage storage = getStorage(from.getWorld());

        setBlockInfo(to, getLocationInfo(from), true);

        if (storage.inventories.containsKey(from)) {
            BlockMenu menu = storage.inventories.get(from);
            storage.inventories.put(to, menu);
            storage.clearInventory(from);
            menu.move(to);
        }

        refreshCache(storage, from, getLocationInfo(from).getString("id"), null, true);
        storage.storage.remove(from);

        String chunkString = locationToChunkString(from);

        if (SlimefunPlugin.getRegistry().getActiveTickers().containsKey(chunkString)) {
            Set<Location> locations = SlimefunPlugin.getRegistry().getActiveTickers().get(chunkString);
            locations.remove(from);

            if (locations.isEmpty()) {
                SlimefunPlugin.getRegistry().getActiveTickers().remove(chunkString);
                SlimefunPlugin.getRegistry().getActiveChunks().remove(chunkString);
            }
            else SlimefunPlugin.getRegistry().getActiveTickers().put(chunkString, locations);
        }
    }

    private static void refreshCache(BlockStorage storage, Location l, String key, String value, boolean updateTicker) {
        if (key == null) {
            // This Block is no longer valid...
            // Fixes #1577
            return;
        }

        Config cfg = storage.blocksCache.computeIfAbsent(key, k -> new Config(PATH_BLOCKS + l.getWorld().getName() + '/' + key + ".sfb"));
        cfg.setValue(serializeLocation(l), value);

        if (updateTicker) {
            SlimefunItem item = SlimefunItem.getByID(key);

            if (item != null && item.isTicking()) {
                String chunkString = locationToChunkString(l);

                if (value != null) {
                    Set<Location> locations = SlimefunPlugin.getRegistry().getActiveTickers().get(chunkString);
                    if (locations == null) locations = new HashSet<>();

                    locations.add(l);
                    SlimefunPlugin.getRegistry().getActiveTickers().put(chunkString, locations);
                    SlimefunPlugin.getRegistry().getActiveChunks().add(chunkString);
                }
            }
        }
    }

    public static SlimefunItem check(Block block) {
        return check(block.getLocation());
    }

    public static SlimefunItem check(Location l) {
        if (!hasBlockInfo(l)) return null;
        return SlimefunItem.getByID(getLocationInfo(l, "id"));
    }

    public static String checkID(Block b) {
        if (SlimefunPlugin.getBlockDataService().isTileEntity(b.getType())) {
            Optional<String> blockData = SlimefunPlugin.getBlockDataService().getBlockData(b);

            if (blockData.isPresent()) return blockData.get();
        }

        return checkID(b.getLocation());
    }

    public static boolean check(Block block, String slimefunItem) {
        String id = checkID(block);
        return id != null && id.equals(slimefunItem);
    }

    public static String checkID(Location l) {
        if (!hasBlockInfo(l)) return null;
        return getLocationInfo(l, "id");
    }

    public static boolean check(Location l, String slimefunItem) {
        if (!hasBlockInfo(l)) return false;
        try {
            String id = getLocationInfo(l, "id");
            return id != null && id.equalsIgnoreCase(slimefunItem);
        }
        catch (NullPointerException x) {
            return false;
        }
    }

    public static boolean isWorldRegistered(String name) {
        return SlimefunPlugin.getRegistry().getWorlds().containsKey(name);
    }

    public static Set<String> getTickingChunks() {
        return new HashSet<>(SlimefunPlugin.getRegistry().getActiveChunks());
    }

    public static Set<Location> getTickingLocations(Chunk chunk) {
        return getTickingLocations(chunk.toString());
    }

    public static Set<Location> getTickingLocations(String chunk) {
        return new HashSet<>(SlimefunPlugin.getRegistry().getActiveTickers().get(chunk));
    }

    public BlockMenu loadInventory(Location l, BlockMenuPreset preset) {
        BlockMenu menu = new BlockMenu(preset, l);
        inventories.put(l, menu);
        return menu;
    }

    public void loadUniversalInventory(BlockMenuPreset preset) {
        SlimefunPlugin.getRegistry().getUniversalInventories().put(preset.getID(), new UniversalBlockMenu(preset));
    }

    public void clearInventory(Location l) {
        BlockMenu menu = getInventory(l);

        if (menu != null) {
            for (HumanEntity human : new ArrayList<>(menu.toInventory().getViewers())) {
                // Prevents "java.lang.IllegalStateException: Asynchronous entity add!" when closing inventory while
                // holding an item
                Slimefun.runSync(human::closeInventory);
            }

            inventories.get(l).delete(l);
            inventories.remove(l);
        }
    }

    public boolean hasInventory(Location l) {
        return inventories.containsKey(l);
    }

    public static boolean hasUniversalInventory(String id) {
        return SlimefunPlugin.getRegistry().getUniversalInventories().containsKey(id);
    }

    public static UniversalBlockMenu getUniversalInventory(Block block) {
        return getUniversalInventory(block.getLocation());
    }

    public static UniversalBlockMenu getUniversalInventory(Location l) {
        String id = checkID(l);
        return id == null ? null : getUniversalInventory(id);
    }

    public static UniversalBlockMenu getUniversalInventory(String id) {
        return SlimefunPlugin.getRegistry().getUniversalInventories().get(id);
    }

    public static BlockMenu getInventory(Block b) {
        return getInventory(b.getLocation());
    }

    public static boolean hasInventory(Block b) {
        BlockStorage storage = getStorage(b.getWorld());
        if (storage == null) return false;
        else return storage.hasInventory(b.getLocation());
    }

    public static BlockMenu getInventory(Location l) {
        BlockStorage storage = getStorage(l.getWorld());
        if (storage == null) return null;

        BlockMenu menu = storage.inventories.get(l);
        if (menu != null) return menu;
        else return storage.loadInventory(l, BlockMenuPreset.getPreset(checkID(l)));
    }

    public static Config getChunkInfo(World world, int x, int z) {
        try {
            BlockInfoConfig cfg = SlimefunPlugin.getRegistry().getChunks().get(serializeChunk(world, x, z));
            return cfg == null ? new BlockInfoConfig() : cfg;
        }
        catch (Exception e) {
            Slimefun.getLogger().log(Level.SEVERE, "Failed to parse ChunkInfo for Slimefun " + SlimefunPlugin.getVersion(), x);
            return new BlockInfoConfig();
        }
    }

    public static void setChunkInfo(World world, int x, int z, String key, String value) {
        String serializedChunk = serializeChunk(world, x, z);
        BlockInfoConfig cfg = SlimefunPlugin.getRegistry().getChunks().get(serializedChunk);

        if (cfg == null) {
            cfg = new BlockInfoConfig();
            SlimefunPlugin.getRegistry().getChunks().put(serializedChunk, cfg);
        }

        cfg.setValue(key, value);

        chunkChanges++;
    }

    public static boolean hasChunkInfo(World world, int x, int z) {
        String serializedChunk = serializeChunk(world, x, z);
        return SlimefunPlugin.getRegistry().getChunks().containsKey(serializedChunk);
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
