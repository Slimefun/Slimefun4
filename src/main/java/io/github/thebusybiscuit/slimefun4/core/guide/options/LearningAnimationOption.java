package io.github.thebusybiscuit.slimefun4.core.guide.options;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

public class LearningAnimationOption implements SlimefunGuideOption<Boolean> {

    @Nonnull
    @Override
    public SlimefunAddon getAddon() {
        return SlimefunPlugin.instance();
    }

    @Nonnull
    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(SlimefunPlugin.instance(), "research_learning_animation");
    }

    @Nonnull
    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        if (SlimefunPlugin.getRegistry().isLearningAnimationDisabled()) {
            return Optional.empty();
        } else {
            boolean enabled = getSelectedOption(p, guide).orElse(true);
            ItemStack item = new CustomItem(Material.PAPER, "&bLearning Animation: &" + (enabled ? "aYes" : "4No"), "", "&7You can now toggle whether you", "&7will see information about your pondering in chat", "&7upon researching an item.", "", "&7\u21E8 &eClick to " + (enabled ? "disable" : "enable") + " your learning animation");
            return Optional.of(item);
        }
    }

    @Override
    public void onClick(@Nonnull Player p, @Nonnull ItemStack guide) {
        setSelectedOption(p, guide, !getSelectedOption(p, guide).orElse(true));
        SlimefunGuideSettings.openSettings(p, guide);
    }

    @Override
    public Optional<Boolean> getSelectedOption(@Nonnull Player p, @Nonnull ItemStack guide) {
        NamespacedKey key = getKey();
        boolean value = !PersistentDataAPI.hasByte(p, key) || PersistentDataAPI.getByte(p, key) == (byte) 1;
        return Optional.of(value);
    }

    @Override
    public void setSelectedOption(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull Boolean value) {
        PersistentDataAPI.setByte(p, getKey(), (byte) (value ? 1 : 0));
    }

}
