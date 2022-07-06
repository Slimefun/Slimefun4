package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
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

            if (isValid(tool, b)) {
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

    private boolean isValid(@Nonnull ItemStack tool, @Nonnull Block b) {
        if (BlockStorage.hasBlockInfo(b)) {
            return false;
        } else {
            Material toolMaterial = tool.getType();
            Material blockMaterial = b.getType();
            if (SlimefunTag.PICKAXES.isTagged(toolMaterial) && SlimefunTag.SMELTERS_PICKAXE_BLOCKS.isTagged(blockMaterial)) {
                return true;
            } else if (SlimefunTag.SHOVELS.isTagged(toolMaterial) && SlimefunTag.SMELTERS_SHOVEL_BLOCKS.isTagged(blockMaterial)) {
                return true;
            } else if (SlimefunTag.AXES.isTagged(toolMaterial) && SlimefunTag.SMELTERS_AXE_BLOCKS.isTagged(blockMaterial)) {
                return true;
            } else {
                return false;
            }
        }
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
