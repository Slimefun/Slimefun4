package me.mrCookieSlime.Slimefun.Objects;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;

public class MultiBlock {
	
	private Material[] blocks;
	private BlockFace trigger;
	private boolean isSymmetric;

	@Deprecated
	public MultiBlock(Material[] build, Material trigger) {
		this.blocks = build;
		this.isSymmetric = isSymmetric(build);
		this.trigger = convertTriggerMaterialToBlockFace(build, trigger);
	}
	
	public MultiBlock(Material[] build, BlockFace trigger) {
		this.blocks = build;
		this.trigger = trigger;
		this.isSymmetric = isSymmetric(build);
	}
	
	private static boolean isSymmetric(Material[] blocks) {
		return blocks[0] == blocks[2]
			&& blocks[3] == blocks[5]
			&& blocks[6] == blocks[8];
	}
	
	public Material[] getBuild() {
		return this.blocks;
	}
	
	public BlockFace getTriggerBlock() {
		return this.trigger;
	}
	
	public void register() {
		SlimefunPlugin.getUtilities().allMultiblocks.add(this);
	}
	
	public static List<MultiBlock> list() {
		return SlimefunPlugin.getUtilities().allMultiblocks;
	}
	
	public boolean isMultiBlock(SlimefunItem machine) {
		if (machine instanceof SlimefunMachine) {
			return isMultiBlock(((SlimefunMachine) machine).toMultiBlock());
		}
		else return false;
	}
	
	public boolean isMultiBlock(MultiBlock mb) {
		if (mb == null) return false;
		
		if (trigger == mb.getTriggerBlock()) {
			for (int i = 0; i < mb.getBuild().length; i++) {
				if (!compareBlocks(blocks[i], mb.getBuild()[i])) return false;
			}
			
			return true;
		}
		
		return false;
	}

	private boolean compareBlocks(Material a, Material b) {
		if (b != null) {
			if (Tag.LOGS.isTagged(b)) {
				return Tag.LOGS.isTagged(a);
			}
			
			if (b != a) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isSymmetric() {
		return this.isSymmetric;
	}
	
	@Deprecated
	public static BlockFace convertTriggerMaterialToBlockFace(Material[] build, Material trigger)
	{
		//Hacky
		for (int i = 1; i < 9; i +=3) {
			if (trigger == build[i]) {
				switch (i) {
					case 1:
						return BlockFace.DOWN;
					case 4:
						return BlockFace.SELF;
					case 7:
						return BlockFace.UP;
					default:
						break;
				}
			}
		}
		return null;
	}
}
