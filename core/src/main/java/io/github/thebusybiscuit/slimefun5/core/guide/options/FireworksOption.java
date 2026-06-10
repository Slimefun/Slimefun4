package io.github.thebusybiscuit.slimefun5.core.guide.options;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;

import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.core.SlimefunRegistry;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

class FireworksOption implements SlimefunGuideOption<Boolean> {

    @Override
    public SlimefunAddon getAddon() {
        return Slimefun.instance();
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(Slimefun.instance(), "research_fireworks");
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        SlimefunRegistry registry = Slimefun.getRegistry();

        if (registry.isResearchingEnabled() && registry.isResearchFireworkEnabled()) {
            boolean enabled = getSelectedOption(p, guide).orElse(true);

            String optionState = enabled ? "enabled" : "disabled";
            List<String> lore = Slimefun.getLocalization().getMessages(p, "guide.options.fireworks." + optionState + ".text");
            lore.add("");
            lore.add("&7\u21E8 " + Slimefun.getLocalization().getMessage(p, "guide.options.fireworks." + optionState + ".click"));

            ItemStack item = CustomItemStack.create(XMaterial.FIREWORK_ROCKET.parseMaterial(), lore);
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
        boolean value = !PersistentDataAPI.hasByte(PdcCompat.holder(p), key) || PersistentDataAPI.getByte(PdcCompat.holder(p), key) == (byte) 1;
        return Optional.of(value);
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, Boolean value) {
        PersistentDataAPI.setByte(PdcCompat.holder(p), getKey(), value.booleanValue() ? (byte) 1 : (byte) 0);
    }

}

