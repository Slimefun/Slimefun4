package io.github.thebusybiscuit.slimefun5.integrations;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;

/**
 * This handles all integrations with ClearLag.
 * We don't want it to clear our altar items.
 *
 * <p>
 * Java-8 universal port: ClearLag's {@code EntityRemoveEvent} only exists when ClearLag is installed,
 * so we cannot reference it in an {@code @EventHandler} signature (the class would fail to load). The
 * event type is resolved reflectively and a dynamic {@link EventExecutor} is registered for it; the
 * event's entity list is read and pruned reflectively.
 *
 * @author TheBusyBiscuit
 */
class ClearLagIntegration implements Listener {

    private static final String EVENT_CLASS = "me.minebuilders.clearlag.events.EntityRemoveEvent";

    private final Slimefun plugin;

    ClearLagIntegration(@Nonnull Slimefun plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Class<? extends Event> eventClass;

        try {
            eventClass = Class.forName(EVENT_CLASS).asSubclass(Event.class);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("ClearLag EntityRemoveEvent not found", e);
        }

        EventExecutor executor = (listener, event) -> onEntityRemove(event);
        plugin.getServer().getPluginManager().registerEvent(eventClass, this, EventPriority.NORMAL, executor, plugin);
    }

    private void onEntityRemove(@Nonnull Object event) {
        Object entityList = ReflectionCompat.invoke(event, "getEntityList");

        if (entityList instanceof List) {
            Iterator<?> iterator = ((List<?>) entityList).iterator();

            while (iterator.hasNext()) {
                Object n = iterator.next();

                if (n instanceof Item && SlimefunUtils.hasNoPickupFlag((Item) n)) {
                    iterator.remove();
                }
            }
        }
    }
}
