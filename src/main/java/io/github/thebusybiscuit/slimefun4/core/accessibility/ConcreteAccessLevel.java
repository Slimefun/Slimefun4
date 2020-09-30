package io.github.thebusybiscuit.slimefun4.core.accessibility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Concrete access levels are defined to be constant and always present.
 * These levels should be used a fallback for when fine-grained access
 * processing is not needed or unavailable.
 *
 * @author md5sha256
 */
public class ConcreteAccessLevel implements AccessLevel {

    /**
     * Represents an access level where no privileges are allocated.
     */
    public static final ConcreteAccessLevel NONE = new ConcreteAccessLevel("&cNone");

    /**
     * Represents an access level where all privileges are allocated.
     */
    public static final ConcreteAccessLevel FULL = new ConcreteAccessLevel("&bFull");

    private final String displayName;

    private ConcreteAccessLevel(final String displayName) {
        this.displayName = displayName;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return displayName;
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
    public int compareTo(@Nonnull final AccessLevel level) {
        if (level instanceof ConcreteAccessLevel) {
            return compare(this, (ConcreteAccessLevel) level);
        } else {
            return this.compareTo(level.toConcreteAccessLevel());
        }
    }

    public static int compare(@Nonnull ConcreteAccessLevel primary, @Nonnull ConcreteAccessLevel secondary) {
        if (primary == secondary) {
            return 0;
        }
        if (primary == NONE) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
