package me.mrCookieSlime.Slimefun.CSCoreLibSetup;

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

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CSCoreLibLoader {
	
	Plugin plugin;
	URL url;
	URL download;
	File file;
	
	public CSCoreLibLoader(Plugin plugin) {
		this.plugin = plugin;
		try {
			this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=88802");
		} catch (MalformedURLException e) {
		}
	}
	
	public boolean load() {
		if (plugin.getServer().getPluginManager().isPluginEnabled("CS-CoreLib")) return true;
		else {
			System.err.println(" ");
			System.err.println("#################### - INFO - ####################");
			System.err.println(" ");
			System.err.println(plugin.getName() + " could not be loaded.");
			System.err.println("It appears that you have not installed CS-CoreLib");
			System.err.println("Your Server will now try to download and install");
			System.err.println("CS-CoreLib for you.");
			System.err.println("You will be asked to restart your Server when it's finished.");
			System.err.println("If this somehow fails, please download and install CS-CoreLib manually:");
			System.err.println("https://dev.bukkit.org/projects/cs-corelib");
			System.err.println(" ");
			System.err.println("#################### - INFO - ####################");
			System.err.println(" ");
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
        	System.err.println(" ");
        	System.err.println("#################### - WARNING - ####################");
			System.err.println(" ");
			System.err.println("Could not connect to BukkitDev.");
			System.err.println("Please download & install CS-CoreLib manually:");
			System.err.println("https://dev.bukkit.org/projects/cs-corelib");
			System.err.println(" ");
			System.err.println("#################### - WARNING - ####################");
			System.err.println(" ");
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

	            switch (connection.getResponseCode()) {
	                case HttpURLConnection.HTTP_MOVED_PERM:
	                case HttpURLConnection.HTTP_MOVED_TEMP:
	                    String loc = connection.getHeaderField("Location");
	                    location = new URL(new URL(location), loc).toExternalForm();
	                    continue;
	            }
	            break;
	        }
	        
	        return new URL(connection.getURL().toString().replaceAll(" ", "%20"));
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
        	System.err.println(" ");
        	System.err.println("#################### - WARNING - ####################");
			System.err.println(" ");
			System.err.println("Failed to download CS-CoreLib");
			System.err.println("Please download & install CS-CoreLib manually:");
			System.err.println("https://dev.bukkit.org/projects/cs-corelib");
			System.err.println(" ");
			System.err.println("#################### - WARNING - ####################");
			System.err.println(" ");
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
                System.err.println(" ");
                System.err.println("#################### - INFO - ####################");
                System.err.println(" ");
                System.err.println("Please restart your Server to finish the Installation");
                System.err.println("of " + plugin.getName() + " and CS-CoreLib");
                System.err.println(" ");
                System.err.println("#################### - INFO - ####################");
                System.err.println(" ");
            } catch (IOException e) {
            	e.printStackTrace();
            } 
        }
	}

}
