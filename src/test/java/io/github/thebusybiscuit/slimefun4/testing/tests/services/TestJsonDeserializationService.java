package io.github.thebusybiscuit.slimefun4.testing.tests.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.thebusybiscuit.slimefun4.core.services.JsonDeserializationService;
import org.bukkit.Material;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

public class TestJsonDeserializationService {

    @Test
    @DisplayName("Test if default enum serialization and deserialization is persistent and consistent")
    public void testDefaultEnumDeserialization() {
        JsonDeserializationService service = new JsonDeserializationService();
        final String data = JsonDeserializationService.serializeEnum(Material.AIR).toString();
        final Optional<Material> optional = service.deserialize(Material.class, data);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(optional.get(), Material.AIR);
    }

    @Test
    @DisplayName("Test if default enum deserialization is correctly overridden")
    public void testOverridingEnumDeserialization() {
        JsonDeserializationService service = new JsonDeserializationService();
        service.registerDeserialization(Material.class, this::deserialize);
        final JsonElement element = serialize(Material.CARROT);
        final Optional<Material> optional = service.deserialize(Material.class, element.toString());
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(optional.get(), Material.AIR);
    }

    @Test
    @DisplayName(
        "Test if deserialization registration and deregistration to the service is correct")
    public void testDeserializationRegistration() {
        JsonDeserializationService service = new JsonDeserializationService();
        Assertions.assertFalse(service.isDeserializationRegistered(Material.class));
        service.registerDeserialization(Material.class, this::deserialize);
        Assertions.assertTrue(service.isDeserializationRegistered(Material.class));
        service.unregisterDeserialization(Material.class);
        Assertions.assertFalse(service.isDeserializationRegistered(Material.class));
    }

    /**
     * Dummy serialization
     */
    private JsonObject serialize(final Material material) {
        return JsonDeserializationService.serializeEnum(material);
    }

    /**
     * Dummy deserialization to always return air.
     */
    private Material deserialize(final JsonElement element) {
        return JsonDeserializationService.deserializeEnum(element) == null ? null : Material.AIR;
    }

}
