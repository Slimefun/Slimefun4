package io.github.thebusybiscuit.slimefun4.test.mocks;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectiveArmor;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MockHazmatSuit extends SlimefunArmorPiece implements ProtectiveArmor {

    @ParametersAreNonnullByDefault
    public MockHazmatSuit(Category category, SlimefunItemStack item) {
        super(category, item, RecipeType.NULL, new ItemStack[9], new PotionEffect[0]);
    }

    @Override
    public ProtectionType[] getProtectionTypes() {
        return new ProtectionType[] { ProtectionType.RADIATION };
    }

    @Override
    public boolean isFullSetRequired() {
        return false;
    }

    @Override
    public NamespacedKey getArmorSetId() {
        return new NamespacedKey(getAddon().getJavaPlugin(), "mock_hazmat");
    }

}
