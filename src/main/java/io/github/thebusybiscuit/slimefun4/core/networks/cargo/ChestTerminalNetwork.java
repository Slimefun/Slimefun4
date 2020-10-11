package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * An abstract super class of {@link CargoNet} that handles interactions with ChestTerminal.
 * 
 * @author TheBusyBiscuit
 *
 */
abstract class ChestTerminalNetwork extends Network {

    private static final int[] slots = { 19, 20, 21, 28, 29, 30, 37, 38, 39 };
    private static final int[] TERMINAL_SLOTS = { 0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15, 18, 19, 20, 21, 22, 23, 24, 27, 28, 29, 30, 31, 32, 33, 36, 37, 38, 39, 40, 41, 42 };
    private static final int TERMINAL_OUT_SLOT = 17;

    private final ItemStack terminalPlaceholderItem = new CustomItem(Material.BARRIER, "&4No Item cached");

    protected final Set<Location> terminals = new HashSet<>();
    protected final Set<Location> imports = new HashSet<>();
    protected final Set<Location> exports = new HashSet<>();

    // This represents a Queue of requests to handle
    private final Queue<ItemRequest> itemRequests = new LinkedList<>();

    // This is a cache for the BlockFace a node is facing, so we don't need to request the
    // BlockData each time we visit a node
    protected Map<Location, BlockFace> connectorCache = new HashMap<>();

    protected ChestTerminalNetwork(Location regulator) {
        super(SlimefunPlugin.getNetworkManager(), regulator);
    }

    protected Optional<Block> getAttachedBlock(Location l) {
        if (l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) {
            Block block = l.getBlock();

            if (block.getType() == Material.PLAYER_WALL_HEAD) {
                BlockFace cached = connectorCache.get(l);

                if (cached != null) {
                    return Optional.of(block.getRelative(cached));
                }

                BlockFace face = ((Directional) block.getBlockData()).getFacing().getOppositeFace();
                connectorCache.put(l, face);
                return Optional.of(block.getRelative(face));
            }
        }

        return Optional.empty();
    }

    protected void handleItemRequests(Map<Location, Inventory> inventories, Set<Location> providers, Set<Location> destinations) {
        collectImportRequests(inventories);
        collectExportRequests(inventories);
        collectTerminalRequests();

        Iterator<ItemRequest> iterator = itemRequests.iterator();
        while (iterator.hasNext()) {
            ItemRequest request = iterator.next();
            BlockMenu menu = BlockStorage.getInventory(request.getTerminal());

            if (menu != null) {
                switch (request.getDirection()) {
                case INSERT:
                    distributeInsertionRequest(inventories, request, menu, iterator, destinations);
                    break;
                case WITHDRAW:
                    collectExtractionRequest(inventories, request, menu, iterator, providers);
                    break;
                default:
                    break;
                }
            }
        }
    }

    private void distributeInsertionRequest(Map<Location, Inventory> inventories, ItemRequest request, BlockMenu terminal, Iterator<ItemRequest> iterator, Set<Location> destinations) {
        ItemStack item = request.getItem();

        for (Location l : destinations) {
            Optional<Block> target = getAttachedBlock(l);

            if (target.isPresent()) {
                item = CargoUtils.insert(inventories, l.getBlock(), target.get(), item);

                if (item == null) {
                    terminal.replaceExistingItem(request.getSlot(), null);
                    break;
                }
            }
        }

        if (item != null) {
            terminal.replaceExistingItem(request.getSlot(), item);
        }

        iterator.remove();
    }

    private void collectExtractionRequest(Map<Location, Inventory> inventories, ItemRequest request, BlockMenu terminal, Iterator<ItemRequest> iterator, Set<Location> providers) {
        int slot = request.getSlot();
        ItemStack prevStack = terminal.getItemInSlot(slot);

        if (!(prevStack == null || (prevStack.getAmount() + request.getItem().getAmount() <= prevStack.getMaxStackSize() && SlimefunUtils.isItemSimilar(prevStack, request.getItem(), true, false)))) {
            iterator.remove();
            return;
        }

        ItemStack stack = null;
        ItemStack item = request.getItem();

        for (Location l : providers) {
            Optional<Block> target = getAttachedBlock(l);

            if (target.isPresent()) {
                ItemStack is = CargoUtils.withdraw(inventories, l.getBlock(), target.get(), item);

                if (is != null) {
                    if (stack == null) {
                        stack = is;
                    } else {
                        stack = new CustomItem(stack, stack.getAmount() + is.getAmount());
                    }

                    if (is.getAmount() == item.getAmount()) {
                        break;
                    } else {
                        item = new CustomItem(item, item.getAmount() - is.getAmount());
                    }
                }
            }
        }

        if (stack != null) {
            ItemStack prev = terminal.getItemInSlot(slot);

            if (prev == null) {
                terminal.replaceExistingItem(slot, stack);
            } else {
                terminal.replaceExistingItem(slot, new CustomItem(stack, stack.getAmount() + prev.getAmount()));
            }
        }

        iterator.remove();
    }

    private void collectImportRequests(Map<Location, Inventory> inventories) {
        SlimefunItem item = SlimefunItem.getByID("CT_IMPORT_BUS");

        for (Location bus : imports) {
            long timestamp = SlimefunPlugin.getProfiler().newEntry();
            BlockMenu menu = BlockStorage.getInventory(bus);

            if (menu.getItemInSlot(17) == null) {
                Optional<Block> target = getAttachedBlock(bus);

                if (target.isPresent()) {
                    ItemStackAndInteger stack = CargoUtils.withdraw(inventories, bus.getBlock(), target.get());

                    if (stack != null) {
                        menu.replaceExistingItem(17, stack.getItem());
                    }
                }
            }

            if (menu.getItemInSlot(17) != null) {
                itemRequests.add(new ItemRequest(bus, 17, menu.getItemInSlot(17), ItemTransportFlow.INSERT));
            }

            SlimefunPlugin.getProfiler().closeEntry(bus, item, timestamp);
        }
    }

    private void collectExportRequests(Map<Location, Inventory> inventories) {
        SlimefunItem item = SlimefunItem.getByID("CT_EXPORT_BUS");

        for (Location bus : exports) {
            long timestamp = SlimefunPlugin.getProfiler().newEntry();
            BlockMenu menu = BlockStorage.getInventory(bus);

            if (menu.getItemInSlot(17) != null) {
                Optional<Block> target = getAttachedBlock(bus);

                target.ifPresent(block -> menu.replaceExistingItem(17, CargoUtils.insert(inventories, bus.getBlock(), block, menu.getItemInSlot(17))));
            }

            if (menu.getItemInSlot(17) == null) {
                List<ItemStack> items = new ArrayList<>();

                for (int slot : slots) {
                    ItemStack template = menu.getItemInSlot(slot);

                    if (template != null) {
                        items.add(new CustomItem(template, 1));
                    }
                }

                if (!items.isEmpty()) {
                    int index = Integer.parseInt(BlockStorage.getLocationInfo(bus, "index"));

                    index++;
                    if (index > (items.size() - 1)) {
                        index = 0;
                    }

                    BlockStorage.addBlockInfo(bus, "index", String.valueOf(index));
                    itemRequests.add(new ItemRequest(bus, 17, items.get(index), ItemTransportFlow.WITHDRAW));
                }
            }

            SlimefunPlugin.getProfiler().closeEntry(bus, item, timestamp);
        }
    }

    private void collectTerminalRequests() {
        for (Location terminal : terminals) {
            BlockMenu menu = BlockStorage.getInventory(terminal);
            ItemStack sendingItem = menu.getItemInSlot(TERMINAL_OUT_SLOT);

            if (sendingItem != null) {
                itemRequests.add(new ItemRequest(terminal, TERMINAL_OUT_SLOT, sendingItem, ItemTransportFlow.INSERT));
            }
        }
    }

    /**
     * This method updates every terminal on the network with {@link ItemStack ItemStacks}
     * found in any provider of the network.
     * 
     * @param providers
     *            A {@link Set} of providers to this {@link ChestTerminalNetwork}
     * 
     * @return The time it took to compute this operation
     */
    protected long updateTerminals(@Nonnull Set<Location> providers) {
        if (terminals.isEmpty()) {
            // Performance improvement - We don't need to compute items for
            // Cargo networks without any Chest Terminals
            return 0;
        }

        // Timings will be slightly inaccurate here but most often people are not
        // gonna use no more than one terminal anyway, so this might be fine
        long timestamp = System.nanoTime();
        Location firstTerminal = null;
        SlimefunItem item = SlimefunItem.getByID("CHEST_TERMINAL");
        List<ItemStackAndInteger> items = findAvailableItems(providers);

        try {
            for (Location l : terminals) {
                BlockMenu terminal = BlockStorage.getInventory(l);
                String data = BlockStorage.getLocationInfo(l, "page");
                int page = data == null ? 1 : Integer.parseInt(data);

                if (!items.isEmpty() && items.size() < (page - 1) * TERMINAL_SLOTS.length + 1) {
                    page = 1;
                    BlockStorage.addBlockInfo(l, "page", String.valueOf(1));
                }

                for (int i = 0; i < TERMINAL_SLOTS.length; i++) {
                    int slot = TERMINAL_SLOTS[i];
                    int index = i + (TERMINAL_SLOTS.length * (page - 1));
                    updateTerminal(l, terminal, slot, index, items);
                }

                if (firstTerminal == null) {
                    firstTerminal = l;
                }
            }
        } catch (Exception | LinkageError x) {
            item.error("An Exception was caused while trying to tick Chest terminals", x);
        }

        if (firstTerminal != null) {
            return SlimefunPlugin.getProfiler().closeEntry(firstTerminal, item, timestamp);
        } else {
            return System.nanoTime() - timestamp;
        }
    }

    @Override
    public void markDirty(@Nonnull Location l) {
        connectorCache.remove(l);
        super.markDirty(l);
    }

    @ParametersAreNonnullByDefault
    private void updateTerminal(Location l, BlockMenu terminal, int slot, int index, List<ItemStackAndInteger> items) {
        if (items.size() > index) {
            ItemStackAndInteger item = items.get(index);

            ItemStack stack = item.getItem().clone();
            stack.setAmount(1);
            ItemMeta im = stack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColors.color("&7Stored Items: &f" + DoubleHandler.getFancyDouble(item.getInt())));

            if (stack.getMaxStackSize() > 1) {
                int amount = item.getInt() > stack.getMaxStackSize() ? stack.getMaxStackSize() : item.getInt();
                lore.add(ChatColors.color("&7<Left Click: Request 1 | Right Click: Request " + amount + ">"));
            } else {
                lore.add(ChatColors.color("&7<Left Click: Request 1>"));
            }

            lore.add("");

            if (im.hasLore()) {
                lore.addAll(im.getLore());
            }

            im.setLore(lore);
            stack.setItemMeta(im);
            terminal.replaceExistingItem(slot, stack);
            terminal.addMenuClickHandler(slot, (p, sl, is, action) -> {
                int amount = item.getInt() > item.getItem().getMaxStackSize() ? item.getItem().getMaxStackSize() : item.getInt();
                ItemStack requestedItem = new CustomItem(item.getItem(), action.isRightClicked() ? amount : 1);
                itemRequests.add(new ItemRequest(l, 44, requestedItem, ItemTransportFlow.WITHDRAW));
                return false;
            });

        } else {
            terminal.replaceExistingItem(slot, terminalPlaceholderItem);
            terminal.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Nonnull
    private List<ItemStackAndInteger> findAvailableItems(@Nonnull Set<Location> providers) {
        List<ItemStackAndInteger> items = new LinkedList<>();

        for (Location l : providers) {
            Optional<Block> block = getAttachedBlock(l);

            if (block.isPresent()) {
                findAllItems(items, l, block.get());
            }
        }

        Collections.sort(items, Comparator.comparingInt(item -> -item.getInt()));
        return items;
    }

    @ParametersAreNonnullByDefault
    private void findAllItems(List<ItemStackAndInteger> items, Location l, Block target) {
        UniversalBlockMenu menu = BlockStorage.getUniversalInventory(target);

        if (menu != null) {
            for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
                ItemStack is = menu.getItemInSlot(slot);
                filter(is, items, l);
            }
        } else if (BlockStorage.hasInventory(target)) {
            BlockMenu blockMenu = BlockStorage.getInventory(target);

            if (blockMenu.getPreset().getID().startsWith("BARREL_")) {
                gatherItemsFromBarrel(l, blockMenu, items);
            } else {
                handleWithdraw(blockMenu, items, l);
            }
        } else if (CargoUtils.hasInventory(target)) {
            BlockState state = PaperLib.getBlockState(target, false).getState();

            if (state instanceof InventoryHolder) {
                Inventory inv = ((InventoryHolder) state).getInventory();

                for (ItemStack is : inv.getContents()) {
                    filter(is, items, l);
                }
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void gatherItemsFromBarrel(Location l, BlockMenu blockMenu, List<ItemStackAndInteger> items) {
        try {
            Config cfg = BlockStorage.getLocationInfo(blockMenu.getLocation());
            String data = cfg.getString("storedItems");

            if (data == null) {
                return;
            }

            int stored = Integer.parseInt(data);

            for (int slot : blockMenu.getPreset().getSlotsAccessedByItemTransport((DirtyChestMenu) blockMenu, ItemTransportFlow.WITHDRAW, null)) {
                ItemStack is = blockMenu.getItemInSlot(slot);

                if (is != null && CargoUtils.matchesFilter(l.getBlock(), is)) {
                    boolean add = true;

                    for (ItemStackAndInteger item : items) {
                        if (SlimefunUtils.isItemSimilar(is, item.getItemStackWrapper(), true, false)) {
                            add = false;
                            item.add(is.getAmount() + stored);
                        }
                    }

                    if (add) {
                        items.add(new ItemStackAndInteger(is, is.getAmount() + stored));
                    }
                }
            }
        } catch (Exception x) {
            Slimefun.getLogger().log(Level.SEVERE, "An Exception occurred while trying to read data from a Barrel", x);
        }
    }

    @ParametersAreNonnullByDefault
    private void handleWithdraw(DirtyChestMenu menu, List<ItemStackAndInteger> items, Location l) {
        for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
            filter(menu.getItemInSlot(slot), items, l);
        }
    }

    @ParametersAreNonnullByDefault
    private void filter(@Nullable ItemStack stack, List<ItemStackAndInteger> items, Location node) {
        if (stack != null && CargoUtils.matchesFilter(node.getBlock(), stack)) {
            boolean add = true;

            for (ItemStackAndInteger item : items) {
                if (SlimefunUtils.isItemSimilar(stack, item.getItemStackWrapper(), true, false)) {
                    add = false;
                    item.add(stack.getAmount());
                }
            }

            if (add) {
                items.add(new ItemStackAndInteger(stack, stack.getAmount()));
            }
        }
    }

}
