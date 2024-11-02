package io.github.thebusybiscuit.slimefun4.core.services.holograms;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class TextDisplayHologram extends Hologram<TextDisplay> {
    private TextDisplayHologram(@Nonnull TextDisplay textDisplay) {
        super(textDisplay.getUniqueId());
    }

    @Override
    public void setText(@Nullable String text) {
        this.lastAccess = System.currentTimeMillis();
        if (Objects.equals(this.text, text)) {
            return;
        }

        TextDisplay textDisplay = getEntity();
        if (textDisplay != null) {
            textDisplay.setText(text);
        }
    }

    @Override
    public Class<TextDisplay> getEntityType() {
        return TextDisplay.class;
    }

    static TextDisplayHologram of(Entity entity, BlockPosition position) {
        if (!(entity instanceof TextDisplay textDisplay)) {
            return null;
        }

        PersistentDataAPI.setLong(entity, Slimefun.getHologramsService().getKey(), position.getPosition());
        return new TextDisplayHologram(textDisplay);
    }

    static TextDisplayHologram create(Location location, BlockPosition position) {
        TextDisplay textDisplay = location.getWorld().spawn(location, TextDisplay.class);
        return of(textDisplay, position);
    }
}
