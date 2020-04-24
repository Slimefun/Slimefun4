package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;

public class VaultHook {
    private static Economy econ = null;

    public static void register() {
        RegisteredServiceProvider<Economy> rsp = SlimefunPlugin.instance.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Slimefun.getLogger().log(Level.WARNING, "无法接入 Vault");
        } else {
            econ = rsp.getProvider();
        }
    }

    public static Economy getEcon() {
        return econ;
    }

    public static boolean isUsable() {
        return econ != null && SlimefunPlugin.getRegistry().isUseMoneyUnlock();
    }
}
