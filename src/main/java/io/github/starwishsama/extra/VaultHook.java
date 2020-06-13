package io.github.starwishsama.extra;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;

/**
 * @author Nameless
 */
public class VaultHook {
    private static Economy econ = null;

    public static void register() {
        try {
            RegisteredServiceProvider<Economy> rsp = SlimefunPlugin.instance.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                Slimefun.getLogger().log(Level.INFO, "成功接入 Vault");
                econ = rsp.getProvider();
            }
        } catch (Exception e) {
            Slimefun.getLogger().log(Level.SEVERE, e, () -> "无法接入 Vault");
        }
    }

    public static Economy getEcon() {
        return econ;
    }

    public static boolean isUsable() {
        return econ != null && SlimefunPlugin.getRegistry().isUseMoneyUnlock();
    }
}
