package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;

class TestHeadTextures {

    @Test
    @DisplayName("Test if the HeadTexture enum contains any duplicates")
    void testForDuplicates() {
        Set<String> textures = new HashSet<>();

        for (HeadTexture head : HeadTexture.valuesCache) {
            String texture = head.getTexture();
            Assertions.assertNotNull(texture);

            // This will fail if a duplicate is found
            Assertions.assertTrue(textures.add(texture));
        }
    }

}
