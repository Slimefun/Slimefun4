package io.github.thebusybiscuit.slimefun4.api.recipes.matching;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeInputItem;

public class ItemMatchResult {

    private final boolean itemsMatch;
    private final AbstractRecipeInputItem recipeItem;
    private final @Nullable ItemStack matchedItem;
    private final int consumeAmount;
    private final int durabilityConsumeAmount;

    public ItemMatchResult(boolean itemsMatch, AbstractRecipeInputItem recipeItem, ItemStack matchedItem, int consumeAmount, int durabilityConsumeAmount) {
        this.itemsMatch = itemsMatch;
        this.recipeItem = recipeItem;
        this.matchedItem = matchedItem;
        this.consumeAmount = consumeAmount;
        this.durabilityConsumeAmount = durabilityConsumeAmount;
    }

    public ItemMatchResult(boolean itemsMatch, AbstractRecipeInputItem recipeItem, ItemStack matchedItem, int consumeAmount) {
        this(itemsMatch, recipeItem, matchedItem, consumeAmount, 0);
    }
    
    /**
     * @return True if the provided items match the recipe items
     */
    public boolean itemsMatch() { return itemsMatch; }
    /**
     * @return The item in the recipe that was being matched to
     */
    public AbstractRecipeInputItem getRecipeItem() { return recipeItem; }
    /**
     * @return The item provided that was being matched
     */
    @Nullable
    public ItemStack getMatchedItem() { return matchedItem; }
    /**
     * @return How much of the item to consume when crafting
     */
    public int getConsumeAmount() {
        return consumeAmount;
    }
    public int getDurabilityConsumeAmount() {
        return durabilityConsumeAmount;
    }
    
}
