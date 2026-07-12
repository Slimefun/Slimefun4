package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link AddonDetailMenu#formatVersion(String, boolean)} strips the orchestrator's
 * "-UNOFFICIAL"/"-EXPERIMENTAL" build suffix and surfaces the short commit sha (if stamped)
 * in parens, so the guide's addon-detail tile never shows a stale released version for a
 * from-source build (see AddonInstaller.isUnofficialBuild).
 */
class AddonVersionFormatTest {

    @Test
    void unofficialWithShaShowsBaseAndShortSha() {
        Assertions.assertEquals("1.1.3.6 (e4ff08d)", AddonDetailMenu.formatVersion("1.1.3.6-UNOFFICIAL-e4ff08d", true));
    }

    @Test
    void unofficialWithoutShaShowsBaseOnly() {
        Assertions.assertEquals("1.1.3.6", AddonDetailMenu.formatVersion("1.1.3.6-UNOFFICIAL", true));
    }

    @Test
    void experimentalWithShaShowsBaseAndShortSha() {
        Assertions.assertEquals("1.1.3.6 (abcdef1)", AddonDetailMenu.formatVersion("1.1.3.6-EXPERIMENTAL-abcdef1", true));
    }

    @Test
    void releaseVersionIsUnchanged() {
        Assertions.assertEquals("1.1.3.6", AddonDetailMenu.formatVersion("1.1.3.6", false));
    }

    @Test
    void longShaIsTruncatedToSeven() {
        Assertions.assertEquals("2.0 (1234567)", AddonDetailMenu.formatVersion("2.0-UNOFFICIAL-1234567abcdef", true));
    }

    @Test
    void nullVersionReturnsEmptyString() {
        Assertions.assertEquals("", AddonDetailMenu.formatVersion(null, true));
    }
}
