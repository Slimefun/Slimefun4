package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.enchanting;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * The {@link AutoEnchanter}, in contrast to the {@link AutoDisenchanter}, adds
 * {@link Enchantment Enchantments} from a given enchanted book and transfers them onto
 * an {@link ItemStack}.
 *
 * @author TheBusyBiscuit
 * @author Poslovitch
 * @author Mooy1
 * @author StarWishSama
 *
 * @see AutoDisenchanter
 *
 */
public class AutoEnchanter extends AContainer {

    private final ItemSetting<Boolean> useEnchantLevelLimit = new ItemSetting<>(this, "use-enchant-level-limit", false);
    private final IntRangeSetting enchantLevelLimit = new IntRangeSetting(this, "enchant-level-limit", 0, 10, Short.MAX_VALUE);

    @ParametersAreNonnullByDefault
    public AutoEnchanter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(useEnchantLevelLimit);
        addItemSetting(enchantLevelLimit);
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_CHESTPLATE);
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        for (int slot : getInputSlots()) {
            ItemStack target = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1] : getInputSlots()[0]);

            // Check if the item is enchantable
            if (!isEnchantable(target)) {
                return null;
            }

            ItemStack item = menu.getItemInSlot(slot);

            if (item != null && item.getType() == Material.ENCHANTED_BOOK && target != null) {
                Map<Enchantment, Integer> enchantments = new HashMap<>();
                int amount = 0;
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

                for (Map.Entry<Enchantment, Integer> e : meta.getStoredEnchants().entrySet()) {
                    if (e.getKey().canEnchantItem(target)) {
                        if (!useEnchantLevelLimit.getValue() || enchantLevelLimit.getValue() >= e.getValue()) {
                            amount++;
                            enchantments.put(e.getKey(), e.getValue());
                        } else if (!menu.toInventory().getViewers().isEmpty()) {
                            String notice = ChatColors.color(SlimefunPlugin.getLocalization().getMessage("messages.above-limit-level"));
                            notice = notice.replace("%level%", String.valueOf(enchantLevelLimit.getValue()));
                            ItemStack progressBar = new CustomItem(Material.BARRIER, " ", notice);
                            menu.replaceExistingItem(22, progressBar);
                            return null;
                        }
                    }
                }

                if (amount > 0) {
                    ItemStack enchantedItem = target.clone();
                    enchantedItem.setAmount(1);

                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        enchantedItem.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                    }

                    MachineRecipe recipe = new MachineRecipe(75 * amount / this.getSpeed(), new ItemStack[] { target, item }, new ItemStack[] { enchantedItem, new ItemStack(Material.BOOK) });

                    if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                        return null;
                    }

                    for (int inputSlot : getInputSlots()) {
                        menu.consumeItem(inputSlot);
                    }

                    return recipe;
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
    public String getMachineIdentifier() {
        return "AUTO_ENCHANTER";
    }

}
