package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import org.bstats.bukkit.Metrics.SimplePie;

import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class ServerLanguageChart extends SimplePie {

    public ServerLanguageChart() {
        super("language", () -> {
            Language language = SlimefunPlugin.getLocal().getDefaultLanguage();
            boolean supported = SlimefunPlugin.getLocal().isLanguageLoaded(language.getID());
            return supported ? language.getID() : "Unsupported Language";
        });
    }

}
