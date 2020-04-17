package me.mrCookieSlime.Slimefun.Lists;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.categories.SeasonalCategory;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;

/**
 * This class holds a static references to every {@link Category}
 * found in Slimefun.
 * 
 * @deprecated This class is no longer available to addons. Please use your own {@link Category} instead.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Category
 * @see LockedCategory
 * @see SeasonalCategory
 *
 */
@Deprecated
public final class Categories {

    private Categories() {}

    // Standard Categories
    public static final Category WEAPONS = new Category(new NamespacedKey(SlimefunPlugin.instance, "weapons"), new CustomItem(SlimefunItems.BLADE_OF_VAMPIRES, "&7Weapons"), 1);
    public static final Category TOOLS = new Category(new NamespacedKey(SlimefunPlugin.instance, "tools"), new CustomItem(SlimefunItems.AUTO_SMELT_PICKAXE, "&7Tools"), 1);
    public static final Category FOOD = new Category(new NamespacedKey(SlimefunPlugin.instance, "food"), new CustomItem(SlimefunItems.FORTUNE_COOKIE, "&7Food"), 2);
    public static final Category BASIC_MACHINES = new Category(new NamespacedKey(SlimefunPlugin.instance, "basic_machines"), new CustomItem(Material.CRAFTING_TABLE, "&7Basic Machines"), 1);
    public static final Category ARMOR = new Category(new NamespacedKey(SlimefunPlugin.instance, "armor"), new CustomItem(SlimefunItems.DAMASCUS_STEEL_CHESTPLATE, "&7Armor"), 2);
    public static final Category MISC = new Category(new NamespacedKey(SlimefunPlugin.instance, "misc"), new CustomItem(SlimefunItems.CAN, "&7Miscellaneous"), 2);
    public static final Category MAGIC_ARMOR = new Category(new NamespacedKey(SlimefunPlugin.instance, "magical_armor"), new CustomItem(SlimefunItems.ENDER_HELMET, "&7Magical Armor"), 2);
}
