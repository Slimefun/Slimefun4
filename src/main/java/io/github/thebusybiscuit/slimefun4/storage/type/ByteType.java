package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

public class ByteType extends Type {

    private final byte value;

    public ByteType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.BYTE;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + value;
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ByteType)) {
            return false;
        }
        ByteType other = (ByteType) obj;
        return this.value == other.value;
    }
}
