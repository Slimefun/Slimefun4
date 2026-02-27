package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Replacement for dough's {@code PlayerSkin}, {@code PlayerHead}, and {@code CustomGameProfile}
 * that avoids extending {@code com.mojang.authlib.GameProfile}, which became {@code final}
 * in Minecraft 1.21.5+.
 *
 * <p>Uses the Bukkit {@link PlayerProfile} API (available since MC 1.18) instead of
 * authlib reflection, making this forward-compatible with all future versions.</p>
 */
public final class VersionedPlayerHead {

    private static final String PROFILE_NAME = "CS-CoreLib";

    private VersionedPlayerHead() {}

    /**
     * Creates a player head {@link ItemStack} with the given base64-encoded texture.
     *
     * @param base64 The base64-encoded texture JSON string
     * @return An {@link ItemStack} with the texture applied
     */
    public static @Nonnull ItemStack getItemStack(@Nonnull String base64) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        applyTextureToMeta(meta, base64);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Applies a base64-encoded texture to the given {@link SkullMeta}.
     *
     * @param meta   The {@link SkullMeta} to modify
     * @param base64 The base64-encoded texture JSON string
     */
    public static void applyTextureToMeta(@Nonnull SkullMeta meta, @Nonnull String base64) {
        UUID uuid = UUID.nameUUIDFromBytes(base64.getBytes(StandardCharsets.UTF_8));
        URL skinUrl = extractSkinUrl(base64);

        if (skinUrl != null) {
            PlayerProfile profile = Bukkit.createPlayerProfile(uuid, PROFILE_NAME);
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(skinUrl);
            profile.setTextures(textures);
            meta.setOwnerProfile(profile);
        }
    }

    /**
     * Sets the skin texture on a player head block using a base64-encoded texture.
     *
     * @param block           The {@link Block} to update (must be PLAYER_HEAD or PLAYER_WALL_HEAD)
     * @param base64          The base64-encoded texture JSON string
     * @param sendBlockUpdate Whether to send a block update to clients
     */
    public static void setSkin(@Nonnull Block block, @Nonnull String base64, boolean sendBlockUpdate) {
        Material material = block.getType();
        if (material != Material.PLAYER_HEAD && material != Material.PLAYER_WALL_HEAD) {
            return;
        }

        UUID uuid = UUID.nameUUIDFromBytes(base64.getBytes(StandardCharsets.UTF_8));
        URL skinUrl = extractSkinUrl(base64);

        if (skinUrl != null) {
            Skull skull = (Skull) block.getState();
            PlayerProfile profile = Bukkit.createPlayerProfile(uuid, PROFILE_NAME);
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(skinUrl);
            profile.setTextures(textures);
            skull.setOwnerProfile(profile);
            skull.update(sendBlockUpdate, false);
        }
    }

    /**
     * Sets the skin texture on a player head block using a texture hash code and a specific UUID.
     *
     * @param block           The {@link Block} to update
     * @param uuid            The {@link UUID} to use for the profile
     * @param hashCode        The hex texture hash code
     * @param sendBlockUpdate Whether to send a block update to clients
     */
    public static void setSkinFromHash(@Nonnull Block block, @Nonnull UUID uuid, @Nonnull String hashCode, boolean sendBlockUpdate) {
        Material material = block.getType();
        if (material != Material.PLAYER_HEAD && material != Material.PLAYER_WALL_HEAD) {
            return;
        }

        URL skinUrl = getTextureUrl(hashCode);

        if (skinUrl != null) {
            Skull skull = (Skull) block.getState();
            PlayerProfile profile = Bukkit.createPlayerProfile(uuid, PROFILE_NAME);
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(skinUrl);
            profile.setTextures(textures);
            skull.setOwnerProfile(profile);
            skull.update(sendBlockUpdate, false);
        }
    }

    /**
     * Converts a hex hash code to a base64-encoded texture string.
     *
     * @param hashCode The hex texture hash code
     * @return A base64-encoded texture JSON string
     */
    public static @Nonnull String hashToBase64(@Nonnull String hashCode) {
        String value = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + hashCode + "\"}}}";
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Fetches a player's skin texture (base64) from the Mojang session server.
     *
     * @param playerUuid The player's {@link UUID}
     * @return A {@link CompletableFuture} containing the base64 texture string, or null if not found
     */
    public static @Nonnull CompletableFuture<String> fetchSkinTexture(@Nonnull UUID playerUuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String targetUrl = "https://sessionserver.mojang.com/session/minecraft/profile/"
                        + playerUuid.toString().replace("-", "") + "?unsigned=false";

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(targetUrl))
                        .timeout(Duration.ofSeconds(30))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();

                if (obj.has("error")) {
                    throw new RuntimeException(obj.get("error").getAsString());
                }

                for (JsonElement el : obj.get("properties").getAsJsonArray()) {
                    if (el.isJsonObject() && el.getAsJsonObject().get("name").getAsString().equals("textures")) {
                        return el.getAsJsonObject().get("value").getAsString();
                    }
                }

                return null;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Looks up a player's UUID from their username using the playerdb.co API.
     *
     * @param username The Minecraft username
     * @return A {@link CompletableFuture} containing the player's UUID, or null if not found
     */
    public static @Nonnull CompletableFuture<UUID> lookupUUID(@Nonnull String username) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String targetUrl = "https://playerdb.co/api/player/minecraft/" + username;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(targetUrl))
                        .timeout(Duration.ofSeconds(30))
                        .header("user-agent", "Mozilla/5.0 Dough (+https://github.com/baked-libs/dough)")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

                if (jsonObject.get("success").getAsBoolean()) {
                    JsonObject data = jsonObject.getAsJsonObject("data");
                    JsonObject player = data.getAsJsonObject("player");
                    return UUID.fromString(player.get("id").getAsString());
                }

                return null;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Gets the texture URL from a hex hash code.
     */
    @Nullable
    private static URL getTextureUrl(@Nonnull String hashCode) {
        try {
            return URI.create("http://textures.minecraft.net/texture/" + hashCode).toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Extracts the skin URL from a base64-encoded texture string.
     */
    @Nullable
    private static URL extractSkinUrl(@Nonnull String base64) {
        try {
            String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
            JsonObject json = JsonParser.parseString(decoded).getAsJsonObject();
            String url = json.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            return URI.create(url).toURL();
        } catch (Exception e) {
            return null;
        }
    }
}
