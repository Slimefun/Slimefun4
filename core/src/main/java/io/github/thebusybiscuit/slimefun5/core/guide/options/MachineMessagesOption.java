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
 * A per-player toggle for the chat feedback Slimefun machines send - the "successfully constructed"
 * multiblock tip and the "Assembled: ..." message. Players who know the mechanics can switch them off.
 */
class MachineMessagesOption implements SlimefunGuideOption<Boolean> {

    @Override
    public SlimefunAddon getAddon() {
        return Slimefun.instance();
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(Slimefun.instance(), "machine_messages");
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        boolean enabled = getSelectedOption(p, guide).orElse(true);
        String state = enabled ? "enabled" : "disabled";
        List<String> lines = Slimefun.getLocalization().getMessages(p, "guide.options.machine-messages." + state + ".text");

        ItemStack item = CustomItemStack.create(XMaterial.OAK_SIGN.parseMaterial(), lines.get(0),
            lines.subList(1, lines.size()).toArray(new String[0]));
        return Optional.of(item);
    }

    @Override
    public void onClick(Player p, ItemStack guide) {
        setSelectedOption(p, guide, !getSelectedOption(p, guide).orElse(true));
        SlimefunGuideSettings.openSettings(p, guide);
    }

    @Override
    public Optional<Boolean> getSelectedOption(Player p, ItemStack guide) {
        NamespacedKey key = getKey();
        boolean value = !PdcCompat.hasByte(p, key) || PdcCompat.getByte(p, key) == (byte) 1;
        return Optional.of(value);
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, Boolean value) {
        PdcCompat.setByte(p, getKey(), value.booleanValue() ? (byte) 1 : (byte) 0);
    }

}
