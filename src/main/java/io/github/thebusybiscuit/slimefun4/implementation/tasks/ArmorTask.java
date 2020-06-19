package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.SolarHelmet;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ArmorTask implements Runnable {

    private final Set<PotionEffect> radiationEffects;

    public ArmorTask() {
        Set<PotionEffect> effects = new HashSet<>();
        effects.add(new PotionEffect(PotionEffectType.WITHER, 400, 2));
        effects.add(new PotionEffect(PotionEffectType.BLINDNESS, 400, 3));
        effects.add(new PotionEffect(PotionEffectType.CONFUSION, 400, 3));
        effects.add(new PotionEffect(PotionEffectType.WEAKNESS, 400, 2));
        effects.add(new PotionEffect(PotionEffectType.SLOW, 400, 1));
        effects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 400, 1));
        radiationEffects = Collections.unmodifiableSet(effects);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.isValid() || p.isDead()) {
                continue;
            }

            PlayerProfile.get(p, profile -> {
                ItemStack[] armor = p.getInventory().getArmorContents();
                HashedArmorpiece[] cachedArmor = profile.getArmor();

                handleSlimefunArmor(p, armor, cachedArmor);
                checkForSolarHelmet(p);
                checkForRadiation(p);
            });
        }
    }

    private void handleSlimefunArmor(Player p, ItemStack[] armor, HashedArmorpiece[] cachedArmor) {
        for (int slot = 0; slot < 4; slot++) {
            ItemStack item = armor[slot];
            HashedArmorpiece armorpiece = cachedArmor[slot];

            if (armorpiece.hasDiverged(item)) {
                SlimefunItem sfItem = SlimefunItem.getByItem(item);
                if (!(sfItem instanceof SlimefunArmorPiece) || !Slimefun.hasUnlocked(p, sfItem, true)) {
                    sfItem = null;
                }

                armorpiece.update(item, sfItem);
            }

            if (item != null && armorpiece.getItem().isPresent()) {
                Slimefun.runSync(() -> {
                    for (PotionEffect effect : armorpiece.getItem().get().getPotionEffects()) {
                        p.removePotionEffect(effect.getType());
                        p.addPotionEffect(effect);
                    }
                });
            }
        }
    }

    private void checkForSolarHelmet(Player p) {
        if (!SlimefunUtils.isItemSimilar(p.getInventory().getHelmet(), SlimefunItems.SOLAR_HELMET, true)) {
            return;
        }

        SlimefunItem item = SlimefunItem.getByItem(p.getInventory().getHelmet());

        if (item instanceof SolarHelmet && Slimefun.hasUnlocked(p, item, true) && hasSunlight(p)) {
            ItemEnergy.chargeInventory(p, (float) ((SolarHelmet) item).getChargeAmount());
        }
    }

    private boolean hasSunlight(Player p) {
        return (p.getWorld().getTime() < 12300 || p.getWorld().getTime() > 23850) && p.getEyeLocation().getBlock().getLightFromSky() == 15;
    }

    private void checkForRadiation(Player p) {
        // Check for a Hazmat Suit
        if (!SlimefunUtils.isItemSimilar(SlimefunItems.SCUBA_HELMET, p.getInventory().getHelmet(), true) || !SlimefunUtils.isItemSimilar(SlimefunItems.HAZMAT_CHESTPLATE, p.getInventory().getChestplate(), true) || !SlimefunUtils.isItemSimilar(SlimefunItems.HAZMAT_LEGGINGS, p.getInventory().getLeggings(), true) || !SlimefunUtils.isItemSimilar(SlimefunItems.RUBBER_BOOTS, p.getInventory().getBoots(), true)) {
            for (ItemStack item : p.getInventory()) {
                if (isRadioactive(p, item)) {
                    break;
                }
            }
        }
    }

    private boolean isRadioactive(Player p, ItemStack item) {
        for (SlimefunItem radioactiveItem : SlimefunPlugin.getRegistry().getRadioactiveItems()) {
            if (radioactiveItem.isItem(item) && Slimefun.isEnabled(p, radioactiveItem, true)) {
                // If the item is enabled in the world, then make radioactivity do its job
                SlimefunPlugin.getLocal().sendMessage(p, "messages.radiation");

                Slimefun.runSync(() -> {
                    p.addPotionEffects(radiationEffects);
                    p.setFireTicks(400);
                });

                return true;
            }
        }

        return false;
    }

}
