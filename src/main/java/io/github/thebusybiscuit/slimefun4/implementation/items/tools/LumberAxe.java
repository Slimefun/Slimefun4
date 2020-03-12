package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import io.github.starwishsama.utils.ResidenceChecker;
import io.github.thebusybiscuit.cscorelib2.blocks.Vein;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LumberAxe extends SimpleSlimefunItem<BlockBreakHandler> implements NotPlaceable {

    public LumberAxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
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
                if (MaterialCollections.getAllLogs().contains(e.getBlock().getType()) && isItem(item)) {
                    if (!Slimefun.hasUnlocked(e.getPlayer(), LumberAxe.this, true)) {
                        return true;
                    }

                    List<Block> logs = Vein.find(e.getBlock(), 100, b -> MaterialCollections.getAllLogs().contains(b.getType()));

                    logs.remove(e.getBlock());

                    for (Block b : logs) {
                        if (SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK)
                                && ResidenceChecker.check(e.getPlayer(), b, false)) {
                            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

                            for (ItemStack drop : b.getDrops(getItem())) {
                                b.getWorld().dropItemNaturally(b.getLocation(), drop);
                            }

                            b.setType(Material.AIR);
                        }
                    }

                    return true;
                } else return false;
            }
        };
    }

}