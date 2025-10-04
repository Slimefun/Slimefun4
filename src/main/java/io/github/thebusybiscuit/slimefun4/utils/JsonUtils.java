package io.github.thebusybiscuit.slimefun4.utils;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


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
    public static @Nonnull JsonElement parseString(@Nonnull String json) {
        return JsonParser.parseString(json);
    }

}
