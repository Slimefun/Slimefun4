package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class HerculesPickaxe extends SimpleSlimefunItem<ToolUseHandler> {

    @ParametersAreNonnullByDefault
    public HerculesPickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public @Nonnull ToolUseHandler getItemHandler() {
        return (e, tool, fortune, drops) -> {
            if (SlimefunTag.ORES.isTagged(e.getBlock().getType())) {
                if (e.getBlock().getType() == Material.IRON_ORE) {
                    drops.add(new CustomItem(SlimefunItems.IRON_DUST, 2));
                } else if (e.getBlock().getType() == Material.GOLD_ORE) {
                    drops.add(new CustomItem(SlimefunItems.GOLD_DUST, 2));
                } else {
                    for (ItemStack drop : e.getBlock().getDrops(tool)) {
                        drops.add(new CustomItem(drop, drop.getAmount() * 2));
                    }
                }
            }
        };
    }

}
