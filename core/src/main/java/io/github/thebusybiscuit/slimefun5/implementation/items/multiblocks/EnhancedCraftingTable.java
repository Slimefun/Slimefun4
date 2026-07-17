package io.github.thebusybiscuit.slimefun5.implementation.items.multiblocks;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.InventoryCompat;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

public class EnhancedCraftingTable extends AbstractCraftingTable {

    @ParametersAreNonnullByDefault
    public EnhancedCraftingTable(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, MaterialCompat.stack(XMaterial.CRAFTING_TABLE), null, null, new ItemStack(Material.DISPENSER), null }, BlockFace.SELF);
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block possibleDispenser = b.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(possibleDispenser, false).getState();

        if (state instanceof Dispenser) {
            // First player to interact claims ownership; this gates the redstone auto-craft later.
            Slimefun.getMultiBlockOwnership().setOwnerIfAbsent(possibleDispenser.getLocation(), p.getUniqueId());

            Dispenser dispenser = (Dispenser) state;            Inventory inv = dispenser.getInventory();
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

            if (InventoryCompat.isEmpty(inv)) {
                if (io.github.thebusybiscuit.slimefun5.core.guide.options.SlimefunGuideSettings.hasMachineMessagesEnabled(p)) {
                    Slimefun.getLocalization().sendMessage(p, "machines.inventory-empty", true);
                }
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

            if (sfItem instanceof SlimefunBackpack) {
                SlimefunBackpack backpack = (SlimefunBackpack) sfItem;                upgradeBackpack(p, inv, backpack, output);
            }

            for (int j = 0; j < 9; j++) {
                ItemStack item = inv.getContents()[j];

                if (item != null && item.getType() != Material.AIR && !isSlotLock(item)) {
                    InventoryCompat.consumeSlot(inv, j, 1, true);
                }
            }

            SoundEffect.ENHANCED_CRAFTING_TABLE_CRAFT_SOUND.playAt(b);
            outputInv.addItem(output);

        } else {
            // Output has nowhere to go (dispenser full): craft anyway and eject it out of the dispenser,
            // the same way the redstone auto-craft does, so it lands in open space instead of being lost.
            consumeInputs(inv);
            ejectOutput(dispenser, output);
            SoundEffect.ENHANCED_CRAFTING_TABLE_CRAFT_SOUND.playAt(b);
        }
    }

    @Override
    protected boolean isCraftable(Inventory inv, ItemStack[] recipe) {
        for (int j = 0; j < inv.getContents().length; j++) {
            ItemStack slot = ignoreLock(inv.getContents()[j]);

            if (!SlimefunUtils.isItemSimilar(slot, recipe[j], true, true, false) && !sameWoodMatch(slot, recipe[j])) {
                if (SlimefunItem.getByItem(recipe[j]) instanceof SlimefunBackpack) {
                    if (!SlimefunUtils.isItemSimilar(slot, recipe[j], false, true, false)) {
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

