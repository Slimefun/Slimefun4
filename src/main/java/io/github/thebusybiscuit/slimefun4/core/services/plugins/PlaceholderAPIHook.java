package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class PlaceholderAPIHook extends PlaceholderExpansion {

    @Override
    public String getAuthor() {
        return SlimefunPlugin.instance.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "slimefun";
    }

    @Override
    public String getVersion() {
        return SlimefunPlugin.instance.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        if (params.equals("researches_total_xp_levels_spent") && PlayerProfile.request(p)) {
            Optional<PlayerProfile> profile = PlayerProfile.find(p);

            if (profile.isPresent()) {
                Stream<Research> stream = profile.get().getResearches().stream();
                return String.valueOf(stream.mapToInt(Research::getCost).sum());
            }
        }

        if (params.equals("researches_total_researches_unlocked") && PlayerProfile.request(p)) {
            Optional<PlayerProfile> profile = PlayerProfile.find(p);

            if (profile.isPresent()) {
                Set<Research> set = profile.get().getResearches();
                return String.valueOf(set.size());
            }
        }

        if (params.equals("researches_total_researches")) {
            return String.valueOf(SlimefunPlugin.getRegistry().getResearches().size());
        }

        if (params.equals("researches_percentage_researches_unlocked") && PlayerProfile.request(p)) {
            Optional<PlayerProfile> profile = PlayerProfile.find(p);

            if (profile.isPresent()) {
                Set<Research> set = profile.get().getResearches();
                return String.valueOf(Math.round(((set.size() * 100.0F) / SlimefunPlugin.getRegistry().getResearches().size()) * 100.0F) / 100.0F);
            }
        }

        if (params.equals("researches_title") && PlayerProfile.request(p)) {
            Optional<PlayerProfile> profile = PlayerProfile.find(p);

            if (profile.isPresent()) {
                return profile.get().getTitle();
            }
        }

        if (params.equals("gps_complexity")) {
            return String.valueOf(SlimefunPlugin.getGPSNetwork().getNetworkComplexity(p.getUniqueId()));
        }

        if (params.equals("timings_lag")) {
            return SlimefunPlugin.getTicker().getTime() + "ms";
        }

        if (params.equals("language")) {
            if (!(p instanceof Player)) {
                return "Unknown";
            }

            return SlimefunPlugin.getLocal().getLanguage((Player) p).getName((Player) p);
        }

        return null;
    }

}
