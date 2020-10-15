package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.categories.FlexCategory;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentGuide;

@Deprecated
class EmeraldEnchantsCategory extends FlexCategory {

    public EmeraldEnchantsCategory(@Nonnull NamespacedKey key) {
        super(key, new CustomItem(Material.ENCHANTED_BOOK, "&2EmeraldEnchants &a(Enchantment Guide)"), 2);
    }

    @Override
    public void open(Player p, PlayerProfile profile, SlimefunGuideLayout layout) {
        EnchantmentGuide.open(p);
    }

    @Override
    public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideLayout layout) {
        return true;
    }

}
