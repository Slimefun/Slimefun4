package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces;

import io.github.thebusybiscuit.slimefun4.core.attributes.ItemAttribute;


public interface ChanceDrop extends ItemAttribute {

    /**
     * This field sets the % chance for an item to drop when a creature is killed.
     */
    int chance = 100;  

    public default int getChance() {
    	return chance;
    }
}
