package io.github.thebusybiscuit.slimefun5.core.guide.options;

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
 * A per-player toggle for detailed item descriptions on physical items in the player's inventory.
 * Default on; players who already know the items can switch the extra lore off. The guide item-view
 * always shows descriptions regardless of this toggle.
 */
class ItemDescriptionsOption implements SlimefunGuideOption<Boolean> {

    private static final NamespacedKey KEY = new NamespacedKey(Slimefun.instance(), "item_descriptions");

    @Override
    public SlimefunAddon getAddon() {
        return Slimefun.instance();
    }

    @Override
    public NamespacedKey getKey() {
        return KEY;
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        boolean enabled = getSelectedOption(p, guide).orElse(true);

        ItemStack item = CustomItemStack.create(XMaterial.WRITABLE_BOOK.parseMaterial(),
            Slimefun.getLocalization().getMessage(p, enabled ? "guide.item-descriptions.enabled" : "guide.item-descriptions.disabled"),
            "",
            Slimefun.getLocalization().getMessage(p, "guide.item-descriptions.lore1"),
            Slimefun.getLocalization().getMessage(p, "guide.item-descriptions.lore2"),
            "",
            Slimefun.getLocalization().getMessage(p, enabled ? "guide.item-descriptions.click-disable" : "guide.item-descriptions.click-enable"));

        return Optional.of(item);
    }

    @Override
    public void onClick(Player p, ItemStack guide) {
        setSelectedOption(p, guide, !getSelectedOption(p, guide).orElse(true));
        // Apply immediately so the player sees their inventory items change now, not on the next sweep.
        Slimefun.getItemTranslationService().retranslateInventory(p);
        SlimefunGuideSettings.openSettings(p, guide);
    }

    @Override
    public Optional<Boolean> getSelectedOption(Player p, ItemStack guide) {
        boolean value = !PdcCompat.hasByte(p, KEY) || PdcCompat.getByte(p, KEY) == (byte) 1;
        return Optional.of(value);
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, Boolean value) {
        PdcCompat.setByte(p, KEY, value.booleanValue() ? (byte) 1 : (byte) 0);
    }

    /** Whether the player currently wants descriptions baked onto physical items (default true). */
    public static boolean isEnabledFor(Player p) {
        return !PdcCompat.hasByte(p, KEY) || PdcCompat.getByte(p, KEY) == (byte) 1;
    }
}
