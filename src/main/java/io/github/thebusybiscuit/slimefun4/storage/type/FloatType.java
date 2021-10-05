package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

public class FloatType implements Type {

    private final float value;

    public FloatType(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.FLOAT;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + Float.hashCode(value);
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FloatType)) {
            return false;
        }
        FloatType other = (FloatType) obj;
        return this.value == other.getValue();
    }
}

