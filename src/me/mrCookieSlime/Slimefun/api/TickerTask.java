package me.mrCookieSlime.Slimefun.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.HoverAction;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;

public class TickerTask implements Runnable {
	
	public Map<Block, Block> move = new HashMap<Block, Block>();
	public Map<Location, Boolean> delete = new HashMap<Location, Boolean>();
	
	private Set<BlockTicker> tickers = new HashSet<BlockTicker>();
	
	private int skipped = 0, chunks = 0, machines = 0;
	private long time = 0;
	private Map<String, Integer> map_chunk = new HashMap<String, Integer>();
	private Map<String, Integer> map_machine = new HashMap<String, Integer>();
	private Map<String, Long> map_machinetime = new HashMap<String, Long>();
	private Map<String, Long> map_chunktime = new HashMap<String, Long>();
	private Set<String> skipped_chunks = new HashSet<String>();

	public static Map<Location, Long> block_timings = new HashMap<Location, Long>();
	
	public static Map<Location, Integer> bugged_blocks = new HashMap<Location, Integer>();
	
	@Override
	public void run() {
		long timestamp = System.currentTimeMillis();
		
		skipped = 0;
		chunks = 0;
		machines = 0;
		map_chunk.clear();
		map_machine.clear();
		time = 0;
		map_chunktime.clear();
		skipped_chunks.clear();
		map_machinetime.clear();
		block_timings.clear();

		Map<Location, Integer> bugged = new HashMap<Location, Integer>(bugged_blocks);
		bugged_blocks.clear();
		
		Map<Location, Boolean> remove = new HashMap<Location, Boolean>(delete);

		for (Map.Entry<Location, Boolean> entry: remove.entrySet()) {
			BlockStorage._integrated_removeBlockInfo(entry.getKey(), entry.getValue());
			delete.remove(entry.getKey());
		}
		
		for (final String c: BlockStorage.getTickingChunks()) {
			long timestamp2 = System.currentTimeMillis();
			chunks++;
			
			blocks:
			for (final Block b: BlockStorage.getTickingBlocks(c)) {
				if (b.getChunk().isLoaded()) {
					final Location l = b.getLocation();
					final SlimefunItem item = BlockStorage.check(l);
					if (item != null) {
						machines++;
						try {
							item.getTicker().update();
							if (item.getTicker().isSynchronized()) {
								Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
									
									@Override
									public void run() {
										long timestamp3 = System.currentTimeMillis();
										item.getTicker().tick(b, item, BlockStorage.getBlockInfo(l));
										
										map_machinetime.put(item.getName(), (map_machinetime.containsKey(item.getName()) ? map_machinetime.get(item.getName()): 0) + (System.currentTimeMillis() - timestamp3));
										map_chunk.put(c, (map_chunk.containsKey(c) ? map_chunk.get(c): 0) + 1);
										map_machine.put(item.getName(), (map_machine.containsKey(item.getName()) ? map_machine.get(item.getName()): 0) + 1);
										block_timings.put(l, System.currentTimeMillis() - timestamp3);
									}
								});
							}
							else {
								long timestamp3 = System.currentTimeMillis();
								item.getTicker().tick(b, item, BlockStorage.getBlockInfo(l));
								
								map_machinetime.put(item.getName(), (map_machinetime.containsKey(item.getName()) ? map_machinetime.get(item.getName()): 0) + (System.currentTimeMillis() - timestamp3));
								map_chunk.put(c, (map_chunk.containsKey(c) ? map_chunk.get(c): 0) + 1);
								map_machine.put(item.getName(), (map_machine.containsKey(item.getName()) ? map_machine.get(item.getName()): 0) + 1);
								block_timings.put(l, System.currentTimeMillis() - timestamp3);
							}
							tickers.add(item.getTicker());
						} catch(Exception x) {
							
							int errors = 0;
							if (bugged.containsKey(l)) errors = bugged.get(l);
							errors++;
							
							if (errors == 1) {
								System.err.println("[Slimefun] Exception caught while ticking a Block:");
								System.err.println("[Slimefun] X: " + l.getBlockX() + " Y: " + l.getBlockY() + " Z: " + l.getBlockZ());
								System.err.println("[Slimefun] Type: " + item.getName());
								System.err.println("[Slimefun] " + BlockStorage.getBlockInfoAsJson(l));
								System.err.println("[Slimefun] ");
								System.err.println("[Slimefun] IGNORE THIS ERROR - Fixing it automatically...");
								System.err.println("[Slimefun] ");
								x.printStackTrace();
								
								bugged_blocks.put(l, errors);
							}
							else if (errors == 2) {
								System.err.println("[Slimefun] X: " + l.getBlockX() + " Y: " + l.getBlockY() + " Z: " + l.getBlockZ() + "(" + item.getName() + ")");
								System.err.println("[Slimefun] 2x " + x.getClass().getName());
								System.err.println("[Slimefun] ");
								
								bugged_blocks.put(l, errors);
							}
							else if (errors == 3) {
								System.err.println("[Slimefun] X: " + l.getBlockX() + " Y: " + l.getBlockY() + " Z: " + l.getBlockZ() + "(" + item.getName() + ")");
								System.err.println("[Slimefun] 3x " + x.getClass().getName());
								System.err.println("[Slimefun] ");
								
								bugged_blocks.put(l, errors);
							}
							else if (errors == 4) {
								System.err.println("[Slimefun] X: " + l.getBlockX() + " Y: " + l.getBlockY() + " Z: " + l.getBlockZ() + "(" + item.getName() + ")");
								System.err.println("[Slimefun] has thrown 4 Exceptions in the last 4 Ticks, the Block has been terminated.");
								System.err.println("[Slimefun] ");
								
								BlockStorage._integrated_removeBlockInfo(l, true);
								
								Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {

									@Override
									public void run() {
										l.getBlock().setType(Material.AIR);
									}
									
								});
							}
						}
					}
					else skipped++;
				}
				else {
					skipped += BlockStorage.getTickingBlocks(c).size();
					skipped_chunks.add(c);
					chunks--;
					break blocks;
				}
			}
			
			map_chunktime.put(c, System.currentTimeMillis() - timestamp2);
		}
		
		for (Map.Entry<Block, Block> entry: move.entrySet()) {
			BlockStorage._integrated_moveBlockInfo(entry.getKey(), entry.getValue());
		}
		move.clear();
		
		for (BlockTicker ticker: tickers) {
			ticker.unique = true;
		}
		tickers.clear();
		
		time = System.currentTimeMillis() - timestamp;
	}

	public void info(CommandSender sender) {
		sender.sendMessage("§2== §aSlimefun Diagnostic Tool §2==");
		sender.sendMessage("§6Impact: §e" + time + "ms / 50-750ms");
		sender.sendMessage("§6Ticked Chunks: §e" + chunks);
		sender.sendMessage("§6Ticked Machines: §e" + machines);
		sender.sendMessage("§6Skipped Machines: §e" + skipped);
		sender.sendMessage("");
		sender.sendMessage("§6Ticking Machines:");
		if (sender instanceof Player) {
			TellRawMessage tellraw = new TellRawMessage();
			tellraw.addText("   §7§oHover for more Info");
			StringBuilder hover = new StringBuilder();
			int hidden = 0;
			for (String item: map_machine.keySet()) {
				if (map_machinetime.get(item) > 0) hover.append("\n§c" + item + " - " + map_machine.get(item) + "x §7(" + map_machinetime.get(item) + "ms)");
				else hidden++;
			}
			hover.append("\n\n§c+ §4" + hidden + " Hidden");
			tellraw.addHoverEvent(HoverAction.SHOW_TEXT, hover.toString());
			try {
				tellraw.send((Player) sender);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			int hidden = 0;
			for (String item: map_machine.keySet()) {
				if (map_machinetime.get(item) > 0) sender.sendMessage("  §e" + item + " - " + map_machine.get(item) + "x §7(" + map_machinetime.get(item) + "ms)");
				else hidden++;
			}
			sender.sendMessage("§c+ §4" + hidden + " Hidden");
		}
		sender.sendMessage("");
		sender.sendMessage("§6Ticking Chunks:");
		if (sender instanceof Player) {
			TellRawMessage tellraw = new TellRawMessage();
			tellraw.addText("   §7§oHover for more Info");
			StringBuilder hover = new StringBuilder();
			int hidden = 0;
			for (String c: map_chunktime.keySet()) {
				if (!skipped_chunks.contains(c)) {
					if (map_chunktime.get(c) > 0) hover.append("\n§c" + c.replace("CraftChunk", "") + " - " + (map_chunk.containsKey(c) ? map_chunk.get(c): 0) + "x §7(" + map_chunktime.get(c) + "ms)");
					else hidden++;
				}
			}
			hover.append("\n\n§c+ §4" + hidden + " Hidden");
			tellraw.addHoverEvent(HoverAction.SHOW_TEXT, hover.toString());
			try {
				tellraw.send((Player) sender);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {

			int hidden = 0;
			for (String c: map_chunktime.keySet()) {
				if (!skipped_chunks.contains(c)) {
					if (map_chunktime.get(c) > 0) sender.sendMessage("  §c" + c.replace("CraftChunk", "") + " - " + (map_chunk.containsKey(c) ? map_chunk.get(c): 0) + "x §7(" + map_chunktime.get(c) + "ms)");
					else hidden++;
				}
			}
			sender.sendMessage("§c+ §4" + hidden + " Hidden");
		}
	}
	
	public long getTimings(Block b) {
		return block_timings.containsKey(b.getLocation()) ? block_timings.get(b.getLocation()): 0L;
	}
	
	public long getTimings(String item) {
		return map_machinetime.containsKey(item) ? map_machinetime.get(item): 0L;
	}
	
	public long getTimings(Chunk c) {
		return map_chunktime.containsKey(c.toString()) ? map_chunktime.get(c.toString()): 0L;
	}

}
