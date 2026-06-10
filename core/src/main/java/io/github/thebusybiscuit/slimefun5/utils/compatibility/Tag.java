package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;

/**
 * Version-independent replacement for {@code org.bukkit.Tag}.
 *
 * <p>
 * The real {@code org.bukkit.Tag} interface is declared as {@code Tag<T extends Keyed>} and only
 * exists from Minecraft 1.13 onwards. On the 1.8.8 API floor that this project compiles against,
 * {@link Material} does not implement {@code Keyed}, so {@code SlimefunTag implements Tag<Material>}
 * cannot be expressed against the real type. This compat interface drops the {@code Keyed} bound and
 * is therefore usable on every supported version.
 *
 * <p>
 * The vanilla tag constants below resolve to the server's real {@code org.bukkit.Tag} values
 * reflectively on modern servers (so modern versions keep the complete, up-to-date tag contents) and
 * fall back to a name-based {@link Material} set on legacy versions where the tag system is absent.
 *
 * @param <T>
 *            The type contained in this {@link Tag}, usually {@link Material}
 */
public interface Tag<T> {

    /**
     * @return The values held by this {@link Tag}.
     */
    Set<T> getValues();

    /**
     * @param value
     *            The value to test
     *
     * @return Whether the given value is contained in this {@link Tag}.
     */
    boolean isTagged(T value);

    /** Registry name mirroring {@code org.bukkit.Tag.REGISTRY_ITEMS}. */
    String REGISTRY_ITEMS = "items";

    /** Registry name mirroring {@code org.bukkit.Tag.REGISTRY_BLOCKS}. */
    String REGISTRY_BLOCKS = "blocks";

    // Vanilla tag constants used across Slimefun. Each is backed by the real server tag on modern
    // versions and a best-effort name list on legacy versions.

    Tag<Material> LOGS = new MinecraftTag(REGISTRY_BLOCKS, "logs", "LOG", "LOG_2", "OAK_LOG", "SPRUCE_LOG", "BIRCH_LOG", "JUNGLE_LOG", "ACACIA_LOG", "DARK_OAK_LOG");
    Tag<Material> PLANKS = new MinecraftTag(REGISTRY_BLOCKS, "planks", "WOOD", "OAK_PLANKS", "SPRUCE_PLANKS", "BIRCH_PLANKS", "JUNGLE_PLANKS", "ACACIA_PLANKS", "DARK_OAK_PLANKS");
    Tag<Material> LEAVES = new MinecraftTag(REGISTRY_BLOCKS, "leaves", "LEAVES", "LEAVES_2", "OAK_LEAVES", "SPRUCE_LEAVES", "BIRCH_LEAVES", "JUNGLE_LEAVES", "ACACIA_LEAVES", "DARK_OAK_LEAVES");
    Tag<Material> SAPLINGS = new MinecraftTag(REGISTRY_BLOCKS, "saplings", "SAPLING", "OAK_SAPLING", "SPRUCE_SAPLING", "BIRCH_SAPLING", "JUNGLE_SAPLING", "ACACIA_SAPLING", "DARK_OAK_SAPLING");
    Tag<Material> SMALL_FLOWERS = new MinecraftTag(REGISTRY_BLOCKS, "small_flowers", "DANDELION", "POPPY", "RED_ROSE", "YELLOW_FLOWER", "BLUE_ORCHID", "ALLIUM", "AZURE_BLUET", "RED_TULIP", "ORANGE_TULIP", "WHITE_TULIP", "PINK_TULIP", "OXEYE_DAISY");
    Tag<Material> CORALS = new MinecraftTag(REGISTRY_BLOCKS, "corals");
    Tag<Material> CORAL_BLOCKS = new MinecraftTag(REGISTRY_BLOCKS, "coral_blocks");
    Tag<Material> WOODEN_SLABS = new MinecraftTag(REGISTRY_BLOCKS, "wooden_slabs", "WOOD_STEP", "WOODEN_SLAB", "OAK_SLAB", "SPRUCE_SLAB", "BIRCH_SLAB", "JUNGLE_SLAB", "ACACIA_SLAB", "DARK_OAK_SLAB");
    Tag<Material> WOODEN_BUTTONS = new MinecraftTag(REGISTRY_BLOCKS, "wooden_buttons", "WOOD_BUTTON", "WOODEN_BUTTON", "OAK_BUTTON", "SPRUCE_BUTTON", "BIRCH_BUTTON", "JUNGLE_BUTTON", "ACACIA_BUTTON", "DARK_OAK_BUTTON");
    Tag<Material> WOODEN_FENCES = new MinecraftTag(REGISTRY_BLOCKS, "wooden_fences", "FENCE", "OAK_FENCE", "SPRUCE_FENCE", "BIRCH_FENCE", "JUNGLE_FENCE", "ACACIA_FENCE", "DARK_OAK_FENCE");
    Tag<Material> WOODEN_TRAPDOORS = new MinecraftTag(REGISTRY_BLOCKS, "wooden_trapdoors", "TRAP_DOOR", "WOODEN_TRAPDOOR", "OAK_TRAPDOOR", "SPRUCE_TRAPDOOR", "BIRCH_TRAPDOOR", "JUNGLE_TRAPDOOR", "ACACIA_TRAPDOOR", "DARK_OAK_TRAPDOOR");
    Tag<Material> WOODEN_PRESSURE_PLATES = new MinecraftTag(REGISTRY_BLOCKS, "wooden_pressure_plates", "WOOD_PLATE", "OAK_PRESSURE_PLATE", "SPRUCE_PRESSURE_PLATE", "BIRCH_PRESSURE_PLATE", "JUNGLE_PRESSURE_PLATE", "ACACIA_PRESSURE_PLATE", "DARK_OAK_PRESSURE_PLATE");
    Tag<Material> WOODEN_DOORS = new MinecraftTag(REGISTRY_BLOCKS, "wooden_doors", "WOOD_DOOR", "WOODEN_DOOR", "OAK_DOOR", "SPRUCE_DOOR", "BIRCH_DOOR", "JUNGLE_DOOR", "ACACIA_DOOR", "DARK_OAK_DOOR");
    Tag<Material> STANDING_SIGNS = new MinecraftTag(REGISTRY_BLOCKS, "standing_signs", "SIGN_POST", "SIGN", "OAK_SIGN", "SPRUCE_SIGN", "BIRCH_SIGN", "JUNGLE_SIGN", "ACACIA_SIGN", "DARK_OAK_SIGN");
    Tag<Material> SIGNS = new MinecraftTag(REGISTRY_BLOCKS, "signs", "SIGN", "WALL_SIGN", "SIGN_POST", "OAK_SIGN", "OAK_WALL_SIGN", "SPRUCE_SIGN", "BIRCH_SIGN", "JUNGLE_SIGN", "ACACIA_SIGN", "DARK_OAK_SIGN");
    Tag<Material> FIRE = new MinecraftTag(REGISTRY_BLOCKS, "fire", "FIRE", "SOUL_FIRE");
    Tag<Material> ITEMS_BOATS = new MinecraftTag(REGISTRY_ITEMS, "boats", "BOAT", "OAK_BOAT", "SPRUCE_BOAT", "BIRCH_BOAT", "JUNGLE_BOAT", "ACACIA_BOAT", "DARK_OAK_BOAT");
    Tag<Material> ITEMS_FISHES = new MinecraftTag(REGISTRY_ITEMS, "fishes", "RAW_FISH", "COOKED_FISH", "COD", "SALMON", "TROPICAL_FISH", "PUFFERFISH", "COOKED_COD", "COOKED_SALMON");

    /**
     * Looks up a vanilla Minecraft {@link Tag} by registry and key, returning a snapshot wrapped as a
     * compat {@link Tag}. Returns {@code null} when the tag does not exist (or on legacy versions
     * without the tag system).
     *
     * @param registry
     *            Either {@link #REGISTRY_ITEMS} or {@link #REGISTRY_BLOCKS}
     * @param key
     *            The key portion of the tag (e.g. {@code "logs"})
     *
     * @return A compat {@link Tag} snapshot, or {@code null} if unavailable
     */
    static Tag<Material> lookup(String registry, String key) {
        Set<Material> values = MinecraftTag.resolveVanilla(registry, key);

        if (values == null) {
            return null;
        }

        return new SnapshotTag(values);
    }
}

/**
 * A {@link Tag} backed by the server's real {@code org.bukkit.Tag} (resolved reflectively and cached
 * lazily), with a name-based {@link Material} fallback for legacy versions.
 */
class MinecraftTag implements Tag<Material> {

    private final String registry;
    private final String key;
    private final String[] fallbackNames;
    private volatile Set<Material> cached;

    MinecraftTag(String registry, String key, String... fallbackNames) {
        this.registry = registry;
        this.key = key;
        this.fallbackNames = fallbackNames;
    }

    @Override
    public Set<Material> getValues() {
        Set<Material> result = cached;

        if (result == null) {
            Set<Material> resolved = resolveVanilla(registry, key);

            if (resolved == null || resolved.isEmpty()) {
                resolved = resolveFallback();
            }

            result = Collections.unmodifiableSet(resolved);
            cached = result;
        }

        return result;
    }

    @Override
    public boolean isTagged(Material value) {
        return getValues().contains(value);
    }

    private Set<Material> resolveFallback() {
        Set<Material> set = new HashSet<>();

        for (String name : fallbackNames) {
            Material material = Material.matchMaterial(name);

            if (material != null) {
                set.add(material);
            }
        }

        return set;
    }

    /**
     * Reflectively resolves the materials of a real {@code org.bukkit.Tag}. Returns {@code null} when
     * the server does not support the tag system (legacy versions) or the tag does not exist.
     */
    static Set<Material> resolveVanilla(String registry, String key) {
        try {
            // Bukkit#getTag expects the REAL org.bukkit.NamespacedKey; build it reflectively from our own
            // key (BukkitKeys) and resolve the real class for the method signature. On legacy versions
            // getTag/NamespacedKey are absent, so this throws and we fall back.
            Class<?> bukkitKeyClass = Class.forName("org.bukkit.NamespacedKey");
            Object namespacedKey = BukkitKeys.toBukkit(NamespacedKey.minecraft(key));
            Method getTag = Bukkit.class.getMethod("getTag", String.class, bukkitKeyClass, Class.class);
            Object tag = getTag.invoke(null, registry, namespacedKey, Material.class);

            if (tag == null) {
                return null;
            }

            Object values = tag.getClass().getMethod("getValues").invoke(tag);

            if (values instanceof Set) {
                Set<Material> result = new HashSet<>();

                for (Object element : (Set<?>) values) {
                    if (element instanceof Material) {
                        result.add((Material) element);
                    }
                }

                return result;
            }

            return null;
        } catch (Throwable e) {
            return null;
        }
    }
}

/**
 * A {@link Tag} wrapping a fixed, pre-resolved {@link Set} of {@link Material Materials}.
 */
class SnapshotTag implements Tag<Material> {

    private final Set<Material> values;

    SnapshotTag(Set<Material> values) {
        this.values = Collections.unmodifiableSet(new HashSet<>(values));
    }

    @Override
    public Set<Material> getValues() {
        return values;
    }

    @Override
    public boolean isTagged(Material value) {
        return values.contains(value);
    }
}
