package me.mrCookieSlime.Slimefun.Objects;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class MultiBlock {
	
	public static final List<Tag<Material>> SUPPORTED_TAGS = Arrays.asList(
			Tag.LOGS,
			Tag.WOODEN_FENCES,
			Tag.WOODEN_TRAPDOORS,
			Tag.WOODEN_SLABS
	);
	
	private final SlimefunItem item;
	private final Material[] blocks;
	private final BlockFace trigger;
	private final boolean isSymmetric;
	
	public MultiBlock(SlimefunItem item, Material[] build, BlockFace trigger) {
		this.item = item;
		
		this.blocks = build;
		this.trigger = trigger;
		this.isSymmetric = isSymmetric(build);
	}
	
	public SlimefunItem getSlimefunItem() {
		return item;
	}
	
	private static boolean isSymmetric(Material[] blocks) {
		return blocks[0] == blocks[2] && blocks[3] == blocks[5] && blocks[6] == blocks[8];
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
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MultiBlock)) return false;
		
		MultiBlock mb = (MultiBlock) obj;
		if (trigger == mb.getTriggerBlock()) {
			for (int i = 0; i < mb.getBuild().length; i++) {
				if (!compareBlocks(blocks[i], mb.getBuild()[i])) return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(item.getID(), blocks, trigger, isSymmetric);
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
	
	@Override
	public String toString() {
		return "MultiBlock (" + item.getID() + ") {" + Arrays.toString(blocks) + "}";
	}
}
