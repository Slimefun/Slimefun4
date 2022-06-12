package io.github.thebusybiscuit.slimefun4.implementation.operations;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.utils.ValidateUtils;

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
        ValidateUtils.notEmpty(ingredients, "The Ingredients array cannot be empty or null");
        ValidateUtils.notEmpty(results, "The results array cannot be empty or null");
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
