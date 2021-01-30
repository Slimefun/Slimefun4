package io.github.thebusybiscuit.slimefun4.core.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.cscorelib2.collections.Pair;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class DiffMap {

    private final Map<String, Pair<String, String>> map = new HashMap<>();

    @ParametersAreNonnullByDefault
    public void addItemSettingsChange(ItemSetting<?> setting, Object prev, Object value) {
        // TODO Implement this
    }

    @ParametersAreNonnullByDefault
    public void addResearchCostChange(Research research, int oldCost, int newCost) {
        // TODO Implement this
    }

    public void addItemPermissionChange(@Nonnull SlimefunItem item, @Nullable String previousPermission, @Nullable String newPermission) {
        // TODO Implement this

    }

}
