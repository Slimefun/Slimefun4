package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

public class BlockStorage {

	private static final String path_blocks = "data-storage/Slimefun/stored-blocks/";
	private static final String path_chunks = "data-storage/Slimefun/stored-chunks/";

	public static Map<String, BlockStorage> worlds = new HashMap<String, BlockStorage>();
	public static Map<String, Set<Location>> ticking_chunks = new HashMap<String, Set<Location>>();
	public static Set<String> loaded_tickers = new HashSet<String>();
	
	private World world;
	
	private Map<Location, String> storage = new HashMap<Location, String>();
	private static Map<String, String> map_chunks = new HashMap<String, String>();
	
	private Map<Location, BlockMenu> inventories = new HashMap<Location, BlockMenu>();
	public static Map<String, UniversalBlockMenu> universal_inventories = new HashMap<String, UniversalBlockMenu>();
	
	private Map<String, Config> cache_blocks = new HashMap<String, Config>();
	
	public static int info_delay;
	
	public static BlockStorage getStorage(World world) {
		return worlds.get(world.getName());
	}
	
	public static BlockStorage getForcedStorage(World world) {
		return isWorldRegistered(world.getName()) ? worlds.get(world.getName()): new BlockStorage(world);
	}
	
	private static String serializeLocation(Location l) {
		return l.getWorld().getName() + ";" + l.getBlockX() + ";" + l.getBlockY() + ";" + l.getBlockZ();
	}
	
	private static String serializeChunk(Chunk chunk) {
		return chunk.getWorld().getName() + ";Chunk;" + chunk.getX() + ";" + chunk.getZ();
	}

	private static String locationToChunkString(Location l) {
		return l.getWorld().getName() + ";Chunk;" + (l.getBlockX() >> 4) + ";" + (l.getBlockZ() >> 4);
	}
	
	private static Location deserializeLocation(String l) {
		try {
			World w = Bukkit.getWorld(l.split(";")[0]);
			if (w != null) return new Location(w, Integer.parseInt(l.split(";")[1]), Integer.parseInt(l.split(";")[2]), Integer.parseInt(l.split(";")[3]));
		} catch(NumberFormatException x) {
		}
		return null;
	}
	
	public BlockStorage(final World w) {
		if (worlds.containsKey(w.getName())) return;
		this.world = w;
		System.out.println("[Slimefun] Loading Blocks for World \"" + w.getName() + "\"");
		System.out.println("[Slimefun] This may take a long time...");
		
		File f = new File(path_blocks + w.getName());
		if (f.exists()) {
			long total = f.listFiles().length, start = System.currentTimeMillis();
			long done = 0, timestamp = System.currentTimeMillis(), totalBlocks = 0;
			
			try {
				for (File file: f.listFiles()) {
					if (file.getName().endsWith(".sfb")) {
						if (timestamp + info_delay < System.currentTimeMillis()) {
							System.out.println("[Slimefun] Loading Blocks... " + Math.round((((done * 100.0f) / total) * 100.0f) / 100.0f) + "% done (\"" + w.getName() + "\")");
							timestamp = System.currentTimeMillis();
						}
						
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
						for (String key: cfg.getKeys(false)) {
							Location l = deserializeLocation(key);
							String chunk_string = locationToChunkString(l);
							try {
								totalBlocks++;
								storage.put(l, cfg.getString(key));
								
								if (SlimefunItem.isTicking(file.getName().replace(".sfb", ""))) {
									Set<Location> locations = ticking_chunks.containsKey(chunk_string) ? ticking_chunks.get(chunk_string): new HashSet<Location>();
									locations.add(l);
									ticking_chunks.put(chunk_string, locations);
									if (!loaded_tickers.contains(chunk_string)) loaded_tickers.add(chunk_string);
								}
							} catch(Exception x) {
								System.err.println("[Slimefun] Failed to load " + file.getName() + "(ERR: " + key + ")");
								x.printStackTrace();
							}
						}
						done++;
					}
				}
			} finally {
				long time = (System.currentTimeMillis() - start);
				System.out.println("[Slimefun] Loading Blocks... 100% (FINISHED - " + time + "ms)");
				System.out.println("[Slimefun] Loaded a total of " + totalBlocks + " Blocks for World \"" + world.getName() + "\"");
				if (totalBlocks > 0) System.out.println("[Slimefun] Avg: " + DoubleHandler.fixDouble((double) time / (double) totalBlocks, 3) + "ms/Block");
			}
		}
		else f.mkdirs();
		
		File chunks = new File(path_chunks + "chunks.sfc");
		if (chunks.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(chunks);
			for (String key: cfg.getKeys(false)) {
				try {
					if (world.getName().equals(key.split(";")[0])) map_chunks.put(key, cfg.getString(key));
				} catch(Exception x) {
					System.err.println("[Slimefun] Failed to load " + chunks.getName() + " for World \"" + world.getName() + "\" (ERR: " + key + ")");
					x.printStackTrace();
				}
			}
		}
		
		worlds.put(world.getName(), this);
		
		for (File file: new File("data-storage/Slimefun/stored-inventories").listFiles()) {
			if (file.getName().startsWith(w.getName()) && file.getName().endsWith(".sfi")) {
				Location l = deserializeLocation(file.getName().replace(".sfi", ""));
				Config cfg = new Config(file);
				try {
					if (cfg.getString("preset") != null) {
						BlockMenuPreset preset = BlockMenuPreset.getPreset(cfg.getString("preset"));
						inventories.put(l, new BlockMenu(preset, l, cfg));
					}
					else {
						BlockMenuPreset preset = BlockMenuPreset.getPreset(checkID(l));
						inventories.put(l, new BlockMenu(preset, l, cfg));
					}
				}
				catch(Exception x) {
				}
			}
		}
		
		for (File file: new File("data-storage/Slimefun/universal-inventories").listFiles()) {
			if (file.getName().endsWith(".sfi")) {
				Config cfg = new Config(file);
				BlockMenuPreset preset = BlockMenuPreset.getPreset(cfg.getString("preset"));
				universal_inventories.put(preset.getID(), new UniversalBlockMenu(preset, cfg));
			}
		}
	}

	private static int chunk_changes = 0;
	private int changes = 0;
	
	public void computeChanges() {
		changes = cache_blocks.size() + chunk_changes;
		
		Map<Location, BlockMenu> inventories2 = new HashMap<Location, BlockMenu>(inventories);
		for (Map.Entry<Location, BlockMenu> entry: inventories2.entrySet()) {
			changes += entry.getValue().changes;
		}
		
		Map<String, UniversalBlockMenu> universal_inventories2 = new HashMap<String, UniversalBlockMenu>(universal_inventories);
		for (Map.Entry<String, UniversalBlockMenu> entry: universal_inventories2.entrySet()) {
			changes += entry.getValue().changes;
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
		
		System.out.println("[Slimefun] Saving Blocks for World \"" + world.getName() + "\" (" + changes + " Changes queued)");
		
		Map<String, Config> cache = new HashMap<String, Config>(cache_blocks);
		
		for (Map.Entry<String, Config> entry: cache.entrySet()) {
			cache_blocks.remove(entry.getKey());
			Config cfg = entry.getValue();
			if (cfg.getKeys().isEmpty()) cfg.getFile().delete();
			else cfg.save();
		}
		
		Map<Location, BlockMenu> inventories2 = new HashMap<Location, BlockMenu>(inventories);
		
		for (Map.Entry<Location, BlockMenu> entry: inventories2.entrySet()) {
			entry.getValue().save(entry.getKey());
		}
		
		Map<String, UniversalBlockMenu> universal_inventories2 = new HashMap<String, UniversalBlockMenu>(universal_inventories);
		
		for (Map.Entry<String, UniversalBlockMenu> entry: universal_inventories2.entrySet()) {
			entry.getValue().save();
		}
		
		if (chunk_changes > 0) {
			File chunks = new File(path_chunks + "chunks.sfc");
			Config cfg = new Config("data-storage/Slimefun/temp.yml");
			
			for (Map.Entry<String, String> entry: map_chunks.entrySet()) {
				cfg.setValue(entry.getKey(), entry.getValue());
			}
			
			cfg.save(chunks);
			
			if (remove) {
				worlds.remove(world.getName());
			}
		}
		
		changes = 0;
		chunk_changes = 0;
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
     * If the specified Block is registered in BlockStorage, its data will be erased from it, regardless of the returned value.
     *
     * @param  block  the block to retrieve the ItemStack from
     * @return the SlimefunItem's ItemStack corresponding to the block if it has one, otherwise null
     *
     * @since 4.0
     */
	public static ItemStack retrieve(Block block) {
		if (!hasBlockInfo(block)) return null;
		else {
			final SlimefunItem item = SlimefunItem.getByID(getBlockInfo(block, "id"));
			clearBlockInfo(block);
			if (item == null) return null;
			else return item.getItem();
		}
	}

	@Deprecated
	public static Config getBlockInfo(Block block) {
		return getLocationInfo(block.getLocation());
	}

	@Deprecated
	public static Config getBlockInfo(Location l) {
		return getLocationInfo(l);
	}

	public static Config getLocationInfo(Location l) {
		try {
			BlockStorage storage = getStorage(l.getWorld());
			Config cfg = new Config("data-storage/Slimefun/temp.yml");
			if (!storage.storage.containsKey(l)) return cfg;
			
			for (Map.Entry<String, String> entry: parseJSON(getJSONData(l)).entrySet()) {
				cfg.setValue(entry.getKey(), entry.getValue());
			}
			
			return cfg;
		} catch(Exception x) {
			System.err.println(x.getClass().getName());
			System.err.println("[Slimefun] Failed to parse BlockInfo for Block @ " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
			try {
				System.err.println(getJSONData(l));
			} catch (Exception x2) {
				System.err.println("No Metadata found!");
			}
			System.err.println("[Slimefun] ");
			System.err.println("[Slimefun] IGNORE THIS ERROR UNLESS IT IS SPAMMING");
			System.err.println("[Slimefun] ");
			x.printStackTrace();
			return new Config("data-storage/Slimefun/temp.yml");
		}
	}
	
	private static Map<String, String> parseJSON(String json) {
		Map<String, String> map = new HashMap<String, String>();
		
		if (json != null && json.length() > 2) {
			try {
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				for (Object entry: obj.keySet()) {
					String key = entry.toString();
					String value = obj.get(entry).toString();
					map.put(key, value);
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	private static String getJSONData(Location l) {
		BlockStorage storage = getStorage(l.getWorld());
		return storage.storage.get(l);
	}
	
	private static String getJSONData(Chunk chunk) {
		return map_chunks.get(serializeChunk(chunk));
	}

	@Deprecated
	public static String getBlockInfo(Block block, String key) {
		return getBlockInfo(block.getLocation(), key);
	}

	@Deprecated
	public static String getBlockInfo(Location l, String key) {
		return getLocationInfo(l, key);
	}

	public static String getLocationInfo(Location l, String key) {
		return parseJSON(getJSONData(l)).get(key);
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
		Config cfg = new Config("data-storage/Slimefun/temp.yml");
		if (hasBlockInfo(l)) cfg = getBlockInfo(l);
		cfg.setValue(key, value);
		setBlockInfo(l, cfg, updateTicker);
	}
	
	public static boolean hasBlockInfo(Block block) {
		return hasBlockInfo(block.getLocation());
	}
	
	public static boolean hasBlockInfo(Location l) {
		BlockStorage storage = getStorage(l.getWorld());
		return storage != null && storage.storage.containsKey(l) && getBlockInfo(l, "id") != null;
	}
	
	public static void setBlockInfo(Block block, Config cfg, boolean updateTicker) {
		setBlockInfo(block.getLocation(), cfg, updateTicker);
	}
	
	@SuppressWarnings("unchecked")
	public static void setBlockInfo(Location l, Config cfg, boolean updateTicker) {
		_integrated_removeBlockInfo(l, false);
		
		JSONObject json = new JSONObject();
		for (String key: cfg.getKeys()) {
			json.put(key, cfg.getString(key));
		}
		
		setBlockInfo(l, json.toJSONString(), updateTicker);
	}
	
	public static void setBlockInfo(Block b, String json, boolean updateTicker) {
		setBlockInfo(b.getLocation(), json, updateTicker);
	}
	
	public static void setBlockInfo(Location l, String json, boolean updateTicker) {
		BlockStorage storage = getStorage(l.getWorld());
		storage.storage.put(l, json);
		Map<String, String> parsed = parseJSON(json);
		if (BlockMenuPreset.isInventory(parsed.get("id"))) {
			if (BlockMenuPreset.isUniversalInventory(parsed.get("id"))) {
				if (!universal_inventories.containsKey(parsed.get("id"))) storage.loadUniversalInventory(BlockMenuPreset.getPreset(parsed.get("id")));
			}
			else if (!storage.hasInventory(l)) {
				File file = new File("data-storage/Slimefun/stored-inventories/" + serializeLocation(l) + ".sfi");
				
				if (file.exists()) storage.inventories.put(l, new BlockMenu(BlockMenuPreset.getPreset(parsed.get("id")), l, new Config(file)));
				else storage.loadInventory(l, BlockMenuPreset.getPreset(parsed.get("id")));
			}
		}
		refreshCache(getStorage(l.getWorld()), l, parsed.get("id"), json, updateTicker);
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
		SlimefunStartup.ticker.delete.put(l, destroy);
	}

	public static void _integrated_removeBlockInfo(Location l, boolean destroy) {
		BlockStorage storage = getStorage(l.getWorld());
		if (hasBlockInfo(l)) {
			refreshCache(storage, l, getBlockInfo(l).getString("id"), null, destroy);
			storage.storage.remove(l);
		}
		
		if (destroy) {
			if (storage.hasInventory(l)) storage.clearInventory(l);
			if (storage.hasUniversalInventory(l)) {
				storage.getUniversalInventory(l).close();
				storage.getUniversalInventory(l).save();
			}
			String chunk_string = locationToChunkString(l);
			if (ticking_chunks.containsKey(chunk_string)) {
				Set<Location> locations = ticking_chunks.get(chunk_string);
				locations.remove(l);
				if (locations.isEmpty()) {
					ticking_chunks.remove(chunk_string);
					loaded_tickers.remove(chunk_string);
				}
				else ticking_chunks.put(chunk_string, locations);
			}
		}
	}

	@Deprecated
	public static void moveBlockInfo(Block block, Block newBlock) {
		moveBlockInfo(block.getLocation(), newBlock.getLocation());
	}

	public static void moveBlockInfo(Location from, Location to) {
		SlimefunStartup.ticker.move.put(from, to);
	}

	@Deprecated
	public static void _integrated_moveBlockInfo(Block block, Block newBlock) {
		_integrated_moveLocationInfo(block.getLocation(), newBlock.getLocation());
	}

	public static void _integrated_moveLocationInfo(Location from, Location to) {
		if (!hasBlockInfo(from)) return;
		BlockStorage storage = getStorage(from.getWorld());
		
		setBlockInfo(to, getBlockInfo(from), true);
		if (storage.inventories.containsKey(from)) {
			BlockMenu menu = storage.inventories.get(from);
			storage.inventories.put(to, menu);
			storage.clearInventory(from);
			menu.move(to);
		}
		
		refreshCache(storage, from, getBlockInfo(from).getString("id"), null, true);
		storage.storage.remove(from);

		String chunk_string = locationToChunkString(from);
		if (ticking_chunks.containsKey(chunk_string)) {
			Set<Location> locations = ticking_chunks.get(chunk_string);
			locations.remove(from);
			if (locations.isEmpty()) {
				ticking_chunks.remove(chunk_string);
				loaded_tickers.remove(chunk_string);
			}
			else ticking_chunks.put(chunk_string, locations);
		}
	}

	@Deprecated
	private static void refreshCache(BlockStorage storage, Block b, String key, String value, boolean updateTicker) {
		refreshCache(storage, b.getLocation(), key, value, updateTicker);
	}

	private static void refreshCache(BlockStorage storage, Location l, String key, String value, boolean updateTicker) {
		Config cfg = storage.cache_blocks.containsKey(key) ? storage.cache_blocks.get(key): new Config(path_blocks + l.getWorld().getName() + "/" + key + ".sfb");
		cfg.setValue(serializeLocation(l), value);
		storage.cache_blocks.put(key, cfg);
		
		if (updateTicker) {
			SlimefunItem item = SlimefunItem.getByID(key);
			if (item != null && item.isTicking()) {
				String chunk_string = locationToChunkString(l);
				if (value != null) {
					Set<Location> locations = ticking_chunks.containsKey(chunk_string) ? ticking_chunks.get(chunk_string): new HashSet<Location>();
					locations.add(l);
					ticking_chunks.put(chunk_string, locations);
					if (!loaded_tickers.contains(chunk_string)) loaded_tickers.add(chunk_string);
				}
			}
		}
	}

	public static SlimefunItem check(Block block) {
		return check(block.getLocation());
	}

	public static SlimefunItem check(Location l) {
		if (!hasBlockInfo(l)) return null;
		return SlimefunItem.getByID(getBlockInfo(l, "id"));
	}
	
	public static String checkID(Block block) {
		return checkID(block.getLocation());
	}

	public static boolean check(Block block, String slimefunItem) {
		return check(block.getLocation(), slimefunItem);
	}
	
	public static String checkID(Location l) {
		if (!hasBlockInfo(l)) return null;
		return getBlockInfo(l, "id");
	}

	public static boolean check(Location l, String slimefunItem) {
		if (!hasBlockInfo(l)) return false;
		try {
			String id = getBlockInfo(l, "id");
			return id != null && id.equalsIgnoreCase(slimefunItem);
		}
		catch(NullPointerException x) {
			return false;
		}
	}

	public static boolean isWorldRegistered(String name) {
		return worlds.containsKey(name);
	}
	
	public static Set<String> getTickingChunks() {
		return new HashSet<String>(loaded_tickers);
	}

	@Deprecated
	public static Set<Block> getTickingBlocks(Chunk chunk) {
		return getTickingBlocks(chunk.toString());
	}
	
	public static Set<Location> getTickingLocations(Chunk chunk) {
		return getTickingLocations(chunk.toString());
	}

	@Deprecated
	public static Set<Block> getTickingBlocks(String chunk) {
		Set<Block> ret = new HashSet<Block>();
		for(Location l: getTickingLocations(chunk)) {
			ret.add(l.getBlock());
		}
		return ret;
	}

	public static Set<Location> getTickingLocations(String chunk) {
		return new HashSet<Location>(ticking_chunks.get(chunk));
	}
	
	public BlockMenu loadInventory(Location l, BlockMenuPreset preset) {
		BlockMenu menu = new BlockMenu(preset, l);
		inventories.put(l, menu);
		return menu;
	}
	
	public void loadUniversalInventory(BlockMenuPreset preset) {
		universal_inventories.put(preset.getID(), new UniversalBlockMenu(preset));
	}
	
	public void clearInventory(Location l) {
		BlockMenu menu = getInventory(l);

		for(HumanEntity human: new ArrayList<>(menu.toInventory().getViewers())) {
			human.closeInventory();
		}

		inventories.get(l).delete(l);
		inventories.remove(l);
	}
	
	public boolean hasInventory(Location l) {
		return inventories.containsKey(l);
	}
	
	public boolean hasUniversalInventory(String id) {
		return universal_inventories.containsKey(id);
	}

	public UniversalBlockMenu getUniversalInventory(Block block) {
		return getUniversalInventory(block.getLocation());
	}

	public UniversalBlockMenu getUniversalInventory(Location l) {
		String id = checkID(l);
		return id == null ? null: getUniversalInventory(id);
	}

	public UniversalBlockMenu getUniversalInventory(String id) {
		return universal_inventories.get(id);
	}
	
	public static BlockMenu getInventory(Block b) {
		return getInventory(b.getLocation());
	}
	
	public static BlockMenu getInventory(Location l) {
		BlockStorage storage = getStorage(l.getWorld());
		if (storage == null) return null;
		if (!storage.hasInventory(l)) return storage.loadInventory(l, BlockMenuPreset.getPreset(checkID(l)));
		else return storage.inventories.get(l);
	}
	
	public static JSONParser getParser() {
		return new JSONParser();
	}

	public static Config getChunkInfo(Chunk chunk) {
		try {
			Config cfg = new Config("data-storage/Slimefun/temp.yml");
			if (!map_chunks.containsKey(serializeChunk(chunk))) return cfg;
			
			for (Map.Entry<String, String> entry: parseJSON(getJSONData(chunk)).entrySet()) {
				cfg.setValue(entry.getKey(), entry.getValue());
			}
			
			return cfg;
		} catch(Exception x) {
			System.err.println(x.getClass().getName());
			System.err.println("[Slimefun] Failed to parse ChunkInfo for Chunk @ " + chunk.getX() + ", " + chunk.getZ());
			try {
				System.err.println(getJSONData(chunk));
			} catch (Exception x2) {
				System.err.println("No Metadata found!");
			}
			x.printStackTrace();
			return new Config("data-storage/Slimefun/temp.yml");
		}
	}
	
	public static boolean hasChunkInfo(Chunk chunk) {
		return map_chunks.containsKey(serializeChunk(chunk));
	}
	
	@SuppressWarnings("unchecked")
	public static void setChunkInfo(Chunk chunk, String key, String value) {
		Config cfg = new Config("data-storage/Slimefun/temp.yml");
		if (hasChunkInfo(chunk)) cfg = getChunkInfo(chunk);
		cfg.setValue(key, value);
		
		JSONObject json = new JSONObject();
		for (String path: cfg.getKeys()) {
			json.put(path, cfg.getString(path));
		}
		
		map_chunks.put(serializeChunk(chunk), json.toJSONString());
		
		chunk_changes++;
	}

	public static String getChunkInfo(Chunk chunk, String key) {
		return getChunkInfo(chunk).getString(key);
	}
	
	public static boolean hasChunkInfo(Chunk chunk, String key) {
		return getChunkInfo(chunk, key) != null;
	}
	
	public static void clearChunkInfo(Chunk chunk) {
		map_chunks.remove(serializeChunk(chunk));
	}

	public static String getBlockInfoAsJson(Block block) {
		return getBlockInfoAsJson(block.getLocation());
	}

	public static String getBlockInfoAsJson(Location l) {
		return getJSONData(l);
	}

	public boolean hasUniversalInventory(Block block) {
		return hasUniversalInventory(block.getLocation());
	}

	public boolean hasUniversalInventory(Location l) {
		String id = checkID(l);
		return id == null ? false: hasUniversalInventory(id);
	}
}
