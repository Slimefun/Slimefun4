package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import me.mrCookieSlime.Slimefun.api.Slimefun;

public abstract class GitHubConnector {

	protected final File file;
	protected final GitHubService github;

	public GitHubConnector(GitHubService github) {
		this.github = github;
		
		file = new File("plugins/Slimefun/cache/github/" + this.getFileName() + ".json");
	}

	public abstract String getFileName();
	public abstract String getRepository();
	public abstract String getURLSuffix();
	public abstract void onSuccess(JsonElement element);
	
	public void onFailure() {
		// Don't do anything by default
	}

	public void pullFile() {
		if (github.isLoggingEnabled()) {
			Slimefun.getLogger().log(Level.INFO, "Retrieving '" + this.getFileName() + ".json' from GitHub...");
		}
	
		try {
			URL website = new URL("https://api.github.com/repos/" + this.getRepository() + this.getURLSuffix());

			URLConnection connection = website.openConnection();
			connection.setConnectTimeout(3000);
			connection.addRequestProperty("User-Agent", "Slimefun 4 GitHub Agent (by TheBusyBiscuit)");
			connection.setDoOutput(true);

			try (ReadableByteChannel channel = Channels.newChannel(connection.getInputStream())) {
				try (FileOutputStream stream = new FileOutputStream(file)) {
					stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
					this.parseData();
				}
			}
		} catch (IOException e) {
			if (github.isLoggingEnabled()) {
				Slimefun.getLogger().log(Level.WARNING, "Could not connect to GitHub in time.");
			}

			if (hasData()) {
				this.parseData();
			}
			else {
				this.onFailure();
			}
		}
	}

	public boolean hasData() {
		return this.getFile().exists();
	}

	public File getFile() {
		return this.file;
	}

	public void parseData() {
		try (BufferedReader reader = new BufferedReader(new FileReader(this.getFile()))) {
			StringBuilder builder = new StringBuilder();

			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			JsonElement element = new JsonParser().parse(builder.toString());
			this.onSuccess(element);
		} 
		catch (IOException x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occured while parsing GitHub-Data for Slimefun " + Slimefun.getVersion(), x);
			this.onFailure();
		}
	}
}
