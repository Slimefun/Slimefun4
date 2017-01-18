package me.mrCookieSlime.Slimefun.GitHub;

import java.text.NumberFormat;
import java.util.Locale;

public class IntegerFormat {

	public static String formatBigNumber(int i) {
		return NumberFormat.getNumberInstance(Locale.US).format(i);
	}

}
