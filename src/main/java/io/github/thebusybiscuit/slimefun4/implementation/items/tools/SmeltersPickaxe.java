package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link SmeltersPickaxe} automatically smelts any ore you mine.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SmeltersPickaxe extends SimpleSlimefunItem<ToolUseHandler> implements DamageableItem {

    @ParametersAreNonnullByDefault
    public SmeltersPickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public @Nonnull ToolUseHandler getItemHandler() {
        return (e, tool, fortune, drops) -> {
            Block b = e.getBlock();

            if (SlimefunTag.SMELTERS_PICKAXE_BLOCKS.isTagged(b.getType()) && !BlockStorage.hasBlockInfo(b)) {
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
        Optional<ItemStack> furnaceOutput = SlimefunPlugin.getMinecraftRecipeService().getFurnaceOutput(drop);

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
