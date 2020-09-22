package io.github.thebusybiscuit.slimefun4.implementation.items.backpacks;

import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * This implementation of {@link SlimefunBackpack} for Radioactive Items.
 *
 * @author TheSilentPro
 *
 */
public class ContainmentBackpack extends SlimefunBackpack {

    public ContainmentBackpack(int size, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(size, category, item, recipeType, recipe);
    }

    @Override
    public boolean isItemAllowed(ItemStack item, SlimefunItem itemAsSlimefunItem) {
        // Shulker Boxes are not allowed!
        if (item.getType() == Material.SHULKER_BOX || item.getType().toString().endsWith("_SHULKER_BOX")) {
            return false;
        }

        return !(itemAsSlimefunItem instanceof SlimefunBackpack);
    }

}
