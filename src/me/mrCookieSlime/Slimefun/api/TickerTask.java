package me.mrCookieSlime.Slimefun.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.HoverAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;

public class TickerTask implements Runnable {
	
	private boolean halted = false;
	
	protected Map<Location, Location> move = new HashMap<>();
	protected Map<Location, Boolean> delete = new HashMap<>();
	protected Map<Location, Long> blockTimings = new HashMap<>();
	
	private Set<BlockTicker> tickers = new HashSet<>();
	
	private int skipped = 0;
	private int chunks = 0;
	private int machines = 0;
	private long time = 0;
	
	private Map<String, Integer> chunkItemCount = new HashMap<>();
	private Map<String, Integer> machineCount = new HashMap<>();
	private Map<String, Long> machineTimings = new HashMap<>();
	private Map<String, Long> chunkTimings = new HashMap<>();
	private Set<String> chunksSkipped = new HashSet<>();
	private Map<Location, Integer> buggedBlocks = new HashMap<>();
	
	@Override
	public void run() {
		long timestamp = System.currentTimeMillis();
		
		skipped = 0;
		chunks = 0;
		machines = 0;
		chunkItemCount.clear();
		machineCount.clear();
		time = 0;
		chunkTimings.clear();
		chunksSkipped.clear();
		machineTimings.clear();
		blockTimings.clear();

		final Map<Location, Integer> bugged = new HashMap<>(buggedBlocks);
		buggedBlocks.clear();
		
		Map<Location, Boolean> remove = new HashMap<>(delete);

		for (Map.Entry<Location, Boolean> entry: remove.entrySet()) {
			BlockStorage._integrated_removeBlockInfo(entry.getKey(), entry.getValue());
			delete.remove(entry.getKey());
		}
		
		if (!halted) {
			for (final String c: BlockStorage.getTickingChunks()) {
				long timestamp2 = System.currentTimeMillis();
				chunks++;
				
				for (final Location l: BlockStorage.getTickingLocations(c)) {
					if (l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) {
						final Block b = l.getBlock();
						final SlimefunItem item = BlockStorage.check(l);
						
						if (item != null && item.getBlockTicker() != null) {
							machines++;
							
							try {
								item.getBlockTicker().update();
								
								if (item.getBlockTicker().isSynchronized()) {
									Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
										try {
											long timestamp3 = System.currentTimeMillis();
											item.getBlockTicker().tick(b, item, BlockStorage.getLocationInfo(l));
											
											Long machinetime = machineTimings.get(item.getID());
											Integer chunk = chunkItemCount.get(c);
											Integer machine = machineCount.get(item.getID());
											
											machineTimings.put(item.getID(), (machinetime != null ? machinetime: 0) + (System.currentTimeMillis() - timestamp3));
											chunkItemCount.put(c, (chunk != null ? chunk: 0) + 1);
											machineCount.put(item.getID(), (machine != null ? machine: 0) + 1);
											blockTimings.put(l, System.currentTimeMillis() - timestamp3);
										} catch (Exception x) {
											int errors = 0;
											if (bugged.containsKey(l)) errors = bugged.get(l);
											errors++;
											
											if (errors == 1) {
												// Generate a new Error-Report
												new ErrorReport(x, this, l, item);
												
												buggedBlocks.put(l, errors);
											}
											else if (errors == 4) {
												Slimefun.getLogger().log(Level.SEVERE, "X: " + l.getBlockX() + " Y: " + l.getBlockY() + " Z: " + l.getBlockZ() + '(' + item.getID() + ")");
												Slimefun.getLogger().log(Level.SEVERE, "has thrown 4 Exceptions in the last 4 Ticks, the Block has been terminated.");
												Slimefun.getLogger().log(Level.SEVERE, "Check your /plugins/Slimefun/error-reports/ folder for details.");
												Slimefun.getLogger().log(Level.SEVERE, " ");
												
												BlockStorage._integrated_removeBlockInfo(l, true);
												
												Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> l.getBlock().setType(Material.AIR));
											}
											else {
												buggedBlocks.put(l, errors);
											}
										}
									});
								}
								else {
									long timestamp3 = System.currentTimeMillis();
									item.getBlockTicker().tick(b, item, BlockStorage.getLocationInfo(l));
									
									machineTimings.put(item.getID(), (machineTimings.containsKey(item.getID()) ? machineTimings.get(item.getID()): 0) + (System.currentTimeMillis() - timestamp3));
									chunkItemCount.put(c, (chunkItemCount.containsKey(c) ? chunkItemCount.get(c): 0) + 1);
									machineCount.put(item.getID(), (machineCount.containsKey(item.getID()) ? machineCount.get(item.getID()): 0) + 1);
									blockTimings.put(l, System.currentTimeMillis() - timestamp3);
								}
								tickers.add(item.getBlockTicker());
							} catch (Exception x) {
								int errors = 0;
								if (bugged.containsKey(l)) errors = bugged.get(l);
								errors++;
								
								if (errors == 1) {
									// Generate a new Error-Report
									new ErrorReport(x, this, l, item);
									buggedBlocks.put(l, errors);
								}
								else if (errors == 4) {
									Slimefun.getLogger().log(Level.SEVERE, "X: " + l.getBlockX() + " Y: " + l.getBlockY() + " Z: " + l.getBlockZ() + '(' + item.getID() + ")");
									Slimefun.getLogger().log(Level.SEVERE, "has thrown 4 Exceptions in the last 4 Ticks, the Block has been terminated.");
									Slimefun.getLogger().log(Level.SEVERE, "Check your /plugins/Slimefun/error-reports/ folder for details.");
									Slimefun.getLogger().log(Level.SEVERE, " ");
									
									BlockStorage._integrated_removeBlockInfo(l, true);
									
									Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> l.getBlock().setType(Material.AIR));
								}
								else {
									buggedBlocks.put(l, errors);
								}
							}
						}
						else skipped++;
					}
					else {
						skipped += BlockStorage.getTickingLocations(c).size();
						chunksSkipped.add(c);
						chunks--;
						break;
					}
				}
				
				chunkTimings.put(c, System.currentTimeMillis() - timestamp2);
			}
		}

		for (Map.Entry<Location, Location> entry: move.entrySet()) {
			BlockStorage._integrated_moveLocationInfo(entry.getKey(), entry.getValue());
		}
		move.clear();
		
		Iterator<BlockTicker> iterator = tickers.iterator();
		while (iterator.hasNext()) {
			iterator.next().startNewTick();
			iterator.remove();
		}
		
		time = System.currentTimeMillis() - timestamp;
	}
	
	public long getTime() {
		return time;
	}

	public void info(CommandSender sender) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2== &aSlimefun Diagnostic Tool &2=="));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Halted: &e&l" + String.valueOf(halted).toUpperCase()));
		sender.sendMessage("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Impact: &e" + time + "ms / 50-750ms"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Ticked Chunks: &e" + chunks));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Ticked Machines: &e" + machines));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Skipped Machines: &e" + skipped));
		sender.sendMessage("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Ticking Machines:"));
		
		if (sender instanceof Player) {
			TellRawMessage tellraw = new TellRawMessage();
			tellraw.addText("   &7&oHover for more Info");
			StringBuilder hover = new StringBuilder();
			int hidden = 0;
			
			for (Map.Entry<String, Integer> entry: machineCount.entrySet()) {
				long timings = machineTimings.get(entry.getKey());
				if (timings > 0) hover.append("\n&c" + entry.getKey() + " - " + entry.getValue()+ "x &7(" + timings + "ms)");
				else hidden++;
			}
			
			hover.append("\n\n&c+ &4" + hidden + " Hidden");
			tellraw.addHoverEvent(HoverAction.SHOW_TEXT, hover.toString());
			
			try {
				tellraw.send((Player) sender);
			} catch (Exception x) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occured while sending a Timings Summary for Slimefun " + Slimefun.getVersion(), x);
			}
		}
		else {
			int hidden = 0;
			
			for (Map.Entry<String, Integer> entry: machineCount.entrySet()) {
				long timings = machineTimings.get(entry.getKey());
				if (timings > 0) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e" + entry.getKey() + " - " + entry.getValue()+ "x &7(" + timings + "ms)"));
				else hidden++;
			}
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c+ &4" + hidden + " Hidden"));
		}
		
		sender.sendMessage("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Ticking Chunks:"));
		
		if (sender instanceof Player) {
			TellRawMessage tellraw = new TellRawMessage();
			tellraw.addText("   &7&oHover for more Info");
			StringBuilder hover = new StringBuilder();
			int hidden = 0;
			
			for (Map.Entry<String, Long> entry: chunkTimings.entrySet()) {
				if (!chunksSkipped.contains(entry.getKey())) {
					if (entry.getValue() > 0) hover.append("\n&c" + entry.getKey().replace("CraftChunk", "") + " - " + (chunkItemCount.containsKey(entry.getKey()) ? chunkItemCount.get(entry.getKey()): 0) + "x &7(" + entry.getValue() + "ms)");
					else hidden++;
				}
			}
			
			hover.append("\n\n&c+ &4" + hidden + " Hidden");
			tellraw.addHoverEvent(HoverAction.SHOW_TEXT, hover.toString());
			
			try {
				tellraw.send((Player) sender);
			} catch (Exception x) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occured while sending a Timings Summary for Slimefun " + Slimefun.getVersion(), x);
			}
		}
		else {
			int hidden = 0;
			for (Map.Entry<String, Long> entry: chunkTimings.entrySet()) {
				if (!chunksSkipped.contains(entry.getKey())) {
					if (entry.getValue() > 0) sender.sendMessage("  &c" + entry.getKey().replace("CraftChunk", "") + " - " + (chunkItemCount.containsKey(entry.getKey()) ? chunkItemCount.get(entry.getKey()): 0) + "x &7(" + entry.getValue() + "ms)");
					else hidden++;
				}
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c+ &4" + hidden + " Hidden"));
		}
	}
	
	public long getTimings(Block b) {
		return blockTimings.containsKey(b.getLocation()) ? blockTimings.get(b.getLocation()): 0L;
	}
	
	public long getTimings(String item) {
		return machineTimings.containsKey(item) ? machineTimings.get(item): 0L;
	}
	
	public long getTimings(Chunk c) {
		return chunkTimings.containsKey(c.toString()) ? chunkTimings.get(c.toString()): 0L;
	}

	public void addBlockTimings(Location l, long time) {
		blockTimings.put(l, time);
	}
	
	public boolean isHalted() {
		return halted;
	}
	
	public void halt() {
		halted = true;
	}
	
	@Override
	public String toString() {
		return "TickerTask {\n" 
				+ "     HALTED = " + halted + "\n"
				+ "     tickers = " + tickers + "\n"
				+ "     move = " + move + "\n"
				+ "     delete = " + delete + "\n"
				+ "     chunks = " + chunkItemCount + "\n"
				+ "     machines = " + machineCount + "\n"
				+ "     machinetime = " + machineTimings + "\n"
				+ "     chunktime = " + chunkTimings + "\n"
				+ "     skipped = " + chunksSkipped + "\n"
				+ "}";
	}

}
