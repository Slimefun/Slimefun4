package io.github.thebusybiscuit.slimefun4.implementation.items.altar;

import io.github.thebusybiscuit.slimefun4.implementation.listeners.AncientAltarListener;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class AncientPedestal extends SlimefunItem {

    public AncientPedestal(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        SlimefunItem.registerBlockHandler(getID(), (p, b, tool, reason) -> {
            Item stack = AncientAltarListener.findItem(b);

            if (SlimefunPlugin.getCfg().getBoolean("options.anti-altar-glitch")) {
                if (stack == null) {
                    return true;
                } else {
                    SlimefunPlugin.getLocal().sendMessage(p, "machines.ANCIENT_PEDESTAL.in-use");
                    return false;
                }
            } else {
                if (stack != null) {
                    stack.removeMetadata("item_placed", SlimefunPlugin.instance);
                    b.getWorld().dropItem(b.getLocation(), AncientAltarListener.fixItemStack(stack.getItemStack(), stack.getCustomName()));
                    stack.remove();
                }
                return true;
            }
        });
    }
}
