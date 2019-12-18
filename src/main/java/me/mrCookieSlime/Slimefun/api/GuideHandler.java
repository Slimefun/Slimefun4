package me.mrCookieSlime.Slimefun.api;

import java.util.List;

import org.bukkit.entity.Player;

import me.mrCookieSlime.CSCoreLibPlugin.PlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

@Deprecated
public interface GuideHandler {
	
	public abstract void addEntry(List<String> texts, List<String> tooltips);
	public abstract PlayerRunnable getRunnable();
	public abstract int getTier();
	public abstract boolean trackHistory();

	public abstract int next(Player p, int index, ChestMenu menu);

	default PlayerRunnable getRunnable(boolean book) {
		return this.getRunnable();
	}
	
	default void run(Player p, boolean survival, boolean book) {
		this.getRunnable(book).run(p);
		
		if (survival && this.trackHistory()) {
			PlayerProfile.get(p, profile -> profile.getGuideHistory().add(this));
		}
	}

}
