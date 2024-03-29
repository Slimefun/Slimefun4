package io.github.thebusybiscuit.slimefun4.implementation.setup;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.ThornyaItems;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ThornyaGroups {

    public static final NestedItemGroup THORNYA_MAIN = new NestedItemGroup(new NamespacedKey(Slimefun.instance(), "thornya_space"), getItemMain());
    public static final SubItemGroup BLOCKS = new SubItemGroup(new NamespacedKey(Slimefun.instance(), "thornya_blocks"), THORNYA_MAIN, new CustomItemStack(ThornyaItems.CHEESE_ORE, "&bBlocos"), 1);
    public static final SubItemGroup SHIPS = new SubItemGroup(new NamespacedKey(Slimefun.instance(), "thornya_ships"), THORNYA_MAIN, getItemShips(), 1);
    public static final SubItemGroup MATERIALS = new SubItemGroup(new NamespacedKey(Slimefun.instance(), "thornya_materials"), THORNYA_MAIN, new CustomItemStack(ThornyaItems.HELIO3, "&bMateriais"), 1);
    public static final SubItemGroup SUITS = new SubItemGroup(new NamespacedKey(Slimefun.instance(), "thornya_suits"), THORNYA_MAIN, getItemSuits(), 1);
    public static final SubItemGroup MACHINES = new SubItemGroup(new NamespacedKey(Slimefun.instance(), "thornya_machines"), THORNYA_MAIN, new CustomItemStack(Material.BLAST_FURNACE, "&bMáquinas"), 1);


    static {
        THORNYA_MAIN.register(Slimefun.instance());
        BLOCKS.register(Slimefun.instance());
        SHIPS.register(Slimefun.instance());
        MATERIALS.register(Slimefun.instance());
        SUITS.register(Slimefun.instance());
        MACHINES.register(Slimefun.instance());
    }


    private static CustomItemStack getItemMain(){
        CustomItemStack item = new CustomItemStack(Material.LEATHER_HORSE_ARMOR, "&bThornya Space");
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(org.bukkit.Color.fromRGB(0, 0, 7));
        item.setItemMeta(meta);
        item.setCustomModel(999);
        return item;
    }

    private static CustomItemStack getItemShips(){
        CustomItemStack item = new CustomItemStack(Material.LEATHER_HORSE_ARMOR, "&bNaves");
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.fromRGB(0, 0, 7));
        item.setItemMeta(meta);
        item.setCustomModel(999);
        return item;
    }

    private static CustomItemStack getItemSuits(){
        CustomItemStack item = new CustomItemStack(Material.LEATHER_HORSE_ARMOR, "&bTrajes Espaciais");
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(org.bukkit.Color.fromRGB(0, 0, 7));
        item.setItemMeta(meta);
        item.setCustomModel(998);
        return item;
    }
}
