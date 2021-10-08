package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

public class ByteType implements Type {

    private final byte value;

    public ByteType(byte value) {
        this.value = value;
    }

    /**
     * Get the byte value stored in this {@link ByteType}
     *
     * @return The byte value stored in this {@link ByteType}
     */
    public byte getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.BYTE;
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
        if (!(obj instanceof ByteType)) {
            return false;
        }
        ByteType other = (ByteType) obj;
        return this.value == other.value;
    }
}
