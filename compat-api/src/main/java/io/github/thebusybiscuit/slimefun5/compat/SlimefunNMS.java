package io.github.thebusybiscuit.slimefun5.compat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * The central SlimefunNMS interface defining abstract methods for operations
 * that require version-specific NMS code.
 */
public interface SlimefunNMS {

    /**
     * Gets a String from the NBT tag of an ItemStack.
     *
     * @param item The ItemStack to read from
     * @param key  The key of the NBT tag
     * @return The String value or null if it does not exist
     */
    String getNBTString(ItemStack item, String key);

    /**
     * Sets a String in the NBT tag of an ItemStack.
     *
     * @param item  The ItemStack to write to
     * @param key   The key of the NBT tag
     * @param value The value to write
     * @return The modified ItemStack
     */
    ItemStack setNBTString(ItemStack item, String key, String value);

    /**
     * Sets a Block to a specific Material without triggering block physics.
     *
     * @param block    The Block to update
     * @param material The Material to set
     */
    void setBlockTypeFast(Block block, Material material);

    /**
     * Breaks a Block without triggering block physics.
     *
     * @param block The Block to break
     */
    void breakBlockFast(Block block);
}