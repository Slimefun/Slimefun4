package me.mrCookieSlime.Slimefun.Setup;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Localization;

public class Messages {
	
	public static Localization local;
	
	public static void setup() {
		local.setPrefix("&aSlimefun &7> ");
		local.setDefault("messages.not-researched", "&4You do not have enough Knowledge to understand this");
		local.setDefault("messages.not-enough-xp", "&4You do not have enough XP to unlock this");
		local.setDefault("messages.unlocked", "&bYou have unlocked &7\"%research%\"");
		local.setDefault("messages.fortune-cookie", new String[] {"&7Help me, I am trapped in a Fortune Cookie Factory!", "&7You will die tomorrow...     by a Creeper", "&7At some Point in your Life something bad will happen!!!", "&7Next week you will notice that this is not the real World, you are in a kind of \"Matrix\" or lets call it Computer game. Yes you are in a Computer Game", "&7This Cookie will taste good in a few seconds", "&7You will die soon and the last word you will hear is gonna be \"EXTERMINATE!!!\"", "&7Whatever you do, do not hug a Creeper... I tried it. It feels good, but it's not worth it."});
		local.setDefault("commands.help", "Displays this Help Screen");
		local.setDefault("commands.cheat", "Allows you to cheat Items");
		local.setDefault("commands.give", "Give somebody some Slimefun Items");
		local.setDefault("commands.research.desc", "Unlock a Research for a Player");
		local.setDefault("commands.guide", "Gives yourself a Slimefun Guide");
		local.setDefault("commands.timings", "Lag-Info about your Server");
		local.setDefault("commands.teleporter", "See other Player's Waypoints");
		local.setDefault("commands.versions", "Lists all installed Addons");
		
		local.setDefault("messages.only-players", "&4This Command is only for Players");
		local.setDefault("messages.no-permission", "&4You do not have the required Permission to do this");
		local.setDefault("messages.usage", "&4Usage: &c%usage%");
		local.setDefault("messages.not-online", "&4%player% &cis not online!");
		local.setDefault("messages.not-valid-item", "&4%item% &cis not a valid Item!");
		local.setDefault("messages.not-valid-amount", "&4%amount% &cis not a valid amount : it must be higher than 0!");
		local.setDefault("messages.given-item", "&bYou have been given &a%amount% &7\"%item%\"");
		local.setDefault("messages.give-item", "&bYou have given %player% &a%amount% &7\"%item%\"");
		local.setDefault("messages.not-valid-research", "&4%research% &cis not a valid Research!");
		local.setDefault("messages.give-research", "&bYou have given %player% the Research &7\"%research%\"");
		local.setDefault("messages.battery.add", "&2+ &7%charge% J &8(%machine%)");
		local.setDefault("messages.battery.remove", "&4- &7%charge% J &8(%machine%)");
		local.setDefault("messages.hungry", "&cYou are too hungry to do that!");
		local.setDefault("messages.mode-change", "&b%device% Mode changed to: &9%mode%");
		local.setDefault("messages.disabled-in-world", "&4&lThis Item has been disabled in this World");
		local.setDefault("messages.talisman.anvil", "&a&oYour Talisman saved your Tool from breaking");
		local.setDefault("messages.talisman.miner", "&a&oYour Talisman just doubled your Drops");
		local.setDefault("messages.talisman.hunter", "&a&oYour Talisman just doubled your Drops");
		local.setDefault("messages.talisman.lava", "&a&oYour Talisman saved you from burning to death");
		local.setDefault("messages.talisman.water", "&a&oYour Talisman saved you from drowning");
		local.setDefault("messages.talisman.angel", "&a&oYour Talisman saved you from taking Fall Damage");
		local.setDefault("messages.talisman.fire", "&a&oYour Talisman saved you from burning to death");
		local.setDefault("messages.talisman.magician", "&a&oYour Talisman gave you an additional Enchantment");
		local.setDefault("messages.talisman.traveller", "&a&oYour Talisman gave you a Speed Boost");
		local.setDefault("messages.talisman.warrior", "&a&oYour Talisman has improved your Strength for a While");
		local.setDefault("messages.talisman.knight", "&a&oYour Talisman gave you 5 Seconds of Regeneration");
		local.setDefault("messages.talisman.whirlwind", "&a&oYour Talisman reflected the Projectile");
		local.setDefault("messages.talisman.wizard", "&a&oYour Talisman has given you a better Fortune Level but maybe also lowered some other Enchantment Levels");
		local.setDefault("messages.broken-leg", "&c&oSeems like you broke your Leg, maybe a Splint could help?");
		local.setDefault("messages.fixed-leg", "&a&oThe Splint helps. It feels better now.");
		local.setDefault("messages.start-bleeding", "&c&oYou started to bleed. Maybe a Bandage could help?");
		local.setDefault("messages.stop-bleeding", "&a&oThe Bandage helps. The Bleeding has stopped!");
		local.setDefault("messages.disabled-item", "&4&lThis Item has been disabled! How did you even get that?");
		local.setDefault("messages.research.start", "&7The Ancient Spirits whisper mysterious Words into your Ear!");
		local.setDefault("messages.research.progress", "&7You start to wonder about &b%research% &e(%progress%)");
		local.setDefault("commands.stats", "Shows some Stats about a Player");
		local.setDefault("messages.fire-extinguish", "&7You have extinguished yourself");
		local.setDefault("machines.pattern-not-found", "&eSorry, I could not recognize this Pattern. Please place the Items in the correct Pattern into the Dispenser.");
		local.setDefault("machines.unknown-material", "&eSorry, I could not recognize the Item in my Dispenser. Please put something in that I know.");
		local.setDefault("machines.wrong-item", "&eSorry, I could not recognize the Item you right clicked me with. Check the Recipes and see which Items you can use.");
		local.setDefault("machines.full-inventory", "&eSorry, my Inventory is too full!");
		local.setDefault("miner.no-ores", "&eSorry, I could not find any Ores nearby!");
		local.setDefault("backpack.already-open", "&cSorry, this backpack is open somewhere else!");
		local.setDefault("backpack.no-stack", "&cYou cannot stack Backpacks");
		local.setDefault("workbench.not-enhanced", "&4You cannot use Slimefun Items in a normal Workbench");
		local.setDefault("anvil.not-working", "&4You cannot use Slimefun Items in an Anvil");
		local.setDefault("commands.research.reset", "&cYou have reset %player%'s Knowledge");
		local.setDefault("commands.research.reset-target", "&cYour Knowledge has been reset");
		local.setDefault("machines.in-use", "&cThis Block's Inventory is currently opened by a different Player.");
		
		local.setDefault("gps.waypoint.new", "&ePlease type in a Name for your new Waypoint in the Chat. &7(Color Codes supported!)");
		local.setDefault("gps.waypoint.added", "&aSuccessfully added a new Waypoint");
		local.setDefault("gps.waypoint.max", "&4You have reached the Maximum Amount of Waypoints");
		local.setDefault("gps.insufficient-complexity", "&4Insufficient GPS Network Complexity: &c%complexity%", "&4a) You do not have a GPS Network setup yet", "&4b) Your GPS Network is not complex enough");
		local.setDefault("gps.geo.scan-required", "&4GEO-Scan required! &cScan this Chunk using a GEO-Scanner first!");
		
		local.setDefault("robot.started", "&7Your Robot resumed running its Script");
		local.setDefault("robot.stopped", "&7Your Robot has paused its Script");
		local.setDefault("inventory.no-access", "&4You are not permitted to access this Block");
		
		local.setDefault("machines.ANCIENT_ALTAR.not-enough-pedestals", "&4The Altar is not surrounded by the needed Amount of Pedestals &c(%pedestals% / 8)");
		local.setDefault("machines.ANCIENT_ALTAR.unknown-catalyst", "&4Unknown Catalyst! &cUse the correct Recipe instead!");
		local.setDefault("machines.ANCIENT_ALTAR.unknown-recipe", "&4Unknown Recipe! &cUse the correct Recipe instead!");
		local.setDefault("machines.HOLOGRAM_PROJECTOR.enter-text", "&7Please enter your desired Hologram Text in your Chat. &r(Color Codes are supported!)");
		local.setDefault("machines.ELEVATOR.no-destinations", "&4No Destinations found");
		local.setDefault("machines.CARGO_NODES.must-be-placed", "&4Must be placed onto a Chest or Machine");

		local.setDefault("android.scripts.already-uploaded", "&4This Script has already been uploaded.");
		local.setDefault("android.scripts.enter-name", "", "&ePlease type in a Name for your Script", "");
		local.setDefault("android.scripts.uploaded", "&bUploading...", "&aSuccessfully uploaded your Script!");
		local.setDefault("android.scripts.rating.own", "&4You cannot rate your own Script!");
		local.setDefault("android.scripts.rating.already", "&4You have already left a Rating for this Script!");
		
		local.save();
	}

}
