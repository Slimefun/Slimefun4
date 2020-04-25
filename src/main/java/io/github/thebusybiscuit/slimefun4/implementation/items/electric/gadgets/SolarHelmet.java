package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SolarHelmet extends SlimefunItem {

    private final ItemSetting<Double> chargeSetting = new ItemSetting<>("charge-amount", 0.1);

    public SolarHelmet(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe, null);

        addItemSetting(chargeSetting);
    }

    public double getChargeAmount() {
        return chargeSetting.getValue();
    }

}
