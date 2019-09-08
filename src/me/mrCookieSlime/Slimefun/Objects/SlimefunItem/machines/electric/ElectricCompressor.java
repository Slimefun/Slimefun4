package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class ElectricCompressor extends AContainer {

    public ElectricCompressor(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, id, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "&9Electric Compressor";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.TURTLE_HELMET);
    }

    public abstract int getSpeed();

    @Override
    protected void tick(Block b) {
        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0 && getSpeed() < 10) {
                MachineHelper.updateProgressbar(BlockStorage.getInventory(b), 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                }
                else progress.put(b, timeleft - 1);
            }
            else if (ChargableBlock.isChargable(b)) {
                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                ChargableBlock.addCharge(b, -getEnergyConsumption());

                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            for (int slot: getInputSlots()) {
                BlockMenu inv = BlockStorage.getInventory(b);
                ItemStack input = inv.getItemInSlot(slot);

                if (input == null || input.getType() == Material.AIR || input.getAmount() == 0) continue;
                int amount = input.getAmount();

                MachineRecipe r = null;
                if (SlimefunManager.isItemSimiliar(input, SlimefunItems.REINFORCED_ALLOY_INGOT, true)) {
                    if (amount >= 8) {
                        r = new MachineRecipe(32 / getSpeed(), new ItemStack[0], new ItemStack[]{SlimefunItems.REINFORCED_PLATE});
                        if (!fits(b, r.getOutput())) return;
                        inv.replaceExistingItem(slot, InvUtils.decreaseItem(inv.getItemInSlot(slot), 8));
                    }
                }
                else if (SlimefunManager.isItemSimiliar(input, SlimefunItems.STEEL_INGOT, true)) {
                    if (amount >= 8) {
                        r = new MachineRecipe(16 / getSpeed(), new ItemStack[0], new ItemStack[]{SlimefunItems.STEEL_PLATE});
                        if (!fits(b, r.getOutput())) return;
                        inv.replaceExistingItem(slot, InvUtils.decreaseItem(inv.getItemInSlot(slot), 8));
                    }
                }
                else if (SlimefunManager.isItemSimiliar(input, SlimefunItems.STONE_CHUNK, true)) {
                    if (amount >= 4) {
                        r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{new ItemStack(Material.COBBLESTONE)});
                        if (!fits(b, r.getOutput())) return;
                        inv.replaceExistingItem(slot, InvUtils.decreaseItem(inv.getItemInSlot(slot), 4));
                    }
                }
                if (r != null) {
                    processing.put(b, r);
                    progress.put(b, r.getTicks());
                    break;
                }
            }
        }
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_COMPRESSOR";
    }

}
