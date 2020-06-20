package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.ReactorAccessPort;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.NetherStarReactor;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.NuclearReactor;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.holograms.ReactorHologram;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.GeneratorTicker;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The abstract {@link AReactor} class is very similar to {@link AGenerator} but is
 * exclusively used for Reactors.
 *
 * @author John000708
 *
 * @see AGenerator
 * @see NuclearReactor
 * @see NetherStarReactor
 *
 */
public abstract class AReactor extends AbstractEnergyGenerator {

    public static Map<Location, MachineFuel> processing = new HashMap<>();
    public static Map<Location, Integer> progress = new HashMap<>();

    private static final int INFO_SLOT = 49;
    private static final int COOLANT_DURATION = 50;
    private static final BlockFace[] WATER_BLOCKS = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};

    private static final int[] border = {0, 1, 2, 3, 5, 6, 7, 8, 12, 13, 14, 21, 23};
    private static final int[] border_1 = {9, 10, 11, 18, 20, 27, 29, 36, 38, 45, 46, 47};
    private static final int[] border_2 = {15, 16, 17, 24, 26, 33, 35, 42, 44, 51, 52, 53};
    private static final int[] border_3 = {30, 31, 32, 39, 41, 48, 50};

    // No coolant border
    private static final int[] border_4 = {25, 34, 43};

    public AReactor(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        new BlockMenuPreset(getID(), getInventoryTitle()) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(BlockMenu menu, Block b) {
                if (BlockStorage.getLocationInfo(b.getLocation(), "reactor-mode") == null) {
                    BlockStorage.addBlockInfo(b, "reactor-mode", "generator");
                }

                if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "reactor-mode").equals("generator")) {
                    menu.replaceExistingItem(4, new CustomItem(SlimefunUtils.getCustomHead("9343ce58da54c79924a2c9331cfc417fe8ccbbea9be45a7ac85860a6c730"), "&7模式: &e发电", "", "&6你的反应堆将专注于发电", "&6如果你的能量网络不需要更多电力", "&6它将停止发电", "", "&7> 点击更改到 &e生产 &7模式"));
                    menu.addMenuClickHandler(4, (p, slot, item, action) -> {
                        BlockStorage.addBlockInfo(b, "reactor-mode", "production");
                        newInstance(menu, b);
                        return false;
                    });
                }
                else {
                    menu.replaceExistingItem(4, new CustomItem(SlimefunItems.PLUTONIUM, "&7模式: &e生产", "", "&6你的反应堆将专注于生产衰变后的产物", "&6如果你的能量网络不需要电力", "&6它将继续运行并生产", "&6同时也不会产生电力", "", "&7> 点击更改到 &e发电 &7模式"));
                    menu.addMenuClickHandler(4, (p, slot, item, action) -> {
                        BlockStorage.addBlockInfo(b, "reactor-mode", "generator");
                        newInstance(menu, b);
                        return false;
                    });
                }

                BlockMenu port = getAccessPort(b.getLocation());
                if (port != null) {
                    menu.replaceExistingItem(INFO_SLOT, new CustomItem(new ItemStack(Material.GREEN_WOOL), "&7反应堆访问接口", "", "&6已检测到", "", "&7> 点击打开访问接口界面"));
                    menu.addMenuClickHandler(INFO_SLOT, (p, slot, item, action) -> {
                        port.open(p);
                        newInstance(menu, b);

                        return false;
                    });
                }
                else {
                    menu.replaceExistingItem(INFO_SLOT, new CustomItem(new ItemStack(Material.RED_WOOL), "&7反应堆访问接口", "", "&c未检测到", "", "&7访问接口必须", "&7放置比反应堆", "&7高出三个方块的位置"));
                    menu.addMenuClickHandler(INFO_SLOT, (p, slot, item, action) -> {
                        newInstance(menu, b);
                        menu.open(p);
                        return false;
                    });
                }
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };

        registerBlockHandler(getID(), (p, b, tool, reason) -> {
            BlockMenu inv = BlockStorage.getInventory(b);

            if (inv != null) {
                for (int slot : getFuelSlots()) {
                    if (inv.getItemInSlot(slot) != null) {
                        b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        inv.replaceExistingItem(slot, null);
                    }
                }

                for (int slot : getCoolantSlots()) {
                    if (inv.getItemInSlot(slot) != null) {
                        b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        inv.replaceExistingItem(slot, null);
                    }
                }

                for (int slot : getOutputSlots()) {
                    if (inv.getItemInSlot(slot) != null) {
                        b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        inv.replaceExistingItem(slot, null);
                    }
                }
            }

            progress.remove(b.getLocation());
            processing.remove(b.getLocation());
            SimpleHologram.remove(b);
            return true;
        });

        registerDefaultFuelTypes();
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : border_1) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : border_3) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());

        preset.addItem(1, new CustomItem(getFuelIcon(), "&7燃料槽", "", "&r这个槽可以放置放射性质的燃料例如", "&2铀 &r或 &a镎"), ChestMenuUtils.getEmptyClickHandler());

        for (int i : border_2) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
        }

        if (needsCooling()) {
            preset.addItem(7, new CustomItem(getCoolant(), "&b冷却剂槽", "", "&r这个槽可以放置反应堆冷却剂", "&4如果没有任何冷却剂", "&4你的反应堆将爆炸"));
        } else {
            preset.addItem(7, new CustomItem(new ItemStack(Material.BARRIER), "&b冷却剂槽", "", "&r这个槽可以放置反应堆冷却剂"));

            for (int i : border_4) {
                preset.addItem(i, new CustomItem(new ItemStack(Material.BARRIER), "&c无需冷却剂"), ChestMenuUtils.getEmptyClickHandler());
            }
        }
    }

    public abstract void extraTick(Location l);

    /**
     * This method returns the {@link ItemStack} that is required to cool this {@link AReactor}.
     * If it returns null, then no cooling is required.
     *
     * @return The {@link ItemStack} required to cool this {@link AReactor}
     */
    public abstract ItemStack getCoolant();

    /**
     * This method returns the displayed icon above the fuel input slot.
     * It should reflect the {@link ItemStack} used to power the reactor.
     * This method does <b>not</b> determine the fuel input, only the icon.
     *
     * @return The {@link ItemStack} used as the fuel icon for this {@link AReactor}.
     */
    public abstract ItemStack getFuelIcon();

    /**
     * This method returns whether this {@link AReactor} requires as some form of
     * coolant.
     * It is a not-null check performed on {@link #getCoolant()}
     *
     * @return Whether this {@link AReactor} requires cooling
     */
    protected final boolean needsCooling() {
        return getCoolant() != null;
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{19, 28, 37, 25, 34, 43};
    }

    public int[] getFuelSlots() {
        return new int[]{19, 28, 37};
    }

    public int[] getCoolantSlots() {
        return needsCooling() ? new int[]{25, 34, 43} : new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{40};
    }

    public MachineFuel getProcessing(Location l) {
        return processing.get(l);
    }

    public boolean isProcessing(Location l) {
        return progress.containsKey(l);
    }

    @Override
    protected GeneratorTicker onTick() {
        return new GeneratorTicker() {

            private final Set<Location> explosionsQueue = new HashSet<>();

            @Override
            public double generateEnergy(Location l, SlimefunItem sf, Config data) {
                BlockMenu inv = BlockStorage.getInventory(l);
                BlockMenu accessPort = getAccessPort(l);
                int charge = ChargableBlock.getCharge(l);

                if (isProcessing(l)) {
                    extraTick(l);
                    int timeleft = progress.get(l);

                    if (timeleft > 0) {
                        int produced = getEnergyProduction();
                        int space = ChargableBlock.getMaxCharge(l) - charge;

                        if (space >= produced || !"generator".equals(BlockStorage.getLocationInfo(l, "reactor-mode"))) {
                            progress.put(l, timeleft - 1);
                            checkForWaterBlocks(l);

                            ChestMenuUtils.updateProgressbar(inv, 22, timeleft, processing.get(l).getTicks(), getProgressBar());

                            if (needsCooling() && !hasSufficientCoolant(l, inv, accessPort, timeleft)) {
                                explosionsQueue.add(l);
                                return 0;
                            }
                        }

                        if (space >= produced) {
                            ChargableBlock.addCharge(l, getEnergyProduction());
                            return (double) (charge + getEnergyProduction());
                        } else {
                            return charge;
                        }
                    }
                    else {
                        createByproduct(l, inv, accessPort);
                        return charge;
                    }
                }
                else {
                    burnNextFuel(l, inv, accessPort);
                    return charge;
                }
            }

            @Override
            public boolean explode(Location l) {
                boolean explosion = explosionsQueue.contains(l);

                if (explosion) {
                    Slimefun.runSync(() -> {
                        BlockStorage.getInventory(l).close();
                        SimpleHologram.remove(l.getBlock());
                    });

                    explosionsQueue.remove(l);
                    processing.remove(l);
                    progress.remove(l);
                }

                return explosion;
            }

            private void checkForWaterBlocks(Location l) {
                Slimefun.runSync(() -> {
                    // We will pick a surrounding block at random and see if this is water.
                    // If it isn't, then we will make it explode.
                    BlockFace randomNeighbour = WATER_BLOCKS[ThreadLocalRandom.current().nextInt(WATER_BLOCKS.length)];

                    if (l.getBlock().getRelative(randomNeighbour).getType() != Material.WATER) {
                        explosionsQueue.add(l);
                    }
                });
            }
        };
    }

    private void createByproduct(Location l, BlockMenu inv, BlockMenu accessPort) {
        inv.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));

        if (processing.get(l).getOutput() != null) {
            inv.pushItem(processing.get(l).getOutput(), getOutputSlots());
        }

        if (accessPort != null) {
            for (int slot : getOutputSlots()) {
                if (inv.getItemInSlot(slot) != null) {
                    inv.replaceExistingItem(slot, accessPort.pushItem(inv.getItemInSlot(slot), ReactorAccessPort.getOutputSlots()));
                }
            }
        }

        progress.remove(l);
        processing.remove(l);
    }

    private void burnNextFuel(Location l, BlockMenu inv, BlockMenu accessPort) {
        Map<Integer, Integer> found = new HashMap<>();
        MachineFuel fuel = findFuel(inv, found);

        if (accessPort != null) {
            restockFuel(inv, accessPort);
        }

        if (fuel != null) {
            for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                inv.consumeItem(entry.getKey(), entry.getValue());
            }

            processing.put(l, fuel);
            progress.put(l, fuel.getTicks());
        }
    }

    /**
     * This method cools the given {@link AReactor}.
     *
     * @param reactor    The {@link Location} of this {@link AReactor}
     * @param menu       The {@link Inventory} of this {@link AReactor}
     * @param accessPort The {@link ReactorAccessPort}, if available
     * @param timeleft   The time left
     * @return Whether the {@link AReactor} was successfully cooled, if not it should explode
     */
    private boolean hasSufficientCoolant(Location reactor, BlockMenu menu, BlockMenu accessPort, int timeleft) {
        boolean coolant = (processing.get(reactor).getTicks() - timeleft) % COOLANT_DURATION == 0;

        if (coolant) {
            if (accessPort != null) {
                for (int slot : getCoolantSlots()) {
                    if (SlimefunUtils.isItemSimilar(accessPort.getItemInSlot(slot), getCoolant(), true)) {
                        accessPort.replaceExistingItem(slot, menu.pushItem(accessPort.getItemInSlot(slot), getCoolantSlots()));
                    }
                }
            }

            for (int slot : getCoolantSlots()) {
                if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), getCoolant(), true)) {
                    menu.consumeItem(slot);
                    ReactorHologram.update(reactor, "&b\u2744 &7100%");
                    return true;
                }
            }

            return false;
        } else {
            ReactorHologram.update(reactor, "&b\u2744 &7" + getPercentage(timeleft, processing.get(reactor).getTicks()) + "%");
        }

        return true;
    }

    private float getPercentage(int time, int total) {
        int passed = ((total - time) % COOLANT_DURATION);
        return Math.round(((((COOLANT_DURATION - passed) * 100.0F) / COOLANT_DURATION) * 100.0F) / 100.0F);
    }

    private void restockFuel(BlockMenu menu, BlockMenu port) {
        for (int slot : getFuelSlots()) {
            for (MachineFuel fuelType : fuelTypes) {
                if (fuelType.test(port.getItemInSlot(slot)) && menu.fits(new CustomItem(port.getItemInSlot(slot), 1), getFuelSlots())) {
                    port.replaceExistingItem(slot, menu.pushItem(port.getItemInSlot(slot), getFuelSlots()));
                    return;
                }
            }
        }
    }

    private MachineFuel findFuel(BlockMenu menu, Map<Integer, Integer> found) {
        for (MachineFuel fuel : fuelTypes) {
            for (int slot : getInputSlots()) {
                if (fuel.test(menu.getItemInSlot(slot))) {
                    found.put(slot, fuel.getInput().getAmount());
                    return fuel;
                }
            }
        }

        return null;
    }

    protected BlockMenu getAccessPort(Location l) {
        Location portL = new Location(l.getWorld(), l.getX(), l.getY() + 3, l.getZ());

        if (BlockStorage.check(portL, "REACTOR_ACCESS_PORT")) {
            return BlockStorage.getInventory(portL);
        } else {
            return null;
        }
    }

}
