package io.github.thebusybiscuit.slimefun4.api.items;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

public class ItemCooldown {

    private final SlimefunItem assignedItem;
    private final long durationSecs;
    private final Set<Class<? extends ItemHandler>> managedHandlers = new HashSet<>();
    private final Map<UUID, Long> activeCooldowns = new HashMap<>();

    public ItemCooldown(SlimefunItem slimefunItem, long durationSecs) {
        this.assignedItem = slimefunItem;
        this.durationSecs = durationSecs;
    }

    @SafeVarargs
    public final void assignHandler(Class<? extends ItemHandler>... handlers) {
        Collections.addAll(managedHandlers, handlers);
    }

    public long applyCooldown(@Nonnull Player player) {
        long cooldownEnd = getCooldownEnd(durationSecs);

        activeCooldowns.put(player.getUniqueId(), cooldownEnd);
        return cooldownEnd;
    }

    public long applyCooldown(@Nonnull Player player, long durationSecs) {
        long cooldownEnd = getCooldownEnd(durationSecs);

        activeCooldowns.put(player.getUniqueId(), cooldownEnd);
        return cooldownEnd;
    }

    public long getCooldownEnd(@Nonnull Player player) {
        return activeCooldowns.getOrDefault(player.getUniqueId(), 0L);
    }

    public long getCooldownRemaining(@Nonnull Player player) {
        return getCooldownEnd(player) - System.currentTimeMillis();
    }

    public long getCooldownSecondsRemaining(@Nonnull Player player) {
        return getCooldownRemaining(player) / 1000;
    }

    public boolean isOnCooldown(@Nonnull Player player) {
        Long cooldown = activeCooldowns.get(player.getUniqueId());

        if (cooldown == null) {
            // Player does not have an active cooldown.
            return false;
        } else if (cooldown >= System.currentTimeMillis()) {
            // Player has an active cooldown that has not lapsed.
            return true;
        } else {
            // Player had an active cooldown that has lapsed, lets remove it.
            activeCooldowns.remove(player.getUniqueId());
            return false;
        }
    }

    public boolean isOnCooldown(@Nonnull Player player, @Nonnull Class<? extends ItemHandler> handlerClass) {
        return isHandlerAssigned(handlerClass) && isOnCooldown(player);
    }

    public void removeCooldown(@Nonnull Player player) {
        activeCooldowns.remove(player.getUniqueId());
    }

    private long getCooldownEnd(long duration) {
        return System.currentTimeMillis() + (duration * 1000);
    }

    public SlimefunItem getAssignedItem() {
        return assignedItem;
    }

    public boolean isHandlerAssigned(@Nonnull Class<? extends ItemHandler> handlerClass) {
        return managedHandlers.contains(handlerClass);
    }

    public Set<Class<? extends ItemHandler>> getManagedHandlers() {
        return managedHandlers;
    }
}
