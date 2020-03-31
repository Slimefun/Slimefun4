package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bstats.bukkit.Metrics.SimplePie;

class ServerLanguageChart extends SimplePie {

    ServerLanguageChart() {
        super("language", () -> {
            Language language = SlimefunPlugin.getLocal().getDefaultLanguage();
            boolean supported = SlimefunPlugin.getLocal().isLanguageLoaded(language.getId());
            return supported ? language.getId() : "Unsupported Language";
        });
    }

}
