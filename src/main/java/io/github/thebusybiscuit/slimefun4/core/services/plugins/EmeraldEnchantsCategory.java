package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.categories.FlexCategory;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentGuide;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

class EmeraldEnchantsCategory extends FlexCategory {

    public EmeraldEnchantsCategory(NamespacedKey key) {
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