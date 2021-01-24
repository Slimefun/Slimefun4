package me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item;

import java.util.Comparator;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer.ItemFlag;

/**
 * An old remnant of CS-CoreLib.
 * This will be removed once we updated everything.
 * Don't look at the code, it will be gone soon, don't worry.
 * 
 * @deprecated This was a horrible idea. Don't use it.
 *
 */
@Deprecated
public class ItemFlagComparator implements Comparator<ItemFlag> {

    @Override
    public int compare(ItemFlag flag1, ItemFlag flag2) {
        return flag1.getWeight() - flag2.getWeight();
    }

}
