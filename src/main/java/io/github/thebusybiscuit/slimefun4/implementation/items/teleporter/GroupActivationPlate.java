package io.github.thebusybiscuit.slimefun4.implementation.items.teleporter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.thebusybiscuit.slimefun4.api.gps.Whitelist;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
 * @author Toast732
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
                if (BlockStorage.getLocationInfo(block.get().getLocation(), "owner").equals(p.getUniqueId().toString())) {
                    Slimefun.getGPSNetwork().createWhitelist(e.getPlayer());
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
                Slimefun.getGPSNetwork().addWhitelist(Bukkit.getPlayer(e.getPlayer().getUniqueId()), Bukkit.getPlayer(e.getPlayer().getUniqueId()));
            }
        };
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hasAccess(Player p, Block b) {
        OfflinePlayer owner = Bukkit.getPlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")));
        AtomicBoolean bool = new AtomicBoolean(false);
        PlayerProfile.get(owner, profile -> {
            for (Whitelist wl : profile.getWhitelists()) {
                if (wl.getId().equals(p.getUniqueId().toString())) {
                    bool.set(true);
                }
            }
        });
        return bool.get();
    }


}