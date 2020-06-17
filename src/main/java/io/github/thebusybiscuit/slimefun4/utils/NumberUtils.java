package io.github.thebusybiscuit.slimefun4.utils;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

import org.bukkit.ChatColor;

public final class NumberUtils {

    private NumberUtils() {}

    public static String formatBigNumber(int i) {
        return NumberFormat.getNumberInstance(Locale.US).format(i);
    }

    public static LocalDateTime parseGitHubDate(String date) {
        return LocalDateTime.parse(date.substring(0, date.length() - 1));
    }

    public static ChatColor getColorFromPercentage(float percentage) {
        if (percentage < 16.0F) return ChatColor.DARK_RED;
        else if (percentage < 32.0F) return ChatColor.RED;
        else if (percentage < 48.0F) return ChatColor.GOLD;
        else if (percentage < 64.0F) return ChatColor.YELLOW;
        else if (percentage < 80.0F) return ChatColor.DARK_GREEN;
        else return ChatColor.GREEN;
    }

    public static String getElapsedTime(LocalDateTime date) {
        long hours = Duration.between(date, LocalDateTime.now()).toHours();

        if (hours == 0) {
            return "< 1h";
        }
        else if ((hours / 24) == 0) {
            return (hours % 24) + "h";
        }
        else if (hours % 24 == 0) {
            return (hours / 24) + "d";
        }
        else {
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

    public static int getInt(String str, int defaultVal) {
        if (PatternUtils.NUMERIC.matcher(str).matches()) {
            return Integer.parseInt(str);
        }

        return defaultVal;
    }
}
