package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.enchanting;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.api.events.AutoEnchantEvent;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * The {@link AutoEnchanter}, in contrast to the {@link AutoDisenchanter}, adds
 * {@link Enchantment Enchantments} from a given enchanted book and transfers them onto
 * an {@link ItemStack}.
 *
 * @author TheBusyBiscuit
 * @author Poslovitch
 * @author Mooy1
 * @author StarWishSama
 *
 * @see AutoDisenchanter
 *
 */
public class AutoEnchanter extends AbstractEnchantmentMachine {

    @ParametersAreNonnullByDefault
    public AutoEnchanter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_CHESTPLATE);
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1] : getInputSlots()[0]);

            // Check if the item is enchantable
            if (!isEnchantable(item)) {
                return null;
            }

            // Call an event so other Plugins can modify it.
            AutoEnchantEvent event = new AutoEnchantEvent(item);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return null;
            }

            ItemStack secondItem = menu.getItemInSlot(slot);

            if (secondItem != null && secondItem.getType() == Material.ENCHANTED_BOOK) {
                return enchant(menu, item, secondItem);
            }
        }

        return null;
    }

    @Nullable
    @ParametersAreNonnullByDefault
    protected MachineRecipe enchant(BlockMenu menu, ItemStack target, ItemStack enchantedBook) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        int amount = 0;

        // Find applicable enchantments
        for (Map.Entry<Enchantment, Integer> entry : meta.getStoredEnchants().entrySet()) {
            if (entry.getKey().canEnchantItem(target)) {
                if (isEnchantmentLevelAllowed(entry.getValue())) {
                    amount++;
                    enchantments.put(entry.getKey(), entry.getValue());
                } else if (!menu.toInventory().getViewers().isEmpty()) {
                    showEnchantmentLevelWarning(menu);
                    return null;
                }
            }
        }

        // Check if we found any valid enchantments
        if (amount > 0) {
            ItemStack enchantedItem = target.clone();
            enchantedItem.setAmount(1);

            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                enchantedItem.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            }

            MachineRecipe recipe = new MachineRecipe(75 * amount / this.getSpeed(), new ItemStack[] { target, enchantedBook }, new ItemStack[] { enchantedItem, new ItemStack(Material.BOOK) });

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

    private boolean isEnchantable(@Nullable ItemStack item) {
        // stops endless checks of getByItem for enchanted book stacks.
        if (item != null && item.getType() != Material.ENCHANTED_BOOK) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            return sfItem == null || sfItem.isEnchantable();
        } else {
            return false;
        }
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_ENCHANTER";
    }

}
