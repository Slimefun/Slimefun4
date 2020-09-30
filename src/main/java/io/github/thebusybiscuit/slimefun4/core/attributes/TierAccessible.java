package io.github.thebusybiscuit.slimefun4.core.attributes;

import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessLevel;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessData;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessManager;

import javax.annotation.Nonnull;


/**
 * Represents anything with tiered access.
 *
 * @see AccessLevel
 * @see AccessData
 *
 * @author md5sha256
 */
public interface TierAccessible {

    @Nonnull
    AccessManager getAccessManager();

}
