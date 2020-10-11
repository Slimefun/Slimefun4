package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 *
 * The {@link AutoBrewer} machine with most if not all potion recipes.
 *
 * @author Linox
 *
 */
public abstract class AutoBrewer extends AContainer {

    private static final Map<Material, PotionType> potionRecipes = new EnumMap<>(Material.class);
    private static final Map<PotionType, PotionType> fermentations = new EnumMap<>(PotionType.class);

    static {
        potionRecipes.put(Material.SUGAR, PotionType.SPEED);
        potionRecipes.put(Material.RABBIT_FOOT, PotionType.JUMP);
        potionRecipes.put(Material.BLAZE_POWDER, PotionType.STRENGTH);
        potionRecipes.put(Material.GLISTERING_MELON_SLICE, PotionType.INSTANT_HEAL);
        potionRecipes.put(Material.SPIDER_EYE, PotionType.POISON);
        potionRecipes.put(Material.GHAST_TEAR, PotionType.REGEN);
        potionRecipes.put(Material.MAGMA_CREAM, PotionType.FIRE_RESISTANCE);
        potionRecipes.put(Material.PUFFERFISH, PotionType.WATER_BREATHING);
        potionRecipes.put(Material.GOLDEN_CARROT, PotionType.NIGHT_VISION);
        potionRecipes.put(Material.TURTLE_HELMET, PotionType.TURTLE_MASTER);
        potionRecipes.put(Material.PHANTOM_MEMBRANE, PotionType.SLOW_FALLING);

        fermentations.put(PotionType.SPEED, PotionType.SLOWNESS);
        fermentations.put(PotionType.JUMP, PotionType.SLOWNESS);
        fermentations.put(PotionType.INSTANT_HEAL, PotionType.INSTANT_DAMAGE);
        fermentations.put(PotionType.POISON, PotionType.INSTANT_DAMAGE);
        fermentations.put(PotionType.NIGHT_VISION, PotionType.INVISIBILITY);
    }

    public AutoBrewer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        ItemStack input1 = menu.getItemInSlot(getInputSlots()[0]);
        ItemStack input2 = menu.getItemInSlot(getInputSlots()[1]);

        if (input1 == null || input2 == null) {
            return null;
        }

        if (isPotion(input1.getType()) || isPotion(input2.getType())) {
            boolean isPotionInFirstSlot = isPotion(input1.getType());
            ItemStack ingredient = isPotionInFirstSlot ? input2 : input1;

            // Reject any named items
            if (ingredient.hasItemMeta()) {
                return null;
            }

            ItemStack potionItem = isPotionInFirstSlot ? input1 : input2;
            PotionMeta potion = (PotionMeta) potionItem.getItemMeta();
            ItemStack output = brew(ingredient.getType(), potionItem.getType(), potion);

            if (output == null) {
                return null;
            }

            output.setItemMeta(potion);

            if (!InvUtils.fits(menu.toInventory(), output, getOutputSlots())) {
                return null;
            }

            for (int slot : getInputSlots()) {
                menu.consumeItem(slot);
            }

            return new MachineRecipe(30, new ItemStack[] { input1, input2 }, new ItemStack[] { output });
        } else {
            return null;
        }
    }

    private ItemStack brew(Material input, Material potionType, PotionMeta potion) {
        PotionData data = potion.getBasePotionData();

        if (data.getType() == PotionType.WATER) {
            if (input == Material.FERMENTED_SPIDER_EYE) {
                potion.setBasePotionData(new PotionData(PotionType.WEAKNESS, false, false));
                return new ItemStack(potionType);
            } else if (input == Material.NETHER_WART) {
                potion.setBasePotionData(new PotionData(PotionType.AWKWARD, false, false));
                return new ItemStack(potionType);
            } else if (potionType == Material.POTION && input == Material.GUNPOWDER) {
                return new ItemStack(Material.SPLASH_POTION);
            } else if (potionType == Material.SPLASH_POTION && input == Material.DRAGON_BREATH) {
                return new ItemStack(Material.LINGERING_POTION);
            }
        } else if (input == Material.FERMENTED_SPIDER_EYE) {
            PotionType fermented = fermentations.get(data.getType());

            if (fermented != null) {
                potion.setBasePotionData(new PotionData(fermented, false, false));
                return new ItemStack(potionType);
            }
        } else if (input == Material.REDSTONE) {
            potion.setBasePotionData(new PotionData(data.getType(), true, data.isUpgraded()));
            return new ItemStack(potionType);
        } else if (input == Material.GLOWSTONE_DUST) {
            potion.setBasePotionData(new PotionData(data.getType(), data.isExtended(), true));
            return new ItemStack(potionType);
        } else if (data.getType() == PotionType.AWKWARD) {
            PotionType potionRecipe = potionRecipes.get(input);

            if (potionRecipe != null) {
                potion.setBasePotionData(new PotionData(potionRecipe, false, false));
                return new ItemStack(potionType);
            }
        }

        return null;
    }

    /**
     * Checks whether a given {@link Material} is a valid Potion material.
     * 
     * @param mat
     *            The {@link Material} to check
     * 
     * @return Whether this {@link Material} is a valid potion
     */
    private boolean isPotion(Material mat) {
        return mat == Material.POTION || mat == Material.SPLASH_POTION || mat == Material.LINGERING_POTION;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FISHING_ROD);
    }

    @Override
    public int getEnergyConsumption() {
        return 6;
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_BREWER";
    }

    @Override
    public int getCapacity() {
        return 128;
    }
}
