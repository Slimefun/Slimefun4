package io.github.thebusybiscuit.slimefun4.utils.itemstack;

import io.github.bakedlibs.dough.items.ItemStackEditor;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedItemFlag;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This simple {@link ItemStack} implementation allows us to obtain
 * a colored {@code Material.FIREWORK_STAR} {@link ItemStack} quickly.
 *
 * @author TheBusyBiscuit
 *
 */
public class ColoredFireworkStar {

    @ParametersAreNonnullByDefault
    public static ItemStack create(Color color, String name, String... lore) {
        FireworkEffect effect = FireworkEffect.builder().with(Type.BURST).withColor(color).build();
        return new ItemStackEditor(Material.FIREWORK_STAR)
                .setDisplayName(name)
                .setLore(lore)
                .addFlags(VersionedItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .andMetaConsumer(FireworkEffectMeta.class, meta -> meta.setEffect(effect))
                .create();
    }

}
