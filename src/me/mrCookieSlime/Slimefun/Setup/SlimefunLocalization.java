package me.mrCookieSlime.Slimefun.Setup;

import io.github.thebusybiscuit.cscorelib2.config.Localization;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class SlimefunLocalization extends Localization {

	public SlimefunLocalization(SlimefunPlugin plugin) {
		super(plugin);
		
		setPrefix("&aSlimefun 4 &7> ");

		setDefaultMessage("commands.help", "Displays this Help Screen");
		setDefaultMessage("commands.cheat", "Allows you to cheat Items");
		setDefaultMessage("commands.give", "Give somebody some Slimefun Items");
		setDefaultMessage("commands.research.desc", "Unlock a Research for a Player");
		setDefaultMessage("commands.guide", "Gives yourself a Slimefun Guide");
		setDefaultMessage("commands.timings", "Lag-Info about your Server");
		setDefaultMessage("commands.teleporter", "See other Player's Waypoints");
		setDefaultMessage("commands.versions", "Lists all installed Addons");
		setDefaultMessage("commands.open_guide", "Opens Slimefun's guide without using the book");
		setDefaultMessage("commands.stats", "Shows some Stats about a Player");
		setDefaultMessage("commands.research.reset", "&cYou have reset %player%'s Knowledge");
		setDefaultMessage("commands.research.reset-target", "&cYour Knowledge has been reset");

		setDefaultMessage("guide.search.name", "&7Search");
		setDefaultMessages("guide.search.lore",
			"&bWhat would you like to search for?", "&7Type your search term into chat");

		setDefaultMessage("search.message", "&bWhat would you like to search for?");

		setDefaultMessage("messages.not-researched", "&4You do not have enough Knowledge to understand this");
		setDefaultMessage("messages.not-enough-xp", "&4You do not have enough XP to unlock this");
		setDefaultMessage("messages.unlocked", "&bYou have unlocked &7\"%research%\"");
		setDefaultMessages("messages.fortune-cookie", "&7Help me, I am trapped in a Fortune Cookie Factory!", "&7You will die tomorrow...     by a Creeper", "&7At some Point in your Life something bad will happen!!!", "&7Next week you will notice that this is not the real World, you are in a kind of \"Matrix\" or lets call it Computer game. Yes you are in a Computer Game", "&7This Cookie will taste good in a few seconds", "&7You will die soon and the last word you will hear is gonna be \"EXTERMINATE!!!\"", "&7Whatever you do, do not hug a Creeper... I tried it. It feels good, but it's not worth it.");
		setDefaultMessage("messages.only-players", "&4This Command is only for Players");
		setDefaultMessage("messages.no-permission", "&4You do not have the required Permission to do this");
		setDefaultMessage("messages.usage", "&4Usage: &c%usage%");
		setDefaultMessage("messages.not-online", "&4%player% &cis not online!");
		setDefaultMessage("messages.not-valid-item", "&4%item% &cis not a valid Item!");
		setDefaultMessage("messages.not-valid-amount", "&4%amount% &cis not a valid amount : it must be higher than 0!");
		setDefaultMessage("messages.given-item", "&bYou have been given &a%amount% &7\"%item%&7\"");
		setDefaultMessage("messages.give-item", "&bYou have given %player% &a%amount% &7\"%item%&7\"");
		setDefaultMessage("messages.not-valid-research", "&4%research% &cis not a valid Research!");
		setDefaultMessage("messages.give-research", "&bYou have given %player% the Research &7\"%research%&7\"");
		setDefaultMessage("messages.battery.add", "&2+ &7%charge% J &8(%machine%)");
		setDefaultMessage("messages.battery.remove", "&4- &7%charge% J &8(%machine%)");
		setDefaultMessage("messages.hungry", "&cYou are too hungry to do that!");
		setDefaultMessage("messages.mode-change", "&b%device% Mode changed to: &9%mode%");
		setDefaultMessage("messages.disabled-in-world", "&4&lThis Item has been disabled in this World");
		setDefaultMessage("messages.talisman.anvil", "&a&oYour Talisman saved your Tool from breaking");
		setDefaultMessage("messages.talisman.miner", "&a&oYour Talisman just doubled your Drops");
		setDefaultMessage("messages.talisman.hunter", "&a&oYour Talisman just doubled your Drops");
		setDefaultMessage("messages.talisman.lava", "&a&oYour Talisman saved you from burning to death");
		setDefaultMessage("messages.talisman.water", "&a&oYour Talisman saved you from drowning");
		setDefaultMessage("messages.talisman.angel", "&a&oYour Talisman saved you from taking Fall Damage");
		setDefaultMessage("messages.talisman.fire", "&a&oYour Talisman saved you from burning to death");
		setDefaultMessage("messages.talisman.magician", "&a&oYour Talisman gave you an additional Enchantment");
		setDefaultMessage("messages.talisman.traveller", "&a&oYour Talisman gave you a Speed Boost");
		setDefaultMessage("messages.talisman.warrior", "&a&oYour Talisman has improved your Strength for a While");
		setDefaultMessage("messages.talisman.knight", "&a&oYour Talisman gave you 5 Seconds of Regeneration");
		setDefaultMessage("messages.talisman.whirlwind", "&a&oYour Talisman reflected the Projectile");
		setDefaultMessage("messages.talisman.wizard", "&a&oYour Talisman has given you a better Fortune Level but maybe also lowered some other Enchantment Levels");
		setDefaultMessage("messages.soulbound-rune.fail", "&cYou can only bind one item to your soul at a time.");
		setDefaultMessage("messages.soulbound-rune.success", "&aYou have successfully bound this item to your soul! You will keep it when you die.");
		setDefaultMessage("messages.broken-leg", "&c&oSeems like you broke your Leg, maybe a Splint could help?");
		setDefaultMessage("messages.fixed-leg", "&a&oThe Splint helps. It feels better now.");
		setDefaultMessage("messages.start-bleeding", "&c&oYou started to bleed. Maybe a Bandage could help?");
		setDefaultMessage("messages.stop-bleeding", "&a&oThe Bandage helps. The Bleeding has stopped!");
		setDefaultMessage("messages.disabled-item", "&4&lThis Item has been disabled! How did you even get that?");
		setDefaultMessage("messages.research.start", "&7The Ancient Spirits whisper mysterious Words into your Ear!");
		setDefaultMessage("messages.research.progress", "&7You start to wonder about &b%research% &e(%progress%)");
		setDefaultMessage("messages.fire-extinguish", "&7You have extinguished yourself");
		setDefaultMessage("messages.cannot-place" ,"&cYou cannot place that block there!");
		setDefaultMessage("messages.no-pvp" ,"&cYou cannot pvp in here!");

		setDefaultMessage("machines.pattern-not-found", "&eSorry, I could not recognize this Pattern. Please place the Items in the correct Pattern into the Dispenser.");
		setDefaultMessage("machines.unknown-material", "&eSorry, I could not recognize the Item in my Dispenser. Please put something in that I know.");
		setDefaultMessage("machines.wrong-item", "&eSorry, I could not recognize the Item you right clicked me with. Check the Recipes and see which Items you can use.");
		setDefaultMessage("machines.full-inventory", "&eSorry, my Inventory is too full!");
		setDefaultMessage("machines.in-use", "&cThis Block's Inventory is currently opened by a different Player.");
		setDefaultMessage("machines.ignition-chamber-no-flint", "&cIgnition Chamber is missing Flint and Steel.");
		setDefaultMessage("anvil.not-working", "&4You cannot use Slimefun Items in an Anvil");
		setDefaultMessage("backpack.already-open", "&cSorry, this backpack is open somewhere else!");
		setDefaultMessage("backpack.no-stack", "&cYou cannot stack Backpacks");
		setDefaultMessage("miner.no-ores", "&eSorry, I could not find any Ores nearby!");
		setDefaultMessage("workbench.not-enhanced", "&4You cannot use Slimefun Items in a normal Workbench");

		setDefaultMessage("machines.ANCIENT_ALTAR.not-enough-pedestals", "&4The Altar is not surrounded by the needed Amount of Pedestals &c(%pedestals% / 8)");
		setDefaultMessage("machines.ANCIENT_ALTAR.unknown-catalyst", "&4Unknown Catalyst! &cUse the correct Recipe instead!");
		setDefaultMessage("machines.ANCIENT_ALTAR.unknown-recipe", "&4Unknown Recipe! &cUse the correct Recipe instead!");
		setDefaultMessage("machines.ANCIENT_PEDESTAL.obstructed", "&4Pedestal is obstructed! &cRemove anything above the pedestal!");
		setDefaultMessage("machines.HOLOGRAM_PROJECTOR.enter-text", "&7Please enter your desired Hologram Text in your Chat. &r(Color Codes are supported!)");
		setDefaultMessage("machines.ELEVATOR.no-destinations", "&4No Destinations found");
		setDefaultMessage("machines.CARGO_NODES.must-be-placed", "&4Must be placed onto a Chest or Machine");

		setDefaultMessage("gps.waypoint.new", "&ePlease type in a Name for your new Waypoint in the Chat. &7(Color Codes supported!)");
		setDefaultMessage("gps.waypoint.added", "&aSuccessfully added a new Waypoint");
		setDefaultMessage("gps.waypoint.max", "&4You have reached the Maximum Amount of Waypoints");
		setDefaultMessages("gps.insufficient-complexity", "&4Insufficient GPS Network Complexity: &c%complexity%", "&4a) You do not have a GPS Network setup yet", "&4b) Your GPS Network is not complex enough");
		setDefaultMessage("gps.geo.scan-required", "&4GEO-Scan required! &cScan this Chunk using a GEO-Scanner first!");
		
		setDefaultMessage("robot.started", "&7Your Robot resumed running its Script");
		setDefaultMessage("robot.stopped", "&7Your Robot has paused its Script");
		setDefaultMessage("inventory.no-access", "&4You are not permitted to access this Block");

		setDefaultMessage("android.scripts.already-uploaded", "&4This Script has already been uploaded.");
		setDefaultMessages("android.scripts.enter-name", "", "&ePlease type in a Name for your Script", "");
		setDefaultMessages("android.scripts.uploaded", "&bUploading...", "&aSuccessfully uploaded your Script!");
		setDefaultMessage("android.scripts.rating.own", "&4You cannot rate your own Script!");
		setDefaultMessage("android.scripts.rating.already", "&4You have already left a Rating for this Script!");
		
		save();
	}
	
	@Override
	public String setDefaultMessage(String key, String message) {
		Object value = getConfig().getValue(key);
		
		if (value == null) {
			return super.setDefaultMessage(key, message);
		}
		else if (!(value instanceof String)) {
			String msg = getMessages(key).get(0);
			getConfig().setValue(key, msg);
			return msg;
		}
		else {
			return (String) value;
		}
	}

}
