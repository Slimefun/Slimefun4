package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

public class GrindStone extends MultiBlockMachine {

    @ParametersAreNonnullByDefault
    public GrindStone(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.OAK_FENCE), null, null, CustomItemStack.create(Material.DISPENSER, "Dispenser (Facing up)"), null }, BlockFace.SELF);
    }

    @Override
    protected void registerDefaultRecipes(@Nonnull List<ItemStack> recipes) {
        recipes.add(new ItemStack(Material.BLAZE_ROD));
        recipes.add(new ItemStack(Material.BLAZE_POWDER, 4));

        recipes.add(new ItemStack(Material.BONE));
        recipes.add(new ItemStack(Material.BONE_MEAL, 4));

        recipes.add(new ItemStack(Material.BONE_BLOCK));
        recipes.add(new ItemStack(Material.BONE_MEAL, 9));

        recipes.add(new ItemStack(Material.COBBLESTONE));
        recipes.add(new ItemStack(Material.GRAVEL));

        recipes.add(new ItemStack(Material.ANDESITE));
        recipes.add(new ItemStack(Material.GRAVEL));

        recipes.add(new ItemStack(Material.BLACKSTONE));
        recipes.add(new ItemStack(Material.GRAVEL));

        recipes.add(new ItemStack(Material.DIORITE));
        recipes.add(new ItemStack(Material.GRAVEL));

        recipes.add(new ItemStack(Material.GRANITE));
        recipes.add(new ItemStack(Material.GRAVEL));

        recipes.add(new ItemStack(Material.DIRT));
        recipes.add(SlimefunItems.STONE_CHUNK);

        recipes.add(new ItemStack(Material.SANDSTONE));
        recipes.add(new ItemStack(Material.SAND, 4));

        recipes.add(new ItemStack(Material.RED_SANDSTONE));
        recipes.add(new ItemStack(Material.RED_SAND, 4));

        recipes.add(new ItemStack(Material.PRISMARINE_BRICKS));
        recipes.add(new ItemStack(Material.PRISMARINE, 2));

        recipes.add(new ItemStack(Material.PRISMARINE));
        recipes.add(new ItemStack(Material.PRISMARINE_SHARD, 4));

        recipes.add(new ItemStack(Material.NETHER_WART_BLOCK));
        recipes.add(new ItemStack(Material.NETHER_WART, 9));

        recipes.add(new ItemStack(Material.QUARTZ_BLOCK));
        recipes.add(new ItemStack(Material.QUARTZ, 4));

        recipes.add(new ItemStack(Material.BASALT, 2));
        recipes.add(new ItemStack(Material.BLACKSTONE));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            recipes.add(new ItemStack(Material.AMETHYST_BLOCK));
            recipes.add(new ItemStack(Material.AMETHYST_SHARD, 4));

            recipes.add(new ItemStack(Material.COBBLED_DEEPSLATE));
            recipes.add(new ItemStack(Material.GRAVEL));
        }

        recipes.add(SlimefunItems.MAGIC_LUMP_2);
        recipes.add(new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_1, 4));

        recipes.add(SlimefunItems.MAGIC_LUMP_3);
        recipes.add(new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_2, 4));

        recipes.add(SlimefunItems.ENDER_LUMP_2);
        recipes.add(new SlimefunItemStack(SlimefunItems.ENDER_LUMP_1, 4));

        recipes.add(SlimefunItems.ENDER_LUMP_3);
        recipes.add(new SlimefunItemStack(SlimefunItems.ENDER_LUMP_2, 4));

        recipes.add(new ItemStack(Material.DIAMOND));
        recipes.add(new SlimefunItemStack(SlimefunItems.CARBON, 4));
    }

    @Override
    public @Nonnull List<ItemStack> getDisplayRecipes() {
        return recipes.stream().map(items -> items[0]).collect(Collectors.toList());
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block possibleDispenser = b.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(possibleDispenser, false).getState();

        if (state instanceof Dispenser dispenser) {
            Inventory inv = dispenser.getInventory();

            for (ItemStack current : inv.getContents()) {
                for (ItemStack convert : RecipeType.getRecipeInputs(this)) {
                    if (convert != null && SlimefunUtils.isItemSimilar(current, convert, true)) {
                        ItemStack output = RecipeType.getRecipeOutput(this, convert);
                        Inventory outputInv = findOutputInventory(output, possibleDispenser, inv);
                        MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, current, output);

                        Bukkit.getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            return;
                        }

                        if (outputInv != null) {
                            ItemStack removing = current.clone();
                            removing.setAmount(1);
                            inv.removeItem(removing);
                            outputInv.addItem(event.getOutput());
                            SoundEffect.GRIND_STONE_INTERACT_SOUND.playAt(b);
                        } else {
                            Slimefun.getLocalization().sendMessage(p, "machines.full-inventory", true);
                        }
                        return;
                    }
                }
            }

            Slimefun.getLocalization().sendMessage(p, "machines.unknown-material", true);
        }
    }
}
