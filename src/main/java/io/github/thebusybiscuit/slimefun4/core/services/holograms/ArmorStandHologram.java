package io.github.thebusybiscuit.slimefun4.core.services.holograms;

import org.bukkit.entity.ArmorStand;

import java.util.Objects;

public class ArmorStandHologram extends Hologram<ArmorStand> {

    public ArmorStandHologram(ArmorStand entity) {
        super(entity.getUniqueId());
    }

    @Override
    public void setText(String text) {
        this.lastAccess = System.currentTimeMillis();
        if (Objects.equals(this.text, text)) {
            return;
        }

        ArmorStand entity = getEntity();
        if (entity != null) {
            entity.setCustomName(text);
            entity.setCustomNameVisible(text != null);
        }
    }

    @Override
    public Class<ArmorStand> getEntityType() {
        return ArmorStand.class;
    }

}
