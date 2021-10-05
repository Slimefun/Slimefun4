package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

public class IntType implements Type {

    private final int value;

    public IntType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.INT;
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
        if (!(obj instanceof IntType)) {
            return false;
        }
        IntType other = (IntType) obj;
        return this.value == other.getValue();
    }
}
