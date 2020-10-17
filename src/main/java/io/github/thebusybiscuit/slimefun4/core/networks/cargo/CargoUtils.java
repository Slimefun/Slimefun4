package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

final class CargoUtils {

    // Whitelist or blacklist slots
    private static final int[] FILTER_SLOTS = { 19, 20, 21, 28, 29, 30, 37, 38, 39 };

    private CargoUtils() {}

    /**
     * This is a performance-saving shortcut to quickly test whether a given
     * {@link Block} might be an {@link InventoryHolder} or not.
     * 
     * @param block
     *            The {@link Block} to check
     * 
     * @return Whether this {@link Block} represents a {@link BlockState} that is an {@link InventoryHolder}
     */
    static boolean hasInventory(@Nullable Block block) {
        if (block == null) {
            // No block, no inventory
            return false;
        }

        Material type = block.getType();

        if (SlimefunTag.SHULKER_BOXES.isTagged(type)) {
            return true;
        }

        switch (type) {
        case CHEST:
        case TRAPPED_CHEST:
        case FURNACE:
        case DISPENSER:
        case DROPPER:
        case HOPPER:
        case BREWING_STAND:
        case BARREL:
        case BLAST_FURNACE:
        case SMOKER:
            return true;
        default:
            return false;
        }
    }

    static int[] getInputSlotRange(@Nonnull Inventory inv, @Nullable ItemStack item) {
        if (inv instanceof FurnaceInventory) {
            if (item != null && item.getType().isFuel()) {
                if (isSmeltable(item, true)) {
                    // Any non-smeltable items should not land in the upper slot
                    return new int[] { 0, 2 };
                } else {
                    return new int[] { 1, 2 };
                }
            } else {
                return new int[] { 0, 1 };
            }
        } else if (inv instanceof BrewerInventory) {
            if (isPotion(item)) {
                // Slots for potions
                return new int[] { 0, 3 };
            } else if (item != null && item.getType() == Material.BLAZE_POWDER) {
                // Blaze Powder slot
                return new int[] { 4, 5 };
            } else {
                // Input slot
                return new int[] { 3, 4 };
            }
        } else {
            // Slot 0-size
            return new int[] { 0, inv.getSize() };
        }
    }

    static int[] getOutputSlotRange(Inventory inv) {
        if (inv instanceof FurnaceInventory) {
            // Slot 2-3
            return new int[] { 2, 3 };
        } else if (inv instanceof BrewerInventory) {
            // Slot 0-3
            return new int[] { 0, 3 };
        } else {
            // Slot 0-size
            return new int[] { 0, inv.getSize() };
        }
    }

    static ItemStack withdraw(Map<Location, Inventory> inventories, Block node, Block target, ItemStack template) {
        DirtyChestMenu menu = getChestMenu(target);

        if (menu == null) {
            if (hasInventory(target)) {
                Inventory inventory = inventories.get(target.getLocation());

                if (inventory != null) {
                    return withdrawFromVanillaInventory(node, template, inventory);
                }

                BlockState state = PaperLib.getBlockState(target, false).getState();

                if (state instanceof InventoryHolder) {
                    inventory = ((InventoryHolder) state).getInventory();
                    inventories.put(target.getLocation(), inventory);
                    return withdrawFromVanillaInventory(node, template, inventory);
                }
            }

            return null;
        }

        ItemStackWrapper wrapper = new ItemStackWrapper(template);

        for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
            ItemStack is = menu.getItemInSlot(slot);

            if (SlimefunUtils.isItemSimilar(is, wrapper, true) && matchesFilter(node, is)) {
                if (is.getAmount() > template.getAmount()) {
                    is.setAmount(is.getAmount() - template.getAmount());
                    menu.replaceExistingItem(slot, is.clone());
                    return template;
                } else {
                    menu.replaceExistingItem(slot, null);
                    return is;
                }
            }
        }

        return null;
    }

    static ItemStack withdrawFromVanillaInventory(Block node, ItemStack template, Inventory inv) {
        ItemStack[] contents = inv.getContents();
        int[] range = getOutputSlotRange(inv);
        int minSlot = range[0];
        int maxSlot = range[1];

        ItemStackWrapper wrapper = new ItemStackWrapper(template);

        for (int slot = minSlot; slot < maxSlot; slot++) {
            // Changes to these ItemStacks are synchronized with the Item in the Inventory
            ItemStack itemInSlot = contents[slot];

            if (SlimefunUtils.isItemSimilar(itemInSlot, wrapper, true, false) && matchesFilter(node, itemInSlot)) {
                if (itemInSlot.getAmount() > template.getAmount()) {
                    itemInSlot.setAmount(itemInSlot.getAmount() - template.getAmount());
                    return template;
                } else {
                    ItemStack clone = itemInSlot.clone();
                    itemInSlot.setAmount(0);
                    return clone;
                }
            }
        }

        return null;
    }

    static ItemStackAndInteger withdraw(Map<Location, Inventory> inventories, Block node, Block target) {
        DirtyChestMenu menu = getChestMenu(target);

        if (menu != null) {
            for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
                ItemStack is = menu.getItemInSlot(slot);

                if (matchesFilter(node, is)) {
                    menu.replaceExistingItem(slot, null);
                    return new ItemStackAndInteger(is, slot);
                }
            }
        } else if (hasInventory(target)) {
            Inventory inventory = inventories.get(target.getLocation());

            if (inventory != null) {
                return withdrawFromVanillaInventory(node, inventory);
            }

            BlockState state = PaperLib.getBlockState(target, false).getState();

            if (state instanceof InventoryHolder) {
                inventory = ((InventoryHolder) state).getInventory();
                inventories.put(target.getLocation(), inventory);
                return withdrawFromVanillaInventory(node, inventory);
            }
        }

        return null;
    }

    private static ItemStackAndInteger withdrawFromVanillaInventory(Block node, Inventory inv) {
        ItemStack[] contents = inv.getContents();
        int[] range = getOutputSlotRange(inv);
        int minSlot = range[0];
        int maxSlot = range[1];

        for (int slot = minSlot; slot < maxSlot; slot++) {
            ItemStack is = contents[slot];

            if (matchesFilter(node, is)) {
                inv.setItem(slot, null);
                return new ItemStackAndInteger(is, slot);
            }
        }

        return null;
    }

    static ItemStack insert(Map<Location, Inventory> inventories, Block node, Block target, ItemStack stack) {
        if (!matchesFilter(node, stack)) {
            return stack;
        }

        DirtyChestMenu menu = getChestMenu(target);

        if (menu == null) {
            if (hasInventory(target)) {
                Inventory inventory = inventories.get(target.getLocation());

                if (inventory != null) {
                    return insertIntoVanillaInventory(stack, inventory);
                }

                BlockState state = PaperLib.getBlockState(target, false).getState();

                if (state instanceof InventoryHolder) {
                    inventory = ((InventoryHolder) state).getInventory();
                    inventories.put(target.getLocation(), inventory);
                    return insertIntoVanillaInventory(stack, inventory);
                }
            }

            return stack;
        }

        ItemStackWrapper wrapper = new ItemStackWrapper(stack);

        for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.INSERT, wrapper)) {
            ItemStack itemInSlot = menu.getItemInSlot(slot);

            if (itemInSlot == null) {
                menu.replaceExistingItem(slot, stack);
                return null;
            }

            int maxStackSize = itemInSlot.getType().getMaxStackSize();
            int currentAmount = itemInSlot.getAmount();

            if (SlimefunUtils.isItemSimilar(itemInSlot, wrapper, true, false) && currentAmount < maxStackSize) {
                int amount = currentAmount + stack.getAmount();

                itemInSlot.setAmount(Math.min(amount, maxStackSize));
                if (amount > maxStackSize) {
                    stack.setAmount(amount - maxStackSize);
                } else {
                    stack = null;
                }

                menu.replaceExistingItem(slot, itemInSlot);
                return stack;
            }
        }

        return stack;
    }

    private static ItemStack insertIntoVanillaInventory(ItemStack stack, Inventory inv) {
        ItemStack[] contents = inv.getContents();
        int[] range = getInputSlotRange(inv, stack);
        int minSlot = range[0];
        int maxSlot = range[1];

        ItemStackWrapper wrapper = new ItemStackWrapper(stack);

        for (int slot = minSlot; slot < maxSlot; slot++) {
            // Changes to this ItemStack are synchronized with the Item in the Inventory
            ItemStack itemInSlot = contents[slot];

            if (itemInSlot == null) {
                inv.setItem(slot, stack);
                return null;
            } else {
                int maxStackSize = itemInSlot.getType().getMaxStackSize();

                if (SlimefunUtils.isItemSimilar(itemInSlot, wrapper, true, false) && itemInSlot.getAmount() < maxStackSize) {
                    int amount = itemInSlot.getAmount() + stack.getAmount();

                    if (amount > maxStackSize) {
                        stack.setAmount(amount - maxStackSize);
                    } else {
                        stack = null;
                    }

                    itemInSlot.setAmount(Math.min(amount, maxStackSize));
                    return stack;
                }
            }
        }

        return stack;
    }

    static DirtyChestMenu getChestMenu(@Nonnull Block block) {
        if (BlockStorage.hasInventory(block)) {
            return BlockStorage.getInventory(block);
        }

        return BlockStorage.getUniversalInventory(block);
    }

    static boolean matchesFilter(@Nonnull Block block, @Nullable ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        // Store the returned Config instance to avoid heavy calls
        Config blockData = BlockStorage.getLocationInfo(block.getLocation());
        String id = blockData.getString("id");

        if (id == null) {
            // This should normally not happen but if it does...
            // Don't accept any items.
            return false;
        } else if (id.equals("CARGO_NODE_OUTPUT")) {
            // Cargo Output nodes have no filter actually
            return true;
        }

        try {
            BlockMenu menu = BlockStorage.getInventory(block.getLocation());

            if (menu == null) {
                return false;
            }

            boolean lore = "true".equals(blockData.getString("filter-lore"));
            boolean allowByDefault = !"whitelist".equals(blockData.getString("filter-type"));
            return matchesFilterList(item, menu, lore, allowByDefault);
        } catch (Exception x) {
            Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Exception occurred while trying to filter items for a Cargo Node (" + id + ") at " + new BlockPosition(block));
            return false;
        }
    }

    private static boolean matchesFilterList(ItemStack item, BlockMenu menu, boolean respectLore, boolean defaultValue) {
        // Little performance optimization:
        // First check if there is more than one item to compare, if so
        // then we know we should create an ItemStackWrapper, otherwise it would
        // be of no benefit to us and just be redundant
        int itemsToCompare = 0;

        for (int slot : FILTER_SLOTS) {
            ItemStack stack = menu.getItemInSlot(slot);

            if (stack != null && stack.getType() != Material.AIR) {
                itemsToCompare++;

                if (itemsToCompare > 1) {
                    break;
                }
            }
        }

        // Check if there are event non-air items
        if (itemsToCompare > 0) {
            // Only create the Wrapper if its worth it
            if (itemsToCompare > 1) {
                // Create an itemStackWrapper to save performance
                item = new ItemStackWrapper(item);
            }

            for (int slot : FILTER_SLOTS) {
                ItemStack stack = menu.getItemInSlot(slot);

                if (SlimefunUtils.isItemSimilar(stack, item, respectLore, false)) {
                    return !defaultValue;
                }
            }
        }

        return defaultValue;
    }

    /**
     * This method checks if a given {@link ItemStack} is smeltable or not.
     * The lazy-option is a performance-saver since actually calculating this can be quite expensive.
     * For the current applicational purposes a quick check for any wooden logs is sufficient.
     * Otherwise the "lazyness" can be turned off in the future.
     * 
     * @param stack
     *            The {@link ItemStack} to test
     * @param lazy
     *            Whether or not to perform a "lazy" but performance-saving check
     * 
     * @return Whether the given {@link ItemStack} can be smelted or not
     */
    private static boolean isSmeltable(@Nullable ItemStack stack, boolean lazy) {
        if (lazy) {
            return stack != null && Tag.LOGS.isTagged(stack.getType());
        } else {
            return SlimefunPlugin.getMinecraftRecipeService().isSmeltable(stack);
        }
    }

    private static boolean isPotion(@Nullable ItemStack item) {
        return item != null && (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION);
    }

    /**
     * Get the whitelist/blacklist slots in a Cargo Input Node. If you wish to access the items
     * in the cargo (without hardcoding the slots in case of change) then you can use this method.
     *
     * @return The slot indexes for the whitelist/blacklist section.
     */
    public static int[] getWhitelistBlacklistSlots() {
        return FILTER_SLOTS;
    }
}
