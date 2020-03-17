package io.github.starwishsama.utils;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;
import java.util.logging.Level;

public class ProtectionChecker implements Listener {
    private static boolean installed = false;

    @EventHandler
    public void onAndroidInteract(AndroidMineEvent e) {
        if (e != null) {
            Block android = e.getAndroid().getBlock();
            Block block = e.getBlock();
            Player p = Bukkit.getPlayer(getOwnerByJson(BlockStorage.getBlockInfoAsJson(android)));

            if (!check(p, block, true) && p != null) {
                e.setCancelled(true);
                SlimefunPlugin.getLocal().sendMessage(p, "android.no-permission");
            }
        }
    }

    public ProtectionChecker(SlimefunPlugin plugin) {
        if (isInstalled(plugin)) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            plugin.getLogger().log(Level.INFO, "检测到领地插件, 领地相关功能已开启");
        } else {
            plugin.getLogger().log(Level.WARNING, "未检测到领地插件, 领地相关功能将自动关闭");
        }
    }

    public static boolean isInstalled(SlimefunPlugin plugin) {
        boolean result = plugin.getServer().getPluginManager().getPlugin("Residence") != null;
        installed = result;
        return result;
    }

    public static boolean check(Player p, Block block, boolean isAndroid) {
        if (installed) {
            ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(block.getLocation());
            if (p != null) {
                if (p.isOp()) {
                    return true;
                } else if (res != null && !p.hasPermission("residence.bypass.build") && !p.hasPermission("residence.bypass.destroy")) {
                    ResidencePermissions perms = res.getPermissions();
                    if (res.getOwnerUUID() == p.getUniqueId()) {
                        return true;
                    }
                    if (isAndroid) {
                        return perms.playerHas(p, Flags.destroy, true) || perms.playerHas(p, Flags.place, true);
                    }
                    if (!perms.playerHas(p, Flags.use, true)) {
                        SlimefunPlugin.getLocal().sendMessage(p, "inventory.no-access");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static UUID getOwnerByJson(String json) {
        if (json != null) {
            JsonElement element = new JsonParser().parse(json);
            if (!element.isJsonNull()) {
                JsonObject object = element.getAsJsonObject();
                return UUID.fromString(object.get("owner").getAsString());
            }
        }
        return null;
    }
}
