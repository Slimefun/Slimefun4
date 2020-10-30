package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Crucible extends SimpleSlimefunItem<BlockUseHandler> implements RecipeDisplayItem {

    private final List<ItemStack> recipes;

    public Crucible(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        recipes = getMachineRecipes();
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return recipes;
    }

    private List<ItemStack> getMachineRecipes() {
        List<ItemStack> items = new LinkedList<>();

        items.add(new ItemStack(Material.COBBLESTONE, 16));
        items.add(new ItemStack(Material.LAVA_BUCKET));

        items.add(new ItemStack(Material.NETHERRACK, 16));
        items.add(new ItemStack(Material.LAVA_BUCKET));

        items.add(new ItemStack(Material.STONE, 12));
        items.add(new ItemStack(Material.LAVA_BUCKET));

        items.add(new ItemStack(Material.OBSIDIAN, 1));
        items.add(new ItemStack(Material.LAVA_BUCKET));

        items.add(new ItemStack(Material.TERRACOTTA, 12));
        items.add(new ItemStack(Material.LAVA_BUCKET));

        for (Material leave : Tag.LEAVES.getValues()) {
            items.add(new ItemStack(leave, 16));
            items.add(new ItemStack(Material.WATER_BUCKET));
        }

        for (Material sapling : SlimefunTag.TERRACOTTA.getValues()) {
            items.add(new ItemStack(sapling, 12));
            items.add(new ItemStack(Material.LAVA_BUCKET));
        }

        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            items.add(new ItemStack(Material.BLACKSTONE, 8));
            items.add(new ItemStack(Material.LAVA_BUCKET));

            items.add(new ItemStack(Material.BASALT, 12));
            items.add(new ItemStack(Material.LAVA_BUCKET));
        }

        return items;
    }

    @Override
    public BlockUseHandler getItemHandler() {
        return e -> {
            Optional<Block> optional = e.getClickedBlock();

            if (optional.isPresent()) {
                e.cancel();

                Player p = e.getPlayer();
                Block b = optional.get();

                if (p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES)) {
                    ItemStack input = e.getItem();
                    Block block = b.getRelative(BlockFace.UP);

                    if (craft(p, input)) {
                        boolean water = Tag.LEAVES.isTagged(input.getType());
                        generateLiquid(block, water);
                    } else {
                        SlimefunPlugin.getLocalization().sendMessage(p, "machines.wrong-item", true);
                    }
                }
            }
        };
    }

    @ParametersAreNonnullByDefault
    private boolean craft(Player p, ItemStack input) {
        for (int i = 0; i < recipes.size(); i += 2) {
            ItemStack convert = recipes.get(i);

            if (SlimefunUtils.isItemSimilar(input, convert, true)) {
                ItemStack removing = input.clone();
                removing.setAmount(convert.getAmount());
                p.getInventory().removeItem(removing);

                return true;
            }
        }

        return false;
    }

    private void generateLiquid(@Nonnull Block block, boolean water) {
        if (block.getType() == (water ? Material.WATER : Material.LAVA)) {
            addLiquidLevel(block, water);
        } else if (block.getType() == (water ? Material.LAVA : Material.WATER)) {
            int level = ((Levelled) block.getBlockData()).getLevel();
            block.setType(level == 0 || level == 8 ? Material.OBSIDIAN : Material.STONE);
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1F, 1F);
        } else {
            SlimefunPlugin.runSync(() -> placeLiquid(block, water), 50L);
        }
    }

    private void addLiquidLevel(@Nonnull Block block, boolean water) {
        int level = ((Levelled) block.getBlockData()).getLevel();

        if (level > 7) {
            level -= 8;
        }

        if (level == 0) {
            block.getWorld().playSound(block.getLocation(), water ? Sound.ENTITY_PLAYER_SPLASH : Sound.BLOCK_LAVA_POP, 1F, 1F);
        } else {
            int finalLevel = 7 - level;
            SlimefunPlugin.runSync(() -> runPostTask(block, water ? Sound.ENTITY_PLAYER_SPLASH : Sound.BLOCK_LAVA_POP, finalLevel), 50L);
        }
    }

    private void placeLiquid(@Nonnull Block block, boolean water) {
        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR || block.getType() == Material.VOID_AIR) {
            block.setType(water ? Material.WATER : Material.LAVA);
        } else {
            if (water && block.getBlockData() instanceof Waterlogged) {
                Waterlogged wl = (Waterlogged) block.getBlockData();
                wl.setWaterlogged(true);
                block.setBlockData(wl, false);
                block.getWorld().playSound(block.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);
                return;
            }

            if (BlockStorage.hasBlockInfo(block)) {
                BlockStorage.clearBlockInfo(block);
            }
        }

        runPostTask(block, water ? Sound.ENTITY_PLAYER_SPLASH : Sound.BLOCK_LAVA_POP, 1);
    }

    @ParametersAreNonnullByDefault
    private void runPostTask(Block block, Sound sound, int times) {
        if (!(block.getBlockData() instanceof Levelled)) {
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_METAL_BREAK, 1F, 1F);
            return;
        }

        block.getWorld().playSound(block.getLocation(), sound, 1F, 1F);
        int level = 8 - times;
        Levelled le = (Levelled) block.getBlockData();
        le.setLevel(level);
        block.setBlockData(le, false);

        if (times < 8) {
            SlimefunPlugin.runSync(() -> runPostTask(block, sound, times + 1), 50L);
        } else {
            block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
        }
    }

}
