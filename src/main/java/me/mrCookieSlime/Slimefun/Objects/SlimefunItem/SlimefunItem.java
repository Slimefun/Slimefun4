package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import io.github.thebusybiscuit.cscorelib2.collections.OptionalMap;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.exceptions.IdConflictException;
import io.github.thebusybiscuit.slimefun4.api.exceptions.UnregisteredItemException;
import io.github.thebusybiscuit.slimefun4.api.items.Placeable;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.AutoDisenchanter;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.AutoEnchanter;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Objects.handlers.GeneratorTicker;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class SlimefunItem implements Placeable {

    private ItemState state = ItemState.UNREGISTERED;

    protected String id;
    protected SlimefunAddon addon;
    protected ItemStack item;
    protected Category category;
    protected ItemStack[] recipe;
    protected RecipeType recipeType;
    protected ItemStack recipeOutput;
    protected Research research;

    protected boolean enchantable = true;
    protected boolean disenchantable = true;
    protected boolean hidden = false;
    protected boolean useableInWorkbench = false;

    private String[] keys;
    private Object[] values;

    private Optional<String> wikiLink = Optional.empty();
    private final OptionalMap<Class<? extends ItemHandler>, ItemHandler> itemhandlers = new OptionalMap<>(HashMap::new);

    private boolean ticking = false;
    private BlockTicker blockTicker;
    private GeneratorTicker energyTicker;

    /**
     * This creates a new {@link SlimefunItem} from the given arguments.
     *
     * @param category   The {@link Category} this {@link SlimefunItem} belongs to
     * @param item       The {@link SlimefunItemStack} that describes the visual features of our {@link SlimefunItem}
     * @param recipeType the {@link RecipeType} that determines how this {@link SlimefunItem} is crafted
     * @param recipe     An Array representing the recipe of this {@link SlimefunItem}
     */
    public SlimefunItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        this(category, item, recipeType, recipe, null);
    }

    /**
     * This creates a new {@link SlimefunItem} from the given arguments.
     *
     * @param category     The {@link Category} this {@link SlimefunItem} belongs to
     * @param item         The {@link SlimefunItemStack} that describes the visual features of our {@link SlimefunItem}
     * @param recipeType   the {@link RecipeType} that determines how this {@link SlimefunItem} is crafted
     * @param recipe       An Array representing the recipe of this {@link SlimefunItem}
     * @param recipeOutput The result of crafting this item
     */
    public SlimefunItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        this(category, item, recipeType, recipe, recipeOutput, null, null);
    }

    public SlimefunItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
        this(category, item, recipeType, recipe, null, keys, values);
    }

    public SlimefunItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, String[] keys, Object[] values) {
        Validate.notNull(category, "'category' is not allowed to be null!");
        Validate.notNull(item, "'item' is not allowed to be null!");

        this.category = category;
        this.item = item;
        this.id = item.getItemID();
        this.recipeType = recipeType;
        this.recipe = recipe;
        this.recipeOutput = recipeOutput;
        this.keys = keys;
        this.values = values;
    }

    // Previously deprecated constructor, now only for internal purposes
    protected SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        this.category = category;
        this.item = item;
        this.id = id;
        this.recipeType = recipeType;
        this.recipe = recipe;
    }

    /**
     * Returns the identifier of this {@link SlimefunItem}.
     *
     * @return the identifier of this {@link SlimefunItem}
     */
    public String getID() {
        return id;
    }

    /**
     * This method returns the {@link ItemState} this {@link SlimefunItem}
     * is currently in. This can be used to determine whether a {@link SlimefunItem}
     * is enabled or disabled.
     *
     * {@link VanillaItem} represents a special case here.
     *
     * @return The {@link ItemState} of this {@link SlimefunItem}
     */
    public ItemState getState() {
        return state;
    }

    /**
     * This returns the {@link ItemStack} of this {@link SlimefunItem}.
     * The {@link ItemStack} describes the look and feel of this {@link SlimefunItem}.
     *
     * @return The {@link ItemStack} that this {@link SlimefunItem} represents
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * This returns the {@link Category} of our {@link SlimefunItem}, every {@link SlimefunItem}
     * is associated with exactly one {@link Category}.
     *
     * @return The {@link Category} that this {@link SlimefunItem} belongs to
     */
    public Category getCategory() {
        return category;
    }

    public ItemStack[] getRecipe() {
        return recipe;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public ItemStack getRecipeOutput() {
        return recipeOutput;
    }

    public Research getResearch() {
        return research;
    }

    /**
     * This returns whether or not this {@link SlimefunItem} is allowed to be used in
     * an {@link AutoEnchanter}.
     *
     * @return Whether this {@link SlimefunItem} can be enchanted.
     */
    public boolean isEnchantable() {
        return enchantable;
    }

    /**
     * This returns whether or not this {@link SlimefunItem} is allowed to be used in
     * an {@link AutoDisenchanter}.
     *
     * @return Whether this {@link SlimefunItem} can be disenchanted.
     */
    public boolean isDisenchantable() {
        return disenchantable;
    }

    /**
     * This method returns whether this {@link SlimefunItem} was hidden from the
     * {@link SlimefunGuide}.
     *
     * @return Whether this {@link SlimefunItem} is hidden.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * This method returns whether this {@link SlimefunItem} was added by an addon.
     *
     * @return Whether this {@link SlimefunItem} was added by an addon.
     */
    public boolean isAddonItem() {
        return !(addon instanceof SlimefunPlugin);
    }

    /**
     * This method returns the {@link SlimefunAddon} that registered this
     * {@link SlimefunItem}. If this Item is from Slimefun itself, the current
     * instance of {@link SlimefunPlugin} will be returned.
     * Use an instanceof check or {@link SlimefunItem#isAddonItem()} to account for that.
     *
     * @return The {@link SlimefunAddon} that registered this {@link SlimefunItem}
     */
    public SlimefunAddon getAddon() {
        return addon;
    }

    public BlockTicker getBlockTicker() {
        return blockTicker;
    }

    // We should maybe refactor this and move it to a subclass
    public GeneratorTicker getEnergyTicker() {
        return energyTicker;
    }

    /**
     * This method returns whether this {@link SlimefunItem} is disabled.
     *
     * @return Whether this {@link SlimefunItem} is disabled.
     */
    public boolean isDisabled() {
        if (state == ItemState.UNREGISTERED) {
            error("isDisabled() cannot be called before registering the item", new UnregisteredItemException(this));
            return false;
        }

        return state != ItemState.ENABLED;
    }

    /**
     * This method registers this {@link SlimefunItem}.
     * Always call this method after your {@link SlimefunItem} has been initialized.
     * Never call it more than once!
     *
     * @param addon
     *            The {@link SlimefunAddon} that this {@link SlimefunItem} belongs to.
     */
    public void register(SlimefunAddon addon) {
        this.addon = addon;

        try {
            preRegister();

            SlimefunItem conflicting = getByID(id);

            if (conflicting != null) {
                throw new IdConflictException(this, conflicting);
            }

            if (recipe == null || recipe.length < 9) {
                recipe = new ItemStack[] { null, null, null, null, null, null, null, null, null };
            }

            SlimefunPlugin.getRegistry().getAllSlimefunItems().add(this);

            SlimefunPlugin.getItemCfg().setDefaultValue(id + ".enabled", true);
            SlimefunPlugin.getItemCfg().setDefaultValue(id + ".can-be-used-in-workbenches", useableInWorkbench);
            SlimefunPlugin.getItemCfg().setDefaultValue(id + ".hide-in-guide", hidden);
            SlimefunPlugin.getItemCfg().setDefaultValue(id + ".allow-enchanting", enchantable);
            SlimefunPlugin.getItemCfg().setDefaultValue(id + ".allow-disenchanting", disenchantable);

            if (keys != null && values != null) {
                for (int i = 0; i < keys.length; i++) {
                    SlimefunPlugin.getItemCfg().setDefaultValue(id + '.' + keys[i], values[i]);
                }
            }

            for (World world : Bukkit.getWorlds()) {
                SlimefunPlugin.getWhitelist().setDefaultValue(world.getName() + ".enabled", true);
                SlimefunPlugin.getWhitelist().setDefaultValue(world.getName() + ".enabled-items." + id, true);
            }

            if (ticking && !SlimefunPlugin.getCfg().getBoolean("URID.enable-tickers")) {
                state = ItemState.DISABLED;
                return;
            }

            if (this instanceof Radioactive) {
                SlimefunPlugin.getRegistry().getRadioactiveItems().add(this);
            }

            if (this instanceof WitherProof) {
                SlimefunPlugin.getRegistry().getWitherProofBlocks().put(id, (WitherProof) this);
            }

            if (this instanceof EnergyNetComponent && !SlimefunPlugin.getRegistry().getEnergyCapacities().containsKey(getID())) {
                registerEnergyNetComponent((EnergyNetComponent) this);
            }

            if (SlimefunPlugin.getItemCfg().getBoolean(id + ".enabled")) {

                if (!SlimefunPlugin.getRegistry().getEnabledCategories().contains(category)) {
                    category.register();
                }

                state = ItemState.ENABLED;

                useableInWorkbench = SlimefunPlugin.getItemCfg().getBoolean(id + ".can-be-used-in-workbenches");
                hidden = SlimefunPlugin.getItemCfg().getBoolean(id + ".hide-in-guide");
                enchantable = SlimefunPlugin.getItemCfg().getBoolean(id + ".allow-enchanting");
                disenchantable = SlimefunPlugin.getItemCfg().getBoolean(id + ".allow-disenchanting");

                SlimefunPlugin.getRegistry().getEnabledSlimefunItems().add(this);
                SlimefunPlugin.getRegistry().getSlimefunItemIds().put(id, this);

                for (ItemHandler handler : itemhandlers.values()) {
                    if (!handler.isPrivate()) {
                        Set<ItemHandler> handlerset = getPublicItemHandlers(handler.getIdentifier());
                        handlerset.add(handler);
                    }
                }
            } else {
                if (this instanceof VanillaItem) {
                    state = ItemState.VANILLA;
                } else {
                    state = ItemState.DISABLED;
                }
            }

            postRegister();
        } catch (Exception x) {
            error("Registering " + toString() + " has failed", x);
        }
    }

    private void registerEnergyNetComponent(EnergyNetComponent component) {
        switch (component.getEnergyComponentType()) {
            case CONSUMER:
                SlimefunPlugin.getRegistry().getEnergyConsumers().add(id);
                break;
            case CAPACITOR:
                SlimefunPlugin.getRegistry().getEnergyCapacitors().add(id);
                break;
            case GENERATOR:
                SlimefunPlugin.getRegistry().getEnergyGenerators().add(id);
                break;
            default:
                break;
        }

        int capacity = component.getCapacity();

        if (capacity > 0) {
            SlimefunPlugin.getRegistry().getEnergyCapacities().put(id, capacity);
        }
    }

    public void bindToResearch(Research r) {
        if (r != null) {
            r.getAffectedItems().add(this);
        }

        this.research = r;
    }

    public void setRecipe(ItemStack[] recipe) {
        this.recipe = recipe;
    }

    public void setRecipeType(RecipeType type) {
        this.recipeType = type;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setRecipeOutput(ItemStack output) {
        this.recipeOutput = output;
    }

    /**
     * This method returns whether or not this {@link SlimefunItem} is allowed to
     * be used in a Crafting Table.
     *
     * Items of type {@link VanillaItem} may be used in workbenches for example.
     *
     * @see #setUseableInWorkbench(boolean)
     *
     * @return Whether this {@link SlimefunItem} may be used in a Workbench.
     */
    public boolean isUseableInWorkbench() {
        return useableInWorkbench;
    }

    /**
     * This sets whether or not this {@link SlimefunItem} is allowed to be
     * used in a normal Crafting Table.
     *
     * @param useable
     *            Whether this {@link SlimefunItem} should be useable in a workbench
     *
     * @return This instance of {@link SlimefunItem}
     */
    public SlimefunItem setUseableInWorkbench(boolean useable) {
        this.useableInWorkbench = useable;
        return this;
    }

    /**
     * This method checks whether the provided {@link ItemStack} represents
     * this {@link SlimefunItem}.
     *
     * @param item
     *            The {@link ItemStack} to compare
     *
     * @return Whether the given {@link ItemStack} represents this {@link SlimefunItem}
     */
    public boolean isItem(ItemStack item) {
        if (item == null) return false;

        if (item.hasItemMeta()) {
            Optional<String> itemID = SlimefunPlugin.getItemDataService().getItemData(item);

            if (itemID.isPresent()) {
                return getID().equals(itemID.get());
            }
        }

        // Support for legacy items
        if (this instanceof ChargableItem && SlimefunUtils.isItemSimilar(item, this.item, false)) return true;
        else if (this instanceof SlimefunBackpack && SlimefunUtils.isItemSimilar(item, this.item, false)) return true;
        else return SlimefunUtils.isItemSimilar(item, this.item, true);
    }

    /**
     * This method is used for internal purposes only.
     */
    public void load() {
        try {
            if (!hidden) {
                category.add(this);
            }

            ItemStack output = recipeOutput == null ? item.clone() : recipeOutput.clone();
            recipeType.register(recipe, output);
        } catch (Exception x) {
            error("Failed to properly load the Item \"" + id + "\"", x);
        }
    }

    public void addItemHandler(ItemHandler... handlers) {
        for (ItemHandler handler : handlers) {
            itemhandlers.put(handler.getIdentifier(), handler);

            // Tickers are a special case (at the moment at least)
            if (handler instanceof BlockTicker) {
                ticking = true;
                SlimefunPlugin.getRegistry().getTickerBlocks().add(getID());
                blockTicker = (BlockTicker) handler;
            } else if (handler instanceof GeneratorTicker) {
                energyTicker = (GeneratorTicker) handler;
            }
        }
    }

    /**
     * This method is called before {@link #register(SlimefunAddon)}.
     * Override this method to add any additional setup, adding an {@link ItemHandler} for example.
     */
    public void preRegister() {
        // Override this method to execute code before the Item has been registered
        // Useful for calls to addItemHandler(...)
    }

    /**
     * This method is called after {@link #register(SlimefunAddon)}.
     * Override this method to add any additional setup that needs to happen after
     * the original registration of this {@link SlimefunItem}.
     */
    public void postRegister() {
        // Override this method to execute code after the Item has been registered
        // Useful for calls to Slimefun.getItemValue(...)
    }

    protected void setItem(ItemStack stack) {
        this.item = stack;
    }

    /**
     * This method will assign the given wiki page to this Item.
     * Note that you only need to provide the page name itself,
     * the URL to our wiki is prepended automatically.
     *
     * @param page
     *            The associated wiki page
     */
    public void addOficialWikipage(String page) {
        Validate.notNull(page, "Wiki page cannot be null.");
        wikiLink = Optional.of("https://github.com/TheBusyBiscuit/Slimefun4/wiki/" + page);
    }

    /**
     * This method returns the wiki page that has been asigned to this item.
     * It will return null, if no wiki page was found.
     *
     * @see SlimefunItem#addOficialWikipage(String)
     *
     * @return This item's wiki page
     */
    public Optional<String> getWikipage() {
        return wikiLink;
    }

    /**
     * This method will return this Item's Name (The name that is displayed when
     * hovering over this {@link ItemStack} in an {@link Inventory}).
     *
     * @return This item's name in {@link ItemStack} form
     */
    public final String getItemName() {
        return ItemUtils.getItemName(item);
    }

    /**
     * This method returns a Set of item handlers associated with this Item.
     *
     * @return The Set of item handlers
     */
    public Collection<ItemHandler> getHandlers() {
        return itemhandlers.values();
    }

    /**
     * This method calls every {@link ItemHandler} of the given {@link Class}
     * and performs the action as specified via the {@link Consumer}.
     *
     * @param c
     *            The {@link Class} of the {@link ItemHandler} to call.
     * @param callable
     *            A {@link Consumer} that is called for any found {@link ItemHandler}.
     * @param <T>
     *            The type of {@link ItemHandler} to call.
     *
     * @return Whether or not an {@link ItemHandler} was found.
     */
    public <T extends ItemHandler> boolean callItemHandler(Class<T> c, Consumer<T> callable) {
        Optional<ItemHandler> handler = itemhandlers.get(c);

        if (handler.isPresent()) {
            try {
                callable.accept(c.cast(handler.get()));
            }
            catch (Throwable x) {
                error("Could not pass \"" + c.getSimpleName() + "\" for " + toString(), x);
            }

            return true;
        }

        return false;
    }

    /**
     * This returns whether or not we are scheduling a ticking task for this block.
     *
     * @return Whether this {@link SlimefunItem} is a ticking block
     */
    public boolean isTicking() {
        return ticking;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " - '" + id + "' (" + addon.getName() + ')';
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return Arrays.asList(item.clone());
    }

    @Override
    public Collection<ItemStack> getDrops(Player p) {
        return getDrops();
    }

    public void info(String message) {
        addon.getLogger().log(Level.INFO, message);
    }

    public void warn(String message) {
        addon.getLogger().log(Level.WARNING, message);
    }

    /**
     * This will throw a {@link Throwable} to the console and signal that
     * this was caused by this {@link SlimefunItem}.
     *
     * @param message
     *            The message to display alongside this Stacktrace
     * @param throwable
     *            The {@link Throwable} to throw as a stacktrace.
     */
    public void error(String message, Throwable throwable) {
        addon.getLogger().log(Level.SEVERE, "Item \"{0}\" from {1} v{2} has caused an Error!", new Object[] { id, addon.getName(), addon.getPluginVersion()});

        if (addon.getBugTrackerURL() != null) {
            // We can prompt the server operator to report it to the addon's bug tracker
            addon.getLogger().log(Level.SEVERE, "You can report it here: {0}", addon.getBugTrackerURL());
        }

        addon.getLogger().log(Level.SEVERE, message, throwable);
    }

    public static SlimefunItem getByID(String id) {
        return SlimefunPlugin.getRegistry().getSlimefunItemIds().get(id);
    }

    public static SlimefunItem getByItem(ItemStack item) {
        if (item == null) return null;

        if (item instanceof SlimefunItemStack) {
            return getByID(((SlimefunItemStack) item).getItemID());
        }

        if (item.hasItemMeta()) {
            Optional<String> itemID = SlimefunPlugin.getItemDataService().getItemData(item);

            if (itemID.isPresent()) {
                return getByID(itemID.get());
            }
        }

        // Quite expensive performance-wise
        // But necessary for supporting legacy items
        for (SlimefunItem sfi : SlimefunPlugin.getRegistry().getAllSlimefunItems()) {
            if (sfi.isItem(item)) {
                // If we have to loop all items for the given item, then at least
                // set the id via PersistenDataAPI for future performance boosts
                SlimefunPlugin.getItemDataService().setItemData(item, sfi.getID());

                return sfi;
            }
        }

        if (SlimefunUtils.isItemSimilar(item, SlimefunItems.BROKEN_SPAWNER, false)) return getByID("BROKEN_SPAWNER");
        if (SlimefunUtils.isItemSimilar(item, SlimefunItems.REPAIRED_SPAWNER, false))
            return getByID("REINFORCED_SPAWNER");

        return null;
    }

    public static ItemStack getItem(String id) {
        SlimefunItem item = getByID(id);
        return item != null ? item.getItem() : null;
    }

    public static Set<ItemHandler> getPublicItemHandlers(Class<? extends ItemHandler> identifier) {
        return SlimefunPlugin.getRegistry().getPublicItemHandlers().computeIfAbsent(identifier, c -> new HashSet<>());
    }

    public static void registerBlockHandler(String id, SlimefunBlockHandler handler) {
        SlimefunPlugin.getRegistry().getBlockHandlers().put(id, handler);
    }
}