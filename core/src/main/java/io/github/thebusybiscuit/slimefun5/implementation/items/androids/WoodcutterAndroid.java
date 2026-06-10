package io.github.thebusybiscuit.slimefun5.implementation.items.androids;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.WorldCompat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.blocks.Vein;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.tags.SlimefunTag;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class WoodcutterAndroid extends ProgrammableAndroid {

    private static final int MAX_REACH = 160;

    /*
     * Maps each (flattened 1.13+) log/wood material NAME to its sapling material NAME and the soil tag it
     * needs to replant on. Resolved by name at runtime (see replant) so that server versions lacking a
     * given material simply skip it — the universal-jar approach to the 1.13 "flattening".
     */
    private static final Map<String, String> LOG_TO_SAPLING = new HashMap<>();
    private static final Map<String, SlimefunTag> LOG_TO_SOIL = new HashMap<>();

    private static void registerTree(String saplingName, SlimefunTag soil, String... logNames) {
        for (String logName : logNames) {
            LOG_TO_SAPLING.put(logName, saplingName);
            LOG_TO_SOIL.put(logName, soil);
        }
    }

    static {
        registerTree("OAK_SAPLING", SlimefunTag.DIRT_VARIANTS, "OAK_LOG", "OAK_WOOD", "STRIPPED_OAK_LOG", "STRIPPED_OAK_WOOD");
        registerTree("BIRCH_SAPLING", SlimefunTag.DIRT_VARIANTS, "BIRCH_LOG", "BIRCH_WOOD", "STRIPPED_BIRCH_LOG", "STRIPPED_BIRCH_WOOD");
        registerTree("JUNGLE_SAPLING", SlimefunTag.DIRT_VARIANTS, "JUNGLE_LOG", "JUNGLE_WOOD", "STRIPPED_JUNGLE_LOG", "STRIPPED_JUNGLE_WOOD");
        registerTree("SPRUCE_SAPLING", SlimefunTag.DIRT_VARIANTS, "SPRUCE_LOG", "SPRUCE_WOOD", "STRIPPED_SPRUCE_LOG", "STRIPPED_SPRUCE_WOOD");
        registerTree("ACACIA_SAPLING", SlimefunTag.DIRT_VARIANTS, "ACACIA_LOG", "ACACIA_WOOD", "STRIPPED_ACACIA_LOG", "STRIPPED_ACACIA_WOOD");
        registerTree("DARK_OAK_SAPLING", SlimefunTag.DIRT_VARIANTS, "DARK_OAK_LOG", "DARK_OAK_WOOD", "STRIPPED_DARK_OAK_LOG", "STRIPPED_DARK_OAK_WOOD");
        registerTree("CRIMSON_FUNGUS", SlimefunTag.FUNGUS_SOIL, "CRIMSON_STEM", "CRIMSON_HYPHAE", "STRIPPED_CRIMSON_STEM", "STRIPPED_CRIMSON_HYPHAE");
        registerTree("WARPED_FUNGUS", SlimefunTag.FUNGUS_SOIL, "WARPED_STEM", "WARPED_HYPHAE", "STRIPPED_WARPED_STEM", "STRIPPED_WARPED_HYPHAE");
        registerTree("MANGROVE_PROPAGULE", SlimefunTag.MANGROVE_BASE_BLOCKS, "MANGROVE_LOG", "STRIPPED_MANGROVE_LOG");
        registerTree("CHERRY_SAPLING", SlimefunTag.DIRT_VARIANTS, "CHERRY_LOG", "STRIPPED_CHERRY_LOG");
        registerTree("PALE_OAK_SAPLING", SlimefunTag.DIRT_VARIANTS, "PALE_OAK_LOG", "PALE_OAK_WOOD", "STRIPPED_PALE_OAK_LOG", "STRIPPED_PALE_OAK_WOOD");
    }

    @ParametersAreNonnullByDefault
    public WoodcutterAndroid(ItemGroup itemGroup, int tier, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, tier, item, recipeType, recipe);
    }

    @Override
    public AndroidType getAndroidType() {
        return AndroidType.WOODCUTTER;
    }

    @Override
    protected boolean chopTree(Block b, BlockMenu menu, BlockFace face) {
        Block target = b.getRelative(face);

        if (!WorldCompat.isInside(target.getWorld(), target.getLocation())) {
            return true;
        }

        if (Tag.LOGS.isTagged(target.getType())) {
            List<Block> list = Vein.find(target, MAX_REACH, block -> Tag.LOGS.isTagged(block.getType()));

            if (!list.isEmpty()) {
                Block log = list.get(list.size() - 1);
                log.getWorld().playEffect(log.getLocation(), Effect.STEP_SOUND, log.getType());

                OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")));
                if (Slimefun.getProtectionManager().hasPermission(owner, log.getLocation(), Interaction.BREAK_BLOCK)) {
                    breakLog(log, b, menu, face);
                }

                return false;
            }
        }

        return true;
    }

    @ParametersAreNonnullByDefault
    private void breakLog(Block log, Block android, BlockMenu menu, BlockFace face) {
        ItemStack drop = new ItemStack(log.getType());

        // We try to push the log into the android's inventory, but nothing happens if it does not fit
        menu.pushItem(drop, getOutputSlots());

        log.getWorld().playEffect(log.getLocation(), Effect.STEP_SOUND, log.getType());

        // If the android just chopped the bottom log, we replant the appropriate sapling
        if (log.getY() == android.getRelative(face).getY()) {
            replant(log);
        } else {
            log.setType(Material.AIR);
        }
    }

    private void replant(@Nonnull Block block) {
        Material logType = block.getType();
        Material saplingType = null;
        Predicate<Material> soilRequirement = null;

        // Version-safe lookup: resolve the sapling Material by name so server versions that lack a given
        // flattened (1.13+) material simply skip it instead of failing to load. Replaces the previous
        // switch-on-Material, whose case labels referenced enum constants absent on the Java-8/1.8 floor.
        String saplingName = LOG_TO_SAPLING.get(logType.name());
        SlimefunTag soilTag = LOG_TO_SOIL.get(logType.name());

        if (saplingName != null && soilTag != null) {
            saplingType = Material.getMaterial(saplingName);
            soilRequirement = soilTag::isTagged;
        }

        if (saplingType != null && soilRequirement != null) {
            if (soilRequirement.test(block.getRelative(BlockFace.DOWN).getType())) {
                // Replant the block
                block.setType(saplingType);
            } else {
                // Simply drop the sapling if the soil does not fit
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(saplingType));
                block.setType(Material.AIR);
            }
        }
    }

}

