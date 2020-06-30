package io.github.thebusybiscuit.slimefun4.core.services.profiler;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.TickerTask;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * The {@link SlimefunProfiler} works closely to the {@link TickerTask} and is responsible for
 * monitoring that task.
 * It collects timings data for any ticked {@link Block} and the corresponding {@link SlimefunItem}.
 * This allows developers to identify laggy {@link SlimefunItem SlimefunItems} or {@link SlimefunAddon SlimefunAddons}.
 * But it also enabled Server Admins to locate lag-inducing areas on the {@link Server}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see TickerTask
 *
 */
public class SlimefunProfiler {

    // The threshold at which a Block or Chunk is significant enough to appear in /sf timings
    private static final int VISIBILITY_THRESHOLD = 275_000;

    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicInteger queued = new AtomicInteger(0);

    private long totalElapsedTime;

    private final Map<ProfiledBlock, Long> timings = new ConcurrentHashMap<>();
    private final Queue<CommandSender> requests = new ConcurrentLinkedQueue<>();

    /**
     * This method starts the profiling, data from previous runs will be cleared.
     */
    public void start() {
        running.set(true);
        queued.set(0);
        timings.clear();
    }

    /**
     * This method starts a new profiler entry.
     * 
     * @return A timestamp, best fed back into {@link #closeEntry(Location, SlimefunItem, long)}
     */
    public long newEntry() {
        if (!running.get()) {
            return 0;
        }

        queued.incrementAndGet();
        return System.nanoTime();
    }

    /**
     * This method closes a previously started entry.
     * Make sure to call {@link #newEntry()} to get the timestamp in advance.
     * 
     * @param l
     *            The {@link Location} of our {@link Block}
     * @param item
     *            The {@link SlimefunItem} at this {@link Location}
     * @param timestamp
     *            The timestamp marking the start of this entry, you can retrieve it using {@link #newEntry()}
     */
    public void closeEntry(Location l, SlimefunItem item, long timestamp) {
        if (timestamp == 0) {
            return;
        }

        long elapsedTime = System.nanoTime() - timestamp;

        executor.execute(() -> {
            ProfiledBlock block = new ProfiledBlock(new BlockPosition(l), item);
            timings.put(block, elapsedTime);
            queued.decrementAndGet();
        });
    }

    /**
     * This stops the profiling.
     */
    public void stop() {
        running.set(false);

        if (SlimefunPlugin.instance == null || !SlimefunPlugin.instance.isEnabled()) {
            // Slimefun has been disabled
            return;
        }

        // Since we got more than one Thread in our pool, blocking this one is completely fine
        executor.execute(() -> {

            // Wait for all timing results to come in
            while (queued.get() > 0 && !running.get()) {
                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException e) {
                    Slimefun.getLogger().log(Level.SEVERE, "A waiting Thread was interrupted", e);
                    Thread.currentThread().interrupt();
                }
            }

            if (running.get()) {
                // Looks like the next profiling has already started, abort!
                return;
            }

            totalElapsedTime = timings.values().stream().mapToLong(Long::longValue).sum();

            Iterator<CommandSender> iterator = requests.iterator();

            while (iterator.hasNext()) {
                sendSummary(iterator.next());
                iterator.remove();
            }
        });

    }

    /**
     * This method requests a summary for the given {@link CommandSender}.
     * The summary will be sent upon the next available moment in time.
     * 
     * @param sender
     *            The {@link CommandSender} who shall receive this summary.
     */
    public void requestSummary(CommandSender sender) {
        requests.add(sender);
    }

    private Map<String, Long> getByItem() {
        Map<String, Long> map = new HashMap<>();

        for (Map.Entry<ProfiledBlock, Long> entry : timings.entrySet()) {
            map.merge(entry.getKey().getId(), entry.getValue(), Long::sum);
        }

        return map;
    }

    private Map<String, Long> getByChunk() {
        Map<String, Long> map = new HashMap<>();

        for (Map.Entry<ProfiledBlock, Long> entry : timings.entrySet()) {
            String world = entry.getKey().getPosition().getWorld().getName();
            int x = entry.getKey().getPosition().getChunkX();
            int z = entry.getKey().getPosition().getChunkZ();

            map.merge(world + " (" + x + ',' + z + ')', entry.getValue(), Long::sum);
        }

        return map;
    }

    private int getBlocksInChunk(String chunk) {
        int blocks = 0;

        for (ProfiledBlock block : timings.keySet()) {
            String world = block.getPosition().getWorld().getName();
            int x = block.getPosition().getChunkX();
            int z = block.getPosition().getChunkZ();

            if (chunk.equals(world + " (" + x + ',' + z + ')')) {
                blocks++;
            }
        }

        return blocks;
    }

    private int getBlocks(String id) {
        int blocks = 0;

        for (ProfiledBlock block : timings.keySet()) {
            if (block.getId().equals(id)) {
                blocks++;
            }
        }

        return blocks;
    }

    private void sendSummary(CommandSender sender) {
        Map<String, Long> chunks = getByChunk();
        Map<String, Long> machines = getByItem();

        sender.sendMessage(ChatColors.color("&2== &aSlimefun Lag Profiler &2=="));
        sender.sendMessage(ChatColors.color("&6Running: &e&l" + String.valueOf(!SlimefunPlugin.getTickerTask().isHalted()).toUpperCase(Locale.ROOT)));
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Impact: &e" + NumberUtils.getAsMillis(totalElapsedTime)));
        sender.sendMessage(ChatColors.color("&6Ticked Chunks: &e" + chunks.size()));
        sender.sendMessage(ChatColors.color("&6Ticked Blocks: &e" + timings.size()));
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Ticking Machines:"));

        summarizeTimings(sender, entry -> {
            int count = getBlocks(entry.getKey());
            String time = NumberUtils.getAsMillis(entry.getValue());
            String average = NumberUtils.getAsMillis(entry.getValue() / count);

            return entry.getKey() + " - " + count + "x (" + time + ", " + average + " avg/block)";
        }, machines.entrySet().stream());

        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Ticking Chunks:"));

        summarizeTimings(sender, entry -> {
            int count = getBlocksInChunk(entry.getKey());
            String time = NumberUtils.getAsMillis(entry.getValue());

            return entry.getKey() + " - " + count + "x (" + time + ")";
        }, chunks.entrySet().stream());
    }

    private void summarizeTimings(CommandSender sender, Function<Map.Entry<String, Long>, String> formatter, Stream<Map.Entry<String, Long>> stream) {
        List<Entry<String, Long>> results = stream.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());

        if (sender instanceof Player) {
            TextComponent component = new TextComponent("   Hover for more details...");
            component.setColor(net.md_5.bungee.api.ChatColor.GRAY);
            component.setItalic(true);
            StringBuilder builder = new StringBuilder();
            int hidden = 0;

            for (Map.Entry<String, Long> entry : results) {
                if (entry.getValue() > VISIBILITY_THRESHOLD) {
                    builder.append("\n&e").append(formatter.apply(entry));
                }
                else {
                    hidden++;
                }
            }

            builder.append("\n\n&c+ &6").append(hidden).append(" more");
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColors.color(builder.toString()))));
            sender.spigot().sendMessage(component);
        }
        else {
            int hidden = 0;

            for (Map.Entry<String, Long> entry : results) {
                if (entry.getValue() > VISIBILITY_THRESHOLD) {
                    sender.sendMessage("  " + ChatColor.stripColor(formatter.apply(entry)));
                }
                else {
                    hidden++;
                }
            }

            sender.sendMessage("+ " + hidden + " more");
        }
    }

    public String getTime() {
        return NumberUtils.getAsMillis(totalElapsedTime);
    }

    public String getTime(Block b) {
        Validate.notNull("Cannot get timings for a null Block");

        long time = timings.getOrDefault(new ProfiledBlock(b), 0L);
        return NumberUtils.getAsMillis(time);
    }

    public String getTime(Chunk chunk) {
        Validate.notNull("Cannot get timings for a null Chunk");

        long time = getByChunk().getOrDefault(chunk.getWorld().getName() + " (" + chunk.getX() + ',' + chunk.getZ() + ')', 0L);
        return NumberUtils.getAsMillis(time);
    }

    public String getTime(SlimefunItem item) {
        Validate.notNull("Cannot get timings for a null SlimefunItem");

        long time = getByItem().getOrDefault(item.getID(), 0L);
        return NumberUtils.getAsMillis(time);
    }

}
