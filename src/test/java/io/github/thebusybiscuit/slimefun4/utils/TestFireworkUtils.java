package io.github.thebusybiscuit.slimefun4.utils;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;

class TestFireworkUtils {
    
    private static World world;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        world = new WorldMock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @MethodSource("getColors")
    @DisplayName("Test colored Fireworks")
    void testColoredFirework(Color color) {
        Location l = new Location(world, 50, 50, 50);
        Firework firework = FireworkUtils.createFirework(l, color);

        Assertions.assertEquals(l, firework.getLocation());
        Assertions.assertEquals(ChatColor.GREEN + "Slimefun Research", firework.getFireworkMeta().getDisplayName());

        List<FireworkEffect> effects = firework.getFireworkMeta().getEffects();
        Assertions.assertEquals(1, effects.size());
        Assertions.assertEquals(color, effects.get(0).getColors().get(0));
    }

    private static Stream<Arguments> getColors() {
        return Stream.of(Color.RED, Color.BLUE, Color.YELLOW, Color.fromRGB(100, 20, 100)).map(Arguments::of);
    }

}
