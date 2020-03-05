package io.github.thebusybiscuit.slimefun4.implementation.items;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * A quick and easy implementation of {@link SlimefunItem} that also implements the
 * interface {@link Radioactive}.
 * 
 * Simply specify a level of {@link Radioactivity} in the constructor.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Radioactive
 * @see Radioactivity
 *
 */
public class RadioactiveItem extends SlimefunItem implements Radioactive {

    private final Radioactivity radioactivity;

    public RadioactiveItem(Category category, Radioactivity radioactivity, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        this.radioactivity = radioactivity;
    }

    @Override
    public Radioactivity getRadioactivity() {
        return radioactivity;
    }

}
