package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.ItemMatchResult;

public abstract class AbstractRecipeInputItem implements Cloneable {

    protected abstract ItemMatchResult matchItem(@Nullable ItemStack item, @Nonnull AbstractRecipeInputItem root);

    @Nonnull
    public ItemMatchResult matchItem(@Nullable ItemStack item) {
        return matchItem(item, clone());
    }

    public abstract boolean isEmpty();

    @Override
    public abstract AbstractRecipeInputItem clone();

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

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
