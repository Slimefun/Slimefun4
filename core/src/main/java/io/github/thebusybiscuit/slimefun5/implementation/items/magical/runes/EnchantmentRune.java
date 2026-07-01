package io.github.thebusybiscuit.slimefun5.implementation.items.magical.runes;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.items.magical.RuneAnvil;

/**
 * This {@link SlimefunItem} is a crafting component which, when applied in a {@link RuneAnvil},
 * enchants any enchantable {@link ItemStack} with a random {@link Enchantment}.
 *
 * @author Linox
 *
 * @see RuneAnvil
 *
 */
public class EnchantmentRune extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public EnchantmentRune(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }
}
