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
 * A per-player toggle for the guide's main-menu layout: the reworked themed (categorized) view, or the
 * classic flat list of every category directly. A player who hasn't chosen inherits the server's
 * {@code guide.categorize-main-menu} default. Only affects the main menu's top level - themes still work.
 */
class MainMenuLayoutOption implements SlimefunGuideOption<Boolean> {

    @Override
    public SlimefunAddon getAddon() {
        return Slimefun.instance();
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(Slimefun.instance(), "categorize_main_menu");
    }

    /** The server-configured default that a player who hasn't toggled the option inherits. */
    static boolean serverDefault() {
        return Slimefun.getCfg().getOrSetDefault("guide.categorize-main-menu", true);
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        boolean categorized = getSelectedOption(p, guide).orElse(serverDefault());
        String state = categorized ? "categorized" : "classic";
        List<String> lines = Slimefun.getLocalization().getMessages(p, "guide.options.main-menu-layout." + state + ".text");

        ItemStack item = CustomItemStack.create(XMaterial.CHEST.parseMaterial(), lines.get(0),
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
