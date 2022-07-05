package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SmeltersTool extends SimpleSlimefunItem<ToolUseHandler> implements DamageableItem {

    @ParametersAreNonnullByDefault
    public SmeltersTool(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public @Nonnull
    ToolUseHandler getItemHandler() {
        return (e, tool, fortune, drops) -> {
            Block b = e.getBlock();
            ItemStack stack = e.getPlayer().getInventory().getItemInMainHand();

            if (SlimefunTag.PICKAXES.isTagged(stack.getType()) && SlimefunTag.SMELTERS_PICKAXE_BLOCKS.isTagged(b.getType()) && !BlockStorage.hasBlockInfo(b)) {
                Collection<ItemStack> blockDrops = b.getDrops(tool);

                for (ItemStack drop : blockDrops) {
                    if (drop != null && !drop.getType().isAir()) {
                        smelt(b, drop, fortune);
                        drops.add(drop);
                    }
                }

                damageItem(e.getPlayer(), tool);
            }
            if (SlimefunTag.SHOVELS.isTagged(stack.getType()) && SlimefunTag.SMELTERS_PICKAXE_BLOCKS.isTagged(b.getType()) && !BlockStorage.hasBlockInfo(b)) {
                Collection<ItemStack> blockDrops = b.getDrops(tool);

                for (ItemStack drop : blockDrops) {
                    if (drop != null && !drop.getType().isAir()) {
                        smelt(b, drop, fortune);
                        drops.add(drop);
                    }
                }

                damageItem(e.getPlayer(), tool);
            }
            if (SlimefunTag.AXES.isTagged(stack.getType()) && SlimefunTag.SMELTERS_PICKAXE_BLOCKS.isTagged(b.getType()) && !BlockStorage.hasBlockInfo(b)) {
                Collection<ItemStack> blockDrops = b.getDrops(tool);

                for (ItemStack drop : blockDrops) {
                    if (drop != null && !drop.getType().isAir()) {
                        smelt(b, drop, fortune);
                        drops.add(drop);
                    }
                }

                damageItem(e.getPlayer(), tool);
            }
        };
    }

    @ParametersAreNonnullByDefault
    private void smelt(Block b, ItemStack drop, int fortune) {
        Optional<ItemStack> furnaceOutput = Slimefun.getMinecraftRecipeService().getFurnaceOutput(drop);

        if (furnaceOutput.isPresent()) {
            b.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
            drop.setType(furnaceOutput.get().getType());
        }

        // Fixes #3116
        drop.setAmount(fortune);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

}
