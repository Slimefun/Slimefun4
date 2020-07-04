package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EnhancedCraftingTable extends BackpackCrafter {

    public EnhancedCraftingTable(Category category, SlimefunItemStack item) {
        super(category, item, new ItemStack[]{null, null, null, null, new ItemStack(Material.CRAFTING_TABLE), null, null, new ItemStack(Material.DISPENSER), null}, new ItemStack[0], BlockFace.SELF);
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block dispenser = b.getRelative(BlockFace.DOWN);
        Dispenser disp = (Dispenser) dispenser.getState();
        Inventory inv = disp.getInventory();

        List<ItemStack[]> inputs = RecipeType.getRecipeInputList(this);

        for (ItemStack[] input : inputs) {
            if (isCraftable(inv, input)) {
                ItemStack output = RecipeType.getRecipeOutputList(this, input).clone();
                if (Slimefun.hasUnlocked(p, output, true)) {
                    craft(inv, dispenser, p, b, output);
                }

                return;
            }
        }


        SlimefunPlugin.getLocalization().sendMessage(p, "machines.pattern-not-found", true);
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
                ItemStack item = inv.getContents()[j];

                if (item != null && item.getType() != Material.AIR) {
                    ItemUtils.consumeItem(item, true);
                }
            }
            p.getWorld().playSound(b.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);

            outputInv.addItem(output);

        } else SlimefunPlugin.getLocalization().sendMessage(p, "machines.full-inventory", true);
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