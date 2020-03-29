package me.mrCookieSlime.Slimefun.api;

import java.util.List;

import org.bukkit.entity.Player;

import me.mrCookieSlime.CSCoreLibPlugin.PlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * Guide Handlers are used to add "fake" categories to the Guide.
 * 
 * @deprecated Some day in the future we will simply allow to override the "opening" method of a Category instead.
 * 
 * @author TheBusyBiscuit
 *
 */
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
