package io.github.thebusybiscuit.slimefun5.test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Regression test for the chunk-data and universal-inventory maps in SlimefunRegistry:
 * they are written on the main thread and iterated by the async autosave thread
 * ({@code AutoSavingService}), so a plain {@link java.util.HashMap} risks a
 * {@link java.util.ConcurrentModificationException} or a resize-loop hang. This only asserts the
 * maps are a thread-safe type - actual cross-thread races aren't deterministically unit-testable.
 */
class ThreadSafeStorageMapsTest {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("BlockStorage chunk map is thread-safe")
    void testChunksMapIsConcurrent() {
        Map<String, ?> chunks = Slimefun.getRegistry().getChunks();
        Assertions.assertTrue(chunks instanceof ConcurrentMap, "getChunks() must return a thread-safe Map (was " + chunks.getClass() + ")");
    }

    @Test
    @DisplayName("BlockStorage universal-inventory map is thread-safe")
    void testUniversalInventoriesMapIsConcurrent() {
        Map<String, ?> universalInventories = Slimefun.getRegistry().getUniversalInventories();
        Assertions.assertTrue(universalInventories instanceof ConcurrentMap, "getUniversalInventories() must return a thread-safe Map (was " + universalInventories.getClass() + ")");
    }

    @Test
    @DisplayName("Both storage maps concretely use ConcurrentHashMap")
    void testMapsAreConcurrentHashMap() {
        Assertions.assertEquals(ConcurrentHashMap.class, Slimefun.getRegistry().getChunks().getClass());
        Assertions.assertEquals(ConcurrentHashMap.class, Slimefun.getRegistry().getUniversalInventories().getClass());
    }
}
