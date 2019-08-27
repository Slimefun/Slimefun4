package me.mrCookieSlime.Slimefun.androids.comparators;

import java.util.Comparator;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.androids.ProgrammableAndroid;

public class ScriptReputationSorter implements Comparator<Config> {

	private ProgrammableAndroid android;
	
	public ScriptReputationSorter(ProgrammableAndroid android) {
		this.android = android;
	}

	@Override
	public int compare(Config c1, Config c2) {
		return (int) (android.getScriptRating(c2) - android.getScriptRating(c1));
	}

}
