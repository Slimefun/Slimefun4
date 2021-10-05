package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.bakedlibs.dough.scheduling.TaskQueue;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.OutputChest;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.NetherGoldPan;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

/**
 * The {@link AutomatedPanningMachine} is a {@link MultiBlockMachine} that
 * functions as a semi-automatic version of the {@link GoldPan} and {@link NetherGoldPan}.
 * 
 * @author TheBusyBiscuit
 * @author Liruxo
 * 
 * @see GoldPan
 *
 */
public class AutomatedPanningMachine extends MultiBlockMachine {

    private final GoldPan goldPan = SlimefunItems.GOLD_PAN.getItem(GoldPan.class);
    private final NetherGoldPan netherGoldPan = SlimefunItems.NETHER_GOLD_PAN.getItem(NetherGoldPan.class);

    @ParametersAreNonnullByDefault
    public AutomatedPanningMachine(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.OAK_TRAPDOOR), null, null, new ItemStack(Material.CAULDRON), null }, BlockFace.SELF);
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
                    Optional<Inventory> outputChest = OutputChest.findOutputChestFor(b.getRelative(BlockFace.DOWN), output);

                    if (outputChest.isPresent()) {
                        outputChest.get().addItem(output.clone());
                    } else {
                        b.getWorld().dropItemNaturally(b.getLocation(), output.clone());
                    }

                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
                } else {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 1F, 1F);
                }
            });

            queue.execute(Slimefun.instance());
        } else {
            Slimefun.getLocalization().sendMessage(p, "machines.wrong-item", true);
        }
    }

}
