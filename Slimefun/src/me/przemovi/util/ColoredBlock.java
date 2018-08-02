package me.przemovi.util;
 
import org.bukkit.Material;
 
public enum ColoredBlock {
    WOOL, GLAZED_TERRACOTTA, TERRACOTTA, STAINED_GLASS_PANE, STAINED_GLASS, CONCRETE_POWDER, CONCRETE, SHULKER_BOX, CARPET, BED, BANNER;
   
    public Material getColoredBlock(int i) {
        switch(i){
        case 0: return Material.valueOf("WHITE_" + this.name());
        case 1: return Material.valueOf("ORANGE_" + this.name());
        case 2: return Material.valueOf("MAGENTA_" + this.name());
        case 3: return Material.valueOf("LIGHT_BLUE_" + this.name());
        case 4: return Material.valueOf("YELLOW_" + this.name());
        case 5: return Material.valueOf("LIME_" + this.name());
        case 6: return Material.valueOf("PINK_" + this.name());
        case 7: return Material.valueOf("GRAY_" + this.name());
        case 8: return Material.valueOf("LIGHT_GRAY_" + this.name());
        case 9: return Material.valueOf("CYAN_" + this.name());
        case 10: return Material.valueOf("PURPLE_" + this.name());
        case 11: return Material.valueOf("BLUE_" + this.name());
        case 12: return Material.valueOf("BROWN_" + this.name());
        case 13: return Material.valueOf("GREEN_" + this.name());
        case 14: return Material.valueOf("RED_" + this.name());
        case 15: return Material.valueOf("BLACK_" + this.name());
        default: return null;
        }
    }
   
    public static ColoredBlock fromBlockType(Material blockType) {
        for(ColoredBlock cb : values()) {
            if(blockType.name().contains(cb.name())) {
                return cb;
            }
        }
        return null;
    }
}