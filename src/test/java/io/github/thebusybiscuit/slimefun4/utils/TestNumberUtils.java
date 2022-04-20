package io.github.thebusybiscuit.slimefun4.utils;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        LocalDateTime current = LocalDateTime.now();

        LocalDateTime a = current.minusDays(1);
        Assertions.assertEquals("1d", NumberUtils.getElapsedTime(current, a));

        LocalDateTime b = current.minusHours(25);
        Assertions.assertEquals("1d 1h", NumberUtils.getElapsedTime(current, b));

        LocalDateTime c = current.minusHours(1);
        Assertions.assertEquals("1h", NumberUtils.getElapsedTime(current, c));

        LocalDateTime d = current.minusMinutes(12);
        Assertions.assertEquals("< 1h", NumberUtils.getElapsedTime(current, d));
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

    @Test
    @DisplayName("Test compact decimals")
    void testCompactDecimals() {
        Assertions.assertEquals("-40.2", NumberUtils.getCompactDouble(-40.2));
        Assertions.assertEquals("1.23", NumberUtils.getCompactDouble(1.234546));
        Assertions.assertEquals("999", NumberUtils.getCompactDouble(999.0));
        Assertions.assertEquals("1K", NumberUtils.getCompactDouble(1000.0));
        Assertions.assertEquals("2.5K", NumberUtils.getCompactDouble(2500.0));
        Assertions.assertEquals("720K", NumberUtils.getCompactDouble(720000.0));
        Assertions.assertEquals("1M", NumberUtils.getCompactDouble(1000000.0));
        Assertions.assertEquals("40M", NumberUtils.getCompactDouble(40000000.0));
        Assertions.assertEquals("1B", NumberUtils.getCompactDouble(1000000000.0));
        Assertions.assertEquals("1.23B", NumberUtils.getCompactDouble(1230000000.0));
        Assertions.assertEquals("1T", NumberUtils.getCompactDouble(1000000000000.0));
        Assertions.assertEquals("1Q", NumberUtils.getCompactDouble(1000000000000000.0));
        Assertions.assertEquals("-2Q", NumberUtils.getCompactDouble(-2000000000000000.0));
    }

}
