package io.github.thebusybiscuit.slimefun5.utils.itemstack;

import io.github.bakedlibs.dough.items.ItemStackEditor;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedItemFlag;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This simple {@link ItemStack} implementation allows us to obtain
 * a colored {@code XMaterial.FIREWORK_STAR.parseMaterial()} {@link ItemStack} quickly.
 *
 * @author TheBusyBiscuit
 *
 */
public class ColoredFireworkStar {

    /** Colour-only variant: display name/lore come from languages/en/items.yml (resource-driven). */
    @ParametersAreNonnullByDefault
    public static ItemStack create(Color color) {
        FireworkEffect effect = FireworkEffect.builder().with(Type.BURST).withColor(color).build();
        return new ItemStackEditor(XMaterial.FIREWORK_STAR.parseMaterial())
                .andMetaConsumer(meta -> VersionedItemFlag.addFlags(meta, VersionedItemFlag.HIDE_ADDITIONAL_TOOLTIP))
                .andMetaConsumer(FireworkEffectMeta.class, meta -> meta.setEffect(effect))
                .create();
    }

    @ParametersAreNonnullByDefault
    public static ItemStack create(Color color, String name, String... lore) {
        FireworkEffect effect = FireworkEffect.builder().with(Type.BURST).withColor(color).build();
        return new ItemStackEditor(XMaterial.FIREWORK_STAR.parseMaterial())
                .setDisplayName(name)
                .setLore(lore)
                .andMetaConsumer(meta -> VersionedItemFlag.addFlags(meta, VersionedItemFlag.HIDE_ADDITIONAL_TOOLTIP))
                .andMetaConsumer(FireworkEffectMeta.class, meta -> meta.setEffect(effect))
                .create();
    }

}

