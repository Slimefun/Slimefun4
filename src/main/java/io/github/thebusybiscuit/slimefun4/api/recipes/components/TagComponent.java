package io.github.thebusybiscuit.slimefun4.api.recipes.components;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

public class TagComponent implements RecipeComponent {

    private final SlimefunTag tag;

    public TagComponent(SlimefunTag tag) {
        Preconditions.checkArgument(!tag.isEmpty(), "Tag must not be empty.");

        this.tag = tag;
    }

    @Override
    public boolean isAir() {
        return tag.isTagged(Material.AIR) || tag.isTagged(Material.CAVE_AIR) || tag.isTagged(Material.VOID_AIR);
    }

    @Override
    public int getAmount() {
        return 1;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public boolean matches(ItemStack givenItem) {
        if (tag.isTagged(givenItem.getType())) {
            return ItemUtils.canStack(new ItemStack(givenItem.getType()), givenItem);
        }

        return false;
    }

    @Override
    public ItemStack getDisplayItem() {
        // TODO display item
        return new ItemStack(tag.stream().findFirst().get());
    }
    
}
