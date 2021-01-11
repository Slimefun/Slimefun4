package io.github.thebusybiscuit.slimefun4.utils.itemstack;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

/**
 * This is just a helper {@link ItemStack} class for the {@link SlimefunGuide} {@link ItemStack}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunGuide
 * @see SlimefunGuideImplementation
 *
 */
public class SlimefunGuideItem extends ItemStack {

    public SlimefunGuideItem(@Nonnull SlimefunGuideImplementation implementation, @Nonnull String name) {
        super(Material.ENCHANTED_BOOK);

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColors.color(name));

        List<String> lore = new ArrayList<>();
        SlimefunGuideMode type = implementation.getMode();
        lore.add(type == SlimefunGuideMode.CHEAT_MODE ? ChatColors.color("&4&lOnly openable by Admins") : "");
        lore.add(ChatColors.color("&eRight Click &8\u21E8 &7Browse Items"));
        lore.add(ChatColors.color("&eShift + Right Click &8\u21E8 &7Open Settings / Credits"));

        meta.setLore(lore);

        PersistentDataAPI.setString(meta, SlimefunPlugin.getRegistry().getGuideDataKey(), type.name());
        SlimefunPlugin.getItemTextureService().setTexture(meta, "SLIMEFUN_GUIDE");

        setItemMeta(meta);
    }

}
