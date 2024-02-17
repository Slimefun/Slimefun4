package io.github.thebusybiscuit.slimefun4.core.debug;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.core.services.AnalyticsService;

/**
 * Test cases in Slimefun. These are very useful for debugging why behavior is happening.
 * Server owners can enable these with {@code /sf debug <test-case>}
 *
 * @author WalshyDev
 */
public enum TestCase {

    /**
     * Cargo input testing. This will log what is going on with CARGO_INPUT_NODEs so that we can see what items are.
     * being checked and why it is comparing IDs or meta.
     * This is helpful for us to check into why input nodes are taking a while for servers.
     */
    CARGO_INPUT_TESTING,

    /**
     * Debug information regarding player profile loading, saving and handling.
     * This is an area we're currently changing quite a bit and this will help ensure we're doing it safely
     */
    PLAYER_PROFILE_DATA,

    /**
     * Debug information regarding our {@link AnalyticsService}.
     */
    ANALYTICS;

    public static final List<String> VALUES_LIST = Arrays.stream(values()).map(TestCase::toString).toList();

    TestCase() {}

    @Override
    public @Nonnull String toString() {
        return "slimefun_" + name().toLowerCase(Locale.ROOT);
    }
}
