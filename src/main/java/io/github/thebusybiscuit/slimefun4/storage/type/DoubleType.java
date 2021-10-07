package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

public class DoubleType implements Type {

    private final double value;

    public DoubleType(double value) {
        this.value = value;
    }

    /**
     * Get the double value stored in this {@link DoubleType}
     *
     * @return The double value stored in this {@link DoubleType}
     */
    public double getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.DOUBLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + Double.hashCode(value);
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DoubleType)) {
            return false;
        }
        DoubleType other = (DoubleType) obj;
        return this.value == other.getValue();
    }
}
