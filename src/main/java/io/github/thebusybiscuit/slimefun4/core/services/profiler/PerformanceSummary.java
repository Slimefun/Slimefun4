package io.github.thebusybiscuit.slimefun4.core.services.profiler;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

class PerformanceSummary {

    // The threshold at which a Block or Chunk is significant enough to appear in /sf timings
    private static final int VISIBILITY_THRESHOLD = 275_000;

    // A minecraft server tick is 50ms and Slimefun ticks are stretched across
    // two ticks (sync and async blocks), so we use 100ms as a reference here
    static final int MAX_TICK_DURATION = 100;

    private final SlimefunProfiler profiler;
    private final PerformanceRating rating;
    private final long totalElapsedTime;
    private final int totalTickedBlocks;

    private final Map<String, Long> chunks;
    private final Map<String, Long> items;

    PerformanceSummary(SlimefunProfiler profiler, long totalElapsedTime, int totalTickedBlocks) {
        this.profiler = profiler;
        this.rating = profiler.getPerformance();
        this.totalElapsedTime = totalElapsedTime;
        this.totalTickedBlocks = totalTickedBlocks;

        chunks = profiler.getByChunk();
        items = profiler.getByItem();
    }

    public void send(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + "===== Slimefun Lag Profiler =====");
        sender.sendMessage(ChatColors.color("&6Total: &e" + NumberUtils.getAsMillis(totalElapsedTime)));
        sender.sendMessage(ChatColors.color("&6Performance: " + getPerformanceRating()));
        sender.sendMessage(ChatColors.color("&6Active Chunks: &e" + chunks.size()));
        sender.sendMessage(ChatColors.color("&6Active Blocks: &e" + totalTickedBlocks));
        sender.sendMessage("");

        summarizeTimings("Chunks", sender, entry -> {
            int count = profiler.getBlocksOfId(entry.getKey());
            String time = NumberUtils.getAsMillis(entry.getValue());

            if (count > 1) {
                String average = NumberUtils.getAsMillis(entry.getValue() / count);

                return entry.getKey() + " - " + count + "x (" + time + ", " + average + " avg/block)";
            }
            else {
                return entry.getKey() + " - " + count + "x (" + time + ')';
            }
        }, items.entrySet().stream());

        sender.sendMessage("");

        summarizeTimings("Blocks", sender, entry -> {
            int count = profiler.getBlocksInChunk(entry.getKey());
            String time = NumberUtils.getAsMillis(entry.getValue());

            return entry.getKey() + " - " + count + "x (" + time + ")";
        }, chunks.entrySet().stream());
    }

    private void summarizeTimings(String prefix, CommandSender sender, Function<Map.Entry<String, Long>, String> formatter, Stream<Map.Entry<String, Long>> stream) {
        List<Entry<String, Long>> results = stream.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());

        if (sender instanceof Player) {
            TextComponent component = new TextComponent(prefix);
            component.setColor(ChatColor.GOLD);

            TextComponent hoverComponent = new TextComponent("\n   Hover for more details...");
            hoverComponent.setColor(ChatColor.GRAY);
            hoverComponent.setItalic(true);
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
            hoverComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColors.color(builder.toString()))));

            component.addExtra(hoverComponent);
            sender.spigot().sendMessage(component);
        }
        else {
            int hidden = 0;

            sender.sendMessage(ChatColor.GOLD + prefix);

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

    private String getPerformanceRating() {
        StringBuilder builder = new StringBuilder();

        float percentage = Math.round(((((totalElapsedTime / 1000000.0) * 100.0F) / MAX_TICK_DURATION) * 100.0F) / 100.0F);
        builder.append(NumberUtils.getColorFromPercentage(100 - Math.min(percentage, 100)));

        int rest = 20;
        for (int i = (int) Math.min(percentage, 100); i >= 5; i = i - 5) {
            builder.append(':');
            rest--;
        }

        builder.append(ChatColor.DARK_GRAY);

        for (int i = 0; i < rest; i++) {
            builder.append(':');
        }

        builder.append(" - ");

        builder.append(rating.getColor() + ChatUtils.humanize(rating.name()));

        builder.append(ChatColor.GRAY);
        builder.append(" (");
        builder.append(NumberUtils.roundDecimalNumber(percentage));
        builder.append("%)");

        return builder.toString();
    }

}
