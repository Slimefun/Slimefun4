package io.github.thebusybiscuit.slimefun4.implementation.setup;

import java.time.Month;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.core.categories.SeasonalCategory;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
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
    protected final Category weapons = Categories.WEAPONS;
    protected final Category tools = Categories.TOOLS;
    protected final Category usefulItems = new Category(new NamespacedKey(SlimefunPlugin.instance, "items"), new CustomItem(SlimefunItems.BACKPACK_MEDIUM, "&7Useful Items"), 1);
    protected final Category food = Categories.FOOD;
    protected final Category basicMachines = Categories.BASIC_MACHINES;
    protected final Category armor = Categories.ARMOR;

    // Magical
    protected final Category magicalResources = new Category(new NamespacedKey(SlimefunPlugin.instance, "magical_items"), new CustomItem(SlimefunItems.RUNE_ENDER, "&7Magical Items"), 2);
    protected final Category magicalGadgets = new Category(new NamespacedKey(SlimefunPlugin.instance, "magical_gadgets"), new CustomItem(SlimefunItems.INFUSED_ELYTRA, "&7Magical Gadgets"), 3);
    protected final Category magicalArmor = Categories.MAGIC_ARMOR;

    // Resources and tech stuff
    protected final Category misc = Categories.MISC;
    protected final Category technicalComponents = new Category(new NamespacedKey(SlimefunPlugin.instance, "tech_misc"), new CustomItem(SlimefunItems.HEATING_COIL, "&7Technical Components"), 2);
    protected final Category technicalGadgets = new Category(new NamespacedKey(SlimefunPlugin.instance, "technical_gadgets"), new CustomItem(SlimefunItems.STEEL_JETPACK, "&7Technical Gadgets"), 3);
    protected final Category resources = new Category(new NamespacedKey(SlimefunPlugin.instance, "resources"), new CustomItem(SlimefunItems.SYNTHETIC_SAPPHIRE, "&7Resources"), 1);

    // Locked Categories
    protected final LockedCategory electricity = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance, "electricity"), new CustomItem(SlimefunItems.NUCLEAR_REACTOR, "&bEnergy and Electricity"), 4, basicMachines.getKey());
    protected final Category cargo = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance, "cargo"), new CustomItem(SlimefunItems.CARGO_MANAGER, "&cCargo Management"), 4, basicMachines.getKey());
    protected final LockedCategory gps = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance, "gps"), new CustomItem(SlimefunItems.GPS_TRANSMITTER, "&bGPS-based Machines"), 4, basicMachines.getKey());

    // Seasonal Categories
    protected final SeasonalCategory christmas = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "christmas"), Month.DECEMBER, 1, new CustomItem(SkullItem.fromHash("215ba31cde2671b8f176de6a9ffd008035f0590d63ee240be6e8921cd2037a45"), ChatUtils.christmas("Christmas") + " &7(December only)"));
    protected final SeasonalCategory valentinesDay = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "valentines_day"), Month.FEBRUARY, 2, new CustomItem(SkullItem.fromHash("55d89431d14bfef2060461b4a3565614dc51115c001fae2508e8684bc0ae6a80"), "&dValentine's Day" + " &7(14th February)"));
    protected final SeasonalCategory easter = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "easter"), Month.APRIL, 2, new CustomItem(SkullItem.fromHash("b2cd5df9d7f1fa8341fcce2f3c118e2f517e4d2d99df2c51d61d93ed7f83e13"), "&6Easter" + " &7(April)"));
    protected final SeasonalCategory birthday = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "birthday"), Month.OCTOBER, 1, new CustomItem(Material.FIREWORK_ROCKET, "&a&lTheBusyBiscuit's Birthday &7(26th October)"));
    protected final SeasonalCategory halloween = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "halloween"), Month.OCTOBER, 1, new CustomItem(Material.JACK_O_LANTERN, "&6&lHalloween &7(31st October)"));

}
