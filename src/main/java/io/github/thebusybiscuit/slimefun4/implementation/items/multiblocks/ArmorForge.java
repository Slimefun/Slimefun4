package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class ArmorForge extends MultiBlockMachine {

    public ArmorForge(Category category, SlimefunItemStack item) {
        super(category, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.ANVIL), null, null, new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), null }, BlockFace.SELF);
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block dispBlock = b.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(dispBlock, false).getState();

        if (state instanceof Dispenser) {
            Dispenser disp = (Dispenser) state;
            Inventory inv = disp.getInventory();
            List<ItemStack[]> inputs = RecipeType.getRecipeInputList(this);

            for (int i = 0; i < inputs.size(); i++) {
                if (isCraftable(inv, inputs.get(i))) {
                    ItemStack output = RecipeType.getRecipeOutputList(this, inputs.get(i)).clone();

                    if (Slimefun.hasUnlocked(p, output, true)) {
                        Inventory outputInv = findOutputInventory(output, dispBlock, inv);

                        if (outputInv != null) {
                            craft(p, output, inv, outputInv);
                        } else {
                            SlimefunPlugin.getLocalization().sendMessage(p, "machines.full-inventory", true);
                        }
                    }

                    return;
                }
            }

            SlimefunPlugin.getLocalization().sendMessage(p, "machines.pattern-not-found", true);
        }
    }

    private boolean isCraftable(Inventory inv, ItemStack[] recipe) {
        for (int j = 0; j < inv.getContents().length; j++) {
            if (!SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], true)) {
                return false;
            }
        }

        return true;
    }

    private void craft(Player p, ItemStack output, Inventory inv, Inventory outputInv) {
        for (int j = 0; j < 9; j++) {
            ItemStack item = inv.getContents()[j];

            if (item != null && item.getType() != Material.AIR) {
                ItemUtils.consumeItem(item, true);
            }
        }

        for (int j = 0; j < 4; j++) {
            int current = j;

            SlimefunPlugin.runSync(() -> {
                if (current < 3) {
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
                } else {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
                    outputInv.addItem(output);
                }
            }, j * 20L);
        }
    }

}
