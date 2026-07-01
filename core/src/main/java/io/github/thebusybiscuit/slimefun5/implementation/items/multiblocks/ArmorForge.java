package io.github.thebusybiscuit.slimefun5.implementation.items.multiblocks;

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

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun5.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.InventoryCompat;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

public class ArmorForge extends AbstractCraftingTable {

    @ParametersAreNonnullByDefault
    public ArmorForge(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.ANVIL), null, null, CustomItemStack.create(Material.DISPENSER, "Dispenser (Facing up)"), null }, BlockFace.SELF);
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block possibleDispenser = b.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(possibleDispenser, false).getState();

        if (state instanceof Dispenser) {
            Dispenser dispenser = (Dispenser) state;            Inventory inv = dispenser.getInventory();
            List<ItemStack[]> inputs = RecipeType.getRecipeInputList(this);

            for (ItemStack[] input : inputs) {
                if (isCraftable(inv, input)) {
                    ItemStack output = RecipeType.getRecipeOutputList(this, input).clone();
                    MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, input, output);

                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled() && SlimefunUtils.canPlayerUseItem(p, output, true)) {
                        craft(p, event.getOutput(), inv, possibleDispenser);
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

    @Override
    protected boolean isCraftable(Inventory inv, ItemStack[] recipe) {
        for (int j = 0; j < inv.getContents().length; j++) {
            ItemStack slot = ignoreLock(inv.getContents()[j]);
            if (!SlimefunUtils.isItemSimilar(slot, recipe[j], true) && !sameWoodMatch(slot, recipe[j])) {
                return false;
            }
        }

        return true;
    }

    @ParametersAreNonnullByDefault
    private void craft(Player p, ItemStack output, Inventory inv, Block dispenser) {
        Inventory fakeInv = createVirtualInventory(inv);
        Inventory outputInv = findOutputInventory(output, dispenser, inv, fakeInv);

        if (outputInv != null) {
            for (int j = 0; j < 9; j++) {
                ItemStack item = inv.getContents()[j];

                if (item != null && item.getType() != Material.AIR && !isSlotLock(item)) {
                    ItemUtils.consumeItem(item, true);
                }
            }

            for (int j = 0; j < 4; j++) {
                int current = j;

                Slimefun.runSync(() -> {
                    if (current < 3) {
                        SoundEffect.ARMOR_FORGE_WORKING_SOUND.playAt(dispenser);
                    } else {
                        SoundEffect.ARMOR_FORGE_FINISH_SOUND.playAt(dispenser);
                        handleCraftedItem(output, dispenser, inv);
                    }
                }, j * 20L);
            }

        } else {
            // Output has nowhere to go (dispenser + surroundings full): craft anyway and drop it.
            consumeInputs(inv);
            dropOutput(dispenser, output);
            SoundEffect.ARMOR_FORGE_FINISH_SOUND.playAt(dispenser);
        }
    }
}

