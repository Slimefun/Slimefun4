package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

public class EnhancedCraftingTable extends AbstractCraftingTable {

    @ParametersAreNonnullByDefault
    public EnhancedCraftingTable(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.CRAFTING_TABLE), null, null, new ItemStack(Material.DISPENSER), null }, BlockFace.SELF);
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block possibleDispenser = b.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(possibleDispenser, false).getState();

        if (state instanceof Dispenser dispenser) {
            Inventory inv = dispenser.getInventory();
            List<ItemStack[]> inputs = RecipeType.getRecipeInputList(this);

            for (ItemStack[] input : inputs) {
                if (isCraftable(inv, input)) {
                    ItemStack output = RecipeType.getRecipeOutputList(this, input).clone();
                    MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, input, output);

                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled() && SlimefunUtils.canPlayerUseItem(p, output, true)) {
                        craft(inv, possibleDispenser, p, b, event.getOutput());
                    }

                    return;
                }
            }

            if (inv.isEmpty()) {
                Slimefun.getLocalization().sendMessage(p, "machines.inventory-empty", true);
            } else {
                Slimefun.getLocalization().sendMessage(p, "machines.pattern-not-found", true);
            }
        }
    }

    private void craft(Inventory inv, Block dispenser, Player p, Block b, ItemStack output) {
        Inventory fakeInv = createVirtualInventory(inv);
        Inventory outputInv = findOutputInventory(output, dispenser, inv, fakeInv);

        if (outputInv != null) {
            SlimefunItem sfItem = SlimefunItem.getByItem(output);

            if (sfItem instanceof SlimefunBackpack backpack) {
                upgradeBackpack(p, inv, backpack, output);
            }

            for (int j = 0; j < 9; j++) {
                ItemStack item = inv.getContents()[j];

                if (item != null && item.getType() != Material.AIR) {
                    ItemUtils.consumeItem(item, true);
                }
            }

            SoundEffect.ENHANCED_CRAFTING_TABLE_CRAFT_SOUND.playAt(b);
            outputInv.addItem(output);

        } else {
            Slimefun.getLocalization().sendMessage(p, "machines.full-inventory", true);
        }
    }

    private boolean isCraftable(Inventory inv, ItemStack[] recipe) {
        for (int j = 0; j < inv.getContents().length; j++) {
            if (!SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], true, true, false)) {
                if (SlimefunItem.getByItem(recipe[j]) instanceof SlimefunBackpack) {
                    if (!SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], false, true, false)) {
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
