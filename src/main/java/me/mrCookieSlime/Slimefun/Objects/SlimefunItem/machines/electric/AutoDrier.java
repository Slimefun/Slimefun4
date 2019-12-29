package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

public class AutoDrier extends AContainer implements RecipeDisplayItem {
	
	private final List<ItemStack> recipeList = new ArrayList<>();

    public AutoDrier(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        
        recipeList.add(new ItemStack(Material.ROTTEN_FLESH));
        recipeList.add(new ItemStack(Material.LEATHER));
        
        recipeList.add(new ItemStack(Material.WET_SPONGE));
        recipeList.add(new ItemStack(Material.SPONGE));
        
        recipeList.add(new ItemStack(Material.KELP));
        recipeList.add(new ItemStack(Material.DRIED_KELP));
        
        recipeList.add(new ItemStack(Material.POTION));
        recipeList.add(new ItemStack(Material.GLASS_BOTTLE));
        
        recipeList.add(new ItemStack(Material.OAK_SAPLING));
        recipeList.add(new ItemStack(Material.STICK, 2));
        
        recipeList.add(new ItemStack(Material.OAK_LEAVES));
        recipeList.add(new ItemStack(Material.STICK));
        
        recipeList.add(new ItemStack(Material.WATER_BUCKET));
        recipeList.add(new ItemStack(Material.BUCKET));
        
        recipeList.add(new ItemStack(Material.COOKED_BEEF));
        recipeList.add(SlimefunItems.BEEF_JERKY);
        
        recipeList.add(new ItemStack(Material.COOKED_PORKCHOP));
        recipeList.add(SlimefunItems.PORK_JERKY);
        
        recipeList.add(new ItemStack(Material.COOKED_CHICKEN));
        recipeList.add(SlimefunItems.CHICKEN_JERKY);
        
        recipeList.add(new ItemStack(Material.COOKED_MUTTON));
        recipeList.add(SlimefunItems.MUTTON_JERKY);
        
        recipeList.add(new ItemStack(Material.COOKED_RABBIT));
        recipeList.add(SlimefunItems.RABBIT_JERKY);
        
        recipeList.add(new ItemStack(Material.COOKED_COD));
        recipeList.add(SlimefunItems.FISH_JERKY);
    }

    @Override
    public String getInventoryTitle() {
        return "&eAuto Drier";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }
    
    @Override
    public List<ItemStack> getDisplayRecipes() {
    	return recipeList;
    }

    @Override
    protected void tick(Block b) {
    	BlockMenu menu = BlockStorage.getInventory(b);
    	
        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0) {
            	MachineHelper.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());
				
                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                }
                else progress.put(b, timeleft - 1);
            }
            else {
            	menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
            	menu.pushItem(processing.get(b).getOutput()[0], getOutputSlots());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            MachineRecipe r = null;
            int inputSlot = -1;
            
            for (int slot : getInputSlots()) {
                ItemStack item = menu.getItemInSlot(slot);
                if (item != null) {
                    Material mat = item.getType();
                    ItemStack output = null;
                    
                    for (int i = 0; i < recipeList.size(); i += 2) {
                    	if (SlimefunManager.isItemSimilar(item, recipeList.get(i), true)) {
                    		output = recipeList.get(i + 1);
                    	}
                    }
                    
                    if (Tag.SAPLINGS.isTagged(mat)) {
                        output = new ItemStack(Material.STICK, 2);
                    }
                    else if (Tag.LEAVES.isTagged(mat)) {
                        output = new ItemStack(Material.STICK, 1);
                    }
                    else if (mat == Material.SPLASH_POTION || mat == Material.LINGERING_POTION) {
                        output = new ItemStack(Material.GLASS_BOTTLE);
                    }
                    
                    if (output != null) {
                    	r = new MachineRecipe(6, new ItemStack[] {item}, new ItemStack[] {output.clone()});
                        inputSlot = slot;
                        break;
                    }
                }
            }

            if (r != null) {
                if (inputSlot == -1) return;
                if (!menu.fits(r.getOutput()[0], getOutputSlots())) return;
                
                menu.consumeItem(inputSlot);
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }
        }
    }

    @Override
    public int getEnergyConsumption() {
        return 5;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_DRIER";
    }
}
