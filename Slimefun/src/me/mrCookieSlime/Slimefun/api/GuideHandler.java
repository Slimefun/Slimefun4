package me.mrCookieSlime.Slimefun.api;

import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.PlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.URID.URID;

import org.bukkit.entity.Player;

public abstract class GuideHandler {
	
	URID urid;
	
	public GuideHandler() {
		this.urid = URID.nextURID(this, false);
	}
	
	public URID getURID() {
		return this.urid;
	}
	
	public abstract void addEntry(List<String> texts, List<String> tooltips);
	public abstract PlayerRunnable getRunnable();
	public abstract int getTier();
	public abstract boolean trackHistory();

	public abstract int next(Player p, int index, ChestMenu menu);

	public PlayerRunnable getRunnable(boolean book) {
		return this.getRunnable();
	}
	
	public void run(Player p, boolean survival, boolean book) {
		this.getRunnable(book).run(p);
		
		if (survival && this.trackHistory()) {
			SlimefunGuide.addToHistory(p, getURID());
		}
	}

}
