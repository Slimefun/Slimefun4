package io.github.thebusybiscuit.slimefun4.implementation.items.altar;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.AncientAltarListener;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.AncientAltarTask;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link AncientPedestal} is a part of the {@link AncientAltar}.
 * You can place any {@link ItemStack} onto the {@link AncientPedestal} to provide it to
 * the altar as a crafting ingredient.
 * 
 * @author Redemption198
 * @author TheBusyBiscuit
 * 
 * @see AncientAltar
 * @see AncientAltarListener
 * @see AncientAltarTask
 *
 */
public class AncientPedestal extends SlimefunItem {

    public static final String ITEM_PREFIX = ChatColors.color("&dALTAR &3Probe - &e");

    public AncientPedestal(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        SlimefunItem.registerBlockHandler(getID(), (p, b, tool, reason) -> {
            Optional<Item> entity = getPlacedItem(b);

            if (entity.isPresent()) {
                Item stack = entity.get();

                if (stack.isValid()) {
                    stack.removeMetadata("no_pickup", SlimefunPlugin.instance());
                    b.getWorld().dropItem(b.getLocation(), getOriginalItemStack(stack));
                    stack.remove();
                }
            }

            return true;
        });
    }

    public Optional<Item> getPlacedItem(Block pedestal) {
        Location l = pedestal.getLocation().add(0.5, 1.2, 0.5);

        for (Entity n : l.getWorld().getNearbyEntities(l, 0.5, 0.5, 0.5, this::testItem)) {
            if (n instanceof Item) {
                return Optional.of((Item) n);
            }
        }

        return Optional.empty();
    }

    private boolean testItem(Entity n) {
        if (n instanceof Item && n.isValid()) {
            Item item = (Item) n;
            ItemMeta meta = item.getItemStack().getItemMeta();

            return meta.hasDisplayName() && meta.getDisplayName().startsWith(ITEM_PREFIX);
        } else {
            return false;
        }
    }

    public ItemStack getOriginalItemStack(Item item) {
        ItemStack stack = item.getItemStack().clone();
        String customName = item.getCustomName();

        if (customName.equals(ItemUtils.getItemName(new ItemStack(stack.getType())))) {
            ItemMeta im = stack.getItemMeta();
            im.setDisplayName(null);
            stack.setItemMeta(im);
        } else {
            ItemMeta im = stack.getItemMeta();

            if (!customName.startsWith(String.valueOf(ChatColor.COLOR_CHAR))) {
                customName = ChatColor.WHITE + customName;
            }

            im.setDisplayName(customName);
            stack.setItemMeta(im);
        }

        return stack;
    }

    public void placeItem(Player p, Block b) {
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack displayItem = new CustomItem(hand, ITEM_PREFIX + System.nanoTime());
        displayItem.setAmount(1);

        // Get the display name of the original Item in the Player's hand
        String nametag = ItemUtils.getItemName(hand);

        if (p.getGameMode() != GameMode.CREATIVE) {
            ItemUtils.consumeItem(hand, false);
        }

        Item entity = b.getWorld().dropItem(b.getLocation().add(0.5, 1.2, 0.5), displayItem);
        entity.setVelocity(new Vector(0, 0.1, 0));
        entity.setCustomNameVisible(true);
        entity.setCustomName(nametag);
        SlimefunUtils.markAsNoPickup(entity, "altar_item");
        p.playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3F, 0.3F);
    }

}
