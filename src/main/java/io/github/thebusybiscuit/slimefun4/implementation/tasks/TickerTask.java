package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.chat.json.ChatComponent;
import io.github.thebusybiscuit.cscorelib2.chat.json.HoverEvent;
import io.github.thebusybiscuit.slimefun4.api.ErrorReport;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class TickerTask implements Runnable {

    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private final ConcurrentMap<Location, Location> move = new ConcurrentHashMap<>();
    private final ConcurrentMap<Location, Boolean> delete = new ConcurrentHashMap<>();
    private final ConcurrentMap<Location, Long> blockTimings = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Integer> chunkItemCount = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Integer> machineCount = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Long> machineTimings = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Long> chunkTimings = new ConcurrentHashMap<>();
    private final ConcurrentMap<Location, Integer> buggedBlocks = new ConcurrentHashMap<>();
    private final Set<String> chunksSkipped = new HashSet<>();

    private final Set<BlockTicker> tickers = new HashSet<>();

    private boolean halted = false;

    private int skipped = 0;
    private int chunks = 0;
    private int machines = 0;
    private long time = 0;

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

        Map<Location, Integer> bugged = new HashMap<>(buggedBlocks);
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

                                            machineTimings.put(item.getID(), (machinetime != null ? machinetime : 0) + (System.nanoTime() - timestamp3));
                                            chunkItemCount.put(tickedChunk, (chunk != null ? chunk : 0) + 1);
                                            machineCount.put(item.getID(), (machine != null ? machine : 0) + 1);
                                            blockTimings.put(l, System.nanoTime() - timestamp3);
                                        } catch (Exception x) {
                                            int errors = bugged.getOrDefault(l, 0);
                                            reportErrors(l, item, x, errors);
                                        }
                                    });
                                } else {
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
                        } else skipped++;
                    } else {
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
            new ErrorReport(x, l, item);

            buggedBlocks.put(l, errors);
        } else if (errors == 4) {
            Slimefun.getLogger().log(Level.SEVERE, "X: {0} Y: {1} Z: {2} ({3})", new Object[]{l.getBlockX(), l.getBlockY(), l.getBlockZ(), item.getID()});
            Slimefun.getLogger().log(Level.SEVERE, "has thrown 4 Exceptions in the last 4 Ticks, the Block has been terminated.");
            Slimefun.getLogger().log(Level.SEVERE, "Check your /plugins/Slimefun/error-reports/ folder for details.");
            Slimefun.getLogger().log(Level.SEVERE, " ");

            BlockStorage._integrated_removeBlockInfo(l, true);

            Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> l.getBlock().setType(Material.AIR));
        } else {
            buggedBlocks.put(l, errors);
        }
    }

    public String getTime() {
        return toMillis(time, false);
    }

    public void info(CommandSender sender) {
        sender.sendMessage(ChatColors.color("&2== &aSlimefun Diagnostic Tool &2=="));
        sender.sendMessage(ChatColors.color("&6Halted: &e&l" + String.valueOf(halted).toUpperCase(Locale.ROOT)));
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Impact: &e" + toMillis(time, true)));
        sender.sendMessage(ChatColors.color("&6Ticked Chunks: &e" + chunks));
        sender.sendMessage(ChatColors.color("&6Ticked Machines: &e" + machines));
        sender.sendMessage(ChatColors.color("&6Skipped Machines: &e" + skipped));
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Ticking Machines:"));

        List<Map.Entry<String, Long>> timings = machineCount.keySet().stream().map(key -> new AbstractMap.SimpleEntry<>(key, machineTimings.getOrDefault(key, 0L))).sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).collect(Collectors.toList());

        if (sender instanceof Player) {
            ChatComponent component = new ChatComponent(ChatColors.color("   &7&oHover for more Info"));
            StringBuilder builder = new StringBuilder();
            int hidden = 0;

            for (Map.Entry<String, Long> entry : timings) {
                int count = machineCount.get(entry.getKey());

                if (entry.getValue() > 300_000) {
                    builder.append("\n&c").append(entry.getKey()).append(" - ").append(count).append("x &7(").append(toMillis(entry.getValue(), true)).append(", ").append(toMillis(entry.getValue() / count, true)).append(" avg/machine)");
                } else hidden++;
            }

            builder.append("\n\n&c+ &4").append(hidden).append(" Hidden");
            component.setHoverEvent(new HoverEvent(ChatColors.color(builder.toString())));

            component.sendMessage((Player) sender);
        } else {
            int hidden = 0;

            for (Map.Entry<String, Long> entry : timings) {
                int count = machineCount.get(entry.getKey());
                if (entry.getValue() > 300_000) {
                    sender.sendMessage("  " + entry.getKey() + " - " + count + "x (" + toMillis(entry.getValue(), false) + ", " + toMillis(entry.getValue() / count, false) + " avg/machine)");
                } else hidden++;
            }

            sender.sendMessage("+ " + hidden + " Hidden");
        }

        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Ticking Chunks:"));

        timings = chunkTimings.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());

        if (sender instanceof Player) {
            ChatComponent component = new ChatComponent(ChatColors.color("   &7&oHover for more Info"));
            StringBuilder builder = new StringBuilder();
            int hidden = 0;

            for (Map.Entry<String, Long> entry : timings) {
                if (!chunksSkipped.contains(entry.getKey())) {
                    if (entry.getValue() > 0) {
                        builder.append("\n&c").append(formatChunk(entry.getKey())).append(" - ").append(chunkItemCount.getOrDefault(entry.getKey(), 0)).append("x &7(").append(toMillis(entry.getValue(), true)).append(')');
                    } else hidden++;
                }
            }

            builder.append("\n\n&c+ &4").append(hidden).append(" Hidden");
            component.setHoverEvent(new HoverEvent(ChatColors.color(builder.toString())));

            component.sendMessage((Player) sender);
        } else {
            int hidden = 0;

            for (Map.Entry<String, Long> entry : timings) {
                if (!chunksSkipped.contains(entry.getKey())) {
                    if (entry.getValue() > 0) {
                        sender.sendMessage("  " + formatChunk(entry.getKey()) + " - " + (chunkItemCount.getOrDefault(entry.getKey(), 0)) + "x (" + toMillis(entry.getValue(), false) + ")");
                    } else hidden++;
                }
            }

            sender.sendMessage(ChatColors.color("&c+ &4" + hidden + " Hidden"));
        }
    }

    private String formatChunk(String chunk) {
        String[] components = PatternUtils.SEMICOLON.split(chunk);
        return components[0] + " [" + components[2] + ',' + components[3] + ']';
    }

    public long getTimings(Block b) {
        return blockTimings.getOrDefault(b.getLocation(), 0L);
    }

    public long getTimings(String item) {
        return machineTimings.getOrDefault(item, 0L);
    }

    public long getTimings(Chunk c) {
        String id = c.getWorld().getName() + ';' + c.getX() + ';' + c.getZ();
        return chunkTimings.getOrDefault(id, 0L);
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

    public String toMillis(long nanoseconds, boolean colors) {
        String number = decimalFormat.format(nanoseconds / 1000000.0);

        if (!colors) {
            return number;
        } else {
            String[] parts = PatternUtils.NUMBER_SEPERATOR.split(number);

            if (parts.length == 1) {
                return parts[0];
            } else {
                return parts[0] + ',' + ChatColor.GRAY + parts[1] + "ms";
            }
        }
    }

    @Override
    public String toString() {
        return "TickerTask {\n" + "     HALTED = " + halted + "\n" + "     tickers = " + tickers + "\n" + "     move = " + move + "\n" + "     delete = " + delete + "\n" + "     chunks = " + chunkItemCount + "\n" + "     machines = " + machineCount + "\n" + "     machinetime = " + machineTimings + "\n" + "     chunktime = " + chunkTimings + "\n" + "     skipped = " + chunksSkipped + "\n" + "}";
    }

    public void queueMove(Location from, Location to) {
        move.put(from, to);
    }

    public void queueDelete(Location l, boolean destroy) {
        delete.put(l, destroy);
    }

    public void start(SlimefunPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                run();
            } catch (Exception | LinkageError x) {
                plugin.getLogger().log(Level.SEVERE, x, () -> "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + SlimefunPlugin.getVersion());
                abortTick();
            }
        }, 100L, SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay"));
    }

}