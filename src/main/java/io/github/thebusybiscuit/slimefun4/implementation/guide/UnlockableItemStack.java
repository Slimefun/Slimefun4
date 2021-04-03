package io.github.thebusybiscuit.slimefun4.implementation.guide;

import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * A subclass of {@link ItemStack} that indicates the item is unlockable in the guide
 *
 * @author RobotHanzo
 */
class UnlockableItemStack extends ItemStack {

    public ItemStack itemStack;

    /**
     * Generates a {@link UnlockableItemStack} from {@link ItemStack}
     * And adds lore to it so it looks like unlockable
     * 
     * @param i
     *              The {@link ItemStack} of the original item
     * @param sfItem
     *              The {@link SlimefunItem} of the original item
     * @param p
     *              The player who is being showed this {@link UnlockableItemStack} to
     */
    public UnlockableItemStack(ItemStack i, SlimefunItem sfItem, Player p){
        ItemMeta resultMeta = i.getItemMeta();
        Research resultResearch = sfItem.getResearch();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_RED + ChatColor.BOLD.toString() + SlimefunPlugin.getLocalization().getMessage(p, "guide.locked"));
        lore.add("");
        lore.add(ChatColor.GREEN + "> Click to unlock");
        lore.add("");
        lore.add(ChatColor.GRAY + "Cost: " + ChatColor.AQUA + resultResearch.getCost() + " Level(s)");
        resultMeta.setLore(lore);
        i.setItemMeta(resultMeta);
        i.setType(Material.BARRIER);
        itemStack = i;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
