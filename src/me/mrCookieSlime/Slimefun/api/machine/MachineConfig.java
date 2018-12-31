package me.mrCookieSlime.Slimefun.api.machine;

import java.util.HashMap;
import java.util.Map;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public class MachineConfig extends Config {
	
	MachineSettings global;
	Map<String, MachineSettings> children;
	
	public MachineConfig(String id) {
		super("plugins/Slimefun/machines/" + id + ".yml");
		
		this.global = new MachineSettings(this);
		this.children = new HashMap<String, MachineSettings>();
	}
	
	public MachineSettings getGlobalSettings() {
		return this.global;
	}
	
	public MachineSettings getSettings(AContainer item) {
		if (!this.children.containsKey(item.getID())) {
			this.children.put(item.getID(), new MachineSettings(this, item));
		}

		return this.children.get(item.getID());
	}
}
