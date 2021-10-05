package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;
import java.util.Objects;

public class StringType implements Type {

    private final String value;

    public StringType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.STRING;
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
        if (!(obj instanceof StringType)) {
            return false;
        }
        StringType other = (StringType) obj;
        return Objects.equals(this.value, other.getValue());
    }
}
