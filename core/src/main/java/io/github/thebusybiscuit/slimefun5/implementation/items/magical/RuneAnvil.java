package io.github.thebusybiscuit.slimefun5.implementation.items.magical;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedEnchantment;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * The {@link RuneAnvil} is a GUI block which applies a {@link SlimefunItems#SOULBOUND_RUNE} or
 * {@link SlimefunItems#ENCHANTMENT_RUNE} to an item. The target item and the rune go in the input
 * slots and the result is shown as a preview in the output slot - it is only actually crafted (and
 * the inputs consumed) when the player takes the result out, like a vanilla anvil.
 *
 * @author TheBusyBiscuit
 *
 */
public class RuneAnvil extends SlimefunItem implements InventoryBlock {

    private static final int ITEM_SLOT = 10;
    private static final int RUNE_SLOT = 11;
    private static final int OUTPUT_SLOT = 16;
    private static final int ARROW_SLOT = 14;
    private static final int INFO_SLOT = 4;

    private final int[] border = { 0, 1, 2, 3, 5, 6, 7, 8, 9, 12, 13, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };

    @ParametersAreNonnullByDefault
    public RuneAnvil(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        // Build the preset directly (instead of createPreset) so we can override newInstance and add
        // a per-block "craft on take" handler. A plain title reads cleanly in the chest title bar.
        new BlockMenuPreset(getId(), "Rune Anvil") {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return flow == ItemTransportFlow.INSERT ? getInputSlots() : getOutputSlots();
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                    || (canUse(p, false) && (Slimefun.instance().isUnitTest() || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.INTERACT_BLOCK)));
            }

            @Override
            public void newInstance(BlockMenu menu, Block b) {
                menu.addMenuClickHandler(OUTPUT_SLOT, new ChestMenu.AdvancedMenuClickHandler() {

                    @Override
                    public boolean onClick(Player p, int slot, ItemStack stack, ClickAction action) {
                        return false;
                    }

                    @Override
                    public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                        // Block depositing into the output; only allow taking (empty cursor).
                        if (cursor != null && cursor.getType() != Material.AIR) {
                            return false;
                        }

                        ItemStack output = menu.getItemInSlot(OUTPUT_SLOT);

                        if (output == null || output.getType() == Material.AIR) {
                            return false;
                        }

                        // The result is only "crafted" here, when collected: re-validate + consume the inputs.
                        if (computeResult(menu.getItemInSlot(ITEM_SLOT), menu.getItemInSlot(RUNE_SLOT)) == null) {
                            menu.replaceExistingItem(OUTPUT_SLOT, null);
                            return false;
                        }

                        menu.consumeItem(ITEM_SLOT, 1);
                        menu.consumeItem(RUNE_SLOT, 1);
                        return true;
                    }
                });
            }
        };
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(INFO_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.ANVIL),
            "&5Rune Anvil",
            "",
            "&7Place an &fitem &7and a &5Soulbound &7or",
            "&5Enchantment Rune &7in the inputs,",
            "&7then take the result from the output.",
            "",
            "&7The rune is consumed when you collect."),
            ChestMenuUtils.getEmptyClickHandler());

        preset.addItem(ARROW_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.LIME_STAINED_GLASS_PANE), "&a➜"), ChestMenuUtils.getEmptyClickHandler());

        // ITEM_SLOT and RUNE_SLOT are left empty so the player (and hoppers) can insert.
        // OUTPUT_SLOT is handled per-block in newInstance (take-only + craft-on-take).
    }

    @Override
    public int[] getInputSlots() {
        return new int[] { ITEM_SLOT, RUNE_SLOT };
    }

    @Override
    public int[] getOutputSlots() {
        // No automated extraction: the result is a preview, only finalised when a player takes it.
        return new int[0];
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                BlockMenu menu = BlockStorage.getInventory(b);

                if (menu != null) {
                    updatePreview(menu);
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    /** Maintains the output preview without consuming inputs. The result is only finalised on take. */
    private void updatePreview(@Nonnull BlockMenu menu) {
        ItemStack item = menu.getItemInSlot(ITEM_SLOT);
        ItemStack rune = menu.getItemInSlot(RUNE_SLOT);
        ItemStack output = menu.getItemInSlot(OUTPUT_SLOT);

        // Keep an existing preview stable (do NOT re-roll a random enchant every tick) as long as it
        // still matches the current input item's type.
        if (output != null && output.getType() != Material.AIR && item != null && output.getType() == item.getType()
                && rune != null && (isRune(rune, SlimefunItems.SOULBOUND_RUNE, "SOULBOUND_RUNE") || isRune(rune, SlimefunItems.ENCHANTMENT_RUNE, "ENCHANTMENT_RUNE"))) {
            return;
        }

        ItemStack result = computeResult(item, rune);

        if (result == null) {
            if (output != null && output.getType() != Material.AIR) {
                menu.replaceExistingItem(OUTPUT_SLOT, null);
            }
        } else {
            menu.replaceExistingItem(OUTPUT_SLOT, result);
        }
    }

    /** Computes the result of applying the rune to the item, or null if the combination is invalid. */
    @Nullable
    private ItemStack computeResult(@Nullable ItemStack item, @Nullable ItemStack rune) {
        if (item == null || rune == null || item.getAmount() != 1) {
            return null;
        }

        if (isRune(rune, SlimefunItems.SOULBOUND_RUNE, "SOULBOUND_RUNE")) {
            if (SlimefunUtils.isSoulbound(item)) {
                return null;
            }

            ItemStack result = item.clone();
            SlimefunUtils.setSoulbound(result, true);
            return result;
        } else if (isRune(rune, SlimefunItems.ENCHANTMENT_RUNE, "ENCHANTMENT_RUNE")) {
            return enchant(item);
        }

        return null;
    }

    private boolean isRune(@Nonnull ItemStack rune, @Nonnull SlimefunItemStack expected, @Nonnull String id) {
        if (SlimefunUtils.isItemSimilar(rune, expected.item(), true)) {
            return true;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(rune);
        return sfItem != null && sfItem.getId().equals(id);
    }

    @Nullable
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
