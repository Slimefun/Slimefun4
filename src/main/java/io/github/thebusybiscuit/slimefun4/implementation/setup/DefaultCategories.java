package io.github.thebusybiscuit.slimefun4.implementation.setup;

import java.time.Month;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.categories.LockedCategory;
import io.github.thebusybiscuit.slimefun4.core.categories.SeasonalCategory;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;

/**
 * This class holds a reference to every {@link Category}
 * found in Slimefun itself.
 * 
 * Addons should use their own {@link Category} hence why the visible of this class was now
 * changed to package-private. Only {@link SlimefunItemSetup} has access to this class.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Category
 * @see LockedCategory
 * @see SeasonalCategory
 *
 */
class DefaultCategories {

    // Standard Categories
    protected final Category weapons = new Category(new NamespacedKey(SlimefunPlugin.instance(), "weapons"), new CustomItem(SlimefunItems.BLADE_OF_VAMPIRES, "&7Weapons"), 1);
    protected final Category tools = new Category(new NamespacedKey(SlimefunPlugin.instance(), "tools"), new CustomItem(SlimefunItems.SMELTERS_PICKAXE, "&7Tools"), 1);
    protected final Category usefulItems = new Category(new NamespacedKey(SlimefunPlugin.instance(), "items"), new CustomItem(SlimefunItems.BACKPACK_MEDIUM, "&7Useful Items"), 1);
    protected final Category basicMachines = new Category(new NamespacedKey(SlimefunPlugin.instance(), "basic_machines"), new CustomItem(SlimefunItems.ENHANCED_CRAFTING_TABLE, "&7Basic Machines"), 1);
    protected final Category food = new Category(new NamespacedKey(SlimefunPlugin.instance(), "food"), new CustomItem(SlimefunItems.FORTUNE_COOKIE, "&7Food"), 2);
    protected final Category armor = new Category(new NamespacedKey(SlimefunPlugin.instance(), "armor"), new CustomItem(SlimefunItems.DAMASCUS_STEEL_CHESTPLATE, "&7Armor"), 2);

    // Magical
    protected final Category magicalResources = new Category(new NamespacedKey(SlimefunPlugin.instance(), "magical_items"), new CustomItem(SlimefunItems.ENDER_RUNE, "&7Magical Items"), 2);
    protected final Category magicalGadgets = new Category(new NamespacedKey(SlimefunPlugin.instance(), "magical_gadgets"), new CustomItem(SlimefunItems.INFUSED_ELYTRA, "&7Magical Gadgets"), 3);
    protected final Category magicalArmor = new Category(new NamespacedKey(SlimefunPlugin.instance(), "magical_armor"), new CustomItem(SlimefunItems.ENDER_HELMET, "&7Magical Armor"), 2);

    // Resources and tech stuff
    protected final Category misc = new Category(new NamespacedKey(SlimefunPlugin.instance(), "misc"), new CustomItem(SlimefunItems.TIN_CAN, "&7Miscellaneous"), 2);
    protected final Category technicalComponents = new Category(new NamespacedKey(SlimefunPlugin.instance(), "tech_misc"), new CustomItem(SlimefunItems.HEATING_COIL, "&7Technical Components"), 2);
    protected final Category technicalGadgets = new Category(new NamespacedKey(SlimefunPlugin.instance(), "technical_gadgets"), new CustomItem(SlimefunItems.STEEL_JETPACK, "&7Technical Gadgets"), 3);
    protected final Category resources = new Category(new NamespacedKey(SlimefunPlugin.instance(), "resources"), new CustomItem(SlimefunItems.SYNTHETIC_SAPPHIRE, "&7Resources"), 1);

    // Locked Categories
    protected final LockedCategory electricity = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance(), "electricity"), new CustomItem(SlimefunItems.NUCLEAR_REACTOR, "&bEnergy and Electricity"), 4, basicMachines.getKey());
    protected final LockedCategory androids = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance(), "androids"), new CustomItem(SlimefunItems.PROGRAMMABLE_ANDROID, "&cProgrammable Androids"), 4, basicMachines.getKey());
    protected final Category cargo = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance(), "cargo"), new CustomItem(SlimefunItems.CARGO_MANAGER, "&cCargo Management"), 4, basicMachines.getKey());
    protected final LockedCategory gps = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance(), "gps"), new CustomItem(SlimefunItems.GPS_TRANSMITTER, "&bGPS-based Machines"), 4, basicMachines.getKey());

    // Seasonal Categories
    protected final SeasonalCategory christmas = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance(), "christmas"), Month.DECEMBER, 1, new CustomItem(SlimefunUtils.getCustomHead("215ba31cde2671b8f176de6a9ffd008035f0590d63ee240be6e8921cd2037a45"), ChatUtils.christmas("Christmas") + " &7(December only)"));
    protected final SeasonalCategory valentinesDay = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance(), "valentines_day"), Month.FEBRUARY, 2, new CustomItem(SlimefunUtils.getCustomHead("55d89431d14bfef2060461b4a3565614dc51115c001fae2508e8684bc0ae6a80"), "&dValentine's Day" + " &7(14th February)"));
    protected final SeasonalCategory easter = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance(), "easter"), Month.APRIL, 2, new CustomItem(HeadTexture.EASTER_EGG.getAsItemStack(), "&6Easter" + " &7(April)"));
    protected final SeasonalCategory birthday = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance(), "birthday"), Month.OCTOBER, 1, new CustomItem(Material.FIREWORK_ROCKET, "&a&lTheBusyBiscuit's Birthday &7(26th October)"));
    protected final SeasonalCategory halloween = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance(), "halloween"), Month.OCTOBER, 1, new CustomItem(Material.JACK_O_LANTERN, "&6&lHalloween &7(31st October)"));

}
