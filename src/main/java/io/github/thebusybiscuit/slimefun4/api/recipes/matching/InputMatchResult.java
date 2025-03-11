package io.github.thebusybiscuit.slimefun4.api.recipes.matching;

import java.util.Collections;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.AbstractRecipeInput;

public class InputMatchResult {

    private final AbstractRecipeInput input;
    private final List<ItemMatchResult> itemMatchResults;
    private final boolean itemsMatch;
    private int possibleCrafts;

    public InputMatchResult(AbstractRecipeInput input, List<ItemMatchResult> itemMatchResults, boolean itemsMatch) {
        this.input = input;
        this.itemMatchResults = itemMatchResults;
        this.itemsMatch = itemsMatch;
        possibleCrafts = 2147483647;
        for (ItemMatchResult res : itemMatchResults) {
            ItemStack item = res.getMatchedItem();
            if (item == null || res.getRecipeItem().isEmpty()) {
                continue;
            }
            possibleCrafts = Math.min(possibleCrafts, item.getAmount() / res.getConsumeAmount());
        }
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

    public int getPossibleCrafts() {
        return possibleCrafts;
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

    /**
     * Consumes the items that were used in matching
     * the recipe, based on the amount of input required
     * in the recipe.
     * @param amount Number of times to consume. May
     * be less if there are not enough items.
     * @return How many times the inputs were actually consumed
     */
    public int consumeItems(int amount) {
        if (possibleCrafts == 0 || amount == 0) {
            return 0;
        }
        
        int amountToCraft = Math.min(possibleCrafts, amount);
        for (ItemMatchResult res : itemMatchResults) {
            ItemStack item = res.getMatchedItem();
            if (item == null || res.getRecipeItem().isEmpty()) {
                continue;
            }
            item.setAmount(item.getAmount() - amountToCraft * res.getConsumeAmount());
        }
        possibleCrafts -= amountToCraft;
        return amountToCraft;
    }
    
}
