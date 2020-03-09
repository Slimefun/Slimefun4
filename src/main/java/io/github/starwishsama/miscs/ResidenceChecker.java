package io.github.starwishsama.miscs;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ResidenceChecker implements Listener {
    private static boolean installed = false;

    @EventHandler
    public void onAndroidInteract(AndroidMineEvent e) {
        Block android = e.getAndroid().getBlock();
        Block block = e.getBlock();
        final Player p = Bukkit.getPlayer(BlockDataUtils.getOwnerByJson(BlockStorage.getBlockInfoAsJson(android)));

        if (!check(p, block, true) && p != null) {
            e.setCancelled(true);
            SlimefunPlugin.getLocal().sendMessage(p, "android.no-permission");
        }
    }

    public ResidenceChecker(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static boolean isInstalled(SlimefunPlugin plugin) {
        boolean result = plugin.getServer().getPluginManager().getPlugin("Residence") != null;
        installed = result;
        return result;
    }

    public static boolean check(Player p, Block block, boolean isAndroid) {
        if (installed) {
            ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(block.getLocation());
            if (p.isOp()) {
                return true;
            }

            if (res != null && !p.hasPermission("residence.bypass.build") && !p.hasPermission("residence.bypass.destroy")) {
                ResidencePermissions perms = res.getPermissions();
                if (res.getOwnerUUID() == p.getUniqueId()) {
                    return true;
                }

                if (isAndroid) {
                    return perms.playerHas(p, Flags.destroy, true) || perms.playerHas(p, Flags.place, true);
                } else if (!perms.playerHas(p, Flags.use, true)) {
                    SlimefunPlugin.getLocal().sendMessage(p, "inventory.no-access");
                    return false;
                }
            }
        }
        return true;
    }
}
