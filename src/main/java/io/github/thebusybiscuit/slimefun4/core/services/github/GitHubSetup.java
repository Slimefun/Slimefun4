package io.github.thebusybiscuit.slimefun4.core.services.github;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun4.core.utils.NumberUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public final class GitHubSetup {
	
	private static final String REPOSITORY = "TheBusyBiscuit/Slimefun4";
	
	private GitHubSetup() {}
	
	public static void setup() {
		new ContributionsConnector("code", REPOSITORY, "&6Developer");
		new ContributionsConnector("wiki", "TheBusyBiscuit/Slimefun4-wiki", "&3Wiki Editor");
		new ContributionsConnector("resourcepack", "TheBusyBiscuit/Slimefun4-Resourcepack", "&cResourcepack Artist");
		
		new GitHubConnector() {
			
			@Override
			public void onSuccess(JsonElement element) {
				JsonObject object = element.getAsJsonObject();
				SlimefunPlugin.getUtilities().forks = object.get("forks").getAsInt();
				SlimefunPlugin.getUtilities().stars = object.get("stargazers_count").getAsInt();
				SlimefunPlugin.getUtilities().lastUpdate = NumberUtils.parseGitHubDate(object.get("pushed_at").getAsString());
			}
			
			@Override
			public String getRepository() {
				return REPOSITORY;
			}
			
			@Override
			public String getFileName() {
				return "repo";
			}

			@Override
			public String getURLSuffix() {
				return "";
			}
		};
		
		new GitHubConnector() {
			
			@Override
			public void onSuccess(JsonElement element) {
				JsonArray array = element.getAsJsonArray();
				
				int issues = 0;
				int prs = 0;
				
				for (JsonElement elem: array) {
					JsonObject obj = elem.getAsJsonObject();
					if (obj.has("pull_request")) prs++;
					else issues++;
				}
				
				SlimefunPlugin.getUtilities().issues = issues;
				SlimefunPlugin.getUtilities().prs = prs;
			}
			
			@Override
			public String getRepository() {
				return REPOSITORY;
			}
			
			@Override
			public String getFileName() {
				return "issues";
			}

			@Override
			public String getURLSuffix() {
				return "/issues";
			}
		};
		
		new GitHubConnector() {
			
			@Override
			public void onSuccess(JsonElement element) {
				JsonObject object = element.getAsJsonObject();
				SlimefunPlugin.getUtilities().codeBytes = object.get("Java").getAsInt();
			}
			
			@Override
			public String getRepository() {
				return REPOSITORY;
			}
			
			@Override
			public String getFileName() {
				return "languages";
			}

			@Override
			public String getURLSuffix() {
				return "/languages";
			}
		};
	}
}
