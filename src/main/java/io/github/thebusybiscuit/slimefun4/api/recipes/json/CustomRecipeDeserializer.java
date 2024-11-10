package io.github.thebusybiscuit.slimefun4.api.recipes.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

@FunctionalInterface
public interface CustomRecipeDeserializer<T> {

    public T deserialize(T base, JsonObject object, JsonDeserializationContext context);
    
}
