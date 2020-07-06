package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;

public class TestHeadTextures {

    @Test
    public void testForDuplicates() {
        Set<String> textures = new HashSet<>();

        for (HeadTexture head : HeadTexture.values()) {
            String texture = head.getTexture();
            Assertions.assertNotNull(texture);

            // This will fail if a duplicate is found
            Assertions.assertTrue(textures.add(texture));
        }
    }

}
