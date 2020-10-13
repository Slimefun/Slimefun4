package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import javax.annotation.Nonnull;

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
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.DoubleRangeSetting;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class HologramProjector extends SlimefunItem {

    private final String DEFAULT_OFFSET = "0.5";

    private static final String X_OFFSET_PARAMETER = "x_offset";
    private static final String Y_OFFSET_PARAMETER = "y_offset";
    private static final String Z_OFFSET_PARAMETER = "z_offset";
    private static final int[] BORDER = {1, 3, 5, 7, 8, 10, 12, 14, 16, 17};

    private final ItemSetting<Double> maxDistance = new DoubleRangeSetting("max-horizontal-offset", 0.0, 5.0, 10.0);

    public HologramProjector(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                             ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        addItemHandler(onPlace(), onRightClick(), onBreak());
        addItemSetting(maxDistance);
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b, "text", "Edit me via the Projector");
                BlockStorage.addBlockInfo(b, X_OFFSET_PARAMETER, DEFAULT_OFFSET);
                BlockStorage.addBlockInfo(b, Y_OFFSET_PARAMETER, DEFAULT_OFFSET);
                BlockStorage.addBlockInfo(b, Z_OFFSET_PARAMETER, DEFAULT_OFFSET);
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
                BlockStorage.addBlockInfo(b, X_OFFSET_PARAMETER, DEFAULT_OFFSET);
                BlockStorage.addBlockInfo(b, Y_OFFSET_PARAMETER, BlockStorage.getLocationInfo(b.getLocation(), "offset")
                );
                BlockStorage.addBlockInfo(b, Z_OFFSET_PARAMETER, DEFAULT_OFFSET);
            }

            if (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString())) {
                openEditor(p, b);
            } else {
                SlimefunPlugin.getLocalization().sendMessage(p, "no-permission", true);
            }
        };
    }

    private void openEditor(Player p, @Nonnull Block projector) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocalization().getMessage(p, "machines.HOLOGRAM_PROJECTOR" +
                ".inventory-title"));

        for (int s : BORDER) {
            menu.addItem(s, new CustomItem(ChestMenuUtils.getBackground(), " "));
            menu.addMenuClickHandler(s, ChestMenuUtils.getEmptyClickHandler());
        }

        String text = ChatColor.WHITE + ChatColors.color(BlockStorage.getLocationInfo(projector.getLocation(), "text"));
        menu.addItem(0, new CustomItem(Material.NAME_TAG, "&7Text &e(Click to edit)", "", text));
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
            hologram.teleport(projector.getLocation().add(0.5, 0.5, 0.5));

            BlockStorage.addBlockInfo(projector, X_OFFSET_PARAMETER, DEFAULT_OFFSET);
            BlockStorage.addBlockInfo(projector, Y_OFFSET_PARAMETER, DEFAULT_OFFSET);
            BlockStorage.addBlockInfo(projector, Z_OFFSET_PARAMETER, DEFAULT_OFFSET);
            openEditor(pl, projector);
            return false;
        });

        createSelector(Type.X_POSITIVE, projector, menu);
        createSelector(Type.X_NEGATIVE, projector, menu);
        createSelector(Type.Y_POSITIVE, projector, menu);
        createSelector(Type.Y_NEGATIVE, projector, menu);
        createSelector(Type.Z_POSITIVE, projector, menu);
        createSelector(Type.Z_NEGATIVE, projector, menu);

        menu.open(p);
    }

    private static ArmorStand getArmorStand(Block projector, boolean createIfNoneExists) {
        String nametag = BlockStorage.getLocationInfo(projector.getLocation(), "text");
        double xOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER));
        double yOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER));
        double zOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Z_OFFSET_PARAMETER));
        Location l = projector.getLocation().add(xOffset, yOffset, zOffset);

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

    private void createSelector(Type type, @Nonnull Block projector, @Nonnull ChestMenu menu) {

        double currentOffset = DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(
                projector.getLocation(), type.parameter)));

        menu.addItem(type.slot, new CustomItem(type.button.getAsItemStack(), "&7" + type.coordinate + " Offset (" + type.direction + ") &e" + currentOffset, "", "&eLeft Click: &7" + type.direction + Math.abs(type.smallChange), "&eRight Click: &7" + type.direction + Math.abs(type.smallChange)));
        menu.addMenuClickHandler(type.slot, (pl, slot, item, action) -> {
            double storedOffset = DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), type.parameter)));
            double offset = action.isRightClicked() ? type.largeChange : type.smallChange;

            if ((type.coordinate == 'X' || type.coordinate == 'Z') && offset + storedOffset >
                    maxDistance.getValue()) {
                offset = maxDistance.getValue() - storedOffset;
            }

            ArmorStand hologram = getArmorStand(projector, true);

            if (type.coordinate == 'X') {
                hologram.teleport(hologram.getLocation().add(offset, 0, 0));
            } else if (type.coordinate == 'Y') {
                hologram.teleport(hologram.getLocation().add(0, offset, 0));
            } else {
                hologram.teleport(hologram.getLocation().add(0, 0, offset));
            }

            BlockStorage.addBlockInfo(projector, type.parameter, String.valueOf(storedOffset + offset));
            openEditor(pl, projector);
            return false;
        });
    }

    private enum Type {
        X_POSITIVE(2, HeadTexture.RED_UP_ARROW, 'X', '+', X_OFFSET_PARAMETER, 0.1F, 0.5F),
        X_NEGATIVE(11, HeadTexture.RED_DOWN_ARROW, 'X', '-', X_OFFSET_PARAMETER, -0.1F, -0.5F),
        Y_POSITIVE(4, HeadTexture.LIME_UP_ARROW, 'Y', '+', Y_OFFSET_PARAMETER, 0.1F, 0.5F),
        Y_NEGATIVE(13, HeadTexture.LIME_DOWN_ARROW, 'Y', '-', Y_OFFSET_PARAMETER, -0.1F, -0.5F),
        Z_POSITIVE(6, HeadTexture.BLUE_UP_ARROW, 'Z', '+', Z_OFFSET_PARAMETER, 0.1F, 0.5F),
        Z_NEGATIVE(15, HeadTexture.BLUE_DOWN_ARROW, 'Z', '-', Z_OFFSET_PARAMETER, -0.1F, -0.5F);

        int slot;
        HeadTexture button;
        char coordinate;
        char direction;
        String parameter;
        float smallChange;
        float largeChange;

        Type(int slot, HeadTexture button,
             char coordinate, char direction, String parameter, float smallChange, float largeChange) {
            this.slot = slot;
            this.button = button;
            this.coordinate = coordinate;
            this.direction = direction;
            this.parameter = parameter;
            this.smallChange = smallChange;
            this.largeChange = largeChange;
        }
    }
}
