package io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.LimitedUseItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This {@link SlimefunItem} casts a {@link LightningStrike} where you are pointing.
 * Unlike the other Staves, it has a limited amount of uses.
 *
 * @author Linox
 * @author Walshy
 * @author TheBusyBiscuit
 */
public class StormStaff extends LimitedUseItem {

    public static final int MAX_USES = 8;

    private static final NamespacedKey usageKey = new NamespacedKey(Slimefun.instance(), "stormstaff_usage");
    private final ItemSetting<Boolean> allowNonPVPUse = new ItemSetting<>(this, "allow-non-pvp-use", false);

    // This is a Map<LightningStrikeUuid, PlayerUuid> to keep track of the caster/lightning strike relationship for listeners
    private static final Map<UUID, UUID> stormStaffLightnings = new HashMap<>();

    @ParametersAreNonnullByDefault
    public StormStaff(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(group, item, recipeType, recipe);

        setMaxUseCount(MAX_USES);
        addItemSetting(allowNonPVPUse);
    }

    @Override
    protected @Nonnull NamespacedKey getStorageKey() {
        return usageKey;
    }

    @Override
    public @Nonnull ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            ItemStack item = e.getItem();

            if (p.getFoodLevel() >= 4 || p.getGameMode() == GameMode.CREATIVE) {
                // Get a target block with max. 30 blocks of distance
                Location loc = p.getTargetBlock(null, 30).getLocation();

                if (loc.getWorld() != null && loc.getChunk().isLoaded()) {
                    if ((loc.getWorld().getPVP() || allowNonPVPUse.getValue()) && Slimefun.getProtectionManager().hasPermission(p, loc, Interaction.ATTACK_PLAYER)) {
                        e.cancel();
                        useItem(p, item, loc);
                    } else {
                        Slimefun.getLocalization().sendMessage(p, "messages.no-pvp", true);
                    }
                }
            } else {
                Slimefun.getLocalization().sendMessage(p, "messages.hungry", true);
            }
        };
    }

    @ParametersAreNonnullByDefault
    private void useItem(Player p, ItemStack item, Location loc) {
        World world = loc.getWorld();
        if (world != null) {
            // Store the strike and caster so StormStaffLightningListener can take care of possible damage events
            LightningStrike lightningStrike = world.strikeLightning(loc);
            stormStaffLightnings.put(lightningStrike.getUniqueId(), p.getUniqueId());
        }

        if (item.getType() == Material.SHEARS) {
            return;
        }

        if (p.getGameMode() != GameMode.CREATIVE) {
            FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 4);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                p.setFoodLevel(event.getFoodLevel());
            }
        }

        damageItem(p, item);
    }

    public static @Nonnull Map<UUID, UUID> getStormStaffLightnings() {
        return stormStaffLightnings;
    }

}
