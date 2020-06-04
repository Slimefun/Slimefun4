package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.scheduling.TaskQueue;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This represents a running instance of an {@link IndustrialMiner}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see IndustrialMiner
 *
 */
class IndustrialMinerInstance implements Runnable {

    private final IndustrialMiner miner;
    private final UUID owner;

    private int fuel = 0;
    private int ores = 0;
    private boolean running = false;

    private final Block chest;
    private final Block[] pistons;

    private final Location start;
    private final Location end;
    private final int height;

    private int x;
    private int z;

    public IndustrialMinerInstance(IndustrialMiner miner, UUID owner, Block chest, Block[] pistons, Location start, Location end) {
        this.miner = miner;
        this.owner = owner;

        this.chest = chest;
        this.pistons = pistons;

        this.start = start;
        this.end = end;

        this.height = start.getBlockY();
        this.x = start.getBlockX();
        this.z = start.getBlockZ();
    }

    /**
     * This starts the {@link IndustrialMiner} at the given {@link Block}.
     * 
     * @param b
     *            The {@link Block} which marks the center of this {@link IndustrialMiner}
     */
    public void start(Block b) {
        miner.activeMiners.put(b.getLocation(), this);
        running = true;

        warmUp();
    }

    /**
     * This method stops the {@link IndustrialMiner}.
     */
    public void stop() {
        running = false;
        miner.activeMiners.remove(chest.getRelative(BlockFace.DOWN).getLocation());
    }

    /**
     * This method stops the {@link IndustrialMiner} with an error message.
     * The error message is a path to the location in Slimefun's localization files.
     * 
     * @param error
     *            The error message to send
     */
    public void stop(String error) {
        Player p = Bukkit.getPlayer(owner);

        if (p != null) {
            SlimefunPlugin.getLocal().sendMessage(p, error);
        }

        stop();
    }

    /**
     * This method starts the warm-up animation for the {@link IndustrialMiner}.
     */
    private void warmUp() {
        fuel = consumeFuel();

        if (fuel <= 0) {
            // This Miner has not enough fuel.
            stop("machines.INDUSTRIAL_MINER.no-fuel");
            return;
        }

        TaskQueue queue = new TaskQueue();

        queue.thenRun(4, () -> setPistonState(pistons[0], true));
        queue.thenRun(10, () -> setPistonState(pistons[0], false));

        queue.thenRun(8, () -> setPistonState(pistons[1], true));
        queue.thenRun(10, () -> setPistonState(pistons[1], false));

        queue.thenRun(6, () -> setPistonState(pistons[0], true));
        queue.thenRun(9, () -> setPistonState(pistons[0], false));

        queue.thenRun(4, () -> setPistonState(pistons[1], true));
        queue.thenRun(7, () -> setPistonState(pistons[1], false));

        queue.thenRun(3, () -> setPistonState(pistons[0], true));
        queue.thenRun(5, () -> setPistonState(pistons[0], false));

        queue.thenRun(2, () -> setPistonState(pistons[1], true));
        queue.thenRun(4, () -> setPistonState(pistons[1], false));

        queue.thenRun(1, () -> setPistonState(pistons[0], true));
        queue.thenRun(3, () -> setPistonState(pistons[0], false));

        queue.thenRun(1, () -> setPistonState(pistons[1], true));
        queue.thenRun(2, () -> setPistonState(pistons[1], false));

        queue.thenRun(1, this);
        queue.execute(SlimefunPlugin.instance);
    }

    @Override
    public void run() {
        if (!running) {
            return;
        }

        try {
            TaskQueue queue = new TaskQueue();

            queue.thenRun(1, () -> setPistonState(pistons[0], true));
            queue.thenRun(3, () -> setPistonState(pistons[0], false));

            queue.thenRun(1, () -> setPistonState(pistons[1], true));
            queue.thenRun(3, () -> setPistonState(pistons[1], false));

            queue.thenRun(() -> {
                Block furnace = chest.getRelative(BlockFace.DOWN);
                furnace.getWorld().playEffect(furnace.getLocation(), Effect.STEP_SOUND, Material.STONE);

                for (int y = height; y > 0; y--) {
                    Block b = start.getWorld().getBlockAt(x, y, z);

                    if (!SlimefunPlugin.getProtectionManager().hasPermission(Bukkit.getOfflinePlayer(owner), b, ProtectableAction.BREAK_BLOCK)) {
                        stop("machines.INDUSTRIAL_MINER.no-permission");
                        return;
                    }

                    if (b.getType().name().endsWith("_ORE") && push(miner.getOutcome(b.getType()))) {
                        furnace.getWorld().playEffect(furnace.getLocation(), Effect.STEP_SOUND, b.getType());
                        furnace.getWorld().playSound(furnace.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.2F, 1F);

                        b.setType(Material.AIR);
                        fuel--;
                        ores++;

                        // Repeat the same column when we hit an ore.
                        Slimefun.runSync(this, 3);
                        return;
                    }
                }

                nextColumn();
            });

            queue.execute(SlimefunPlugin.instance);
        }
        catch (Exception e) {
            Slimefun.getLogger().log(Level.SEVERE, "An Error occured while running an Industrial Miner", e);
            stop();
        }
    }

    /**
     * This advanced the {@link IndustrialMiner} to the next column
     */
    private void nextColumn() {
        if (x < end.getBlockX()) {
            x++;
        }
        else if (z < end.getBlockZ()) {
            x = start.getBlockX();
            z++;
        }
        else {
            stop();

            Player p = Bukkit.getPlayer(owner);

            if (p != null) {
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.4F, 1F);
                SlimefunPlugin.getLocal().sendMessage(p, "machines.INDUSTRIAL_MINER.finished", msg -> msg.replace("%ores%", String.valueOf(ores)));
            }

            return;
        }

        Slimefun.runSync(this, 4);
    }

    private boolean push(ItemStack outcome) {
        if (fuel < 1) {
            fuel = consumeFuel();
        }

        if (fuel > 0) {
            if (chest.getType() == Material.CHEST) {
                Inventory inv = ((Chest) chest.getState()).getBlockInventory();

                if (InvUtils.fits(inv, outcome)) {
                    inv.addItem(outcome);
                    return true;
                }
                else {
                    stop("machines.INDUSTRIAL_MINER.chest-full");
                }
            }
            else {
                // The chest has been destroyed
                stop("machines.INDUSTRIAL_MINER.destroyed");
            }
        }
        else {
            stop("machines.INDUSTRIAL_MINER.no-fuel");
        }

        return false;
    }

    /**
     * This consumes fuel from the given {@link Chest}.
     * 
     * @return The gained fuel value
     */
    private int consumeFuel() {
        if (chest.getType() == Material.CHEST) {
            Inventory inv = ((Chest) chest.getState()).getBlockInventory();

            for (int i = 0; i < inv.getSize(); i++) {
                for (MachineFuel fuelType : miner.fuelTypes) {
                    ItemStack item = inv.getContents()[i];

                    if (fuelType.test(item)) {
                        ItemUtils.consumeItem(item, false);
                        return fuelType.getTicks();
                    }
                }
            }
        }

        return 0;
    }

    private void setPistonState(Block block, boolean extended) {
        if (!running) {
            return;
        }

        try {
            // Smoke Particles around the Chest for dramatic effect
            Location particleLoc = chest.getLocation().clone().add(0, -1, 0);
            block.getWorld().spawnParticle(Particle.SMOKE_NORMAL, particleLoc, 20, 0.7, 0.7, 0.7, 0);

            if (block.getType() == Material.MOVING_PISTON) {
                // Yeah it isn't really cool when this happens
                block.getRelative(BlockFace.UP).setType(Material.AIR);
            }
            else if (block.getType() == Material.PISTON) {
                Block above = block.getRelative(BlockFace.UP);

                if (above.isEmpty() || above.getType() == Material.PISTON_HEAD) {
                    Piston piston = (Piston) block.getBlockData();

                    if (piston.getFacing() == BlockFace.UP) {
                        piston.setExtended(extended);
                        block.setBlockData(piston, false);

                        // Updating the Piston Head
                        if (extended) {
                            PistonHead head = (PistonHead) Material.PISTON_HEAD.createBlockData();
                            head.setFacing(BlockFace.UP);

                            block.getRelative(BlockFace.UP).setBlockData(head, false);
                        }
                        else {
                            block.getRelative(BlockFace.UP).setType(Material.AIR);
                        }

                        block.getWorld().playSound(block.getLocation(), extended ? Sound.BLOCK_PISTON_EXTEND : Sound.BLOCK_PISTON_CONTRACT, 0.1F, 1F);
                    }
                    else {
                        // The pistons must be facing upwards
                        stop("machines.INDUSTRIAL_MINER.piston-facing");
                    }
                }
                else {
                    // The pistons must be facing upwards
                    stop("machines.INDUSTRIAL_MINER.piston-space");
                }
            }
            else {
                // The piston has been destroyed
                stop("machines.INDUSTRIAL_MINER.destroyed");
            }
        }
        catch (Exception e) {
            Slimefun.getLogger().log(Level.SEVERE, "An Error occured while moving a Piston for an Industrial Miner", e);
            stop();
        }
    }

}
