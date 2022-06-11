package io.github.thebusybiscuit.slimefun4.implementation.operations;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;

/**
 * This {@link MachineOperation} represents a crafting process.
 * 
 * @author TheBusyBiscuit
 *
 */
public class CraftingOperation implements MachineOperation {

    private final ItemStack[] ingredients;
    private final ItemStack[] results;

    private final int totalTicks;
    private int currentTicks = 0;

    public CraftingOperation(@Nonnull MachineRecipe recipe) {
        this(recipe.getInput(), recipe.getOutput(), recipe.getTicks());
    }

    public CraftingOperation(@Nonnull ItemStack[] ingredients, @Nonnull ItemStack[] results, int totalTicks) {
        Preconditions.checkArgument(ingredients != null && ingredients.length > 0, "The Ingredients array cannot be empty or null");
        Preconditions.checkArgument(results != null && results.length > 0, "The results array cannot be empty or null");
        Preconditions.checkArgument(totalTicks >= 0, "The amount of total ticks must be a positive integer or zero, received: " + totalTicks);

        this.ingredients = ingredients;
        this.results = results;
        this.totalTicks = totalTicks;
    }

    @Override
    public void addProgress(int num) {
        Preconditions.checkArgument(num > 0, "Progress must be positive.");
        currentTicks += num;
    }

    public @Nonnull ItemStack[] getIngredients() {
        return ingredients;
    }

    public @Nonnull ItemStack[] getResults() {
        return results;
    }

    @Override
    public int getProgress() {
        return currentTicks;
    }

    @Override
    public int getTotalTicks() {
        return totalTicks;
    }

}
