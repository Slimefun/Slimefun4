package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.AncientAltar.AltarRecipe;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet.NetworkComponent;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class SlimefunItem {
	
	public static List<SlimefunItem> items = new ArrayList<>();
	
	public static Map<String, SlimefunItem> map_id = new HashMap<>();
	public static List<ItemStack> radioactive = new ArrayList<>();
	public static int vanilla = 0;
	public static Set<String> tickers = new HashSet<>();
	
	public static List<SlimefunItem> all = new ArrayList<>();
	public static Map<String, Set<ItemHandler>> handlers = new HashMap<>();
	public static Map<String, SlimefunBlockHandler> blockhandler = new HashMap<>();

	private String id;
	private String hash;
	private State state;
	private ItemStack item;
	private Category category;
	private ItemStack[] recipe;
	private RecipeType recipeType;
	private ItemStack recipeOutput = null;
	private Research research;
	private int month = -1;
	private boolean enchantable = true, disenchantable = true;
	private boolean hidden = false;
	private boolean replacing = false;
	private boolean addon = false;
	private String permission = "";
	private Set<ItemHandler> itemhandlers = new HashSet<ItemHandler>();
	private boolean ticking = false;
	private BlockTicker blockTicker;
	private EnergyTicker energyTicker;
	private String[] keys = null;
	private Object[] values = null;

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
	 * @since 4.0
	 * @deprecated As of 4.1.11, renamed to {@link #getID()} for better name convenience.
	 */
	@Deprecated
	public String getName()				{		return id;			}
	/**
	 * Returns the identifier of this SlimefunItem.
	 *
	 * @return the identifier of this SlimefunItem
	 *
	 * @since 4.1.11, rename of {@link #getName()}.
	 */
	public String getID()				{		return id;			}
	public String getHash()				{		return hash;			}
	public State getState()				{		return state;			}
	public ItemStack getItem()			{		return item;			}
	public Category getCategory()			{		return category;		}
	public ItemStack[] getRecipe()			{		return recipe;			}
	public RecipeType getRecipeType()		{		return recipeType;		}
	/**
	 * @since 4.0
	 * @deprecated As of 4.1.11, renamed to {@link #getRecipeOutput()} for better name convenience.
	 */
	@Deprecated
	public ItemStack getCustomOutput()		{		return recipeOutput;		}
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
	public Set<ItemHandler> getHandlers() 		{		return itemhandlers;		}
	public boolean isTicking() 			{		return ticking;			}
	/**
	 * @since 4.0
	 * @deprecated As of 4.1.11, renamed to {@link #getBlockTicker()} for better name convenience.
	 */
	@Deprecated
	public BlockTicker getTicker()			{		return blockTicker;		}
	/**
	 * @since 4.1.11, rename of {@link #getTicker()}.
	 */
	public BlockTicker getBlockTicker()		{		return blockTicker;		}
	public EnergyTicker getEnergyTicker()		{		return energyTicker;		}
	public String[] listKeys()			{		return keys;			}
	public Object[] listValues()			{		return values;			}
	public boolean isDisabled()			{		return state != State.ENABLED;	}

	public void register() {
		register(false);
	}
	
	public void register(boolean slimefun) {
		this.addon = !slimefun;
		try {
			if (map_id.containsKey(this.id)) throw new IllegalArgumentException("ID \"" + this.id + "\" already exists");
			if (this.recipe.length < 9) this.recipe = new ItemStack[] {null, null, null, null, null, null, null, null, null};
			all.add(this);
			
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".enabled", true);
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".can-be-used-in-workbenches", this.replacing);
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".hide-in-guide", this.hidden);
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".allow-enchanting", this.enchantable);
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".allow-disenchanting", this.disenchantable);
			SlimefunStartup.getItemCfg().setDefaultValue(this.id + ".required-permission", this.permission);
			if (this.keys != null && this.values != null) {
				for (int i = 0; i < this.keys.length; i++) {
					SlimefunStartup.getItemCfg().setDefaultValue(this.id + "." + this.keys[i], this.values[i]);
				}
			}
			
			for (World world: Bukkit.getWorlds()) {
				SlimefunStartup.getWhitelist().setDefaultValue(world.getName() + ".enabled", true);
				SlimefunStartup.getWhitelist().setDefaultValue(world.getName() + ".enabled-items." + this.id, true);
			}
			
			if (this.ticking && !SlimefunStartup.getCfg().getBoolean("URID.enable-tickers")) {
			    this.state = State.DISABLED;
			    return;
			}
			
			if (SlimefunStartup.getItemCfg().getBoolean(id + ".enabled")) {
				if (!Category.list().contains(category)) category.register();
				
				this.state = State.ENABLED;
				
				this.replacing = SlimefunStartup.getItemCfg().getBoolean(this.id + ".can-be-used-in-workbenches");
				this.hidden = SlimefunStartup.getItemCfg().getBoolean(this.id + ".hide-in-guide");
				this.enchantable = SlimefunStartup.getItemCfg().getBoolean(this.id + ".allow-enchanting");
				this.disenchantable = SlimefunStartup.getItemCfg().getBoolean(this.id + ".allow-disenchanting");
				this.permission = SlimefunStartup.getItemCfg().getString(this.id + ".required-permission");
				items.add(this);
				if (slimefun) vanilla++;
				map_id.put(this.id, this);
				this.create();
				for (ItemHandler handler: itemhandlers) {
					Set<ItemHandler> handlerset = getHandlers(handler.toCodename());
					handlerset.add(handler);
					handlers.put(handler.toCodename(), handlerset);
				}
				
				if (SlimefunStartup.getCfg().getBoolean("options.print-out-loading")) System.out.println("[Slimefun] Loaded Item \"" + this.id + "\"");
			} else {
			    if (this instanceof VanillaItem) this.state = State.VANILLA;
			    else this.state = State.DISABLED;
			}
		} catch(Exception x) {
			System.err.println("[Slimefun] Item Registration failed: " + this.id);
			x.printStackTrace();
		}
	}
	
	public static List<SlimefunItem> list() {
		return items;
	}
	
	public void bindToResearch(Research r) {
		if (r != null) r.getEffectedItems().add(this);
		this.research = r;
	}

	public void setHash(String hash) {
		this.hash = hash;
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
		return map_id.get(name);
	}

	/**
	 * @since 4.1.11, rename of {@link #getByName(String)}.
	 */
	public static SlimefunItem getByID(String id) {
		return map_id.get(id);
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
				try {
					EntityType entity = EntityType.valueOf(ChatColor.stripColor(recipe[4].getItemMeta().getDisplayName()).toUpperCase().replace(" ", "_"));
					List<ItemStack> dropping = new ArrayList<ItemStack>();
					if (SlimefunManager.drops.containsKey(entity)) dropping = SlimefunManager.drops.get(entity);
					dropping.add(output);
					SlimefunManager.drops.put(entity, dropping);
				} catch(Exception x) {
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
			System.err.println("[Slimefun] Item Initialization failed: " + id);
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
	
	public void install() {}
	public void create()  {}

	public void addItemHandler(ItemHandler... handler) {
		this.itemhandlers.addAll(Arrays.asList(handler));
		
		for (ItemHandler h: handler) {
			if (h instanceof BlockTicker) {
				this.ticking = true;
				tickers.add(getID());
				this.blockTicker = (BlockTicker) h;
			}
			else if (h instanceof EnergyTicker) {
				this.energyTicker = (EnergyTicker) h;
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
	
	public static boolean isTicking(String item) {
		return tickers.contains(item);
	}
	
	public static void registerBlockHandler(String id, SlimefunBlockHandler handler) {
		blockhandler.put(id, handler);
	}

	public void registerChargeableBlock(boolean vanilla, int capacity, ItemHandler... handlers) {
		addItemHandler(handlers);
		registerChargeableBlock(vanilla, capacity);
	}
	
	public BlockMenu getBlockMenu(Block b) {
		return BlockStorage.getInventory(b);
	}
	
	public void addWikipage(String page) {
		Slimefun.addWikiPage(this.getID(), "https://github.com/TheBusyBiscuit/Slimefun4/wiki/" + page);
	}
}
