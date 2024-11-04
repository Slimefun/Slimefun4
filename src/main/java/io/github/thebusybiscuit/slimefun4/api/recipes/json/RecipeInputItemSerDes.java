package io.github.thebusybiscuit.slimefun4.api.recipes.json;

import java.lang.reflect.Type;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputTag;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils;

public final class RecipeInputItemSerDes implements JsonDeserializer<AbstractRecipeInputItem>, JsonSerializer<AbstractRecipeInputItem> {

    @Override
    public JsonElement serialize(AbstractRecipeInputItem src, Type typeOfSrc, JsonSerializationContext context) {
        return src.serialize(context);
    }

    public AbstractRecipeInputItem deserialize(JsonElement el, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        if (el.isJsonPrimitive()) {
            return RecipeInputItem.fromString(el.getAsString());
        }

        AbstractRecipeInputItem inputItem;
        JsonObject obj = el.getAsJsonObject();

        if (obj.has("id")) {
            AbstractRecipeInputItem aItem = RecipeInputItem.fromString(
                    obj.getAsJsonPrimitive("id").getAsString());
            if (aItem instanceof RecipeInputItem item) {
                item.setAmount(obj.getAsJsonPrimitive("amount").getAsInt());
                if (obj.has("durability")) {
                    item.setDurabilityCost(obj.getAsJsonPrimitive("durability").getAsInt());
                }
                inputItem = item;
            } else {
                inputItem = aItem;
            }
        } else if (obj.has("group")) {
            inputItem = new RecipeInputGroup(obj.getAsJsonArray("group")
                    .asList().stream()
                    .map(e -> deserialize(e, type, context))
                    .toList());
        } else {
            Optional<Tag<Material>> tag = RecipeUtils.tagFromString(obj.getAsJsonPrimitive("tag").getAsString());
            inputItem = tag
                    .map(t -> (AbstractRecipeInputItem) new RecipeInputTag(t,
                            obj.getAsJsonPrimitive("amount").getAsInt()))
                    .orElseGet(() -> RecipeInputItem.EMPTY);
        }

        if (obj.has("class")) {
            String cl = obj.getAsJsonPrimitive("class").getAsString();
            CustomRecipeDeserializer<AbstractRecipeInputItem> deserializer = Slimefun.getRecipeService()
                    .getRecipeInputItemDeserializer(NamespacedKey.fromString(cl));
            if (deserializer != null) {
                inputItem = deserializer.deserialize(inputItem, obj, context);
            }
        }

        return inputItem;
    }
}