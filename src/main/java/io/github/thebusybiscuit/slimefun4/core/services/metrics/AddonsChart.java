package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import java.util.HashMap;
import java.util.Map;

import org.bstats.bukkit.Metrics.AdvancedPie;
import org.bukkit.plugin.Plugin;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class AddonsChart extends AdvancedPie {

    AddonsChart() {
        super("installed_addons", () -> {
            Map<String, Integer> addons = new HashMap<>();

            for (Plugin plugin : SlimefunPlugin.getInstalledAddons()) {
                if (plugin.isEnabled()) {
                    addons.put(plugin.getName(), 1);
                }
            }

            return addons;
        });
    }

}
