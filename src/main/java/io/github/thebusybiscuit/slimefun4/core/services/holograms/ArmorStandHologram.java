package io.github.thebusybiscuit.slimefun4.core.services.holograms;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ArmorStandUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class ArmorStandHologram extends Hologram<ArmorStand> {
    private ArmorStandHologram(ArmorStand entity) {
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

    static ArmorStandHologram of(Entity entity, BlockPosition position) {
        if (!(entity instanceof ArmorStand armorStand)) {
            return null;
        }

        armorStand.setAI(false);
        armorStand.setInvulnerable(true);
        ArmorStandUtils.setupArmorStand(armorStand);
        PersistentDataAPI.setLong(entity, Slimefun.getHologramsService().getKey(), position.getPosition());
        return new ArmorStandHologram(armorStand);
    }

    static ArmorStandHologram create(Location location, BlockPosition position) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        return of(armorStand, position);
    }
}
