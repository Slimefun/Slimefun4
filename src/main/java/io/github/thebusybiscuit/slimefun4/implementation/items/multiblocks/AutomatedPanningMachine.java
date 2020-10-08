package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.scheduling.TaskQueue;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class AutomatedPanningMachine extends MultiBlockMachine {

    private final GoldPan goldPan = (GoldPan) SlimefunItems.GOLD_PAN.getItem();
    private final GoldPan netherGoldPan = (GoldPan) SlimefunItems.NETHER_GOLD_PAN.getItem();

    public AutomatedPanningMachine(Category category, SlimefunItemStack item) {
        super(category, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.OAK_TRAPDOOR), null, null, new ItemStack(Material.CAULDRON), null }, BlockFace.SELF);
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> recipes = new ArrayList<>();

        recipes.addAll(goldPan.getDisplayRecipes());
        recipes.addAll(netherGoldPan.getDisplayRecipes());

        return recipes;
    }

    @Override
    public void onInteract(Player p, Block b) {
        ItemStack input = p.getInventory().getItemInMainHand();

        if (SlimefunUtils.isItemSimilar(input, new ItemStack(Material.GRAVEL), true, false) || SlimefunUtils.isItemSimilar(input, new ItemStack(Material.SOUL_SAND), true, false)) {
            Material material = input.getType();

            if (p.getGameMode() != GameMode.CREATIVE) {
                ItemUtils.consumeItem(input, false);
            }

            ItemStack output = material == Material.GRAVEL ? goldPan.getRandomOutput() : netherGoldPan.getRandomOutput();
            TaskQueue queue = new TaskQueue();

            queue.thenRepeatEvery(20, 5, () -> b.getWorld().playEffect(b.getRelative(BlockFace.DOWN).getLocation(), Effect.STEP_SOUND, material));

            queue.thenRun(20, () -> {
                if (output.getType() != Material.AIR) {
                    Inventory outputChest = findOutputChest(b.getRelative(BlockFace.DOWN), output);

                    if (outputChest != null) {
                        outputChest.addItem(output.clone());
                    } else {
                        b.getWorld().dropItemNaturally(b.getLocation(), output.clone());
                    }

                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
                } else {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 1F, 1F);
                }
            });

            queue.execute(SlimefunPlugin.instance());
        } else {
            SlimefunPlugin.getLocalization().sendMessage(p, "machines.wrong-item", true);
        }
    }

}
