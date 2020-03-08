package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.bukkit.Effect;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.recipes.MinecraftRecipe;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SmeltersPickaxe extends SimpleSlimefunItem<BlockBreakHandler> {

    public SmeltersPickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
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
                if (MaterialCollections.getAllOres().contains(e.getBlock().getType()) && isItem(item)) {
                    if (BlockStorage.hasBlockInfo(e.getBlock())) return true;
                    if (!Slimefun.hasUnlocked(e.getPlayer(), SmeltersPickaxe.this, true)) return true;

                    Collection<ItemStack> blockDrops = e.getBlock().getDrops(getItem());
                    for (ItemStack drop : blockDrops) {
                        if (drop != null) {
                            ItemStack output = drop;
                            output.setAmount(fortune);

                            Optional<ItemStack> furnaceOutput = SlimefunPlugin.getMinecraftRecipes().getRecipeOutput(MinecraftRecipe.FURNACE, drop);
                            if (furnaceOutput.isPresent()) {
                                e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                                output.setType(furnaceOutput.get().getType());
                            }

                            drops.add(output);
                        }
                    }

                    return true;
                }
                else return false;
            }
        };
    }

}
