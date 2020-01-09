package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.items.Placeable;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.ancient_altar.AltarRecipe;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNetComponent;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;

public class SlimefunItem implements Placeable {
	
	private String id;
	private ItemState state;
	private ItemStack item;
	private Category category;
	private ItemStack[] recipe;
	private RecipeType recipeType;
	protected ItemStack recipeOutput;
	private Research research;
	
	protected boolean enchantable = true;
	protected boolean disenchantable = true;
	protected boolean hidden = false;
	protected boolean useableInWorkbench = false;
	
	private boolean addon = false;
	private String permission = "";
	private List<String> noPermissionTooltip;
	private final Set<ItemHandler> itemhandlers = new HashSet<>();
	private boolean ticking = false;
	private BlockTicker blockTicker;
	private EnergyTicker energyTicker;
	private String[] keys;
	private Object[] values;
	private String wiki = null;

	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		this(category, item, id, recipeType, recipe, null);
	}

	public SlimefunItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		this(category, item, recipeType, recipe, null);
	}

	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		this(category, item, id, recipeType, recipe, recipeOutput, null, null);
	}

	public SlimefunItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		this(category, item, recipeType, recipe, recipeOutput, null, null);
	}

	public SlimefunItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		this(category, item, recipeType, recipe, null, keys, values);
	}

	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		this(category, item, id, recipeType, recipe, null, keys, values);
	}

	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, boolean hidden) {
		this(category, item, id, recipeType, recipe);
		this.hidden = hidden;
	}

	// Root constructors
	public SlimefunItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, String[] keys, Object[] values) {
		this(category, item, item.getItemID(), recipeType, recipe, recipeOutput, keys, values);
	}

	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, String[] keys, Object[] values) {
		this.category = category;
		this.item = item;
		this.id = id;
		this.recipeType = recipeType;
		this.recipe = recipe;
		this.recipeOutput = recipeOutput;
		this.keys = keys;
		this.values = values;
	}

	/**
	 * Returns the identifier of this SlimefunItem.
	 *
	 * @return the identifier of this SlimefunItem
	 *
	 * @since 4.1.11, rename of {@link #getName()}.
	 */
	public String getID()				{		return id;			}
	
	public ItemState getState()				{		return state;			}
	public ItemStack getItem()			{		return item;			}
	public Category getCategory()			{		return category;		}
	public ItemStack[] getRecipe()			{		return recipe;			}
	public RecipeType getRecipeType()		{		return recipeType;		}
	
	/**
	 * @since 4.1.11, rename of {@link #getCustomOutput()}.
	 */
	public ItemStack getRecipeOutput()		{		return recipeOutput;		}
	public Research getResearch()			{		return research;		}
	public boolean isEnchantable() 			{		return enchantable;		}
	public boolean isDisenchantable() 		{		return disenchantable;		}
	/**
	 * @since 4.1.11
	 */
	public boolean isHidden() 			{		return hidden;			}

	@Deprecated
	public boolean isReplacing() 			{		return useableInWorkbench;		}
	public boolean isAddonItem() 			{		return addon;			}
	/**
	 * @since 4.1.11
	 */
	public String getPermission() 			{		return permission;		}
	public List<String> getNoPermissionTooltip()    {       return noPermissionTooltip; }

	/**
	 * @since 4.1.11, rename of {@link #getTicker()}.
	 */
	public BlockTicker getBlockTicker()		{		return blockTicker;		}
	public EnergyTicker getEnergyTicker()		{		return energyTicker;		}
	public String[] listKeys()			{		return keys;			}
	public Object[] listValues()			{		return values;			}
	public boolean isDisabled()			{		return state != ItemState.ENABLED;	}

	public void register() {
		register(false);
	}
	
	public void register(boolean slimefun) {
		this.addon = !slimefun;
		
		try {
			preRegister();
			
			if (SlimefunPlugin.getUtilities().itemIDs.containsKey(this.id)) {
				throw new IllegalArgumentException("ID \"" + this.id + "\" already exists");
			}
			
			if (this.recipe.length < 9) {
				this.recipe = new ItemStack[] {null, null, null, null, null, null, null, null, null};
			}
			
			SlimefunPlugin.getUtilities().allItems.add(this);

			SlimefunPlugin.getItemCfg().setDefaultValue(this.id + ".enabled", true);
			SlimefunPlugin.getItemCfg().setDefaultValue(this.id + ".can-be-used-in-workbenches", this.useableInWorkbench);
			SlimefunPlugin.getItemCfg().setDefaultValue(this.id + ".hide-in-guide", this.hidden);
			SlimefunPlugin.getItemCfg().setDefaultValue(this.id + ".allow-enchanting", this.enchantable);
			SlimefunPlugin.getItemCfg().setDefaultValue(this.id + ".allow-disenchanting", this.disenchantable);
			SlimefunPlugin.getItemCfg().setDefaultValue(this.id + ".required-permission", this.permission);
			SlimefunPlugin.getItemCfg().setDefaultValue(this.id + ".no-permission-tooltip", new String[] {"&4&lLOCKED", "", "&rYou do not have Permission", "&rto access this Item"});

			if (this.keys != null && this.values != null) {
				for (int i = 0; i < this.keys.length; i++) {
					SlimefunPlugin.getItemCfg().setDefaultValue(this.id + '.' + this.keys[i], this.values[i]);
				}
			}

			for (World world : Bukkit.getWorlds()) {
				SlimefunPlugin.getWhitelist().setDefaultValue(world.getName() + ".enabled", true);
				SlimefunPlugin.getWhitelist().setDefaultValue(world.getName() + ".enabled-items." + this.id, true);
			}

			if (this.ticking && !SlimefunPlugin.getCfg().getBoolean("URID.enable-tickers")) {
				this.state = ItemState.DISABLED;
				return;
			}

			if (SlimefunPlugin.getItemCfg().getBoolean(id + ".enabled")) {
				
				if (!Category.list().contains(category)) {
					category.register();
				}

				this.state = ItemState.ENABLED;

				this.useableInWorkbench = SlimefunPlugin.getItemCfg().getBoolean(this.id + ".can-be-used-in-workbenches");
				this.hidden = SlimefunPlugin.getItemCfg().getBoolean(this.id + ".hide-in-guide");
				this.enchantable = SlimefunPlugin.getItemCfg().getBoolean(this.id + ".allow-enchanting");
				this.disenchantable = SlimefunPlugin.getItemCfg().getBoolean(this.id + ".allow-disenchanting");
				this.permission = SlimefunPlugin.getItemCfg().getString(this.id + ".required-permission");
				this.noPermissionTooltip = SlimefunPlugin.getItemCfg().getStringList(this.id + ".no-permission-tooltip");
				
				SlimefunPlugin.getUtilities().enabledItems.add(this);
				
				if (slimefun) {
					SlimefunPlugin.getUtilities().vanillaItems++;
				}
				
				SlimefunPlugin.getUtilities().itemIDs.put(this.id, this);
				
				create();
				
				for (ItemHandler handler : itemhandlers) {
					if (areItemHandlersPrivate()) continue;
					
					Set<ItemHandler> handlerset = getHandlers(handler.toCodename());
					handlerset.add(handler);
					
					SlimefunPlugin.getUtilities().itemHandlers.put(handler.toCodename(), handlerset);
				}

				if (SlimefunPlugin.getSettings().printOutLoading) {
					Slimefun.getLogger().log(Level.INFO, "Loaded Item \"{0}\"", this.id);
				}
			} 
			else {
				if (this instanceof VanillaItem) {
					this.state = ItemState.VANILLA;
				}
				else {
					this.state = ItemState.DISABLED;
				}
			}

			postRegister();
		} catch(Exception x) {
			Slimefun.getLogger().log(Level.WARNING, "Registering the Item '" + id + "' for Slimefun " + Slimefun.getVersion() + " has failed", x);
		}
	}

	public static List<SlimefunItem> list() {
		return SlimefunPlugin.getUtilities().enabledItems;
	}

	public void bindToResearch(Research r) {
		if (r != null) r.getAffectedItems().add(this);
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
	
	@Deprecated
	public void setReplacing(boolean replacing) {
		this.useableInWorkbench = replacing;
	}
	
	public boolean isUseableInWorkbench() {
		return useableInWorkbench;
	}

	/**
	 * @since 4.1.11, rename of {@link #getByName(String)}.
	 */
	public static SlimefunItem getByID(String id) {
		return SlimefunPlugin.getUtilities().itemIDs.get(id);
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

		for (SlimefunItem sfi : SlimefunPlugin.getUtilities().enabledItems) {
			if (sfi.isItem(item)) {
				// If we have to loop all items for the given item, then at least
				// set the id via PersistenDataAPI for future performance boosts
				SlimefunPlugin.getItemDataService().setItemData(item, sfi.getID());
				
				return sfi;
			}
		}
		if (SlimefunManager.isItemSimilar(item, SlimefunItems.BROKEN_SPAWNER, false)) return getByID("BROKEN_SPAWNER");
		if (SlimefunManager.isItemSimilar(item, SlimefunItems.REPAIRED_SPAWNER, false)) return getByID("REINFORCED_SPAWNER");
		
		return null;
	}

	public boolean isItem(ItemStack item) {
		if (item == null) return false;

		if (item.hasItemMeta()) {
			Optional<String> itemID = SlimefunPlugin.getItemDataService().getItemData(item);
			
			if (itemID.isPresent()) {
				return getID().equals(itemID.get());
			}
		}

		if (this instanceof ChargableItem && SlimefunManager.isItemSimilar(item, this.item, false)) return true;
		else if (this instanceof DamagableChargableItem && SlimefunManager.isItemSimilar(item, this.item, false)) return true;
		else if (this instanceof ChargedItem && SlimefunManager.isItemSimilar(item, this.item, false)) return true;
		else if (this instanceof SlimefunBackpack && SlimefunManager.isItemSimilar(item, this.item, false)) return true;
		else return SlimefunManager.isItemSimilar(item, this.item, true);		
	}

	public void load() {
		try {
			if (!hidden) category.add(this);
			ItemStack output = item.clone();
			if (recipeOutput != null) output = recipeOutput.clone();

			if (recipeType.toItem().isSimilar(RecipeType.MOB_DROP.toItem())) {
				String mob = ChatColor.stripColor(recipe[4].getItemMeta().getDisplayName()).toUpperCase().replace(' ', '_');
				try {
					EntityType entity = EntityType.valueOf(mob);
					List<ItemStack> dropping = SlimefunPlugin.getUtilities().drops.getOrDefault(entity, new ArrayList<>());
					dropping.add(output);
					SlimefunPlugin.getUtilities().drops.put(entity, dropping);
				} catch(Exception x) {
					Slimefun.getLogger().log(Level.WARNING, "An Exception occured when setting a Drop for the Mob: " + mob + " (" + x.getClass().getSimpleName() + ")");
				}
			}
			else if (recipeType.toItem().isSimilar(RecipeType.ANCIENT_ALTAR.toItem())) {
				new AltarRecipe(Arrays.asList(recipe), output);
			}
			else if (recipeType.getMachine() != null && getByID(recipeType.getMachine().getID()) instanceof SlimefunMachine) {
				((SlimefunMachine) getByID(recipeType.getMachine().getID())).addRecipe(recipe, output);
			}
			
			install();
		} catch(Exception x) {
			Slimefun.getLogger().log(Level.WARNING, "Item Setup failed: " + id + " (" + x.getClass().getSimpleName() + ")");
		}
	}

	public static ItemState getState(ItemStack item) {
		for (SlimefunItem i : SlimefunPlugin.getUtilities().allItems) {
			if (i.isItem(item)) {
				return i.getState();
			}
		}
		return ItemState.ENABLED;
	}

	public static boolean isDisabled(ItemStack item) {
		for (SlimefunItem i : SlimefunPlugin.getUtilities().allItems) {
			if (i.isItem(item)) {
				return i.isDisabled();
			}
		}
		return false;
	}

	@Deprecated
	public void install() {
		// Deprecated
	}
	
	/**
	 *  @deprecated Use {@link SlimefunItem#postRegister()} instead
	 */
	@Deprecated
	public void create()  {
		// Deprecated
	}
	
	public void addItemHandler(ItemHandler... handlers) {
		this.itemhandlers.addAll(Arrays.asList(handlers));

		for (ItemHandler handler : handlers) {
			if (handler instanceof BlockTicker) {
				this.ticking = true;
				SlimefunPlugin.getUtilities().tickers.add(getID());
				this.blockTicker = (BlockTicker) handler;
			}
			else if (handler instanceof EnergyTicker) {
				this.energyTicker = (EnergyTicker) handler;
				EnergyNet.registerComponent(getID(), EnergyNetComponent.SOURCE);
			}
		}
	}

	public void register(boolean vanilla, ItemHandler... handlers) {
		addItemHandler(handlers);
		register(vanilla);
	}

	public void register(ItemHandler... handlers) {
		addItemHandler(handlers);
		register(false);
	}

	public void register(boolean vanilla, SlimefunBlockHandler handler) {
		SlimefunPlugin.getUtilities().blockHandlers.put(getID(), handler);
		register(vanilla);
	}

	public void register(SlimefunBlockHandler handler) {
		SlimefunPlugin.getUtilities().blockHandlers.put(getID(), handler);
		register(false);
	}

	public static Set<ItemHandler> getHandlers(String codeid) {
		return SlimefunPlugin.getUtilities().itemHandlers.getOrDefault(codeid, new HashSet<>());
	}

	/**
	 * This method marks the item as radioactive.
	 * 
	 * @deprecated The Interface {@link Radioactive} should be used instead in the future.
	 * 
	 * @param item	The {@link ItemStack} to set as radioactive
	 */
	@Deprecated
	public static void setRadioactive(ItemStack item) {
		SlimefunPlugin.getUtilities().radioactiveItems.add(item);
	}

	public static ItemStack getItem(String id) {
		SlimefunItem item = getByID(id);
		return item != null ? item.getItem(): null;
	}

	public void registerChargeableBlock(int capacity) {
		this.registerChargeableBlock(false, capacity);
	}

	public void registerChargeableBlock(boolean slimefun, int capacity) {
		this.register(slimefun);
		ChargableBlock.registerChargableBlock(id, capacity, true);
		EnergyNet.registerComponent(id, EnergyNetComponent.CONSUMER);
	}

	public void registerUnrechargeableBlock(boolean slimefun, int capacity) {
		this.register(slimefun);
		ChargableBlock.registerChargableBlock(id, capacity, false);
	}

	public void registerBlockCapacitor(boolean slimefun, int capacity) {
		this.register(slimefun);
		ChargableBlock.registerCapacitor(id, capacity);
	}

	public void registerEnergyDistributor(boolean slimefun) {
		this.register(slimefun);
		EnergyNet.registerComponent(id, EnergyNetComponent.DISTRIBUTOR);
	}

	public void registerDistibutingCapacitor(boolean slimefun, final int capacity) {
		this.register(slimefun);
		EnergyNet.registerComponent(id, EnergyNetComponent.DISTRIBUTOR);
		ChargableBlock.registerCapacitor(id, capacity);
	}

	public void preRegister() {
		// Override this method to execute code before the Item has been registered
		// Useful for calls to addItemHandler(...)
	}

	public void postRegister() {
		// Override this method to execute code after the Item has been registered
		// Useful for calls to Slimefun.getItemValue(...)
	}

	protected void setItem(ItemStack stack) {
		this.item = stack;
	}

	public static boolean isTicking(String item) {
		return SlimefunPlugin.getUtilities().tickers.contains(item);
	}

	public static void registerBlockHandler(String id, SlimefunBlockHandler handler) {
		SlimefunPlugin.getUtilities().blockHandlers.put(id, handler);
	}

	public void registerChargeableBlock(boolean vanilla, int capacity, ItemHandler... handlers) {
		addItemHandler(handlers);
		registerChargeableBlock(vanilla, capacity);
	}

	/**
	 * This method will assign the given wiki page to this Item.
	 * Note that you only need to provide the page name itself,
	 * the URL to our wiki is prepended automatically.
	 * 
	 * @param page	The associated wiki page
	 */
	public void addWikipage(String page) {
		wiki = "https://github.com/TheBusyBiscuit/Slimefun4/wiki/" + page;
	}
	
	/**
	 * This method returns whether this item has been assigned a wiki page.
	 * @see SlimefunItem#addWikipage(String)
	 * 
	 * @return	Whether this Item has a wiki page
	 */
	public boolean hasWiki() {
		return wiki != null;
	}
	
	/**
	 * This method returns the wiki page that has been asigned to this item.
	 * It will return null, if no wiki page was found.
	 * @see SlimefunItem#addWikipage(String)
	 * 
	 * @return	This item's wiki page
	 */
	public String getWiki() {
		return wiki;
	}
	
	/**
	 * This method will return this Item's Name (The name that is displayed when
	 * hovering over this Item in an Inventory).
	 * 
	 * @return	This item's name in ItemStack form
	 */
	public final String getItemName() {
		return ItemUtils.getItemName(item);
	}

	/**
	 * This method returns a Set of item handlers associated with this Item.
	 * 
	 * @return	The Set of item handlers
	 */
	public Set<ItemHandler> getHandlers() {
		return itemhandlers;
	}

	/**
	 * Override this method if you don't want to add your Item Handler to the global list.
	 * Only use this method if you absolutely know what you are doing and can make sure that the
	 * Item handler is handled somewhere else.
	 * 
	 * @return	Whether this Item handler is handled directly by the Item itself
	 */
	protected boolean areItemHandlersPrivate() {
		return false;
	}
	
	public <T extends ItemHandler> void callItemHandler(Class<T> c, Consumer<T> callable) {
		itemhandlers.stream().filter(c::isInstance).map(c::cast).forEach(callable);
	}
	
	public boolean isTicking() {
		return ticking;
	}
	
	@Override
	public String toString() {
		return "SlimefunItem: " + id + " (" + state + ", vanilla=" + !addon + ")";
	}

	@Override
	public Collection<ItemStack> getDrops() {
		return Arrays.asList(item.clone());
	}

	@Override
	public Collection<ItemStack> getDrops(Player p) {
		return getDrops();
	}
}
