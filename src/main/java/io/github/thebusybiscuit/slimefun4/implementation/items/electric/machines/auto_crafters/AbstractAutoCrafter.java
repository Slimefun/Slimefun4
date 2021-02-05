package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.auto_crafters;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemState;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.AsyncRecipeChoiceTask;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This is the abstract super class for our auto crafters.
 * 
 * @author TheBusyBiscuit
 * 
 * @see VanillaAutoCrafter
 * @see EnhancedAutoCrafter
 *
 */
public abstract class AbstractAutoCrafter extends SlimefunItem implements EnergyNetComponent {

    /**
     * The amount of energy consumed per crafting operation.
     */
    private int energyConsumed = -1;

    /**
     * The amount of energy this machine can store.
     */
    private int energyCapacity = -1;

    /**
     * The {@link NamespacedKey} used to store recipe data.
     */
    protected final NamespacedKey recipeStorageKey;

    // @formatter:off
    protected final int[] background = {
        0, 1, 2, 3, 4, 5, 6, 7, 8,
        9, 10, 14, 15, 16, 17,
        18, 19, 23, 25, 26,
        27, 28, 32, 33, 34, 35,
        36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    // @formatter:on

    @ParametersAreNonnullByDefault
    public AbstractAutoCrafter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        recipeStorageKey = new NamespacedKey(SlimefunPlugin.instance(), "recipe_key");

        addItemHandler(onRightClick());
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                AbstractAutoCrafter.this.tick(b, data);
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    @Nonnull
    private BlockUseHandler onRightClick() {
        return e -> e.getClickedBlock().ifPresent(b -> {
            Player p = e.getPlayer();

            if (SlimefunPlugin.getProtectionManager().hasPermission(p, b, ProtectableAction.INTERACT_BLOCK)) {
                if (p.isSneaking()) {
                    // Select a new recipe
                    updateRecipe(b, p);
                } else {
                    AbstractRecipe recipe = getSelectedRecipe(b);

                    if (recipe == null) {
                        // Prompt the User to crouch
                        SlimefunPlugin.getLocalization().sendMessage(p, "messages.auto-crafting.select-a-recipe");
                    } else {
                        // Show the current recipe
                        showRecipe(p, b, recipe);
                    }
                }
            }
        });
    }

    protected void tick(@Nonnull Block b, @Nonnull Config data) {
        AbstractRecipe recipe = getSelectedRecipe(b);

        if (recipe == null || getCharge(b.getLocation(), data) < getEnergyConsumption()) {
            // No valid recipe selected, abort...
            return;
        }

        Block chest = b.getRelative(BlockFace.DOWN);

        // Make sure this is a Chest
        if (chest.getType() == Material.CHEST) {
            BlockState state = PaperLib.getBlockState(chest, false).getState();

            if (state instanceof InventoryHolder) {
                Inventory inv = ((InventoryHolder) state).getInventory();

                if (craft(inv, recipe)) {
                    // We are done crafting!
                    removeCharge(b.getLocation(), getEnergyConsumption());
                }
            }
        }
    }

    /**
     * This method returns the currently selected {@link AbstractRecipe} for the given
     * {@link Block}.
     * 
     * @param b
     *            The {@link Block}
     * 
     * @return The currently selected {@link AbstractRecipe} or null
     */
    @Nullable
    public abstract AbstractRecipe getSelectedRecipe(@Nonnull Block b);

    /**
     * This method is called when a {@link Player} right clicks the {@link AbstractAutoCrafter}
     * while holding the shift button.
     * Use it to choose the {@link AbstractRecipe}.
     * 
     * @param b
     *            The {@link Block} which was clicked
     * @param p
     *            The {@link Player} who clicked
     */
    protected abstract void updateRecipe(@Nonnull Block b, @Nonnull Player p);

    protected void setSelectedRecipe(@Nonnull Block b, @Nullable AbstractRecipe recipe) {
        BlockState state = PaperLib.getBlockState(b, false).getState();

        if (state instanceof Skull) {
            if (recipe == null) {
                // Clear the value from persistent data storage
                PersistentDataAPI.remove((Skull) state, recipeStorageKey);
            } else {
                // Store the value to persistent data storage
                PersistentDataAPI.setString((Skull) state, recipeStorageKey, recipe.toString());
            }
        }
    }

    @ParametersAreNonnullByDefault
    protected void showRecipe(Player p, Block b, AbstractRecipe recipe) {
        ChestMenu menu = new ChestMenu(getItemName());
        menu.setPlayerInventoryClickable(false);
        menu.setEmptySlotsClickable(false);

        ChestMenuUtils.drawBackground(menu, background);
        ChestMenuUtils.drawBackground(menu, 45, 46, 47, 48, 50, 51, 52, 53);

        menu.addItem(49, new CustomItem(Material.BARRIER, ChatColor.RED + SlimefunPlugin.getLocalization().getMessage(p, "messages.auto-crafting.remove")));
        menu.addMenuClickHandler(49, (pl, item, slot, action) -> {
            setSelectedRecipe(b, null);
            pl.closeInventory();
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            SlimefunPlugin.getLocalization().sendMessage(p, "messages.auto-crafting.recipe-removed");
            return false;
        });

        AsyncRecipeChoiceTask task = new AsyncRecipeChoiceTask();
        recipe.show(menu, task);
        menu.open(p);

        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);

        if (!task.isEmpty()) {
            task.start(menu.toInventory());
        }
    }

    /**
     * This method checks whether the given {@link Predicate} matches the provided {@link ItemStack}.
     * 
     * @param item
     *            The {@link ItemStack} to check
     * @param predicate
     *            The {@link Predicate}
     * 
     * @return Whether the {@link Predicate} matches the {@link ItemStack}
     */
    @ParametersAreNonnullByDefault
    protected boolean matches(ItemStack item, Predicate<ItemStack> predicate) {
        return predicate.test(item);
    }

    @ParametersAreNonnullByDefault
    protected boolean matchesAny(Inventory inv, Map<Integer, Integer> itemQuantities, Predicate<ItemStack> predicate) {
        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack item = inv.getItem(slot);

            if (item != null) {
                int amount = itemQuantities.getOrDefault(slot, item.getAmount());

                if (amount > 0 && matches(item, predicate)) {
                    // Update our local quantity map
                    itemQuantities.put(slot, amount - 1);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean craft(@Nonnull Inventory inv, @Nonnull AbstractRecipe recipe) {
        Validate.notNull(inv, "The Inventory must not be null");
        Validate.notNull(recipe, "The Recipe shall not be null");

        if (InvUtils.fits(inv, recipe.getResult())) {
            Map<Integer, Integer> itemQuantities = new HashMap<>();

            for (Predicate<ItemStack> predicate : recipe.getInputs()) {
                // Check if any Item matches the Predicate
                if (!matchesAny(inv, itemQuantities, predicate)) {
                    return false;
                }
            }

            // Remove ingredients
            for (Map.Entry<Integer, Integer> entry : itemQuantities.entrySet()) {
                ItemStack item = inv.getItem(entry.getKey());

                // Double-check to be extra safe
                if (item != null) {
                    item.setAmount(entry.getValue());
                }
            }

            // All Predicates have found a match
            return true;
        }

        return false;
    }

    /**
     * This method returns the max amount of electricity this machine can hold.
     * 
     * @return The max amount of electricity this Block can store.
     */
    @Override
    public int getCapacity() {
        return energyCapacity;
    }

    /**
     * This method returns the amount of energy that is consumed per operation.
     * 
     * @return The rate of energy consumption
     */
    public int getEnergyConsumption() {
        return energyConsumed;
    }

    /**
     * This sets the energy capacity for this machine.
     * This method <strong>must</strong> be called before registering the item
     * and only before registering.
     * 
     * @param capacity
     *            The amount of energy this machine can store
     * 
     * @return This method will return the current instance of {@link AContainer}, so that can be chained.
     */
    @Nonnull
    public final AbstractAutoCrafter setCapacity(int capacity) {
        Validate.isTrue(capacity > 0, "The capacity must be greater than zero!");

        if (getState() == ItemState.UNREGISTERED) {
            this.energyCapacity = capacity;
            return this;
        } else {
            throw new IllegalStateException("You cannot modify the capacity after the Item was registered.");
        }
    }

    /**
     * This method sets the energy consumed by this machine per tick.
     * 
     * @param energyConsumption
     *            The energy consumed per tick
     * 
     * @return This method will return the current instance of {@link AContainer}, so that can be chained.
     */
    @Nonnull
    public final AbstractAutoCrafter setEnergyConsumption(int energyConsumption) {
        Validate.isTrue(energyConsumption > 0, "The energy consumption must be greater than zero!");
        Validate.isTrue(energyCapacity > 0, "You must specify the capacity before you can set the consumption amount.");
        Validate.isTrue(energyConsumption <= energyCapacity, "The energy consumption cannot be higher than the capacity (" + energyCapacity + ')');

        this.energyConsumed = energyConsumption;
        return this;
    }

    @Override
    public void register(@Nonnull SlimefunAddon addon) {
        this.addon = addon;

        if (getCapacity() <= 0) {
            warn("The capacity has not been configured correctly. The Item was disabled.");
            warn("Make sure to call '" + getClass().getSimpleName() + "#setEnergyCapacity(...)' before registering!");
        }

        if (getEnergyConsumption() <= 0) {
            warn("The energy consumption has not been configured correctly. The Item was disabled.");
            warn("Make sure to call '" + getClass().getSimpleName() + "#setEnergyConsumption(...)' before registering!");
        }

        if (getCapacity() > 0 && getEnergyConsumption() > 0) {
            super.register(addon);
        }
    }

    @Override
    public final EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

}
