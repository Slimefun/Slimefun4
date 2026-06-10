package io.github.thebusybiscuit.slimefun5.implementation.items.magical.runes;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.EntityCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedEnchantment;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ParticleCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.handlers.ItemDropHandler;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedParticle;

/**
 * This {@link SlimefunItem} allows you to enchant any enchantable {@link ItemStack} with a random
 * {@link Enchantment}. It is also one of the very few utilisations of {@link ItemDropHandler}.
 *
 * @author Linox
 *
 * @see ItemDropHandler
 *
 */
public class EnchantmentRune extends SimpleSlimefunItem<ItemDropHandler> {

    private static final double RANGE = 1.5;
    private final Map<Material, List<Enchantment>> applicableEnchantments = new HashMap<>();

    @ParametersAreNonnullByDefault
    public EnchantmentRune(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        for (Material mat : Material.values()) {
            if (MaterialCompat.isLegacy(mat) || !MaterialCompat.isItem(mat)) continue;

            List<Enchantment> enchantments = new ArrayList<>();

            for (Enchantment enchantment : Enchantment.values()) {
                if (enchantment.equals(VersionedEnchantment.BINDING_CURSE) || enchantment.equals(VersionedEnchantment.VANISHING_CURSE)) {
                    continue;
                }

                try {
                    if (enchantment.canEnchantItem(new ItemStack(mat))) {
                        enchantments.add(enchantment);
                    }
                } catch (Exception | LinkageError ex) {
                    // Legacy NMS (1.8) throws for some material/enchantment combinations - skip them.
                }
            }

            applicableEnchantments.put(mat, enchantments);
        }
    }

    @Override
    public ItemDropHandler getItemHandler() {
        return (e, p, item) -> {
            if (isItem(item.getItemStack())) {
                if (canUse(p, true)) {
                    Slimefun.runSync(() -> {
                        try {
                            addRandomEnchantment(p, item);
                        } catch (Exception x) {
                            error("An Exception occurred while trying to apply an Enchantment Rune", x);
                        }
                    }, 20L);
                }

                return true;
            }

            return false;
        };
    }

    private void addRandomEnchantment(@Nonnull Player p, @Nonnull Item rune) {
        // Being sure the entity is still valid and not picked up or whatsoever.
        if (!rune.isValid()) {
            return;
        }

        Location l = rune.getLocation();
        Collection<Entity> entites = EntityCompat.getNearbyEntities(l.getWorld(), l, RANGE, RANGE, RANGE, this::findCompatibleItem);
        Optional<Entity> optional = entites.stream().findFirst();

        if (optional.isPresent()) {
            Item item = (Item) optional.get();
            ItemStack itemStack = item.getItemStack();

            List<Enchantment> potentialEnchantments = applicableEnchantments.get(itemStack.getType());

            if (potentialEnchantments == null) {
                Slimefun.getLocalization().sendMessage(p, "messages.enchantment-rune.fail", true);
                return;
            } else {
                potentialEnchantments = new ArrayList<>(potentialEnchantments);
            }

            SlimefunItem slimefunItem = SlimefunItem.getByItem(itemStack);

            // Fixes #2878 - Respect enchatability config setting.
            if (slimefunItem != null && !slimefunItem.isEnchantable()) {
                Slimefun.getLocalization().sendMessage(p, "messages.enchantment-rune.fail", true);
                return;
            }

            /*
             * Removing the enchantments that the item already has from enchantmentSet.
             * This also removes any conflicting enchantments
             */
            removeIllegalEnchantments(itemStack, potentialEnchantments);

            if (potentialEnchantments.isEmpty()) {
                Slimefun.getLocalization().sendMessage(p, "messages.enchantment-rune.no-enchantment", true);
                return;
            }

            Enchantment enchantment = potentialEnchantments.get(ThreadLocalRandom.current().nextInt(potentialEnchantments.size()));
            int level = getRandomlevel(enchantment);

            if (itemStack.getAmount() == 1) {
                // This lightning is just an effect, it deals no damage.
                l.getWorld().strikeLightningEffect(l);

                Slimefun.runSync(() -> {
                    // Being sure entities are still valid and not picked up or whatsoever.
                    if (rune.isValid() && item.isValid() && itemStack.getAmount() == 1) {

                        ParticleCompat.spawn(l.getWorld(), VersionedParticle.ENCHANTED_HIT, l, 1);
                        SoundEffect.ENCHANTMENT_RUNE_ADD_ENCHANT_SOUND.playAt(l, SoundCategory.PLAYERS);

                        item.remove();
                        rune.remove();

                        itemStack.addEnchantment(enchantment, level);
                        l.getWorld().dropItemNaturally(l, itemStack);

                        Slimefun.getLocalization().sendMessage(p, "messages.enchantment-rune.success", true);
                    }
                }, 10L);
            } else {
                Slimefun.getLocalization().sendMessage(p, "messages.enchantment-rune.fail", true);
            }
        }
    }

    private int getRandomlevel(@Nonnull Enchantment enchantment) {
        int level = 1;

        if (enchantment.getMaxLevel() != 1) {
            level = ThreadLocalRandom.current().nextInt(enchantment.getMaxLevel()) + 1;
        }

        return level;
    }

    private void removeIllegalEnchantments(@Nonnull ItemStack target, @Nonnull List<Enchantment> potentialEnchantments) {
        for (Enchantment enchantment : target.getEnchantments().keySet()) {
            Iterator<Enchantment> iterator = potentialEnchantments.iterator();

            while (iterator.hasNext()) {
                Enchantment possibleEnchantment = iterator.next();

                // Duplicate or conflict
                if (possibleEnchantment.equals(enchantment) || possibleEnchantment.conflictsWith(enchantment)) {
                    iterator.remove();
                }
            }
        }
    }

    private boolean findCompatibleItem(@Nonnull Entity n) {
        if (n instanceof Item) {
            Item item = (Item) n;            return !isItem(item.getItemStack());
        }

        return false;
    }

}


