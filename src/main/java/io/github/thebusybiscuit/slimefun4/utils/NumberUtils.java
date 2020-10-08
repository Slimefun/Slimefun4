package io.github.thebusybiscuit.slimefun4.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

public final class NumberUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private NumberUtils() {}

    public static String formatBigNumber(int i) {
        return NumberFormat.getNumberInstance(Locale.US).format(i);
    }

    public static LocalDateTime parseGitHubDate(@Nonnull String date) {
        Validate.notNull(date, "Provided date was null");
        return LocalDateTime.parse(date.substring(0, date.length() - 1));
    }

    public static ChatColor getColorFromPercentage(float percentage) {
        if (percentage < 16.0F)
            return ChatColor.DARK_RED;
        else if (percentage < 32.0F)
            return ChatColor.RED;
        else if (percentage < 48.0F)
            return ChatColor.GOLD;
        else if (percentage < 64.0F)
            return ChatColor.YELLOW;
        else if (percentage < 80.0F)
            return ChatColor.DARK_GREEN;
        else
            return ChatColor.GREEN;
    }

    public static String getElapsedTime(@Nonnull LocalDateTime date) {
        Validate.notNull(date, "Provided date was null");
        long hours = Duration.between(date, LocalDateTime.now()).toHours();

        if (hours == 0) {
            return "< 1h";
        } else if ((hours / 24) == 0) {
            return (hours % 24) + "h";
        } else if (hours % 24 == 0) {
            return (hours / 24) + "d";
        } else {
            return (hours / 24) + "d " + (hours % 24) + "h";
        }
    }

    public static String getTimeLeft(int seconds) {
        String timeleft = "";

        int minutes = (int) (seconds / 60L);
        if (minutes > 0) {
            timeleft += minutes + "m ";
        }

        seconds -= minutes * 60;
        return timeleft + seconds + "s";
    }

    public static int getInt(String str, int defaultValue) {
        if (PatternUtils.NUMERIC.matcher(str).matches()) {
            return Integer.parseInt(str);
        }

        return defaultValue;
    }

    public static String getAsMillis(long nanoseconds) {
        if (nanoseconds == 0) {
            return "0ms";
        }

        String number = roundDecimalNumber(nanoseconds / 1000000.0);
        String[] parts = PatternUtils.NUMBER_SEPARATOR.split(number);

        if (parts.length == 1) {
            return parts[0] + "ms";
        } else {
            return parts[0] + '.' + parts[1] + "ms";
        }
    }

    public static String roundDecimalNumber(double number) {
        return DECIMAL_FORMAT.format(number);
    }

    public static long getLong(@Nullable Long value, long defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static int getInt(@Nullable Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static float getFloat(@Nullable Float value, float defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * This method is a combination of Math.min and Math.max, it clamps the given value
     * between a minimum and a maximum.
     * 
     * @param min
     *            The minimum value
     * @param value
     *            The value to clamp
     * @param max
     *            The maximum value
     */
    public static int clamp(int min, int value, int max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }
}
