package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AutoDrier extends AContainer {

    public AutoDrier(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, id, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "&eAuto Drier";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

    @Override
    protected void tick(Block b) {
        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0) {
                ItemStack item = getProgressBar().clone();
                ItemMeta im = item.getItemMeta();
                ((Damageable) im).setDamage(MachineHelper.getDurability(item, timeleft, processing.get(b).getTicks()));
                im.setDisplayName(" ");
                List<String> lore = new ArrayList<>();
                lore.add(MachineHelper.getProgress(timeleft, processing.get(b).getTicks()));
                lore.add("");
                lore.add(MachineHelper.getTimeLeft(timeleft / 2));
                im.setLore(lore);
                item.setItemMeta(im);

                BlockStorage.getInventory(b).replaceExistingItem(22, item);

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                }
                else progress.put(b, timeleft - 1);
            }
            else {
                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            MachineRecipe r = null;
            int inputSlot = -1;
            for (int slot: getInputSlots()) {
                ItemStack item = BlockStorage.getInventory(b).getItemInSlot(slot);
                if (item == null) continue;

                // Checking if dryable
                Material mat = item.getType();
                ItemStack output;
                if (mat == Material.ROTTEN_FLESH) {
                    output = new ItemStack(Material.LEATHER);
                }
                else if (mat == Material.WATER_BUCKET) {
                    output = new ItemStack(Material.BUCKET);
                }
                else if (mat == Material.WET_SPONGE) {
                    output = new ItemStack(Material.SPONGE);
                }
                else if (Tag.SAPLINGS.isTagged(mat) || Tag.LEAVES.isTagged(mat)) {
                    output = new ItemStack(Material.STICK);
                }
                else if (mat.name().contains("POTION")) {
                    output = new ItemStack(Material.GLASS_BOTTLE);
                }
                else continue;

                r = new MachineRecipe(5, new ItemStack[] {item}, new ItemStack[] {output});
                inputSlot = slot;
                break;
            }

            if (r != null) {
                if (inputSlot == -1) return;
                if (!fits(b, r.getOutput())) return;
                BlockStorage.getInventory(b).replaceExistingItem(inputSlot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(inputSlot), 1));
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }
        }
    }

    @Override
    public int getEnergyConsumption() {
        return 5;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_DRIER";
    }
}
