package me.mrCookieSlime.Slimefun.api;

import me.mrCookieSlime.CSCoreLibPlugin.PlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.entity.Player;

import java.util.List;

@Deprecated
public interface GuideHandler {

    void addEntry(List<String> texts, List<String> tooltips);

    PlayerRunnable getRunnable();

    int getTier();

    boolean trackHistory();

    int next(Player p, int index, ChestMenu menu);

    default PlayerRunnable getRunnable(boolean book) {
        return this.getRunnable();
    }

    default void run(Player p, boolean survival, boolean book) {
        this.getRunnable(book).run(p);
    }

}