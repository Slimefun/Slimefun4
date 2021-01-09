package io.github.thebusybiscuit.slimefun4.core.services.profiler;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;

import io.github.thebusybiscuit.slimefun4.core.services.profiler.inspectors.PlayerPerformanceInspector;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

class PerformanceSummary {

    // The threshold at which a Block or Chunk is significant enough to appear in /sf timings
    private static final int VISIBILITY_THRESHOLD = 260_000;
    private static final int MIN_ITEMS = 6;
    private static final int MAX_ITEMS = 20;

    private final SlimefunProfiler profiler;
    private final PerformanceRating rating;
    private final long totalElapsedTime;
    private final int totalTickedBlocks;
    private final float percentage;
    private final int tickRate;

    private final Map<String, Long> chunks;
    private final Map<String, Long> plugins;
    private final Map<String, Long> items;

    PerformanceSummary(@Nonnull SlimefunProfiler profiler, long totalElapsedTime, int totalTickedBlocks) {
        this.profiler = profiler;
        this.rating = profiler.getPerformance();
        this.percentage = profiler.getPercentageOfTick();
        this.totalElapsedTime = totalElapsedTime;
        this.totalTickedBlocks = totalTickedBlocks;
        this.tickRate = profiler.getTickRate();

        chunks = profiler.getByChunk();
        plugins = profiler.getByPlugin();
        items = profiler.getByItem();
    }

    public void send(@Nonnull PerformanceInspector sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + "===== Slimefun Lag Profiler =====");
        sender.sendMessage(ChatColor.GOLD + "Total time: " + ChatColor.YELLOW + NumberUtils.getAsMillis(totalElapsedTime));
        sender.sendMessage(ChatColor.GOLD + "Running every: " + ChatColor.YELLOW + NumberUtils.roundDecimalNumber(tickRate / 20.0) + "s (" + tickRate + " ticks)");
        sender.sendMessage(ChatColor.GOLD + "Performance: " + getPerformanceRating());
        sender.sendMessage("");

        summarizeTimings(totalTickedBlocks, "block", sender, items, entry -> {
            int count = profiler.getBlocksOfId(entry.getKey());
            String time = NumberUtils.getAsMillis(entry.getValue());

            if (count > 1) {
                String average = NumberUtils.getAsMillis(entry.getValue() / count);

                return entry.getKey() + " - " + count + "x (" + time + " | avg: " + average + ')';
            } else {
                return entry.getKey() + " - " + count + "x (" + time + ')';
            }
        });

        summarizeTimings(chunks.size(), "chunk", sender, chunks, entry -> {
            int count = profiler.getBlocksInChunk(entry.getKey());
            String time = NumberUtils.getAsMillis(entry.getValue());

            return entry.getKey() + " - " + count + " block" + (count != 1 ? 's' : "") + " (" + time + ")";
        });

        summarizeTimings(plugins.size(), "plugin", sender, plugins, entry -> {
            int count = profiler.getBlocksFromPlugin(entry.getKey());
            String time = NumberUtils.getAsMillis(entry.getValue());

            return entry.getKey() + " - " + count + " block" + (count != 1 ? 's' : "") + " (" + time + ")";
        });
    }

    @ParametersAreNonnullByDefault
    private void summarizeTimings(int count, String name, PerformanceInspector inspector, Map<String, Long> map, Function<Map.Entry<String, Long>, String> formatter) {
        Stream<Map.Entry<String, Long>> stream = map.entrySet().stream();
        List<Entry<String, Long>> results = stream.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());
        String prefix = count + " " + name + (count != 1 ? 's' : "");

        if (inspector instanceof PlayerPerformanceInspector) {
            Component component = summarizeAsTextComponent(count, prefix, results, formatter);
            ((PlayerPerformanceInspector) inspector).sendMessage(component);
        } else {
            String text = summarizeAsString(inspector, count, prefix, results, formatter);
            inspector.sendMessage(text);
        }
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private Component summarizeAsTextComponent(int count, String prefix, List<Map.Entry<String, Long>> results, Function<Entry<String, Long>, String> formatter) {
        TextComponent.Builder component = Component.text().content(prefix).color(NamedTextColor.YELLOW);

        if (count > 0) {
            TextComponent.Builder details = Component.text().content("  (Hover for details)").color(NamedTextColor.GRAY);
            TextComponent.Builder tooltip = Component.text();

            int shownEntries = 0;
            int hiddenEntries = 0;

            for (Map.Entry<String, Long> entry : results) {
                if (shownEntries < MAX_ITEMS && (shownEntries < MIN_ITEMS || entry.getValue() > VISIBILITY_THRESHOLD)) {
                    tooltip.append(Component.newline());
                    tooltip.append(Component.text(formatter.apply(entry), NamedTextColor.YELLOW));
                    shownEntries++;
                } else {
                    hiddenEntries++;
                }
            }

            if (hiddenEntries > 0) {
                tooltip.append(Component.newline());
                tooltip.append(Component.newline());
                tooltip.append(Component.text("+ ", NamedTextColor.RED));
                tooltip.append(Component.text(hiddenEntries + " more", NamedTextColor.GOLD));
            }

            details.hoverEvent(HoverEvent.showText(tooltip));
            component.append(details);
        }

        return component.build();
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private String summarizeAsString(PerformanceInspector inspector, int count, String prefix, List<Entry<String, Long>> results, Function<Entry<String, Long>, String> formatter) {
        int shownEntries = 0;
        int hiddenEntries = 0;

        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GOLD).append(prefix);

        if (count > 0) {
            builder.append(ChatColor.YELLOW);

            for (Map.Entry<String, Long> entry : results) {
                if (inspector.isVerbose() || (shownEntries < MAX_ITEMS && (shownEntries < MIN_ITEMS || entry.getValue() > VISIBILITY_THRESHOLD))) {
                    builder.append("\n  ");
                    builder.append(ChatColor.stripColor(formatter.apply(entry)));
                    shownEntries++;
                } else {
                    hiddenEntries++;
                }
            }

            if (hiddenEntries > 0) {
                builder.append("\n+ ").append(hiddenEntries).append(" more...");
            }
        }

        return builder.toString();
    }

    @Nonnull
    private String getPerformanceRating() {
        StringBuilder builder = new StringBuilder();
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
