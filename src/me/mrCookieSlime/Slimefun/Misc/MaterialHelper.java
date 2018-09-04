package me.mrCookieSlime.Slimefun.Misc;

import org.bukkit.Material;

public class MaterialHelper {
    public static Material getSaplingFromLog(Material log){
        switch (log){
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
    public static Material getWoodFromLog(Material log){
        switch (log){
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
    public static boolean isLog(Material log){
        switch (log){
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

}
