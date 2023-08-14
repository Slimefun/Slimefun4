package io.github.thebusybiscuit.slimefun4.integrations;

import com.google.common.base.Preconditions;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultIntegration {

    private static Economy economy;

    static void register(@Nonnull Plugin plugin) {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        } else {
            throw new RuntimeException("Unable to hook into vault");
        }
    }

    public static double getPlayerBalance(@Nonnull OfflinePlayer p) {
        Preconditions.checkArgument(p != null, "Player cannot be null!");
        Preconditions.checkArgument(economy != null, "Vault instance cannot be null!");

        return economy.getBalance(p);
    }

    public static void withdrawPlayer(@Nonnull OfflinePlayer p, double withdraw) {
        Preconditions.checkArgument(p != null, "Player cannot be null!");
        Preconditions.checkArgument(economy != null, "Vault instance cannot be null!");

        economy.withdrawPlayer(p, withdraw);
    }

    public static boolean isAvailable() {
        return economy != null && Slimefun.getRegistry().isResearchingEnabled();
    }
}
