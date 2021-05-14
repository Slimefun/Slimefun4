package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import io.github.thebusybiscuit.cscorelib2.collections.LoopIterator;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

/**
 * The {@link RainbowArmorTask} is responsible for handling the change in color of any Rainbow Armor piece.
 *
 * @author martinbrom
 */
public class RainbowArmorTask extends AbstractArmorTask {

    private static final DyeColor[] DYE_COLORS = { DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE, DyeColor.PURPLE, DyeColor.MAGENTA };

    private final LoopIterator<Color> iterator;

    public RainbowArmorTask() {
        iterator = new LoopIterator<>(Arrays.stream(DYE_COLORS)
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
