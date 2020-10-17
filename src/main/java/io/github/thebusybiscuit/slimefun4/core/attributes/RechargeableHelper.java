package io.github.thebusybiscuit.slimefun4.core.attributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import net.md_5.bungee.api.ChatColor;

/**
 * This is just a simple helper class to provide static methods to the {@link Rechargeable}
 * interface.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Rechargeable
 *
 */
final class RechargeableHelper {

    private static final NamespacedKey CHARGE_KEY = new NamespacedKey(SlimefunPlugin.instance(), "item_charge");
    private static final String LORE_PREFIX = ChatColors.color("&8\u21E8 &e\u26A1 &7");
    private static final Pattern REGEX = Pattern.compile(ChatColors.color("(&c&o)?" + LORE_PREFIX) + "[0-9.]+ / [0-9.]+ J");

    private RechargeableHelper() {}

    static void setCharge(@Nonnull ItemMeta meta, float charge, float capacity) {
        BigDecimal decimal = BigDecimal.valueOf(charge).setScale(2, RoundingMode.HALF_UP);
        float value = decimal.floatValue();

        meta.getPersistentDataContainer().set(CHARGE_KEY, PersistentDataType.FLOAT, value);

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);

            if (REGEX.matcher(line).matches()) {
                lore.set(i, LORE_PREFIX + value + " / " + capacity + " J");
                meta.setLore(lore);
                return;
            }
        }

        lore.add(LORE_PREFIX + value + " / " + capacity + " J");
        meta.setLore(lore);
    }

    static float getCharge(@Nonnull ItemMeta meta) {
        Float value = meta.getPersistentDataContainer().get(CHARGE_KEY, PersistentDataType.FLOAT);

        // If persistent data is available, we just return this value
        if (value != null) {
            return value;
        }

        // If no persistent data exists, we will just fall back to the lore
        if (meta.hasLore()) {
            for (String line : meta.getLore()) {
                if (REGEX.matcher(line).matches()) {
                    String data = ChatColor.stripColor(PatternUtils.SLASH_SEPARATOR.split(line)[0].replace(LORE_PREFIX, ""));
                    return Float.parseFloat(data);
                }
            }
        }

        return 0;
    }

}
