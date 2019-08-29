package me.mrCookieSlime.Slimefun.hooks.github;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunStartup;

public final class GitHubSetup {
	
	private GitHubSetup() {}
	
	public static void setup() {
		new GitHubConnector() {
			
			@Override
			public void onSuccess(JsonElement element) {
				SlimefunStartup.instance.getUtilities().contributors.clear();
				JsonArray array = element.getAsJsonArray();
			    
			    for (int i = 0; i < array.size(); i++) {
			    	JsonObject object = array.get(i).getAsJsonObject();
			    	
			    	String name = object.get("login").getAsString();
			    	String job = "&cAuthor";
			    	int commits = object.get("contributions").getAsInt();
			    	String profile = object.get("html_url").getAsString();
			    	
			    	if (!name.equals("invalid-email-address")) {
			    		Contributor contributor = new Contributor(name, job, commits);
			    		contributor.setProfile(profile);
			    		SlimefunStartup.instance.getUtilities().contributors.add(contributor);
			    	}
			    }
			    SlimefunStartup.instance.getUtilities().contributors.add(new Contributor("AquaLazuryt", "&6Lead Head Artist", 0));
				
				SlimefunStartup.instance.getServer().getScheduler().runTaskAsynchronously(SlimefunStartup.instance, () -> {
					for (JsonElement e: array) {
						String name = e.getAsJsonObject().get("login").getAsString();
						
						if (SlimefunStartup.instance.getUtilities().contributorHeads.containsKey(name)) {
							continue;
						}
						
						InputStreamReader profile_reader = null;
						InputStreamReader session_reader = null;
						
						try {
							URL profile = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
							profile_reader = new InputStreamReader(profile.openStream());
							String uuid = new JsonParser().parse(profile_reader).getAsJsonObject().get("id").getAsString();
							
							URL session = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
				            session_reader = new InputStreamReader(session.openStream());
				            JsonArray properties = new JsonParser().parse(session_reader).getAsJsonObject().get("properties").getAsJsonArray();
				            
				            for (JsonElement el: properties) {
				            	if (el.isJsonObject() && el.getAsJsonObject().get("name").getAsString().equals("textures")) {
				            		SlimefunStartup.instance.getUtilities().contributorHeads.put(name, el.getAsJsonObject().get("value").getAsString());
									break;
				            	}
				            }
						} catch (Exception x) {
							SlimefunStartup.instance.getUtilities().contributorHeads.put(name, null);
						} finally {
							if (profile_reader != null) {
								try {
									profile_reader.close();
								} catch (IOException x) {
									x.printStackTrace();
								}
							}
							if (session_reader != null) {
								try {
									session_reader.close();
								} catch (IOException x) {
									x.printStackTrace();
								}
							}
						}
					}
				});
			}
			
			@Override
			public void onFailure() {
				SlimefunStartup.instance.getUtilities().contributors.clear();
				SlimefunStartup.instance.getUtilities().contributors.add(new Contributor("TheBusyBiscuit", "&cAuthor", 3));
				SlimefunStartup.instance.getUtilities().contributors.add(new Contributor("John000708", "&cAuthor", 2));
				SlimefunStartup.instance.getUtilities().contributors.add(new Contributor("AquaLazuryt", "&6Lead Head Artist", 0));
			}
			
			@Override
			public String getRepository() {
				return "TheBusyBiscuit/Slimefun4";
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
				SlimefunGuide.issues = object.get("open_issues_count").getAsInt();
				SlimefunGuide.forks = object.get("forks").getAsInt();
				SlimefunGuide.stars = object.get("stargazers_count").getAsInt();
				SlimefunGuide.last_update = IntegerFormat.parseGitHubDate(object.get("pushed_at").getAsString());
			}
			
			@Override
			public void onFailure() {
			}
			
			@Override
			public String getRepository() {
				return "TheBusyBiscuit/Slimefun4";
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
				JsonObject object = element.getAsJsonObject();
				SlimefunGuide.code_bytes = object.get("Java").getAsInt();
			}
			
			@Override
			public void onFailure() {
			}
			
			@Override
			public String getRepository() {
				return "TheBusyBiscuit/Slimefun4";
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
