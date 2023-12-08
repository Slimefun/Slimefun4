package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.events.TalismanActivateEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class Talisman extends SlimefunItem {

    protected static final ItemGroup TALISMANS_ITEMGROUP = new ItemGroup(new NamespacedKey(Slimefun.instance(), "talismans"), new CustomItemStack(SlimefunItems.COMMON_TALISMAN, "&7Talismans - &aTier I"), 2);
    private static final String WIKI_PAGE = "Talismans";

    private final SlimefunItemStack enderTalisman;

    protected final String suffix;
    protected final boolean consumable;
    protected final boolean cancel;
    protected final PotionEffect[] effects;
    protected final int chance;

    @ParametersAreNonnullByDefault
    public Talisman(SlimefunItemStack item, ItemStack[] recipe, boolean consumable, boolean cancelEvent, @Nullable String messageSuffix, PotionEffect... effects) {
        this(item, recipe, consumable, cancelEvent, messageSuffix, 100, effects);
    }

    @ParametersAreNonnullByDefault
    public Talisman(SlimefunItemStack item, ItemStack[] recipe, @Nullable String messageSuffix, int chance, PotionEffect... effects) {
        this(item, recipe, true, true, messageSuffix, chance, effects);
    }

    @ParametersAreNonnullByDefault
    public Talisman(SlimefunItemStack item, ItemStack[] recipe, boolean consumable, boolean cancelEvent, @Nullable String messageSuffix, int chance, PotionEffect... effects) {
        this(TALISMANS_ITEMGROUP, item, recipe, consumable, cancelEvent, messageSuffix, chance, effects);
    }

    @ParametersAreNonnullByDefault
    protected Talisman(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, boolean consumable, boolean cancelEvent, @Nullable String messageSuffix, int chance, PotionEffect... effects) {
        super(itemGroup, item, RecipeType.MAGIC_WORKBENCH, recipe, new CustomItemStack(item, consumable ? 4 : 1));

        this.consumable = consumable;
        this.cancel = cancelEvent;
        this.suffix = messageSuffix;
        this.effects = effects;
        this.chance = chance;
        addOfficialWikipage(WIKI_PAGE);

        if (!(this instanceof EnderTalisman)) {
            String name = "&5Ender " + ChatColor.stripColor(getItem().getItemMeta().getDisplayName());
            List<String> lore = new ArrayList<>();
            lore.add("&7&oEnder Infused");
            lore.add("");

            for (String line : getItem().getItemMeta().getLore()) {
                lore.add(line);
            }

            enderTalisman = new SlimefunItemStack("ENDER_" + getId(), getItem().getType(), name, lore.toArray(new String[0]));
        } else {
            enderTalisman = null;
        }
    }

    /**
     * This returns whether the {@link Talisman} will be consumed upon use.
     * 
     * @return Whether this {@link Talisman} is consumed on use.
     */
    public boolean isConsumable() {
        return consumable;
    }

    /**
     * This returns the chance of this {@link Talisman} activating.
     * The chance will be between 1 and 100.
     * 
     * @return The chance of this {@link Talisman} activating.
     */
    public int getChance() {
        return chance;
    }

    @Nonnull
    public PotionEffect[] getEffects() {
        return effects;
    }

    protected boolean isEventCancelled() {
        return cancel;
    }

    @Nullable
    private SlimefunItemStack getEnderVariant() {
        return enderTalisman;
    }

    @Override
    public void postRegister() {
        EnderTalisman talisman = new EnderTalisman(this, getEnderVariant());
        talisman.register(getAddon());
    }

    @Override
    public void load() {
        super.load();
        loadEnderTalisman();
    }

    void loadEnderTalisman() {
        EnderTalisman talisman = (EnderTalisman) SlimefunItem.getByItem(getEnderVariant());
        Optional<Research> research = Research.getResearch(new NamespacedKey(Slimefun.instance(), "ender_talismans"));

        if (talisman != null && research.isPresent()) {
            talisman.setResearch(research.get());
        }
    }

    @ParametersAreNonnullByDefault
    public static boolean trigger(Event e, SlimefunItemStack stack) {
        return trigger(e, stack.getItem(), true);
    }

    @ParametersAreNonnullByDefault
    public static boolean trigger(Event e, SlimefunItemStack stack, boolean sendMessage) {
        return trigger(e, stack.getItem(), sendMessage);
    }

    @ParametersAreNonnullByDefault
    public static boolean trigger(Event e, SlimefunItem item) {
        return trigger(e, item, true);
    }

    @ParametersAreNonnullByDefault
    public static boolean trigger(Event e, SlimefunItem item, boolean sendMessage) {
        if (!(item instanceof Talisman talisman)) {
            return false;
        }

        if (ThreadLocalRandom.current().nextInt(100) > talisman.getChance()) {
            return false;
        }

        Player p = getPlayerByEventType(e);

        if (p == null || !talisman.canEffectsBeApplied(p)) {
            return false;
        }

        ItemStack talismanItem = talisman.getItem();

        if (SlimefunUtils.containsSimilarItem(p.getInventory(), talismanItem, true)) {
            if (talisman.canUse(p, true)) {
                activateTalisman(e, p, p.getInventory(), talisman, talismanItem, sendMessage);
                return true;
            } else {
                return false;
            }
        } else {
            SlimefunItemStack enderTalismanItem = talisman.getEnderVariant();
            if (enderTalismanItem == null) {
                return false;
            }

            EnderTalisman enderTalisman = enderTalismanItem.getItem(EnderTalisman.class);
            if (enderTalisman != null && SlimefunUtils.containsSimilarItem(p.getEnderChest(), enderTalismanItem, true)) {
                if (talisman.canUse(p, true)) {
                    activateTalisman(e, p, p.getEnderChest(), enderTalisman, enderTalismanItem, sendMessage);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    @ParametersAreNonnullByDefault
    private static void activateTalisman(Event e, Player p, Inventory inv, Talisman talisman, ItemStack talismanItem, boolean sendMessage) {
        TalismanActivateEvent talismanEvent = new TalismanActivateEvent(p, talisman, talismanItem);
        Bukkit.getPluginManager().callEvent(talismanEvent);
        if (!talismanEvent.isCancelled()) {
            if (!talismanEvent.preventsConsumption()) {
                consumeItem(inv, talisman, talismanItem);
            }

            applyTalismanEffects(p, talisman);
            cancelEvent(e, talisman);

            if (sendMessage) {
                talisman.sendMessage(p);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private static void consumeItem(Inventory inv, Talisman talisman, ItemStack talismanItem) {
        if (talisman.isConsumable()) {
            ItemStack[] contents = inv.getContents();

            for (int i = 0; i < contents.length; i++) {
                ItemStack item = contents[i];

                if (SlimefunUtils.isItemSimilar(item, talismanItem, true, false)) {
                    ItemUtils.consumeItem(item, false);
                    return;
                }
            }
        }
    }

    @ParametersAreNonnullByDefault
    private static void applyTalismanEffects(Player p, Talisman talisman) {
        for (PotionEffect effect : talisman.getEffects()) {
            p.addPotionEffect(effect);
        }
    }

    @ParametersAreNonnullByDefault
    private static void cancelEvent(Event e, Talisman talisman) {
        if (e instanceof Cancellable cancellable && talisman.isEventCancelled()) {
            cancellable.setCancelled(true);
        }
    }

    /**
     * This returns whether the {@link Talisman} is silent.
     * A silent {@link Talisman} will not send a message to a {@link Player}
     * when activated.
     * 
     * @return Whether this {@link Talisman} is silent
     */
    public boolean isSilent() {
        return getMessageSuffix() == null;
    }

    @Nullable
    protected final String getMessageSuffix() {
        return suffix;
    }

    /**
     * This method sends the given {@link Player} the message of this {@link Talisman}.
     * Dependent on the selected config setting, the message will be sent via the actionbar
     * or in the chat window.
     * 
     * @param p
     *            The {@link Player} who shall receive the message
     */
    public void sendMessage(@Nonnull Player p) {
        Validate.notNull(p, "The Player must not be null.");

        // Check if this Talisman has a message
        if (!isSilent()) {
            try {
                String messageKey = "messages.talisman." + getMessageSuffix();

                if (Slimefun.getRegistry().useActionbarForTalismans()) {
                    // Use the actionbar
                    Slimefun.getLocalization().sendActionbarMessage(p, messageKey, false);
                } else {
                    // Send the message via chat
                    Slimefun.getLocalization().sendMessage(p, messageKey, true);
                }
            } catch (Exception x) {
                error("An Exception was thrown while trying to send a Talisman message", x);
            }
        }
    }

    private boolean canEffectsBeApplied(@Nonnull Player p) {
        for (PotionEffect effect : getEffects()) {
            if (effect != null && p.hasPotionEffect(effect.getType())) {
                return false;
            }
        }

        return true;
    }

    @Nullable
    private static Player getPlayerByEventType(@Nonnull Event e) {
        if (e instanceof EntityDeathEvent entityDeathEvent) {
            return entityDeathEvent.getEntity().getKiller();
        } else if (e instanceof BlockBreakEvent blockBreakEvent) {
            return blockBreakEvent.getPlayer();
        } else if (e instanceof BlockDropItemEvent blockDropItemEvent) {
            return blockDropItemEvent.getPlayer();
        } else if (e instanceof PlayerEvent playerEvent) {
            return playerEvent.getPlayer();
        } else if (e instanceof EntityEvent entityEvent) {
            return (Player) entityEvent.getEntity();
        } else if (e instanceof EnchantItemEvent enchantItemEvent) {
            return enchantItemEvent.getEnchanter();
        }

        return null;
    }
}
