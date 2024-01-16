package io.github.thebusybiscuit.slimefun4.api.recipes.input;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeStructure;
import io.github.thebusybiscuit.slimefun4.api.recipes.components.RecipeComponent;

public class ItemInputs implements RecipeInputs {

    private final List<RecipeComponent> components;
    private RecipeStructure structure;
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
        return components;
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
    public void setStructure(RecipeStructure structure) {
        this.structure = structure;
    }

    @Override
    public RecipeStructure getStructure() {
        return structure;
    }

    @Override
    public ItemStack[] asDisplayGrid() {
        ItemStack[] displayGrid = new ItemStack[9];
        int numItems = Math.min(9, components.size());
        for (int i = 0; i < numItems; i++) {
            List<ItemStack> displayItems = components.get(i).getDisplayItems();
            displayGrid[i] = displayItems.size() > 0 ? displayItems.get(0) : new ItemStack(Material.AIR);
        }
        return displayGrid;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + structure + ", " + components.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ItemInputs other = (ItemInputs) obj;
        return other.getComponents().equals(components) && other.getStructure().equals(structure);
    }

    @Override
    public int hashCode() {
        return components.hashCode() * 31 + structure.hashCode();
    }
    
}
