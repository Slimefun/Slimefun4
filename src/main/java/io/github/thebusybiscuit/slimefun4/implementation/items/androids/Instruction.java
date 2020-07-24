package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.function.Predicate;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

enum Instruction {

    // Start and End Parts
    START(AndroidType.NONE, HeadTexture.SCRIPT_START),
    REPEAT(AndroidType.NONE, HeadTexture.SCRIPT_REPEAT),
    WAIT(AndroidType.NONE, HeadTexture.SCRIPT_WAIT),

    // Movement
    GO_FORWARD(AndroidType.NON_FIGHTER, HeadTexture.SCRIPT_FORWARD, (android, b, inv, face) -> {
        Block target = b.getRelative(face);
        android.move(b, face, target);
    }),

    GO_UP(AndroidType.NON_FIGHTER, HeadTexture.SCRIPT_UP, (android, b, inv, face) -> {
        Block target = b.getRelative(BlockFace.UP);
        android.move(b, face, target);
    }),

    GO_DOWN(AndroidType.NON_FIGHTER, HeadTexture.SCRIPT_DOWN, (android, b, inv, face) -> {
        Block target = b.getRelative(BlockFace.DOWN);
        android.move(b, face, target);
    }),

    // Directions
    TURN_LEFT(AndroidType.NONE, HeadTexture.SCRIPT_LEFT, (android, b, inv, face) -> {
        int mod = -1;
        android.rotate(b, face, mod);
    }),

    TURN_RIGHT(AndroidType.NONE, HeadTexture.SCRIPT_RIGHT, (android, b, inv, face) -> {
        int mod = 1;
        android.rotate(b, face, mod);
    }),

    // Action - Pickaxe
    DIG_UP(AndroidType.MINER, HeadTexture.SCRIPT_DIG_UP, (android, b, inv, face) -> {
        Block target = b.getRelative(BlockFace.UP);
        android.dig(b, inv, target);
    }),

    DIG_FORWARD(AndroidType.MINER, HeadTexture.SCRIPT_DIG_FORWARD, (android, b, inv, face) -> {
        Block target = b.getRelative(face);
        android.dig(b, inv, target);
    }),

    DIG_DOWN(AndroidType.MINER, HeadTexture.SCRIPT_DIG_DOWN, (android, b, inv, face) -> {
        Block target = b.getRelative(BlockFace.DOWN);
        android.dig(b, inv, target);
    }),

    MOVE_AND_DIG_UP(AndroidType.MINER, HeadTexture.SCRIPT_DIG_UP, (android, b, inv, face) -> {
        Block target = b.getRelative(BlockFace.UP);
        android.moveAndDig(b, inv, face, target);
    }),

    MOVE_AND_DIG_FORWARD(AndroidType.MINER, HeadTexture.SCRIPT_DIG_FORWARD, (android, b, inv, face) -> {
        Block target = b.getRelative(face);
        android.moveAndDig(b, inv, face, target);
    }),

    MOVE_AND_DIG_DOWN(AndroidType.MINER, HeadTexture.SCRIPT_DIG_DOWN, (android, b, inv, face) -> {
        Block target = b.getRelative(BlockFace.DOWN);
        android.moveAndDig(b, inv, face, target);
    }),

    // Action - Sword
    ATTACK_MOBS_ANIMALS(AndroidType.FIGHTER, HeadTexture.SCRIPT_ATTACK, (android, b, inv, face) -> {
        Predicate<LivingEntity> predicate = e -> true;
        android.attack(b, face, predicate);
    }),

    ATTACK_MOBS(AndroidType.FIGHTER, HeadTexture.SCRIPT_ATTACK, (android, b, inv, face) -> {
        Predicate<LivingEntity> predicate = e -> e instanceof Monster;
        android.attack(b, face, predicate);
    }),

    ATTACK_ANIMALS(AndroidType.FIGHTER, HeadTexture.SCRIPT_ATTACK, (android, b, inv, face) -> {
        Predicate<LivingEntity> predicate = e -> e instanceof Animals;
        android.attack(b, face, predicate);
    }),

    ATTACK_ANIMALS_ADULT(AndroidType.FIGHTER, HeadTexture.SCRIPT_ATTACK, (android, b, inv, face) -> {
        Predicate<LivingEntity> predicate = e -> e instanceof Animals && e instanceof Ageable && ((Ageable) e).isAdult();
        android.attack(b, face, predicate);
    }),

    // Action - Axe
    CHOP_TREE(AndroidType.WOODCUTTER, HeadTexture.SCRIPT_CHOP_TREE),

    // Action - Fishing Rod
    CATCH_FISH(AndroidType.FISHERMAN, HeadTexture.SCRIPT_FISH, (android, b, inv, face) -> android.fish(b, inv)),

    // Action - Hoe
    FARM_FORWARD(AndroidType.FARMER, HeadTexture.SCRIPT_FARM_FORWARD, (android, b, inv, face) -> {
        Block target = b.getRelative(face);
        android.farm(inv, target);
    }),

    FARM_DOWN(AndroidType.FARMER, HeadTexture.SCRIPT_FARM_DOWN, (android, b, inv, face) -> {
        Block target = b.getRelative(BlockFace.DOWN);
        android.farm(inv, target);
    }),

    // Action - ExoticGarden
    FARM_EXOTIC_FORWARD(AndroidType.ADVANCED_FARMER, HeadTexture.SCRIPT_FARM_FORWARD, (android, b, inv, face) -> {
        Block target = b.getRelative(face);
        android.exoticFarm(inv, target);
    }),

    FARM_EXOTIC_DOWN(AndroidType.ADVANCED_FARMER, HeadTexture.SCRIPT_FARM_DOWN, (android, b, inv, face) -> {
        Block target = b.getRelative(BlockFace.DOWN);
        android.exoticFarm(inv, target);
    }),

    // Action - Interface
    INTERFACE_ITEMS(AndroidType.NONE, HeadTexture.SCRIPT_PUSH_ITEMS, (android, b, inv, face) -> {
        Block target = b.getRelative(face);
        android.depositItems(inv, target);
    }),

    INTERFACE_FUEL(AndroidType.NONE, HeadTexture.SCRIPT_PULL_FUEL, (android, b, inv, face) -> {
        Block target = b.getRelative(face);
        android.refuel(inv, target);
    });

    private final ItemStack item;
    private final AndroidType type;
    private final AndroidAction method;

    Instruction(AndroidType type, HeadTexture head, AndroidAction method) {
        this.type = type;
        this.item = SlimefunUtils.getCustomHead(head.getTexture());
        this.method = method;
    }

    Instruction(AndroidType type, HeadTexture head) {
        this(type, head, null);
    }

    public ItemStack getItem() {
        return item;
    }

    public AndroidType getRequiredType() {
        return type;
    }

    public void execute(ProgrammableAndroid android, Block b, BlockMenu inventory, BlockFace face) {
        Validate.notNull(method, "Instruction '" + name() + "' must be executed manually!");
        method.perform(android, b, inventory, face);
    }

}
