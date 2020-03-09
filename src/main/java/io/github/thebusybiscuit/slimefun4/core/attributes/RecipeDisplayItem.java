package io.github.thebusybiscuit.slimefun4.core.attributes;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface RecipeDisplayItem {

    List<ItemStack> getDisplayRecipes();

    default String getLabelLocalPath() {
        return "guide.tooltips.recipes.machine";
    }

    default String getRecipeSectionLabel(Player p) {
        return "&7\u21E9 " + SlimefunPlugin.getLocal().getMessage(p, getLabelLocalPath()) + " \u21E9";
    }
}