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
            String enabled = getSelectedOption(p, guide).orElse(true) ? "enabled" : "disabled";
            ItemStack item = new CustomItem(Material.PAPER, SlimefunPlugin.getLocalization().getMessages(
                    p, "guide.options.learning-animation." + enabled));
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
