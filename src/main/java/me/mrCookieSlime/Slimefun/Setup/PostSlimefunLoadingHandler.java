package me.mrCookieSlime.Slimefun.Setup;

import java.util.List;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

@Deprecated
@FunctionalInterface
public interface PostSlimefunLoadingHandler {
	
	public void run(List<SlimefunItem> preloaded, List<SlimefunItem> loaded, List<SlimefunItem> postloaded);

}
