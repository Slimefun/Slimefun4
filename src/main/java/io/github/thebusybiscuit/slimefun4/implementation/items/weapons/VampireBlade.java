package io.github.thebusybiscuit.slimefun4.implementation.items.weapons;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.VampireBladeListener;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * The {@link VampireBlade} is a weapon that applies a Healing effect to any {@link Player}
 * who damages another {@link LivingEntity} with this sword.
 *
 * @author TheBusyBiscuit
 * @see VampireBladeListener
 */
public class VampireBlade extends SlimefunItem {

    private final ItemSetting<Integer> chance = new ItemSetting<>("chance", 45);

    public VampireBlade(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(chance);
    }

    /**
     * This method returns the chance of a {@link VampireBlade} to apply its healing effect.
     *
     * @return The chance for a healing effect
     */
    public int getChance() {
        return chance.getValue();
    }

    public void heal(Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7F, 0.7F);
        p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
    }
}