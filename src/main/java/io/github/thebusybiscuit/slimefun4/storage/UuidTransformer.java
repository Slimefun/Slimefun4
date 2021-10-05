package io.github.thebusybiscuit.slimefun4.storage;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public class UuidTransformer implements Transformer<UUID> {

    @Override
    @ParametersAreNonnullByDefault
    public void transformInto(DataObject dataObject, NamespacedKey key, UUID uuid) {
        dataObject.addIntArray(key, toIntArray(uuid));
    }

    @Override
    @Nullable
    @ParametersAreNonnullByDefault
    public UUID transformFrom(DataObject dataObject, NamespacedKey key) {
        final int[] arr = dataObject.getIntArray(key);

        if (arr == null) return null;

        return fromIntArray(arr);
    }

    @Nonnull
    private UUID fromIntArray(@Nonnull int[] ints) {
        Validate.notNull(ints, "The provided integer array cannot be null!");
        Validate.isTrue(ints.length == 4, "The integer array must have a length of 4");

        return UUID((long) ints[0] << 32L | ints[1] & 0xFFFFFFFFL,
            (long) ints[2] << 32L | ints[3] & 0xFFFFFFFFL
        );
    }
}
