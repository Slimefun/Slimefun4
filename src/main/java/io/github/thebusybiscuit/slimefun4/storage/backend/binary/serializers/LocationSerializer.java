package io.github.thebusybiscuit.slimefun4.storage.backend.binary.serializers;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.bakedlibs.dough.nbt.tags.CompoundTag;
import io.github.bakedlibs.dough.nbt.tags.IntArrayTag;
import io.github.thebusybiscuit.slimefun4.storage.backend.binary.CommonKeys;

public class LocationSerializer implements BinarySerialize<Location, CompoundTag> {

    public static final LocationSerializer INSTANCE = new LocationSerializer();

    private LocationSerializer() {}

    @Override
    public CompoundTag serialize(Location location) {
        CompoundTag tag = new CompoundTag();

        if (location.getWorld() != null) {
            tag.put(CommonKeys.WORLD, UUIDSerializer.INSTANCE.serialize(location.getWorld().getUID()));
        }

        tag.putDouble(CommonKeys.POSITION, BlockPosition.getAsLong(location));

        if (location.getPitch() != 0.0F) {
            tag.putFloat(CommonKeys.PITCH, location.getPitch());
        }
        if (location.getYaw() != 0.0F) {
            tag.putFloat(CommonKeys.YAW, location.getYaw());
        }

        return tag;
    }

    @Override
    public Location deserialize(CompoundTag location) {
        UUID worldUuid = null;
        IntArrayTag worldTag = (IntArrayTag) location.get(CommonKeys.WORLD);
        if (worldTag != null) {
            worldUuid = UUIDSerializer.INSTANCE.deserialize(worldTag);
        }

        BlockPosition position = new BlockPosition(null, location.getLong(CommonKeys.POSITION).getAsLong());

        double x = position.getX();
        double y = position.getY();
        double z = position.getZ();
        float pitch = location.getFloat(CommonKeys.PITCH).orElse(0.0f);
        float yaw = location.getFloat(CommonKeys.YAW).orElse(0.0f);

        return new Location(
            worldUuid != null ? Bukkit.getWorld(worldUuid) : null,
            x, y, z, yaw, pitch
        );
    }
}
