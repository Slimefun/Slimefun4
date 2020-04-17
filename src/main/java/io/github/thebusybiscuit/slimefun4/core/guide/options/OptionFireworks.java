package io.github.thebusybiscuit.slimefun4.core.guide.options;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

class OptionFireworks implements SlimefunGuideOption<Boolean> {

    @Override
    public SlimefunAddon getAddon() {
        return SlimefunPlugin.instance;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(SlimefunPlugin.instance, "research_fireworks");
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        if (SlimefunPlugin.getRegistry().isResearchFireworkEnabled()) {
            boolean enabled = getSelectedOption(p, guide).orElse(true);
            ItemStack item = new CustomItem(Material.FIREWORK_ROCKET, "&b烟花: &" + (enabled ? "a启用" : "4禁用"), "", "&7You can now toggle whether you", "&7will be presented with a big firework", "&7upon researching an item.", "", "&7\u21E8 &eClick to " + (enabled ? "disable" : "enable") + " your fireworks");
            return Optional.of(item);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void onClick(Player p, ItemStack guide) {
        setSelectedOption(p, guide, !getSelectedOption(p, guide).orElse(true));
        SlimefunGuideSettings.openSettings(p, guide);
    }

    @Override
    public Optional<Boolean> getSelectedOption(Player p, ItemStack guide) {
        NamespacedKey key = getKey();
        boolean value = !PersistentDataAPI.hasByte(p, key) || PersistentDataAPI.getByte(p, key) == (byte) 1;
        return Optional.of(value);
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, Boolean value) {
        PersistentDataAPI.setByte(p, getKey(), value ? (byte) 1 : (byte) 0);
    }

}