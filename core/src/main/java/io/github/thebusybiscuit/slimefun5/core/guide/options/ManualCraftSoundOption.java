package io.github.thebusybiscuit.slimefun5.core.guide.options;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;

/**
 * A per-player toggle for whether the craft sounds play when this player manually crafts at a multiblock
 * (Enhanced Crafting Table, Magic Workbench, Armor Forge) by clicking it. Ownership is irrelevant - it is
 * simply whether the crafting player hears the sounds. Defaults to the server's {@code auto-craft.sound.manual} value.
 */
class ManualCraftSoundOption implements SlimefunGuideOption<Boolean> {

    static boolean serverDefault() {
        return Slimefun.getCfg().getOrSetDefault("auto-craft.sound.manual", true);
    }

    @Override
    public SlimefunAddon getAddon() {
        return Slimefun.instance();
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(Slimefun.instance(), "manual_craft_sound");
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        boolean enabled = getSelectedOption(p, guide).orElse(serverDefault());
        String state = enabled ? "enabled" : "disabled";
        List<String> lines = Slimefun.getLocalization().getMessages(p, "guide.options.manual-craft-sound." + state + ".text");

        ItemStack item = CustomItemStack.create(XMaterial.CRAFTING_TABLE.parseMaterial(), lines.get(0),
            lines.subList(1, lines.size()).toArray(new String[0]));
        return Optional.of(item);
    }

    @Override
    public void onClick(Player p, ItemStack guide) {
        setSelectedOption(p, guide, !getSelectedOption(p, guide).orElse(serverDefault()));
        SlimefunGuideSettings.openSettings(p, guide);
    }

    @Override
    public Optional<Boolean> getSelectedOption(Player p, ItemStack guide) {
        NamespacedKey key = getKey();
        boolean value = PdcCompat.hasByte(p, key) ? PdcCompat.getByte(p, key) == (byte) 1 : serverDefault();
        return Optional.of(value);
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, Boolean value) {
        PdcCompat.setByte(p, getKey(), value.booleanValue() ? (byte) 1 : (byte) 0);
    }

}
