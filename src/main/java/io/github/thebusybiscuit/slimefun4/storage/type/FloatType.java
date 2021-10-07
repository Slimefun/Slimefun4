package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

public class FloatType implements Type {

    private final float value;

    public FloatType(float value) {
        this.value = value;
    }

    /**
     * Get the float value stored in this {@link FloatType}
     *
     * @return The float value stored in this {@link FloatType}
     */
    public float getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.FLOAT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + Float.hashCode(value);
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FloatType)) {
            return false;
        }
        FloatType other = (FloatType) obj;
        return this.value == other.getValue();
    }
}

