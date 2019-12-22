package me.mrCookieSlime.Slimefun.api;

import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.ErrorReport;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.HoverAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;

public class TickerTask implements Runnable {
	
	private static final DecimalFormat decimalFormat = new DecimalFormat("#.###");
	
	private boolean halted = false;

	protected final Map<Location, Location> move = new HashMap<>();
	protected final Map<Location, Boolean> delete = new HashMap<>();
	private final Map<Location, Long> blockTimings = new HashMap<>();
	
	private final Set<BlockTicker> tickers = new HashSet<>();
	
	private int skipped = 0;
	private int chunks = 0;
	private int machines = 0;
	private long time = 0;
	
	private final Map<String, Integer> chunkItemCount = new HashMap<>();
	private final Map<String, Integer> machineCount = new HashMap<>();
	private final Map<String, Long> machineTimings = new HashMap<>();
	private final Map<String, Long> chunkTimings = new HashMap<>();
	private final Set<String> chunksSkipped = new HashSet<>();
	private final Map<Location, Integer> buggedBlocks = new HashMap<>();
	
	private boolean running = false;
	
	public void abortTick() {
		running = false;
	}
	
	@Override
	public void run() {
		if (running) return;
		
		running = true;
		long timestamp = System.nanoTime();
		
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

		for (Map.Entry<Location, Boolean> entry : remove.entrySet()) {
			BlockStorage._integrated_removeBlockInfo(entry.getKey(), entry.getValue());
			delete.remove(entry.getKey());
		}
		
		if (!halted) {
			for (String tickedChunk : BlockStorage.getTickingChunks()) {
				long timestamp2 = System.nanoTime();
				chunks++;
				
				for (Location l : BlockStorage.getTickingLocations(tickedChunk)) {
					if (l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) {
						Block b = l.getBlock();
						SlimefunItem item = BlockStorage.check(l);
						
						if (item != null && item.getBlockTicker() != null) {
							machines++;
							
							try {
								item.getBlockTicker().update();
								
								if (item.getBlockTicker().isSynchronized()) {
									Slimefun.runSync(() -> {
										try {
											long timestamp3 = System.nanoTime();
											item.getBlockTicker().tick(b, item, BlockStorage.getLocationInfo(l));
											
											Long machinetime = machineTimings.get(item.getID());
											Integer chunk = chunkItemCount.get(tickedChunk);
											Integer machine = machineCount.get(item.getID());
											
											machineTimings.put(item.getID(), (machinetime != null ? machinetime: 0) + (System.nanoTime() - timestamp3));
											chunkItemCount.put(tickedChunk, (chunk != null ? chunk: 0) + 1);
											machineCount.put(item.getID(), (machine != null ? machine: 0) + 1);
											blockTimings.put(l, System.nanoTime() - timestamp3);
										} catch (Exception x) {
											int errors = bugged.getOrDefault(l, 0);
											reportErrors(l, item, x, errors);
										}
									});
								}
								else {
									long timestamp3 = System.nanoTime();
									item.getBlockTicker().tick(b, item, BlockStorage.getLocationInfo(l));

									machineTimings.merge(item.getID(), (System.nanoTime() - timestamp3), Long::sum);
									chunkItemCount.merge(tickedChunk, 1, Integer::sum);
									machineCount.merge(item.getID(), 1, Integer::sum);
									blockTimings.put(l, System.nanoTime() - timestamp3);
								}
								
								tickers.add(item.getBlockTicker());
							} catch (Exception x) {
								int errors = bugged.getOrDefault(l, 0);
								reportErrors(l, item, x, errors);
							}
						}
						else skipped++;
					}
					else {
						skipped += BlockStorage.getTickingLocations(tickedChunk).size();
						chunksSkipped.add(tickedChunk);
						chunks--;
						break;
					}
				}
				
				chunkTimings.put(tickedChunk, System.nanoTime() - timestamp2);
			}
		}

		for (Map.Entry<Location, Location> entry : move.entrySet()) {
			BlockStorage._integrated_moveLocationInfo(entry.getKey(), entry.getValue());
		}
		move.clear();
		
		Iterator<BlockTicker> iterator = tickers.iterator();
		while (iterator.hasNext()) {
			iterator.next().startNewTick();
			iterator.remove();
		}
		
		time = System.nanoTime() - timestamp;
		running = false;
	}

	private void reportErrors(Location l, SlimefunItem item, Exception x, int errors) {
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

	public String getTime() {
		return toMillis(time);
	}

	public void info(CommandSender sender) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2== &aSlimefun Diagnostic Tool &2=="));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Halted: &e&l" + String.valueOf(halted).toUpperCase()));
		sender.sendMessage("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Impact: &e" + toMillis(time)));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Ticked Chunks: &e" + chunks));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Ticked Machines: &e" + machines));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Skipped Machines: &e" + skipped));
		sender.sendMessage("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Ticking Machines:"));

		List<Map.Entry<String, Long>> timings = machineCount.keySet().stream()
				.map(key -> new AbstractMap.SimpleEntry<>(key, machineTimings.getOrDefault(key, 0L)))
				.sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
				.collect(Collectors.toList());

		if (sender instanceof Player) {
			TellRawMessage tellraw = new TellRawMessage();
			tellraw.addText("   &7&oHover for more Info");
			StringBuilder hover = new StringBuilder();
			int hidden = 0;

			for (Map.Entry<String, Long> entry : timings) {
				int count = machineCount.get(entry.getKey());
				if (entry.getValue() > 500_000)
				    hover.append("\n&c").append(entry.getKey()).append(" - ")
						.append(count).append("x &7(").append(toMillis(entry.getValue())).append(", ")
			            .append(toMillis(entry.getValue() / count)).append(" avg/machine)");
				else
				    hidden++;
			}

			hover.append("\n\n&c+ &4").append(hidden).append(" Hidden");
			tellraw.addHoverEvent(HoverAction.SHOW_TEXT, hover.toString());

			try {
				tellraw.send((Player) sender);
			} catch (Exception x) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occurred while sending a Timings Summary for Slimefun " + Slimefun.getVersion(), x);
			}
		}
		else {
			int hidden = 0;

			for (Map.Entry<String, Long> entry : timings) {
				int count = machineCount.get(entry.getKey());
				if (entry.getValue() > 500_000) {
					sender.sendMessage("  " + entry.getKey() + " - " + count + "x (" + toMillis(entry.getValue()) + ", " + toMillis(entry.getValue() / count) + " avg/machine)");
				}	
				else hidden++;
			}

			sender.sendMessage("+ " + hidden + " Hidden");
		}
		
		sender.sendMessage("");
		sender.sendMessage(ChatColors.color("&6Ticking Chunks:"));

		timings = chunkTimings.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toList());

		if (sender instanceof Player) {
			TellRawMessage tellraw = new TellRawMessage();
			tellraw.addText("   &7&oHover for more Info");
			StringBuilder hover = new StringBuilder();
			int hidden = 0;

			for (Map.Entry<String, Long> entry : timings) {
				if (!chunksSkipped.contains(entry.getKey())) {
					if (entry.getValue() > 0)
						hover.append("\n&c").append(formatChunk(entry.getKey())).append(" - ")
								.append(chunkItemCount.getOrDefault(entry.getKey(), 0))
								.append("x &7(").append(toMillis(entry.getValue())).append(")");
					else
					    hidden++;
				}
			}
			
			hover.append("\n\n&c+ &4").append(hidden).append(" Hidden");
			tellraw.addHoverEvent(HoverAction.SHOW_TEXT, hover.toString());
			
			try {
				tellraw.send((Player) sender);
			} catch (Exception x) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occurred while sending a Timings Summary for Slimefun " + Slimefun.getVersion(), x);
			}
		}
		else {
			int hidden = 0;
			for (Map.Entry<String, Long> entry : timings) {
				if (!chunksSkipped.contains(entry.getKey())) {
					if (entry.getValue() > 0) sender.sendMessage("  " + formatChunk(entry.getKey()) + " - "
							+ (chunkItemCount.getOrDefault(entry.getKey(), 0)) + "x (" + toMillis(entry.getValue()) + ")");
					else hidden++;
				}
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c+ &4" + hidden + " Hidden"));
		}
	}
	
	private String formatChunk(String chunk) {
		String[] components = chunk.split(";");
		return components[0] + " [" + components[2] + "," + components[3] + "]";
	}
	
	public long getTimings(Block b) {
		return blockTimings.getOrDefault(b.getLocation(), 0L);
	}
	
	public long getTimings(String item) {
		return machineTimings.getOrDefault(item, 0L);
	}
	
	public long getTimings(Chunk c) {
		return chunkTimings.getOrDefault(c.toString(), 0L);
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

	private String toMillis(long time) {
		return decimalFormat.format(time / 1000000F) + "ms";
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
