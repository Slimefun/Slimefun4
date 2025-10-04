package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemFlag;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class VersionedItemFlag {

    public static final ItemFlag HIDE_ADDITIONAL_TOOLTIP;
    public static final ItemFlag HIDE_TOOLTIP;
    public static final ItemFlag HIDE_VILLAGER_VARIANT;
    public static final ItemFlag HIDE_WEAPON;
    public static final ItemFlag HIDE_WOLF_COLLAR;
    public static final ItemFlag HIDE_WOLF_SOUND_VARIANT;
    public static final ItemFlag HIDE_WOLF_VARIANT;
    public static final ItemFlag HIDE_WRITABLE_BOOK_CONTENT;
    public static final ItemFlag HIDE_WRITTEN_BOOK_CONTENT;
    public static final ItemFlag HIDE_LLAMA_VARIANT;
    public static final ItemFlag HIDE_AXOLOTL_VARIANT;
    public static final ItemFlag HIDE_CAT_VARIANT;
    public static final ItemFlag HIDE_CAT_COLLAR;
    public static final ItemFlag HIDE_SHEEP_COLOR;
    public static final ItemFlag HIDE_SHULKER_COLOR;

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();

        HIDE_ADDITIONAL_TOOLTIP = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? ItemFlag.HIDE_ADDITIONAL_TOOLTIP
            : getKey("HIDE_POTION_EFFECTS");

        ItemFlag hideTooltip = version.isAtLeast(MinecraftVersion.MINECRAFT_1_21)
            ? getKey("HIDE_TOOLTIP")
            : null;
        HIDE_TOOLTIP = hideTooltip != null ? hideTooltip : HIDE_ADDITIONAL_TOOLTIP;

        HIDE_VILLAGER_VARIANT = getKey("HIDE_VILLAGER_VARIANT");
        HIDE_WEAPON = getKey("HIDE_WEAPON");
        HIDE_WOLF_COLLAR = getKey("HIDE_WOLF_COLLAR");
        HIDE_WOLF_SOUND_VARIANT = getKey("HIDE_WOLF_SOUND_VARIANT");
        HIDE_WOLF_VARIANT = getKey("HIDE_WOLF_VARIANT");
        HIDE_WRITABLE_BOOK_CONTENT = getKey("HIDE_WRITABLE_BOOK_CONTENT");
        HIDE_WRITTEN_BOOK_CONTENT = getKey("HIDE_WRITTEN_BOOK_CONTENT");
        HIDE_LLAMA_VARIANT = getKey("HIDE_LLAMA_VARIANT");
        HIDE_AXOLOTL_VARIANT = getKey("HIDE_AXOLOTL_VARIANT");
        HIDE_CAT_VARIANT = getKey("HIDE_CAT_VARIANT");
        HIDE_CAT_COLLAR = getKey("HIDE_CAT_COLLAR");
        HIDE_SHEEP_COLOR = getKey("HIDE_SHEEP_COLOR");
        HIDE_SHULKER_COLOR = getKey("HIDE_SHULKER_COLOR");
    }

    @Nullable
    private static ItemFlag getKey(@Nonnull String key) {
        try {
            Field field = ItemFlag.class.getDeclaredField(key);
            return (ItemFlag) field.get(null);
        } catch(Exception e) {
            return null;
        }
    }
}
