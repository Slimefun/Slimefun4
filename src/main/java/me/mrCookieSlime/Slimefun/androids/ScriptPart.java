package me.mrCookieSlime.Slimefun.androids;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;

public enum ScriptPart {

	// Start and End Parts
	START(AndroidType.NONE, "ae29422db4047efdb9bac2cdae5a0719eb772fccc88a66d912320b343c341"),
	REPEAT(AndroidType.NONE, "c8def67a12622ead1decd3d89364257b531896d87e469813131ca235b5c7"),
	WAIT(AndroidType.NONE, "ee174f41e594e64ea3141c07daf7acf1fa045c230b2b0b0fb3da163db22f455"),

	// Movement
	GO_FORWARD(AndroidType.NON_FIGHTER, "9bf6db4aeda9d8822b9f736538e8c18b9a4844f84eb45504adfbfee87eb"),
	GO_UP(AndroidType.NON_FIGHTER, "05a2cab8b68ea57e3af992a36e47c8ff9aa87cc8776281966f8c3cf31a38"),
	GO_DOWN(AndroidType.NON_FIGHTER, "01586e39f6ffa63b4fb301b65ca7da8a92f7353aaab89d3886579125dfbaf9"),

	//Directions
	TURN_LEFT(AndroidType.NONE, "185c97dbb8353de652698d24b64327b793a3f32a98be67b719fbedab35e"),
	TURN_RIGHT(AndroidType.NONE, "1c0ededd7115fc1b23d51ce966358b27195daf26ebb6e45a66c34c69c34091"),

	// Action - Pickaxe
	DIG_UP(AndroidType.MINER, "e6ce011ac9a7a75b2fcd408ad21a3ac1722f6e2eed8781cafd12552282b88"),
	DIG_FORWARD(AndroidType.MINER, "6ea2135838461534372f2da6c862d21cd5f3d2c7119f2bb674bbd42791"),
	DIG_DOWN(AndroidType.MINER, "d862024108c785bc0ef7199ec77c402dbbfcc624e9f41f83d8aed8b39fd13"),

	MOVE_AND_DIG_UP(AndroidType.MINER, "e6ce011ac9a7a75b2fcd408ad21a3ac1722f6e2eed8781cafd12552282b88"),
	MOVE_AND_DIG_FORWARD(AndroidType.MINER, "6ea2135838461534372f2da6c862d21cd5f3d2c7119f2bb674bbd42791"),
	MOVE_AND_DIG_DOWN(AndroidType.MINER, "d862024108c785bc0ef7199ec77c402dbbfcc624e9f41f83d8aed8b39fd13"),

	// Action - Sword
	ATTACK_MOBS_ANIMALS(AndroidType.FIGHTER, "7e6c40f68b775f2efcd7bd9916b327869dcf27e24c855d0a18e07ac04fe1"),
	ATTACK_MOBS(AndroidType.FIGHTER, "7e6c40f68b775f2efcd7bd9916b327869dcf27e24c855d0a18e07ac04fe1"),
	ATTACK_ANIMALS(AndroidType.FIGHTER, "7e6c40f68b775f2efcd7bd9916b327869dcf27e24c855d0a18e07ac04fe1"),
	ATTACK_ANIMALS_ADULT(AndroidType.FIGHTER, "7e6c40f68b775f2efcd7bd9916b327869dcf27e24c855d0a18e07ac04fe1"),

	// Action - Axe
	CHOP_TREE(AndroidType.WOODCUTTER, "4ba49384dba7b7acdb4f70e9361e6d57cbbcbf720cf4f16c2bb83e4557"),

	// Action - Fishing Rod
	CATCH_FISH(AndroidType.FISHERMAN, "d4fde511f4454101e4a2a72bc86f12985dfcda76b64bb24dc63a9fa9e3a3"),

	// Action - Hoe
	FARM_FORWARD(AndroidType.FARMER, "de9a522c3d9e7d85f3d82c375dc37fecc856dbd801eb3bcedc1165198bf"),
	FARM_DOWN(AndroidType.FARMER, "d4296b333d25319af3f33051797f9e6d821cd19a014fb7137beb86a4e9e96"),

	// Action - ExoticGarden
	FARM_EXOTIC_FORWARD(AndroidType.ADVANCED_FARMER, "de9a522c3d9e7d85f3d82c375dc37fecc856dbd801eb3bcedc1165198bf"),
	FARM_EXOTIC_DOWN(AndroidType.ADVANCED_FARMER, "d4296b333d25319af3f33051797f9e6d821cd19a014fb7137beb86a4e9e96"),

	// Action - Interface
	INTERFACE_ITEMS(AndroidType.NONE, "0a4dbf6625c42be57a8ba2c330954a76bdf22785540e87a5c9672685238ec"),
	INTERFACE_FUEL(AndroidType.NONE, "432f5282a50745b912be14deda581bd4a09b977a3c32d7e9578491fee8fa7");


	private final ItemStack item;
	private final AndroidType type;

	private ScriptPart(AndroidType type, String texture) {
		this.type = type;
		this.item = SkullItem.fromHash(texture);
	}

	public ItemStack getItem() {
		return item;
	}

	public AndroidType getRequiredType() {
		return type;
	}

}
