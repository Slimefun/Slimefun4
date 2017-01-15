package me.mrCookieSlime.Slimefun.GitHub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.mrCookieSlime.Slimefun.SlimefunGuide;

public class GitHubConnector {
	
	private File file;

	public GitHubConnector() {
		this.file = new File("plugins/Slimefun/cache/contributors.json");;
	}
	
	public void pullFile() {
		System.out.println("[Slimefun - GitHub] Downloading 'contributors.json' from GitHub...");
		
		try {
			URL website = new URL("https://api.github.com/repos/TheBusyBiscuit/Slimefun4/contributors");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			System.out.println("[Slimefun - GitHub] Finished download: 'contributors.json'");
		} catch (IOException e) {
			System.err.println("[Slimefun - GitHub] ERROR - Could not connect to GitHub in time.");
		}
	}
	
	public boolean hasData() {
		return this.getFile().exists();
	}
	
	public File getFile() {
		return this.file;
	}

	public void parseData() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));
			
			String full = "";
			
			String line;
		    while ((line = reader.readLine()) != null) {
		        full = full + line;
		    }
		    
		    reader.close();
		    
		    JsonElement element = new JsonParser().parse(full);
		    JsonArray array = element.getAsJsonArray();
		    
		    for (int i = 0; i < array.size(); i++) {
		    	JsonObject object = array.get(i).getAsJsonObject();
		    	
		    	String name = object.get("login").getAsString();
		    	String job = "&cAuthor";
		    	int commits = object.get("contributions").getAsInt();
		    	
		    	if (!name.equals("invalid-email-address")) {
		    		SlimefunGuide.contributors.add(new Contributor(name, job, commits));
		    	}
		    }
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
