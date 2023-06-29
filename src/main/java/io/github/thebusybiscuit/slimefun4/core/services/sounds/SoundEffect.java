package io.github.thebusybiscuit.slimefun4.core.services.sounds;

import java.util.Locale;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

/**
 * This enum holds references to all our sounds.
 * 
 * @author TheBusyBiscuit
 * @author J3fftw1
 *
 * @see SoundService
 * @see SoundConfiguration
 *
 */
public enum SoundEffect {

    ANCIENT_ALTAR_ITEM_CHECK_SOUND(Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 2F),
    ANCIENT_ALTAR_ITEM_DROP_SOUND(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1F, 1F),
    ANCIENT_ALTAR_ITEM_PICK_UP_SOUND(Sound.ENTITY_ITEM_PICKUP, 1F, 1F),
    ANCIENT_ALTAR_FINISH_SOUND(Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1F, 1F),
    ANCIENT_ALTAR_START_SOUND(Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1F, 1F),
    ANCIENT_PEDESTAL_ITEM_PLACE_SOUND(Sound.ENTITY_ITEM_PICKUP, 0.5F, 0.5F),
    ARMOR_FORGE_FINISH_SOUND(Sound.BLOCK_ANVIL_USE, 1F, 1F),
    ARMOR_FORGE_WORKING_SOUND(Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F),
    AUTO_CRAFTER_GUI_CLICK_SOUND(Sound.UI_BUTTON_CLICK, 1F, 1F),
    AUTO_CRAFTER_UPDATE_RECIPE(Sound.UI_BUTTON_CLICK, 1F, 1F),
    AUTOMATED_PANNING_MACHINE_FAIL_SOUND(Sound.ENTITY_ARMOR_STAND_BREAK, 1F, 1F),
    AUTOMATED_PANNING_MACHINE_SUCCESS_SOUND(Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F),
    BEE_BOOTS_FALL_SOUND(Sound.BLOCK_HONEY_BLOCK_FALL, 1F, 1F),
    BACKPACK_CLOSE_SOUND(Sound.ENTITY_HORSE_ARMOR, 1F, 1F),
    BACKPACK_OPEN_SOUND(Sound.ENTITY_HORSE_ARMOR, 1F, 1F),
    COMPOSTER_COMPOST_SOUND(Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F),
    COMPRESSOR_CRAFT_SOUND(Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F),
    COMPRESSOR_CRAFT_CONTRACT_SOUND(Sound.BLOCK_PISTON_CONTRACT, 1F, 1F),
    COMPRESSOR_CRAFT_EXTEND_SOUND(Sound.BLOCK_PISTON_EXTEND, 1F, 1F),
    COOLER_CONSUME_SOUND(Sound.ENTITY_GENERIC_DRINK, 1F, 1F),
    CRUCIBLE_ADD_WATER_SOUND(Sound.ENTITY_PLAYER_SPLASH, 1F, 1F),
    CRUCIBLE_ADD_LAVA_SOUND(Sound.BLOCK_LAVA_POP, 1F , 1F),
    CRUCIBLE_BLOCK_BREAK_SOUND(Sound.BLOCK_METAL_BREAK, 1F, 1F),
    CRUCIBLE_GENERATE_LIQUID_SOUND(Sound.BLOCK_LAVA_EXTINGUISH, 1F, 1F),
    CRUCIBLE_INTERACT_SOUND(Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F),
    CRUCIBLE_PLACE_LAVA_SOUND(Sound.BLOCK_LAVA_POP, 1F , 1F),
    CRUCIBLE_PLACE_WATER_SOUND(Sound.ENTITY_PLAYER_SPLASH, 1F, 1F),
    DEBUG_FISH_CLICK_SOUND(Sound.BLOCK_BAMBOO_PLACE, 1F, 1F),
    DIET_COOKIE_CONSUME_SOUND(Sound.ENTITY_GENERIC_EAT, 1F, 1F),
    ENCHANTMENT_RUNE_ADD_ENCHANT_SOUND(Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1F, 1F),
    ENDER_BACKPACK_OPEN_SOUND(Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F),
    ENHANCED_CRAFTING_TABLE_CRAFT_SOUND(Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1F, 1F),
    ELYTRA_CAP_IMPACT_SOUND(Sound.BLOCK_STONE_HIT, 1F, 1F),
    EXPLOSIVE_BOW_HIT_SOUND(Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F),
    EXPLOSIVE_TOOL_EXPLODE_SOUND(Sound.ENTITY_GENERIC_EXPLODE, 0.2F, 1F),
    FISHERMAN_ANDROID_FISHING_SOUND(Sound.ENTITY_PLAYER_SPLASH, 0.3F, 0.7F),
    FLASK_OF_KNOWLEDGE_FILLUP_SOUND(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0.5F),
    GUIDE_BUTTON_CLICK_SOUND(Sound.ITEM_BOOK_PAGE_TURN, 1F, 1F),
    GUIDE_CONTRIBUTORS_OPEN_SOUND(Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F),
    GUIDE_LANGUAGE_OPEN_SOUND(Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F),
    GUIDE_OPEN_SETTING_SOUND(Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F),
    GRIND_STONE_INTERACT_SOUND(Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1F, 1F),
    IGNITION_CHAMBER_USE_FLINT_AND_STEEL_SOUND(Sound.ENTITY_ITEM_BREAK, 1F, 1F),
    INFUSED_HOPPER_TELEPORT_SOUND(Sound.ENTITY_ENDERMAN_TELEPORT, 0.5F, 2F),
    INFUSED_MAGNET_TELEPORT_SOUND(Sound.ENTITY_ENDERMAN_TELEPORT, 0.25F, 0.9F),
    IRON_GOLEM_ASSEMBLER_ASSEMBLE_SOUND(Sound.ENTITY_IRON_GOLEM_REPAIR, 0.5F, 1F),
    JETBOOTS_THRUST_SOUND(Sound.ENTITY_TNT_PRIMED, 0.25F, 1F),
    JETPACK_THRUST_SOUND(Sound.ENTITY_GENERIC_EXPLODE, 0.25F, 1F),
    JUICER_USE_SOUND(Sound.ENTITY_PLAYER_SPLASH, 1F, 1F),
    LIMITED_USE_ITEM_BREAK_SOUND(Sound.ENTITY_ITEM_BREAK, 1F, 1F),
    MAGICAL_EYE_OF_ENDER_USE_SOUND(Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F),
    MAGIC_SUGAR_CONSUME_SOUND(Sound.ENTITY_GENERIC_EAT, 1F, 1F),
    MAGIC_WORKBENCH_FINISH_SOUND(Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F),
    MAGIC_WORKBENCH_START_ANIMATION_SOUND(Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1F, 1F),
    MINER_ANDROID_BLOCK_GENERATION_SOUND(Sound.BLOCK_FIRE_EXTINGUISH, 0.075F, 0.8F),
    MINING_TASK_SOUND(Sound.ENTITY_ARROW_HIT_PLAYER, 0.2F, 1F),
    ORE_WASHER_WASH_SOUND(Sound.ENTITY_PLAYER_SPLASH, 1F, 1F),
    PLAYER_RESEARCHING_SOUND(Sound.ENTITY_BAT_TAKEOFF, 0.7F, 1F),
    PORTABLE_DUSTBIN_OPEN_SOUND(Sound.BLOCK_ANVIL_LAND, 1F, 1F),
    PORTABLE_CRAFTER_OPEN_SOUND(Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1F, 1F),
    PRESSURE_CHAMBER_FINISH_SOUND(Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F),
    PRESSURE_CHAMBER_WORKING_SOUND(Sound.ENTITY_TNT_PRIMED, 1F, 1F),
    PROGRAMMABLE_ANDROID_SCRIPT_DOWNLOAD_SOUND(Sound.BLOCK_NOTE_BLOCK_HAT, 0.7F, 0.7F),
    SLIME_BOOTS_FALL_SOUND(Sound.BLOCK_SLIME_BLOCK_FALL, 1F, 1F),
    TELEPORTATION_MANAGER_OPEN_GUI(Sound.UI_BUTTON_CLICK, 1F, 1F),
    GPS_NETWORK_ADD_WAYPOINT(Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1F),
    GPS_NETWORK_CREATE_WAYPOINT(Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1F),
    GPS_NETWORK_OPEN_PANEL_SOUND(Sound.UI_BUTTON_CLICK, 1F, 1F),
    SMELTERY_CRAFT_SOUND(Sound.BLOCK_LAVA_POP, 1F, 1F),
    SOULBOUND_RUNE_RITUAL_SOUND(Sound.ENTITY_GENERIC_EXPLODE, 0.3F, 1F),
    SPLINT_CONSUME_SOUND(Sound.ENTITY_SKELETON_HURT, 1F, 1F),
    STOMPER_BOOTS_STOMP_SOUND(Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1F, 2F),
    TAPE_MEASURE_MEASURE_SOUND(Sound.ITEM_BOOK_PUT, 1, 0.7F),
    TOME_OF_KNOWLEDGE_USE_SOUND(Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F),
    TELEPORT_UPDATE_SOUND(Sound.BLOCK_BEACON_AMBIENT, 1F, 0.6F),
    TELEPORT_SOUND(Sound.BLOCK_BEACON_ACTIVATE, 1F, 1F),
    VAMPIRE_BLADE_HEALING_SOUND(Sound.ENTITY_ARROW_HIT_PLAYER, 0.7F, 0.7F),
    VANILLA_AUTO_CRAFTER_UPDATE_RECIPE_SOUND(Sound.UI_BUTTON_CLICK, 1F, 1F),
    VILLAGER_RUNE_TRANSFORM_SOUND(Sound.ENTITY_VILLAGER_CELEBRATE, 1F, 1.4F),
    VITAMINS_CONSUME_SOUND(Sound.ENTITY_GENERIC_EAT, 1F, 1F),
    WIND_STAFF_USE_SOUND(Sound.ENTITY_TNT_PRIMED, 1F, 1F);

    private final String defaultSound;
    private final float defaultVolume;
    private final float defaultPitch;

    SoundEffect(@Nonnull String sound, float volume, float pitch) {
        Preconditions.checkNotNull(sound, "The Sound id cannot be null!");
        Preconditions.checkArgument(volume >= 0, "The volume cannot be a negative number.");
        Preconditions.checkArgument(pitch >= 0.5, "A pitch below 0.5 has no effect on the sound.");

        this.defaultSound = sound;
        this.defaultVolume = volume;
        this.defaultPitch = pitch;
    }

    SoundEffect(@Nonnull Sound sound, float volume, float pitch) {
        Preconditions.checkNotNull(sound, "The Sound id cannot be null!");
        Preconditions.checkArgument(volume >= 0, "The volume cannot be a negative number.");
        Preconditions.checkArgument(pitch >= 0.5, "A pitch below 0.5 has no effect on the sound.");

        this.defaultSound = sound.getKey().getKey();
        this.defaultVolume = volume;
        this.defaultPitch = pitch;
    }

    private @Nullable SoundConfiguration getConfiguration() {
        SoundConfiguration config = Slimefun.getSoundService().getConfiguration(this);

        if (config == null) {
            // This should not happen. But if it does... send a warning
            Slimefun.logger().log(Level.WARNING, "Could not find any sound configuration for: {0}", name());
        }

        return config;
    }

    /**
     * This method will play this {@link SoundEffect} only to the given {@link Player} using the
     * eye {@link Location} of the {@link Player} and the {@link SoundCategory} {@code PLAYERS}.
     *
     * @param player The {@link Player} which to play the {@link Sound} to.
     */
    public void playFor(@Nonnull Player player) {
        Preconditions.checkNotNull(player, "Cannot play sounds to a Player that is null!");
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
     * @param loc      The {@link Location} at which to play the {@link SoundEffect}.
     * @param category The {@link SoundCategory} that should be used.
     */
    public void playAt(@Nonnull Location loc, @Nonnull SoundCategory category) {
        Preconditions.checkNotNull(loc, "The location should not be null.");
        SoundConfiguration config = getConfiguration();

        if (config != null && loc.getWorld() != null) {
            loc.getWorld().playSound(loc, config.getSoundId(), category, config.getVolume(), config.getPitch());
        }
    }

    /**
     * This method will play this {@link SoundEffect} at the {@link Location} of the given {@link Block},
     * the used {@link SoundCategory} will be {@code BLOCKS}.
     *
     * @param block The {@link Block} at which to play the {@link SoundEffect}
     */
    public void playAt(@Nonnull Block block) {
        Preconditions.checkNotNull(block, "The block cannot be null.");
        playAt(block.getLocation(), SoundCategory.BLOCKS);
    }

    /**
     * This returns the default sound id.
     *
     * @return The default sound id.
     */
    public @Nonnull String getDefaultSoundId() {
        return defaultSound;
    }

    /**
     * This returns the default volume.
     *
     * @return The default volume.
     */
    public float getDefaultVolume() {
        return defaultVolume;
    }

    /**
     * This returns the default pitch.
     *
     * @return The default pitch.
     */
    public float getDefaultPitch() {
        return defaultPitch;
    }
}
