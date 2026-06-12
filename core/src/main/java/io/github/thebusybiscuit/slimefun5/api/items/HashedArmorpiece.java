package io.github.thebusybiscuit.slimefun5.api.items;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun5.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.armor.SlimefunArmorTask;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;

/**
 * This class serves as a way of checking whether a {@link Player} has changed their armor
 * between ticks. We do that by storing the hash of their armor and a reference to the
 * corresponding {@link SlimefunArmorPiece} if such a correlation exists.
 * 
 * This gives us a significant performance improvement as we only need to check for a
 * {@link SlimefunArmorPiece} if the item diverged in the first place.
 * 
 * @author TheBusyBiscuit
 *
 * @see SlimefunArmorPiece
 * @see SlimefunArmorTask
 */
public final class HashedArmorpiece {

    // org.bukkit.inventory.meta.Damageable is 1.13+; pre-1.13 stores durability on the ItemStack instead.
    private static final boolean DAMAGEABLE_META = classExists("org.bukkit.inventory.meta.Damageable");

    private int hash;
    private Optional<SlimefunArmorPiece> item;

    /**
     * This initializes a new {@link HashedArmorpiece} with no {@link SlimefunArmorPiece}
     * and a zero hash.
     */
    public HashedArmorpiece() {
        this.hash = 0;
        this.item = Optional.empty();
    }

    /**
     * This will update this {@link HashedArmorpiece} with the given {@link ItemStack}
     * and the corresponding {@link SlimefunItem}
     * 
     * @param stack
     *            The new armorpiece to be stored in this {@link HashedArmorpiece}
     * @param item
     *            The {@link SlimefunItem} corresponding to the provided {@link ItemStack}, may be null
     */
    public void update(@Nullable ItemStack stack, @Nullable SlimefunItem item) {
        if (stack == null || stack.getType() == Material.AIR) {
            this.hash = 0;
        } else {
            this.hash = normalizedHash(stack);
        }

        if (item instanceof SlimefunArmorPiece) {
            SlimefunArmorPiece armorPiece = (SlimefunArmorPiece) item;            this.item = Optional.of(armorPiece);
        } else {
            this.item = Optional.empty();
        }
    }

    /**
     * This method checks whether the given {@link ItemStack} is no longer similar to the
     * one represented by this {@link HashedArmorpiece}.
     * 
     * @param stack
     *            The {@link ItemStack} to compare
     * @return Whether the {@link HashedArmorpiece} and the given {@link ItemStack} mismatch
     */
    public boolean hasDiverged(@Nullable ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) {
            return hash != 0;
        } else {
            return normalizedHash(stack) != hash;
        }
    }

    /**
     * Hashes the {@link ItemStack} with its durability normalised to zero, so that natural durability
     * loss does not register as a divergence. On 1.13+ the damage lives on the {@link ItemMeta}
     * ({@code Damageable#setDamage}, invoked reflectively to avoid referencing a 1.13+ type); on older
     * versions it lives on the {@link ItemStack} ({@code setDurability}).
     */
    private static int normalizedHash(@Nonnull ItemStack stack) {
        ItemStack copy = stack.clone();

        if (DAMAGEABLE_META) {
            ItemMeta meta = copy.getItemMeta();
            ReflectionCompat.invoke(meta, "setDamage", 0);
            copy.setItemMeta(meta);
        } else {
            copy.setDurability((short) 0);
        }

        return copy.hashCode();
    }

    private static boolean classExists(@Nonnull String name) {
        try {
            Class.forName(name);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /**
     * Returns the {@link SlimefunArmorPiece} that corresponds to this {@link HashedArmorpiece},
     * or an empty {@link Optional}
     * 
     * @return An {@link Optional} describing the result
     */
    public @Nonnull Optional<SlimefunArmorPiece> getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "HashedArmorpiece {hash=" + hash + ",item=" + item.map(SlimefunItem::getId).orElse("null") + '}';
    }

}

