package io.github.thebusybiscuit.slimefun4.storage.type;

import io.github.bakedlibs.dough.blocks.ChunkPosition;
import io.github.thebusybiscuit.slimefun4.storage.DataObject;

import javax.annotation.Nonnull;

public class BooleanType implements Type {

    private final boolean value;

    public BooleanType(boolean value) {
        this.value = value;
    }

    /**
     * Get the boolean value stored in this {@link BooleanType}
     *
     * @return The boolean value stored in this {@link BooleanType}
     */
    public boolean getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.BOOLEAN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + (value ? 1 : 0);
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BooleanType)) {
            return false;
        }
        BooleanType other = (BooleanType) obj;
        return this.value == other.getValue();
    }
}
