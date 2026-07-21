package io.github.thebusybiscuit.slimefun5.core.guide.widgets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The set of {@link GuideWidget}s for one server boot. Empty by default (core registers none); addons
 * register their functional guide screens in {@code onEnable}. Not thread-safe: touched only on the main
 * thread (setup + guide opens).
 */
public final class GuideWidgetRegistry {

    private final Map<String, GuideWidget> byId = new LinkedHashMap<>();

    public void register(@Nonnull GuideWidget widget) {
        byId.put(widget.getId(), widget);
    }

    @Nullable
    public GuideWidget getById(@Nullable String id) {
        return id == null ? null : byId.get(id);
    }

    @Nonnull
    public List<GuideWidget> getAll() {
        List<GuideWidget> all = new ArrayList<>(byId.values());
        all.sort(Comparator.comparingInt(GuideWidget::getOrder).thenComparing(GuideWidget::getId));
        return all;
    }
}
