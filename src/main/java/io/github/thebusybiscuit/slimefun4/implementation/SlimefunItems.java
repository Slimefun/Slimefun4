package io.github.thebusybiscuit.slimefun4.implementation;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.StormStaff;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.CustomHeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public final class SlimefunItems {

    private SlimefunItems() {
    }

    /*		 Items 		*/
    public static final SlimefunItemStack PORTABLE_CRAFTER = new SlimefunItemStack("PORTABLE_CRAFTER", CustomHeadTexture.PORTABLE_CRAFTER, "&6便携工作台", "&a&o一个便携式的工作台", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack PORTABLE_DUSTBIN = new SlimefunItemStack("PORTABLE_DUSTBIN", CustomHeadTexture.TRASH_CAN, "&6便携垃圾箱", "&r轻松的消除多余的物品", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack ENDER_BACKPACK = new SlimefunItemStack("ENDER_BACKPACK", CustomHeadTexture.ENDER_BACKPACK, "&6末影背包", "&a&o便携式末影箱", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack MAGIC_EYE_OF_ENDER = new SlimefunItemStack("MAGIC_EYE_OF_ENDER", Material.ENDER_EYE, "&6&l魔法末影之眼", "&4&l需要全套末影护甲", "", "&7&e右键&7 以射出一颗末影珍珠");
    public static final SlimefunItemStack BROKEN_SPAWNER = new SlimefunItemStack("BROKEN_SPAWNER", Material.SPAWNER, "&c已损坏的刷怪笼", "&7类型: &b<类型>", "", "&c已损坏, 需要在古代祭坛中修复");
    public static final SlimefunItemStack REPAIRED_SPAWNER = new SlimefunItemStack("REINFORCED_SPAWNER", Material.SPAWNER, "&b已修复的刷怪笼", "&7类型: &b<类型>");
    public static final SlimefunItemStack INFERNAL_BONEMEAL = new SlimefunItemStack("INFERNAL_BONEMEAL", Material.BONE_MEAL, "&4地狱骨粉", "", "&c加速地狱疣的生长速度");

    /*		 Gadgets 		*/
    public static final SlimefunItemStack GOLD_PAN = new SlimefunItemStack("GOLD_PAN", Material.BOWL, "&6淘金盘", "&a&o可以获得各种各样的矿物", "", "&7&e右键&7 从沙砾中淘金");
    public static final SlimefunItemStack NETHER_GOLD_PAN = new SlimefunItemStack("NETHER_GOLD_PAN", Material.BOWL, "&4下界淘金盘", "", "&7&e右键&7 从灵魂沙中淘金");
    public static final SlimefunItemStack PARACHUTE = new SlimefunItemStack("PARACHUTE", Material.LEATHER_CHESTPLATE, Color.WHITE, "&r&l降落伞", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack GRAPPLING_HOOK = new SlimefunItemStack("GRAPPLING_HOOK", Material.LEAD, "&6抓钩", "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack SOLAR_HELMET = new SlimefunItemStack("SOLAR_HELMET", Material.IRON_HELMET, "&b太阳能头盔", "", "&a&o为你拿着的物品和装备充电");
    public static final SlimefunItemStack CLOTH = new SlimefunItemStack("CLOTH", Material.PAPER, "&b布");
    public static final SlimefunItemStack CAN = new SlimefunItemStack("CAN", CustomHeadTexture.TIN_CAN, "&r锡罐");
    public static final SlimefunItemStack NIGHT_VISION_GOGGLES = new SlimefunItemStack("NIGHT_VISION_GOGGLES", Material.LEATHER_HELMET, Color.BLACK, "&a夜视眼镜", "", "&9+ 夜视效果");
    public static final SlimefunItemStack FARMER_SHOES = new SlimefunItemStack("FARMER_SHOES", Material.LEATHER_BOOTS, Color.YELLOW, "&e农夫的靴子", "", "&6&o能够防止你踩坏农田");
    public static final SlimefunItemStack INFUSED_MAGNET = new SlimefunItemStack("INFUSED_MAGNET", CustomHeadTexture.MAGNET, "&a吸入磁铁", "", "&r注入了魔法的磁铁", "&r能够将附近的物品", "&r放入你的背包", "", "&7按住 &eShift&7 吸取周围的物品");
    public static final SlimefunItemStack RAG = new SlimefunItemStack("RAG", Material.PAPER, "&c破布", "", "&a1级医疗供给", "", "&r恢复2点血量", "&r可以熄灭身上的火", "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack BANDAGE = new SlimefunItemStack("BANDAGE", Material.PAPER, "&c绷带", "", "&a2级医疗供给", "", "&r恢复4点血量", "&r可以熄灭身上的火", "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack SPLINT = new SlimefunItemStack("SPLINT", Material.STICK, "&c夹板", "", "&a1级医疗供给", "", "&r恢复2点血量", "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack VITAMINS = new SlimefunItemStack("VITAMINS", Material.NETHER_WART, "&c维他命", "", "&a3级医疗供给", "", "&r恢复4点血量", "&r可以熄灭身上的火", "&r治愈中毒/凋零/辐射的负面效果", "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack MEDICINE = new SlimefunItemStack("MEDICINE", Material.POTION, Color.RED, "&c药物", "", "&a3级医疗供给", "", "&r恢复4点血量", "&r可以熄灭身上的火", "&r治愈中毒/凋零/辐射的负面效果", "", "&7&e右键&7 饮用");

    public static final SlimefunItemStack FLASK_OF_KNOWLEDGE = new SlimefunItemStack("FLASK_OF_KNOWLEDGE", Material.GLASS_BOTTLE, "&c学识之瓶", "", "&r允许你将经验储存在瓶子里", "&7需要消耗 &a1 个等级");
    public static final SlimefunItemStack FILLED_FLASK_OF_KNOWLEDGE = new SlimefunItemStack("FILLED_FLASK_OF_KNOWLEDGE", Material.EXPERIENCE_BOTTLE, "&a学识之瓶");

    /*		Backpacks		*/
    public static final SlimefunItemStack BACKPACK_SMALL = new SlimefunItemStack("SMALL_BACKPACK", CustomHeadTexture.BACKPACK, "&e小型背包", "", "&7大小: &e9", "&7ID: <ID>", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack BACKPACK_MEDIUM = new SlimefunItemStack("MEDIUM_BACKPACK", CustomHeadTexture.BACKPACK, "&e普通背包", "", "&7大小: &e18", "&7ID: <ID>", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack BACKPACK_LARGE = new SlimefunItemStack("LARGE_BACKPACK", CustomHeadTexture.BACKPACK, "&e中型背包", "", "&7大小: &e27", "&7ID: <ID>", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack WOVEN_BACKPACK = new SlimefunItemStack("WOVEN_BACKPACK", CustomHeadTexture.BACKPACK, "&e编织背包", "", "&7大小: &e36", "&7ID: <ID>", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack GILDED_BACKPACK = new SlimefunItemStack("GILDED_BACKPACK", CustomHeadTexture.BACKPACK, "&e镀金背包", "", "&7大小: &e45", "&7ID: <ID>", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack RADIANT_BACKPACK = new SlimefunItemStack("RADIANT_BACKPACK", CustomHeadTexture.BACKPACK, "&e金光闪闪的背包", "", "&7大小: &e54 (大箱子)", "&7ID: <ID>", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack BOUND_BACKPACK = new SlimefunItemStack("BOUND_BACKPACK", CustomHeadTexture.ENDER_BACKPACK, "&c灵魂绑定背包", "", "&7大小: &e36", "&7ID: <ID>", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack COOLER = new SlimefunItemStack("COOLER", CustomHeadTexture.COOLER, "&b小冰柜", "&r可以储存果汁和冰沙", "&r当小冰柜在你的物品栏里时", "&r在你饥饿时将会自动消耗里面的食物", "", "&7大小: &e27", "&7ID: <ID>", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack RESTORED_BACKPACK = new SlimefunItemStack("RESTORED_BACKPACK", CustomHeadTexture.RESTORED_BACKPACK, "&e背包恢复器", "", "&7重新获取你丢失背包里的物品", "&7ID: <ID>", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);

    /*		 Jetpacks		*/
    public static final SlimefunItemStack DURALUMIN_JETPACK = new SlimefunItemStack("DURALUMIN_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eI", "", LoreBuilder.material("硬铝"), LoreBuilder.powerCharged(0, 20), "&8\u21E8 &7推力: &c0.35", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack SOLDER_JETPACK = new SlimefunItemStack("SOLDER_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eII", "", LoreBuilder.material("焊锡"), LoreBuilder.powerCharged(0, 30), "&8\u21E8 &7推力: &c0.4", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack BILLON_JETPACK = new SlimefunItemStack("BILLON_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eIII", "", LoreBuilder.material("银铜合金"), LoreBuilder.powerCharged(0, 45), "&8\u21E8 &7推力: &c0.45", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack STEEL_JETPACK = new SlimefunItemStack("STEEL_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eIV", "", LoreBuilder.material("钢"), LoreBuilder.powerCharged(0, 60), "&8\u21E8 &7推力: &c0.5", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack DAMASCUS_STEEL_JETPACK = new SlimefunItemStack("DAMASCUS_STEEL_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eV", "", LoreBuilder.material("大马士革钢"), LoreBuilder.powerCharged(0, 75), "&8\u21E8 &7推力: &c0.55", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack REINFORCED_ALLOY_JETPACK = new SlimefunItemStack("REINFORCED_ALLOY_JETPACK", Material.LEATHER_CHESTPLATE, Color.SILVER, "&9电力喷气背包 &7- &eVI", "", LoreBuilder.material("强化合金"), LoreBuilder.powerCharged(0, 100), "&8\u21E8 &7推力: &c0.6", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack CARBONADO_JETPACK = new SlimefunItemStack("CARBONADO_JETPACK", Material.LEATHER_CHESTPLATE, Color.BLACK, "&9电力喷气背包 &7- &eVII", "", LoreBuilder.material("黑金刚石"), LoreBuilder.powerCharged(0, 150), "&8\u21E8 &7推力: &c0.7", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack ARMORED_JETPACK = new SlimefunItemStack("ARMORED_JETPACK", Material.IRON_CHESTPLATE, "&9装甲喷气背包", LoreBuilder.material("钢"), "", LoreBuilder.powerCharged(0, 50), "&8\u21E8 &7推力: &c0.45", "", LoreBuilder.CROUCH_TO_USE);

    /*		 Jetboots		*/
    public static final SlimefunItemStack DURALUMIN_JETBOOTS = new SlimefunItemStack("DURALUMIN_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eI", "", LoreBuilder.material("硬铝"), LoreBuilder.powerCharged(0, 20), "&8\u21E8 &7速度: &a0.35", "&8\u21E8 &7准确度: &c50%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack SOLDER_JETBOOTS = new SlimefunItemStack("SOLDER_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eII", "", LoreBuilder.material("焊锡"), LoreBuilder.powerCharged(0, 30), "&8\u21E8 &7速度: &a0.4", "&8\u21E8 &7准确度: &660%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack BILLON_JETBOOTS = new SlimefunItemStack("BILLON_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eIII", "", LoreBuilder.material("银铜合金"), LoreBuilder.powerCharged(0, 40), "&8\u21E8 &7速度: &a0.45", "&8\u21E8 &7准确度: &665%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack STEEL_JETBOOTS = new SlimefunItemStack("STEEL_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eIV", "", LoreBuilder.material("钢"), LoreBuilder.powerCharged(0, 50), "&8\u21E8 &7速度: &a0.5", "&8\u21E8 &7准确度: &e70%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack DAMASCUS_STEEL_JETBOOTS = new SlimefunItemStack("DAMASCUS_STEEL_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eV", "", LoreBuilder.material("大马士革钢"), LoreBuilder.powerCharged(0, 75), "&8\u21E8 &7速度: &a0.55", "&8\u21E8 &7准确度: &a75%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack REINFORCED_ALLOY_JETBOOTS = new SlimefunItemStack("REINFORCED_ALLOY_JETBOOTS", Material.LEATHER_BOOTS, Color.SILVER, "&9喷气靴 &7- &eVI", "", LoreBuilder.material("强化合金"), LoreBuilder.powerCharged(0, 100), "&8\u21E8 &7速度: &a0.6", "&8\u21E8 &7准确度: &c80%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack CARBONADO_JETBOOTS = new SlimefunItemStack("CARBONADO_JETBOOTS", Material.LEATHER_BOOTS, Color.BLACK, "&9喷气靴 &7- &eVII", "", LoreBuilder.material("黑金刚石"), LoreBuilder.powerCharged(0, 125), "&8\u21E8 &7速度: &a0.7", "&8\u21E8 &7准确度: &c99.9%", "", LoreBuilder.CROUCH_TO_USE);
    public static final SlimefunItemStack ARMORED_JETBOOTS = new SlimefunItemStack("ARMORED_JETBOOTS", Material.IRON_BOOTS, "&9装甲喷气靴", "", LoreBuilder.material("钢"), LoreBuilder.powerCharged(0, 50), "&8\u21E8 &7速度: &a0.45", "&8\u21E8 &7准确度: &e70%", "", LoreBuilder.CROUCH_TO_USE);

    /*		 Multi Tools		*/
    public static final SlimefunItemStack DURALUMIN_MULTI_TOOL = new SlimefunItemStack("DURALUMIN_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eI", "", LoreBuilder.material("硬铝"), LoreBuilder.powerCharged(0, 20), "", LoreBuilder.RIGHT_CLICK_TO_USE, "&7按住 &eShift + 右键&7 以更改模式");
    public static final SlimefunItemStack SOLDER_MULTI_TOOL = new SlimefunItemStack("SOLDER_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eII", "", LoreBuilder.material("焊锡"), LoreBuilder.powerCharged(0, 30), "", LoreBuilder.RIGHT_CLICK_TO_USE, "&7按住 &eShift + 右键&7 以更改模式");
    public static final SlimefunItemStack BILLON_MULTI_TOOL = new SlimefunItemStack("BILLON_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eIII", "", LoreBuilder.material("银铜合金"), LoreBuilder.powerCharged(0, 40), "", LoreBuilder.RIGHT_CLICK_TO_USE, "&7按住 &eShift + 右键&7 以更改模式");
    public static final SlimefunItemStack STEEL_MULTI_TOOL = new SlimefunItemStack("STEEL_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eIV", "", LoreBuilder.material("钢"), LoreBuilder.powerCharged(0, 50), "", LoreBuilder.RIGHT_CLICK_TO_USE, "&7按住 &eShift + 右键&7 以更改模式");
    public static final SlimefunItemStack DAMASCUS_STEEL_MULTI_TOOL = new SlimefunItemStack("DAMASCUS_STEEL_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eV", "", LoreBuilder.material("大马士革钢"), LoreBuilder.powerCharged(0, 60), "", LoreBuilder.RIGHT_CLICK_TO_USE, "&7按住 &eShift + 右键&7 以更改模式");
    public static final SlimefunItemStack REINFORCED_ALLOY_MULTI_TOOL = new SlimefunItemStack("REINFORCED_ALLOY_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eVI", "", LoreBuilder.material("强化合金"), LoreBuilder.powerCharged(0, 75), "", LoreBuilder.RIGHT_CLICK_TO_USE, "&7按住 &eShift + 右键&7 以更改模式");
    public static final SlimefunItemStack CARBONADO_MULTI_TOOL = new SlimefunItemStack("CARBONADO_MULTI_TOOL", Material.SHEARS, "&9多功能工具 &7- &eVII", "", LoreBuilder.material("黑金刚石"), LoreBuilder.powerCharged(0, 100), "", LoreBuilder.RIGHT_CLICK_TO_USE, "&7按住 &eShift + 右键&7 以更改模式");

    static {
        ItemMeta duralumin = DURALUMIN_MULTI_TOOL.getItemMeta();
        duralumin.setUnbreakable(true);
        DURALUMIN_MULTI_TOOL.setItemMeta(duralumin);

        ItemMeta solder = SOLDER_MULTI_TOOL.getItemMeta();
        solder.setUnbreakable(true);
        SOLDER_MULTI_TOOL.setItemMeta(solder);

        ItemMeta billon = BILLON_MULTI_TOOL.getItemMeta();
        billon.setUnbreakable(true);
        BILLON_MULTI_TOOL.setItemMeta(billon);

        ItemMeta steel = STEEL_MULTI_TOOL.getItemMeta();
        steel.setUnbreakable(true);
        STEEL_MULTI_TOOL.setItemMeta(steel);

        ItemMeta damascus = DAMASCUS_STEEL_MULTI_TOOL.getItemMeta();
        damascus.setUnbreakable(true);
        DAMASCUS_STEEL_MULTI_TOOL.setItemMeta(damascus);

        ItemMeta reinforced = REINFORCED_ALLOY_MULTI_TOOL.getItemMeta();
        reinforced.setUnbreakable(true);
        REINFORCED_ALLOY_MULTI_TOOL.setItemMeta(reinforced);

        ItemMeta carbonado = CARBONADO_MULTI_TOOL.getItemMeta();
        carbonado.setUnbreakable(true);
        CARBONADO_MULTI_TOOL.setItemMeta(carbonado);
    }

    /*		 Food 		*/
    public static final SlimefunItemStack FORTUNE_COOKIE = new SlimefunItemStack("FORTUNE_COOKIE", Material.COOKIE, "&6幸运饼干", "", "&a&o告诉你未来发生的事 :o");
    public static final SlimefunItemStack DIET_COOKIE = new SlimefunItemStack("DIET_COOKIE", Material.COOKIE, "&6减肥曲奇", "", "&a一个非常&o轻&r&a的曲奇");
    public static final SlimefunItemStack MAGIC_SUGAR = new SlimefunItemStack("MAGIC_SUGAR", Material.SUGAR, "&6魔法糖", "", "&a&o感受赫尔墨斯的力量!");
    public static final SlimefunItemStack MONSTER_JERKY = new SlimefunItemStack("MONSTER_JERKY", Material.ROTTEN_FLESH, "&6怪物肉干", "", "&a&o提神抗饥饿");
    public static final SlimefunItemStack APPLE_JUICE = new SlimefunItemStack("APPLE_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&c苹果汁", "", "&7&o恢复 &b&o" + "3.0" + " &7&o的饥饿值");
    public static final SlimefunItemStack MELON_JUICE = new SlimefunItemStack("MELON_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&c西瓜汁", "", "&7&o恢复 &b&o" + "3.0" + " &7&o的饥饿值");
    public static final SlimefunItemStack CARROT_JUICE = new SlimefunItemStack("CARROT_JUICE", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&6胡萝卜汁", "", "&7&o恢复 &b&o" + "3.0" + " &7&o的饥饿值");
    public static final SlimefunItemStack PUMPKIN_JUICE = new SlimefunItemStack("PUMPKIN_JUICE", Color.ORANGE, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&6南瓜汁", "", "&7&o恢复 &b&o" + "3.0" + " &7&o的饥饿值");
    public static final SlimefunItemStack SWEET_BERRY_JUICE = new SlimefunItemStack("SWEET_BERRY_JUICE", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "&c浆果汁", "", "&7&o恢复 &b&o" + "3.0" + " &7&o的饥饿值");
    public static final SlimefunItemStack GOLDEN_APPLE_JUICE = new SlimefunItemStack("GOLDEN_APPLE_JUICE", Color.YELLOW, new PotionEffect(PotionEffectType.ABSORPTION, 20 * 20, 0), "&b金苹果汁");

    public static final SlimefunItemStack BEEF_JERKY = new SlimefunItemStack("BEEF_JERKY", Material.COOKED_BEEF, "&6牛肉干", "", "&r横扫饥饿!");
    public static final SlimefunItemStack PORK_JERKY = new SlimefunItemStack("PORK_JERKY", Material.COOKED_PORKCHOP, "&6猪肉干", "", "&r横扫饥饿!");
    public static final SlimefunItemStack CHICKEN_JERKY = new SlimefunItemStack("CHICKEN_JERKY", Material.COOKED_CHICKEN, "&6鸡肉干", "", "&r横扫饥饿!");
    public static final SlimefunItemStack MUTTON_JERKY = new SlimefunItemStack("MUTTON_JERKY", Material.COOKED_MUTTON, "&6羊肉干", "", "&r横扫饥饿!");
    public static final SlimefunItemStack RABBIT_JERKY = new SlimefunItemStack("RABBIT_JERKY", Material.COOKED_RABBIT, "&6兔肉干", "", "&r横扫饥饿!");
    public static final SlimefunItemStack FISH_JERKY = new SlimefunItemStack("FISH_JERKY", Material.COOKED_COD, "&6鱼干", "", "&r横扫饥饿!");
    public static final SlimefunItemStack KELP_COOKIE = new SlimefunItemStack("KELP_COOKIE", Material.COOKIE, "&2海带曲奇");

    /*		Christmas		*/
    public static final SlimefunItemStack CHRISTMAS_MILK = new SlimefunItemStack("CHRISTMAS_MILK", Color.WHITE, new PotionEffect(PotionEffectType.SATURATION, 5, 0), "&6一杯牛奶", "", "&7&o恢复 &b&o" + "2.5" + " &7&o的饥饿值");
    public static final SlimefunItemStack CHRISTMAS_CHOCOLATE_MILK = new SlimefunItemStack("CHRISTMAS_CHOCOLATE_MILK", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 12, 0), "&6巧克力牛奶", "", "&7&o恢复 &b&o" + "3.5" + " &7&o的饥饿值");
    public static final SlimefunItemStack CHRISTMAS_EGG_NOG = new SlimefunItemStack("CHRISTMAS_EGG_NOG", Color.GRAY, new PotionEffect(PotionEffectType.SATURATION, 7, 0), "&a蛋酒", "", "&7&o恢复 &b&o" + "7.0" + " &7&o的饥饿值");
    public static final SlimefunItemStack CHRISTMAS_APPLE_CIDER = new SlimefunItemStack("CHRISTMAS_APPLE_CIDER", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 14, 0), "&c苹果酒", "", "&7&o恢复 &b&o" + "7.0" + " &7&o的饥饿值");
    public static final SlimefunItemStack CHRISTMAS_COOKIE = new SlimefunItemStack("CHRISTMAS_COOKIE", Material.COOKIE, ChatUtils.christmas("圣诞曲奇"));
    public static final SlimefunItemStack CHRISTMAS_FRUIT_CAKE = new SlimefunItemStack("CHRISTMAS_FRUIT_CAKE", Material.PUMPKIN_PIE, ChatUtils.christmas("水果蛋糕"));
    public static final SlimefunItemStack CHRISTMAS_APPLE_PIE = new SlimefunItemStack("CHRISTMAS_APPLE_PIE", Material.PUMPKIN_PIE, "&r苹果派");
    public static final SlimefunItemStack CHRISTMAS_HOT_CHOCOLATE = new SlimefunItemStack("CHRISTMAS_HOT_CHOCOLATE", Color.MAROON, new PotionEffect(PotionEffectType.SATURATION, 14, 0), "&6热可可", "", "&7&o恢复 &b&o" + "7.0" + " &7&o的饥饿值");
    public static final SlimefunItemStack CHRISTMAS_CAKE = new SlimefunItemStack("CHRISTMAS_CAKE", Material.PUMPKIN_PIE, ChatUtils.christmas("圣诞蛋糕"));
    public static final SlimefunItemStack CHRISTMAS_CARAMEL = new SlimefunItemStack("CHRISTMAS_CARAMEL", Material.BRICK, "&6焦糖");
    public static final SlimefunItemStack CHRISTMAS_CARAMEL_APPLE = new SlimefunItemStack("CHRISTMAS_CARAMEL_APPLE", Material.APPLE, "&6焦糖苹果");
    public static final SlimefunItemStack CHRISTMAS_CHOCOLATE_APPLE = new SlimefunItemStack("CHRISTMAS_CHOCOLATE_APPLE", Material.APPLE, "&6巧克力苹果");
    public static final SlimefunItemStack CHRISTMAS_PRESENT = new SlimefunItemStack("CHRISTMAS_PRESENT", CustomHeadTexture.CHRISTMAS_PRESENT, ChatUtils.christmas("圣诞礼物"), "&7来自 &eTheBusyBiscuit &7的礼物", "&7收件人: &e你", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);

    /*		Easter			*/
    public static final SlimefunItemStack EASTER_EGG = new SlimefunItemStack("EASTER_EGG", CustomHeadTexture.EASTER_EGG, "&r复活节彩蛋", "&d复活节快乐! 拆开礼物看看吧.", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
    public static final SlimefunItemStack EASTER_CARROT_PIE = new SlimefunItemStack("CARROT_PIE", Material.PUMPKIN_PIE, "&6胡萝卜派");
    public static final SlimefunItemStack EASTER_APPLE_PIE = new SlimefunItemStack("EASTER_APPLE_PIE", Material.PUMPKIN_PIE, "&r苹果派");

    /*		 Weapons 		*/
    public static final SlimefunItemStack GRANDMAS_WALKING_STICK = new SlimefunItemStack("GRANDMAS_WALKING_STICK", Material.STICK, "&7奶奶的拐杖");
    public static final SlimefunItemStack GRANDPAS_WALKING_STICK = new SlimefunItemStack("GRANDPAS_WALKING_STICK", Material.STICK, "&7爷爷的拐杖");
    public static final SlimefunItemStack SWORD_OF_BEHEADING = new SlimefunItemStack("SWORD_OF_BEHEADING", Material.IRON_SWORD, "&6处决之剑", "&7斩首处决 II", "", "&r有几率砍下生物的头", "&r(提高掉落凋灵骷髅头的几率)");
    public static final SlimefunItemStack BLADE_OF_VAMPIRES = new SlimefunItemStack("BLADE_OF_VAMPIRES", Material.GOLDEN_SWORD, "&c吸血鬼之刀", "&7生命窃取 I", "", "&r在攻击时有 45% 的几率", "使自己恢复 2 点血量");
    public static final SlimefunItemStack SEISMIC_AXE = new SlimefunItemStack("SEISMIC_AXE", Material.IRON_AXE, "&a地震斧", "", "&7&o制造一场地震...", "", LoreBuilder.RIGHT_CLICK_TO_USE);

    static {
        GRANDMAS_WALKING_STICK.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        GRANDPAS_WALKING_STICK.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        BLADE_OF_VAMPIRES.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
    }

    /*		Bows		*/
    public static final SlimefunItemStack EXPLOSIVE_BOW = new SlimefunItemStack("EXPLOSIVE_BOW", Material.BOW, "&c爆裂之弓", "&r被爆裂之弓射出的箭击中将会飞上天");
    public static final SlimefunItemStack ICY_BOW = new SlimefunItemStack("ICY_BOW", Material.BOW, "&b冰封之弓", "&r被此弓射出的箭击中", "&r将会因寒冷而无法移动 (2秒内)");

    /*		 Tools		*/
    public static final SlimefunItemStack SMELTERS_PICKAXE = new SlimefunItemStack("SMELTERS_PICKAXE", Material.DIAMOND_PICKAXE, "&6熔炉镐", "&c&l自动熔炼", "", "&9在挖矿时有时运效果");
    public static final SlimefunItemStack LUMBER_AXE = new SlimefunItemStack("LUMBER_AXE", Material.DIAMOND_AXE, "&6伐木斧", "&a&o砍倒整棵树木...");
    public static final SlimefunItemStack PICKAXE_OF_CONTAINMENT = new SlimefunItemStack("PICKAXE_OF_CONTAINMENT", Material.IRON_PICKAXE, "&c刷怪笼之镐", "", "&9可以获取刷怪笼");
    public static final SlimefunItemStack HERCULES_PICKAXE = new SlimefunItemStack("HERCULES_PICKAXE", Material.IRON_PICKAXE, "&9赫拉克勒斯之镐", "", "&r它如此强大", "&r因此能自动将挖到的矿物变为粉末...");
    public static final SlimefunItemStack EXPLOSIVE_PICKAXE = new SlimefunItemStack("EXPLOSIVE_PICKAXE", Material.DIAMOND_PICKAXE, "&e爆炸稿", "", "&r允许你在一瞬间挖掘矿物", "", "&9在挖矿时有时运效果");
    public static final SlimefunItemStack EXPLOSIVE_SHOVEL = new SlimefunItemStack("EXPLOSIVE_SHOVEL", Material.DIAMOND_SHOVEL, "&e爆炸铲", "", "&r让你一下子就能挖掉很多方块");
    public static final SlimefunItemStack PICKAXE_OF_THE_SEEKER = new SlimefunItemStack("PICKAXE_OF_THE_SEEKER", Material.DIAMOND_PICKAXE, "&a寻矿稿", "&r使用时将会指出你附近的矿物", "&r但可能它会受到损伤", "", "&7&e右键&7 以寻找四周的矿物");
    public static final SlimefunItemStack COBALT_PICKAXE = new SlimefunItemStack("COBALT_PICKAXE", Material.IRON_PICKAXE, "&9钴镐");
    public static final SlimefunItemStack PICKAXE_OF_VEIN_MINING = new SlimefunItemStack("PICKAXE_OF_VEIN_MINING", Material.DIAMOND_PICKAXE, "&e矿脉镐", "", "&r这个镐子将会挖出", "&r整个矿脉的矿物...");

    static {
        HERCULES_PICKAXE.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        HERCULES_PICKAXE.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);

        COBALT_PICKAXE.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        COBALT_PICKAXE.addUnsafeEnchantment(Enchantment.DIG_SPEED, 6);
    }

    /*		 Armor 		*/
    public static final SlimefunItemStack GLOWSTONE_HELMET = new SlimefunItemStack("GLOWSTONE_HELMET", Material.LEATHER_HELMET, Color.YELLOW, "&e&l萤石头盔", "", "&a&o像太阳一样闪耀!", "", "&9+ 夜视效果");
    public static final SlimefunItemStack GLOWSTONE_CHESTPLATE = new SlimefunItemStack("GLOWSTONE_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.YELLOW, "&e&l萤石胸甲", "", "&a&o像太阳一样闪耀!", "", "&9+ 夜视效果");
    public static final SlimefunItemStack GLOWSTONE_LEGGINGS = new SlimefunItemStack("GLOWSTONE_LEGGINGS", Material.LEATHER_LEGGINGS, Color.YELLOW, "&e&l萤石护腿", "", "&a&o像太阳一样闪耀!", "", "&9+ 夜视效果");
    public static final SlimefunItemStack GLOWSTONE_BOOTS = new SlimefunItemStack("GLOWSTONE_BOOTS", Material.LEATHER_BOOTS, Color.YELLOW, "&e&l萤石靴子", "", "&a&o像太阳一样闪耀!", "", "&9+ 夜视效果");

    public static final SlimefunItemStack ENDER_HELMET = new SlimefunItemStack("ENDER_HELMET", Material.LEATHER_HELMET, Color.fromRGB(28, 25, 112), "&5&l末影头盔", "", "&a&o任意移动");
    public static final SlimefunItemStack ENDER_CHESTPLATE = new SlimefunItemStack("ENDER_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.fromRGB(28, 25, 112), "&5&l末影胸甲", "", "&a&o任意移动");
    public static final SlimefunItemStack ENDER_LEGGINGS = new SlimefunItemStack("ENDER_LEGGINGS", Material.LEATHER_LEGGINGS, Color.fromRGB(28, 25, 112), "&5&l末影护腿", "", "&a&o任意移动");
    public static final SlimefunItemStack ENDER_BOOTS = new SlimefunItemStack("ENDER_BOOTS", Material.LEATHER_BOOTS, Color.fromRGB(28, 25, 112), "&5&l末影靴子", "", "&a&o任意移动", "", "&9+ 使用末影珍珠时无伤害");

    public static final SlimefunItemStack SLIME_HELMET = new SlimefunItemStack("SLIME_HELMET", Material.LEATHER_HELMET, Color.LIME, "&a&l史莱姆头盔", "", "&a&o有弹性的感觉");
    public static final SlimefunItemStack SLIME_CHESTPLATE = new SlimefunItemStack("SLIME_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.LIME, "&a&l史莱姆胸甲", "", "&a&o有弹性的感觉");
    public static final SlimefunItemStack SLIME_LEGGINGS = new SlimefunItemStack("SLIME_LEGGINGS", Material.LEATHER_LEGGINGS, Color.LIME, "&a&l史莱姆护腿", "", "&a&o有弹性的感觉", "", "&9+ 速度");
    public static final SlimefunItemStack SLIME_BOOTS = new SlimefunItemStack("SLIME_BOOTS", Material.LEATHER_BOOTS, Color.LIME, "&a&l史莱姆靴子", "", "&a&o有弹性的感觉", "", "&9+ 跳跃提升", "&9+ 减免摔落伤害");

    public static final SlimefunItemStack CACTUS_HELMET = new SlimefunItemStack("CACTUS_HELMET", Material.LEATHER_HELMET, Color.GREEN, "&2仙人掌头盔");
    public static final SlimefunItemStack CACTUS_CHESTPLATE = new SlimefunItemStack("CACTUS_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.GREEN, "&2仙人掌胸甲");
    public static final SlimefunItemStack CACTUS_LEGGINGS = new SlimefunItemStack("CACTUS_LEGGINGS", Material.LEATHER_LEGGINGS, Color.GREEN, "&2仙人掌护腿");
    public static final SlimefunItemStack CACTUS_BOOTS = new SlimefunItemStack("CACTUS_BOOTS", Material.LEATHER_BOOTS, Color.GREEN, "&2仙人掌靴子");

    public static final SlimefunItemStack DAMASCUS_STEEL_HELMET = new SlimefunItemStack("DAMASCUS_STEEL_HELMET", Material.IRON_HELMET, "&7大马士革钢头盔");
    public static final SlimefunItemStack DAMASCUS_STEEL_CHESTPLATE = new SlimefunItemStack("DAMASCUS_STEEL_CHESTPLATE", Material.IRON_CHESTPLATE, "&7大马士革钢胸甲");
    public static final SlimefunItemStack DAMASCUS_STEEL_LEGGINGS = new SlimefunItemStack("DAMASCUS_STEEL_LEGGINGS", Material.IRON_LEGGINGS, "&7大马士革钢护腿");
    public static final SlimefunItemStack DAMASCUS_STEEL_BOOTS = new SlimefunItemStack("DAMASCUS_STEEL_BOOTS", Material.IRON_BOOTS, "&7大马士革钢靴子");

    public static final SlimefunItemStack REINFORCED_ALLOY_HELMET = new SlimefunItemStack("REINFORCED_ALLOY_HELMET", Material.IRON_HELMET, "&b强化合金头盔");
    public static final SlimefunItemStack REINFORCED_ALLOY_CHESTPLATE = new SlimefunItemStack("REINFORCED_ALLOY_CHESTPLATE", Material.IRON_CHESTPLATE, "&b强化合金胸甲");
    public static final SlimefunItemStack REINFORCED_ALLOY_LEGGINGS = new SlimefunItemStack("REINFORCED_ALLOY_LEGGINGS", Material.IRON_LEGGINGS, "&b强化合金护腿");
    public static final SlimefunItemStack REINFORCED_ALLOY_BOOTS = new SlimefunItemStack("REINFORCED_ALLOY_BOOTS", Material.IRON_BOOTS, "&b强化合金靴子");

    public static final SlimefunItemStack SCUBA_HELMET = new SlimefunItemStack("SCUBA_HELMET", Material.LEATHER_HELMET, Color.ORANGE, "&c潜水头盔", "", "&b让你能够在水下呼吸", "&4&o防化套装的一部分");
    public static final SlimefunItemStack HAZMAT_CHESTPLATE = new SlimefunItemStack("HAZMAT_CHESTPLATE", Material.LEATHER_CHESTPLATE, Color.ORANGE, "&c防化服", "", "&b让你能在火焰中行走", "&4&o防化套装的一部分");
    public static final SlimefunItemStack HAZMAT_LEGGINGS = new SlimefunItemStack("HAZMAT_LEGGINGS", Material.LEATHER_LEGGINGS, Color.ORANGE, "&c防化护腿", "", "&4&o防化套装的一部分");
    public static final SlimefunItemStack RUBBER_BOOTS = new SlimefunItemStack("RUBBER_BOOTS", Material.LEATHER_BOOTS, Color.BLACK, "&c橡胶靴", "", "&4&o防化套装的一部分");

    public static final SlimefunItemStack GILDED_IRON_HELMET = new SlimefunItemStack("GILDED_IRON_HELMET", Material.GOLDEN_HELMET, "&6镀金铁头盔");
    public static final SlimefunItemStack GILDED_IRON_CHESTPLATE = new SlimefunItemStack("GILDED_IRON_CHESTPLATE", Material.GOLDEN_CHESTPLATE, "&6镀金铁胸甲");
    public static final SlimefunItemStack GILDED_IRON_LEGGINGS = new SlimefunItemStack("GILDED_IRON_LEGGINGS", Material.GOLDEN_LEGGINGS, "&6镀金铁护腿");
    public static final SlimefunItemStack GILDED_IRON_BOOTS = new SlimefunItemStack("GILDED_IRON_BOOTS", Material.GOLDEN_BOOTS, "&6镀金铁靴子");

    public static final SlimefunItemStack GOLD_HELMET = new SlimefunItemStack("GOLD_12K_HELMET", Material.GOLDEN_HELMET, "&6金头盔", "&912克拉");
    public static final SlimefunItemStack GOLD_CHESTPLATE = new SlimefunItemStack("GOLD_12K_CHESTPLATE", Material.GOLDEN_CHESTPLATE, "&6金胸甲", "&912克拉");
    public static final SlimefunItemStack GOLD_LEGGINGS = new SlimefunItemStack("GOLD_12K_LEGGINGS", Material.GOLDEN_LEGGINGS, "&6金护腿", "&912克拉");
    public static final SlimefunItemStack GOLD_BOOTS = new SlimefunItemStack("GOLD_12K_BOOTS", Material.GOLDEN_BOOTS, "&6金靴子", "&912克拉");

    public static final SlimefunItemStack SLIME_HELMET_STEEL = new SlimefunItemStack("SLIME_STEEL_HELMET", Material.IRON_HELMET, "&a&l史莱姆头盔", "&7&o已强化", "", "&a&o有弹性的感觉");
    public static final SlimefunItemStack SLIME_CHESTPLATE_STEEL = new SlimefunItemStack("SLIME_STEEL_CHESTPLATE", Material.IRON_CHESTPLATE, "&a&l史莱姆胸甲", "&7&o已强化", "", "&a&o有弹性的感觉");
    public static final SlimefunItemStack SLIME_LEGGINGS_STEEL = new SlimefunItemStack("SLIME_STEEL_LEGGINGS", Material.IRON_LEGGINGS, "&a&l史莱姆护腿", "&7&o已强化", "", "&a&o有弹性的感觉", "", "&9+ 速度");
    public static final SlimefunItemStack SLIME_BOOTS_STEEL = new SlimefunItemStack("SLIME_STEEL_BOOTS", Material.IRON_BOOTS, "&a&l史莱姆靴子", "&7&o已强化", "", "&a&o有弹性的感觉", "", "&9+ 跳跃提升", "&9+ 减免摔落伤害");

    public static final SlimefunItemStack BOOTS_OF_THE_STOMPER = new SlimefunItemStack("BOOTS_OF_THE_STOMPER", Material.LEATHER_BOOTS, Color.AQUA, "&b践踏者之靴", "", "&9你受到的所有摔落伤害", "&9将转给附近的生物/玩家", "", "&9+ 减免摔落伤害");

    static {
        Map<Enchantment, Integer> cactus = new HashMap<>();
        cactus.put(Enchantment.THORNS, 3);
        cactus.put(Enchantment.DURABILITY, 6);

        CACTUS_HELMET.addUnsafeEnchantments(cactus);
        CACTUS_CHESTPLATE.addUnsafeEnchantments(cactus);
        CACTUS_LEGGINGS.addUnsafeEnchantments(cactus);
        CACTUS_BOOTS.addUnsafeEnchantments(cactus);

        Map<Enchantment, Integer> damascus = new HashMap<>();
        damascus.put(Enchantment.DURABILITY, 5);
        damascus.put(Enchantment.PROTECTION_ENVIRONMENTAL, 5);

        DAMASCUS_STEEL_HELMET.addUnsafeEnchantments(damascus);
        DAMASCUS_STEEL_CHESTPLATE.addUnsafeEnchantments(damascus);
        DAMASCUS_STEEL_LEGGINGS.addUnsafeEnchantments(damascus);
        DAMASCUS_STEEL_BOOTS.addUnsafeEnchantments(damascus);

        Map<Enchantment, Integer> reinforced = new HashMap<>();
        reinforced.put(Enchantment.DURABILITY, 9);
        reinforced.put(Enchantment.PROTECTION_ENVIRONMENTAL, 9);

        REINFORCED_ALLOY_HELMET.addUnsafeEnchantments(reinforced);
        REINFORCED_ALLOY_CHESTPLATE.addUnsafeEnchantments(reinforced);
        REINFORCED_ALLOY_LEGGINGS.addUnsafeEnchantments(reinforced);
        REINFORCED_ALLOY_BOOTS.addUnsafeEnchantments(reinforced);

        Map<Enchantment, Integer> gilded = new HashMap<>();
        gilded.put(Enchantment.DURABILITY, 6);
        gilded.put(Enchantment.PROTECTION_ENVIRONMENTAL, 8);

        GILDED_IRON_HELMET.addUnsafeEnchantments(gilded);
        GILDED_IRON_CHESTPLATE.addUnsafeEnchantments(gilded);
        GILDED_IRON_LEGGINGS.addUnsafeEnchantments(gilded);
        GILDED_IRON_BOOTS.addUnsafeEnchantments(gilded);

        GOLD_HELMET.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        GOLD_CHESTPLATE.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        GOLD_LEGGINGS.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        GOLD_BOOTS.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        Map<Enchantment, Integer> slime = new HashMap<>();
        slime.put(Enchantment.DURABILITY, 4);
        slime.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        SLIME_HELMET_STEEL.addUnsafeEnchantments(slime);
        SLIME_CHESTPLATE_STEEL.addUnsafeEnchantments(slime);
        SLIME_LEGGINGS_STEEL.addUnsafeEnchantments(slime);
        SLIME_BOOTS_STEEL.addUnsafeEnchantments(slime);
    }

    /*		 Misc 		*/
    public static final SlimefunItemStack MAGIC_LUMP_1 = new SlimefunItemStack("MAGIC_LUMP_1", Material.GOLD_NUGGET, "&6魔法因子 &7- &eI", "", "&c&o等级: I");
    public static final SlimefunItemStack MAGIC_LUMP_2 = new SlimefunItemStack("MAGIC_LUMP_2", Material.GOLD_NUGGET, "&6魔法因子 &7- &eII", "", "&c&o等级: II");
    public static final SlimefunItemStack MAGIC_LUMP_3 = new SlimefunItemStack("MAGIC_LUMP_3", Material.GOLD_NUGGET, "&6魔法因子 &7- &eIII", "", "&c&o等级: III");
    public static final SlimefunItemStack ENDER_LUMP_1 = new SlimefunItemStack("ENDER_LUMP_1", Material.GOLD_NUGGET, "&5末影因子 &7- &eI", "", "&c&o等级: I");
    public static final SlimefunItemStack ENDER_LUMP_2 = new SlimefunItemStack("ENDER_LUMP_2", Material.GOLD_NUGGET, "&5末影因子 &7- &eII", "", "&c&o等级: II");
    public static final SlimefunItemStack ENDER_LUMP_3 = new SlimefunItemStack("ENDER_LUMP_3", Material.GOLD_NUGGET, "&5末影因子 &7- &eIII", "", "&c&o等级: III");
    public static final SlimefunItemStack MAGICAL_BOOK_COVER = new SlimefunItemStack("MAGICAL_BOOK_COVER", Material.PAPER, "&6魔法书皮", "", "&a&o用于各种魔法书");
    public static final SlimefunItemStack BASIC_CIRCUIT_BOARD = new SlimefunItemStack("BASIC_CIRCUIT_BOARD", Material.ACTIVATOR_RAIL, "&b基础电路板");
    public static final SlimefunItemStack ADVANCED_CIRCUIT_BOARD = new SlimefunItemStack("ADVANCED_CIRCUIT_BOARD", Material.POWERED_RAIL, "&b高级电路板");
    public static final SlimefunItemStack WHEAT_FLOUR = new SlimefunItemStack("WHEAT_FLOUR", Material.SUGAR, "&r小麦粉");
    public static final SlimefunItemStack STEEL_PLATE = new SlimefunItemStack("STEEL_PLATE", Material.PAPER, "&7&l钢板");
    public static final SlimefunItemStack BATTERY = new SlimefunItemStack("BATTERY", CustomHeadTexture.BATTERY, "&6电池");
    public static final SlimefunItemStack CARBON = new SlimefunItemStack("CARBON", CustomHeadTexture.CARBON, "&e碳");
    public static final SlimefunItemStack COMPRESSED_CARBON = new SlimefunItemStack("COMPRESSED_CARBON", CustomHeadTexture.COMPRESSED_CARBON, "&c压缩碳");
    public static final SlimefunItemStack CARBON_CHUNK = new SlimefunItemStack("CARBON_CHUNK", CustomHeadTexture.CARBON, "&4碳块");
    public static final SlimefunItemStack STEEL_THRUSTER = new SlimefunItemStack("STEEL_THRUSTER", Material.BUCKET, "&7&l钢推进器");
    public static final SlimefunItemStack POWER_CRYSTAL = new SlimefunItemStack("POWER_CRYSTAL", CustomHeadTexture.POWER_CRYSTAL, "&c&l能量水晶");
    public static final SlimefunItemStack CHAIN = new SlimefunItemStack("CHAIN", Material.STRING, "&b锁链");
    public static final SlimefunItemStack HOOK = new SlimefunItemStack("HOOK", Material.FLINT, "&b钩子");
    public static final SlimefunItemStack SIFTED_ORE = new SlimefunItemStack("SIFTED_ORE", Material.GUNPOWDER, "&6筛矿");
    public static final SlimefunItemStack STONE_CHUNK = new SlimefunItemStack("STONE_CHUNK", CustomHeadTexture.STONE_CHUNK, "&6石块");
    public static final SlimefunItemStack LAVA_CRYSTAL = new SlimefunItemStack("LAVA_CRYSTAL", CustomHeadTexture.LAVA_CRYSTAL, "&4岩浆水晶");
    public static final SlimefunItemStack SALT = new SlimefunItemStack("SALT", Material.SUGAR, "&r盐");
    public static final SlimefunItemStack CHEESE = new SlimefunItemStack("CHEESE", CustomHeadTexture.CHEESE, "&r黄油");
    public static final SlimefunItemStack BUTTER = new SlimefunItemStack("BUTTER", CustomHeadTexture.BUTTER, "&r奶酪");
    public static final SlimefunItemStack DUCT_TAPE = new SlimefunItemStack("DUCT_TAPE", CustomHeadTexture.DUCT_TAPE, "&8强力胶布", "", "&r可以用这个在自动铁砧里", "&r修复物品");
    public static final SlimefunItemStack HEAVY_CREAM = new SlimefunItemStack("HEAVY_CREAM", Material.SNOWBALL, "&r浓奶油");
    public static final SlimefunItemStack CRUSHED_ORE = new SlimefunItemStack("CRUSHED_ORE", Material.GUNPOWDER, "&6已粉碎的矿石");
    public static final SlimefunItemStack PULVERIZED_ORE = new SlimefunItemStack("PULVERIZED_ORE", Material.GUNPOWDER, "&6粉末状的矿石");
    public static final SlimefunItemStack PURE_ORE_CLUSTER = new SlimefunItemStack("PURE_ORE_CLUSTER", Material.GUNPOWDER, "&6纯矿簇");
    public static final SlimefunItemStack SMALL_URANIUM = new SlimefunItemStack("SMALL_URANIUM", CustomHeadTexture.URANIUM, "&c一小堆铀", "", LoreBuilder.radioactive(Radioactivity.LOW), "&4&o不需要防化套装");
    public static final SlimefunItemStack TINY_URANIUM = new SlimefunItemStack("TINY_URANIUM", CustomHeadTexture.URANIUM, "&c一小块铀", "", LoreBuilder.radioactive(Radioactivity.MODERATE), "&4&o需要防化套装");

    public static final SlimefunItemStack MAGNET = new SlimefunItemStack("MAGNET", CustomHeadTexture.MAGNET, "&c磁铁");
    public static final SlimefunItemStack NECROTIC_SKULL = new SlimefunItemStack("NECROTIC_SKULL", Material.WITHER_SKELETON_SKULL, "&c坏死颅骨");
    public static final SlimefunItemStack ESSENCE_OF_AFTERLIFE = new SlimefunItemStack("ESSENCE_OF_AFTERLIFE", Material.GUNPOWDER, "&4来世精华");
    public static final SlimefunItemStack ELECTRO_MAGNET = new SlimefunItemStack("ELECTRO_MAGNET", CustomHeadTexture.MAGNET, "&c电磁铁");
    public static final SlimefunItemStack HEATING_COIL = new SlimefunItemStack("HEATING_COIL", CustomHeadTexture.HEATING_COIL, "&c加热线圈");
    public static final SlimefunItemStack COOLING_UNIT = new SlimefunItemStack("COOLING_UNIT", CustomHeadTexture.COOLING_UNIT, "&b冷却装置");
    public static final SlimefunItemStack ELECTRIC_MOTOR = new SlimefunItemStack("ELECTRIC_MOTOR", CustomHeadTexture.MOTOR, "&c电动马达");
    public static final SlimefunItemStack CARGO_MOTOR = new SlimefunItemStack("CARGO_MOTOR", CustomHeadTexture.MOTOR, "&3货运马达");
    public static final SlimefunItemStack SCROLL_OF_DIMENSIONAL_TELEPOSITION = new SlimefunItemStack("SCROLL_OF_DIMENSIONAL_TELEPOSITION", Material.PAPER, "&6维度传送卷轴", "", "&c这个卷轴可以便携地创建", "&c一个临时的黑洞", "&c将附近的实体都传送至", "&c另一个维度上", "&c所有东西都反转了", "", "&r就是说: 让实体转180°");
    public static final SlimefunItemStack TOME_OF_KNOWLEDGE_SHARING = new SlimefunItemStack("TOME_OF_KNOWLEDGE_SHARING", Material.BOOK, "&6知识共享之书", "&7主人: &bNone", "", "&e右键&7 以绑定你的所有研究", "", "", "&e右键&7 以获得前任主人的所有研究");
    public static final SlimefunItemStack HARDENED_GLASS = new SlimefunItemStack("HARDENED_GLASS", Material.LIGHT_GRAY_STAINED_GLASS, "&7钢化玻璃", "", "&r可以抵抗爆炸");
    public static final SlimefunItemStack WITHER_PROOF_OBSIDIAN = new SlimefunItemStack("WITHER_PROOF_OBSIDIAN", Material.OBSIDIAN, "&5防凋零黑曜石", "", "&r可以抵抗爆炸和", "&r凋零的攻击");
    public static final SlimefunItemStack WITHER_PROOF_GLASS = new SlimefunItemStack("WITHER_PROOF_GLASS", Material.PURPLE_STAINED_GLASS, "&5防凋零玻璃", "", "&r可以抵抗爆炸和", "&r凋零的攻击");
    public static final SlimefunItemStack REINFORCED_PLATE = new SlimefunItemStack("REINFORCED_PLATE", Material.PAPER, "&7钢筋板");
    public static final SlimefunItemStack ANCIENT_PEDESTAL = new SlimefunItemStack("ANCIENT_PEDESTAL", Material.DISPENSER, "&d古代基座", "", "&5古代祭坛的一部分");
    public static final SlimefunItemStack ANCIENT_ALTAR = new SlimefunItemStack("ANCIENT_ALTAR", Material.ENCHANTING_TABLE, "&d古代祭坛", "", "&5在世界里建造祭坛", "&5并用古老的仪式合成物品");
    public static final SlimefunItemStack COPPER_WIRE = new SlimefunItemStack("COPPER_WIRE", Material.STRING, "&6铜线", "", "&6电子配件中的重要组成部分");

    public static final SlimefunItemStack RAINBOW_WOOL = new SlimefunItemStack("RAINBOW_WOOL", Material.WHITE_WOOL, "&5彩虹羊毛", "", "&d轮番展现彩虹的颜色!");
    public static final SlimefunItemStack RAINBOW_GLASS = new SlimefunItemStack("RAINBOW_GLASS", Material.WHITE_STAINED_GLASS, "&5彩虹玻璃", "", "&d轮番展现彩虹的颜色!");
    public static final SlimefunItemStack RAINBOW_CLAY = new SlimefunItemStack("RAINBOW_CLAY", Material.WHITE_TERRACOTTA, "&5彩虹粘土块", "", "&d轮番展现彩虹的颜色!");
    public static final SlimefunItemStack RAINBOW_GLASS_PANE = new SlimefunItemStack("RAINBOW_GLASS_PANE", Material.WHITE_STAINED_GLASS_PANE, "&5彩虹玻璃板", "", "&d轮番展现彩虹的颜色!");
    public static final SlimefunItemStack RAINBOW_CONCRETE = new SlimefunItemStack("RAINBOW_CONCRETE", Material.WHITE_CONCRETE, "&5彩虹混凝土", "", "&d轮番展现彩虹的颜色!");
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA", Material.WHITE_GLAZED_TERRACOTTA, "&5Rainbow Glazed Terracotta", "", "&d轮番展现彩虹的颜色!");

    private static final String CHRISTMAS = ChatUtils.christmas("< 圣诞节版 >");

    public static final SlimefunItemStack RAINBOW_WOOL_XMAS = new SlimefunItemStack("RAINBOW_WOOL_XMAS", Material.WHITE_WOOL, "&5彩虹羊毛 &7(圣诞节版)", "", CHRISTMAS);
    public static final SlimefunItemStack RAINBOW_GLASS_XMAS = new SlimefunItemStack("RAINBOW_GLASS_XMAS", Material.WHITE_STAINED_GLASS, "&5彩虹玻璃 &7(圣诞节版)", "", CHRISTMAS);
    public static final SlimefunItemStack RAINBOW_CLAY_XMAS = new SlimefunItemStack("RAINBOW_CLAY_XMAS", Material.WHITE_TERRACOTTA, "&5彩虹粘土块 &7(圣诞节版)", "", CHRISTMAS);
    public static final SlimefunItemStack RAINBOW_GLASS_PANE_XMAS = new SlimefunItemStack("RAINBOW_GLASS_PANE_XMAS", Material.WHITE_STAINED_GLASS_PANE, "&5彩虹玻璃板 &7(圣诞节版)", "", CHRISTMAS);
    public static final SlimefunItemStack RAINBOW_CONCRETE_XMAS = new SlimefunItemStack("RAINBOW_CONCRETE_XMAS", Material.WHITE_CONCRETE, "&5彩虹混凝土 &7(圣诞节版)", "", CHRISTMAS);
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA_XMAS = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_XMAS", Material.WHITE_GLAZED_TERRACOTTA, "&5彩虹带釉陶瓦 &7(圣诞节版)", "", CHRISTMAS);

    private static final String VALENTINES_DAY = "&d< 情人节版 >";

    public static final SlimefunItemStack RAINBOW_WOOL_VALENTINE = new SlimefunItemStack("RAINBOW_WOOL_VALENTINE", Material.PINK_WOOL, "&5彩虹羊毛 &7(情人节版)", "", VALENTINES_DAY);
    public static final SlimefunItemStack RAINBOW_GLASS_VALENTINE = new SlimefunItemStack("RAINBOW_GLASS_VALENTINE", Material.PINK_STAINED_GLASS, "&5彩虹玻璃 &7(情人节版)", "", VALENTINES_DAY);
    public static final SlimefunItemStack RAINBOW_CLAY_VALENTINE = new SlimefunItemStack("RAINBOW_CLAY_VALENTINE", Material.PINK_TERRACOTTA, "&5彩虹粘土块 &7(情人节版)", "", VALENTINES_DAY);
    public static final SlimefunItemStack RAINBOW_GLASS_PANE_VALENTINE = new SlimefunItemStack("RAINBOW_GLASS_PANE_VALENTINE", Material.PINK_STAINED_GLASS_PANE, "&5彩虹玻璃板 &7(情人节版)", "", VALENTINES_DAY);
    public static final SlimefunItemStack RAINBOW_CONCRETE_VALENTINE = new SlimefunItemStack("RAINBOW_CONCRETE_VALENTINE", Material.PINK_CONCRETE, "&5彩虹混凝土 &7(情人节版)", "", VALENTINES_DAY);
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA_VALENTINE = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_VALENTINE", Material.PINK_GLAZED_TERRACOTTA, "&5彩虹带釉陶瓦 &7(情人节版)", "", VALENTINES_DAY);

    private static final String HALLOWEEN = "&6< 万圣节版 >";

    public static final SlimefunItemStack RAINBOW_WOOL_HALLOWEEN = new SlimefunItemStack("RAINBOW_WOOL_HALLOWEEN", Material.ORANGE_WOOL, "&5彩虹羊毛 &7(万圣节版)", "", HALLOWEEN);
    public static final SlimefunItemStack RAINBOW_GLASS_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLASS_HALLOWEEN", Material.ORANGE_STAINED_GLASS, "&5彩虹玻璃 &7(万圣节版)", "", HALLOWEEN);
    public static final SlimefunItemStack RAINBOW_CLAY_HALLOWEEN = new SlimefunItemStack("RAINBOW_CLAY_HALLOWEEN", Material.ORANGE_TERRACOTTA, "&5彩虹粘土块 &7(万圣节版)", "", HALLOWEEN);
    public static final SlimefunItemStack RAINBOW_GLASS_PANE_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLASS_PANE_HALLOWEEN", Material.ORANGE_STAINED_GLASS_PANE, "&5彩虹玻璃板 &7(万圣节版)", "", HALLOWEEN);
    public static final SlimefunItemStack RAINBOW_CONCRETE_HALLOWEEN = new SlimefunItemStack("RAINBOW_CONCRETE_HALLOWEEN", Material.ORANGE_CONCRETE, "&5彩虹混凝土 &7(万圣节版)", "", HALLOWEEN);
    public static final SlimefunItemStack RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN = new SlimefunItemStack("RAINBOW_GLAZED_TERRACOTTA_HALLOWEEN", Material.ORANGE_GLAZED_TERRACOTTA, "&5彩虹带釉陶瓦a &7(万圣节版)", "", HALLOWEEN);

    /*		锭 		*/
    public static final SlimefunItemStack COPPER_INGOT = new SlimefunItemStack("COPPER_INGOT", Material.BRICK, "&b铜锭");
    public static final SlimefunItemStack TIN_INGOT = new SlimefunItemStack("TIN_INGOT", Material.IRON_INGOT, "&b锡锭");
    public static final SlimefunItemStack SILVER_INGOT = new SlimefunItemStack("SILVER_INGOT", Material.IRON_INGOT, "&b银锭");
    public static final SlimefunItemStack ALUMINUM_INGOT = new SlimefunItemStack("ALUMINUM_INGOT", Material.IRON_INGOT, "&b铝锭");
    public static final SlimefunItemStack LEAD_INGOT = new SlimefunItemStack("LEAD_INGOT", Material.IRON_INGOT, "&b铅锭");
    public static final SlimefunItemStack ZINC_INGOT = new SlimefunItemStack("ZINC_INGOT", Material.IRON_INGOT, "&b锌锭");
    public static final SlimefunItemStack MAGNESIUM_INGOT = new SlimefunItemStack("MAGNESIUM_INGOT", Material.IRON_INGOT, "&b镁锭");

    /*		Alloy (Carbon + Iron)	*/
    public static final SlimefunItemStack STEEL_INGOT = new SlimefunItemStack("STEEL_INGOT", Material.IRON_INGOT, "&b钢锭");
    /*		Alloy (Copper + Tin)	*/
    public static final SlimefunItemStack BRONZE_INGOT = new SlimefunItemStack("BRONZE_INGOT", Material.BRICK, "&b青铜锭");
    /*		Alloy (Copper + Aluminum)	*/
    public static final SlimefunItemStack DURALUMIN_INGOT = new SlimefunItemStack("DURALUMIN_INGOT", Material.IRON_INGOT, "&b硬铝锭");
    /*		Alloy (Copper + Silver)	*/
    public static final SlimefunItemStack BILLON_INGOT = new SlimefunItemStack("BILLON_INGOT", Material.IRON_INGOT, "&b银铜合金锭");
    /*		Alloy (Copper + Zinc)	*/
    public static final SlimefunItemStack BRASS_INGOT = new SlimefunItemStack("BRASS_INGOT", Material.GOLD_INGOT, "&b黄铜锭");
    /*		Alloy (Aluminum + Brass)	*/
    public static final SlimefunItemStack ALUMINUM_BRASS_INGOT = new SlimefunItemStack("ALUMINUM_BRASS_INGOT", Material.GOLD_INGOT, "&b铝黄铜锭");
    /*		Alloy (Aluminum + Bronze)	*/
    public static final SlimefunItemStack ALUMINUM_BRONZE_INGOT = new SlimefunItemStack("ALUMINUM_BRONZE_INGOT", Material.GOLD_INGOT, "&b铝青铜锭");
    /*		Alloy (Gold + Silver + Copper)	*/
    public static final SlimefunItemStack CORINTHIAN_BRONZE_INGOT = new SlimefunItemStack("CORINTHIAN_BRONZE_INGOT", Material.GOLD_INGOT, "&b科林斯青铜锭");
    /*		Alloy (Lead + Tin)	*/
    public static final SlimefunItemStack SOLDER_INGOT = new SlimefunItemStack("SOLDER_INGOT", Material.IRON_INGOT, "&b焊锡锭");
    /*		Alloy (Steel + Iron + Carbon)	*/
    public static final SlimefunItemStack DAMASCUS_STEEL_INGOT = new SlimefunItemStack("DAMASCUS_STEEL_INGOT", Material.IRON_INGOT, "&b大马士革钢锭");
    /*		Alloy (大马士革钢 + 硬铝 + Compressed Carbon + Aluminium Bronze)	*/
    public static final SlimefunItemStack HARDENED_METAL_INGOT = new SlimefunItemStack("HARDENED_METAL_INGOT", Material.IRON_INGOT, "&b&l硬化金属");
    /*		Alloy (Hardened Metal + Corinthian Bronze + Solder + Billon + 大马士革钢)	*/
    public static final SlimefunItemStack REINFORCED_ALLOY_INGOT = new SlimefunItemStack("REINFORCED_ALLOY_INGOT", Material.IRON_INGOT, "&b&l强化合金锭");
    /*		Alloy (Iron + Silicon)		*/
    public static final SlimefunItemStack FERROSILICON = new SlimefunItemStack("FERROSILICON", Material.IRON_INGOT, "&b硅铁");
    /*		Alloy (Iron + Gold)			*/
    public static final SlimefunItemStack GILDED_IRON = new SlimefunItemStack("GILDED_IRON", Material.GOLD_INGOT, "&6&l镀金铁锭");
    /*		Alloy (Redston + Ferrosilicon)	*/
    public static final SlimefunItemStack REDSTONE_ALLOY = new SlimefunItemStack("REDSTONE_ALLOY", Material.BRICK, "&c红石合金锭");
    /*		Alloy (Iron + Copper)		*/
    public static final SlimefunItemStack NICKEL_INGOT = new SlimefunItemStack("NICKEL_INGOT", Material.IRON_INGOT, "&b镍锭");
    /*		Alloy (Nickel + Iron + Copper)		*/
    public static final SlimefunItemStack COBALT_INGOT = new SlimefunItemStack("COBALT_INGOT", Material.IRON_INGOT, "&9钴锭");

    /*		Gold		*/
    public static final SlimefunItemStack GOLD_4K = new SlimefunItemStack("GOLD_4K", Material.GOLD_INGOT, "&r金锭 &7(4克拉)");
    public static final SlimefunItemStack GOLD_6K = new SlimefunItemStack("GOLD_6K", Material.GOLD_INGOT, "&r金锭 &7(6克拉)");
    public static final SlimefunItemStack GOLD_8K = new SlimefunItemStack("GOLD_8K", Material.GOLD_INGOT, "&r金锭 &7(8克拉)");
    public static final SlimefunItemStack GOLD_10K = new SlimefunItemStack("GOLD_10K", Material.GOLD_INGOT, "&r金锭 &7(10克拉)");
    public static final SlimefunItemStack GOLD_12K = new SlimefunItemStack("GOLD_12K", Material.GOLD_INGOT, "&r金锭 &7(12克拉)");
    public static final SlimefunItemStack GOLD_14K = new SlimefunItemStack("GOLD_14K", Material.GOLD_INGOT, "&r金锭 &7(14克拉)");
    public static final SlimefunItemStack GOLD_16K = new SlimefunItemStack("GOLD_16K", Material.GOLD_INGOT, "&r金锭 &7(16克拉)");
    public static final SlimefunItemStack GOLD_18K = new SlimefunItemStack("GOLD_18K", Material.GOLD_INGOT, "&r金锭 &7(18克拉)");
    public static final SlimefunItemStack GOLD_20K = new SlimefunItemStack("GOLD_20K", Material.GOLD_INGOT, "&r金锭 &7(20克拉)");
    public static final SlimefunItemStack GOLD_22K = new SlimefunItemStack("GOLD_22K", Material.GOLD_INGOT, "&r金锭 &7(22克拉)");
    public static final SlimefunItemStack GOLD_24K = new SlimefunItemStack("GOLD_24K", Material.GOLD_INGOT, "&r金锭 &7(24克拉)");

    /*		 Dusts 		*/
    public static final SlimefunItemStack IRON_DUST = new SlimefunItemStack("IRON_DUST", Material.GUNPOWDER, "&6铁粉");
    public static final SlimefunItemStack GOLD_DUST = new SlimefunItemStack("GOLD_DUST", Material.GLOWSTONE_DUST, "&6金粉");
    public static final SlimefunItemStack TIN_DUST = new SlimefunItemStack("TIN_DUST", Material.SUGAR, "&6锡粉");
    public static final SlimefunItemStack COPPER_DUST = new SlimefunItemStack("COPPER_DUST", Material.GLOWSTONE_DUST, "&6铜粉");
    public static final SlimefunItemStack SILVER_DUST = new SlimefunItemStack("SILVER_DUST", Material.SUGAR, "&6银粉");
    public static final SlimefunItemStack ALUMINUM_DUST = new SlimefunItemStack("ALUMINUM_DUST", Material.SUGAR, "&6铝粉");
    public static final SlimefunItemStack LEAD_DUST = new SlimefunItemStack("LEAD_DUST", Material.GUNPOWDER, "&6铅粉");
    public static final SlimefunItemStack SULFATE = new SlimefunItemStack("SULFATE", Material.GLOWSTONE_DUST, "&6硫酸盐");
    public static final SlimefunItemStack ZINC_DUST = new SlimefunItemStack("ZINC_DUST", Material.SUGAR, "&6锌粉");
    public static final SlimefunItemStack MAGNESIUM_DUST = new SlimefunItemStack("MAGNESIUM_DUST", Material.SUGAR, "&6镁");
    public static final SlimefunItemStack SILICON = new SlimefunItemStack("SILICON", Material.FIREWORK_STAR, "&6硅");
    public static final SlimefunItemStack GOLD_24K_BLOCK = new SlimefunItemStack("GOLD_24K_BLOCK", Material.GOLD_BLOCK, "&r&r金块 &7(24克拉)");

    /*		 Gems 		*/
    public static final SlimefunItemStack SYNTHETIC_DIAMOND = new SlimefunItemStack("SYNTHETIC_DIAMOND", Material.DIAMOND, "&b人造钻石", "", "&r可以在工作台中作为钻石使用");
    public static final SlimefunItemStack SYNTHETIC_EMERALD = new SlimefunItemStack("SYNTHETIC_EMERALD", Material.EMERALD, "&b人造绿宝石", "", "&r可以用来和村民交易");
    public static final SlimefunItemStack SYNTHETIC_SAPPHIRE = new SlimefunItemStack("SYNTHETIC_SAPPHIRE", CustomHeadTexture.SAPPHIRE, "&b人造蓝宝石", "", "&r可以在工作台中作为青金石使用");
    public static final SlimefunItemStack CARBONADO = new SlimefunItemStack("CARBONADO", CustomHeadTexture.CARBONADO, "&b&l黑金刚石", "", "&7&o\"黑色钻石\"");
    public static final SlimefunItemStack RAW_CARBONADO = new SlimefunItemStack("RAW_CARBONADO", CustomHeadTexture.RAW_CARBONADO, "&b黑金刚石原矿");

    public static final SlimefunItemStack URANIUM = new SlimefunItemStack("URANIUM", CustomHeadTexture.URANIUM, "&4铀", "", LoreBuilder.radioactive(Radioactivity.HIGH), "&4&o需要防化套装");
    public static final SlimefunItemStack NEPTUNIUM = new SlimefunItemStack("NEPTUNIUM", CustomHeadTexture.NEPTUNIUM, "&a镎", "", LoreBuilder.radioactive(Radioactivity.HIGH), "&4&o需要防化套装");
    public static final SlimefunItemStack PLUTONIUM = new SlimefunItemStack("PLUTONIUM", CustomHeadTexture.PLUTONIUM, "&7钚", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), "&4&o需要防化套装");
    public static final SlimefunItemStack BOOSTED_URANIUM = new SlimefunItemStack("BOOSTED_URANIUM", CustomHeadTexture.BOOSTED_URANIUM, "&2高纯度铀", "", LoreBuilder.radioactive(Radioactivity.VERY_HIGH), "&4&o需要防化套装");

    /*		Talisman		*/
    public static final SlimefunItemStack COMMON_TALISMAN = new SlimefunItemStack("COMMON_TALISMAN", Material.EMERALD, "&6普通护身符");
    public static final SlimefunItemStack ENDER_TALISMAN = new SlimefunItemStack("ENDER_TALISMAN", Material.EMERALD, "&5末影护身符");

    public static final SlimefunItemStack TALISMAN_ANVIL = new SlimefunItemStack("ANVIL_TALISMAN", Material.EMERALD, "&a铁砧护身符", "", "&r每个护身符可以防止", "&r一个工具因耐久不足而损坏", "&r然后就会被消耗", "", "&4&l警告:", "&4由于过于强大的工具的复杂性", "&4此护身符不能修复过于强大的工具");
    public static final SlimefunItemStack TALISMAN_MINER = new SlimefunItemStack("MINER_TALISMAN", Material.EMERALD, "&a矿工护身符", "", "&r当这个护身符在你的背包里时", "&r将有 20% 的几率双倍掉落", "&r你挖出的矿物");
    public static final SlimefunItemStack TALISMAN_HUNTER = new SlimefunItemStack("HUNTER_TALISMAN", Material.EMERALD, "&a猎人护身符", "", "&r当这个护身符在你的背包里时", "&r将有 20% 的几率双倍掉落", "&r你杀死的生物的掉落物");
    public static final SlimefunItemStack TALISMAN_LAVA = new SlimefunItemStack("LAVA_TALISMAN", Material.EMERALD, "&a岩浆行者护身符", "", "&r当这个护身符在你的背包里时", "&r获得火焰保护效果", "&r仅在你行走在岩浆上时可用", "&r然后就会被消耗");
    public static final SlimefunItemStack TALISMAN_WATER = new SlimefunItemStack("WATER_TALISMAN", Material.EMERALD, "&a潜水者护身符", "", "&r当这个护身符在你的背包里时", "&r一旦你即将溺水而死", "&r护身符将会给予你水下呼吸", "&r然后就会被消耗");
    public static final SlimefunItemStack TALISMAN_ANGEL = new SlimefunItemStack("ANGEL_TALISMAN", Material.EMERALD, "&a天使护身符", "", "&r当这个护身符在你的背包里时", "&r有 75% 的几率减免你的摔落伤害");
    public static final SlimefunItemStack TALISMAN_FIRE = new SlimefunItemStack("FIRE_TALISMAN", Material.EMERALD, "&a消防员护身符", "", "&r当这个护身符在你的背包里时", "&r在你着火时", "&r给予你防火效果", "&r然后就会被消耗");
    public static final SlimefunItemStack TALISMAN_MAGICIAN = new SlimefunItemStack("MAGICIAN_TALISMAN", Material.EMERALD, "&a魔法师护身符", "", "&r当这个护身符在你的背包里时", "&r在附魔时有 80% 的几率", "&r得到一个额外的附魔");
    public static final SlimefunItemStack TALISMAN_TRAVELLER = new SlimefunItemStack("TRAVELLER_TALISMAN", Material.EMERALD, "&a旅行者护身符", "", "&r当这个护身符在你的背包里时", "&r在你开始疾跑时有 60% 的几率", "&r给予你速度效果");
    public static final SlimefunItemStack TALISMAN_WARRIOR = new SlimefunItemStack("WARRIOR_TALISMAN", Material.EMERALD, "&a战士护身符", "", "&r当这个护身符在你的背包里时", "&r你被攻击后将会获得力量3的效果", "&r然后就会被消耗");
    public static final SlimefunItemStack TALISMAN_KNIGHT = new SlimefunItemStack("KNIGHT_TALISMAN", Material.EMERALD, "&a骑士护身符", "", "&r当这个护身符在你的背包里时", "&r在你被攻击后", "&r有 30% 的几率获得五秒的生命恢复", "&r然后就会被消耗");
    public static final SlimefunItemStack TALISMAN_WHIRLWIND = new SlimefunItemStack("WHIRLWIND_TALISMAN", Material.EMERALD, "&a旋风护身符", "", "&r当这个护身符在你的背包里时", "&r将有 60% 的几率", "&r反弹所有冲向你的弹射物");
    public static final SlimefunItemStack TALISMAN_WIZARD = new SlimefunItemStack("WIZARD_TALISMAN", Material.EMERALD, "&a巫师护身符", "", "&r当这个护身符在你的背包里时", "&r在你附魔时可获得时运4/5", "&r但它也可能降低该物品", "&r其他附魔的等级");

    /*		Staves		*/
    public static final SlimefunItemStack STAFF_ELEMENTAL = new SlimefunItemStack("STAFF_ELEMENTAL", Material.STICK, "&6元素法杖");
    public static final SlimefunItemStack STAFF_WIND = new SlimefunItemStack("STAFF_ELEMENTAL_WIND", Material.STICK, "&6元素法杖 &7- &b&o风", "", "&7元素: &b&o风", "", "&7&e右键&7 以将你吹飞");
    public static final SlimefunItemStack STAFF_FIRE = new SlimefunItemStack("STAFF_ELEMENTAL_FIRE", Material.STICK, "&6元素法杖 &7- &c&o火", "", "&7元素: &c&o火", "", "&c让火焰净化一切!");
    public static final SlimefunItemStack STAFF_WATER = new SlimefunItemStack("STAFF_ELEMENTAL_WATER", Material.STICK, "&6元素法杖 &7- &1&o水", "", "&7元素: &1&o水", "", "&7&e右键&7 以灭掉你身上的火");
    public static final SlimefunItemStack STAFF_STORM = new SlimefunItemStack("STAFF_ELEMENTAL_STORM", Material.STICK, "&6元素法杖 &7- &8&o雷", "", "&7元素: &8&o雷", "", "&e右键&7 召唤一道闪电", "&7剩余 &e" + StormStaff.MAX_USES + " 次");

    static {
        STAFF_WIND.addUnsafeEnchantment(Enchantment.LUCK, 1);
        STAFF_FIRE.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 5);
        STAFF_WATER.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
        STAFF_STORM.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
    }

    /*		 Machines 		*/
    public static final SlimefunItemStack GRIND_STONE = new SlimefunItemStack("GRIND_STONE", Material.DISPENSER, "&b磨石", "", "&a&o更高效的磨物品");
    public static final SlimefunItemStack ARMOR_FORGE = new SlimefunItemStack("ARMOR_FORGE", Material.ANVIL, "&6盔甲锻造台", "", "&a&o给你创造强大防具的力量");
    public static final SlimefunItemStack MAKESHIFT_SMELTERY;
    public static final SlimefunItemStack SMELTERY = new SlimefunItemStack("SMELTERY", Material.FURNACE, "&6冶炼炉", "", "&a能够冶炼金属的高温炉");
    public static final SlimefunItemStack IGNITION_CHAMBER = new SlimefunItemStack("IGNITION_CHAMBER", Material.DROPPER, "&4自动点火机", "&r以防你的冶炼炉失去火焰", "&r把打火石放入后", "&r放置于冶炼炉的发射器旁边");
    public static final SlimefunItemStack ORE_CRUSHER = new SlimefunItemStack("ORE_CRUSHER", Material.DISPENSER, "&b矿石粉碎机", "", "&a&o粉碎矿石并且得到双倍的矿粉");
    public static final SlimefunItemStack COMPRESSOR = new SlimefunItemStack("COMPRESSOR", Material.PISTON, "&b压缩机", "", "&a压缩物品");
    public static final SlimefunItemStack PRESSURE_CHAMBER = new SlimefunItemStack("PRESSURE_CHAMBER", Material.GLASS, "&b压力机", "", "&a压缩更多的物品");
    public static final SlimefunItemStack MAGIC_WORKBENCH = new SlimefunItemStack("MAGIC_WORKBENCH", Material.CRAFTING_TABLE, "&6魔法工作台", "", "&d给物品注入魔法能量");
    public static final SlimefunItemStack ORE_WASHER = new SlimefunItemStack("ORE_WASHER", Material.CAULDRON, "&6洗矿机", "", "&a&o清洗筛矿变成过滤矿石", "&a&o并且给你一些小石块");
    public static final SlimefunItemStack TABLE_SAW;
    public static final SlimefunItemStack COMPOSTER = new SlimefunItemStack("COMPOSTER", Material.CAULDRON, "&a搅拌机", "", "&a&o随着时间的推移可以转换各种材料...");
    public static final SlimefunItemStack ENHANCED_CRAFTING_TABLE = new SlimefunItemStack("ENHANCED_CRAFTING_TABLE", Material.CRAFTING_TABLE, "&e高级工作台", "", "&a&o一个原始的工作台", "&a&o无法承受强大的力量...");
    public static final SlimefunItemStack CRUCIBLE = new SlimefunItemStack("CRUCIBLE", Material.CAULDRON, "&c坩埚", "", "&a&o用来把物品变为液体");
    public static final SlimefunItemStack JUICER = new SlimefunItemStack("JUICER", Material.GLASS_BOTTLE, "&a榨汁机", "", "&a让你创造美味的果汁");

    public static final SlimefunItemStack INDUSTRIAL_MINER = new SlimefunItemStack("INDUSTRIAL_MINER", Material.GOLDEN_PICKAXE, "&b工业矿机", "", "&r工业矿机能够开采其下方 7x7", "&r的范围内的所有矿物.", "&r将煤炭或其他燃料放入", "&r机器的箱子中为它添加燃料.");
    public static final SlimefunItemStack ADVANCED_INDUSTRIAL_MINER = new SlimefunItemStack("ADVANCED_INDUSTRIAL_MINER", Material.DIAMOND_PICKAXE, "&c进阶工业矿机", "", "&r进阶工业矿机能够开采其下方 11x11", "&r的范围中的所有矿物.", "&r将燃油或岩浆放入机器的箱子中", "&r为它添加燃料.", "", "&a+ 精准采集");

    static {
        ItemMeta meta = INDUSTRIAL_MINER.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        INDUSTRIAL_MINER.setItemMeta(meta);

        ItemMeta meta2 = ADVANCED_INDUSTRIAL_MINER.getItemMeta();
        meta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ADVANCED_INDUSTRIAL_MINER.setItemMeta(meta2);
    }

    public static final SlimefunItemStack SOLAR_PANEL = new SlimefunItemStack("SOLAR_PANEL", Material.DAYLIGHT_DETECTOR, "&b太阳能板", "", "&a&o将阳光变为能量");

    public static final SlimefunItemStack AUTOMATED_PANNING_MACHINE = new SlimefunItemStack("AUTOMATED_PANNING_MACHINE", Material.BOWL, "&a自动淘金机", "", "&a&o升级版淘金筛");
    public static final SlimefunItemStack OUTPUT_CHEST = new SlimefunItemStack("OUTPUT_CHEST", Material.CHEST, "&4物品输出箱", "", "&c&o将它放在基础机械的发射器旁边", "&c&o做出的物品会放入箱子里面");
    public static final SlimefunItemStack HOLOGRAM_PROJECTOR = new SlimefunItemStack("HOLOGRAM_PROJECTOR", Material.QUARTZ_SLAB, "&b全息投影仪", "", "&r投影出可编辑的全息文字");

    /*		 Enhanced Furnaces 		*/
    public static final SlimefunItemStack ENHANCED_FURNACE = new SlimefunItemStack("ENHANCED_FURNACE", Material.FURNACE, "&7强化熔炉 - &eI", "", "&7燃烧速度: &e1x", "&7燃料效率: &e1x", "&7产物翻倍倍数: &e1x");
    public static final SlimefunItemStack ENHANCED_FURNACE_2 = new SlimefunItemStack("ENHANCED_FURNACE_2", Material.FURNACE, "&7强化熔炉 - &eII", "", "&7燃烧速度: &e2x", "&7燃料效率: &e1x", "&7产物翻倍倍数: &e1x");
    public static final SlimefunItemStack ENHANCED_FURNACE_3 = new SlimefunItemStack("ENHANCED_FURNACE_3", Material.FURNACE, "&7强化熔炉 - &eIII", "", "&7燃烧速度: &e2x", "&7燃料效率: &e2x", "&7产物翻倍倍数: &e1x");
    public static final SlimefunItemStack ENHANCED_FURNACE_4 = new SlimefunItemStack("ENHANCED_FURNACE_4", Material.FURNACE, "&7强化熔炉 - &eIV", "", "&7燃烧速度: &e3x", "&7燃料效率: &e2x", "&7产物翻倍倍数: &e1x");
    public static final SlimefunItemStack ENHANCED_FURNACE_5 = new SlimefunItemStack("ENHANCED_FURNACE_5", Material.FURNACE, "&7强化熔炉 - &eV", "", "&7燃烧速度: &e3x", "&7燃料效率: &e2x", "&7产物翻倍倍数: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_6 = new SlimefunItemStack("ENHANCED_FURNACE_6", Material.FURNACE, "&7强化熔炉 - &eVI", "", "&7燃烧速度: &e3x", "&7燃料效率: &e3x", "&7产物翻倍倍数: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_7 = new SlimefunItemStack("ENHANCED_FURNACE_7", Material.FURNACE, "&7强化熔炉 - &eVII", "", "&7燃烧速度: &e4x", "&7燃料效率: &e3x", "&7产物翻倍倍数: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_8 = new SlimefunItemStack("ENHANCED_FURNACE_8", Material.FURNACE, "&7强化熔炉 - &eVIII", "", "&7燃烧速度: &e4x", "&7燃料效率: &e4x", "&7产物翻倍倍数: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_9 = new SlimefunItemStack("ENHANCED_FURNACE_9", Material.FURNACE, "&7强化熔炉 - &eIX", "", "&7燃烧速度: &e5x", "&7燃料效率: &e4x", "&7产物翻倍倍数: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_10 = new SlimefunItemStack("ENHANCED_FURNACE_10", Material.FURNACE, "&7强化熔炉 - &eX", "", "&7燃烧速度: &e5x", "&7燃料效率: &e5x", "&7产物翻倍倍数: &e2x");
    public static final SlimefunItemStack ENHANCED_FURNACE_11 = new SlimefunItemStack("ENHANCED_FURNACE_11", Material.FURNACE, "&7强化熔炉 - &eXI", "", "&7燃烧速度: &e5x", "&7燃料效率: &e5x", "&7产物翻倍倍数: &e3x");
    public static final SlimefunItemStack REINFORCED_FURNACE = new SlimefunItemStack("REINFORCED_FURNACE", Material.FURNACE, "&7强化合金熔炉", "", "&7燃烧速度: &e10x", "&7燃料效率: &e10x", "&7产物翻倍倍数: &e3x");
    public static final SlimefunItemStack CARBONADO_EDGED_FURNACE = new SlimefunItemStack("CARBONADO_EDGED_FURNACE", Material.FURNACE, "&7黑金刚石镶边熔炉", "", "&7燃烧速度: &e20x", "&7燃料效率: &e10x", "&7产物翻倍倍数: &e3x");

    public static final SlimefunItemStack BLOCK_PLACER = new SlimefunItemStack("BLOCK_PLACER", Material.DISPENSER, "&a方块放置机", "", "&r所有在这个发射器内的方块", "&r都会被自动放置");

    /*		Soulbound Items		*/
    public static final SlimefunItemStack SOULBOUND_SWORD = new SlimefunItemStack("SOULBOUND_SWORD", Material.DIAMOND_SWORD, "&c灵魂绑定剑");
    public static final SlimefunItemStack SOULBOUND_BOW = new SlimefunItemStack("SOULBOUND_BOW", Material.BOW, "&c灵魂绑定弓");
    public static final SlimefunItemStack SOULBOUND_PICKAXE = new SlimefunItemStack("SOULBOUND_PICKAXE", Material.DIAMOND_PICKAXE, "&c灵魂绑定镐");
    public static final SlimefunItemStack SOULBOUND_AXE = new SlimefunItemStack("SOULBOUND_AXE", Material.DIAMOND_AXE, "&c灵魂绑定斧");
    public static final SlimefunItemStack SOULBOUND_SHOVEL = new SlimefunItemStack("SOULBOUND_SHOVEL", Material.DIAMOND_SHOVEL, "&c灵魂绑定铲");
    public static final SlimefunItemStack SOULBOUND_HOE = new SlimefunItemStack("SOULBOUND_HOE", Material.DIAMOND_HOE, "&c灵魂绑定锄");

    public static final SlimefunItemStack SOULBOUND_HELMET = new SlimefunItemStack("SOULBOUND_HELMET", Material.DIAMOND_HELMET, "&c灵魂绑定头盔");
    public static final SlimefunItemStack SOULBOUND_CHESTPLATE = new SlimefunItemStack("SOULBOUND_CHESTPLATE", Material.DIAMOND_CHESTPLATE, "&c灵魂绑定胸甲");
    public static final SlimefunItemStack SOULBOUND_LEGGINGS = new SlimefunItemStack("SOULBOUND_LEGGINGS", Material.DIAMOND_LEGGINGS, "&c灵魂绑定护腿");
    public static final SlimefunItemStack SOULBOUND_BOOTS = new SlimefunItemStack("SOULBOUND_BOOTS", Material.DIAMOND_BOOTS, "&c灵魂绑定靴子");
    public static final SlimefunItemStack SOULBOUND_TRIDENT = new SlimefunItemStack("SOULBOUND_TRIDENT", Material.TRIDENT, "&c灵魂绑定三叉戟");

    /*		Runes				*/
    /* Runes */
    public static final SlimefunItemStack BLANK_RUNE = new SlimefunItemStack("BLANK_RUNE", new ColoredFireworkStar(Color.BLACK, "&8空白符文"));

    public static final SlimefunItemStack AIR_RUNE = new SlimefunItemStack("ANCIENT_RUNE_AIR", new ColoredFireworkStar(Color.AQUA, "&7古代符文 &8&l[&b&l气&8&l]"));
    public static final SlimefunItemStack WATER_RUNE = new SlimefunItemStack("ANCIENT_RUNE_WATER", new ColoredFireworkStar(Color.BLUE, "&7古代符文 &8&l[&1&l水&8&l]"));
    public static final SlimefunItemStack FIRE_RUNE = new SlimefunItemStack("ANCIENT_RUNE_FIRE", new ColoredFireworkStar(Color.RED, "&7古代符文 &8&l[&4&l火&8&l]"));
    public static final SlimefunItemStack EARTH_RUNE = new SlimefunItemStack("ANCIENT_RUNE_EARTH", new ColoredFireworkStar(Color.fromRGB(112, 47, 7), "&7古代符文 &8&l[&c&l地&8&l]"));
    public static final SlimefunItemStack ENDER_RUNE = new SlimefunItemStack("ANCIENT_RUNE_ENDER", new ColoredFireworkStar(Color.PURPLE, "&7古代符文 &8&l[&5&l末影&8&l]"));

    public static final SlimefunItemStack RAINBOW_RUNE = new SlimefunItemStack("ANCIENT_RUNE_RAINBOW", new ColoredFireworkStar(Color.FUCHSIA, "&7古代符文 &8&l[&d&l虹&8&l]"));
    public static final SlimefunItemStack LIGHTNING_RUNE = new SlimefunItemStack("ANCIENT_RUNE_LIGHTNING", new ColoredFireworkStar(Color.fromRGB(255, 255, 95), "&7古代符文 &8&l[&e&l雷&8&l]"));
    public static final SlimefunItemStack SOULBOUND_RUNE = new SlimefunItemStack("ANCIENT_RUNE_SOULBOUND", new ColoredFireworkStar(Color.fromRGB(47, 0, 117), "&7古代符文 &8&l[&5&l灵魂绑定&8&l]", "&e先把你要绑定的物品丢到地上", "&e然后把这个符文丢向那个物品", "&5就能灵魂绑定 &e那个物品.", " ", "&e建议你在 &6重要 &e物品上使用.", " ", "&e绑定后的物品死亡后不会掉落."));

    /*		Electricity			*/
    public static final SlimefunItemStack SOLAR_GENERATOR = new SlimefunItemStack("SOLAR_GENERATOR", Material.DAYLIGHT_DETECTOR, "&b太阳能发电机", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(4));
    public static final SlimefunItemStack SOLAR_GENERATOR_2 = new SlimefunItemStack("SOLAR_GENERATOR_2", Material.DAYLIGHT_DETECTOR, "&c高级太阳能发电机", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(16));
    public static final SlimefunItemStack SOLAR_GENERATOR_3 = new SlimefunItemStack("SOLAR_GENERATOR_3", Material.DAYLIGHT_DETECTOR, "&4黑金刚石太阳能发电机", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(64));
    public static final SlimefunItemStack SOLAR_GENERATOR_4 = new SlimefunItemStack("SOLAR_GENERATOR_4", Material.DAYLIGHT_DETECTOR, "&e充能太阳能发电机", "", "&9可以在夜间工作", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(256), " (日间)", LoreBuilder.powerPerSecond(128), " (夜间)");

    public static final SlimefunItemStack COAL_GENERATOR = new SlimefunItemStack("COAL_GENERATOR", CustomHeadTexture.GENERATOR, "&c煤发电机", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.GENERATOR), LoreBuilder.powerBuffer(64), LoreBuilder.powerPerSecond(16));
    public static final SlimefunItemStack COAL_GENERATOR_2 = new SlimefunItemStack("COAL_GENERATOR_2", CustomHeadTexture.GENERATOR, "&c煤发电机", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.GENERATOR), LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(30));

    public static final SlimefunItemStack LAVA_GENERATOR = new SlimefunItemStack("LAVA_GENERATOR", CustomHeadTexture.GENERATOR, "&4岩浆发电机", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.GENERATOR), LoreBuilder.powerBuffer(512), LoreBuilder.powerPerSecond(20));
    public static final SlimefunItemStack LAVA_GENERATOR_2 = new SlimefunItemStack("LAVA_GENERATOR_2", CustomHeadTexture.GENERATOR, "&4岩浆发电机 &7(&eII&7)", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.GENERATOR), LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(40));

    public static final SlimefunItemStack ELECTRIC_FURNACE = new SlimefunItemStack("ELECTRIC_FURNACE", Material.FURNACE, "&c电炉", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(4));
    public static final SlimefunItemStack ELECTRIC_FURNACE_2 = new SlimefunItemStack("ELECTRIC_FURNACE_2", Material.FURNACE, "&c电炉 &7- &eII", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7速度: 2x", LoreBuilder.powerPerSecond(6));
    public static final SlimefunItemStack ELECTRIC_FURNACE_3 = new SlimefunItemStack("ELECTRIC_FURNACE_3", Material.FURNACE, "&c电炉 &7- &eIII", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7速度: 4x", LoreBuilder.powerPerSecond(10));

    public static final SlimefunItemStack ELECTRIC_ORE_GRINDER = new SlimefunItemStack("ELECTRIC_ORE_GRINDER", Material.FURNACE, "&c电力碎矿机", "", "&r矿物粉碎机与磨石的完美结合", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(12));
    public static final SlimefunItemStack ELECTRIC_ORE_GRINDER_2 = new SlimefunItemStack("ELECTRIC_ORE_GRINDER_2", Material.FURNACE, "&c电力碎矿机 &7(&eII&7)", "", "&r矿物粉碎机与磨石的完美结合", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 4x", LoreBuilder.powerPerSecond(30));
    public static final SlimefunItemStack ELECTRIC_INGOT_PULVERIZER = new SlimefunItemStack("ELECTRIC_INGOT_PULVERIZER", Material.FURNACE, "&c电力打粉机", "", "&r将锭变为粉", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(14));
    public static final SlimefunItemStack AUTO_DRIER;
    public static final SlimefunItemStack AUTO_ENCHANTER = new SlimefunItemStack("AUTO_ENCHANTER", Material.ENCHANTING_TABLE, "&5自动附魔机", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(18));
    public static final SlimefunItemStack AUTO_DISENCHANTER = new SlimefunItemStack("AUTO_DISENCHANTER", Material.ENCHANTING_TABLE, "&5自动祛魔机", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(18));
    public static final SlimefunItemStack AUTO_ANVIL = new SlimefunItemStack("AUTO_ANVIL", Material.IRON_BLOCK, "&7自动铁砧", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7可修复百分比: 10%", LoreBuilder.powerPerSecond(24));
    public static final SlimefunItemStack AUTO_ANVIL_2 = new SlimefunItemStack("AUTO_ANVIL_2", Material.IRON_BLOCK, "&7自动铁砧 Mk.II", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7可修复百分比: 25%", LoreBuilder.powerPerSecond(32));

    public static final SlimefunItemStack BIO_REACTOR = new SlimefunItemStack("BIO_REACTOR", Material.LIME_TERRACOTTA, "&2生物反应器", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &7128 J 可储存", LoreBuilder.powerPerSecond(8));
    public static final SlimefunItemStack MULTIMETER = new SlimefunItemStack("MULTIMETER", Material.CLOCK, "&e万用表", "", "&r查看机器中储存的能量");

    public static final SlimefunItemStack SMALL_CAPACITOR = new SlimefunItemStack("SMALL_CAPACITOR", CustomHeadTexture.CAPACITOR_25, "&a小型储能电容", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.CAPACITOR), "&8\u21E8 &e\u26A1 &7128 J 可储存");
    public static final SlimefunItemStack MEDIUM_CAPACITOR = new SlimefunItemStack("MEDIUM_CAPACITOR", CustomHeadTexture.CAPACITOR_25, "&a中型储能电容", "", LoreBuilder.machine(MachineTier.AVERAGE, MachineType.CAPACITOR), LoreBuilder.powerBuffer(512));
    public static final SlimefunItemStack BIG_CAPACITOR = new SlimefunItemStack("BIG_CAPACITOR", CustomHeadTexture.CAPACITOR_25, "&a大型储能电容", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.CAPACITOR), LoreBuilder.powerBuffer(1024));
    public static final SlimefunItemStack LARGE_CAPACITOR = new SlimefunItemStack("LARGE_CAPACITOR", CustomHeadTexture.CAPACITOR_25, "&a巨型储能电容", "", LoreBuilder.machine(MachineTier.GOOD, MachineType.CAPACITOR), LoreBuilder.powerBuffer(8192));
    public static final SlimefunItemStack CARBONADO_EDGED_CAPACITOR = new SlimefunItemStack("CARBONADO_EDGED_CAPACITOR", CustomHeadTexture.CAPACITOR_25, "&a黑金刚石镶边储能电容", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.CAPACITOR), LoreBuilder.powerBuffer(65536));

    /*		Robots				*/
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID = new SlimefunItemStack("PROGRAMMABLE_ANDROID", CustomHeadTexture.PROGRAMMABLE_ANDROID, "&c可编程式机器人 &7(普通)", "", "&8\u21E8 &7功能: 无", "&8\u21E8 &7燃料效率: 1.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_FARMER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_FARMER", CustomHeadTexture.PROGRAMMABLE_ANDROID_FARMER, "&c可编程式机器人 &7(农夫)", "", "&8\u21E8 &7功能: 耕作", "&8\u21E8 &7燃料效率: 1.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_MINER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_MINER", CustomHeadTexture.PROGRAMMABLE_ANDROID_MINER, "&c可编程式机器人 &7(矿工)", "", "&8\u21E8 &7功能: 挖矿", "&8\u21E8 &7燃料效率: 1.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_WOODCUTTER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_WOODCUTTER", CustomHeadTexture.PROGRAMMABLE_ANDROID_WOODCUTTER, "&c可编程式机器人 &7(樵夫)", "", "&8\u21E8 &7功能: 伐木", "&8\u21E8 &7燃料效率: 1.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_BUTCHER", CustomHeadTexture.PROGRAMMABLE_ANDROID_BUTCHER, "&c可编程式机器人 &7(屠夫)", "", "&8\u21E8 &7功能: 屠宰", "&8\u21E8 &7伤害: 4", "&8\u21E8 &7燃料效率: 1.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_FISHERMAN", CustomHeadTexture.PROGRAMMABLE_ANDROID_FISHERMAN, "&c可编程式机器人 &7(渔夫)", "", "&8\u21E8 &7功能: 钓鱼", "&8\u21E8 &7成功几率: 10%", "&8\u21E8 &7燃料效率: 1.0x");

    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2 = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2", CustomHeadTexture.PROGRAMMABLE_ANDROID, "&c高级可编程式机器人 &7(普通)", "", "&8\u21E8 &7功能: 无", "&8\u21E8 &7燃料效率: 1.5x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_FISHERMAN", CustomHeadTexture.PROGRAMMABLE_ANDROID_FISHERMAN, "&c高级可编程式机器人 &7(渔夫)", "", "&8\u21E8 &7功能: 钓鱼", "&8\u21E8 &7成功几率: 20%", "&8\u21E8 &7燃料效率: 1.5x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2_FARMER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_FARMER", CustomHeadTexture.PROGRAMMABLE_ANDROID_FARMER, "&c高级可编程式机器人 &7(农夫)", "", "&8\u21E8 &7功能: 耕作", "&8\u21E8 &7燃料效率: 1.5x", "&8\u21E8 &7可以收割异域花园内的植物");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_2_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_2_BUTCHER", CustomHeadTexture.PROGRAMMABLE_ANDROID_BUTCHER, "&c高级可编程式机器人 &7(屠夫)", "", "&8\u21E8 &7功能: 屠宰", "&8\u21E8 &7伤害: 8", "&8\u21E8 &7燃料效率: 1.5x");

    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_3 = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3", CustomHeadTexture.PROGRAMMABLE_ANDROID, "&e可授权式可编程式机器人 &7(普通)", "", "&8\u21E8 &7功能: 无", "&8\u21E8 &7燃料效率: 3.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_3_FISHERMAN = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3_FISHERMAN", CustomHeadTexture.PROGRAMMABLE_ANDROID_FISHERMAN, "&e可授权式可编程式机器人 &7(渔夫)", "", "&8\u21E8 &7功能: 钓鱼", "&8\u21E8 &7成功几率: 30%", "&8\u21E8 &7燃料效率: 8.0x");
    public static final SlimefunItemStack PROGRAMMABLE_ANDROID_3_BUTCHER = new SlimefunItemStack("PROGRAMMABLE_ANDROID_3_BUTCHER", CustomHeadTexture.PROGRAMMABLE_ANDROID_BUTCHER, "&e可授权式可编程式机器人 &7(屠夫)", "", "&8\u21E8 &7功能: 屠宰", "&8\u21E8 &7伤害: 20", "&8\u21E8 &7燃料效率: 8.0x");

    /*		       GPS		       */
    public static final SlimefunItemStack GPS_TRANSMITTER = new SlimefunItemStack("GPS_TRANSMITTER", CustomHeadTexture.GPS_TRANSMITTER, "&bGPS 发射器", "", LoreBuilder.powerBuffer(16), LoreBuilder.powerPerSecond(2));
    public static final SlimefunItemStack GPS_TRANSMITTER_2 = new SlimefunItemStack("GPS_TRANSMITTER_2", CustomHeadTexture.GPS_TRANSMITTER, "&c高级 GPS 发射器", "", LoreBuilder.powerBuffer(64), LoreBuilder.powerPerSecond(6));
    public static final SlimefunItemStack GPS_TRANSMITTER_3 = new SlimefunItemStack("GPS_TRANSMITTER_3", CustomHeadTexture.GPS_TRANSMITTER, "&4黑金刚石 GPS 发射器", "", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(22));
    public static final SlimefunItemStack GPS_TRANSMITTER_4 = new SlimefunItemStack("GPS_TRANSMITTER_4", CustomHeadTexture.GPS_TRANSMITTER, "&e充能 GPS 发射器", "", LoreBuilder.powerBuffer(1024), "&8\u21E8 &e\u26A1 &792 J/s");

    public static final SlimefunItemStack GPS_MARKER_TOOL = new SlimefunItemStack("GPS_MARKER_TOOL", Material.REDSTONE_TORCH, "&bGPS 设置路径点工具", "", "&r允许你在放置标记工具的位置", "&r设置一个传送点并命名");
    public static final SlimefunItemStack GPS_CONTROL_PANEL = new SlimefunItemStack("GPS_CONTROL_PANEL", CustomHeadTexture.GPS_CONTROL_PANEL, "&bGPS 控制面板", "", "&r允许你追踪你的 GPS 卫星", "&r并且管理已有的路径点");
    public static final SlimefunItemStack GPS_EMERGENCY_TRANSMITTER = new SlimefunItemStack("GPS_EMERGENCY_TRANSMITTER", CustomHeadTexture.GPS_TRANSMITTER, "&cGPS 应急发射器", "", "&r当你死亡的时候", "&r如果应急发射器在你的背包内", "&r将会自动把你的死亡位置设为路径点");

    public static final SlimefunItemStack ANDROID_INTERFACE_FUEL = new SlimefunItemStack("ANDROID_INTERFACE_FUEL", Material.DISPENSER, "&7机器人交互接口 &c(燃料)", "", "&r当脚本告诉它这样做时", "&r储存在交互接口的物品", "&r将会被放入机器人的燃料槽");
    public static final SlimefunItemStack ANDROID_INTERFACE_ITEMS = new SlimefunItemStack("ANDROID_INTERFACE_ITEMS", Material.DISPENSER, "&7机器人交互接口 &9(物品)", "", "&r当脚本告诉它该这样做时", "&r储存在机器人物品栏的物品", "&r将会被放入交互界面");

    public static final SlimefunItemStack GPS_GEO_SCANNER = new SlimefunItemStack("GPS_GEO_SCANNER", CustomHeadTexture.GEO_SCANNER, "&bGPS 地形扫描器", "", "&r扫描一个区块中有多少自然资源", "&r例如 &8原油");
    public static final SlimefunItemStack PORTABLE_GEO_SCANNER = new SlimefunItemStack("PORTABLE_GEO_SCANNER", Material.CLOCK, "&b便携式资源扫描器", "", "&r扫描出区块中的自然资源", "", "&e右键&7 扫描");
    public static final SlimefunItemStack GEO_MINER = new SlimefunItemStack("GEO_MINER", CustomHeadTexture.GEO_MINER, "&6GEO 矿机", "", "&e从区块中开采出资源", "&e可以开采出不能被矿镐挖出的资源", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(48), "", "&c&l! &c确保你已经进行了 GEO 地形扫描");
    public static final SlimefunItemStack OIL_PUMP = new SlimefunItemStack("OIL_PUMP", CustomHeadTexture.OIL_PUMP, "&r原油泵", "", "&7泵出原油并把它装进桶里", "", "&c&l! &c请先对所在区块进行地形扫描");
    public static final SlimefunItemStack OIL_BUCKET = new SlimefunItemStack("BUCKET_OF_OIL", CustomHeadTexture.OIL_BUCKET, "&r原油桶");
    public static final SlimefunItemStack FUEL_BUCKET = new SlimefunItemStack("BUCKET_OF_FUEL", CustomHeadTexture.FUEL_BUCKET, "&r燃料桶");

    public static final SlimefunItemStack REFINERY = new SlimefunItemStack("REFINERY", Material.PISTON, "&c精炼机", "", "&r将原油提炼为燃油");
    public static final SlimefunItemStack COMBUSTION_REACTOR = new SlimefunItemStack("COMBUSTION_REACTOR", CustomHeadTexture.GENERATOR, "&c燃烧反应机", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.GENERATOR), LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(24));
    public static final SlimefunItemStack ANDROID_MEMORY_CORE = new SlimefunItemStack("ANDROID_MEMORY_CORE", CustomHeadTexture.ENERGY_REGULATOR, "&b机器人内存核心");

    public static final SlimefunItemStack GPS_TELEPORTER_PYLON = new SlimefunItemStack("GPS_TELEPORTER_PYLON", Material.PURPLE_STAINED_GLASS, "&5GPS 传送塔", "", "&7传送器组件");
    public static final SlimefunItemStack GPS_TELEPORTATION_MATRIX = new SlimefunItemStack("GPS_TELEPORTATION_MATRIX", Material.IRON_BLOCK, "&bGPS 传送矩阵", "", "&r这是 GPS 传送的主要零件", "&r这个矩阵允许玩家传送至", "&r已设置的路径点");
    public static final SlimefunItemStack GPS_ACTIVATION_DEVICE_SHARED = new SlimefunItemStack("GPS_ACTIVATION_DEVICE_SHARED", Material.STONE_PRESSURE_PLATE, "&rGPS 激活设备 &3(公共)", "", "&r把它放在传送矩阵上", "&r并且踩下这个踏板以选择", "&r要传送的路径点");
    public static final SlimefunItemStack GPS_ACTIVATION_DEVICE_PERSONAL = new SlimefunItemStack("GPS_ACTIVATION_DEVICE_PERSONAL", Material.STONE_PRESSURE_PLATE, "&rGPS 激活设备 &a(私人)", "", "&r把它放在传送矩阵上", "&r并且踩下这个踏板以选择", "&r要传送的路径点", "", "&r这种激活装置仅允许", "&r放置它的人使用");

    public static final SlimefunItemStack ELEVATOR_PLATE = new SlimefunItemStack("ELEVATOR_PLATE", Material.STONE_PRESSURE_PLATE, "&b电梯板", "", "&r在每一层放置电梯板", "&r你就能够在每一层之间传送.", "", "&e右键电梯板&7 以为此层命名");

    public static final SlimefunItemStack INFUSED_HOPPER = new SlimefunItemStack("INFUSED_HOPPER", Material.HOPPER, "&5吸入漏斗", "", "&r自动吸入在漏斗附近", "&r7x7x7 范围内的所有物品");

    public static final SlimefunItemStack PLASTIC_SHEET = new SlimefunItemStack("PLASTIC_SHEET", Material.PAPER, "&r塑料纸");

    public static final SlimefunItemStack HEATED_PRESSURE_CHAMBER = new SlimefunItemStack("HEATED_PRESSURE_CHAMBER", Material.LIGHT_GRAY_STAINED_GLASS, "&c加热压力舱", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(10));
    public static final SlimefunItemStack HEATED_PRESSURE_CHAMBER_2 = new SlimefunItemStack("HEATED_PRESSURE_CHAMBER_2", Material.LIGHT_GRAY_STAINED_GLASS, "&c加热压力舱 &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 5x", LoreBuilder.powerPerSecond(44));

    public static final SlimefunItemStack ELECTRIC_SMELTERY = new SlimefunItemStack("ELECTRIC_SMELTERY", Material.FURNACE, "&c电力冶炼炉", "", "&4仅支持合金, 不能将粉冶炼成锭", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(20));
    public static final SlimefunItemStack ELECTRIC_SMELTERY_2 = new SlimefunItemStack("ELECTRIC_SMELTERY_2", Material.FURNACE, "&c电力冶炼炉 &7- &eII", "", "&4仅支持合金, 不能将粉冶炼成锭", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 3x", LoreBuilder.powerPerSecond(40));

    public static final SlimefunItemStack ELECTRIC_PRESS = new SlimefunItemStack("ELECTRIC_PRESS", CustomHeadTexture.ELECTRIC_PRESS, "&e压缩机", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(16));
    public static final SlimefunItemStack ELECTRIC_PRESS_2 = new SlimefunItemStack("ELECTRIC_PRESS_2", CustomHeadTexture.ELECTRIC_PRESS, "&e压缩机 &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 3x", LoreBuilder.powerPerSecond(40));

    public static final SlimefunItemStack ELECTRIFIED_CRUCIBLE = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE", Material.RED_TERRACOTTA, "&c电动坩埚", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(48));
    public static final SlimefunItemStack ELECTRIFIED_CRUCIBLE_2 = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE_2", Material.RED_TERRACOTTA, "&c电动坩埚 &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 2x", "&8\u21E8 &e\u26A1 &780 J/s");
    public static final SlimefunItemStack ELECTRIFIED_CRUCIBLE_3 = new SlimefunItemStack("ELECTRIFIED_CRUCIBLE_3", Material.RED_TERRACOTTA, "&c电动坩埚 &7- &eIII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 4x", "&8\u21E8 &e\u26A1 &7120 J/s");

    public static final SlimefunItemStack CARBON_PRESS = new SlimefunItemStack("CARBON_PRESS", Material.BLACK_STAINED_GLASS, "&c碳压机", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(20));
    public static final SlimefunItemStack CARBON_PRESS_2 = new SlimefunItemStack("CARBON_PRESS_2", Material.BLACK_STAINED_GLASS, "&c碳压机 &7- &eII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 3x", "&8\u21E8 &e\u26A1 &750 J/s");
    public static final SlimefunItemStack CARBON_PRESS_3 = new SlimefunItemStack("CARBON_PRESS_3", Material.BLACK_STAINED_GLASS, "&c碳压机 &7- &eIII", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 15x", "&8\u21E8 &e\u26A1 &7180 J/s");

    public static final SlimefunItemStack BLISTERING_INGOT = new SlimefunItemStack("BLISTERING_INGOT", Material.GOLD_INGOT, "&6起泡锭 &7(33%)", "", LoreBuilder.radioactive(Radioactivity.HIGH), "&4&o需要防化套装");
    public static final SlimefunItemStack BLISTERING_INGOT_2 = new SlimefunItemStack("BLISTERING_INGOT_2", Material.GOLD_INGOT, "&6起泡锭 &7(66%)", "", LoreBuilder.radioactive(Radioactivity.HIGH), "&4&o需要防化套装");
    public static final SlimefunItemStack BLISTERING_INGOT_3 = new SlimefunItemStack("BLISTERING_INGOT_3", Material.GOLD_INGOT, "&6起泡锭", "", LoreBuilder.radioactive(Radioactivity.HIGH), "&4&o需要防化套装");

    public static final SlimefunItemStack ENERGY_REGULATOR = new SlimefunItemStack("ENERGY_REGULATOR", CustomHeadTexture.ENERGY_REGULATOR, "&6能源调节器", "", "&r能源网络的核心部分");
    public static final SlimefunItemStack DEBUG_FISH = new SlimefunItemStack("DEBUG_FISH", Material.SALMON, "&3这鱼多少钱?", "", "&e右键 &r任意方块以查看它的方块数据", "&e左键 &r破坏方块", "&eShift + 左键 &r任意方块以清除它的方块数据", "&eShift + 右键 &r放置一个占位符方块");

    public static final SlimefunItemStack NETHER_ICE = new SlimefunItemStack("NETHER_ICE", CustomHeadTexture.NETHER_ICE, "&e下界冰", "", "&e辐射等级: 中等", "&4&o需要防化套装");
    public static final SlimefunItemStack ENRICHED_NETHER_ICE = new SlimefunItemStack("ENRICHED_NETHER_ICE", CustomHeadTexture.ENRICHED_NETHER_ICE, "&e浓缩下界冰", "", "&2辐射等级: 极高", "&4&o需要防化套装");
    public static final SlimefunItemStack NETHER_ICE_COOLANT_CELL = new SlimefunItemStack("NETHER_ICE_COOLANT_CELL", CustomHeadTexture.NETHER_ICE_COOLANT_CELL, "&6下界冰冷却剂");

    // Cargo
    public static final SlimefunItemStack CARGO_MANAGER = new SlimefunItemStack("CARGO_MANAGER", CustomHeadTexture.CARGO_MANAGER, "&6货运管理器", "", "&r物品传输网络的核心组件");
    public static final SlimefunItemStack CARGO_CONNECTOR_NODE = new SlimefunItemStack("CARGO_NODE", CustomHeadTexture.CARGO_CONNECTOR_NODE, "&7货运节点 &c(连接器)", "", "&r货运连接管道");
    public static final SlimefunItemStack CARGO_INPUT_NODE = new SlimefunItemStack("CARGO_NODE_INPUT", CustomHeadTexture.CARGO_INPUT_NODE, "&7货运节点 &c(输入)", "", "&r货运输入管道");
    public static final SlimefunItemStack CARGO_OUTPUT_NODE = new SlimefunItemStack("CARGO_NODE_OUTPUT", CustomHeadTexture.CARGO_OUTPUT_NODE, "&7货运节点 &c(输出)", "", "&r货运输出管道");
    public static final SlimefunItemStack CARGO_OUTPUT_NODE_2 = new SlimefunItemStack("CARGO_NODE_OUTPUT_ADVANCED", CustomHeadTexture.CARGO_OUTPUT_NODE, "&6高级货运节点 &c(输出)", "", "&r货运输出管道");

    public static final SlimefunItemStack AUTO_BREEDER = new SlimefunItemStack("AUTO_BREEDER", Material.HAY_BLOCK, "&e自动喂食机", "", "&r需要 &a有机食物", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerBuffer(1024), "&8\u21E8 &e\u26A1 &760 J/个动物");

    public static final SlimefunItemStack ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9???");
    public static final SlimefunItemStack WHEAT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_WHEAT", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9小麦");
    public static final SlimefunItemStack CARROT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_CARROT", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9胡萝卜");
    public static final SlimefunItemStack POTATO_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_POTATO", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9土豆");
    public static final SlimefunItemStack SEEDS_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SEEDS", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9小麦种子");
    public static final SlimefunItemStack BEETROOT_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_BEETROOT", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9甜菜");
    public static final SlimefunItemStack MELON_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_MELON", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9西瓜");
    public static final SlimefunItemStack APPLE_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_APPLE", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9苹果");
    public static final SlimefunItemStack SWEET_BERRIES_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_SWEET_BERRIES", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9浆果");
    public static final SlimefunItemStack KELP_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_KELP", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9干海带");
    public static final SlimefunItemStack COCOA_ORGANIC_FOOD = new SlimefunItemStack("ORGANIC_FOOD_COCOA", CustomHeadTexture.FILLED_CAN, "&a有机食物", "&7内含 &9可可豆");

    public static final SlimefunItemStack FERTILIZER = new SlimefunItemStack("FERTILIZER", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9???");
    public static final SlimefunItemStack WHEAT_FERTILIZER = new SlimefunItemStack("FERTILIZER_WHEAT", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9小麦");
    public static final SlimefunItemStack CARROT_FERTILIZER = new SlimefunItemStack("FERTILIZER_CARROT", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9胡萝卜");
    public static final SlimefunItemStack POTATO_FERTILIZER = new SlimefunItemStack("FERTILIZER_POTATO", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9土豆");
    public static final SlimefunItemStack SEEDS_FERTILIZER = new SlimefunItemStack("FERTILIZER_SEEDS", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9小麦种子");
    public static final SlimefunItemStack BEETROOT_FERTILIZER = new SlimefunItemStack("FERTILIZER_BEETROOT", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9甜菜");
    public static final SlimefunItemStack MELON_FERTILIZER = new SlimefunItemStack("FERTILIZER_MELON", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9西瓜");
    public static final SlimefunItemStack APPLE_FERTILIZER = new SlimefunItemStack("FERTILIZER_APPLE", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9苹果");
    public static final SlimefunItemStack SWEET_BERRIES_FERTILIZER = new SlimefunItemStack("FERTILIZER_SWEET_BERRIES", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9浆果");
    public static final SlimefunItemStack KELP_FERTILIZER = new SlimefunItemStack("FERTILIZER_KELP", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9干海带");
    public static final SlimefunItemStack COCOA_FERTILIZER = new SlimefunItemStack("FERTILIZER_COCOA", CustomHeadTexture.FILLED_CAN, "&a有机肥料", "&7内含 &9可可豆");

    public static final SlimefunItemStack ANIMAL_GROWTH_ACCELERATOR = new SlimefunItemStack("ANIMAL_GROWTH_ACCELERATOR", Material.HAY_BLOCK, "&b动物生长加速器", "", "&r需要 &a有机食物", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(28));
    public static final SlimefunItemStack CROP_GROWTH_ACCELERATOR = new SlimefunItemStack("CROP_GROWTH_ACCELERATOR", Material.LIME_TERRACOTTA, "&a作物生长加速器", "", "&r需要 &a有机肥料", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7半径: 7x7", "&8\u21E8 &7速度: &a3/次", LoreBuilder.powerBuffer(1024), "&8\u21E8 &e\u26A1 &750 J/s");
    public static final SlimefunItemStack CROP_GROWTH_ACCELERATOR_2 = new SlimefunItemStack("CROP_GROWTH_ACCELERATOR_2", Material.LIME_TERRACOTTA, "&a作物生长加速器 &7(&eII&7)", "", "&r需要 &a有机肥料", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7半径: 9x9", "&8\u21E8 &7速度: &a4/次", LoreBuilder.powerBuffer(1024), "&8\u21E8 &e\u26A1 &760 J/s");
    public static final SlimefunItemStack TREE_GROWTH_ACCELERATOR = new SlimefunItemStack("TREE_GROWTH_ACCELERATOR", Material.BROWN_TERRACOTTA, "&a树木生长加速器", "", "&r需要 &a有机肥料", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7半径: 9x9", "&8\u21E8 &7速度: &a4/次", LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(48));

    public static final SlimefunItemStack FOOD_FABRICATOR = new SlimefunItemStack("FOOD_FABRICATOR", Material.GREEN_STAINED_GLASS, "&c食品加工机", "", "&r可制造 &a有机食物", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(14));
    public static final SlimefunItemStack FOOD_FABRICATOR_2 = new SlimefunItemStack("FOOD_FABRICATOR_2", Material.GREEN_STAINED_GLASS, "&c食品加工机 &7(&eII&7)", "", "&r可制造 &a有机食物", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 6x", LoreBuilder.powerBuffer(512), LoreBuilder.powerPerSecond(48));

    public static final SlimefunItemStack FOOD_COMPOSTER = new SlimefunItemStack("FOOD_COMPOSTER", Material.GREEN_TERRACOTTA, "&c食品堆肥器", "", "&r可制造 &a有机肥料", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(16));
    public static final SlimefunItemStack FOOD_COMPOSTER_2 = new SlimefunItemStack("FOOD_COMPOSTER_2", Material.GREEN_TERRACOTTA, "&c食品堆肥器 &7(&eII&7)", "", "&r可制造 &a有机肥料", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 10x", LoreBuilder.powerBuffer(512), "&8\u21E8 &e\u26A1 &752 J/s");

    public static final SlimefunItemStack EXP_COLLECTOR = new SlimefunItemStack("XP_COLLECTOR", CustomHeadTexture.EXP_COLLECTOR, "&a经验收集器", "", "&r收集附近的经验并储存它们", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerBuffer(1024), LoreBuilder.powerPerSecond(20));
    public static final SlimefunItemStack REACTOR_COOLANT_CELL = new SlimefunItemStack("REACTOR_COLLANT_CELL", CustomHeadTexture.COOLANT_CELL, "&b反应堆冷却剂");

    public static final SlimefunItemStack NUCLEAR_REACTOR = new SlimefunItemStack("NUCLEAR_REACTOR", CustomHeadTexture.GENERATOR, "&2核反应堆", "", "&r需要冷却剂!", "&8\u21E8 &b必须被水包围", "&8\u21E8 &b必须使用反应堆冷却剂工作", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &716384 J 可储存", "&8\u21E8 &e\u26A1 &7500 J/s");
    public static final SlimefunItemStack NETHERSTAR_REACTOR = new SlimefunItemStack("NETHERSTAR_REACTOR", CustomHeadTexture.GENERATOR, "&f下界之星反应堆", "", "&f需要下界之星", "&8\u21E8 &b必须被水包围", "&8\u21E8 &b必须使用下界之星冷却剂工作", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), "&8\u21E8 &e\u26A1 &732768 J 可储存", "&8\u21E8 &e\u26A1 &71024 J/s", "&8\u21E8 &4会导致附近的生物获得凋零效果");
    public static final SlimefunItemStack REACTOR_ACCESS_PORT = new SlimefunItemStack("REACTOR_ACCESS_PORT", Material.CYAN_TERRACOTTA, "&2反应堆访问接口", "", "&r允许你通过货运节点来访问反应堆", "&r也可以用于储存", "", "&8\u21E8 &c必须 &e放置在反应堆上方的第三格处");

    public static final SlimefunItemStack FREEZER = new SlimefunItemStack("FREEZER", Material.LIGHT_BLUE_STAINED_GLASS, "&b冰箱", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(18));
    public static final SlimefunItemStack FREEZER_2 = new SlimefunItemStack("FREEZER_2", Material.LIGHT_BLUE_STAINED_GLASS, "&b冰箱 &7(&eII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 2x", LoreBuilder.powerBuffer(256), LoreBuilder.powerPerSecond(30));

    public static final SlimefunItemStack ELECTRIC_GOLD_PAN = new SlimefunItemStack("ELECTRIC_GOLD_PAN", Material.BROWN_TERRACOTTA, "&6电动淘金机", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(2));
    public static final SlimefunItemStack ELECTRIC_GOLD_PAN_2 = new SlimefunItemStack("ELECTRIC_GOLD_PAN_2", Material.BROWN_TERRACOTTA, "&6电动淘金机 &7(&eII&7)", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7速度: 3x", LoreBuilder.powerPerSecond(4));
    public static final SlimefunItemStack ELECTRIC_GOLD_PAN_3 = new SlimefunItemStack("ELECTRIC_GOLD_PAN_3", Material.BROWN_TERRACOTTA, "&6电动淘金机 &7(&eIII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 10x", LoreBuilder.powerPerSecond(14));

    public static final SlimefunItemStack ELECTRIC_DUST_WASHER = new SlimefunItemStack("ELECTRIC_DUST_WASHER", Material.BLUE_STAINED_GLASS, "&3电动洗矿机", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(6));
    public static final SlimefunItemStack ELECTRIC_DUST_WASHER_2 = new SlimefunItemStack("ELECTRIC_DUST_WASHER_2", Material.BLUE_STAINED_GLASS, "&3电动洗矿机 &7(&eII&7)", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7速度: 2x", LoreBuilder.powerPerSecond(10));
    public static final SlimefunItemStack ELECTRIC_DUST_WASHER_3 = new SlimefunItemStack("ELECTRIC_DUST_WASHER_3", Material.BLUE_STAINED_GLASS, "&3电动洗矿机 &7(&eIII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 10x", LoreBuilder.powerPerSecond(30));

    public static final SlimefunItemStack ELECTRIC_INGOT_FACTORY = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY", Material.RED_TERRACOTTA, "&c电动铸锭机", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(8));
    public static final SlimefunItemStack ELECTRIC_INGOT_FACTORY_2 = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY_2", Material.RED_TERRACOTTA, "&c电动铸锭机 &7(&eII&7)", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &7速度: 2x", LoreBuilder.powerPerSecond(14));
    public static final SlimefunItemStack ELECTRIC_INGOT_FACTORY_3 = new SlimefunItemStack("ELECTRIC_INGOT_FACTORY_3", Material.RED_TERRACOTTA, "&c电动铸锭机 &7(&eIII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 8x", LoreBuilder.powerPerSecond(40));

    public static final SlimefunItemStack AUTOMATED_CRAFTING_CHAMBER = new SlimefunItemStack("AUTOMATED_CRAFTING_CHAMBER", Material.CRAFTING_TABLE, "&6自动合成机", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &710 J/个物品");
    public static final SlimefunItemStack FLUID_PUMP = new SlimefunItemStack("FLUID_PUMP", Material.BLUE_TERRACOTTA, "&9流体泵", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &732 J/个方块");
    public static final SlimefunItemStack CHARGING_BENCH = new SlimefunItemStack("CHARGING_BENCH", Material.CRAFTING_TABLE, "&6充电台", "", "&r能够给物品充电, 比如喷气背包", "", LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &7128 J 可储存", "&8\u21E8 &e\u26A1 &7能源损失率: &c50%");

    public static final SlimefunItemStack WITHER_ASSEMBLER = new SlimefunItemStack("WITHER_ASSEMBLER", Material.OBSIDIAN, "&5凋零汇编器", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7冷却时间: &b30 秒", "&8\u21E8 &e\u26A1 &74096 J 可储存", "&8\u21E8 &e\u26A1 &74096 J/个凋灵");

    public static final SlimefunItemStack TRASH_CAN = new SlimefunItemStack("TRASH_CAN_BLOCK", CustomHeadTexture.TRASH_CAN, "&3垃圾箱", "", "&r将不需要的物品放入可以消除");

    public static final SlimefunItemStack ELYTRA_SCALE = new SlimefunItemStack("ELYTRA_SCALE", Material.FEATHER, "&b鞘翅鳞片");
    public static final SlimefunItemStack INFUSED_ELYTRA = new SlimefunItemStack("INFUSED_ELYTRA", Material.ELYTRA, "&5鞘翅 (经验修补)");
    public static final SlimefunItemStack SOULBOUND_ELYTRA = new SlimefunItemStack("SOULBOUND_ELYTRA", Material.ELYTRA, "&c鞘翅 (灵魂绑定)");

    public static final SlimefunItemStack MAGNESIUM_SALT = new SlimefunItemStack("MAGNESIUM_SALT", Material.SUGAR, "&c镁盐", "", "&7是一种能在镁发电机中使用的特殊燃料");
    public static final SlimefunItemStack MAGNESIUM_GENERATOR = new SlimefunItemStack("MAGNESIUM_GENERATOR", CustomHeadTexture.GENERATOR, "&c镁发电机", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.GENERATOR), LoreBuilder.powerBuffer(128), LoreBuilder.powerPerSecond(36));

    static {
        INFUSED_ELYTRA.addUnsafeEnchantment(Enchantment.MENDING, 1);
        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            MAKESHIFT_SMELTERY = new SlimefunItemStack("MAKESHIFT_SMELTERY", Material.BLAST_FURNACE, "&e简易冶炼炉", "", "&r简易版的冶炼炉", "&r只能将矿物粉末熔炼成锭");
            TABLE_SAW = new SlimefunItemStack("TABLE_SAW", Material.STONECUTTER, "&6台锯", "", "&a&o从1个木头里获得8个木板", "&a&o(支持所有的原木)");
            AUTO_DRIER = new SlimefunItemStack("AUTO_DRIER", Material.SMOKER, "&e自动烘干机", "", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", LoreBuilder.powerPerSecond(10));
        } else {
            MAKESHIFT_SMELTERY = new SlimefunItemStack("MAKESHIFT_SMELTERY", Material.FURNACE, "&e简易冶炼炉", "", "&r简易版的冶炼炉", "&r只能将矿物粉末熔炼成锭");
            TABLE_SAW = null;
            AUTO_DRIER = null;
        }
    }
}
