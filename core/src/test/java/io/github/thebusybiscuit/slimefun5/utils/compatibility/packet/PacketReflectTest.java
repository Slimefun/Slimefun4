package io.github.thebusybiscuit.slimefun5.utils.compatibility.packet;

import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Regression test for the 26.2 overload mis-bind: {@code CraftItemStack} has two one-arg
 * {@code asNMSCopy} overloads ({@code ItemStack} and {@code List}), and matching by name+arity alone
 * (no parameter-type check) can bind to whichever {@code getMethods()} returns first, which is JVM/version
 * dependent. {@link PacketReflect#resolveByParamType} must always pick the overload whose parameter type
 * matches the requested argument type, regardless of declaration order.
 */
class PacketReflectTest {

    static class Fixture {
        public static void foo(List<?> x) {}

        public static void foo(ItemStack x) {}
    }

    @Test
    void picksItemStackOverloadForItemStackArgType() {
        Method m = PacketReflect.resolveByParamType(Fixture.class, "foo", ItemStack.class);
        Assertions.assertNotNull(m);
        Assertions.assertEquals(ItemStack.class, m.getParameterTypes()[0]);
    }

    @Test
    void picksListOverloadForListArgType() {
        Method m = PacketReflect.resolveByParamType(Fixture.class, "foo", List.class);
        Assertions.assertNotNull(m);
        Assertions.assertEquals(List.class, m.getParameterTypes()[0]);
    }

    @Test
    void returnsNullWhenNoOverloadMatchesTheArgType() {
        Method m = PacketReflect.resolveByParamType(Fixture.class, "foo", String.class);
        Assertions.assertNull(m);
    }

    @Test
    void returnsNullForUnknownMethodName() {
        Method m = PacketReflect.resolveByParamType(Fixture.class, "bar", ItemStack.class);
        Assertions.assertNull(m);
    }
}
