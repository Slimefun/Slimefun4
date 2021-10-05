package io.github.thebusybiscuit.slimefun4.storage.type;

import io.github.thebusybiscuit.slimefun4.storage.DataObject;

import javax.annotation.Nonnull;
import java.util.Objects;

public class DataObjectType implements Type {

    private final DataObject value;

    public DataObjectType(DataObject value) {
        this.value = value;
    }

    public DataObject getValue() {
        return value;
    }

    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.OBJECT;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + value.hashCode();
        result = result * 59 + getTypeEnum().ordinal();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataObjectType)) {
            return false;
        }
        DataObjectType other = (DataObjectType) obj;
        return Objects.equals(this.value, other.getValue());
    }
}

