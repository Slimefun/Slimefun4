package me.mrCookieSlime.Slimefun.hooks.github;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public final class GitHubSetup {
	
	private static final String REPOSITORY = "TheBusyBiscuit/Slimefun4";
	
	private static final String ROLE_AUTHOR = "&c作者";
	
	private GitHubSetup() {}
	
	public static void setup() {
		new GitHubConnector() {
			
			@Override
			public void onSuccess(JsonElement element) {
				SlimefunPlugin.getUtilities().contributors.clear();
				JsonArray array = element.getAsJsonArray();
			    
			    for (int i = 0; i < array.size(); i++) {
			    	JsonObject object = array.get(i).getAsJsonObject();
			    	
			    	String name = object.get("login").getAsString();
			    	int commits = object.get("contributions").getAsInt();
			    	String profile = object.get("html_url").getAsString();
			    	
			    	if (!name.equals("invalid-email-address")) {
			    		Contributor contributor = new Contributor(name, ROLE_AUTHOR, commits);
			    		contributor.setProfile(profile);
			    		SlimefunPlugin.getUtilities().contributors.add(contributor);
			    	}
			    }
			    SlimefunPlugin.getUtilities().contributors.add(new Contributor("AquaLazuryt", "&6Lead Head Artist", 0));
				
				SlimefunPlugin.instance.getServer().getScheduler().runTaskAsynchronously(SlimefunPlugin.instance, () -> {
					for (JsonElement e: array) {
						String name = e.getAsJsonObject().get("login").getAsString();
						
						if (SlimefunPlugin.getUtilities().contributorHeads.containsKey(name)) {
							continue;
						}
						
						try (InputStreamReader profileReader = new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream())) {
							String uuid = new JsonParser().parse(profileReader).getAsJsonObject().get("id").getAsString();
							
							try (InputStreamReader sessionReader = new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false").openStream())) {
								JsonArray properties = new JsonParser().parse(sessionReader).getAsJsonObject().get("properties").getAsJsonArray();
					            
					            for (JsonElement el: properties) {
					            	if (el.isJsonObject() && el.getAsJsonObject().get("name").getAsString().equals("textures")) {
					            		SlimefunPlugin.getUtilities().contributorHeads.put(name, el.getAsJsonObject().get("value").getAsString());
										break;
					            	}
					            }
							}
						} catch (IOException | IllegalStateException x) {
							if (SlimefunPlugin.isActive()) {
								SlimefunPlugin.getUtilities().contributorHeads.put(name, null);
							}
						}
					}
				});
			}
			
			@Override
			public void onFailure() {
				SlimefunPlugin.getUtilities().contributors.clear();
				SlimefunPlugin.getUtilities().contributors.add(new Contributor("TheBusyBiscuit", ROLE_AUTHOR, 1));
				SlimefunPlugin.getUtilities().contributors.add(new Contributor("AquaLazuryt", "&6Lead Head Artist", 0));
			}
			
			@Override
			public String getRepository() {
				return REPOSITORY;
			}
			
			@Override
			public String getFileName() {
				return "contributors";
			}

			@Override
			public String getURLSuffix() {
				return "/contributors";
			}
		};
		
		new GitHubConnector() {
			
			@Override
			public void onSuccess(JsonElement element) {
				JsonObject object = element.getAsJsonObject();
				SlimefunPlugin.getUtilities().forks = object.get("forks").getAsInt();
				SlimefunPlugin.getUtilities().stars = object.get("stargazers_count").getAsInt();
				SlimefunPlugin.getUtilities().lastUpdate = IntegerFormat.parseGitHubDate(object.get("pushed_at").getAsString());
			}
			
			@Override
			public void onFailure() {
				// We don't have to do anything on failure
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
			public void onFailure() {
				// We don't have to do anything on failure
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
			public void onFailure() {
				// We don't have to do anything on failure
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
