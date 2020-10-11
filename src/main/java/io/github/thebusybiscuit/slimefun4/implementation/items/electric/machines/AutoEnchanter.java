package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class AutoEnchanter extends AContainer {

    private final int emeraldEnchantsLimit;

    public AutoEnchanter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        emeraldEnchantsLimit = SlimefunPlugin.getCfg().getInt("options.emerald-enchantment-limit");
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_CHESTPLATE);
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
                Set<ItemEnchantment> emeraldEnchantments = new HashSet<>();
                int amount = 0;
                int specialAmount = 0;
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

                for (Map.Entry<Enchantment, Integer> e : meta.getStoredEnchants().entrySet()) {
                    if (e.getKey().canEnchantItem(target)) {
                        amount++;
                        enchantments.put(e.getKey(), e.getValue());
                    }
                }

                if (SlimefunPlugin.getThirdPartySupportService().isEmeraldEnchantsInstalled()) {
                    for (ItemEnchantment enchantment : EmeraldEnchants.getInstance().getRegistry().getEnchantments(item)) {
                        if (EmeraldEnchants.getInstance().getRegistry().isApplicable(target, enchantment.getEnchantment()) && EmeraldEnchants.getInstance().getRegistry().getEnchantmentLevel(target, enchantment.getEnchantment().getName()) < enchantment.getLevel()) {
                            amount++;
                            specialAmount++;
                            emeraldEnchantments.add(enchantment);
                        }
                    }

                    specialAmount += EmeraldEnchants.getInstance().getRegistry().getEnchantments(target).size();
                }

                if (amount > 0 && specialAmount <= emeraldEnchantsLimit) {
                    ItemStack enchantedItem = target.clone();
                    enchantedItem.setAmount(1);

                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        enchantedItem.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                    }

                    for (ItemEnchantment ench : emeraldEnchantments) {
                        EmeraldEnchants.getInstance().getRegistry().applyEnchantment(enchantedItem, ench.getEnchantment(), ench.getLevel());
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
    public int getSpeed() {
        return 1;
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_ENCHANTER";
    }

}
