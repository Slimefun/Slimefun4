package io.github.thebusybiscuit.slimefun4.implementation.setup;

import java.time.Month;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.core.categories.SeasonalCategory;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;

/**
 * This class holds a reference to every {@link Category}
 * found in Slimefun itself.
 * 
 * Addons should use their own {@link Category} hence why the visible of this class was now
 * changed to package-private.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Category
 * @see LockedCategory
 * @see SeasonalCategory
 *
 */
final class DefaultCategories {

    // Standard Categories
    final Category weapons = me.mrCookieSlime.Slimefun.Lists.Categories.WEAPONS;
    final Category tools = me.mrCookieSlime.Slimefun.Lists.Categories.TOOLS;
    final Category usefulItems = me.mrCookieSlime.Slimefun.Lists.Categories.PORTABLE;
    final Category food = me.mrCookieSlime.Slimefun.Lists.Categories.FOOD;
    final Category basicMachines = me.mrCookieSlime.Slimefun.Lists.Categories.BASIC_MACHINES;
    final Category armor = me.mrCookieSlime.Slimefun.Lists.Categories.ARMOR;
    final Category magicalResources = me.mrCookieSlime.Slimefun.Lists.Categories.LUMPS_AND_MAGIC;
    final Category magicalGadgets = me.mrCookieSlime.Slimefun.Lists.Categories.MAGIC;
    final Category misc = me.mrCookieSlime.Slimefun.Lists.Categories.MISC;
    final Category technicalGadgets = me.mrCookieSlime.Slimefun.Lists.Categories.TECH;
    final Category resources = me.mrCookieSlime.Slimefun.Lists.Categories.RESOURCES;
    final Category cargo = me.mrCookieSlime.Slimefun.Lists.Categories.CARGO;
    final Category technicalComponents = me.mrCookieSlime.Slimefun.Lists.Categories.TECH_MISC;
    final Category magicalArmor = me.mrCookieSlime.Slimefun.Lists.Categories.MAGIC_ARMOR;

    // Locked Categories
    final LockedCategory electricity = me.mrCookieSlime.Slimefun.Lists.Categories.ELECTRICITY;
    final LockedCategory gps = me.mrCookieSlime.Slimefun.Lists.Categories.GPS;

    // Seasonal Categories
    final SeasonalCategory christmas = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "christmas"), Month.DECEMBER, 1, new CustomItem(SkullItem.fromHash("215ba31cde2671b8f176de6a9ffd008035f0590d63ee240be6e8921cd2037a45"), ChatUtils.christmas("Christmas") + " &7(December only)"));
    final SeasonalCategory valentinesDay = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "valentines_day"), Month.FEBRUARY, 2, new CustomItem(SkullItem.fromHash("55d89431d14bfef2060461b4a3565614dc51115c001fae2508e8684bc0ae6a80"), "&dValentine's Day" + " &7(14th February)"));
    final SeasonalCategory easter = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "easter"), Month.APRIL, 2, new CustomItem(SkullItem.fromHash("b2cd5df9d7f1fa8341fcce2f3c118e2f517e4d2d99df2c51d61d93ed7f83e13"), "&6Easter" + " &7(April)"));
    final SeasonalCategory birthday = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "birthday"), Month.OCTOBER, 1, new CustomItem(Material.FIREWORK_ROCKET, "&a&lTheBusyBiscuit's Birthday &7(26th October)"));
    final SeasonalCategory halloween = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "halloween"), Month.OCTOBER, 1, new CustomItem(Material.JACK_O_LANTERN, "&6&lHalloween &7(31st October)"));

}
