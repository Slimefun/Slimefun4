package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

import javax.annotation.Nonnull;

import com.cryptomorin.xseries.XMaterial;

/**
 * A single entry shown on the wiki home: a guide topic. Its text comes from
 * {@link WikiText#getMechanic(String)} and its related items from {@link WikiText#getTopicItems(String)},
 * both keyed by {@link #getId()}. Core registers a fixed set; one is also auto-generated per installed
 * addon, and addons may register their own via {@link WikiText#registerTopic(WikiTopic)}.
 */
public final class WikiTopic {

    private final String id;
    private final String displayName;
    private final XMaterial icon;
    private final String summary;

    public WikiTopic(@Nonnull String id, @Nonnull String displayName, @Nonnull XMaterial icon, @Nonnull String summary) {
        this.id = id;
        this.displayName = displayName;
        this.icon = icon;
        this.summary = summary;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getDisplayName() {
        return displayName;
    }

    @Nonnull
    public XMaterial getIcon() {
        return icon;
    }

    @Nonnull
    public String getSummary() {
        return summary;
    }
}
