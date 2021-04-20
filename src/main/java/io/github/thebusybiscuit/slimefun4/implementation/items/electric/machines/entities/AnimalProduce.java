package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.entities;

import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;

/**
 * An {@link AnimalProduce} can be obtained via the {@link ProduceCollector}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ProduceCollector
 *
 */
public class AnimalProduce extends MachineRecipe implements Predicate<LivingEntity> {

    private final Predicate<LivingEntity> predicate;

    @ParametersAreNonnullByDefault
    public AnimalProduce(ItemStack input, ItemStack result, Predicate<LivingEntity> predicate) {
        super(5, new ItemStack[] { input }, new ItemStack[] { result });
        Validate.notNull(predicate, "The Predicate must not be null");

        this.predicate = predicate;
    }

    @Override
    public boolean test(@Nonnull LivingEntity entity) {
        return predicate.test(entity);
    }

}
