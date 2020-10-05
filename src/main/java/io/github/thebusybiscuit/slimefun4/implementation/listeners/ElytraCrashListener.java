package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectiveArmor;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.ElytraCap;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Optional;

/**
 * The {@link Listener} for the {@link ElytraCap}.
 *
 * @author Seggan
 */
public class ElytraCrashListener implements Listener {

    public ElytraCrashListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerCrash(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getCause() == EntityDamageEvent.DamageCause.FALL ||
            e.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL)) return;

        Player p = (Player) e.getEntity();
        if (p.isGliding()) {
            ItemStack stack = p.getInventory().getHelmet();
            SlimefunItem item = SlimefunItem.getByItem(stack);
            if (!Slimefun.hasUnlocked(p, item, true)) return;
            if (item instanceof ProtectiveArmor) {
                Optional<PlayerProfile> optional = PlayerProfile.find(p);
                if (!optional.isPresent()) {
                    PlayerProfile.request(p);
                    return;
                }
                PlayerProfile profile = optional.get();

                if (profile.hasFullProtectionAgainst(ProtectionType.FLYING_INTO_WALL)) {
                    e.setDamage(0);
                    p.playSound(p.getLocation(), Sound.BLOCK_STONE_HIT, 20, 1);
                    if (p.getGameMode() != GameMode.CREATIVE && item instanceof DamageableItem) {
                        ((DamageableItem) item).damageItem(p, stack);
                    }
                }
            }
        }
    }
}
