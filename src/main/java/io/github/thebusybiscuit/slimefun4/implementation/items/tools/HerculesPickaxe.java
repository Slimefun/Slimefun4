package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class HerculesPickaxe extends SimpleSlimefunItem<BlockBreakHandler> {

    public HerculesPickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockBreakHandler getItemHandler() {
        return new BlockBreakHandler() {

            @Override
            public boolean isPrivate() {
                return false;
            }

            @Override
            public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
                if (e.getBlock().getType().toString().endsWith("_ORE") && isItem(item)) {
                    if (!Slimefun.hasUnlocked(e.getPlayer(), HerculesPickaxe.this, true)) {
                        return true;
                    }

                    if (e.getBlock().getType() == Material.IRON_ORE) {
                        drops.add(new CustomItem(SlimefunItems.IRON_DUST, 2));
                    }
                    else if (e.getBlock().getType() == Material.GOLD_ORE) {
                        drops.add(new CustomItem(SlimefunItems.GOLD_DUST, 2));
                    }
                    else {
                        for (ItemStack drop : e.getBlock().getDrops(getItem())) {
                            drops.add(new CustomItem(drop, drop.getAmount() * 2));
                        }
                    }

                    return true;
                }
                else return false;
            }
        };
    }

}
