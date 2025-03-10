package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Block;
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
            public void onPlayerPlace(@Nonnull BlockPlaceEvent event) {
                Block block = event.getBlockPlaced();
                BlockStorage.addBlockInfo(block, "text", "Edit me via the Projector");
                BlockStorage.addBlockInfo(block, OFFSET_PARAMETER, "0.5");
                BlockStorage.addBlockInfo(block, "owner", event.getPlayer().getUniqueId().toString());
                updateHologram(block, "Edit me via the Projector");
            }
        };
    }

    private @Nonnull BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {
            @Override
            public void onBlockBreak(@Nonnull Block block) {
                removeHologram(block);
            }
        };
    }

    private @Nonnull BlockUseHandler onRightClick() {
        return event -> {
            event.cancel();

            Player player = event.getPlayer();
            Block block = event.getClickedBlock().get();
            if (BlockStorage.getLocationInfo(block.getLocation(), "owner").equals(player.getUniqueId().toString())) {
                openEditor(player, block);
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
            setOffset(projector, new Vector(0.5, offset, 0.5));
            openEditor(pl, projector);
            BlockStorage.addBlockInfo(projector, OFFSET_PARAMETER, String.valueOf(offset));
            return false;
        });

        menu.open(p);
    }

}
