package io.github.thebusybiscuit.slimefun4.implementation.items.altar;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.ArmorStand;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSpawnReason;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockDispenseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.AncientAltarListener;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.AncientAltarTask;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.ArmorStandUtils;

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
public class AncientPedestal extends SimpleSlimefunItem<BlockDispenseHandler> {

    public static final String ITEM_PREFIX = ChatColors.color("&dALTAR &3Probe - &e");
    private static NamespacedKey key;

    @ParametersAreNonnullByDefault
    public AncientPedestal(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, Plugin plugin) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);

        key = new NamespacedKey(plugin, "altar_hologram");
        addItemHandler(onBreak());
    }

    private @Nonnull BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                Optional<Item> entity = getPlacedItem(b);

                if (entity.isPresent()) {
                    Item stack = entity.get();

                    if (stack.isValid()) {
                        stack.removeMetadata("no_pickup", Slimefun.instance());
                        b.getWorld().dropItem(b.getLocation(), getOriginalItemStack(stack));
                        stack.remove();
                    }
                }
                killArmorStand(b);
            }
        };
    }

    @Override
    public @Nonnull BlockDispenseHandler getItemHandler() {
        return (e, d, block, machine) -> e.setCancelled(true);
    }

    public @Nonnull Optional<Item> getPlacedItem(@Nonnull Block pedestal) {
        Location l = pedestal.getLocation().add(0.5, 1.2, 0.5);

        for (Entity n : l.getWorld().getNearbyEntities(l, 0.5, 0.5, 0.5, this::testItem)) {
            if (n instanceof Item item) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    private boolean testItem(@Nullable Entity n) {
        if (n instanceof Item item && n.isValid()) {
            ItemMeta meta = item.getItemStack().getItemMeta();

            return meta.hasDisplayName() && meta.getDisplayName().startsWith(ITEM_PREFIX);
        } else {
            return false;
        }
    }

    public @Nonnull ItemStack getOriginalItemStack(@Nonnull Item item) {
        ItemStack stack = item.getItemStack().clone();
        String customName = item.getCustomName();

        if (customName.equals(ItemUtils.getItemName(new ItemStack(stack.getType())))) {
            ItemMeta im = stack.getItemMeta();
            im.setDisplayName(null);
            stack.setItemMeta(im);
        } else {
            ItemMeta im = stack.getItemMeta();
            im.setDisplayName(customName);
            stack.setItemMeta(im);
        }

        return stack;
    }

    public void placeItem(@Nonnull Player p, @Nonnull Block b) {
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack displayItem = new CustomItemStack(hand, ITEM_PREFIX + System.nanoTime());
        displayItem.setAmount(1);

        // Get the display name of the original Item in the Player's hand
        String nametag = ItemUtils.getItemName(hand);

        if (p.getGameMode() != GameMode.CREATIVE) {
            ItemUtils.consumeItem(hand, false);
        }

        Item entity = SlimefunUtils.spawnItem(b.getLocation().add(0.5, 1.2, 0.5), displayItem, ItemSpawnReason.ANCIENT_PEDESTAL_PLACE_ITEM);

        if (entity != null) {
            ArmorStand armorStand = getArmorStand(b);
            entity.setVelocity(new Vector(0, 0.1, 0));
            entity.setUnlimitedLifetime(true);
            entity.setInvulnerable(true);
            entity.setCustomNameVisible(true);
            entity.setCustomName(nametag);
            SlimefunUtils.markAsNoPickup(entity, "altar_item");
            armorStand.addPassenger(entity);
            p.playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3F, 0.3F);
        }
    }

    public static ArmorStand getArmorStand(@Nonnull Block pedestal) {
        BlockPosition blockPosition = new BlockPosition(pedestal.getLocation());
        Location l = new Location(pedestal.getWorld(), pedestal.getX() + 0.5, pedestal.getY() + 1, pedestal.getZ() + 0.5);

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand armorStand && l.distanceSquared(n.getLocation()) < 0.4) {
                PersistentDataContainer container = n.getPersistentDataContainer();

                if (container.has(key, PersistentDataType.LONG) && container.get(key, PersistentDataType.LONG) == blockPosition.getPosition()) {
                    return armorStand;
                }
            }
        }

        return spawnArmorStand(l, blockPosition);
    }

    private static @Nonnull ArmorStand spawnArmorStand(@Nonnull Location l, @Nonnull BlockPosition blockPosition) {
        ArmorStand armorStand = ArmorStandUtils.spawnFakeHologram(l);
        PersistentDataContainer container = armorStand.getPersistentDataContainer();
        container.set(key, PersistentDataType.LONG, blockPosition.getPosition());
        armorStand.setCustomNameVisible(false);

        return armorStand;
    }

    private static void killArmorStand(@Nonnull Block b) {
        ArmorStand pedestal = getArmorStand(b);

        if (pedestal != null) {
            pedestal.remove();
        }
    }
}
