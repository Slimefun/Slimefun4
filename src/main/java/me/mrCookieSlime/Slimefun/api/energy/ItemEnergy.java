package me.mrCookieSlime.Slimefun.api.energy;

import java.math.BigDecimal;
import java.util.List;

import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;

public final class ItemEnergy {

    // We should find a replacement for this class
    // Perhaps we could also use PersistentData here too?
    private ItemEnergy() {}

    // "&c&o&8\u21E8 &e\u26A1 &70 / 50 J"

    public static float getStoredEnergy(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || item.getAmount() < 1) return 0F;
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0F;

        for (String line : item.getItemMeta().getLore()) {
            if (line.startsWith(ChatColors.color("&c&o&8\u21E8 &e\u26A1 &7")) && line.contains(" / ") && line.endsWith(" J")) {
                return Float.parseFloat(PatternUtils.SLASH_SEPARATOR.split(line)[0].replace(ChatColors.color("&c&o&8\u21E8 &e\u26A1 &7"), ""));
            }
        }

        return 0F;
    }

    public static float getMaxEnergy(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || item.getAmount() < 1) return 0F;
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0F;

        for (String line : item.getItemMeta().getLore()) {
            if (line.startsWith(ChatColors.color("&c&o&8\u21E8 &e\u26A1 &7")) && line.contains(" / ") && line.endsWith(" J")) {
                return Float.parseFloat(PatternUtils.SLASH_SEPARATOR.split(line)[1].replace(" J", ""));
            }
        }

        return 0F;
    }

    public static float addStoredEnergy(ItemStack item, float energy) {
        if (item == null || item.getType() == Material.AIR || item.getAmount() < 1) return 0F;
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0;

        float rest = 0F;
        float capacity = getMaxEnergy(item);

        if ((int) capacity == 0) {
            return rest;
        }

        float stored = getStoredEnergy(item);

        if (stored + energy > capacity) {
            rest = (stored + energy) - capacity;
            stored = capacity;
        }
        else if (stored + energy < 0) {
            stored = 0F;
        }
        else {
            stored = stored + energy;
        }

        List<String> lore = item.getItemMeta().getLore();

        int index = -1;
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);

            if (line.startsWith(ChatColors.color("&c&o&8\u21E8 &e\u26A1 &7")) && line.contains(" / ") && line.endsWith(" J")) {
                index = i;
                break;
            }
        }

        BigDecimal decimal = BigDecimal.valueOf(stored).setScale(2, BigDecimal.ROUND_HALF_UP);

        lore.set(index, ChatColors.color("&c&o&8\u21E8 &e\u26A1 &7") + decimal.floatValue() + " / " + capacity + " J");

        ItemMeta im = item.getItemMeta();
        im.setLore(lore);
        item.setItemMeta(im);
        return rest;
    }

    public static ItemStack chargeItem(ItemStack item, float energy) {
        addStoredEnergy(item, energy);
        return item;
    }

    public static void chargeInventory(Player p, float energy) {
        p.getInventory().setItemInMainHand(chargeItem(p.getInventory().getItemInMainHand(), energy));
        p.getInventory().setItemInOffHand(chargeItem(p.getInventory().getItemInOffHand(), energy));
        p.getInventory().setHelmet(chargeItem(p.getInventory().getHelmet(), energy));
        p.getInventory().setChestplate(chargeItem(p.getInventory().getChestplate(), energy));
        p.getInventory().setLeggings(chargeItem(p.getInventory().getLeggings(), energy));
        p.getInventory().setBoots(chargeItem(p.getInventory().getBoots(), energy));
    }

}
