package io.github.thebusybiscuit.slimefun4.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

/**
 * Drop-in replacement for the shaded dough {@code PlayerSkin} / {@code PlayerHead}
 * helpers. The dough library's {@code CustomGameProfile} extends Mojang's
 * {@code GameProfile}, which was made {@code final} in Minecraft 26.1.2,
 * so any call into dough.skins crashes with {@link IncompatibleClassChangeError}.
 * This utility talks to Paper's native {@link PlayerProfile} API instead.
 */
public final class PlayerSkinUtils {

    private static final Pattern URL_PATTERN = Pattern.compile("\"url\"\\s*:\\s*\"([^\"]+)\"");
    private static final UUID ZERO_UUID = new UUID(0L, 0L);
    private static final String TEXTURE_URL_PREFIX = "http://textures.minecraft.net/texture/";

    private PlayerSkinUtils() {}

    public static @Nonnull String hashToBase64(@Nonnull String hash) {
        String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + TEXTURE_URL_PREFIX + hash + "\"}}}";
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    private static @Nullable URL extractUrl(@Nonnull String base64) {
        try {
            String json = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
            Matcher m = URL_PATTERN.matcher(json);
            if (!m.find()) {
                return null;
            }
            return new URI(m.group(1)).toURL();
        } catch (IllegalArgumentException | URISyntaxException | java.net.MalformedURLException e) {
            return null;
        }
    }

    private static @Nonnull PlayerProfile buildProfile(@Nonnull UUID uuid, @Nonnull String base64) {
        PlayerProfile profile = Bukkit.createPlayerProfile(uuid);
        URL url = extractUrl(base64);
        if (url != null) {
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(url);
            profile.setTextures(textures);
        }
        return profile;
    }

    public static @Nonnull ItemStack getItemStackFromBase64(@Nonnull String base64) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwnerProfile(buildProfile(ZERO_UUID, base64));
            head.setItemMeta(meta);
        }
        return head;
    }

    public static @Nonnull ItemStack getItemStackFromHash(@Nonnull String hash) {
        return getItemStackFromBase64(hashToBase64(hash));
    }

    public static void setBlockSkinFromBase64(@Nonnull Block block, @Nonnull String base64, boolean sendUpdate) {
        if (!(block.getState() instanceof Skull skull)) {
            return;
        }
        skull.setOwnerProfile(buildProfile(ZERO_UUID, base64));
        skull.update(true, sendUpdate);
    }

    public static void setBlockSkinFromHash(@Nonnull Block block, @Nonnull UUID uuid, @Nonnull String hash, boolean sendUpdate) {
        if (!(block.getState() instanceof Skull skull)) {
            return;
        }
        skull.setOwnerProfile(buildProfile(uuid, hashToBase64(hash)));
        skull.update(true, sendUpdate);
    }
}
