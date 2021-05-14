package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Arrays;
import java.util.stream.Collectors;

import io.github.thebusybiscuit.cscorelib2.collections.LoopIterator;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * The {@link RainbowArmorTask} is responsible for handling the change in color of any Rainbow Armor piece
 *
 * @author martinbrom
 */
public class RainbowArmorTask extends AbstractArmorTask {

    private final LoopIterator<Color> iterator;

    // TODO: 14.05.21 Reorder the colors to look nice
    public RainbowArmorTask() {
        // TODO: 14.05.21 Can I maybe just call iterator() instead of collect() and cast to LoopIterator ??
        iterator = new LoopIterator<>(Arrays.stream(DyeColor.values())
                .map(DyeColor::getColor)
                .collect(Collectors.toList()));
    }

    @Override
    protected void handle(Player p, PlayerProfile profile, ItemStack[] armor) {
        Color color = iterator.next();
        for (int slot = 0; slot < 4; slot++) {
            ItemStack item = armor[slot];
            if (item != null && SlimefunTag.LEATHER_ARMOR.isTagged(item.getType())) {
                LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                meta.setColor(color);
                item.setItemMeta(meta);
            }
        }
    }
}
