package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemDropHandler;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SoulboundRune extends SimpleSlimefunItem<ItemDropHandler> {

    public SoulboundRune(Category category, ItemStack item, String id, RecipeType type, ItemStack[] recipe) {
        super(category, item, id, type, recipe);
    }

    @Override
    public ItemDropHandler getItemHandler() {
        return (e, p, i) -> {
            ItemStack item = i.getItemStack();
            // We are using a boolean array because we will change the boolean's value inside a lambda
            // but you can't access non-final variables from outside the lambda inside the lambda.
            final boolean[] boo = {false};

            if (SlimefunManager.isItemSimiliar(item, SlimefunItems.RUNE_SOULBOUND, true)) {
                if (!PlayerProfile.fromUUID(p.getUniqueId()).hasUnlocked(Research.getByID(246))) {
                    Messages.local.sendTranslation(p, "messages.not-researched", true);
                    return true;
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
                    // Being sure the entity is still valid and not picked up or whatsoever.
                    if (!i.isValid()) return;

                    Location l = i.getLocation();
                    Collection<Entity> entites = l.getWorld().getNearbyEntities(l, 1.5, 1.5, 1.5,
                            entity -> entity.getType() == EntityType.DROPPED_ITEM &&
                                    !SlimefunManager.isItemSimiliar(((Item) entity).getItemStack(), SlimefunItems.RUNE_SOULBOUND, true)
                    );

                    ItemStack ench = null;
                    Item ent = null;
                    // Collections do not have a #get method so we need to use a for loop.
                    // We do not use streams for foreach loops as they are more resource consuming.
                    for (Entity entity: entites) {
                        ItemStack dropped = ((Item) entity).getItemStack();
                        if (SlimefunManager.isItemSoulbound(dropped)) {
                            boo[0] = false;
                            return;
                        }
                        ench = ((Item) entity).getItemStack();
                        ent = (Item) entity;
                        break;
                    }

                    if (ench == null || ench.getAmount() == 1) {
                        e.setCancelled(true);

                        Item finalEnt = ent;
                        ItemStack finalEnch = ench;

                        ItemMeta enchMeta = finalEnch.getItemMeta();
                        if (enchMeta == null) enchMeta = Bukkit.getItemFactory().getItemMeta(finalEnch.getType());
                        ItemMeta finalMeta = enchMeta;

                        List<String> lore = finalMeta.getLore();
                        if (lore == null) lore = new ArrayList<>();
                        List<String> finalLore = lore;

                        // This lightning is just an effect, it deals no damage.
                        l.getWorld().strikeLightningEffect(l);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {

                            // Being sure entities are still valid and not picked up or whatsoever.
                            if (i.isValid() && finalEnt.isValid()) {

                                l.getWorld().createExplosion(l, 0.0F);
                                l.getWorld().playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 0.3F, 1F);

                                finalLore.add(ChatColor.GRAY + "Soulbound");

                                finalMeta.setLore(finalLore);
                                finalEnch.setItemMeta(finalMeta);

                                finalEnt.remove();
                                i.remove();
                                l.getWorld().dropItemNaturally(l, finalEnch);

                                Messages.local.sendTranslation(p, "messages.soulbound-rune.success", true);
                                boo[0] = true;
                            }
                        }, 10L);
                    } else {
                        Messages.local.sendTranslation(p, "messages.soulbound-rune.fail", true);
                    }
                }, 20L);
            }
            return boo[0];
        };
    }
}
