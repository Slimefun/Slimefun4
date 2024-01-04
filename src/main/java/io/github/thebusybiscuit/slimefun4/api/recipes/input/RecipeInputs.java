package io.github.thebusybiscuit.slimefun4.api.recipes.input;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeStructure;
import io.github.thebusybiscuit.slimefun4.api.recipes.components.RecipeComponent;

public interface RecipeInputs {

    public static final RecipeInputs EMPTY = new RecipeInputs() {

        @Override
        public RecipeMatchResult match(RecipeStructure structure, ItemStack[] givenItems) {
            return RecipeMatchResult.NO_MATCH;
        }

        @Override
        public List<RecipeComponent> getComponents() {
            return Collections.emptyList();
        }

        @Override
        public boolean isDisabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public RecipeStructure getStructure() {
            return RecipeStructure.IDENTICAL;
        }

        @Override
        public ItemStack[] asDisplayGrid() {
            return new ItemStack[9];
        }

    };

    public static RecipeInputs of(RecipeStructure structure, ItemStack... items) {
        return new ItemInputs(structure, Arrays.stream(items).map(item -> RecipeComponent.of(item)).toList());
    }

    /**
     * Match a set of given items against these inputs with some structure
     * 
     * @param structure  The structure of the recipe
     * @param givenItems The items to match
     * @return The result of the match. See {@link RecipeMatchResult}
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public RecipeMatchResult match(RecipeStructure structure, ItemStack[] givenItems);

    /**
     * @return The {@link RecipeComponents} in this input
     */
    public List<RecipeComponent> getComponents();

    /**
     * @return Whether or not any of the components in this input are disabled
     */
    public boolean isDisabled();

    /**
     * @return Whether or not this input is empty
     */
    public boolean isEmpty();

    /**
     * @return The structure of this input
     */
    public @Nonnull RecipeStructure getStructure();

    /**
     * @return A 9-item (3x3) {@link ItemStack} array for display purposes (e.g. in the guide)
     */
    public ItemStack[] asDisplayGrid();

}
