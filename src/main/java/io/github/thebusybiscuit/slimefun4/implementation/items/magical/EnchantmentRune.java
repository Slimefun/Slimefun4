package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
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
    private final HashMap<Material, Enchantment[]> applicableEnchs = new HashMap<>();

    public EnchantmentRune(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        for (Material mat : Material.values()) {
            Set<Enchantment> enchSet = new HashSet<>();
            for (Enchantment ench : Enchantment.values()) {
                if (ench.canEnchantItem(new ItemStack(mat))) enchSet.add(ench);
            }
            applicableEnchs.put(mat, enchSet.toArray(new Enchantment[0]));
        }
    }

    @Override
    public ItemDropHandler getItemHandler() {
        return (e, p, item) -> {
            if (isItem(item.getItemStack())) {

                if (!Slimefun.hasUnlocked(p, SlimefunItems.ENCHANTMENT_RUNE, true)) {
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

            Enchantment[] enchArr = findEnchArr(target.getType());
            if (enchArr.length == 0) return;
            int enchIndex = ThreadLocalRandom.current().nextInt(enchArr.length);
            Enchantment ench = enchArr[enchIndex];

            int level = 1;
            if (ench.getMaxLevel() != 1) level = ThreadLocalRandom.current().nextInt(ench.getMaxLevel() + 1);

            target.addEnchantment(ench, level);

            if (target.getAmount() == 1) {
                e.setCancelled(true);

                // This lightning is just an effect, it deals no damage.
                l.getWorld().strikeLightningEffect(l);

                Slimefun.runSync(() -> {
                    // Being sure entities are still valid and not picked up or whatsoever.
                    if (item.isValid() && entity.isValid() && target.getAmount() == 1) {

                        l.getWorld().createExplosion(l, 0);
                        l.getWorld().playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 0.3F, 1);

                        entity.remove();
                        item.remove();
                        l.getWorld().dropItemNaturally(l, target);

                        SlimefunPlugin.getLocal().sendMessage(p, "messages.enchantment-rune.success", true);
                    }
                }, 10L);
            }
            else {
                SlimefunPlugin.getLocal().sendMessage(p, "messages.enchantment-rune.fail", true);
            }
        }
    }

    private Enchantment[] findEnchArr(Material type) {
        Enchantment[] enchArr = applicableEnchs.get(type);
        if (enchArr == null) enchArr = new Enchantment[0];
        return enchArr;
    }

    private boolean findCompatibleItem(Entity n) {
        if (n instanceof Item) {
            Item item = (Item) n;

            return !isItem(item.getItemStack());
        }

        return false;
    }

}
