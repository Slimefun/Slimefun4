package io.github.thebusybiscuit.slimefun5.implementation.items.multiblocks;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.InventoryCompat;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

public class MagicWorkbench extends AbstractCraftingTable {

    @ParametersAreNonnullByDefault
    public MagicWorkbench(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, null, null, new ItemStack(Material.BOOKSHELF), MaterialCompat.stack(XMaterial.CRAFTING_TABLE), new ItemStack(Material.DISPENSER) }, BlockFace.UP);
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block possibleDispener = locateDispenser(b);

        if (possibleDispener == null) {
            // How even...
            return;
        }

        BlockState state = PaperLib.getBlockState(possibleDispener, false).getState();

        if (state instanceof Dispenser) {
            // First player to interact claims ownership; this gates the redstone auto-craft later.
            Slimefun.getMultiBlockOwnership().setOwnerIfAbsent(possibleDispener.getLocation(), p.getUniqueId());

            Dispenser dispenser = (Dispenser) state;            Inventory inv = dispenser.getInventory();
            List<ItemStack[]> inputs = RecipeType.getRecipeInputList(this);

            for (ItemStack[] input : inputs) {
                if (isCraftable(inv, input)) {
                    ItemStack output = RecipeType.getRecipeOutputList(this, input).clone();
                    MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, input, output);

                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled() && SlimefunUtils.canPlayerUseItem(p, output, true)) {
                        craft(inv, possibleDispener, p, b, event.getOutput(), input);
                    }

                    return;
                }
            }

            if (InventoryCompat.isEmpty(inv)) {
                if (io.github.thebusybiscuit.slimefun5.core.guide.options.SlimefunGuideSettings.hasMachineMessagesEnabled(p)) {
                    Slimefun.getLocalization().sendMessage(p, "machines.inventory-empty", true);
                }
            } else {
                Slimefun.getLocalization().sendMessage(p, "machines.pattern-not-found", true);
            }
        }
    }

    @Override
    protected int getAutoCraftDelayTicks() {
        // Matches the manual craft animation (4 steps at 20-tick intervals, finishing at ~60 ticks).
        return 60;
    }

    @Override
    protected SoundEffect getAutoCraftSound() {
        return SoundEffect.MAGIC_WORKBENCH_FINISH_SOUND;
    }

    @ParametersAreNonnullByDefault
    private void craft(Inventory inv, Block dispenser, Player p, Block b, ItemStack output, ItemStack[] recipe) {
        Inventory fakeInv = createVirtualInventory(inv);
        Inventory outputInv = findOutputInventory(output, dispenser, inv, fakeInv);

        if (outputInv != null) {
            SlimefunItem sfItem = SlimefunItem.getByItem(output);

            if (sfItem instanceof SlimefunBackpack) {
                SlimefunBackpack backpack = (SlimefunBackpack) sfItem;                upgradeBackpack(p, inv, backpack, output);
            }

            consumeInputs(inv, recipe);

            startAnimation(p, b, inv, dispenser, output);
        } else {
            // Output has nowhere to go (dispenser full): craft anyway and eject it out of the dispenser,
            // the same way the redstone auto-craft does, so it lands in open space instead of being lost.
            consumeInputs(inv, recipe);
            ejectOutput(dispenser, output);
            SoundEffect.MAGIC_WORKBENCH_FINISH_SOUND.playAt(b);
        }
    }

    private void startAnimation(Player p, Block b, Inventory dispInv, Block dispenser, ItemStack output) {
        for (int j = 0; j < 4; j++) {
            int current = j;
            Slimefun.runSync(() -> {
                p.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                p.getWorld().playEffect(b.getLocation(), Effect.ENDER_SIGNAL, 1);

                if (current < 3) {
                    SoundEffect.MAGIC_WORKBENCH_START_ANIMATION_SOUND.playAt(b);
                } else {
                    SoundEffect.MAGIC_WORKBENCH_FINISH_SOUND.playAt(b);
                    handleCraftedItem(output, dispenser, dispInv);
                }
            }, j * 20L);
        }
    }

    private Block locateDispenser(Block b) {
        Block block = null;

        if (b.getRelative(1, 0, 0).getType() == Material.DISPENSER) {
            block = b.getRelative(1, 0, 0);
        } else if (b.getRelative(0, 0, 1).getType() == Material.DISPENSER) {
            block = b.getRelative(0, 0, 1);
        } else if (b.getRelative(-1, 0, 0).getType() == Material.DISPENSER) {
            block = b.getRelative(-1, 0, 0);
        } else if (b.getRelative(0, 0, -1).getType() == Material.DISPENSER) {
            block = b.getRelative(0, 0, -1);
        }

        return block;
    }

    @Override
    protected boolean isCraftable(Inventory inv, ItemStack[] recipe) {
        for (int j = 0; j < inv.getContents().length; j++) {
            ItemStack slot = ignoreLock(inv.getContents()[j]);

            if (!SlimefunUtils.isItemSimilar(slot, recipe[j], true, true, false) && !sameWoodMatch(slot, recipe[j])) {
                if (SlimefunItem.getByItem(recipe[j]) instanceof SlimefunBackpack) {
                    if (!SlimefunUtils.isItemSimilar(slot, recipe[j], false, true, false)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }

}

