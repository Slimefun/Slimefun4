package io.github.thebusybiscuit.slimefun4.api.recipes.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.NamespacedKey;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeInput;
import io.github.thebusybiscuit.slimefun4.api.recipes.AbstractRecipeInput;
import io.github.thebusybiscuit.slimefun4.api.recipes.AbstractRecipeOutput;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeOutput;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public final class RecipeSerDes implements JsonDeserializer<Recipe>, JsonSerializer<Recipe> {

    @Override
    public JsonElement serialize(Recipe src, Type typeOfSrc, JsonSerializationContext context) {
        return src.serialize(context);
    }

    public Recipe deserialize(JsonElement el, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = el.getAsJsonObject();

        Optional<String> id = Optional.empty();
        if (obj.has("id")) {
            id = Optional.of(obj.getAsJsonPrimitive("id").getAsString());
        }
        String filename = obj.getAsJsonPrimitive("__filename").getAsString();
        AbstractRecipeInput input = RecipeInput.EMPTY;
        if (obj.has("input")) {
            input = context.deserialize(obj.get("input"), AbstractRecipeInput.class);
        }
        AbstractRecipeOutput output = RecipeOutput.EMPTY;
        if (obj.has("output")) {
            output = context.deserialize(obj.get("output"), AbstractRecipeOutput.class);
        }
        Optional<Integer> energy = Optional.empty();
        Optional<Integer> craftingTime = Optional.empty();
        if (obj.has("energy")) {
            energy = Optional.of(obj.get("energy").getAsInt());
        }
        if (obj.has("crafting-time")) {
            craftingTime = Optional.of(obj.get("crafting-time").getAsInt());
        }
        List<RecipeType> types = new ArrayList<>();
        List<String> perms = new ArrayList<>();
        JsonElement jsonType = obj.get("type");
        if (jsonType.isJsonPrimitive()) {
            RecipeType recipeType = RecipeType.fromString(jsonType.getAsString());
            if (recipeType == null) {
                Slimefun.logger().warning("Invalid Recipe Type '" + jsonType.getAsString() + "'");
            } else {
                types.add(recipeType);
            }
        } else {
            jsonType.getAsJsonArray().forEach(e -> {
                RecipeType recipeType = RecipeType.fromString(jsonType.getAsString());
                if (recipeType == null) {
                    Slimefun.logger().warning("Invalid Recipe Type '" + jsonType.getAsString() + "'");
                } else {
                    types.add(recipeType);
                }
            });
        }
        if (obj.has("permission-node")) {
            JsonElement jsonPerms = obj.get("permission-node");
            if (jsonPerms.isJsonPrimitive()) {
                perms.add(jsonType.getAsString());
            } else {
                jsonPerms.getAsJsonArray().forEach(e -> perms.add(e.getAsString()));
            }
        }

        Recipe recipe = new Recipe(id, filename, input, output, types, energy, craftingTime, perms);

        if (obj.has("class")) {
            String cl = obj.getAsJsonPrimitive("class").getAsString();
            CustomRecipeDeserializer<Recipe> deserializer = Slimefun.getRecipeService().getRecipeDeserializer(NamespacedKey.fromString(cl));
            if (deserializer != null) {
                recipe = deserializer.deserialize(recipe, obj, context);
            }
        }

        return recipe;
    }
}