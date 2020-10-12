package io.github.thebusybiscuit.slimefun4.implementation.items.misc;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Cow;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SteelThruster extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public SteelThruster(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(onRightClickBlock(), onRightClickEntity());
    }

    @Nonnull
    private ItemUseHandler onRightClickBlock() {
        return PlayerRightClickEvent::cancel;
    }

    @Nonnull
    private EntityInteractHandler onRightClickEntity() {
        return (e, item, hand) -> {
            // Milking cows with a rocket engine? Yeah, that would be weird.
            if (e.getRightClicked() instanceof Cow) {
                e.setCancelled(true);
            }
        };
    }

}