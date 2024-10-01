package io.github.thebusybiscuit.slimefun4.utils.itemstack;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
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
public class ColoredFireworkStar extends ItemStack {

    @Deprecated
    @ParametersAreNonnullByDefault
    public ColoredFireworkStar(Color color, String name, String... lore) {
        super(Material.FIREWORK_STAR);
        itemBuilder(color, name, lore).edit(this);
    }

    @ParametersAreNonnullByDefault
    private static ItemStackEditor itemBuilder(Color color, String name, String... lore) {
        FireworkEffect effect = FireworkEffect.builder().with(Type.BURST).withColor(color).build();
        return new ItemStackEditor().withColor(color)
                .withNameString(name)
                .withLoreString(lore)
                .withAdditionalFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .withMetaTransform(FireworkEffectMeta.class, meta -> meta.setEffect(effect));
    }

    @ParametersAreNonnullByDefault
    public static ItemStack createItem(Color color, String name, String... lore) {
        return itemBuilder(color, name, lore).createAs(Material.FIREWORK_STAR);
    }

}
