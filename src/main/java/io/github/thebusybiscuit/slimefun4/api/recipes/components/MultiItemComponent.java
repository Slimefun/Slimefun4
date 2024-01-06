package io.github.thebusybiscuit.slimefun4.api.recipes.components;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

public class MultiItemComponent implements RecipeComponent {

    private final List<RecipeComponent> choices = new ArrayList<>();
    private final boolean disabled;
    private final List<String> slimefunIDs = new ArrayList<>();

    public MultiItemComponent(List<RecipeComponent> choices) {
        Preconditions.checkArgument(!choices.isEmpty(), "The 'choices' list must be non-empty");

        for (final RecipeComponent choice : choices) {
            if (choice.isDisabled()) {
                continue;
            }

            this.choices.add(choice);
        }

        this.disabled = this.choices.size() == 0;
        
        for (final RecipeComponent choice : choices) {
            slimefunIDs.addAll(choice.getSlimefunItemIDs());
        }
    }

    public List<RecipeComponent> getChoices() {
        return choices;
    }

    @Override
    public boolean isAir() {
        return disabled ? true : choices.get(0).isAir();
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public int getAmount() {
        return disabled ? 0 : choices.get(0).getAmount();
    }

    @Override
    public boolean matches(ItemStack givenItem) {
        for (final RecipeComponent item : choices) {
            if (item.matches(givenItem)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ItemStack getDisplayItem() {
        // TODO: Guide Display
        return choices.get(0).getDisplayItem();
    }

    @Override
    public String toString() {
        return choices.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return ((MultiItemComponent) obj).getChoices().equals(choices);
    }

    @Override
    public int hashCode() {
        return choices.hashCode();
    }

    @Override
    public List<String> getSlimefunItemIDs() {
        return slimefunIDs;
    }
    
}
