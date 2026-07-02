package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.armor.SlimefunArmorTask;

/**
 * This {@link Listener} applies the {@link SlimefunArmorPiece} potion effects the moment a piece is
 * equipped, so a player does not have to wait for the next periodic armor tick.
 * <p>
 * {@link PlayerArmorChangeEvent} is a Paper event. Registration is guarded by class availability in
 * {@link Slimefun}, so on a non-Paper server this listener is simply never constructed.
 *
 * @author TheBusyBiscuit
 */
public class ArmorEquipListener implements Listener {

    public ArmorEquipListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent e) {
        ItemStack newItem = e.getNewItem();

        if (newItem == null) {
            return;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(newItem);

        if (sfItem instanceof SlimefunArmorPiece) {
            Player p = e.getPlayer();
            SlimefunArmorPiece armorPiece = (SlimefunArmorPiece) sfItem;

            if (armorPiece.canUse(p, true)) {
                Slimefun.runSync(() -> SlimefunArmorTask.applyPotionEffects(p, armorPiece));
            }
        }
    }
}
