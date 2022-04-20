package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Simple interface to map ore blocks to their respective item(s).
 *
 * @author TheBusyBiscuit
 */
interface OreDictionary {

    static @Nonnull
    OreDictionary forVersion(@Nonnull MinecraftVersion version) {
        if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            // MC 1.17 - 1.18
            return new OreDictionary17();
        } else if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            // MC 1.16
            return new OreDictionary16();
        } else {
            // MC 1.14 - 1.15
            return new OreDictionary14();
        }
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    ItemStack getDrops(Material material, Random random);
}
