package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.api.exceptions.TagMisconfigurationException;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

class TestSlimefunTags {

    @BeforeAll
    public static void load() {
        ServerMock server = MockBukkit.mock();
        MockBukkit.load(SlimefunPlugin.class);
        TestUtilities.registerDefaultTags(server);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test for Exceptions with Slimefun Tags")
    void testTags() {
        for (SlimefunTag tag : SlimefunTag.values()) {
            Assertions.assertDoesNotThrow(tag::reload);
        }
    }

    @Test
    @DisplayName("Test for infinite loops with Slimefun Tags")
    void testForInfiniteLoops() throws TagMisconfigurationException {
        for (SlimefunTag tag : SlimefunTag.values()) {
            tag.reload();
        }

        for (SlimefunTag tag : SlimefunTag.values()) {
            assertNotCyclic(tag);
        }
    }

    private void assertNotCyclic(@Nonnull SlimefunTag tag) {
        Set<SlimefunTag> visiting = new HashSet<>();
        Set<SlimefunTag> visited = new HashSet<>();

        if (isCyclic(visiting, visited, tag)) {
            System.out.println("Currently visiting: " + visiting);
            System.out.println("Previously visited" + visiting);
            Assertions.fail("Tag '" + tag.getKey() + "' is cyclic!");
        }
    }

    @ParametersAreNonnullByDefault
    private boolean isCyclic(Set<SlimefunTag> visiting, Set<SlimefunTag> visited, SlimefunTag tag) {
        visiting.add(tag);

        for (Tag<Material> sub : tag.getSubTags()) {
            if (sub instanceof SlimefunTag) {
                if (visiting.contains(sub)) {
                    return true;
                }
                else if (!visited.contains(sub) && isCyclic(visiting, visited, (SlimefunTag) sub)) {
                    return true;
                }
            }
        }

        visiting.remove(tag);
        visited.add(tag);
        return false;
    }

}
