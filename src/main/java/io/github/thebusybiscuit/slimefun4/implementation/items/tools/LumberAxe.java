package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.List;

import org.bukkit.Axis;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.blocks.Vein;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class LumberAxe extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public LumberAxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        super.preRegister();

        addItemHandler(new BlockBreakHandler() {

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

                    if (logs.contains(e.getBlock())) {
                        logs.remove(e.getBlock());
                    }

                    for (Block b : logs) {
                        if (SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK)) {
                            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

                            for (ItemStack drop : b.getDrops(getItem())) {
                                b.getWorld().dropItemNaturally(b.getLocation(), drop);
                            }

                            b.setType(Material.AIR);
                        }
                    }

                    return true;
                }
                else return false;
            }
        });
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            if (e.getClickedBlock().isPresent()) {
                Block block = e.getClickedBlock().get();

                if (isUnstrippedLog(block)) {
                    List<Block> logs = Vein.find(block, 20, this::isUnstrippedLog);

                    if (logs.contains(block)) {
                        logs.remove(block);
                    }

                    for (Block b : logs) {
                        Material type = b.getType();

                        if (SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK)) {
                            b.getWorld().playSound(b.getLocation(), Sound.ITEM_AXE_STRIP, 1, 1);
                            Axis axis = ((Orientable) b.getBlockData()).getAxis();
                            b.setType(Material.valueOf("STRIPPED_" + type.name()));
                            
                            Orientable orientable = (Orientable) b.getBlockData();
                            orientable.setAxis(axis);
                            b.setBlockData(orientable);
                        }
                    }
                }
            }
        };
    }

    private boolean isUnstrippedLog(Block block) {
        return Tag.LOGS.isTagged(block.getType()) && !block.getType().name().startsWith("STRIPPED_");
    }

}
