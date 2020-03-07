package io.github.starwishsama.miscs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.UUID;

public class BlockDataUtils {
    public static UUID getOwnerByJson(String json) {
        if (json != null) {
            JsonElement element = new JsonParser().parse(json);
            if (!element.isJsonNull()) {
                JsonObject object = element.getAsJsonObject();
                return UUID.fromString(object.get("owner").getAsString());
            }
        }
        return null;
    }
}
