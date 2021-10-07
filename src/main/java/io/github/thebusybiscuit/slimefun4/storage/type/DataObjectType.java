package io.github.thebusybiscuit.slimefun4.storage.type;

import io.github.thebusybiscuit.slimefun4.storage.DataObject;

import javax.annotation.Nonnull;
import java.util.Objects;

public class DataObjectType implements Type {

    private final DataObject value;

    public DataObjectType(DataObject value) {
        this.value = value;
    }

    /**
     * Get the {@link DataObject} stored in this {@link DataObjectType}
     *
     * @return The {@link DataObject} stored in this {@link DataObjectType}
     */
    public DataObject getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.OBJECT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + value.hashCode();
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataObjectType)) {
            return false;
        }
        DataObjectType other = (DataObjectType) obj;
        return Objects.equals(this.value, other.getValue());
    }
}

