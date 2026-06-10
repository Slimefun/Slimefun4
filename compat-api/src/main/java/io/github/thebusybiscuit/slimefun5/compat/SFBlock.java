package io.github.thebusybiscuit.slimefun5.compat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Directional;

import java.lang.reflect.Method;

public class SFBlock {

    private final Block block;

    private static Method getBlockDataMethod;
    private static Method setBlockDataMethod;
    private static Class<?> blockDataClass;
    private static Class<?> directionalClass;
    private static Method getFacingMethod;
    private static Method setFacingMethod;
    
    private static boolean isModern;

    static {
        try {
            blockDataClass = Class.forName("org.bukkit.block.data.BlockData");
            directionalClass = Class.forName("org.bukkit.block.data.Directional");
            getBlockDataMethod = Block.class.getMethod("getBlockData");
            setBlockDataMethod = Block.class.getMethod("setBlockData", blockDataClass);
            getFacingMethod = directionalClass.getMethod("getFacing");
            setFacingMethod = directionalClass.getMethod("setFacing", BlockFace.class);
            isModern = true;
        } catch (Exception e) {
            isModern = false;
        }
    }

    public SFBlock(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    @SuppressWarnings("deprecation")
    public void setType(SFMaterial material) {
        Material mat = material.toMaterial();
        if (mat == null) return;
        
        block.setType(mat);
        
        if (!isModern) {
            byte legacyData = material.getLegacyData();
            if (legacyData != 0) {
                block.setData(legacyData);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public BlockFace getFacing() {
        if (isModern) {
            try {
                Object blockData = getBlockDataMethod.invoke(block);
                if (directionalClass.isInstance(blockData)) {
                    return (BlockFace) getFacingMethod.invoke(blockData);
                }
            } catch (Exception e) {
                // Ignore
            }
        } else {
            BlockState state = block.getState();
            if (state.getData() instanceof Directional) {
                return ((Directional) state.getData()).getFacing();
            }
        }
        return BlockFace.SELF;
    }

    @SuppressWarnings("deprecation")
    public void setFacing(BlockFace face) {
        if (isModern) {
            try {
                Object blockData = getBlockDataMethod.invoke(block);
                if (directionalClass.isInstance(blockData)) {
                    setFacingMethod.invoke(blockData, face);
                    setBlockDataMethod.invoke(block, blockData);
                }
            } catch (Exception e) {
                // Ignore
            }
        } else {
            BlockState state = block.getState();
            if (state.getData() instanceof Directional) {
                Directional dir = (Directional) state.getData();
                dir.setFacingDirection(face);
                state.setData((org.bukkit.material.MaterialData) dir);
                state.update(true);
            }
        }
    }
}
