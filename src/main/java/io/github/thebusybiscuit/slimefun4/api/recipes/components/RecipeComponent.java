package io.github.thebusybiscuit.slimefun4.api.recipes.components;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

public interface RecipeComponent {

    public static RecipeComponent AIR = new RecipeComponent() {

        @Override
        public boolean isAir() {
            return true;
        }

        @Override
        public boolean isDisabled() {
            return false;
        }

        @Override
        public int getAmount() {
            return 0;
        }

        @Override
        public boolean matches(ItemStack givenItem) {
            return givenItem == null || givenItem.getType().isAir();
        }

        @Override
        public ItemStack getDisplayItem() {
            return null;
        }
        
    };

    public static @Nonnull RecipeComponent of(@Nonnull String slimefunItemId) {
        final SlimefunItem item = SlimefunItem.getById(slimefunItemId);
        return item == null ? AIR : of(item.getItem());
    }

    public static @Nonnull RecipeComponent of(@Nullable ItemStack item) {
        return item == null || item.getType().isAir() ? AIR : new ItemComponent(item);
    }

    /**
     * @return If this item is disabled and cannot be used in a recipe
     */
    public boolean isDisabled();

    /**
     * @return If this item is equivalent to an empty slot
     */
    public boolean isAir();

    /**
     * @return The amount this component needs in the recipe
     */
    public int getAmount();
    
    /**
     * @param givenItem The item to match
     * @return If the given item can be used as this component in a recipe
     */
    public boolean matches(@Nullable ItemStack givenItem);

    /**
     * @return An {@link ItemStack} for display purposes (e.g. in the guide)
     */
    public @Nullable ItemStack getDisplayItem();


}
