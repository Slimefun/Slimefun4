package io.github.thebusybiscuit.slimefun4.implementation.items.teleporter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
 * to which only the {@link Player} who are whitelisted have access.
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
            String user = "" + p.getUniqueId();
            Optional<Block> block = e.getClickedBlock();

            if(block.isPresent()) {
                if (BlockStorage.getLocationInfo(block.get().getLocation(), "owner").equals(user)) {
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
                Player p = e.getPlayer();
                String owner = "" + p.getUniqueId();
                BlockStorage.addBlockInfo(e.getBlock(), "owner", owner);
                Bukkit.getScheduler().runTaskAsynchronously(Slimefun.instance(), () -> {
                    Slimefun.getGPSNetwork().addWhitelist(Bukkit.getPlayer(p.getUniqueId()), Bukkit.getOfflinePlayer(p.getUniqueId()));
                });
            }
        };
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hasAccess(Player p, Block b) {
        AtomicBoolean bool = new AtomicBoolean(false);
        OfflinePlayer owner = (Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"))));
        PlayerProfile.get(owner, profile -> {
            for (Whitelist wl : profile.getWhitelists()) {
                if (wl.getId().equals(p.getUniqueId())) {
                    bool.set(true);
                }
            }
        });
        return bool.get();
    }
}