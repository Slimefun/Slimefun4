package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
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

    private static final String Y_OFFSET_PARAMETER = "y_offset";
    private static final String X_OFFSET_PARAMETER = "x_offset";
    private static final String Z_OFFSET_PARAMETER = "z_offset";
    private static final int[] BORDER = {1, 3, 5, 7, 8, 10, 12, 14, 16, 17};

    public HologramProjector(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        addItemHandler(onPlace(), onRightClick(), onBreak());
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b, "text", ChatColors.color("&fEdit me via the Projector"));
                BlockStorage.addBlockInfo(b, Y_OFFSET_PARAMETER, "0.5");
                BlockStorage.addBlockInfo(b, X_OFFSET_PARAMETER, "0.5");
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
                BlockStorage.addBlockInfo(b, Y_OFFSET_PARAMETER, BlockStorage.getLocationInfo(b.getLocation(), "offset"));
                BlockStorage.addBlockInfo(b, X_OFFSET_PARAMETER, "0.5");
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
            menu.addItem(s, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " ", " "));
            menu.addMenuClickHandler(s, (player, slot, item, action) -> false);
        }

        menu.addItem(0, new CustomItem(Material.NAME_TAG, "&7Text &e(Click to edit)", "", ChatColors.color(BlockStorage.getLocationInfo(projector.getLocation(), "text"))));
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
            Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + 0.5, projector.getZ() + 0.5);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, Y_OFFSET_PARAMETER, String.valueOf(0.5));
            BlockStorage.addBlockInfo(projector, X_OFFSET_PARAMETER, String.valueOf(0.5));
            BlockStorage.addBlockInfo(projector, Z_OFFSET_PARAMETER, String.valueOf(0.5));
            openEditor(pl, projector);
            return false;
        });

        // Y Axis
        menu.addItem(2, new CustomItem(SkullItem.fromHash("b221da4418bd3bfb42eb64d2ab429c61decb8f4bf7d4cfb77a162be3dcb0b927"),
            "&7Vertical Offset (+): &e" + DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER))),
            "", "&eLeft Click: &7+0.1", "&eRight Click: &7+0.5"));
        menu.addMenuClickHandler(2, (pl, slot, item, action) -> {
            double yOffset = DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER)) + (action.isRightClicked() ? 0.5F : 0.1F));
            double xOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER));
            double zOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Z_OFFSET_PARAMETER));
            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + xOffset, projector.getY() + yOffset, projector.getZ() + zOffset);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, Y_OFFSET_PARAMETER, String.valueOf(yOffset));
            openEditor(pl, projector);
            return false;
        });

        menu.addItem(11, new CustomItem(SkullItem.fromHash("3b83bbccf4f0c86b12f6f79989d159454bf9281955d7e2411ce98c1b8aa38d8"),
            "&7Vertical Offset (-): &e" + DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER))),
            "", "&eLeft Click: &7+0.1", "&eRight Click: &7+0.5"));
        menu.addMenuClickHandler(11, (pl, slot, item, action) -> {
            double yOffset = DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER)) + (action.isRightClicked() ? -0.5F : -0.1F));
            double xOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER));
            double zOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Z_OFFSET_PARAMETER));
            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + xOffset, projector.getY() + yOffset, projector.getZ() + zOffset);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, Y_OFFSET_PARAMETER, String.valueOf(yOffset));
            openEditor(pl, projector);
            return false;
        });


        // X Axis
        menu.addItem(4, new CustomItem(SkullItem.fromHash("f4628ace7c3afc61a476dc144893aaa642ba976d952b51ece26abafb896b8"),
            "&7X Offset (+): &e" + DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER))),
            "", "&eLeft Click: &7+0.1", "&eRight Click: &7+0.5"));
        menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
            double yOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER));
            double xOffset = DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER)) + (action.isRightClicked() ? 0.5F : 0.1F));
            double zOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Z_OFFSET_PARAMETER));
            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + xOffset, projector.getY() + yOffset, projector.getZ() + zOffset);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, X_OFFSET_PARAMETER, String.valueOf(xOffset));
            openEditor(pl, projector);
            return false;
        });

        menu.addItem(13, new CustomItem(SkullItem.fromHash("2ae425c5ba9f3c2962b38178cbc23172a6c6215a11accb92774a4716e96cada"),
            "&7X Offset (-): &e" + DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER))),
            "", "&eLeft Click: &7-0.1", "&eRight Click: &7-0.5"));
        menu.addMenuClickHandler(13, (pl, slot, item, action) -> {
            double yOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER));
            double xOffset = DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER)) + (action.isRightClicked() ? -0.5F : -0.1F));
            double zOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Z_OFFSET_PARAMETER));
            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + xOffset, projector.getY() + yOffset, projector.getZ() + zOffset);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, X_OFFSET_PARAMETER, String.valueOf(xOffset));
            openEditor(pl, projector);
            return false;
        });


        // Z Axis
        menu.addItem(6, new CustomItem(SkullItem.fromHash("2d9287616343d833e9e7317159caa2cb3e59745113962c1379052ce478884fa"),
            "&7Z Offset (+): &e" + DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Z_OFFSET_PARAMETER))),
            "", "&eLeft Click: &7+0.1", "&eRight Click: &7+0.5"));
        menu.addMenuClickHandler(6, (pl, slot, item, action) -> {
            double yOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER));
            double xOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER));
            double zOffset = DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Z_OFFSET_PARAMETER)) + (action.isRightClicked() ? 0.5F : 0.1F));
            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + xOffset, projector.getY() + yOffset, projector.getZ() + zOffset);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, Z_OFFSET_PARAMETER, String.valueOf(zOffset));
            openEditor(pl, projector);
            return false;
        });

        menu.addItem(15, new CustomItem(SkullItem.fromHash("a3852bf616f31ed67c37de4b0baa2c5f8d8fca82e72dbcafcba66956a81c4"),
            "&7Z Offset (-): &e" + DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Z_OFFSET_PARAMETER))),
            "", "&eLeft Click: &7-0.1", "&eRight Click: &7-0.5"));
        menu.addMenuClickHandler(15, (pl, slot, item, action) -> {
            double yOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER));
            double xOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER));
            double zOffset = DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Z_OFFSET_PARAMETER)) + (action.isRightClicked() ? -0.5F : -0.1F));
            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + xOffset, projector.getY() + yOffset, projector.getZ() + zOffset);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, Z_OFFSET_PARAMETER, String.valueOf(zOffset));
            openEditor(pl, projector);
            return false;
        });

        menu.open(p);
    }

    private static ArmorStand getArmorStand(Block projector, boolean createIfNoneExists) {
        String nametag = BlockStorage.getLocationInfo(projector.getLocation(), "text");
        double yOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), Y_OFFSET_PARAMETER));
        double xOffset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), X_OFFSET_PARAMETER));
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
}
