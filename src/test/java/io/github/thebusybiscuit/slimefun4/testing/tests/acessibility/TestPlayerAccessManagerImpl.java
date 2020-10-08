package io.github.thebusybiscuit.slimefun4.testing.tests.acessibility;

import be.seeseemelk.mockbukkit.MockBukkit;
import com.google.gson.JsonElement;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessData;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessManager;
import io.github.thebusybiscuit.slimefun4.api.accessibility.ConcreteAccessLevel;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.accessibility.PlayerAccessDataImpl;
import io.github.thebusybiscuit.slimefun4.implementation.accessibility.PlayerAccessManagerImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class TestPlayerAccessManagerImpl {

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
        AccessManager accessManager = new PlayerAccessManagerImpl();
        Assertions.assertFalse(accessManager.hasDataFor(random));
        // Default is NONE
        Assertions.assertEquals(accessManager.getAccessLevel(random), ConcreteAccessLevel.NONE);
    }

    @Test
    @DisplayName("Test if added values are persisted correctly")
    public void testAdd() {
        UUID uuid = UUID.randomUUID();
        AccessManager accessManager = new PlayerAccessManagerImpl();
        AccessData<UUID> def = new PlayerAccessDataImpl();
        def.setAccessLevel(uuid, ConcreteAccessLevel.FULL);
        AccessData<UUID> accessData = accessManager.getOrRegisterAccessData(UUID.class, new PlayerAccessDataImpl());
        Assertions.assertNotEquals(accessData, def);
        Assertions.assertTrue(accessManager.getAccessData(UUID.class).isPresent());
        Assertions.assertFalse(accessManager.hasDataFor(uuid));
        accessData.setAccessLevel(uuid, ConcreteAccessLevel.FULL);
        Assertions.assertTrue(accessManager.hasDataFor(uuid));
        Assertions.assertEquals(accessManager.getAccessLevel(uuid), ConcreteAccessLevel.FULL);
    }

    @Test
    @DisplayName("Test if values are removed and if reset is functioning correctly")
    public void testReset() {
        UUID uuid = UUID.randomUUID();
        AccessManager accessManager = new PlayerAccessManagerImpl();
        AccessData<UUID> def = new PlayerAccessDataImpl();
        def.setAccessLevel(uuid, ConcreteAccessLevel.FULL);
        accessManager.getOrRegisterAccessData(UUID.class, def);
        Assertions.assertTrue(accessManager.hasDataFor(uuid));
        accessManager.clear();
        Assertions.assertFalse(accessManager.hasDataFor(uuid));
        def.setAccessLevel(uuid, ConcreteAccessLevel.FULL);
        accessManager.getOrRegisterAccessData(UUID.class, def);
        Assertions.assertTrue(accessManager.hasDataFor(uuid));
        def.setAccessLevel(uuid, null);
        Assertions.assertFalse(accessManager.hasDataFor(uuid));
    }

    @Test
    @DisplayName("Test if serialization and deserialization is working")
    public void testSerialization() {
        UUID uuid = UUID.randomUUID();
        AccessManager accessManager = new PlayerAccessManagerImpl();
        AccessData<UUID> accessData = accessManager.getOrRegisterAccessData(UUID.class, new PlayerAccessDataImpl());
        accessData.setAccessLevel(uuid, ConcreteAccessLevel.FULL);
        JsonElement serial = accessManager.saveToJsonElement();
        AccessManager reconstructed = SlimefunPlugin.getJsonDeserializationService().deserialize(PlayerAccessManagerImpl.class, serial).orElse(null);
        Assertions.assertEquals(accessManager, reconstructed);
    }

    @Test
    @DisplayName("Test if the equals method is correct")
    public void testEquals() {
        UUID uuid = UUID.randomUUID();
        AccessManager accessManager = new PlayerAccessManagerImpl();
        AccessData<UUID> accessData = accessManager.getOrRegisterAccessData(UUID.class, new PlayerAccessDataImpl());
        accessData.setAccessLevel(uuid, ConcreteAccessLevel.FULL);
        AccessManager secondary = new PlayerAccessManagerImpl();
        secondary.getOrRegisterAccessData(UUID.class, accessData);
        Assertions.assertEquals(accessManager, secondary);
    }

}
