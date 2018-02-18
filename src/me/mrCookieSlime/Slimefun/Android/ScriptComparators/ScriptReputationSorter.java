package me.mrCookieSlime.Slimefun.Android.ScriptComparators;

import java.util.Comparator;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Android.ProgrammableAndroid;

public class ScriptReputationSorter implements Comparator<Config> {

	private ProgrammableAndroid android;
	
	public ScriptReputationSorter(ProgrammableAndroid programmableAndroid) {
		this.android = programmableAndroid;
	}

	@Override
	public int compare(Config c1, Config c2) {
		return (int) (android.getScriptRating(c2) - android.getScriptRating(c1));
	}

}
