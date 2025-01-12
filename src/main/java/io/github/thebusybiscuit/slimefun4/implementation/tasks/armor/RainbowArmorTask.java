package io.github.thebusybiscuit.slimefun4.implementation.tasks.armor;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.RainbowArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;

/**
 * The {@link RainbowArmorTask} is responsible for handling the change in color of any Rainbow Armor piece.
 *
 * @author martinbrom
 */
public class RainbowArmorTask extends AbstractArmorTask {

    private long currentColorIndex = 0;

    @Override
    protected void onTick() {
        currentColorIndex++;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onPlayerTick(Player p, PlayerProfile profile) {
        final ItemStack[] armorContents = p.getInventory().getArmorContents();
        final HashedArmorpiece[] hashedArmorpieces = profile.getArmor();
        for (int i = 0; i < 4; i++) {
            Optional<SlimefunArmorPiece> sfArmorPiece = hashedArmorpieces[i].getItem();
            if (sfArmorPiece.isPresent() && sfArmorPiece.get() instanceof RainbowArmorPiece rainbowArmorPiece && rainbowArmorPiece.canUse(p, true) && armorContents[i].hasItemMeta() && !hashedArmorpieces[i].hasDiverged(armorContents[i], true)) {
                updateRainbowArmor(armorContents[i], rainbowArmorPiece);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void updateRainbowArmor(ItemStack itemStack, RainbowArmorPiece armorPiece) {
        Color[] colors = armorPiece.getColors();
        Color newColor = colors[(int) (currentColorIndex % colors.length)];

        if (itemStack.getItemMeta() instanceof  LeatherArmorMeta leatherArmorMeta) {
            leatherArmorMeta.setColor(newColor);
            itemStack.setItemMeta(leatherArmorMeta);
        }
    }
}
