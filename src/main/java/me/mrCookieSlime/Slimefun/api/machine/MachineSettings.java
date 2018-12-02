package me.mrCookieSlime.Slimefun.api.machine;

import java.util.List;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public class MachineSettings {
	
	MachineConfig cfg;
	String prefix = "global";

	public MachineSettings(MachineConfig cfg) {
		this.cfg = cfg;
	}

	public MachineSettings(MachineConfig cfg, AContainer machine) {
		this.cfg = cfg;
		this.prefix = machine.getID();
	}
	
	public String getString(String path) {
		return this.cfg.getString(prefix + "." + path);
	}
	
	public int getInt(String path) {
		return this.cfg.getInt(prefix + "." + path);
	}
	
	public List<String> getStringList(String path) {
		return this.cfg.getStringList(prefix + "." + path);
	}

}
