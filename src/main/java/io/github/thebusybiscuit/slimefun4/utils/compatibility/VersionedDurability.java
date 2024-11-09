package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class VersionedDurability {
    private static final MinecraftVersion version = Slimefun.getMinecraftVersion();

    public static int getDurability(ItemStack itemStack) {

        if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)) {
            Damageable damageable = (Damageable) itemStack.getItemMeta();
            if (damageable == null) {
                return 0;
            }

            return damageable.getMaxDamage();
        } else {
            return itemStack.getType().getMaxDurability();
        }
    }
}
