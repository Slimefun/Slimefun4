package me.mrCookieSlime.Slimefun.Lists;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.SeasonalCategory;

/**
 * This class holds a static references to every {@link Category}
 * found in Slimefun.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Category
 * @see LockedCategory
 * @see SeasonalCategory
 *
 */
public final class Categories {

    private Categories() {}

    // Standard Categories
    public static final Category WEAPONS = new Category(new NamespacedKey(SlimefunPlugin.instance, "weapons"), new CustomItem(SlimefunItems.BLADE_OF_VAMPIRES, "&7Weapons"), 1);
    public static final Category TOOLS = new Category(new NamespacedKey(SlimefunPlugin.instance, "tools"), new CustomItem(SlimefunItems.AUTO_SMELT_PICKAXE, "&7Tools"), 1);
    public static final Category PORTABLE = new Category(new NamespacedKey(SlimefunPlugin.instance, "items"), new CustomItem(SlimefunItems.BACKPACK_MEDIUM, "&7Useful Items"), 1);
    public static final Category FOOD = new Category(new NamespacedKey(SlimefunPlugin.instance, "food"), new CustomItem(SlimefunItems.FORTUNE_COOKIE, "&7Food"), 2);
    public static final Category MACHINES_1 = new Category(new NamespacedKey(SlimefunPlugin.instance, "basic_machines"), new CustomItem(Material.SMITHING_TABLE, "&7Basic Machines"), 1);
    public static final Category ARMOR = new Category(new NamespacedKey(SlimefunPlugin.instance, "armor"), new CustomItem(SlimefunItems.DAMASCUS_STEEL_CHESTPLATE, "&7Armor"), 2);
    public static final Category LUMPS_AND_MAGIC = new Category(new NamespacedKey(SlimefunPlugin.instance, "magical_items"), new CustomItem(SlimefunItems.RUNE_ENDER, "&7Magical Items"), 2);
    public static final Category MAGIC = new Category(new NamespacedKey(SlimefunPlugin.instance, "magical_gadgets"), new CustomItem(SlimefunItems.INFUSED_ELYTRA, "&7Magical Gadgets"), 3);
    public static final Category MISC = new Category(new NamespacedKey(SlimefunPlugin.instance, "misc"), new CustomItem(SlimefunItems.CAN, "&7Miscellaneous"), 2);
    public static final Category TECH = new Category(new NamespacedKey(SlimefunPlugin.instance, "technical_gadgets"), new CustomItem(SlimefunItems.STEEL_JETPACK, "&7Technical Gadgets"), 3);
    public static final Category RESOURCES = new Category(new NamespacedKey(SlimefunPlugin.instance, "resources"), new CustomItem(SlimefunItems.SYNTHETIC_SAPPHIRE, "&7Resources"), 1);
    public static final Category CARGO = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance, "cargo"), new CustomItem(SlimefunItems.CARGO_MANAGER, "&cCargo Management"), 4, MACHINES_1);
    public static final Category TECH_MISC = new Category(new NamespacedKey(SlimefunPlugin.instance, "tech_misc"), new CustomItem(SlimefunItems.HEATING_COIL, "&7Technical Components"), 2);
    public static final Category MAGIC_ARMOR = new Category(new NamespacedKey(SlimefunPlugin.instance, "magical_armor"), new CustomItem(SlimefunItems.ENDER_HELMET, "&7Magical Armor"), 2);
    public static final Category TALISMANS_1 = new Category(new NamespacedKey(SlimefunPlugin.instance, "talismans"), new CustomItem(SlimefunItems.TALISMAN, "&7Talismans - &aTier I"), 2);

    // Locked Categories
    public static final LockedCategory ELECTRICITY = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance, "electricity"), new CustomItem(SlimefunItems.NUCLEAR_REACTOR, "&bEnergy and Electricity"), 4, MACHINES_1);
    public static final LockedCategory GPS = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance, "gps"), new CustomItem(SlimefunItems.GPS_TRANSMITTER, "&bGPS-based Machines"), 4, MACHINES_1);
    public static final LockedCategory TALISMANS_2 = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance, "ender_talismans"), new CustomItem(SlimefunItems.ENDER_TALISMAN, "&7Talismans - &aTier II"), 3, TALISMANS_1);

    // Seasonal Categories
    public static final SeasonalCategory CHRISTMAS = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "christmas"), 12, 1, new CustomItem(Material.NETHER_STAR, ChatUtils.christmas("Christmas") + " &7(December only)"));
    public static final SeasonalCategory VALENTINES_DAY = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "valentines_day"), 2, 2, new CustomItem(Material.POPPY, "&dValentine's Day" + " &7(14th February)"));
    public static final SeasonalCategory EASTER = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "easter"), 4, 2, new CustomItem(Material.EGG, "&6Easter" + " &7(April)"));
    public static final SeasonalCategory BIRTHDAY = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "birthday"), 10, 1, new CustomItem(Material.FIREWORK_ROCKET, "&a&lTheBusyBiscuit's Birthday &7(26th October)"));
    public static final SeasonalCategory HALLOWEEN = new SeasonalCategory(new NamespacedKey(SlimefunPlugin.instance, "halloween"), 10, 1, new CustomItem(Material.JACK_O_LANTERN, "&6&lHalloween &7(31st October)"));

}
