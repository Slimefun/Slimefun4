package io.github.thebusybiscuit.slimefun4.testing.tests.acessibility;

import be.seeseemelk.mockbukkit.MockBukkit;
import com.google.gson.JsonElement;
import io.github.thebusybiscuit.slimefun4.api.accessibility.ConcreteAccessLevel;
import io.github.thebusybiscuit.slimefun4.api.accessibility.PlayerAccessData;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.accessibility.PlayerAccessDataImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class TestPlayerAccessDataImpl {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(SlimefunPlugin.class);
        SlimefunPlugin.getJsonDeserializationService().registerDeserialization(PlayerAccessDataImpl.class, PlayerAccessDataImpl::new);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test if default values for undefined behaviour are correct")
    public void testDefaults() {
        UUID random = UUID.randomUUID();
        PlayerAccessData accessData = new PlayerAccessDataImpl();
        Assertions.assertFalse(accessData.hasDataFor(random));
        // Default is NONE
        Assertions.assertEquals(accessData.getAccessLevel(random), ConcreteAccessLevel.NONE);
    }

    @Test
    @DisplayName("Test if added values are persisted correctly")
    public void testAdd() {
        UUID uuid = UUID.randomUUID();
        PlayerAccessData accessData = new PlayerAccessDataImpl();
        accessData.setAccessLevel(uuid, ConcreteAccessLevel.PARTIAL);
        Assertions.assertEquals(accessData.getAccessLevel(uuid), ConcreteAccessLevel.PARTIAL);
        Assertions.assertTrue(accessData.hasDataFor(uuid));
        accessData.setAccessLevel(uuid, ConcreteAccessLevel.NONE);
        Assertions.assertTrue(accessData.hasDataFor(uuid));
    }

    @Test
    @DisplayName("Test if added values are removed correctly")
    public void testRemove() {
        UUID uuid = UUID.randomUUID();
        PlayerAccessData accessData = new PlayerAccessDataImpl();
        accessData.setAccessLevel(uuid, ConcreteAccessLevel.PARTIAL);
        Assertions.assertEquals(accessData.getAccessLevel(uuid), ConcreteAccessLevel.PARTIAL);
        Assertions.assertTrue(accessData.hasDataFor(uuid));
        accessData.setAccessLevel(uuid, null);
        Assertions.assertFalse(accessData.hasDataFor(uuid));
    }

    @Test
    @DisplayName("Test if serialization and deserialization is working")
    public void testSerialization() {
        UUID uuid = UUID.randomUUID();
        PlayerAccessData accessData = new PlayerAccessDataImpl();
        accessData.setAccessLevel(uuid, ConcreteAccessLevel.PARTIAL);
        JsonElement serial = accessData.saveToJsonElement();
        PlayerAccessData reconstructed = SlimefunPlugin.getJsonDeserializationService().deserialize(PlayerAccessDataImpl.class, serial).orElse(null);
        Assertions.assertEquals(accessData, reconstructed);
    }

    @Test
    @DisplayName("Test if the equals method is correct")
    public void testEquals() {
        UUID uuid = UUID.randomUUID();
        PlayerAccessData accessData = new PlayerAccessDataImpl();
        accessData.setAccessLevel(uuid, ConcreteAccessLevel.PARTIAL);
        PlayerAccessData secondary = new PlayerAccessDataImpl();
        secondary.setAccessLevel(uuid, ConcreteAccessLevel.PARTIAL);
        Assertions.assertEquals(accessData, secondary);
    }

}
