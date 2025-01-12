package io.github.thebusybiscuit.slimefun4.api.recipes.matching;

import java.util.Optional;

import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;

public class RecipeSearchResult {

    private final boolean matchFound;
    private final Optional<RecipeMatchResult> result;

    public RecipeSearchResult(RecipeMatchResult result) {
        this.matchFound = result.itemsMatch();
        this.result = Optional.of(result);
    }
    
    public RecipeSearchResult() {
        this.matchFound = false;
        this.result = Optional.empty();
    }

    public boolean matchFound() {
        return matchFound;
    }

    public Optional<RecipeMatchResult> getResult() {
        return result;
    }

    public Optional<Recipe> getRecipe() {
        return result.map(res -> res.getRecipe());
    }
    
}
