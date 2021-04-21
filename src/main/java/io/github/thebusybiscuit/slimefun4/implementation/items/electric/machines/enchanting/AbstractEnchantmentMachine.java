package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.enchanting;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

import java.util.Arrays;
import java.util.List;

/**
 * This is a super class of the {@link AutoEnchanter} and {@link AutoDisenchanter} which is
 * used to streamline some methods and combine common attributes to reduce redundancy.
 *
 * @author TheBusyBiscuit
 *
 * @see AutoEnchanter
 * @see AutoDisenchanter
 *
 */
abstract class AbstractEnchantmentMachine extends AContainer {

    private final ItemSetting<Boolean> useLevelLimit = new ItemSetting<>(this, "use-enchant-level-limit", false);
    private final IntRangeSetting levelLimit = new IntRangeSetting(this, "enchant-level-limit", 0, 10, Short.MAX_VALUE);
    private final ItemSetting<List<String>> ignoredLores = new ItemSetting<>(this, "ignored-lores", Arrays.asList("&7- &cCan't be used in " + this.getItemName()));

    @ParametersAreNonnullByDefault
    protected AbstractEnchantmentMachine(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(useLevelLimit);
        addItemSetting(levelLimit);
        addItemSetting(ignoredLores);
    }

    protected boolean isEnchantmentLevelAllowed(int enchantmentLevel) {
        return !useLevelLimit.getValue() || levelLimit.getValue() >= enchantmentLevel;
    }

    protected void showEnchantmentLevelWarning(@Nonnull BlockMenu menu) {
        if (!useLevelLimit.getValue()) {
            throw new IllegalStateException("Enchantment level limit not enabled, cannot display a warning.");
        }

        String notice = ChatColors.color(SlimefunPlugin.getLocalization().getMessage("messages.above-limit-level"));
        notice = notice.replace("%level%", String.valueOf(levelLimit.getValue()));
        ItemStack progressBar = new CustomItem(Material.BARRIER, " ", notice);
        menu.replaceExistingItem(22, progressBar);
    }

    protected boolean hasIgnoredLore(@Nonnull ItemStack itemStack) {
        List<String> ignoredLore = ignoredLores.getValue();
        if (itemStack.hasItemMeta()) {
            List<String> itemLore = itemStack.getItemMeta().getLore();
            for (String lore : ignoredLore) {
                if (itemLore.contains(ChatColors.color(lore))) {
                    return true;
                }
            }
        }
        return false;
    }
}
