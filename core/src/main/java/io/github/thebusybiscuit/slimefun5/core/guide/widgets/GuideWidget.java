package io.github.thebusybiscuit.slimefun5.core.guide.widgets;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;

/**
 * A functional guide screen an addon registers so the core guide embeds it as a dedicated entry on both
 * layouts (classic + categorized), instead of the addon injecting its own category/custom layout. Meant
 * for genuinely functional UIs (e.g. an advancement tree), NOT item browsing - items are always listed
 * under the shared categories.
 */
public final class GuideWidget {

    /** Where the widget's button sits on the guide's main menu. */
    public enum Position {
        TOP,
        BOTTOM
    }

    /** Opens the widget's screen for a viewer; runs on the main thread from a guide menu click. */
    @FunctionalInterface
    public interface Opener {
        void open(@Nonnull Player player, @Nonnull PlayerProfile profile);
    }

    private final String id;
    private final String defaultName;
    private final XMaterial icon;
    private final int order;
    private final Position position;
    private final Opener opener;

    /** Convenience constructor placing the widget on the bottom row. */
    public GuideWidget(@Nonnull String id, @Nonnull String defaultName, @Nonnull XMaterial icon, int order, @Nonnull Opener opener) {
        this(id, defaultName, icon, order, Position.BOTTOM, opener);
    }

    public GuideWidget(@Nonnull String id, @Nonnull String defaultName, @Nonnull XMaterial icon, int order, @Nonnull Position position, @Nonnull Opener opener) {
        this.id = id;
        this.defaultName = defaultName;
        this.icon = icon;
        this.order = order;
        this.position = position;
        this.opener = opener;
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

    @Nonnull
    public Position getPosition() {
        return position;
    }

    public void open(@Nonnull Player player, @Nonnull PlayerProfile profile) {
        opener.open(player, profile);
    }
}
