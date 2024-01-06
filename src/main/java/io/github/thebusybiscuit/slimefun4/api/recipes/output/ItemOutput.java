package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

public class ItemOutput implements RecipeOutput {

    private final ItemStack output;
    private final boolean disabled;
    private final @Nullable String slimefunID;

    public ItemOutput(ItemStack output) {
        this.output = output;
        if (output == null || output.getType().isAir()) {
            slimefunID = null;
            disabled = true;
        } else {
            final SlimefunItem sfItem = SlimefunItem.getByItem(output);
            if (sfItem != null) {
                slimefunID = sfItem.getId();
                if (sfItem != null && sfItem.isDisabled()) {
                    disabled = true;
                } else {
                    disabled = false;
                }
            } else {
                disabled = false;
                slimefunID = null;
            }
        }
    }

    @Override
    public ItemStack generateOutput() {
        return output.clone();
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public String toString() {
        return output.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return ((ItemOutput) obj).getOutputTemplate().equals(output);
    }

    @Override
    public int hashCode() {
        return output.hashCode();
    }

    @Override
    public List<String> getSlimefunItemIDs() {
        return slimefunID == null ? Collections.emptyList() : List.of(slimefunID);
    }

}
