package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
* Represents Book Binder, a machine that binds multiple enchantments books into one.
*
* @author ProfElements
*/
public class BookBinder extends AContainer {

    private final ItemSetting<Boolean> bypassVanillaMaxLevel = new ItemSetting<>("bypass-vanilla-max-level", false);
    private final ItemSetting<Boolean> hasCustomMaxLevel = new ItemSetting<>("has-custom-max-level", false);
    private final ItemSetting<Integer> customMaxLevel = new IntRangeSetting("custom-max-level", 0, 15, Integer.MAX_VALUE);

    @ParametersAreNonnullByDefault
    public BookBinder(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        
        addItemSetting(bypassVanillaMaxLevel, hasCustomMaxLevel, customMaxLevel); 
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        for (int slot : getInputSlots()) {
            ItemStack target = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1] : getInputSlots()[0]);

            if (!isCompatible(target)) {
                return null;
            }

            ItemStack item = menu.getItemInSlot(slot);
            if  (isCompatible(item) && isCompatible(target)) {
                
                EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) item.getItemMeta();
                EnchantmentStorageMeta targetMeta = (EnchantmentStorageMeta) target.getItemMeta();

                Map<Enchantment, Integer> storedItemEnchantments = itemMeta.getStoredEnchants();
                Map<Enchantment, Integer> storedTargetEnchantments = targetMeta.getStoredEnchants();
                Map<Enchantment, Integer> enchantments = combineEnchantments(storedItemEnchantments, storedTargetEnchantments);
                
                if (enchantments.size() > 0) {
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    book.setAmount(1);

                    EnchantmentStorageMeta enchantMeta = (EnchantmentStorageMeta) book.getItemMeta();
                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        if (bypassVanillaMaxLevel.getValue()) {
                            enchantMeta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
                        } else {
                            enchantMeta.addStoredEnchant(entry.getKey(), entry.getValue(), false);
                        }
                    }

                    book.setItemMeta(enchantMeta);
                    
                    MachineRecipe recipe = new MachineRecipe(25 * enchantments.size() / this.getSpeed(), new ItemStack[] {target, item}, new ItemStack[] {book});

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

    private boolean isCompatible(@Nullable ItemStack item) {
        if (item != null && item.getType() == Material.ENCHANTED_BOOK) {
            return true;
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

    private Map<Enchantment, Integer> combineEnchantments(Map<Enchantment, Integer> ech1, Map<Enchantment, Integer> ech2) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        
        enchantments.putAll(ech1);
        for (Map.Entry<Enchantment, Integer> entry : ech2.entrySet()) {
            enchantments.merge(entry.getKey(), entry.getValue(), (a, b) -> {
                    if (a == b) {
                        if (hasCustomMaxLevel.getValue()) {
                            return a + 1 > customMaxLevel.getValue() ? customMaxLevel.getValue() : a + 1;
                        } else {
                            return a + 1;
                        }
                        
                    } else {
                        if (hasCustomMaxLevel.getValue()) {
                            return Math.max(a, b) > customMaxLevel.getValue() ? customMaxLevel.getValue() : Math.max(a, b);
                        } else {
                            return Math.max(a, b);
                        }
                       
                    }
            });
        }        

    return enchantments;
    
    }
}