package io.github.thebusybiscuit.slimefun4.implementation.items.magical.runes;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientAltar;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An {@link ElementalRune} is a very simple and basic crafting component
 * used to craft various magical gadgets.
 * <p>
 * These runes are crafted using an {@link AncientAltar}.
 *
 * @author TheBusyBiscuit
 */
public class ElementalRune extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public ElementalRune(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
        this(itemGroup, item, recipe, null);
    }

    @ParametersAreNonnullByDefault
    public ElementalRune(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, @Nullable ItemStack recipeResult) {
        super(itemGroup, item, RecipeType.ANCIENT_ALTAR, recipe, recipeResult);
    }

}
