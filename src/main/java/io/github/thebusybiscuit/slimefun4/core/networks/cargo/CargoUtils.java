package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

final class CargoUtils {

    // Whitelist or blacklist slots
    private static final int[] SLOTS = { 19, 20, 21, 28, 29, 30, 37, 38, 39 };

    private CargoUtils() {}

    public static boolean hasInventory(Block block) {
        if (block == null) {
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

    public static ItemStack withdraw(Block node, Block target, ItemStack template) {
        DirtyChestMenu menu = getChestMenu(target);

        if (menu == null) {
            if (hasInventory(target)) {
                BlockState state = target.getState();

                if (state instanceof InventoryHolder) {
                    return withdrawFromVanillaInventory(node, template, ((InventoryHolder) state).getInventory());
                }
            }
            return null;
        }

        ItemStackWrapper wrapper = new ItemStackWrapper(template);

        for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
            ItemStack is = menu.getItemInSlot(slot);

            if (SlimefunUtils.isItemSimilar(is, wrapper, true) && matchesFilter(node, is, -1)) {
                if (is.getAmount() > template.getAmount()) {
                    is.setAmount(is.getAmount() - template.getAmount());
                    menu.replaceExistingItem(slot, is);
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

    private static ItemStack withdrawFromVanillaInventory(Block node, ItemStack template, Inventory inv) {
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
            ItemStack itemInSlot = contents[slot]; // changes to this ItemStack is reflected into item in inventory

            if (SlimefunUtils.isItemSimilar(itemInSlot, wrapper, true) && matchesFilter(node, itemInSlot, -1)) {
                if (itemInSlot.getAmount() > template.getAmount()) {
                    itemInSlot.setAmount(itemInSlot.getAmount() - template.getAmount());
                    return template;
                }
                else {
                    itemInSlot.setAmount(itemInSlot.getAmount() - template.getAmount());
                    return itemInSlot;
                }
            }
        }

        return null;
    }

    public static ItemStackAndInteger withdraw(Block node, Block target, int index) {
        DirtyChestMenu menu = getChestMenu(target);

        if (menu != null) {
            for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
                ItemStack is = menu.getItemInSlot(slot);

                if (matchesFilter(node, is, index)) {
                    menu.replaceExistingItem(slot, null);
                    return new ItemStackAndInteger(is, slot);
                }
            }
        }
        else if (hasInventory(target)) {
            BlockState state = target.getState();

            if (state instanceof InventoryHolder) {
                Inventory inv = ((InventoryHolder) state).getInventory();

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

                    if (matchesFilter(node, is, index)) {
                        inv.setItem(slot, null);
                        return new ItemStackAndInteger(is, slot);
                    }
                }
            }
        }
        return null;
    }

    public static ItemStack insert(Block node, Block target, ItemStack stack, int index) {
        if (!matchesFilter(node, stack, index)) return stack;

        DirtyChestMenu menu = getChestMenu(target);

        if (menu == null) {
            if (hasInventory(target)) {
                BlockState state = target.getState();

                if (state instanceof InventoryHolder) {
                    return insertIntoVanillaInventory(stack, ((InventoryHolder) state).getInventory());
                }
            }

            return stack;
        }

        ItemStackWrapper wrapper = new ItemStackWrapper(stack);

        for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.INSERT, stack)) {
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
                minSlot = 1;
                maxSlot = 2;
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
            ItemStack itemInSlot = contents[slot]; // changes to this ItemStack is reflected into item in inventory

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
                    inv.setItem(slot, itemInSlot); // setting item in inventory will clone the ItemStack
                    return stack;
                }
            }
        }

        return stack;
    }

    public static DirtyChestMenu getChestMenu(Block block) {
        if (BlockStorage.hasInventory(block)) {
            return BlockStorage.getInventory(block);
        }

        return BlockStorage.getUniversalInventory(block);
    }

    public static boolean matchesFilter(Block block, ItemStack item, int index) {
        if (item == null || item.getType() == Material.AIR) return false;

        // Store the returned Config instance to avoid heavy calls
        Config blockInfo = BlockStorage.getLocationInfo(block.getLocation());
        if (blockInfo.getString("id").equals("CARGO_NODE_OUTPUT")) return true;

        BlockMenu menu = BlockStorage.getInventory(block.getLocation());
        if (menu == null) return false;

        boolean lore = "true".equals(blockInfo.getString("filter-lore"));

        if ("whitelist".equals(blockInfo.getString("filter-type"))) {
            List<ItemStack> templateItems = new ArrayList<>();

            for (int slot : SLOTS) {
                ItemStack template = menu.getItemInSlot(slot);
                if (template != null) {
                    templateItems.add(template);
                }
            }

            if (templateItems.isEmpty()) {
                return false;
            }

            if (index >= 0) {
                index++;
                if (index > (templateItems.size() - 1)) index = 0;

                // Should probably replace this with a simple HashMap.
                blockInfo.setValue("index", String.valueOf(index));
                BlockStorage.setBlockInfo(block, blockInfo, false);

                return SlimefunUtils.isItemSimilar(item, templateItems.get(index), lore);
            }
            else {
                for (ItemStack stack : templateItems) {
                    if (SlimefunUtils.isItemSimilar(item, stack, lore)) {
                        return true;
                    }
                }

                return false;
            }
        }
        else {
            for (int slot : SLOTS) {
                ItemStack itemInSlot = menu.getItemInSlot(slot);
                if (itemInSlot != null && SlimefunUtils.isItemSimilar(item, itemInSlot, lore, false)) {
                    return false;
                }
            }

            return true;
        }
    }

}
