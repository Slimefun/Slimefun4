package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.player.StatusEffect;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.RadioactiveItem;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.RadiationSymptom;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

/**
 * The {@link RadioactivityTask} handles radioactivity for
 * {@link Radioactive} items.
 *
 * @author Semisol
 *
 */
public class RadioactivityTask implements Runnable {

    private static final RadiationSymptom[] SYMPTOMS = RadiationSymptom.values();
    private static StatusEffect RADIATION_EFFECT;

    {
        assert SlimefunPlugin.instance() != null;
        RADIATION_EFFECT = new StatusEffect(new NamespacedKey(SlimefunPlugin.instance(), "radiation"));
    }

    public static void removePlayer(@Nonnull Player p) {
        RADIATION_EFFECT.clear(p);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.isValid() || p.isDead()) {
                continue;
            }

            PlayerProfile.get(p, profile -> handleRadiation(p, profile));
        }
    }

    private void handleRadiation(@Nonnull Player p, @Nonnull PlayerProfile profile) {
        if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        int exposureTotal = 0;

        if (!profile.hasFullProtectionAgainst(ProtectionType.RADIATION)) {
            for (ItemStack item : p.getInventory()) {
                if (item == null || item.getType().isAir()) {
                    continue;
                }
                SlimefunItem sfItem = SlimefunItem.getByItem(item);
                if (sfItem instanceof RadioactiveItem) {
                    exposureTotal += item.getAmount() * ((RadioactiveItem) sfItem).getRadioactivity().getExposureModifier();
                }
            }
        }
        
        int exposureLevelBefore = RADIATION_EFFECT.getLevel(p).orElse(0);
        if (exposureTotal > 0) {
            if (exposureLevelBefore == 0) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.radiation");
            }
            RADIATION_EFFECT.addPermanent(p, Math.min(exposureLevelBefore + exposureTotal, 100));
        } else if (exposureLevelBefore > 0) {
            RADIATION_EFFECT.addPermanent(p, RADIATION_EFFECT.getLevel(p).orElse(1) - 1);
        }
        
        int exposureLevelAfter = RADIATION_EFFECT.getLevel(p).orElse(0);
        for (RadiationSymptom symptom : SYMPTOMS) {
            if (symptom.shouldApply(exposureLevelAfter)) {
                symptom.apply(p);
            }
        }
        
        if (exposureLevelAfter > 0 || exposureLevelBefore > 0) {
            String msg = SlimefunPlugin.getLocalization().getMessage(p, "actionbar.radiation")
                    .replace("%level%", "" + exposureLevelAfter);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new ComponentBuilder().append(ChatColors.color(msg)).create()
            );
        }
    }
}
