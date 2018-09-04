package me.mrCookieSlime.Slimefun.Misc;

import org.bukkit.Material;

//ToDO: At some point I/we should really improve this
public class MaterialHelper {

    public static Material getSaplingFromLog(Material log) {
        switch (log) {
            case OAK_LOG:
                return Material.OAK_SAPLING;
            case SPRUCE_LOG:
                return Material.SPRUCE_SAPLING;
            case BIRCH_LOG:
                return Material.BIRCH_SAPLING;
            case JUNGLE_LOG:
                return Material.JUNGLE_SAPLING;
            case ACACIA_LOG:
                return Material.ACACIA_SAPLING;
            case DARK_OAK_LOG:
                return Material.DARK_OAK_SAPLING;
        }
        return Material.OAK_SAPLING;
    }

    public static Material getWoodFromLog(Material log) {
        switch (log) {
            case OAK_LOG:
                return Material.OAK_WOOD;
            case SPRUCE_LOG:
                return Material.SPRUCE_WOOD;
            case BIRCH_LOG:
                return Material.BIRCH_WOOD;
            case JUNGLE_LOG:
                return Material.JUNGLE_WOOD;
            case ACACIA_LOG:
                return Material.ACACIA_WOOD;
            case DARK_OAK_LOG:
                return Material.DARK_OAK_WOOD;
        }
        return Material.OAK_WOOD;
    }

    public static boolean isLog(Material log) {
        switch (log) {
            case OAK_LOG:
            case SPRUCE_LOG:
            case BIRCH_LOG:
            case JUNGLE_LOG:
            case ACACIA_LOG:
            case DARK_OAK_LOG:
                return true;
        }
        return false;
    }


    public static final Material[] WoolColours = {
            Material.WHITE_WOOL,
            Material.ORANGE_WOOL,
            Material.MAGENTA_WOOL,
            Material.LIGHT_BLUE_WOOL,
            Material.YELLOW_WOOL,
            Material.LIME_WOOL,
            Material.PINK_WOOL,
            Material.GRAY_WOOL,
            Material.LIGHT_GRAY_WOOL,
            Material.CYAN_WOOL,
            Material.PURPLE_WOOL,
            Material.BLUE_WOOL,
            Material.BROWN_WOOL,
            Material.GREEN_WOOL,
            Material.RED_WOOL,
            Material.BLACK_WOOL
    };

    public static boolean isWool(Material material) {
        for (Material mat : WoolColours)
            if (mat == material) return true;
        return false;
    }

    public static final Material[] StainedGlassColours = {
            Material.WHITE_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS,
            Material.CYAN_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS,
            Material.RED_STAINED_GLASS,
            Material.BLACK_STAINED_GLASS
    };

    public static boolean isStainedGlass(Material material) {
        for (Material mat : StainedGlassColours)
            if (mat == material) return true;
        return false;
    }

    public static final Material[] StainedGlassPaneColours = {
            Material.WHITE_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.BLUE_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE,
            Material.GREEN_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE
    };

    public static boolean isStainedGlassPane(Material material) {
        for (Material mat : StainedGlassPaneColours)
            if (mat == material) return true;
        return false;
    }

    public static final Material[] TerracottaColours = {
            Material.WHITE_TERRACOTTA,
            Material.ORANGE_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA,
            Material.LIGHT_BLUE_TERRACOTTA,
            Material.YELLOW_TERRACOTTA,
            Material.LIME_TERRACOTTA,
            Material.PINK_TERRACOTTA,
            Material.GRAY_TERRACOTTA,
            Material.LIGHT_GRAY_TERRACOTTA,
            Material.CYAN_TERRACOTTA,
            Material.PURPLE_TERRACOTTA,
            Material.BLUE_TERRACOTTA,
            Material.BROWN_TERRACOTTA,
            Material.GREEN_TERRACOTTA,
            Material.RED_TERRACOTTA,
            Material.BLACK_TERRACOTTA
    };

     public static boolean isTerracotta(Material material) {
        for (Material mat : TerracottaColours)
            if (mat == material) return true;
        return false;
    }

}
