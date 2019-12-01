package me.mrCookieSlime.Slimefun.hooks.github;

import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class ContributionsConnector extends GitHubConnector {
	
	// GitHub Bots that do not count as Contributors
	// (includes "invalid-email-address" because it is an invalid contributor)
	private static final List<String> blacklist = Arrays.asList(
		"invalid-email-address",
		"renovate-bot",
		"ImgBotApp",
		"TheBusyBot"
	);
	
	private final String prefix;
	private final String repository;
	private final String role;
	
	public ContributionsConnector(String prefix, String repository, String role) {
		this.prefix = prefix;
		this.repository = repository;
		this.role = role;
	}
	
	@Override
	public void onSuccess(JsonElement element) {
		computeContributors(element.getAsJsonArray());
	}
	
	@Override
	public String getRepository() {
		return repository;
	}
	
	@Override
	public String getFileName() {
		return prefix + "_contributors";
	}

	@Override
	public String getURLSuffix() {
		return "/contributors?per_page=100";
	}

	private void computeContributors(JsonArray array) {
	    for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	String name = object.get("login").getAsString();
	    	int commits = object.get("contributions").getAsInt();
	    	String profile = object.get("html_url").getAsString();
	    	
	    	if (!blacklist.contains(name)) {
	    		Contributor contributor = SlimefunPlugin.getUtilities().contributors.computeIfAbsent(name, key -> new Contributor(name, profile));
	    		contributor.setContribution(role, commits);
	    	}
	    }
	}
}