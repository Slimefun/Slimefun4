package me.mrCookieSlime.Slimefun.Objects;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.compatibility.MaterialHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;

import org.bukkit.Material;

public class MultiBlock {
	
	public static List<MultiBlock> list = new ArrayList<MultiBlock>();
	
	Material[] blocks;
	Material trigger;
	
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
		list.add(this);
	}
	
	public static List<MultiBlock> list() {
		return list;
	}
	
	public boolean isMultiBlock(SlimefunItem machine) {
		if (machine == null) return false;
		else if (!(machine instanceof SlimefunMachine)) return false;
		else if (machine instanceof SlimefunMachine) {
			MultiBlock mb = ((SlimefunMachine) machine).toMultiBlock();
			if (trigger == mb.getTriggerBlock()) {
				for (int i = 0; i < mb.getBuild().length; i++) {
					if (mb.getBuild()[i] != null) {
						if (MaterialHelper.isLog( mb.getBuild()[i])) {
							if (!MaterialHelper.isLog(blocks[i])) return false;
						}
						else if (mb.getBuild()[i] != blocks[i]) return false;
					}
				}
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	public boolean isMultiBlock(MultiBlock mb) {
		if (mb == null) return false;
		else if (trigger == mb.getTriggerBlock()) {
			for (int i = 0; i < mb.getBuild().length; i++) {
				if (mb.getBuild()[i] != null) {
					if (MaterialHelper.isLog(mb.getBuild()[i])) {
						if (!MaterialHelper.isLog(blocks[i])) return false;
					}
					else if (mb.getBuild()[i] != blocks[i]) return false;
				}
			}
			return true;
		}
		else return false;
	}

}
