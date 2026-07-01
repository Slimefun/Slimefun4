package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Java-8 universal port: cross-version main/off-hand item access.
 * <p>
 * {@code getItemInMainHand()/setItemInMainHand()} and the off-hand slot are 1.9+ APIs; on 1.8 there is
 * a single hand accessed via the now-deprecated {@code getItemInHand()/setItemInHand()} and no off-hand
 * at all. The holder is taken as {@link Object} (a {@code PlayerInventory} or {@code EntityEquipment} at
 * runtime) and methods are resolved reflectively, so this class loads on every version. Off-hand reads
 * return {@code AIR} on 1.8 and off-hand writes are ignored.
 *
 * @author Slimefun (Java-8 port)
 */
public final class HandCompat {

    private HandCompat() {}

    /**
     * The off-hand {@link EquipmentSlot}, added in 1.9. {@code null} on 1.8 (no off-hand slot), where it
     * is therefore never equal to a resolved hand — see {@link #getHand(Object)}.
     */
    @Nullable
    public static final EquipmentSlot OFF_HAND = resolveSlot("OFF_HAND");

    @Nullable
    private static EquipmentSlot resolveSlot(@Nonnull String name) {
        try {
            return EquipmentSlot.valueOf(name);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * The {@link EquipmentSlot} used in an interaction event. {@code getHand()} is a 1.9+ method; on 1.8
     * there is only one hand, so this falls back to {@link EquipmentSlot#HAND} (which exists on 1.8).
     *
     * @param event
     *            The interaction event (a {@code PlayerInteractEvent} / {@code PlayerInteractEntityEvent})
     *
     * @return The hand used, defaulting to {@link EquipmentSlot#HAND}
     */
    @Nonnull
    public static EquipmentSlot getHand(@Nonnull Object event) {
        Object result = ReflectionCompat.invoke(event, "getHand");
        return result instanceof EquipmentSlot ? (EquipmentSlot) result : EquipmentSlot.HAND;
    }

    // getMethod() scans the class's public methods on every call; these run on the interaction path,
    // so cache the resolved handle per (holder class, name, arity). A sentinel marks "no such method".
    private static final ConcurrentHashMap<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();
    private static final Method MISSING = missingSentinel();

    private static Method missingSentinel() {
        try {
            return Object.class.getMethod("toString");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Nullable
    private static Method method(@Nonnull Object holder, @Nonnull String name, Class<?>... params) {
        String key = holder.getClass().getName() + '#' + name + '/' + params.length;
        Method cached = METHOD_CACHE.get(key);

        if (cached != null) {
            return cached == MISSING ? null : cached;
        }

        Method resolved;

        try {
            resolved = holder.getClass().getMethod(name, params);
        } catch (Throwable ignored) {
            resolved = null;
        }

        METHOD_CACHE.put(key, resolved == null ? MISSING : resolved);
        return resolved;
    }

    @Nonnull
    private static ItemStack air() {
        return new ItemStack(Material.AIR);
    }

    @Nonnull
    public static ItemStack getMainHand(@Nonnull Object holder) {
        Method m = method(holder, "getItemInMainHand");

        if (m == null) {
            m = method(holder, "getItemInHand"); // 1.8 single-hand fallback
        }

        if (m != null) {
            try {
                Object result = m.invoke(holder);
                return result instanceof ItemStack ? (ItemStack) result : air();
            } catch (Throwable ignored) {
                // fall through
            }
        }

        return air();
    }

    @Nonnull
    public static ItemStack getOffHand(@Nonnull Object holder) {
        Method m = method(holder, "getItemInOffHand");

        if (m != null) {
            try {
                Object result = m.invoke(holder);
                return result instanceof ItemStack ? (ItemStack) result : air();
            } catch (Throwable ignored) {
                // fall through
            }
        }

        // No off-hand slot before 1.9.
        return air();
    }

    public static void setMainHand(@Nonnull Object holder, @Nullable ItemStack item) {
        Method m = method(holder, "setItemInMainHand", ItemStack.class);

        if (m == null) {
            m = method(holder, "setItemInHand", ItemStack.class); // 1.8 single-hand fallback
        }

        if (m != null) {
            try {
                m.invoke(holder, item);
            } catch (Throwable ignored) {
                // ignore
            }
        }
    }

    public static void setOffHand(@Nonnull Object holder, @Nullable ItemStack item) {
        Method m = method(holder, "setItemInOffHand", ItemStack.class);

        if (m != null) {
            try {
                m.invoke(holder, item);
            } catch (Throwable ignored) {
                // ignore
            }
        }

        // No-op before 1.9.
    }
}
