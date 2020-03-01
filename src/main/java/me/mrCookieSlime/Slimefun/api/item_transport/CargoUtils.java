package me.mrCookieSlime.Slimefun.api.item_transport;

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

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;

public final class CargoUtils {

    //Whitelist or blacklist slots
    private static final int[] SLOTS = {19, 20, 21, 28, 29, 30, 37, 38, 39};

    private CargoUtils() {}

    public static ItemStack withdraw(Block node, Block target, ItemStack template) {
        DirtyChestMenu menu = getChestMenu(target);

        if (menu != null) {
            for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
                ItemStack is = menu.getItemInSlot(slot);

                if (SlimefunManager.isItemSimilar(is, template, true) && matchesFilter(node, is, -1)) {
                    if (is.getAmount() > template.getAmount()) {
                        menu.replaceExistingItem(slot, new CustomItem(is, is.getAmount() - template.getAmount()));
                        return template;
                    } 
                    else {
                        menu.replaceExistingItem(slot, null);
                        return is.clone();
                    }
                }
            }
        } 
        else {
            BlockState state = target.getState();

            if (state instanceof InventoryHolder) {
                return withdrawFromVanillaInventory(node, template, ((InventoryHolder) state).getInventory());
            }
        }
        
        return null;
    }

    private static ItemStack withdrawFromVanillaInventory(Block node, ItemStack template, Inventory inv) {
        int minSlot = 0;
        int maxSlot = inv.getContents().length;
        
        if (inv instanceof FurnaceInventory) {
            minSlot = 2;
            maxSlot = 3;
        } 
        else if (inv instanceof BrewerInventory) {
            maxSlot = 3;
        }
        
        for (int slot = minSlot; slot < maxSlot; slot++) {
            ItemStack is = inv.getContents()[slot];

            if (SlimefunManager.isItemSimilar(is, template, true) && matchesFilter(node, is, -1)) {
                if (is.getAmount() > template.getAmount()) {
                    inv.setItem(slot, new CustomItem(is, is.getAmount() - template.getAmount()));
                    return template;
                } 
                else {
                    inv.setItem(slot, new CustomItem(is, is.getAmount() - template.getAmount()));
                    return is.clone();
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
                    return new ItemStackAndInteger(is.clone(), slot);
                }
            }
        } 
        else {
            BlockState state = target.getState();

            if (state instanceof InventoryHolder) {
                Inventory inv = ((InventoryHolder) state).getInventory();

                int minSlot = 0;
                int maxSlot = inv.getContents().length;

                if (inv instanceof FurnaceInventory) {
                    minSlot = 2;
                    maxSlot = 3;
                } 
                else if (inv instanceof BrewerInventory) {
                    maxSlot = 3;
                }

                for (int slot = minSlot; slot < maxSlot; slot++) {
                    ItemStack is = inv.getContents()[slot];

                    if (matchesFilter(node, is, index)) {
                        inv.setItem(slot, null);
                        return new ItemStackAndInteger(is.clone(), slot);
                    }
                }
            }
        }
        return null;
    }

    public static ItemStack insert(Block node, Block target, ItemStack stack, int index) {
        if (!matchesFilter(node, stack, index)) return stack;

        DirtyChestMenu menu = getChestMenu(target);

        if (menu != null) {
            for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.INSERT, stack)) {
                ItemStack is = menu.getItemInSlot(slot) == null ? null : menu.getItemInSlot(slot).clone();

                if (is == null) {
                    menu.replaceExistingItem(slot, stack.clone());
                    return null;
                } 
                else if (SlimefunManager.isItemSimilar(new CustomItem(is, 1), new CustomItem(stack, 1), true) && is.getAmount() < is.getType().getMaxStackSize()) {
                    int amount = is.getAmount() + stack.getAmount();

                    if (amount > is.getType().getMaxStackSize()) {
                        is.setAmount(is.getType().getMaxStackSize());
                        stack.setAmount(amount - is.getType().getMaxStackSize());
                    } 
                    else {
                        is.setAmount(amount);
                        stack = null;
                    }

                    menu.replaceExistingItem(slot, is);
                    return stack;
                }
            }
        } 
        else {
            BlockState state = target.getState();

            if (state instanceof InventoryHolder) {
                return insertIntoVanillaInventory(stack, ((InventoryHolder) state).getInventory());
            }
        }
        
        return stack;
    }

    private static ItemStack insertIntoVanillaInventory(ItemStack stack, Inventory inv) {
    	int minSlot = 0;
        int maxSlot = inv.getContents().length;

        //Check if it is a normal furnace
        if (inv instanceof FurnaceInventory) {
            //Check if it is fuel or not
            if (stack.getType().isFuel()) {
                minSlot = 1;
                maxSlot = 2;
            } 
            else {
                maxSlot = 1;
            }
        } 
        else if (inv instanceof BrewerInventory) {
            //Check if it goes in the potion slot,
            if (stack.getType() == Material.POTION || stack.getType() == Material.LINGERING_POTION || stack.getType() == Material.SPLASH_POTION) {
                maxSlot = 3;
                //The blaze powder slot,
            } 
            else if (stack.getType() == Material.BLAZE_POWDER) {
                minSlot = 4;
                maxSlot = 5;
            } 
            else {
                //Or the input
                minSlot = 3;
                maxSlot = 4;
            }
        }

        for (int slot = minSlot; slot < maxSlot; slot++) {
            ItemStack is = inv.getContents()[slot];

            if (is == null) {
                inv.setItem(slot, stack.clone());
                return null;
            } 
            else if (SlimefunManager.isItemSimilar(new CustomItem(is, 1), new CustomItem(stack, 1), true) && is.getAmount() < is.getType().getMaxStackSize()) {
                int amount = is.getAmount() + stack.getAmount();
                
                if (amount > is.getType().getMaxStackSize()) {
                    is.setAmount(is.getType().getMaxStackSize());
                    stack.setAmount(amount - is.getType().getMaxStackSize());
                } 
                else {
                    is.setAmount(amount);
                    stack = null;
                }

                inv.setItem(slot, is);
                return stack;
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
        if (item == null) return false;

        // Store the returned Config instance to avoid heavy calls
        Config blockInfo = BlockStorage.getLocationInfo(block.getLocation());
        if (blockInfo.getString("id").equals("CARGO_NODE_OUTPUT")) return true;

        BlockMenu menu = BlockStorage.getInventory(block.getLocation());
        boolean lore = "true".equals(blockInfo.getString("filter-lore"));

        if ("whitelist".equals(blockInfo.getString("filter-type"))) {
            List<ItemStack> items = new ArrayList<>();

            for (int slot : SLOTS) {
                ItemStack template = menu.getItemInSlot(slot);
                if (template != null) items.add(new CustomItem(template, 1));
            }

            if (items.isEmpty()) {
                return false;
            }

            if (index >= 0) {
                index++;
                if (index > (items.size() - 1)) index = 0;

                BlockStorage.addBlockInfo(block, "index", String.valueOf(index));

                return SlimefunManager.isItemSimilar(item, items.get(index), lore);
            } 
            else {
                for (ItemStack stack : items) {
                    if (SlimefunManager.isItemSimilar(item, stack, lore)) {
                    	return true;
                    }
                }
                
                return false;
            }
        } 
        else {
            for (int slot : SLOTS) {
                if (menu.getItemInSlot(slot) != null && SlimefunManager.isItemSimilar(item, new CustomItem(menu.getItemInSlot(slot), 1), lore)) {
                    return false;
                }
            }
            
            return true;
        }
    }

}
