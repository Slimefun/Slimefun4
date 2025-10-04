package io.github.thebusybiscuit.slimefun4.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedEnchantment;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedEntityType;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedPotionEffectType;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedPotionType;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedItemFlag;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedParticle;
import org.bukkit.Particle;

class TestVersionedCompatibility {

    @Test
    void testNewEntityTypes() {
        assertNotNull(VersionedEntityType.ARMADILLO);
        assertNotNull(VersionedEntityType.BOGGED);
        assertNotNull(VersionedEntityType.BREEZE);
        assertNotNull(VersionedEntityType.WIND_CHARGE);
        assertNotNull(VersionedEntityType.CREAKING);
    }

    @Test
    void testNewEnchantments() {
        assertNotNull(VersionedEnchantment.DENSITY);
        assertNotNull(VersionedEnchantment.BREACH);
        assertNotNull(VersionedEnchantment.WIND_BURST);
    }

    @Test
    void testNewPotionEffects() {
        assertNotNull(VersionedPotionEffectType.OOZING);
        assertNotNull(VersionedPotionEffectType.WEAVING);
        assertNotNull(VersionedPotionEffectType.WIND_CHARGED);
        assertNotNull(VersionedPotionEffectType.INFESTED);
    }

    @Test
    void testNewPotionTypes() {
        assertNotNull(VersionedPotionType.OOZING);
        assertNotNull(VersionedPotionType.WEAVING);
        assertNotNull(VersionedPotionType.WIND_CHARGED);
        assertNotNull(VersionedPotionType.INFESTED);
    }

    @Test
    void testNewItemFlag() {
        assertNotNull(VersionedItemFlag.HIDE_TOOLTIP);
    }

    @Test
    void testNewParticles() {
        assertParticle("BREEZE_WIND", VersionedParticle.BREEZE_WIND);
        assertParticle("TRIAL_SPAWNER_DETECTION", VersionedParticle.TRIAL_SPAWNER_DETECTION);
        assertParticle("TRIAL_SPAWNER_DETECTION_OMINOUS", VersionedParticle.TRIAL_SPAWNER_DETECTION_OMINOUS);
        assertParticle("TRIAL_SPAWNER_EJECTION", VersionedParticle.TRIAL_SPAWNER_EJECTION);
        assertParticle("TRIAL_SPAWNER_EJECTION_OMINOUS", VersionedParticle.TRIAL_SPAWNER_EJECTION_OMINOUS);
        assertParticle("TRIAL_SPAWNER_SMOKE", VersionedParticle.TRIAL_SPAWNER_SMOKE);
        assertParticle("TRIAL_SPAWNER_SMOKE_OMINOUS", VersionedParticle.TRIAL_SPAWNER_SMOKE_OMINOUS);
        assertParticle("GUST", VersionedParticle.GUST);
        assertParticle("SMALL_GUST", VersionedParticle.SMALL_GUST);
        assertParticle("GUST_EMITTER_LARGE", VersionedParticle.GUST_EMITTER_LARGE);
        assertParticle("GUST_EMITTER_SMALL", VersionedParticle.GUST_EMITTER_SMALL);
    }

    private void assertParticle(String field, Particle particle) {
        try {
            Particle.class.getDeclaredField(field);
            assertNotNull(particle);
        } catch (NoSuchFieldException e) {
            assertNull(particle);
        }
    }
}
