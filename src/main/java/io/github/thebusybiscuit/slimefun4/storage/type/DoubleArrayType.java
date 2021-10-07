package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class DoubleArrayType implements Type {

    private final double[] value;

    public DoubleArrayType(double[] value) {
        this.value = value;
    }

    /**
     * Get the double array stored in this {@link DoubleArrayType}
     *
     * @return The double array stored in this {@link DoubleArrayType}
     */
    public double[] getValue() {
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
        if (!(obj instanceof DoubleArrayType)) {
            return false;
        }
        DoubleArrayType other = (DoubleArrayType) obj;
        return Arrays.equals(this.value, other.getValue());
    }
}

