package io.github.thebusybiscuit.slimefun5.core.attributes;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.ItemMetaCompat;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.SoundCompat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.utils.UnbreakingAlgorithm;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedEnchantment;

/**
 * This interface, when attached to a {@link SlimefunItem}, provides an easy method for damaging
 * an {@link ItemStack}, see {@link #damageItem(Player, ItemStack)}.
 * 
 * It also provides a simple {@link #isDamageable()} method, in case you wanna add a config
 * option that decides whether or not this {@link SlimefunItem} shall be damageable.
 * 
 * @author TheBusyBiscuit
 * @author RobotHanzo
 * 
 * @see UnbreakingAlgorithm
 * 
 */
public interface DamageableItem extends ItemAttribute {

    /**
     * Implement this method to make the behaviour of this interface dependent
     * on the state of your object.
     * You could add a {@link Config} option to toggle the behaviour for example.
     * 
     * @return Whether this {@link SlimefunItem} is damageable
     */
    boolean isDamageable();

    /**
     * This method will damage the given {@link ItemStack} once.
     * It also takes into account the {@link Enchantment} {@code Unbreaking}.
     * 
     * It will only apply the damage if {@link #isDamageable()} returned true.
     * 
     * @param p
     *            The {@link Player} to which the item belongs
     * @param item
     *            The {@link ItemStack} to damage
     */
    default void damageItem(@Nonnull Player p, @Nullable ItemStack item) {
        if (isDamageable() && item != null && !MaterialCompat.isAir(item.getType()) && item.getAmount() > 0) {
            int unbreakingLevel = item.getEnchantmentLevel(VersionedEnchantment.UNBREAKING);

            if (evaluateUnbreakingEnchantment(unbreakingLevel)) {
                return;
            }

            ItemMeta meta = item.getItemMeta();

            if (meta != null && !ItemMetaCompat.isUnbreakable(meta)) {
                int maxDurability = item.getType().getMaxDurability();
                // 1.13+ stores damage on the ItemMeta (Damageable, accessed reflectively to avoid a
                // 1.13+ type reference); older versions store it on the ItemStack's durability.
                int damage = DAMAGEABLE_META ? (Integer) ReflectionCompat.invoke(meta, "getDamage") : item.getDurability();

                if (damage >= maxDurability) {
                    // No need for a SoundEffect equivalent here since this is supposed to be a vanilla sound.
                    SoundCompat.playFor(p, p.getEyeLocation(), "ENTITY_ITEM_BREAK", null, 1, 1);
                    item.setAmount(0);
                    // On 1.8 a 0-amount in-hand stack isn't reliably cleared (it lingers and stays
                    // usable); blanking the type to AIR empties the slot on every version.
                    item.setType(Material.AIR);
                } else if (DAMAGEABLE_META) {
                    ReflectionCompat.invoke(meta, "setDamage", damage + 1);
                    item.setItemMeta(meta);
                } else {
                    item.setDurability((short) (damage + 1));
                }
            }
        }
    }

    // org.bukkit.inventory.meta.Damageable is 1.13+; pre-1.13 uses ItemStack durability instead.
    boolean DAMAGEABLE_META = classExists("org.bukkit.inventory.meta.Damageable");

    static boolean classExists(@Nonnull String name) {
        try {
            Class.forName(name);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /**
     * This method will randomly decide if the item should be damaged or not
     * This does not damage the item, it is called by {@link #damageItem(Player, ItemStack)} to randomly generate a
     * boolean
     * This function should be overridden when the item type is not a tool which is the default value
     *
     * @param unbreakingLevel
     *            The {@link Integer} level of the unbreaking {@link Enchantment}
     * 
     * @return Whether to save the item from taking damage
     *
     */
    default boolean evaluateUnbreakingEnchantment(int unbreakingLevel) {
        return UnbreakingAlgorithm.TOOLS.evaluate(unbreakingLevel);
    }

}

