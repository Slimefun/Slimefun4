package me.mrCookieSlime.Slimefun.autosave;

import java.util.Iterator;

import me.mrCookieSlime.Slimefun.api.PlayerProfile;

public class PlayerAutoSaver implements Runnable {

	@Override
	public void run() {
		Iterator<PlayerProfile> iterator = PlayerProfile.iterator();
		int players = 0;
		
		while (iterator.hasNext()) {
			PlayerProfile profile = iterator.next();
			
			if (profile.isDirty()) {
				players++;
				profile.save();
			}
			
			if (profile.isMarkedForDeletion()) iterator.remove();
		}
		
		if (players > 0) {
			System.out.println("[Slimefun] Auto-Saved Player Data for " + players + " Player(s)!");
		}
	}

}
