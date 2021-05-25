package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.scheduling.TaskQueue;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Composter extends SimpleSlimefunItem<BlockUseHandler> implements RecipeDisplayItem {

    private final List<ItemStack> recipes;

    @ParametersAreNonnullByDefault
    public Composter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        recipes = getMachineRecipes();
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return recipes;
    }

    private List<ItemStack> getMachineRecipes() {
        List<ItemStack> items = new LinkedList<>();

        for (Material leave : Tag.LEAVES.getValues()) {
            items.add(new ItemStack(leave, 8));
            items.add(new ItemStack(Material.DIRT));
        }

        for (Material sapling : Tag.SAPLINGS.getValues()) {
            items.add(new ItemStack(sapling, 8));
            items.add(new ItemStack(Material.DIRT));
        }

        items.add(new ItemStack(Material.STONE, 4));
        items.add(new ItemStack(Material.NETHERRACK));

        items.add(new ItemStack(Material.SAND, 2));
        items.add(new ItemStack(Material.SOUL_SAND));

        items.add(new ItemStack(Material.WHEAT, 4));
        items.add(new ItemStack(Material.NETHER_WART));

        return items;
    }

    @Override
    public BlockUseHandler getItemHandler() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();

            if (block.isPresent()) {
                e.cancel();

                Player p = e.getPlayer();
                Block b = block.get();

                if (p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.INTERACT_BLOCK)) {
                    ItemStack input = e.getItem();
                    ItemStack output = getOutput(p, input);

                    if (output != null) {
                        TaskQueue tasks = new TaskQueue();

                        tasks.thenRepeatEvery(30, 10, () -> {
                            Material material = input.getType().isBlock() ? input.getType() : Material.HAY_BLOCK;
                            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, material);
                        });

                        tasks.thenRun(20, () -> {
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
                            pushItem(b, output.clone());
                        });

                        tasks.execute(SlimefunPlugin.instance());
                    } else {
                        SlimefunPlugin.getLocalization().sendMessage(p, "machines.wrong-item", true);
                    }
                }
            }
        };
    }

    private void pushItem(Block b, ItemStack output) {
        Optional<Inventory> outputChest = findOutputChest(b, output);

        if (outputChest.isPresent()) {
            outputChest.get().addItem(output);
        } else {
            Location loc = b.getRelative(BlockFace.UP).getLocation();
            b.getWorld().dropItemNaturally(loc, output);
        }
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private Optional<Inventory> findOutputChest(Block b, ItemStack output) {
        return OutputChest.findOutputChestFor(b, output);
    }

    private ItemStack getOutput(Player p, ItemStack input) {
        for (int i = 0; i < recipes.size(); i += 2) {
            ItemStack convert = recipes.get(i);

            if (convert != null && SlimefunUtils.isItemSimilar(input, convert, true)) {
                ItemStack removing = input.clone();
                removing.setAmount(convert.getAmount());
                p.getInventory().removeItem(removing);

                return recipes.get(i + 1);
            }
        }

        return null;
    }

}
