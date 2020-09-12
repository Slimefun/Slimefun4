package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.Collection;
import java.util.Optional;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SmeltersPickaxe extends SimpleSlimefunItem<ToolUseHandler> implements DamageableItem {

    public SmeltersPickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ToolUseHandler getItemHandler() {
        return (e, tool, fortune, drops) -> {
            if (MaterialCollections.getAllOres().contains(e.getBlock().getType()) && !BlockStorage.hasBlockInfo(e.getBlock())) {
                Collection<ItemStack> blockDrops = e.getBlock().getDrops(getItem());

                for (ItemStack drop : blockDrops) {
                    if (drop != null && drop.getType() != Material.AIR) {
                        smelt(e.getBlock(), drop, fortune);
                        drops.add(drop);
                    }
                }

                damageItem(e.getPlayer(), tool);
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
