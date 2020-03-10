package me.mrCookieSlime.Slimefun.utils;

import java.util.List;

import io.github.thebusybiscuit.cscorelib2.config.Config;

// Soon this class can be discarded
public final class ConfigCache {

    public boolean researchesEnabled;
    public final boolean researchesFreeInCreative;
    public final boolean researchFireworksEnabled;
    public final List<String> researchesTitles;

    public ConfigCache(Config cfg) {
        researchesFreeInCreative = cfg.getBoolean("options.allow-free-creative-research");
        researchesTitles = cfg.getStringList("research-ranks");
        researchFireworksEnabled = cfg.getBoolean("options.research-unlock-fireworks");
    }

}
