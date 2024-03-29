package io.github.thebusybiscuit.slimefun4.implementation;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ThornyaItems {
    public static final SlimefunItemStack MK1_SPACESHIP = new SlimefunItemStack("THORNYA_MK1_SPACESHIP", new ItemStack(Material.LEATHER_HORSE_ARMOR), "&bMK1 Spaceship");
    public static final SlimefunItemStack SPACE_HELMET = new SlimefunItemStack("THORNYA_SPACE_HELMET", new ItemStack(Material.LEATHER_HORSE_ARMOR), "&bSpace Helmet");
    public static final SlimefunItemStack UPGRADER_OXYGEN_TANK = new SlimefunItemStack("THORNYA_UPGRADER_OXYGEN_TANK", new ItemStack(Material.PAPER), "&e[Upgrade] &bOxygen Tank I");
    public static final SlimefunItemStack UPGRADER_UPGRADER_DEFENSE_1 = new SlimefunItemStack("THORNYA_UPGRADER_DEFENSE_1", new ItemStack(Material.PAPER), "&e[Upgrade] &bDefense I");
    public static final SlimefunItemStack UPGRADER_UPGRADER_DEFENSE_2 = new SlimefunItemStack("THORNYA_UPGRADER_DEFENSE_2", new ItemStack(Material.PAPER), "&e[Upgrade] &bDefense II");
    public static final SlimefunItemStack UPGRADER_UPGRADER_DEFENSE_3 = new SlimefunItemStack("THORNYA_UPGRADER_DEFENSE_3", new ItemStack(Material.PAPER), "&e[Upgrade] &bDefense III");
    public static final SlimefunItemStack HELIO3_GENERATOR = new SlimefunItemStack("THORNYA_HELIO3_GENERATOR", new ItemStack(Material.FURNACE), "&bHelio-3 Generator");
    public static final SlimefunItemStack HELIO3 = new SlimefunItemStack("THORNYA_HELIO3", new ItemStack(Material.QUARTZ), "&bHelio-3");
    public static final SlimefunItemStack OXYGEN_TANK_Mk1 = new SlimefunItemStack("THORNYA_OXYGEN_TANK_MK1", new ItemStack(Material.PAPER), "&bOxygen Tank MK1");
    public static final SlimefunItemStack OXYGEN_TANK_Mk2 = new SlimefunItemStack("THORNYA_OXYGEN_TANK_MK2", new ItemStack(Material.PAPER), "&bOxygen Tank MK2");
    public static final SlimefunItemStack OXYGEN_TANK_Mk3 = new SlimefunItemStack("THORNYA_OXYGEN_TANK_MK3", new ItemStack(Material.PAPER), "&bOxygen Tank MK3");
    public static final SlimefunItemStack SOLAR_MOON = new SlimefunItemStack("THORNYA_SOLAR_MOON", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bSolar Moon");
    public static final SlimefunItemStack SOLAR_BASE = new SlimefunItemStack("THORNYA_SOLAR_BASE", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bSolar Base");
    public static final SlimefunItemStack CHEESE_ORE = new SlimefunItemStack("THORNYA_CHEESE_ORE", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bCheese Ore");
    public static final SlimefunItemStack LEAD_ORE = new SlimefunItemStack("THORNYA_LEAD_ORE", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bLead Ore");
    public static final SlimefunItemStack TIN_ORE = new SlimefunItemStack("THORNYA_TIN_ORE", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bTin Ore");
    public static final SlimefunItemStack SILVER_ORE = new SlimefunItemStack("THORNYA_SILVER_ORE", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bSilver Ore");
    public static final SlimefunItemStack URANIUM_ORE = new SlimefunItemStack("THORNYA_URANIUM_ORE", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bUranium Ore");
    public static final SlimefunItemStack ALUMINUM_ORE = new SlimefunItemStack("THORNYA_ALUMINUM_ORE", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bAluminum Ore");
    public static final SlimefunItemStack COBALT_ORE = new SlimefunItemStack("THORNYA_COBALT_ORE", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bCobalt Ore");
    public static final SlimefunItemStack MAGNESIUM_ORE = new SlimefunItemStack("THORNYA_MAGNESIUM_ORE", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bMagnesium Ore");
    public static final SlimefunItemStack ZINC_ORE = new SlimefunItemStack("THORNYA_ZINC_ORE", new ItemStack(Material.BROWN_MUSHROOM_BLOCK), "&bZinc Ore");





    static {
        LeatherArmorMeta MK1_SPACESHIP_META = (LeatherArmorMeta) MK1_SPACESHIP.getItemMeta();
        MK1_SPACESHIP_META.setCustomModelData(999);
        MK1_SPACESHIP_META.setColor(org.bukkit.Color.fromRGB(0, 0, 7));
        MK1_SPACESHIP_META.addItemFlags(ItemFlag.HIDE_DYE);
        MK1_SPACESHIP.setItemMeta(MK1_SPACESHIP_META);


        LeatherArmorMeta SPACE_HELMET_META = (LeatherArmorMeta) SPACE_HELMET.getItemMeta();
        SPACE_HELMET_META.setCustomModelData(998);
        SPACE_HELMET_META.addItemFlags(ItemFlag.HIDE_DYE);
        SPACE_HELMET.setItemMeta(SPACE_HELMET_META);

        ItemMeta UPGRADER_OXYGEN_TANK_META = UPGRADER_OXYGEN_TANK.getItemMeta();
        UPGRADER_OXYGEN_TANK_META.setCustomModelData(3);
        UPGRADER_OXYGEN_TANK.setItemMeta(UPGRADER_OXYGEN_TANK_META);

        ItemMeta UPGRADER_UPGRADER_DEFENSE_1_META = UPGRADER_UPGRADER_DEFENSE_1.getItemMeta();
        UPGRADER_UPGRADER_DEFENSE_1_META.setCustomModelData(1);
        UPGRADER_UPGRADER_DEFENSE_1.setItemMeta(UPGRADER_UPGRADER_DEFENSE_1_META);

        ItemMeta UPGRADER_UPGRADER_DEFENSE_2_META = UPGRADER_UPGRADER_DEFENSE_2.getItemMeta();
        UPGRADER_UPGRADER_DEFENSE_2_META.setCustomModelData(1);
        UPGRADER_UPGRADER_DEFENSE_2.setItemMeta(UPGRADER_UPGRADER_DEFENSE_2_META);
        ItemMeta UPGRADER_UPGRADER_DEFENSE_3_META = UPGRADER_UPGRADER_DEFENSE_3.getItemMeta();
        UPGRADER_UPGRADER_DEFENSE_3_META.setCustomModelData(1);
        UPGRADER_UPGRADER_DEFENSE_3.setItemMeta(UPGRADER_UPGRADER_DEFENSE_3_META);

        ItemMeta OXYGEN_TANK_Mk1_META = OXYGEN_TANK_Mk1.getItemMeta();
        OXYGEN_TANK_Mk1_META.setCustomModelData(4);
        OXYGEN_TANK_Mk1.setItemMeta(OXYGEN_TANK_Mk1_META);

        ItemMeta OXYGEN_TANK_Mk2_META = OXYGEN_TANK_Mk2.getItemMeta();
        OXYGEN_TANK_Mk2_META.setCustomModelData(5);
        OXYGEN_TANK_Mk2.setItemMeta(OXYGEN_TANK_Mk2_META);

        ItemMeta OXYGEN_TANK_Mk3_META = OXYGEN_TANK_Mk3.getItemMeta();
        OXYGEN_TANK_Mk3_META.setCustomModelData(6);
        OXYGEN_TANK_Mk3.setItemMeta(OXYGEN_TANK_Mk3_META);
    }




}
