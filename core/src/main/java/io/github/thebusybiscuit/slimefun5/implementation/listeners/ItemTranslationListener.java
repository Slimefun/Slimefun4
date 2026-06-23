package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.events.PlayerLanguageChangeEvent;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Implements per-holder item translation: a Slimefun item shows its name (and static lore) in the
 * language of whoever is holding it. When a player picks an item up, changes their language, or simply
 * has items in their inventory, those items are re-skinned to that player's language - so an English
 * item picked up by a German player turns German, identified by its Slimefun id.
 *
 * @author TheBusyBiscuit
 *
 * @see io.github.thebusybiscuit.slimefun5.core.services.localization.ItemTranslationService
 */
public class ItemTranslationListener implements Listener {

    public ItemTranslationListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // A gentle periodic sweep so items added by other means (commands, other plugins) also end up
        // in the holder's language. It only rewrites a stack when its name/lore is not already correct,
        // so stable inventories incur no item changes.
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                translateInventory(p);
            }
        }, 100L, 60L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            ItemStack stack = e.getItem().getItemStack();

            if (Slimefun.getItemTranslationService().applyHolderTranslation(p, stack)
                | Slimefun.getItemTranslationService().applyGuideTranslation(p, stack)) {
                e.getItem().setItemStack(stack);
            }
        }
    }

    @EventHandler
    public void onLanguageChange(PlayerLanguageChangeEvent e) {
        // The language is applied after this event resolves, so re-skin one tick later.
        Player p = e.getPlayer();
        Slimefun.runSync(() -> translateInventory(p), 1L);
    }

    private void translateInventory(@Nonnull Player p) {
        ItemStack[] contents = p.getInventory().getContents();

        for (int slot = 0; slot < contents.length; slot++) {
            ItemStack stack = contents[slot];

            // getContents() may return copies, so write the stack back when it was changed.
            if (Slimefun.getItemTranslationService().applyHolderTranslation(p, stack)
                | Slimefun.getItemTranslationService().applyGuideTranslation(p, stack)) {
                p.getInventory().setItem(slot, stack);
            }
        }
    }
}
