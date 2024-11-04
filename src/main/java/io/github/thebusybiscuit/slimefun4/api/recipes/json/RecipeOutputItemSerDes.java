package io.github.thebusybiscuit.slimefun4.api.recipes.json;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.bukkit.NamespacedKey;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeOutputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeOutputGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeOutputItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public final class RecipeOutputItemSerDes implements JsonDeserializer<AbstractRecipeOutputItem>, JsonSerializer<AbstractRecipeOutputItem> {

    @Override
    public JsonElement serialize(AbstractRecipeOutputItem src, Type typeOfSrc, JsonSerializationContext context) {
        return src.serialize(context);
    }

    public AbstractRecipeOutputItem deserialize(JsonElement el, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        if (el.isJsonPrimitive()) {
            return RecipeOutputItem.fromString(el.getAsString());
        }

        JsonObject obj = el.getAsJsonObject();
        AbstractRecipeOutputItem outputItem;

        if (obj.has("id")) {
            AbstractRecipeOutputItem aItem = RecipeOutputItem.fromString(
                    obj.getAsJsonPrimitive("id").getAsString());
            if (aItem instanceof RecipeOutputItem item) {
                item.setAmount(obj.getAsJsonPrimitive("amount").getAsInt());
                outputItem = item;
            } else {
                outputItem = aItem;
            }
        } else {
            List<AbstractRecipeOutputItem> group = obj.getAsJsonArray("group")
                .asList().stream()
                .map(e -> deserialize(e, type, context))
                .toList();
            List<Integer> weights;
            if (obj.has("weights")) {
                weights = obj.getAsJsonArray("weights")
                .asList().stream()
                .map(e -> e.getAsInt())
                .toList();
            } else {
                int[] arr = new int[group.size()];
                Arrays.fill(arr, 1);
                weights = Arrays.stream(arr).boxed().toList();
            }
            outputItem = new RecipeOutputGroup(group, weights);
        }
        
        if (obj.has("class")) {
            String cl = obj.getAsJsonPrimitive("class").getAsString();
            CustomRecipeDeserializer<AbstractRecipeOutputItem> deserializer = Slimefun.getRecipeService().getRecipeOutputItemDeserializer(NamespacedKey.fromString(cl));
            if (deserializer != null) {
                outputItem = deserializer.deserialize(outputItem, obj, context);
            }
        }

        return outputItem;
    }
}