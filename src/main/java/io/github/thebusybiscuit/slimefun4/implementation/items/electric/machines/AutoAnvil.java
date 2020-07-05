package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class AutoAnvil extends AContainer {

    public AutoAnvil(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "Auto-Anvil";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.IRON_PICKAXE);
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_ANVIL";
    }

    public abstract int getRepairFactor();

    @Override
    protected void tick(Block b) {
        BlockMenu menu = BlockStorage.getInventory(b);

        if (isProcessing(b)) {
            int timeleft = progress.get(b);

            if (timeleft > 0) {
                ChestMenuUtils.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                    return;
                }

                ChargableBlock.addCharge(b, -getEnergyConsumption());
                progress.put(b, timeleft - 1);
            }
            else {
                menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
                menu.pushItem(processing.get(b).getOutput()[0].clone(), getOutputSlots());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            MachineRecipe recipe = null;

            for (int slot : getInputSlots()) {
                ItemStack target = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1] : getInputSlots()[0]);
                ItemStack item = menu.getItemInSlot(slot);

                if (item != null && item.getType().getMaxDurability() > 0 && ((Damageable) item.getItemMeta()).getDamage() > 0) {
                    if (SlimefunUtils.isItemSimilar(target, SlimefunItems.DUCT_TAPE, true)) {
                        ItemStack repaired = repair(item);
                        recipe = new MachineRecipe(30, new ItemStack[] { target, item }, new ItemStack[] { repaired });
                    }

                    break;
                }
            }

            if (recipe != null) {
                if (!menu.fits(recipe.getOutput()[0], getOutputSlots())) return;

                for (int slot : getInputSlots()) {
                    menu.consumeItem(slot);
                }

                processing.put(b, recipe);
                progress.put(b, recipe.getTicks());
            }
        }
    }

    private ItemStack repair(ItemStack item) {
        ItemStack repaired = item.clone();
        ItemMeta meta = repaired.getItemMeta();

        short maxDurability = item.getType().getMaxDurability();
        short durability = (short) (((Damageable) meta).getDamage() - (maxDurability / getRepairFactor()));

        if (durability < 0) {
            durability = 0;
        }

        ((Damageable) meta).setDamage(durability);
        repaired.setItemMeta(meta);
        return repaired;
    }

}
