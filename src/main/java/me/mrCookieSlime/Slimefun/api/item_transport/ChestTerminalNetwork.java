package me.mrCookieSlime.Slimefun.api.item_transport;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
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

import java.util.*;

/**
 * An abstract super class of {@link CargoNet} that handles interactions with ChestTerminal.
 *
 * @author TheBusyBiscuit
 */
abstract class ChestTerminalNetwork extends Network {

    private static final int[] slots = {19, 20, 21, 28, 29, 30, 37, 38, 39};
    private static final int[] TERMINAL_SLOTS = {0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15, 18, 19, 20, 21, 22, 23, 24, 27, 28, 29, 30, 31, 32, 33, 36, 37, 38, 39, 40, 41, 42};
    private static final int TERMINAL_OUT_SLOT = 17;

    private final ItemStack terminalPlaceholderItem = new CustomItem(Material.BARRIER, "&4No Item cached");

    protected final Set<Location> terminals = new HashSet<>();
    protected final Set<Location> imports = new HashSet<>();
    protected final Set<Location> exports = new HashSet<>();

    private final Set<ItemRequest> itemRequests = new HashSet<>();

    protected ChestTerminalNetwork(Location regulator) {
        super(regulator);
    }

    protected static Optional<Block> getAttachedBlock(Block block) {
        if (block.getType() == Material.PLAYER_WALL_HEAD) {
            BlockFace face = ((Directional) block.getBlockData()).getFacing().getOppositeFace();
            return Optional.of(block.getRelative(face));
        }

        return Optional.empty();
    }

    protected void handleItemRequests(Set<Location> providers, Set<Location> destinations) {
        for (Location bus : imports) {
            BlockMenu menu = BlockStorage.getInventory(bus);

            if (menu.getItemInSlot(17) == null) {
                Optional<Block> target = getAttachedBlock(bus.getBlock());

                if (target.isPresent()) {
                    ItemStackAndInteger stack = CargoUtils.withdraw(bus.getBlock(), target.get(), -1);

                    if (stack != null) {
                        menu.replaceExistingItem(17, stack.getItem());
                    }
                }
            }

            if (menu.getItemInSlot(17) != null) {
                itemRequests.add(new ItemRequest(bus, 17, menu.getItemInSlot(17), ItemTransportFlow.INSERT));
            }
        }

        for (Location bus : exports) {
            BlockMenu menu = BlockStorage.getInventory(bus);

            if (menu.getItemInSlot(17) != null) {
                Optional<Block> target = getAttachedBlock(bus.getBlock());

                if (target.isPresent()) {
                    menu.replaceExistingItem(17, CargoUtils.insert(bus.getBlock(), target.get(), menu.getItemInSlot(17), -1));
                }
            }

            if (menu.getItemInSlot(17) == null) {
                List<ItemStack> items = new ArrayList<>();
                for (int slot : slots) {
                    ItemStack template = menu.getItemInSlot(slot);
                    if (template != null) items.add(new CustomItem(template, 1));
                }

                if (!items.isEmpty()) {
                    int index = Integer.parseInt(BlockStorage.getLocationInfo(bus, "index"));

                    index++;
                    if (index > (items.size() - 1)) index = 0;

                    BlockStorage.addBlockInfo(bus, "index", String.valueOf(index));
                    itemRequests.add(new ItemRequest(bus, 17, items.get(index), ItemTransportFlow.WITHDRAW));
                }
            }
        }

        for (Location terminal : terminals) {
            BlockMenu menu = BlockStorage.getInventory(terminal);
            ItemStack sendingItem = menu.getItemInSlot(TERMINAL_OUT_SLOT);

            if (sendingItem != null) {
                itemRequests.add(new ItemRequest(terminal, TERMINAL_OUT_SLOT, sendingItem, ItemTransportFlow.INSERT));
            }
        }

        Iterator<ItemRequest> iterator = itemRequests.iterator();
        while (iterator.hasNext()) {
            ItemRequest request = iterator.next();

            if (terminals.contains(request.getTerminal()) || imports.contains(request.getTerminal()) || exports.contains(request.getTerminal())) {
                BlockMenu menu = BlockStorage.getInventory(request.getTerminal());

                switch (request.getDirection()) {
                    case INSERT:
                        ItemStack requestedItem = request.getItem();

                        for (Location l : destinations) {
                            Optional<Block> target = getAttachedBlock(l.getBlock());

                            if (target.isPresent()) {
                                requestedItem = CargoUtils.insert(l.getBlock(), target.get(), requestedItem, -1);

                                if (requestedItem == null) {
                                    menu.replaceExistingItem(request.getSlot(), null);
                                    break;
                                }
                            }
                        }

                        if (requestedItem != null) {
                            menu.replaceExistingItem(request.getSlot(), requestedItem);
                        }

                        iterator.remove();
                        break;
                    case WITHDRAW:
                        int slot = request.getSlot();
                        ItemStack prevStack = menu.getItemInSlot(slot);

                        if (!(prevStack == null || (prevStack.getAmount() + request.getItem().getAmount() <= prevStack.getMaxStackSize() && SlimefunUtils.isItemSimilar(prevStack, new CustomItem(request.getItem(), 1), true)))) {
                            iterator.remove();
                            break;
                        }

                        ItemStack stack = null;
                        ItemStack requested = request.getItem();

                        for (Location l : providers) {
                            Optional<Block> target = getAttachedBlock(l.getBlock());

                            if (target.isPresent()) {
                                ItemStack is = CargoUtils.withdraw(l.getBlock(), target.get(), requested);

                                if (is != null) {
                                    if (stack == null) {
                                        stack = is;
                                    } else {
                                        stack = new CustomItem(stack, stack.getAmount() + is.getAmount());
                                    }

                                    if (is.getAmount() == requested.getAmount()) {
                                        break;
                                    } else {
                                        requested = new CustomItem(requested, requested.getAmount() - is.getAmount());
                                    }
                                }
                            }
                        }

                        if (stack != null) {
                            ItemStack prev = menu.getItemInSlot(slot);

                            if (prev == null) menu.replaceExistingItem(slot, stack);
                            else
                                menu.replaceExistingItem(slot, new CustomItem(stack, stack.getAmount() + prev.getAmount()));
                        }

                        iterator.remove();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    protected void updateTerminals(Set<Location> providers) {
        List<ItemStackAndInteger> items = new ArrayList<>();

        for (Location l : providers) {
            Optional<Block> block = getAttachedBlock(l.getBlock());

            if (block.isPresent()) {
                Block target = block.get();
                UniversalBlockMenu menu = BlockStorage.getUniversalInventory(target);

                if (menu != null) {
                    for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
                        ItemStack is = menu.getItemInSlot(slot);
                        filter(is, items, l);
                    }
                } else if (BlockStorage.hasInventory(target)) {
                    BlockMenu blockMenu = BlockStorage.getInventory(target);
                    Config cfg = BlockStorage.getLocationInfo(target.getLocation());

                    if (cfg.getString("id").startsWith("BARREL_") && cfg.getString("storedItems") != null) {
                        int stored = Integer.parseInt(cfg.getString("storedItems"));

                        for (int slot : blockMenu.getPreset().getSlotsAccessedByItemTransport(blockMenu, ItemTransportFlow.WITHDRAW, null)) {
                            ItemStack is = blockMenu.getItemInSlot(slot);

                            if (is != null && CargoUtils.matchesFilter(l.getBlock(), is, -1)) {
                                boolean add = true;

                                for (ItemStackAndInteger item : items) {
                                    if (SlimefunUtils.isItemSimilar(is, item.getItem(), true)) {
                                        add = false;
                                        item.add(is.getAmount() + stored);
                                    }
                                }

                                if (add) {
                                    items.add(new ItemStackAndInteger(new CustomItem(is, 1), is.getAmount() + stored));
                                }
                            }
                        }
                    } else {
                        handleWithdraw(blockMenu, items, l);
                    }
                } else {
                    BlockState state = target.getState();

                    if (state instanceof InventoryHolder) {
                        Inventory inv = ((InventoryHolder) state).getInventory();

                        for (ItemStack is : inv.getContents()) {
                            filter(is, items, l);
                        }
                    }
                }
            }
        }

        Collections.sort(items, Comparator.comparingInt(item -> -item.getInt()));

        for (Location l : terminals) {
            BlockMenu menu = BlockStorage.getInventory(l);
            int page = Integer.parseInt(BlockStorage.getLocationInfo(l, "page"));

            if (!items.isEmpty() && items.size() < (page - 1) * TERMINAL_SLOTS.length + 1) {
                page = 1;
                BlockStorage.addBlockInfo(l, "page", String.valueOf(1));
            }

            for (int i = 0; i < TERMINAL_SLOTS.length; i++) {
                int slot = TERMINAL_SLOTS[i];

                if (items.size() > i + (TERMINAL_SLOTS.length * (page - 1))) {
                    ItemStackAndInteger item = items.get(i + (TERMINAL_SLOTS.length * (page - 1)));

                    ItemStack stack = item.getItem().clone();
                    ItemMeta im = stack.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add("");
                    lore.add(ChatColors.color("&7Stored Items: &r" + DoubleHandler.getFancyDouble(item.getInt())));

                    if (stack.getMaxStackSize() > 1)
                        lore.add(ChatColors.color("&7<Left Click: Request 1 | Right Click: Request " + (item.getInt() > stack.getMaxStackSize() ? stack.getMaxStackSize() : item.getInt()) + ">"));
                    else lore.add(ChatColors.color("&7<Left Click: Request 1>"));

                    lore.add("");
                    if (im.hasLore()) {
                        lore.addAll(im.getLore());
                    }

                    im.setLore(lore);
                    stack.setItemMeta(im);
                    menu.replaceExistingItem(slot, stack);
                    menu.addMenuClickHandler(slot, (p, sl, is, action) -> {
                        int amount = item.getInt() > item.getItem().getMaxStackSize() ? item.getItem().getMaxStackSize() : item.getInt();
                        itemRequests.add(new ItemRequest(l, 44, new CustomItem(item.getItem(), action.isRightClicked() ? amount : 1), ItemTransportFlow.WITHDRAW));
                        return false;
                    });

                } else {
                    menu.replaceExistingItem(slot, terminalPlaceholderItem);
                    menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
                }
            }
        }
    }

    private void handleWithdraw(DirtyChestMenu menu, List<ItemStackAndInteger> items, Location l) {
        for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
            filter(menu.getItemInSlot(slot), items, l);
        }
    }

    private void filter(ItemStack is, List<ItemStackAndInteger> items, Location l) {
        if (is != null && CargoUtils.matchesFilter(l.getBlock(), is, -1)) {
            boolean add = true;

            for (ItemStackAndInteger item : items) {
                if (SlimefunUtils.isItemSimilar(is, item.getItem(), true)) {
                    add = false;
                    item.add(is.getAmount());
                }
            }

            if (add) {
                items.add(new ItemStackAndInteger(new CustomItem(is, 1), is.getAmount()));
            }
        }
    }

}