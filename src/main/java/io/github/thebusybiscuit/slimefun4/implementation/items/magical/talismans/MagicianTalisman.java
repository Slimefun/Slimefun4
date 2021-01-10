package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.settings.TalismanEnchantment;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link MagicianTalisman} is a special kind of {@link Talisman} which awards a {@link Player}
 * with an extra {@link Enchantment} when they enchant their {@link ItemStack}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class MagicianTalisman extends Talisman {

    private final Set<TalismanEnchantment> enchantments = new HashSet<>();

    @ParametersAreNonnullByDefault
    public MagicianTalisman(SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, false, false, "magician", 80);

        for (Enchantment enchantment : Enchantment.values()) {
            try {
                for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                    enchantments.add(new TalismanEnchantment(enchantment, i));
                }
            } catch (Exception x) {
                SlimefunPlugin.logger().log(Level.SEVERE, x, () -> "The following Exception occurred while trying to register the following Enchantment: " + enchantment);
            }
        }

        if (!enchantments.isEmpty()) {
            addItemSetting(enchantments.toArray(new ItemSetting[0]));
        }
    }

    /**
     * This method picks a random {@link TalismanEnchantment} for the provided {@link ItemStack}.
     * The method will return null, if null was provided or no applicable {@link Enchantment} was found.
     * 
     * @param item
     *            The {@link ItemStack} to find an {@link Enchantment} for
     * @param existingEnchantments
     *            A {@link Set} containing the {@link Enchantment Enchantments} that currently exist on the
     *            {@link ItemStack}
     * 
     * @return An applicable {@link TalismanEnchantment} or null
     */
    @Nullable
    public TalismanEnchantment getRandomEnchantment(@Nonnull ItemStack item, @Nonnull Set<Enchantment> existingEnchantments) {
        Validate.notNull(item, "The ItemStack cannot be null");
        Validate.notNull(existingEnchantments, "The Enchantments Set cannot be null");

        // @formatter:off
        List<TalismanEnchantment> enabled = enchantments.stream()
                .filter(e -> e.getEnchantment().canEnchantItem(item))
                .filter(e -> hasConflicts(existingEnchantments, e))
                .filter(TalismanEnchantment::getValue)
                .collect(Collectors.toList());
        // @formatter:on

        return enabled.isEmpty() ? null : enabled.get(ThreadLocalRandom.current().nextInt(enabled.size()));
    }

    @ParametersAreNonnullByDefault
    private boolean hasConflicts(Set<Enchantment> enchantments, TalismanEnchantment ench) {
        for (Enchantment existing : enchantments) {
            if (existing.conflictsWith(ench.getEnchantment())) {
                return false;
            }
        }

        return true;
    }

}
