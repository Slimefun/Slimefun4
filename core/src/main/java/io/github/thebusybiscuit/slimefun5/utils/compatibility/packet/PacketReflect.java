package io.github.thebusybiscuit.slimefun5.utils.compatibility.packet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Best-effort NMS/CraftBukkit reflection for packet translation. Fields are located by TYPE (never by
 * obfuscated name) so it survives remapping. Every method returns null on any failure, so a caller can
 * always fall back to leaving a packet untouched.
 */
public final class PacketReflect {

    private PacketReflect() {}

    private static final Class<?> CHANNEL = forName("io.netty.channel.Channel");
    // Declaration order matters: NMS_ITEM must resolve before the two AS_* fields use it as an argType.
    private static final Class<?> NMS_ITEM = resolveNmsItemClass();
    private static final Method AS_BUKKIT_COPY = resolveCraftItemMethod("asBukkitCopy", NMS_ITEM);
    private static final Method AS_NMS_COPY = resolveCraftItemMethod("asNMSCopy", ItemStack.class);

    @Nullable
    private static Class<?> forName(String n) {
        try {
            return Class.forName(n);
        } catch (Throwable t) {
            return null;
        }
    }

    // Mirrors PacketItemDescriptor.resolveNmsItemClass: Mojang-mapped modern NMS, else legacy versioned Spigot NMS.
    @Nullable
    private static Class<?> resolveNmsItemClass() {
        Class<?> c = forName("net.minecraft.world.item.ItemStack");
        if (c != null) {
            return c;
        }
        String legacy = legacyNmsPackage();
        return legacy != null ? forName(legacy + ".ItemStack") : null;
    }

    @Nullable
    private static String legacyNmsPackage() {
        // Spigot: org.bukkit.craftbukkit.v1_8_R3 -> net.minecraft.server.v1_8_R3
        try {
            String cb = Bukkit.getServer().getClass().getPackage().getName();
            int i = cb.lastIndexOf('.');
            if (i < 0) {
                return null;
            }
            String ver = cb.substring(i + 1);
            return ver.startsWith("v") ? "net.minecraft.server." + ver : null;
        } catch (Throwable t) {
            return null;
        }
    }

    // CraftItemStack lives at org.bukkit.craftbukkit.<ver>.inventory.CraftItemStack (versioned pre-1.20.5)
    // or org.bukkit.craftbukkit.inventory.CraftItemStack (unversioned on modern Paper). Try both. Matched
    // by PARAMETER TYPE (not just name+arity): on 26.2 CraftItemStack has two one-arg asNMSCopy overloads
    // (ItemStack and List), and getMethods() order is not guaranteed across JVMs.
    @Nullable
    private static Method resolveCraftItemMethod(String name, @Nullable Class<?> argType) {
        try {
            String pkg = Bukkit.getServer().getClass().getPackage().getName(); // org.bukkit.craftbukkit[.<ver>]
            for (String cls : new String[] { pkg + ".inventory.CraftItemStack", "org.bukkit.craftbukkit.inventory.CraftItemStack" }) {
                Class<?> c = forName(cls);
                if (c != null) {
                    Method m = resolveByParamType(c, name, argType);
                    if (m != null) {
                        return m;
                    }
                }
            }
            return null;
        } catch (Throwable t) {
            return null;
        }
    }

    @Nullable
    static Method resolveByParamType(Class<?> c, String name, @Nullable Class<?> argType) {
        for (Method m : c.getMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == 1
                    && argType != null && m.getParameterTypes()[0].isAssignableFrom(argType)) {
                return m;
            }
        }
        return null;
    }

    @Nullable
    public static Object channelOf(Player player) {
        try {
            Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = fieldValueOfType(nmsPlayer, forName("net.minecraft.server.level.ServerPlayerConnection"));
            if (connection == null) {
                // Legacy/other mappings: scan the player's fields for anything owning a Channel.
                connection = firstOwnerOfChannel(nmsPlayer);
            }
            if (connection == null) {
                return null;
            }
            return fieldValueOfType(connection, CHANNEL) != null
                ? fieldValueOfType(connection, CHANNEL)
                : fieldValueOfType(fieldValueOfNetworkManager(connection), CHANNEL);
        } catch (Throwable t) {
            return null;
        }
    }

    @Nullable
    private static Object firstOwnerOfChannel(Object nmsPlayer) throws Exception {
        for (Field f : allFields(nmsPlayer.getClass())) {
            f.setAccessible(true);
            Object v = f.get(nmsPlayer);
            if (v != null && (hasFieldOfType(v, CHANNEL) || hasFieldOfType(fieldValueOfNetworkManager(v), CHANNEL))) {
                return v;
            }
        }
        return null;
    }

    @Nullable
    private static Object fieldValueOfNetworkManager(Object connection) {
        // PlayerConnection -> NetworkManager (the object that owns the Channel). Found by "has a Channel".
        if (connection == null) {
            return null;
        }
        try {
            for (Field f : allFields(connection.getClass())) {
                f.setAccessible(true);
                Object v = f.get(connection);
                if (v != null && hasFieldOfType(v, CHANNEL)) {
                    return v;
                }
            }
        } catch (Throwable ignored) {
            // fall through
        }
        return connection;
    }

    private static boolean hasFieldOfType(Object owner, Class<?> type) {
        return owner != null && type != null && firstFieldOfType(owner, type) != null;
    }

    @Nullable
    public static Field firstFieldOfType(Object owner, Class<?> type) {
        if (owner == null || type == null) {
            return null;
        }
        try {
            for (Field f : allFields(owner.getClass())) {
                if (type.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    return f;
                }
            }
            return null;
        } catch (Throwable t) {
            return null;
        }
    }

    @Nullable
    public static Object fieldValueOfType(Object owner, Class<?> type) {
        Field f = firstFieldOfType(owner, type);
        if (f == null) {
            return null;
        }
        try {
            return f.get(owner);
        } catch (Throwable t) {
            return null;
        }
    }

    private static java.util.List<Field> allFields(Class<?> c) {
        java.util.List<Field> out = new java.util.ArrayList<>();
        for (Class<?> k = c; k != null && k != Object.class; k = k.getSuperclass()) {
            for (Field f : k.getDeclaredFields()) {
                out.add(f);
            }
        }
        return out;
    }

    @Nullable
    public static ItemStack asBukkit(Object nmsItem) {
        if (nmsItem == null || AS_BUKKIT_COPY == null) {
            return null;
        }
        try {
            return (ItemStack) AS_BUKKIT_COPY.invoke(null, nmsItem);
        } catch (Throwable t) {
            return null;
        }
    }

    /** The resolved parameter type of {@code asNMSCopy}, for diagnostics (e.g. {@code /sf debugpackets}). */
    @Nullable
    public static Class<?> asNmsParamType() {
        return AS_NMS_COPY != null ? AS_NMS_COPY.getParameterTypes()[0] : null;
    }

    @Nullable
    public static Object asNms(ItemStack bukkit) {
        if (bukkit == null || AS_NMS_COPY == null) {
            return null;
        }
        try {
            return AS_NMS_COPY.invoke(null, bukkit);
        } catch (Throwable t) {
            return null;
        }
    }
}
