package io.github.thebusybiscuit.slimefun4.implementation.guide;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * An extension of {@link ItemStack} that indicates the item is unlockable in the guide
 *
 * @author RobotHanzo
 */
class UnlockableItemStack extends SlimefunItemStack {
    public UnlockableItemStack(@Nonnull String id, @Nonnull ItemStack item, @Nonnull Player player) {
        super(id, item);

        ItemMeta resultMeta = this.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_RED + ChatColor.BOLD.toString() + Slimefun.getLocalization().getMessage(player, "guide.locked"));
        lore.add("");
        lore.add(ChatColors.color(Slimefun.getLocalization().getMessage(player, "guide.unlock.click")));
        lore.add("");
        lore.add(ChatColors.color(Slimefun.getLocalization().getMessage(player, "guide.unlock.cost").replace("%cost%", String.valueOf(this.getItem().getResearch().getCost()))));
        resultMeta.setLore(lore);
        this.setItemMeta(resultMeta);
        this.setType(Material.BARRIER);
    }
}
