package io.github.thebusybiscuit.slimefun5.compat;

import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SFItemStack {

    private final ItemStack item;

    private static Method setCustomModelDataMethod;
    private static Method setLocalizedNameMethod;

    static {
        try {
            setCustomModelDataMethod = ItemMeta.class.getMethod("setCustomModelData", Integer.class);
        } catch (NoSuchMethodException | SecurityException e) {
            // Ignore
        }
        try {
            setLocalizedNameMethod = ItemMeta.class.getMethod("setLocalizedName", String.class);
        } catch (NoSuchMethodException | SecurityException e) {
            // Ignore
        }
    }

    public SFItemStack(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public SFItemStack setLocalizedName(String name) {
        if (item != null && item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            if (setLocalizedNameMethod != null) {
                try {
                    setLocalizedNameMethod.invoke(meta, name);
                    item.setItemMeta(meta);
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
        return this;
    }

    public SFItemStack setLore(List<String> lore) {
        if (item != null && item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return this;
    }

    public SFItemStack setCustomModelData(Integer data) {
        if (item != null && item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            if (setCustomModelDataMethod != null) {
                try {
                    setCustomModelDataMethod.invoke(meta, data);
                    item.setItemMeta(meta);
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
        return this;
    }
}
