package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Axis;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.blocks.Vein;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * The {@link LumberAxe} is a powerful tool which can chop entire trees.
 * Breaking a log will result in all attached logs being broken as well.
 * Similarly stripping a log will strip all attached logs too.
 * 
 * @author TheBusyBiscuit
 *
 */
public class LumberAxe extends SlimefunItem implements NotPlaceable {

    private static final int MAX_BROKEN = 100;
    private static final int MAX_STRIPPED = 20;

    @ParametersAreNonnullByDefault
    public LumberAxe(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(onBlockBreak(), onItemUse());
    }

    @Nonnull
    private ToolUseHandler onBlockBreak() {
        return (e, tool, fortune, drops) -> {
            if (!e.getPlayer().isSneaking() && Tag.LOGS.isTagged(e.getBlock().getType())) {
                List<Block> logs = Vein.find(e.getBlock(), MAX_BROKEN, b -> Tag.LOGS.isTagged(b.getType()));
                logs.remove(e.getBlock());

                for (Block b : logs) {
                    if (!BlockStorage.hasBlockInfo(b) && Slimefun.getProtectionManager().hasPermission(e.getPlayer(), b, Interaction.BREAK_BLOCK)) {
                        breakLog(b);
                    }
                }
            }
        };
    }

    @Nonnull
    public ItemUseHandler onItemUse() {
        return e -> {
            if (e.getClickedBlock().isPresent()) {
                Block block = e.getClickedBlock().get();

                if (isUnstrippedLog(block)) {
                    List<Block> logs = Vein.find(block, MAX_STRIPPED, this::isUnstrippedLog);

                    if (logs.contains(block)) {
                        logs.remove(block);
                    }

                    for (Block b : logs) {
                        if (!BlockStorage.hasBlockInfo(b) && Slimefun.getProtectionManager().hasPermission(e.getPlayer(), b, Interaction.BREAK_BLOCK)) {
                            stripLog(b);
                        }
                    }
                }
            }
        };
    }

    private boolean isUnstrippedLog(@Nonnull Block block) {
        return Tag.LOGS.isTagged(block.getType()) && !block.getType().name().startsWith("STRIPPED_");
    }

    private void stripLog(@Nonnull Block b) {
        b.getWorld().playSound(b.getLocation(), Sound.ITEM_AXE_STRIP, 1, 1);
        Axis axis = ((Orientable) b.getBlockData()).getAxis();
        b.setType(Material.valueOf("STRIPPED_" + b.getType().name()));

        Orientable orientable = (Orientable) b.getBlockData();
        orientable.setAxis(axis);
        b.setBlockData(orientable);
    }

    private void breakLog(@Nonnull Block b) {
        b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

        for (ItemStack drop : b.getDrops(getItem())) {
            b.getWorld().dropItemNaturally(b.getLocation(), drop);
        }

        b.setType(Material.AIR);
    }

}
