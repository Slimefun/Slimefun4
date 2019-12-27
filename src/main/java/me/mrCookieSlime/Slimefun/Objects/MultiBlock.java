package me.mrCookieSlime.Slimefun.Objects;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;

public class MultiBlock {
	
	public static final List<Tag<Material>> SUPPORTED_TAGS = Arrays.asList(
			Tag.LOGS,
			Tag.WOODEN_FENCES,
			Tag.WOODEN_TRAPDOORS,
			Tag.WOODEN_SLABS
	);
	
	private final Material[] blocks;
	private final BlockFace trigger;
	private final boolean isSymmetric;
	
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
			
			for (Tag<Material> tag : SUPPORTED_TAGS) {
				if (tag.isTagged(b)) {
					return tag.isTagged(a);
				}
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
}
