package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemDropHandler;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This {@link SlimefunItem} allows you to enchant any enchantable {@link ItemStack} with a random
 * {@link Enchantment}. It is also one of the very few utilisations of {@link ItemDropHandler}.
 *
 * @author Linox
 *
 * @see ItemDropHandler
 * @see Enchantment
 *
 */
public class EnchantmentRune extends SimpleSlimefunItem<ItemDropHandler> {

    private static final double RANGE = 1.5;
    private final Map<Material, List<Enchantment>> applicableEnchantments = new EnumMap<>(Material.class);

    public EnchantmentRune(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        for (Material mat : Material.values()) {
            List<Enchantment> enchantments = new ArrayList<>();
            for (Enchantment enchantment : Enchantment.values()) {
                if (enchantment == Enchantment.BINDING_CURSE || enchantment == Enchantment.VANISHING_CURSE) continue;
                if (enchantment.canEnchantItem(new ItemStack(mat))) enchantments.add(enchantment);
            }
            applicableEnchantments.put(mat, enchantments);
        }
    }

    @Override
    public ItemDropHandler getItemHandler() {
        return (e, p, item) -> {
            if (isItem(item.getItemStack())) {

                if (!Slimefun.hasUnlocked(p, this, true)) {
                    return true;
                }

                Slimefun.runSync(() -> activate(p, e, item), 20L);

                return true;
            }
            return false;
        };
    }

    private void activate(Player p, PlayerDropItemEvent e, Item item) {
        // Being sure the entity is still valid and not picked up or whatsoever.
        if (!item.isValid()) {
            return;
        }

        Location l = item.getLocation();
        Collection<Entity> entites = l.getWorld().getNearbyEntities(l, RANGE, RANGE, RANGE, this::findCompatibleItem);
        Optional<Entity> optional = entites.stream().findFirst();

        if (optional.isPresent()) {
            Item entity = (Item) optional.get();
            ItemStack target = entity.getItemStack();

            List<Enchantment> enchantmentList = new ArrayList<>(applicableEnchantments.getOrDefault(target.getType(), new ArrayList<>()));
            if (enchantmentList.isEmpty()) {
                SlimefunPlugin.getLocal().sendMessage(p, "messages.enchantment-rune.fail", true);
                return;
            }

            //Removing the enchantments that the item already has from enchantmentSet
            enchantmentList.removeIf(enchantment -> target.getEnchantments().containsKey(enchantment));
            if (enchantmentList.isEmpty()) {
                SlimefunPlugin.getLocal().sendMessage(p, "messages.enchantment-rune.no-enchantment", true);
                return;
            }

            Enchantment enchantment = enchantmentList.get(ThreadLocalRandom.current().nextInt(enchantmentList.size()));
            int level = 1;
            if (enchantment.getMaxLevel() != 1) {
                level = ThreadLocalRandom.current().nextInt(enchantment.getMaxLevel()) + 1;
            }
            target.addEnchantment(enchantment, level);

            if (target.getAmount() == 1) {
                e.setCancelled(true);

                // This lightning is just an effect, it deals no damage.
                l.getWorld().strikeLightningEffect(l);

                Slimefun.runSync(() -> {
                    // Being sure entities are still valid and not picked up or whatsoever.
                    if (item.isValid() && entity.isValid() && target.getAmount() == 1) {

                        l.getWorld().spawnParticle(Particle.CRIT_MAGIC, l, 1);
                        l.getWorld().playSound(l, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1F, 1F);

                        entity.remove();
                        item.remove();
                        l.getWorld().dropItemNaturally(l, target);

                        SlimefunPlugin.getLocal().sendMessage(p, "messages.enchantment-rune.success", true);
                    }
                }, 10L);
            } else {
                SlimefunPlugin.getLocal().sendMessage(p, "messages.enchantment-rune.fail", true);
            }
        }
    }

    private boolean findCompatibleItem(Entity n) {
        if (n instanceof Item) {
            Item item = (Item) n;

            return !isItem(item.getItemStack());
        }

        return false;
    }

}
