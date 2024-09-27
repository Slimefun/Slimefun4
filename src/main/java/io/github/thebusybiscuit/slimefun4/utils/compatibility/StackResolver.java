package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * This utility class provides backwards compatibility to versions of minecraft
 * regarding the creation of ItemStacks.
 *
 * @author Vaan1310
 */
public final class StackResolver {

    private StackResolver() {}

    public static ItemStack of(Material material) {
        return of(material, 1);
    }

    public static ItemStack of(Material material, int amount) {
        var version = Slimefun.getMinecraftVersion();

        if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_21)) {
            return ItemStack.of(material, amount);
        } else {
            return new ItemStack(material, amount);
        }
    }
}
