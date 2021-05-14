package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import io.github.thebusybiscuit.cscorelib2.collections.LoopIterator;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.RainbowArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;

/**
 * The {@link RainbowArmorTask} is responsible for handling the change in color of any Rainbow Armor piece.
 *
 * @author martinbrom
 */
public class RainbowArmorTask extends AbstractArmorTask {

    private static final DyeColor[] DYE_COLORS = { DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE, DyeColor.PURPLE, DyeColor.MAGENTA };

    private final LoopIterator<Color> iterator;

    private Color currentColor = DYE_COLORS[0].getColor();

    public RainbowArmorTask() {
        iterator = new LoopIterator<>(Arrays.stream(DYE_COLORS)
                .map(DyeColor::getColor)
                .collect(Collectors.toList()));
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void handleArmorPiece(Player p, SlimefunArmorPiece sfArmorPiece, ItemStack armorPiece) {
        if (sfArmorPiece instanceof RainbowArmorPiece) {
            LeatherArmorMeta meta = (LeatherArmorMeta) armorPiece.getItemMeta();
            meta.setColor(currentColor);
            armorPiece.setItemMeta(meta);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void handlePlayer(Player p, PlayerProfile profile) {
    }

    @Override
    protected void handleTick() {
        currentColor = iterator.next();
    }
}
