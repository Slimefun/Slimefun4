package io.github.thebusybiscuit.slimefun4.api.recipes.components;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class MultiItemComponent implements RecipeComponent {

    private final List<ItemStack> choices = new ArrayList<>();
    private final boolean disabled;

    public MultiItemComponent(List<ItemStack> choices) {
        Preconditions.checkArgument(!choices.isEmpty(), "The 'choices' list must be non-empty");

        for (final ItemStack choice : choices) {
            if (choice instanceof final SlimefunItemStack sfStack) {
                final SlimefunItem sfItem = SlimefunItem.getById(sfStack.getItemId());
                if (sfItem != null && sfItem.isDisabled()) {
                    continue;
                }
            }

            this.choices.add(choice);
        }

        this.disabled = this.choices.size() == 0;
    }

    @Override
    public boolean isAir() {
        return disabled ? true : choices.get(0).getType().isAir();
    }

    @Override
    public boolean isDisabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getAmount() {
        return disabled ? 0 : choices.get(0).getAmount();
    }

    @Override
    public boolean matches(ItemStack givenItem) {
        for (final ItemStack item : choices) {
            if (SlimefunUtils.isItemSimilar(givenItem, item, true)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ItemStack getDisplayItem() {
        // TODO: Guide Display
        return choices.get(0);
    }
    
}
