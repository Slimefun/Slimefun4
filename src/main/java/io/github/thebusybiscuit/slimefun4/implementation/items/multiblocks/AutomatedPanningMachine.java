package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.bakedlibs.dough.scheduling.TaskQueue;
import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.OutputChest;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.NetherGoldPan;

/**
 * The {@link AutomatedPanningMachine} is a {@link MultiBlockMachine} that
 * functions as a semi-automatic version of the {@link GoldPan} and {@link NetherGoldPan}.
 * 
 * @author TheBusyBiscuit
 * @author Liruxo
 * @author svr333
 * @author JustAHuman
 * 
 * @see GoldPan
 */
public class AutomatedPanningMachine extends MultiBlockMachine {

    private final GoldPan goldPan = SlimefunItems.GOLD_PAN.getItem(GoldPan.class);
    private final NetherGoldPan netherGoldPan = SlimefunItems.NETHER_GOLD_PAN.getItem(NetherGoldPan.class);

    @ParametersAreNonnullByDefault
    public AutomatedPanningMachine(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.OAK_TRAPDOOR), null, null, new ItemStack(Material.CAULDRON), null }, BlockFace.SELF);
    }

    @Override
    public @Nonnull List<ItemStack> getDisplayRecipes() {
        List<ItemStack> recipes = new ArrayList<>();

        recipes.addAll(goldPan.getDisplayRecipes());
        recipes.addAll(netherGoldPan.getDisplayRecipes());

        return recipes;
    }

    @Override
    public void onInteract(Player p, Block b) {
        ItemStack input = p.getInventory().getItemInMainHand();
        Material material = input.getType();
        ItemStack output;

        if (goldPan.isValidInputMaterial(material)) {
            output = goldPan.getRandomOutput();
        } else if (netherGoldPan.isValidInputMaterial(material)) {
            output = netherGoldPan.getRandomOutput();
        } else {
            Slimefun.getLocalization().sendMessage(p, "machines.wrong-item", true);
            return;
        }

        MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, input, output);

        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        ItemStack finalOutput = event.getOutput();
        if (p.getGameMode() != GameMode.CREATIVE) {
            ItemUtils.consumeItem(input, false);
        }

        TaskQueue queue = new TaskQueue();

        queue.thenRepeatEvery(20, 5, () -> b.getWorld().playEffect(b.getRelative(BlockFace.DOWN).getLocation(), Effect.STEP_SOUND, material));
        queue.thenRun(20, () -> {
            if (finalOutput.getType() != Material.AIR) {
                Optional<Inventory> outputChest = OutputChest.findOutputChestFor(b.getRelative(BlockFace.DOWN), output);

                if (outputChest.isPresent()) {
                    outputChest.get().addItem(finalOutput.clone());
                } else {
                    b.getWorld().dropItemNaturally(b.getLocation(), finalOutput.clone());
                }

                SoundEffect.AUTOMATED_PANNING_MACHINE_SUCCESS_SOUND.playAt(b);
            } else {
                SoundEffect.AUTOMATED_PANNING_MACHINE_FAIL_SOUND.playAt(b);
            }
        });

        queue.execute(Slimefun.instance());
    }
}
