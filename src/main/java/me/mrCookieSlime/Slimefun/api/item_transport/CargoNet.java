package me.mrCookieSlime.Slimefun.api.item_transport;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkComponent;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Level;

public class CargoNet extends Network {

    private static final int RANGE = 5;
    private static final int[] slots = {19, 20, 21, 28, 29, 30, 37, 38, 39};

    // Chest Terminal Stuff
    private static final int[] TERMINAL_SLOTS = {0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15, 18, 19, 20, 21, 22, 23, 24, 27, 28, 29, 30, 31, 32, 33, 36, 37, 38, 39, 40, 41, 42};
    private static final int TERMINAL_OUT_SLOT = 17;

    private final ItemStack terminalPlaceholderItem = new CustomItem(new ItemStack(Material.BARRIER), "&4没有已缓存的物品");

    private Set<Location> inputNodes = new HashSet<>();
    private Set<Location> outputNodes = new HashSet<>();

    // Chest Terminal Stuff
    private final Set<Location> terminals = new HashSet<>();
    private final Set<Location> imports = new HashSet<>();
    private final Set<Location> exports = new HashSet<>();

    private final Map<Location, Integer> roundRobin = new HashMap<>();
    private final Set<ItemRequest> itemRequests = new HashSet<>();

    public static CargoNet getNetworkFromLocation(Location l) {
        return SlimefunPlugin.getNetworkManager().getNetworkFromLocation(l, CargoNet.class);
    }

    public static CargoNet getNetworkFromLocationOrCreate(Location l) {
        CargoNet cargoNetwork = getNetworkFromLocation(l);

        if (cargoNetwork == null) {
            cargoNetwork = new CargoNet(l);
            SlimefunPlugin.getNetworkManager().registerNetwork(cargoNetwork);
        }

        return cargoNetwork;
    }

    protected CargoNet(Location l) {
        super(l);
    }

    public int getRange() {
        return RANGE;
    }

    public NetworkComponent classifyLocation(Location l) {
        String id = BlockStorage.checkID(l);
        if (id == null) return null;

        switch (id) {
            case "CARGO_MANAGER":
                return NetworkComponent.REGULATOR;
            case "CARGO_NODE":
                return NetworkComponent.CONNECTOR;
            case "CARGO_NODE_INPUT":
            case "CARGO_NODE_OUTPUT":
            case "CARGO_NODE_OUTPUT_ADVANCED":
            case "CT_IMPORT_BUS":
            case "CT_EXPORT_BUS":
            case "CHEST_TERMINAL":
                return NetworkComponent.TERMINUS;
            default:
                return null;
        }
    }

    public void onClassificationChange(Location l, NetworkComponent from, NetworkComponent to) {
        if (from == NetworkComponent.TERMINUS) {
            inputNodes.remove(l);
            outputNodes.remove(l);
            terminals.remove(l);
            imports.remove(l);
            exports.remove(l);
        }
        if (to == NetworkComponent.TERMINUS) {
            switch (BlockStorage.checkID(l)) {
                case "CARGO_NODE_INPUT":
                    inputNodes.add(l);
                    break;
                case "CARGO_NODE_OUTPUT":
                case "CARGO_NODE_OUTPUT_ADVANCED":
                    outputNodes.add(l);
                    break;
                case "CHEST_TERMINAL":
                    terminals.add(l);
                    break;
                case "CT_IMPORT_BUS":
                    imports.add(l);
                    break;
                case "CT_EXPORT_BUS":
                    exports.add(l);
                    break;
                default:
                    break;
            }
        }
    }

    public void tick(Block b) {
        if (!regulator.equals(b.getLocation())) {
            SimpleHologram.update(b, "&4检测到连接到了多个货运网络调节器");
            return;
        }

        super.tick();

        if (connectorNodes.isEmpty() && terminusNodes.isEmpty()) {
            SimpleHologram.update(b, "&c找不到货运网络节点");
        }
        else {
            SimpleHologram.update(b, "&7状态: &a&l在线");
            Map<Integer, List<Location>> output = new HashMap<>();

            List<Location> list = new LinkedList<>();
            int lastFrequency = -1;

            for (Location outputNode : outputNodes) {
                int frequency = getFrequency(outputNode);

                if (frequency != lastFrequency && lastFrequency != -1) {
                    output.merge(lastFrequency, list, (prev, next) -> {
                        prev.addAll(next);
                        return prev;
                    });

                    list = new LinkedList<>();
                }

                list.add(outputNode);
                lastFrequency = frequency;
            }

            if (!list.isEmpty()) {
                output.merge(lastFrequency, list, (prev, next) -> {
                    prev.addAll(next);
                    return prev;
                });
            }

            // Chest Terminal Stuff
            Set<Location> providers = new HashSet<>();
            Set<Location> destinations = new HashSet<>();

            List<Location> output16 = output.get(16);
            if (output16 != null) destinations.addAll(output16);

            for (Location inputNode : inputNodes) {
                int frequency = getFrequency(inputNode);

                if (frequency == 16) {
                    providers.add(inputNode);
                }
            }

            Slimefun.runSync(() -> run(b, providers, destinations, output));
        }
    }

    private void run(Block b, Set<Location> providers, Set<Location> destinations, Map<Integer, List<Location>> output) {
        if (BlockStorage.getLocationInfo(b.getLocation(), "visualizer") == null) {
            display();
        }

        // Chest Terminal Code
        if (SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled()) {
            for (Location bus : imports) {
                BlockMenu menu = BlockStorage.getInventory(bus);

                if (menu.getItemInSlot(17) == null) {
                    Block target = getAttachedBlock(bus.getBlock());
                    ItemStackAndInteger stack = CargoUtils.withdraw(bus.getBlock(), target, -1);

                    if (stack != null) {
                        menu.replaceExistingItem(17, stack.getItem());
                    }
                }

                if (menu.getItemInSlot(17) != null) {
                    itemRequests.add(new ItemRequest(bus, 17, menu.getItemInSlot(17), ItemTransportFlow.INSERT));
                }
            }

            for (Location bus : exports) {
                BlockMenu menu = BlockStorage.getInventory(bus);

                if (menu.getItemInSlot(17) != null) {
                    Block target = getAttachedBlock(bus.getBlock());

                    menu.replaceExistingItem(17, CargoUtils.insert(bus.getBlock(), target, menu.getItemInSlot(17), -1));
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
                                Block target = getAttachedBlock(l.getBlock());
                                requestedItem = CargoUtils.insert(l.getBlock(), target, requestedItem, -1);

                                if (requestedItem == null) {
                                    menu.replaceExistingItem(request.getSlot(), null);
                                    break;
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

                            if (!(prevStack == null || (prevStack.getAmount() + request.getItem().getAmount() <= prevStack.getMaxStackSize() && SlimefunManager.isItemSimilar(prevStack, new CustomItem(request.getItem(), 1), true)))) {
                                iterator.remove();
                                break;
                            }

                            ItemStack stack = null;
                            ItemStack requested = request.getItem();

                            for (Location l : providers) {
                                Block target = getAttachedBlock(l.getBlock());
                                ItemStack is = CargoUtils.withdraw(l.getBlock(), target, requested);

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

        // All operations happen here: Everything gets iterated from the Input Nodes. (Apart from ChestTerminal
        // Buses)
        for (Location input : inputNodes) {
            int frequency = getFrequency(input);

            if (frequency < 0 || frequency > 15) {
                continue;
            }

            Block inputTarget = getAttachedBlock(input.getBlock());
            ItemStack stack = null;
            int previousSlot = -1;

            Config cfg = BlockStorage.getLocationInfo(input);
            boolean roundrobin = "true".equals(cfg.getString("round-robin"));

            if (inputTarget != null) {
                ItemStackAndInteger slot = CargoUtils.withdraw(input.getBlock(), inputTarget, Integer.parseInt(cfg.getString("index")));

                if (slot != null) {
                    stack = slot.getItem();
                    previousSlot = slot.getInt();
                }
            }

            if (stack != null) {
                List<Location> outputs = output.get(frequency);

                if (outputs != null) {
                    List<Location> outputlist = new ArrayList<>(outputs);

                    if (roundrobin) {
                        int cIndex = roundRobin.getOrDefault(input, 0);

                        if (cIndex < outputlist.size()) {
                            for (int i = 0; i < cIndex; i++) {
                                Location temp = outputlist.get(0);
                                outputlist.remove(temp);
                                outputlist.add(temp);
                            }
                            cIndex++;
                        } else cIndex = 1;

                        roundRobin.put(input, cIndex);
                    }

                    for (Location out : outputlist) {
                        Block target = getAttachedBlock(out.getBlock());

                        if (target != null) {
                            stack = CargoUtils.insert(out.getBlock(), target, stack, -1);
                            if (stack == null) break;
                        }
                    }
                }
            }

            if (stack != null && previousSlot > -1) {
                DirtyChestMenu menu = CargoUtils.getChestMenu(inputTarget);

                if (menu != null) {
                    menu.replaceExistingItem(previousSlot, stack);
                } else {
                    BlockState state = inputTarget.getState();
                    if (state instanceof InventoryHolder) {
                        Inventory inv = ((InventoryHolder) state).getInventory();
                        inv.setItem(previousSlot, stack);
                    }
                }
            }
        }

        // Chest Terminal Code
        if (SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled()) {
            List<ItemStackAndInteger> items = new ArrayList<>();

            for (Location l : providers) {
                Block target = getAttachedBlock(l.getBlock());
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
                                    if (SlimefunManager.isItemSimilar(is, item.getItem(), true)) {
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
                        lore.add(ChatColors.color("&7已储存的物品数: &r" + DoubleHandler.getFancyDouble(item.getInt())));

                        if (stack.getMaxStackSize() > 1)
                            lore.add(ChatColors.color("&7<左键: 拿取一个物品 | 右键: 拿取 " + (Math.min(item.getInt(), stack.getMaxStackSize())) + "个物品>"));
                        else lore.add(ChatColors.color("&7<左键: 拿取一个物品>"));

                        lore.add("");
                        if (im.hasLore()) {
                            lore.addAll(im.getLore());
                        }

                        im.setLore(lore);
                        stack.setItemMeta(im);
                        menu.replaceExistingItem(slot, stack);
                        menu.addMenuClickHandler(slot, (p, sl, is, action) -> {
                            int amount = Math.min(item.getInt(), item.getItem().getMaxStackSize());
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
    }

    private static Block getAttachedBlock(Block block) {
        if (block.getBlockData() instanceof Directional) {
            return block.getRelative(((Directional) block.getBlockData()).getFacing().getOppositeFace());
        }

        return null;
    }

    private static int getFrequency(Location l) {
        int freq = 0;
        try {
            String str = BlockStorage.getLocationInfo(l).getString("frequency");
            if (str != null) freq = Integer.parseInt(str);
        } catch (Exception x) {
            Slimefun.getLogger().log(Level.SEVERE, "An Error occured while parsing a Cargo Node Frequency", x);
        }
        return freq;
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
                if (SlimefunManager.isItemSimilar(is, item.getItem(), true)) {
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