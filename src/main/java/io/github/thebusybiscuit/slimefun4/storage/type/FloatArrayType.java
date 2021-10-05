package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class FloatArrayType implements Type {

    private final float[] value;

    public FloatArrayType(float[] value) {
        this.value = value;
    }

    public float[] getValue() {
        return value;
    }

    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.BYTE_ARRAY;
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
        if (!(obj instanceof FloatArrayType)) {
            return false;
        }
        FloatArrayType other = (FloatArrayType) obj;
        return Arrays.equals(this.value, other.getValue());
    }
}

