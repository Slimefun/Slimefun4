package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.enchanting;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import io.github.bakedlibs.dough.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.api.events.AutoDisenchantEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;


import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * The {@link AutoDisenchanter}, in contrast to the {@link AutoEnchanter}, removes
 * {@link Enchantment Enchantments} from a given {@link ItemStack} and transfers them
 * to a book.
 *
 * @author TheBusyBiscuit
 * @author Poslovitch
 * @author John000708
 * @author Walshy
 * @author poma123
 * @author mrcoffee1026
 * @author VoidAngel
 * @author StarWishSama
 *
 * @see AutoEnchanter
 *
 */
public class AutoDisenchanter extends AbstractEnchantmentMachine {

    @ParametersAreNonnullByDefault
    public AutoDisenchanter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_CHESTPLATE);
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);

            if (!isDisenchantable(item)) {
                continue;
            }

            // Call an event so other Plugins can modify it.
            AutoDisenchantEvent event = new AutoDisenchantEvent(item);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return null;
            }

            ItemStack secondItem = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1] : getInputSlots()[0]);

            if (secondItem != null && secondItem.getType() == Material.BOOK) {
                return disenchant(menu, item, secondItem);
            }
        }

        return null;
    }

    @ParametersAreNonnullByDefault
    private @Nullable MachineRecipe disenchant(BlockMenu menu, ItemStack item, ItemStack book) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();

        // Find enchantments
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            if (isEnchantmentLevelAllowed(entry.getValue())) {
                enchantments.put(entry.getKey(), entry.getValue());
            } else if (!menu.toInventory().getViewers().isEmpty()) {
                showEnchantmentLevelWarning(menu);
                return null;
            }
        }

        if (!isEnchantmentAmountAllowed(enchantments.size())) {
            return null;
        }

        // Check if we found any valid enchantments
        if (!enchantments.isEmpty()) {
            ItemStack disenchantedItem = item.clone();
            disenchantedItem.setAmount(1);

            ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
            transferEnchantments(disenchantedItem, enchantedBook, enchantments);

            MachineRecipe recipe = new MachineRecipe(90 * enchantments.size() / this.getSpeed(), new ItemStack[] { book, item }, new ItemStack[] { disenchantedItem, enchantedBook });

            if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                return null;
            }

            for (int inputSlot : getInputSlots()) {
                menu.consumeItem(inputSlot);
            }

            return recipe;
        } else {
            return null;
        }
    }

    @ParametersAreNonnullByDefault
    private void transferEnchantments(ItemStack item, ItemStack book, Map<Enchantment, Integer> enchantments) {
        ItemMeta itemMeta = item.getItemMeta();
        ItemMeta bookMeta = book.getItemMeta();
        ((Repairable) bookMeta).setRepairCost(((Repairable) itemMeta).getRepairCost());
        ((Repairable) itemMeta).setRepairCost(0);
        book.setItemMeta(bookMeta);

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantmentToTransfer = entry.getKey();
            boolean wasEnchantmentRemoved = itemMeta.removeEnchant(enchantmentToTransfer);
            boolean stillHasEnchantment = itemMeta.getEnchants().containsKey(enchantmentToTransfer);

            // Prevent future enchantment duplication (#3837)
            if (wasEnchantmentRemoved && !stillHasEnchantment) {
                meta.addStoredEnchant(enchantmentToTransfer, entry.getValue(), true);
            } else {
                // Get Enchantment Name
                Slimefun.logger().log(Level.SEVERE, "AutoDisenchanter has failed to remove enchantment \"{0}\"", enchantmentToTransfer.getKey().getKey());
            }
        }

        item.setItemMeta(itemMeta);
        book.setItemMeta(meta);
    }

    private boolean isDisenchantable(@Nullable ItemStack item) {
        if (item != null && !item.getType().isAir() && item.getType() != Material.BOOK && !hasIgnoredLore(item)) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            return sfItem == null || sfItem.isDisenchantable();
        } else {
            return false;
        }
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_DISENCHANTER";
    }

}
