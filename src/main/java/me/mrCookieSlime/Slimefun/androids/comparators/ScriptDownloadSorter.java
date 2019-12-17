package me.mrCookieSlime.Slimefun.androids.comparators;

import java.util.Comparator;

import io.github.thebusybiscuit.cscorelib2.config.Config;

public class ScriptDownloadSorter implements Comparator<Config> {

	@Override
	public int compare(Config c1, Config c2) {
		return c2.getInt("downloads") - c1.getInt("downloads");
	}

}
