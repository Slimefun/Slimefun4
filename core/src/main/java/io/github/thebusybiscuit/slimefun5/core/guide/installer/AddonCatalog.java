package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cryptomorin.xseries.XMaterial;

/**
 * The static source of truth for everything the in-game installer can install: Slimefun core
 * plus every known Slimefun5 addon. Mirrors the repo list in scripts/run.ps1.
 */
public final class AddonCatalog {

    /** Permission required to use the installer. Declared in plugin.yml, default op. */
    public static final String PERMISSION = "slimefun.installer";

    /** The owner/org all entries live under on GitHub. */
    public static final String OWNER = "Slimefun5";

    /** The id of the Slimefun core entry. Treated specially (uses full releases, self-update). */
    public static final String CORE_ID = "slimefun";

    /** A single installable entry. */
    public static final class Entry {

        private final String id;
        private final String repo;
        private final String displayName;
        private final XMaterial icon;
        private final List<String> dependencies;
        private final boolean core;

        Entry(String id, String repo, String displayName, XMaterial icon, List<String> dependencies, boolean core) {
            this.id = id;
            this.repo = repo;
            this.displayName = displayName;
            this.icon = icon;
            this.dependencies = dependencies;
            this.core = core;
        }

        @Nonnull
        public String getId() {
            return id;
        }

        /** The GitHub repository name (without owner). */
        @Nonnull
        public String getRepo() {
            return repo;
        }

        /** "Slimefun5/Repo" — the full GitHub slug. */
        @Nonnull
        public String getSlug() {
            return OWNER + '/' + repo;
        }

        @Nonnull
        public String getDisplayName() {
            return displayName;
        }

        @Nonnull
        public XMaterial getIcon() {
            return icon;
        }

        /** Ids of hard dependencies that must also be installed. */
        @Nonnull
        public List<String> getDependencies() {
            return dependencies;
        }

        public boolean isCore() {
            return core;
        }
    }

    private static final Map<String, Entry> ENTRIES = new LinkedHashMap<>();

    static {
        // Core, pinned first.
        register(new Entry(CORE_ID, "Slimefun5", "Slimefun", XMaterial.BLAZE_POWDER, empty(), true));

        // Library (a dependency of others, also installable on its own).
        register(new Entry("infinitylib", "InfinityLib", "InfinityLib", XMaterial.BOOK, empty(), false));

        // Addons. Dependencies reference ids declared above.
        register(new Entry("infinityexpansion", "InfinityExpansion", "Infinity Expansion", XMaterial.NETHER_STAR, deps("infinitylib"), false));
        register(new Entry("networks", "Networks", "Networks", XMaterial.HOPPER, deps("infinitylib", "infinityexpansion"), false));
        register(new Entry("exoticgarden", "ExoticGarden", "Exotic Garden", XMaterial.MELON, empty(), false));
        register(new Entry("dynatech", "DynaTech", "DynaTech", XMaterial.FURNACE, empty(), false));
        register(new Entry("galactifun", "Galactifun", "Galactifun", XMaterial.FIREWORK_ROCKET, empty(), false));
        register(new Entry("slimetinker", "SlimeTinker", "Slime Tinker", XMaterial.ANVIL, empty(), false));
        register(new Entry("fluffymachines", "FluffyMachines", "Fluffy Machines", XMaterial.PISTON, empty(), false));
        register(new Entry("litexpansion", "LiteXpansion", "LiteXpansion", XMaterial.REDSTONE, empty(), false));
        register(new Entry("sensibletoolbox", "SensibleToolbox", "Sensible Toolbox", XMaterial.IRON_PICKAXE, empty(), false));
        register(new Entry("chestterminal", "ChestTerminal", "Chest Terminal", XMaterial.CHEST, empty(), false));
        register(new Entry("extragear", "ExtraGear", "Extra Gear", XMaterial.DIAMOND_CHESTPLATE, empty(), false));
        register(new Entry("luckyblocks", "LuckyBlocks", "Lucky Blocks", XMaterial.GOLD_BLOCK, empty(), false));
        register(new Entry("missilewarfare", "MissileWarfare", "Missile Warfare", XMaterial.TNT, empty(), false));
        register(new Entry("slimefunadvancements", "SlimefunAdvancements", "Slimefun Advancements", XMaterial.KNOWLEDGE_BOOK, empty(), false));
    }

    private AddonCatalog() {}

    private static void register(Entry entry) {
        ENTRIES.put(entry.getId(), entry);
    }

    private static List<String> empty() {
        return Collections.emptyList();
    }

    private static List<String> deps(String... ids) {
        return Collections.unmodifiableList(Arrays.asList(ids));
    }

    /** All entries in declared order (core first). */
    @Nonnull
    public static List<Entry> getEntries() {
        return new ArrayList<>(ENTRIES.values());
    }

    @Nullable
    public static Entry getById(@Nonnull String id) {
        return ENTRIES.get(id);
    }

    /**
     * Returns the given entry's full hard-dependency closure (transitive), in install order
     * (dependencies before dependents), excluding the entry itself. Used to auto-install deps.
     */
    @Nonnull
    public static List<Entry> resolveDependencies(@Nonnull Entry entry) {
        List<Entry> ordered = new ArrayList<>();
        collectDependencies(entry, ordered);
        return ordered;
    }

    private static void collectDependencies(Entry entry, List<Entry> ordered) {
        for (String depId : entry.getDependencies()) {
            Entry dep = ENTRIES.get(depId);

            if (dep != null && !containsId(ordered, dep.getId())) {
                collectDependencies(dep, ordered);

                if (!containsId(ordered, dep.getId())) {
                    ordered.add(dep);
                }
            }
        }
    }

    private static boolean containsId(List<Entry> list, String id) {
        for (Entry e : list) {
            if (e.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }
}
