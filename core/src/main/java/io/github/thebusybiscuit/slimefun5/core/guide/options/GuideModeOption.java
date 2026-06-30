package io.github.thebusybiscuit.slimefun5.core.guide.options;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChatUtils;

class GuideModeOption implements SlimefunGuideOption<SlimefunGuideMode> {

    @Nonnull
    @Override
    public SlimefunAddon getAddon() {
        return Slimefun.instance();
    }

    @Nonnull
    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(Slimefun.instance(), "guide_mode");
    }

    @Nonnull
    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        if (!SlimefunGuide.canUseCheatSheet(p)) {
            // Only players allowed to use the cheat sheet (see guide.cheat-sheet.* config) get this toggle
            return Optional.empty();
        }

        Optional<SlimefunGuideMode> current = getSelectedOption(p, guide);

        if (current.isPresent()) {
            SlimefunGuideMode selectedMode = current.get();
            ItemStack item = new ItemStack(Material.AIR);

            if (selectedMode == SlimefunGuideMode.SURVIVAL_MODE) {
                item.setType(Material.CHEST);
            } else {
                item.setType(XMaterial.COMMAND_BLOCK.parseMaterial());
            }

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + Slimefun.getLocalization().getMessage(p, "guide.modes.selected") +
                    ChatColor.YELLOW + Slimefun.getLocalization().getMessage(p, "guide.modes." + selectedMode.name()));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add((selectedMode == SlimefunGuideMode.SURVIVAL_MODE ? ChatColor.GREEN : ChatColor.GRAY) + Slimefun.getLocalization().getMessage(p, "guide.modes.SURVIVAL_MODE"));
            lore.add((selectedMode == SlimefunGuideMode.CHEAT_MODE ? ChatColor.GREEN : ChatColor.GRAY) + Slimefun.getLocalization().getMessage(p, "guide.modes.CHEAT_MODE"));

            lore.add("");
            lore.add(ChatColor.GRAY + "\u21E8 " + ChatColor.YELLOW + Slimefun.getLocalization().getMessage(p, "guide.modes.change"));
            meta.setLore(lore);
            item.setItemMeta(meta);

            return Optional.of(item);
        }

        return Optional.empty();
    }

    @Override
    public void onClick(@Nonnull Player p, @Nonnull ItemStack guide) {
        Optional<SlimefunGuideMode> current = getSelectedOption(p, guide);

        if (current.isPresent()) {
            SlimefunGuideMode next = getNextMode(p, current.get());
            setSelectedOption(p, guide, next);
        }

        SlimefunGuideSettings.openSettings(p, guide);
    }

    @Nonnull
    private SlimefunGuideMode getNextMode(@Nonnull Player p, @Nonnull SlimefunGuideMode mode) {
        if (SlimefunGuide.canUseCheatSheet(p)) {
            if (mode == SlimefunGuideMode.SURVIVAL_MODE) {
                return SlimefunGuideMode.CHEAT_MODE;
            } else {
                return SlimefunGuideMode.SURVIVAL_MODE;
            }
        } else {
            return SlimefunGuideMode.SURVIVAL_MODE;
        }
    }

    @Nonnull
    @Override
    public Optional<SlimefunGuideMode> getSelectedOption(@Nonnull Player p, @Nonnull ItemStack guide) {
        // Detect via the language-independent PDC tag; isItemSimilar fails on a translated guide,
        // which made the cheat sheet show up as the survival guide in its own settings menu.
        SlimefunGuideMode mode = SlimefunGuide.getGuideMode(guide);
        return Optional.of(mode != null ? mode : SlimefunGuideMode.SURVIVAL_MODE);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setSelectedOption(Player p, ItemStack guide, SlimefunGuideMode value) {
        guide.setItemMeta(SlimefunGuide.getItem(value).getItemMeta());
    }

}

