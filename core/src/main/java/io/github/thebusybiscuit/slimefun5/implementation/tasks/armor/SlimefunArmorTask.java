package io.github.thebusybiscuit.slimefun5.implementation.tasks.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun5.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.armor.GlowstoneArmorPiece;
import io.github.thebusybiscuit.slimefun5.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ParticleCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedParticle;

/**
 * The {@link SlimefunArmorTask} is responsible for handling {@link SlimefunArmorPiece}
 *
 * @author TheBusyBiscuit
 * @author martinbrom
 * @author Semisol
 */
public class SlimefunArmorTask extends AbstractArmorTask {

    @Override
    @ParametersAreNonnullByDefault
    protected void onPlayerTick(Player p, PlayerProfile profile) {
        ItemStack[] armor = p.getInventory().getArmorContents();
        updateAndHandleArmor(p, armor, profile.getArmor());
    }

    @ParametersAreNonnullByDefault
    private void updateAndHandleArmor(Player p, ItemStack[] armor, HashedArmorpiece[] cachedArmor) {
        for (int slot = 0; slot < 4; slot++) {
            ItemStack item = armor[slot];
            HashedArmorpiece armorPiece = cachedArmor[slot];

            if (armorPiece.hasDiverged(item)) {
                SlimefunItem sfItem = SlimefunItem.getByItem(item);

                if (!(sfItem instanceof SlimefunArmorPiece)) {
                    // If it isn't actually Armor, then we won't care about it.
                    sfItem = null;
                }

                armorPiece.update(item, sfItem);
            }

            if (item != null && armorPiece.getItem().isPresent()) {
                Slimefun.runSync(() -> {
                    SlimefunArmorPiece sfArmorPiece = armorPiece.getItem().get();

                    if (sfArmorPiece.canUse(p, true)) {
                        onArmorPieceTick(p, sfArmorPiece, item);
                    }
                });
            }
        }
    }

    /**
     * Method to handle behavior for pieces of armor.
     * It is called per-player and per piece of armor.
     *
     * @param p
     *            The {@link Player} wearing the piece of armor
     * @param sfArmorPiece
     *            {@link SlimefunArmorPiece} Slimefun instance of the piece of armor
     * @param armorPiece
     *            The actual {@link ItemStack} of the armor piece
     */
    @ParametersAreNonnullByDefault
    protected void onArmorPieceTick(Player p, SlimefunArmorPiece sfArmorPiece, ItemStack armorPiece) {
        applyPotionEffects(p, sfArmorPiece);

        if (sfArmorPiece instanceof GlowstoneArmorPiece) {
            spawnGlowAura(p);
        }
    }

    @ParametersAreNonnullByDefault
    private void spawnGlowAura(Player p) {
        // ENCHANT is a soft sparkle; both it and the spawn call no-op on 1.8 where the particle API is absent.
        ParticleCompat.spawn(p.getWorld(), VersionedParticle.ENCHANT, p.getLocation().add(0, 1, 0), 3, 0.4, 0.6, 0.4);
    }

    /**
     * Applies every {@link PotionEffect} of the given {@link SlimefunArmorPiece} to the {@link Player}.
     * Shared between the periodic tick and the {@code PlayerArmorChangeEvent} listener so effects can be
     * applied immediately on equip instead of waiting for the next tick.
     *
     * @param p
     *            The {@link Player} wearing the piece of armor
     * @param sfArmorPiece
     *            The {@link SlimefunArmorPiece} whose effects should be applied
     */
    @ParametersAreNonnullByDefault
    public static void applyPotionEffects(Player p, SlimefunArmorPiece sfArmorPiece) {
        for (PotionEffect effect : sfArmorPiece.getPotionEffects()) {
            p.removePotionEffect(effect.getType());
            p.addPotionEffect(effect);
        }
    }
}

