package io.github.thebusybiscuit.slimefun4.implementation.items.backpacks;

import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

public class SoulboundBackpack extends SlimefunBackpack implements Soulbound {

    public SoulboundBackpack(int size, Category category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
        super(size, category, item, type, recipe);
    }

}
