package io.github.thebusybiscuit.slimefun5.implementation.items.magical.runes;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.ParticleCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VillagerCompat;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.Particle;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.SoundCategory;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedParticle;

/**
 * This {@link SlimefunItem} allows you to reset a {@link Villager} profession.
 * Useful to reset a villager who does not have desirable trades.
 *
 * @author dNiym
 *
 */
public class VillagerRune extends SimpleSlimefunItem<EntityInteractHandler> {

    @ParametersAreNonnullByDefault
    public VillagerRune(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public @Nonnull EntityInteractHandler getItemHandler() {
        return (e, item, offhand) -> {
            if (e.isCancelled() || !Slimefun.getProtectionManager().hasPermission(e.getPlayer(), e.getRightClicked().getLocation(), Interaction.INTERACT_ENTITY)) {
                // They don't have permission to use it in this area
                return;
            }

            if (e.getRightClicked() instanceof Villager) {
                Villager villager = (Villager) e.getRightClicked();                if (villager.getProfession() == VillagerCompat.profession("NONE") || villager.getProfession() == VillagerCompat.profession("NITWIT")) {
                    return;
                }

                if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    ItemUtils.consumeItem(item, false);
                }

                // Reset Villager
                VillagerCompat.setVillagerExperience(villager, 0);
                VillagerCompat.setVillagerLevel(villager, 1);
                Profession none = VillagerCompat.profession("NONE");
                if (none != null) {
                    villager.setProfession(none);
                }
                e.setCancelled(true);

                double offset = ThreadLocalRandom.current().nextDouble(0.5);

                SoundEffect.VILLAGER_RUNE_TRANSFORM_SOUND.playAt(villager.getLocation(), SoundCategory.NEUTRAL);
                ParticleCompat.spawn(villager.getWorld(), Particle.CRIMSON_SPORE, villager.getLocation(), 10, 0, offset / 2, 0, 0);
                ParticleCompat.spawn(villager.getWorld(), VersionedParticle.ENCHANT, villager.getLocation(), 5, 0.04, 1, 0.04);
            }
        };
    }
}


