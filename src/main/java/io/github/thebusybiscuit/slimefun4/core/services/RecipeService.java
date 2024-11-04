package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import io.github.thebusybiscuit.slimefun4.api.recipes.AbstractRecipeInput;
import io.github.thebusybiscuit.slimefun4.api.recipes.AbstractRecipeOutput;
import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeOutputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.RecipeSerDes;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.RecipeInputSerDes;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.CustomRecipeDeserializer;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.RecipeInputItemSerDes;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.RecipeOutputSerDes;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.RecipeOutputItemSerDes;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.MatchProcedure;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeSearchResult;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils;

public class RecipeService {

    private GsonBuilder gsonBuilder;
    private Gson gson;

    private final Map<NamespacedKey, CustomRecipeDeserializer<AbstractRecipeInputItem>> customRIItemDeserializers = new HashMap<>();
    private final Map<NamespacedKey, CustomRecipeDeserializer<AbstractRecipeInput>> customRInputDeserializers = new HashMap<>();
    private final Map<NamespacedKey, CustomRecipeDeserializer<AbstractRecipeOutputItem>> customROItemDeserializers = new HashMap<>();
    private final Map<NamespacedKey, CustomRecipeDeserializer<AbstractRecipeOutput>> customROutputDeserializers = new HashMap<>();
    private final Map<NamespacedKey, CustomRecipeDeserializer<Recipe>> customRecipeDeserializers = new HashMap<>();

    private final Map<NamespacedKey, MatchProcedure> matchProcedures = new HashMap<>();

    private final Map<String, ItemStack> emptyItems = new HashMap<>();

    private final Map<RecipeType, List<Recipe>> recipesByType = new HashMap<>();
    private final Map<String, Recipe> recipesById = new HashMap<>();
    private final Map<String, List<Recipe>> recipesByFilename = new HashMap<>();

    private int maxCacheEntries = 1000;
    private final Map<Integer, Recipe> recipeCache = new LinkedHashMap<>() {
        protected boolean removeEldestEntry(Map.Entry<Integer, Recipe> eldest) {
            return size() > maxCacheEntries;
        };
    };

    public RecipeService(@Nonnull Plugin plugin) {
        registerMatchProcedure(MatchProcedure.SHAPED);
        registerMatchProcedure(MatchProcedure.SHAPED_FLIPPABLE);
        registerMatchProcedure(MatchProcedure.SHAPED_ROTATABLE_45_3X3);
        registerMatchProcedure(MatchProcedure.SHAPELESS);
        registerMatchProcedure(MatchProcedure.SUBSET);

        this.gsonBuilder = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Recipe.class, new RecipeSerDes())
            .registerTypeAdapter(AbstractRecipeInput.class, new RecipeInputSerDes())
            .registerTypeAdapter(AbstractRecipeOutput.class, new RecipeOutputSerDes())
            .registerTypeAdapter(AbstractRecipeInputItem.class, new RecipeInputItemSerDes())
            .registerTypeAdapter(AbstractRecipeOutputItem.class, new RecipeOutputItemSerDes());
    }

    public void registerMatchProcedure(MatchProcedure m) {
        matchProcedures.put(m.getKey(), m);
    }

    @Nonnull
    public List<Recipe> getRecipesByType(RecipeType type) {
        List<Recipe> list = recipesByType.get(type);
        return list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
    }
    /**
     * You shouldn't call this directly, call recipe.addRecipeType(type) instead
     */
    public void addRecipeToType(Recipe recipe, RecipeType type) {
        if (!recipesByType.containsKey(type)) {
            recipesByType.put(type, new ArrayList<>());
        }
        recipesByType.get(type).add(recipe);
    }

    @Nullable
    public Recipe getRecipe(String id) {
        return recipesById.get(id);
    }
    public void addRecipe(Recipe recipe) {
        System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        System.out.println(recipe);
        if (recipe.getId().isPresent()) {
            String id = recipe.getId().get();
            if (recipesById.containsKey(id)) {
                Slimefun.logger().warning("A recipe with id " + id + " already exists!");
            } else {
                recipesById.put(id, recipe);
            }
        }

        if (!recipesByFilename.containsKey(recipe.getFilename())) {
            recipesByFilename.put(recipe.getFilename(), new ArrayList<>());
        }
        recipesByFilename.get(recipe.getFilename()).add(recipe);
        recipe.getTypes().forEach(type -> addRecipeToType(recipe, type));
    }
    public List<Recipe> getRecipesByFilename(String filename) {
        List<Recipe> list = recipesByFilename.get(filename);
        return list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
    }

    @Nullable
    public Recipe getCachedRecipe(List<ItemStack> givenItems) {
        return recipeCache.get(RecipeUtils.hashItemsIgnoreAmount(givenItems));
    }
    @Nullable
    public Recipe getCachedRecipe(int hash) {
        return recipeCache.get(hash);
    }
    public void cacheRecipe(Recipe recipe, List<ItemStack> givenItems) {
        cacheRecipe(recipe, RecipeUtils.hashItemsIgnoreAmount(givenItems));
    }
    public void cacheRecipe(Recipe recipe, int hash) {
        recipeCache.put(hash, recipe);
    }

    public RecipeSearchResult searchRecipes(RecipeType type, Function<Recipe, RecipeMatchResult> recipeIsMatch, Function<Recipe, Integer> getHash) {
        List<Recipe> recipes = getRecipesByType(type);
        for (Recipe recipe : recipes) {
            RecipeMatchResult matchResult = recipeIsMatch.apply(recipe);
            if (matchResult.itemsMatch()) {
                cacheRecipe(recipe, getHash.apply(recipe));
                return new RecipeSearchResult(matchResult);
            }
        }
        return new RecipeSearchResult();
    }

    public RecipeSearchResult searchRecipes(RecipeType type, List<ItemStack> givenItems) {
        return searchRecipes(type, recipe -> recipe.match(givenItems), recipe -> RecipeUtils.hashItemsIgnoreAmount(givenItems));
    }

    public RecipeSearchResult searchRecipes(Collection<RecipeType> types, Function<Recipe, RecipeMatchResult> recipeIsMatch, Function<Recipe, Integer> getHash) {
        for (RecipeType type : types) {
            RecipeSearchResult result = searchRecipes(type, recipeIsMatch, getHash);
            if (result.matchFound()) {
                return result;
            }
        }
        return new RecipeSearchResult();
    }

    public RecipeSearchResult searchRecipes(Collection<RecipeType> types, List<ItemStack> givenItems) {
        return searchRecipes(types, recipe -> recipe.match(givenItems), recipe -> RecipeUtils.hashItemsIgnoreAmount(givenItems));
    }

    @Nullable
    public MatchProcedure getMatchProcedure(@Nonnull NamespacedKey key) {
        return matchProcedures.get(key);
    }
    /**
     * Registers another match procedure if one with key <code>key</code> doesn't already exist. Used when deserializing recipes from json
     * @return If the procedure was successfully added
     */
    public boolean registerMatchProcedure(NamespacedKey key, MatchProcedure match) {
        if (matchProcedures.containsKey(key)) {
            return false;
        }
        matchProcedures.put(key, match);
        return true;
    }

    public void addEmptyItem(String id, ItemStack empty) {
        emptyItems.put(id, empty);
    }
    public ItemStack getEmptyItem(String id) {
        return emptyItems.get(id);
    }

    @ParametersAreNonnullByDefault
    public void addRecipeInputItemDeserializer(NamespacedKey key, CustomRecipeDeserializer<AbstractRecipeInputItem> des) {
        customRIItemDeserializers.put(key, des);
    }
    @ParametersAreNonnullByDefault
    public void addRecipeInputDeserializer(NamespacedKey key, CustomRecipeDeserializer<AbstractRecipeInput> des) {
        customRInputDeserializers.put(key, des);
    }
    @ParametersAreNonnullByDefault
    public void addRecipeOutputItemDeserializer(NamespacedKey key, CustomRecipeDeserializer<AbstractRecipeOutputItem> des) {
        customROItemDeserializers.put(key, des);
    }
    @ParametersAreNonnullByDefault
    public void addRecipeOutputDeserializer(NamespacedKey key, CustomRecipeDeserializer<AbstractRecipeOutput> des) {
        customROutputDeserializers.put(key, des);
    }
    @ParametersAreNonnullByDefault
    public void addRecipeDeserializer(NamespacedKey key, CustomRecipeDeserializer<Recipe> des) {
        customRecipeDeserializers.put(key, des);
    }
    public CustomRecipeDeserializer<AbstractRecipeInputItem> getRecipeInputItemDeserializer(@Nonnull NamespacedKey key) {
        return customRIItemDeserializers.get(key);
    }
    public CustomRecipeDeserializer<AbstractRecipeInput> getRecipeInputDeserializer(@Nonnull NamespacedKey key) {
        return customRInputDeserializers.get(key);
    }
    public CustomRecipeDeserializer<AbstractRecipeOutputItem> getRecipeOutputItemDeserializer(@Nonnull NamespacedKey key) {
        return customROItemDeserializers.get(key);
    }
    public CustomRecipeDeserializer<AbstractRecipeOutput> getRecipeOutputDeserializer(@Nonnull NamespacedKey key) {
        return customROutputDeserializers.get(key);
    }
    public CustomRecipeDeserializer<Recipe> getRecipeDeserializer(@Nonnull NamespacedKey key) {
        return customRecipeDeserializers.get(key);
    }

    /**
     * For addons to add custom deserialization for fields in recipe subclasses or recipe component subclasses
     */
    @Nonnull
    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    private void createGson() {
        gson = gsonBuilder.create();
    }

    public void loadAllRecipes() {
        createGson();

        final String RECIPE_PATH = "plugins/Slimefun/recipes/";

        try {
            Path dir = Files.createDirectories(Paths.get(RECIPE_PATH));
            Files.list(dir).forEach(file -> {
                loadRecipesFromFile(file.toString()).forEach(recipe -> addRecipe(recipe));
            });
        } catch (IOException e) {
            Slimefun.logger().warning("Could not load recipes: " + e.getMessage());
        }
    }

    /**
     * Gets a recipe from a json file
     * @param filename Filename WITH .json
     */
    public List<Recipe> loadRecipesFromFile(String filename) {
        return loadRecipesFromFile(filename, gson);
    }

    /**
     * Gets a recipe from a json file
     * @param filename Filename WITH .json
     * @param gson The instance of gson to use
     */
    public List<Recipe> loadRecipesFromFile(String filename, Gson gson) {
        try {
            JsonElement obj = gson.fromJson(new FileReader(new File(filename)), JsonElement.class);
            if (obj.isJsonArray()) {
                JsonArray jsonRecipes = obj.getAsJsonArray();
                List<Recipe> recipes = new ArrayList<>();
                for (JsonElement jsonRecipe : jsonRecipes) {
                    JsonObject recipe = jsonRecipe.getAsJsonObject();
                    recipe.addProperty("__filename", filename);
                    recipes.add(gson.fromJson(recipe, Recipe.class));
                }
                return recipes;
            } else {
                JsonObject recipe = obj.getAsJsonObject();
                recipe.addProperty("__filename", filename);
                return List.of(gson.fromJson(obj, Recipe.class));
            }
        } catch (IOException e) {
            Slimefun.logger().warning("Could not load recipe file '" + filename + "': " + e.getMessage());
        } catch (NullPointerException e) {
            Slimefun.logger().warning("Could not load recipe file '" + filename + "': " + e.getMessage());
        }

        return Collections.emptyList();
    }

    public void saveAllRecipes() {
        for (Map.Entry<String, List<Recipe>> entry : recipesByFilename.entrySet()) {
            String filename = entry.getKey();
            List<Recipe> recipes = entry.getValue();
            if (recipes.size() == 1) {
                try (Writer writer = new FileWriter(filename)) {
                    JsonWriter jsonWriter = gson.newJsonWriter(writer);
                    jsonWriter.setIndent("    ");
                    gson.toJson(recipes.get(0), Recipe.class, jsonWriter);
                } catch (IOException e) {
                    Slimefun.logger().warning("Couldn't save recipe to '" + filename + "': " + e.getMessage());
                } catch (JsonIOException e) {
                    Slimefun.logger().warning("Couldn't save recipe to '" + filename + "': " + e.getMessage());
                }
            }
        }
    }

}
