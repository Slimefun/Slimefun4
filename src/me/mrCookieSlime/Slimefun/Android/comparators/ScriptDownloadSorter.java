package me.mrCookieSlime.Slimefun.Android.comparators;

import java.util.Comparator;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Android.ProgrammableAndroid;

public class ScriptDownloadSorter implements Comparator<Config> {

	@Override
	public int compare(Config c1, Config c2) {
		return (int) (c2.getInt("downloads") - c1.getInt("downloads"));
	}

}
