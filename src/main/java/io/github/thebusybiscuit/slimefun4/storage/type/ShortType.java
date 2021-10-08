package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

public class ShortType implements Type {

    private final short value;

    public ShortType(short value) {
        this.value = value;
    }

    /**
     * Get the short value stored in this {@link ShortType}
     *
     * @return The short value stored in this {@link ShortType}
     */
    public short getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.SHORT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + value;
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ShortType)) {
            return false;
        }
        ShortType other = (ShortType) obj;
        return this.value == other.getValue();
    }
}
