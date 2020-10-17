package me.mrCookieSlime.Slimefun.api.inventory;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuOpeningHandler;

public class MenuSavingHandler implements MenuOpeningHandler {

    private final DirtyChestMenu menu;
    private final MenuOpeningHandler handler;

    @ParametersAreNonnullByDefault
    public MenuSavingHandler(DirtyChestMenu menu, MenuOpeningHandler handler) {
        this.menu = menu;
        this.handler = handler;
    }

    @Override
    public void onOpen(Player p) {
        handler.onOpen(p);
        menu.markDirty();
    }

    public MenuOpeningHandler getOpeningHandler() {
        return handler;
    }

}
