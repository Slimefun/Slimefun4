package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.runes.SoulboundRune;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SoulboundListener;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * Represents an Item that will not drop upon death.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Soulbound
 * @see SoulboundRune
 * @see SoulboundListener
 *
 */
public class SoulboundItem extends SlimefunItem implements Soulbound, NotPlaceable {

    @ParametersAreNonnullByDefault
    public SoulboundItem(Category category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
        super(category, item, type, recipe);
    }

}
