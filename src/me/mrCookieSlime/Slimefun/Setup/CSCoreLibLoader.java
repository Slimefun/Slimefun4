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

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.mrCookieSlime.Slimefun.api.Slimefun;

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
			Slimefun.getLogger().log(Level.INFO, plugin.getName() + " could not be loaded.");
			Slimefun.getLogger().log(Level.INFO, "It appears that you have not installed CS-CoreLib");
			Slimefun.getLogger().log(Level.INFO, "Your Server will now try to download and install");
			Slimefun.getLogger().log(Level.INFO, "CS-CoreLib for you.");
			Slimefun.getLogger().log(Level.INFO, "You will be asked to restart your Server when it's finished.");
			Slimefun.getLogger().log(Level.INFO, "If this somehow fails, please download and install CS-CoreLib manually:");
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
        	Slimefun.getLogger().log(Level.WARNING, " ");
        	Slimefun.getLogger().log(Level.WARNING, "#################### - WARNING - ####################");
			Slimefun.getLogger().log(Level.WARNING, " ");
			Slimefun.getLogger().log(Level.WARNING, "Could not connect to BukkitDev.");
			Slimefun.getLogger().log(Level.WARNING, "Please download & install CS-CoreLib manually:");
			Slimefun.getLogger().log(Level.WARNING, "https://dev.bukkit.org/projects/cs-corelib");
			Slimefun.getLogger().log(Level.WARNING, " ");
			Slimefun.getLogger().log(Level.WARNING, "#################### - WARNING - ####################");
			Slimefun.getLogger().log(Level.WARNING, " ");
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
        	Slimefun.getLogger().log(Level.WARNING, " ");
        	Slimefun.getLogger().log(Level.WARNING, "#################### - WARNING - ####################");
			Slimefun.getLogger().log(Level.WARNING, " ");
			Slimefun.getLogger().log(Level.WARNING, "Failed to download CS-CoreLib");
			Slimefun.getLogger().log(Level.WARNING, "Please download & install CS-CoreLib manually:");
			Slimefun.getLogger().log(Level.WARNING, "https://dev.bukkit.org/projects/cs-corelib");
			Slimefun.getLogger().log(Level.WARNING, " ");
			Slimefun.getLogger().log(Level.WARNING, "#################### - WARNING - ####################");
			Slimefun.getLogger().log(Level.WARNING, " ");
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
                Slimefun.getLogger().log(Level.INFO, " ");
                Slimefun.getLogger().log(Level.INFO, "#################### - INFO - ####################");
                Slimefun.getLogger().log(Level.INFO, " ");
                Slimefun.getLogger().log(Level.INFO, "Please restart your Server to finish the Installation");
                Slimefun.getLogger().log(Level.INFO, "of " + plugin.getName() + " and CS-CoreLib");
                Slimefun.getLogger().log(Level.INFO, " ");
                Slimefun.getLogger().log(Level.INFO, "#################### - INFO - ####################");
                Slimefun.getLogger().log(Level.INFO, " ");
            } catch (IOException x) {
            	Slimefun.getLogger().log(Level.SEVERE, "An Error occured while closing the Download Stream for CS-CoreLib", x);
            } 
        }
	}

}
