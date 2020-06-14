package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

@FunctionalInterface
interface AndroidAction {

    void perform(ProgrammableAndroid android, Block b, BlockMenu inventory, BlockFace face);

}