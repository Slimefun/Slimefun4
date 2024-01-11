package io.github.thebusybiscuit.slimefun4.api.recipes;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

public class RecipeSearchResult {

    public static final RecipeSearchResult NO_MATCH = new RecipeSearchResult(null, null, RecipeMatchResult.NO_MATCH);

    private final @Nullable Recipe recipe;
    private final @Nullable RecipeCategory searchCategory;
    private final RecipeMatchResult matchResult;

    @ParametersAreNonnullByDefault
    public RecipeSearchResult(Recipe recipe, RecipeCategory searchCategory, RecipeMatchResult matchResult) {
        Preconditions.checkArgument(matchResult != null, "'matchResult' cannot be null!");
        Preconditions.checkArgument(!matchResult.isMatch() || recipe != null, "'recipe' cannot be null when there is a match!");
        Preconditions.checkArgument(!matchResult.isMatch() || searchCategory != null, "'searchCategory' cannot be null when there is a match!");

        this.recipe = recipe;
        this.searchCategory = searchCategory;
        this.matchResult = matchResult;
    }

    /**
     * @return The matched recipe, or null if {@code RecipeSearchResult.isMatch()} is false
     */
    public @Nullable Recipe getRecipe() {
        return recipe;
    }

    /**
     * @return The category this recipe was found in, or null if {@code isMatch()} is false
     */
    public @Nullable RecipeCategory getSearchCategory() {
        return searchCategory;
    }

    /**
     * @return The match result
     */
    public RecipeMatchResult getMatchResult() {
        return matchResult;
    }

    public boolean isMatch() {
        return matchResult.isMatch();
    }

    @Override
    public String toString() {
        return "{Match Result: " + matchResult + ", Category: " + searchCategory + ", Recipe: " + recipe;
    }
    
}
