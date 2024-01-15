package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

public class ItemOutput implements RecipeOutput {

    private final ItemStack output;
    private final boolean disabled;
    private final @Nullable String slimefunID;

    public ItemOutput(ItemStack output) {
        this.output = output == null ? new ItemStack(Material.AIR) : output;
        if (this.output.getType().isAir()) {
            slimefunID = null;
            disabled = false;
        } else {
            SlimefunItem sfItem = SlimefunItem.getByItem(output);
            slimefunID = sfItem != null ? sfItem.getId() : null;
            disabled = sfItem != null ? sfItem.isDisabled() : false;
        }
    }

    @Override
    public ItemStack generateOutput() {
        return output.clone();
    }

    public ItemStack getOutputTemplate() {
        return output;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + output.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return Objects.equals(((ItemOutput) obj).getOutputTemplate(), getOutputTemplate());
    }

    @Override
    public int hashCode() {
        return output.hashCode();
    }

    @Override
    public List<String> getSlimefunItemIDs() {
        return slimefunID == null ? Collections.emptyList() : List.of(slimefunID);
    }

    @Override
    public ItemStack asDisplayItem() {
        return output.clone();
    }

    @Override
    public ItemStack asDisplayItem(String slimefunID) {
        return slimefunID.equals(this.slimefunID) ? asDisplayItem() : new ItemStack(Material.AIR);
    }

}
