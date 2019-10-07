package me.mrCookieSlime.Slimefun.Setup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.plugin.Plugin;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CSCoreLibLoader {
	
	private Plugin plugin;
	private URL url;
	private URL download;
	private File file;
	
	public CSCoreLibLoader(Plugin plugin) {
		this.plugin = plugin;
		try {
			this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=88802");
		} catch (MalformedURLException e) {
            plugin.getLogger().log(Level.SEVERE, "The Auto-Updater URL is malformed!", e);
		}
	}
	
	public boolean load() {
		if (plugin.getServer().getPluginManager().isPluginEnabled("CS-CoreLib")) return true;
		else {
			Slimefun.getLogger().log(Level.INFO, " ");
			Slimefun.getLogger().log(Level.INFO, "#################### - INFO - ####################");
			Slimefun.getLogger().log(Level.INFO, " ");
			System.err.println(plugin.getName() + " 无法被加载.");
			Slimefun.getLogger().log(Level.INFO, "你的服务器可能没有安装前置 CS-CoreLib,");
			Slimefun.getLogger().log(Level.INFO, "我们正在自动为你下载.");
			Slimefun.getLogger().log(Level.INFO, "在下载完成后, 请重启服务器以安装前置插件");
			Slimefun.getLogger().log(Level.INFO, "如果无法自动下载, 你可以打开这个链接尝试重新下载:");
			Slimefun.getLogger().log(Level.INFO, "https://dev.bukkit.org/projects/cs-corelib");
			Slimefun.getLogger().log(Level.INFO, " ");
			Slimefun.getLogger().log(Level.INFO, "#################### - INFO - ####################");
			Slimefun.getLogger().log(Level.INFO, " ");
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				if (connect()) install();
			}, 10L);
			return false;
		}
	}
	
	private boolean connect() {
        try {
            final URLConnection connection = this.url.openConnection();
            connection.setConnectTimeout(5000);
            connection.addRequestProperty("User-Agent", "CS-CoreLib Loader (by mrCookieSlime)");
            connection.setDoOutput(true);

            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final JsonArray array = new JsonParser().parse(reader).getAsJsonArray();
            final JsonObject json = array.get(array.size() - 1).getAsJsonObject();

            download = traceURL(json.get("downloadUrl").getAsString().replace("https:", "http:"));
            file = new File("plugins/" + json.get("name").getAsString() + ".jar");
            
            return true;
        } catch (IOException e) {
        	Slimefun.getLogger().log(Level.INFO, " ");
        	Slimefun.getLogger().log(Level.INFO, "#################### - WARNING - ####################");
			Slimefun.getLogger().log(Level.INFO, " ");
			Slimefun.getLogger().log(Level.INFO, "无法连接至 BukkitDev.");
			Slimefun.getLogger().log(Level.INFO, "请手动下载并安装 CS-CoreLib:");
			Slimefun.getLogger().log(Level.INFO, "https://dev.bukkit.org/projects/cs-corelib");
			Slimefun.getLogger().log(Level.INFO, " ");
			Slimefun.getLogger().log(Level.INFO, "#################### - WARNING - ####################");
			Slimefun.getLogger().log(Level.INFO, " ");
            return false;
        }
    }
	
	private URL traceURL(String location) throws IOException {
	    	HttpURLConnection connection = null;
	    	
	        while (true) {
	            URL url = new URL(location);
	            connection = (HttpURLConnection) url.openConnection();

	            connection.setInstanceFollowRedirects(false);
	            connection.setConnectTimeout(5000);
	            connection.addRequestProperty("User-Agent", "Auto Updater (by mrCookieSlime)");

                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_MOVED_PERM || response == HttpURLConnection.HTTP_MOVED_TEMP) {
                    String loc = connection.getHeaderField("Location");
                    location = new URL(new URL(location), loc).toExternalForm();
	            }
                else {
                    break;
                }
	        }
	        
	        return new URL(connection.getURL().toString().replace(" ", "%20"));
	}
	
	private void install() {
		BufferedInputStream input = null;
		FileOutputStream output = null;
        try {
            input = new BufferedInputStream(download.openStream());
            output = new FileOutputStream(file);

            final byte[] data = new byte[1024];
            int read;
            while ((read = input.read(data, 0, 1024)) != -1) {
                output.write(data, 0, read);
            }
        } catch (Exception ex) {
        	Slimefun.getLogger().log(Level.INFO, " ");
        	Slimefun.getLogger().log(Level.INFO, "#################### - WARNING - ####################");
			Slimefun.getLogger().log(Level.INFO, " ");
            Slimefun.getLogger().log(Level.INFO, "无法连接至 BukkitDev.");
            Slimefun.getLogger().log(Level.INFO, "请手动下载并安装 CS-CoreLib:");
			Slimefun.getLogger().log(Level.INFO, "https://dev.bukkit.org/projects/cs-corelib");
			Slimefun.getLogger().log(Level.INFO, " ");
			Slimefun.getLogger().log(Level.INFO, "#################### - WARNING - ####################");
			Slimefun.getLogger().log(Level.INFO, " ");
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
                Slimefun.getLogger().log(Level.INFO, " ");
                Slimefun.getLogger().log(Level.INFO, "####################### - INFO - #######################");
                Slimefun.getLogger().log(Level.INFO, " ");
                Slimefun.getLogger().log(Level.INFO, "请重启服务器完成 " + plugin.getName() + " 和 CS-CoreLib 的安装");
                Slimefun.getLogger().log(Level.INFO, " ");
                Slimefun.getLogger().log(Level.INFO, "####################### - INFO - #######################");
                Slimefun.getLogger().log(Level.INFO, " ");
            } catch (IOException x) {
                Slimefun.getLogger().log(Level.SEVERE, "An Error occured while closing the Download Stream for CS-CoreLib", x);
            } 
        }
	}

}
