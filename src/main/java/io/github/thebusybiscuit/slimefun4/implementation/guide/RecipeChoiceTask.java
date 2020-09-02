package io.github.thebusybiscuit.slimefun4.implementation.guide;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

import io.github.thebusybiscuit.cscorelib2.collections.LoopIterator;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

/**
 * A {@link RecipeChoiceTask} is an asynchronously repeating task that cycles
 * through the different variants of {@link Material} that a {@link MaterialChoice} or {@link Tag} can represent.
 * 
 * It is used in the {@link ChestSlimefunGuide} for any {@link ItemStack} from Minecraft
 * that accepts more than one {@link Material} in its {@link Recipe}.
 * 
 * @author TheBusyBiscuit
 *
 */
class RecipeChoiceTask implements Runnable {

    private static final int UPDATE_INTERVAL = 14;

    private Inventory inventory;
    private int id;
    private final Map<Integer, LoopIterator<Material>> iterators = new HashMap<>();

    /**
     * This will start this task for the given {@link Inventory}.
     * 
     * @param inv
     *            The {@link Inventory} to start this task for
     */
    public void start(@Nonnull Inventory inv) {
        Validate.notNull(inv, "Inventory must not be null");
        inventory = inv;
        id = Bukkit.getScheduler().runTaskTimerAsynchronously(SlimefunPlugin.instance(), this, 0, UPDATE_INTERVAL).getTaskId();
    }

    public void add(int slot, @Nonnull MaterialChoice choice) {
        Validate.notNull(choice, "Cannot add a null RecipeChoice");
        iterators.put(slot, new LoopIterator<>(choice.getChoices()));
    }

    public void add(int slot, @Nonnull Tag<Material> tag) {
        Validate.notNull(tag, "Cannot add a null Tag");
        iterators.put(slot, new LoopIterator<>(tag.getValues()));
    }

    /**
     * This method checks if there are any slots that need to be updated.
     * 
     * @return Whether this task has nothing to do
     */
    public boolean isEmpty() {
        return iterators.isEmpty();
    }

    @Override
    public void run() {
        if (inventory.getViewers().isEmpty()) {
            Bukkit.getScheduler().cancelTask(id);
            return;
        }

        for (Map.Entry<Integer, LoopIterator<Material>> entry : iterators.entrySet()) {
            inventory.setItem(entry.getKey(), new ItemStack(entry.getValue().next()));
        }
    }

}
