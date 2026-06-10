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
        Material material = xMaterial.parseMaterial();
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
        Material material = xMaterial.parseMaterial();
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
