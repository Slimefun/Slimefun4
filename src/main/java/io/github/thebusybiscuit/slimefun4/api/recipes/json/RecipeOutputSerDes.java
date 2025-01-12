package io.github.thebusybiscuit.slimefun4.api.recipes.json;

import java.lang.reflect.Type;

import org.bukkit.NamespacedKey;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import io.github.thebusybiscuit.slimefun4.api.recipes.AbstractRecipeOutput;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeOutput;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeOutputItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public final class RecipeOutputSerDes implements JsonDeserializer<AbstractRecipeOutput>, JsonSerializer<AbstractRecipeOutput> {

    @Override
    public JsonElement serialize(AbstractRecipeOutput src, Type typeOfSrc, JsonSerializationContext context) {
        return src.serialize(context);
    }

    public AbstractRecipeOutput deserialize(JsonElement el, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = el.getAsJsonObject();
        
        AbstractRecipeOutput output = new RecipeOutput(
            obj.getAsJsonArray("items")
                .asList().stream()
                .map(e -> (AbstractRecipeOutputItem) context.deserialize(e, AbstractRecipeOutputItem.class))
                .toList()
        );

        if (obj.has("class")) {
            String cl = obj.getAsJsonPrimitive("class").getAsString();
            CustomRecipeDeserializer<AbstractRecipeOutput> deserializer = Slimefun.getRecipeService().getRecipeOutputDeserializer(NamespacedKey.fromString(cl));
            if (deserializer != null) {
                output = deserializer.deserialize(output, obj, context);
            }
        }

        return output;
    }
}