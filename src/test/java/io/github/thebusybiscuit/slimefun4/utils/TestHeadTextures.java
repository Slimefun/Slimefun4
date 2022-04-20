package io.github.thebusybiscuit.slimefun4.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class TestHeadTextures {

    @Test
    @DisplayName("Test if the HeadTexture enum contains any duplicates")
    void testForDuplicates() {
        Set<String> textures = new HashSet<>();

        for (HeadTexture head : HeadTexture.values()) {
            String texture = head.getTexture();
            Assertions.assertNotNull(texture);

            // This will fail if a duplicate is found
            Assertions.assertTrue(textures.add(texture));
        }
    }

}
