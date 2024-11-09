package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils;

public class RecipeService {

    public static final String SAVED_RECIPE_DIR = "plugins/Slimefun/recipes/";

    private Plugin plugin;
    private Gson gson;

    private final Map<NamespacedKey, CustomRecipeDeserializer<AbstractRecipeInputItem>> customRIItemDeserializers = new HashMap<>();
    private final Map<NamespacedKey, CustomRecipeDeserializer<AbstractRecipeInput>> customRInputDeserializers = new HashMap<>();
    private final Map<NamespacedKey, CustomRecipeDeserializer<AbstractRecipeOutputItem>> customROItemDeserializers = new HashMap<>();
    private final Map<NamespacedKey, CustomRecipeDeserializer<AbstractRecipeOutput>> customROutputDeserializers = new HashMap<>();
    private final Map<NamespacedKey, CustomRecipeDeserializer<Recipe>> customRecipeDeserializers = new HashMap<>();

    private final Map<NamespacedKey, MatchProcedure> matchProcedures = new HashMap<>();

    private final Map<String, ItemStack> emptyItems = new HashMap<>();

    private final Map<String, Recipe> recipesById = new HashMap<>();
    // This map allows loading and saving from JSON files
    private final Map<String, Set<Recipe>> recipesByFilename = new HashMap<>();
    // This holds the names of json files read in, it helps differentiates between
    // entries in `recipesByFilename` existing because it was read in from a file,
    // vs if the recipe was added directly in code.
    private final Set<String> filesRead = new HashSet<>();
    // This map facilitates searching through recipe with a certain RecipeType
    private final Map<RecipeType, Set<Recipe>> recipesByType = new HashMap<>();

    private final Set<String> recipeOverrides = new HashSet<>();

    private int maxCacheEntries = 1000;
    private boolean allRecipesLoaded = false;

    private final Map<Integer, Recipe> recipeCache = new LinkedHashMap<>() {
        protected boolean removeEldestEntry(Map.Entry<Integer, Recipe> eldest) {
            return size() > maxCacheEntries;
        };
    };

    public RecipeService(@Nonnull Plugin plugin) {
        this.plugin = plugin;

        registerMatchProcedure(MatchProcedure.SHAPED);
        registerMatchProcedure(MatchProcedure.SHAPED_FLIPPABLE);
        registerMatchProcedure(MatchProcedure.SHAPED_ROTATABLE_45_3X3);
        registerMatchProcedure(MatchProcedure.SHAPELESS);
        registerMatchProcedure(MatchProcedure.SUBSET);

        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Recipe.class, new RecipeSerDes())
            .registerTypeAdapter(AbstractRecipeInput.class, new RecipeInputSerDes())
            .registerTypeAdapter(AbstractRecipeOutput.class, new RecipeOutputSerDes())
            .registerTypeAdapter(AbstractRecipeInputItem.class, new RecipeInputItemSerDes())
            .registerTypeAdapter(AbstractRecipeOutputItem.class, new RecipeOutputItemSerDes())
            .create();

        try (BufferedReader reader = new BufferedReader(new FileReader("plugins/Slimefun/recipe-overrides"))) {
            String line = reader.readLine();
            while (line != null) {
                recipeOverrides.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Could not load recipe overrides: " + e.getLocalizedMessage());
        } finally {
            allRecipesLoaded = true;
        }
    }

    /**
     * Adds a recipe override
     * @return If the override was applied or not
     */
    public boolean addRecipeOverride(String override, String... filenames) {
        if (allRecipesLoaded) {
            plugin.getLogger().warning("Recipes were already loaded, so the recipe override '" + override + "' was not processed!");
            return false;
        }
        if (recipeOverrides.contains(override)) {
            return false;
        }
        for (String filename : filenames) {
            File file = new File(SAVED_RECIPE_DIR + filename + ".json");
            if (file.isFile()) {
                try {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        plugin.getLogger().severe("Could not delete file '" + filename + "' for recipe override '" + override + "'");
                        return false;
                    }
                } catch (Exception e) {
                    plugin.getLogger().severe("An error occurred when applying recipe override '" + override + "' to file '" + filename + "': " + e.getLocalizedMessage());
                    return false;
                };
            } else {
                plugin.getLogger().warning("Skipping file '" + filename + "' for recipe override '" + override + "' because it is a directory");
            }
        }
        recipeOverrides.add(override);
        return true;
    }

    public void registerMatchProcedure(MatchProcedure m) {
        matchProcedures.put(m.getKey(), m);
    }

    @Nonnull
    public Set<Recipe> getRecipesByType(RecipeType type) {
        Set<Recipe> set = recipesByType.get(type);
        return set == null ? Collections.emptySet() : Collections.unmodifiableSet(set);
    }
    /**
     * You shouldn't call this directly, call recipe.addRecipeType(type) instead
     */
    public void addRecipeToType(Recipe recipe, RecipeType type) {
        if (!recipesByType.containsKey(type)) {
            recipesByType.put(type, new HashSet<>());
        }
        recipesByType.get(type).add(recipe);
    }

    @Nullable
    public Recipe getRecipe(String id) {
        return recipesById.get(id);
    }

    /**
     * Registers a recipe in the service. Ideally recipes should be defined
     * in a JSON file in the resources directory of your plugin.
     * @param recipe Recipe to add
     * @param forceId Override the recipe with the same id, if it exists
     * @param forceFilename If file was already read, add this recipe to
     * the list anyways.
     */
    public void addRecipe(Recipe recipe, boolean forceId, boolean forceFilename) {
        // Check id conflicts, add to id map
        if (recipe.getId().isPresent()) {
            String id = recipe.getId().get();
            if (recipesById.containsKey(id) && !forceId) {
                plugin.getLogger().warning("A recipe with id " + id + " already exists!");
            } else {
                if (forceId && recipesById.containsKey(id)) {
                    Recipe old = recipesById.get(id);
                    removeRecipeFromFilename(old);
                    removeRecipeFromTypes(old);
                    recipeCache.clear();
                }
                recipesById.put(id, recipe);
            }
        }

        // Add to file map
        if (!recipesByFilename.containsKey(recipe.getFilename())) {
            // We want to preserve the order the recipes are
            // listed in the file, for consistency.
            Set<Recipe> newList = new LinkedHashSet<>();
            newList.add(recipe);
            recipesByFilename.put(recipe.getFilename(), newList);
        } else if (forceFilename || !filesRead.contains(recipe.getFilename())) {
            // If we have already loaded the recipe file with this filename,
            // Then we don't want to modify it (or else we get duplicate recipes)
            recipesByFilename.get(recipe.getFilename()).add(recipe);
        }

        // Add to type map
        recipe.getTypes().forEach(type -> addRecipeToType(recipe, type));
    }
    /**
     * Registers a recipe in the service.
     * @param recipe Recipe to register
     */
    public void addRecipe(Recipe recipe) {
        addRecipe(recipe, false, false);
    }

    /**
     * Internal utility method for removing old recipes from the id map when a recipe is to be deleted
     */
    private void removeRecipeFromId(Recipe recipe) {
        if (recipe.getId().isPresent()) {
            recipesById.remove(recipe.getId().get());
        }
    }
    /**
     * Internal utility method for removing old recipes from the type map when a recipe is to be deleted
     */
    private void removeRecipeFromTypes(Recipe recipe) {
        for (RecipeType type : recipe.getTypes()) {
            recipesByType.get(type).remove(recipe);
        }
    }
    /**
     * Internal utility method for removing old recipes from the file map when a recipe is to be deleted
     */
    private void removeRecipeFromFilename(Recipe recipe) {
        recipesByFilename.get(recipe.getFilename()).remove(recipe);
    }

    public Set<Recipe> getRecipesByFilename(String filename) {
        Set<Recipe> set = recipesByFilename.get(filename);
        return set == null ? Collections.emptySet() : Collections.unmodifiableSet(set);
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

    public RecipeSearchResult searchRecipes(RecipeType type, Function<Recipe, RecipeMatchResult> recipeIsMatch, int hash) {
        Recipe cachedRecipe = getCachedRecipe(hash);
        // Sanity check
        if (cachedRecipe != null && cachedRecipe.getTypes().contains(type)) {
            RecipeMatchResult result = recipeIsMatch.apply(cachedRecipe);
            if (result.itemsMatch()) {
                return new RecipeSearchResult(result);
            }
        }
        Set<Recipe> recipes = getRecipesByType(type);
        for (Recipe recipe : recipes) {
            RecipeMatchResult matchResult = recipeIsMatch.apply(recipe);
            if (matchResult.itemsMatch()) {
                cacheRecipe(recipe, hash);
                return new RecipeSearchResult(matchResult);
            }
        }
        return new RecipeSearchResult();
    }
    public RecipeSearchResult searchRecipes(RecipeType type, List<ItemStack> givenItems, MatchProcedure matchAs) {
        return searchRecipes(type, recipe -> recipe.matchAs(matchAs, givenItems), RecipeUtils.hashItemsIgnoreAmount(givenItems));
    }
    public RecipeSearchResult searchRecipes(RecipeType type, List<ItemStack> givenItems) {
        return searchRecipes(type, recipe -> recipe.match(givenItems), RecipeUtils.hashItemsIgnoreAmount(givenItems));
    }

    public RecipeSearchResult searchRecipes(Collection<RecipeType> types, Function<Recipe, RecipeMatchResult> recipeIsMatch, int hash) {
        for (RecipeType type : types) {
            RecipeSearchResult result = searchRecipes(type, recipeIsMatch, hash);
            if (result.matchFound()) {
                return result;
            }
        }
        return new RecipeSearchResult();
    }
    public RecipeSearchResult searchRecipes(Collection<RecipeType> types, List<ItemStack> givenItems, MatchProcedure matchAs) {
        return searchRecipes(types, recipe -> recipe.matchAs(matchAs, givenItems), RecipeUtils.hashItemsIgnoreAmount(givenItems));
    }
    public RecipeSearchResult searchRecipes(Collection<RecipeType> types, List<ItemStack> givenItems) {
        return searchRecipes(types, recipe -> recipe.match(givenItems), RecipeUtils.hashItemsIgnoreAmount(givenItems));
    }

    @Nullable
    public MatchProcedure getMatchProcedure(@Nonnull NamespacedKey key) {
        return matchProcedures.get(key);
    }
    /**
     * Registers another match procedure if one with key <code>key</code> doesn't
     * already exist. Used when deserializing recipes from json
     * 
     * @return If the procedure was successfully added
     */
    public boolean registerMatchProcedure(NamespacedKey key, MatchProcedure match) {
        if (matchProcedures.containsKey(key)) {
            return false;
        }
        matchProcedures.put(key, match);
        return true;
    }

    public ItemStack getEmptyItem(String id) {
        return emptyItems.get(id);
    }
    public void addEmptyItem(String id, ItemStack empty) {
        emptyItems.put(id, empty);
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

    public Recipe parseRecipeString(String s) {
        return gson.fromJson(s, Recipe.class);
    }

    /**
     * @return The list of all recipe files in <code>/plugins/Slimefun/recipes/[subdirectory]</code>
     * with the .json removed
     */
    public Set<String> getAllRecipeFilenames(String subdirectory) {
        Path dir = Path.of(SAVED_RECIPE_DIR, subdirectory);
        if (!dir.toFile().exists()) {
            return Collections.emptySet();
        }
        try (Stream<Path> files = Files.walk(dir)) {
            return files
                .filter(f -> f.toString().endsWith(".json"))
                .map(file -> {
                    String filename = dir.relativize(file).toString();
                    return filename.substring(0, filename.length() - 5);
                })
                .collect(Collectors.toSet());
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    /**
     * @return The list of all recipe files in <code>/plugins/Slimefun/recipes</code>
     * directory, with the .json removed
     */
    public Set<String> getAllRecipeFilenames() {
        return getAllRecipeFilenames("");
    }

    public void loadAllRecipes() {
        getAllRecipeFilenames().forEach(this::loadRecipesFromFile);
        allRecipesLoaded = true;
    }

    /**
     * Gets a recipe from a json file
     * 
     * @param filename Filename WITHOUT .json
     */
    public List<Recipe> loadRecipesFromFile(String filename) {
        return loadRecipesFromFile(filename, gson);
    }

    /**
     * Gets a recipe from a json file
     * 
     * @param filename Filename WITHOUT .json
     * @param gson     The instance of gson to use
     */
    public List<Recipe> loadRecipesFromFile(String filename, Gson gson) {
        List<Recipe> recipes = new ArrayList<>();
        if (recipesByFilename.containsKey(filename)) {
            for (Recipe recipe : recipesByFilename.get(filename)) {
                removeRecipeFromId(recipe);
                removeRecipeFromTypes(recipe);
                recipeCache.clear();
            }
            recipesByFilename.get(filename).clear();
        }
        try (FileReader fileReader = new FileReader(new File(SAVED_RECIPE_DIR + filename + ".json"))) {
            JsonElement obj = gson.fromJson(fileReader, JsonElement.class);
            if (obj.isJsonArray()) {
                JsonArray jsonRecipes = obj.getAsJsonArray();
                recipes = new ArrayList<>();
                for (JsonElement jsonRecipe : jsonRecipes) {
                    JsonObject recipe = jsonRecipe.getAsJsonObject();
                    recipe.addProperty("__filename", filename);
                    recipes.add(gson.fromJson(recipe, Recipe.class));
                }
            } else {
                JsonObject recipe = obj.getAsJsonObject();
                recipe.addProperty("__filename", filename);
                recipes.add(gson.fromJson(obj, Recipe.class));
            }
            filesRead.add(filename);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not load recipe file '" + filename + "': " + e.getLocalizedMessage());
            recipes = Collections.emptyList();
        } catch (NullPointerException e) {
            plugin.getLogger().warning("Could not load recipe file '" + filename + "': " + e.getLocalizedMessage());
            recipes = Collections.emptyList();
        }

        recipes.forEach(r -> addRecipe(r, true, true));
        return recipes;
    }

    public boolean areAllRecipesLoaded() {
        return allRecipesLoaded;
    }

    public void saveAllRecipes() {
        for (Map.Entry<String, Set<Recipe>> entry : recipesByFilename.entrySet()) {
            String filename = entry.getKey();
            Set<Recipe> recipes = entry.getValue();
            try (Writer writer = new FileWriter(SAVED_RECIPE_DIR + filename + ".json")) {
                JsonWriter jsonWriter = gson.newJsonWriter(writer);
                jsonWriter.setIndent("    ");
                if (recipes.size() == 1) {
                    gson.toJson(recipes.stream().findFirst().get(), Recipe.class, jsonWriter);
                } else {
                    gson.toJson(recipes, List.class, jsonWriter);
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Couldn't save recipe to '" + filename + "': " + e.getLocalizedMessage());
            } catch (JsonIOException e) {
                plugin.getLogger().warning("Couldn't save recipe to '" + filename + "': " + e.getLocalizedMessage());
            }
        }
    }

}
