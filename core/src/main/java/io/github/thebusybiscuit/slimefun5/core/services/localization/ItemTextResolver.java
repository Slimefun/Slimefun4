package io.github.thebusybiscuit.slimefun5.core.services.localization;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.core.services.localization.ItemTranslationService.RenderedDisplay;

/**
 * A per-viewer name/lore provider for items whose display is generated at runtime and therefore
 * cannot live in a static {@code items.yml} entry - tiered variants with per-tier colours (Supreme
 * mob-tech), items named from data not present in their id (GeneticChickengineering chicken icons),
 * or items whose name depends on the individual stack's PDC (SlimeTinker assembled tools).
 *
 * <p>
 * Register via {@link ItemTranslationService#registerResolver(ItemTextResolver)}. The translation
 * service consults resolvers only when no explicit {@code items.yml} entry (or {@code %MOB%} family)
 * covers the id, and before the English-baseline / raw-id fallback. An explicit entry always wins.
 *
 * <p>
 * Called on the Netty write thread, so an implementation MUST be thread-safe and must only read the
 * passed {@link ItemStack} (a deserialized packet copy) and its own immutable data - never live
 * Bukkit entity/world state.
 */
@FunctionalInterface
public interface ItemTextResolver {

    /**
     * @param item
     *            the stack being rendered on the packet path, or {@code null} on the id-only bake
     *            path (where no individual stack exists). Id-keyed resolvers ignore this; resolvers
     *            that depend on per-instance data must return {@code null} when it is {@code null}.
     * @param itemId
     *            the Slimefun item id.
     * @param languageId
     *            the viewer's language id, or {@code null} for the server default.
     * @return the composed display, or {@code null} if this resolver does not handle {@code itemId}.
     */
    @Nullable
    RenderedDisplay resolve(@Nullable ItemStack item, @Nonnull String itemId, @Nullable String languageId);
}
