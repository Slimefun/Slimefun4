package me.mrCookieSlime.Slimefun.androids;

import java.util.logging.Level;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public enum ScriptPart {

	// Start and End Parts
	START(AndroidType.NONE, "&2Start Script", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFlMjk0MjJkYjQwNDdlZmRiOWJhYzJjZGFlNWEwNzE5ZWI3NzJmY2NjODhhNjZkOTEyMzIwYjM0M2MzNDEifX19"),
	REPEAT(AndroidType.NONE, "&9Repeat Script", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM4ZGVmNjdhMTI2MjJlYWQxZGVjZDNkODkzNjQyNTdiNTMxODk2ZDg3ZTQ2OTgxMzEzMWNhMjM1YjVjNyJ9fX0="),
	WAIT(AndroidType.NONE, "&eWait 0.5s", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmVlMTc0ZjQxZTU5NGU2NGVhMzE0MWMwN2RhZjdhY2YxZmEwNDVjMjMwYjJiMGIwZmIzZGExNjNkYjIyZjQ1NSJ9fX0="),

	// Movement
	GO_FORWARD(AndroidType.NON_FIGHTER, "&7Move forward", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliZjZkYjRhZWRhOWQ4ODIyYjlmNzM2NTM4ZThjMThiOWE0ODQ0Zjg0ZWI0NTUwNGFkZmJmZWU4N2ViIn19fQ=="),
	GO_UP(AndroidType.NON_FIGHTER, "&7Move upwards", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTA1YTJjYWI4YjY4ZWE1N2UzYWY5OTJhMzZlNDdjOGZmOWFhODdjYzg3NzYyODE5NjZmOGMzY2YzMWEzOCJ9fX0="),
	GO_DOWN(AndroidType.NON_FIGHTER, "&7Move downwards", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzAxNTg2ZTM5ZjZmZmE2M2I0ZmIzMDFiNjVjYTdkYThhOTJmNzM1M2FhYWI4OWQzODg2NTc5MTI1ZGZiYWY5In19fQ=="),

	//Directions
	TURN_LEFT(AndroidType.NONE, "&7Turn Left", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE4NWM5N2RiYjgzNTNkZTY1MjY5OGQyNGI2NDMyN2I3OTNhM2YzMmE5OGJlNjdiNzE5ZmJlZGFiMzVlIn19fQ=="),
	TURN_RIGHT(AndroidType.NONE, "&7Turn Right", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzFjMGVkZWRkNzExNWZjMWIyM2Q1MWNlOTY2MzU4YjI3MTk1ZGFmMjZlYmI2ZTQ1YTY2YzM0YzY5YzM0MDkxIn19fQ=="),

	// Action - Pickaxe
	DIG_UP(AndroidType.MINER, "&bDig upwards", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmU2Y2UwMTFhYzlhN2E3NWIyZmNkNDA4YWQyMWEzYWMxNzIyZjZlMmVlZDg3ODFjYWZkMTI1NTIyODJiODgifX19"),
	DIG_FORWARD(AndroidType.MINER, "&bDig forward", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlYTIxMzU4Mzg0NjE1MzQzNzJmMmRhNmM4NjJkMjFjZDVmM2QyYzcxMTlmMmJiNjc0YmJkNDI3OTEifX19"),
	DIG_DOWN(AndroidType.MINER, "&bDig downwards", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ4NjIwMjQxMDhjNzg1YmMwZWY3MTk5ZWM3N2M0MDJkYmJmY2M2MjRlOWY0MWY4M2Q4YWVkOGIzOWZkMTMifX19"),

	MOVE_AND_DIG_UP(AndroidType.MINER, "&bMove & Dig upwards", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmU2Y2UwMTFhYzlhN2E3NWIyZmNkNDA4YWQyMWEzYWMxNzIyZjZlMmVlZDg3ODFjYWZkMTI1NTIyODJiODgifX19"),
	MOVE_AND_DIG_FORWARD(AndroidType.MINER, "&bMove & Dig forward", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlYTIxMzU4Mzg0NjE1MzQzNzJmMmRhNmM4NjJkMjFjZDVmM2QyYzcxMTlmMmJiNjc0YmJkNDI3OTEifX19"),
	MOVE_AND_DIG_DOWN(AndroidType.MINER, "&bMove & Dig downwards", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ4NjIwMjQxMDhjNzg1YmMwZWY3MTk5ZWM3N2M0MDJkYmJmY2M2MjRlOWY0MWY4M2Q4YWVkOGIzOWZkMTMifX19"),

	// Action - Sword
	ATTACK_MOBS_ANIMALS(AndroidType.FIGHTER, "&4Attack &c(Hostile Mobs & Animals)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzdlNmM0MGY2OGI3NzVmMmVmY2Q3YmQ5OTE2YjMyNzg2OWRjZjI3ZTI0Yzg1NWQwYTE4ZTA3YWMwNGZlMSJ9fX0="),
	ATTACK_MOBS(AndroidType.FIGHTER, "&4Attack &c(Hostile Mobs)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzdlNmM0MGY2OGI3NzVmMmVmY2Q3YmQ5OTE2YjMyNzg2OWRjZjI3ZTI0Yzg1NWQwYTE4ZTA3YWMwNGZlMSJ9fX0="),
	ATTACK_ANIMALS(AndroidType.FIGHTER, "&4Attack &c(Animals)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzdlNmM0MGY2OGI3NzVmMmVmY2Q3YmQ5OTE2YjMyNzg2OWRjZjI3ZTI0Yzg1NWQwYTE4ZTA3YWMwNGZlMSJ9fX0="),
	ATTACK_ANIMALS_ADULT(AndroidType.FIGHTER, "&4Attack &c(Animals &7[Adult]&c)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzdlNmM0MGY2OGI3NzVmMmVmY2Q3YmQ5OTE2YjMyNzg2OWRjZjI3ZTI0Yzg1NWQwYTE4ZTA3YWMwNGZlMSJ9fX0="),

	// Action - Axe
	CHOP_TREE(AndroidType.WOODCUTTER, "&cChop and Replant", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRiYTQ5Mzg0ZGJhN2I3YWNkYjRmNzBlOTM2MWU2ZDU3Y2JiY2JmNzIwY2Y0ZjE2YzJiYjgzZTQ1NTcifX19"),

	// Action - Fishing Rod
	CATCH_FISH(AndroidType.FISHERMAN, "&bCatch Fish", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQ0ZmRlNTExZjQ0NTQxMDFlNGEyYTcyYmM4NmYxMjk4NWRmY2RhNzZiNjRiYjI0ZGM2M2E5ZmE5ZTNhMyJ9fX0="),

	// Action - Hoe
	FARM_FORWARD(AndroidType.FARMER, "&bHarvest and Replant", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGRlOWE1MjJjM2Q5ZTdkODVmM2Q4MmMzNzVkYzM3ZmVjYzg1NmRiZDgwMWViM2JjZWRjMTE2NTE5OGJmIn19fQ=="),
	FARM_DOWN(AndroidType.FARMER, "&bHarvest and Replant &7(Block underneath)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQ0Mjk2YjMzM2QyNTMxOWFmM2YzMzA1MTc5N2Y5ZTZkODIxY2QxOWEwMTRmYjcxMzdiZWI4NmE0ZTllOTYifX19"),

	// Action - ExoticGarden
	FARM_EXOTIC_FORWARD(AndroidType.ADVANCED_FARMER, "&bAdvanced Harvest and Replant", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGRlOWE1MjJjM2Q5ZTdkODVmM2Q4MmMzNzVkYzM3ZmVjYzg1NmRiZDgwMWViM2JjZWRjMTE2NTE5OGJmIn19fQ=="),
	FARM_EXOTIC_DOWN(AndroidType.ADVANCED_FARMER, "&bAdvanced Harvest and Replant &7(Block underneath)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQ0Mjk2YjMzM2QyNTMxOWFmM2YzMzA1MTc5N2Y5ZTZkODIxY2QxOWEwMTRmYjcxMzdiZWI4NmE0ZTllOTYifX19"),

	// Action - Interface
	INTERFACE_ITEMS(AndroidType.NONE, "&9Push Inventory Contents to the faced Interface", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTBhNGRiZjY2MjVjNDJiZTU3YThiYTJjMzMwOTU0YTc2YmRmMjI3ODU1NDBlODdhNWM5NjcyNjg1MjM4ZWMifX19"),
	INTERFACE_FUEL(AndroidType.NONE, "&cPull Fuel from the faced Interface", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQzMmY1MjgyYTUwNzQ1YjkxMmJlMTRkZWRhNTgxYmQ0YTA5Yjk3N2EzYzMyZDdlOTU3ODQ5MWZlZThmYTcifX19");


	private ItemStack item;
	private AndroidType type;

	private ScriptPart(AndroidType type, String name, String texture) {
		try {
			this.type = type;
			this.item = new CustomItem(CustomSkull.getItem(texture), name);
		} catch(Exception x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occured while initializing Android-Script Texture for Slimefun " + Slimefun.getVersion(), x);
		}
	}

	public ItemStack toItemStack() {
		return this.item;
	}

	public AndroidType getRequiredType() {
		return this.type;
	}

}
