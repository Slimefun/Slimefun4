package io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link WaterStaff} is a magical {@link SlimefunItem}.
 * When you right click it, any fire on your {@link Player} will be extinguished.
 * 
 * @author TheBusyBiscuit
 *
 */
public class WaterStaff extends SimpleSlimefunItem<ItemUseHandler> {

    @ParametersAreNonnullByDefault
    public WaterStaff(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();

            p.setFireTicks(0);
            SlimefunPlugin.getLocalization().sendMessage(p, "messages.fire-extinguish", true);
        };
    }

}
