package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * The {@link WitherAssembler} is an electrical machine that can automatically spawn
 * a {@link Wither} if the required ingredients have been provided.
 * 
 * @author TheBusyBiscuit
 *
 */
public class WitherAssembler extends SimpleSlimefunItem<BlockTicker> implements EnergyNetComponent {

    private static final int ENERGY_CONSUMPTION = 4096;

    private final int[] border = { 0, 2, 3, 4, 5, 6, 8, 12, 14, 21, 23, 30, 32, 39, 40, 41 };
    private final int[] skullBorder = { 9, 10, 11, 18, 20, 27, 29, 36, 37, 38 };
    private final int[] sandBorder = { 15, 16, 17, 24, 26, 33, 35, 42, 43, 44 };

    private int lifetime = 0;

    public WitherAssembler(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        new BlockMenuPreset(getID(), "&5Wither Assembler") {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(BlockMenu menu, Block b) {
                if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "enabled") == null || BlockStorage.getLocationInfo(b.getLocation(), "enabled").equals(String.valueOf(false))) {
                    menu.replaceExistingItem(22, new CustomItem(Material.GUNPOWDER, "&7Enabled: &4\u2718", "", "&e> Click to enable this Machine"));
                    menu.addMenuClickHandler(22, (p, slot, item, action) -> {
                        BlockStorage.addBlockInfo(b, "enabled", String.valueOf(true));
                        newInstance(menu, b);
                        return false;
                    });
                }
                else {
                    menu.replaceExistingItem(22, new CustomItem(Material.REDSTONE, "&7Enabled: &2\u2714", "", "&e> Click to disable this Machine"));
                    menu.addMenuClickHandler(22, (p, slot, item, action) -> {
                        BlockStorage.addBlockInfo(b, "enabled", String.valueOf(false));
                        newInstance(menu, b);
                        return false;
                    });
                }

                double offset = (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "offset") == null) ? 3.0F : Double.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "offset"));

                menu.replaceExistingItem(31, new CustomItem(Material.PISTON, "&7Offset: &3" + offset + " Block(s)", "", "&rLeft Click: &7+0.1", "&rRight Click: &7-0.1"));
                menu.addMenuClickHandler(31, (p, slot, item, action) -> {
                    double offsetv = DoubleHandler.fixDouble(Double.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "offset")) + (action.isRightClicked() ? -0.1F : 0.1F));
                    BlockStorage.addBlockInfo(b, "offset", String.valueOf(offsetv));
                    newInstance(menu, b);
                    return false;
                });
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.INSERT) {
                    return getInputSlots();
                }
                else {
                    return new int[0];
                }
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                if (flow == ItemTransportFlow.INSERT && item != null) {
                    if (item.getType() == Material.SOUL_SAND) {
                        return getSoulSandSlots();
                    }

                    if (item.getType() == Material.WITHER_SKELETON_SKULL) {
                        return getWitherSkullSlots();
                    }
                }

                return new int[0];
            }
        };

        registerBlockHandler(getID(), new SlimefunBlockHandler() {

            @Override
            public void onPlace(Player p, Block b, SlimefunItem item) {
                BlockStorage.addBlockInfo(b, "offset", "3.0");
                BlockStorage.addBlockInfo(b, "enabled", String.valueOf(false));
            }

            @Override
            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
                if (reason == UnregisterReason.EXPLODE) {
                    return false;
                }

                BlockMenu inv = BlockStorage.getInventory(b);

                if (inv != null) {
                    for (int slot : getSoulSandSlots()) {
                        if (inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                            inv.replaceExistingItem(slot, null);
                        }
                    }

                    for (int slot : getWitherSkullSlots()) {
                        if (inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                            inv.replaceExistingItem(slot, null);
                        }
                    }
                }

                return true;
            }
        });
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : skullBorder) {
            preset.addItem(i, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : sandBorder) {
            preset.addItem(i, new CustomItem(Material.BROWN_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(1, new CustomItem(Material.WITHER_SKELETON_SKULL, "&7Wither Skull Slot", "", "&rThis Slot accepts Wither Skeleton Skulls"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(7, new CustomItem(Material.SOUL_SAND, "&7Soul Sand Slot", "", "&rThis Slot accepts Soul Sand"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(13, new CustomItem(Material.CLOCK, "&7Cooldown: &b30 Seconds", "", "&rThis Machine takes up to half a Minute to operate", "&rso give it some Time!"), ChestMenuUtils.getEmptyClickHandler());
    }

    public int[] getInputSlots() {
        return new int[] { 19, 28, 25, 34 };
    }

    public int[] getWitherSkullSlots() {
        return new int[] { 19, 28 };
    }

    public int[] getSoulSandSlots() {
        return new int[] { 25, 34 };
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return 4096;
    }

    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                if (String.valueOf(false).equals(BlockStorage.getLocationInfo(b.getLocation(), "enabled"))) {
                    return;
                }

                if (lifetime % 60 == 0 && ChargableBlock.getCharge(b) >= ENERGY_CONSUMPTION) {
                    BlockMenu menu = BlockStorage.getInventory(b);

                    boolean soulsand = findResource(menu, Material.SOUL_SAND, 4, getSoulSandSlots());
                    boolean skulls = findResource(menu, Material.WITHER_SKELETON_SKULL, 3, getWitherSkullSlots());

                    if (soulsand && skulls) {
                        consumeResources(menu);

                        ChargableBlock.addCharge(b, -ENERGY_CONSUMPTION);
                        double offset = Double.parseDouble(BlockStorage.getLocationInfo(b.getLocation(), "offset"));

                        Slimefun.runSync(() -> b.getWorld().spawnEntity(new Location(b.getWorld(), b.getX() + 0.5D, b.getY() + offset, b.getZ() + 0.5D), EntityType.WITHER));
                    }
                }
            }

            @Override
            public void uniqueTick() {
                lifetime++;
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }
        };
    }

    private boolean findResource(BlockMenu menu, Material resource, int required, int[] slots) {
        int found = 0;

        for (int slot : slots) {
            if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), new ItemStack(resource), true, false)) {
                found += menu.getItemInSlot(slot).getAmount();

                if (found > required) {
                    return true;
                }
            }
        }

        return false;
    }

    private void consumeResources(BlockMenu inv) {
        int soulsand = 4;
        int skulls = 3;

        for (int slot : getSoulSandSlots()) {
            if (SlimefunUtils.isItemSimilar(inv.getItemInSlot(slot), new ItemStack(Material.SOUL_SAND), true, false)) {
                int amount = inv.getItemInSlot(slot).getAmount();

                if (amount >= soulsand) {
                    inv.consumeItem(slot, soulsand);
                    break;
                }
                else {
                    soulsand -= amount;
                    inv.replaceExistingItem(slot, null);
                }
            }
        }

        for (int slot : getWitherSkullSlots()) {
            if (SlimefunUtils.isItemSimilar(inv.getItemInSlot(slot), new ItemStack(Material.WITHER_SKELETON_SKULL), true, false)) {
                int amount = inv.getItemInSlot(slot).getAmount();

                if (amount >= skulls) {
                    inv.consumeItem(slot, skulls);
                    break;
                }
                else {
                    skulls -= amount;
                    inv.replaceExistingItem(slot, null);
                }
            }
        }
    }

}
