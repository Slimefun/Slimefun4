package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.InputMatchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.MatchProcedure;
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils.BoundingBox;

public abstract class AbstractRecipeInput {

    public abstract int getWidth();
    public abstract void setWidth(int width);

    public abstract int getHeight();
    public abstract void setHeight(int height);

    public abstract boolean isEmpty();

    @Nonnull
    public abstract List<AbstractRecipeInputItem> getItems();
    public AbstractRecipeInputItem getItem(int index) {
        return getItems().get(index);
    }
    public abstract void setItems(@Nonnull List<AbstractRecipeInputItem> items);

    @Nonnull
    public abstract MatchProcedure getMatchProcedure();
    public abstract void setMatchProcedure(MatchProcedure matchProcedure);

    public abstract Optional<BoundingBox> getBoundingBox();

    public InputMatchResult match(List<ItemStack> givenItems) {
        return getMatchProcedure().match(this, givenItems);
    }

    public InputMatchResult matchAs(MatchProcedure match, List<ItemStack> givenItems) {
        return match.match(this, givenItems);
    }

    @Override
    public abstract String toString();

    public abstract JsonElement serialize(JsonSerializationContext context);

    @Override
    public abstract boolean equals(Object obj);
    
}
