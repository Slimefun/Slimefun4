package me.mrCookieSlime.Slimefun.api.inventory;

import org.bukkit.entity.Player;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

public class DirtyChestMenu extends ChestMenu {
	
	protected int changes = 0;

	public DirtyChestMenu(String title) {
		super(title);
	}
	
	public void markDirty() {
		changes++;
	}
	
	public boolean isDirty() {
		return changes > 0;
	}

	public int getUnsavedChanges() {
		return changes;
	}
	
	@Override
	public ChestMenu addMenuOpeningHandler(MenuOpeningHandler handler) {
		if (handler instanceof SaveHandler) {
			return super.addMenuOpeningHandler(new SaveHandler(this, ((SaveHandler) handler).getOpeningHandler()));
		}
		else {
			return super.addMenuOpeningHandler(new SaveHandler(this, handler));
		}
	}
	
	public static class SaveHandler implements MenuOpeningHandler {
		
		private DirtyChestMenu menu;
		private MenuOpeningHandler handler;
		
		public SaveHandler(DirtyChestMenu menu, MenuOpeningHandler handler) {
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

}
