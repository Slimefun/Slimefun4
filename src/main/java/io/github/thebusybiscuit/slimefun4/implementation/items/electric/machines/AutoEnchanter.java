package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class AutoEnchanter extends AContainer {

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
            ItemStack target = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1] : getInputSlots()[0]);

            // Check if the item is enchantable
            if (!isEnchantable(target)) {
                return null;
            }

            ItemStack item = menu.getItemInSlot(slot);

            if (item != null && item.getType() == Material.ENCHANTED_BOOK && target != null) {
                Map<Enchantment, Integer> enchantments = new HashMap<>();
                int amount = 0;
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

                for (Map.Entry<Enchantment, Integer> e : meta.getStoredEnchants().entrySet()) {
                    if (e.getKey().canEnchantItem(target)) {
                        amount++;
                        enchantments.put(e.getKey(), e.getValue());
                    }
                }

                if (amount > 0) {
                    ItemStack enchantedItem = target.clone();
                    enchantedItem.setAmount(1);

                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        enchantedItem.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                    }

                    MachineRecipe recipe = new MachineRecipe(75 * amount / this.getSpeed(), new ItemStack[] { target, item }, new ItemStack[] { enchantedItem, new ItemStack(Material.BOOK) });

                    if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                        return null;
                    }

                    for (int inputSlot : getInputSlots()) {
                        menu.consumeItem(inputSlot);
                    }

                    return recipe;
                }

                return null;
            }
        }

        return null;
    }

    private boolean isEnchantable(ItemStack item) {
        SlimefunItem sfItem = null;

        // stops endless checks of getByItem for enchanted book stacks.
        if (item != null && item.getType() != Material.ENCHANTED_BOOK) {
            sfItem = SlimefunItem.getByItem(item);
        }

        return sfItem == null || sfItem.isEnchantable();
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_ENCHANTER";
    }

}
