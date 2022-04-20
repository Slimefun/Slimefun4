package io.github.thebusybiscuit.slimefun4.core.services.profiler;

import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

/**
 * This enum is used to quantify Slimefun's performance impact. This way we can assign a
 * "grade" to each timings report and also use this for metrics collection.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunProfiler
 *
 */
public enum PerformanceRating implements Predicate<Float> {

    // Thresholds might change in the future!

    UNKNOWN(ChatColor.WHITE, -1),

    GOOD(ChatColor.DARK_GREEN, 10),
    FINE(ChatColor.DARK_GREEN, 20),
    OKAY(ChatColor.GREEN, 30),
    MODERATE(ChatColor.YELLOW, 55),
    SEVERE(ChatColor.RED, 85),
    HURTFUL(ChatColor.DARK_RED, 500),
    BAD(ChatColor.DARK_RED, Float.MAX_VALUE);

    private final ChatColor color;
    private final float threshold;

    PerformanceRating(@Nonnull ChatColor color, float threshold) {
        Validate.notNull(color, "Color cannot be null");
        this.color = color;
        this.threshold = threshold;
    }

    @Override
    public boolean test(@Nullable Float value) {
        if (value == null) {
            // This way null will only test true for UNKNOWN
            return threshold < 0;
        }

        return value <= threshold;
    }

    @Nonnull
    public ChatColor getColor() {
        return color;
    }

}
