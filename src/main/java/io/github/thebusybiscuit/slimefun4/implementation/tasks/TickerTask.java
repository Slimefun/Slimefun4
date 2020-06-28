package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TickerTask implements Runnable {

    private static final int VISIBILITY_THRESHOLD = 225_000;

    private final Set<BlockTicker> tickers = new HashSet<>();

    // These are "Queues" of blocks that need to be removed or moved
    private final ConcurrentMap<Location, Location> movingQueue = new ConcurrentHashMap<>();
    private final ConcurrentMap<Location, Boolean> deletionQueue = new ConcurrentHashMap<>();

    private final ConcurrentMap<Location, Integer> buggedBlocks = new ConcurrentHashMap<>();

    private final ConcurrentMap<Location, Long> blockTimings = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Integer> machineCount = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Long> machineTimings = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Long> chunkTimings = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Integer> chunkItemCount = new ConcurrentHashMap<>();
    private final Set<String> skippedChunks = new HashSet<>();

    private boolean halted = false;

    private int skippedBlocks = 0;
    private int chunks = 0;
    private int blocks = 0;
    private long time = 0;

    private boolean running = false;

    public void abortTick() {
        running = false;
    }

    @Override
    public void run() {
        if (running) {
            return;
        }

        running = true;
        long timestamp = System.nanoTime();

        skippedBlocks = 0;
        chunks = 0;
        blocks = 0;
        chunkItemCount.clear();
        machineCount.clear();
        time = 0;
        chunkTimings.clear();
        skippedChunks.clear();
        machineTimings.clear();
        blockTimings.clear();

        Map<Location, Integer> bugs = new HashMap<>(buggedBlocks);
        buggedBlocks.clear();

        Map<Location, Boolean> removals = new HashMap<>(deletionQueue);

        for (Map.Entry<Location, Boolean> entry : removals.entrySet()) {
            BlockStorage._integrated_removeBlockInfo(entry.getKey(), entry.getValue());
            deletionQueue.remove(entry.getKey());
        }

        if (!halted) {
            for (String chunk : BlockStorage.getTickingChunks()) {
                long chunkTimestamp = System.nanoTime();
                chunks++;

                for (Location l : BlockStorage.getTickingLocations(chunk)) {
                    if (l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) {
                        tick(l, chunk, bugs);
                    }
                    else {
                        skippedBlocks += BlockStorage.getTickingLocations(chunk).size();
                        skippedChunks.add(chunk);
                        chunks--;
                        break;
                    }
                }

                chunkTimings.put(chunk, System.nanoTime() - chunkTimestamp);
            }
        }

        for (Map.Entry<Location, Location> entry : movingQueue.entrySet()) {
            BlockStorage._integrated_moveLocationInfo(entry.getKey(), entry.getValue());
        }

        movingQueue.clear();

        Iterator<BlockTicker> iterator = tickers.iterator();
        while (iterator.hasNext()) {
            iterator.next().startNewTick();
            iterator.remove();
        }

        time = System.nanoTime() - timestamp;
        running = false;
    }

    private void tick(Location l, String tickedChunk, Map<Location, Integer> bugs) {
        Block b = l.getBlock();
        SlimefunItem item = BlockStorage.check(l);

        if (item != null && item.getBlockTicker() != null) {
            blocks++;

            try {
                item.getBlockTicker().update();

                if (item.getBlockTicker().isSynchronized()) {
                    Slimefun.runSync(() -> {
                        try {
                            long timestamp = System.nanoTime();
                            item.getBlockTicker().tick(b, item, BlockStorage.getLocationInfo(l));

                            long machinetime = NumberUtils.getLong(machineTimings.get(item.getID()), 0);
                            int chunk = NumberUtils.getInt(chunkItemCount.get(tickedChunk), 0);
                            int machine = NumberUtils.getInt(machineCount.get(item.getID()), 0);

                            machineTimings.put(item.getID(), machinetime + (System.nanoTime() - timestamp));
                            chunkItemCount.put(tickedChunk, chunk + 1);
                            machineCount.put(item.getID(), machine + 1);
                            blockTimings.put(l, System.nanoTime() - timestamp);
                        }
                        catch (Exception | LinkageError x) {
                            int errors = bugs.getOrDefault(l, 0);
                            reportErrors(l, item, x, errors);
                        }
                    });
                }
                else {
                    long timestamp = System.nanoTime();
                    item.getBlockTicker().tick(b, item, BlockStorage.getLocationInfo(l));

                    machineTimings.merge(item.getID(), (System.nanoTime() - timestamp), Long::sum);
                    chunkItemCount.merge(tickedChunk, 1, Integer::sum);
                    machineCount.merge(item.getID(), 1, Integer::sum);
                    blockTimings.put(l, System.nanoTime() - timestamp);
                }

                tickers.add(item.getBlockTicker());
            }
            catch (Exception x) {
                int errors = bugs.getOrDefault(l, 0);
                reportErrors(l, item, x, errors);
            }
        }
        else {
            skippedBlocks++;
        }
    }

    private void reportErrors(Location l, SlimefunItem item, Throwable x, int errors) {
        errors++;

        if (errors == 1) {
            // Generate a new Error-Report
            new ErrorReport(x, l, item);
            buggedBlocks.put(l, errors);
        }
        else if (errors == 4) {
            Slimefun.getLogger().log(Level.SEVERE, "X: {0} Y: {1} Z: {2} ({3})", new Object[] { l.getBlockX(), l.getBlockY(), l.getBlockZ(), item.getID() });
            Slimefun.getLogger().log(Level.SEVERE, "has thrown 4 error messages in the last 4 Ticks, the Block has been terminated.");
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
        return NumberUtils.getAsMillis(time);
    }

    public void info(CommandSender sender) {
        sender.sendMessage(ChatColors.color("&2== &aSlimefun Diagnostic Tool &2=="));
        sender.sendMessage(ChatColors.color("&6Halted: &e&l" + String.valueOf(halted).toUpperCase(Locale.ROOT)));
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Impact: &e" + NumberUtils.getAsMillis(time)));
        sender.sendMessage(ChatColors.color("&6Ticked Chunks: &e" + chunks));
        sender.sendMessage(ChatColors.color("&6Ticked Machines: &e" + blocks));
        sender.sendMessage(ChatColors.color("&6Skipped Machines: &e" + skippedBlocks));
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Ticking Machines:"));

        summarizeTimings(sender, entry -> {
            int count = machineCount.get(entry.getKey());
            String timings = NumberUtils.getAsMillis(entry.getValue());
            String average = NumberUtils.getAsMillis(entry.getValue() / count);

            return entry.getKey() + " - " + count + "x (" + timings + ", " + average + " avg/machine)";
        }, machineCount.keySet().stream().map(key -> new AbstractMap.SimpleEntry<>(key, machineTimings.getOrDefault(key, 0L))));

        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Ticking Chunks:"));

        summarizeTimings(sender, entry -> {
            int count = chunkItemCount.getOrDefault(entry.getKey(), 0);
            String timings = NumberUtils.getAsMillis(entry.getValue());

            return formatChunk(entry.getKey()) + " - " + count + "x (" + timings + ")";
        }, chunkTimings.entrySet().stream().filter(entry -> !skippedChunks.contains(entry.getKey())));
    }

    private void summarizeTimings(CommandSender sender, Function<Map.Entry<String, Long>, String> formatter, Stream<Map.Entry<String, Long>> stream) {
        List<Entry<String, Long>> timings = stream.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());

        if (sender instanceof Player) {
            TextComponent component = new TextComponent("   Hover for more Info");
            component.setColor(net.md_5.bungee.api.ChatColor.GRAY);
            component.setItalic(true);
            StringBuilder builder = new StringBuilder();
            int hidden = 0;

            for (Map.Entry<String, Long> entry : timings) {
                if (entry.getValue() > VISIBILITY_THRESHOLD) {
                    builder.append("\n&c").append(formatter.apply(entry));
                }
                else {
                    hidden++;
                }
            }

            builder.append("\n\n&c+ &4").append(hidden).append(" Hidden");
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColors.color(builder.toString()))));
            sender.spigot().sendMessage(component);
        }
        else {
            int hidden = 0;

            for (Map.Entry<String, Long> entry : timings) {
                if (entry.getValue() > VISIBILITY_THRESHOLD) {
                    sender.sendMessage("  " + ChatColor.stripColor(formatter.apply(entry)));
                }
                else {
                    hidden++;
                }
            }

            sender.sendMessage("+ " + hidden + " Hidden");
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

    @Override
    public String toString() {
        return "TickerTask {\n" + "     HALTED = " + halted + "\n" + "     tickers = " + tickers + "\n" + "     move = " + movingQueue + "\n" + "     delete = " + deletionQueue + "\n" + "     chunks = " + chunkItemCount + "\n" + "     machines = " + machineCount + "\n" + "     machinetime = " + machineTimings + "\n" + "     chunktime = " + chunkTimings + "\n" + "     skipped = " + skippedChunks + "\n" + "}";
    }

    public void queueMove(Location from, Location to) {
        movingQueue.put(from, to);
    }

    public void queueDelete(Location l, boolean destroy) {
        deletionQueue.put(l, destroy);
    }

    public void start(SlimefunPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                run();
            }
            catch (Exception | LinkageError x) {
                plugin.getLogger().log(Level.SEVERE, x, () -> "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + SlimefunPlugin.getVersion());
                abortTick();
            }
        }, 100L, SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay"));
    }

}
