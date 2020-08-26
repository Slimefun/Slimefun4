package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import java.util.Map;
import java.util.logging.Level;

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
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
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
    static boolean hasInventory(Block block) {
        if (block == null) {
            // No block, no inventory
            return false;
        }

        Material type = block.getType();

        switch (type) {
        case CHEST:
        case TRAPPED_CHEST:
        case FURNACE:
        case DISPENSER:
        case DROPPER:
        case HOPPER:
        case BREWING_STAND:
        case SHULKER_BOX:
            return true;
        default:
            break;
        }

        if (type.name().endsWith("_SHULKER_BOX")) {
            return true;
        }

        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            switch (type) {
            case BARREL:
            case BLAST_FURNACE:
            case SMOKER:
                return true;
            default:
                break;
            }
        }

        return false;
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
                }
                else {
                    menu.replaceExistingItem(slot, null);
                    return is;
                }
            }
        }

        return null;
    }

    static ItemStack withdrawFromVanillaInventory(Block node, ItemStack template, Inventory inv) {
        ItemStack[] contents = inv.getContents();
        int minSlot = 0;
        int maxSlot = contents.length;

        if (inv instanceof FurnaceInventory) {
            minSlot = 2;
            maxSlot = 3;
        }
        else if (inv instanceof BrewerInventory) {
            maxSlot = 3;
        }

        ItemStackWrapper wrapper = new ItemStackWrapper(template);

        for (int slot = minSlot; slot < maxSlot; slot++) {
            // Changes to these ItemStacks are synchronized with the Item in the Inventory
            ItemStack itemInSlot = contents[slot];

            if (SlimefunUtils.isItemSimilar(itemInSlot, wrapper, true, false) && matchesFilter(node, itemInSlot)) {
                if (itemInSlot.getAmount() > template.getAmount()) {
                    itemInSlot.setAmount(itemInSlot.getAmount() - template.getAmount());
                    return template;
                }
                else {
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
        }
        else if (hasInventory(target)) {
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
        int minSlot = 0;
        int maxSlot = contents.length;

        if (inv instanceof FurnaceInventory) {
            minSlot = 2;
            maxSlot = 3;
        }
        else if (inv instanceof BrewerInventory) {
            maxSlot = 3;
        }

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
                }
                else {
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
        int minSlot = 0;
        int maxSlot = contents.length;

        // Check if it is a normal furnace
        if (inv instanceof FurnaceInventory) {
            // Check if it is fuel or not
            if (stack.getType().isFuel()) {
                maxSlot = 2;

                // Any non-smeltable items should not land in the upper slot
                if (!isSmeltable(stack, true)) {
                    minSlot = 1;
                }
            }
            else {
                maxSlot = 1;
            }
        }
        else if (inv instanceof BrewerInventory) {
            if (stack.getType() == Material.POTION || stack.getType() == Material.LINGERING_POTION || stack.getType() == Material.SPLASH_POTION) {
                // Potions slot
                maxSlot = 3;
            }
            else if (stack.getType() == Material.BLAZE_POWDER) {
                // Blaze Powder slot
                minSlot = 4;
                maxSlot = 5;
            }
            else {
                // Input slot
                minSlot = 3;
                maxSlot = 4;
            }
        }

        ItemStackWrapper wrapper = new ItemStackWrapper(stack);

        for (int slot = minSlot; slot < maxSlot; slot++) {
            // Changes to this ItemStack are synchronized with the Item in the Inventory
            ItemStack itemInSlot = contents[slot];

            if (itemInSlot == null) {
                inv.setItem(slot, stack);
                return null;
            }
            else {
                int maxStackSize = itemInSlot.getType().getMaxStackSize();

                if (SlimefunUtils.isItemSimilar(itemInSlot, wrapper, true, false) && itemInSlot.getAmount() < maxStackSize) {
                    int amount = itemInSlot.getAmount() + stack.getAmount();

                    if (amount > maxStackSize) {
                        stack.setAmount(amount - maxStackSize);
                    }
                    else {
                        stack = null;
                    }

                    itemInSlot.setAmount(Math.min(amount, maxStackSize));
                    return stack;
                }
            }
        }

        return stack;
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
    private static boolean isSmeltable(ItemStack stack, boolean lazy) {
        if (lazy) {
            return stack != null && Tag.LOGS.isTagged(stack.getType());
        }
        else {
            return SlimefunPlugin.getMinecraftRecipeService().isSmeltable(stack);
        }
    }

    static DirtyChestMenu getChestMenu(Block block) {
        if (BlockStorage.hasInventory(block)) {
            return BlockStorage.getInventory(block);
        }

        return BlockStorage.getUniversalInventory(block);
    }

    static boolean matchesFilter(Block block, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        // Store the returned Config instance to avoid heavy calls
        Config blockData = BlockStorage.getLocationInfo(block.getLocation());
        String id = blockData.getString("id");

        // Cargo Output nodes have no filter actually
        if (id.equals("CARGO_NODE_OUTPUT")) {
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
        }
        catch (Exception x) {
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
     * Get the whitelist/blacklist slots in a Cargo Input Node. If you wish to access the items
     * in the cargo (without hardcoding the slots in case of change) then you can use this method.
     *
     * @return The slot indexes for the whitelist/blacklist section.
     */
    public static int[] getWhitelistBlacklistSlots() {
        return FILTER_SLOTS;
    }
}
