package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

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
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNetComponent;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class SlimefunItem {
	
	private String id;
	private ItemState state;
	private ItemStack item;
	private Category category;
	private ItemStack[] recipe;
	private RecipeType recipeType;
	protected ItemStack recipeOutput = null;
	private Research research;
	private int month = -1;
	private boolean enchantable = true;
	private boolean disenchantable = true;
	private boolean hidden = false;
	private boolean replacing = false;
	private boolean addon = false;
	private String permission = "";
	private List<String> noPermissionTooltip;
	private Set<ItemHandler> itemhandlers = new HashSet<>();
	private boolean ticking = false;
	private BlockTicker blockTicker;
	private EnergyTicker energyTicker;
	private String[] keys = null;
	private Object[] values = null;
	private String wiki = null;

	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		this.item = item;
		this.category = category;
		this.id = id;
		this.recipeType = recipeType;
		this.recipe = recipe;
	}

	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		this.item = item;
		this.category = category;
		this.id = id;
		this.recipeType = recipeType;
		this.recipe = recipe;
		this.recipeOutput = recipeOutput;
	}

	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, String[] keys, Object[] values) {
		this.item = item;
		this.category = category;
		this.id = id;
		this.recipeType = recipeType;
		this.recipe = recipe;
		this.recipeOutput = recipeOutput;
		this.keys = keys;
		this.values = values;
	}

	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		this.item = item;
		this.category = category;
		this.id = id;
		this.recipeType = recipeType;
		this.recipe = recipe;
		this.keys = keys;
		this.values = values;
	}

	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, boolean hidden) {
		this.item = item;
		this.category = category;
		this.id = id;
		this.recipeType = recipeType;
		this.recipe = recipe;
		this.hidden = hidden;
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
	public int getMonth()	 			{		return month;			}
	public boolean isEnchantable() 			{		return enchantable;		}
	public boolean isDisenchantable() 		{		return disenchantable;		}
	/**
	 * @since 4.1.11
	 */
	public boolean isHidden() 			{		return hidden;			}
	public boolean isReplacing() 			{		return replacing;		}
	public boolean isAddonItem() 			{		return addon;			}
	/**
	 * @since 4.1.11
	 */
	public String getPermission() 			{		return permission;		}
	public List<String> getNoPermissionTooltip()    {       return noPermissionTooltip;       }
	public Set<ItemHandler> getHandlers() 		{		return itemhandlers;		}
	public boolean isTicking() 			{		return ticking;			}

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
			if (this.recipe.length < 9) this.recipe = new ItemStack[] {null, null, null, null, null, null, null, null, null};
			SlimefunPlugin.getUtilities().allItems.add(this);

			SlimefunPlugin.getItemCfg().setDefaultValue(this.id + ".enabled", true);
			SlimefunPlugin.getItemCfg().setDefaultValue(this.id + ".can-be-used-in-workbenches", this.replacing);
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

			for (World world: Bukkit.getWorlds()) {
				SlimefunPlugin.getWhitelist().setDefaultValue(world.getName() + ".enabled", true);
				SlimefunPlugin.getWhitelist().setDefaultValue(world.getName() + ".enabled-items." + this.id, true);
			}

			if (this.ticking && !SlimefunPlugin.getCfg().getBoolean("URID.enable-tickers")) {
				this.state = ItemState.DISABLED;
				return;
			}

			if (SlimefunPlugin.getItemCfg().getBoolean(id + ".enabled")) {
				if (!Category.list().contains(category)) category.register();

				this.state = ItemState.ENABLED;

				this.replacing = SlimefunPlugin.getItemCfg().getBoolean(this.id + ".can-be-used-in-workbenches");
				this.hidden = SlimefunPlugin.getItemCfg().getBoolean(this.id + ".hide-in-guide");
				this.enchantable = SlimefunPlugin.getItemCfg().getBoolean(this.id + ".allow-enchanting");
				this.disenchantable = SlimefunPlugin.getItemCfg().getBoolean(this.id + ".allow-disenchanting");
				this.permission = SlimefunPlugin.getItemCfg().getString(this.id + ".required-permission");
				this.noPermissionTooltip = SlimefunPlugin.getItemCfg().getStringList(this.id + ".no-permission-tooltip");
				SlimefunPlugin.getUtilities().enabledItems.add(this);
				if (slimefun) SlimefunPlugin.getUtilities().vanillaItems++;
				SlimefunPlugin.getUtilities().itemIDs.put(this.id, this);
				
				create();
				
				for (ItemHandler handler: itemhandlers) {
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

	public void setReplacing(boolean replacing) {
		this.replacing = replacing;
	}

	/**
	 * @since 4.0
	 * @deprecated As of 4.1.11, renamed to {@link #getByID(String)} for better name convenience.
	 */
	@Deprecated
	public static SlimefunItem getByName(String name) {
		return getByID(name);
	}

	/**
	 * @since 4.1.11, rename of {@link #getByName(String)}.
	 */
	public static SlimefunItem getByID(String id) {
		return SlimefunPlugin.getUtilities().itemIDs.get(id);
	}

	public static SlimefunItem getByItem(ItemStack item) {
		if (item == null) return null;		
		for (SlimefunItem sfi: SlimefunPlugin.getUtilities().enabledItems) {
			if ((sfi instanceof ChargableItem && SlimefunManager.isItemSimiliar(item, sfi.getItem(), false)) ||
					(sfi instanceof DamagableChargableItem && SlimefunManager.isItemSimiliar(item, sfi.getItem(), false)) ||
					(sfi instanceof ChargedItem && SlimefunManager.isItemSimiliar(item, sfi.getItem(), false)) ||
					(sfi instanceof SlimefunBackpack && SlimefunManager.isItemSimiliar(item, sfi.getItem(), false)) ||
					SlimefunManager.isItemSimiliar(item, sfi.getItem(), true))

						return sfi;
		}
		if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BROKEN_SPAWNER, false)) return getByID("BROKEN_SPAWNER");
		if (SlimefunManager.isItemSimiliar(item, SlimefunItems.REPAIRED_SPAWNER, false)) return getByID("REINFORCED_SPAWNER");
		return null;
	}

	public boolean isItem(ItemStack item) {
		if (item == null) return false;
		if (this instanceof ChargableItem && SlimefunManager.isItemSimiliar(item, this.item, false)) return true;
		else if (this instanceof DamagableChargableItem && SlimefunManager.isItemSimiliar(item, this.item, false)) return true;
		else if (this instanceof ChargedItem && SlimefunManager.isItemSimiliar(item, this.item, false)) return true;
		else return SlimefunManager.isItemSimiliar(item, this.item, true);		
	}

	public void load() {
		try {
			if (!hidden) category.add(this);
			ItemStack output = item.clone();
			if (recipeOutput != null) output = recipeOutput.clone();

			if (recipeType.toItem().isSimilar(RecipeType.MOB_DROP.toItem())) {
				String mob = ChatColor.stripColor(recipe[4].getItemMeta().getDisplayName()).toUpperCase()
					.replace(' ', '_');
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
		for (SlimefunItem i: SlimefunPlugin.getUtilities().allItems) {
			if (i.isItem(item)) {
				return i.getState();
			}
		}
		return ItemState.ENABLED;
	}

	public static boolean isDisabled(ItemStack item) {
		for (SlimefunItem i: SlimefunPlugin.getUtilities().allItems) {
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

	/**
	 * @deprecated Use {@link SlimefunItem#addItemHandler(ItemHandler...)} instead
	 */
	@Deprecated
	public void addItemHandler(me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler... handler) {
		addItemHandler((ItemHandler[]) handler);
	}
	
	public void addItemHandler(ItemHandler... handler) {
		this.itemhandlers.addAll(Arrays.asList(handler));

		for (ItemHandler h: handler) {
			if (h instanceof BlockTicker) {
				this.ticking = true;
				SlimefunPlugin.getUtilities().tickers.add(getID());
				this.blockTicker = (BlockTicker) h;
			}
			else if (h instanceof EnergyTicker) {
				this.energyTicker = (EnergyTicker) h;
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

	/**
	 * @deprecated Use {@link SlimefunItem#register(boolean, ItemHandler...)} instead
	 */
	@Deprecated
	public void register(boolean vanilla, me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler... handlers) {
		addItemHandler(handlers);
		register(vanilla);
	}

	/**
	 * @deprecated Use {@link SlimefunItem#register(ItemHandler...)} instead
	 */
	@Deprecated
	public void register(me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler... handlers) {
		register((ItemHandler[]) handlers);
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
		if (SlimefunPlugin.getUtilities().itemHandlers.containsKey(codeid)) return SlimefunPlugin.getUtilities().itemHandlers.get(codeid);
		else return new HashSet<>();
	}

	public static void setRadioactive(ItemStack item) {
		SlimefunPlugin.getUtilities().radioactiveItems.add(item);
	}

	public static ItemStack getItem(String id) {
		SlimefunItem item = getByID(id);
		return item != null ? item.getItem(): null;
	}

	public static void patchExistingItem(String id, ItemStack stack) {
		SlimefunItem item = getByID(id);
		if (item != null) {
			Slimefun.getLogger().log(Level.INFO, "Patching existing Item... {0}", id);
			Slimefun.getLogger().log(Level.INFO, "This might take a while");

			final ItemStack old = item.getItem();
			item.setItem(stack);
			for (SlimefunItem sfi: list()) {
				ItemStack[] recipe = sfi.getRecipe();
				
				for (int i = 0; i < 9; i++) {
					if (SlimefunManager.isItemSimiliar(recipe[i], old, true)) recipe[i] = stack;
				}
				sfi.setRecipe(recipe);
			}
		}
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

	public BlockMenu getBlockMenu(Block b) {
		return BlockStorage.getInventory(b);
	}

	public void addWikipage(String page) {
		wiki = "https://github.com/TheBusyBiscuit/Slimefun4/wiki/" + page;
	}
	
	public boolean hasWiki() {
		return wiki != null;
	}
	
	public String getWiki() {
		return wiki;
	}
	
	@Override
	public String toString() {
		return "SlimefunItem: " + id + " (" + state + ", vanilla=" + !addon + ")";
	}
}
