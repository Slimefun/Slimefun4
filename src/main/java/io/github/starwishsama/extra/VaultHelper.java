package io.github.starwishsama.extra;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;

/**
 * @author Nameless
 */
public class VaultHelper {
    private static Economy econ = null;

    public static void register() {
        RegisteredServiceProvider<Economy> rsp = SlimefunPlugin.instance.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            Slimefun.getLogger().log(Level.INFO, "成功接入 Vault");
            econ = rsp.getProvider();
        } else {
            Slimefun.getLogger().log(Level.SEVERE, "无法接入 Vault. 如果你是 CMI 用户, 请至配置文件启用经济系统");
        }
    }

    public static Economy getEcon() {
        return econ;
    }

    public static boolean isUsable() {
        return econ != null && SlimefunPlugin.getRegistry().isUseMoneyUnlock();
    }
}
