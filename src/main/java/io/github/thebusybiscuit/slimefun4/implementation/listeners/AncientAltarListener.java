package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AltarRecipe;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientAltar;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientPedestal;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.AncientAltarTask;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This {@link Listener} is responsible for providing the core mechanics of the {@link AncientAltar}
 * and the {@link AncientPedestal}, it also handles the crafting of items using the Altar.
 * 
 * @author Redemption198
 * @author TheBusyBiscuit
 *
 */
public class AncientAltarListener implements Listener {

    public static final String ITEM_PREFIX = ChatColors.color("&dALTAR &3Probe - &e");

    private AncientAltar altar;

    private final Set<AltarRecipe> altarRecipes = new HashSet<>();
    private final Set<Location> altarsInUse = new HashSet<>();

    private final List<Block> altars = new ArrayList<>();
    private final Set<UUID> removedItems = new HashSet<>();

    public void register(SlimefunPlugin plugin, AncientAltar altar) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.altar = altar;
    }

    /**
     * This returns all {@link AncientAltar Altars} that are currently in use.
     * 
     * @return A {@link Set} of every {@link AncientAltar} currently in use
     */
    public Set<Location> getAltarsInUse() {
        return altarsInUse;
    }

    public List<Block> getAltars() {
        return altars;
    }

    public Set<AltarRecipe> getRecipes() {
        return altarRecipes;
    }

    @EventHandler
    public void onInteract(PlayerRightClickEvent e) {
        if (altar == null || altar.isDisabled() || e.useBlock() == Result.DENY) {
            return;
        }

        Optional<Block> blockOptional = e.getClickedBlock();
        if (!blockOptional.isPresent()) {
            return;
        }

        Block b = blockOptional.get();
        if (b.getType() != Material.ENCHANTING_TABLE && b.getType() != Material.DISPENSER) {
            return;
        }

        Optional<SlimefunItem> slimefunBlock = e.getSlimefunBlock();
        if (!slimefunBlock.isPresent()) {
            return;
        }

        String id = slimefunBlock.get().getID();

        if (id.equals(SlimefunItems.ANCIENT_PEDESTAL.getItemId())) {
            e.cancel();
            usePedestal(b, e.getPlayer());
        }
        else if (id.equals(SlimefunItems.ANCIENT_ALTAR.getItemId())) {
            if (!Slimefun.hasUnlocked(e.getPlayer(), SlimefunItems.ANCIENT_ALTAR.getItem(), true) || altarsInUse.contains(b.getLocation())) {
                e.cancel();
                return;
            }

            // Make altarinuse simply because that was the last block clicked.
            altarsInUse.add(b.getLocation());
            e.cancel();

            useAltar(b, e.getPlayer());
        }
    }

    private void usePedestal(Block pedestal, Player p) {
        if (altarsInUse.contains(pedestal.getLocation())) {
            return;
        }

        if (!SlimefunPlugin.getProtectionManager().hasPermission(p, pedestal, ProtectableAction.ACCESS_INVENTORIES)) {
            SlimefunPlugin.getLocalization().sendMessage(p, "inventory.no-access", true);
            return;
        }

        // getting the currently placed item
        Item stack = findItem(pedestal);

        if (stack == null) {
            // Check if the Item in hand is valid
            if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                // Check for pedestal obstructions
                if (pedestal.getRelative(0, 1, 0).getType() != Material.AIR) {
                    SlimefunPlugin.getLocalization().sendMessage(p, "machines.ANCIENT_PEDESTAL.obstructed", true);
                    return;
                }

                // place the item onto the pedestal
                insertItem(p, pedestal);
            }
        }
        else if (!removedItems.contains(stack.getUniqueId())) {
            UUID uuid = stack.getUniqueId();
            removedItems.add(uuid);

            Slimefun.runSync(() -> removedItems.remove(uuid), 30L);

            stack.remove();
            p.getInventory().addItem(fixItemStack(stack.getItemStack(), stack.getCustomName()));
            p.playSound(pedestal.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
        }
    }

    private void useAltar(Block altar, Player p) {
        if (!SlimefunPlugin.getProtectionManager().hasPermission(p, altar, ProtectableAction.ACCESS_INVENTORIES)) {
            SlimefunPlugin.getLocalization().sendMessage(p, "inventory.no-access", true);
            return;
        }

        ItemStack catalyst = new CustomItem(p.getInventory().getItemInMainHand(), 1);
        List<Block> pedestals = getPedestals(altar);

        if (!altars.contains(altar)) {
            altars.add(altar);

            if (pedestals.size() == 8) {
                pedestals.forEach(block -> altarsInUse.add(block.getLocation()));

                if (catalyst.getType() != Material.AIR) {
                    startRitual(p, altar, pedestals, catalyst);
                }
                else {
                    altars.remove(altar);
                    SlimefunPlugin.getLocalization().sendMessage(p, "machines.ANCIENT_ALTAR.unknown-catalyst", true);

                    for (Block block : pedestals) {
                        altarsInUse.remove(block.getLocation());
                    }

                    // Unknown catalyst, no longer in use
                    altarsInUse.remove(altar.getLocation());
                }
            }
            else {
                altars.remove(altar);
                SlimefunPlugin.getLocalization().sendMessage(p, "machines.ANCIENT_ALTAR.not-enough-pedestals", true, msg -> msg.replace("%pedestals%", String.valueOf(pedestals.size())));

                // Not a valid altar so remove from inuse
                altarsInUse.remove(altar.getLocation());
            }
        }
    }

    private void startRitual(Player p, Block b, List<Block> pedestals, ItemStack catalyst) {
        List<ItemStack> input = new ArrayList<>();

        for (Block pedestal : pedestals) {
            Item stack = findItem(pedestal);

            if (stack != null) {
                input.add(fixItemStack(stack.getItemStack(), stack.getCustomName()));
            }
        }

        Optional<ItemStack> result = getRecipeOutput(catalyst, input);
        if (result.isPresent()) {
            if (Slimefun.hasUnlocked(p, result.get(), true)) {
                List<ItemStack> consumed = new ArrayList<>();
                consumed.add(catalyst);

                if (p.getGameMode() != GameMode.CREATIVE) {
                    ItemUtils.consumeItem(p.getInventory().getItemInMainHand(), false);
                }

                b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1);
                Slimefun.runSync(new AncientAltarTask(b, altar.getSpeed(), result.get(), pedestals, consumed, p), 10L);
            }
            else {
                altars.remove(b);

                for (Block block : pedestals) {
                    altarsInUse.remove(block.getLocation());
                }

                // Item not unlocked, no longer in use.
                altarsInUse.remove(b.getLocation());
            }
        }
        else {
            altars.remove(b);
            SlimefunPlugin.getLocalization().sendMessage(p, "machines.ANCIENT_ALTAR.unknown-recipe", true);

            for (Block block : pedestals) {
                altarsInUse.remove(block.getLocation());
            }

            // Bad recipe, no longer in use.
            altarsInUse.remove(b.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (altar == null || altar.isDisabled()) {
            return;
        }

        Block pedestal = e.getBlockPlaced().getRelative(BlockFace.DOWN);

        if (pedestal.getType() == Material.DISPENSER) {
            String id = BlockStorage.checkID(pedestal);

            if (id != null && id.equals(SlimefunItems.ANCIENT_PEDESTAL.getItemId())) {
                SlimefunPlugin.getLocalization().sendMessage(e.getPlayer(), "messages.cannot-place", true);
                e.setCancelled(true);
            }
        }
    }

    public ItemStack fixItemStack(ItemStack itemStack, String customName) {
        ItemStack stack = itemStack.clone();

        if (customName.equals(ItemUtils.getItemName(new ItemStack(itemStack.getType())))) {
            ItemMeta im = stack.getItemMeta();
            im.setDisplayName(null);
            stack.setItemMeta(im);
        }
        else {
            ItemMeta im = stack.getItemMeta();
            if (!customName.startsWith(String.valueOf(ChatColor.COLOR_CHAR))) customName = ChatColor.RESET + customName;
            im.setDisplayName(customName);
            stack.setItemMeta(im);
        }
        return stack;
    }

    public Item findItem(Block b) {
        for (Entity n : b.getChunk().getEntities()) {
            if (n instanceof Item && b.getLocation().add(0.5, 1.2, 0.5).distanceSquared(n.getLocation()) < 0.5D && n.getCustomName() != null) {
                return (Item) n;
            }
        }
        return null;
    }

    private void insertItem(Player p, Block b) {
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack stack = new CustomItem(hand, 1);

        if (p.getGameMode() != GameMode.CREATIVE) {
            ItemUtils.consumeItem(hand, false);
        }

        String nametag = ItemUtils.getItemName(stack);
        Item entity = b.getWorld().dropItem(b.getLocation().add(0.5, 1.2, 0.5), new CustomItem(stack, ITEM_PREFIX + System.nanoTime()));
        entity.setVelocity(new Vector(0, 0.1, 0));
        SlimefunUtils.markAsNoPickup(entity, "altar_item");
        entity.setCustomNameVisible(true);
        entity.setCustomName(nametag);
        p.playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3F, 0.3F);
    }

    private List<Block> getPedestals(Block altar) {
        List<Block> list = new ArrayList<>();
        String id = SlimefunItems.ANCIENT_PEDESTAL.getItemId();

        if (BlockStorage.check(altar.getRelative(2, 0, -2), id)) {
            list.add(altar.getRelative(2, 0, -2));
        }
        if (BlockStorage.check(altar.getRelative(3, 0, 0), id)) {
            list.add(altar.getRelative(3, 0, 0));
        }
        if (BlockStorage.check(altar.getRelative(2, 0, 2), id)) {
            list.add(altar.getRelative(2, 0, 2));
        }
        if (BlockStorage.check(altar.getRelative(0, 0, 3), id)) {
            list.add(altar.getRelative(0, 0, 3));
        }
        if (BlockStorage.check(altar.getRelative(-2, 0, 2), id)) {
            list.add(altar.getRelative(-2, 0, 2));
        }
        if (BlockStorage.check(altar.getRelative(-3, 0, 0), id)) {
            list.add(altar.getRelative(-3, 0, 0));
        }
        if (BlockStorage.check(altar.getRelative(-2, 0, -2), id)) {
            list.add(altar.getRelative(-2, 0, -2));
        }
        if (BlockStorage.check(altar.getRelative(0, 0, -3), id)) {
            list.add(altar.getRelative(0, 0, -3));
        }

        return list;
    }

    public Optional<ItemStack> getRecipeOutput(ItemStack catalyst, List<ItemStack> input) {
        if (input.size() != 8) {
            return Optional.empty();
        }

        ItemStackWrapper wrapper = new ItemStackWrapper(catalyst);
        List<ItemStackWrapper> items = ItemStackWrapper.wrapList(input);

        if (SlimefunUtils.isItemSimilar(wrapper, SlimefunItems.BROKEN_SPAWNER, false)) {
            if (!checkRecipe(SlimefunItems.BROKEN_SPAWNER, items).isPresent()) {
                return Optional.empty();
            }

            ItemStack spawner = SlimefunItems.REPAIRED_SPAWNER.clone();
            ItemMeta im = spawner.getItemMeta();
            im.setLore(Arrays.asList(wrapper.getItemMeta().getLore().get(0)));
            spawner.setItemMeta(im);
            return Optional.of(spawner);
        }

        return checkRecipe(wrapper, items);
    }

    private Optional<ItemStack> checkRecipe(ItemStack catalyst, List<ItemStackWrapper> items) {
        for (AltarRecipe recipe : altarRecipes) {
            if (SlimefunUtils.isItemSimilar(catalyst, recipe.getCatalyst(), true)) {
                Optional<ItemStack> optional = checkPedestals(items, recipe);

                if (optional.isPresent()) {
                    return optional;
                }
            }
        }

        return Optional.empty();
    }

    private Optional<ItemStack> checkPedestals(List<ItemStackWrapper> items, AltarRecipe recipe) {
        for (int i = 0; i < 8; i++) {
            if (SlimefunUtils.isItemSimilar(items.get(i), recipe.getInput().get(0), true)) {
                for (int j = 1; j < 8; j++) {
                    if (!SlimefunUtils.isItemSimilar(items.get((i + j) % items.size()), recipe.getInput().get(j), true)) {
                        break;
                    }
                    else if (j == 7) {
                        return Optional.of(recipe.getOutput());
                    }
                }
            }
        }

        return Optional.empty();
    }

}
