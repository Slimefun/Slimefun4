package io.github.thebusybiscuit.slimefun4.core.services.holograms;

import org.bukkit.entity.TextDisplay;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class TextDisplayHologram extends Hologram<TextDisplay> {

    public TextDisplayHologram(@Nonnull TextDisplay textDisplay) {
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

}
