package me.mrCookieSlime.Slimefun.hooks.github;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class IntegerFormat {
	
	private IntegerFormat() {}
	
	public static String formatBigNumber(int i) {
		return NumberFormat.getNumberInstance(Locale.US).format(i);
	}
	
	public static Date parseGitHubDate(String str) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str.replace('T', ' ').replace("Z", ""));
		} catch (ParseException x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occured while parsing a GitHub-Date for Slimefun " + Slimefun.getVersion(), x);
			return null;
		}
	}
	
	public static String timeDelta(Date date) {
		long timestamp = date.getTime();
		int hours = (int) ((System.currentTimeMillis() - timestamp) / (1000 * 60 * 60));
		
		if (hours == 0) {
			return "> 1h";
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

}
