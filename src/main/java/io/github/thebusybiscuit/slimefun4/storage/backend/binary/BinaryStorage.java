package io.github.thebusybiscuit.slimefun4.storage.backend.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.github.thebusybiscuit.slimefun4.core.debug.Debug;
import io.github.thebusybiscuit.slimefun4.core.debug.TestCase;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

import io.github.bakedlibs.dough.nbt.TagType;
import io.github.bakedlibs.dough.nbt.streams.CompressionType;
import io.github.bakedlibs.dough.nbt.streams.TagInputStream;
import io.github.bakedlibs.dough.nbt.streams.TagOutputStream;
import io.github.bakedlibs.dough.nbt.tags.CompoundTag;
import io.github.bakedlibs.dough.nbt.tags.ListTag;
import io.github.bakedlibs.dough.nbt.tags.StringTag;
import io.github.bakedlibs.dough.nbt.tags.Tag;
import io.github.thebusybiscuit.slimefun4.api.gps.Waypoint;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.storage.Storage;
import io.github.thebusybiscuit.slimefun4.storage.backend.binary.serializers.LocationSerializer;
import io.github.thebusybiscuit.slimefun4.storage.data.PlayerData;

public class BinaryStorage implements Storage {

    private static final NamespacedKey PLAYER_DATA = new NamespacedKey(Slimefun.instance(), "player_data");
    private static final NamespacedKey RESEARCHES_KEY = new NamespacedKey(Slimefun.instance(), "researches");
    private static final NamespacedKey BACKPACKS_KEY = new NamespacedKey(Slimefun.instance(), "backpacks");
    private static final NamespacedKey WAYPOINTS_KEY = new NamespacedKey(Slimefun.instance(), "waypoints");

    // TODO: Move to lz4 or zstd
    private static final CompressionType compression = CompressionType.GZIP;

    /*
     * Structure as JSON:
     * {
     *   "slimefun:researches": ["slimefun:some_research", "slimefun:some_other_research"],
     *   "slimefun:backpacks": [
     *     {
     *       "id": x,
     *       "item": y
     *     }
     *   ],
     *   "slimefun:waypoints": {
     *     "<ID>": {
     *       "name": "",
     *       "location": {}
     *     }
     *   }
     * }
     */

    @Override
    public PlayerData loadPlayerData(UUID uuid) {
        Debug.log(TestCase.PLAYER_PROFILE_DATA, "Loading player data from binary storage for {}", uuid);
        File file = new File("data-storage/Slimefun/Players/" + uuid + ".dat");

        if (!file.exists()) {
            return new PlayerData(Set.of(), Map.of(), Set.of());
        }

        CompoundTag root;
        try {
            try (TagInputStream stream = new TagInputStream(new FileInputStream(file), compression)) {
                root = (CompoundTag) stream.readTag();
            }
        } catch(IOException e) {
            throw new IllegalStateException("Failed to read Player data for " + uuid, e);
        }

        // Load researches
        ListTag<StringTag> list = root.getList(RESEARCHES_KEY, TagType.STRING);

        Set<Research> researches = new HashSet<>();
        for (Research research : Slimefun.getRegistry().getResearches()) {
            for (StringTag tag : list) {
                if (tag.getValue().equals(research.getKey().toString())) {
                    researches.add(research);
                }
            }
        }

        // Load backpacks
        Map<Integer, PlayerBackpack> backpacks = Map.of();
        CompoundTag backpackMap = root.getCompound(BACKPACKS_KEY);
        // TODO: Item serialization

        // Load waypoints
        Set<Waypoint> waypoints = new HashSet<>();
        CompoundTag waypointMap = root.getCompound(WAYPOINTS_KEY);
        for (Map.Entry<NamespacedKey, Tag<?>> key : waypointMap) {
            CompoundTag waypoint = (CompoundTag) key.getValue();

            String name = waypoint.getString(CommonKeys.NAME);
            Location location = LocationSerializer.INSTANCE.deserialize(waypoint.getCompound(CommonKeys.LOCATION));

            waypoints.add(new Waypoint(uuid, key.getKey().getKey(), location, name));
        }

        return new PlayerData(researches, backpacks, waypoints);
    }

    @Override
    public void savePlayerData(UUID uuid, PlayerData data) {
        Debug.log(TestCase.PLAYER_PROFILE_DATA, "Saving player data from binary storage for {}", uuid);
        File file = new File("data-storage/Slimefun/Players/" + uuid + ".dat");

        CompoundTag root = new CompoundTag(PLAYER_DATA);

        // Save researches
        ListTag<StringTag> list = new ListTag<>();
        for (Research research : data.getResearches()) {
            list.add(new StringTag(research.getKey().toString()));
        }

        root.putList(RESEARCHES_KEY, list);

        // Save backpacks
        CompoundTag backpackMap = new CompoundTag();

        // Save waypoints
        CompoundTag waypointMap = new CompoundTag();
        for (Waypoint waypoint : data.getWaypoints()) {
            CompoundTag waypointTag = new CompoundTag();

            waypointTag.putString(CommonKeys.ID, waypoint.getId());
            waypointTag.putString(CommonKeys.NAME, waypoint.getName());
            waypointTag.putCompound(CommonKeys.LOCATION, LocationSerializer.INSTANCE.serialize(waypoint.getLocation()));

            waypointMap.putCompound(new NamespacedKey(Slimefun.instance(), waypoint.getId()), waypointTag);
        }
        root.putCompound(WAYPOINTS_KEY, waypointMap);

        try {
            try (TagOutputStream stream = new TagOutputStream(new FileOutputStream(file), compression)) {
                stream.writeTag(root);
            }
        } catch(IOException e) {
            throw new IllegalStateException("Failed to read Player data for " + uuid, e);
        }
    }
}
