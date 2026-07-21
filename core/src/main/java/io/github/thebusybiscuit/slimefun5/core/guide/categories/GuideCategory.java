package io.github.thebusybiscuit.slimefun5.core.guide.categories;

import javax.annotation.Nonnull;

import com.cryptomorin.xseries.XMaterial;

/**
 * One top-level guide category. A definition only: which groups belong to it is derived from each
 * {@link io.github.thebusybiscuit.slimefun5.api.items.ItemGroup}'s declared category id. Icons must exist
 * on MC 1.8 so the guide renders on the legacy floor.
 */
public final class GuideCategory {

    private final String id;
    private final String defaultName;
    private final XMaterial icon;
    private final int order;

    public GuideCategory(@Nonnull String id, @Nonnull String defaultName, @Nonnull XMaterial icon, int order) {
        this.id = id;
        this.defaultName = defaultName;
        this.icon = icon;
        this.order = order;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getDefaultName() {
        return defaultName;
    }

    @Nonnull
    public XMaterial getIcon() {
        return icon;
    }

    public int getOrder() {
        return order;
    }
}
