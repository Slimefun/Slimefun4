package io.github.thebusybiscuit.slimefun4.implementation.items.teleporter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * The {@link GroupActivationPlate} is a teleporter activation plate
 * to which only the {@link Player} who placed it down has access.
 *
 * @author Toastery
 *
 * @see SharedActivationPlate
 * @see PersonalActivationPlate
 *
 */
public class GroupActivationPlate extends AbstractTeleporterPlate {

    @ParametersAreNonnullByDefault
    public GroupActivationPlate(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemHandler(onPlace());
        addItemHandler(onUse());

    }
    @Nonnull
    private BlockUseHandler onUse() {
        return e -> {
            e.cancel();
            Player p = e.getPlayer();
            Optional<Block> block = e.getClickedBlock();

            if(block.isPresent()) {
                if (hasAccess(p, block.get())) {
                } else {
                    Slimefun.getLocalization().sendMessage(p, "inventory.no-access", true);
                }
            }
        };
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlock(), "owner", e.getPlayer().getUniqueId().toString());
                BlockStorage.addBlockInfo(e.getBlock(), "user", e.getPlayer().getUniqueId().toString());
            }
        };
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hasAccess(Player p, Block b) {
        return BlockStorage.getLocationInfo(b.getLocation(), "user").equals(p.getUniqueId().toString());
    }
}