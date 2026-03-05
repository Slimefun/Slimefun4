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

public final class VersionedPlayerHead {

    private static final String PROFILE_NAME = "CS-CoreLib";

    private VersionedPlayerHead() {}

    public static @Nonnull ItemStack getItemStack(@Nonnull String base64) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        applyTextureToMeta(meta, base64);

        item.setItemMeta(meta);
        return item;
    }

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

    public static @Nonnull String hashToBase64(@Nonnull String hashCode) {
        String value = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + hashCode + "\"}}}";
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

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

    @Nullable
    private static URL getTextureUrl(@Nonnull String hashCode) {
        try {
            return URI.create("http://textures.minecraft.net/texture/" + hashCode).toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

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