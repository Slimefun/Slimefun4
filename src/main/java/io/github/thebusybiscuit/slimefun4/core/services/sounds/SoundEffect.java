package io.github.thebusybiscuit.slimefun4.core.services.sounds;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

/**
 * This enum holds references to all our sounds.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SoundService
 * @see SoundConfiguration
 *
 */
@SuppressWarnings("java:S1192") // Ignore warnings about re-used string literals.
public enum SoundEffect {

    DIET_COOKIE_CONSUME_SOUND("entity.generic.eat", 1F, 1F),
    ENDER_BACKPACK_OPEN_SOUND("entity.enderman.teleport", 1F, 1F),
    ELYTRA_CAP_IMPACT_SOUND("block.stone.hit", 1F, 1F),
    FISHERMAN_ANDROID_FISHING_SOUND("entity.player.splash", 0.3F, 0.7F),
    FLASK_OF_KNOWLEDGE_FILLUP_SOUND("entity.experience_orb.pickup", 1F, 0.5F),
    INFUSED_HOPPER_TELEPORT_SOUND("entity.enderman.teleport", 0.5F, 2F),
    INFUSED_MAGNET_TELEPORT_SOUND("entity.enderman.teleport", 0.25F, 0.9F),
    IRON_GOLEM_ASSEMBLER_ASSEMBLE_SOUND("entity.iron_golem.repair", 0.5F, 1F),
    JETBOOTS_THRUST_SOUND("entity.tnt.primed", 0.25F, 1F),
    JETPACK_THRUST_SOUND("entity.generic.explode", 0.25F, 1F),
    JUICER_USE_SOUND("entity.player.splash", 1F, 1F),
    MAGICAL_EYE_OF_ENDER_USE_SOUND("entity.enderman.teleport", 1F, 1F),
    MAGIC_SUGAR_CONSUME_SOUND("entity.generic.eat", 1F, 1F),
    PORTABLE_CRAFTER_OPEN_SOUND("block.wooden_button.click_on", 1F, 1F),
    SPLINT_CONSUME_SOUND("entity.skeleton.hurt", 1F, 1F),
    TOME_OF_KNOWLEDGE_USE_SOUND("entity.player.levelup", 1F, 1F),
    TRASH_CAN_OPEN_SOUND("block.anvil.land", 1F, 1F),
    VAMPIRE_BLADE_HEALING_SOUND("entity.arrow.hit_player", 0.7F, 0.7F),
    VILLAGER_RUNE_TRANSFORM_SOUND("entity.villager.celebrate", 1F, 1.4F),
    VITAMINS_CONSUME_SOUND("entity.generic.eat", 1F, 1F),
    WIND_STAFF_USE_SOUND("entity.tnt.primed", 1F, 1F);

    private final String defaultSound;
    private final float defaultVolume;
    private final float defaultPitch;

    SoundEffect(@Nonnull String sound, float volume, float pitch) {
        Validate.notNull(sound, "The Sound id cannot be null!");
        Validate.isTrue(volume >= 0, "The volume cannot be a negative number.");
        Validate.isTrue(pitch >= 0.5, "A pitch below 0.5 has no effect on the sound.");

        this.defaultSound = sound;
        this.defaultVolume = volume;
        this.defaultPitch = pitch;
    }

    @Nullable
    private SoundConfiguration getConfiguration() {
        SoundConfiguration config = SlimefunPlugin.getSoundService().getConfiguration(this);

        if (config == null) {
            // This should not happen. But if it does... send a warning
            SlimefunPlugin.logger().log(Level.WARNING, "Could not find any sound configuration for: {0}", name());
        }

        return config;
    }

    /**
     * This method will play this {@link SoundEffect} only to the given {@link Player} using the
     * eye {@link Location} of the {@link Player} and the {@link SoundCategory} {@code PLAYERS}.
     * 
     * @param player
     *            The {@link Player} which to play the {@link Sound} to.
     */
    public void playFor(@Nonnull Player player) {
        Validate.notNull(player, "Cannot play sounds to a Player that is null!");
        SoundConfiguration config = getConfiguration();

        if (config != null) {
            Location loc = player.getEyeLocation();
            player.playSound(loc, config.getSoundId(), SoundCategory.PLAYERS, config.getVolume(), config.getPitch());
        }
    }

    /**
     * This method will play this {@link SoundEffect} at the given {@link Location} using the
     * provided {@link SoundCategory}.
     * 
     * @param loc
     *            The {@link Location} at which to play the {@link SoundEffect}.
     * @param category
     *            The {@link SoundCategory} that should be used.
     */
    public void playAt(@Nonnull Location loc, @Nonnull SoundCategory category) {
        Validate.notNull(loc, "The location should not be null.");
        SoundConfiguration config = getConfiguration();

        if (config != null) {
            loc.getWorld().playSound(loc, config.getSoundId(), category, config.getVolume(), config.getPitch());
        }
    }

    /**
     * This method will play this {@link SoundEffect} at the {@link Location} of the given {@link Block},
     * the used {@link SoundCategory} will be {@code BLOCKS}.
     * 
     * @param block
     *            The {@link Block} at which to play the {@link SoundEffect}
     */
    public void playAt(@Nonnull Block block) {
        Validate.notNull(block, "The block cannot be null.");
        playAt(block.getLocation(), SoundCategory.BLOCKS);
    }

    /**
     * This returns the default sound id.
     * 
     * @return The default sound id.
     */
    @Nonnull
    String getDefaultSoundId() {
        return defaultSound;
    }

    /**
     * This returns the default volume.
     * 
     * @return The default volume.
     */
    float getDefaultVolume() {
        return defaultVolume;
    }

    /**
     * This returns the default pitch.
     * 
     * @return The default pitch.
     */
    float getDefaultPitch() {
        return defaultPitch;
    }

}
