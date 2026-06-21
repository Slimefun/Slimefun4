package io.github.thebusybiscuit.slimefun5.implementation.items.magical.runes;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.Soulbound;
import io.github.thebusybiscuit.slimefun5.implementation.items.magical.RuneAnvil;
import io.github.thebusybiscuit.slimefun5.implementation.items.magical.SoulboundItem;

/**
 * This {@link SlimefunItem} is a crafting component which, when applied in a {@link RuneAnvil},
 * converts any {@link ItemStack} into a {@link SoulboundItem}.
 *
 * @author Linox
 * @author Walshy
 * @author TheBusyBiscuit
 *
 * @see RuneAnvil
 * @see Soulbound
 *
 */
public class SoulboundRune extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public SoulboundRune(ItemGroup itemGroup, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
        super(itemGroup, item, type, recipe);
    }
}
