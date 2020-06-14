package io.github.starwishsama.extra;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.plotsquared.core.plot.Plot;
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

/**
 * 保护插件权限检查器
 *
 * @author Nameless
 */
public class ProtectionChecker implements Listener {
    private static boolean resInstalled = false;
    private static boolean plotInstalled = false;

    @EventHandler
    public void onAndroidMine(AndroidMineEvent e) {
        if (e != null) {
            Player p = Bukkit.getPlayer(getOwnerByJson(BlockStorage.getBlockInfoAsJson(e.getAndroid().getBlock())));

            if (!canInteract(p, e.getBlock(), InteractType.DESTROY)) {
                e.setCancelled(true);
                SlimefunPlugin.getLocal().sendMessage(p, "android.no-permission");
            }
        }
    }

    public ProtectionChecker(SlimefunPlugin plugin) {
        checkInstallStatus(plugin);

        if (!resInstalled && !plotInstalled) {
            plugin.getLogger().log(Level.WARNING, "未检测到领地/地皮插件, 相关功能将自动关闭");
            return;
        }

        if (resInstalled) plugin.getLogger().log(Level.INFO, "检测到领地插件, 相关功能已开启");
        if (plotInstalled) plugin.getLogger().log(Level.INFO, "检测到地皮插件, 相关功能已开启");

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static void checkInstallStatus(SlimefunPlugin plugin) {
        resInstalled = plugin.getServer().getPluginManager().getPlugin("Residence") != null;
        plotInstalled = plugin.getServer().getPluginManager().getPlugin("PlotSquared") != null;
    }

    /**
     * 检查是否可以在领地/地皮内破坏/交互方块
     *
     * @param p     玩家
     * @param block 被破坏的方块
     * @param type  交互类型
     * @return 是否可以破坏
     */
    public static boolean canInteract(Player p, Block block, InteractType type) {
        if (p != null && block != null) {
            if (p.isOp()) {
                return true;
            }

            if (resInstalled) {
                ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(block.getLocation());
                if (res != null) {
                    ResidencePermissions perms = res.getPermissions();

                    if (res.getOwnerUUID() == p.getUniqueId()) {
                        return true;
                    }

                    switch (type) {
                        case DESTROY:
                            return perms.playerHas(p, Flags.destroy, true) || perms.playerHas(p, Flags.build, true);
                        case PLACE:
                        case MOVE:
                            return perms.playerHas(p, Flags.place, true) || perms.playerHas(p, Flags.build, true);
                        case INTERACT:
                            if (!perms.playerHas(p, Flags.use, true)) {
                                SlimefunPlugin.getLocal().sendMessage(p, "inventory.no-access");
                                return false;
                            } else {
                                return true;
                            }
                    }
                }
            }

            if (plotInstalled) {
                Plot plot = Plot.getPlot(new com.plotsquared.core.location.Location(block.getWorld().getName(), block.getX(), block.getY(), block.getZ()));

                if (plot != null) {
                    return plot.isOwner(p.getUniqueId()) || plot.isAdded(p.getUniqueId());
                }
            }
        }
        return true;
    }

    public static UUID getOwnerByJson(String json) {
        if (json != null) {
            JsonElement element = new JsonParser().parse(json);
            if (!element.isJsonNull()) {
                JsonObject object = element.getAsJsonObject();
                return UUID.fromString(object.get("owner").getAsString());
            }
        }
        return null;
    }

    public enum InteractType {
        DESTROY, PLACE, INTERACT, MOVE
    }
}
