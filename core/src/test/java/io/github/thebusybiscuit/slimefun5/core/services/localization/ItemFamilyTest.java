package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Verifies the item-family resolver: an id key containing {@code %MOB%} becomes a template that any
 * concrete id resolves through, with the captured segment humanized into {@code %mob%}. This is what lets
 * an addon localize a whole family of runtime-generated per-mob items (e.g. SoulJars) from one entry.
 */
class ItemFamilyTest {

    private static final String YAML = String.join("\n",
        "SOUL_JAR:",
        "  name: '&bSoul Jar &7(Empty)'",
        "'%MOB%_SOUL_JAR':",
        "  name: '&cSoul Jar &7(%mob%)'",
        "'FILLED_%MOB%_SOUL_JAR':",
        "  name: '&cFilled Soul Jar &7(%mob%)'",
        "'%MOB%_BROKEN_SPAWNER':",
        "  name: '&cBroken Spawner &7(%mob%)'");

    private static ItemTranslationService load() {
        ItemTranslationService service = new ItemTranslationService();
        service.loadTranslationsForTest("en", new ByteArrayInputStream(YAML.getBytes(StandardCharsets.UTF_8)));
        return service;
    }

    @Test
    @DisplayName("An exact id still resolves to its own entry (not a family)")
    void exactWins() {
        Assertions.assertEquals("&bSoul Jar &7(Empty)", load().resolveNameForTest("en", "SOUL_JAR"));
    }

    @Test
    @DisplayName("A per-mob id resolves through the family with the humanized mob name")
    void familyResolves() {
        Assertions.assertEquals("&cSoul Jar &7(Zombie)", load().resolveNameForTest("en", "ZOMBIE_SOUL_JAR"));
    }

    @Test
    @DisplayName("Multi-word mob names are humanized (ZOMBIE_PIGMAN -> Zombie Pigman)")
    void humanizesMultiWord() {
        Assertions.assertEquals("&cSoul Jar &7(Zombie Pigman)", load().resolveNameForTest("en", "ZOMBIE_PIGMAN_SOUL_JAR"));
    }

    @Test
    @DisplayName("A more specific family wins (FILLED_%MOB%_SOUL_JAR over %MOB%_SOUL_JAR)")
    void mostSpecificFamilyWins() {
        Assertions.assertEquals("&cFilled Soul Jar &7(Zombie)", load().resolveNameForTest("en", "FILLED_ZOMBIE_SOUL_JAR"));
    }

    @Test
    @DisplayName("A distinct family resolves independently")
    void spawnerFamily() {
        Assertions.assertEquals("&cBroken Spawner &7(Skeleton)", load().resolveNameForTest("en", "SKELETON_BROKEN_SPAWNER"));
    }

    @Test
    @DisplayName("An id matching no family and no exact entry resolves to null")
    void noMatchIsNull() {
        Assertions.assertNull(load().resolveNameForTest("en", "SOME_RANDOM_ITEM"));
    }
}
