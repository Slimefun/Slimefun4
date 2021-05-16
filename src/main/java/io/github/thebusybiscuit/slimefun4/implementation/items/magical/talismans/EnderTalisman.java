package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.block.EnderChest;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.categories.LockedCategory;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * An {@link EnderTalisman} is a special version of {@link Talisman}
 * that works while it is in your {@link EnderChest}.
 * 
 * @author TheBusyBiscuit
 *
 */
class EnderTalisman extends Talisman {

    private static final LockedCategory ENDER_TALISMANS_CATEGORY = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance(), "ender_talismans"), new CustomItem(SlimefunItems.ENDER_TALISMAN, "&7Talismans - &aTier II"), 3, Talisman.TALISMANS_CATEGORY.getKey());

    @ParametersAreNonnullByDefault
    public EnderTalisman(Talisman parent, SlimefunItemStack item) {
        super(ENDER_TALISMANS_CATEGORY, item, new ItemStack[] { SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, null, parent.getItem(), null, SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3 }, parent.isConsumable(), parent.isEventCancelled(), parent.getMessageSuffix(), parent.getChance(), parent.getEffects());
    }

    @Override
    void loadEnderTalisman() {
        /*
         * Let's override that, otherwise we would be creating Ender Talismans
         * for every Ender Talisman too.
         */
    }

    @Override
    public void postRegister() {
        /*
         * Let's override that, otherwise we would be creating Ender Talismans
         * for every Ender Talisman too.
         */
    }
}
