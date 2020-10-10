package io.github.thebusybiscuit.slimefun4.implementation.items.food;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This {@link SlimefunItem} can be obtained by crafting, it's
 * used for various foods and recipes
 *
 * @author TheSilentPro
 */
public class HeavyCream extends SimpleSlimefunItem<ItemUseHandler> {

    @ParametersAreNonnullByDefault
    public HeavyCream(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return this::use;
    }

    public void use(PlayerRightClickEvent e) {
        if (e.getClickedBlock().isPresent()) {
            if (!e.getClickedBlock().get().getType().isInteractable()) {
                e.cancel();
            }
        }
        else {
            e.cancel();
        }
    }

}
