package io.github.thebusybiscuit.slimefun4.implementation.setup;

import java.time.LocalDate;
import java.time.Month;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;

/**
 * A super ordinary class.
 *
 * @author TheBusyBiscuit
 *
 */
class RickFlexGroup extends FlexItemGroup {

    // Never instantiate more than once.
    RickFlexGroup(@Nonnull NamespacedKey key) {
        super(key, CustomItemStack.create(Material.NETHER_STAR, "&6&lSuper secret items"), 1);
    }

    // Gonna override this method
    @Override
    public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode layout) {
        // Give me the current date
        LocalDate date = LocalDate.now();

        // You can see where this is going
        return date.getMonth() == Month.APRIL && date.getDayOfMonth() == 1;
    }

    @Override
    public void open(Player p, PlayerProfile profile, SlimefunGuideMode layout) {
        // Up the game with this easter egg
        ChatUtils.sendURL(p, "https://youtu.be/dQw4w9WgXcQ");
        p.closeInventory();
    }

}
