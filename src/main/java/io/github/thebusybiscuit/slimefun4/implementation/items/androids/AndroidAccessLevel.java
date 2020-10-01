package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessLevel;
import io.github.thebusybiscuit.slimefun4.api.accessibility.ConcreteAccessLevel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an access level for android operations specifically.
 */
public enum AndroidAccessLevel implements AccessLevel {

    /**
     * No access to the android
     */
    NONE(ConcreteAccessLevel.NONE),

    /**
     * Some access, able to open the android's UI
     */
    INTERACT(ConcreteAccessLevel.PARTIAL),

    /**
     * Full access
     */
    FULL(ConcreteAccessLevel.FULL);

    private final ConcreteAccessLevel equiv;

    AndroidAccessLevel(@Nonnull final ConcreteAccessLevel equiv) {
        this.equiv = equiv;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return name().toLowerCase();
    }

    @Nullable
    @Override
    public AccessLevel next() {
        final AccessLevel[] vals = values();
        if (ordinal() > vals.length - 1) {
            return null;
        }
        return vals[ordinal() + 1];
    }

    @Nullable
    @Override
    public AccessLevel previous() {
        final AccessLevel[] vals = values();
        if (ordinal() == 0) {
            return null;
        }
        return vals[ordinal() - 1];
    }

    @Nonnull
    @Override
    public ConcreteAccessLevel toConcreteAccessLevel() {
        return equiv;
    }

    @Override
    public int compare(final AccessLevel accessLevel) {
        if (accessLevel instanceof AndroidAccessLevel) {
            return compareTo((AndroidAccessLevel) accessLevel);
        }
        return equiv.compareTo(accessLevel.toConcreteAccessLevel());
    }
}
