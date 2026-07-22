package io.github.thebusybiscuit.slimefun5.implementation.listeners.crafting;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.core.services.localization.RenamedItems;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Tags the result of a pure anvil rename of a {@link SlimefunItem} as player-renamed (see
 * {@link RenamedItems}), so the translation layer keeps that custom name instead of overwriting it.
 * <p>
 * {@code PrepareAnvilEvent} is 1.9+ and does not exist at the 1.8.8 compile floor, so the event is
 * registered reflectively via an {@link org.bukkit.plugin.EventExecutor}: on 1.8 the class is absent and
 * registration is skipped (renames simply are not preserved there), on 1.9+ it wires up normally. The
 * event's inventory and result are read through the base {@link Inventory}/{@link ItemStack} types, which
 * exist on every version.
 *
 * @see AnvilListener
 */
public class AnvilRenameListener implements Listener {

    public AnvilRenameListener(@Nonnull Slimefun plugin) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventClass =
                (Class<? extends Event>) Class.forName("org.bukkit.event.inventory.PrepareAnvilEvent");

            plugin.getServer().getPluginManager().registerEvent(eventClass, this, EventPriority.NORMAL,
                (listener, event) -> onPrepareAnvil(event), plugin, true);
        } catch (Throwable ignored) {
            // PrepareAnvilEvent absent (1.8) - renames are not preserved on that legacy floor.
        }
    }

    private void onPrepareAnvil(@Nonnull Event event) {
        try {
            ItemStack result = (ItemStack) event.getClass().getMethod("getResult").invoke(event);

            if (result == null || result.getType() == Material.AIR) {
                return;
            }

            Inventory inventory = (Inventory) event.getClass().getMethod("getInventory").invoke(event);
            ItemStack first = inventory.getItem(0);
            ItemStack second = inventory.getItem(1);

            // Only a pure rename: left slot only, a non-guide SlimefunItem.
            if (second != null && second.getType() != Material.AIR) {
                return;
            }

            if (SlimefunGuide.isGuideItem(first) || SlimefunItem.getByItem(first) == null) {
                return;
            }

            ItemMeta resultMeta = result.getItemMeta();

            if (resultMeta == null || !resultMeta.hasDisplayName()) {
                return;
            }

            // Skip when the name did not actually change, so we never needlessly mark.
            ItemMeta firstMeta = first.getItemMeta();
            String firstName = firstMeta != null && firstMeta.hasDisplayName() ? firstMeta.getDisplayName() : null;

            if (resultMeta.getDisplayName().equals(firstName)) {
                return;
            }

            RenamedItems.mark(resultMeta);
            result.setItemMeta(resultMeta);
            event.getClass().getMethod("setResult", ItemStack.class).invoke(event, result);
        } catch (Throwable ignored) {
            // A reflective failure must never break anvil use.
        }
    }
}
