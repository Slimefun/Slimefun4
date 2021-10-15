package io.github.thebusybiscuit.slimefun4.storage.transformer;

import io.github.thebusybiscuit.slimefun4.storage.DataObject;
import io.github.thebusybiscuit.slimefun4.storage.NamedKey;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public class UuidTransformer implements Transformer<UUID> {

    private static UuidTransformer instance = null;

    private UuidTransformer(){}

    /**
     * Retrieves this class' singleton
     *
     * @return The transformed object
     */
    public static UuidTransformer getInstance()
    {
        if (instance == null)
            instance = new UuidTransformer();

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ParametersAreNonnullByDefault
    public void transformInto(DataObject dataObject, NamedKey key, UUID uuid) {
        dataObject.setIntArray(key, toIntArray(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    @ParametersAreNonnullByDefault
    public UUID transformFrom(DataObject dataObject, NamedKey key) {
        final int[] arr = dataObject.getIntArray(key);

        if (arr == null) return null;

        return fromIntArray(arr);
    }

    /**
     * Gets the {@link UUID} stored in this int array
     *
     * @param ints The int array to transform
     * @return The {@link UUID}
     */
    @Nonnull
    private UUID fromIntArray(@Nonnull int[] ints) {
        Validate.notNull(ints, "The provided integer array cannot be null!");
        Validate.isTrue(ints.length == 4, "The integer array must have a length of 4");

        return new UUID((long) ints[0] << 32L | ints[1] & 0xFFFFFFFFL,
            (long) ints[2] << 32L | ints[3] & 0xFFFFFFFFL
        );
    }

    /**
     * Gets the int array this {@link UUID} is represented by
     *
     * @param uuid The {@link UUID} to transform
     * @return The int array
     */
    @Nonnull
    private int[] toIntArray(@Nonnull UUID uuid) {
        Validate.notNull(uuid, "The provided uuid cannot be null");

        long mostSig = uuid.getMostSignificantBits();
        long leastSig = uuid.getLeastSignificantBits();
        return new int[] { (int) (mostSig >> 32), (int) mostSig, (int) (leastSig >> 32), (int) leastSig };
    }
}
