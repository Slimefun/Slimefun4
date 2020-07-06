package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import java.util.HashMap;
import java.util.Map;

import org.bstats.bukkit.Metrics.AdvancedPie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class PlayerLanguageChart extends AdvancedPie {

    PlayerLanguageChart() {
        super("player_languages", () -> {
            Map<String, Integer> languages = new HashMap<>();

            for (Player p : Bukkit.getOnlinePlayers()) {
                Language language = SlimefunPlugin.getLocalization().getLanguage(p);
                boolean supported = SlimefunPlugin.getLocalization().isLanguageLoaded(language.getId());

                String lang = supported ? language.getId() : "Unsupported Language";
                languages.merge(lang, 1, Integer::sum);
            }

            return languages;
        });
    }

}
