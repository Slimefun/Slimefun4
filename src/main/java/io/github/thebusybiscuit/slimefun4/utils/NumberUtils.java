package io.github.thebusybiscuit.slimefun4.utils;

import org.bukkit.ChatColor;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

public final class NumberUtils {

    private NumberUtils() {
    }

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
            return "< 1 小时";
        } else if ((hours / 24) == 0) {
            return (hours % 24) + "小时";
        } else if (hours % 24 == 0) {
            return (hours / 24) + "天";
        } else {
            return (hours / 24) + "天 " + (hours % 24) + "小时";
        }
    }

    public static String getTimeLeft(int seconds) {
        String timeleft = "";

        int minutes = (int) (seconds / 60L);
        if (minutes > 0) {
            timeleft += minutes + "分钟 ";
        }

        seconds -= minutes * 60;
        return timeleft + seconds + "秒";
    }

}
