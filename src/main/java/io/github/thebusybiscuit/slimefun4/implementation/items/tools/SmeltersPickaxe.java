package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SmeltersPickaxe extends SimpleSlimefunItem<BlockBreakHandler> implements DamageableItem {

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
                    if (!Slimefun.hasUnlocked(e.getPlayer(), SmeltersPickaxe.this, true)) {
                        return true;
                    }

                    if (BlockStorage.hasBlockInfo(e.getBlock())) {
                        return true;
                    }

                    Collection<ItemStack> blockDrops = e.getBlock().getDrops(getItem());

                    for (ItemStack drop : blockDrops) {
                        if (drop != null && drop.getType() != Material.AIR) {
                            smelt(e.getBlock(), drop, fortune);
                            drops.add(drop);
                        }
                    }

                    damageItem(e.getPlayer(), item);

                    return true;
                }
                else return false;
            }
        };
    }

    private void smelt(Block b, ItemStack drop, int fortune) {
        Optional<ItemStack> furnaceOutput = SlimefunPlugin.getMinecraftRecipeService().getFurnaceOutput(drop);

        if (furnaceOutput.isPresent()) {
            b.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
            drop.setType(furnaceOutput.get().getType());
            drop.setAmount(fortune);
        }
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

}
