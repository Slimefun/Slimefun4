package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.AncientAltar.AltarRecipe;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.URID.URID;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet.NetworkComponent;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class SlimefunItem {
	
	public static List<SlimefunItem> items = new ArrayList<SlimefunItem>();
	
	public static Map<String, URID> map_id = new HashMap<String, URID>();
	public static List<ItemStack> radioactive = new ArrayList<ItemStack>();
	public static int vanilla = 0;
	public static Set<String> tickers = new HashSet<String>();
	
	public static List<SlimefunItem> all = new ArrayList<SlimefunItem>();
	public static Map<String, Set<ItemHandler>> handlers = new HashMap<String, Set<ItemHandler>>();
	public static Map<String, SlimefunBlockHandler> blockhandler = new HashMap<String, SlimefunBlockHandler>();
	
	private ItemStack item;
	private Category category;
	private ItemStack recipeOutput = null;
	private ItemStack[] recipe;
	private RecipeType recipeType;
	private String id;
	private String[] keys;
	private Object[] values;
	private Research research;
	private boolean ghost = false, replacing = false, enchantable = true, disenchantable = true;
	private String permission = "";
	private boolean requirePermissionToUse = false;
	private Set<ItemHandler> itemhandlers;
	private URID urid;
	private boolean ticking = false;
	private boolean addon = false;
	private BlockTicker ticker;
	private EnergyTicker energy;
	public String hash;
	
	private State state;

	/**
	 * Defines whether a SlimefunItem is enabled, disabled or fall-back to its vanilla behavior.
	 *
	 * @since 4.1.10
	 */
	public enum State {
		/**
		 * This SlimefunItem is enabled.
		 */
	    ENABLED,

		/**
		 * This SlimefunItem is disabled and is not a {@link VanillaItem}.
		 */
	    DISABLED,

		/**
		 * This SlimefunItem is fall-back to its vanilla behavior, because it is disabled and is a {@link VanillaItem}.
		 */
	    VANILLA
	}

	private int month = -1;

	public int getMonth() {
		return this.month;
	}
	
	public ItemStack getItem()			{		return this.item;			}
	public Category getCategory()		{		return this.category;		}
	public ItemStack getCustomOutput()	{		return this.recipeOutput;	}
	public ItemStack[] getRecipe()		{		return this.recipe;			}
	public RecipeType getRecipeType()	{		return this.recipeType;		}

	/**
	 * Returns the identifier of this SlimefunItem.
	 *
	 * @return the identifier of this SlimefunItem
	 *
	 * @since 4.0
	 * @deprecated As of 4.1.11, renamed to {@link #getID()} for better name convenience.
	 */
	@Deprecated
	public String getName()				{		return this.id;				}

	/**
	 * Returns the identifier of this SlimefunItem.
	 *
	 * @return the identifier of this SlimefunItem
	 *
	 * @since 4.1.11, rename of {@link #getName()}.
	 */
	public String getID()				{		return this.id;				}
	public String[] listKeys()			{		return this.keys;			}
	public Object[] listValues()		{		return this.values;			}
	public Research getResearch()		{		return this.research;		}
	
	public Set<ItemHandler> getHandlers() {
		return itemhandlers;
	}
	
	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		this.item = item;
		this.category = category;
		this.id = id;
		this.recipeType = recipeType;
		this.recipe = recipe;
		this.keys = null;
		this.values = null;
		this.ghost = false;
		this.replacing = false;
		itemhandlers = new HashSet<ItemHandler>();
		
		urid = URID.nextURID(this, false);
	}
	
	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		this.item = item;
		this.category = category;
		this.id = id;
		this.recipeType = recipeType;
		this.recipe = recipe;
		this.recipeOutput = recipeOutput;
		this.keys = null;
		this.values = null;
		itemhandlers = new HashSet<ItemHandler>();
		
		urid = URID.nextURID(this, false);
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
		itemhandlers = new HashSet<ItemHandler>();
		
		urid = URID.nextURID(this, false);
	}
	
	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		this.item = item;
		this.category = category;
		this.id = id;
		this.recipeType = recipeType;
		this.recipe = recipe;
		this.keys = keys;
		this.values = values;
		itemhandlers = new HashSet<ItemHandler>();
		
		urid = URID.nextURID(this, false);
	}
	
	public SlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, boolean ghost) {
		this.item = item;
		this.category = category;
		this.id = id;
		this.recipeType = recipeType;
		this.recipe = recipe;
		this.keys = null;
		this.values = null;
		this.ghost = ghost;
		itemhandlers = new HashSet<ItemHandler>();
		
		urid = URID.nextURID(this, false);
	}
	
	public void register() {
		register(false);
	}
	
	public void register(boolean slimefun) {
		addon = !slimefun;
		try {
			if (recipe.length < 9) recipe = new ItemStack[] {null, null, null, null, null, null, null, null, null};
			all.add(this);
			
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".enabled", true);
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".can-be-used-in-workbenches", this.replacing);
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".allow-enchanting", this.enchantable);
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".allow-disenchanting", this.disenchantable);
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".required-permission", this.permission);
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".require-permission-to-use", this.requirePermissionToUse);
			if (this.keys != null && this.values != null) {
				for (int i = 0; i < this.keys.length; i++) {
					SlimefunStartup.getItemCfg().setDefaultValue(this.id + "." + this.keys[i], this.values[i]);
				}
			}
			
			for (World world: Bukkit.getWorlds()) {
				SlimefunStartup.getWhitelist().setDefaultValue(world.getName() + ".enabled", true);
				SlimefunStartup.getWhitelist().setDefaultValue(world.getName() + ".enabled-items." + this.id, true);
			}
			
			if (this.isTicking() && !SlimefunStartup.getCfg().getBoolean("URID.enable-tickers")) {
			    this.state = State.DISABLED;
			    return;
			}
			
			if (SlimefunStartup.getItemCfg().getBoolean(this.id + ".enabled")) {
				if (!Category.list().contains(category)) category.register();
				
				this.state = State.ENABLED;
				
				this.replacing = SlimefunStartup.getItemCfg().getBoolean(this.id + ".can-be-used-in-workbenches");
				this.enchantable = SlimefunStartup.getItemCfg().getBoolean(this.id + ".allow-enchanting");
				this.disenchantable = SlimefunStartup.getItemCfg().getBoolean(this.id + ".allow-disenchanting");
				this.permission = SlimefunStartup.getItemCfg().getString(this.id + ".required-permission");
				this.requirePermissionToUse = SlimefunStartup.getItemCfg().getBoolean(this.id + ".require-permission-to-use");
				items.add(this);
				if (slimefun) vanilla++;
				map_id.put(this.getID(), this.getURID());
				this.create();
				for (ItemHandler handler: itemhandlers) {
					Set<ItemHandler> handlerset = getHandlers(handler.toCodename());
					handlerset.add(handler);
					handlers.put(handler.toCodename(), handlerset);
				}
				
				if (SlimefunStartup.getCfg().getBoolean("options.print-out-loading")) System.out.println("[Slimefun] Loaded Item \"" + this.getID() + "\"");
			} else {
			    if (this instanceof VanillaItem) this.state = State.VANILLA;
			    else this.state = State.DISABLED;
			}
		} catch(Exception x) {
			System.err.println("[Slimefun] Item Registration failed: " + this.id);
		}
	}
	
	public static List<SlimefunItem> list() {
		return items;
	}
	
	public void bindToResearch(Research r) {
		if (r != null) r.getEffectedItems().add(this);
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
	 * Doc needed here
	 *
	 * @param name
	 * @return
	 *
	 * @since 4.0
	 * @deprecated As of 4.1.11, renamed to {@link #getByID(String)} for better name convenience.
	 */
	@Deprecated
	public static SlimefunItem getByName(String name) {
		return (SlimefunItem) URID.decode(map_id.get(name));
	}

	/**
	 * Doc needed here
	 * @param id
	 * @return
	 *
	 * @since 4.1.11, rename of {@link #getByName(String)}.
	 */
	public static SlimefunItem getByID(String id) {
		return (SlimefunItem) URID.decode(map_id.get(id));
	}
	
	public static SlimefunItem getByItem(ItemStack item) {
		if (item == null) return null;
		for (SlimefunItem sfi: items) {
			if (sfi instanceof ChargableItem && SlimefunManager.isItemSimiliar(item, sfi.getItem(), false)) return sfi;
			else if (sfi instanceof DamagableChargableItem && SlimefunManager.isItemSimiliar(item, sfi.getItem(), false)) return sfi;
			else if (sfi instanceof ChargedItem && SlimefunManager.isItemSimiliar(item, sfi.getItem(), false)) return sfi;
			else if (sfi instanceof SlimefunBackpack && SlimefunManager.isItemSimiliar(item, sfi.getItem(), false)) return sfi;
			else if (SlimefunManager.isItemSimiliar(item, sfi.getItem(), true)) return sfi;
		}
		return null;
	}
	
	public boolean isItem(ItemStack item) {
		if (item == null) return false;
		if (this instanceof ChargableItem && SlimefunManager.isItemSimiliar(item, this.getItem(), false)) return true;
		else if (this instanceof DamagableChargableItem && SlimefunManager.isItemSimiliar(item, this.getItem(), false)) return true;
		else if (this instanceof ChargedItem && SlimefunManager.isItemSimiliar(item, this.getItem(), false)) return true;
		else return SlimefunManager.isItemSimiliar(item, this.getItem(), true);
	}
	
	public void load() {
		try {
			if (!ghost) this.category.add(this);
			ItemStack output = this.item.clone();
			if (getCustomOutput() != null) output = this.recipeOutput.clone();

			if (this.getRecipeType().toItem().isSimilar(RecipeType.MOB_DROP.toItem())) {
				try {
					EntityType entity = EntityType.valueOf(ChatColor.stripColor(recipe[4].getItemMeta().getDisplayName()).toUpperCase().replace(" ", "_"));
					List<ItemStack> dropping = new ArrayList<ItemStack>();
					if (SlimefunManager.drops.containsKey(entity)) dropping = SlimefunManager.drops.get(entity);
					dropping.add(output);
					SlimefunManager.drops.put(entity, dropping);
				} catch(Exception x) {
				}
			}
			else if (this.getRecipeType().toItem().isSimilar(RecipeType.ANCIENT_ALTAR.toItem())) {
				new AltarRecipe(Arrays.asList(this.getRecipe()), output);
			}
			else if (this.getRecipeType().getMachine() != null && getByID(this.getRecipeType().getMachine().getID()) instanceof SlimefunMachine) {
				((SlimefunMachine) getByID(this.getRecipeType().getMachine().getID())).addRecipe(recipe, output);
			}
			this.install();
		} catch(Exception x) {
			System.err.println("[Slimefun] Item Initialization failed: " + this.id);
		}
	}
	
	public static State getState(ItemStack item) {
	    for (SlimefunItem i: all) {
            if (i.isItem(item)) {
                return i.getState();
            }
        }
        return State.ENABLED;
	}
	
	public static boolean isDisabled(ItemStack item) {
	    for (SlimefunItem i: all) {
			if (i.isItem(item)) {
				return i.isDisabled();
			}
		}
	    return false;
	}
	
	public State getState(){
	    return state;
	}
	
	public boolean isDisabled(){
	    return state != State.ENABLED;
	}
	
	public void install() {}
	public void create()  {}
	
	public boolean isReplacing() {
		return replacing;
	}
	
	public boolean isEnchantable() {
	    return enchantable;
	}
	
	public boolean isDisenchantable() {
		return disenchantable;
	}

	/**
	 * doc needed here
	 * @return
	 *
	 * @since 4.1.11
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * doc needed here
	 * @return
	 *
	 * @since 4.1.11
	 */
	public boolean requirePermissionToUse() {
		return requirePermissionToUse;
	}
	
	public void setReplacing(boolean replacing) {
		this.replacing = replacing;
	}
	
	public void addItemHandler(ItemHandler... handler) {
		this.itemhandlers.addAll(Arrays.asList(handler));
		
		for (ItemHandler h: handler) {
			if (h instanceof BlockTicker) {
				this.ticking = true;
				tickers.add(getID());
				this.ticker = (BlockTicker) h;
			}
			else if (h instanceof EnergyTicker) {
				this.energy = (EnergyTicker) h;
				EnergyNet.registerComponent(getID(), NetworkComponent.SOURCE);
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
		blockhandler.put(getID(), handler);
		register(vanilla);
	}
	
	public void register(SlimefunBlockHandler handler) {
		blockhandler.put(getID(), handler);
		register(false);
	}
	
	public static Set<ItemHandler> getHandlers(String codeid) {
		if (handlers.containsKey(codeid)) return handlers.get(codeid);
		else return new HashSet<ItemHandler>();
	}

	public static void setRadioactive(ItemStack item) {
		radioactive.add(item);
	}
	
	public static ItemStack getItem(String id) {
		SlimefunItem item = getByID(id);
		return item != null ? item.getItem(): null;
	}
	
	public static void patchExistingItem(String id, ItemStack stack) {
		SlimefunItem item = getByID(id);
		if (item != null) {
			System.out.println("[Slimefun] WARNING - Patching existing Item - " + id);
			System.out.println("[Slimefun] This might take a while");
			
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
		EnergyNet.registerComponent(id, NetworkComponent.CONSUMER);
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
		EnergyNet.registerComponent(id, NetworkComponent.DISTRIBUTOR);
	}
	
	public void registerDistibutingCapacitor(boolean slimefun, final int capacity) {
		this.register(slimefun);
		EnergyNet.registerComponent(id, NetworkComponent.DISTRIBUTOR);
		ChargableBlock.registerCapacitor(id, capacity);
	}
	
	protected void setItem(ItemStack stack) {
		this.item = stack;
	}
	
	public URID getURID() {
		return urid;
	}
	
	public boolean isTicking() {
		return this.ticking;
	}
	
	public static boolean isTicking(String item) {
		return tickers.contains(item);
	}
	
	public BlockTicker getTicker() {
		return this.ticker;
	}
	
	public static void registerBlockHandler(String id, SlimefunBlockHandler handler) {
		blockhandler.put(id, handler);
	}

	public void registerChargeableBlock(boolean vanilla, int capacity, ItemHandler... handlers) {
		addItemHandler(handlers);
		registerChargeableBlock(vanilla, capacity);
	}

	public EnergyTicker getEnergyTicker() {
		return this.energy;
	}
	
	public BlockMenu getBlockMenu(Block b) {
		return BlockStorage.getInventory(b);
	}
	
	public void addWikipage(String page) {
		Slimefun.addWikiPage(this.getID(), "https://github.com/mrCookieSlime/Slimefun4/wiki/" + page);
	}
	
	public boolean isAddonItem() {
		return this.addon;
	}
}
