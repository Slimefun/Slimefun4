package io.github.thebusybiscuit.slimefun4.utils;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * Some helper methods for dealing with Json data.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class JsonUtils {

    /**
     * Do not instantiate this class.
     */
    private JsonUtils() {}

    /**
     * Little helper method to provide {@link JsonParser} functionality across different
     * versions of Gson.
     * 
     * @param json
     *            The {@link String} to parse
     * 
     * @return The parsed {@link JsonElement}
     */
    @SuppressWarnings("deprecation")
    public static @Nonnull JsonElement parseString(@Nonnull String json) {
        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_18)) {
            /*
             * As of 1.18 Spigot includes a newer version of Gson that
             * favours static method access.
             */
            return JsonParser.parseString(json);
        } else {
            /*
             * For older versions, we will need to use this way.
             */
            return new JsonParser().parse(json);
        }
    }

}
