package io.github.thebusybiscuit.slimefun5.implementation.items.magical;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.EntityCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.HandCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.InventoryCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.SoundCompat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun5.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun5.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun5.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.SimpleSlimefunItem;

/**
 * This {@link SlimefunItem} allows you to convert any {@link ZombieVillager} to
 * their {@link Villager} variant. It is also one of the very few utilisations of {@link EntityInteractHandler}.
 *
 * @author Linox
 *
 * @see EntityInteractHandler
 *
 */
public class MagicalZombiePills extends SimpleSlimefunItem<EntityInteractHandler> implements NotPlaceable {

    @ParametersAreNonnullByDefault
    public MagicalZombiePills(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);

        addItemHandler(onRightClick());
    }

    @Override
    public @Nonnull EntityInteractHandler getItemHandler() {
        return (e, item, offhand) -> {
            Entity entity = e.getRightClicked();

            if (e.isCancelled() || !Slimefun.getProtectionManager().hasPermission(e.getPlayer(), entity.getLocation(), Interaction.INTERACT_ENTITY)) {
                // They don't have permission to use it in this area
                return;
            }

            Player p = e.getPlayer();

            if (entity instanceof ZombieVillager) {
                ZombieVillager zombieVillager = (ZombieVillager) entity;                useItem(p, offhand);
                healZombieVillager(zombieVillager, p);
            } else if (entity instanceof PigZombie) {
                PigZombie pigZombie = (PigZombie) entity;                useItem(p, offhand);
                healZombifiedPiglin(pigZombie);
            }
        };
    }

    /**
     * This method cancels {@link PlayerRightClickEvent} to prevent placing {@link MagicalZombiePills}.
     *
     * @return the {@link ItemUseHandler} of this {@link SlimefunItem}
     */
    public ItemUseHandler onRightClick() {
        return PlayerRightClickEvent::cancel;
    }

    private void useItem(@Nonnull Player p, boolean offHand) {
        if (p.getGameMode() != GameMode.CREATIVE) {
            InventoryCompat.consumeHeldItem(p, offHand ? HandCompat.OFF_HAND : EquipmentSlot.HAND, 1, false);
        }

        // This is supposed to be a vanilla sound. No need for a SoundEffect
        SoundCompat.playFor(p, p.getLocation(), "ENTITY_ZOMBIE_VILLAGER_CONVERTED", null, 1, 1);
    }

    private void healZombieVillager(@Nonnull ZombieVillager zombieVillager, @Nonnull Player p) {
        ReflectionCompat.invoke(zombieVillager, "setConversionTime", 1);
        ReflectionCompat.invoke(zombieVillager, "setConversionPlayer", p);
    }

    private void healZombifiedPiglin(@Nonnull PigZombie zombiePiglin) {
        Location loc = zombiePiglin.getLocation();

        zombiePiglin.remove();
        loc.getWorld().spawnEntity(loc, EntityCompat.entityType("PIGLIN"));
    }
}

