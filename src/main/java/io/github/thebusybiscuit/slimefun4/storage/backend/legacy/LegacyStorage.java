package io.github.thebusybiscuit.slimefun4.storage.backend.legacy;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.storage.Storage;
import io.github.thebusybiscuit.slimefun4.storage.data.PlayerData;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class LegacyStorage implements Storage {

    @Override
    public PlayerData loadPlayerData(@Nonnull UUID uuid) {
        Config file = new Config("data-storage/Slimefun/Players/" + uuid + ".yml");

        Set<NamespacedKey> researches = new HashSet<>();

        for (Research research : Slimefun.getRegistry().getResearches()) {
            if (file.contains("researches." + research.getID())) {
                researches.add(research.getKey());
            }
        }

        // TODO:
        // * Backpacks
        // * Waypoints?

        return new PlayerData(researches);
    }

    @Override
    public void savePlayerData(@Nonnull UUID uuid, @Nonnull PlayerData data) {
        Config file = new Config("data-storage/Slimefun/Players/" + uuid + ".yml");

        for (NamespacedKey key : data.getResearches()) {
            // Legacy data uses IDs, we'll look these up
            Optional<Research> research = Slimefun.getRegistry().getResearches().stream()
                .filter((rs) -> key == rs.getKey())
                .findFirst();

            research.ifPresent(value -> file.setValue("researches." + value.getID(), true));
        }

        file.save();
    }
}
