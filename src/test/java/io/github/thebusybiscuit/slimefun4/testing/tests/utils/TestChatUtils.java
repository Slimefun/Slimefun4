package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;

public class TestChatUtils {

    @Test
    public void testHumanize() {
        String input = "TEST_STRING_COOL";
        String expected = "Test String Cool";
        Assertions.assertEquals(expected, ChatUtils.humanize(input));
    }

    @Test
    public void testChristmas() {
        String input = "Tis the season";
        String expected = ChatColors.color("&aT&ci&as&c &at&ch&ae&c &as&ce&aa&cs&ao&cn");
        Assertions.assertEquals(expected, ChatUtils.christmas(input));
    }

}
