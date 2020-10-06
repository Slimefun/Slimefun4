package io.github.thebusybiscuit.slimefun4.testing.tests.services;

import be.seeseemelk.mockbukkit.MockBukkit;
import com.google.gson.JsonElement;
import io.github.thebusybiscuit.slimefun4.core.services.JsonDeserializationService;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

public class TestJsonDeserializationService {

    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test if default enum serialization and deserialization is persistent and consistent")
    public void testDefaultEnumDeserialization() {
        JsonDeserializationService service = new JsonDeserializationService();
        final String data = JsonDeserializationService.serializeEnum(TestValues.ONE).toString();
        final Optional<TestValues> optional = service.deserialize(TestValues.class, data);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(optional.get(), TestValues.ONE);
    }

    @Test
    @DisplayName("Test if default enum deserialization is correctly overridden")
    public void testOverridingEnumDeserialization() {
        JsonDeserializationService service = new JsonDeserializationService();
        service.registerDeserialization(TestValues.class, TestValues::deserialize);
        final JsonElement element = TestValues.serialize(TestValues.ONE);
        final Optional<TestValues> optional = service.deserialize(TestValues.class, element.toString());
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(optional.get(), TestValues.ONE.faked());
    }

    @Test
    @DisplayName("Test if deserialization registration and deregistration to the service is correct")
    public void testDeserializationRegistration() {
        JsonDeserializationService service = new JsonDeserializationService();
        Assertions.assertFalse(service.isDeserializationRegistered(TestValues.class));
        service.registerDeserialization(TestValues.class, TestValues::deserialize);
        Assertions.assertTrue(service.isDeserializationRegistered(TestValues.class));
        service.unregisterDeserialization(TestValues.class);
        Assertions.assertFalse(service.isDeserializationRegistered(TestValues.class));
    }

    private enum TestValues {

        ONE,
        TWO;

        private TestValues faked() {
            if (this == ONE)
                return TWO;
            return ONE;
        }

        public static JsonElement serialize(TestValues values) {
            return JsonDeserializationService.serializeEnum(values.faked());
        }

        public static TestValues deserialize(final JsonElement element) {
            final Enum<?> deserialized = JsonDeserializationService.deserializeEnum(element);
            if (deserialized instanceof TestValues) {
                return ((TestValues) deserialized).faked();
            }
            return null;
        }

    }

}
