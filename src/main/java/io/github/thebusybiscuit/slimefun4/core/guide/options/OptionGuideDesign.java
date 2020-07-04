package io.github.thebusybiscuit.slimefun4.core.guide.options;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class OptionGuideDesign implements SlimefunGuideOption<SlimefunGuideLayout> {

    @Override
    public SlimefunAddon getAddon() {
        return SlimefunPlugin.instance;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(SlimefunPlugin.instance, "guide_layout");
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        Optional<SlimefunGuideLayout> current = getSelectedOption(p, guide);

        if (current.isPresent()) {
            SlimefunGuideLayout layout = current.get();
            ItemStack item = new ItemStack(Material.AIR);

            if (layout == SlimefunGuideLayout.CHEST) {
                item.setType(Material.CHEST);
            } else if (layout == SlimefunGuideLayout.BOOK) {
                item.setType(Material.BOOK);
            } else {
                item.setType(Material.COMMAND_BLOCK);
            }

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "Slimefun 指南样式: " + ChatColor.YELLOW + ChatUtils.humanize(layout.name()));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add((layout == SlimefunGuideLayout.CHEST ? ChatColor.GREEN : ChatColor.GRAY) + "箱子界面");
            lore.add((layout == SlimefunGuideLayout.BOOK ? ChatColor.GREEN : ChatColor.GRAY) + "书与笔界面");

            if (p.hasPermission("slimefun.cheat.items")) {
                lore.add((layout == SlimefunGuideLayout.CHEAT_SHEET ? ChatColor.GREEN : ChatColor.GRAY) + "作弊界面");
            }

            lore.add("");
            lore.add(ChatColor.GRAY + "\u21E8 " + ChatColor.YELLOW + "单击修改指南样式");
            meta.setLore(lore);
            item.setItemMeta(meta);

            return Optional.of(item);
        }

        return Optional.empty();
    }

    @Override
    public void onClick(Player p, ItemStack guide) {
        Optional<SlimefunGuideLayout> current = getSelectedOption(p, guide);

        if (current.isPresent()) {
            SlimefunGuideLayout next = getNextLayout(p, current.get());
            setSelectedOption(p, guide, next);
        }

        SlimefunGuideSettings.openSettings(p, guide);
    }

    private SlimefunGuideLayout getNextLayout(Player p, SlimefunGuideLayout layout) {
        if (p.hasPermission("slimefun.cheat.items")) {
            if (layout == SlimefunGuideLayout.CHEST) {
                return SlimefunGuideLayout.BOOK;
            }

            if (layout == SlimefunGuideLayout.BOOK) {
                return SlimefunGuideLayout.CHEAT_SHEET;
            }

            return SlimefunGuideLayout.CHEST;
        } else {
            return layout == SlimefunGuideLayout.CHEST ? SlimefunGuideLayout.BOOK : SlimefunGuideLayout.CHEST;
        }
    }

    @Override
    public Optional<SlimefunGuideLayout> getSelectedOption(Player p, ItemStack guide) {
        for (SlimefunGuideLayout layout : SlimefunGuideLayout.values()) {
            if (SlimefunUtils.isItemSimilar(guide, SlimefunGuide.getItem(layout), true)) {
                return Optional.of(layout);
            }
        }

        return Optional.empty();
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, SlimefunGuideLayout value) {
        guide.setItemMeta(SlimefunGuide.getItem(value).getItemMeta());
    }

}