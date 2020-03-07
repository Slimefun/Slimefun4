package io.github.thebusybiscuit.slimefun4.core.attributes;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public interface RecipeDisplayItem {

    List<ItemStack> getDisplayRecipes();

    default String getLabelLocalPath() {
        return "guide.tooltips.recipes.machine";
    }

    default String getRecipeSectionLabel(Player p) {
        return "&7\u21E9 " + SlimefunPlugin.getLocal().getMessage(p, getLabelLocalPath()) + " \u21E9";
    }
}
