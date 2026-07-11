package io.github.thebusybiscuit.slimefun5.utils.compatibility.packet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;

/**
 * Describes an item-bearing clientbound packet and knows how to rewrite the item(s) it carries. Resolved
 * reflectively per server version; families whose class does not resolve are simply not produced.
 */
public final class PacketItemDescriptor {

    // Candidate simple/class names across the version range (Spigot obf + Mojang-mapped Paper).
    private static final String[][] FAMILIES = {
        { "net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket" },
        { "net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket" },
        // Legacy Spigot NMS lived in net.minecraft.server.<ver>; resolved via the server package below.
        { "PacketPlayOutSetSlot" },
        { "PacketPlayOutWindowItems" },
    };

    private final Class<?> packetClass;
    private final Class<?> nmsItemClass;

    private PacketItemDescriptor(Class<?> packetClass, Class<?> nmsItemClass) {
        this.packetClass = packetClass;
        this.nmsItemClass = nmsItemClass;
    }

    public static List<PacketItemDescriptor> resolveAll() {
        Class<?> nmsItem = resolveNmsItemClass();
        List<PacketItemDescriptor> out = new ArrayList<>();
        if (nmsItem == null) {
            return out;
        }
        String legacyPkg = legacyNmsPackage();
        for (String[] family : FAMILIES) {
            for (String name : family) {
                Class<?> c = tryClass(name);
                if (c == null && legacyPkg != null && name.indexOf('.') < 0) {
                    c = tryClass(legacyPkg + '.' + name);
                }
                if (c != null) {
                    out.add(new PacketItemDescriptor(c, nmsItem));
                    break;
                }
            }
        }
        return out;
    }

    public boolean matches(Object packet) {
        return packet != null && packetClass.isInstance(packet);
    }

    /** Rewrites every NMS item the packet carries via {@code itemRewriter}. Never throws. */
    public Object rewrite(Object packet, UnaryOperator<Object> itemRewriter) {
        try {
            // List payload (WindowItems / SetContent): mutate elements in place.
            Field listField = PacketReflect.firstFieldOfType(packet, List.class);
            if (listField != null) {
                Object listObj = listField.get(packet);
                if (listObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Object> list = (List<Object>) listObj;
                    for (int i = 0; i < list.size(); i++) {
                        Object el = list.get(i);
                        if (nmsItemClass.isInstance(el)) {
                            Object rewritten = itemRewriter.apply(el);
                            if (rewritten != null && rewritten != el) {
                                list.set(i, rewritten);
                            }
                        }
                    }
                }
            }
            // Single-item payload (SetSlot / ContainerSetSlot, or the carried/cursor item
            // alongside the list on ClientboundContainerSetContentPacket): set the field if writable.
            Field itemField = PacketReflect.firstFieldOfType(packet, nmsItemClass);
            if (itemField != null) {
                Object current = itemField.get(packet);
                if (nmsItemClass.isInstance(current)) {
                    Object rewritten = itemRewriter.apply(current);
                    if (rewritten != null && rewritten != current) {
                        trySet(itemField, packet, rewritten);
                    }
                }
            }
        } catch (Throwable ignored) {
            // fall through to returning the original packet (fallback)
        }
        return packet;
    }

    private static boolean trySet(Field f, Object owner, Object value) {
        try {
            f.setAccessible(true);
            f.set(owner, value);
            return true;
        } catch (Throwable t) {
            return false; // final/immutable field on this version — leave packet unchanged
        }
    }

    @Nullable
    private static Class<?> resolveNmsItemClass() {
        Class<?> c = tryClass("net.minecraft.world.item.ItemStack");
        if (c != null) {
            return c;
        }
        String legacy = legacyNmsPackage();
        return legacy != null ? tryClass(legacy + ".ItemStack") : null;
    }

    @Nullable
    private static String legacyNmsPackage() {
        // Spigot: org.bukkit.craftbukkit.v1_8_R3 -> net.minecraft.server.v1_8_R3
        try {
            String cb = org.bukkit.Bukkit.getServer().getClass().getPackage().getName();
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

    @Nullable
    private static Class<?> tryClass(String n) {
        try {
            return Class.forName(n);
        } catch (Throwable t) {
            return null;
        }
    }
}
