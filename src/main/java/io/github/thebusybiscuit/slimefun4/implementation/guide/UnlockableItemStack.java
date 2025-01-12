package io.github.thebusybiscuit.slimefun4.implementation.guide;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.services.localization.SlimefunLocalization;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * An extension of {@link SlimefunItemStack} that indicates the item is unlockable in the guide.
 * This is only used by {@link SurvivalSlimefunGuide}.
 *
 * @author RobotHanzo
 * 
 * @see SurvivalSlimefunGuide
 */
public class UnlockableItemStack extends SlimefunItemStack {

    @ParametersAreNonnullByDefault
    UnlockableItemStack(SlimefunItem item, Player p) {
        super(item.getId(), item.getItem());

        SlimefunLocalization locale = Slimefun.getLocalization();
        ItemMeta meta = getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add(ChatColors.color("&4&l") + locale.getMessage(p, "guide.locked"));
        lore.add("");
        lore.add(ChatColors.color(locale.getMessage(p, "guide.unlock.click")));
        lore.add("");
        lore.add(ChatColors.color(locale.getMessage(p, "guide.unlock.cost").replace("%cost%", String.valueOf(item.getResearch().getCost()))));
        meta.setLore(lore);

        setItemMeta(meta);
        setType(Material.BARRIER);
    }
}
