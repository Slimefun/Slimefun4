package me.mrCookieSlime.Slimefun.Lists;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.categories.LockedCategory;
import io.github.thebusybiscuit.slimefun4.core.categories.SeasonalCategory;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import org.bukkit.NamespacedKey;

/**
 * This class holds a static references to every {@link Category}
 * found in Slimefun.
 *
 * @author TheBusyBiscuit
 * @see Category
 * @see LockedCategory
 * @see SeasonalCategory
 * @deprecated This class is no longer available to addons. Please use your own {@link Category} instead.
 */
@Deprecated
public final class Categories {

    private Categories() {
    }

    // 兼容科技复兴
    public static final Category TECH_MISC = new Category(new NamespacedKey(SlimefunPlugin.instance, "tech_misc"), new CustomItem(SlimefunItems.HEATING_COIL, "&7Technical Components"), 2);
    public static final Category RESOURCES = new Category(new NamespacedKey(SlimefunPlugin.instance, "resources"), new CustomItem(SlimefunItems.SYNTHETIC_SAPPHIRE, "&7Resources"), 1);
}