package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class IntArrayType implements Type {

    private final int[] value;

    public IntArrayType(int[] value) {
        this.value = value;
    }

    /**
     * Get the int array stored in this {@link IntArrayType}
     *
     * @return The int array stored in this {@link IntArrayType}
     */
    public int[] getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.INT_ARRAY;
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
        if (!(obj instanceof IntArrayType)) {
            return false;
        }
        IntArrayType other = (IntArrayType) obj;
        return Arrays.equals(this.value, other.getValue());
    }
}


