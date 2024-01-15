package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.collections.Pair;
import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeSearchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.components.RecipeComponent;

/**
 * This class holds the recipes that workstations and machines use
 * (with exceptions such as Auto Anvil, Auto Enchanter, etc...)
 * 
 * @author SchnTgaiSpock
 */
public final class SlimefunRecipeService {

    public enum CachingStrategy {
        /**
         * Save the result to LRU cache even if no recipe was found,
         * or if only 1 output can be crafted
         */
        ALWAYS,
        /**
         * Save the result to LRU cache if multiple outputs can be crafted,
         * or if no recipe was found
         */
        IF_MULTIPLE_CRAFTABLE,
        /**
         * Do not save to LRU cache
         */
        NEVER
    }

    public final int CACHE_SIZE = 100;

    private final Map<RecipeCategory, List<Recipe>> recipesByCategory = new HashMap<>();
    private final Map<String, Map<RecipeCategory, Set<Recipe>>> recipesByOutput = new HashMap<>();
    private final Map<String, Map<RecipeCategory, Set<Recipe>>> recipesByInput = new HashMap<>();
    // private final Map<ItemStack, Map<RecipeCategory, List<Recipe>>> recipeByVanillaOutput = new HashMap<>();
    // private final Map<ItemStack, Map<RecipeCategory, Set<Recipe>>> recipeByVanillaInput = new HashMap<>();
    private final Map<Integer, RecipeSearchResult> cache = new LinkedHashMap<>(CACHE_SIZE, 0.75f,
            true) {
        protected boolean removeEldestEntry(Map.Entry<Integer, RecipeSearchResult> eldest) {
            return size() >= CACHE_SIZE;
        };
    };

    public SlimefunRecipeService() {
    }

    /**
     * Linearly searches all recipes in a category for a recipe using the given
     * items
     * 
     * @param category        The category of the recipe to search in
     * @param givenItems      Items from the crafting grid
     * @param cachingStrategy When to save the result to the LRU cache
     * @param onRecipeFound   To be called when a matching recipe is found
     * @return (The recipe if found, The match result) See {@link RecipeMatchResult}
     */
    @ParametersAreNonnullByDefault
    public RecipeSearchResult searchRecipes(
            RecipeCategory category,
            ItemStack[] givenItems,
            CachingStrategy cachingStrategy,
            @Nullable BiConsumer<Recipe, RecipeMatchResult> onRecipeFound) {
        // No recipes registered, no match
        List<Recipe> categoryRecipes = recipesByCategory.get(category);
        if (categoryRecipes == null) {
            return RecipeSearchResult.NO_MATCH;
        }

        // Check LRU cache
        int givenItemsHash = hashIgnoreAmount(givenItems, category);
        Optional<RecipeSearchResult> cachedMatchResult = getFromCache(givenItemsHash);
        if (cachedMatchResult.isPresent()) {
            Recipe recipe = cachedMatchResult.get().getRecipe();
            RecipeMatchResult match = cachedMatchResult.get().getMatchResult();
            if (match.isMatch() && onRecipeFound != null) {
                onRecipeFound.accept(recipe, match);
            }
            
            return cachedMatchResult.get();
        }

        // Linearly search through the recipes
        for (final Recipe recipe : categoryRecipes) {
            RecipeMatchResult match = recipe.match(givenItems);

            if (match.isMatch()) {
                if (onRecipeFound != null) {
                    onRecipeFound.accept(recipe, match);
                }

                RecipeSearchResult result = new RecipeSearchResult(recipe, category, match);

                switch (cachingStrategy) {
                    case IF_MULTIPLE_CRAFTABLE:
                        for (final Map.Entry<Integer, Integer> entry : match.getConsumption().entrySet()) {
                            ItemStack item = givenItems[entry.getKey()];
                            if (item.getAmount() < entry.getValue() * 2) {
                                break;
                            }
                        }

                    case ALWAYS:
                        cache(givenItems, category, result);

                    default:
                        break;
                }

                return result;
            }
        }

        if (cachingStrategy != CachingStrategy.NEVER) {
            cache(givenItems, category, RecipeSearchResult.NO_MATCH);
        }
        return RecipeSearchResult.NO_MATCH;
    }

    /**
     * Linearly searches all recipes in a category for a recipe using the given
     * items, and crafts it once.
     * 
     * @param category        The category of the recipe to search in
     * @param givenItems      Items from the crafting grid
     * @param cachingStrategy When to save the result to the LRU cache
     * @param onRecipeFound   To be called when a matching recipe is found. If it
     *                        returns true, consumes the input items according to
     *                        the recipe
     * @return (The recipe if found, The match result) See {@link RecipeMatchResult}
     */
    @ParametersAreNonnullByDefault
    public RecipeSearchResult searchRecipes(
            RecipeCategory category,
            ItemStack[] givenItems,
            CachingStrategy cachingStrategy,
            BiPredicate<Recipe, RecipeMatchResult> onRecipeFound) {
        return searchRecipes(category, givenItems, cachingStrategy, (recipe, match) -> {
            if (onRecipeFound != null && onRecipeFound.test(recipe, match)) {
                for (final Map.Entry<Integer, Integer> entry : match.getConsumption().entrySet()) {
                    ItemStack item = givenItems[entry.getKey()];
                    ItemUtils.consumeItem(item, entry.getValue(), true);
                }
            }
        });
    }

    /**
     * Linearly searches all recipes in a category for a recipe using the given
     * items, and crafts it once.
     * 
     * @param category        The category of the recipe to search in
     * @param givenItems      Items from the crafting grid
     * @param cachingStrategy When to save the result to the LRU cache
     * @return (The recipe if found, The match result) See {@link RecipeMatchResult}
     */
    @ParametersAreNonnullByDefault
    public RecipeSearchResult searchRecipes(
            RecipeCategory category,
            ItemStack[] givenItems,
            CachingStrategy cachingStrategy) {
        return searchRecipes(category, givenItems, cachingStrategy, (recipe, match) -> {
        });
    }

    /**
     * Registers the given recipe to the given category
     * 
     * @param category The recipe category
     * @param recipe   The recipe to register
     */
    @ParametersAreNonnullByDefault
    public void registerRecipe(RecipeCategory category, Recipe recipe) {
        registerRecipes(category, List.of(recipe));
    }

    /**
     * Registers all given recipes to the given category
     * 
     * @param category The recipe category
     * @param recipes  The recipes to register
     */
    @ParametersAreNonnullByDefault
    public void registerRecipes(RecipeCategory category, List<Recipe> recipes) {
        List<Recipe> categoryRecipes = this.recipesByCategory.getOrDefault(category, new ArrayList<>());
        for (final Recipe recipe : recipes) {
            if (recipe.isDisabled()) {
                continue;
            }

            category.onRegisterRecipe(recipe);
            categoryRecipes.add(recipe);

            for (final RecipeComponent comp : recipe.getInputs().getComponents()) {
                for (final String id : comp.getSlimefunItemIDs()) {
                    Map<RecipeCategory, Set<Recipe>> inputRecipesByCategory = recipesByInput.getOrDefault(id, new HashMap<>());
                    addToRecipeSet(category, recipe, inputRecipesByCategory);
                    recipesByInput.put(id, inputRecipesByCategory);
                }
            }

            for (final String id : recipe.getOutput().getSlimefunItemIDs()) {
                Map<RecipeCategory, Set<Recipe>> outputRecipesByCategory = recipesByOutput.getOrDefault(id, new HashMap<>());
                addToRecipeSet(category, recipe, outputRecipesByCategory);
                recipesByOutput.put(id, outputRecipesByCategory);
            }
        }
        this.recipesByCategory.put(category, categoryRecipes);
    }

    private static void addToRecipeSet(RecipeCategory category, Recipe recipe, Map<RecipeCategory, Set<Recipe>> map) {
        Set<Recipe> categoryRecipes = map.getOrDefault(category, new LinkedHashSet<>());
        categoryRecipes.add(recipe);
        map.put(category, categoryRecipes);
    }

    /**
     * @return All Slimefun recipes by category
     */
    @Nonnull
    public Map<RecipeCategory, List<Recipe>> getAllRecipes() {
        return recipesByCategory;
    }

    /**
     * Gets all recipes in the specified category
     * @param category The category to get
     */
    @Nonnull
    public List<Recipe> getRecipes(@Nonnull RecipeCategory category) {
        return recipesByCategory.getOrDefault(category, Collections.emptyList());
    }

    /**
     * Gets all recipes that craft the specified Slimefun item
     * @param slimefunID The id of the item
     */
    @Nonnull
    public Map<RecipeCategory, Set<Recipe>> getRecipesByOutput(@Nonnull String slimefunID) {
        return recipesByOutput.getOrDefault(slimefunID, Collections.emptyMap());
    }

    /**
     * Gets a stream of all recipes that craft the specified Slimefun item
     * @param slimefunID The id of the item
     */
    @Nonnull
    public Stream<Pair<RecipeCategory, Recipe>> getRecipeStreamByOutput(@Nonnull String slimefunID) {
        return recipesByOutput
            .get(slimefunID)
            .entrySet()
            .stream()
            .flatMap(entry -> entry
                .getValue()
                .stream()
                .map(elem -> new Pair<>(entry.getKey(), elem)));
    }

    /**
     * Gets all recipes in a category that craft the specified item
     * @param slimefunID The id of the item
     * @param category The category
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public Set<Recipe> getRecipesByOutput(String slimefunID, RecipeCategory category) {
        return getRecipesByOutput(slimefunID).getOrDefault(category, Collections.emptySet());
    }

    public int getNumberOfRecipes(@Nonnull String slimefunID) {
        return getRecipesByOutput(slimefunID).entrySet().stream().map(entry -> entry.getValue().size()).reduce(0, (a, b) -> a+b);
    }

    public int getNumberOfRecipesUsedIn(@Nonnull String slimefunID) {
        return getRecipesByInput(slimefunID).entrySet().stream().map(entry -> entry.getValue().size()).reduce(0, (a, b) -> a+b);
    }

    /**
     * Gets all recipes that the item is used in
     * @param slimefunID The id of the Slimefun item
     */
    @Nonnull
    public Map<RecipeCategory, Set<Recipe>> getRecipesByInput(@Nonnull String slimefunID) {
        return recipesByInput.getOrDefault(slimefunID, Collections.emptyMap());
    }

    /**
     * Gets a stream of the recipes that the item is used in
     * @param slimefunID The id of the Slimefun item
     */
    @Nonnull
    public Stream<Pair<RecipeCategory, Recipe>> getRecipeStreamByInput(@Nonnull String slimefunID) {
        return recipesByInput
            .get(slimefunID)
            .entrySet()
            .stream()
            .flatMap(entry -> entry
                .getValue()
                .stream()
                .map(elem -> new Pair<>(entry.getKey(), elem)));
    }

    /**
     * Gets all recipes in a category that the item is used in
     * @param slimefunID The id of the Slimefun item
     * @param category The category
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public Set<Recipe> getRecipesByInput(String slimefunID, RecipeCategory category) {
        return getRecipesByInput(slimefunID).getOrDefault(category, Collections.emptySet());
    }

    /**
     * Saves a pattern of given items and its match result to the LRU cache
     * 
     * @param givenItems  The items to save
     * @param matchResult The match result to save
     * @return The hash of the given items
     */
    @ParametersAreNonnullByDefault
    public int cache(ItemStack[] givenItems, RecipeCategory category, RecipeSearchResult matchResult) {
        int hash = hashIgnoreAmount(givenItems, category);
        cache.put(hash, matchResult);
        return hash;
    }

    /**
     * Returns the LRU recipe cache
     * 
     * @return The cache
     */
    @Nonnull
    public Map<Integer, RecipeSearchResult> getCache() {
        return cache;
    }

    /**
     * Returns the RecipeMatchResult associated with the given hash,
     * if it exists in the cache
     * 
     * @param hash The hash to check
     * @return The Recipe, or empty if nonexisting
     */
    @Nonnull
    public Optional<RecipeSearchResult> getFromCache(int hash) {
        return Optional.ofNullable(cache.get(hash));
    }

    @ParametersAreNonnullByDefault
    public int hashIgnoreAmount(ItemStack[] items, RecipeCategory category) {
        int hash = 1;
        for (final ItemStack item : items) {
            if (item != null) {
                hash = hash * 31 + item.getType().hashCode();
                hash = hash * 31 + (item.hasItemMeta() ? item.getItemMeta().hashCode() : 0);
            } else {
                hash *= 31;
            }
        }
        hash = hash * 31 + category.hashCode();
        return hash;
    }

    /**
     * Clears the cache
     */
    public void clearCache() {
        cache.clear();
    }

}
