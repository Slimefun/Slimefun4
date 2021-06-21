package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.RadioactiveItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * The {@link RadioactivityTask} handles radioactivity for
 * {@link Radioactive} items.
 *
 * @author Semisol
 *
 */
public class RadioactivityTask implements Runnable {
    public static HashMap<UUID, Double> radioactivityLevel = new HashMap<>();
    private final PotionEffect WITHER = new PotionEffect(PotionEffectType.WITHER, SlimefunPlugin.getCfg().getOrSetDefault("options.radiation-update-interval", 1) * 20 + 20, 1);
    private final PotionEffect WITHER2 = new PotionEffect(PotionEffectType.WITHER, SlimefunPlugin.getCfg().getOrSetDefault("options.radiation-update-interval", 1) * 20 + 20, 4);
    private final PotionEffect BLINDNESS = new PotionEffect(PotionEffectType.BLINDNESS, SlimefunPlugin.getCfg().getOrSetDefault("options.radiation-update-interval", 1) * 20 + 40, 0);
    private final PotionEffect SLOW = new PotionEffect(PotionEffectType.SLOW, SlimefunPlugin.getCfg().getOrSetDefault("options.radiation-update-interval", 1) * 20 + 20, 3);
    @Override
    public void run() {
        for (Player p: Bukkit.getOnlinePlayers()){
            if (!p.isValid() || p.isDead()) {
                continue;
            }

            PlayerProfile.get(p, profile -> {
                handleRadiation(p, profile);
            });
        }
    }
    private void handleRadiation(@Nonnull Player p, @Nonnull PlayerProfile prof){
        Set<SlimefunItem> radioactiveItems = SlimefunPlugin.getRegistry().getRadioactiveItems();
        if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) return;
        double exposureTotal = 0;
        UUID u = p.getUniqueId();
        if (!prof.hasFullProtectionAgainst(ProtectionType.RADIATION)){
            for (ItemStack item : p.getInventory()) {
                if (item == null || item.getType() == Material.AIR) continue;
                ItemStack tmpItem = item;

                if (!(item instanceof SlimefunItemStack) && radioactiveItems.size() > 1) {
                    // Performance optimization to reduce ItemMeta calls
                    tmpItem = ItemStackWrapper.wrap(tmpItem);
                }
                for (SlimefunItem i: radioactiveItems){
                    if (i.isItem(tmpItem)){
                        exposureTotal += ((double) tmpItem.getAmount()) * ((RadioactiveItem) i).getRadioactivity().exposureModifier;
                    }
                }
            }
        }
        double v2 = radioactivityLevel.getOrDefault(u, 0d);
        if (exposureTotal > 0){
            if (v2 == 0){
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.radiation-exposed");
            }
            double v = v2 + exposureTotal;
            radioactivityLevel.put(u, Math.min(v, 100));
        } else if (v2 > 0) {
            radioactivityLevel.put(u, Math.max(v2 - 1, 0));
        }
        double s = radioactivityLevel.getOrDefault(u, 0d);
        for (CurrentSymptom symptom: CurrentSymptom.values()){
            if (symptom.minExposure <= s){
                applySymptom(symptom, p);
            }
        }
        if (s > 0 || v2 > 0){
            p.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new ComponentBuilder().append(ChatColor.translateAlternateColorCodes('&', SlimefunPlugin.getLocalization().getMessage(p, "actionbar.radiation").replace("%level%", "" + s))).create()
            );
        }
    }
    private void applySymptom(@Nonnull CurrentSymptom s, @Nonnull Player p){
        SlimefunPlugin.runSync(() -> {
            switch (s) {
                case SLOW: {
                    p.addPotionEffect(SLOW);
                    break;
                }
                case SLOW_DAMAGE: {
                    p.addPotionEffect(WITHER);
                    break;
                }
                case BLINDNESS: {
                    p.addPotionEffect(BLINDNESS);
                    break;
                }
                case FAST_DAMAGE: {
                    p.addPotionEffect(WITHER2);
                    break;
                }
                case IMMINENT_DEATH: {
                    p.setHealth(0);
                    break;
                }
            }
        });
    }
    private enum CurrentSymptom {
        SLOW(10),
        SLOW_DAMAGE(25),
        BLINDNESS(50),
        FAST_DAMAGE(75),
        IMMINENT_DEATH(100);
        private final double minExposure;
        CurrentSymptom(double minExposure){
            this.minExposure = minExposure;
        }
    }
}

