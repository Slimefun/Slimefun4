package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.Collections;
import java.util.Map;

/**
 * Stores detailed information about the result of matching a recipe against
 * some given inputs
 */
public class RecipeMatchResult {

    public static final RecipeMatchResult NO_MATCH = new RecipeMatchResult(false, Collections.emptyMap());

    public static RecipeMatchResult match(Map<Integer, Integer> consumption, String message) {
        return new RecipeMatchResult(true, consumption, message);
    }
    
    public static RecipeMatchResult match(Map<Integer, Integer> consumption) {
        return match(consumption, "");
    }

    private final boolean isMatch;
    private Map<Integer, Integer> consumption;
    private final String message;

    public RecipeMatchResult(boolean isMatch, Map<Integer, Integer> consumption, String message) {
        this.isMatch = isMatch;
        this.consumption = consumption;
        this.message = message;
    }

    public RecipeMatchResult(boolean isMatch, Map<Integer, Integer> consumption) {
        this(isMatch, consumption, "");
    }

    /**
     * Whether or not the recipe be crafted (barring research, etc...)
     * @return
     */
    public boolean isMatch() {
        return isMatch;
    }

    public Map<Integer, Integer> getConsumption() {
        return consumption;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Match: " + isMatch + " | Message: " + message + " | Consumption Map: " + consumption;
    }

}
