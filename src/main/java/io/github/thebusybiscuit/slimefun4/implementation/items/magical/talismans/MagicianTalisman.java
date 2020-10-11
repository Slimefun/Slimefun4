package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.implementation.settings.TalismanEnchantment;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MagicianTalisman extends Talisman {

    private final Set<TalismanEnchantment> enchantments = new HashSet<>();

    public MagicianTalisman(SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, false, false, "magician", 80);

        for (Enchantment enchantment : Enchantment.values()) {
            try {
                for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                    enchantments.add(new TalismanEnchantment(enchantment, i));
                }
            } catch (Exception x) {
                Slimefun.getLogger().log(Level.SEVERE, x, () -> "The following Exception occurred while trying to register the following Enchantment: " + enchantment);
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
     * 
     * @return An applicable {@link TalismanEnchantment} or null
     */
    public TalismanEnchantment getRandomEnchantment(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        List<TalismanEnchantment> enabled = enchantments.stream().filter(e -> e.getEnchantment().canEnchantItem(item)).filter(TalismanEnchantment::getValue).collect(Collectors.toList());
        return enabled.isEmpty() ? null : enabled.get(ThreadLocalRandom.current().nextInt(enabled.size()));
    }

}
