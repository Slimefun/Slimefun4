package io.github.thebusybiscuit.slimefun4.api.recipes.components;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class ItemComponent implements RecipeComponent {

    private final ItemStack component;
    private final boolean disabled;
    private final @Nullable String slimefunID;

    public ItemComponent(@Nullable ItemStack component) {
        if (component == null) {
            this.component = new ItemStack(Material.AIR);
            disabled = false;
            slimefunID = null;
        } else {
            SlimefunItem sfItem = SlimefunItem.getByItem(component);
            this.component = component;
            slimefunID = sfItem != null ? sfItem.getId() : null;
            disabled = sfItem != null ? sfItem.isDisabled() : false;
        }
    }

    public ItemComponent(@Nonnull Material component) {
        this.component = new ItemStack(component);
        this.disabled = false;
        this.slimefunID = null;
    }

    public ItemStack getComponent() {
        return component;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public boolean isAir() {
        return component.getType().isAir();
    }

    @Override
    public int getAmount() {
        return component.getAmount();
    }

    @Override
    public boolean matches(ItemStack givenItem) {
        return SlimefunUtils.isItemSimilar(givenItem, component, true);
    }

    @Override
    public ItemStack getDisplayItem() {
        return component;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + component.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return ((ItemComponent) obj).getComponent().equals(component);
    }

    @Override
    public int hashCode() {
        return component.hashCode();
    }

    @Override
    public List<String> getSlimefunItemIDs() {
        return slimefunID == null ? Collections.emptyList() : List.of(slimefunID);
    }

}
