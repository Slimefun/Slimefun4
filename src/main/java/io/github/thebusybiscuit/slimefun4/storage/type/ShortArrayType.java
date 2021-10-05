package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ShortArrayType implements Type {

    private final short[] value;

    public ShortArrayType(short[] value) {
        this.value = value;
    }

    public short[] getValue() {
        return value;
    }

    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.SHORT_ARRAY;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + Arrays.hashCode(value);
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ShortArrayType)) {
            return false;
        }
        ShortArrayType other = (ShortArrayType) obj;
        return Arrays.equals(this.value, other.getValue());
    }
}


