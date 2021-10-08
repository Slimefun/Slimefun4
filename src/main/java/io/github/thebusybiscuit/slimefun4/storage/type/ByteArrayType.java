package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ByteArrayType implements Type {

    private final byte[] value;

    public ByteArrayType(byte[] value) {
        this.value = value;
    }

    /**
     * Get the byte array stored in this {@link ByteArrayType}
     *
     * @return The byte array stored in this {@link ByteArrayType}
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.BYTE_ARRAY;
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
        if (!(obj instanceof ByteArrayType)) {
            return false;
        }
        ByteArrayType other = (ByteArrayType) obj;
        return Arrays.equals(this.value, other.getValue());
    }
}

