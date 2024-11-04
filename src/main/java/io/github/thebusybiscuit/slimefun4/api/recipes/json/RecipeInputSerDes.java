package io.github.thebusybiscuit.slimefun4.api.recipes.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import io.github.thebusybiscuit.slimefun4.api.recipes.AbstractRecipeInput;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeInput;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.MatchProcedure;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public final class RecipeInputSerDes implements JsonDeserializer<AbstractRecipeInput>, JsonSerializer<AbstractRecipeInput> {

    @Override
    public JsonElement serialize(AbstractRecipeInput src, Type typeOfSrc, JsonSerializationContext context) {
        return src.serialize(context);
    }

    public AbstractRecipeInput deserialize(JsonElement el, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = el.getAsJsonObject();

        String template = "";
        int width = 0;
        int height = 0;
        JsonElement jsonItems = obj.get("items");
        if (jsonItems.isJsonPrimitive()) {
            template = jsonItems.toString();
            width = template.length();
        } else {
            // Join the entire string together
            List<JsonElement> itemsList = jsonItems.getAsJsonArray().asList();
            StringBuilder builder = new StringBuilder();
            width = 1;
            height = itemsList.size();
            for (JsonElement item : itemsList) {
                String itemString = item.getAsString();
                width = Math.max(width, itemString.length());
                builder.append(itemString);
            }
            template = builder.toString();
        }

        Map<Character, AbstractRecipeInputItem> key = new HashMap<>();
        JsonObject jsonKey = obj.getAsJsonObject("key");
        jsonKey.entrySet().forEach(e -> {
            key.put(e.getKey().charAt(0), context.deserialize(e.getValue(), AbstractRecipeInputItem.class));
        });

        MatchProcedure match = obj.has("match") ? Slimefun.getRecipeService().getMatchProcedure(
            NamespacedKey.fromString(obj.getAsJsonPrimitive("match").getAsString())
        ) : null;

        AbstractRecipeInput input = new RecipeInput(
            template.chars().mapToObj(i -> {
                char c = (char) i; // i is a zero-extended char so this is fine
                if (key.containsKey(c)) {
                    return key.get(c);
                }
                return RecipeInputItem.EMPTY;
            }).toList(),
            match == null ? MatchProcedure.SHAPED : match,
            width,
            height
        );
        
        if (obj.has("class")) {
            String cl = obj.getAsJsonPrimitive("class").getAsString();
            CustomRecipeDeserializer<AbstractRecipeInput> deserializer = Slimefun.getRecipeService().getRecipeInputDeserializer(NamespacedKey.fromString(cl));
            if (deserializer != null) {
                input = deserializer.deserialize(input, obj, context);
            }
        }

        return input;
        
    }
}