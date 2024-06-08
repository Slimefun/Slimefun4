package io.github.thebusybiscuit.slimefun4.api.recipes.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

public class MultiItemComponent implements RecipeComponent {

    private final List<RecipeComponent> choices = new ArrayList<>();
    private final boolean disabled;
    private final List<String> slimefunIDs = new ArrayList<>();

    public MultiItemComponent(@Nonnull List<RecipeComponent> choices) {
        Preconditions.checkNotNull(choices, "'choices' cannot be null!");
        Preconditions.checkArgument(!choices.isEmpty(), "'choices' must be non-empty");

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

    public MultiItemComponent(@Nonnull ItemStack... choices) {
        this(Arrays.stream(choices).map(choice -> choice == null ? RecipeComponent.AIR : new ItemComponent(choice)).toList());
    }

    public MultiItemComponent(@Nonnull Material... materials) {
        Preconditions.checkNotNull(materials, "'materials' cannot be null!");
        Preconditions.checkArgument(materials.length > 0, "'materials' must be non-empty");

        for (final Material material : materials) {
            this.choices.add(new ItemComponent(material));
        }

        this.disabled = false;
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
    public List<ItemStack> getDisplayItems() {
        List<ItemStack> displayItems = new ArrayList<>();
        for (RecipeComponent choice : choices) {
            displayItems.addAll(choice.getDisplayItems());
        }
        return displayItems;
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
