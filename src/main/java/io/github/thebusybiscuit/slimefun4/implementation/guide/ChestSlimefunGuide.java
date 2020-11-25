package io.github.thebusybiscuit.slimefun4.implementation.guide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItemRecipe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.recipes.MinecraftRecipe;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.categories.FlexCategory;
import io.github.thebusybiscuit.slimefun4.core.categories.LockedCategory;
import io.github.thebusybiscuit.slimefun4.core.guide.GuideHistory;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.SlimefunGuideItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * The {@link ChestSlimefunGuide} is the standard version of our {@link SlimefunGuide}.
 * It uses an {@link Inventory} to display {@link SlimefunGuide} contents.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunGuide
 * @see SlimefunGuideImplementation
 * @see BookSlimefunGuide
 * @see CheatSheetSlimefunGuide
 *
 */
public class ChestSlimefunGuide implements SlimefunGuideImplementation {
    
    private static final int RECIPE_NEXT = 34;
    private static final int RECIPE_PREVIOUS = 28;
    private static final int RECIPE_BACK = 0;
    private static final int RECIPE_OUTPUT = 16;
    private static final int RECIPE_TYPE = 10;
    private static final int RECIPE_WIKI = 8;
    
    private static final int MAIN_SEARCH = 7;
    private static final int MAIN_SETTINGS = 1;
    private static final int DISPLAY_RECIPES_SIZE = 14;
    private static final int CATEGORY_SIZE = 36;
    private static final Sound sound = Sound.ITEM_BOOK_PAGE_TURN;

    private final int[] recipeSlots = { 3, 4, 5, 12, 13, 14, 21, 22, 23 };
    private final ItemStack item;
    private final boolean showVanillaRecipes;

    public ChestSlimefunGuide(boolean showVanillaRecipes) {
        this.showVanillaRecipes = showVanillaRecipes;
        item = new SlimefunGuideItem(this, "&aSlimefun Guide &7(Chest GUI)");
    }

    @Nonnull
    @Override
    public SlimefunGuideLayout getLayout() {
        return SlimefunGuideLayout.CHEST;
    }

    @Nonnull
    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public boolean isSurvivalMode() {
        return true;
    }

    /**
     * Returns a {@link List} of visible {@link Category} instances that the {@link SlimefunGuide} would display.
     *
     * @param p
     *            The {@link Player} who opened his {@link SlimefunGuide}
     * @param profile
     *            The {@link PlayerProfile} of the {@link Player}
     * @return a {@link List} of visible {@link Category} instances
     */
    @Nonnull
    protected List<Category> getVisibleCategories(@Nonnull Player p, @Nonnull PlayerProfile profile) {
        List<Category> categories = new LinkedList<>();

        for (Category category : SlimefunPlugin.getRegistry().getCategories()) {
            if (!category.isHidden(p) && (!(category instanceof FlexCategory) || ((FlexCategory) category).isVisible(p, profile, getLayout()))) {
                categories.add(category);
            }
        }

        return categories;
    }
    
    /**
     * Opens the main menu of the guide
     */
    @Override
    public void openMainMenu(PlayerProfile profile, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        if (isSurvivalMode()) {
            profile.getGuideHistory().clear();
        }

        ChestMenu menu = create(p);
        List<Category> categories = getVisibleCategories(p, profile);

        int index = 9;
        createHeader(p, profile, menu);

        int target = (CATEGORY_SIZE * (page - 1)) - 1;

        while (target < (categories.size() - 1) && index < CATEGORY_SIZE + 9) {
            target++;

            Category category = categories.get(target);
            displayCategory(menu, p, profile, category, index);

            index++;
        }

        int pages = target == categories.size() - 1 ? page : (categories.size() - 1) / CATEGORY_SIZE + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;

            if (next != page && next > 0) {
                openMainMenu(profile, next);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;

            if (next != page && next <= pages) {
                openMainMenu(profile, next);
            }

            return false;
        });

        menu.open(p);
    }
    
    /**
     * Displays a category item in a {@link ChestMenu}
     */
    private void displayCategory(ChestMenu menu, Player p, PlayerProfile profile, Category category, int index) {
        if (!(category instanceof LockedCategory) || !isSurvivalMode() || ((LockedCategory) category).hasUnlocked(p, profile)) {
            menu.addItem(index, category.getItem(p));
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                openCategory(profile, category, 1);
                return false;
            });
        } else {
            List<String> lore = new ArrayList<>();
            lore.add("");

            for (String line : SlimefunPlugin.getLocalization().getMessages(p, "guide.locked-category")) {
                lore.add(ChatColor.WHITE + line);
            }

            lore.add("");

            for (Category parent : ((LockedCategory) category).getParents()) {
                ItemMeta meta = parent.getItem(p).getItemMeta();
                lore.add(meta != null ? meta.getDisplayName() : "");
            }

            ItemMeta meta = category.getItem(p).getItemMeta();
            menu.addItem(index, new CustomItem(Material.BARRIER, "&4" + SlimefunPlugin.getLocalization().getMessage(p, "guide.locked") + " &7- &f" + (meta != null ? meta.getDisplayName() : ""), lore.toArray(new String[0])));
            menu.addMenuClickHandler(index, ChestMenuUtils.getEmptyClickHandler());
        }
    }
    
    /**
     * Opens a {@link Category} of the guide
     */
    @Override
    public void openCategory(PlayerProfile profile, Category category, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        if (category instanceof FlexCategory) {
            ((FlexCategory) category).open(p, profile, getLayout());
            return;
        }

        if (isSurvivalMode()) {
            profile.getGuideHistory().add(category, page);
        }

        ChestMenu menu = create(p);
        createHeader(p, profile, menu);

        menu.addItem(1, new CustomItem(ChestMenuUtils.getBackButton(p, "", ChatColor.GRAY + SlimefunPlugin.getLocalization().getMessage(p, "guide.back.guide"))));
        menu.addMenuClickHandler(1, (pl, s, is, action) -> {
            openMainMenu(profile, 1);
            return false;
        });

        int pages = (category.getItems().size() - 1) / CATEGORY_SIZE + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;

            if (next != page && next > 0) {
                openCategory(profile, category, next);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;

            if (next != page && next <= pages) {
                openCategory(profile, category, next);
            }

            return false;
        });

        int index = 9;
        int categoryIndex = CATEGORY_SIZE * (page - 1);

        for (int i = 0; i < CATEGORY_SIZE; i++) {
            int target = categoryIndex + i;

            if (target >= category.getItems().size()) {
                break;
            }

            SlimefunItem sfitem = category.getItems().get(target);

            if (Slimefun.isEnabled(p, sfitem, false)) {
                displaySlimefunItem(menu, category, p, profile, sfitem, page, index);
                index++;
            }
        }

        menu.open(p);
    }
    
    /**
     * Displays a slimefun item within a {@link Category}
     */
    private void displaySlimefunItem(ChestMenu menu, Category category, Player p, PlayerProfile profile, SlimefunItem sfitem, int page, int index) {
        Research research = sfitem.getResearch();

        if (isSurvivalMode() && !Slimefun.hasPermission(p, sfitem, false)) {
            List<String> message = SlimefunPlugin.getPermissionsService().getLore(sfitem);
            menu.addItem(index, new CustomItem(ChestMenuUtils.getNoPermissionItem(), sfitem.getItemName(), message.toArray(new String[0])));
            menu.addMenuClickHandler(index, ChestMenuUtils.getEmptyClickHandler());
        } else if (isSurvivalMode() && research != null && !profile.hasUnlocked(research)) {
            menu.addItem(index, new CustomItem(ChestMenuUtils.getNotResearchedItem(), ChatColor.WHITE + ItemUtils.getItemName(sfitem.getItem()), "&4&l" + SlimefunPlugin.getLocalization().getMessage(p, "guide.locked"), "", "&a> Click to unlock", "", "&7Cost: &b" + research.getCost() + " Level(s)"));
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                research.unlockFromGuide(this, p, profile, sfitem, category, page);
                return false;
            });
        } else {
            menu.addItem(index, sfitem.getItem());
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                try {
                    if (isSurvivalMode()) {
                        displaySlimefunItem(profile, sfitem, 0, true);
                    } else {
                        if (sfitem instanceof MultiBlockMachine) {
                            SlimefunPlugin.getLocalization().sendMessage(pl, "guide.cheat.no-multiblocks");
                        } else {
                            pl.getInventory().addItem(sfitem.getItem().clone());
                        }
                    }
                } catch (Exception | LinkageError x) {
                    printErrorMessage(pl, x);
                }

                return false;
            });
        }
    }
    
    /**
     * Opens the search menu for a player
     */
    @Override
    public void openSearch(PlayerProfile profile, String input, boolean addToHistory) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocalization().getMessage(p, "guide.search.inventory").replace("%item%", ChatUtils.crop(ChatColor.WHITE, input)));
        String searchTerm = input.toLowerCase(Locale.ROOT);

        if (addToHistory) {
            profile.getGuideHistory().add(searchTerm);
        }

        menu.setEmptySlotsClickable(false);
        createHeader(p, profile, menu);
        addBackButton(menu, 1, p, profile);

        int index = 9;
        // Find items and add them
        for (SlimefunItem slimefunItem : SlimefunPlugin.getRegistry().getEnabledSlimefunItems()) {
            if (index == 44) {
                break;
            }

            if (isSearchFilterApplicable(slimefunItem, searchTerm)) {
                ItemStack itemstack = new CustomItem(slimefunItem.getItem(), meta -> {
                    Category category = slimefunItem.getCategory();
                    meta.setLore(Arrays.asList("", ChatColor.DARK_GRAY + "\u21E8 " + ChatColor.WHITE + category.getDisplayName(p)));
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
                });

                menu.addItem(index, itemstack);
                menu.addMenuClickHandler(index, (pl, slot, itm, action) -> {
                    try {
                        if (!isSurvivalMode()) {
                            pl.getInventory().addItem(slimefunItem.getItem().clone());
                        } else {
                            displaySlimefunItem(profile, slimefunItem, 0, true);
                        }
                    } catch (Exception | LinkageError x) {
                        printErrorMessage(pl, x);
                    }

                    return false;
                });

                index++;
            }
        }

        menu.open(p);
    }

    @ParametersAreNonnullByDefault
    private boolean isSearchFilterApplicable(SlimefunItem slimefunItem, String searchTerm) {
        String itemName = ChatColor.stripColor(slimefunItem.getItemName()).toLowerCase(Locale.ROOT);
        return !itemName.isEmpty() && (itemName.equals(searchTerm) || itemName.contains(searchTerm));
    }
    
    /**
     * Displays an {@link ItemStack}'s Recipe
     */
    @Override
    public void displayItem(PlayerProfile profile, ItemStack item, int index, boolean addToHistory) {
        Player p = profile.getPlayer();

        if (p == null || item == null || item.getType() == Material.AIR) {
            return;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem != null) {
            if (sfItem.getAllRecipes().size() > 0) {
                displaySlimefunItem(profile, sfItem, index, addToHistory);
            }
            return;
        }

        if (!showVanillaRecipes) {
            return;
        }

        Recipe[] recipes = SlimefunPlugin.getMinecraftRecipeService().getRecipesFor(item);

        if (recipes.length == 0) {
            return;
        }

        showMinecraftRecipe(recipes, index, item, profile, p, addToHistory);
    }
    
    /**
     * Displays a Vanilla item's recipe
     */
    private void showMinecraftRecipe(Recipe[] recipes, int index, ItemStack item, PlayerProfile profile, Player p, boolean addToHistory) {
        Recipe recipe = recipes[index];

        ItemStack[] recipeItems = new ItemStack[9];
        RecipeType recipeType = RecipeType.NULL;
        ItemStack result = null;

        Optional<MinecraftRecipe<? super Recipe>> optional = MinecraftRecipe.of(recipe);
        RecipeChoiceTask task = new RecipeChoiceTask();

        if (optional.isPresent()) {
            showRecipeChoices(recipe, recipeItems, task);

            recipeType = new RecipeType(optional.get());
            result = recipe.getResult();
        } else {
            recipeItems = new ItemStack[] { null, null, null, null, new CustomItem(Material.BARRIER, "&4We are somehow unable to show you this Recipe :/"), null, null, null, null };
        }

        ChestMenu menu = create(p);

        if (addToHistory) {
            profile.getGuideHistory().add(item, index);
        }

        displayItemRecipe(menu, profile, p, item, recipeItems, result, recipeType,  task);

        if (recipes.length > 1) {
            for (int i = 27; i < 36; i++) {
                menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }

            menu.addItem(RECIPE_PREVIOUS, ChestMenuUtils.getPreviousButton(p, index + 1, recipes.length), (pl, slot, action, stack) -> {
                if (index > 0) {
                    showMinecraftRecipe(recipes, index - 1, item, profile, p, true);
                }
                return false;
            });

            menu.addItem(RECIPE_NEXT, ChestMenuUtils.getNextButton(p, index + 1, recipes.length), (pl, slot, action, stack) -> {
                if (index < recipes.length - 1) {
                    showMinecraftRecipe(recipes, index + 1, item, profile, p, true);
                }
                return false;
            });
        }

        menu.open(p);

        if (!task.isEmpty()) {
            task.start(menu.toInventory());
        }
    }

    private <T extends Recipe> void showRecipeChoices(T recipe, ItemStack[] recipeItems, RecipeChoiceTask task) {
        RecipeChoice[] choices = SlimefunPlugin.getMinecraftRecipeService().getRecipeShape(recipe);

        if (choices.length == 1 && choices[0] instanceof MaterialChoice) {
            recipeItems[4] = new ItemStack(((MaterialChoice) choices[0]).getChoices().get(0));

            if (((MaterialChoice) choices[0]).getChoices().size() > 1) {
                task.add(recipeSlots[4], (MaterialChoice) choices[0]);
            }
        } else {
            for (int i = 0; i < choices.length; i++) {
                if (choices[i] instanceof MaterialChoice) {
                    recipeItems[i] = new ItemStack(((MaterialChoice) choices[i]).getChoices().get(0));

                    if (((MaterialChoice) choices[i]).getChoices().size() > 1) {
                        task.add(recipeSlots[i], (MaterialChoice) choices[i]);
                    }
                }
            }
        }
    }
    
    /**
     * Displays a {@link SlimefunItem}'s recipe
     */
    @Override
    public void displaySlimefunItem(PlayerProfile profile, SlimefunItem item, int index, boolean addToHistory) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        SlimefunItemRecipe recipe = item.getRecipe(index);

        //slimefun item might not have recipes
        if (recipe == null) {
            return;
        }

        ChestMenu menu = create(p);
        Optional<String> wiki = item.getWikipage();

        if (wiki.isPresent()) {
            menu.addItem(RECIPE_WIKI, new CustomItem(Material.KNOWLEDGE_BOOK, ChatColor.WHITE + SlimefunPlugin.getLocalization().getMessage(p, "guide.tooltips.wiki"), "", ChatColor.GRAY + "\u21E8 " + ChatColor.GREEN + SlimefunPlugin.getLocalization().getMessage(p, "guide.tooltips.open-category")));
            menu.addMenuClickHandler(RECIPE_WIKI, (pl, slot, itemStack, action) -> {
                pl.closeInventory();
                ChatUtils.sendURL(pl, wiki.get());
                return false;
            });
        }

        RecipeChoiceTask task = new RecipeChoiceTask();

        if (addToHistory) {
            profile.getGuideHistory().add(item, index);
        }
        
        List<SlimefunItemRecipe> recipes = item.getAllRecipes();
        displayItemRecipe(menu, profile, p, item, recipe.getInput(), recipe.getOutput(), recipe.getType(), task);
        boolean hasMultipleRecipes = item.getAllRecipes().size() > 1;
        
        if (hasMultipleRecipes) {
            for (int i = 27; i < 36; i++) {
                menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }

            menu.addItem(RECIPE_PREVIOUS, ChestMenuUtils.getPreviousButton(p, index + 1, recipes.size()), (pl, slot, action, stack) -> {
                if (index > 0) {
                    displaySlimefunItem(profile, item, index - 1, true);
                }
                return false;
            });
            
            menu.addItem(RECIPE_NEXT, ChestMenuUtils.getNextButton(p, index + 1, recipes.size()), (pl, slot, action, stack) -> {
                if (index < recipes.size() - 1) {
                    displaySlimefunItem(profile, item, index + 1, true);
                }
                return false;
            });
        }

        if (item instanceof RecipeDisplayItem) {
            displayRecipes(p, profile, menu, (RecipeDisplayItem) item, 0, hasMultipleRecipes);
        }
        
        menu.open(p);

        if (!task.isEmpty()) {
            task.start(menu.toInventory());
        }
    }
    
    /**
     * Displays an item's recipe in a {@link ChestMenu}
     */
    private void displayItemRecipe(ChestMenu menu, PlayerProfile profile, Player p, Object item, ItemStack[] recipe, ItemStack output, RecipeType recipeType, RecipeChoiceTask task) {
        addBackButton(menu, RECIPE_BACK, p, profile);

        MenuClickHandler clickHandler = (pl, slot, itemstack, action) -> {
            try {
                if (itemstack != null && itemstack.getType() != Material.BARRIER) {
                    displayItem(profile, itemstack, 0, true);
                }
            } catch (Exception | LinkageError x) {
                printErrorMessage(pl, x);
            }
            return false;
        };

        boolean isSlimefunRecipe = item instanceof SlimefunItem;

        for (int i = 0; i < 9; i++) {
            ItemStack recipeItem = getDisplayItem(p, isSlimefunRecipe, recipe[i]);
            menu.addItem(recipeSlots[i], recipeItem, clickHandler);

            if (recipeItem != null && item instanceof MultiBlockMachine) {
                for (Tag<Material> tag : MultiBlock.getSupportedTags()) {
                    if (tag.isTagged(recipeItem.getType())) {
                        task.add(recipeSlots[i], tag);
                        break;
                    }
                }
            }
        }
    
        menu.addItem(RECIPE_TYPE, recipeType.getItem(p), clickHandler);
        menu.addItem(RECIPE_OUTPUT, output, ChestMenuUtils.getEmptyClickHandler());
    }
    
    /**
     * Creates the header for a {@link ChestMenu} in the guide.
     */
    protected void createHeader(Player p, PlayerProfile profile, ChestMenu menu) {
        for (int i = 0; i < 9; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        // Settings Panel
        menu.addItem(MAIN_SETTINGS, ChestMenuUtils.getMenuButton(p));
        menu.addMenuClickHandler(MAIN_SETTINGS, (pl, slot, item, action) -> {
            SlimefunGuideSettings.openSettings(pl, pl.getInventory().getItemInMainHand());
            return false;
        });

        // Search feature!
        menu.addItem(MAIN_SEARCH, ChestMenuUtils.getSearchButton(p));
        menu.addMenuClickHandler(MAIN_SEARCH, (pl, slot, item, action) -> {
            pl.closeInventory();

            SlimefunPlugin.getLocalization().sendMessage(pl, "guide.search.message");
            ChatInput.waitForPlayer(SlimefunPlugin.instance(), pl, msg -> SlimefunGuide.openSearch(profile, msg, isSurvivalMode(), isSurvivalMode()));

            return false;
        });

        for (int i = 45; i < 54; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }
    
    /**
     * Adds a back button to go back in the {@link PlayerProfile}'s {@link GuideHistory}
     */
    private void addBackButton(ChestMenu menu, int slot, Player p, PlayerProfile profile) {
        GuideHistory history = profile.getGuideHistory();

        if (isSurvivalMode() && history.size() > 1) {
            menu.addItem(slot, new CustomItem(ChestMenuUtils.getBackButton(p, "", "&fLeft Click: &7Go back to previous Page", "&fShift + left Click: &7Go back to Main Menu")));

            menu.addMenuClickHandler(slot, (pl, s, is, action) -> {
                if (action.isShiftClicked()) {
                    openMainMenu(profile, 1);
                } else {
                    history.goBack(this);
                }
                return false;
            });

        } else {
            menu.addItem(slot, new CustomItem(ChestMenuUtils.getBackButton(p, "", ChatColor.GRAY + SlimefunPlugin.getLocalization().getMessage(p, "guide.back.guide"))));
            menu.addMenuClickHandler(slot, (pl, s, is, action) -> {
                openMainMenu(profile, 1);
                return false;
            });
        }
    }
    
    /**
     * Gets the display {@link ItemStack} of an item in a recipe
     */
    private static ItemStack getDisplayItem(Player p, boolean isSlimefunRecipe, ItemStack item) {
        if (isSlimefunRecipe) {
            SlimefunItem slimefunItem = SlimefunItem.getByItem(item);

            if (slimefunItem == null) {
                return item;
            }

            String lore = Slimefun.hasPermission(p, slimefunItem, false) ? "&fNeeds to be unlocked elsewhere" : "&fNo Permission";
            return Slimefun.hasUnlocked(p, slimefunItem, false) ? item : new CustomItem(Material.BARRIER, ItemUtils.getItemName(item), "&4&l" + SlimefunPlugin.getLocalization().getMessage(p, "guide.locked"), "", lore);
        } else {
            return item;
        }
    }
    
    /**
     * Gets and displays {@link RecipeDisplayItem} recipes to the given {@link ChestMenu}
     */
    private void displayRecipes(Player p, PlayerProfile profile, ChestMenu menu, RecipeDisplayItem sfItem, int page, boolean hasMultipleRecipes) {
        List<ItemStack> recipes = sfItem.getDisplayRecipes();

        if (!recipes.isEmpty()) {
            menu.addItem(53, null);

            if (page == 0) {
                for (int i = hasMultipleRecipes ? 36 : 27 ; i < 45 ; i++) {
                    if (i == 37) {
                        i = 44;
                    }
                    menu.addItem(i, new CustomItem(ChestMenuUtils.getBackground(), sfItem.getRecipeSectionLabel(p)), ChestMenuUtils.getEmptyClickHandler());
                }
            }

            int pages = (recipes.size() - 1) / DISPLAY_RECIPES_SIZE + 1;

            menu.replaceExistingItem(45, ChestMenuUtils.getPreviousButton(p, page + 1, pages));
            menu.addMenuClickHandler(45, (pl, slot, itemstack, action) -> {
                if (page > 0) {
                    displayRecipes(pl, profile, menu, sfItem, page - 1, hasMultipleRecipes);
                    pl.playSound(pl.getLocation(), sound, 1, 1);
                }

                return false;
            });

            menu.replaceExistingItem(53, ChestMenuUtils.getNextButton(p, page + 1, pages));
            menu.addMenuClickHandler(53, (pl, slot, itemstack, action) -> {
                if (recipes.size() > (DISPLAY_RECIPES_SIZE * (page + 1))) {
                    displayRecipes(pl, profile, menu, sfItem, page + 1, hasMultipleRecipes);
                    pl.playSound(pl.getLocation(), sound, 1, 1);
                }

                return false;
            });

            int inputs = 37;
            int outputs = 46;

            for (int i = 0; i < DISPLAY_RECIPES_SIZE; i++) {
                int slot;

                if (i % 2 == 0) {
                    slot = inputs;
                    inputs++;
                } else {
                    slot = outputs;
                    outputs++;
                }

                addDisplayRecipe(menu, profile, recipes, slot, i, page);
            }
        }
    }
    
    /**
     * Adds {@link RecipeDisplayItem} recipes to the given {@link ChestMenu}
     */
    private void addDisplayRecipe(ChestMenu menu, PlayerProfile profile, List<ItemStack> recipes, int slot, int i, int page) {
        if ((i + (page * DISPLAY_RECIPES_SIZE)) < recipes.size()) {
            ItemStack displayItem = recipes.get(i + (page * DISPLAY_RECIPES_SIZE));

            // We want to clone this item to avoid corrupting the original
            // but we wanna make sure no stupid addon creator sneaked some nulls in here
            if (displayItem != null) {
                displayItem = displayItem.clone();
            }

            menu.replaceExistingItem(slot, displayItem);

            if (page == 0) {
                menu.addMenuClickHandler(slot, (pl, s, itemstack, action) -> {
                    displayItem(profile, itemstack, 0, true);
                    return false;
                });
            }
            
        } else {
            menu.replaceExistingItem(slot, null);
            menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
        }
    }
    
    /**
     * Creates an {@link ChestMenu} for use in the guide.
     */
    private ChestMenu create(Player p) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocalization().getMessage(p, "guide.title.main"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), sound, 1, 1));
        return menu;
    }

    private void printErrorMessage(Player p, Throwable x) {
        p.sendMessage(ChatColor.DARK_RED + "An internal server error has occurred. Please inform an admin, check the console for further info.");
        Slimefun.getLogger().log(Level.SEVERE, "An error has occurred while trying to open a SlimefunItem in the guide!", x);
    }

}
