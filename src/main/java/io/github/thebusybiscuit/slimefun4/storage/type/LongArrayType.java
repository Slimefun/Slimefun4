package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class LongArrayType implements Type {

    private final long[] value;

    public LongArrayType(long[] value) {
        this.value = value;
    }

    /**
     * Get the long array stored in this {@link LongArrayType}
     *
     * @return The long array stored in this {@link LongArrayType}
     */
    public long[] getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.LONG_ARRAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + Arrays.hashCode(value);
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LongArrayType)) {
            return false;
        }
        LongArrayType other = (LongArrayType) obj;
        return Arrays.equals(this.value, other.getValue());
    }
}
