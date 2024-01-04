package io.github.thebusybiscuit.slimefun4.api.recipes.input;

import java.util.Collections;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeStructure;
import io.github.thebusybiscuit.slimefun4.api.recipes.components.RecipeComponent;

public class ItemInputs implements RecipeInputs {

    private final List<RecipeComponent> components;
    private final RecipeStructure structure;
    private final boolean disabled;
    private final boolean empty;

    /**
     * The constructor for a basic {@link RecipeInputs} object.
     * 
     * @param structure
     * @param components
     */
    public ItemInputs(RecipeStructure structure, List<RecipeComponent> components) {
        this.structure = structure;
        this.components = components;
        boolean disabled = false;
        boolean empty = true;
        for (final RecipeComponent component : components) {
            if (component.isDisabled()) {
                disabled = true;
            }
            if (!component.isAir()) {
                empty = false;
            }
        }
        this.disabled = disabled;
        this.empty = empty;
    }

    @Override
    public RecipeMatchResult match(RecipeStructure structure, ItemStack[] givenItems) {
        return structure.match(givenItems, components);
    }

    public List<RecipeComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public RecipeStructure getStructure() {
        return structure;
    }

    @Override
    public ItemStack[] asDisplayGrid() {
        final ItemStack[] displayGrid = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            displayGrid[i] = components.get(i).getDisplayItem();
        }
        return displayGrid;
    }
    
}
