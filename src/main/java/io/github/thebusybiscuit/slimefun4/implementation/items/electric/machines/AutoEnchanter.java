package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.*;

public class AutoEnchanter extends AContainer {

    private final int emeraldEnchantsLimit;

    private final List<UUID> noticedPlayer = new LinkedList<>();

    private final int limit;

    public AutoEnchanter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        limit = SlimefunPlugin.getCfg().getInt("options.enchanter-level-limit");
        emeraldEnchantsLimit = SlimefunPlugin.getCfg().getInt("options.emerald-enchantment-limit");
    }

    @Override
    public String getInventoryTitle() {
        return SlimefunItems.AUTO_ENCHANTER.clone().getItemMeta().getDisplayName();
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_CHESTPLATE);
    }

    @Override
    public int getEnergyConsumption() {
        return 9;
    }

    @Override
    public int getCapacity() {
        return 128;
    }

    @Override
    protected void tick(Block b) {
        BlockMenu menu = BlockStorage.getInventory(b.getLocation());

        if (isProcessing(b)) {
            int timeleft = progress.get(b);

            if (timeleft > 0) {
                ChestMenuUtils.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                    return;
                }

                ChargableBlock.addCharge(b, -getEnergyConsumption());
                progress.put(b, timeleft - 1);
            } else {
                menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));

                for (ItemStack item : processing.get(b).getOutput()) {
                    menu.pushItem(item, getOutputSlots());
                }

                progress.remove(b);
                processing.remove(b);
            }
        } else {
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
        for (int slot : getInputSlots()) {
            ItemStack target = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1] : getInputSlots()[0]);

            // Check if the item is enchantable
            if (!isEnchantable(target)) {
                return null;
            }

            ItemStack item = menu.getItemInSlot(slot);

            // Enchant
            if (item != null && item.getType() == Material.ENCHANTED_BOOK && target != null) {
                Map<Enchantment, Integer> enchantments = new HashMap<>();
                Set<ItemEnchantment> emeraldEnchantments = new HashSet<>();
                int amount = 0;
                int specialAmount = 0;
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

                for (Map.Entry<Enchantment, Integer> e : meta.getStoredEnchants().entrySet()) {
                    if (e.getKey().canEnchantItem(target)) {
                        if (e.getValue() <= limit || limit == 0) {
                            amount++;
                            enchantments.put(e.getKey(), e.getValue());
                        } else {
                            if (!menu.toInventory().getViewers().isEmpty()) {
                                HumanEntity p = menu.toInventory().getViewers().get(0);
                                if (!noticedPlayer.contains(p.getUniqueId())) {
                                    SlimefunPlugin.getLocalization().sendMessage(menu.toInventory().getViewers().get(0), "messages.above-limit-level", true);
                                    noticedPlayer.add(p.getUniqueId());
                                }
                            }
                        }
                    }
                }

                if (SlimefunPlugin.getThirdPartySupportService().isEmeraldEnchantsInstalled()) {
                    for (ItemEnchantment enchantment : EmeraldEnchants.getInstance().getRegistry().getEnchantments(item)) {
                        if (EmeraldEnchants.getInstance().getRegistry().isApplicable(target, enchantment.getEnchantment()) && EmeraldEnchants.getInstance().getRegistry().getEnchantmentLevel(target, enchantment.getEnchantment().getName()) < enchantment.getLevel()) {
                            amount++;
                            specialAmount++;
                            emeraldEnchantments.add(enchantment);
                        }
                    }
                    specialAmount += EmeraldEnchants.getInstance().getRegistry().getEnchantments(target).size();
                }

                if (amount > 0 && specialAmount <= emeraldEnchantsLimit) {
                    ItemStack enchantedItem = target.clone();
                    enchantedItem.setAmount(1);

                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        enchantedItem.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                    }

                    for (ItemEnchantment ench : emeraldEnchantments) {
                        EmeraldEnchants.getInstance().getRegistry().applyEnchantment(enchantedItem, ench.getEnchantment(), ench.getLevel());
                    }

                    return new MachineRecipe(75 * amount, new ItemStack[]{target, item}, new ItemStack[]{enchantedItem, new ItemStack(Material.BOOK)});
                }

                return null;
            }
        }

        return null;
    }

    private boolean isEnchantable(ItemStack item) {
        SlimefunItem sfItem = null;

        // stops endless checks of getByItem for enchanted book stacks.
        if (item != null && item.getType() != Material.ENCHANTED_BOOK) {
            sfItem = SlimefunItem.getByItem(item);
        }

        return sfItem == null || sfItem.isEnchantable();
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_ENCHANTER";
    }

}