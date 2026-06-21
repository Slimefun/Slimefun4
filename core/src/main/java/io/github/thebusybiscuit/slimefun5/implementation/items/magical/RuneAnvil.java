package io.github.thebusybiscuit.slimefun5.implementation.items.magical;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedEnchantment;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

/**
 * The {@link RuneAnvil} is a GUI block which applies a {@link SlimefunItems#SOULBOUND_RUNE} or
 * {@link SlimefunItems#ENCHANTMENT_RUNE} to an item. The target item and the rune are placed in the
 * input slots and the transformed item appears in the output, consuming both inputs.
 *
 * @author TheBusyBiscuit
 *
 */
public class RuneAnvil extends SlimefunItem implements InventoryBlock {

    private static final int ITEM_SLOT = 10;
    private static final int RUNE_SLOT = 12;
    private static final int OUTPUT_SLOT = 16;

    private final int[] border = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };

    @ParametersAreNonnullByDefault
    public RuneAnvil(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        createPreset(this, this::constructMenu);
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        // The two input slots (ITEM_SLOT, RUNE_SLOT) are left empty so the player (and hoppers/cargo)
        // can place items into them. The output slot uses the default output handler: items can be
        // taken out but not inserted. Placeholder textures/handlers on these slots would block them.
        preset.addMenuClickHandler(OUTPUT_SLOT, ChestMenuUtils.getDefaultOutputHandler());
    }

    @Override
    public int[] getInputSlots() {
        return new int[] { ITEM_SLOT, RUNE_SLOT };
    }

    @Override
    public int[] getOutputSlots() {
        return new int[] { OUTPUT_SLOT };
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                BlockMenu menu = BlockStorage.getInventory(b);

                if (menu != null) {
                    process(menu);
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    private void process(@Nonnull BlockMenu menu) {
        ItemStack output = menu.getItemInSlot(OUTPUT_SLOT);

        // Wait until the previous result has been collected.
        if (output != null && output.getType() != Material.AIR) {
            return;
        }

        ItemStack item = menu.getItemInSlot(ITEM_SLOT);
        ItemStack rune = menu.getItemInSlot(RUNE_SLOT);

        if (item == null || rune == null || item.getAmount() != 1) {
            return;
        }

        ItemStack result;

        if (isRune(rune, SlimefunItems.SOULBOUND_RUNE, "SOULBOUND_RUNE")) {
            if (SlimefunUtils.isSoulbound(item)) {
                return;
            }

            result = item.clone();
            SlimefunUtils.setSoulbound(result, true);
        } else if (isRune(rune, SlimefunItems.ENCHANTMENT_RUNE, "ENCHANTMENT_RUNE")) {
            result = enchant(item);

            if (result == null) {
                return;
            }
        } else {
            // Unknown rune - do nothing.
            return;
        }

        menu.replaceExistingItem(OUTPUT_SLOT, result);
        menu.consumeItem(ITEM_SLOT, 1);
        menu.consumeItem(RUNE_SLOT, 1);
    }

    private boolean isRune(@Nonnull ItemStack rune, @Nonnull SlimefunItemStack expected, @Nonnull String id) {
        if (SlimefunUtils.isItemSimilar(rune, expected.item(), true)) {
            return true;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(rune);
        return sfItem != null && sfItem.getId().equals(id);
    }

    @javax.annotation.Nullable
    private ItemStack enchant(@Nonnull ItemStack item) {
        Material type = item.getType();

        if (MaterialCompat.isLegacy(type) || !MaterialCompat.isItem(type)) {
            return null;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        // Fixes #2878 - Respect enchantability config setting.
        if (sfItem != null && !sfItem.isEnchantable()) {
            return null;
        }

        List<Enchantment> enchantments = new ArrayList<>();

        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.equals(VersionedEnchantment.BINDING_CURSE) || enchantment.equals(VersionedEnchantment.VANISHING_CURSE)) {
                continue;
            }

            try {
                if (enchantment.canEnchantItem(new ItemStack(type))) {
                    enchantments.add(enchantment);
                }
            } catch (Exception | LinkageError ex) {
                // Legacy NMS (1.8) throws for some material/enchantment combinations - skip them.
            }
        }

        removeIllegalEnchantments(item, enchantments);

        if (enchantments.isEmpty()) {
            return null;
        }

        Enchantment enchantment = enchantments.get(ThreadLocalRandom.current().nextInt(enchantments.size()));
        int level = getRandomLevel(enchantment);

        ItemStack result = item.clone();
        result.addEnchantment(enchantment, level);
        return result;
    }

    private void removeIllegalEnchantments(@Nonnull ItemStack target, @Nonnull List<Enchantment> potentialEnchantments) {
        for (Enchantment enchantment : target.getEnchantments().keySet()) {
            Iterator<Enchantment> iterator = potentialEnchantments.iterator();

            while (iterator.hasNext()) {
                Enchantment possibleEnchantment = iterator.next();

                if (possibleEnchantment.equals(enchantment) || possibleEnchantment.conflictsWith(enchantment)) {
                    iterator.remove();
                }
            }
        }
    }

    private int getRandomLevel(@Nonnull Enchantment enchantment) {
        int level = 1;

        if (enchantment.getMaxLevel() != 1) {
            level = ThreadLocalRandom.current().nextInt(enchantment.getMaxLevel()) + 1;
        }

        return level;
    }

}
