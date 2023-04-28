package io.github.thebusybiscuit.slimefun4.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestExperienceUtils {
    @Test
    @DisplayName("Test ExperienceUtils.convertLevelToXp")
    void testConvertLevelToXp() {
        // case: level >= 32
        Assertions.assertEquals(5345F, ExperienceUtils.convertLevelToFloatExp(50));

        // case: level >= 17 && level <= 31
        Assertions.assertEquals(910F, ExperienceUtils.convertLevelToFloatExp(25));

        // case: level <= 16
        Assertions.assertEquals(160F, ExperienceUtils.convertLevelToFloatExp(10));
    }
}
