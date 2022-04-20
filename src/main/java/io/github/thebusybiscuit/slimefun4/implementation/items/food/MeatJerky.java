package io.github.thebusybiscuit.slimefun4.implementation.items.food;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemConsumptionHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * {@link MeatJerky} is just a piece of meat that gives some extra saturation.
 * {@link MeatJerky} is available for all meat variants.
 *
 * @author TheBusyBiscuit
 * @see MonsterJerky
 */
public class MeatJerky extends SimpleSlimefunItem<ItemConsumptionHandler> {

    private final ItemSetting<Integer> saturation = new IntRangeSetting(this, "saturation-level", 0, 6, Integer.MAX_VALUE);

    @ParametersAreNonnullByDefault
    public MeatJerky(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemSetting(saturation);
    }

    @Override
    public ItemConsumptionHandler getItemHandler() {
        return (e, p, item) -> p.setSaturation(p.getSaturation() + saturation.getValue());
    }

}
