package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class LongArrayType implements Type {

    private final long[] value;

    public LongArrayType(long[] value) {
        this.value = value;
    }

    public long[] getValue() {
        return value;
    }

    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.LONG_ARRAY;
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
        if (!(obj instanceof LongArrayType)) {
            return false;
        }
        LongArrayType other = (LongArrayType) obj;
        return Arrays.equals(this.value, other.getValue());
    }
}
