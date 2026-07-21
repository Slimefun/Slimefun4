package io.github.thebusybiscuit.slimefun5.core.guide.categories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The set of guide categories for one server boot. Core registers the canonical categories at startup
 * (see {@link DefaultGuideCategories}); addons register their own in {@code onEnable}. Not thread-safe:
 * only touched on the main thread (setup + guide opens).
 */
public final class GuideCategoryRegistry {

    private final Map<String, GuideCategory> byId = new LinkedHashMap<>();

    public void register(@Nonnull GuideCategory category) {
        byId.put(category.getId(), category);
    }

    @Nullable
    public GuideCategory getById(@Nullable String id) {
        return id == null ? null : byId.get(id);
    }

    @Nonnull
    public List<GuideCategory> getAll() {
        List<GuideCategory> all = new ArrayList<>(byId.values());
        all.sort(Comparator.comparingInt(GuideCategory::getOrder).thenComparing(GuideCategory::getId));
        return all;
    }
}
