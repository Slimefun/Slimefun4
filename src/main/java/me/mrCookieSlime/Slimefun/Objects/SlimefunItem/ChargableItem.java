package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;

/**
 * This class is deprecated.
 * 
 * @deprecated Please implement the {@link Rechargeable} interface from now on.
 * 
 * @author TheBusyBiscuit
 *
 */
@Deprecated
public class ChargableItem extends SlimefunItem implements Rechargeable {

    public ChargableItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public float getMaxItemCharge(ItemStack item) {
        return ItemEnergy.getMaxEnergy(item);
    }

}
