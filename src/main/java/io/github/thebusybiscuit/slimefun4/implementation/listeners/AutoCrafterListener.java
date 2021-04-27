package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters.AbstractAutoCrafter;
import io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters.EnhancedAutoCrafter;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.Multimeter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link Listener} is responsible for providing interactions to the auto crafters.
 * See Issue #2896 with the {@link EnhancedAutoCrafter}, any {@link SlimefunItem} which
 * overrides the right click functonality would be ignored.
 * This {@link Listener} resolves that issue.
 * 
 * @author TheBusyBiscuit
 * 
 * @see EnhancedAutoCrafter
 *
 */
public class AutoCrafterListener implements Listener {

    @ParametersAreNonnullByDefault
    public AutoCrafterListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerRightClickEvent e) {
        Optional<Block> clickedBlock = e.getClickedBlock();

        // We want to make sure we used the main hand, the interaction was not cancelled and a Block was clicked.
        if (e.getHand() == EquipmentSlot.HAND && e.useBlock() != Result.DENY && clickedBlock.isPresent()) {
            Optional<SlimefunItem> slimefunBlock = e.getSlimefunBlock();

            // Check if the clicked Block is a Slimefun block.
            if (!slimefunBlock.isPresent()) {
                return;
            }

            SlimefunItem block = slimefunBlock.get();

            if (block instanceof AbstractAutoCrafter) {
                Optional<SlimefunItem> slimefunItem = e.getSlimefunItem();

                if (!e.getPlayer().isSneaking() && slimefunItem.isPresent() && slimefunItem.get() instanceof Multimeter) {
                    // Allow Multimeters to pass through and do their job
                    return;
                }

                // Prevent blocks from being placed, food from being eaten, etc...
                e.cancel();

                // Fixes 2896 - Forward the interaction before items get handled.
                AbstractAutoCrafter crafter = (AbstractAutoCrafter) block;

                try {
                    crafter.onRightClick(clickedBlock.get(), e.getPlayer());
                } catch (Exception | LinkageError x) {
                    crafter.error("Something went wrong while right-clicking an Auto-Crafter", x);
                }
            }
        }
    }

}
