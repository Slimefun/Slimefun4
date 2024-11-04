package io.github.thebusybiscuit.slimefun4.api.recipes.matching;

import java.util.List;
import java.util.Optional;

import org.bukkit.inventory.Inventory;

import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;

public class RecipeMatchResult {

    private final Recipe recipe;
    private final InputMatchResult inputMatchResult;
    private final Optional<Inventory> craftingInventory;
    private final Optional<List<Integer>> outputSlots;

    public RecipeMatchResult(Recipe recipe, InputMatchResult inputMatchResult, Optional<Inventory> craftingInventory, Optional<List<Integer>> outputSlots) {
        this.recipe = recipe;
        this.inputMatchResult = inputMatchResult;
        this.craftingInventory = craftingInventory;
        this.outputSlots = outputSlots;
    }

    public RecipeMatchResult(Recipe recipe, InputMatchResult inputMatchResult) {
        this(recipe, inputMatchResult, Optional.empty(), Optional.empty());
    }

    public Recipe getRecipe() { return recipe; }
    public InputMatchResult getInputMatchResult() { return inputMatchResult; }
    public Optional<Inventory> getCraftingInventory() { return craftingInventory; }
    public Optional<List<Integer>> getOutputSlots() { return outputSlots; }

    public boolean itemsMatch() {
        return inputMatchResult.itemsMatch();
    }
    
}
