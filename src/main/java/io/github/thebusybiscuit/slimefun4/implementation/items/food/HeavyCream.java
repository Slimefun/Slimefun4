package io.github.thebusybiscuit.slimefun4.implementation.items.food;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * This {@link SlimefunItem} can be obtained by crafting and, it's
 * used for various food recipes recipes
 *
 * @author TheSilentPro
 */
public class HeavyCream extends SlimefunItem {

    public HeavyCream(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public void preRegister() {
        addItemHandler((ItemUseHandler) PlayerRightClickEvent::cancel);
    }

}
