package io.github.thebusybiscuit.slimefun4.api.recipes.components;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.base.Preconditions;

public class DurabilityComponent extends ItemComponent {

    final int durabilityCost;

    public DurabilityComponent(ItemStack component, int durabilityCost) {
        super(component);
        this.durabilityCost = durabilityCost;
        Preconditions.checkArgument(!component.hasItemMeta() || !(component.getItemMeta() instanceof Damageable),
        "DurabilityComponent needs an item with durability!");
    }

    public int getDurabilityCost() {
        return durabilityCost;
    }

    @Override
    public void consume(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta.isUnbreakable()) {
            return;
        }
        int unbLevel = meta.getEnchantLevel(Enchantment.DURABILITY);
        if (unbLevel > 0 && ThreadLocalRandom.current().nextDouble() > (1 / (1 + unbLevel))) { // %TODO
            return;
        }
        Damageable damageable = (Damageable) meta;
        damageable.setDamage(Math.min(damageable.getDamage() + durabilityCost, item.getType().getMaxDurability()));
        item.setItemMeta(damageable);
    }

    @Override
    public boolean matches(ItemStack givenItem) {
        if (!givenItem.hasItemMeta() || !(givenItem.getItemMeta() instanceof Damageable damageable)) {
            return false;
        }

        ItemMeta meta = getComponent().getItemMeta();

        return super.matches(givenItem) 
            && (meta.isUnbreakable() || damageable.getDamage() <= ((Damageable) meta).getDamage());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        DurabilityComponent other = (DurabilityComponent) obj;
        return other.getComponent().equals(getComponent()) && durabilityCost == other.getDurabilityCost();
    }
    
}
