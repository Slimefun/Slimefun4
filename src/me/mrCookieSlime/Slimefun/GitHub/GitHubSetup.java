package me.mrCookieSlime.Slimefun.GitHub;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.mrCookieSlime.Slimefun.SlimefunGuide;

public class GitHubSetup {

	public static void setup() {
		new GitHubConnector() {
			
			@Override
			public void onSuccess(JsonElement element) {
				SlimefunGuide.contributors.clear();
				JsonArray array = element.getAsJsonArray();
			    
			    for (int i = 0; i < array.size(); i++) {
			    	JsonObject object = array.get(i).getAsJsonObject();
			    	
			    	String name = object.get("login").getAsString();
			    	String job = "&cAuthor";
			    	int commits = object.get("contributions").getAsInt();
			    	String profile = object.get("html_url").getAsString();
			    	
			    	if (!name.equals("invalid-email-address")) {
			    		Contributor contributor = new Contributor(name, job, commits);
			    		contributor.profile = profile;
			    		SlimefunGuide.contributors.add(contributor);
			    	}
			    }
				SlimefunGuide.contributors.add(new Contributor("AquaLazuryt", "&6Lead Head Artist", 0));
			}
			
			@Override
			public void onFailure() {
				SlimefunGuide.contributors.clear();
				SlimefunGuide.contributors.add(new Contributor("TheBusyBiscuit", "&cAuthor", 3));
				SlimefunGuide.contributors.add(new Contributor("John000708", "&cAuthor", 2));
				SlimefunGuide.contributors.add(new Contributor("AquaLazuryt", "&6Lead Head Artist", 0));
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
				SlimefunGuide.code_lines = object.get("Java").getAsInt();
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
				return "lines_of_code";
			}

			@Override
			public String getURLSuffix() {
				return "/languages";
			}
		};
	}

}
