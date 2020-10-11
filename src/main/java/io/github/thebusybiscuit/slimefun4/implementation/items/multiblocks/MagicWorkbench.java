package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MagicWorkbench extends BackpackCrafter {

    public MagicWorkbench(Category category, SlimefunItemStack item) {
        super(category, item, new ItemStack[] { null, null, null, null, null, null, new ItemStack(Material.BOOKSHELF), new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.DISPENSER) }, BlockFace.UP);
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block dispenser = locateDispenser(b);

        if (dispenser == null) {
            // How even...
            return;
        }

        BlockState state = PaperLib.getBlockState(dispenser, false).getState();

        if (state instanceof Dispenser) {
            Dispenser disp = (Dispenser) state;
            Inventory inv = disp.getInventory();
            List<ItemStack[]> inputs = RecipeType.getRecipeInputList(this);

            for (int i = 0; i < inputs.size(); i++) {
                if (isCraftable(inv, inputs.get(i))) {
                    ItemStack output = RecipeType.getRecipeOutputList(this, inputs.get(i)).clone();

                    if (Slimefun.hasUnlocked(p, output, true)) {
                        craft(inv, dispenser, p, b, output);
                    }

                    return;
                }
            }

            SlimefunPlugin.getLocalization().sendMessage(p, "machines.pattern-not-found", true);
        }
    }

    private void craft(Inventory inv, Block dispenser, Player p, Block b, ItemStack output) {
        Inventory fakeInv = createVirtualInventory(inv);
        Inventory outputInv = findOutputInventory(output, dispenser, inv, fakeInv);

        if (outputInv != null) {
            SlimefunItem sfItem = SlimefunItem.getByItem(output);

            if (sfItem instanceof SlimefunBackpack) {
                upgradeBackpack(p, inv, (SlimefunBackpack) sfItem, output);
            }

            for (int j = 0; j < 9; j++) {
                if (inv.getContents()[j] != null && inv.getContents()[j].getType() != Material.AIR) {
                    if (inv.getContents()[j].getAmount() > 1) {
                        inv.setItem(j, new CustomItem(inv.getContents()[j], inv.getContents()[j].getAmount() - 1));
                    } else {
                        inv.setItem(j, null);
                    }
                }
            }

            startAnimation(p, b, outputInv, output);
        } else {
            SlimefunPlugin.getLocalization().sendMessage(p, "machines.full-inventory", true);
        }
    }

    private void startAnimation(Player p, Block b, Inventory inv, ItemStack output) {
        for (int j = 0; j < 4; j++) {
            int current = j;
            SlimefunPlugin.runSync(() -> {
                p.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                p.getWorld().playEffect(b.getLocation(), Effect.ENDER_SIGNAL, 1);

                if (current < 3) {
                    p.getWorld().playSound(b.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1F, 1F);
                } else {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
                    inv.addItem(output);
                }
            }, j * 20L);
        }
    }

    private Block locateDispenser(Block b) {
        Block block = null;

        if (b.getRelative(1, 0, 0).getType() == Material.DISPENSER) {
            block = b.getRelative(1, 0, 0);
        } else if (b.getRelative(0, 0, 1).getType() == Material.DISPENSER) {
            block = b.getRelative(0, 0, 1);
        } else if (b.getRelative(-1, 0, 0).getType() == Material.DISPENSER) {
            block = b.getRelative(-1, 0, 0);
        } else if (b.getRelative(0, 0, -1).getType() == Material.DISPENSER) {
            block = b.getRelative(0, 0, -1);
        }

        return block;
    }

    private boolean isCraftable(Inventory inv, ItemStack[] recipe) {
        for (int j = 0; j < inv.getContents().length; j++) {
            if (!SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], true)) {
                if (SlimefunItem.getByItem(recipe[j]) instanceof SlimefunBackpack) {
                    if (!SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], false)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }

}
