package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

/**
 * Java-8 universal port: a tiny reflective method invoker for calling Bukkit/Paper APIs that do not
 * exist at the 1.8.8 compile floor but are present on newer servers.
 * <p>
 * It resolves a public method by name, argument count, and assignable parameter types (so overloads with
 * the same arity are disambiguated), then invokes it. Returns {@code null} when the method is absent
 * (e.g. on legacy servers) or the call fails — callers supply a sensible default for primitive returns.
 * This preserves full behaviour on modern servers while degrading gracefully on legacy ones.
 *
 * @author Slimefun (Java-8 port)
 */
public final class ReflectionCompat {

    private ReflectionCompat() {}

    @Nullable
    public static Object invoke(@Nullable Object target, String name, Object... args) {
        if (target == null) {
            return null;
        }

        try {
            for (Method method : target.getClass().getMethods()) {
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
                    return method.invoke(target, args);
                }
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
            for (Method method : clazz.getMethods()) {
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
                    return method.invoke(null, args);
                }
            }
        } catch (Throwable ignored) {
            // Method missing on this server version or invocation failed — fall through to null.
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
