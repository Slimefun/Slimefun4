package io.github.thebusybiscuit.slimefun5.implementation.items.electric.machines;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import io.github.bakedlibs.dough.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.NotHopperable;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PotionCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedPotionType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 *
 * The {@link AutoBrewer} machine with most if not all potion recipes.
 *
 * @author Linox
 *
 */
public class AutoBrewer extends AContainer implements NotHopperable {

    private static final Map<Material, PotionType> potionRecipes = new HashMap<>();
    private static final Map<PotionType, PotionType> fermentations = new HashMap<>();

    static {
        potionRecipes.put(Material.SUGAR, VersionedPotionType.SWIFTNESS);
        potionRecipes.put(Material.RABBIT_FOOT, VersionedPotionType.LEAPING);
        potionRecipes.put(Material.BLAZE_POWDER, PotionType.STRENGTH);
        potionRecipes.put(XMaterial.GLISTERING_MELON_SLICE.parseMaterial(), VersionedPotionType.HEALING);
        potionRecipes.put(Material.SPIDER_EYE, PotionType.POISON);
        potionRecipes.put(Material.GHAST_TEAR, VersionedPotionType.REGENERATION);
        potionRecipes.put(Material.MAGMA_CREAM, PotionType.FIRE_RESISTANCE);
        potionRecipes.put(XMaterial.PUFFERFISH.parseMaterial(), PotionType.WATER_BREATHING);
        potionRecipes.put(Material.GOLDEN_CARROT, PotionType.NIGHT_VISION);
        potionRecipes.put(XMaterial.TURTLE_HELMET.parseMaterial(), VersionedPotionType.TURTLE_MASTER);
        potionRecipes.put(XMaterial.PHANTOM_MEMBRANE.parseMaterial(), VersionedPotionType.SLOW_FALLING);

        fermentations.put(VersionedPotionType.SWIFTNESS, PotionType.SLOWNESS);
        fermentations.put(VersionedPotionType.LEAPING, PotionType.SLOWNESS);
        fermentations.put(VersionedPotionType.HEALING, VersionedPotionType.HARMING);
        fermentations.put(PotionType.POISON, VersionedPotionType.HARMING);
        fermentations.put(PotionType.NIGHT_VISION, PotionType.INVISIBILITY);
    }

    @ParametersAreNonnullByDefault
    public AutoBrewer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected @Nullable MachineRecipe findNextRecipe(BlockMenu menu) {
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

    @ParametersAreNonnullByDefault
    private @Nullable ItemStack brew(Material input, Material potionType, PotionMeta potion) {
        if (Slimefun.getMinecraftVersion().isBefore(20,2)) {
            return brewPreBasePotionType(input, potionType, potion);
        }
        PotionType type = PotionCompat.getBasePotionType(potion);
        if (type == PotionType.WATER) {
            if (input == Material.FERMENTED_SPIDER_EYE) {
                PotionCompat.setBasePotionType(potion, PotionType.WEAKNESS);
                return new ItemStack(potionType);
            } else if (input == XMaterial.NETHER_WART.parseMaterial()) {
                PotionCompat.setBasePotionType(potion, VersionedPotionType.AWKWARD);
                return new ItemStack(potionType);
            } else if (potionType == Material.POTION && input == XMaterial.GUNPOWDER.parseMaterial()) {
                return new ItemStack(XMaterial.SPLASH_POTION.parseMaterial());
            } else if (potionType == XMaterial.SPLASH_POTION.parseMaterial() && input == XMaterial.DRAGON_BREATH.parseMaterial()) {
                return new ItemStack(XMaterial.LINGERING_POTION.parseMaterial());
            }
        } else if (input == Material.FERMENTED_SPIDER_EYE) {
            PotionType fermented = fermentations.get(type);

            if (fermented != null) {
                PotionCompat.setBasePotionType(potion, fermented);
                return new ItemStack(potionType);
            }
        } else if (input == Material.REDSTONE && PotionCompat.isExtendable(type) && !PotionCompat.isUpgradeable(type)) {
            // Fixes #3390 - Potions can only be either extended or upgraded. Not both.
            PotionCompat.setBasePotionType(potion, type);
            return new ItemStack(potionType);
        } else if (input == Material.GLOWSTONE_DUST && PotionCompat.isUpgradeable(type) && !PotionCompat.isExtendable(type)) {
            // Fixes #3390 - Potions can only be either extended or upgraded. Not both.
            PotionCompat.setBasePotionType(potion, type);
            return new ItemStack(potionType);
        } else if (type == VersionedPotionType.AWKWARD) {
            PotionType potionRecipe = potionRecipes.get(input);

            if (potionRecipe != null) {
                PotionCompat.setBasePotionType(potion, potionRecipe);
                return new ItemStack(potionType);
            }
        }

        return null;
    }

    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    private ItemStack brewPreBasePotionType(Material input, Material potionType, PotionMeta potion) {
        PotionData data = PotionCompat.getBasePotionData(potion);
        PotionType type = data.getType();
        if (type == PotionType.WATER) {
            if (input == Material.FERMENTED_SPIDER_EYE) {
                PotionCompat.setBasePotionData(potion, new PotionData(PotionType.WEAKNESS, false, false));
                return new ItemStack(potionType);
            } else if (input == XMaterial.NETHER_WART.parseMaterial()) {
                PotionCompat.setBasePotionData(potion, new PotionData(VersionedPotionType.AWKWARD, false, false));
                return new ItemStack(potionType);
            } else if (potionType == Material.POTION && input == XMaterial.GUNPOWDER.parseMaterial()) {
                return new ItemStack(XMaterial.SPLASH_POTION.parseMaterial());
            } else if (potionType == XMaterial.SPLASH_POTION.parseMaterial() && input == XMaterial.DRAGON_BREATH.parseMaterial()) {
                return new ItemStack(XMaterial.LINGERING_POTION.parseMaterial());
            }
        } else if (input == Material.FERMENTED_SPIDER_EYE) {
            PotionType fermented = fermentations.get(type);

            if (fermented != null) {
                PotionCompat.setBasePotionData(potion, new PotionData(fermented, data.isExtended(), data.isUpgraded()));
                return new ItemStack(potionType);
            }
        } else if (input == Material.REDSTONE && PotionCompat.isExtendable(type) && !data.isUpgraded()) {
            // Fixes #3390 - Potions can only be either extended or upgraded. Not both.
            PotionCompat.setBasePotionData(potion, new PotionData(type, true, false));
            return new ItemStack(potionType);
        } else if (input == Material.GLOWSTONE_DUST && PotionCompat.isUpgradeable(type) && !data.isExtended()) {
            // Fixes #3390 - Potions can only be either extended or upgraded. Not both.
            PotionCompat.setBasePotionData(potion, new PotionData(type, false, true));
            return new ItemStack(potionType);
        } else if (type == VersionedPotionType.AWKWARD) {
            PotionType potionRecipe = potionRecipes.get(input);

            if (potionRecipe != null) {
                PotionCompat.setBasePotionData(potion, new PotionData(potionRecipe, false, false));
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
    private boolean isPotion(@Nonnull Material mat) {
        return mat == Material.POTION || mat == XMaterial.SPLASH_POTION.parseMaterial() || mat == XMaterial.LINGERING_POTION.parseMaterial();
    }

    @Override
    public @Nonnull ItemStack getProgressBar() {
        return new ItemStack(Material.FISHING_ROD);
    }

    @Override
    public @Nonnull String getMachineIdentifier() {
        return "AUTO_BREWER";
    }
}

