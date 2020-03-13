package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockUseHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Composter extends SimpleSlimefunItem<BlockUseHandler> implements RecipeDisplayItem {

    private final List<ItemStack> recipes;

    public Composter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        recipes = getMachineRecipes();
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return recipes;
    }

    private static List<ItemStack> getMachineRecipes() {
        List<ItemStack> items = new LinkedList<>();

        for (Material leave : MaterialCollections.getAllLeaves()) {
            items.add(new ItemStack(leave, 8));
            items.add(new ItemStack(Material.DIRT));
        }

        for (Material sapling : MaterialCollections.getAllSaplings()) {
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

                if (p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES)) {
                    ItemStack input = e.getItem();
                    ItemStack output = getOutput(p, input);

                    if (output != null) {
                        for (int j = 1; j < 12; j++) {
                            int index = j;

                            Slimefun.runSync(() -> {
                                if (index < 11) {
                                    b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, input.getType().isBlock() ? input.getType() : Material.HAY_BLOCK);
                                }
                                else {
                                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
                                    b.getWorld().dropItemNaturally(b.getRelative(BlockFace.UP).getLocation(), output);
                                }
                            }, j * 30L);
                        }
                    }
                    else {
                        SlimefunPlugin.getLocal().sendMessage(p, "machines.wrong-item", true);
                    }
                }
            }
        };
    }

    private ItemStack getOutput(Player p, ItemStack input) {
        for (int i = 0; i < recipes.size(); i += 2) {
            ItemStack convert = recipes.get(i);

            if (convert != null && SlimefunManager.isItemSimilar(input, convert, true)) {
                ItemStack removing = input.clone();
                removing.setAmount(convert.getAmount());
                p.getInventory().removeItem(removing);

                return recipes.get(i + 1);
            }
        }

        return null;
    }

}
