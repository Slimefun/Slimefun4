package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;

class TestNumberUtils {

    @Test
    @DisplayName("Test NumberUtils.clamp(...)")
    void testHumanize() {
        // Below minimum
        Assertions.assertEquals(2, NumberUtils.clamp(2, 0, 5));

        // Normal
        Assertions.assertEquals(3, NumberUtils.clamp(0, 3, 5));

        // Above maximum
        Assertions.assertEquals(20, NumberUtils.clamp(1, 100, 20));
    }

    @Test
    @DisplayName("Test elapsed time string")
    void testElapsedTime() {
        LocalDateTime start = LocalDateTime.now();

        LocalDateTime a = start.plusDays(1);
        Assertions.assertEquals("1d", NumberUtils.getElapsedTime(start, a));

        LocalDateTime b = start.plusHours(25);
        Assertions.assertEquals("1d 1h", NumberUtils.getElapsedTime(start, b));

        LocalDateTime c = start.plusHours(1);
        Assertions.assertEquals("1h", NumberUtils.getElapsedTime(start, c));

        LocalDateTime d = start.plusMinutes(12);
        Assertions.assertEquals("< 1h", NumberUtils.getElapsedTime(start, d));
    }

    @Test
    @DisplayName("Test Integer parsing")
    void testIntegerParsing() {
        Assertions.assertEquals(6, NumberUtils.getInt("6", 0));
        Assertions.assertEquals(12, NumberUtils.getInt("I am a String", 12));
    }

    @Test
    @DisplayName("Test nullable Long")
    void testNullableLong() {
        Assertions.assertEquals(10, NumberUtils.getLong(10L, 20L));
        Assertions.assertEquals(20, NumberUtils.getLong(null, 20L));
    }

    @Test
    @DisplayName("Test nullable Int")
    void testNullableInt() {
        Assertions.assertEquals(10, NumberUtils.getInt(10, 20));
        Assertions.assertEquals(20, NumberUtils.getInt((Integer) null, 20));
    }

    @Test
    @DisplayName("Test nullable Float")
    void testNullableFloat() {
        Assertions.assertEquals(10, NumberUtils.getFloat(10F, 20F));
        Assertions.assertEquals(20, NumberUtils.getFloat(null, 20F));
    }

    @Test
    @DisplayName("Test decimal rounding")
    void testRounding() {
        Assertions.assertEquals("5.25", NumberUtils.roundDecimalNumber(5.249999999999));
    }

}
