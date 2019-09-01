package me.mrCookieSlime.Slimefun.Objects.tasks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public class GrapplingHookTask extends SlimefunTask {

    public GrapplingHookTask(Player p) {
        super(p);
    }

    @Override
    void executeTask() {
        Utilities utilities = SlimefunPlugin.getUtilities();

        for (Entity n: utilities.remove.get(uuid)) {
            n.remove();
        }

        utilities.jumpState.remove(uuid);
        utilities.remove.remove(uuid);
    }
}
