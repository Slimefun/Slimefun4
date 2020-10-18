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

/**
 * This class contains various utilities related to numbers and number formatting.
 * 
 * @author TheBusyBiscuit
 * @author Walshy
 *
 */
public final class NumberUtils {

    /**
     * This is our {@link DecimalFormat} for decimal values.
     */
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    /**
     * We do not want any instance of this to be created.
     */
    private NumberUtils() {}

    /**
     * This method formats a given {@link Integer} to be displayed nicely with
     * decimal digit grouping.
     * {@code 1000000} for example will return {@code "1,000,000"} as a {@link String}.
     * It uses the american (US) {@link Locale} for this transformation.
     * 
     * @param number
     *            Your {@link Integer}
     * 
     * @return The formatted String
     */
    @Nonnull
    public static String formatBigNumber(int number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    /**
     * This method transforms a String representation of a {@link LocalDateTime}
     * from GitHub's API back into a {@link LocalDateTime} object
     * 
     * @param date
     *            The formatted String version of a date from GitHub
     * 
     * @return The {@link LocalDateTime} for the given input
     */
    @Nonnull
    public static LocalDateTime parseGitHubDate(@Nonnull String date) {
        Validate.notNull(date, "Provided date was null");
        return LocalDateTime.parse(date.substring(0, date.length() - 1));
    }

    /**
     * This will return a representative color for the given percentage.
     * Lower levels will result in a darker tone of red, higher levels will
     * result in more brighter shades of green.
     * 
     * @param percentage
     *            The amount of percentage as a float
     * 
     * @return A representative {@link ChatColor}
     */
    @Nonnull
    public static ChatColor getColorFromPercentage(float percentage) {
        if (percentage < 16.0F) {
            return ChatColor.DARK_RED;
        } else if (percentage < 32.0F) {
            return ChatColor.RED;
        } else if (percentage < 48.0F) {
            return ChatColor.GOLD;
        } else if (percentage < 64.0F) {
            return ChatColor.YELLOW;
        } else if (percentage < 80.0F) {
            return ChatColor.DARK_GREEN;
        } else {
            return ChatColor.GREEN;
        }
    }

    /**
     * This returns the elapsed time since the given {@link LocalDateTime}.
     * The output will be nicely formatted based on the elapsed hours or days since the
     * given {@link LocalDateTime}.
     * 
     * If a {@link LocalDateTime} from yesterday was passed it will return {@code "1d"}.
     * One hour later it will read {@code "1d 1h"}. For values smaller than an hour {@code "< 1h"}
     * will be returned instead.
     * 
     * @param date
     *            The {@link LocalDateTime}.
     * 
     * @return The elapsed time as a {@link String}
     */
    @Nonnull
    public static String getElapsedTime(@Nonnull LocalDateTime date) {
        Validate.notNull(date, "Provided date was null");
        long hours = Duration.between(date, LocalDateTime.now()).toHours();

        if (hours == 0) {
            return "< 1h";
        } else if (hours / 24 == 0) {
            return (hours % 24) + "h";
        } else if (hours % 24 == 0) {
            return (hours / 24) + "d";
        } else {
            return (hours / 24) + "d " + (hours % 24) + "h";
        }
    }

    @Nonnull
    public static String getTimeLeft(int seconds) {
        String timeleft = "";

        int minutes = (int) (seconds / 60L);

        if (minutes > 0) {
            timeleft += minutes + "m ";
        }

        seconds -= minutes * 60;
        return timeleft + seconds + "s";
    }

    public static int getInt(@Nonnull String str, int defaultValue) {
        if (PatternUtils.NUMERIC.matcher(str).matches()) {
            return Integer.parseInt(str);
        }

        return defaultValue;
    }

    @Nonnull
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
