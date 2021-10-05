package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

public class BooleanType implements Type {

    private final boolean value;

    public BooleanType(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.BOOLEAN;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + (value ? 1 : 0);
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BooleanType)) {
            return false;
        }
        BooleanType other = (BooleanType) obj;
        return this.value == other.getValue();
    }
}
