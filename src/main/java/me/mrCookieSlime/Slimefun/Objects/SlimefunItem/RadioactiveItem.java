package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

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