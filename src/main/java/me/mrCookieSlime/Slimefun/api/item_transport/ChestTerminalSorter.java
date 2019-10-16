package me.mrCookieSlime.Slimefun.api.item_transport;

import java.util.Comparator;

public class ChestTerminalSorter implements Comparator<StoredItem>{

	@Override
	public int compare(StoredItem item1, StoredItem item2) {
		return item2.getAmount() - item1.getAmount();
	}

}
