package io.github.thebusybiscuit.slimefun4.api.accessibility;

import com.google.gson.JsonElement;
import io.github.thebusybiscuit.slimefun4.core.services.JsonDeserializationService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Concrete access levels are defined to be constant and always present.
 * These levels should be used a fallback for when fine-grained access
 * processing is not needed or unavailable.
 *
 * @author md5sha256
 */
public enum ConcreteAccessLevel implements AccessLevel {

    /**
     * Represents an access level where no privileges are allocated.
     */
    NONE,

    /**
     * Represents an access level where some privileges are allocated.
     */
    PARTIAL,

    /**
     * Represents an access level where all privileges are allocated.
     */
    FULL;

    @Nonnull
    @Override
    public String getDisplayName() {
        return toString();
    }

    @Nullable
    @Override
    public AccessLevel next() {
        return this == NONE ? FULL : null;
    }

    @Nullable
    @Override
    public AccessLevel previous() {
        return this == FULL ? NONE : null;
    }

    @Nonnull
    @Override
    public ConcreteAccessLevel toConcreteAccessLevel() {
        return this;
    }

    @Override
    public int compare(@Nonnull final AccessLevel level) {
        if (level instanceof ConcreteAccessLevel) {
            return this.compareTo(((ConcreteAccessLevel) level));
        } else {
            return this.compareTo(level.toConcreteAccessLevel());
        }
    }

    @Override
    @Nonnull
    public JsonElement saveToJsonElement() {
        return JsonDeserializationService.serializeEnum(this);
    }

    @Override
    @Nonnull
    public String saveToString() {
        return saveToJsonElement().toString();
    }
}
