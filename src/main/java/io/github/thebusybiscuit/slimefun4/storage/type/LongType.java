package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

public class LongType implements Type {

    private final long value;

    public LongType(long value) {
        this.value = value;
    }

    /**
     * Get the long value stored in this {@link LongType}
     *
     * @return The long value stored in this {@link LongType}
     */
    public long getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.LONG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + Long.hashCode(value);
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LongType)) {
            return false;
        }
        LongType other = (LongType) obj;
        return this.value == other.getValue();
    }
}
