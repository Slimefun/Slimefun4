package me.mrCookieSlime.Slimefun.Lists;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.categories.SeasonalCategory;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.Material;
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

    private Categories() {}

    // Standard Categories
    public static final Category WEAPONS = new Category(new NamespacedKey(SlimefunPlugin.instance, "weapons"), new CustomItem(SlimefunItems.BLADE_OF_VAMPIRES, "&7Weapons"), 1);
    public static final Category TOOLS = new Category(new NamespacedKey(SlimefunPlugin.instance, "tools"), new CustomItem(SlimefunItems.AUTO_SMELT_PICKAXE, "&7Tools"), 1);
    public static final Category PORTABLE = new Category(new NamespacedKey(SlimefunPlugin.instance, "items"), new CustomItem(SlimefunItems.BACKPACK_MEDIUM, "&7Useful Items"), 1);
    public static final Category FOOD = new Category(new NamespacedKey(SlimefunPlugin.instance, "food"), new CustomItem(SlimefunItems.FORTUNE_COOKIE, "&7Food"), 2);
    public static final Category BASIC_MACHINES = new Category(new NamespacedKey(SlimefunPlugin.instance, "basic_machines"), new CustomItem(Material.CRAFTING_TABLE, "&7Basic Machines"), 1);
    public static final Category ARMOR = new Category(new NamespacedKey(SlimefunPlugin.instance, "armor"), new CustomItem(SlimefunItems.DAMASCUS_STEEL_CHESTPLATE, "&7Armor"), 2);
    public static final Category LUMPS_AND_MAGIC = new Category(new NamespacedKey(SlimefunPlugin.instance, "magical_items"), new CustomItem(SlimefunItems.RUNE_ENDER, "&7Magical Items"), 2);
    public static final Category MAGIC = new Category(new NamespacedKey(SlimefunPlugin.instance, "magical_gadgets"), new CustomItem(SlimefunItems.INFUSED_ELYTRA, "&7Magical Gadgets"), 3);
    public static final Category MISC = new Category(new NamespacedKey(SlimefunPlugin.instance, "misc"), new CustomItem(SlimefunItems.CAN, "&7Miscellaneous"), 2);
    public static final Category TECH = new Category(new NamespacedKey(SlimefunPlugin.instance, "technical_gadgets"), new CustomItem(SlimefunItems.STEEL_JETPACK, "&7Technical Gadgets"), 3);
    public static final Category RESOURCES = new Category(new NamespacedKey(SlimefunPlugin.instance, "resources"), new CustomItem(SlimefunItems.SYNTHETIC_SAPPHIRE, "&7Resources"), 1);
    public static final Category CARGO = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance, "cargo"), new CustomItem(SlimefunItems.CARGO_MANAGER, "&cCargo Management"), 4, BASIC_MACHINES.getKey());
    public static final Category TECH_MISC = new Category(new NamespacedKey(SlimefunPlugin.instance, "tech_misc"), new CustomItem(SlimefunItems.HEATING_COIL, "&7Technical Components"), 2);
    public static final Category MAGIC_ARMOR = new Category(new NamespacedKey(SlimefunPlugin.instance, "magical_armor"), new CustomItem(SlimefunItems.ENDER_HELMET, "&7Magical Armor"), 2);

    // Locked Categories
    public static final LockedCategory ELECTRICITY = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance, "electricity"), new CustomItem(SlimefunItems.NUCLEAR_REACTOR, "&bEnergy and Electricity"), 4, BASIC_MACHINES.getKey());
    public static final LockedCategory GPS = new LockedCategory(new NamespacedKey(SlimefunPlugin.instance, "gps"), new CustomItem(SlimefunItems.GPS_TRANSMITTER, "&bGPS-based Machines"), 4, BASIC_MACHINES.getKey());
}