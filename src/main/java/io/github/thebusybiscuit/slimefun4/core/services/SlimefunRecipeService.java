package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeMatchResult;

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

    public final int CACHE_SIZE = 50;

    private final Map<RecipeCategory, List<Recipe>> recipes = new HashMap<>();
    private final Map<Integer, Pair<Optional<Recipe>, RecipeMatchResult>> cache = new LinkedHashMap<>(CACHE_SIZE, 0.75f,
            true) {
        protected boolean removeEldestEntry(Map.Entry<Integer, Pair<Optional<Recipe>, RecipeMatchResult>> eldest) {
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
    public Pair<Optional<Recipe>, RecipeMatchResult> searchRecipes(
            RecipeCategory category,
            ItemStack[] givenItems,
            CachingStrategy cachingStrategy,
            @Nullable BiConsumer<Recipe, RecipeMatchResult> onRecipeFound) {
        // No recipes registered, no match
        final List<Recipe> categoryRecipes = recipes.get(category);
        if (categoryRecipes == null) {
            return new Pair<>(Optional.empty(), RecipeMatchResult.NO_MATCH);
        }

        // Check LRU cache
        final int givenItemsHash = hash(givenItems);
        final Optional<Pair<Optional<Recipe>, RecipeMatchResult>> cachedMatchResult = getFromCache(givenItemsHash);
        if (cachedMatchResult.isPresent()) {
            final Optional<Recipe> recipe = cachedMatchResult.get().getFirstValue();
            final RecipeMatchResult match = cachedMatchResult.get().getSecondValue();
            if (recipe.isPresent() && onRecipeFound != null) {
                onRecipeFound.accept(recipe.get(), match);
            }
            return cachedMatchResult.get();
        }

        // Linear search through the recipes
        for (final Recipe recipe : categoryRecipes) {
            final RecipeMatchResult match = recipe.match(givenItems);

            if (match.isMatch()) {
                if (onRecipeFound != null) {
                    onRecipeFound.accept(recipe, match);
                }
                final Pair<Optional<Recipe>, RecipeMatchResult> result = new Pair<>(Optional.of(recipe), match);

                switch (cachingStrategy) {
                    case IF_MULTIPLE_CRAFTABLE:
                        for (final Map.Entry<Integer, Integer> entry : match.getConsumption().entrySet()) {
                            final ItemStack item = givenItems[entry.getKey()];
                            if (item.getAmount() < entry.getValue() * 2) {
                                break;
                            }
                        }

                    case ALWAYS:
                        cache(givenItems, result);

                    default:
                        break;
                }

                return result;
            }
        }

        final Pair<Optional<Recipe>, RecipeMatchResult> result = new Pair<>(Optional.empty(),
                RecipeMatchResult.NO_MATCH);
        if (cachingStrategy == CachingStrategy.ALWAYS) {
            cache(givenItems, result);
        }
        return result;
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
    public Pair<Optional<Recipe>, RecipeMatchResult> searchRecipes(
            RecipeCategory category,
            ItemStack[] givenItems,
            CachingStrategy cachingStrategy,
            BiPredicate<Recipe, RecipeMatchResult> onRecipeFound) {
        return searchRecipes(category, givenItems, cachingStrategy, (recipe, match) -> {
            if (onRecipeFound != null && onRecipeFound.test(recipe, match)) {
                for (final Map.Entry<Integer, Integer> entry : match.getConsumption().entrySet()) {
                    final ItemStack item = givenItems[entry.getKey()];
                    item.setAmount(item.getAmount() - entry.getValue());
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
    public Pair<Optional<Recipe>, RecipeMatchResult> searchRecipes(
            RecipeCategory category,
            ItemStack[] givenItems,
            CachingStrategy cachingStrategy) {
        return searchRecipes(category, givenItems, cachingStrategy, (recipe, match) -> {
        });
    }

    /**
     * Registers all given recipes to the given category
     * 
     * @param category The recipe category
     * @param recipes  The recipes to register
     */
    @ParametersAreNonnullByDefault
    public void registerRecipes(RecipeCategory category, List<Recipe> recipes) {
        final List<Recipe> categoryRecipes = this.recipes.getOrDefault(category, new ArrayList<>());
        for (final Recipe recipe : recipes) {
            category.onRegisterRecipe(recipe);
            categoryRecipes.add(recipe);
        }
        this.recipes.put(category, categoryRecipes);
    }

    /**
     * Gets all Slimefun recipes by category
     * 
     * @return All recipes
     */
    @Nonnull
    public Map<RecipeCategory, List<Recipe>> getAllRecipes() {
        return recipes;
    }

    @Nonnull
    public List<Recipe> getRecipes(@Nonnull RecipeCategory category) {
        return recipes.getOrDefault(category, Collections.emptyList());
    }

    /**
     * Saves a pattern of given items and its match result to the LRU cache
     * 
     * @param givenItems  The items to save
     * @param matchResult The match result to save
     * @return The hash of the given items
     */
    @ParametersAreNonnullByDefault
    public int cache(ItemStack[] givenItems, Pair<Optional<Recipe>, RecipeMatchResult> matchResult) {
        final int hash = hash(givenItems);
        cache.put(hash, matchResult);
        return hash;
    }

    /**
     * Returns the LRU recipe cache
     * 
     * @return The cache
     */
    @Nonnull
    public Map<Integer, Pair<Optional<Recipe>, RecipeMatchResult>> getCache() {
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
    public Optional<Pair<Optional<Recipe>, RecipeMatchResult>> getFromCache(int hash) {
        return Optional.ofNullable(cache.get(hash));
    }

    private int hash(@Nonnull ItemStack... items) {
        int hash = 1;
        for (final ItemStack item : items) {
            hash = hash * 31 + (item == null ? 0 : item.hashCode());
        }
        return hash;
    }

}
