package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun5.core.attributes.ProtectiveArmor;
import io.github.thebusybiscuit.slimefun5.implementation.items.armor.SlimefunArmorPiece;

class MockBeeProtectionSuit extends SlimefunArmorPiece implements ProtectiveArmor {

    public MockBeeProtectionSuit(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[9], new PotionEffect[0]);
    }

    @Override
    public ProtectionType[] getProtectionTypes() {
        return new ProtectionType[] { ProtectionType.BEES };
    }

    @Override
    public boolean isFullSetRequired() {
        return false;
    }

    @Override
    public NamespacedKey getArmorSetId() {
        return new NamespacedKey(getAddon().getJavaPlugin(), "mock_bees");
    }

}

