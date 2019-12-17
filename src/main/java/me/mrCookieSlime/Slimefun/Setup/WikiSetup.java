package me.mrCookieSlime.Slimefun.Setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class WikiSetup {

	private WikiSetup() {}
	
	public static void addWikiPages(SlimefunPlugin plugin) {
		JsonParser parser = new JsonParser();
		Slimefun.getLogger().log(Level.INFO, "正在加载 Wiki 页面...");
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream("/wiki.json")))) {
            JsonElement element = parser.parse(reader.lines().collect(Collectors.joining("")));
            JsonObject json = element.getAsJsonObject();
            
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            	SlimefunItem item = SlimefunItem.getByID(entry.getKey());
            	
            	if (item != null) {
            		item.addWikipage(entry.getValue().getAsString());
            	}
            }
        } catch (IOException e) {
            Slimefun.getLogger().log(Level.SEVERE, "无法加载 wiki.json 文件", e);
        }
	}
	
}
