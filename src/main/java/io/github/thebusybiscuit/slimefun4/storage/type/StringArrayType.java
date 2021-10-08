package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class StringArrayType implements Type {

    private final String[] value;

    public StringArrayType(String[] value) {
        this.value = value;
    }

    /**
     * Get the string array stored in this {@link StringArrayType}
     *
     * @return The string array stored in this {@link StringArrayType}
     */
    public String[] getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.STRING_ARRAY;
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
        if (!(obj instanceof StringArrayType)) {
            return false;
        }
        StringArrayType other = (StringArrayType) obj;
        return Arrays.equals(this.value, other.getValue());
    }
}
