package io.github.thebusybiscuit.slimefun4.integrations;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.milkbowl.vault.economy.Economy;

/**
 * This handles all integrations with Vault's economy.
 * Used to unlock research.
 *
 * @author Mis
 *
 */
public class VaultIntegration {
    private RegisteredServiceProvider<Economy> economy;

    public VaultIntegration() {
        this.economy = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
    }

    public double getBalance(@Nonnull Player player) {
        return this.economy.getProvider().getBalance(player);
    }

    public boolean hasBalance(@Nonnull Player player, double amount) {
        return this.getBalance(player) >= amount;
    }

    public void deposit(@Nonnull Player player, double amount) {
        this.economy.getProvider().depositPlayer(player, amount);
    }

    public void withdraw(@Nonnull Player player, double amount) {
        this.economy.getProvider().withdrawPlayer(player, amount);
    }

    /**
     * Checks if the player has enough money to buy this research,
     * depending on the xp levels required and the price multiplier.
     *
     * @return whether the player can afford the research or not.
     */
    public boolean hasBalanceForResearch(@Nonnull Player player, int xpCost) {
        return this.hasBalance(player, xpCost * Slimefun.getRegistry().getEconomyPriceMultiplier());
    }

    /**
     * Withdraws money from the player by the given amount times the price multiplier.
     */
    public void withdrawForResearch(@Nonnull Player player, int xpCost) {
        this.withdraw(player, xpCost * Slimefun.getRegistry().getEconomyPriceMultiplier());
    }

    /**
     * Returns the formatted price, according to the xp level cost and price multiplier.
     *
     * @return the formatted price.
     */
    public String getResearchPrice(int xpCost) {
        return this.economy.getProvider().format(xpCost * Slimefun.getRegistry().getEconomyPriceMultiplier());
    }
}
