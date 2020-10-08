package io.github.thebusybiscuit.slimefun4.api.accessibility;

import com.google.gson.JsonElement;
import io.github.thebusybiscuit.slimefun4.core.services.JsonDeserializationService;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Translators;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

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
    public String getRawDisplayName() {
        return toString();
    }

    @Nonnull
    @Override
    public String getDisplayName(@Nonnull Player player) {
        // TODO Translations?
        return toString();
    }

    @Nullable
    @Override
    public AccessLevel getNextLevel() {
        return this == NONE ? FULL : null;
    }

    @Nullable
    @Override
    public AccessLevel getPreviousLevel() {
        return this == FULL ? NONE : null;
    }

    @Nonnull
    @Override
    public ConcreteAccessLevel toConcreteAccessLevel() {
        return this;
    }

    @Override
    public int compare(@Nonnull AccessLevel level) {
        return this.compareTo(level.toConcreteAccessLevel());
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
