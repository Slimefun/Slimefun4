package io.github.thebusybiscuit.slimefun4.api.recipes.components;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;

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
        public List<ItemStack> getDisplayItems() {
            return List.of(new ItemStack(Material.AIR));
        }
        
        @Override
        public String toString() {
            return "EMPTY";
        }

        @Override
        public boolean equals(Object obj) {
            return obj == null || obj == RecipeComponent.AIR;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public List<String> getSlimefunItemIDs() {
            return Collections.emptyList();
        }
        
    };

    public static @Nonnull RecipeComponent of(@Nonnull String slimefunItemId) {
        SlimefunItem item = SlimefunItem.getById(slimefunItemId);

        if (item == null) {
            return AIR;
        }

        if (item instanceof DistinctiveItem) {
            return new DistinctiveComponent(item.getItem());
        }

        return new ItemComponent(item.getItem());
    }

    public static @Nonnull RecipeComponent of(@Nullable ItemStack item) {
        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem != null && sfItem instanceof DistinctiveItem) {
            return new DistinctiveComponent(item);
        }

        return item == null ? AIR : new ItemComponent(item);
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
     * Consumes the item when crafting
     * @param item The item to consume
     */
    public default void consume(@Nonnull ItemStack item) {
        ItemUtils.consumeItem(item, getAmount(), true);
    }

    /**
     * @return An {@link ItemStack} for display purposes (e.g. in the guide)
     */
    public @Nonnull List<ItemStack> getDisplayItems();

    public List<String> getSlimefunItemIDs();

}
