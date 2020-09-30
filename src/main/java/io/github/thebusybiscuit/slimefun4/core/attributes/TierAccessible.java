package io.github.thebusybiscuit.slimefun4.core.attributes;

import io.github.thebusybiscuit.slimefun4.core.accessibility.AccessData;
import io.github.thebusybiscuit.slimefun4.core.accessibility.AccessManager;

import javax.annotation.Nonnull;


/**
 * Represents anything with tiered access.
 *
 * @see io.github.thebusybiscuit.slimefun4.core.accessibility.AccessLevel
 * @see AccessData
 *
 * @author md5sha256
 */
public interface TierAccessible {

    @Nonnull
    AccessManager getAccessManager();

}
