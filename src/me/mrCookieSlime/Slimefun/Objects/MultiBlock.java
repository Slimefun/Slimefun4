package me.mrCookieSlime.Slimefun.Objects;

import java.util.List;

import org.bukkit.Material;

import me.mrCookieSlime.CSCoreLibPlugin.compatibility.MaterialHelper;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;

public class MultiBlock {
	
	private Material[] blocks;
	private Material trigger;
	
	public MultiBlock(Material[] build, Material trigger) {
		this.blocks = build;
		this.trigger = trigger;
	}
	
	public Material[] getBuild() {
		return this.blocks;
	}
	
	public Material getTriggerBlock() {
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
			if (MaterialHelper.isLog(b)) {
				return MaterialHelper.isLog(a);
			}
			
			if (b != a) {
				return false;
			}
		}
		
		return true;
	}

}
