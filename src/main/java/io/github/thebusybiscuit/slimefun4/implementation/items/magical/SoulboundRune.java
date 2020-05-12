package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemDropHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This {@link SlimefunItem} allows you to convert any {@link ItemStack} into a
 * {@link SoulboundItem}. It is also one of the very few utilisations of {@link ItemDropHandler}.
 * 
 * @author Linox
 * 
 * @see ItemDropHandler
 * @see Soulbound
 *
 */
public class SoulboundRune extends SimpleSlimefunItem<ItemDropHandler> {

    public SoulboundRune(Category category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
        super(category, item, type, recipe);
    }

    @Override
    public ItemDropHandler getItemHandler() {
        return (e, p, droppedItem) -> {
            ItemStack item = droppedItem.getItemStack();

            if (isItem(item)) {

                if (!Slimefun.hasUnlocked(p, SlimefunItems.RUNE_SOULBOUND, true)) {
                    return true;
                }

                Slimefun.runSync(() -> {
                    // Being sure the entity is still valid and not picked up or whatsoever.
                    if (!droppedItem.isValid()) {
                        return;
                    }

                    Location l = droppedItem.getLocation();
                    Collection<Entity> entites = l.getWorld().getNearbyEntities(l, 1.5, 1.5, 1.5, this::findCompatibleItem);

                    if (entites.isEmpty()) {
                        return;
                    }

                    Entity entity = entites.stream().findFirst().get();
                    ItemStack target = ((Item) entity).getItemStack();
                    Item targetItem = (Item) entity;

                    SlimefunUtils.setSoulbound(target, true);

                    if (target.getAmount() == 1) {
                        e.setCancelled(true);

                        // This lightning is just an effect, it deals no damage.
                        l.getWorld().strikeLightningEffect(l);

                        Slimefun.runSync(() -> {
                            // Being sure entities are still valid and not picked up or whatsoever.
                            if (droppedItem.isValid() && targetItem.isValid() && target.getAmount() == 1) {

                                l.getWorld().createExplosion(l, 0.0F);
                                l.getWorld().playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 0.3F, 1F);

                                ent.remove();
                                i.remove();
                                l.getWorld().dropItemNaturally(l, ench);

                                SlimefunPlugin.getLocal().sendMessage(p, "messages.soulbound-rune.success", true);
                            }
                        }, 10L);
                    }
                    else {
                        SlimefunPlugin.getLocal().sendMessage(p, "messages.soulbound-rune.fail", true);
                    }
                }, 20L);

                return true;
            }
            return false;
        };
    }

    /**
     * This method applies the {@link Soulbound} effect onto a given {@link ItemStack}.
     * 
     * @param item
     *            The {@link ItemStack} to apply this effect to
     */
    public void apply(ItemStack item) {
        // Should rather use PersistentData here
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.add(ChatColor.GRAY + "Soulbound");

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private boolean findCompatibleItem(Entity n) {
        if (n instanceof Item) {
            Item item = (Item) n;

            return !SlimefunUtils.isSoulbound(item.getItemStack()) && !isItem(item.getItemStack());
        }

        return false;
    }

}
