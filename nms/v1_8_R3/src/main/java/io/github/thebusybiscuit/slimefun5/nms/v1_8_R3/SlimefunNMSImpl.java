package io.github.thebusybiscuit.slimefun5.nms.v1_8_R3;

import io.github.thebusybiscuit.slimefun5.compat.SlimefunNMS;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

public class SlimefunNMSImpl implements SlimefunNMS {

    @Override
    public String getNBTString(org.bukkit.inventory.ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) return null;
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (nmsItem != null && nmsItem.hasTag()) {
            NBTTagCompound tag = nmsItem.getTag();
            if (tag.hasKey(key)) {
                return tag.getString(key);
            }
        }
        return null;
    }

    @Override
    public org.bukkit.inventory.ItemStack setNBTString(org.bukkit.inventory.ItemStack item, String key, String value) {
        if (item == null || item.getType() == Material.AIR) return item;
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (nmsItem == null) return item;
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        tag.setString(key, value);
        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public void setBlockTypeFast(Block block, Material material) {
        WorldServer world = ((CraftWorld) block.getWorld()).getHandle();
        BlockPosition pos = new BlockPosition(block.getX(), block.getY(), block.getZ());
        net.minecraft.server.v1_8_R3.Block nmsBlock = CraftMagicNumbers.getBlock(material);
        IBlockData data = nmsBlock.getBlockData();
        world.setTypeAndData(pos, data, 2); // 2 = update client, no physics
    }

    @Override
    public void breakBlockFast(Block block) {
        setBlockTypeFast(block, Material.AIR);
    }
}
