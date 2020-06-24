package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import java.util.Collection;
import java.util.Optional;
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

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
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

    private static final Enchantment[] helmEnch = {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.OXYGEN, Enchantment.WATER_WORKER, Enchantment.THORNS, Enchantment.PROTECTION_PROJECTILE, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_EXPLOSIONS};
    private static final Enchantment[] chestLeggingsEnch = {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.THORNS, Enchantment.PROTECTION_PROJECTILE, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_EXPLOSIONS};
    private static final Enchantment[] bootsEnch = {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.FROST_WALKER, Enchantment.PROTECTION_FALL, Enchantment.DEPTH_STRIDER, Enchantment.THORNS, Enchantment.PROTECTION_PROJECTILE, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_EXPLOSIONS};

    private static final Enchantment[] swordEnch = {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.FIRE_ASPECT, Enchantment.LOOT_BONUS_MOBS, Enchantment.KNOCKBACK, Enchantment.SWEEPING_EDGE, Enchantment.DAMAGE_ALL, Enchantment.DAMAGE_ARTHROPODS, Enchantment.DAMAGE_UNDEAD};
    private static final Enchantment[] tridentEnch = {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.IMPALING, Enchantment.CHANNELING, Enchantment.LOYALTY, Enchantment.RIPTIDE};
    private static Enchantment[] crossbowEnch;
    private static final Enchantment[] bowEnch = {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.ARROW_DAMAGE, Enchantment.ARROW_FIRE, Enchantment.ARROW_INFINITE, Enchantment.ARROW_KNOCKBACK};

    private static final Enchantment[] axeEnch = {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.DIG_SPEED, Enchantment.LOOT_BONUS_BLOCKS, Enchantment.SILK_TOUCH, Enchantment.DAMAGE_ALL, Enchantment.DAMAGE_UNDEAD, Enchantment.DAMAGE_ARTHROPODS};
    private static final Enchantment[] toolEnch = {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.DIG_SPEED, Enchantment.LOOT_BONUS_BLOCKS, Enchantment.SILK_TOUCH};
    private static final Enchantment[] shearEnch = {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.DIG_SPEED};
    private static final Enchantment[] fishRodEnch = {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.LURE, Enchantment.LUCK};
    private static final Enchantment[] otherItemsEnch = {Enchantment.MENDING, Enchantment.DURABILITY};

    public EnchantmentRune(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            crossbowEnch = new Enchantment[] {Enchantment.MENDING, Enchantment.DURABILITY, Enchantment.MULTISHOT, Enchantment.QUICK_CHARGE, Enchantment.PIERCING};
        } else {
            crossbowEnch = null;
        }
        super.preRegister();
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
        Enchantment[] enchArr = null;

        if (type.name().endsWith("_HELMET")) enchArr = helmEnch;
        if (type.name().endsWith("_CHESTPLATE") || type.name().endsWith("_LEGGINGS")) enchArr = chestLeggingsEnch;
        if (type.name().endsWith("_BOOTS")) enchArr = bootsEnch;
        if (type.name().endsWith("_SWORD")) enchArr = swordEnch;
        if (type.name().endsWith("_AXE")) enchArr = axeEnch;
        if (type.name().endsWith("_PICKAXE") || type.name().endsWith("_SHOVEL") || type.name().endsWith("_HOE")) enchArr = toolEnch;

        if (type == Material.TRIDENT) enchArr = tridentEnch;
        if (type == Material.BOW) enchArr = bowEnch;
        if (type == Material.SHEARS) enchArr = shearEnch;
        if (type == Material.FISHING_ROD) enchArr = fishRodEnch;
        if (crossbowEnch != null && type.name().equals("CROSSBOW")) enchArr = crossbowEnch;

        if (type == Material.SHIELD || type == Material.ELYTRA || type == Material.CARROT_ON_A_STICK || type == Material.FLINT_AND_STEEL) enchArr = otherItemsEnch;

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
