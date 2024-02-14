package io.github.thebusybiscuit.slimefun4.storage.backend.binary.serializers;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

import io.github.bakedlibs.dough.nbt.tags.IntArrayTag;

public class UUIDSerializer implements BinarySerialize<UUID, IntArrayTag> {
    
    public static final UUIDSerializer INSTANCE = new UUIDSerializer();

    private UUIDSerializer() {}

    @Override
    public IntArrayTag serialize(@Nonnull UUID uuid) {
        Validate.notNull(uuid, "The provided UUID cannot be null");

        long mostSig = uuid.getMostSignificantBits();
        long leastSig = uuid.getLeastSignificantBits();
        int[] ints = new int[] {
            (int) (mostSig >> 32),
            (int) mostSig,
            (int) (leastSig >> 32),
            (int) leastSig
        };

        return new IntArrayTag(ints);
    }

    @Override
    public UUID deserialize(@Nonnull IntArrayTag uuid) {
        Validate.notNull(uuid, "The provided UUID cannot be null");

        int[] ints = uuid.getValue();
        return new UUID(
            (long) ints[0] << 32L | ints[1] & 0xFFFFFFFFL,
            (long) ints[2] << 32L | ints[3] & 0xFFFFFFFFL
        );
    }
}
