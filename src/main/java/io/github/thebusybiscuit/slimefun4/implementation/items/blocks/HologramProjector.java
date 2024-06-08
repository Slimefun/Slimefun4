package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.services.holograms.HologramsService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.utils.ArmorStandUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * The {@link HologramProjector} is a very simple block which allows the {@link Player}
 * to create a floating text that is completely configurable.
 * 
 * @author TheBusyBiscuit
 * @author Kry-Vosa
 * @author SoSeDiK
 * @author test137e29b
 * 
 * @see HologramOwner
 * @see HologramsService
 *
 */
public class HologramProjector extends SlimefunItem implements HologramOwner {

    private static final String OFFSET_PARAMETER = "offset";
    private static final String OFFSET_PARAMETER_X = "offset-x";
    private static final String OFFSET_PARAMETER_Z = "offset-z";
    private static final double MAX_HORIZONTAL_OFFSET = 32.0D; // Up to 2 chunks away in any direction horizontally
    private static final double DEFAULT_OFFSET_X = 0.5D;
    private static final double DEFAULT_OFFSET_Y = 0.5D;
    private static final double DEFAULT_OFFSET_Z = 0.5D;


    @ParametersAreNonnullByDefault
    public HologramProjector(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);

        addItemHandler(onPlace(), onRightClick(), onBreak());
    }

    private @Nonnull BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b, "text", "Edit me via the Projector");
                BlockStorage.addBlockInfo(b, OFFSET_PARAMETER_X, String.valueOf(DEFAULT_OFFSET_X));
                BlockStorage.addBlockInfo(b, OFFSET_PARAMETER, String.valueOf(DEFAULT_OFFSET_Y));
                BlockStorage.addBlockInfo(b, OFFSET_PARAMETER_Z, String.valueOf(DEFAULT_OFFSET_Z));
                BlockStorage.addBlockInfo(b, "owner", e.getPlayer().getUniqueId().toString());

                getArmorStand(b, true);
            }

        };
    }

    private @Nonnull BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                killArmorStand(b);
            }
        };
    }

    public @Nonnull BlockUseHandler onRightClick() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();
            Block b = e.getClickedBlock().get();

            if (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString())) {
                openEditor(p, b);
            }
        };
    }

    private static double getXOffset(@Nonnull Block projector) {
        String offsetXString = BlockStorage.getLocationInfo(projector.getLocation(), OFFSET_PARAMETER_X);
        if (offsetXString == null) return DEFAULT_OFFSET_X;
        return NumberUtils.reparseDouble(Double.valueOf(offsetXString));
    }

    private static double getYOffset(@Nonnull Block projector) {
        String offsetYString = BlockStorage.getLocationInfo(projector.getLocation(), OFFSET_PARAMETER);
        if (offsetYString == null) return DEFAULT_OFFSET_Y;
        return NumberUtils.reparseDouble(Double.valueOf(offsetYString));
    }

    private static double getZOffset(@Nonnull Block projector) {
        String offsetZString = BlockStorage.getLocationInfo(projector.getLocation(), OFFSET_PARAMETER_Z);
        if (offsetZString == null) return DEFAULT_OFFSET_Z;
        return NumberUtils.reparseDouble(Double.valueOf(offsetZString));
    }

    private void openEditor(@Nonnull Player p, @Nonnull Block projector) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "machines.HOLOGRAM_PROJECTOR.inventory-title"));

        menu.addItem(0, new CustomItemStack(Material.NAME_TAG, "&7Text &e(Click to edit)", "", "&f" + ChatColors.color(BlockStorage.getLocationInfo(projector.getLocation(), "text"))));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            pl.closeInventory();
            Slimefun.getLocalization().sendMessage(pl, "machines.HOLOGRAM_PROJECTOR.enter-text", true);

            ChatUtils.awaitInput(pl, message -> {
                // Fixes #3445 - Make sure the projector is not broken
                if (!BlockStorage.check(projector, getId())) {
                    // Hologram projector no longer exists.
                    // TODO: Add a chat message informing the player that their message was ignored.
                    return;
                }

                ArmorStand hologram = getArmorStand(projector, true);
                hologram.setCustomName(ChatColors.color(message));
                BlockStorage.addBlockInfo(projector, "text", hologram.getCustomName());
                openEditor(pl, projector);
            });

            return false;
        });

        menu.addItem(1, new CustomItemStack(Material.CLOCK, "&7Offset X: &e" + getXOffset(projector), "", "&fLeft Click: &7+0.1", "&fRight Click: &7-0.1"));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            double offsetX = NumberUtils.reparseDouble(getXOffset(projector) + (action.isRightClicked() ? -0.1F : 0.1F));
            double offsetY = getYOffset(projector);
            double offsetZ = getZOffset(projector);

            offsetX = Math.min(Math.max(offsetX, -MAX_HORIZONTAL_OFFSET), MAX_HORIZONTAL_OFFSET);
            
            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + offsetX, projector.getY() + offsetY, projector.getZ() + offsetZ);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, OFFSET_PARAMETER_X, String.valueOf(offsetX));
            openEditor(pl, projector);
            return false;
        });

        menu.addItem(1, new CustomItemStack(Material.CLOCK, "&7Offset Y: &e" + getYOffset(projector) + 1.0D, "", "&fLeft Click: &7+0.1", "&fRight Click: &7-0.1"));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            double offsetX = getXOffset(projector);
            double offsetY = NumberUtils.reparseDouble(getYOffset(projector) + (action.isRightClicked() ? -0.1F : 0.1F));
            double offsetZ = getZOffset(projector);
            
            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + offsetX, projector.getY() + offsetY, projector.getZ() + offsetZ);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, OFFSET_PARAMETER, String.valueOf(offsetY));
            openEditor(pl, projector);
            return false;
        });

        menu.addItem(1, new CustomItemStack(Material.CLOCK, "&7Offset Z: &e" + getZOffset(projector), "", "&fLeft Click: &7+0.1", "&fRight Click: &7-0.1"));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            double offsetX = getXOffset(projector);
            double offsetY = getYOffset(projector);
            double offsetZ = NumberUtils.reparseDouble(getZOffset(projector) + (action.isRightClicked() ? -0.1F : 0.1F));

            offsetZ = Math.min(Math.max(offsetZ, -MAX_HORIZONTAL_OFFSET), MAX_HORIZONTAL_OFFSET);

            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + offsetX, projector.getY() + offsetY, projector.getZ() + offsetZ);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, OFFSET_PARAMETER_Z, String.valueOf(offsetZ));
            openEditor(pl, projector);
            return false;
        });

        menu.open(p);
    }

    private static ArmorStand getArmorStand(@Nonnull Block projector, boolean createIfNoneExists) {
        String nametag = BlockStorage.getLocationInfo(projector.getLocation(), "text");
        double offsetX = getXOffset(projector);
        double offsetY = getYOffset(projector);
        double offsetZ = getZOffset(projector);
        Location l = new Location(projector.getWorld(), projector.getX() + offsetX, projector.getY() + offsetY, projector.getZ() + offsetZ);

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand armorStand && l.distanceSquared(n.getLocation()) < 0.4) {
                String customName = n.getCustomName();

                if (customName != null && customName.equals(nametag)) {
                    return armorStand;
                }
            }
        }

        if (!createIfNoneExists) {
            return null;
        }
        
        return ArmorStandUtils.spawnArmorStand(l, nametag);
    }

    private static void killArmorStand(@Nonnull Block b) {
        ArmorStand hologram = getArmorStand(b, false);

        if (hologram != null) {
            hologram.remove();
        }
    }
}
