package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.utils.CustomHeadTexture;

public class TestHeadTextures {

    @Test
    public void testForDuplicates() {
        Set<String> textures = new HashSet<>();

        for (CustomHeadTexture head : CustomHeadTexture.values()) {
            String texture = head.getTexture();
            Assertions.assertNotNull(texture);

            // This will fail if a duplicate is found
            Assertions.assertTrue(textures.add(texture));
        }
    }

}
