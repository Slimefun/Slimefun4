package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeMatchResult;

public abstract class AbstractRecipeOutputItem {

    public enum SpaceRequirement {
        EMPTY_SLOT, MATCHING_ITEM
    }

    /**
     * Generate the output given the match result.
     * @param result The result of this recipe being matched
     * @return Item to be output
     */
    @Nonnull
    public abstract ItemStack generateOutput(@Nonnull RecipeMatchResult result);
    public abstract boolean matchItem(@Nullable ItemStack item);
    /**
     * @return What space the item needs to be placed in the output
     */
    public SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.EMPTY_SLOT;
    }

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);

    /**
     * Used in the serialize method to determine if it can convert to a
     * JSON string (e.g. <code>minecraft:netherrack|16</code>) instead of an object
     * 
     * Should be overriden in any custom subclasses that contain extra fields
     */
    public boolean canUseShortSerialization() {
        return false;
    };

    public abstract JsonElement serialize(JsonSerializationContext context);

}

