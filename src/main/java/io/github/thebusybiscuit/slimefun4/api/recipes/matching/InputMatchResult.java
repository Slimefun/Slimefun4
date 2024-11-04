package io.github.thebusybiscuit.slimefun4.api.recipes.matching;

import java.util.Collections;
import java.util.List;

import io.github.thebusybiscuit.slimefun4.api.recipes.AbstractRecipeInput;

public class InputMatchResult {

    private final AbstractRecipeInput input;
    private final List<ItemMatchResult> itemMatchResults;
    private final boolean itemsMatch;

    public InputMatchResult(AbstractRecipeInput input, List<ItemMatchResult> itemMatchResults, boolean itemsMatch) {
        this.input = input;
        this.itemMatchResults = itemMatchResults;
        this.itemsMatch = itemsMatch;
    }

    public InputMatchResult(AbstractRecipeInput input, List<ItemMatchResult> itemMatchResults) {
        this(input, itemMatchResults, itemMatchResults.stream().allMatch(r -> r.itemsMatch()));
    }

    public static InputMatchResult noMatch(AbstractRecipeInput input) {
        return new InputMatchResult(input, Collections.emptyList()) {
            @Override
            public boolean itemsMatch() {
                return false;
            }
        };
    }

    public AbstractRecipeInput getInput() {
        return input; 
    }
    
    /**
     * <b>IMPORTANT</b> If itemsMatch() is false, then not all match results may be present
     */
    public List<ItemMatchResult> getItemMatchResults() {
        return Collections.unmodifiableList(itemMatchResults);
    }

    public boolean itemsMatch() {
        return itemsMatch;
    }
    
}
