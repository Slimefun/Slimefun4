package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.events.AutoDisenchantEvent;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class AutoDisenchanter extends AContainer {

    public AutoDisenchanter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "&5Auto-Disenchanter";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_CHESTPLATE);
    }

    @Override
    public int getEnergyConsumption() {
        return 9;
    }

    @Override
    public int getCapacity() {
        return 128;
    }

    @Override
    protected void tick(Block b) {
        BlockMenu menu = BlockStorage.getInventory(b);

        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0) {
                ChestMenuUtils.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                }
                else progress.put(b, timeleft - 1);
            }
            else {
                menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));

                for (ItemStack item : processing.get(b).getOutput()) {
                    menu.pushItem(item, getOutputSlots());
                }

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            MachineRecipe recipe = null;
            Map<Enchantment, Integer> enchantments = new HashMap<>();
            Set<ItemEnchantment> emeraldEnchantments = new HashSet<>();

            for (int slot : getInputSlots()) {
                ItemStack item = menu.getItemInSlot(slot);

                // Check if disenchantable
                SlimefunItem sfItem = null;

                // stops endless checks of getByItem for empty book stacks.
                if ((item != null) && (item.getType() != Material.BOOK)) {
                    sfItem = SlimefunItem.getByItem(item);
                }
                if (sfItem != null && !sfItem.isDisenchantable()) {
                    return;
                }

                AutoDisenchantEvent event = new AutoDisenchantEvent(item);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return;
                }

                ItemStack target = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1] : getInputSlots()[0]);

                // Disenchanting
                if (item != null && target != null && target.getType() == Material.BOOK) {
                    int amount = 0;

                    for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
                        enchantments.put(entry.getKey(), entry.getValue());
                        amount++;
                    }

                    if (SlimefunPlugin.getThirdPartySupportService().isEmeraldEnchantsInstalled()) {
                        for (ItemEnchantment enchantment : EmeraldEnchants.getInstance().getRegistry().getEnchantments(item)) {
                            amount++;
                            emeraldEnchantments.add(enchantment);
                        }
                    }

                    if (amount > 0) {
                        ItemStack newItem = item.clone();
                        newItem.setAmount(1);
                        ItemStack book = target.clone();
                        book.setAmount(1);
                        book.setType(Material.ENCHANTED_BOOK);

                        ItemMeta itemMeta = newItem.getItemMeta();
                        ItemMeta bookMeta = book.getItemMeta();
                        ((Repairable) bookMeta).setRepairCost(((Repairable) itemMeta).getRepairCost());
                        ((Repairable) itemMeta).setRepairCost(0);
                        newItem.setItemMeta(itemMeta);
                        book.setItemMeta(bookMeta);

                        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();

                        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                            newItem.removeEnchantment(entry.getKey());
                            meta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
                        }

                        book.setItemMeta(meta);

                        for (ItemEnchantment ench : emeraldEnchantments) {
                            EmeraldEnchants.getInstance().getRegistry().applyEnchantment(book, ench.getEnchantment(), ench.getLevel());
                            EmeraldEnchants.getInstance().getRegistry().applyEnchantment(newItem, ench.getEnchantment(), 0);
                        }

                        recipe = new MachineRecipe(100 * amount, new ItemStack[] { target, item }, new ItemStack[] { newItem, book });
                        break;
                    }
                }
            }

            if (recipe != null) {
                if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                    return;
                }

                for (int slot : getInputSlots()) {
                    menu.consumeItem(slot);
                }

                processing.put(b, recipe);
                progress.put(b, recipe.getTicks());
            }
        }
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_DISENCHANTER";
    }

}
