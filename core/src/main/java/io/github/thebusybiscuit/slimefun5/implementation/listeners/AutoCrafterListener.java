package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.HandCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import io.github.thebusybiscuit.slimefun5.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.autocrafters.AbstractAutoCrafter;
import io.github.thebusybiscuit.slimefun5.implementation.items.autocrafters.EnhancedAutoCrafter;
import io.github.thebusybiscuit.slimefun5.implementation.items.autocrafters.VanillaAutoCrafter;
import io.github.thebusybiscuit.slimefun5.implementation.items.electric.gadgets.Multimeter;

/**
 * This {@link Listener} is responsible for providing interactions to the auto crafters.
 * See Issue #2896 with the {@link EnhancedAutoCrafter}, any {@link SlimefunItem} which
 * overrides the right click functonality would be ignored.
 * This {@link Listener} resolves that issue.
 *
 * @author TheBusyBiscuit
 * @author LilBC
 * 
 * @see VanillaAutoCrafter
 * @see EnhancedAutoCrafter
 */
public class AutoCrafterListener implements Listener {

    @ParametersAreNonnullByDefault
    public AutoCrafterListener(Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerRightClickEvent e) {
        Optional<Block> clickedBlock = e.getClickedBlock();

        // We want to make sure we used the main hand, the interaction was not cancelled and a Block was clicked.
        if (HandCompat.getHand(e) == EquipmentSlot.HAND && e.useBlock() != Result.DENY && clickedBlock.isPresent()) {
            Optional<SlimefunItem> slimefunBlock = e.getSlimefunBlock();

            // Check if the clicked Block is a Slimefun block.
            if (!slimefunBlock.isPresent()) {
                return;
            }

            SlimefunItem block = slimefunBlock.get();

            // Fixes #2957
            if (block instanceof AbstractAutoCrafter && ((AbstractAutoCrafter) block).canUse(e.getPlayer(), true)) {
                AbstractAutoCrafter crafter = (AbstractAutoCrafter) block;                Optional<SlimefunItem> slimefunItem = e.getSlimefunItem();

                if (!e.getPlayer().isSneaking() && slimefunItem.isPresent() && slimefunItem.get() instanceof Multimeter) {
                    // Allow Multimeters to pass through and do their job
                    return;
                }

                // Prevent blocks from being placed, food from being eaten, etc...
                e.cancel();

                // Check for the "doLimitedCrafting" gamerule when using a Vanilla Auto-Crafter
                if (block instanceof VanillaAutoCrafter) {
                    // Check if the recipe of the item is disabled.
                    if (isLimitedCrafting(e.getPlayer().getWorld()) && !hasUnlockedRecipe(e.getPlayer(), e.getItem())) {
                        Slimefun.getLocalization().sendMessage(e.getPlayer(), "messages.auto-crafting.recipe-unavailable");
                        return;
                    }
                }

                // Fixes 2896 - Forward the interaction before items get handled.
                try {
                    crafter.onRightClick(clickedBlock.get(), e.getPlayer());
                } catch (Exception | LinkageError x) {
                    crafter.error("Something went wrong while right-clicking an Auto-Crafter", x);
                }
            }
        }
    }

    /**
     * Reads the {@code doLimitedCrafting} gamerule without ever throwing. The typed
     * {@code GameRule.DO_LIMITED_CRAFTING} accessor (1.13+) is resolved reflectively so this class carries
     * no hard reference to {@code org.bukkit.GameRule} (absent on 1.8-1.12). The deprecated
     * {@code getGameRuleValue(String)} overload is the legacy fallback, but on some newer servers (e.g.
     * 26.2/purpur) it throws {@link IllegalArgumentException} for an "unknown gamerule" — so the whole
     * lookup is guarded and defaults to {@code false}, since a gamerule read must never crash the interaction.
     */
    private boolean isLimitedCrafting(@Nonnull World world) {
        try {
            Class<?> gameRuleClass = Class.forName("org.bukkit.GameRule");
            Object rule = gameRuleClass.getField("DO_LIMITED_CRAFTING").get(null);
            Object value = ReflectionCompat.invoke(world, "getGameRuleValue", rule);

            if (value instanceof Boolean) {
                return (Boolean) value;
            }
        } catch (Throwable ignored) {
            // GameRule type/field absent (1.8-1.12) — fall back to the legacy String overload below.
        }

        try {
            return Boolean.parseBoolean(world.getGameRuleValue("doLimitedCrafting"));
        } catch (Throwable ignored) {
            // Unknown-gamerule or removed overload on newer servers — treat as disabled.
            return false;
        }
    }

    @ParametersAreNonnullByDefault
    private boolean hasUnlockedRecipe(Player p, ItemStack item) {
        for (Recipe recipe : Slimefun.getMinecraftRecipeService().getRecipesFor(item)) {
            // recipe is a real org.bukkit Recipe; its key (Keyed, 1.12+) and hasDiscoveredRecipe (1.12+)
            // are reached reflectively so we never reference org.bukkit.Keyed (absent on 1.8-1.11).
            Object key = ReflectionCompat.invoke(recipe, "getKey");

            if (key != null && !Boolean.TRUE.equals(ReflectionCompat.invoke(p, "hasDiscoveredRecipe", key))) {
                return false;
            }
        }

        return true;
    }
}

