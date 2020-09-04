package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

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

    private static final String OFFSET_PARAMETER = "offset";

    public HologramProjector(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        addItemHandler(onPlace(), onRightClick(), onBreak());
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b, "text", "Edit me via the Projector");
                BlockStorage.addBlockInfo(b, OFFSET_PARAMETER, "0.5");
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

            if (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString())) {
                openEditor(p, b);
            }
        };
    }

    private static void openEditor(Player p, Block projector) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocalization().getMessage(p, "machines.HOLOGRAM_PROJECTOR.inventory-title"));

        menu.addItem(0, new CustomItem(Material.NAME_TAG, "&7Text &e(Click to edit)", "", "&r" + ChatColors.color(BlockStorage.getLocationInfo(projector.getLocation(), "text"))));
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

        menu.addItem(1, new CustomItem(Material.CLOCK, "&7Offset: &e" + DoubleHandler.fixDouble(Double.valueOf(BlockStorage.getLocationInfo(projector.getLocation(), OFFSET_PARAMETER)) + 1.0D), "", "&rLeft Click: &7+0.1", "&rRight Click: &7-0.1"));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            double offset = DoubleHandler.fixDouble(Double.valueOf(BlockStorage.getLocationInfo(projector.getLocation(), OFFSET_PARAMETER)) + (action.isRightClicked() ? -0.1F : 0.1F));
            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + offset, projector.getZ() + 0.5);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, OFFSET_PARAMETER, String.valueOf(offset));
            openEditor(pl, projector);
            return false;
        });

        menu.open(p);
    }

    private static ArmorStand getArmorStand(Block projector, boolean createIfNoneExists) {
        String nametag = BlockStorage.getLocationInfo(projector.getLocation(), "text");
        double offset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), OFFSET_PARAMETER));
        Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + offset, projector.getZ() + 0.5);

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
