package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class HologramProjector extends SlimefunItem {

    private static final String X_OFFSET_PARAMETER = "x_offset";
    private static final String Y_OFFSET_PARAMETER = "y_offset";
    private static final String Z_OFFSET_PARAMETER = "z_offset";
    private static final int[] BORDER = {1, 3, 5, 7, 8, 10, 12, 14, 16, 17};

    private static final ItemSetting<Double> maxDistance = new ItemSetting<>("max-horizontal-offset", 5.0);

    public HologramProjector(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        addItemHandler(onPlace(), onRightClick(), onBreak());
        addItemSetting(maxDistance);
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b, "text", ChatColors.color("Edit me via the Projector"));
                BlockStorage.addBlockInfo(b, X_OFFSET_PARAMETER, "0.5");
                BlockStorage.addBlockInfo(b, Y_OFFSET_PARAMETER, "0.5");
                BlockStorage.addBlockInfo(b, Z_OFFSET_PARAMETER, "0.5");
                BlockStorage.addBlockInfo(b, "owner", e.getPlayer().getUniqueId().toString());

                getArmorStand(b, true);
            }

        };
    }

    private BlockBreakHandler onBreak() {
        return (e, item, fortune, drops) -> {
            remove(e.getBlock());
            return true;
        };
    }

    public BlockUseHandler onRightClick() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();
            Block b = e.getClickedBlock().get();

            // Updates old holograms to use new coordinate system
            if (BlockStorage.getLocationInfo(b.getLocation(), Y_OFFSET_PARAMETER) == null) {
                BlockStorage.addBlockInfo(b, X_OFFSET_PARAMETER, "0.5");
                BlockStorage.addBlockInfo(b, Y_OFFSET_PARAMETER, BlockStorage.getLocationInfo(b.getLocation(), "offset"));
                BlockStorage.addBlockInfo(b, Z_OFFSET_PARAMETER, "0.5");
            }

            if (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString())) {
                openEditor(p, b);
            } else {
                SlimefunPlugin.getLocalization().sendMessage(p, "no-permission", true);
            }
        };
    }

    private static void openEditor(Player p, Block projector) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocalization().getMessage(p, "machines.HOLOGRAM_PROJECTOR.inventory-title"));

        for (int s : BORDER) {
            menu.addItem(s, new CustomItem(ChestMenuUtils.getBackground(), " "));
            menu.addMenuClickHandler(s, ChestMenuUtils.getEmptyClickHandler());
        }

        menu.addItem(0, new CustomItem(Material.NAME_TAG, "&7Text &e(Click to edit)", "", ChatColor.WHITE + ChatColors.color(BlockStorage.getLocationInfo(projector.getLocation(), "text"))));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            pl.closeInventory();
            SlimefunPlugin.getLocalization().sendMessage(pl, "machines.HOLOGRAM_PROJECTOR.enter-text", true);

            ChatUtils.awaitInput(pl, message -> {
                ArmorStand hologram = getArmorStand(projector, true);
                hologram.setCustomName(ChatColors.color(message));
                BlockStorage.addBlockInfo(projector, "text", hologram.getCustomName());
                openEditor(pl, projector);
            });

            return false;
        });

        menu.addItem(9, new CustomItem(Material.CLOCK, "&cReset Position", "", "&eClick &7to reset hologram location"));
        menu.addMenuClickHandler(9, (pl, slot, item, action) -> {
            ArmorStand hologram = getArmorStand(projector, true);
            hologram.teleport(new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + 0.5, projector.getZ() + 0.5));

            BlockStorage.addBlockInfo(projector, X_OFFSET_PARAMETER, String.valueOf(0.5));
            BlockStorage.addBlockInfo(projector, Y_OFFSET_PARAMETER, String.valueOf(0.5));
            BlockStorage.addBlockInfo(projector, Z_OFFSET_PARAMETER, String.valueOf(0.5));
            openEditor(pl, projector);
            return false;
        });

        createSelector(2, 'X', '+', X_OFFSET_PARAMETER, projector, menu);
        createSelector(11, 'X', '-', X_OFFSET_PARAMETER, projector, menu);

        createSelector(4, 'Y', '+', Y_OFFSET_PARAMETER, projector, menu);
        createSelector(13, 'Y', '-', Y_OFFSET_PARAMETER, projector, menu);

        createSelector(6, 'Z', '+', Z_OFFSET_PARAMETER, projector, menu);
        createSelector(15, 'Z', '-', Z_OFFSET_PARAMETER, projector, menu);

        menu.open(p);
    }

    private static ArmorStand getArmorStand(Block projector, boolean createIfNoneExists) {
        String nametag = BlockStorage.getLocationInfo(projector.getLocation(), "text");
        double xOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER));
        double yOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER));
        double zOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Z_OFFSET_PARAMETER));
        Location l = new Location(projector.getWorld(), projector.getX() + xOffset, projector.getY() + yOffset, projector.getZ() + zOffset);

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand && l.distanceSquared(n.getLocation()) < 0.4) {
                String customName = n.getCustomName();

                if (customName != null && customName.equals(nametag)) {
                    return (ArmorStand) n;
                }
            }
        }

        if (!createIfNoneExists) {
            return null;
        }

        ArmorStand hologram = SimpleHologram.create(l);
        hologram.setCustomName(nametag);
        return hologram;
    }

    private static void remove(Block b) {
        ArmorStand hologram = getArmorStand(b, false);

        if (hologram != null) {
            hologram.remove();
        }
    }

    private static void createSelector(int menuSlot, char coordinate, char direction, String parameter, Block projector, ChestMenu menu) {
        HeadTexture head = null;
        if (direction == '+') {
            if (coordinate == 'X') {
                head = HeadTexture.RED_UP_ARROW;
            } else if (coordinate == 'Y') {
                head = HeadTexture.LIME_UP_ARROW;
            } else {
                head = HeadTexture.BLUE_UP_ARROW;
            }
        } else if (direction == '-') {
            if (coordinate == 'X') {
                head = HeadTexture.RED_DOWN_ARROW;
            } else if (coordinate == 'Y') {
                head = HeadTexture.LIME_DOWN_ARROW;
            } else {
                head = HeadTexture.BLUE_DOWN_ARROW;
            }
        }

        menu.addItem(menuSlot, new CustomItem(head.getAsItemStack(),
            "&7" + coordinate + " Offset (" + direction + ") &e" + DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), parameter))),
            "", "&eLeft Click: &7+0.1", "&eRight Click: &7+0.5"));
        menu.addMenuClickHandler(menuSlot, (pl, slot, item, action) -> {
            double storedOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), parameter));
            double offset;

            if (direction == '+') {
                offset = action.isRightClicked() ? 0.5F : 0.1F;
            } else {
                offset = action.isRightClicked() ? -0.5F : -0.1F;
            }

            if ((coordinate == 'X' || coordinate == 'Z')
                && offset + storedOffset > maxDistance.getValue()) {
                return false;
            }
            ArmorStand hologram = getArmorStand(projector, true);

            if (coordinate == 'X') {
                moveHologram(hologram, offset, 0, 0);
            } else if (coordinate == 'Y') {
                moveHologram(hologram, 0, offset, 0);
            } else if (coordinate == 'Z') {
                moveHologram(hologram, 0, 0, offset);
            }

            BlockStorage.addBlockInfo(projector, parameter,
                String.valueOf(storedOffset + offset));
            openEditor(pl, projector);
            return false;
        });
    }

    private static void moveHologram(ArmorStand hologram, double x, double y, double z) {
        hologram.teleport(hologram.getLocation().add(x, y, z));
    }
}
