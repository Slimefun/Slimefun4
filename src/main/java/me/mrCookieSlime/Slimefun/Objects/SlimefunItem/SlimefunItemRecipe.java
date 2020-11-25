package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class represents a single recipe of an {@link SlimefunItem}
 * 
 * @author Mooy1
 * 
 * @see SlimefunItem
 * @see RecipeType
 *
 */
public class SlimefunItemRecipe {
    
    @Nonnull
    private final RecipeType type;
    @Nonnull
    private final ItemStack[] input;
    @Nullable
    private final ItemStack output;
    
    public SlimefunItemRecipe(@Nonnull RecipeType recipeType, @Nullable ItemStack[] input, @Nullable ItemStack output) {
        Validate.notNull(recipeType, "'recipeType' is not allowed to be null!!");
        this.type = recipeType;
        this.output = output;
        
        if (input == null || input.length < 9) {
            this.input = new ItemStack[9];
        } else {
            this.input = input;
        }
    }
    
    @Nonnull
    public RecipeType getType() {
        return this.type;
    }
    
    @Nonnull
    public ItemStack[] getInput() {
        return this.input;
    }
    
    @Nullable
    public ItemStack getOutput() {
        return this.output;
    }
    
    public void enable() { 
        this.type.register(this.input, this.output);
    }
    
}
