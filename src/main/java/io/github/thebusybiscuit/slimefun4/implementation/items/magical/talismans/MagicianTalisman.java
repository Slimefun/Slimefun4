package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.TalismanListener;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MagicianTalisman extends Talisman {

    private final Set<TalismanEnchantment> enchantments = new HashSet<>();

    public MagicianTalisman(SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, false, false, "magician", 80);

        for (Enchantment enchantment : Enchantment.values()) {
            for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                enchantments.add(new TalismanEnchantment(enchantment, i));
            }
        }

        if (!enchantments.isEmpty()) {
            addItemSetting(enchantments.toArray(new ItemSetting[0]));
        }
    }

    public TalismanEnchantment getRandomEnchantment(ItemStack item) {
        List<TalismanEnchantment> enabled = enchantments.stream().filter(e -> e.getEnchantment().canEnchantItem(item)).filter(TalismanEnchantment::getValue).collect(Collectors.toList());
        return enabled.isEmpty() ? null : enabled.get(ThreadLocalRandom.current().nextInt(enabled.size()));
    }

    /**
     * This class is an extension of {@link ItemSetting} that holds an {@link Enchantment} and
     * a level. It is only used by the {@link TalismanListener} to handle the {@link MagicianTalisman}.
     * 
     * @author TheBusyBiscuit
     *
     */
    public static class TalismanEnchantment extends ItemSetting<Boolean> {

        private final Enchantment enchantment;
        private final int level;

        public TalismanEnchantment(Enchantment enchantment, int level) {
            super("allow-enchantments." + enchantment.getKey().getKey() + ".level." + level, true);

            this.enchantment = enchantment;
            this.level = level;
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }

        public int getLevel() {
            return level;
        }

    }

}
