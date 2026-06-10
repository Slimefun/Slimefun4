package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.EntityCompat;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun5.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.armor.ElytraCap;
import io.github.thebusybiscuit.slimefun5.implementation.items.armor.SlimefunArmorPiece;
import org.bukkit.event.entity.EntityToggleGlideEvent;

/**
 * The {@link Listener} for the {@link ElytraCap}.
 *
 * @author Seggan
 * @author J3fftw1
 * 
 * @see ElytraCap
 */
public class ElytraImpactListener implements Listener {

    private final Set<UUID> gliding =  new HashSet<>();

    public ElytraImpactListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onGlideToggle(EntityToggleGlideEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && EntityCompat.isGliding(entity)) {
            Player player = (Player) entity;            UUID uuid = player.getUniqueId();
            gliding.add(uuid);
        }
        // We tick 1 tick later because the player is being toggled of at the same tick as it takes damage.
        Slimefun.instance().getServer().getScheduler().runTaskLater(Slimefun.instance(), gliding::clear, 1);
    }

    @EventHandler
    public void onPlayerCrash(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            // We only wanna handle damaged Players
            return;
        }
        Player p = (Player) e.getEntity();

        if ((e.getCause() == DamageCause.FALL || "FLY_INTO_WALL".equals(e.getCause().name()))
            && (EntityCompat.isGliding(p) || gliding.contains(p.getUniqueId()))
        ) {
            Optional<PlayerProfile> optional = PlayerProfile.find(p);

            if (!optional.isPresent()) {
                PlayerProfile.request(p);
                return;
            }

            PlayerProfile profile = optional.get();
            Optional<SlimefunArmorPiece> helmet = profile.getArmor()[3].getItem();

            if (helmet.isPresent()) {
                SlimefunItem item = helmet.get();

                if (item.canUse(p, true) && profile.hasFullProtectionAgainst(ProtectionType.FLYING_INTO_WALL)) {
                    SoundEffect.ELYTRA_CAP_IMPACT_SOUND.playFor(p);
                    e.setCancelled(true);

                    if (item instanceof DamageableItem) {
                        DamageableItem damageableItem = (DamageableItem) item;                        damageableItem.damageItem(p, p.getInventory().getHelmet());
                    }
                }
            }
        }
    }
}

