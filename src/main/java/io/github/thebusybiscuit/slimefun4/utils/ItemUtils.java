package io.github.thebusybiscuit.slimefun4.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class ItemUtils {
    public static int getAllItemAmount(@Nonnull ItemStack... itemStacks) {
        int amount = 0;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null || itemStack.getType().isAir()) continue;
            amount += itemStack.getAmount();
        }

        return amount;
    }

    public static int getAllItemTypeAmount(@Nonnull ItemStack... itemStacks) {
        Set<SlimefunItem> sfitems = new HashSet<>();
        Set<Material> materials = new HashSet<>();

        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null || itemStack.getType().isAir()) continue;
            SlimefunItem sfitem = SlimefunItem.getByItem(itemStack);
            if (sfitem != null) sfitems.add(sfitem);
            else materials.add(itemStack.getType());
        }

        return sfitems.size() + materials.size();
    }
}
