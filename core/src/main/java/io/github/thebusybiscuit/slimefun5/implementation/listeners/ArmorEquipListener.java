package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.armor.SlimefunArmorTask;

/**
 * Applies {@link SlimefunArmorPiece} potion effects the instant a piece is equipped, so a player does
 * not have to wait for the next periodic armor tick.
 * <p>
 * This relies on Paper's {@code com.destroystokyo.paper.event.player.PlayerArmorChangeEvent}, which is
 * NOT on our Spigot compile classpath, so the event is looked up and registered entirely by reflection.
 * On a non-Paper server the class is absent, the listener never registers, and on-equip effects fall
 * back to the periodic {@link SlimefunArmorTask}.
 *
 * @author TheBusyBiscuit
 */
public class ArmorEquipListener implements Listener {

    public ArmorEquipListener(@Nonnull Slimefun plugin) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName("com.destroystokyo.paper.event.player.PlayerArmorChangeEvent");
            Method getNewItem = eventClass.getMethod("getNewItem");
            Method getPlayer = eventClass.getMethod("getPlayer");

            EventExecutor executor = (listener, event) -> {
                try {
                    onArmorChange((Player) getPlayer.invoke(event), (ItemStack) getNewItem.invoke(event));
                } catch (ReflectiveOperationException ignored) {
                    // Unexpected event shape on this server; skip silently.
                }
            };

            Bukkit.getPluginManager().registerEvent(eventClass, this, EventPriority.NORMAL, executor, plugin);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            // Non-Paper server: on-equip effects fall back to the periodic armor task.
        }
    }

    private void onArmorChange(@Nonnull Player p, @Nullable ItemStack newItem) {
        if (newItem == null) {
            return;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(newItem);

        if (sfItem instanceof SlimefunArmorPiece) {
            SlimefunArmorPiece armorPiece = (SlimefunArmorPiece) sfItem;

            if (armorPiece.canUse(p, true)) {
                Slimefun.runSync(() -> SlimefunArmorTask.applyPotionEffects(p, armorPiece));
            }
        }
    }
}
