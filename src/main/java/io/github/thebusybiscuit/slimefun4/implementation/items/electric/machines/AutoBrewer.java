package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class AutoBrewer extends AContainer {

    private static final HashMap<Material, PotionType> potionRecipes = new HashMap<>();
    private static final HashMap<PotionType, PotionType> fermSpiderEye = new HashMap<>();

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

        fermSpiderEye.put(PotionType.SPEED, PotionType.SLOWNESS);
        fermSpiderEye.put(PotionType.JUMP, PotionType.SLOWNESS);
        fermSpiderEye.put(PotionType.INSTANT_HEAL, PotionType.INSTANT_DAMAGE);
        fermSpiderEye.put(PotionType.POISON, PotionType.INSTANT_DAMAGE);
        fermSpiderEye.put(PotionType.NIGHT_VISION, PotionType.INVISIBILITY);
    }

    public AutoBrewer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void tick(Block b) {
        BlockMenu menu = BlockStorage.getInventory(b.getLocation());

        if (isProcessing(b)) {
            int timeleft = progress.get(b);

            if (timeleft > 0) {
                ChestMenuUtils.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                        return;
                    }

                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                }
                else {
                    progress.put(b, timeleft - 1);
                }
            }
            else {
                menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));

                for (ItemStack item : processing.get(b).getOutput()) {
                    menu.pushItem(item, getOutputSlots());
                }

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            MachineRecipe recipe = findRecipe(menu);

            if (recipe != null) {
                if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                    return;
                }

                for (int slot : getInputSlots()) {
                    menu.consumeItem(slot);
                }

                processing.put(b, recipe);
                progress.put(b, recipe.getTicks());
            }
        }
    }

    private MachineRecipe findRecipe(BlockMenu menu) {
        ItemStack input1 = menu.getItemInSlot(getInputSlots()[0]);
        ItemStack input2 = menu.getItemInSlot(getInputSlots()[1]);

        if (input1 == null || input2 == null) return null;

        if (input1.getType().name().endsWith("POTION") || input2.getType().name().endsWith("POTION")) {
            boolean slot = input1.getType().name().endsWith("POTION");
            ItemStack pItem = slot ? input1 : input2;
            ItemStack iItem = slot ? input2 : input1;

            PotionMeta potion = (PotionMeta) pItem.getItemMeta();
            if (potion == null) return null;
            PotionData potionData = potion.getBasePotionData();

            ItemStack output;
            if (potionData.getType() == PotionType.WATER) {
                if (iItem.getType() == Material.FERMENTED_SPIDER_EYE) {
                    potion.setBasePotionData(new PotionData(PotionType.WEAKNESS, false, false));
                    output = new ItemStack(pItem.getType());

                } else if (iItem.getType() == Material.NETHER_WART) {
                    potion.setBasePotionData(new PotionData(PotionType.AWKWARD, false, false));
                    output = new ItemStack(pItem.getType());

                } else if (pItem.getType() == Material.POTION && iItem.getType() == Material.GUNPOWDER) {
                    output = new ItemStack(Material.SPLASH_POTION);

                } else if (pItem.getType() == Material.SPLASH_POTION && iItem.getType() == Material.DRAGON_BREATH) {
                    output = new ItemStack(Material.LINGERING_POTION);

                } else return null;

            } else {
                if (iItem.getType() == Material.FERMENTED_SPIDER_EYE) {
                    potion.setBasePotionData(new PotionData(fermSpiderEye.get(potionData.getType()), false, false));
                    output = new ItemStack(pItem.getType());

                } else if (iItem.getType() == Material.REDSTONE) {
                    potion.setBasePotionData(new PotionData(potionData.getType(), true, potionData.isUpgraded()));
                    output = new ItemStack(pItem.getType());

                } else if (iItem.getType() == Material.GLOWSTONE_DUST) {
                    potion.setBasePotionData(new PotionData(potionData.getType(), potionData.isExtended(), true));
                    output = new ItemStack(pItem.getType());

                } else if (potionData.getType() == PotionType.AWKWARD && potionRecipes.containsKey(iItem.getType())) {
                    potion.setBasePotionData(new PotionData(potionRecipes.get(iItem.getType()), false, false));
                    output = new ItemStack(pItem.getType());

                } else return null;
            }

            output.setItemMeta(potion);

            return new MachineRecipe(30, new ItemStack[]{input1, input2}, new ItemStack[]{output});
        } else return null;
    }

    @Override
    public String getInventoryTitle() {
        return "&6Auto-Brewer";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.CARROT_ON_A_STICK);
    }

    @Override
    public int getEnergyConsumption() {
        return 6;
    }

    @Override
    public int getSpeed() {
        return 1;
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
