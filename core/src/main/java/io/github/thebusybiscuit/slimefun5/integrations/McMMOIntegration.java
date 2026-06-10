package io.github.thebusybiscuit.slimefun5.integrations;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;

import io.github.thebusybiscuit.slimefun5.api.events.AutoDisenchantEvent;
import io.github.thebusybiscuit.slimefun5.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;

/**
 * This handles all integrations with mcMMO.
 *
 * <p>
 * Java-8 universal port: the mcMMO API is Java-17 bytecode and cannot sit on the Java-8 compile
 * classpath, so all mcMMO calls go through reflection. The handlers on Slimefun's own events
 * ({@link BlockPlacerPlaceEvent}, {@link AutoDisenchantEvent}) are normal {@code @EventHandler}s; the
 * handler for mcMMO's own {@code McMMOPlayerSalvageCheckEvent} is registered via a dynamic
 * {@link EventExecutor} because that event class only exists when mcMMO is installed.
 *
 * @author TheBusyBiscuit
 */
class McMMOIntegration implements Listener {

    private static final String SALVAGE_EVENT = "com.gmail.nossr50.events.skills.salvage.McMMOPlayerSalvageCheckEvent";
    private static final String MCMMO_CLASS = "com.gmail.nossr50.mcMMO";
    private static final String SKILL_UTILS_CLASS = "com.gmail.nossr50.util.skills.SkillUtils";

    private final Slimefun plugin;

    McMMOIntegration(@Nonnull Slimefun plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // mcMMO's salvage event only exists when mcMMO is installed - register it reflectively.
        try {
            Class<? extends Event> salvageEvent = Class.forName(SALVAGE_EVENT).asSubclass(Event.class);
            EventExecutor executor = (listener, event) -> onItemSalvage(event);
            plugin.getServer().getPluginManager().registerEvent(salvageEvent, this, EventPriority.NORMAL, executor, plugin);
        } catch (ClassNotFoundException e) {
            // mcMMO without the salvage skill - nothing to hook.
            Slimefun.logger().info("mcMMO salvage event not present; skipping that hook.");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlacerPlace(BlockPlacerPlaceEvent e) {
        // This registers blocks placed by the BlockPlacer as "player-placed"
        try {
            Object placeStore = ReflectionCompat.invokeStatic(Class.forName(MCMMO_CLASS), "getPlaceStore");
            ReflectionCompat.invoke(placeStore, "setTrue", e.getBlock());
        } catch (Exception | LinkageError x) {
            Slimefun.getIntegrations().logError("mcMMO", x);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onAutoDisenchant(AutoDisenchantEvent e) {
        try {
            ReflectionCompat.invokeStatic(Class.forName(SKILL_UTILS_CLASS), "removeAbilityBuff", e.getItem());
        } catch (Exception | LinkageError x) {
            Slimefun.getIntegrations().logError("mcMMO", x);
        }
    }

    private void onItemSalvage(@Nonnull Object event) {
        // Prevent Slimefun items from being salvaged
        Object salvageItem = ReflectionCompat.invoke(event, "getSalvageItem");

        if (salvageItem instanceof ItemStack && !isSalvageable((ItemStack) salvageItem)) {
            ReflectionCompat.invoke(event, "setCancelled", true);
            Object player = ReflectionCompat.invoke(event, "getPlayer");

            if (player instanceof Player) {
                Slimefun.getLocalization().sendMessage((Player) player, "anvil.mcmmo-salvaging");
            }
        }
    }

    /**
     * This method checks if an {@link ItemStack} can be salvaged or not.
     * We basically don't want players to salvage any {@link SlimefunItem} unless
     * it is a {@link VanillaItem}.
     *
     * @param item
     *            The {@link ItemStack} to check
     *
     * @return Whether this item can be safely salvaged
     */
    private boolean isSalvageable(@Nonnull ItemStack item) {
        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        return sfItem == null || sfItem instanceof VanillaItem;
    }
}
