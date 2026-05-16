package io.github.thebusybiscuit.slimefun5.implementation.items.androids;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

@FunctionalInterface
interface AndroidAction {

    void perform(ProgrammableAndroid android, Block b, BlockMenu inventory, BlockFace face);

}

