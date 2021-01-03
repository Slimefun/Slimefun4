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
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class BookBinder extends AContainer {

    @ParametersAreNonnullByDefault
    public BookBinder(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        for (int slot : getInputSlots()) {
            ItemStack target = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1] : getInputSlots()[0]);

            if (!isCompatible(target)) {
                return null;
            }

            ItemStack item = menu.getItemInSlot(slot);
            if  (item != null && item.getType() == Material.ENCHANTED_BOOK && target != null) {
                Map<Enchantment, Integer> enchantments = new HashMap<>();
                int amount = 0;
                EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) item.getItemMeta();
                EnchantmentStorageMeta targetMeta = (EnchantmentStorageMeta) target.getItemMeta();

                for(Map.Entry<Enchantment, Integer> e : itemMeta.getStoredEnchants().entrySet()) {
                    for (Map.Entry<Enchantment, Integer> e2 : targetMeta.getStoredEnchants().entrySet()) {
                        if (e.getKey() == e2.getKey()) {
                            if (e.getValue() == e2.getValue()) {
                                enchantments.put(e.getKey(), e.getValue()+1);
                            } else {
                                enchantments.put(e.getKey(), e.getValue() > e2.getValue() ? e.getValue() : e2.getValue());
                            }
                        }
                        enchantments.put(e2.getKey(), e2.getValue());
                    }

                    amount++;
                    enchantments.put(e.getKey(), e.getValue());
                }

                if (amount > 0) {
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    book.setAmount(1);

                    EnchantmentStorageMeta enchantMeta = (EnchantmentStorageMeta) book.getItemMeta();
                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        enchantMeta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
                    }

                    book.setItemMeta(enchantMeta);
                    
                    MachineRecipe recipe = new MachineRecipe(50 * amount / this.getSpeed(), new ItemStack[] {target, item}, new ItemStack[] {book});

                    if(!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
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

    private boolean isCompatible(ItemStack item) {
        if (item != null && item.getType() == Material.ENCHANTED_BOOK) {
            return true;

            //Figure out what to do with incompatible enchantments if we should allow it at all ex. Protection + Fire Protection
        }

        return false;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.IRON_CHESTPLATE);
    }

    @Override
    public String getMachineIdentifier() {
        return "BOOK_BINDER";
    }
}