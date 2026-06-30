package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

/**
 * Java-8 universal port: a tiny reflective method invoker for calling Bukkit/Paper APIs that do not
 * exist at the 1.8.8 compile floor but are present on newer servers.
 * <p>
 * It resolves a method by name, argument count, and assignable parameter types (so overloads with the
 * same arity are disambiguated), then invokes it. Crucially, the matched method is re-resolved against a
 * <em>public</em> supertype/interface before invocation: server objects are frequently instances of
 * non-public {@code org.bukkit.craftbukkit.*} implementation classes (e.g. {@code CraftMetaItem},
 * {@code CraftPersistentDataContainer}), and invoking a {@link Method} whose declaring class is non-public
 * throws {@link IllegalAccessException}. Resolving the same signature on a public interface (e.g.
 * {@code PersistentDataHolder}, {@code PersistentDataContainer}) yields an invocable handle.
 * <p>
 * Returns {@code null} when the method is absent (e.g. on legacy servers) or the call fails — callers
 * supply a sensible default for primitive returns. This preserves full behaviour on modern servers while
 * degrading gracefully on legacy ones.
 *
 * @author Slimefun (Java-8 port)
 */
public final class ReflectionCompat {

    private ReflectionCompat() {}

    // Resolving a method means scanning getMethods() (O(n)) plus a public-supertype walk. These calls
    // sit on hot paths (per-tick, per-event), so cache the resolved handle by (class, name, arg types).
    // A sentinel marks "no such method" so absent APIs aren't re-scanned every call on legacy servers.
    private static final ConcurrentHashMap<String, Method> RESOLVE_CACHE = new ConcurrentHashMap<>();
    private static final Method MISSING = missingSentinel();

    private static Method missingSentinel() {
        try {
            return Object.class.getMethod("toString");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Nullable
    public static Object invoke(@Nullable Object target, String name, Object... args) {
        if (target == null) {
            return null;
        }

        try {
            Method method = resolve(target.getClass(), name, args);

            if (method != null) {
                return method.invoke(target, args);
            }
        } catch (Throwable ignored) {
            // Method missing on this server version or invocation failed — fall through to null.
        }

        return null;
    }

    /**
     * Invokes a {@code public static} method on {@code clazz} by name + arity + assignable param types.
     * Returns {@code null} when the method is absent (e.g. on legacy servers) or the call fails.
     */
    @Nullable
    public static Object invokeStatic(Class<?> clazz, String name, Object... args) {
        try {
            Method method = resolve(clazz, name, args);

            if (method != null) {
                return method.invoke(null, args);
            }
        } catch (Throwable ignored) {
            // Method missing on this server version or invocation failed — fall through to null.
        }

        return null;
    }

    @Nullable
    private static Method resolve(Class<?> type, String name, Object[] args) {
        String key = cacheKey(type, name, args);
        Method cached = RESOLVE_CACHE.get(key);

        if (cached != null) {
            return cached == MISSING ? null : cached;
        }

        Method resolved = resolveUncached(type, name, args);
        RESOLVE_CACHE.put(key, resolved == null ? MISSING : resolved);
        return resolved;
    }

    private static String cacheKey(Class<?> type, String name, Object[] args) {
        StringBuilder builder = new StringBuilder(type.getName()).append('#').append(name).append('/').append(args.length);

        for (Object arg : args) {
            builder.append(';').append(arg == null ? "null" : arg.getClass().getName());
        }

        return builder.toString();
    }

    @Nullable
    private static Method resolveUncached(Class<?> type, String name, Object[] args) {
        for (Method method : type.getMethods()) {
            if (!method.getName().equals(name) || method.getParameterCount() != args.length) {
                continue;
            }

            Class<?>[] paramTypes = method.getParameterTypes();
            boolean matches = true;

            for (int i = 0; i < args.length; i++) {
                if (args[i] != null && !box(paramTypes[i]).isAssignableFrom(args[i].getClass())) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                return invocable(method, paramTypes);
            }
        }

        return null;
    }

    /**
     * Ensures the matched method can actually be invoked. If its declaring class is non-public (a
     * craftbukkit implementation type), the same signature is looked up on a public supertype/interface;
     * forcing access is the last resort.
     */
    private static Method invocable(Method method, Class<?>[] paramTypes) {
        if (Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            return method;
        }

        Method publicMethod = searchPublic(method.getDeclaringClass(), method.getName(), paramTypes);

        if (publicMethod != null) {
            return publicMethod;
        }

        try {
            method.setAccessible(true);
        } catch (Throwable ignored) {
            // Strong encapsulation may forbid this — invocation will then fail and the caller gets null.
        }

        return method;
    }

    @Nullable
    private static Method searchPublic(Class<?> type, String name, Class<?>[] paramTypes) {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            if (Modifier.isPublic(current.getModifiers())) {
                try {
                    Method candidate = current.getMethod(name, paramTypes);

                    if (Modifier.isPublic(candidate.getDeclaringClass().getModifiers())) {
                        return candidate;
                    }
                } catch (NoSuchMethodException ignored) {
                    // Not declared here — keep walking.
                }
            }

            for (Class<?> iface : current.getInterfaces()) {
                Method candidate = searchPublic(iface, name, paramTypes);

                if (candidate != null) {
                    return candidate;
                }
            }
        }

        return null;
    }

    private static Class<?> box(Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        }

        if (type == int.class) {
            return Integer.class;
        } else if (type == boolean.class) {
            return Boolean.class;
        } else if (type == long.class) {
            return Long.class;
        } else if (type == double.class) {
            return Double.class;
        } else if (type == float.class) {
            return Float.class;
        } else if (type == short.class) {
            return Short.class;
        } else if (type == byte.class) {
            return Byte.class;
        } else if (type == char.class) {
            return Character.class;
        }

        return type;
    }
}
