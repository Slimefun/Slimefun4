package me.mrCookieSlime.Slimefun.Misc;

import java.util.List;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public interface PostSlimefunLoadingHandler {
	
	public void run(List<SlimefunItem> preloaded, List<SlimefunItem> loaded, List<SlimefunItem> postloaded);

}
