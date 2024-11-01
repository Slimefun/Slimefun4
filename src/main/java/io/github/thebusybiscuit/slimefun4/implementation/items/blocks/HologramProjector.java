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
import org.bukkit.util.Vector;

/**
 * The {@link HologramProjector} is a very simple block which allows the {@link Player}
 * to create a floating text that is completely configurable.
 * 
 * @author TheBusyBiscuit
 * @author Kry-Vosa
 * @author SoSeDiK
 * 
 * @see HologramOwner
 * @see HologramsService
 *
 */
public class HologramProjector extends SlimefunItem implements HologramOwner {

    private static final String OFFSET_PARAMETER = "offset";

    @ParametersAreNonnullByDefault
    public HologramProjector(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);

        addItemHandler(onPlace(), onRightClick(), onBreak());
    }

    private @Nonnull BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b, "text", "Edit me via the Projector");
                BlockStorage.addBlockInfo(b, OFFSET_PARAMETER, "0.5");
                BlockStorage.addBlockInfo(b, "owner", e.getPlayer().getUniqueId().toString());
                updateHologram(b, "Edit me via the Projector");
            }

        };
    }

    private @Nonnull BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                removeHologram(b);
            }
        };
    }

    private @Nonnull BlockUseHandler onRightClick() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();
            Block b = e.getClickedBlock().get();

            if (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString())) {
                openEditor(p, b);
            }
        };
    }

    public double getOffset(@Nonnull Block projector) {
        return NumberUtils.reparseDouble(Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), OFFSET_PARAMETER)));
    }

    public String getText(@Nonnull Block projector) {
        return BlockStorage.getLocationInfo(projector.getLocation(), "text");
    }

    @Override
    public @Nonnull Vector getHologramOffset(@Nonnull Block projector) {
        return new Vector(0.5, getOffset(projector), 0.5);
    }

    private void openEditor(@Nonnull Player p, @Nonnull Block projector) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "machines.HOLOGRAM_PROJECTOR.inventory-title"));

        menu.addItem(0, new CustomItemStack(Material.NAME_TAG, "&7Text &e(Click to edit)", "", "&f" + getText(projector)));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            pl.closeInventory();
            Slimefun.getLocalization().sendMessage(pl, "machines.HOLOGRAM_PROJECTOR.enter-text", true);

            ChatUtils.awaitInput(pl, message -> {
                // Fixes #3445 - Make sure the projector is not broken
                if (!BlockStorage.check(projector, getId())) {
                    // Hologram projector no longer exists.
                    Slimefun.getLocalization().sendMessage(pl, "machines.HOLOGRAM_PROJECTOR.broken", true);
                    return;
                }

                message = ChatColors.color(message);
                updateHologram(projector, message);
                BlockStorage.addBlockInfo(projector, "text", message);
                openEditor(pl, projector);
            });

            return false;
        });

        menu.addItem(1, new CustomItemStack(Material.CLOCK, "&7Offset: &e" + NumberUtils.roundDecimalNumber(getOffset(projector) + 1.0D), "", "&fLeft Click: &7+0.1", "&fRight Click: &7-0.1"));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            double offset = getOffset(projector) + (action.isRightClicked() ? -0.1F : 0.1F);
            ArmorStand hologram = getArmorStand(projector, true);
            Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + offset, projector.getZ() + 0.5);
            hologram.teleport(l);

            BlockStorage.addBlockInfo(projector, OFFSET_PARAMETER, String.valueOf(offset));
            openEditor(pl, projector);
            return false;
        });

        menu.open(p);
    }

    private static ArmorStand getArmorStand(@Nonnull Block projector, boolean createIfNoneExists) {
        String nametag = BlockStorage.getLocationInfo(projector.getLocation(), "text");
        double offset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), OFFSET_PARAMETER));
        Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + offset, projector.getZ() + 0.5);

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
