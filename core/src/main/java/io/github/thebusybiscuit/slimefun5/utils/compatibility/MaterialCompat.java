package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

/**
 * Java-8 universal port: cross-version {@link Material} helpers for APIs added after 1.8.8.
 *
 * @author Slimefun (Java-8 port)
 */
public final class MaterialCompat {

    /**
     * Legacy-safe placeholder material. Materials introduced after the running server's version
     * resolve to {@code null} via {@link XMaterial#parseMaterial()}; substituting this keeps recipe
     * ingredients and items constructible on legacy instead of crashing in {@code new ItemStack(null)}.
     * {@code PAPER} exists on every supported Minecraft version.
     */
    private static final Material FALLBACK = Material.PAPER;

    // Sensible legacy substitutes for materials that don't exist on older servers, so an icon resolves
    // to something recognisable (e.g. NETHERITE_BLOCK -> DIAMOND_BLOCK on 1.8) instead of the placeholder.
    private static final java.util.Map<XMaterial, XMaterial> LEGACY_SUBSTITUTES = buildLegacySubstitutes();

    private static java.util.Map<XMaterial, XMaterial> buildLegacySubstitutes() {
        java.util.Map<XMaterial, XMaterial> m = new java.util.EnumMap<>(XMaterial.class);
        m.put(XMaterial.NETHERITE_BLOCK, XMaterial.DIAMOND_BLOCK);
        m.put(XMaterial.NETHERITE_INGOT, XMaterial.DIAMOND);
        m.put(XMaterial.NETHERITE_SCRAP, XMaterial.IRON_NUGGET);
        m.put(XMaterial.ANCIENT_DEBRIS, XMaterial.NETHERRACK);
        m.put(XMaterial.BEEHIVE, XMaterial.DISPENSER);
        m.put(XMaterial.BEE_NEST, XMaterial.DISPENSER);
        m.put(XMaterial.HONEY_BLOCK, XMaterial.SLIME_BLOCK);
        m.put(XMaterial.BARREL, XMaterial.CHEST);
        m.put(XMaterial.BLAST_FURNACE, XMaterial.FURNACE);
        m.put(XMaterial.SMOKER, XMaterial.FURNACE);
        m.put(XMaterial.CAMPFIRE, XMaterial.NETHERRACK);
        m.put(XMaterial.SMITHING_TABLE, XMaterial.CRAFTING_TABLE);
        m.put(XMaterial.CARTOGRAPHY_TABLE, XMaterial.CRAFTING_TABLE);
        m.put(XMaterial.FLETCHING_TABLE, XMaterial.CRAFTING_TABLE);
        m.put(XMaterial.LOOM, XMaterial.CRAFTING_TABLE);
        m.put(XMaterial.STONECUTTER, XMaterial.CRAFTING_TABLE);
        m.put(XMaterial.GRINDSTONE, XMaterial.ANVIL);
        m.put(XMaterial.LANTERN, XMaterial.GLOWSTONE);
        m.put(XMaterial.COMPOSTER, XMaterial.CHEST);
        m.put(XMaterial.MAGMA_BLOCK, XMaterial.NETHERRACK);
        m.put(XMaterial.LODESTONE, XMaterial.IRON_BLOCK);
        m.put(XMaterial.BLACKSTONE, XMaterial.COBBLESTONE);
        m.put(XMaterial.OBSERVER, XMaterial.PISTON);
        return m;
    }

    /**
     * A sensible substitute {@link Material} that exists on the running server for an {@link XMaterial}
     * that doesn't (e.g. {@code NETHERITE_BLOCK} -> {@code DIAMOND_BLOCK} on 1.8), or {@code null} if there
     * is no mapping. Used as a smarter fallback than a generic placeholder for guide/category icons.
     */
    @Nullable
    public static Material substitute(@Nonnull XMaterial xMaterial) {
        XMaterial sub = LEGACY_SUBSTITUTES.get(xMaterial);
        return sub != null ? sub.parseMaterial() : null;
    }

    private MaterialCompat() {}

    /**
     * Builds an {@link ItemStack} from an {@link XMaterial}, substituting {@link #FALLBACK} when the
     * material does not exist on the running (legacy) server. Replaces the unsafe
     * {@code new ItemStack(xMaterial.parseMaterial())} idiom, which throws {@link NullPointerException}
     * on legacy servers where {@link XMaterial#parseMaterial()} returns {@code null}.
     *
     * @param xMaterial
     *            The {@link XMaterial} to resolve
     *
     * @return A non-null {@link ItemStack}
     */
    @Nonnull
    public static ItemStack stack(@Nonnull XMaterial xMaterial) {
        // parseItem() carries the legacy data value on 1.8-1.12 (e.g. SKULL_ITEM:3 = player head,
        // SKULL_ITEM:1 = wither skull, wool/dye colors). parseMaterial() drops it, yielding the data-0
        // variant - so a player head would render as a skeleton skull. Fall back only if parseItem fails.
        ItemStack item = xMaterial.parseItem();
        if (item != null) {
            item.setAmount(1);
            return item;
        }
        Material material = xMaterial.parseMaterial();
        if (material == null) {
            material = substitute(xMaterial);
        }
        return new ItemStack(material != null ? material : FALLBACK);
    }

    /**
     * Amount-aware variant of {@link #stack(XMaterial)}.
     *
     * @param xMaterial
     *            The {@link XMaterial} to resolve
     * @param amount
     *            The stack size
     *
     * @return A non-null {@link ItemStack} of the given amount
     */
    @Nonnull
    public static ItemStack stack(@Nonnull XMaterial xMaterial, int amount) {
        // See stack(XMaterial): parseItem() preserves the legacy data value that parseMaterial() drops.
        ItemStack item = xMaterial.parseItem();
        if (item != null) {
            item.setAmount(amount);
            return item;
        }
        Material material = xMaterial.parseMaterial();
        if (material == null) {
            material = substitute(xMaterial);
        }
        return new ItemStack(material != null ? material : FALLBACK, amount);
    }

    /**
     * Version-safe replacement for {@code Material#isAir()} (added in 1.13). Resolved by name so it
     * works on every version: {@code AIR} on all, plus {@code CAVE_AIR}/{@code VOID_AIR} on 1.13+.
     */
    public static boolean isAir(@Nullable Material material) {
        if (material == null) {
            return true;
        }

        switch (material.name()) {
            case "AIR":
            case "CAVE_AIR":
            case "VOID_AIR":
                return true;
            default:
                return false;
        }
    }

    /**
     * Reflective {@code Material#isInteractable()} (added in 1.13). Returns {@code false} on older
     * servers, where the distinction is unavailable.
     */
    public static boolean isInteractable(@Nullable Material material) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(material, "isInteractable"));
    }

    /**
     * Reflective {@code Material#isItem()} (1.13+). Returns {@code true} on older servers, where every
     * material can exist as an item.
     */
    public static boolean isItem(@Nullable Material material) {
        Object result = ReflectionCompat.invoke(material, "isItem");
        return result instanceof Boolean ? (Boolean) result : true;
    }

    /**
     * Reflective {@code Material#isLegacy()} (1.13+). Returns {@code false} on older servers, which have
     * no legacy-material concept.
     */
    public static boolean isLegacy(@Nullable Material material) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(material, "isLegacy"));
    }

    /**
     * Reflective {@code Material#isFuel()} (1.13+). Returns {@code false} on older servers, where the
     * distinction is unavailable.
     */
    public static boolean isFuel(@Nullable Material material) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(material, "isFuel"));
    }

    /**
     * Whether the given {@link Material} can be placed as a block, version-aware.
     * <p>
     * On 1.13+ this is just {@link Material#isBlock()}. On legacy versions (pre-1.13) some placeable
     * blocks only have an <em>item</em> form whose {@code isBlock()} returns {@code false} - most
     * importantly {@code SKULL_ITEM} (player/mob heads), which Slimefun uses for the vast majority of
     * its machine blocks. Those are treated as placeable here so head-textured machines can register
     * and function on 1.8-1.12 instead of being rejected.
     *
     * @param material
     *            The {@link Material} (may be {@code null})
     *
     * @return Whether it can be placed as a block
     */
    public static boolean isPlaceableBlock(@Nullable Material material) {
        if (material == null) {
            return false;
        }

        if (material.isBlock()) {
            return true;
        }

        // Legacy item-form materials that are nonetheless placeable as blocks.
        switch (material.name()) {
            case "SKULL_ITEM": // player/mob heads (the placed form is SKULL)
                return true;
            default:
                return false;
        }
    }
}
