package io.github.thebusybiscuit.slimefun4.core.debug;

import java.text.MessageFormat;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestDebugLogging {

    private final String testCase = "unit-test";
    private String lastLogMessage = null;

    @BeforeEach
    void load() {
        MockBukkit.mock();
        MockBukkit.load(Slimefun.class);

        // Attach a custom handler to catch logged messages
        Slimefun.logger().addHandler(new Handler() {

            @Override
            public void publish(@Nonnull LogRecord record) {
                // We need to apply formatting too
                lastLogMessage = MessageFormat.format(record.getMessage(), record.getParameters());
            }

            @Override
            public void flush() {}

            @Override
            public void close() throws SecurityException {}

        });
    }

    @AfterEach
    void unload() {
        MockBukkit.unmock();

        // Clean up afterwards
        Debug.setTestCase(null);
    }

    @Test
    @DisplayName("Test logging with no test case")
    void testLoggingNoTestCase() {
        Debug.setTestCase(null);

        String msg = "Ramen is delicious";
        Debug.log(testCase, msg);

        Assertions.assertNull(lastLogMessage);
    }

    @Test
    @DisplayName("Test logging with different test case")
    void testLoggingWithDifferentTestCase() {
        Debug.setTestCase("different-test-case");

        String msg = "Ramen is delicious";
        Debug.log(testCase, msg);

        Assertions.assertNull(lastLogMessage);
    }

    @Test
    @DisplayName("Test log message")
    void testMessage() {
        Debug.setTestCase(testCase);
        Assertions.assertEquals(testCase, Debug.getTestCase());

        String msg = "Ramen is delicious";
        Debug.log(testCase, msg);

        Assertions.assertNotNull(lastLogMessage);
        Assertions.assertTrue(lastLogMessage.endsWith(msg));
    }

    @Test
    @DisplayName("Test log message with single parameter")
    void testMessageWithSingleParam() {
        Debug.setTestCase(testCase);
        Assertions.assertEquals(testCase, Debug.getTestCase());

        String pattern = "{} is delicious";
        String result = "Sushi is delicious";
        Debug.log(testCase, pattern, "Sushi");

        Assertions.assertNotNull(lastLogMessage);
        Assertions.assertTrue(lastLogMessage.endsWith(result));
    }

    @Test
    @DisplayName("Test log message with multiple parameters")
    void testMessageWithParams() {
        Debug.setTestCase(testCase);
        Assertions.assertEquals(testCase, Debug.getTestCase());

        String pattern = "{} is delicious and {} likes it!";
        String result = "Sushi is delicious and TheBusyBiscuit likes it!";
        Debug.log(testCase, pattern, "Sushi", "TheBusyBiscuit");

        Assertions.assertNotNull(lastLogMessage);
        Assertions.assertTrue(lastLogMessage.endsWith(result));
    }

    @Test
    @DisplayName("Test log message with lookalike pattern")
    void testMessageAwkwardPattern() {
        Debug.setTestCase(testCase);
        Assertions.assertEquals(testCase, Debug.getTestCase());

        /*
         * If our formatter trips up, the test will fail
         * with an IndexOutOfBoundsException.
         * Normally, this should pass though.
         */
        String msg = "{Ramen} is} {delicious }{";
        Debug.log(testCase, msg);

        Assertions.assertNotNull(lastLogMessage);
        Assertions.assertTrue(lastLogMessage.endsWith(msg));
    }

}
