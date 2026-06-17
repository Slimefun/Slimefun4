package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

/**
 * Java-8 universal port: applies head textures across versions. The player-profile API (1.18+) is
 * reached reflectively via {@link ProfileCompat} (so legacy servers simply skip the texture path), and
 * the HTTP lookups use Java 8's {@link HttpURLConnection} instead of the 11+ {@code java.net.http} client.
 */
public final class VersionedPlayerHead {

    private static final String PROFILE_NAME = "CS-CoreLib";

    private VersionedPlayerHead() {}

    public static @Nonnull ItemStack getItemStack(@Nonnull String base64) {
        // XMaterial#parseItem carries the legacy data value (SKULL_ITEM:3 = player head) on 1.8-1.12,
        // unlike parseMaterial() which drops it and would yield a skeleton skull (data 0).
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();

        if (item == null) {
            item = MaterialCompat.stack(XMaterial.PLAYER_HEAD);
        }

        SkullMeta meta = (SkullMeta) item.getItemMeta();

        applyTextureToMeta(meta, base64);

        item.setItemMeta(meta);
        return item;
    }

    public static void applyTextureToMeta(@Nonnull SkullMeta meta, @Nonnull String base64) {
        UUID uuid = UUID.nameUUIDFromBytes(base64.getBytes(StandardCharsets.UTF_8));
        PlayerProfile profile = ProfileCompat.createProfile(uuid, PROFILE_NAME);

        if (profile != null) {
            // Modern path (1.18+): the Bukkit PlayerProfile / PlayerTextures API.
            URL skinUrl = extractSkinUrl(base64);

            if (skinUrl != null) {
                PlayerTextures textures = profile.getTextures();
                textures.setSkin(skinUrl);
                profile.setTextures(textures);
                ProfileCompat.setOwnerProfile(meta, profile);
            }
        } else {
            // Legacy path (1.8-1.17): set the GameProfile "textures" property directly on the meta.
            applyLegacyTexture(meta, uuid, base64);
        }
    }

    /**
     * Applies a head texture on servers without the {@code PlayerProfile} API (pre-1.18) by reflectively
     * setting a {@code com.mojang.authlib.GameProfile} (with a {@code textures} property) on the
     * {@code CraftMetaSkull#profile} field. No-op if the server's internals don't match.
     *
     * @param meta
     *            The {@link SkullMeta} to modify
     * @param uuid
     *            A stable UUID derived from the texture
     * @param base64
     *            The base64-encoded textures value
     */
    private static void applyLegacyTexture(@Nonnull SkullMeta meta, @Nonnull UUID uuid, @Nonnull String base64) {
        try {
            Class<?> gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            Class<?> propertyClass = Class.forName("com.mojang.authlib.properties.Property");

            Object gameProfile = gameProfileClass.getConstructor(UUID.class, String.class).newInstance(uuid, PROFILE_NAME);
            Object properties = gameProfileClass.getMethod("getProperties").invoke(gameProfile);
            Object property = propertyClass.getConstructor(String.class, String.class).newInstance("textures", base64);

            // PropertyMap extends Guava's ForwardingMultimap -> put(key, value)
            properties.getClass().getMethod("put", Object.class, Object.class).invoke(properties, "textures", property);

            java.lang.reflect.Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch (Throwable e) {
            // Server internals differ - leave the head without a custom texture rather than crashing.
        }
    }

    public static void setSkin(@Nonnull Block block, @Nonnull String base64, boolean sendBlockUpdate) {
        Material material = block.getType();
        if (material != XMaterial.PLAYER_HEAD.parseMaterial() && material != XMaterial.PLAYER_WALL_HEAD.parseMaterial()) {
            return;
        }

        UUID uuid = UUID.nameUUIDFromBytes(base64.getBytes(StandardCharsets.UTF_8));
        URL skinUrl = extractSkinUrl(base64);

        if (skinUrl != null) {
            PlayerProfile profile = ProfileCompat.createProfile(uuid, PROFILE_NAME);

            if (profile != null) {
                Skull skull = (Skull) block.getState();
                PlayerTextures textures = profile.getTextures();
                textures.setSkin(skinUrl);
                profile.setTextures(textures);
                ProfileCompat.setOwnerProfile(skull, profile);
                skull.update(sendBlockUpdate, false);
            }
        }
    }

    public static void setSkinFromHash(@Nonnull Block block, @Nonnull UUID uuid, @Nonnull String hashCode, boolean sendBlockUpdate) {
        Material material = block.getType();
        if (material != XMaterial.PLAYER_HEAD.parseMaterial() && material != XMaterial.PLAYER_WALL_HEAD.parseMaterial()) {
            return;
        }

        URL skinUrl = getTextureUrl(hashCode);

        if (skinUrl != null) {
            PlayerProfile profile = ProfileCompat.createProfile(uuid, PROFILE_NAME);

            if (profile != null) {
                Skull skull = (Skull) block.getState();
                PlayerTextures textures = profile.getTextures();
                textures.setSkin(skinUrl);
                profile.setTextures(textures);
                ProfileCompat.setOwnerProfile(skull, profile);
                skull.update(sendBlockUpdate, false);
            }
        }
    }

    public static @Nonnull String hashToBase64(@Nonnull String hashCode) {
        String value = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/" + hashCode + "\"}}}";
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public static @Nonnull CompletableFuture<String> fetchSkinTexture(@Nonnull UUID playerUuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String targetUrl = "https://sessionserver.mojang.com/session/minecraft/profile/"
                        + playerUuid.toString().replace("-", "") + "?unsigned=false";

                String body = httpGet(targetUrl, null);
                JsonObject obj = new JsonParser().parse(body).getAsJsonObject();

                if (obj.has("error")) {
                    throw new RuntimeException(obj.get("error").getAsString());
                }

                for (JsonElement el : obj.get("properties").getAsJsonArray()) {
                    if (el.isJsonObject() && el.getAsJsonObject().get("name").getAsString().equals("textures")) {
                        return el.getAsJsonObject().get("value").getAsString();
                    }
                }

                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static @Nonnull CompletableFuture<UUID> lookupUUID(@Nonnull String username) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String targetUrl = "https://playerdb.co/api/player/minecraft/" + username;

                String body = httpGet(targetUrl, "Mozilla/5.0 Dough (+https://github.com/baked-libs/dough)");
                JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();

                if (jsonObject.get("success").getAsBoolean()) {
                    JsonObject data = jsonObject.getAsJsonObject("data");
                    JsonObject player = data.getAsJsonObject("player");
                    return UUID.fromString(player.get("id").getAsString());
                }

                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Java-8 HTTP GET via {@link HttpURLConnection} (the universal jar cannot use the 11+ HTTP client).
     * Reads the response body from the input stream, falling back to the error stream on a non-2xx
     * status so error JSON (e.g. Mojang's {@code {"error":...}}) is still returned to the caller.
     */
    @Nonnull
    private static String httpGet(@Nonnull String targetUrl, @Nullable String userAgent) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(targetUrl).openConnection();

        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            if (userAgent != null) {
                connection.setRequestProperty("user-agent", userAgent);
            }

            InputStream stream;
            try {
                stream = connection.getInputStream();
            } catch (IOException e) {
                stream = connection.getErrorStream();

                if (stream == null) {
                    throw e;
                }
            }

            try (InputStream in = stream; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096];
                int read;

                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }

                return new String(out.toByteArray(), StandardCharsets.UTF_8);
            }
        } finally {
            connection.disconnect();
        }
    }

    @Nullable
    private static URL getTextureUrl(@Nonnull String hashCode) {
        try {
            return URI.create("https://textures.minecraft.net/texture/" + hashCode).toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Nullable
    private static URL extractSkinUrl(@Nonnull String base64) {
        try {
            String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
            JsonObject json = new JsonParser().parse(decoded).getAsJsonObject();
            String url = json.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            return URI.create(url).toURL();
        } catch (Exception e) {
            return null;
        }
    }
}
